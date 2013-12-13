/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import DataStructures.TuplesModule.Triple;
import java.util.ArrayList;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 *
 * @author Neo
 */
public class SinglyLinkedListModule {
  public interface List<A, L extends List<A, L>> {
    // Constructor
    public L cons(final A a);

    // Basic functions
    public A head();
    public L tail();
    public A last();
    public L init();
    public boolean isEmpty();
    public int length();

    // Transformations
    public <B> List<B, ?> map(final Function<A, B> f);
    public L reverse();
    public L intersperse(final A a);

    // Folds
    public <B> B foldl(final BiFunction<B, A, B> f, final B b);
    public A foldl1(final BiFunction<A, A, A> f);

    public <B> B foldr(final BiFunction<A, B, B> f, final B b);
    public A foldr1(final BiFunction<A, A, A> f);

    // Special Folds
    public boolean any(final Predicate<A> f);
    public boolean all(final Predicate<A> f);

    // Building lists
    public <B> List<B, ?> scanl(final BiFunction<B, A, B> f, final B b);
    public L scanl1(final BiFunction<A, A, A> f);

    public <B> List<B, ?> scanr(final BiFunction<A, B, B> f, final B b);
    public L scanr1(final BiFunction<A, A, A> f);

    public <ACC, B> Pair<ACC, List<B, ?>> mapAccumL(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc);
    public <ACC, B> Pair<ACC, List<B, ?>> mapAccumR(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc);

    // Sublists
    public L take(final int n);
    public L drop(final int n);

    public Pair<L, L> splitAt(final int n);

    public L takeWhile(final Predicate<A> pred);
    public L dropWhile(final Predicate<A> pred);
    public L dropWhileEnd(final Predicate<A> pred);

    public Pair<L, L> spanPred(final Predicate<A> pred);
    public Pair<L, L> breakPred(final Predicate<A> pred);
    
    public Optional<L> stripPrefix(final L list);

    public List<L, ?> group();
    public List<L, ?> inits();
    public List<L, ?> tails();

    // Predicates
    public boolean isPrefixOf(final L list);
    public boolean isSuffixOf(final L list);
    public boolean isInfixOf(final L list);

    // Searching (by Equality)
    public boolean elem(final A a);
    public boolean notElem(final A a);

    // Searching (by Predicate)
    public Optional<A> find(final Predicate<A> pred);
    public L filter(final Predicate<A> pred);
    public Pair<L, L> partition(final Predicate<A> pred);

    // Indexing
    public A nth(final int n);

    public OptionalInt elemIndex(final A a);
    public List<Integer, ?> elemIndices(final A a);

    public OptionalInt findIndex(final Predicate<A> pred);
    public List<Integer, ?> findIndices(final Predicate<A> pred);
  }
  
  
  
  public static class LinkedList<A> implements List<A, LinkedList<A>> {
    public static <T> LinkedList<T> create(final T a, final LinkedList<T> list) { return new LinkedList<>(a, list); }

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

    @Override
    public LinkedList<A> cons(final A a) {
      return create(a, this);
    }

    private boolean isNull()    { return this == sEmptyList; }
    private boolean isNotNull() { return this != sEmptyList; }

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

    @Override
    public A last() {
      LinkedList<A> list = null, next = this;
      while (next.isNotNull()) {
        list = next;
        next = next.mCdr;
      }

      if (list == null) {
        throw new AssertionError("last() called on empty list.");
      }
      else {
        return list.mCar;
      }
    }

    private static <A> ArrayList<A> toArray(final LinkedList<A> m) {
      final ArrayList<A> v = new ArrayList<>();
      LinkedList<A> t = m;

      while (t.isNotNull()) {
        v.add(t.mCar);
        t = t.mCdr;
      }

      return v;
    }

    private static <A> LinkedList<A> fromArray(final ArrayList<A> v) {
      final int lastIdx = v.size() - 1;
      return IntStream.rangeClosed(0, lastIdx)
                      .mapToObj(i -> v.get(lastIdx - i))
                      .reduce(empty(),
                              (list, elem) -> list.cons(elem),
                              (l1, l2) -> { throw new AssertionError("Should never be called. Stream was sequential."); });
    }

    @Override
    public LinkedList<A> init() {
      if (isNull()) {
        throw new AssertionError("init() called on empty list.");
      }
      else {
        LinkedList<A> t = this;

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
    public boolean isEmpty() {
      return isNull();
    }

    @Override
    public int length() {
      int len = 0;
      LinkedList<A> t = this;
      while (t.isNotNull()) {
        ++len;
        t = t.mCdr;
      }

      return len;
    }

    @Override
    public <B> List<B, ?> map(Function<A, B> f) {
      LinkedList<B> lb = empty();
      LinkedList<A> la = this;

      while (la.isNotNull()) {
        lb = lb.cons(f.apply(la.mCar));
        la = la.mCdr;
      }

      return lb;
    }

    @Override
    public LinkedList<A> reverse() {
      LinkedList<A> acc = empty(), t = this;

      while (t.isNotNull()) {
        acc = acc.cons(t.mCar);
        t = t.mCdr;
      }

      return acc;
    }

    @Override
    public LinkedList<A> intersperse(final A a) {
      if (isNull()) {
        return empty();
      }
      else {
        final ArrayList<A> v = new ArrayList<>();
        v.add(mCar);
        LinkedList<A> t = mCdr;

        while (t.isNotNull()) {
          v.add(a);
          v.add(t.mCar);
          t = t.mCdr;
        }

        return fromArray(v);
      }
    }

    @Override
    public <B> B foldl(final BiFunction<B, A, B> f, final B b) {
      B acc = b;
      LinkedList<A> t = this;

      while (t.isNotNull()) {
        acc = f.apply(acc, t.mCar);
        t = t.mCdr;
      }

      return acc;
    }

    @Override
    public A foldl1(final BiFunction<A, A, A> f) {
      if (isNull()) {
        throw new AssertionError("foldl1() applied to an empty list.");
      }
      else {
        return mCdr.foldl(f, mCar);
      }
    }

    @Override
    public <B> B foldr(final BiFunction<A, B, B> f, final B b) {
      final ArrayList<A> v = toArray(this);
      final int lastIdx = v.size() - 1;
      return IntStream.rangeClosed(0, lastIdx)
                      .mapToObj(i -> v.get(lastIdx - i))
                      .reduce(b,
                              (acc, e) -> f.apply(e, acc),
                              (b0, b1) -> { throw new AssertionError("Should never be called. Stream was sequential."); });
    }

    @Override
    public A foldr1(final BiFunction<A, A, A> f) {
      if (isNull()) {
        throw new AssertionError("foldr1() applied to an empty list.");
      }
      else {
        return mCdr.foldr(f, mCar);
      }
    }

    @Override
    public boolean any(final Predicate<A> f) {
      LinkedList<A> t = this;
      
      while (t.isNotNull()) {
        if (f.test(t.mCar)) {
          return true;
        }
        t = t.mCdr;
      }

      return false;
    }

    @Override
    public boolean all(final Predicate<A> f) {
      return ! any(f.negate());
    }

    @Override
    public <B> LinkedList<B> scanl(BiFunction<B, A, B> f, B b) {
      B acc = b;
      LinkedList<A> t = this;
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
    public LinkedList<A> scanl1(BiFunction<A, A, A> f) {
      if (isNull()) {
        return empty();
      }
      else {
        return mCdr.scanl(f, mCar);
      }
    }

    @Override
    public <B> LinkedList<B> scanr(BiFunction<A, B, B> f, B b) {
      final ArrayList<A> v = toArray(this);
      final int lastIdx = v.size() - 1;
      final ArrayList<B> bs = new ArrayList<>();

      bs.add(b);
      IntStream.rangeClosed(0, lastIdx)
               .mapToObj(i -> v.get(lastIdx - i))
               .reduce(b,
                       (acc, e) -> { final B newAcc = f.apply(e, acc); bs.add(newAcc); return newAcc; },
                       (b0, b1) -> { throw new AssertionError("Should never be called. Stream was sequential."); });

      return fromArray(bs);
    }

    @Override
    public LinkedList<A> scanr1(BiFunction<A, A, A> f) {
      if (isNull()) {
        return empty();
      }
      else {
        return mCdr.scanr(f, mCar);
      }
    }

    @Override
    public <ACC, B> Pair<ACC, List<B, ?>> mapAccumL(BiFunction<ACC, A, Pair<ACC, B>> f, ACC acc) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <ACC, B> Pair<ACC, List<B, ?>> mapAccumR(BiFunction<ACC, A, Pair<ACC, B>> f, ACC acc) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<A> take(final int n) {
      final ArrayList<A> v = new ArrayList<>();
      LinkedList<A> t = this;

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
    public LinkedList<A> drop(final int n) {
      LinkedList<A> t = this;
      for (int i = 0; i != n; ++i) {
        if (t.isNull()) {
          return t;
        }
        t = t.mCdr;
      }

      return t;
    }

    @Override
    public Pair<LinkedList<A>, LinkedList<A>> splitAt(final int n) {
      LinkedList<A> t = this;
      final ArrayList<A> v = new ArrayList<>();

      for (int i = 0; i != n; ++i) {
        if (t.isNull()) {
          return Pair.create(this, t);
        }
        v.add(t.mCar);
        t = t.mCdr;
      }

      return Pair.create(fromArray(v), t);
    }

    @Override
    public LinkedList<A> takeWhile(Predicate<A> pred) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<A> dropWhile(Predicate<A> pred) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<A> dropWhileEnd(Predicate<A> pred) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Pair<LinkedList<A>, LinkedList<A>> spanPred(Predicate<A> pred) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Pair<LinkedList<A>, LinkedList<A>> breakPred(Predicate<A> pred) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Optional<LinkedList<A>> stripPrefix(LinkedList<A> list) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <L2 extends List<LinkedList<A>, L2>> L2 group() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <L2 extends List<LinkedList<A>, L2>> L2 inits() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <L2 extends List<LinkedList<A>, L2>> L2 tails() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isPrefixOf(LinkedList<A> list) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isSuffixOf(LinkedList<A> list) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isInfixOf(LinkedList<A> list) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean elem(A a) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean notElem(A a) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Optional<A> find(Predicate<A> pred) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<A> filter(Predicate<A> pred) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Pair<LinkedList<A>, LinkedList<A>> partition(Predicate<A> pred) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public A nth(int n) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OptionalInt elemIndex(A a) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <L2 extends List<Integer, L2>> L2 elemIndices(A a) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OptionalInt findIndex(Predicate<A> pred) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <L2 extends List<Integer, L2>> L2 findIndices(Predicate<A> pred) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  }
  
  class Factory{
    // replicate :: Int -> a -> [a]
    // unfoldr :: (b -> Maybe (a, b)) -> b -> [a]
    // public boolean lookup(final A a); // this must be an association list
    // Zipping and unzipping
    // public <B> SLinkedList<Pair<A, B>> zip(final SLinkedList<B> list);
    // public <B, C> SLinkedList<Triple<A, B, C>> zip(final SLinkedList<B> list1, final SLinkedList<C> list2);
    
    // public <B> Pair<SLinkedList<A>, SLinkedList<B>> unzip();

//     public L concat();

//    public <L2 extends List<L, L2>> L intercalate(final List<L, ?> list);
//    public <L2 extends List<L, L2>> L2 transpose();
//    public <L2 extends List<L, L2>> L2 subsequences();
//    public <L2 extends List<L, L2>> L2 permutations();
//    public <B, L2 extends List<B, L2>> List<B, L2> concatMap(final Function<A, L2> f);

  }
}
