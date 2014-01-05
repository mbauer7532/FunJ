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
public interface List<A, L extends List<A, L>> extends Iterable<A> {
  // Constructor
  public L addFirst(final A a);
  public L addLast(final A a);
  public L cons(final A a); // For the faithful... same as addFirst().
  public L append(final L list);

  // Basic functions
  public A head();
  public L tail();
  public A last();
  public L init();
  public boolean isEmpty();
  public boolean isSingleton();
  public int length();

  @Override
  void forEach(Consumer<? super A> action);

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

  public Pair<L, L> spanBy(final Predicate<A> pred);
  public Pair<L, L> breakBy(final Predicate<A> pred);

  public Optional<L> stripPrefix(final L prefix);

  public List<L, ?> group();
  public List<L, ?> groupBy(final BiPredicate<A, A> eqPred);
  public List<L, ?> inits();
  public List<L, ?> tails();

  // Predicates
  public boolean isPrefixOf(final L list);
  public boolean isSuffixOf(final L list);
  public boolean isInfixOf(final L list);

  // Searching (by Equality)
  public boolean contains(final A a);
  public boolean notContains(final A a);

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

  public L nub();
  public L nubBy(final Comparator<? super A> cmp);
  public L delete(final A a);
  public L deleteBy(final A a, final BiPredicate<A, A> pred);
  public L listDiff(final L list);

  public L union(final L list);
  public L unionBy(final L list, final Comparator<? super A> cmp);
  public L intersect(final L list);
  public L intersectBy(final L list, final Comparator<? super A> cmp);

  public L sort();
  public L sortBy(final Comparator<? super A> cmp);
  public L insert(final A a);
  public L insertBy(final A a, final Comparator<? super A> cmp);

  L deleteFirstBy(final L list, final BiPredicate<A, A> pred);

  public A max();
  public A maxBy(final Comparator<? super A> cmp);
    
  public A min();
  public A minBy(final Comparator<? super A> cmp);

  public List<L, ?> subsequences();
  public List<L, ?> permutations();

  public Iterator<A> iterator();
  public Stream<A> stream();
  public ArrayList<A> toArray();
}
