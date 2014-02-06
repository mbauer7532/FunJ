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
public interface PersistentSequence<A, S extends PersistentSequence<A, S>> extends List<A, S>, Iterable<A> {
  // Constructor
  @Override
  public S addFirst(final A a);
  @Override
  public S addLast(final A a);
  @Override
  public S cons(final A a); // For the faithful... same as addFirst().
  @Override
  public S append(final S seq);

  // Basic functions
  @Override
  public A head();
  @Override
  public S tail();
  @Override
  public A last();
  @Override
  public S init();
  @Override
  public boolean isEmpty();
  @Override
  public boolean isSingleton();
  @Override
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
  @Override
  public <B> PersistentSequence<B, ?> map(final Function<A, B> f);
  @Override
  public S reverse();
  @Override
  public S intersperse(final A a);

  // Folds
  @Override
  public <B> B foldl(final BiFunction<B, A, B> f, final B b);
  @Override
  public A foldl1(final BiFunction<A, A, A> f);

  @Override
  public <B> B foldr(final BiFunction<A, B, B> f, final B b);
  @Override
  public A foldr1(final BiFunction<A, A, A> f);

  // Special Folds
  @Override
  public boolean any(final Predicate<A> f);
  @Override
  public boolean all(final Predicate<A> f);

  // Building Sequences
  @Override
  public <B> PersistentSequence<B, ?> scanl(final BiFunction<B, A, B> f, final B b);
  @Override
  public S scanl1(final BiFunction<A, A, A> f);

  @Override
  public <B> PersistentSequence<B, ?> scanr(final BiFunction<A, B, B> f, final B b);
  @Override
  public S scanr1(final BiFunction<A, A, A> f);

  @Override
  public <ACC, B> Pair<ACC, PersistentSequence<B, ?>> mapAccumL(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc);
  @Override
  public <ACC, B> Pair<ACC, PersistentSequence<B, ?>> mapAccumR(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc);

  // Subsequences
  @Override
  public S take(final int n);
  @Override
  public S drop(final int n);

  @Override
  public Pair<S, S> splitAt(final int n);

  @Override
  public S takeWhile(final Predicate<A> pred);
  @Override
  public S dropWhile(final Predicate<A> pred);
  @Override
  public S dropWhileEnd(final Predicate<A> pred);

  @Override
  public Pair<S, S> spanBy(final Predicate<A> pred);
  @Override
  public Pair<S, S> breakBy(final Predicate<A> pred);

  @Override
  public Optional<S> stripPrefix(final S prefix);

  @Override
  public PersistentSequence<S, ?> group();
  @Override
  public PersistentSequence<S, ?> groupBy(final BiPredicate<A, A> eqPred);
  @Override
  public PersistentSequence<S, ?> inits();
  @Override
  public PersistentSequence<S, ?> tails();

  // Predicates
  @Override
  public boolean isPrefixOf(final S seq);
  @Override
  public boolean isSuffixOf(final S seq);
  @Override
  public boolean isInfixOf(final S seq);

  // Searching (by Equality)
  @Override
  public boolean contains(final A a);
  @Override
  public boolean notContains(final A a);

  // Searching (by Predicate)
  @Override
  public Optional<A> find(final Predicate<A> pred);
  @Override
  public S filter(final Predicate<A> pred);
  @Override
  public Pair<S, S> partition(final Predicate<A> pred);

  // Indexing
  @Override
  public A nth(final int n);
  @Override
  public S nthTail(final int n);

  @Override
  public OptionalInt elemIndex(final A a);
  @Override
  public PersistentSequence<Integer, ?> elemIndices(final A a);

  @Override
  public OptionalInt findIndex(final Predicate<A> pred);
  @Override
  public PersistentSequence<Integer, ?> findIndices(final Predicate<A> pred);

  @Override
  public S nub();
  @Override
  public S nubBy(final Comparator<? super A> cmp);
  @Override
  public S delete(final A a);
  @Override
  public S deleteBy(final A a, final BiPredicate<A, A> pred);
  public S seqDiff(final S seq);

  @Override
  public S union(final S seq);
  @Override
  public S unionBy(final S seq, final Comparator<? super A> cmp);
  @Override
  public S intersect(final S seq);
  @Override
  public S intersectBy(final S seq, final Comparator<? super A> cmp);

  @Override
  public S sort();
  @Override
  public S sortBy(final Comparator<? super A> cmp);
  @Override
  public S insert(final A a);
  @Override
  public S insertBy(final A a, final Comparator<? super A> cmp);

  @Override
  S deleteFirstBy(final S seq, final BiPredicate<A, A> pred);

  @Override
  public A max();
  @Override
  public A maxBy(final Comparator<? super A> cmp);
    
  @Override
  public A min();
  @Override
  public A minBy(final Comparator<? super A> cmp);

  @Override
  public PersistentSequence<S, ?> subsequences();
  @Override
  public PersistentSequence<S, ?> permutations();

  @Override
  public Iterator<A> iterator();

  @Override
  public Stream<A> stream();

  @Override
  public ArrayList<A> toArray();
}
