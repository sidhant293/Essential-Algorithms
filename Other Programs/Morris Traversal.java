/**
 Inorder Morris Traversal
 
 Last node of any subtree, connect it to root
 */
public class Solution {
    public ArrayList<Integer> inorderTraversal(TreeNode A) {
        TreeNode root=A;
        ArrayList<Integer> list=new ArrayList<>();
        
        while(root!=null){
//           if left is null then we are at processing part of inorder traversal.
//           Right will never be null as we are connecting right of every subtree to its root
            if(root.left==null){
                list.add(root.val);
                root=root.right;
            }else{
                TreeNode rightMost=getRightMost(root.left,root);
//                 if right null that means we are first time visiting it, so connect it to its parent
                if(rightMost.right==null) {
                    rightMost.right=root;
                    root=root.left;
                }else{
//                   if right isn't null so it was visited before, remove the previously created link
                    rightMost.right=null;
                    list.add(root.val);
                    root=root.right;
                } 
            }
        }
        
        return list;
    }
    
    TreeNode getRightMost(TreeNode root,TreeNode parent){
        while(root.right!=null && root.right!=parent) root=root.right;
        return root;
    }
}
