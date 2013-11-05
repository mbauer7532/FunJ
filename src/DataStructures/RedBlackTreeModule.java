/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

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

    public Tree<K, V> insert(final BiFunction<V, V, V> f, final K key, final V value) {
      return blackify(ins(f, key, value));
    }

    public abstract boolean isEmpty();
    public abstract Optional<V> get(final K key);
    public abstract Tree<K, V> remove(final K key);
    public abstract int size();
    public abstract int depth();
    public abstract void appi(final BiConsumer<K, V> f);
    public abstract <W> Tree<K, W> mapi(final BiFunction<K, V, W> f);
    public abstract <W> Tree<K, W> mapPartiali(final BiFunction<K, V, Optional<W>> f);
    public abstract <W> W foldli(final TriFunction<K, V, W, W> f, final W w);
    public abstract <W> W foldri(final TriFunction<K, V, W, W> f, final W w);
    public abstract Tree<K, V> filteri(final BiPredicate<K, V> f);
    public abstract Tree<K, V> merge(final BiFunction<V, V, V> f, final Tree<K, V> t);

    protected abstract Tree<K, V> ins(final BiFunction<V, V, V> f, final K key, final V value);
  }

  private static final class EmptyNode<K extends Comparable<K>, V> extends Tree<K, V> {
    private static final EmptyNode<? extends Comparable<?>, ?> sEmptyNode = new EmptyNode<>();

    @SuppressWarnings("unchecked")
    public static <K extends Comparable<K>, V> EmptyNode<K, V> createEmptyNode() {
      return (EmptyNode<K, V>) sEmptyNode;
    }

    @Override
    public boolean isEmpty() {
      return true;
    }

    @Override
    public Tree<K, V> ins(final BiFunction<V, V, V> f, final K key, final V value) {
      return createEmptyNode();
    }

    @Override
    public Optional<V> get(final K key) {
      return Optional.empty();
    }

    @Override
    public Tree<K, V> remove(final K key) {
      return this;
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
      return createEmptyNode();
    }

    @Override
    public <W> Tree<K, W> mapPartiali(final BiFunction<K, V, Optional<W>> f) {
      return createEmptyNode();
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
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object DSgetValue() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Color DSgetColor() {
      throw new UnsupportedOperationException("Not supported yet.");
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
    public boolean isEmpty() {
      return false;
    }

    @Override
    protected abstract Tree<K, V> ins(final BiFunction<V, V, V> f, final K key, final V value);

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
    public abstract Tree<K, V> remove(final K key);

    @Override
    public int size() {
      return mLeft.size() + mRight.size();
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
    public abstract DSTreeNode[] DSgetChildren();

    @Override
    public abstract Object DSgetValue();

    @Override
    public abstract Color DSgetColor();
  }

  private static final class RedNode<K extends Comparable<K>, V> extends Node<K, V> {
    private RedNode(final Tree<K, V> left, final K key, final V value, final Tree<K, V> right) {
      super(key, value, left, right);
    }

    public static <K extends Comparable<K>, V> RedNode<K, V> create(final Tree<K, V> left, final K key, final V value, final Tree<K, V> right) {
      return new RedNode<>(left, key, value, right);
    }

    @Override
    public <W> Node<K, W> createNode(final Tree<K, W> left, final K key, final W newValue, final Tree<K, W> right) {
      return create(left, key, newValue, right);
    }

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
    protected Tree<K, V> ins(final BiFunction<V, V, V> f, final K key, final V value) {
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

    @Override
    public Tree<K, V> remove(final K key) {
      throw new UnsupportedOperationException("Not supported yet.");
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
    public DSTreeNode[] DSgetChildren() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object DSgetValue() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Color DSgetColor() {
      throw new UnsupportedOperationException("Not supported yet.");
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
    protected Tree<K, V> ins(final BiFunction<V, V, V> f, final K key, final V value) {
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

    @Override
    public Tree<K, V> remove(final K key) {
      throw new UnsupportedOperationException("Not supported yet.");
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
    public DSTreeNode[] DSgetChildren() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object DSgetValue() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Color DSgetColor() {
      throw new UnsupportedOperationException("Not supported yet.");
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
    if (left instanceof RedNode) {
      final RedNode<K, V> l = (RedNode<K, V>) left;
      if (l.mLeft instanceof RedNode) {
        final RedNode<K, V> ll = (RedNode<K, V>) l.mLeft;

        return RedNode.create(
                BlackNode.create(ll.mLeft, ll.mKey, ll.mValue, ll.mRight),
                l.mKey,
                l.mValue,
                BlackNode.create(l.mRight, key, value, right));
      }
      else if (l.mRight instanceof RedNode) {
        final RedNode<K, V> lr = (RedNode<K, V>) l.mRight;

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
    if (right instanceof RedNode) {
      final RedNode<K, V> r = (RedNode<K, V>) right;
      if (r.mLeft instanceof RedNode) {
        final RedNode<K, V> rl = (RedNode<K, V>) r.mLeft;

        return RedNode.create(
                BlackNode.create(left, key, value, rl.mLeft),
                rl.mKey,
                rl.mValue,
                BlackNode.create(rl.mRight, r.mKey, r.mValue, r.mRight));
      }
      else if (r.mRight instanceof RedNode) {
        final RedNode<K, V> rr = (RedNode<K, V>) r.mRight;

        return RedNode.create(
                BlackNode.create(left, key, value, r.mLeft),
                r.mKey,
                r.mValue,
                BlackNode.create(rr.mLeft, rr.mKey, rr.mValue, rr.mRight));
      }
    }

    return BlackNode.create(left, key, value, right);
  }

//   match ins s with
//      | Black _ as s -> s
//      | Red (a, y, b) -> Black (a, y, b)
//      | Empty -> assert false
  private static <K extends Comparable<K>, V> Tree<K, V> blackify(final Tree<K, V> t) {
    if (t instanceof RedNode) {
      final RedNode<K, V> r = (RedNode<K, V>) t;
      return BlackNode.create(r.mLeft, r.mKey, r.mValue, r.mRight);
    }
    else {
      return t;
    }
  }
}
