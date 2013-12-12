/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import DataStructures.TuplesModule.Triple;
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
    public <B, L2 extends List<B, L2>> List<B, L2> map(final Function<A, L2> f);
    public L reverse();
    public L intersperse(final A a);
    public <L2 extends List<L, L2>> L intercalate(final L2 list);
    public <L2 extends List<L, L2>> L2 transpose();
    public <L2 extends List<L, L2>> L2 subsequences();
    public <L2 extends List<L, L2>> L2 permutations();

    // Folds
    public <B> B foldl(final BiFunction<B, A, B> f, final B b);
    public A foldl1(final BiFunction<A, A, A> f);

    public <B> B foldr(final BiFunction<A, B, B> f, final B b);
    public A foldr1(final BiFunction<A, A, A> f);

    // Special Folds
    public L concat();
    public <B, L2 extends List<B, L2>> List<B, L2> concatMap(final Function<A, L2> f);
    public boolean any(final Predicate<A> f);
    public boolean all(final Predicate<A> f);

    // Building lists
    public <B, L2 extends List<B, L2>> L2 scanl(final BiFunction<B, A, B> f, final B b);
    public <B> L scanl1(final BiFunction<A, A, A> f);

    public <B, L2 extends List<B, L2>> L2 scanr(final BiFunction<A, B, B> f, final B b);
    public L scanr1(final BiFunction<A, A, A> f);

    public <ACC, B, L2 extends List<B, L2>> Pair<ACC, L2> mapAccumL(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc);
    public <ACC, B, L2 extends List<B, L2>> Pair<ACC, L2> mapAccumR(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc);

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

    public <L2 extends List<L, L2>> L2 group();
    public <L2 extends List<L, L2>> L2 inits();
    public <L2 extends List<L, L2>> L2 tails();

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
    public <L2 extends List<Integer, L2>> L2 elemIndices(final A a);

    public OptionalInt findIndex(final Predicate<A> pred);
    public <L2 extends List<Integer, L2>> L2 findIndices(final Predicate<A> pred);

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

    private boolean isNull() { return this == sEmptyList; }

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
      while (! next.isNull()) {
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

    @Override
    public LinkedList<A> init() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEmpty() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int length() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <B, L2 extends List<B, L2>> List<B, L2> map(Function<A, L2> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<A> reverse() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<A> intersperse(A a) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <L2 extends List<LinkedList<A>, L2>> LinkedList<A> intercalate(L2 list) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <L2 extends List<LinkedList<A>, L2>> L2 transpose() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <L2 extends List<LinkedList<A>, L2>> L2 subsequences() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <L2 extends List<LinkedList<A>, L2>> L2 permutations() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <B> B foldl(BiFunction<B, A, B> f, B b) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public A foldl1(BiFunction<A, A, A> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <B> B foldr(BiFunction<A, B, B> f, B b) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public A foldr1(BiFunction<A, A, A> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<A> concat() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <B, L2 extends List<B, L2>> List<B, L2> concatMap(Function<A, L2> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean any(Predicate<A> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean all(Predicate<A> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <B, L2 extends List<B, L2>> L2 scanl(BiFunction<B, A, B> f, B b) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <B> LinkedList<A> scanl1(BiFunction<A, A, A> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <B, L2 extends List<B, L2>> L2 scanr(BiFunction<A, B, B> f, B b) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<A> scanr1(BiFunction<A, A, A> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <ACC, B, L2 extends List<B, L2>> Pair<ACC, L2> mapAccumL(BiFunction<ACC, A, Pair<ACC, B>> f, ACC acc) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <ACC, B, L2 extends List<B, L2>> Pair<ACC, L2> mapAccumR(BiFunction<ACC, A, Pair<ACC, B>> f, ACC acc) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<A> take(int n) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<A> drop(int n) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Pair<LinkedList<A>, LinkedList<A>> splitAt(int n) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
  }
}
