/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import DataStructures.TuplesModule.Triple;
import Utils.Functionals.TriFunction;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
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
      final Pair<ArrayList<Pair<K, V>>, ArrayList<Pair<K, V>>> elemsPair = splitElemsAccordingToPredicate(f);

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

    @Override
    public final Tree<K, V> remove(final K a) {
      return root_del(del(a));
    }

    @Override
    public final Tree<K, V> merge(final BiFunction<V, V, V> f, final Tree<K, V> t) {
      return fromStrictlyIncreasingArray(mergeArrays(f, keyValuePairs(), t.keyValuePairs()));
    }

    @Override
    public final Optional<Pair<K, V>> lowerPair(final K key) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public final Optional<Pair<K, V>> higherPair(final K key) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public abstract <W> Tree<K, W> mapi(final BiFunction<K, V, W> f);

    @Override
    public final <W> Tree<K, W> map(final Function<V, W> f) {
      return mapi((k, v) -> f.apply(v));
    }

    protected abstract Tree<K, V> ins(final BiFunction<V, V, V> f, final K a, final V v);
    protected abstract Tree<K, V> del(final K a);
    protected abstract Optional<Triple<K, V, Tree<K, V>>> splitMin();

    @Override
    public final Color DSgetColor() {
      return Color.BLACK;
    }
  }

  private static final class N0<K extends Comparable<K>, V> extends Tree<K, V> {
    @SuppressWarnings("unchecked")
    static final <K extends Comparable<K>, V> N0<K, V> create() { return (N0<K, V>) sN0; }

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
    protected Tree<K, V> del(final K a) {
      return this;
    }

    @Override
    protected Optional<Triple<K, V, Tree<K, V>>> splitMin() {
      return Optional.empty();
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
    public boolean containsValue(V value) {
      return false;
    }

    @Override
    public Optional<Pair<K, V>> minElementPair() {
      return Optional.empty();
    }

    @Override
    public Optional<Pair<K, V>> maxElementPair() {
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
  }

  private static final class N1<K extends Comparable<K>, V> extends Tree<K, V> {
    private static <K extends Comparable<K>, V> N1<K, V> create(final Tree<K, V> t) {
      return new N1<>(t);
    }

    private N1(final Tree<K, V> t) {
      mt = t;
    }

    private final Tree<K, V> mt;

    @Override
    protected Tree<K, V> ins(final BiFunction<V, V, V> f, final K a, final V v) {
      return n1(mt.ins(f, a, v));
    }

    @Override
    protected Tree<K, V> del(final K a) {
      final Tree<K, V> t = mt.del(a);
      return t != mt ? create(mt.del(a)) : this;
    }

    @Override
    protected Optional<Triple<K, V, Tree<K, V>>> splitMin() {
      return mt.splitMin().map(p -> Triple.create(p.mx1, p.mx2, create(p.mx3)));
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
    public boolean containsValue(V value) {
      return mt.containsValue(value);
    }

    @Override
    public Optional<Pair<K, V>> minElementPair() {
      return mt.minElementPair();
    }

    @Override
    public Optional<Pair<K, V>> maxElementPair() {
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
  }

  private static final class N2<K extends Comparable<K>, V> extends Tree<K, V> {
    private static <K extends Comparable<K>, V> N2<K, V> create(final Tree<K, V> t1, final K a1, final V v1, final Tree<K, V> t2) {
      return new N2<>(t1, a1, v1, t2);
    }

    private N2(final Tree<K, V> t1, final K a1, final V v1, final Tree<K, V> t2) {
      mt1 = t1;
      mt2 = t2;
      ma1 = a1;
      mv1 = v1;
    }

    private final Tree<K, V> mt1;
    private final K ma1;
    private final V mv1;
    private final Tree<K, V> mt2;

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
    protected Tree<K, V> del(final K a) {
      final int res = a.compareTo(this.ma1);
      if (res < 0) {
        return n2_del(mt1.del(a), ma1, mv1, mt2);
      }
      else if (res > 0) {
        return n2_del(mt1, ma1, mv1, mt2.del(a));
      }
      else {
        return ((N2<K,V>) mt2).splitMin()
                              .map(p -> n2_del(mt1, p.mx1, p.mx2, p.mx3))
                              .orElseGet(() -> N1.create(mt1));
      }
    }

    @Override
    protected Optional<Triple<K, V, Tree<K, V>>> splitMin() {
      return Optional.of(mt1.splitMin()
                            .map(p -> Triple.<K, V, Tree<K, V>> create(p.mx1, p.mx2, create(p.mx3, ma1, mv1, mt2)))
                            .orElseGet(() -> Triple.create(ma1, mv1, N1.create(mt2))));
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
    public boolean containsValue(V value) {
      return ma1.equals(value)
              || mt1.containsValue(value)
              || mt2.containsValue(value);
    }

    @Override
    public Optional<Pair<K, V>> minElementPair() {
       if (mt1.isEmpty()) {
        return Optional.of(Pair.create(ma1, mv1));
      }
      else {
        return mt1.minElementPair();
      }
    }

    @Override
    public Optional<Pair<K, V>> maxElementPair() {
      if (mt2.isEmpty()) {
        return Optional.of(Pair.create(ma1, mv1));
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
  }

  private static final class N3<K extends Comparable<K>, V> extends Tree<K, V> {
    private static <K extends Comparable<K>, V> N3<K, V> create(
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

    private final Tree<K, V> mt1;
    private final K ma1;
    private final V mv1;
    private final Tree<K, V> mt2;
    private final K ma2;
    private final V mv2;
    private final Tree<K, V> mt3;

    private final static AssertionError sTreeStructureError = new AssertionError("Error in tree structure.  N3 notes encountered where it shouldn't be.");

    @Override
    protected Tree<K, V> ins(final BiFunction<V, V, V> f, final K a, final V v) {
      throw sTreeStructureError;
    }

    @Override
    protected Tree<K, V> del(final K a) {
      throw sTreeStructureError;
    }

    @Override
    protected Optional<Triple<K, V, Tree<K, V>>> splitMin() {
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
    public boolean containsValue(V value) {
      throw sTreeStructureError;
    }

    @Override
    public Optional<Pair<K, V>> minElementPair() {
      throw sTreeStructureError;
    }

    @Override
    public Optional<Pair<K, V>> maxElementPair() {
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
}

  private static final class L2<K extends Comparable<K>, V> extends Tree<K, V> {
    private static <K extends Comparable<K>, V> L2<K, V> create(final K a1, final V v1) {
      return new L2<>(a1, v1);
    }

    private L2(final K a1, final V v1) {
      ma1 = a1;
      mv1 = v1;
    }

    private final K ma1;
    private final V mv1;

    private final static AssertionError sTreeStructureError = new AssertionError("Error in tree structure.  L2 notes encountered where it shouldn't be.");

    @Override
    protected Tree<K, V> ins(final BiFunction<V, V, V> f, final K a, final V v) {
      throw sTreeStructureError;
    }

    @Override
    protected Tree<K, V> del(K a) {
      throw sTreeStructureError;
    }

    @Override
    protected Optional<Triple<K, V, Tree<K, V>>> splitMin() {
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
    public boolean containsValue(V value) {
      throw sTreeStructureError;
    }

    @Override
    public Optional<Pair<K, V>> minElementPair() {
      throw sTreeStructureError;
    }

    @Override
    public Optional<Pair<K, V>> maxElementPair() {
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
  }

  private static <K extends Comparable<K>, V> Tree<K, V> root_ins(final Tree<K,V> t) {
    return n1_aux(t, false);
  }

  private static <K extends Comparable<K>, V> Tree<K, V> n1(final Tree<K,V> t) {
    return n1_aux(t, true);
  }

  private static <K extends Comparable<K>, V> Tree<K, V> n1_aux(final Tree<K,V> t, final boolean boxN1) {
    if (t instanceof L2) {
      final L2<K, V> l2t = (L2<K, V>) t;
      final K a1 = l2t.ma1;
      final V v1 = l2t.mv1;
      final Tree<K, V> cn0 = N0.create();

      return N2.create(cn0, a1, v1, cn0);
    }
    else if (t instanceof N3) {
      final N3<K, V> n3t = (N3<K, V>) t;
      final Tree<K, V> t1 = n3t.mt1, t2 = n3t.mt2, t3 = n3t.mt3;
      final K a1 = n3t.ma1, a2 = n3t.ma2;
      final V v1 = n3t.mv1, v2 = n3t.mv2;

      return N2.create(N2.create(t1, a1, v1, t2), a2, v2, N1.create(t3));
    }
    else {
      return boxN1 ? N1.create(t) : t;
    }
  }

  private static <K extends Comparable<K>, V> Tree<K, V> n2_ins(final Tree<K,V> left, final K a, final V v, final Tree<K, V> right) {
    if (left instanceof L2) {
      final L2<K, V> lt = (L2<K, V>) left;
      final Tree<K, V> t1 = right;
      final K a1 = lt.ma1, a2 = a;
      final V v1 = lt.mv1, v2 = v;
      final Tree<K, V> cn0 = N0.create();

      return N3.create(cn0, a1, v1, cn0, a2, v2, t1);
    }

    if (left instanceof N3) {
      if (right instanceof N1) {
         final N3<K, V> n3t = (N3<K, V>) left;
         final Tree<K, V> t1 = n3t.mt1, t2 = n3t.mt2, t3 = n3t.mt3;
         final K a1 = n3t.ma1, a2 = n3t.ma2, a3 = a;
         final V v1 = n3t.mv1, v2 = n3t.mv2, v3 = v;

         final N1<K, V> n1t = (N1<K, V>) right;
         final Tree<K, V> t4 = n1t.mt;

         return N2.create(N2.create(t1, a1, v1, t2), a2, v2, N2.create(t3, a3, v3, t4));
      }
      else if (right instanceof N2)
      {
        final N3<K, V> n3t = (N3<K, V>) left;
        final Tree<K, V> t1 = n3t.mt1, t2 = n3t.mt2, t3 = n3t.mt3;
        final K a1 = n3t.ma1, a2 = n3t.ma2, a3 = a;
        final V v1 = n3t.mv1, v2 = n3t.mv2, v3 = v;
        final Tree<K, V> t4 = right;

        return N3.create(N2.create(t1, a1, v1, t2), a2, v2, N1.create(t3), a3, v3, t4);
      }
    }

    if (right instanceof L2) {
      final L2<K, V> rt = (L2<K, V>) right;
      final Tree<K, V> t1 = left;
      final K a1 = a, a2 = rt.ma1;
      final V v1 = v, v2 = rt.mv1;
      final Tree<K, V> cn0 = N0.create();

      return N3.create(t1, a1, v1, cn0, a2, v2, cn0);
    }

    if (right instanceof N3) {
      if (left instanceof N1) {
         final N3<K, V> n3t = (N3<K, V>) right;
         final Tree<K, V> t2 = n3t.mt1, t3 = n3t.mt2, t4 = n3t.mt3;
         final K a1 = a, a2 = n3t.ma1, a3 = n3t.ma2;
         final V v1 = v, v2 = n3t.mv1, v3 = n3t.mv2;

         final N1<K, V> n1t = (N1<K, V>) left;
         final Tree<K, V> t1 = n1t.mt;

         return N2.create(N2.create(t1, a1, v1, t2), a2, v2, N2.create(t3, a3, v3, t4));
      }
      else if (left instanceof N2)
      {
        final N3<K, V> n3t = (N3<K, V>) right;
        final Tree<K, V> t2 = n3t.mt1, t3 = n3t.mt2, t4 = n3t.mt3;
        final K a1 = a, a2 = n3t.ma1, a3 = n3t.ma2;
        final V v1 = v, v2 = n3t.mv1, v3 = n3t.mv2;
        final Tree<K, V> t1 = left;

        return N3.create(t1, a1, v1, N1.create(t2), a2, v2, N2.create(t3, a3, v3, t4));
//                N2.create(t1, a1, v1, t2), a2, v2, N1.create(t3), a3, v3, t4);
      }
    }

    final Tree<K, V> t1 = left, t2 = right;
    final K a1 = a;
    final V v1 = v;

    return N2.create(t1, a1, v1, t2);
  }

  private static <K extends Comparable<K>, V> Tree<K, V> root_del(final Tree<K,V> t) {
    if (t instanceof N1) {
      return ((N1<K, V>) t).mt;
    }
    else {
      return t;
    }
  }

  private static <K extends Comparable<K>, V> Tree<K, V> n2_del(final Tree<K,V> left, final K a, final V v, final Tree<K, V> right) {
    final Tree<K, V> cn0 = N0.create();

    final boolean leftIsN1 = left instanceof N1;
    if (leftIsN1 && (right instanceof N1)) {
      final Tree<K, V> t1 = ((N1<K, V>) left).mt;
      final Tree<K, V> t2 = ((N1<K, V>) right).mt;

      return c1(t1, a, v, t2);
    }
    else if (leftIsN1 && (right instanceof N2)) {
      final N1<K, V> ln1 = (N1<K, V>) left;
      final N2<K, V> rn2 = (N2<K, V>) right;

      if (ln1.mt instanceof N1) {
        if (rn2.mt2 instanceof N2) {
          if (rn2.mt1 instanceof N1) {
            final Tree<K, V> t1 = ((N1<K, V>) ln1.mt).mt;
            final Tree<K, V> t2 = ((N1<K, V>) rn2.mt1).mt;
            final Tree<K, V> t3 = rn2.mt2;
            final K a1 = a, a2 = rn2.ma1;
            final V v1 = v, v2 = rn2.mv1;

            return c2(t1, a1, v1, t2, a2, v2, t3);
          }
          else if (rn2.mt1 instanceof N2) {
            final Tree<K, V> t1 = ln1.mt;
            final Tree<K, V> t2 = rn2.mt1;
            final Tree<K, V> t3 = rn2.mt2;
            final K a1 = a, a2 = rn2.ma1;
            final V v1 = v, v2 = rn2.mv1;

            return c4(t1, a1, v1, t2, a2, v2, t3);
          }
        }
        else if (rn2.mt2 instanceof N1) {
          if (rn2.mt1 instanceof N2) {
            final N2<K, V> z1 = (N2<K, V>) rn2.mt1;
            final N1<K, V> z2 = (N1<K, V>) rn2.mt2;

            final Tree<K, V> t1 = ((N1<K, V>) ln1.mt).mt;
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
    else if ((left instanceof N2) && (right instanceof N1)) {
      final N2<K, V> ln2 = (N2<K, V>) left;
      final N1<K, V> rn1 = (N1<K, V>) right;

      if (rn1.mt instanceof N1) {
        if (ln2.mt2 instanceof N1) {
          if (ln2.mt1 instanceof N2) {
            final Tree<K, V> t1 = ln2.mt1;
            final Tree<K, V> t2 = ((N1<K, V>) ln2.mt2).mt;
            final Tree<K, V> t3 = ((N1<K, V>) rn1.mt).mt;
            final K a1 = ln2.ma1, a2 = a;
            final V v1 = ln2.mv1, v2 = v;

            return c6(t1, a1, v1, t2, a2, v2, t3);
          }
        }
        else if (ln2.mt2 instanceof N2) {
          if (ln2.mt1 instanceof N1) {
            final N2<K, V> z = (N2<K, V>) ln2.mt2;
            final Tree<K, V> t1 = ((N1<K, V>) ln2.mt1).mt;
            final Tree<K, V> t2 = z.mt1;
            final Tree<K, V> t3 = z.mt2;
            final Tree<K, V> t4 = ((N1<K, V>) rn1.mt).mt;
            final K a1 = ln2.ma1, a2 = z.ma1, a3 = a;
            final V v1 = ln2.mv1, v2 = z.mv1, v3 = v;

            return c5(t1, a1, v1, t2, a2, v2, t3, a3, v3, t4);
          }
          else if (ln2.mt1 instanceof N2) {
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

  private static final class InitFromArrayWorker<K extends Comparable<K>, V> {
    private final ArrayList<Pair<K, V>> mVector;
    private final boolean mIncreasing;

    public InitFromArrayWorker(final ArrayList<Pair<K, V>> vector,
                               final boolean increasing) {
      mVector = vector;
      mIncreasing = increasing;
    }

    private Tree<K, V> workerFunc(final int left, final int right, final int depth) {
      if (left > right)
        return empty();

      final int mid = (left + right) >>> 1;
      final Pair<K, V> p = mVector.get(mid);

      Tree<K, V> lt, rt;
      final int newDepth = depth + 1;
      lt = workerFunc(left, mid - 1, newDepth);
      rt = workerFunc(mid + 1, right, newDepth);

      if (! mIncreasing) {
        Tree<K, V> t = lt;
        lt = rt;
        rt = t;
      }

      return null;
//
//      return depth == mRedLevel
//              ? RedNode.create(lt, p.mx1, p.mx2, rt)
//              : BlackNode.create(lt, p.mx1, p.mx2, rt);
    }

    public final Tree<K, V> doIt() {
      return workerFunc(0, mVector.size() - 1, 0);
    }
  }

  public static <K extends Comparable<K>, V> Tree<K, V> fromStrictlyIncreasingArray(final ArrayList<Pair<K, V>> v) {
    return fromArray(v);
//    return (new InitFromArrayWorker<>(v, true, computeRedDepth(v.size())).doIt());
  }

  public static <K extends Comparable<K>, V> Tree<K, V> fromStrictlyDecreasingArray(final ArrayList<Pair<K, V>> v) {
    return fromArray(v);
    //return (new InitFromArrayWorker<>(v, false, computeRedDepth(v.size())).doIt());
  }

  public static <K extends Comparable<K>, V> Tree<K, V> fromArray(final ArrayList<Pair<K, V>> v) {
    return v.stream()
            .reduce(empty(),
                    ((t, p) -> t.insert(p.mx1, p.mx2)),
                    ((t1, t2) -> { throw new AssertionError("Must not be used.  Stream is not parallel."); }));
  }

  public static <K extends Comparable<K>, V> Tree<K, V> empty() {
    return N0.create();
  }

  public static <K extends Comparable<K>, V> Tree<K, V> singleton(final K a, final V v) {
    Tree<K, V> e = empty();
    return N2.create(e, a, v, e);
  }
}
