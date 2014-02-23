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
 * @param <L>
 */
public interface List<A, L extends List<A, L>> extends Iterable<A>, PersistentSequence<A, L> {
  // Constructor
  @Override
  public L addFirst(final A a);
  @Override
  public L addLast(final A a);
  @Override
  public L cons(final A a); // For the faithful... same as addFirst().
  @Override
  public L append(final L list);

  // Basic functions
  @Override
  public A head();
  @Override
  public L tail();
  @Override
  public A last();
  @Override
  public L init();
  @Override
  public boolean isEmpty();
  @Override
  public boolean isSingleton();
  @Override
  public int length();

  @Override
  void forEach(Consumer<? super A> action);

  // Transformations
  @Override
  public <B> List<B, ?> map(final Function<A, B> f);
  @Override
  public L reverse();
  @Override
  public L intersperse(final A a);

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

  // Building lists
  @Override
  public <B> List<B, ?> scanl(final BiFunction<B, A, B> f, final B b);
  @Override
  public L scanl1(final BiFunction<A, A, A> f);

  @Override
  public <B> List<B, ?> scanr(final BiFunction<A, B, B> f, final B b);
  @Override
  public L scanr1(final BiFunction<A, A, A> f);

  @Override
  public <ACC, B> Pair<ACC, PersistentSequence<B, ?>> mapAccumL(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc);
  @Override
  public <ACC, B> Pair<ACC, PersistentSequence<B, ?>> mapAccumR(final BiFunction<ACC, A, Pair<ACC, B>> f, final ACC acc);

  // Sublists
  @Override
  public L take(final int n);
  @Override
  public L drop(final int n);

  @Override
  public Pair<L, L> splitAt(final int n);

  @Override
  public L takeWhile(final Predicate<A> pred);
  @Override
  public L dropWhile(final Predicate<A> pred);
  @Override
  public L dropWhileEnd(final Predicate<A> pred);

  @Override
  public Pair<L, L> spanBy(final Predicate<A> pred);
  @Override
  public Pair<L, L> breakBy(final Predicate<A> pred);

  @Override
  public Optional<L> stripPrefix(final L prefix);

  @Override
  public List<L, ?> group();
  @Override
  public List<L, ?> groupBy(final BiPredicate<A, A> eqPred);
  @Override
  public List<L, ?> inits();
  @Override
  public List<L, ?> tails();

  // Predicates
  @Override
  public boolean isPrefixOf(final L list);
  @Override
  public boolean isSuffixOf(final L list);
  public boolean isInfixOf(final L list);

  // Searching (by Equality)
  @Override
  public boolean contains(final A a);
  @Override
  public boolean notContains(final A a);

  // Searching (by Predicate)
  @Override
  public Optional<A> find(final Predicate<A> pred);
  @Override
  public L filter(final Predicate<A> pred);
  @Override
  public Pair<L, L> partition(final Predicate<A> pred);

  // Indexing
  @Override
  public A nth(final int n);
  @Override
  public L nthTail(final int n);

  @Override
  public OptionalInt elemIndex(final A a);
  @Override
  public List<Integer, ?> elemIndices(final A a);

  @Override
  public OptionalInt findIndex(final Predicate<A> pred);
  @Override
  public List<Integer, ?> findIndices(final Predicate<A> pred);

  @Override
  public L nub();
  @Override
  public L nubBy(final Comparator<? super A> cmp);
  @Override
  public L delete(final A a);
  @Override
  public L deleteBy(final A a, final BiPredicate<A, A> pred);
  public L seqDiff(final L list);

  @Override
  public L union(final L list);
  @Override
  public L unionBy(final L list, final Comparator<? super A> cmp);
  @Override
  public L intersect(final L list);
  @Override
  public L intersectBy(final L list, final Comparator<? super A> cmp);

  @Override
  public L sort();
  @Override
  public L sortBy(final Comparator<? super A> cmp);
  @Override
  public L insert(final A a);
  @Override
  public L insertBy(final A a, final Comparator<? super A> cmp);

  @Override
  L deleteFirstBy(final L list, final BiPredicate<A, A> pred);

  @Override
  public A max();
  @Override
  public A maxBy(final Comparator<? super A> cmp);
    
  @Override
  public A min();
  @Override
  public A minBy(final Comparator<? super A> cmp);

  @Override
  public List<L, ?> subsequences();
  @Override
  public List<L, ?> permutations();

  @Override
  public Iterator<A> iterator();

  @Override
  public Stream<A> stream();

  @Override
  public ArrayList<A> toArray();
}
