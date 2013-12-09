/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// https://github.com/bmeurer/ocaml-rbtrees/blob/master/src/rbmap.ml
// https://www.lri.fr/~filliatr/ftp/ocaml/ds/rbset.ml.html

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.ArrayUtils;
import Utils.Functionals.TriFunction;
import Utils.Numeric;
import Utils.Ref;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.StructureGraphic.v1.DSTreeNode;

/**
 *
 * @author Neo
 */
public class RedBlackTreeModule2 {
  public static final class Tree<K extends Comparable<K>, V>
                                 extends PersistentMapBase<K, V, Tree<K, V>> {
    private static final int sEmptyTag = 0;
    private static final int sRedTag   = 1;
    private static final int sBlackTag = 2;

    private Tree(final Tree<K, V> left, final K key, final V value, final Tree<K, V> right, final int tag) {
      mKey   = key;
      mValue = value;
      mLeft  = left;
      mRight = right;
      mTag   = tag;
    }

    @Override
    public K getKey() {
      if (isNull()) {
        throw new AssertionError("The empty tree has no key.");
      }

      return mKey;
    }

    @Override
    public V getValue() {
      if (isNull()) {
        throw new AssertionError("The empty tree has no value.");
      }

      return mValue;
    }

    public static <K extends Comparable<K>, V> Tree<K, V> empty() {
      return createEmptyNode();
    }

    public static <K extends Comparable<K>, V> Tree<K, V> singleton(final K key, final V value) {
      final Tree<K, V> e = empty();
      return createBlackNode(e, key, value, e);
    }

    public static <K extends Comparable<K>, V> Tree<K, V> fromStrictlyIncreasingStream(final Stream<PersistentMapEntry<K, V>> stream) {
      return fromStrictlyIncreasingArray(stream.collect(Collectors.toCollection(ArrayList::new)));
    }

    public static <K extends Comparable<K>, V> Tree<K, V> fromStrictlyDecreasingStream(final Stream<PersistentMapEntry<K, V>> stream) {
      return fromStrictlyDecreasingArray(stream.collect(Collectors.toCollection(ArrayList::new)));
    }

    public static <K extends Comparable<K>, V> Tree<K, V> fromStrictlyIncreasingArray(final ArrayList<PersistentMapEntry<K, V>> v) {
      return (new InitFromArrayWorker<>(v, true, computeRedDepth(v.size())).doIt());
    }

    public static <K extends Comparable<K>, V> Tree<K, V> fromStrictlyDecreasingArray(final ArrayList<PersistentMapEntry<K, V>> v) {
      return (new InitFromArrayWorker<>(v, false, computeRedDepth(v.size())).doIt());
    }

    public static <K extends Comparable<K>, V> Tree<K, V> fromStream(final Stream<PersistentMapEntry<K, V>> stream) {
      return stream.reduce(empty(),
                           ((t, p) -> t.insert(p.getKey(), p.getValue())),
                           ((t1, t2) -> { throw new AssertionError("Must not be used.  Stream is not parallel."); }));
    }

    public static <K extends Comparable<K>, V> Tree<K, V> fromArray(final ArrayList<PersistentMapEntry<K, V>> v) {
      return fromStream(v.stream());
    }

    private static final Tree<? extends Comparable<?>, ?> sEmptyNode = new Tree<>(null, null, null, null, sEmptyTag);

    @SuppressWarnings("unchecked")
    private static <K extends Comparable<K>, V> Tree<K, V> createEmptyNode() {
      return (Tree<K, V>) sEmptyNode;
    }

    private static <K extends Comparable<K>, V> Tree<K, V> createBlackNode(
            final Tree<K, V> left,
            final K key,
            final V value,
            final Tree<K, V> right) {
      return new Tree<>(left, key, value, right, sBlackTag);
    }

    private static <K extends Comparable<K>, V> Tree<K, V> createRedNode(
            final Tree<K, V> left,
            final K key,
            final V value,
            final Tree<K, V> right) {
      return new Tree<>(left, key, value, right, sRedTag);
    }

    private final K mKey;
    private final V mValue;
    private final Tree<K, V> mLeft;
    private final Tree<K, V> mRight;
    private final int mTag;

    private boolean isNull()  { return mTag == sEmptyTag; }
    private boolean isRed()   { return mTag == sRedTag; }
    private boolean isBlack() { return mTag == sBlackTag; }

    private static <K extends Comparable<K>, V> Tree<K, V> b2r(final Tree<K, V> b) {
      return createRedNode(b.mLeft, b.mKey, b.mValue, b.mRight);
    }

    private static <K extends Comparable<K>, V> Tree<K, V> r2b(final Tree<K, V> r) {
      return createBlackNode(r.mLeft, r.mKey, r.mValue, r.mRight);
    }

//    let lbalance x1 x2 x3 = match x1, x2, x3 with
//    | Red (Red (a,x,b), y, c), z, d ->
//        Red (Black (a,x,b), y, Black (c,z,d))
//    | Red (a, x, Red (b,y,c)), z, d ->
//        Red (Black (a,x,b), y, Black (c,z,d))
//    | a,x,b ->
//        Black (a,x,b)
    private static <K extends Comparable<K>, V> Tree<K, V> leftBalance(final Tree<K, V> left,
                                                                       final K key,
                                                                       final V value,
                                                                       final Tree<K, V> right) {
      if (left.isRed()) {
        if (left.mLeft.isRed()) {

          return createRedNode(r2b(left.mLeft),
                               left.mKey,
                               left.mValue,
                               createBlackNode(left.mRight, key, value, right));
        }
        else if (left.mRight.isRed()) {
          final Tree<K, V> lr = left.mRight;

          return createRedNode(createBlackNode(left.mLeft, left.mKey, left.mValue, lr.mLeft),
                               lr.mKey,
                               lr.mValue,
                               createBlackNode(lr.mRight, key, value, right));
        }
      }
 
      return createBlackNode(left, key, value, right);
    }

//  let rbalance x1 x2 x3 = match x1, x2, x3 with
//    | a, x, Red (Red (b,y,c), z, d) ->
//        Red (Black (a,x,b), y, Black (c,z,d))
//    | a, x, Red (b, y, Red (c,z,d)) ->
//        Red (Black (a,x,b), y, Black (c,z,d))
//    | a,x,b ->
//        Black (a,x,b)

    private static <K extends Comparable<K>, V> Tree<K, V> rightBalance(final Tree<K, V> left,
                                                                        final K key,
                                                                        final V value,
                                                                        final Tree<K, V> right) {
      if (right.isRed()) {
        if (right.mLeft.isRed()) {
          final Tree<K, V> rl = right.mLeft;

          return createRedNode(
                  createBlackNode(left, key, value, rl.mLeft),
                  rl.mKey,
                  rl.mValue,
                  createBlackNode(rl.mRight, right.mKey, right.mValue, right.mRight));
        }
        else if (right.mRight.isRed()) {
          return createRedNode(
                  createBlackNode(left, key, value, right.mLeft),
                  right.mKey,
                  right.mValue,
                  r2b(right.mRight));
        }
      }

      return createBlackNode(left, key, value, right);
    }

//    (* [unbalanced_left] repares invariant (2) when the black height of the
//     left son exceeds (by 1) the black height of the right son *)
//
//  let unbalanced_left = function
//    | Red (Black (t1, x1, t2), x2, t3) ->
//        lbalance (Red (t1, x1, t2)) x2 t3, false
//    | Black (Black (t1, x1, t2), x2, t3) ->
//        lbalance (Red (t1, x1, t2)) x2 t3, true
//    | Black (Red (t1, x1, Black (t2, x2, t3)), x3, t4) ->
//        Black (t1, x1, lbalance (Red (t2, x2, t3)) x3 t4), false
//    | _ ->
//        assert false

    private static <K extends Comparable<K>, V> boolean unbalancedLeft(final Ref<Tree<K, V>> treeRef, final Tree<K, V> t) {
      if (t.isRed()) {
        if (t.mLeft.isBlack()) {
          treeRef.r = leftBalance(b2r(t.mLeft), t.mKey, t.mValue, t.mRight);
          return false;
        }
    }
    else if (t.isBlack()) {
      final Tree<K, V> left = t.mLeft;

      if (left.isBlack()) {
        treeRef.r = leftBalance(b2r(left), t.mKey, t.mValue, t.mRight);
        return true;
      }
      else if (left.isRed() && left.mRight.isBlack()) {
        treeRef.r = createBlackNode(left.mLeft, left.mKey, left.mValue,
                                    leftBalance(b2r(left.mRight), t.mKey, t.mValue, t.mRight));
        return false;
      }
    }

    throw new AssertionError("Should never get here.");
  }

//  let unbalanced_right = function
//    | Red (t1, x1, Black (t2, x2, t3)) ->
//        rbalance t1 x1 (Red (t2, x2, t3)), false
//    | Black (t1, x1, Black (t2, x2, t3)) ->
//        rbalance t1 x1 (Red (t2, x2, t3)), true
//    | Black (t1, x1, Red (Black (t2, x2, t3), x3, t4)) ->
//        Black (rbalance t1 x1 (Red (t2, x2, t3)), x3, t4), false
//    | _ ->
//        assert false

    private static <K extends Comparable<K>, V> boolean unbalancedRight(final Ref<Tree<K, V>> treeRef, final Tree<K, V> t) {
      if (t.isRed()) {
        if (t.mRight.isBlack()) {
          treeRef.r = rightBalance(t.mLeft, t.mKey, t.mValue, b2r(t.mRight));
          return false;
        }
      }
      else if (t.isBlack()) {
        final Tree<K, V> right = t.mRight;

        if (right.isBlack()) {
          treeRef.r = rightBalance(t.mLeft, t.mKey, t.mValue, b2r(right));
          return true;
        }
        else if (right.isRed() && right.mLeft.isBlack()) {
          treeRef.r = createBlackNode(rightBalance(t.mLeft, t.mKey, t.mValue, b2r(right.mLeft)),
                                      right.mKey, right.mValue, right.mRight);
          return false;
        }
      }

      throw new AssertionError("Should never get here.");
    }

    @Override
    public final boolean isEmpty() {
      return isNull();
    }

    private int sizeImpl() {
      return isNull() ? 0 : mLeft.sizeImpl() + mRight.sizeImpl() + 1;
    }

    @Override
    public int size() {
      return sizeImpl();
    }

    private int heightImpl() {
      return isNull() ? 0 : Math.max(mLeft.heightImpl(), mRight.heightImpl()) + 1;
    }

    @Override
    public int height() {
      return heightImpl();
    }

    @Override
    public void appEntry(final Consumer<PersistentMapBase<K, V, Tree<K, V>>> f) {
      if (! isNull()) {
        mLeft.appEntry(f);
        f.accept(this);
        mRight.appEntry(f);
      }
    }

    private void appiImpl(final BiConsumer<K, V> f) {
      if (! isNull()) {
        mLeft.appiImpl(f);
        f.accept(mKey, mValue);
        mRight.appiImpl(f);
      }
    }

    @Override
    public void appi(final BiConsumer<K, V> f) {
      appiImpl(f);
    }

    private <W> W foldliImpl(final TriFunction<K, V, W, W> f, final W w) {
      return isNull() ? w : mRight.foldliImpl(f, f.apply(mKey, mValue, mLeft.foldliImpl(f, w)));
    }

    @Override
    public <W> W foldli(final TriFunction<K, V, W, W> f, final W w) {
      return foldliImpl(f, w);
    }

    private <W> W foldriImpl(final TriFunction<K, V, W, W> f, final W w) {
      return isNull() ? w : mLeft.foldriImpl(f, f.apply(mKey, mValue, mRight.foldriImpl(f, w)));
    }

    @Override
    public <W> W foldri(final TriFunction<K, V, W, W> f, final W w) {
      return foldriImpl(f, w);
    }

    private static <K extends Comparable<K>, V> Optional<PersistentMapEntry<K, V>> minElementPairImpl(final Tree<K, V> t) {
      if (t.isNull()) {
        return Optional.empty();
      }
      else {
        Tree<K, V> m = t;

        while (true) {
          if (m.mLeft.isNull()) {
            return Optional.of(new EntryRef<>(m));
          }
          else {
            m = m.mLeft;
          }
        }
      }
    }

    @Override
    public Optional<PersistentMapEntry<K, V>> minElementPair() {
      return minElementPairImpl(this);
    }

    private static <K extends Comparable<K>, V> Optional<PersistentMapEntry<K, V>> maxElementPairImpl(final Tree<K, V> t) {
      if (t.isNull()) {
        return Optional.empty();
      }
      else {
        Tree<K, V> m = t;

        while (true) {
          if (m.mRight.isNull()) {
            return Optional.of(new EntryRef<>(m));
          }
          else {
            m = m.mRight;
          }
        }
      }
    }

    @Override
    public Optional<PersistentMapEntry<K, V>> maxElementPair() {
      return maxElementPairImpl(this);
    }

    private static <K extends Comparable<K>, V> Optional<V> getImpl(final Tree<K, V> tree, final K key) {
      Tree<K, V> t = tree;

      while (! t.isNull()) {
        final int res = key.compareTo(t.mKey);
        if (res < 0) {
          t = t.mLeft;
        }
        else if (res > 0) {
          t = t.mRight;
        }
        else {
          return Optional.of(t.mValue);
        }
      }

      return Optional.empty();
    }

    @Override
    public Optional<V> get(final K key) {
      return getImpl(this, key);
    }

    private static <K extends Comparable<K>, V> boolean containsValueImpl(final Tree<K, V> tree, final V value) {
      if (tree.isNull()) {
        return false;
      }

      final ArrayDeque<Tree<K, V>> dq = new ArrayDeque<>();
      dq.addFirst(tree);

      do {
        final Tree<K, V> t = dq.removeFirst();
        if (! t.isNull()) {
          if (t.mValue.equals(value)) {
            return true;
          }
          dq.addFirst(t.mRight);
          dq.addFirst(t.mLeft);
        }
      } while (! dq.isEmpty());

      return false;
    }

    @Override
    public boolean containsValue(final V value) {
      return containsValueImpl(this, value);
    }

    @Override
    public final Tree<K, V> filteri(final BiPredicate<K, V> f) {
      return fromStrictlyIncreasingArray(getElementsSatisfyingPredicate(f));
    }

    @Override
    public final Pair<Tree<K, V>, Tree<K, V>> partitioni(final BiPredicate<K, V> f) {
      final Pair<ArrayList<PersistentMapEntry<K, V>>, ArrayList<PersistentMapEntry<K, V>>> elemsPair = splitElemsAccordingToPredicate(f);

      return Pair.create(fromStrictlyIncreasingArray(elemsPair.mx1),
                         fromStrictlyIncreasingArray(elemsPair.mx2));
    }

    @Override
    public final <W> Tree<K, W> mapPartial(final Function<V, Optional<W>> f) {
      return mapPartiali((k, v) -> f.apply(v));
    }

    @Override
    public final <W> Tree<K, W> mapPartiali(final BiFunction<K, V, Optional<W>> f) {
      return fromStrictlyIncreasingArray(selectNonEmptyOptionalElements(f));
    }

    @Override
    public final Tree<K, V> insert(final BiFunction<V, V, V> f, final K key, final V value) {
      return blackify(ins(f, key, value));
    }

    private static final class ControlExnNoSuchElement extends Exception {};

    private static final ControlExnNoSuchElement sNoSuchElement = new ControlExnNoSuchElement();

    static final class RemoveMinRes<K extends Comparable<K>, V> {
      public Tree<K, V> mTree;
      public K mMinKey;
      public V mMinValue;
    }

    @Override
    public final Tree<K, V> remove(final K key) {
      try {
        final Ref<Tree<K, V>> treeRef = new Ref<>();
        final RemoveMinRes<K, V> rmRes = new RemoveMinRes<>();
        rem(treeRef, rmRes, key);
        return treeRef.r;
      } catch (ControlExnNoSuchElement ex) {
        return this;
      }
    }

    @Override
    public final Tree<K, V> merge(final BiFunction<V, V, V> f, final PersistentMap<K, V, Tree<K, V>> t) {
      return fromStrictlyIncreasingArray(mergeArrays(f, keyValuePairs(), t.keyValuePairs()));
    }

    private <W> Tree<K, W> mapiImpl(final BiFunction<K, V, W> f) {
      if (isNull()) {
        return createEmptyNode();
      }
      else {
        final Tree<K, W> newLeft = mLeft.mapiImpl(f);
        final Tree<K, W> newRight = mRight.mapiImpl(f);
        final W newVal = f.apply(mKey, mValue);

        return isBlack()
          ? createBlackNode(newLeft, mKey, newVal, newRight)
          : createRedNode(newLeft, mKey, newVal, newRight);
      }
    }

    @Override
    public <W> Tree<K, W> mapi(final BiFunction<K, V, W> f) {
      return mapiImpl(f);
    }

    @Override
    public final <W> Tree<K, W> map(final Function<V, W> f) {
      return mapiImpl((k, v) -> f.apply(v));
    }

    private static <K extends Comparable<K>, V> Optional<PersistentMapEntry<K, V>> lowerPairImpl(final Tree<K, V> tree, final K key) {
      Tree<K, V> t = tree;
      Tree<K, V> candidate = null;

      while (! t.isNull()) {
        final int res = key.compareTo(t.mKey);
        if (res > 0) {
          candidate = t;
          t = t.mRight;
        }
        else if (res < 0) {
          t = t.mLeft;
        }
        else {
          final Optional<PersistentMapEntry<K, V>> p = t.mLeft.maxElementPair();
          if (p.isPresent()) {
            return p;
          }
          else {
            break;
          }
        }
      }

      return Optional.ofNullable(candidate == null ? null : new EntryRef<>(candidate));
    }

    @Override
    public final Optional<PersistentMapEntry<K, V>> lowerPair(final K key) {
      return lowerPairImpl(this, key);
    }

    private static <K extends Comparable<K>, V> Optional<PersistentMapEntry<K, V>> higherPairImpl(final Tree<K, V> tree, final K key) {
      Tree<K, V> t = tree;
      Tree<K, V> candidate = null;

      while (! t.isNull()) {
        final int res = key.compareTo(t.mKey);
        if (res > 0) {
          t = t.mRight;
        }
        else if (res < 0) {
          candidate = t;
          t = t.mLeft;
        }
        else {
          final Optional<PersistentMapEntry<K, V>> p = t.mRight.minElementPair();
          if (p.isPresent()) {
            return p;
          }
          else {
           break;
          }
        }
      }

      return Optional.ofNullable(candidate == null ? null : new EntryRef<>(candidate));
    }

    @Override
    public final Optional<PersistentMapEntry<K, V>> higherPair(final K key) {
      return higherPairImpl(this, key);
    }

    @Override
    public Pair<Boolean, String> verifyMapProperties() {
      return verifyRedBlackProperties(this);
    }

    private Tree<K, V> ins(final BiFunction<V, V, V> f, final K key, final V value) {
      if (isNull()) {
        return createRedNode(this, key, value, this);
      }
      else {
        final int res = key.compareTo(mKey);

        if (isBlack()) {
          if (res < 0) {
            return leftBalance(mLeft.ins(f, key, value), mKey, mValue, mRight);
          }
          else if (res > 0) {
            return rightBalance(mLeft, mKey, mValue, mRight.ins(f, key, value));
          }
          else {
            return createBlackNode(mLeft, key, f.apply(mValue, value), mRight);
          }
        }
        else {
          if (res < 0) {
            return createRedNode(mLeft.ins(f, key, value), mKey, mValue, mRight);
          }
          else if (res > 0) {
            return createRedNode(mLeft, mKey, mValue, mRight.ins(f, key, value));
          }
          else {
            return createRedNode(mLeft, mKey, f.apply(mValue, value), mRight);
          }
        }
      }
    }

    @Override
    public DSTreeNode[] DSgetChildren() {
      return isNull() ? new DSTreeNode[0] : new DSTreeNode[] { mLeft, mRight };
    }

    @Override
    public Object DSgetValue() {
      return isNull() ? "E" : mKey.toString() + "," + mValue.toString();
    }

    @Override
    public Color DSgetColor() {
      return isRed() ? Color.RED : Color.BLACK;
    }

    private boolean rem(final Ref<Tree<K, V>> treeRef, final RemoveMinRes<K, V> rmRes, final K key) throws ControlExnNoSuchElement {
      if (isNull()) {
        throw sNoSuchElement;
      }
      else {
        final int res = key.compareTo(mKey);
        final Tree<K, V> l = mLeft, r = mRight;

        if (isBlack()) {
          if (res < 0) {
            final boolean c = l.rem(treeRef, rmRes, key);
            final Tree<K, V> m = createBlackNode(treeRef.r, mKey, mValue, r);

            if (c) {
              return unbalancedRight(treeRef, m);
            }
            else {
              treeRef.r = m;
              return false;
            }
          }
          else if (res > 0) {
            final boolean c = r.rem(treeRef, rmRes, key);
            final Tree<K, V> m = createBlackNode(l, mKey, mValue, treeRef.r);

            if (c) {
              return unbalancedLeft(treeRef, m);
            }
            else {
              treeRef.r = m;
              return false;
            }
          }
          else {
            if (r.isNull()) {
              return blackifyRem(treeRef, l);
            }
            else {
              final boolean c = removeMin(treeRef, rmRes, r);
              final Tree<K, V> m = createBlackNode(l, rmRes.mMinKey, rmRes.mMinValue, rmRes.mTree);
              
              if (c) {
                return unbalancedLeft(treeRef, m);
              }
              else {
                treeRef.r = m;
                return false;
              }
            }
          }
        }
        else {
          if (res < 0) {
            final boolean c = l.rem(treeRef, rmRes, key);
            final Tree<K, V> m = createRedNode(treeRef.r, mKey, mValue, r);

            if (c) {
              return unbalancedRight(treeRef, m);
            }
            else {
              treeRef.r = m;
              return false;
            }
          }
          else if (res > 0) {
            final boolean c = r.rem(treeRef, rmRes, key);
            final Tree<K, V> m = createRedNode(l, mKey, mValue, treeRef.r);

            if (c) {
              return unbalancedLeft(treeRef, m);
            }
            else {
              treeRef.r = m;
              return false;
            }
          }
          else {
            if (r.isNull()) {
              treeRef.r = l;
              return false;
            }
            else {
              final boolean c = removeMin(treeRef, rmRes, r);
              final Tree<K, V> m = createRedNode(l, rmRes.mMinKey, rmRes.mMinValue, rmRes.mTree);

              if (c) {
                return unbalancedLeft(treeRef, m);
              }
              else {
                treeRef.r = m;
                return false;
              }
            }
          }
        }
      }
    }

// (* [remove_min s = (s',m,b)] extracts the minimum [m] of [s], [s'] being the
//      resulting set, and indicates with [b] whether the black height has
//      decreased *)
//   let rec remove_min = function
//     | Empty ->
//         assert false
//     (* minimum is reached *)
//     | Black (Empty, x, Empty) ->
//         Empty, x, true
//     | Black (Empty, x, Red (l, y, r)) ->
//         Black (l, y, r), x, false
//     | Black (Empty, _, Black _) ->
//         assert false
//     | Red (Empty, x, r) ->
//         r, x, false
//     (* minimum is recursively extracted from [l] *)
//     | Black (l, x, r) ->
//         let l',m,d = remove_min l in
//         let t = Black (l', x, r) in
//         if d then
//           let t,d' = unbalanced_right t in t,m,d'
//         else
//           t, m, false
//     | Red (l, x, r) ->
//         let l',m,d = remove_min l in
//         let t = Red (l', x, r) in
//         if d then
//           let t,d' = unbalanced_right t in t,m,d'
//         else
//           t, m, false

    private static <K extends Comparable<K>, V> boolean removeMin(final Ref<Tree<K, V>> treeRef, final RemoveMinRes<K, V> rmRes, final Tree<K, V> tree) {
      if (tree.isBlack()) {
        final Tree<K, V> l = tree.mLeft, r = tree.mRight;

        if (l.isNull()) {
          if (r.isNull()) {
            rmRes.mTree = l;
            rmRes.mMinKey = tree.mKey;
            rmRes.mMinValue = tree.mValue;
            return true;
          }

          if (r.isRed()) {
            final Tree<K, V> br = r;
            rmRes.mTree = r2b(br);
            rmRes.mMinKey = tree.mKey;
            rmRes.mMinValue = tree.mValue;
            return false;
          }

          if (r.isBlack()) {
            throw new AssertionError("BlackNode encountered at inapropriate place.");
          }
        }

        final boolean cr = removeMin(treeRef, rmRes, l);
        final Tree<K, V> t = createBlackNode(rmRes.mTree, tree.mKey, tree.mValue, r);

        if (cr) {
          final boolean c = unbalancedRight(treeRef, t);
          rmRes.mTree = treeRef.r;
          return c;
        } else {
          rmRes.mTree = t;
          return false;
        }
      }

      if (tree.isRed()) {
        final Tree<K, V> l = tree.mLeft, r = tree.mRight;

        if (l.isNull()) {
          rmRes.mTree = tree.mRight;
          rmRes.mMinKey = tree.mKey;
          rmRes.mMinValue = tree.mValue;
          
          return false;
        } else {
          final boolean cr = removeMin(treeRef, rmRes, l);
          final Tree<K, V> t = createRedNode(rmRes.mTree, tree.mKey, tree.mValue, r);

          if (cr) {
            final boolean c = unbalancedRight(treeRef, t);
            rmRes.mTree = treeRef.r;
            return c;
          } else {
            rmRes.mTree = t;
            return false;
          }
        }
      }

      throw new AssertionError("The tree cannot be empty in this context.");
    }

//   match ins s with
//      | Black _ as s -> s
//      | Red (a, y, b) -> Black (a, y, b)
//      | Empty -> assert false
    private static <K extends Comparable<K>, V> Tree<K, V> blackify(final Tree<K, V> t) {
      return t.isRed() ? r2b(t) : t;
    }

    private static <K extends Comparable<K>, V> boolean blackifyRem(final Ref<Tree<K, V>> treeRef, final Tree<K, V> t) {
      if (t.isRed()) {
        treeRef.r = r2b(t);
        return false;
      }
      else {
        treeRef.r = t;
        return true;
      }
    }

    private static <K extends Comparable<K>, V> Pair<Boolean, String> verifyRedBlackProperties(final Tree<K, V> t) {
      if (! binaryTreePropertyHolds(t)) {
        return Pair.create(false, "Binary Tree property does not hold.");
      }

      if (! redChildrenPropertyHolds(t)) {
        return Pair.create(false, "There are red nodes that have red children.");
      }
      final Pair<Integer, Boolean> p = blackNodeCountPropertyHolds(t);
      if (! p.mx2)  {
        return Pair.create(false, "Number of black nodes to leaves of tree from left and right subtree did not match.");
      }

      if (! heightOfRedBlackTreeConstraintHolds(t)) {
        return Pair.create(false, "The depth of the tree was much larger than the log(size).");
      }

      return Pair.create(true, "Success!");
    }

    private static <K extends Comparable<K>, V> boolean heightOfRedBlackTreeConstraintHolds(final Tree<K, V> t) {
      final int n = t.size();
      final double logSize = Numeric.log((double)(n + 1), 2.0);
      final int height = t.height();

      // In CLR's Algorithms you would find height <= 2 * log(n) but they have a weird definition for height --
      // it considers the tree with 0 nodes to have size 0, and the tree with 1 node to also have height zero.
      // We consider the second to have heigh 1.  Basically we count the edges traversed to reach a leaf which
      // makes a lot more sense.  That's why below in order to have the same condition as CLR we subtract 1 from height.
      return ((double) (height - 1)) <= 2.0 * logSize;
    }
    
    private static <K extends Comparable<K>, V> boolean redChildrenPropertyHolds(final Tree<K, V> t) {
      if (t.isNull()) {
        return true;
      }
      
      if (t.isBlack()) {
        final Tree<K, V> l = t.mLeft, r = t.mRight;
        return redChildrenPropertyHolds(l) && redChildrenPropertyHolds(r);
      }

      if (! t.isRed()) {
        throw new AssertionError("Internal error during verification.");
      }

      final Tree<K, V> l = t.mLeft, r = t.mRight;
      if (l.isRed() || r.isRed()) {
        return false;
      }
      else {
        return redChildrenPropertyHolds(l) && redChildrenPropertyHolds(r);
      }
    }

    private static <K extends Comparable<K>, V> Pair<Integer, Boolean> blackNodeCountPropertyHolds(final Tree<K, V> t) {
      if (t.isNull()) {
        return Pair.create(1, true);
      }
      {
        if (t.isBlack()) {
          final Pair<Integer, Boolean> pleft  = blackNodeCountPropertyHolds(t.mLeft);
          final Pair<Integer, Boolean> pright = blackNodeCountPropertyHolds(t.mRight);

          return Pair.create(pleft.mx1 + 1, pleft.mx2 && pright.mx2 && pleft.mx1.equals(pright.mx1));
        }
      }
      {
        final Pair<Integer, Boolean> pleft  = blackNodeCountPropertyHolds(t.mLeft);
        final Pair<Integer, Boolean> pright = blackNodeCountPropertyHolds(t.mRight);
        
        return Pair.create(pleft.mx1, pleft.mx2 && pright.mx2 && pleft.mx1.equals(pright.mx1));
      }
    }

    private static <K extends Comparable<K>, V> boolean binaryTreePropertyHolds(final Tree<K, V> t) {
      return ArrayUtils.isStrictlyIncreasing(t.keys());
    }

    private static int computeRedDepth(final int size) {
      return Numeric.ilog(size + 1);
    }

    private static final class InitFromArrayWorker<K extends Comparable<K>, V> {
      private final ArrayList<PersistentMapEntry<K, V>> mVector;
      private final boolean mIncreasing;
      private final int mRedLevel;

      public InitFromArrayWorker(final ArrayList<PersistentMapEntry<K, V>> vector,
                                 final boolean increasing,
                                 final int redLevel) {
        mVector = vector;
        mIncreasing = increasing;
        mRedLevel = redLevel;
      }

      private Tree<K, V> workerFunc(final int left, final int right, final int depth) {
        if (left > right)
          return createEmptyNode();

        final int mid = (left + right) >>> 1;
        final PersistentMapEntry<K, V> p = mVector.get(mid);

        final int newDepth = depth + 1;
        Tree<K, V> lt = workerFunc(left, mid - 1, newDepth);
        Tree<K, V> rt = workerFunc(mid + 1, right, newDepth);

        if (! mIncreasing) {
          Tree<K, V> t = lt;
          lt = rt;
          rt = t;
        }

        return depth == mRedLevel
                ? createRedNode(lt, p.getKey(), p.getValue(), rt)
                : createBlackNode(lt, p.getKey(), p.getValue(), rt);
      }

      public final Tree<K, V> doIt() {
        return workerFunc(0, mVector.size() - 1, 0);
      }
    }
  }

  public static final class RedBlackTreeFactory2<K extends Comparable<K>, V> implements PersistentMapFactory<K, V, Tree<K, V>> {
    @Override
    public String getMapName() {
      return "RedBlackTreeMap";
    }

    @Override
    public Tree<K, V> empty() {
      return Tree.empty();
    }

    @Override
    public Tree<K, V> singleton(final K key, final V value) {
      return Tree.singleton(key, value);
    }

    @Override
    public Tree<K, V> fromStrictlyIncreasingStream(final Stream<PersistentMapEntry<K, V>> stream) {
      return Tree.fromStrictlyIncreasingStream(stream);
    }

    @Override
    public Tree<K, V> fromStrictlyDecreasingStream(final Stream<PersistentMapEntry<K, V>> stream) {
      return Tree.fromStrictlyDecreasingStream(stream);
    }

    @Override
    public Tree<K, V> fromArray(final ArrayList<PersistentMapEntry<K, V>> v) {
      return Tree.fromArray(v);
    }

    @Override
    public Tree<K, V> fromStrictlyIncreasingArray(final ArrayList<PersistentMapEntry<K, V>> v) {
      return Tree.fromStrictlyIncreasingArray(v);
    }

    @Override
    public Tree<K, V> fromStrictlyDecreasingArray(final ArrayList<PersistentMapEntry<K, V>> v) {
      return Tree.fromStrictlyDecreasingArray(v);
    }

    @Override
    public Tree<K, V> fromStream(final Stream<PersistentMapEntry<K, V>> stream) {
      return Tree.fromStream(stream);
    }
  }

  private static final RedBlackTreeFactory2<? extends Comparable<?>, ?> sRedBlackTreeFactory = new RedBlackTreeFactory2<>();

  @SuppressWarnings("unchecked")
  public static <K extends Comparable<K>, V> RedBlackTreeFactory2<K, V> makeFactory() {
    return (RedBlackTreeFactory2<K, V>) sRedBlackTreeFactory;
  }
}
