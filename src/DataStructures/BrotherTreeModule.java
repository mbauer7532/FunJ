/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.ArrayUtils;
import Utils.Functionals;
import Utils.Functionals.TriFunction;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.StructureGraphic.v1.DSTreeNode;

/**
 *
 * @author Neo
 */
public final class BrotherTreeModule {
  public abstract static class Tree<K extends Comparable<K>, V>
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
    public final Tree<K, V> insert(final BiFunction<V, V, V> f, final K a, final V v) {
      return root_ins(ins(f, a, v));
    }

    private static final class ControlExnNoSuchElement extends Exception {};

    protected static final ControlExnNoSuchElement sNoSuchElement = new ControlExnNoSuchElement();

    @Override
    public final Tree<K, V> remove(final K a) {
      try {
        final Pair<K, V> minPair = Pair.create(null, null);
        return root_del(del(minPair, a));
      }
      catch (ControlExnNoSuchElement ex) {
        return this;
      }
    }

    @Override
    public final Tree<K, V> merge(final BiFunction<V, V, V> f, final PersistentMap<K, V, Tree<K, V>> t) {
      return fromStrictlyIncreasingArray(mergeArrays(f, keyValuePairs(), t.keyValuePairs()));
    }

    @Override
    public final Optional<PersistentMapEntry<K, V>> lowerPair(final K key) {
      return lowerPair(this, key);
    }

    @Override
    public final Optional<PersistentMapEntry<K, V>> higherPair(final K key) {
      return higherPair(this, key);
    }

    @Override
    public abstract <W> Tree<K, W> mapi(final BiFunction<K, V, W> f);

    @Override
    public final <W> Tree<K, W> map(final Function<V, W> f) {
      return mapi((k, v) -> f.apply(v));
    }

    @Override
    public Pair<Boolean, String> verifyMapProperties() {
      return verifyBrotherTreeProperties(this);
    }

    protected abstract Tree<K, V> ins(final BiFunction<V, V, V> f, final K a, final V v);
    protected abstract Tree<K, V> del(final Pair<K, V> minPair, final K a) throws ControlExnNoSuchElement;
    protected abstract Tree<K, V> splitMin(final Pair<K, V> minPair);

    boolean isN0() { return false; }
    boolean isN1() { return false; }
    boolean isN2() { return false; }
    boolean isN3() { return false; }
    boolean isL2() { return false; }

    N0<K, V> asN0() { return null; }
    N1<K, V> asN1() { return null; }
    N2<K, V> asN2() { return null; }
    N3<K, V> asN3() { return null; }
    L2<K, V> asL2() { return null; }

    private static <K extends Comparable<K>, V> Tree<K, V> root_del(final Tree<K,V> t) {
      final N1<K, V> n = t.asN1();
      if (n != null) {
        return n.mt;
      }
      else {
        return t;
      }
    }

    private static <K extends Comparable<K>, V> Tree<K, V> root_ins(final Tree<K,V> t) {
      return n1_aux(t, false);
    }

    private static <K extends Comparable<K>, V> Optional<PersistentMapEntry<K, V>> lowerPair(final Tree<K, V> t, final K key) {
      Tree<K, V> tree = t;
      N2<K, V> candidate = null;

      while (! tree.isEmpty()) {
        final N1<K, V> n = tree.asN1();
        if (n != null) {
          tree = n.mt;
          if (tree.isEmpty()) {  // Here we could just continue; but that would cause us to check that mt is not an N1
            break;               // and we already know from the brother condition that it cannot be.  So this is just a
          }                      // small optimization.  Mt must be either N0 or N2 and not N1.
        }

        final N2<K, V> n2 = (N2<K, V>) tree;
        int res = key.compareTo(n2.ma1);
        if (res > 0) {
          tree = n2.mt2;
          candidate = n2;
        }
        else if (res < 0) {
          tree = n2.mt1;
        }
        else {
          final Optional<PersistentMapEntry<K, V>> p = n2.mt1.maxElementPair();
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

    private static <K extends Comparable<K>, V> Optional<PersistentMapEntry<K, V>> higherPair(final Tree<K, V> t, final K key) {
      Tree<K, V> tree = t;
      N2<K, V> candidate = null;

      while (! tree.isEmpty()) {
        final N1<K, V> n = tree.asN1();
        if (n != null) {
          tree = n.mt;
          if (tree.isEmpty()) {  // Here we could just continue; but that would cause us to check that mt is not an N1
            break;               // and we already know from the brother condition that it cannot be.  So this is just a
          }                      // small optimization.  Mt must be either N0 or N2 and not N1.
        }

        final N2<K, V> n2 = (N2<K, V>) tree;
        int res = key.compareTo(n2.ma1);
        if (res > 0) {
          tree = n2.mt2;
        }
        else if (res < 0) {
          tree = n2.mt1;
          candidate = n2;
        }
        else {
          final Optional<PersistentMapEntry<K, V>> p = n2.mt2.minElementPair();
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

    private static <K extends Comparable<K>, V> Tree<K, V> n1_aux(final Tree<K,V> t, final boolean boxN1) {
      final L2<K, V> l2t;
      final N3<K, V> n3t;

      if ((l2t = t.asL2()) != null) {
        final K a1 = l2t.ma1;
        final V v1 = l2t.mv1;
        final Tree<K, V> cn0 = N0.create();

        return N2.create(cn0, a1, v1, cn0);
      }
      else if ((n3t = t.asN3()) != null) {
        final Tree<K, V> t1 = n3t.mt1, t2 = n3t.mt2, t3 = n3t.mt3;
        final K a1 = n3t.ma1, a2 = n3t.ma2;
        final V v1 = n3t.mv1, v2 = n3t.mv2;

        return N2.create(N2.create(t1, a1, v1, t2), a2, v2, N1.create(t3));
      }
      else {
        return boxN1 ? N1.create(t) : t;
      }
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
      public final Color DSgetColor() {
      return Color.BLACK;
    }
  }

  private static final class N0<K extends Comparable<K>, V> extends Tree<K, V> {
    @SuppressWarnings("unchecked")
    public static final <K extends Comparable<K>, V> N0<K, V> create() { return (N0<K, V>) sN0; }

    static final N0<? extends Comparable<?>, ?> sN0 = new N0<>();

    @Override
    public boolean isEmpty() {
      return true;
    }

    @Override
    protected Tree<K, V> ins(final BiFunction<V, V, V> f, final K a, final V v) {
      return L2.create(a, v);
    }

    @Override
    protected Tree<K, V> del(final Pair<K, V> minPair, final K a) throws Tree.ControlExnNoSuchElement {
      // This exception is used for control purposes.
      // When removing non-existent elements we simply return the same input tree
      // no new allocations take place.
      throw Tree.sNoSuchElement;
    }

    @Override
    protected Tree<K, V> splitMin(final Pair<K, V> minPair) {
      return null;
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
    public <W> Tree<K, W> mapi(final BiFunction<K, V, W> f) {
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
    public boolean containsValue(final V value) {
      return false;
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
      return "N0";
    }

    @Override
    boolean isN0() { return true; }

    @Override
    N0<K, V> asN0() { return this; }
  }

  private static final class N1<K extends Comparable<K>, V> extends Tree<K, V> {
    public static <K extends Comparable<K>, V> N1<K, V> create(final Tree<K, V> t) {
      return new N1<>(t);
    }

    private N1(final Tree<K, V> t) {
      mt = t;
    }

    public final Tree<K, V> mt;

    @Override
    protected Tree<K, V> ins(final BiFunction<V, V, V> f, final K a, final V v) {
      return n1(mt.ins(f, a, v));
    }

    @Override
    protected Tree<K, V> del(final Pair<K, V> minPair, final K a)  throws Tree.ControlExnNoSuchElement {
      final Tree<K, V> t = mt.del(minPair, a);
      return create(t);
    }

    @Override
    protected Tree<K, V> splitMin(final Pair<K, V> minPair) {
      final Tree<K, V> mtTree = mt.splitMin(minPair);

      return mtTree == null ? null : create(mtTree);
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public Optional<V> get(final K key) {
      return mt.get(key);
    }

    @Override
    public int size() {
      return mt.size();
    }

    @Override
    public int height() {
      return mt.height() + 1;
    }

    @Override
    public void appi(final BiConsumer<K, V> f) {
      mt.appi(f);

      return;
    }

    @Override
    public void appEntry(final Consumer<PersistentMapBase<K, V, Tree<K, V>>> f) {
      mt.appEntry(f);
    }

    @Override
    public <W> Tree<K, W> mapi(final BiFunction<K, V, W> f) {
      return N1.create(mt.mapi(f));
    }

    @Override
    public <W> W foldli(final TriFunction<K, V, W, W> f, final W w) {
      return mt.foldli(f, w);
    }

    @Override
    public <W> W foldri(final TriFunction<K, V, W, W> f, final W w) {
      return mt.foldri(f, w);
    }

    @Override
    public boolean containsValue(final V value) {
      return mt.containsValue(value);
    }

    @Override
    public Optional<PersistentMapEntry<K, V>> minElementPair() {
      return mt.minElementPair();
    }

    @Override
    public Optional<PersistentMapEntry<K, V>> maxElementPair() {
      return mt.maxElementPair();
    }

    @Override
    public DSTreeNode[] DSgetChildren() {
      return new DSTreeNode[] { mt };
    }

    @Override
    public Object DSgetValue() {
      return "N1";
    }

    @Override
    boolean isN1() { return true; }

    @Override
    N1<K, V> asN1() { return this; }

    private static <K extends Comparable<K>, V> Tree<K, V> n1(final Tree<K,V> t) {
      return Tree.n1_aux(t, true);
    }
  }

  private static final class N2<K extends Comparable<K>, V> extends Tree<K, V> {
    public static <K extends Comparable<K>, V> N2<K, V> create(final Tree<K, V> t1, final K a1, final V v1, final Tree<K, V> t2) {
      return new N2<>(t1, a1, v1, t2);
    }

    private N2(final Tree<K, V> t1, final K a1, final V v1, final Tree<K, V> t2) {
      mt1 = t1;
      mt2 = t2;
      ma1 = a1;
      mv1 = v1;
    }

    public final Tree<K, V> mt1;
    public final K ma1;
    public final V mv1;
    public final Tree<K, V> mt2;

    @Override
    public K getKey() {
      return ma1;
    }

    @Override
    public V getValue() {
      return mv1;
    }

    @Override
    protected Tree<K, V> ins(final BiFunction<V, V, V> f, final K a, final V v) {
      final int res = a.compareTo(ma1);
      if (res < 0) {
        return n2_ins(mt1.ins(f, a, v), ma1, mv1, mt2);
      }
      else if (res > 0) {
        return n2_ins(mt1, ma1, mv1, mt2.ins(f, a, v));
      }
      else {
        return create(mt1, a, v, mt2);
      }
    }

    @Override
    protected Tree<K, V> del(final Pair<K, V> minPair, final K a) throws Tree.ControlExnNoSuchElement {
      final int res = a.compareTo(this.ma1);
      if (res < 0) {
        return n2_del(mt1.del(minPair, a), ma1, mv1, mt2);
      }
      else if (res > 0) {
        return n2_del(mt1, ma1, mv1, mt2.del(minPair, a));
      }
      else {
        final Tree<K, V> minTree = mt2.splitMin(minPair);

        return minTree == null
               ? N1.create(mt1)
               : n2_del(mt1, minPair.mx1, minPair.mx2, minTree);
      }
    }

    @Override
    protected Tree<K, V> splitMin(final Pair<K, V> minPair) {
      final Tree<K, V> minTree = mt1.splitMin(minPair);
      if (minTree == null) {
        minPair.mx1 = ma1;
        minPair.mx2 = mv1;
        return N1.create(mt2);
      }
      else {
        return n2_del(minTree, ma1, mv1, mt2);
      }
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public Optional<V> get(final K key) {
      final int res = key.compareTo(ma1);

      if (res < 0) {
        return mt1.get(key);
      }
      else if (res > 0) {
        return mt2.get(key);
      }
      else {
        return Optional.of(mv1);
      }      
    }

    @Override
    public int size() {
      return mt1.size() + mt2.size() + 1;
    }

    @Override
    public int height() {
      return 1 + mt1.height();   // These trees are fully symmetric so all we need to do is find the
    }                            // height of one branch.

    @Override
    public void appi(final BiConsumer<K, V> f) {
      mt1.appi(f);
      f.accept(ma1, mv1);
      mt2.appi(f);
      
      return;
    }

    @Override
    public void appEntry(final Consumer<PersistentMapBase<K, V, Tree<K, V>>> f) {
      mt1.appEntry(f);
      f.accept(this);
      mt2.appEntry(f);

      return;
    }

    @Override
    public <W> Tree<K, W> mapi(final BiFunction<K, V, W> f) {
      return create(mt1.mapi(f), ma1, f.apply(ma1, mv1), mt2.mapi(f));
    }

    @Override
    public <W> W foldli(final TriFunction<K, V, W, W> f, final W w) {
      return mt2.foldli(f, f.apply(ma1, mv1, mt1.foldli(f, w)));
    }

    @Override
    public <W> W foldri(final TriFunction<K, V, W, W> f, final W w) {
      return mt1.foldri(f, f.apply(ma1, mv1, mt2.foldri(f, w)));
    }

    @Override
    public boolean containsValue(final V value) {
      return mv1.equals(value)
              || mt1.containsValue(value)
              || mt2.containsValue(value);
    }

    @Override
    public Optional<PersistentMapEntry<K, V>> minElementPair() {
      N1<K, V> n;
      if (mt1.isN0() || ((n = mt1.asN1()) != null && n.mt.isN0())) {
        return Optional.of(new EntryRef<>(this));
      }
      else {
        return mt1.minElementPair();
      }
    }

    @Override
    public Optional<PersistentMapEntry<K, V>> maxElementPair() {
      N1<K, V> n;
      if (mt2.isN0() || ((n = mt2.asN1()) != null && n.mt.isN0())) {
        return Optional.of(new EntryRef<>(this));
      }
      else {
        return mt2.maxElementPair();
      }
    }

    @Override
    public DSTreeNode[] DSgetChildren() {
      return new DSTreeNode[] { mt1, mt2 };
    }

    @Override
    public Object DSgetValue() {
      return String.format("N2(%s,%s)", ma1.toString(), mv1.toString());
    }

    @Override
    boolean isN2() { return true; }

    @Override
    N2<K, V> asN2() { return this; }

    private static <K extends Comparable<K>, V> Tree<K, V> n2_ins(final Tree<K,V> left, final K a, final V v, final Tree<K, V> right) {
      final L2<K, V> lt;

      if ((lt = left.asL2()) != null) {
        final Tree<K, V> t1 = right;
        final K a1 = lt.ma1, a2 = a;
        final V v1 = lt.mv1, v2 = v;
        final Tree<K, V> cn0 = N0.create();

        return N3.create(cn0, a1, v1, cn0, a2, v2, t1);
      }

      N3<K, V> n3t;
      if ((n3t = left.asN3()) != null) {
        final N1<K, V> n1t;

        if ((n1t = right.asN1()) != null) {
          final Tree<K, V> t1 = n3t.mt1, t2 = n3t.mt2, t3 = n3t.mt3;
          final K a1 = n3t.ma1, a2 = n3t.ma2, a3 = a;
          final V v1 = n3t.mv1, v2 = n3t.mv2, v3 = v;

          final Tree<K, V> t4 = n1t.mt;

          return N2.create(N2.create(t1, a1, v1, t2), a2, v2, N2.create(t3, a3, v3, t4));
        }
        else if (right.isN2()) {
          final Tree<K, V> t1 = n3t.mt1, t2 = n3t.mt2, t3 = n3t.mt3;
          final K a1 = n3t.ma1, a2 = n3t.ma2, a3 = a;
          final V v1 = n3t.mv1, v2 = n3t.mv2, v3 = v;
          final Tree<K, V> t4 = right;

          return N3.create(N2.create(t1, a1, v1, t2), a2, v2, N1.create(t3), a3, v3, t4);
        }
      }

      final L2<K, V> rt;
      if ((rt = right.asL2()) != null) {
        final Tree<K, V> t1 = left;
        final K a1 = a, a2 = rt.ma1;
        final V v1 = v, v2 = rt.mv1;
        final Tree<K, V> cn0 = N0.create();

        return N3.create(t1, a1, v1, cn0, a2, v2, cn0);
      }

      if ((n3t = right.asN3()) != null) {
        final N1<K, V> n1t;
        if ((n1t = left.asN1()) != null) {
          final Tree<K, V> t2 = n3t.mt1, t3 = n3t.mt2, t4 = n3t.mt3;
          final K a1 = a, a2 = n3t.ma1, a3 = n3t.ma2;
          final V v1 = v, v2 = n3t.mv1, v3 = n3t.mv2;

          final Tree<K, V> t1 = n1t.mt;

          return N2.create(N2.create(t1, a1, v1, t2), a2, v2, N2.create(t3, a3, v3, t4));
        }
        else if (left.isN2())
        {
          final Tree<K, V> t2 = n3t.mt1, t3 = n3t.mt2, t4 = n3t.mt3;
          final K a1 = a, a2 = n3t.ma1, a3 = n3t.ma2;
          final V v1 = v, v2 = n3t.mv1, v3 = n3t.mv2;
          final Tree<K, V> t1 = left;

          return N3.create(t1, a1, v1, N1.create(t2), a2, v2, N2.create(t3, a3, v3, t4));
        }
      }

      final Tree<K, V> t1 = left, t2 = right;
      final K a1 = a;
      final V v1 = v;

      return N2.create(t1, a1, v1, t2);
    }

    private static <K extends Comparable<K>, V> Tree<K, V> n2_del(final Tree<K,V> left, final K a, final V v, final Tree<K, V> right) {
      N1<K, V> ln1 = left.asN1(), rn1;
      final N2<K, V> ln2, rn2;

      if (ln1 != null && ((rn1 = right.asN1()) != null)) {
        final Tree<K, V> t1 = ln1.mt;
        final Tree<K, V> t2 = rn1.mt;

        return c1(t1, a, v, t2);
      }
      else if (ln1 != null && ((rn2 = right.asN2()) != null)) {
        final N1<K, V> ln1mtn1 = ln1.mt.asN1(), rn2mt2n1;

        if (ln1mtn1 != null) {
          if (rn2.mt2.isN2()) {
            final N1<K, V> rn2mt1n1 = rn2.mt1.asN1();
            if (rn2mt1n1 != null) {
              final Tree<K, V> t1 = ln1mtn1.mt;
              final Tree<K, V> t2 = rn2mt1n1.mt;
              final Tree<K, V> t3 = rn2.mt2;
              final K a1 = a, a2 = rn2.ma1;
              final V v1 = v, v2 = rn2.mv1;

              return c2(t1, a1, v1, t2, a2, v2, t3);
            }
            else if (rn2.mt1.isN2()) {
              final Tree<K, V> t1 = ln1.mt;
              final Tree<K, V> t2 = rn2.mt1;
              final Tree<K, V> t3 = rn2.mt2;
              final K a1 = a, a2 = rn2.ma1;
              final V v1 = v, v2 = rn2.mv1;

              return c4(t1, a1, v1, t2, a2, v2, t3);
            }
          }
          else if ((rn2mt2n1 = rn2.mt2.asN1()) != null) {
            final N2<K, V> rn2mt1n2 = rn2.mt1.asN2();
            if (rn2mt1n2 != null) {
              final N2<K, V> z1 = rn2mt1n2;
              final N1<K, V> z2 = rn2mt2n1;

              final Tree<K, V> t1 = ln1mtn1.mt;
              final Tree<K, V> t2 = z1.mt1;
              final Tree<K, V> t3 = z1.mt2;
              final Tree<K, V> t4 = z2.mt;
              final K a1 = a, a2 = z1.ma1, a3 = rn2.ma1;
              final V v1 = v, v2 = z1.mv1, v3 = rn2.mv1;

              return c3(t1, a1, v1, t2, a2, v2, t3, a3, v3, t4);
            }
          }
        }
      }
      else if ((ln2 = left.asN2()) != null && ((rn1 = right.asN1()) != null)) {
        final N1<K, V> rn1mtn1 = rn1.mt.asN1();
        final N2<K, V>ln2mt2n2;

        if (rn1mtn1 != null) {
          final N1<K, V> ln2mt2n1 = ln2.mt2.asN1();
          if (ln2mt2n1 != null) {
            if (ln2.mt1.isN2()) {
              final Tree<K, V> t1 = ln2.mt1;
              final Tree<K, V> t2 = ln2mt2n1.mt;
              final Tree<K, V> t3 = rn1mtn1.mt;
              final K a1 = ln2.ma1, a2 = a;
              final V v1 = ln2.mv1, v2 = v;

              return c6(t1, a1, v1, t2, a2, v2, t3);
            }
          }
          else if ((ln2mt2n2 = ln2.mt2.asN2()) != null) {
            final N1<K, V> ln2mt1n1 = ln2.mt1.asN1();
            if (ln2mt1n1 != null) {
              final N2<K, V> z = ln2mt2n2;
              final Tree<K, V> t1 = ln2mt1n1.mt;
              final Tree<K, V> t2 = z.mt1;
              final Tree<K, V> t3 = z.mt2;
              final Tree<K, V> t4 = rn1mtn1.mt;
              final K a1 = ln2.ma1, a2 = z.ma1, a3 = a;
              final V v1 = ln2.mv1, v2 = z.mv1, v3 = v;

              return c5(t1, a1, v1, t2, a2, v2, t3, a3, v3, t4);
            }
            else if (ln2.mt1.isN2()) {
              final Tree<K, V> t1 = ln2.mt1;
              final Tree<K, V> t2 = ln2.mt2;
              final Tree<K, V> t3 = rn1.mt;
              final K a1 = ln2.ma1, a2 = a;
              final V v1 = ln2.mv1, v2 = v;

              return c7(t1, a1, v1, t2, a2, v2, t3);
            }
          }
        }
      }

      return c8(left, a, v, right);
    }

    // Case (1)
    private static <K extends Comparable<K>, V> Tree<K, V> c1(final Tree<K, V> t1, final K a, final V v, final Tree<K, V> t2) {
      return N1.create(N2.create(t1, a, v, t2));
    }
    // Case (2)
    private static <K extends Comparable<K>, V> Tree<K, V> c2(final Tree<K, V> t1, final K a1, final V v1, final Tree<K, V> t2, final K a2, final V v2, final Tree<K, V> t3) {
      return N1.create(N2.create(N2.create(t1, a1, v1, t2), a2, v2, t3));
    }
    // Case (3)
    private static <K extends Comparable<K>, V> Tree<K, V> c3(final Tree<K, V> t1, final K a1, final V v1, final Tree<K, V> t2, final K a2, final V v2, final Tree<K, V> t3, final K a3, final V v3, final Tree<K, V> t4) {
      return N1.create(N2.create(N2.create(t1, a1, v1, t2), a2, v2, N2.create(t3, a3, v3, t4)));
    }
    // Case (4)
    private static <K extends Comparable<K>, V> Tree<K, V> c4(final Tree<K, V> t1, final K a1, final V v1, final Tree<K, V> t2, final K a2, final V v2, final Tree<K, V> t3) {
      return N2.create(N2.create(t1, a1, v1, t2), a2, v2, N1.create(t3));
    }
    // Case (5)
    private static <K extends Comparable<K>, V> Tree<K, V> c5(final Tree<K, V> t1, final K a1, final V v1, final Tree<K, V> t2, final K a2, final V v2, final Tree<K, V> t3, final K a3, final V v3, final Tree<K, V> t4) {
      return c3(t1, a1, v1, t2, a2, v2, t3, a3, v3, t4);
    }
    // Case (6)
    private static <K extends Comparable<K>, V> Tree<K, V> c6(final Tree<K, V> t1, final K a1, final V v1, final Tree<K, V> t2, final K a2, final V v2, final Tree<K, V> t3) {
      return N1.create(N2.create(t1, a1, v1, N2.create(t2, a2, v2, t3)));
    }
    // Case (7)
    private static <K extends Comparable<K>, V> Tree<K, V> c7(final Tree<K, V> t1, final K a1, final V v1, final Tree<K, V> t2, final K a2, final V v2, final Tree<K, V> t3) {
      return N2.create(N1.create(t1), a1, v1, (N2.create(t2, a2, v2, t3)));
    }
    // Case (8)
    private static <K extends Comparable<K>, V> Tree<K, V> c8(final Tree<K, V> t1, final K a, final V v, final Tree<K, V> t2) {
      return N2.create(t1, a, v, t2);
    }
  }

  private static final class N3<K extends Comparable<K>, V> extends Tree<K, V> {
    public static <K extends Comparable<K>, V> N3<K, V> create(
            final Tree<K, V> t1,
            final K a1,
            final V v1,
            final Tree<K, V> t2,
            final K a2,
            final V v2,
            final Tree<K, V> t3) {
      return new N3<>(t1, a1, v1, t2, a2, v2, t3);
    }

    private N3(final Tree<K, V> t1, final K a1, final V v1, final Tree<K, V> t2, final K a2, final V v2, final Tree<K, V> t3) {
      mt1 = t1;
      ma1 = a1;
      mv1 = v1;
      mt2 = t2;
      ma2 = a2;
      mv2 = v2;
      mt3 = t3;
    }

    public final Tree<K, V> mt1;
    public final K ma1;
    public final V mv1;
    public final Tree<K, V> mt2;
    public final K ma2;
    public final V mv2;
    public final Tree<K, V> mt3;

    private final static AssertionError sTreeStructureError = new AssertionError("Error in tree structure.  N3 notes encountered where it shouldn't be.");

    @Override
    protected Tree<K, V> ins(final BiFunction<V, V, V> f, final K a, final V v) {
      throw sTreeStructureError;
    }

    @Override
    protected Tree<K, V> del(final Pair<K, V> minPair, final K a) throws Tree.ControlExnNoSuchElement {
      throw sTreeStructureError;
    }

    @Override
    protected Tree<K, V> splitMin(final Pair<K, V> minPair) {
      throw sTreeStructureError;
    }

    @Override
    public boolean isEmpty() {
      throw sTreeStructureError;
    }

    @Override
    public Optional<V> get(K key) {
      throw sTreeStructureError;
    }

    @Override
    public int size() {
      throw sTreeStructureError;
    }

    @Override
    public int height() {
      throw sTreeStructureError;
    }

    @Override
    public void appi(BiConsumer<K, V> f) {
      throw sTreeStructureError;
    }

    @Override
    public void appEntry(final Consumer<PersistentMapBase<K, V, Tree<K, V>>> f) {
      throw sTreeStructureError;
    }

    @Override
    public <W> Tree<K, W> mapi(BiFunction<K, V, W> f) {
      throw sTreeStructureError;
    }

    @Override
    public <W> W foldli(TriFunction<K, V, W, W> f, W w) {
      throw sTreeStructureError;
    }

    @Override
    public <W> W foldri(TriFunction<K, V, W, W> f, W w) {
      throw sTreeStructureError;
    }

    @Override
    public boolean containsValue(final V value) {
      throw sTreeStructureError;
    }

    @Override
    public Optional<PersistentMapEntry<K, V>> minElementPair() {
      throw sTreeStructureError;
    }

    @Override
    public Optional<PersistentMapEntry<K, V>> maxElementPair() {
      throw sTreeStructureError;
    }

    @Override
    public DSTreeNode[] DSgetChildren() {
      return new DSTreeNode[] { mt1, mt2, mt3 };
    }

    @Override
    public Object DSgetValue() {
      return String.format("N2(%s,%s,%s,%s)",
              ma1.toString(), mv1.toString(),
              ma2.toString(), mv2.toString());
    }

    @Override
    boolean isN3() { return true; }

    @Override
    N3<K, V> asN3() { return this; }
}

  private static final class L2<K extends Comparable<K>, V> extends Tree<K, V> {
    public static <K extends Comparable<K>, V> L2<K, V> create(final K a1, final V v1) {
      return new L2<>(a1, v1);
    }

    private L2(final K a1, final V v1) {
      ma1 = a1;
      mv1 = v1;
    }

    public final K ma1;
    public final V mv1;

    private final static AssertionError sTreeStructureError = new AssertionError("Error in tree structure.  L2 notes encountered where it shouldn't be.");

    @Override
    protected Tree<K, V> ins(final BiFunction<V, V, V> f, final K a, final V v) {
      throw sTreeStructureError;
    }

    @Override
    protected Tree<K, V> del(final Pair<K, V> minPair, K a) throws Tree.ControlExnNoSuchElement {
      throw sTreeStructureError;
    }

    @Override
    protected Tree<K, V> splitMin(final Pair<K, V> minPair) {
      throw sTreeStructureError;
    }

    @Override
    public boolean isEmpty() {
      throw sTreeStructureError;
    }

    @Override
    public Optional<V> get(K key) {
      throw sTreeStructureError;
    }

    @Override
    public int size() {
      throw sTreeStructureError;
    }

    @Override
    public int height() {
      throw sTreeStructureError;
    }

    @Override
    public void appi(BiConsumer<K, V> f) {
      throw sTreeStructureError;
    }

    @Override
    public void appEntry(final Consumer<PersistentMapBase<K, V, Tree<K, V>>> f) {
      throw sTreeStructureError;
    }

    @Override
    public <W> Tree<K, W> mapi(BiFunction<K, V, W> f) {
      throw sTreeStructureError;
    }

    @Override
    public <W> W foldli(TriFunction<K, V, W, W> f, W w) {
      throw sTreeStructureError;
    }

    @Override
    public <W> W foldri(TriFunction<K, V, W, W> f, W w) {
      throw sTreeStructureError;
    }

    @Override
    public boolean containsValue(final V value) {
      throw sTreeStructureError;
    }

    @Override
    public Optional<PersistentMapEntry<K, V>> minElementPair() {
      throw sTreeStructureError;
    }

    @Override
    public Optional<PersistentMapEntry<K, V>> maxElementPair() {
      throw sTreeStructureError;
    }

    @Override
    public DSTreeNode[] DSgetChildren() {
      return new DSTreeNode[0];
    }

    @Override
    public Object DSgetValue() {
      return String.format("L2(%s,%s)", ma1.toString(), mv1.toString());
    }

    @Override
    boolean isL2() { return true; }

    @Override
    L2<K, V> asL2() { return this; }
  }

  public static <K extends Comparable<K>, V> Tree<K, V> fromStrictlyIncreasingStream(final Stream<PersistentMapEntry<K, V>> stream) {
    return fromStrictlyIncreasingArray(stream.collect(Collectors.toCollection(ArrayList::new)));
  }

  public static <K extends Comparable<K>, V> Tree<K, V> fromStrictlyDecreasingStream(final Stream<PersistentMapEntry<K, V>> stream) {
    return fromSpine(BrotherTreeModule.<K, V> empty(),
                     stream.reduce(Nil.create(),
                                   (s, p) -> cons(p.getKey(), p.getValue(), s),
                                   Functionals::functionShouldNotBeCalled));
  }

  public static <K extends Comparable<K>, V> Tree<K, V> fromStrictlyIncreasingArray(final ArrayList<PersistentMapEntry<K, V>> v) {
    final int n = v.size();
    return fromStrictlyDecreasingStream(IntStream.rangeClosed(1, n).mapToObj(idx -> v.get(n - idx)));
  }

  public static <K extends Comparable<K>, V> Tree<K, V> fromStrictlyDecreasingArray(final ArrayList<PersistentMapEntry<K, V>> v) {
    return fromStrictlyDecreasingStream(v.stream());
  }

  public static <K extends Comparable<K>, V> Tree<K, V> fromStream(final Stream<PersistentMapEntry<K, V>> stream) {
    return stream.reduce(empty(),
                         ((t, p) -> t.insert(p.getKey(), p.getValue())),
                         Functionals::functionShouldNotBeCalled);
  }

  public static <K extends Comparable<K>, V> Tree<K, V> fromArray(final ArrayList<PersistentMapEntry<K, V>> v) {
    return fromStream(v.stream());
  }

  public static <K extends Comparable<K>, V> Tree<K, V> empty() {
    return N0.create();
  }

  public static <K extends Comparable<K>, V> Tree<K, V> singleton(final K a, final V v) {
    Tree<K, V> e = empty();
    return N2.create(e, a, v, e);
  }

  private static enum SpineKind {
    NIL,
    HALF,
    FULL
  }

  private static class Spine<K extends Comparable<K>, V> {
    public Spine(final SpineKind spineKind) {
      mSpineKind = spineKind;
    }
    public SpineKind mSpineKind;
  }

  private static class Nil<K extends Comparable<K>, V> extends Spine<K, V> {
    @SuppressWarnings("unchecked")
    public static final <K extends Comparable<K>, V> Spine<K, V> create() { return (Spine<K, V>) sNil; }

    private Nil() { super(SpineKind.NIL); }
    private static final Nil<? extends Comparable<?>, ?> sNil = new Nil<>();
  }

  private static class HF<K extends Comparable<K>, V> extends Spine<K, V> {
    public K mKey;
    public V mValue;
    public Tree<K, V> mTree;
    public Spine<K, V> mSpine;

    public HF(final SpineKind spineKind, final K key, final V value, final Tree<K, V> tree, final Spine<K, V> spine) {
      super(spineKind);
      mKey = key;
      mValue = value;
      mTree = tree;
      mSpine = spine;
    }
  }

  private static <K extends Comparable<K>, V> Spine<K, V> cons(final K key, final V value, final Spine<K, V> spine) {
    final Spine<K, V> newSpine;

    if (spine.mSpineKind == SpineKind.FULL) {
      final HF<K, V> fs = ((HF<K, V>) spine);
      newSpine = half(fs.mKey, fs.mValue, fs.mTree, fs.mSpine);
    }
    else {
      newSpine = Nil.create();
    }

    return new HF<>(SpineKind.FULL, key, value, N0.create(), newSpine);
  }

  private static <K extends Comparable<K>, V> Spine<K, V> half(final K key, final V value, final Tree<K, V> tree, Spine<K, V> spine) {
    final SpineKind sKind = spine.mSpineKind;

    if (sKind == SpineKind.NIL) {
      return new HF<>(SpineKind.HALF, key, value, tree, Nil.create());
    }
    else {
      final HF<K, V> hf = (HF<K, V>) spine;

      if (sKind == SpineKind.HALF) {
        return new HF<>(SpineKind.FULL, key, value, N2.create(tree, hf.mKey, hf.mValue, hf.mTree), hf.mSpine);
      }
      else {
        return new HF<>(SpineKind.HALF, key, value, tree, half(hf.mKey, hf.mValue, hf.mTree, hf.mSpine));
      }
    }
  }

  private static <K extends Comparable<K>, V> Tree<K, V> fromSpine(final Tree<K, V> t1, final Spine<K, V> spine) {
    final SpineKind sKind = spine.mSpineKind;

    if (sKind == SpineKind.NIL) {
      return t1;
    }
    else {
      final HF<K, V> hf        = (HF<K, V>) spine;
      final K key              = hf.mKey;
      final V value            = hf.mValue;
      final Tree<K, V> t2      = hf.mTree;
      final Spine<K, V> s      = hf.mSpine;
      final Tree<K, V> newTree = sKind == SpineKind.HALF ? N1.create(t2) : t2;

      return fromSpine(N2.create(t1, key, value, newTree), s);
    }
  }

  private static <K extends Comparable<K>, V> boolean binaryTreePropertyHolds(final Tree<K, V> t) {
    return ArrayUtils.isStrictlyIncreasing(t.keys());
  }

  private static <K extends Comparable<K>, V> int findDepthWithCombiningFunction(final IntBinaryOperator f, final Tree<K, V> t) {
    final N1<K, V> n1;
    final N2<K, V> n2;

    if (t.isN0()) {
      return 0;
    }
    else if ((n1 = t.asN1()) != null) {
      return 1 + findDepthWithCombiningFunction(f, n1.mt);
    }
    else if ((n2 = t.asN2()) != null) {
      return 1 + f.applyAsInt(findDepthWithCombiningFunction(f, n2.mt1),
                              findDepthWithCombiningFunction(f, n2.mt2));
    }
    else {
      throw new AssertionError("Badly formed tree.");
    }
  }

  private static <K extends Comparable<K>, V> boolean depthOfN0NodesPropertyHolds(final Tree<K, V> t) {
    final int maxN0Depth = findDepthWithCombiningFunction(Math::max, t);
    final int minN0Depth = findDepthWithCombiningFunction(Math::min, t);

    return maxN0Depth == minN0Depth;
  }

  private static <K extends Comparable<K>, V> boolean brotherPropertyHolds(final Tree<K, V> t) {
    final N2<K, V> n2 = t.asN2();
    if (n2 != null) {
      final Tree<K, V> left = n2.mt1, right = n2.mt2;
      boolean isLeftN1  = left.isN1(),  isLeftN2  = left.isN2();
      boolean isRightN1 = right.isN1(), isRightN2 = right.isN2();

      if (isLeftN1 && ! isRightN2) {
        return false;
      }
      else if (isRightN1 && ! isLeftN2) {
        return false;
      }
      else {
        return brotherPropertyHolds(left) && brotherPropertyHolds(right);
      }
    }
    else if (t.isN1()) {
      return brotherPropertyHolds(((N1<K, V>) t).mt);
    }
    else {
      return t.isN0();
    }
  }

  static <K extends Comparable<K>, V> Pair<Boolean, String> verifyBrotherTreeProperties(final Tree<K, V> t) {
    if (! binaryTreePropertyHolds(t)) {
      return Pair.create(false, "Binary Tree property does not hold.");
    }

    if (! depthOfN0NodesPropertyHolds(t)) {
      return Pair.create(false, "Depth of N0 nodes is not the same for all such nodes.");
    }
 
    if (! brotherPropertyHolds(t)) {
      return Pair.create(false, "The brother tree property does not hold.");
    }

    return Pair.create(true, "Success!");
  }

  public static final class BrotherTreeFactory<K extends Comparable<K>, V> implements PersistentMapFactory<K, V, Tree<K, V>> {
    @Override
    public String getMapName() {
      return "BrotherTreeMap";
    }

    @Override
    public Tree<K, V> empty() {
      return BrotherTreeModule.empty();
    }

    @Override
    public Tree<K, V> singleton(final K key, final V value) {
      return BrotherTreeModule.singleton(key, value);
    }

    @Override
    public Tree<K, V> fromStrictlyIncreasingStream(final Stream<PersistentMapEntry<K, V>> stream) {
      return BrotherTreeModule.fromStrictlyIncreasingStream(stream);
    }

    @Override
    public Tree<K, V> fromStrictlyDecreasingStream(final Stream<PersistentMapEntry<K, V>> stream) {
      return BrotherTreeModule.fromStrictlyDecreasingStream(stream);
    }

    @Override
    public Tree<K, V> fromArray(final ArrayList<PersistentMapEntry<K, V>> v) {
      return BrotherTreeModule.fromArray(v);
    }

    @Override
    public Tree<K, V> fromStrictlyIncreasingArray(final ArrayList<PersistentMapEntry<K, V>> v) {
      return BrotherTreeModule.fromStrictlyIncreasingArray(v);
    }

    @Override
    public Tree<K, V> fromStrictlyDecreasingArray(final ArrayList<PersistentMapEntry<K, V>> v) {
      return BrotherTreeModule.fromStrictlyDecreasingArray(v);
    }

    @Override
    public Tree<K, V> fromStream(final Stream<PersistentMapEntry<K, V>> stream) {
      return BrotherTreeModule.fromStream(stream);
    }
  }

  private static final BrotherTreeFactory<? extends Comparable<?>, ?> sBrotherTreeFactory = new BrotherTreeFactory<>();

  @SuppressWarnings("unchecked")
  public static <K extends Comparable<K>, V> BrotherTreeFactory<K, V> makeFactory() {
    return (BrotherTreeFactory<K, V>) sBrotherTreeFactory;
  }
}
