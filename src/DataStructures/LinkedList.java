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
import java.util.Optional;
import java.util.OptionalInt;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Neo
 */
public final class LinkedList<A> implements List<A, LinkedList<A>> {
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

  @SuppressWarnings("unchecked")
    public static <T> LinkedList<T> empty() { return (LinkedList<T>) sEmptyList; }

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
    intersperseImpl(mapImpl(this, e -> e.toString()), ", ").forEach(sb::append);
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

  @Override
  public int hashCode() {
    return foldlImpl(this, (h, e) -> h + e.hashCode(), 23);
  }

  private static <A> LinkedList<A> appendImpl(final LinkedList<A> list1, final LinkedList<A> list2) {
    final ArrayList<A> v = toArray(list1);
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
      throw new AssertionError("head() called on empty list.");
    }
    else {
      return mCar;
    }
  }

  @Override
  public LinkedList<A> tail() {
    if (isNull()) {
      throw new AssertionError("tail() called on empty list.");
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
      throw new AssertionError("last() called on empty list.");
    }
    else {
      return t.mCar;
    }
  }

  @Override
  public A last() {
    return lastImpl(this);
  }

  public static <A> ArrayList<A> toArray(final LinkedList<A> m) {
    final ArrayList<A> v = new ArrayList<>();
    LinkedList<A> t = m;

    while (t.isNotNull()) {
      v.add(t.mCar);
      t = t.mCdr;
    }

    return v;
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
      throw new AssertionError("init() called on empty list.");
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
    LinkedList<A> la = list;

    while (la.isNotNull()) {
      v.add(f.apply(la.mCar));
      la = la.mCdr;
    }

    return fromArray(v);
  }

  @Override
  public <B> LinkedList<B> map(final Function<A, B> f) {
    return mapImpl(this, f);
  }

  private static <A> LinkedList<A> reverseImpl(final LinkedList<A> list) {
    LinkedList<A> acc = empty(), t = list;

    while (t.isNotNull()) {
      acc = create(t.mCar, acc);
      t = t.mCdr;
    }

    return acc;
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

      while (t.isNotNull()) {
        v.add(a);
        v.add(t.mCar);
        t = t.mCdr;
      }

      return fromArray(v);
    }
  }

  @Override
  public LinkedList<A> intersperse(final A a) {
    return intersperseImpl(this, a);
  }

  private static <A, B> B foldlImpl(final LinkedList<A> list, final BiFunction<B, A, B> f, final B b) {
    B acc = b;
    LinkedList<A> t = list;

    while (t.isNotNull()) {
      acc = f.apply(acc, t.mCar);
      t = t.mCdr;
    }

    return acc;
  }

  @Override
  public <B> B foldl(final BiFunction<B, A, B> f, final B b) {
    return foldlImpl(this, f, b);
  }

  private static <A> A foldl1Impl(final LinkedList<A> list, final BiFunction<A, A, A> f) {
    if (list.isNull()) {
      throw new AssertionError("foldl1() applied to an empty list.");
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
    final ArrayList<A> v = toArray(list);
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
      throw new AssertionError("foldr1() applied to an empty list.");
    }
    else {
      return foldrImpl(list.mCdr, f, list.mCar);
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
    final ArrayList<A> v = toArray(list);
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
    LinkedList<A> t = list;
    ACC newAcc = acc;
    ArrayList<B> v = new ArrayList<>();

    while (t.isNotNull()) {
      final Pair<ACC, B> r = f.apply(newAcc, t.mCar);
      newAcc = r.mx1;        
      v.add(r.mx2);
      t = t.mCdr;
    }
    return Pair.create(newAcc, fromArray(v));
  }

  @Override
  public <ACC, B> Pair<ACC, List<B, ?>> mapAccumL(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc) {
    return mapAccumLImpl(this, f, acc);
  }

  private static <A, ACC, B> Pair<ACC, List<B, ?>> mapAccumRImpl(
    final LinkedList<A> list,
    final BiFunction<ACC, A, Pair<ACC, B>> f,
    final ACC acc) {
    final ArrayList<A> v = toArray(list);
    final ArrayList<B> u = new ArrayList<>();
    final int lastIdx = v.size() - 1;
    final ACC newAcc = IntStream.rangeClosed(0, lastIdx)
      .mapToObj(i -> v.get(lastIdx - i))
      .reduce(acc,
              (rAcc, e) -> { final Pair<ACC, B> r = f.apply(rAcc, e); u.add(r.mx2); return r.mx1; },
              Functionals::functionShouldNotBeCalled);
    return Pair.create(newAcc, u.stream().reduce(empty(), LinkedList::createInv, Functionals::functionShouldNotBeCalled));
  }

  @Override
  public <ACC, B> Pair<ACC, List<B, ?>> mapAccumR(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc) {
    return mapAccumRImpl(this, f, acc);
  }

  private static <A> LinkedList<A> takeImpl(final LinkedList<A> list, final int n) {
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

  @Override
  public LinkedList<A> take(final int n) {
    return takeImpl(this, n);
  }

  private static <A> LinkedList<A> dropImpl(final LinkedList<A> list, final int n) {
    LinkedList<A> t = list;
    for (int i = 0; i != n; ++i) {
      if (t.isNull()) {
        return t;
      }
      t = t.mCdr;
    }

    return t;
  }

  @Override
  public LinkedList<A> drop(final int n) {
    return dropImpl(this, n);
  }

  private static <A> Pair<LinkedList<A>, LinkedList<A>> splitAtImpl(final LinkedList<A> list, final int n) {
    LinkedList<A> t = list;
    final ArrayList<A> v = new ArrayList<>();

    for (int i = 0; i != n; ++i) {
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
    final ArrayList<A> v = toArray(list);

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

  private static <A> Pair<LinkedList<A>, LinkedList<A>> spanByPredicateImpl(final LinkedList<A> list, final Predicate<A> pred) {
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
  public Pair<LinkedList<A>, LinkedList<A>> spanByPredicate(final Predicate<A> pred) {
    return spanByPredicateImpl(this, pred);
  }

  @Override
  public Pair<LinkedList<A>, LinkedList<A>> breakByPredicate(final Predicate<A> pred) {
    return spanByPredicateImpl(this, pred.negate());
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
      LinkedList<A> t = list;

      while (t.isNotNull()) {
        final int idx = res.size() - 1;
        final ArrayList<A> v = res.get(idx);
        final A a = t.mCar;

        if (v.isEmpty() || eqPred.test(a, v.get(0))) {
          v.add(a);
        }
        else {
          final ArrayList<A> newV = new ArrayList<>();
          newV.add(a);
          res.add(newV);
        }
      }

      return fromArray(res.stream().map(LinkedList::fromArray).collect(Collectors.toCollection(ArrayList::new)));
    }
  }

  @Override
  public LinkedList<LinkedList<A>> group() {
    return groupByImpl(this, (e1, e2) -> e1.equals(e2));
  }

  @Override
  public LinkedList<LinkedList<A>> groupBy(final BiPredicate<A, A> eqPred) {
    return groupByImpl(this, eqPred);
  }

  private static <A> LinkedList<LinkedList<A>> initsImpl(final LinkedList<A> list) {
    final ArrayList<A> v = toArray(list);
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
    LinkedList<A> t = list;
      
    while (t.isNotNull()) {
      if (pred.test(t.mCar)) {
        v.add(t.mCar);
      }
      t = t.mCdr;
    }

    return fromArray(v);
  }

  @Override
  public LinkedList<A> filter(final Predicate<A> pred) {
    return filterImpl(this, pred);
  }

  private static <A> Pair<LinkedList<A>, LinkedList<A>> partitionImpl(final LinkedList<A> list, final Predicate<A> pred) {
    final ArrayList<A> vt = new ArrayList<>(), vf = new ArrayList<>();
    LinkedList<A> t = list;
      
    while (t.isNotNull()) {
      (pred.test(t.mCar) ? vt : vf).add(t.mCar);
      t = t.mCdr;
    }

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
    return elemIndicesImpl(this, x -> a.equals(x));
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

    LinkedList<A> t = list;
    while (t.isNotNull()) {
      final A elem = t.mCar;
      final boolean notAreadyThere = s.add(elem);
      if (notAreadyThere) {
        v.add(elem);
      }
      t = t.mCdr;
    }

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
    return deleteByImpl(this, a, (x, y) -> x.equals(y));
  }

  @Override
  public LinkedList<A> deleteBy(final A a, final BiPredicate<A, A> pred) {
    return deleteByImpl(this, a, pred);
  }

  @Override
  public LinkedList<A> deleteFirstBy(final LinkedList<A> list, final BiPredicate<A, A> pred) {
    final Ref<LinkedList<A>> ref = new Ref<>(empty());
    list.forEach(elem -> { ref.r = deleteByImpl(ref.r, elem, pred); });
    return ref.r;
  }

  @Override
  public LinkedList<A> listDiff(LinkedList<A> list) {
    return foldlImpl(list, (xs, x) -> xs.delete(x), this);
  }

  private static <A> LinkedList<A> unionByImpl(final LinkedList<A> list1, final LinkedList<A> list2, final Comparator<? super A> cmp) {
    final TreeSet<A> s = new TreeSet<>(cmp);
    final ArrayList<A> v = new ArrayList<>();

    list1.forEach(a -> { s.add(a); v.add(a); });
    list2.forEach(a -> {
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

    list2.forEach(a -> { s.add(a); });
    list1.forEach(a -> {
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
    final ArrayList<A> v = toArray(list);
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
      LinkedList<A> t = list;
      A m = t.mCar;
      t = t.mCdr;
        
      while (t.isNotNull()) {
        m = selector.apply(m, t.mCar);
        t = t.mCdr;
      }

      return Optional.of(m);
    }
  }

  @Override
  public A max() {
    return Functionals.mapOptOrElse(orderingByImpl(this, (m0, m1) -> Functionals.comparator(m0, m1) >= 0 ? m0 : m1),
                                    Function.identity(),
                                    () -> { throw new AssertionError("Cannot apply function max() on an empty list."); });
  }

  @Override
  public A maxBy(final Comparator<? super A> cmp) {
    return Functionals.mapOptOrElse(orderingByImpl(this, (m0, m1) -> cmp.compare(m0, m1) >= 0 ? m0 : m1),
                                    Function.identity(),
                                    () -> { throw new AssertionError("Cannot apply function maxBy() on an empty list."); });
  }

  @Override
  public A min() {
    return Functionals.mapOptOrElse(orderingByImpl(this, (m0, m1) -> Functionals.comparator(m0, m1) >= 0 ? m1 : m0),
                                    Function.identity(),
                                    () -> { throw new AssertionError("Cannot apply function min() on an empty list."); });
  }

  @Override
  public A minBy(final Comparator<? super A> cmp) {
    return Functionals.mapOptOrElse(orderingByImpl(this, (m0, m1) -> cmp.compare(m0, m1) >= 0 ? m1 : m0),
                                    Function.identity(),
                                    () -> { throw new AssertionError("Cannot apply function minBy() on an empty list."); });
  }

  @Override
  public LinkedList<LinkedList<A>> subsequences(final LinkedList<A> list) {
    return null;
  }

  @Override
  public LinkedList<LinkedList<A>> permutations(final LinkedList<A> list) {
    return null;
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

    list.forEach(p -> { v0.add(p.mx1); v1.add(p.mx2); });

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

    list.forEach(l -> { l.forEach(e -> { v.add(e); }); });
      
    return fromArray(v);
  }

  public static <A, B> LinkedList<B> concatMap(final Function<A, LinkedList<B>> f, final LinkedList<A> list) {
    final ArrayList<B> v = new ArrayList<>();

    list.forEach(l -> f.apply(l).forEach(e -> { v.add(e); }));
      
    return fromArray(v);
  }

  public static <A> LinkedList<A> intercalate(final LinkedList<A> list1, final LinkedList<LinkedList<A>> list2) {
    return concat(intersperseImpl(list2, list1));
  }

  private static <A> LinkedList<A> getCars(final LinkedList<LinkedList<A>> list) {
    return list.filter(LinkedList::isNull).map(LinkedList::car);
  }

  private static <A> LinkedList<LinkedList<A>> getCdrs(final LinkedList<LinkedList<A>> list) {
    return list.filter(LinkedList::isNull).map(LinkedList::cdr);
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

}