/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode reverseList(ListNode head) {
        if (head == null) {
            return head;
        }
        ListNode tempNode = null;
        ListNode prevNode = null;
        while(head != null) {
            tempNode = head;
            head = head.next;
            tempNode.next = prevNode;
            prevNode = tempNode;
        }
        head = tempNode;
        return head;
    }
}