/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.Numeric;
import java.awt.Color;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import org.StructureGraphic.v1.DSTreeNode;
import org.graphstream.graph.*;

public final class AvlTreeModule {
  public static abstract class Tree<K extends Comparable<K>, V> implements DSTreeNode {
    private Tree(final int height) {
      mHeight = height;
    }

    protected final int mHeight;

    public final Optional<K> minKey() {
      return minElementPair().map(p -> p.mx1);
    }

    public final Optional<K> maxKey() {
      return maxElementPair().map(p -> p.mx1);
    }
    
    public abstract boolean isEmpty();
    public abstract V get(final K key);
    public abstract Tree<K, V> put(final K key, final V value);
    public abstract boolean containsKey(final K key);
    public abstract int size();
    public abstract int depth();
    public abstract <U> U fold(BiFunction<V, U, U> f, U acc);
    public abstract Optional<Pair<K, V>> minElementPair();
    public abstract Optional<Pair<K, V>> maxElementPair();

    public abstract String graph(final Graph g);
  }

  private final static class EmptyNode<K extends Comparable<K>, V> extends Tree<K, V> {
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
    public Tree<K, V> put(final K key, final V value) {
      return new Node<>(this, this, key, value, 1);
    }

    @Override
    public boolean containsKey(final K key) {
      Objects.requireNonNull(key, "Key cannot be null.");

      return false;
    }

    @Override
    public int size() {
      return 0;
    }

    @Override
    public int depth() {
      return 0;
    }

    @Override
    public Optional<Pair<K, V>> minElementPair() {
      throw new AssertionError("An empty tree does not have a maximum.");
    }

    @Override
    public Optional<Pair<K, V>> maxElementPair() {
      throw new AssertionError("An empty tree does not have a maximum.");
    }

    @Override
    public String graph(final Graph g) {
      return null;
    }

    @Override
    public <U> U fold(BiFunction<V, U, U> f, U acc) {
      return acc;
    }

    @Override
    public DSTreeNode[] DSgetChildren() {
      return new DSTreeNode[0];
    }

    @Override
    public Object DSgetValue() {
      return "E";
    }

    @Override
    public Color DSgetColor() {
      return Color.GREEN;
    }
  }

  private final static class Node<K extends Comparable<K>, V> extends Tree<K, V> {
    private final Tree<K, V> mLeft;
    private final Tree<K, V> mRight;
    private final K mKey;
    private final V mValue;

    private Node(final Tree<K, V> left,
                 final Tree<K, V> right,
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
      return res < 0 ? mLeft.get(key) : (res > 0 ? mRight.get(key) : mValue);
    }

    @Override
    public Tree<K, V> put(final K key, final V value) {
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

    @Override
    public boolean containsKey(final K key) {
      Objects.requireNonNull(key, "Key cannot be null.");

      final int res = mKey.compareTo(key);
      if (res < 0) {
        return mLeft.containsKey(key);
      }
      else if (res > 0) {
        return mRight.containsKey(key);
      }
      else {
        return true;
      }
    }

    private static <K extends Comparable<K>, V> Node<K, V> rotateLeft(
            final K key,
            final V value,
            final Node<K, V> r,
            final Tree<K, V> l,
            final Tree<K, V> rr,
            final Tree<K, V> rl) {
      return createNode(rr, createNode(rl, l, key, value), r.mKey, r.mValue);
    }

    private static <K extends Comparable<K>, V> Node<K, V> rotateRight(
            final K key,
            final V value,
            final Node<K, V> l,
            final Tree<K, V> r,
            final Tree<K, V> ll,
            final Tree<K, V> lr) {
      return createNode(ll, createNode(lr, r, key, value), l.mKey, l.mValue);
    }

    private static <K extends Comparable<K>, V> Tree<K, V> rebalance(
            final Tree<K, V> left,
            final Tree<K, V> right,
            final K key,
            final V value) {
      final int leftHeight = left.mHeight;
      final int rightHeight = right.mHeight;

      if (leftHeight > rightHeight + 1) {
        final Node<K, V> l = (Node<K, V>) left; // Cannot fail!
        final Tree<K, V> r = right;             // Alias
        final Tree<K, V> ll = l.mLeft;
        final Tree<K, V> lr = l.mRight;

        if (ll.mHeight < lr.mHeight) {
          final Node<K, V> lrAsNode = (Node<K,V>) lr; // Cannot fail!

          // This is equivalalent to:
          //   final Node<K, V> newLeft = rotateLeft(l.mKey, l.mValue, lrAsNode, ll, lrAsNode.mRight, lrAsNode.mLeft);
          //   return rotateRight(key, value, newLeft, r, newLeft.mLeft, newLeft.mRight);
          // but creates one less node.

          return createNode(
                  createNode(ll, lrAsNode.mLeft, l.mKey, l.mValue),
                  createNode(lrAsNode.mRight, r, key, value),
                  lrAsNode.mKey,
                  lrAsNode.mValue);
        }
        else {
          return rotateRight(key, value, l, r, ll, lr);
        }
      }
      else if (leftHeight + 1 < rightHeight) {
        final Node<K, V> r = (Node<K, V>) right;  // Cannot fail!
        final Tree<K, V> l = left;                // Alias
        final Tree<K, V> rl = r.mLeft;
        final Tree<K, V> rr = r.mRight;

        if (rr.mHeight < rl.mHeight) {
          final Node<K, V> rlAsNode = (Node<K,V>) rl; // Cannot fail!

          // This is equivalalent to:
          //   final Node<K, V> newRight = rotateRight(r.mKey, r.mValue, rlAsNode, rr, rlAsNode.mLeft, rlAsNode.mRight);
          //   return rotateLeft(key, value, newRight, l, newRight.mRight, newRight.mLeft);
          // but creates one less node.

          return createNode(
                  createNode(l, rlAsNode.mLeft, key, value),
                  createNode(rlAsNode.mRight, rr, r.mKey, r.mValue),
                  rlAsNode.mKey,
                  rlAsNode.mValue);
        }
        else {
          return rotateLeft(key, value, r, l, rr, rl);
        }
      }
      else {
        return createNode(left, right, key, value);
      }
    }

    @Override
    public int size() {
      return mLeft.size() + mRight.size() + 1;
    }

    @Override
    public String graph(final Graph g) {
      final String nodeName = String.format("%s : %s", mKey.toString(), Integer.toString(mHeight));
      g.addNode(nodeName).addAttribute("ui.label", nodeName);

      final String leftName = mLeft.graph(g);
      final String rightName = mRight.graph(g);

      if (leftName != null)
        g.addEdge(nodeName + "->" + leftName, nodeName, leftName, true);
      if (rightName != null)
        g.addEdge(nodeName + "->" + rightName, nodeName, rightName, true);

      return nodeName;
    }

    @Override
    public int depth() {
      return Math.max(mLeft.depth(), mRight.depth()) + 1;
    }

    @Override
    public Optional<Pair<K, V>> minElementPair() {
      return mLeft == sEmptyNode ? Optional.of(Pair.create(mKey, mValue)) : mLeft.minElementPair();
    }

    @Override
    public Optional<Pair<K, V>> maxElementPair() {
      return mRight == sEmptyNode ? Optional.of(Pair.create(mKey, mValue)) : mRight.maxElementPair();
    }

    @Override
    public <U> U fold(BiFunction<V, U, U> f, U acc) {
      return mLeft.fold(f, f.apply(mValue, mRight.fold(f, acc)));
    }

    @Override
    public DSTreeNode[] DSgetChildren() {
      return new DSTreeNode[] { mLeft, mRight };
    }

    @Override
    public Object DSgetValue() {
      return mKey;
    }

    @Override
    public Color DSgetColor() {
      return Color.BLUE;
    }
  }

  private static final EmptyNode<? extends Comparable<?>, ?> sEmptyNode = new EmptyNode<>();

  private static <K extends Comparable<K>, V> Node<K, V> createNode(
          final Tree<K, V> left,
          final Tree<K, V> right,
          final K key,
          final V value,
          final int height) {
    return new Node<>(left, right, key, value, height);
  }

  private static <K extends Comparable<K>, V> Node<K, V> createNode(
          final Tree<K, V> left,
          final Tree<K, V> right,
          final K key,
          final V value) {
    return createNode(left, right, key, value, Math.max(left.mHeight, right.mHeight) + 1);
  }

  // Public interface
  @SuppressWarnings("unchecked")
  public static <K extends Comparable<K>, V> Tree<K, V> empty() {
    return (EmptyNode<K, V>) sEmptyNode;
  }

  public static <K extends Comparable<K>, V> Tree<K, V> singleton(final K key, final V value) {
    final Tree<K, V> e = empty();
    return createNode(e, e, key, value, 1);
  }

  public static double expectedDepth(final int n) {
    return sDepthCoefficient * Numeric.log(sSqrtOf5 * (double)(n + 2), 2.0) - 2.0;
  }

  private static final double sSqrtOf5 = Math.sqrt(5.0);
  private static final double sDepthCoefficient = Numeric.log(2.0, Numeric.sGoldenRatio);
}
