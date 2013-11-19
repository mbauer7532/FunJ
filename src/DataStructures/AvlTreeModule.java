/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.Numeric;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.StructureGraphic.v1.DSTreeNode;
import org.graphstream.graph.*;

public final class AvlTreeModule {
  public static abstract class Tree<K extends Comparable<K>, V>
                               extends PersistentMapBase<K, V, Tree<K, V>>
                               implements DSTreeNode {
    private Tree(final int height) {
      mHeight = height;
    }

    protected final int mHeight;

    @Override
    public abstract <W> Tree<K, W> mapi(final BiFunction<K, V, W> f);
    
    public abstract String graph(final Graph g);
  }

  private final static class EmptyNode<K extends Comparable<K>, V> extends Tree<K, V> {
    private static final EmptyNode<? extends Comparable<?>, ?> sEmptyNode = new EmptyNode<>();

    @SuppressWarnings("unchecked")
    public static <K extends Comparable<K>, V> EmptyNode<K, V> create() {
      return (EmptyNode<K, V>) sEmptyNode;
    }

    private EmptyNode() {
      super(0);
    }

    @Override
    public final boolean isEmpty() { return true; }

    @Override
    public Optional<V> get(final K key) {
      return Optional.empty();
    }

    @Override
    public Tree<K, V> insert(final K key, final V value) {
      return Node.create(this, key, value, this, 1);
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
    public <W> Tree<K, W> mapi(final BiFunction<K, V, W> f) {
      return create();
    }

    @Override
    public void appi(final BiConsumer<K, V> f) {
      return;
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
    private final K mKey;
    private final V mValue;
    private final Tree<K, V> mRight;

    private Node(final Tree<K, V> left,
                 final K key,
                 final V value,
                 final Tree<K, V> right,
                 final int height) {
      super(height);
      mLeft = left;
      mKey = key;
      mValue = value;
      mRight = right;
    }

    public static <K extends Comparable<K>, V> Node<K, V> create(
            final Tree<K, V> left,
            final K key,
            final V value,
            final Tree<K, V> right,
            final int height) {
      return new Node<>(left, key, value, right, height);
    }

    public static <K extends Comparable<K>, V> Node<K, V> create(
            final Tree<K, V> left,
            final K key,
            final V value,
            final Tree<K, V> right) {
      return create(left, key, value, right, Math.max(left.mHeight, right.mHeight) + 1);
    }

    @Override
    public final boolean isEmpty() { return false; }

    @Override
    public Optional<V> get(final K key) {
      Objects.requireNonNull(key, "Key cannot be null.");

      final int res = mKey.compareTo(key);
      return res < 0
              ? mLeft.get(key)
              : (res > 0
                 ? mRight.get(key)
                 : Optional.of(mValue));
    }

    @Override
    public Tree<K, V> insert(final K key, final V value) {
      Objects.requireNonNull(key, "Key cannot be null.");
      Objects.requireNonNull(value, "Value cannot be null.");

      final int res = mKey.compareTo(key);
      if (res < 0) {
        return rebalance(mLeft.insert(key, value), mRight, mKey, mValue);
      }
      else if (res > 0) {
        return rebalance(mLeft, mRight.insert(key, value), mKey, mValue);
      }
      else {
        return create(mLeft, key, value, mRight, mHeight);
      }
    }

    private static <K extends Comparable<K>, V> Node<K, V> rotateLeft(
            final K key,
            final V value,
            final Node<K, V> r,
            final Tree<K, V> l,
            final Tree<K, V> rr,
            final Tree<K, V> rl) {
      return create(rr, r.mKey, r.mValue, create(rl, key, value, l));
    }

    private static <K extends Comparable<K>, V> Node<K, V> rotateRight(
            final K key,
            final V value,
            final Node<K, V> l,
            final Tree<K, V> r,
            final Tree<K, V> ll,
            final Tree<K, V> lr) {
      return create(ll, l.mKey, l.mValue, create(lr, key, value, r));
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

          return create(
                  create(ll, l.mKey, l.mValue, lrAsNode.mLeft),
                  lrAsNode.mKey,
                  lrAsNode.mValue,
                  create(lrAsNode.mRight, key, value, r));
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

          return create(
                  create(l, key, value, rlAsNode.mLeft),
                  rlAsNode.mKey,
                  rlAsNode.mValue,
                  create(rlAsNode.mRight, r.mKey, r.mValue, rr));
        }
        else {
          return rotateLeft(key, value, r, l, rr, rl);
        }
      }
      else {
        return create(left, key, value, right);
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
      return mLeft.isEmpty()
              ? Optional.of(Pair.create(mKey, mValue))
              : mLeft.minElementPair();
    }

    @Override
    public Optional<Pair<K, V>> maxElementPair() {
      return mRight.isEmpty()
              ? Optional.of(Pair.create(mKey, mValue))
              : mRight.maxElementPair();
    }

    @Override
    public <W> Tree<K, W> mapi(final BiFunction<K, V, W> f) {
      return create(mLeft.mapi(f), mKey, f.apply(mKey, mValue), mRight.mapi(f), mHeight);
    }

    @Override
    public void appi(final BiConsumer<K, V> f) {
      mLeft.appi(f);
      f.accept(mKey, mValue);
      mRight.appi(f);

      return;
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

  // Public interface
  public static <K extends Comparable<K>, V> Tree<K, V> empty() {
    return EmptyNode.create();
  }

  public static <K extends Comparable<K>, V> Tree<K, V> singleton(final K key, final V value) {
    final Tree<K, V> e = empty();
    return Node.create(e, key, value, e, 1);
  }

  private static final class InitFromArrayWorker<K extends Comparable<K>, V> {
    private final ArrayList<Pair<K, V>> mVector;
    private final boolean mIncreasing;

    
    public InitFromArrayWorker(final ArrayList<Pair<K, V>> vector,
                               final boolean increasing) {
      mVector = vector;
      mIncreasing = increasing;
    }

    private Tree<K, V> workerFunc(final int left, final int right) {
      if (left > right)
        return empty();

      final int mid = (left + right) >>> 1;
      final Pair<K, V> p = mVector.get(mid);

      Tree<K, V> lt, rt;
      lt = workerFunc(left, mid - 1);
      rt = workerFunc(mid + 1, right);

      if (! mIncreasing) {
        Tree<K, V> t = lt;
        lt = rt;
        rt = t;
      }

      return Node.create(lt, p.mx1, p.mx2, rt);
    }
    
    public final Tree<K, V> doIt() {
      return workerFunc(0, mVector.size() - 1);
    }
  }

  public static <K extends Comparable<K>, V> Tree<K, V> fromStrictlyIncreasingArray(final ArrayList<Pair<K, V>> v) {
    return (new InitFromArrayWorker<>(v, true).doIt());
  }

  public static <K extends Comparable<K>, V> Tree<K, V> fromStrictlyDecreasingArray(final ArrayList<Pair<K, V>> v) {
    return (new InitFromArrayWorker<>(v, false).doIt());
  }

  public static <K extends Comparable<K>, V> Tree<K, V> fromArray(final ArrayList<Pair<K, V>> v) {
    return v.stream()
            .reduce(empty(),
                    ((t, p) -> t.insert(p.mx1, p.mx2)),
                    ((t1, t2) -> { throw new AssertionError("Must not be used.  Stream is not parallel."); }));
  }

  public static double expectedDepth(final int n) {
    return sDepthCoefficient * Numeric.log(sSqrtOf5 * (double)(n + 2), 2.0) - 2.0;
  }

  private static final double sSqrtOf5 = Math.sqrt(5.0);
  private static final double sDepthCoefficient = Numeric.log(2.0, Numeric.sGoldenRatio);
}
