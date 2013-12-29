/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.Functionals;
import Utils.Ref;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Spliterator;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author Neo
 * @param <A>
 */
public final class LinkedList<A> implements List<A, LinkedList<A>> {
  @SuppressWarnings("unchecked")
  public static <A> LinkedList<A> empty() { return (LinkedList<A>) sEmptyList; }

  public static <A> LinkedList<A> singleton(final A a) { return create(a, empty()); }

  private static <T> LinkedList<T> create(final T a, final LinkedList<T> list) {
    return new LinkedList<>(a, list);
  }

  private static <T> LinkedList<T> createInv(final LinkedList<T> list, final T a) {
    return new LinkedList<>(a, list);
  }

  private LinkedList() { mCar = null; mCdr = null; }

  private LinkedList(final A a, final LinkedList<A> list) {
    mCar = a;
    mCdr = list;
  }

  private final A mCar;
  private final LinkedList<A> mCdr;

  private static final LinkedList<?> sEmptyList = new LinkedList<>();

  private boolean isNull()    { return this == sEmptyList; }
  private boolean isNotNull() { return this != sEmptyList; }

  private A car() { return mCar; }
  private LinkedList<A> cdr() { return mCdr; }

  @Override
  public LinkedList<A> addFirst(final A a) {
    return create(a, this);
  }

  @Override
  public LinkedList<A> addLast(final A a) {
    return appendImpl(this, create(a, empty()));
  }

  @Override
  public LinkedList<A> cons(final A a) {
    return create(a, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    forEachImpl(intersperseImpl(mapImpl(this, e -> e.toString()), ", "), sb::append);
    sb.append("]");
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (! (obj instanceof List)) {
      return false;
    }
    else {
      List<A, ?> la = this;
      @SuppressWarnings("unchecked")
      List<A, ?> lb = (List<A, ?>) obj;

      while (! la.isEmpty() && ! lb.isEmpty()) {
        if (! la.head().equals(lb.head())) {
          return false;
        }
        la = la.tail();
        lb = lb.tail();
      }
 
      return la.isEmpty() && lb.isEmpty();
    }
  }

  private static <A> int hashCodeImpl(final LinkedList<A> list) {
    LinkedList<A> t = list;
    int hashRes = 23;

    while (t.isNotNull()) {
      hashRes += t.mCar.hashCode();
      t = t.mCdr;
    }

    return hashRes;
  }

  @Override
  public int hashCode() {
    return hashCodeImpl(this);
  }

  private static <A> LinkedList<A> appendImpl(final LinkedList<A> list1, final LinkedList<A> list2) {
    final ArrayList<A> v = toArrayImpl(list1);
    final int lastIdx = v.size() - 1;
    return IntStream.rangeClosed(0, lastIdx)
      .mapToObj(idx -> v.get(lastIdx - idx))
      .reduce(list2,
              LinkedList::createInv,
              Functionals::functionShouldNotBeCalled);
  }

  @Override
  public LinkedList<A> append(final LinkedList<A> list) {
    return appendImpl(this, list);
  }

  @Override
  public A head() {
    if (isNull()) {
      throw buildExnEmptyList("head");
    }
    else {
      return mCar;
    }
  }

  @Override
  public LinkedList<A> tail() {
    if (isNull()) {
      throw buildExnEmptyList("tail");
    }
    else {
      return mCdr;
    }
  }

  private static <A> A lastImpl(final LinkedList<A> list) {
    LinkedList<A> t = null, next = list;
    while (next.isNotNull()) {
      t = next;
      next = next.mCdr;
    }

    if (t == null) {
      throw buildExnEmptyList("last");
    }
    else {
      return t.mCar;
    }
  }

  @Override
  public A last() {
    return lastImpl(this);
  }

  private static <A> ArrayList<A> toArrayImpl(final LinkedList<A> list) {
    final ArrayList<A> v = new ArrayList<>();
    LinkedList<A> t = list;

    while (t.isNotNull()) {
      v.add(t.mCar);
      t = t.mCdr;
    }

    return v;
  }

  @Override
  public ArrayList<A> toArray() {
    return toArrayImpl(this);
  }

  public static <A> LinkedList<A> fromStream(final Stream<A> s) {
    return fromArray(s.collect(Collectors.toCollection(ArrayList::new)));
  }

  public static <A> LinkedList<A> fromArray(final ArrayList<A> v, final int i, final int j) {
    return IntStream.rangeClosed(i, j)
      .mapToObj(idx -> v.get(j - idx))
      .reduce(empty(),
              LinkedList::createInv,
              Functionals::functionShouldNotBeCalled);
  }

  public static <A> LinkedList<A> fromArray(final ArrayList<A> v) {
    return fromArray(v, 0, v.size() - 1);
  }

  private static <A> LinkedList<A> initImpl(final LinkedList<A> list) {
    if (list.isNull()) {
      throw buildExnEmptyList("init");
    }
    else {
      LinkedList<A> t = list;

      if (t.mCdr.isNull()) {
        return empty();
      }
      else {
        final ArrayList<A> v = new ArrayList<>();
        do {
          v.add(t.mCar);
          t = t.mCdr;
        } while (t.mCdr.isNotNull());

        return fromArray(v);
      }
    }
  }

  @Override
  public LinkedList<A> init() {
    return initImpl(this);
  }

  @Override
  public boolean isEmpty() {
    return isNull();
  }

  private static <A> int lengthImpl(final LinkedList<A> list) {
    int len = 0;
    LinkedList<A> t = list;
    while (t.isNotNull()) {
      ++len;
      t = t.mCdr;
    }

    return len;
  }

  @Override
  public int length() {
    return lengthImpl(this);
  }

  private static <A> void forEachImpl(final LinkedList<A> list, final Consumer<? super A> action) {
    LinkedList<A> t = list;
    while (t.isNotNull()) {
      action.accept(t.mCar);
      t = t.mCdr;
    }
      
    return;
  }

  @Override
  public void forEach(final Consumer<? super A> action) {
    forEachImpl(this, action);
      
    return;
  }

  private static <A, B> LinkedList<B> mapImpl(final LinkedList<A> list, final Function<A, B> f) {
    ArrayList<B> v = new ArrayList<>();
    forEachImpl(list, a -> { v.add(f.apply(a)); });

    return fromArray(v);
  }

  @Override
  public <B> LinkedList<B> map(final Function<A, B> f) {
    return mapImpl(this, f);
  }

  private static <A> LinkedList<A> reverseImpl(final LinkedList<A> list) {
    final Ref<LinkedList<A>> acc = new Ref<>(empty());

    forEachImpl(list, a -> { acc.r = create(a, acc.r); });

    return acc.r;
  }

  @Override
  public LinkedList<A> reverse() {
    return reverseImpl(this);
  }

  private static <A> LinkedList<A> intersperseImpl(final LinkedList<A> list, final A a) {
    if (list.isNull()) {
      return list;
    }
    else {
      final ArrayList<A> v = new ArrayList<>();
      v.add(list.mCar);
      LinkedList<A> t = list.mCdr;

      forEachImpl(list.mCdr, m -> { v.add(a); v.add(m); });

      return fromArray(v);
    }
  }

  @Override
  public LinkedList<A> intersperse(final A a) {
    return intersperseImpl(this, a);
  }

  private static <A, B> B foldlImpl(final LinkedList<A> list, final BiFunction<B, A, B> f, final B b) {
    final Ref<B> acc = new Ref<>(b);

    forEachImpl(list, a -> { acc.r = f.apply(acc.r, a); });

    return acc.r;
  }

  @Override
  public <B> B foldl(final BiFunction<B, A, B> f, final B b) {
    return foldlImpl(this, f, b);
  }

  private static <A> A foldl1Impl(final LinkedList<A> list, final BiFunction<A, A, A> f) {
    if (list.isNull()) {
      throw buildExnEmptyList("foldl1");
    }
    else {
      return foldlImpl(list.mCdr, f, list.mCar);
    }
  }

  @Override
  public A foldl1(final BiFunction<A, A, A> f) {
    return foldl1Impl(this, f);
  }

  private static <A, B> B foldrImpl(final LinkedList<A> list, final BiFunction<A, B, B> f, final B b) {
    final ArrayList<A> v = toArrayImpl(list);
    final int lastIdx = v.size() - 1;

    return IntStream.rangeClosed(0, lastIdx)
                    .mapToObj(i -> v.get(lastIdx - i))
                    .reduce(b,
                            (acc, e) -> f.apply(e, acc),
                            Functionals::functionShouldNotBeCalled);
  }

  @Override
  public <B> B foldr(final BiFunction<A, B, B> f, final B b) {
    return foldrImpl(this, f, b);
  }

  private static <A> A foldr1Impl(final LinkedList<A> list, final BiFunction<A, A, A> f) {
    if (list.isNull()) {
      throw buildExnEmptyList("foldr1");
    }
    else {
      final ArrayList<A> v = toArrayImpl(list);
      final int lastIdx = v.size() - 1;
      final int butLastIdx = lastIdx - 1;
      final A b = v.get(lastIdx);

      return IntStream.rangeClosed(0, butLastIdx)
                      .mapToObj(i -> v.get(butLastIdx - i))
                      .reduce(b,
                              (acc, e) -> f.apply(e, acc),
                              Functionals::functionShouldNotBeCalled);
    }
  }

  @Override
  public A foldr1(final BiFunction<A, A, A> f) {
    return foldr1Impl(this, f);
  }

  private static <A> boolean anyImpl(final LinkedList<A> list, final Predicate<A> f) {
    LinkedList<A> t = list;

    while (t.isNotNull()) {
      if (f.test(t.mCar)) {
        return true;
      }
      t = t.mCdr;
    }

    return false;
  }

  @Override
  public boolean any(final Predicate<A> f) {
    return anyImpl(this, f);
  }

  private static <A> boolean allImpl(final LinkedList<A> list, final Predicate<A> f) {
    return ! anyImpl(list, f.negate());
  }

  @Override
  public boolean all(final Predicate<A> f) {
    return allImpl(this, f);
  }

  private static <A, B> LinkedList<B> scanlImpl(final LinkedList<A> list, final BiFunction<B, A, B> f, final B b) {
    B acc = b;
    LinkedList<A> t = list;
    final ArrayList<B> v = new ArrayList<>();

    while (t.isNotNull()) {
      v.add(acc);
      acc = f.apply(acc, t.mCar);
      t = t.mCdr;
    }
    v.add(acc);

    return fromArray(v);
  }

  @Override
  public <B> LinkedList<B> scanl(final BiFunction<B, A, B> f, final B b) {
    return scanlImpl(this, f, b);
  }

  private static <A> LinkedList<A> scanl1Impl(final LinkedList<A> list, final BiFunction<A, A, A> f) {
    if (list.isNull()) {
      return list;
    }
    else {
      return scanlImpl(list.mCdr, f, list.mCar);
    }
  }

  @Override
  public LinkedList<A> scanl1(final BiFunction<A, A, A> f) {
    return scanl1Impl(this, f);
  }

  private static <A, B> LinkedList<B> scanrImpl(final LinkedList<A> list, final BiFunction<A, B, B> f, final B b) {
    final ArrayList<A> v = toArrayImpl(list);
    final int lastIdx = v.size() - 1;
    final ArrayList<B> bs = new ArrayList<>();

    bs.add(b);
    IntStream.rangeClosed(0, lastIdx)
             .mapToObj(i -> v.get(lastIdx - i))
             .reduce(b,
                     (acc, e) -> { final B newAcc = f.apply(e, acc); bs.add(newAcc); return newAcc; },
                     Functionals::functionShouldNotBeCalled);

    return fromArray(bs);
  }

  @Override
  public <B> LinkedList<B> scanr(final BiFunction<A, B, B> f, final B b) {
    return scanrImpl(this, f, b);
  }

  private static <A> LinkedList<A> scanr1Impl(final LinkedList<A> list, final BiFunction<A, A, A> f) {
    if (list.isNull()) {
      return list;
    }
    else {
      return scanrImpl(list.mCdr, f, list.mCar);
    }
  }

  @Override
  public LinkedList<A> scanr1(final BiFunction<A, A, A> f) {
    return scanr1Impl(this, f);
  }

  private static <A, ACC, B> Pair<ACC, List<B, ?>> mapAccumLImpl(
          final LinkedList<A> list,
          final BiFunction<ACC, A, Pair<ACC, B>> f,
          final ACC acc) {
    final Ref<ACC> newAcc = new Ref<>(acc);
    final ArrayList<B> v = new ArrayList<>();

    forEachImpl(list, a -> { 
      final Pair<ACC, B> p = f.apply(newAcc.r, a);
      newAcc.r = p.mx1;        
      v.add(p.mx2);
    });

    return Pair.create(newAcc.r, fromArray(v));
  }

  @Override
  public <ACC, B> Pair<ACC, List<B, ?>> mapAccumL(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc) {
    return mapAccumLImpl(this, f, acc);
  }

  private static <A, ACC, B> Pair<ACC, List<B, ?>> mapAccumRImpl(
          final LinkedList<A> list,
          final BiFunction<ACC, A, Pair<ACC, B>> f,
          final ACC acc) {
    final ArrayList<A> v = toArrayImpl(list);
    final ArrayList<B> u = new ArrayList<>();
    final int lastIdx = v.size() - 1;

    final ACC newAcc = IntStream.rangeClosed(0, lastIdx)
                                .mapToObj(i -> v.get(lastIdx - i))
                                .reduce(acc,
                                        (rAcc, e) -> { final Pair<ACC, B> r = f.apply(rAcc, e); u.add(r.mx2); return r.mx1; },
                                        Functionals::functionShouldNotBeCalled);
    return Pair.create(newAcc,
                       u.stream()
                        .reduce(empty(),
                                LinkedList::createInv,
                                Functionals::functionShouldNotBeCalled));
  }

  @Override
  public <ACC, B> Pair<ACC, List<B, ?>> mapAccumR(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc) {
    return mapAccumRImpl(this, f, acc);
  }

  private static <A> LinkedList<A> takeImpl(final LinkedList<A> list, final int n) {
    if (n < 0) {
      throw new AssertionError("The n parameter in function take() cannot be negative.");
    }
    else {
      final ArrayList<A> v = new ArrayList<>();
      LinkedList<A> t = list;

      for (int i = 0; i != n; ++i) {
        if (t.isNull()) {
          break;
        }
        v.add(t.mCar);
        t = t.mCdr;
      }

      return fromArray(v);
    }
  }

  @Override
  public LinkedList<A> take(final int n) {
    return takeImpl(this, n);
  }

  private static <A> LinkedList<A> dropImpl(final LinkedList<A> list, final int n) {
    if (n < 0) {
      throw new AssertionError("The n parameter in function drop() cannot be negative.");
    }
    else {
      LinkedList<A> t = list;
      for (int i = 0; i != n; ++i) {
        if (t.isNull()) {
          return t;
        }
        t = t.mCdr;
      }

      return t;
    }
  }

  @Override
  public LinkedList<A> drop(final int n) {
    return dropImpl(this, n);
  }

  private static <A> Pair<LinkedList<A>, LinkedList<A>> splitAtImpl(final LinkedList<A> list, final int n) {
    final int m = Math.max(0, n);
    LinkedList<A> t = list;
    final ArrayList<A> v = new ArrayList<>();

    for (int i = 0; i != m; ++i) {
      if (t.isNull()) {
        return Pair.create(list, t);
      }
      v.add(t.mCar);
      t = t.mCdr;
    }

    return Pair.create(fromArray(v), t);
  }

  @Override
  public Pair<LinkedList<A>, LinkedList<A>> splitAt(final int n) {
    return splitAtImpl(this, n);
  }

  private static <A> void takeDropImpl(final LinkedList<A> list, final Predicate<A> pred, final Ref<LinkedList<A>> tk, final Ref<LinkedList<A>> dr) {
    final ArrayList<A> v = new ArrayList<>();
    LinkedList<A> t = list;

    while (t.isNotNull() && pred.test(t.mCar)) {
      v.add(t.mCar);
      t = t.mCdr;
    }

    tk.r = fromArray(v);
    dr.r = t;
  }

  private static <A> LinkedList<A> takeWhileImpl(final LinkedList<A> list, final Predicate<A> pred) {
    final ArrayList<A> v = new ArrayList<>();
    LinkedList<A> t = list;
      
    while (t.isNotNull() && pred.test(t.mCar)) {
      v.add(t.mCar);
      t = t.mCdr;
    }

    return fromArray(v);
  }

  @Override
  public LinkedList<A> takeWhile(final Predicate<A> pred) {
    return takeWhileImpl(this, pred);
  }

  private static <A> LinkedList<A> dropWhileImpl(final LinkedList<A> list, final Predicate<A> pred) {
    LinkedList<A> t = list;
      
    while (t.isNotNull() && pred.test(t.mCar)) {
      t = t.mCdr;
    }

    return t;
  }

  @Override
  public LinkedList<A> dropWhile(Predicate<A> pred) {
    return dropWhileImpl(this, pred);
  }

  private static <A> LinkedList<A> dropWhileEndImpl(final LinkedList<A> list, final Predicate<A> pred) {
    final ArrayList<A> v = toArrayImpl(list);

    int i = v.size() - 1;
    for (; i >= 0; --i) {
      if (! pred.test(v.get(i))) {
        break;
      }
    }

    return fromArray(v, 0, i);
  }

  @Override
  public LinkedList<A> dropWhileEnd(final Predicate<A> pred) {
    return dropWhileEndImpl(this, pred);
  }

  private static <A> Pair<LinkedList<A>, LinkedList<A>> spanByImpl(final LinkedList<A> list, final Predicate<A> pred) {
    LinkedList<A> t = list;
    int n = 0;
    while (t.isNotNull()) {
      if (! pred.test(t.mCar)) {
        break;
      }
      t = t.mCdr;
      ++n;
    }

    final LinkedList<A> l1, l2;
    if (n == 0) {
      l1 = empty();
      l2 = list;
    }
    else if (t.isNull()) {
      l1 = list;
      l2 = empty();
    }
    else {
      final Ref<LinkedList<A>> tk = new Ref<>(), dr = new Ref<>();
      takeDropImpl(list, pred, tk, dr);
      l1 = tk.r;
      l2 = dr.r;
    }
      
    return Pair.create(l1, l2);
  }

  @Override
  public Pair<LinkedList<A>, LinkedList<A>> spanBy(final Predicate<A> pred) {
    return spanByImpl(this, pred);
  }

  @Override
  public Pair<LinkedList<A>, LinkedList<A>> breakBy(final Predicate<A> pred) {
    return spanByImpl(this, pred.negate());
  }

  private static <A> Optional<LinkedList<A>> stripPrefixImpl(final LinkedList<A> list, final LinkedList<A> prefix) {
    LinkedList<A> t0 = prefix, t1 = list;

    while (t0.isNotNull() && t1.isNotNull()) {
      if (! t0.mCar.equals(t1.mCar)) {
        break;
      }
      t0 = t0.mCdr;
      t1 = t1.mCdr;
    }

    return t0.isNotNull() ? Optional.empty() : Optional.of(t1);
  }

  @Override
  public Optional<LinkedList<A>> stripPrefix(final LinkedList<A> list) {
    return stripPrefixImpl(list, this);
  }

  private static <A> LinkedList<LinkedList<A>> groupByImpl(final LinkedList<A> list, final BiPredicate<A, A> eqPred) {
    if (list.isNull()) {
      return empty();
    }
    else {
      final ArrayList<ArrayList<A>> res = new ArrayList<>();
      res.add(new ArrayList<>());

      forEachImpl(list, a -> {
        final int idx = res.size() - 1;
        final ArrayList<A> v = res.get(idx);

        if (v.isEmpty() || eqPred.test(a, v.get(0))) {
          v.add(a);
        }
        else {
          final ArrayList<A> newV = new ArrayList<>();
          newV.add(a);
          res.add(newV);
        }
      });

      return fromStream(res.stream().map(LinkedList::fromArray));
    }
  }

  private static <A> boolean equalityPredicate(final A a1, final A a2) { return a1.equals(a2); }

  @Override
  public LinkedList<LinkedList<A>> group() {
    return groupByImpl(this, LinkedList::equalityPredicate);
  }

  @Override
  public LinkedList<LinkedList<A>> groupBy(final BiPredicate<A, A> eqPred) {
    return groupByImpl(this, eqPred);
  }

  private static <A> LinkedList<LinkedList<A>> initsImpl(final LinkedList<A> list) {
    final ArrayList<A> v = toArrayImpl(list);
    final int lastIdx = v.size() - 1;

    final LinkedList<LinkedList<A>> res =
      IntStream.rangeClosed(0, lastIdx)
               .mapToObj(i -> fromArray(v, 0, lastIdx - i))
               .reduce(empty(),
                       LinkedList::createInv,
                       Functionals::functionShouldNotBeCalled);
    return create(empty(), res);
  }

  @Override
  public LinkedList<LinkedList<A>> inits() {
    return initsImpl(this);
  }

  private static <A> LinkedList<LinkedList<A>> tailsImpl(final LinkedList<A> list) {
    LinkedList<A> t = list;
    LinkedList<LinkedList<A>> res = empty();

    while (t.isNotNull()) {
      res = create(t, res);
      t = t.mCdr;
    }

    return create(t, res).reverse();
  }

  @Override
  public LinkedList<LinkedList<A>> tails() {
    return tailsImpl(this);
  }

  private static <A> boolean isPrefixOfImpl(final LinkedList<A> prefix, final LinkedList<A> list) {
    LinkedList<A> t0 = prefix, t1 = list;

    while (t0.isNotNull() && t1.isNotNull()) {
      if (! t0.mCar.equals(t1.mCar)) {
        break;
      }
      t0 = t0.mCdr;
      t1 = t1.mCdr;
    }

    return t0.isNull();
  }

  @Override
  public boolean isPrefixOf(final LinkedList<A> list) {
    return isPrefixOfImpl(this, list);
  }

  @Override
  public boolean isSuffixOf(final LinkedList<A> list) {
    return isPrefixOfImpl(reverseImpl(this), reverseImpl(list));
  }

  @Override
  public boolean isInfixOf(final LinkedList<A> list) {
    return anyImpl(tailsImpl(this), x -> isPrefixOfImpl(list, x));
  }

  private static <A> Optional<Pair<A, Integer>> findByImpl(final LinkedList<A> list, final Predicate<A> pred) {
    LinkedList<A> t = list;

    int i = 0;
    while (t.isNotNull()) {
      if (pred.test(t.mCar)) {
        return Optional.of(Pair.create(t.mCar, i));
      }
      ++i;
      t = t.mCdr;
    }

    return Optional.empty();
  }

  @Override
  public boolean elem(final A a) {
    return findByImpl(this, x -> a.equals(x)).isPresent();
  }

  @Override
  public boolean notElem(final A a) {
    return ! elem(a);
  }

  @Override
  public Optional<A> find(final Predicate<A> pred) {
    return findByImpl(this, pred).map(Pair::getFirst);
  }

  private static <A> LinkedList<A> filterImpl(final LinkedList<A> list, final Predicate<A> pred) {
    final ArrayList<A> v = new ArrayList<>();

    forEachImpl(list, a -> { if (pred.test(a)) { v.add(a); } });

    return fromArray(v);
  }

  @Override
  public LinkedList<A> filter(final Predicate<A> pred) {
    return filterImpl(this, pred);
  }

  private static <A> Pair<LinkedList<A>, LinkedList<A>> partitionImpl(final LinkedList<A> list, final Predicate<A> pred) {
    final ArrayList<A> vt = new ArrayList<>(), vf = new ArrayList<>();

    forEachImpl(list, a -> { (pred.test(a) ? vt : vf).add(a); });

    return Pair.create(fromArray(vt), fromArray(vf));
  }

  @Override
  public Pair<LinkedList<A>, LinkedList<A>> partition(final Predicate<A> pred) {
    return partitionImpl(this, pred);
  }

  private static <A> A nthImpl(final LinkedList<A> list, final int n) {
    if (n < 0) {
      throw new AssertionError("Index was negative in nth()");
    }
    else {
      int i = 0;
      LinkedList<A> t = list;
      while (t.isNotNull() && i < n) {
        t = t.mCdr;
        ++i;
      }

      if (t.isNull()) {
        throw new AssertionError("Index was out of bounds of list in nth()");
      }
      else {
        return t.mCar;
      }
    }
  }

  @Override
  public A nth(final int n) {
    return nthImpl(this, n);
  }

  @Override
  public OptionalInt elemIndex(final A a) {
    return Functionals.mapOptOrElse(
      findByImpl(this, x -> a.equals(x)).map(Pair::getSecond),
      x -> OptionalInt.of(x.intValue()),
      OptionalInt::empty);
  }

  private static <A> LinkedList<Integer> elemIndicesImpl(final LinkedList<A> list, final Predicate<A> pred) {
    LinkedList<A> t = list;
    int idx = 0;
    final ArrayList<Integer> v = new ArrayList<>();

    while (t.isNotNull()) {
      if (pred.test(t.mCar)) {
        v.add(idx);
      }
      t = t.mCdr;
      ++idx;
    }

    return fromArray(v);
  }

  @Override
  public LinkedList<Integer> elemIndices(final A a) {
    return elemIndicesImpl(this, a::equals);
  }

  @Override
  public OptionalInt findIndex(final Predicate<A> pred) {
    return Functionals.mapOptOrElse(
      findByImpl(this, pred).map(Pair::getSecond),
      x -> OptionalInt.of(x.intValue()),
      OptionalInt::empty);
  }

  @Override
  public LinkedList<Integer> findIndices(final Predicate<A> pred) {
    return elemIndicesImpl(this, pred);
  }

  private static <A> LinkedList<A> nubByImpl(final LinkedList<A> list, final Comparator<? super A> cmp) {
    final ArrayList<A> v = new ArrayList<>();
    final TreeSet<A> s = new TreeSet<>(cmp);

    forEachImpl(list, a -> {
      final boolean notAreadyThere = s.add(a);
      if (notAreadyThere) {
        v.add(a);
      }
    });

    return fromArray(v);
  }

  @Override
  public LinkedList<A> nub() {
    return nubByImpl(this, Functionals::comparator);
  }

  @Override
  public LinkedList<A> nubBy(final Comparator<? super A> cmp) {
    return nubByImpl(this, cmp);
  }

  private static <A> LinkedList<A> deleteByImpl(final LinkedList<A> list, final A a, final BiPredicate<A, A> pred) {
    LinkedList<A> t = list;
    final ArrayList<A> v = new ArrayList<>();

    boolean skippedOne = false;
    while (t.isNotNull()) {
      if (skippedOne) {
        v.add(t.mCar);
      }
      else if (pred.test(a, t.mCar)) {
        skippedOne = true;
      }
      else {
        v.add(t.mCar);
      }
      t = t.mCdr;
    }

    return skippedOne ? fromArray(v) : list;
  }

  @Override
  public LinkedList<A> delete(final A a) {
    return deleteByImpl(this, a, LinkedList::equalityPredicate);
  }

  @Override
  public LinkedList<A> deleteBy(final A a, final BiPredicate<A, A> pred) {
    return deleteByImpl(this, a, pred);
  }

  @Override
  public LinkedList<A> deleteFirstBy(final LinkedList<A> list, final BiPredicate<A, A> pred) {
    final Ref<LinkedList<A>> ref = new Ref<>(empty());
    forEachImpl(list, elem -> { ref.r = deleteByImpl(ref.r, elem, pred); });
    return ref.r;
  }

  @Override
  public LinkedList<A> listDiff(LinkedList<A> list) {
    return foldlImpl(list, (xs, x) -> xs.delete(x), this);
  }

  private static <A> LinkedList<A> unionByImpl(final LinkedList<A> list1, final LinkedList<A> list2, final Comparator<? super A> cmp) {
    final TreeSet<A> s = new TreeSet<>(cmp);
    final ArrayList<A> v = new ArrayList<>();

    forEachImpl(list1, a -> { s.add(a); v.add(a); });
    forEachImpl(list2, a -> {
        final boolean notAlreadyThere = s.add(a);
        if (notAlreadyThere) {
          v.add(a);
        }
      });

    return fromArray(v);
  }

  @Override
  public LinkedList<A> union(final LinkedList<A> list) {
    return unionByImpl(this, list, Functionals::comparator);
  }

  @Override
  public LinkedList<A> unionBy(final LinkedList<A> list, final Comparator<? super A> cmp) {
    return unionByImpl(this, list, cmp);
  }

  private static <A> LinkedList<A> intersectByImpl(final LinkedList<A> list1, final LinkedList<A> list2, final Comparator<? super A> cmp) {
    final TreeSet<A> s = new TreeSet<>(cmp);
    final ArrayList<A> v = new ArrayList<>();

    forEachImpl(list2, a -> { s.add(a); });
    forEachImpl(list1, a -> {
        if (s.contains(a)) {
          v.add(a);
        }
      });

    return fromArray(v);
  }

  @Override
  public LinkedList<A> intersect(final LinkedList<A> list) {
    return intersectByImpl(this, list, Functionals::comparator);
  }

  @Override
  public LinkedList<A> intersectBy(final LinkedList<A> list, final Comparator<? super A> cmp) {
    return intersectByImpl(this, list, cmp);
  }

  private static <A> LinkedList<A> sortByImpl(final LinkedList<A> list, final Comparator<? super A> cmp) {
    final ArrayList<A> v = toArrayImpl(list);
    v.sort(cmp);
    return fromArray(v);
  }

  @Override
  public LinkedList<A> sort() {
    return sortByImpl(this, null);
  }

  @Override
  public LinkedList<A> sortBy(final Comparator<? super A> cmp) {
    return sortByImpl(this, cmp);
  }

  private static <A> LinkedList<A> insertByImpl(final LinkedList<A> list, final A a, final Comparator<? super A> cmp) {
    LinkedList<A> t = list;
    final ArrayList<A> v = new ArrayList<>();

    while (t.isNotNull()) {
      if (cmp.compare(a, t.mCar) <= 0) {
        break;
      }
      else {
        v.add(t.mCar);
      }
      t = t.mCdr;
    }

    final int lastIdx = v.size() - 1;
    return IntStream.rangeClosed(0, lastIdx)
                    .mapToObj(i -> v.get(lastIdx - i))
                    .reduce(create(a, t),
                            LinkedList::createInv,
                            Functionals::functionShouldNotBeCalled);
  }

  @Override
  public LinkedList<A> insert(final A a) {
    return insertByImpl(this, a, Functionals::comparator);
  }

  @Override
  public LinkedList<A> insertBy(final A a, final Comparator<? super A> cmp) {
    return insertByImpl(this, a, cmp);
  }

  private static <A> Optional<A> orderingByImpl(final LinkedList<A> list, final BiFunction<A, A, A> selector) {
    if (list.isEmpty()) {
      return Optional.empty();
    }
    else {
      return Optional.of(foldlImpl(list.mCdr, selector, list.mCar));
    }
  }

  private static <A> A computeOrdinal(final LinkedList<A> list, final BiFunction<A, A, A> selector, final String functionName) {
    return Functionals.mapOptOrElse(orderingByImpl(list, selector),
                                    Function.identity(),
                                    () -> { throw buildExnEmptyList(functionName); });
  }

  @Override
  public A max() {
    return computeOrdinal(this, Functionals::maxSelector, "max");
  }

  @Override
  public A maxBy(final Comparator<? super A> cmp) {
    return computeOrdinal(this, (x, y) -> cmp.compare(x, y) >= 0 ? x : y, "maxBy");
  }

  @Override
  public A min() {
    return computeOrdinal(this, Functionals::minSelector, "min");
  }

  @Override
  public A minBy(final Comparator<? super A> cmp) {
    return computeOrdinal(this, (x, y) -> cmp.compare(x, y) < 0 ? x : y, "minBy");
  }

  public static <A, B> LinkedList<B> flatMap(final LinkedList<A> list, final Function<A, LinkedList<B>> f) {
    final ArrayList<B> v = new ArrayList<>();

    forEachImpl(list, l -> forEachImpl(f.apply(l), v::add));

    return fromArray(v);
  }

  private static <A> LinkedList<LinkedList<A>> subsequencesImpl(final LinkedList<A> list) {
    final LinkedList<LinkedList<A>> e = empty();
    if (list.isNull()) {
      return create(list, e);
    }
    else {
      final A a = list.mCar;
      final LinkedList<LinkedList<A>> subseqs = subsequencesImpl(list.mCdr);
      return foldrImpl(subseqs, (ls, res) -> create(ls, (create(create(a, ls), res))), e);
    }
  }

  @Override
  public LinkedList<LinkedList<A>> subsequences() {
    return subsequencesImpl(this);
  }

  private static final class ListIterator<A> implements Iterator<A> {
    private LinkedList<A> mCurrentElem;

    public ListIterator(final LinkedList<A> list) {
      mCurrentElem = list;
    }
    
    @Override
    public boolean hasNext() {
      return mCurrentElem.isNotNull();
    }

    @Override
    public A next() {
      if (mCurrentElem.isNull()) {
        throw new NoSuchElementException("Iterator has reached the end of the list.");
      }
      final A a = mCurrentElem.mCar;
      mCurrentElem = mCurrentElem.mCdr;
      return a;
    }
  }

  private static final class ListSpliterator<A> implements Spliterator<A> {
    private LinkedList<A> mCurrentElem;

    public ListSpliterator(final LinkedList<A> list) {
      mCurrentElem = list;
    }

    @Override
    public boolean tryAdvance(Consumer<? super A> action) {
      if (mCurrentElem.isNull()) {
        return false;
      }
      else {
        action.accept(mCurrentElem.mCar);
        mCurrentElem = mCurrentElem.mCdr;
        return true;
      }
    }

    @Override
    public Spliterator<A> trySplit() {
      return null;
    }

    @Override
    public void forEachRemaining(Consumer<? super A> action) {
      forEachImpl(mCurrentElem, action);
    }

    @Override
    public long estimateSize() {
      return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
      return ORDERED | SIZED | NONNULL | IMMUTABLE | CONCURRENT;
    }
  }

  @Override
  public Iterator<A> iterator() {
    return new ListIterator<>(this);
  }

  @Override
  public Stream<A> stream() {
    return StreamSupport.stream(new ListSpliterator<A>(this), false);
  }

  private static <A> LinkedList<LinkedList<A>> intersperseIncludingEdgesAndAppend(final LinkedList<A> list, final A a, final LinkedList<LinkedList<A>> res) {
    final ArrayList<A> v = toArrayImpl(list);
    LinkedList<LinkedList<A>> newRes = res;

    final int resElems = v.size() + 1;
    LinkedList<A> t = list;
    for (int i = 0; i != resElems; ++i) {
      LinkedList<A> newList = create(a, t);
      for (int j = i; j != 0; --j) {
        newList = create(v.get(j - 1), newList);
      }
      newRes = create(newList, newRes);
      t = t.mCdr;
    }

    return newRes;
  }

  private static <A> LinkedList<LinkedList<A>> permutationsImpl(final LinkedList<A> list) {
    final LinkedList<LinkedList<A>> e = empty();
    if (list.isNull()) {
      return create(empty(), e);
    }
    else {
      final A a = list.mCar;
      final LinkedList<LinkedList<A>> perms = permutationsImpl(list.mCdr);

      return foldlImpl(perms, (res, perm) -> intersperseIncludingEdgesAndAppend(perm, a, res), e);
    }
  }

  @Override
  public LinkedList<LinkedList<A>> permutations() {
    return permutationsImpl(this);
  }

  public static <A, B, C> LinkedList<C> zipWith(final LinkedList<A> listA, final LinkedList<B> listB, final BiFunction<A, B, C> zipper) {
    LinkedList<A> ta = listA;
    LinkedList<B> tb = listB;
    ArrayList<C> v = new ArrayList<>();

    while (ta.isNotNull() && tb.isNotNull()) {
      v.add(zipper.apply(ta.mCar, tb.mCar));
      ta = ta.mCdr;
      tb = tb.mCdr;
    }

    return fromArray(v);
  }

  public static <A, B> LinkedList<Pair<A, B>> zip(final LinkedList<A> listA, final LinkedList<B> listB) {
    return zipWith(listA, listB, Pair::create);
  }

  public static <A> LinkedList<A> replicate(final int n, final A a) {
    return Stream.generate(() -> a)
                 .limit(n)
                 .reduce(empty(),
                         LinkedList::createInv,
                         Functionals::functionShouldNotBeCalled);
  }

  public static <A, B> Pair<LinkedList<A>, LinkedList<B>> unzip(final LinkedList<Pair<A, B>> list) {
    final ArrayList<A> v0 = new ArrayList<>();
    final ArrayList<B> v1 = new ArrayList<>();

    forEachImpl(list, p -> { v0.add(p.mx1); v1.add(p.mx2); });

    return Pair.create(fromArray(v0), fromArray(v1));
  }

  public static <A, B> LinkedList<A> unfoldr(final Function<B, Optional<Pair<A, B>>> f, final B b) {
    final ArrayList<A> v = new ArrayList<>();
    Optional<Pair<A, B>> opt = f.apply(b);

    while (opt.isPresent()) {
      final Pair<A, B> p = opt.get();
      v.add(p.mx1);
      opt = f.apply(p.mx2);
    }

    return fromArray(v);
  }

  public static <A> LinkedList<A> concat(final LinkedList<LinkedList<A>> list) {
    final ArrayList<A> v = new ArrayList<>();

    forEachImpl(list, l -> { forEachImpl(l, v::add); });

    return fromArray(v);
  }

  public static <A> LinkedList<A> intercalate(final LinkedList<A> list1, final LinkedList<LinkedList<A>> list2) {
    return concat(intersperseImpl(list2, list1));
  }

  private static <A, B> LinkedList<B> getFromListOfLists(final LinkedList<LinkedList<A>> list, final Function<LinkedList<A>, B> f) {
    LinkedList<LinkedList<A>> t = list;
    final ArrayList<B> v = new ArrayList<>();

    forEachImpl(list, l -> { if (l.isNotNull()) { v.add(f.apply(l)); } });

    return fromArray(v);
  }

  private static <A> LinkedList<A> getCars(final LinkedList<LinkedList<A>> list) {
    return getFromListOfLists(list, LinkedList::car);
  }

  private static <A> LinkedList<LinkedList<A>> getCdrs(final LinkedList<LinkedList<A>> list) {
    return getFromListOfLists(list, LinkedList::cdr);
  }

  public static <A> LinkedList<LinkedList<A>> transpose(final LinkedList<LinkedList<A>> list) {
    if (list.isNull()) {
      return list;
    }
    else {
      final LinkedList<A> ys = list.mCar;
      final LinkedList<LinkedList<A>> xss = list.mCdr;
      if (ys.isNull()) {
        return transpose(xss);
      }
      else {
        final A x = ys.mCar;
        final LinkedList<A> xs = ys.mCdr;
        return create(create(x, getCars(xss)),
                      transpose(create(xs, getCdrs(xss))));
      }
    }
  }

  private static AssertionError buildExnEmptyList(final String funName) {
    return new AssertionError(String.format("%s() called on empty list.", funName));
  }

  @SafeVarargs
  public static <A> LinkedList<A> of(A ... v) {
    LinkedList<A> t = empty();

    for (int i = v.length - 1; i >= 0; --i) {
      t = create(v[i], t);
    }

    return t;
  }
}
