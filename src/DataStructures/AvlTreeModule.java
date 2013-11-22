/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.ArrayUtils;
import Utils.Functionals.TriFunction;
import Utils.Numeric;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
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

    @Override
    public final <W> Tree<K, W> map(final Function<V, W> f) {
      return mapi((k, v) -> f.apply(v));
    }

    @Override
    public final Tree<K, V> remove(final K key) {
      try {
        return rem(key);
      } catch (ControlExnNoSuchElement ex) {
        return this;
      }
    }

    @Override
    public final Tree<K, V> filteri(final BiPredicate<K, V> f) {
      return fromStrictlyIncreasingArray(getElementsSatisfyingPredicate(f));
    }

    @Override
    public final Pair<Tree<K, V>, Tree<K, V>> partitioni(BiPredicate<K, V> f) {
      final Pair<ArrayList<Pair<K, V>>, ArrayList<Pair<K, V>>> elemsPair = splitElemsAccordingToPredicate(f);

      return Pair.create(fromStrictlyIncreasingArray(elemsPair.mx1),
                         fromStrictlyIncreasingArray(elemsPair.mx2));
    }

    @Override
    public final <W> Tree<K, W> mapPartial(Function<V, Optional<W>> f) {
      return mapPartiali((k, v) -> f.apply(v));
    }

    @Override
    public final <W> Tree<K, W> mapPartiali(BiFunction<K, V, Optional<W>> f) {
      return fromStrictlyIncreasingArray(selectNonEmptyOptionalElements(f));
    }

    @Override
    public final Tree<K, V> merge(final BiFunction<V, V, V> f, final Tree<K, V> t) {
      return fromStrictlyIncreasingArray(mergeArrays(f, keyValuePairs(), t.keyValuePairs()));
    }

    @Override
    public final Optional<Pair<K, V>> lowerPair(final K key) {
      return AvlTreeModule.lowerPair(this, key);
    }

    @Override
    public final Optional<Pair<K, V>> higherPair(final K key) {
      return AvlTreeModule.higherPair(this, key);
    }

    abstract Tree<K, V> rem(final K key) throws ControlExnNoSuchElement;
    abstract int getBalance();
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
    public Tree<K, V> insert(final BiFunction<V, V, V> f, final K key, final V value) {
      return Node.create(this, key, value, this, 1);
    }

    @Override
    public int size() {
      return 0;
    }

    @Override
    public int height() {
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

    @Override
    public boolean containsValue(final V value) {
      return false;
    }

    @Override
    Tree<K, V> rem(final K key)  throws ControlExnNoSuchElement {
      // This exception is used for control purposes.
      // When removing non-existent elements we simply return the same input tree
      // no new allocations take place.  The alternative implementation that 
      // copies the path is: return Pair.create(this, false);
      throw sNoSuchElement;
    }

    @Override
    public <W> W foldli(final TriFunction<K, V, W, W> f, final W w) {
      return w;
    }

    @Override
    public <W> W foldri(final TriFunction<K, V, W, W> f, final W w) {
      return w;
    }

    @Override
    final int getBalance() {
      return 0;
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
      final int res = mKey.compareTo(key);

      if (res < 0) {
        return mLeft.get(key);
      }
      else if (res > 0) {
        return mRight.get(key);
      }
      else {
        return Optional.of(mValue);
      }
    }

    @Override
    public Tree<K, V> insert(final BiFunction<V, V, V> f, final K key, final V value) {
      final int res = mKey.compareTo(key);

      if (res < 0) {
        return rebalance(mLeft.insert(key, value), mRight, mKey, mValue);
      }
      else if (res > 0) {
        return rebalance(mLeft, mRight.insert(key, value), mKey, mValue);
      }
      else {
        return create(mLeft, key, f.apply(mValue, value), mRight, mHeight);
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
    public int height() {
      return Math.max(mLeft.height(), mRight.height()) + 1;
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
    public Optional<Pair<K, V>> minElementPair() {
      if (mLeft.isEmpty()) {
        return Optional.of(Pair.create(mKey, mValue));
      }
      else {
        return mLeft.minElementPair();
      }
    }

    @Override
    public Optional<Pair<K, V>> maxElementPair() {
      if (mRight.isEmpty()) {
        return Optional.of(Pair.create(mKey, mValue));
      }
      else {
        return mRight.maxElementPair();
      }
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

    @Override
    public boolean containsValue(final V value) {
      return mValue.equals(value)
              || mLeft.containsValue(value)
              || mRight.containsValue(value);
    }

    private static <K extends Comparable<K>, V> Tree<K, V> rightRotate(final Node<K, V> z) {
      final Node<K, V> y = (Node<K, V>) z.mLeft;
      return create(
              y.mLeft,
              y.mKey,
              y.mValue,
              create(y.mRight, z.mKey, z.mValue, z.mRight));
    }

    private static <K extends Comparable<K>, V> Tree<K, V> leftRotate(final Node<K, V> z) {
      final Node<K, V> y = (Node<K, V>) z.mRight;
      return create(
              create(z.mLeft, z.mKey, z.mValue, y.mLeft),
              y.mKey,
              y.mValue,
              y.mRight);
    }

    private static <K extends Comparable<K>, V> Tree<K, V> leftRightRotate(final Node<K, V> z) {
      final Node<K, V> y = (Node<K, V>) z.mLeft;
      final Node<K, V> x = (Node<K, V>) y.mRight;
      return create(
              create(y.mLeft, y.mKey, y.mValue, x.mLeft),
              x.mKey,
              x.mValue,
              create(x.mRight, z.mKey, z.mValue, z.mRight));
    }

    private static <K extends Comparable<K>, V> Tree<K, V> rightLeftRotate(final Node<K, V> z) {
      final Node<K, V> y = (Node<K, V>) z.mRight;
      final Node<K, V> x = (Node<K, V>) y.mLeft;
      return create(
              create(z.mLeft, z.mKey, z.mValue, x.mLeft),
              x.mKey,
              x.mValue,
              create(x.mRight, y.mKey, y.mValue, y.mRight));
    }

    @Override
    Tree<K, V> rem(final K key) throws ControlExnNoSuchElement {
      final Node<K, V> root;
      final int res = key.compareTo(mKey);

      if (res < 0) {
        root = create(mLeft.rem(key), mKey, mValue, mRight);
      }
      else if (res > 0) {
        root = create(mLeft, mKey, mValue, mRight.rem(key));
      }
      else {
        final boolean leftIsEmpty  = mLeft.isEmpty();
        final boolean rightIsEmpty = mRight.isEmpty();

        if (leftIsEmpty && rightIsEmpty) {
          return EmptyNode.create();
        }
        else if (leftIsEmpty) {
          root = (Node<K, V>) mRight;
        }
        else if (rightIsEmpty) {
          root = (Node<K, V>) mLeft;
        }
        else {
          final Pair<K, V> successor = mRight.minElementPair().get(); // We know it is there.
          root = create(mLeft, successor.mx1, successor.mx2, mRight.rem(successor.mx1));
        }
      }

      final int balance = root.getBalance();

      if (balance > 1) {
        if (root.mLeft.getBalance() >= 0) {
          return rightRotate(root);
        }
        else {
          return leftRightRotate(root);
        }
      }
      else if (balance < -1) {
        if (root.mRight.getBalance() <= 0) {
          return leftRotate(root);
        }
        else {
          return rightLeftRotate(root);
        }
      }
      else {
        return root;
      }
    }

    @Override
    public <W> W foldli(final TriFunction<K, V, W, W> f, final W w) {
      return mRight.foldli(f, f.apply(mKey, mValue, mLeft.foldli(f, w)));
    }

    @Override
    public <W> W foldri(final TriFunction<K, V, W, W> f, final W w) {
      return mLeft.foldri(f, f.apply(mKey, mValue, mRight.foldri(f, w)));
    }

    @Override
    final int getBalance() {
      return mLeft.mHeight - mRight.mHeight;
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

  private static <K extends Comparable<K>, V> Optional<Pair<K, V>> makeBoundPair(final Node<K, V> candidate) {
    return candidate == null
            ? Optional.empty()
            : Optional.of(Pair.create(candidate.mKey, candidate.mValue));
  }

  static <K extends Comparable<K>, V> Optional<Pair<K, V>> lowerPair(final Tree<K, V> t, final K key) {
    Tree<K, V> tree = t;
    Node<K, V> n, candidate = null;

    while (! tree.isEmpty()) {
      n = (Node<K, V>) tree;
      final int res = key.compareTo(n.mKey);
      if (res > 0) {
        tree = n.mRight;
        candidate = n;
      }
      else if (res < 0) {
        tree = n.mLeft;
      }
      else {
         final Optional<Pair<K, V>> p = n.mLeft.maxElementPair();
         if (p.isPresent()) {
           return p;
         }
         else {
           break;
         }
      }
    }

    return makeBoundPair(candidate);
  }

  private static <K extends Comparable<K>, V> Optional<Pair<K, V>> higherPair(final Tree<K, V> t, final K key) {
    Tree<K, V> tree = t;
    Node<K, V> n, candidate = null;

    while (! tree.isEmpty()) {
      n = (Node<K, V>) tree;
      final int res = key.compareTo(n.mKey);
      if (res > 0) {
        tree = n.mRight;
      }
      else if (res < 0) {
        tree = n.mLeft;
        candidate = n;
      }
      else {
        final Optional<Pair<K, V>> p = n.mRight.minElementPair();
        if (p.isPresent()) {
           return p;
         }
         else {
           break;
         }
      }
    }

    return makeBoundPair(candidate);
  }

  public static double expectedHeight(final int n) {
    return sDepthCoefficient * Numeric.log(sSqrtOf5 * (double)(n + 2), 2.0) - 2.0;
  }

  private static final double sSqrtOf5 = Math.sqrt(5.0);
  private static final double sDepthCoefficient = Numeric.log(2.0, Numeric.sGoldenRatio);

  private static final class ControlExnNoSuchElement extends Exception {};

  private static final ControlExnNoSuchElement sNoSuchElement = new ControlExnNoSuchElement();

  private static <K extends Comparable<K>, V> boolean binaryTreePropertyHolds(final Tree<K, V> t) {
    return ArrayUtils.isStrictlyIncreasing(t.keys());
  }

  private static <K extends Comparable<K>, V> boolean heightOfAvlTreeConstraintHolds(final Tree<K, V> t) {
    final int avlTreeSize   = t.size();
    final int avlTreeHeight = t.height();
    
    final double expectedHeight = expectedHeight(avlTreeSize);
    
    return (double)avlTreeHeight < expectedHeight;
  }

  private static final class ControlExnAvlTreeProperty extends Exception {};

  private static final ControlExnAvlTreeProperty sAvlTreeInvariantViolated = new ControlExnAvlTreeProperty();
  
  private static <K extends Comparable<K>, V> int avlTreePropertyHoldsAux(final Tree<K, V> t) throws ControlExnAvlTreeProperty {
    if (t.isEmpty()) {
      return 0;
    }
    else {
      final Node<K, V> node = (Node<K, V>) t;
      final int leftHeight  = avlTreePropertyHoldsAux(node.mLeft);
      final int rightHeight = avlTreePropertyHoldsAux(node.mRight);
      if (Math.abs(leftHeight - rightHeight) > 1) {
        throw sAvlTreeInvariantViolated;
      }

      return Math.max(leftHeight, rightHeight) + 1;
    }
  }

  private static <K extends Comparable<K>, V> boolean avlTreePropertyHolds(final Tree<K, V> t) {
    try {
      avlTreePropertyHoldsAux(t);
      return true;
    }
    catch (ControlExnAvlTreeProperty e) {
      return false;
    }
  }

  static <K extends Comparable<K>, V> Pair<Boolean, String> verifyAVLTreeProperties(final Tree<K, V> t) {
    if (! binaryTreePropertyHolds(t)) {
      return Pair.create(false, "Binary Tree property does not hold.");
    }

    if (! heightOfAvlTreeConstraintHolds(t)) {
      return Pair.create(false, "The depth of the tree was larger than the expected size.");
    }

    if (! avlTreePropertyHolds(t)) {
      return Pair.create(false, "AVL property does not hold on this tree.");
    }
 
    return Pair.create(true, "Success!");
  }
}
