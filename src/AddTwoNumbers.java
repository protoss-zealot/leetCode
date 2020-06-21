/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int flag = 0;
        ListNode head = new ListNode(0);
        ListNode result = new ListNode(0);
        ListNode left =  l1;
        ListNode right = l2;
        int leftVal = 0;
        int rightVal = 0;

        result = head;
        while(left!=null || right!=null) {
            leftVal = left == null ? 0 : left.val;
            rightVal = right == null ? 0 : right.val;

            head.val = (rightVal + leftVal + flag) % 10;
            
            if (leftVal + rightVal + flag >= 10) {
                flag = 1;
            } else {
                flag = 0;
            }

            if (left != null) {
              left = left.next;  
            }
            if (right != null) {
                right = right.next;
            }

            if (left != null || right != null || flag == 1) {
                ListNode nextNode = new ListNode(0);
                head.next = nextNode;
                head = nextNode;  
            }              
        }

        if (flag == 1) {
            head.val = 1; 
        }
        return result;
    }
}