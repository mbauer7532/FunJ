/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

/**
 *
 * @author Neo
 * @param <T>
 */
public class RedBlackTree<T extends Comparable<T>> {
  static public <P extends Comparable<P>> RedBlackTree<P> empty() {
    return (RedBlackTree<P>) EMPTY;
  }

  public boolean member(final T elem) {
    assert elem != null;

    if (mElem == null) {
      return false;
    }
    else {
      final int res = elem.compareTo(mElem);
      return res < 0 ? mLeft.member(elem) : (res > 0 ? mRight.member(elem) : true);
    }
  }

  public RedBlackTree<T> insert(final T elem) {
    assert elem != null;
    return null;
  }

  public RedBlackTree<T> delete(final T elem) {
    assert elem != null;
    return null;
  }

  private enum Color {
    RED,
    BLACK
  }

  private RedBlackTree<T> ins(final T elem) {
    if (mElem == null) {
      return new RedBlackTree<>(Color.RED, elem, null, null);
    }
    else {
      final int res = elem.compareTo(mElem);
      if (res < 0) {
        return balance(mColor, mLeft.ins(elem), mElem, mRight);
      }
      else if (res > 0) {
        return balance(mColor, mLeft, mElem, mRight.ins(elem));
      }
      else {
        return this;
      }
    }
  }

  static private <T extends Comparable<T>> RedBlackTree<T> balance(
          final Color color,
          final RedBlackTree<T> left,
          final T elem,
          final RedBlackTree<T> right) {
    return null;
  }

  private RedBlackTree(final Color color,
                       final T elem,
                       final RedBlackTree<T> left,
                       final RedBlackTree<T> right) {
    mColor = color;
    mElem  = elem;
    mLeft  = left;
    mRight = right;
  }

  static private class F implements Comparable<F> {
    @Override
    public int compareTo(F o) {
      throw new UnsupportedOperationException("Not supported.");
    }
  }

  private static final RedBlackTree<F> EMPTY = new RedBlackTree<>(Color.RED, null, null, null);

  private final Color mColor;
  private final T mElem;
  private final RedBlackTree<T> mLeft;
  private final RedBlackTree<T> mRight;
}
