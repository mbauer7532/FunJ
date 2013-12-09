/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.Functionals.IntBiConsumer;
import Utils.Functionals.IntBiFunction;
import Utils.Functionals.IntBiPredicate;
import Utils.Functionals.IntTriFunction;
import Utils.Numeric;
import Utils.Ref;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.StructureGraphic.v1.DSTreeNode;

/**
 *
 * @author Neo
 */
public final class IntMapModule {
  static final class EntryRef<V> extends PersistentMapIntEntry<V> {
    public EntryRef(final LeafNode<V> t) { mRef = t; }
    private LeafNode<V> mRef;

    @Override
    public int getKey() { return mRef.getKey(); }

    @Override
    public V getValue() { return mRef.getValue(); }
  }

  public static abstract class Tree<V> implements PersistentMapInt<V, Tree<V>>, DSTreeNode {
    @Override
    public final boolean containsKey(final int key) {
      return get(key).isPresent();
    }

    @Override
    public final V getOrElse(final int key, final V def) {
      return get(key).orElse(def);
    }

    @Override
    public V getOrElseSupplier(final int key, final Supplier<V> other) {
      return get(key).orElseGet(other);
    }

    @Override
    public final <W> Tree<W> map(final Function<V, W> f) {
      return mapi((k, v) -> f.apply(v));
    }

    @Override
    public final void app(final Consumer<V> f) {
      appi((k, v) -> f.accept(v));

      return;
    }

    @Override
    public final <W> W foldl(final BiFunction<V, W, W> f, final W w) {
      return foldli((k, v, z) -> f.apply(v, z), w);
    }

    @Override
    public final <W> W foldr(final BiFunction<V, W, W> f, final W w) {
      return foldri((k, v, z) -> f.apply(v, z), w);
    }

    @Override
    public final Tree<V> filter(final Predicate<V> f) {
      return filteri((k, v) -> f.test(v));
    }

    @Override
    public final <W> Tree<W> mapPartial(final Function<V, Optional<W>> f) {
      return mapPartiali((k, v) -> f.apply(v));
    }

    @Override
    public abstract <W> Tree<W> mapi(final IntBiFunction<V, W> f);

    @Override
    public abstract <W> Tree<W> mapPartiali(final IntBiFunction<V, Optional<W>> f);

    @Override
    public abstract Tree<V> filteri(final IntBiPredicate<V> f);

    @Override
    public abstract Tree<V> merge(final BiFunction<V, V, V> f, final PersistentMapInt<V, Tree<V>> t);
    
    @Override
    public Pair<Tree<V>, Tree<V>> partition(final Predicate<V> f) {
      return partitioni((k, v) -> f.test(v));
    }
    
    private static <V> OptionalInt getOptInt(final Optional<PersistentMapIntEntry<V>> opt) {
      if (opt.isPresent()) {
        return OptionalInt.of(opt.get().getKey());
      }
      else {
        return OptionalInt.empty();
      }
    }

    @Override
    public OptionalInt higherKey(final int key) {
      return getOptInt(higherPair(key));
    }

    @Override
    public final OptionalInt lowerKey(final int key) {
      return getOptInt(lowerPair(key));
    }

    @Override
    public OptionalInt minKey() {
      return getOptInt(minElementPair());
    }

    @Override
    public OptionalInt maxKey() {
      return getOptInt(maxElementPair());
    }

    @Override
    public Optional<PersistentMapIntEntry<V>> lowerPair(final int key) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Optional<PersistentMapIntEntry<V>> higherPair(final int key) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Optional<PersistentMapIntEntry<V>> minElementPair() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Optional<PersistentMapIntEntry<V>> maxElementPair() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Pair<Boolean, String> verifyMapProperties() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected abstract void partitioniImpl(final IntBiPredicate<V> f, final Ref<Tree<V>> t0, final Ref<Tree<V>> t1);

    @Override
    public final Pair<Tree<V>, Tree<V>> partitioni(final IntBiPredicate<V> f) {
      final Ref<Tree<V>> r0 = new Ref<>(), r1 = new Ref<>();
      partitioniImpl(f, r0, r1);
      
      return Pair.create(r0.r, r1.r);
    }

    @Override
    public int[] keys() {
      final ArrayList<Integer> xs = new ArrayList<>();
      appi((i, v) -> { xs.add(i); });

      return xs.stream().mapToInt(i -> i).toArray();
    }

    @Override
    public ArrayList<PersistentMapIntEntry<V>> keyValuePairs() {
      final ArrayList<PersistentMapIntEntry<V>> vs = new ArrayList<>();
      appEntry(n -> { vs.add(new EntryRef<>(n)); });

      return vs;
    }

    protected abstract void appEntry(final Consumer<LeafNode<V>> c);
  }

  private static final class EmptyNode<V> extends Tree<V> {
    @Override
    public boolean containsValue(final V value) {
      return false;
    }

    @Override
    public Tree<V> insert(final BiFunction<V, V, V> f, final int key, final V value) {
      Objects.requireNonNull(f);

      return LeafNode.create(key, value);
    }

    @Override
    public Tree<V> insert(final int key, final V value) {
      return LeafNode.create(key, value);
    }

    @Override
    public boolean isEmpty() {
      return true;
    }

    @Override
    public Optional<V> get(final int key) {
      return Optional.empty();
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
    public int height() {
      return 0;
    }

    @Override
    public void appi(final IntBiConsumer<V> f) {
      return;
    }

    @Override
    public <W> Tree<W> mapi(final IntBiFunction<V, W> f) {
      return create();
    }

    @Override
    public <W> Tree<W> mapPartiali(final IntBiFunction<V, Optional<W>> f) {
      return create();
    }

    @Override
    public <W> W foldli(final IntTriFunction<V, W, W> f, final W w) {
      return w;
    }

    @Override
    public <W> W foldri(final IntTriFunction<V, W, W> f, final W w) {
      return w;
    }

    @Override
    public Tree<V> filteri(final IntBiPredicate<V> f) {
      return this;
    }

    @Override
    public Tree<V> merge(final BiFunction<V, V, V> f, final PersistentMapInt<V, Tree<V>> t) {
      return (Tree<V>) t;
    }

    @Override
    protected void partitioniImpl(final IntBiPredicate<V> f, final Ref<Tree<V>> t0, final Ref<Tree<V>> t1)
    {
      t0.r = t1.r = create();

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
    
    private static final EmptyNode<?> sEmptyNode = new EmptyNode<>();

    @SuppressWarnings("unchecked")
    public static <V> EmptyNode<V> create() {
      return (EmptyNode<V>) sEmptyNode;
    }

    @Override
    protected final void appEntry(final Consumer<LeafNode<V>> c) {
      return;
    }
  }

  private static final class LeafNode<V> extends Tree<V> {
    public int getKey() { return mKey; }

    public V getValue() { return mValue; }

    @Override
    public boolean containsValue(final V value) {
      return mValue.equals(value);
    }

    @Override
    public Tree<V> insert(final BiFunction<V, V, V> f, final int key, final V value) {
      Objects.requireNonNull(f);

      return key == mKey
              ? create(key, f.apply(value, mValue))
              : join(key, create(key, value), mKey, this);
    }

    @Override
    public Tree<V> insert(final int key, final V value) {
      return key == mKey
              ? create(key, value)
              : join(key, create(key, value), mKey, this);
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public Optional<V> get(final int key) {
      return mKey == key ? Optional.of(mValue) : Optional.empty();
    }

    @Override
    public Tree<V> remove(final int key) {
      return mKey == key ? EmptyNode.create() : this;
    }

    @Override
    public int size() {
      return 1;
    }

    @Override
    public int height() {
      return 1;
    }

    @Override
    public void appi(final IntBiConsumer<V> f) {
      f.accept(mKey, mValue); 
      return;
    }

    @Override
    public <W> Tree<W> mapi(final IntBiFunction<V, W> f) {
      return create(mKey, f.apply(mKey, mValue));
    }

    @Override
    public <W> Tree<W> mapPartiali(final IntBiFunction<V, Optional<W>> f) {
      return compOptMapedValue(f.apply(mKey, mValue));
    }

    @Override
    public <W> W foldli(final IntTriFunction<V, W, W> f, final W w) {
      return f.apply(mKey, mValue, w);
    }

    @Override
    public <W> W foldri(final IntTriFunction<V, W, W> f, final W w) {
      return f.apply(mKey, mValue, w);
    }

    @Override
    public Tree<V> filteri(final IntBiPredicate<V> f) {
      return f.test(mKey, mValue) ? this : EmptyNode.create();
    }

    @Override
    public Tree<V> merge(final BiFunction<V, V, V> f, final PersistentMapInt<V, Tree<V>> t) {
      return t.insert((x, y) -> f.apply(y, x), mKey, mValue);
    }

    @Override
    protected void partitioniImpl(final IntBiPredicate<V> f, final Ref<Tree<V>> t0, final Ref<Tree<V>> t1)
    {
      final Tree<V> e = EmptyNode.create();

      if (f.test(mKey, mValue)) {
        t0.r = this;
        t1.r = e;
      }
      else {
        t0.r = e;
        t1.r = this;
      }
      return;
    }

    private <W> Tree<W> compOptMapedValue(final Optional<W> opt) {
      if (opt.isPresent()) {
        return create(mKey, opt.get());
      } 
      else {
        return EmptyNode.create();
      }
    }

    @Override
    protected final void appEntry(final Consumer<LeafNode<V>> c) {
      c.accept(this);
      return;
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

    private static <V> LeafNode<V> create(final int key, final V value) {
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
    public boolean containsValue(final V value) {
      return mLeft.containsValue(value) || mRight.containsValue(value);
    }

    @Override
    public Tree<V> insert(final BiFunction<V, V, V> f, final int key, final V value) {
      Objects.requireNonNull(f);
      Objects.requireNonNull(value);

      if (matchPrefix(key, mPrefix, mBranchingBit)) {
        if (zeroBit(key, mBranchingBit)) {
          return create(mPrefix, mBranchingBit, mLeft.insert(f, key, value), mRight);
        }
        else {
          return create(mPrefix, mBranchingBit, mLeft, mRight.insert(f, key, value));
        }
      }
      else {
        return join(key, LeafNode.create(key, value), mPrefix, this);
      }
    }

    @Override
    public Tree<V> insert(final int key, final V value) {
      if (matchPrefix(key, mPrefix, mBranchingBit)) {
        if (zeroBit(key, mBranchingBit)) {
          return create(mPrefix, mBranchingBit, mLeft.insert(key, value), mRight);
        }
        else {
          return create(mPrefix, mBranchingBit, mLeft, mRight.insert(key, value));
        }
      }
      else {
        return join(key, LeafNode.create(key, value), mPrefix, this);
      }
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public Optional<V> get(final int key) {
      return zeroBit(key, mBranchingBit) ? mLeft.get(key) : mRight.get(key);
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
    public int height() {
      return Integer.max(mLeft.height(), mRight.height()) + 1;
    }

    @Override
    public void appi(final IntBiConsumer<V> f) {
      mLeft.appi(f);
      mRight.appi(f);

      return;
    }

    @Override
    public <W> Tree<W> mapi(final IntBiFunction<V, W> f) {
      return create(mPrefix, mBranchingBit, mLeft.mapi(f), mRight.mapi(f));
    }

    @Override
    public <W> Tree<W> mapPartiali(final IntBiFunction<V, Optional<W>> f) {
      final Tree<W> newL = mLeft.mapPartiali(f);
      final Tree<W> newR = mRight.mapPartiali(f);

      return smartBranchNodeConstructor(mPrefix, mBranchingBit, newL, newR);
    }

    @Override
    public <W> W foldli(final IntTriFunction<V, W, W> f, final W w) {
      return mRight.foldli(f, mLeft.foldli(f, w));
    }

    @Override
    public <W> W foldri(final IntTriFunction<V, W, W> f, final W w) {
      return mLeft.foldri(f, mRight.foldri(f, w));
    }

    @Override
    public Tree<V> filteri(final IntBiPredicate<V> f) {
      final Tree<V> newLeft  = mLeft.filteri(f);
      final Tree<V> newRight = mRight.filteri(f);

      if (newLeft == mLeft && newRight == mRight) {
        return this;
      }
      else {
        return smartBranchNodeConstructor(mPrefix, mBranchingBit, newLeft, newRight);
      }
    }

    @Override
    public Tree<V> merge(final BiFunction<V, V, V> f, final PersistentMapInt<V, Tree<V>> tree) {
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
          return create(p, m, s0.merge(f, t0), s1.merge(f, t1));
        }
        else if (m < n && matchPrefix(q, p, m)) {
          if (zeroBit(q, m)) {
            return create(p, m, s0.merge(f, t), s1);
          }
          else {
            return create(p, m, s0, s1.merge(f, t));
          }
        }
        else if (m > n && matchPrefix(p, q, n)) {
          if (zeroBit(p, n)) {
            return create(q, n, s.merge(f, t0), t1);
          }
          else {
            return create(q, n, t0, s.merge(f, t1));
          }
        }
        else {
          return join(p, s, q, t);
        }
      }
    }

    @Override
    protected void partitioniImpl(final IntBiPredicate<V> f, final Ref<Tree<V>> t0, final Ref<Tree<V>> t1)
    {
      mLeft.partitioniImpl(f, t0, t1);
      final Tree<V> l0 = t0.r, l1 = t1.r;
      mRight.partitioniImpl(f, t0, t1);
      final Tree<V> r0 = t0.r, r1 = t1.r;

      t0.r = smartBranchNodeConstructor(mPrefix, mBranchingBit, l0, r0);
      t1.r = smartBranchNodeConstructor(mPrefix, mBranchingBit, l1, r1);

      return;
    }

    @Override
    protected final void appEntry(final Consumer<LeafNode<V>> c) {
      mLeft.appEntry(c);
      mRight.appEntry(c);

      return;
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
        return create(prefix, branchingBit, left, right);
      }
    }

    private BranchNode(final int prefix,
                       final int branchingBit,
                       final Tree<V> left,
                       final Tree<V> right) {
      mPrefix       = prefix;
      mBranchingBit = branchingBit;
      mLeft         = left;
      mRight        = right;
    }

    private static <V> BranchNode<V> create(final int prefix,
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

  private static boolean matchPrefix(final int k, final int p, final int m) {
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

    return BranchNode.create(pnew, m, nt0, nt1);
  }

  public static <V> Tree<V> empty() {
    return EmptyNode.create();
  }

  public static <V> Tree<V> singleton(final int key, final V value) {
    return LeafNode.create(key, value);
  }

  public static <V> Tree<V> fromStrictlyIncreasingStream(final Stream<PersistentMapIntEntry<V>> stream) {
    return fromStream(stream);
  }

  public static <V> Tree<V> fromStrictlyDecreasingStream(final Stream<PersistentMapIntEntry<V>> stream) {
    return fromStream(stream);
  }

  public static <V> Tree<V> fromStream(final Stream<PersistentMapIntEntry<V>> stream) {
    return stream.reduce(EmptyNode.<V> create(),
                         (final Tree<V> t, final PersistentMapIntEntry<V> e) -> t.insert(e.getKey(), e.getValue()),
                         (t1, t2) -> { throw new AssertionError("Must not be called.  The stream was sequential."); });
  }

  public static <V> Tree<V> fromStrictlyIncreasingArray(final ArrayList<PersistentMapIntEntry<V>> v) {
    return fromArray(v);
  }

  public static <V> Tree<V> fromStrictlyDecreasingArray(final ArrayList<PersistentMapIntEntry<V>> v) {
    return fromArray(v);
  }

  public static <V> Tree<V> fromArray(final ArrayList<PersistentMapIntEntry<V>> v) {
    return fromStream(v.stream());
  }

  /**
   *
   * @param <V>
   */
  public static final class IntMapFactory<V> implements PersistentMapIntFactory<V, Tree<V>> {
    @Override
    public String getMapName() {
      return "IntMap";
    }

    @Override
    public Tree<V> empty() {
      return IntMapModule.empty();
    }

    @Override
    public Tree<V> singleton(final int key, final V value) {
      return IntMapModule.singleton(key, value);
    }

    @Override
    public Tree<V> fromStrictlyIncreasingStream(final Stream<PersistentMapIntEntry<V>> stream) {
      return IntMapModule.fromStrictlyIncreasingStream(stream);
    }

    @Override
    public Tree<V> fromStrictlyDecreasingStream(final Stream<PersistentMapIntEntry<V>> stream) {
      return IntMapModule.fromStrictlyDecreasingStream(stream);
    }

    @Override
    public Tree<V> fromStream(final Stream<PersistentMapIntEntry<V>> stream) {
      return IntMapModule.fromStream(stream);
    }

    @Override
    public Tree<V> fromStrictlyIncreasingArray(final ArrayList<PersistentMapIntEntry<V>> v) {
      return IntMapModule.fromStrictlyIncreasingArray(v);
    }

    @Override
    public Tree<V> fromStrictlyDecreasingArray(final ArrayList<PersistentMapIntEntry<V>> v) {
      return IntMapModule.fromStrictlyDecreasingArray(v);
    }

    @Override
    public Tree<V> fromArray(final ArrayList<PersistentMapIntEntry<V>> v) {
      return IntMapModule.fromArray(v);
    }
  }

  private static final IntMapFactory<?> sIntMapFactory = new IntMapFactory<>();

  @SuppressWarnings("unchecked")
  public static <K extends Comparable<K>, V> IntMapFactory<V> makeFactory() {
    return (IntMapFactory<V>) sIntMapFactory;
  }
}
