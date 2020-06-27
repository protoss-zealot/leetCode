

/**
* 暴力解法，时间复杂度O(n*n)
*/
class Solution {
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.equals("")) {
            return 0;
        }
        Set<Character> subStr = new HashSet<>();
        int maxLength = 1;
        int tempLength = 1;
        for (int i=0;i<s.length()-1;i++) {
            subStr.add(s.charAt(i));
            for (int j=i+1;j<s.length(); j++) {
                if (!subStr.contains(s.charAt(j))) {
                    subStr.add(s.charAt(j));
                    tempLength++; 
                } else {
                    if (tempLength >= maxLength) {
                        maxLength = tempLength;
                    }
                    tempLength =1;
                    subStr.clear();
                    break;
                }
            }
            if (maxLength < tempLength) {
                maxLength = tempLength;
            }
        }
        return maxLength;
    }

}



/**
* 滑动窗口法，时间复杂度O(n)
*/
class Solution {
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.equals("")) {
            return 0;
        }
        
        Map<Character, Integer> map = new HashMap<>();
        int maxLength = 0;

        for (int end = 0, start = 0; end < s.length(); end++) {
            if (map.containsKey(s.charAt(end))) {
                start = Math.max(map.get(s.charAt(end)) +1, start);
            }
            maxLength = Math.max(end - start +1, maxLength);
            map.put(s.charAt(end), end);
        }
        return maxLength;
    }
}







