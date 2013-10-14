/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import java.util.Objects;

/**
 *
 * @author Neo
 * @param <K> Key
 * @param <V> Value
 */
public class AvlTree<K extends Comparable<K>, V> {

  public static abstract class NodeBase<K, V> {
    private NodeBase(final int height) {
      mHeight = height;
    }
    protected final int mHeight;

    public abstract boolean isEmpty();
    public abstract V get(final K key);
    public abstract NodeBase<K, V> put(final K key, final V value);
  }

  private final static class EmptyNode<K extends Comparable<K>, V> extends NodeBase<K, V> {
    private EmptyNode() {
      super(0);
    }

    @Override
    public final boolean isEmpty() { return true; }

    @Override
    public V get(final K key) {
      return null;
    }

    @Override
    public NodeBase<K, V> put(final K key, final V value) {
      return new Node(this, this, key, value, 1);
    }
  }

  private final static class Node<K extends Comparable<K>, V> extends NodeBase<K, V> {
    private final NodeBase<K, V> mLeft;
    private final NodeBase<K, V> mRight;
    private final K mKey;
    private final V mValue;

    private Node(final NodeBase<K, V> left,
                 final NodeBase<K, V> right,
                 final K key,
                 final V value,
                 final int height) {
      super(height);
      mLeft = left;
      mRight = right;
      mKey = key;
      mValue = value;
    }

    @Override
    public final boolean isEmpty() { return false; }

    @Override
    public V get(final K key) {
      Objects.requireNonNull(key, "Key cannot be null.");

      final int res = mKey.compareTo(key);
      if (res < 0) {
        return mLeft.get(key);
      }
      else if (res > 0) {
        return mRight.get(key);
      }
      else {
        return mValue;
      }
    }

    @Override
    public NodeBase<K, V> put(final K key, final V value) {
      Objects.requireNonNull(key, "Key cannot be null.");
      Objects.requireNonNull(value, "Value cannot be null.");

      final int res = mKey.compareTo(key);
      if (res < 0) {
        return rebalance(mLeft.put(key, value), mRight, mKey, mValue);
      }
      else if (res > 0) {
        return rebalance(mLeft, mRight.put(key, value), mKey, mValue);
      }
      else {
        return createNode(mLeft, mRight, key, value, mHeight);
      }
    }

    private static <K extends Comparable<K>, V> Node<K, V> rotateLeft(
            final K key,
            final V value,
            final Node<K, V> r,
            final NodeBase<K, V> l,
            final NodeBase<K, V> rr,
            final NodeBase<K, V> rl) {
      return createNode(rr, createNode(rl, l, key, value), r.mKey, r.mValue);
    }

    private static <K extends Comparable<K>, V> Node<K, V> rotateRight(
            final K key,
            final V value,
            final Node<K, V> l,
            final NodeBase<K, V> r,
            final NodeBase<K, V> ll,
            final NodeBase<K, V> lr) {
      return createNode(ll, createNode(lr, r, key, value), l.mKey, l.mValue);
    }

    private static <K extends Comparable<K>, V> NodeBase<K, V> rebalance(
            final NodeBase<K, V> left,
            final NodeBase<K, V> right,
            final K key,
            final V value) {
      final int leftHeight = left.mHeight;
      final int rightHeight = right.mHeight;

      if (leftHeight > rightHeight + 1) {
        final Node<K, V> l = (Node<K, V>) left;      // Cannot fail!
        final NodeBase<K, V> r = right; // Alias
        final NodeBase<K, V> ll = l.mLeft;
        final NodeBase<K, V> lr = l.mRight;

        if (ll.mHeight < lr.mHeight) {
          final Node<K, V> lrAsNode = (Node<K,V>) lr; // Cannot fail!
          final Node<K, V> newLeft = rotateLeft(l.mKey, l.mValue, lrAsNode, ll, lrAsNode.mRight, lrAsNode.mLeft);
          return rotateRight(key, value, newLeft, r, newLeft.mLeft, newLeft.mRight);
        }
        else {
          return rotateRight(key, value, l, r, ll, lr);
        }
      }
      else if (leftHeight + 1 < rightHeight) {
        final Node<K, V> r = (Node<K, V>) right;    // Cannot fail!
        final NodeBase<K, V> l = left;              // Alias
        final NodeBase<K, V> rl = r.mLeft;
        final NodeBase<K, V> rr = r.mRight;

        if (rr.mHeight < rl.mHeight) {
          final Node<K, V> rlAsNode = (Node<K,V>) rl; // Cannot fail!
          final Node<K, V> newRight = rotateRight(r.mKey, r.mValue, rlAsNode, rr, rlAsNode.mLeft, rlAsNode.mRight);
          return rotateLeft(key, value, newRight, l, newRight.mRight, newRight.mLeft);
        }
        else {
          return rotateLeft(key, value, r, l, rr, rl);
        }
      }
      else {
        return createNode(left, right, key, value);
      }
    }
  }

  private static final EmptyNode<? extends Comparable<?>, ?> sEmptyNode = new EmptyNode<>();

  private static <K extends Comparable<K>, V> Node<K, V> createNode(
          final NodeBase<K, V> left,
          final NodeBase<K, V> right,
          final K key,
          final V value,
          final int height) {
    return new Node(left, right, key, value, height);
  }

  private static <K extends Comparable<K>, V> Node<K, V> createNode(
          final NodeBase<K, V> left,
          final NodeBase<K, V> right,
          final K key,
          final V value) {
    return createNode(left, right, key, value, Math.max(left.mHeight, right.mHeight) + 1);
  }

  // Public interface
  public static <K extends Comparable<K>, V> NodeBase<K, V> empty() {
    return (EmptyNode<K, V>) sEmptyNode;
  }

  public static <K extends Comparable<K>, V> NodeBase<K, V> singleton(final K key, final V value) {
    final NodeBase<K, V> e = empty();
    return createNode(e, e, key, value, 1);
  }
}
