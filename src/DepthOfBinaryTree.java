/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
class Solution {
    public int maxDepth(TreeNode root) {
        int maxDepth = 1;
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) {
            return 1;
        }
        if (root.left != null) {
            int leftDepth = maxDepth(root.left);
            maxDepth = Math.max(maxDepth, leftDepth + 1);
        }
        if (root.right != null) {
            int rightDepth = maxDepth(root.right);
            maxDepth = Math.max(maxDepth, rightDepth + 1);
        }
        return maxDepth;
    }
}