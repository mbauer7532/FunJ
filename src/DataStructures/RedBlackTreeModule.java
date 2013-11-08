/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// https://github.com/bmeurer/ocaml-rbtrees/blob/master/src/rbmap.ml
// https://www.lri.fr/~filliatr/ftp/ocaml/ds/rbset.ml.html

package DataStructures;

import DataStructures.TuplesModule.Pair;
import DataStructures.TuplesModule.Tuple4;
import Utils.Functionals.TriFunction;
import java.awt.Color;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.StructureGraphic.v1.DSTreeNode;

/**
 *
 * @author Neo
 */
public class RedBlackTreeModule {
  public static abstract class Tree<K extends Comparable<K>, V> implements DSTreeNode {
    public final boolean contains(final K key) {
      return get(key).isPresent();
    }

    public final V getWithDefault(final K key, final V def) {
      return get(key).orElse(def);
    }

    public final <W> Tree<K, W> map(final Function<V, W> f) {
      return mapi((k, v) -> f.apply(v));
    }

    public final void app(final Consumer<V> f) {
      appi((k, v) -> f.accept(v));

      return;
    }

    public final <W> W foldl(final BiFunction<V, W, W> f, final W w) {
      return foldli((k, v, z) -> f.apply(v, z), w);
    }

    public final <W> W foldr(final BiFunction<V, W, W> f, final W w) {
      return foldri((k, v, z) -> f.apply(v, z), w);
    }

    public final Tree<K, V> filter(final Predicate<V> f) {
      return filteri((k, v) -> f.test(v));
    }

    public final <W> Tree<K, W> mapPartial(final Function<V, Optional<W>> f) {
      return mapPartiali((k, v) -> f.apply(v));
    }

    public final Tree<K, V> insert(final BiFunction<V, V, V> f, final K key, final V value) {
      return blackify(ins(f, key, value));
    }

    public final Tree<K, V> insert(final K key, final V value) {
      return insert(Tree::chooseExisting, key, value);
    }

    public final Tree<K, V> remove(final K key) {
      try {
        return rem(key).mx1;
      } catch (ControlExnNoSuchElement ex) {
        return this;
      }
    }

    public abstract boolean isEmpty();
    public abstract Optional<V> get(final K key);
    public abstract int size();
    public abstract int depth();
    public abstract void appi(final BiConsumer<K, V> f);
    public abstract <W> Tree<K, W> mapi(final BiFunction<K, V, W> f);
    public abstract <W> Tree<K, W> mapPartiali(final BiFunction<K, V, Optional<W>> f);
    public abstract <W> W foldli(final TriFunction<K, V, W, W> f, final W w);
    public abstract <W> W foldri(final TriFunction<K, V, W, W> f, final W w);
    public abstract Tree<K, V> filteri(final BiPredicate<K, V> f);
    public abstract Tree<K, V> merge(final BiFunction<V, V, V> f, final Tree<K, V> t);

    boolean isRed() { return false; }
    boolean isBlack() { return false; }

    RedNode<K, V> asRed() { return null; }
    BlackNode<K, V> asBlack() { return null; }

    abstract Tree<K, V> ins(final BiFunction<V, V, V> f, final K key, final V value);
    abstract Pair<Tree<K, V>, Boolean> rem(final K key) throws ControlExnNoSuchElement;

    private static <K extends Comparable<K>, V> V chooseExisting(final V existingElem, final V newElem) {
      return existingElem;
    }
  }

  private static final class EmptyNode<K extends Comparable<K>, V> extends Tree<K, V> {
    private static final EmptyNode<? extends Comparable<?>, ?> sEmptyNode = new EmptyNode<>();

    @SuppressWarnings("unchecked")
    public static <K extends Comparable<K>, V> EmptyNode<K, V> create() {
      return (EmptyNode<K, V>) sEmptyNode;
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
    Pair<Tree<K, V>, Boolean> rem(final K key) throws ControlExnNoSuchElement {
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
    public int size() {
      return 0;
    }

    @Override
    public int depth() {
      return 0;
    }

    @Override
    public void appi(final BiConsumer<K, V> f) {
      return;
    }

    @Override
    public <W> Tree<K, W> mapi(BiFunction<K, V, W> f) {
      return create();
    }

    @Override
    public <W> Tree<K, W> mapPartiali(final BiFunction<K, V, Optional<W>> f) {
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
    public Tree<K, V> filteri(final BiPredicate<K, V> f) {
      return this;
    }

    @Override
    public Tree<K, V> merge(final BiFunction<V, V, V> f, final Tree<K, V> t) {
      return t;
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

    public abstract <W> Node<K, W> createNode(final Tree<K, W> left, final K key, final W newValue, final Tree<K, W> right);

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
    public int size() {
      return mLeft.size() + mRight.size() + 1;
    }

    @Override
    public int depth() {
      return Math.max(mLeft.depth(), mRight.depth()) + 1;
    }

    @Override
    public void appi(final BiConsumer<K, V> f) {
      mLeft.appi(f);
      f.accept(mKey, mValue);
      mRight.appi(f);
      
      return;
    }

    @Override
    public <W> Tree<K, W> mapi(final BiFunction<K, V, W> f) {
      return createNode(mLeft.mapi(f), mKey, f.apply(mKey, mValue), mRight.mapi(f));
    }

    @Override
    public abstract <W> Tree<K, W> mapPartiali(final BiFunction<K, V, Optional<W>> f);

    @Override
    public <W> W foldli(final TriFunction<K, V, W, W> f, final W w) {
      return mRight.foldli(f, f.apply(mKey, mValue, mLeft.foldli(f, w)));
    }

    @Override
    public <W> W foldri(final TriFunction<K, V, W, W> f, final W w) {
      return mLeft.foldri(f, f.apply(mKey, mValue, mRight.foldri(f, w)));
    }

    @Override
    public abstract Tree<K, V> filteri(final BiPredicate<K, V> f);

    @Override
    public Tree<K, V> merge(final BiFunction<V, V, V> f, final Tree<K, V> t) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DSTreeNode[] DSgetChildren() {
      return new DSTreeNode[] { mLeft, mRight };
    }

    @Override
    public Object DSgetValue() {
      return mKey.toString() + "," + mValue.toString();
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
    public <W> Node<K, W> createNode(final Tree<K, W> left, final K key, final W newValue, final Tree<K, W> right) {
      return create(left, key, newValue, right);
    }

    private BlackNode<K, V> convertToBlack() {
      return BlackNode.create(mLeft, mKey, mValue, mRight);
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
    Pair<Tree<K, V>, Boolean> rem(final K key) throws ControlExnNoSuchElement {
      final int c = key.compareTo(mKey);
      final Tree<K, V> l = mLeft, r = mRight;
      
      if (c < 0) {
        final Pair<Tree<K, V>, Boolean> p = l.rem(key);
        final RedNode<K, V> m = RedNode.create(p.mx1, mKey, mValue, r);

        return p.mx2 ? unbalancedRight(m) : Pair.create(m, false);
      }
      else if (c > 0) {
        final Pair<Tree<K, V>, Boolean> p = r.rem(key);
        final RedNode<K, V> m = RedNode.create(l, mKey, mValue, p.mx1);

        return p.mx2 ? unbalancedLeft(m) : Pair.create(m, false);
      }
      else {
        if (r.isEmpty()) {
          return Pair.create(l, false);
        }
        else {
          final Tuple4<Tree<K, V>, K, V, Boolean> p = removeMin(r);
          final RedNode<K, V> m = RedNode.create(l, p.mx2, p.mx3, p.mx1);

          return p.mx4 ? unbalancedLeft(m) : Pair.create(m, false);
        }
      }
    }

    @Override
    public <W> Tree<K, W> mapPartiali(final BiFunction<K, V, Optional<W>> f) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Tree<K, V> filteri(final BiPredicate<K, V> f) {
      throw new UnsupportedOperationException("Not supported yet.");
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
    public <W> Node<K, W> createNode(final Tree<K, W> left, final K key, final W value, final Tree<K, W> right) {
      return create(left, key, value, right);
    }

    private RedNode<K, V> convertToRed() {
      return RedNode.create(mLeft, mKey, mValue, mRight);
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
    Pair<Tree<K, V>, Boolean> rem(final K key) throws ControlExnNoSuchElement {
      final int c = key.compareTo(mKey);
      final Tree<K, V> l = mLeft, r = mRight;
      
      if (c < 0) {
        final Pair<Tree<K, V>, Boolean> p = l.rem(key);
        final BlackNode<K, V> m = BlackNode.create(p.mx1, mKey, mValue, r);

        return p.mx2 ? unbalancedRight(m) : Pair.create(m, false);
      }
      else if (c > 0) {
        final Pair<Tree<K, V>, Boolean> p = r.rem(key);
        final BlackNode<K, V> m = BlackNode.create(l, mKey, mValue, p.mx1);

        return p.mx2 ? unbalancedLeft(m) : Pair.create(m, false);
      }
      else {
        if (r.isEmpty()) {
          return blackifyRem(l);
        }
        else {
          final Tuple4<Tree<K, V>, K, V, Boolean> p = removeMin(r);
          final BlackNode<K, V> m = BlackNode.create(l, p.mx2, p.mx3, p.mx1);

          return p.mx4 ? unbalancedLeft(m) : Pair.create(m, false);
        }
      }
    }

    @Override
    public <W> Tree<K, W> mapPartiali(final BiFunction<K, V, Optional<W>> f) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Tree<K, V> filteri(final BiPredicate<K, V> f) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Color DSgetColor() {
      return Color.BLACK;
    }
  }

//    let lbalance x1 x2 x3 = match x1, x2, x3 with
//    | Red (Red (a,x,b), y, c), z, d ->
//        Red (Black (a,x,b), y, Black (c,z,d))
//    | Red (a, x, Red (b,y,c)), z, d ->
//        Red (Black (a,x,b), y, Black (c,z,d))
//    | a,x,b ->
//        Black (a,x,b)
//
  private static <K extends Comparable<K>, V> Tree<K, V> leftBalance(
          final Tree<K, V> left,
          final K key,
          final V value,
          final Tree<K, V> right) {
    final RedNode<K, V> l;

    if ((l = left.asRed()) != null) {
      final RedNode<K, V> ll, lr;

      if ((ll = l.mLeft.asRed()) != null) {
        return RedNode.create(
                ll.convertToBlack(),
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

  private static <K extends Comparable<K>, V> Tree<K, V> rightBalance(
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
                rr.convertToBlack());
      }
    }

    return BlackNode.create(left, key, value, right);
  }

//   match ins s with
//      | Black _ as s -> s
//      | Red (a, y, b) -> Black (a, y, b)
//      | Empty -> assert false
  private static <K extends Comparable<K>, V> Tree<K, V> blackify(final Tree<K, V> t) {
    final RedNode<K, V> red = t.asRed();
    return (red != null) ? red.convertToBlack() : t;
  }

  private static <K extends Comparable<K>, V> Pair<Tree<K, V>, Boolean> blackifyRem(final Tree<K, V> t) {
    final RedNode<K, V> red = t.asRed();
    return (red != null) ? Pair.create(red.convertToBlack(), false)
                         : Pair.create(t, true);
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

  private static <K extends Comparable<K>, V> Pair<Tree<K, V>, Boolean> unbalancedLeft(final Tree<K, V> t) {
    final RedNode<K, V> red;
    final BlackNode<K, V> black;
    
    if ((red = t.asRed()) != null) {
      final BlackNode<K, V> rb = red.mLeft.asBlack();
      if (rb != null) {
        return Pair.create(leftBalance(rb.convertToRed(), red.mKey, red.mValue, red.mRight), false);
      }
    }
    else if ((black = t.asBlack()) != null) {
      final Tree<K, V> left = black.mLeft;
      BlackNode<K, V> bb;
      final RedNode<K, V> br;

      if ((bb = left.asBlack()) != null) {
        return Pair.create(leftBalance(bb.convertToRed(), black.mKey, black.mValue, black.mRight), true);
      }
      else if ((br = left.asRed()) != null && (bb = br.mRight.asBlack()) != null) {
        return Pair.create(
                BlackNode.create(br.mLeft, br.mKey, br.mValue,
                                 leftBalance(bb.convertToRed(), black.mKey, black.mValue, black.mRight)),
                false);
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

  private static <K extends Comparable<K>, V> Pair<Tree<K, V>, Boolean> unbalancedRight(final Tree<K, V> t) {
    final RedNode<K, V> red;
    final BlackNode<K, V> black;

    if ((red = t.asRed()) != null) {
      final BlackNode<K, V> rb = red.mRight.asBlack();

      if (rb != null) {
        return Pair.create(rightBalance(red.mLeft, red.mKey, red.mValue, rb.convertToRed()), false);
      }
    }
    else if ((black = t.asBlack()) != null) {
      final Tree<K, V> right = black.mRight;
      BlackNode<K, V> bb;
      final RedNode<K, V> br;

      if ((bb = right.asBlack()) != null) {
        return Pair.create(rightBalance(black.mLeft, black.mKey, black.mValue, bb.convertToRed()), true);
      }
      else if ((br = right.asRed()) != null && (bb = br.mLeft.asBlack()) != null) {
        return Pair.create(
                BlackNode.create(rightBalance(black.mLeft, black.mKey, black.mValue, bb.convertToRed()),
                                 br.mKey, br.mValue, br.mRight),
                false);
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

  private static <K extends Comparable<K>, V> Tuple4<Tree<K,V>, K, V, Boolean> removeMin(final Tree<K, V> tree) {
    final BlackNode<K, V> black = tree.asBlack();
    if (black != null) {
      final Tree<K, V> l = black.mLeft, r = black.mRight;

      if (l.isEmpty()) {
        if (r.isEmpty()) {
          return Tuple4.create(l, black.mKey, black.mValue, true);
        }

        final RedNode<K, V> br = r.asRed();
        if (br != null) {
          return Tuple4.create(br.convertToBlack(), black.mKey, black.mValue, false);
        }

        final BlackNode<K, V> bb = r.asBlack();
        if (bb != null) {
          throw new AssertionError("BlackNode encountered at inapropriate place.");
        }
      }

      final Tuple4<Tree<K, V>, K, V, Boolean> res = removeMin(l);
      final Tree<K, V> t = BlackNode.create(res.mx1, black.mKey, black.mValue, r);

      if (res.mx4) {
        final Pair<Tree<K, V>, Boolean> u = unbalancedRight(t);
        return Tuple4.create(u.mx1, res.mx2, res.mx3, u.mx2);
      }
      else {
        return Tuple4.create(t, res.mx2, res.mx3, false);
      }
    }

    final RedNode<K, V> red = tree.asRed();
    if (red != null) {
      final Tree<K, V> l = red.mLeft, r = red.mRight;
    
      if (l.isEmpty()) {
        return Tuple4.create(red.mRight, red.mKey, red.mValue, false);
      }
      else {
        final Tuple4<Tree<K, V>, K, V, Boolean> res = removeMin(l);
        final Tree<K, V> t = RedNode.create(res.mx1, red.mKey, red.mValue, r);

        if (res.mx4) {
          final Pair<Tree<K, V>, Boolean> u = unbalancedRight(t);
          return Tuple4.create(u.mx1, res.mx2, res.mx3, u.mx2);
        }
        else {
          return Tuple4.create(t, res.mx2, res.mx3, false);
        }
      }
    }

    throw new AssertionError("The tree cannot be empty in this context.");
  }

  static <K extends Comparable<K>, V> Pair<Boolean, String> verifyRedBlackProperties(final Tree<K, V> t) {
    if (! redChildrenPropertyHolds(t)) {
      return Pair.create(false, "There are red nodes that have red children.");
    }
    final Pair<Integer, Boolean> p = blackNodeCountPropertyHolds(t);
    if (! p.mx2)  {
      return Pair.create(false, "Number of black nodes to leaves of tree "
                                + "from left and right subtree did not match.");
    }
    return Pair.create(true, "Success!");
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

  // Public interface
  //@SuppressWarnings("unchecked")
  public static <K extends Comparable<K>, V> Tree<K, V> empty() {
    return EmptyNode.create();
  }

  public static <K extends Comparable<K>, V> Tree<K, V> singleton(final K key, final V value) {
    final Tree<K, V> e = empty();
    return BlackNode.create(e, key, value, e);
  }

  private static final class ControlExnNoSuchElement extends Exception {}
  private static final ControlExnNoSuchElement sNoSuchElement = new ControlExnNoSuchElement();
}
