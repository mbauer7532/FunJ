/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// https://github.com/bmeurer/ocaml-rbtrees/blob/master/src/rbmap.ml
// https://www.lri.fr/~filliatr/ftp/ocaml/ds/rbset.ml.html

package DataStructures;

import DataStructures.PersistentMapBase.EntryRef;
import DataStructures.TuplesModule.Pair;
import Utils.ArrayUtils;
import Utils.Functionals;
import Utils.Functionals.TriFunction;
import Utils.Numeric;
import Utils.Ref;
import java.awt.Color;
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
public class RedBlackTreeModule {
  public static abstract class Tree<K extends Comparable<K>, V>
                                extends PersistentMapBase<K, V, Tree<K, V>> {
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

    protected static final ControlExnNoSuchElement sNoSuchElement = new ControlExnNoSuchElement();

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

    @Override
    public abstract <W> Tree<K, W> mapi(final BiFunction<K, V, W> f);

    @Override
    public final <W> Tree<K, W> map(final Function<V, W> f) {
      return mapi((k, v) -> f.apply(v));
    }

    @Override
    public final Optional<PersistentMapEntry<K, V>> lowerPair(final K key) {
      return RedBlackTreeModule.lowerPair(this, key);
    }

    @Override
    public final Optional<PersistentMapEntry<K, V>> higherPair(final K key) {
      return RedBlackTreeModule.higherPair(this, key);
    }

    @Override
    public Pair<Boolean, String> verifyMapProperties() {
      return verifyRedBlackProperties(this);
    }

    boolean isRed() { return false; }
    boolean isBlack() { return false; }

    RedNode<K, V> asRed() { return null; }
    BlackNode<K, V> asBlack() { return null; }

    abstract Tree<K, V> ins(final BiFunction<V, V, V> f, final K key, final V value);
    abstract boolean rem(final Ref<Tree<K, V>> treeRef, final RemoveMinRes<K, V> rmRes, final K key) throws ControlExnNoSuchElement;
  }

  private static final class EmptyNode<K extends Comparable<K>, V> extends Tree<K, V> {
    private static final EmptyNode<? extends Comparable<?>, ?> sEmptyNode = new EmptyNode<>();

    @SuppressWarnings("unchecked")
    public static <K extends Comparable<K>, V> EmptyNode<K, V> create() {
      return (EmptyNode<K, V>) sEmptyNode;
    }

    @Override
    public K getKey() {
      throw new AssertionError("The empty tree has no key.");
    }

    @Override
    public V getValue() {
      throw new AssertionError("The empty tree has no value.");
    }

    @Override
    public final boolean isEmpty() {
      return true;
    }

    @Override
    public Tree<K, V> ins(final BiFunction<V, V, V> f, final K key, final V value) {
      final Tree<K, V> e = create();
      return RedNode.create(e, key, value, e);
    }

    @Override
    boolean rem(final Ref<Tree<K, V>> treeRef, final RemoveMinRes<K, V> rmRes, final K key) throws Tree.ControlExnNoSuchElement {
      // This exception is used for control purposes.
      // When removing non-existent elements we simply return the same input tree
      // no new allocations take place.  The alternative implementation that 
      // copies the path is: return Pair.create(this, false);
      throw sNoSuchElement;
    }

    @Override
    public Optional<V> get(final K key) {
      return Optional.empty();
    }

    @Override
    public boolean containsValue(final V value) {
      return false;
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
    public void appi(final BiConsumer<K, V> f) {
      return;
    }

    @Override
    public void appEntry(final Consumer<PersistentMapBase<K, V, Tree<K, V>>> f) {
      return;
    }

    @Override
    public <W> Tree<K, W> mapi(BiFunction<K, V, W> f) {
      return create();
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
    public Optional<PersistentMapEntry<K, V>> minElementPair() {
      return Optional.empty();
    }

    @Override
    public Optional<PersistentMapEntry<K, V>> maxElementPair() {
      return Optional.empty();
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
      return Color.BLACK;
    }
  }

  private static abstract class Node<K extends Comparable<K>, V> extends Tree<K, V> {
    private Node(final K key, final V value, final Tree<K, V> left, final Tree<K, V> right) {
      mKey = key;
      mValue = value;
      mLeft = left;
      mRight = right;
    }

    protected final K mKey;
    protected final V mValue;
    protected final Tree<K, V> mLeft;
    protected final Tree<K, V> mRight;

    @Override
    public K getKey() {
      return mKey;
    }

    @Override
    public V getValue() {
      return mValue;
    }

    @Override
    public final boolean isEmpty() {
      return false;
    }

    @Override
    public Optional<V> get(final K key) {
      final int res = key.compareTo(mKey);
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
    public boolean containsValue(final V value) {
      return mValue.equals(value)
              || mLeft.containsValue(value)
              || mRight.containsValue(value);
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
    public void appi(final BiConsumer<K, V> f) {
      mLeft.appi(f);
      f.accept(mKey, mValue);
      mRight.appi(f);

      return;
    }

    @Override
    public void appEntry(final Consumer<PersistentMapBase<K, V, Tree<K, V>>> f) {
      mLeft.appEntry(f);
      f.accept(this);
      mRight.appEntry(f);

      return;
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
    public Optional<PersistentMapEntry<K, V>> minElementPair() {
      if (mLeft.isEmpty()) {
        return Optional.of(new EntryRef<>(this));
      }
      else {
        return mLeft.minElementPair();
      }
    }

    @Override
    public Optional<PersistentMapEntry<K, V>> maxElementPair() {
      if (mRight.isEmpty()) {
        return Optional.of(new EntryRef<>(this));
      }
      else {
        return mRight.maxElementPair();
      }
    }

    @Override
    public DSTreeNode[] DSgetChildren() {
      return new DSTreeNode[] { mLeft, mRight };
    }

    @Override
    public Object DSgetValue() {
      return mKey.toString() + "," + mValue.toString();
    }

//    let lbalance x1 x2 x3 = match x1, x2, x3 with
//    | Red (Red (a,x,b), y, c), z, d ->
//        Red (Black (a,x,b), y, Black (c,z,d))
//    | Red (a, x, Red (b,y,c)), z, d ->
//        Red (Black (a,x,b), y, Black (c,z,d))
//    | a,x,b ->
//        Black (a,x,b)
//
    protected static <K extends Comparable<K>, V> Tree<K, V> leftBalance(
            final Tree<K, V> left,
            final K key,
            final V value,
            final Tree<K, V> right) {
      final RedNode<K, V> l;

      if ((l = left.asRed()) != null) {
        final RedNode<K, V> ll, lr;

        if ((ll = l.mLeft.asRed()) != null) {
          return RedNode.create(
                  r2b(ll),
                  l.mKey,
                  l.mValue,
                  BlackNode.create(l.mRight, key, value, right));
        }
        else if ((lr = l.mRight.asRed()) != null) {
          return RedNode.create(
                  BlackNode.create(l.mLeft, l.mKey, l.mValue, lr.mLeft),
                  lr.mKey,
                  lr.mValue,
                  BlackNode.create(lr.mRight, key, value, right));
        }
      }
 
      return BlackNode.create(left, key, value, right);
    }

//  let rbalance x1 x2 x3 = match x1, x2, x3 with
//    | a, x, Red (Red (b,y,c), z, d) ->
//        Red (Black (a,x,b), y, Black (c,z,d))
//    | a, x, Red (b, y, Red (c,z,d)) ->
//        Red (Black (a,x,b), y, Black (c,z,d))
//    | a,x,b ->
//        Black (a,x,b)

    protected static <K extends Comparable<K>, V> Tree<K, V> rightBalance(
            final Tree<K, V> left,
            final K key,
            final V value,
            final Tree<K, V> right) {
      final RedNode<K, V> r = right.asRed();

      if (r != null) {
        final RedNode<K, V> rl, rr;

        if ((rl = r.mLeft.asRed()) != null) {
          return RedNode.create(
                  BlackNode.create(left, key, value, rl.mLeft),
                  rl.mKey,
                  rl.mValue,
                  BlackNode.create(rl.mRight, r.mKey, r.mValue, r.mRight));
        }
        else if ((rr = r.mRight.asRed()) != null) {
          return RedNode.create(
                  BlackNode.create(left, key, value, r.mLeft),
                  r.mKey,
                  r.mValue,
                  r2b(rr));
        }
      }

      return BlackNode.create(left, key, value, right);
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

    protected static <K extends Comparable<K>, V> boolean unbalancedLeft(final Ref<Tree<K, V>> treeRef, final Tree<K, V> t) {
      final RedNode<K, V> red;
      final BlackNode<K, V> black;

      if ((red = t.asRed()) != null) {
        final BlackNode<K, V> rb = red.mLeft.asBlack();
        if (rb != null) {
          treeRef.r = leftBalance(b2r(rb), red.mKey, red.mValue, red.mRight);
          return false;
        }
      }
      else if ((black = t.asBlack()) != null) {
        final Tree<K, V> left = black.mLeft;
        BlackNode<K, V> bb;
        final RedNode<K, V> br;

        if ((bb = left.asBlack()) != null) {
          treeRef.r = leftBalance(b2r(bb), black.mKey, black.mValue, black.mRight);
          return true;
        }
        else if ((br = left.asRed()) != null && (bb = br.mRight.asBlack()) != null) {
          treeRef.r = BlackNode.create(br.mLeft, br.mKey, br.mValue,
                                       leftBalance(b2r(bb), black.mKey, black.mValue, black.mRight));
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

    protected static <K extends Comparable<K>, V> boolean unbalancedRight(final Ref<Tree<K, V>> treeRef, final Tree<K, V> t) {
      final RedNode<K, V> red;
      final BlackNode<K, V> black;

      if ((red = t.asRed()) != null) {
        final BlackNode<K, V> rb = red.mRight.asBlack();

        if (rb != null) {
          treeRef.r = rightBalance(red.mLeft, red.mKey, red.mValue, b2r(rb));
          return false;
        }
      }
      else if ((black = t.asBlack()) != null) {
        final Tree<K, V> right = black.mRight;
        BlackNode<K, V> bb;
        final RedNode<K, V> br;

        if ((bb = right.asBlack()) != null) {
          treeRef.r = rightBalance(black.mLeft, black.mKey, black.mValue, b2r(bb));
          return true;
        }
        else if ((br = right.asRed()) != null && (bb = br.mLeft.asBlack()) != null) {
          treeRef.r = BlackNode.create(rightBalance(black.mLeft, black.mKey, black.mValue, b2r(bb)),
                                       br.mKey, br.mValue, br.mRight);
          return false;
        }
      }

      throw new AssertionError("Should never get here.");
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

    protected static <K extends Comparable<K>, V> boolean removeMin(final Ref<Tree<K, V>> treeRef, final RemoveMinRes<K, V> rmRes, final Tree<K, V> tree) {
      final BlackNode<K, V> black = tree.asBlack();
      if (black != null) {
        final Tree<K, V> l = black.mLeft, r = black.mRight;

        if (l.isEmpty()) {
          if (r.isEmpty()) {
            rmRes.mTree = l;
            rmRes.mMinKey = black.mKey;
            rmRes.mMinValue = black.mValue;
            return true;
          }

          final RedNode<K, V> br = r.asRed();
          if (br != null) {
            rmRes.mTree = r2b(br);
            rmRes.mMinKey = black.mKey;
            rmRes.mMinValue = black.mValue;
            return false;
          }

          final BlackNode<K, V> bb = r.asBlack();
          if (bb != null) {
            throw new AssertionError("BlackNode encountered at inapropriate place.");
          }
        }

        final boolean cr = removeMin(treeRef, rmRes, l);
        final Tree<K, V> t = BlackNode.create(rmRes.mTree, black.mKey, black.mValue, r);

        if (cr) {
          final boolean c = unbalancedRight(treeRef, t);
          rmRes.mTree = treeRef.r;
          return c;
        } else {
          rmRes.mTree = t;
          return false;
        }
      }

      final RedNode<K, V> red = tree.asRed();
      if (red != null) {
        final Tree<K, V> l = red.mLeft, r = red.mRight;

        if (l.isEmpty()) {
          rmRes.mTree = red.mRight;
          rmRes.mMinKey = red.mKey;
          rmRes.mMinValue = red.mValue;
          
          return false;
        } else {
          final boolean cr = removeMin(treeRef, rmRes, l);
          final Tree<K, V> t = RedNode.create(rmRes.mTree, red.mKey, red.mValue, r);

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
    
    public static <K extends Comparable<K>, V> RedNode<K, V> b2r(final BlackNode<K, V> b) {
      return RedNode.create(b.mLeft, b.mKey, b.mValue, b.mRight);
    }

    public static <K extends Comparable<K>, V> BlackNode<K, V> r2b(final RedNode<K, V> r) {
      return BlackNode.create(r.mLeft, r.mKey, r.mValue, r.mRight);
    }
  }

  private static final class RedNode<K extends Comparable<K>, V> extends Node<K, V> {
    private RedNode(final Tree<K, V> left, final K key, final V value, final Tree<K, V> right) {
      super(key, value, left, right);
    }

    public static <K extends Comparable<K>, V> RedNode<K, V> create(
            final Tree<K, V> left,
            final K key,
            final V value,
            final Tree<K, V> right) {
      return new RedNode<>(left, key, value, right);
    }

    @Override
    final boolean isRed() { return true; }

    @Override
    final RedNode<K, V> asRed() { return this; }

//    let add x s =
//    let rec ins = function
//      | Empty ->
//          Red (Empty, x, Empty)
//      | Red (a, y, b) as s ->
//          let c = Ord.compare x y in
//          if c < 0 then Red (ins a, y, b)
//          else if c > 0 then Red (a, y, ins b)
//          else s
//      | Black (a, y, b) as s ->
//          let c = Ord.compare x y in
//          if c < 0 then lbalance (ins a) y b
//          else if c > 0 then rbalance a y (ins b)
//          else s
//    in
//    match ins s with
//      | Black _ as s -> s
//      | Red (a, y, b) -> Black (a, y, b)
//      | Empty -> assert false

    @Override
    Tree<K, V> ins(final BiFunction<V, V, V> f, final K key, final V value) {
      final int res = key.compareTo(mKey);

      if (res < 0) {
        return create(mLeft.ins(f, key, value), mKey, mValue, mRight);
      }
      else if (res > 0) {
        return create(mLeft, mKey, mValue, mRight.ins(f, key, value));
      }
      else {
        return create(mLeft, mKey, f.apply(mValue, value), mRight);
      }
    }

//    let remove k m =
//    let rec remove_aux = function
//      | Empty ->
//          Empty, false
//      | Black(l, kx, x, r) ->
//          let c = Ord.compare k kx in
//            if c < 0 then
//              let l, d = remove_aux l in
//              let m = Black(l, kx, x, r) in
//                if d then unbalanced_right m else m, false
//            else if c > 0 then
//              let r, d = remove_aux r in
//              let m = Black(l, kx, x, r) in
//                if d then unbalanced_left m else m, false
//            else
//              begin match r with
//                | Empty ->
//                    blackify l
//                | _ ->
//                    let r, kx, x, d = remove_min r in
//                    let m = Black(l, kx, x, r) in
//                      if d then unbalanced_left m else m, false
//              end
//      | Red(l, kx, x, r) ->
//          let c = Ord.compare k kx in
//            if c < 0 then
//              let l, d = remove_aux l in
//              let m = Red(l, kx, x, r) in
//                if d then unbalanced_right m else m, false
//            else if c > 0 then
//              let r, d = remove_aux r in
//              let m = Red(l, kx, x, r) in
//                if d then unbalanced_left m else m, false
//            else
//              begin match r with
//                | Empty ->
//                    l, false
//                | _ ->
//                    let r, kx, x, d = remove_min r in
//                    let m = Red(l, kx, x, r) in
//                      if d then unbalanced_left m else m, false
//              end
//    in fst (remove_aux m)

    @Override
   boolean rem(final Ref<Tree<K, V>> treeRef, final RemoveMinRes<K, V> rmRes, final K key) throws Tree.ControlExnNoSuchElement {
      final int res = key.compareTo(mKey);
      final Tree<K, V> l = mLeft, r = mRight;

      if (res < 0) {
        final boolean c = l.rem(treeRef, rmRes, key);
        final RedNode<K, V> m = RedNode.create(treeRef.r, mKey, mValue, r);

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
        final RedNode<K, V> m = RedNode.create(l, mKey, mValue, treeRef.r);

        if (c) {
          return unbalancedLeft(treeRef, m);
        }
        else {
          treeRef.r = m;
          return false;
        }
      }
      else {
        if (r.isEmpty()) {
          treeRef.r = l;
          return false;
        }
        else {
          final boolean c = removeMin(treeRef, rmRes, r);
          final RedNode<K, V> m = RedNode.create(l, rmRes.mMinKey, rmRes.mMinValue, rmRes.mTree);

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

    @Override
    public <W> Tree<K, W> mapi(final BiFunction<K, V, W> f) {
      return create(mLeft.mapi(f), mKey, f.apply(mKey, mValue), mRight.mapi(f));
    }

    @Override
    public Color DSgetColor() {
      return Color.RED;
    }
  }

  private static final class BlackNode<K extends Comparable<K>, V> extends Node<K, V> {
    private BlackNode(final Tree<K, V> left, final K key, final V value, final Tree<K, V> right) {
      super(key, value, left, right);
    }

    public static <K extends Comparable<K>, V> BlackNode<K, V> create(final Tree<K, V> left, final K key, final V value, final Tree<K, V> right) {
      return new BlackNode<>(left, key, value, right);
    }

    @Override
    final boolean isBlack() { return true; }

    @Override
    final BlackNode<K, V> asBlack() { return this; }

//    let add x s =
//    let rec ins = function
//      | Empty ->
//          Red (Empty, x, Empty)
//      | Red (a, y, b) as s ->
//          let c = Ord.compare x y in
//          if c < 0 then Red (ins a, y, b)
//          else if c > 0 then Red (a, y, ins b)
//          else s
//      | Black (a, y, b) as s ->
//          let c = Ord.compare x y in
//          if c < 0 then lbalance (ins a) y b
//          else if c > 0 then rbalance a y (ins b)
//          else s
//    in
//    match ins s with
//      | Black _ as s -> s
//      | Red (a, y, b) -> Black (a, y, b)
//      | Empty -> assert false

    @Override
    Tree<K, V> ins(final BiFunction<V, V, V> f, final K key, final V value) {
      final int res = key.compareTo(mKey);

      if (res < 0) {
        return leftBalance(mLeft.ins(f, key, value), mKey, mValue, mRight);
      }
      else if (res > 0) {
        return rightBalance(mLeft, mKey, mValue, mRight.ins(f, key, value));
      }
      else {
        return create(mLeft, key, f.apply(mValue, value), mRight);
      }
    }

//    let remove k m =
//    let rec remove_aux = function
//      | Empty ->
//          Empty, false
//      | Black(l, kx, x, r) ->
//          let c = Ord.compare k kx in
//            if c < 0 then
//              let l, d = remove_aux l in
//              let m = Black(l, kx, x, r) in
//                if d then unbalanced_right m else m, false
//            else if c > 0 then
//              let r, d = remove_aux r in
//              let m = Black(l, kx, x, r) in
//                if d then unbalanced_left m else m, false
//            else
//              begin match r with
//                | Empty ->
//                    blackify l
//                | _ ->
//                    let r, kx, x, d = remove_min r in
//                    let m = Black(l, kx, x, r) in
//                      if d then unbalanced_left m else m, false
//              end

    @Override
    boolean rem(final Ref<Tree<K, V>> treeRef, final RemoveMinRes<K, V> rmRes, final K key) throws Tree.ControlExnNoSuchElement {
      final int res = key.compareTo(mKey);
      final Tree<K, V> l = mLeft, r = mRight;

      if (res < 0) {
        final boolean c = l.rem(treeRef, rmRes, key);
        final BlackNode<K, V> m = BlackNode.create(treeRef.r, mKey, mValue, r);

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
        final BlackNode<K, V> m = BlackNode.create(l, mKey, mValue, treeRef.r);

        if (c) {
          return unbalancedLeft(treeRef, m);
        }
        else {
          treeRef.r = m;
          return false;
        }
      }
      else {
        if (r.isEmpty()) {
          return blackifyRem(treeRef, l);
        }
        else {
          final boolean c = removeMin(treeRef, rmRes, r);
          final BlackNode<K, V> m = BlackNode.create(l, rmRes.mMinKey, rmRes.mMinValue, rmRes.mTree);

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

    @Override
    public <W> Tree<K, W> mapi(final BiFunction<K, V, W> f) {
      return create(mLeft.mapi(f), mKey, f.apply(mKey, mValue), mRight.mapi(f));
    }

    @Override
    public Color DSgetColor() {
      return Color.BLACK;
    }
  }

//   match ins s with
//      | Black _ as s -> s
//      | Red (a, y, b) -> Black (a, y, b)
//      | Empty -> assert false
  static <K extends Comparable<K>, V> Tree<K, V> blackify(final Tree<K, V> t) {
    final RedNode<K, V> red = t.asRed();
    return (red != null) ? RedNode.r2b(red) : t;
  }

  static <K extends Comparable<K>, V> boolean blackifyRem(final Ref<Tree<K, V>> treeRef, final Tree<K, V> t) {
    final RedNode<K, V> red = t.asRed();
    if (red != null) {
      treeRef.r = RedNode.r2b(red);
      return false;
    }
    else {
      treeRef.r = t;
      return true;
    }
  }

  static <K extends Comparable<K>, V> Pair<Boolean, String> verifyRedBlackProperties(final Tree<K, V> t) {
    if (! binaryTreePropertyHolds(t)) {
      return Pair.create(false, "Binary Tree property does not hold.");
    }

    if (! redChildrenPropertyHolds(t)) {
      return Pair.create(false, "There are red nodes that have red children.");
    }
    final Pair<Integer, Boolean> p = blackNodeCountPropertyHolds(t);
    if (! p.mx2)  {
      return Pair.create(false, "Number of black nodes to leaves of tree "
                                + "from left and right subtree did not match.");
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
    if (t.isEmpty()) {
      return true;
    }

    final BlackNode<K, V> black = t.asBlack();
    if (black != null) {
      final Tree<K, V> l = black.mLeft, r = black.mRight;
      return redChildrenPropertyHolds(l) && redChildrenPropertyHolds(r);
    }

    final RedNode<K, V> red = t.asRed();
    if (red == null) {
      throw new AssertionError("Internal error during verification.");
    }

    final Tree<K, V> l = red.mLeft, r = red.mRight;
    if (l.isRed() || r.isRed()) {
      return false;
    }
    else {
      return redChildrenPropertyHolds(l) && redChildrenPropertyHolds(r);
    }
  }

  private static <K extends Comparable<K>, V> Pair<Integer, Boolean> blackNodeCountPropertyHolds(final Tree<K, V> t) {
    if (t.isEmpty()) {
      return Pair.create(1, true);
    }
    {
      final BlackNode<K, V> b = t.asBlack();
      if (b != null) {
        final Pair<Integer, Boolean> pleft  = blackNodeCountPropertyHolds(b.mLeft);
        final Pair<Integer, Boolean> pright = blackNodeCountPropertyHolds(b.mRight);

        return Pair.create(pleft.mx1 + 1, pleft.mx2 && pright.mx2 && pleft.mx1.equals(pright.mx1));
      }
    }
    {
      final RedNode<K, V> r = t.asRed();
      final Pair<Integer, Boolean> pleft  = blackNodeCountPropertyHolds(r.mLeft);
      final Pair<Integer, Boolean> pright = blackNodeCountPropertyHolds(r.mRight);
      
      return Pair.create(pleft.mx1, pleft.mx2 && pright.mx2 && pleft.mx1.equals(pright.mx1));
    }
  }

  private static <K extends Comparable<K>, V> boolean binaryTreePropertyHolds(final Tree<K, V> t) {
    return ArrayUtils.isStrictlyIncreasing(t.keys());
  }

  // Public interface
  public static <K extends Comparable<K>, V> Tree<K, V> empty() {
    return EmptyNode.create();
  }

  public static <K extends Comparable<K>, V> Tree<K, V> singleton(final K key, final V value) {
    final Tree<K, V> e = empty();
    return BlackNode.create(e, key, value, e);
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
        return empty();

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
               ? RedNode.create(lt, p.getKey(), p.getValue(), rt)
               : BlackNode.create(lt, p.getKey(), p.getValue(), rt);
    }

    public final Tree<K, V> doIt() {
      return workerFunc(0, mVector.size() - 1, 0);
    }
  }

  private static int computeRedDepth(final int size) {
    return Numeric.ilog(size + 1);
  }

  static <K extends Comparable<K>, V> Optional<PersistentMapEntry<K, V>> lowerPair(final Tree<K, V> t, final K key) {
    Tree<K, V> tree = t;
    Node<K, V> candidate = null;

    while (! tree.isEmpty()) {
      final Node<K, V> n = (Node<K, V>) tree;
      int res = key.compareTo(n.mKey);
      if (res > 0) {
        tree = n.mRight;
        candidate = n;
      }
      else if (res < 0) {
        tree = n.mLeft;
      }
      else {
         final Optional<PersistentMapEntry<K, V>> p = n.mLeft.maxElementPair();
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

  static <K extends Comparable<K>, V> Optional<PersistentMapEntry<K, V>> higherPair(final Tree<K, V> t, final K key) {
    Tree<K, V> tree = t;
    Node<K, V> candidate = null;

    while (! tree.isEmpty()) {
      final Node<K, V> n = (Node<K, V>) tree;
      int res = key.compareTo(n.mKey);
      if (res > 0) {
        tree = n.mRight;
      }
      else if (res < 0) {
        tree = n.mLeft;
        candidate = n;
      }
      else {
        final Optional<PersistentMapEntry<K, V>> p = n.mRight.minElementPair();
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
                         (t, p) -> t.insert(p.getKey(), p.getValue()),
                         Functionals::functionShouldNotBeCalled);
  }

  public static <K extends Comparable<K>, V> Tree<K, V> fromArray(final ArrayList<PersistentMapEntry<K, V>> v) {
    return fromStream(v.stream());
  }

  private static final class RemoveMinRes<K extends Comparable<K>, V> {
    public Tree<K, V> mTree;
    public K mMinKey;
    public V mMinValue;
  }

  public static final class RedBlackTreeFactory<K extends Comparable<K>, V> implements PersistentMapFactory<K, V, Tree<K, V>> {
    @Override
    public String getMapName() {
      return "RedBlackTreeMap";
    }

    @Override
    public Tree<K, V> empty() {
      return RedBlackTreeModule.empty();
    }

    @Override
    public Tree<K, V> singleton(final K key, final V value) {
      return RedBlackTreeModule.singleton(key, value);
    }

    @Override
    public Tree<K, V> fromStrictlyIncreasingStream(final Stream<PersistentMapEntry<K, V>> stream) {
      return RedBlackTreeModule.fromStrictlyIncreasingStream(stream);
    }

    @Override
    public Tree<K, V> fromStrictlyDecreasingStream(final Stream<PersistentMapEntry<K, V>> stream) {
      return RedBlackTreeModule.fromStrictlyDecreasingStream(stream);
    }

    @Override
    public Tree<K, V> fromArray(final ArrayList<PersistentMapEntry<K, V>> v) {
      return RedBlackTreeModule.fromArray(v);
    }

    @Override
    public Tree<K, V> fromStrictlyIncreasingArray(final ArrayList<PersistentMapEntry<K, V>> v) {
      return RedBlackTreeModule.fromStrictlyIncreasingArray(v);
    }

    @Override
    public Tree<K, V> fromStrictlyDecreasingArray(final ArrayList<PersistentMapEntry<K, V>> v) {
      return RedBlackTreeModule.fromStrictlyDecreasingArray(v);
    }

    @Override
    public Tree<K, V> fromStream(final Stream<PersistentMapEntry<K, V>> stream) {
      return RedBlackTreeModule.fromStream(stream);
    }
  }

  private static final RedBlackTreeFactory<? extends Comparable<?>, ?> sRedBlackTreeFactory = new RedBlackTreeFactory<>();

  @SuppressWarnings("unchecked")
  public static <K extends Comparable<K>, V> RedBlackTreeFactory<K, V> makeFactory() {
    return (RedBlackTreeFactory<K, V>) sRedBlackTreeFactory;
  }
}
