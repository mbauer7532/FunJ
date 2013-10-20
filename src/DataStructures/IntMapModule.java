/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

/**
 *
 * @author Neo
 */
public final class IntMapModule {
  public static class Tree<V> {

  }

  private static final class EmptyNode<V> extends Tree<V> {

  }

  private static final class LeafNode<V> extends Tree<V> {
    private LeafNode(final int j, final V val) {
      mj = j;
      mVal = val;
    }

    private final int mj;
    private final V mVal;
  }

  private static final class BranchNode<V> extends Tree<V> {
    private BranchNode(
            final int p,
            final int m,
            final Tree<V> left,
            final Tree<V> right) {
      mp = p;
      mm = m;
      mLeft = left;
      mRight = right;
    }

    private final int mp;
    private final int mm;
    private final Tree<V> mLeft;
    private final Tree<V> mRight;
  }

  private static final EmptyNode<?> sEmptyNode = new EmptyNode<>();

  @SuppressWarnings("unchecked")
  public static <V> Tree<V> empty() {
    return (Tree<V>) sEmptyNode;
  }
}
