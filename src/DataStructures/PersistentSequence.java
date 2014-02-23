/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 *
 * @author Neo
 * @param <A>
 * @param <S>
 */
public interface PersistentSequence<A, S extends PersistentSequence<A, S>> extends Iterable<A> {
  public S addFirst(final A a);
  public S addLast(final A a);
  public S cons(final A a); // For the faithful... same as addFirst().
  public S append(final S seq);

  // Basic functions
  public A head();
  public S tail();
  public A last();
  public S init();
  public boolean isEmpty();
  public boolean isSingleton();
  public int length();

  @Override
  void forEach(Consumer<? super A> action);

  // Transformations

  /**
   *
   * @param <B>
   * @param f
   * @return
   */
  public <B> PersistentSequence<B, ?> map(final Function<A, B> f);
  public S reverse();
  public S intersperse(final A a);

  // Folds
  public <B> B foldl(final BiFunction<B, A, B> f, final B b);
  public A foldl1(final BiFunction<A, A, A> f);

  public <B> B foldr(final BiFunction<A, B, B> f, final B b);
  public A foldr1(final BiFunction<A, A, A> f);

  // Special Folds
  public boolean any(final Predicate<A> f);
  public boolean all(final Predicate<A> f);

  // Building Sequences
  public <B> PersistentSequence<B, ?> scanl(final BiFunction<B, A, B> f, final B b);
  public S scanl1(final BiFunction<A, A, A> f);

  public <B> PersistentSequence<B, ?> scanr(final BiFunction<A, B, B> f, final B b);
  public S scanr1(final BiFunction<A, A, A> f);

 /* 
  @Override
  public <ACC, B> Pair<ACC, ? extends List<B, ?>> mapAccumL(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc);
  @Override
  public <ACC, B> Pair<ACC, ? extends List<B, ?>> mapAccumR(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc);
*/
  public <ACC, B> Pair<ACC, PersistentSequence<B, ?>> mapAccumL(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc);
  public <ACC, B> Pair<ACC, PersistentSequence<B, ?>> mapAccumR(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc);

  // Subsequences
  public S take(final int n);
  public S drop(final int n);

  public Pair<S, S> splitAt(final int n);

  public S takeWhile(final Predicate<A> pred);
  public S dropWhile(final Predicate<A> pred);
  public S dropWhileEnd(final Predicate<A> pred);

  public Pair<S, S> spanBy(final Predicate<A> pred);
  public Pair<S, S> breakBy(final Predicate<A> pred);

  public Optional<S> stripPrefix(final S prefix);

  public PersistentSequence<S, ?> group();
  public PersistentSequence<S, ?> groupBy(final BiPredicate<A, A> eqPred);
  public PersistentSequence<S, ?> inits();
  public PersistentSequence<S, ?> tails();

  // Predicates
  public boolean isPrefixOf(final S seq);
  public boolean isSuffixOf(final S seq);
  public boolean isInfixOf(final S seq);

  // Searching (by Equality)
  public boolean contains(final A a);
  public boolean notContains(final A a);

  // Searching (by Predicate)
  public Optional<A> find(final Predicate<A> pred);
  public S filter(final Predicate<A> pred);
  public Pair<S, S> partition(final Predicate<A> pred);

  // Indexing
  public A nth(final int n);
  public S nthTail(final int n);

  public OptionalInt elemIndex(final A a);
  public PersistentSequence<Integer, ?> elemIndices(final A a);

  public OptionalInt findIndex(final Predicate<A> pred);
  public PersistentSequence<Integer, ?> findIndices(final Predicate<A> pred);

  public S nub();
  public S nubBy(final Comparator<? super A> cmp);
  public S delete(final A a);
  public S deleteBy(final A a, final BiPredicate<A, A> pred);
  public S seqDiff(final S seq);

  public S union(final S seq);
  public S unionBy(final S seq, final Comparator<? super A> cmp);
  public S intersect(final S seq);
  public S intersectBy(final S seq, final Comparator<? super A> cmp);

  public S sort();
  public S sortBy(final Comparator<? super A> cmp);
  public S insert(final A a);
  public S insertBy(final A a, final Comparator<? super A> cmp);

  S deleteFirstBy(final S seq, final BiPredicate<A, A> pred);

  public A max();
  public A maxBy(final Comparator<? super A> cmp);
    
  public A min();
  public A minBy(final Comparator<? super A> cmp);

  public PersistentSequence<S, ?> subsequences();
  public PersistentSequence<S, ?> permutations();

  public Iterator<A> iterator();

  public Stream<A> stream();

  public ArrayList<A> toArray();
}
