class LRUCache {

    private final int capacity;

    private Map<Integer, Integer> map = new ConcurrentHashMap<>();
    private Deque<Integer> queue = new LinkedList<Integer>();

    public LRUCache(int capacity) {
        this.capacity = capacity;
    }
    
    public int get(int key) {
        Integer value = map.get(key);
        if (value != null) {
            this.queue.remove((Integer)key);
            this.queue.addLast(key);
            return value;
        }
        return -1;
    }
    
    public void put(int key, int value) {
        if (map.get(key) != null) {
            this.queue.remove((Integer)key);
            this.queue.addLast(key);
            map.put(key, value);
            return;
        }

        if (queue.size() >= capacity) {
            Integer lruKey = this.queue.pollFirst();
            map.remove(lruKey);
        }
        this.queue.addLast(key);
        map.put(key, value);
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */