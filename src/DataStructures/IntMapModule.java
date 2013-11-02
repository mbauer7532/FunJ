/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import Utils.Functionals.TriFunction;
import Utils.Numeric;
import java.awt.Color;
import java.util.Objects;
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
public final class IntMapModule {
  public static abstract class Tree<V> implements DSTreeNode {
    public abstract boolean isEmpty();
    public abstract boolean contains(final int key);
    public abstract Tree<V> insert(final BiFunction<V, V, V> f, final int key, final V value);
    public abstract Optional<V> get(final int key);
    public abstract V getWithDefault(final int key, final V def);
    public abstract Tree<V> remove(final int key);
    public abstract int size();
    public abstract int depth();
    public abstract void app(final Consumer<V> f);
    public abstract void appi(final BiConsumer<Integer, V> f);
    public abstract <W> Tree<W> map(final Function<V, W> f);
    public abstract <W> Tree<W> mapi(final BiFunction<Integer, V, W> f);
    public abstract <W> Tree<W> mapPartial(final Function<V, Optional<W>> f);
    public abstract <W> Tree<W> mapPartiali(final BiFunction<Integer, V, Optional<W>> f);
    public abstract <W> W foldl(final BiFunction<V, W, W> f, final W w);
    public abstract <W> W foldli(final TriFunction<Integer, V, W, W> f, final W w);
    public abstract <W> W foldr(final BiFunction<V, W, W> f, final W w);
    public abstract <W> W foldri(final TriFunction<Integer, V, W, W> f, final W w);
    public abstract Tree<V> filter(final Predicate<V> f);
    public abstract Tree<V> filteri(final BiPredicate<Integer, V> f);
    public abstract Tree<V> merge(final BiFunction<V, V, V> f, final Tree<V> t);
  }

  private static final class EmptyNode<V> extends Tree<V> {
    @Override
    public Tree<V> insert(final BiFunction<V, V, V> f, final int key, final V value) {
      Objects.requireNonNull(f);

      return LeafNode.createLeafNode(key, value);
    }

    @Override
    public boolean isEmpty() {
      return true;
    }

    @Override
    public boolean contains(final int key) {
      return false;
    }

    @Override
    public Optional<V> get(final int key) {
      return Optional.empty();
    }

    @Override
    public V getWithDefault(final int key, final V def) {
      return def;
    }

    @Override
    public Tree<V> remove(final int key) {
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
    public void app(final Consumer<V> f) {
      Objects.requireNonNull(f);

      return;
    }

    @Override
    public void appi(final BiConsumer<Integer, V> f) {
      Objects.requireNonNull(f);

      return;
    }

    @Override
    public <W> Tree<W> map(final Function<V, W> f) {
      Objects.requireNonNull(f);

      return createEmptyNode();
    }

    @Override
    public <W> Tree<W> mapi(final BiFunction<Integer, V, W> f) {
      Objects.requireNonNull(f);

      return createEmptyNode();
    }

    @Override
    public <W> Tree<W> mapPartial(final Function<V, Optional<W>> f) {
      Objects.requireNonNull(f);

      return createEmptyNode();
    }

    @Override
    public <W> Tree<W> mapPartiali(final BiFunction<Integer, V, Optional<W>> f) {
      Objects.requireNonNull(f);

      return createEmptyNode();
    }

    @Override
    public <W> W foldl(final BiFunction<V, W, W> f, final W w) {
      Objects.requireNonNull(f);

      return w;
    }

    @Override
    public <W> W foldli(final TriFunction<Integer, V, W, W> f, final W w) {
      Objects.requireNonNull(f);

      return w;
    }

    @Override
    public <W> W foldr(final BiFunction<V, W, W> f, final W w) {
      Objects.requireNonNull(f);

      return w;
    }

    @Override
    public <W> W foldri(final TriFunction<Integer, V, W, W> f, final W w) {
      Objects.requireNonNull(f);

      return w;
    }

    @Override
    public Tree<V> filter(final Predicate<V> f) {
      Objects.requireNonNull(f);

      return this;
    }

    @Override
    public Tree<V> filteri(final BiPredicate<Integer, V> f) {
      Objects.requireNonNull(f);

      return this;
    }

    @Override
    public Tree<V> merge(final BiFunction<V, V, V> f, final Tree<V> t) {
      Objects.requireNonNull(f);
      Objects.requireNonNull(t);

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
      return Color.GREEN;
    }
    
    private static final EmptyNode<?> sEmptyNode = new EmptyNode<>();

    @SuppressWarnings("unchecked")
    public static <V> EmptyNode<V> createEmptyNode() {
      return (EmptyNode<V>) sEmptyNode;
    }
  }

  private static final class LeafNode<V> extends Tree<V> {
    @Override
    public Tree<V> insert(final BiFunction<V, V, V> f, final int key, final V value) {
      Objects.requireNonNull(f);

      if (key == mKey) {
        return createLeafNode(key, f.apply(value, mValue));
      }
      else {
        return join(key, createLeafNode(key, value), mKey, this);
      }
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public boolean contains(final int key) {
      return mKey == key;
    }

    @Override
    public Optional<V> get(final int key) {
      return mKey == key ? Optional.of(mValue) : Optional.empty();
    }

    @Override
    public V getWithDefault(final int key, final V def) {
      return mKey == key ? mValue : def;
    }

    @Override
    public Tree<V> remove(final int key) {
      return mKey == key ? EmptyNode.createEmptyNode() : this;
    }

    @Override
    public int size() {
      return 1;
    }

    @Override
    public int depth() {
      return 1;
    }

    @Override
    public void app(final Consumer<V> f) {
      Objects.requireNonNull(f);

      f.accept(mValue);
      return;
    }

    @Override
    public void appi(final BiConsumer<Integer, V> f) {
      Objects.requireNonNull(f);

      f.accept(mKey, mValue); 
      return;
    }

    @Override
    public <W> Tree<W> map(final Function<V, W> f) {
      Objects.requireNonNull(f);

      return createLeafNode(mKey, f.apply(mValue));
    }

    @Override
    public <W> Tree<W> mapi(final BiFunction<Integer, V, W> f) {
      Objects.requireNonNull(f);

      return createLeafNode(mKey, f.apply(mKey, mValue));
    }

    @Override
    public <W> Tree<W> mapPartial(final Function<V, Optional<W>> f) {
      Objects.requireNonNull(f);

      return compOptMapedValue(f.apply(mValue));
    }

    @Override
    public <W> Tree<W> mapPartiali(final BiFunction<Integer, V, Optional<W>> f) {
      Objects.requireNonNull(f);

      return compOptMapedValue(f.apply(mKey, mValue));
    }

    @Override
    public <W> W foldl(final BiFunction<V, W, W> f, final W w) {
      Objects.requireNonNull(f);

      return f.apply(mValue, w);
    }

    @Override
    public <W> W foldli(final TriFunction<Integer, V, W, W> f, final W w) {
      Objects.requireNonNull(f);
      
      return f.apply(mKey, mValue, w);
    }

    @Override
    public <W> W foldr(final BiFunction<V, W, W> f, final W w) {
      Objects.requireNonNull(f);
      
      return f.apply(mValue, w);
    }

    @Override
    public <W> W foldri(final TriFunction<Integer, V, W, W> f, final W w) {
      Objects.requireNonNull(f);
      
      return f.apply(mKey, mValue, w);
    }

    @Override
    public Tree<V> filter(final Predicate<V> f) {
      Objects.requireNonNull(f);
      
      return f.test(mValue) ? this : EmptyNode.createEmptyNode();
    }

    @Override
    public Tree<V> filteri(final BiPredicate<Integer, V> f) {
      Objects.requireNonNull(f);
      
      return f.test(mKey, mValue) ? this : EmptyNode.createEmptyNode();
    }

    @Override
    public Tree<V> merge(final BiFunction<V, V, V> f, final Tree<V> t) {
      Objects.requireNonNull(f);
      Objects.requireNonNull(t);

      return t.insert((x, y) -> f.apply(y, x), mKey, mValue);
    }
    
    private <W> Tree<W> compOptMapedValue(final Optional<W> opt) {
      if (opt.isPresent()) {
        return createLeafNode(mKey, opt.get());
      }
      else {
        return EmptyNode.createEmptyNode();
      }
    }

    @Override
    public DSTreeNode[] DSgetChildren() {
      return new DSTreeNode[0];
    }

    @Override
    public Object DSgetValue() {
      return String.format("(%d -> %s)", mKey, mValue.toString());
    }

    @Override
    public Color DSgetColor() {
      return Color.BLUE;
    }

    private static <V> LeafNode<V> createLeafNode(final int key, final V value) {
      Objects.requireNonNull(value);

      return new LeafNode<>(key, value);
    }

    private LeafNode(final int key, final V value) {
      mKey = key;
      mValue = value;
    }

    private final int mKey;
    private final V mValue;
  }

  private static final class BranchNode<V> extends Tree<V> {
    @Override
    public Tree<V> insert(final BiFunction<V, V, V> f, final int key, final V value) {
      Objects.requireNonNull(f);
      Objects.requireNonNull(value);

      if (matchPrefix(key, mPrefix, mBranchingBit)) {
        if (zeroBit(key, mBranchingBit)) {
          return createBranchNode(mPrefix, mBranchingBit, mLeft.insert(f, key, value), mRight);
        }
        else {
          return createBranchNode(mPrefix, mBranchingBit, mLeft, mRight.insert(f, key, value));
        }
      }
      else {
        return join(key, LeafNode.createLeafNode(key, value), mPrefix, this);
      }
    }
    
    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public boolean contains(final int key) {
      return zeroBit(key, mBranchingBit) ? mLeft.contains(key) : mRight.contains(key);
    }

    @Override
    public Optional<V> get(final int key) {
      return zeroBit(key, mBranchingBit) ? mLeft.get(key) : mRight.get(key);
    }

    @Override
    public V getWithDefault(final int key, final V def) {
      return zeroBit(key, mBranchingBit)
              ? mLeft.getWithDefault(key, def)
              : mRight.getWithDefault(key, def);
    }

    @Override
    public Tree<V> remove(final int key) {
      if (! matchPrefix(key, mPrefix, mBranchingBit)) {
        return this;
      }
      else {
        final Tree<V> t0, t1;
        if (zeroBit(key, mBranchingBit)) {
          t0 = mLeft.remove(key);
          t1 = mRight;
        }
        else {
          t0 = mLeft;
          t1 = mRight.remove(key);
        }

        return smartBranchNodeConstructor(mPrefix, mBranchingBit, t0, t1);
      }
    }

    @Override
    public int size() {
      return mLeft.size() + mRight.size();
    }

    @Override
    public int depth() {
      return Integer.max(mLeft.depth(), mRight.depth()) + 1;
    }

    @Override
    public void app(final Consumer<V> f) {
      mLeft.app(f);
      mRight.app(f);
      
      return;
    }

    @Override
    public void appi(final BiConsumer<Integer, V> f) {
      mLeft.appi(f);
      mRight.appi(f);
      
      return;
    }

    @Override
    public <W> Tree<W> map(final Function<V, W> f) {
      return createBranchNode(mPrefix, mBranchingBit, mLeft.map(f), mRight.map(f));
    }

    @Override
    public <W> Tree<W> mapi(final BiFunction<Integer, V, W> f) {
      return createBranchNode(mPrefix, mBranchingBit, mLeft.mapi(f), mRight.mapi(f));
    }

    @Override
    public <W> Tree<W> mapPartial(final Function<V, Optional<W>> f) {
      final Tree<W> newL = mLeft.mapPartial(f);
      final Tree<W> newR = mRight.mapPartial(f);

      return smartBranchNodeConstructor(mPrefix, mBranchingBit, newL, newR);
    }

    @Override
    public <W> Tree<W> mapPartiali(final BiFunction<Integer, V, Optional<W>> f) {
      final Tree<W> newL = mLeft.mapPartiali(f);
      final Tree<W> newR = mRight.mapPartiali(f);

      return smartBranchNodeConstructor(mPrefix, mBranchingBit, newL, newR);
    }

    @Override
    public <W> W foldl(final BiFunction<V, W, W> f, final W w) {
      return mRight.foldl(f, mLeft.foldl(f, w));
    }

    @Override
    public <W> W foldli(final TriFunction<Integer, V, W, W> f, final W w) {
      return mRight.foldli(f, mLeft.foldli(f, w));
    }

    @Override
    public <W> W foldr(final BiFunction<V, W, W> f, final W w) {
      return mLeft.foldr(f, mRight.foldr(f, w));
    }

    @Override
    public <W> W foldri(final TriFunction<Integer, V, W, W> f, final W w) {
      return mLeft.foldri(f, mRight.foldri(f, w));
    }

    @Override
    public Tree<V> filter(final Predicate<V> f) {
      final Tree<V> newL = mLeft.filter(f);
      final Tree<V> newR = mRight.filter(f);

      if (newL == mLeft && newR == mRight) {
        return this;
      }
      else {
        return smartBranchNodeConstructor(mPrefix, mBranchingBit, newL, newR);
      }
    }

    @Override
    public Tree<V> filteri(final BiPredicate<Integer, V> f) {
      final Tree<V> newL = mLeft.filteri(f);
      final Tree<V> newR = mRight.filteri(f);

      if (newL == mLeft && newR == mRight) {
        return this;
      }
      else {
        return smartBranchNodeConstructor(mPrefix, mBranchingBit, newL, newR);
      }
    }

    @Override
    public Tree<V> merge(final BiFunction<V, V, V> f, final Tree<V> tree) {
      if (! (tree instanceof BranchNode)) {
        return tree.merge((x, y) -> f.apply(y, x), this);
      }
      else {
        final BranchNode<V> s = this;
        final BranchNode<V> t = (BranchNode<V>) tree;
        final int p = s.mPrefix;
        final int q = t.mPrefix;

        final int m = s.mBranchingBit;
        final int n = t.mBranchingBit;

        final Tree<V> s0 = s.mLeft, s1 = s.mRight;
        final Tree<V> t0 = t.mLeft, t1 = t.mRight;

        if (m == n && p == q) {
          return createBranchNode(p, m, s0.merge(f, t0), s1.merge(f, t1));
        }
        else if (m < n && matchPrefix(q, p, m)) {
          if (zeroBit(q, m)) {
            return createBranchNode(p, m, s0.merge(f, t), s1);
          }
          else {
            return createBranchNode(p, m, s0, s1.merge(f, t));
          }
        }
        else if (m > n && matchPrefix(p, q, n)) {
          if (zeroBit(p, n)) {
            return createBranchNode(q, n, s.merge(f, t0), t1);
          }
          else {
            return createBranchNode(q, n, t0, s.merge(f, t1));
          }
        }
        else {
          return join(p, s, q, t);
        }
      }
    }

    private static <W> Tree<W> smartBranchNodeConstructor(final int prefix,
                                                          final int branchingBit,
                                                          final Tree<W> left,
                                                          final Tree<W> right) {
      if (left.isEmpty()) {
        return right;
      }
      else if (right.isEmpty()) {
        return left;
      }
      else {
        return createBranchNode(prefix, branchingBit, left, right);
      }
    }

    private BranchNode(final int prefix,
                       final int branchingBit,
                       final Tree<V> left,
                       final Tree<V> right) {
      mPrefix = prefix;
      mBranchingBit = branchingBit;
      mLeft = left;
      mRight = right;
    }

    private static <V> BranchNode<V> createBranchNode(final int prefix,
                                                      final int branchingBit,
                                                      final Tree<V> left,
                                                      final Tree<V> right) {
      return new BranchNode<>(prefix, branchingBit, left, right);
    }


    @Override
    public DSTreeNode[] DSgetChildren() {
      return new DSTreeNode[] {mLeft, mRight};
    }

    @Override
    public Object DSgetValue() {
      return String.format("(%d, %d)", mPrefix, mBranchingBit);
    }

    @Override
    public Color DSgetColor() {
      return Color.MAGENTA;
    }

    private final int mPrefix;
    private final int mBranchingBit;
    private final Tree<V> mLeft;
    private final Tree<V> mRight;
  }

  private static boolean zeroBit(final int k, final int m) {
    return (k & m) == 0;
  }

  private static int mask(final int k, final int m) {
    return (k | (m - 1)) & (~ m);
  }

  private static boolean matchPrefix(final int k, final int p, final int m)
  {
    return mask(k, m) == p;
  }

  private static int branchingBit(final int p0, final int p1) {
    return Numeric.highestBit(p0 ^ p1);
  }

  private static <W> Tree<W> join(final int p0, final Tree<W> t0,
                                  final int p1, final Tree<W> t1) {
    final int m = branchingBit(p0, p1);
    final int pnew = mask(p0, m);

    final Tree<W> nt0, nt1;
    if (zeroBit(p0, m)) {
      nt0 = t0;
      nt1 = t1;
    }
    else {
      nt0 = t1;
      nt1 = t0;
    }

    return BranchNode.createBranchNode(pnew, m, nt0, nt1);
  }

  public static <V> Tree<V> empty() {
    return EmptyNode.createEmptyNode();
  }

  public static <V> Tree<V> singleton(final int key, final V value) {
    return LeafNode.createLeafNode(key, value);
  }
}
