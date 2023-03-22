package com.alibaba.i18n.category.union.service.lock;


import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;
import com.taobao.tair.ResultCode;
import com.taobao.tair.TairManager;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.UUID;

/**
 *
 * 用tair实现分布式锁设计,提供锁的标准接口
 * @Date 22/03/2023
 * @Version 1.0
 */
@Slf4j
public class DistributionLock implements Lock {

    private final static int DEFAULT_RETRY_TIMES = 3;
    private final static int DEFAULT_NAME_SPACE  = 111;
    private final static int DEFAULT_EXPIRE_TIME = 5;

    @Resource
    private TairManager tairManager;

    /**
     * 当前可能会
     */
    private ThreadLocal<String> currentThreadContex = new InheritableThreadLocal<>();

    @Override
    public boolean unlock(String lockKey) {
        try {
            Result<DataEntry> result = tairManager.get(DEFAULT_NAME_SPACE, lockKey);
            if (!result.isSuccess() || result.getValue() == null || result.getValue().getValue() == null) {
                // 弱依赖
                return true;
            }

            if (result.getValue() != null && result.getValue().getValue() != null) {
                String currentThreadValue = currentThreadContex.get();
                if (currentThreadValue.equals(result.getValue().getValue())) {
                    ResultCode resultCode = tairManager.delete(DEFAULT_NAME_SPACE, lockKey);
                    if (!resultCode.isSuccess()) {
                        log.error("ERROR#unlock#errCode={}errMsg={}", resultCode.getCode(), resultCode.getMessage());
                        return false;
                    }
                    // 解锁成功清除
                    currentThreadContex.remove();
                    return true;
                } else {
                    // 值不同非当前线程
                    return false;
                }
            }

        } catch (Exception ex) {
            log.error("unlock failed#key={}", lockKey, ex);
        }
        return true;
    }


    /**
     * 加锁
     * @param lockKey
     * @return
     */
    @Override
    public boolean tryLock(String lockKey) {
        try {
            int times = 0;
            while (times++ < DEFAULT_RETRY_TIMES) {
                Result<DataEntry> result = tairManager.get(DEFAULT_NAME_SPACE, lockKey);
                if (!result.isSuccess()) {
                    // 弱依赖
                    return true;
                }
                // 未被锁住
                if (result.getValue() == null || result.getValue().getValue() == null) {
                    String value = genThreadLockValue();
                    // 加锁
                    ResultCode resultCode = tairManager.put(DEFAULT_NAME_SPACE, lockKey, value, DEFAULT_EXPIRE_TIME);
                    if (!resultCode.isSuccess()) {
                        // 弱依赖未成功也返回true
                        log.error("ERROR#try lock#errCode={}errMsg={}", resultCode.getCode(), resultCode.getMessage());
                        return true;
                    }
                    currentThreadContex.set(value);
                    return true;
                }
                if (result.isSuccess() && result.getValue() != null && result.getValue().getValue() != null) {
                    return false;
                }
            }
        } catch (Exception ex) {
            log.error("Try lock error, key={}", lockKey, ex);
        }
        return true;
    }


    /**
     * 生成lock value
     * @return
     */
    private String genThreadLockValue() {
        return Thread.currentThread().getName() + "_" + UUID.randomUUID();
    }

}
