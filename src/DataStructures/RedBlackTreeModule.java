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
public class RedBlackTreeModule<T extends Comparable<T>> {
  @SuppressWarnings("unchecked")
  static public <P extends Comparable<P>> RedBlackTreeModule<P> empty() {
    return (RedBlackTreeModule<P>) EMPTY;
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

  public RedBlackTreeModule<T> insert(final T elem) {
    assert elem != null;
    return null;
  }

  public RedBlackTreeModule<T> delete(final T elem) {
    assert elem != null;
    return null;
  }

  private enum Color {
    RED,
    BLACK
  }

  private RedBlackTreeModule<T> ins(final T elem) {
    if (mElem == null) {
      return new RedBlackTreeModule<>(Color.RED, elem, null, null);
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

  static private <T extends Comparable<T>> RedBlackTreeModule<T> balance(
          final Color color,
          final RedBlackTreeModule<T> left,
          final T elem,
          final RedBlackTreeModule<T> right) {
    return null;
  }

  private RedBlackTreeModule(final Color color,
                       final T elem,
                       final RedBlackTreeModule<T> left,
                       final RedBlackTreeModule<T> right) {
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

  private static final RedBlackTreeModule<F> EMPTY = new RedBlackTreeModule<>(Color.RED, null, null, null);

  private final Color mColor;
  private final T mElem;
  private final RedBlackTreeModule<T> mLeft;
  private final RedBlackTreeModule<T> mRight;
}


/*

   let add x s =
    let rec ins = function
      | Empty ->
          Red (Empty, x, Empty)
      | Red (a, y, b) as s ->
          let c = Ord.compare x y in
          if c < 0 then Red (ins a, y, b)
          else if c > 0 then Red (a, y, ins b)
          else s
      | Black (a, y, b) as s ->
          let c = Ord.compare x y in
          if c < 0 then lbalance (ins a) y b
          else if c > 0 then rbalance a y (ins b)
          else s
    in
    match ins s with
      | Black _ as s -> s
      | Red (a, y, b) -> Black (a, y, b)
      | Empty -> assert false

  private RedBlackTreeModule<V> ins(final V x) {
    return new Red(sEmptyNode, x, sEmptyNode);
 */