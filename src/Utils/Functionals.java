/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 *
 * @author Neo
 */
public class Functionals {

  /**
   *
   * @param <T>
   * @param <U>
   * @param <V>
   * @param <R>
   */
  @FunctionalInterface
  public interface TriFunction<T, U, V, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @param v the third function argument
     * @return the function result
     */
    R apply(final T t, final U u, final V v);

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <W> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default <W> TriFunction<T, U, V, W> andThen(final Function<? super R, ? extends W> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v) -> after.apply(apply(t, u, v));
    }
  }

  /**
   *
   * @param <U>
   * @param <V>
   * @param <R>
   */
  @FunctionalInterface
  public interface IntTriFunction<U, V, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @param v the third function argument
     * @return the function result
     */
    R apply(final int t, final U u, final V v);

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <W> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default <W> IntTriFunction<U, V, W> andThen(final Function<? super R, ? extends W> after) {
        Objects.requireNonNull(after);
        return (int t, U u, V v) -> after.apply(apply(t, u, v));
    }
  }

  /**
   *
   * @param <V>
   * @param <R>
   */
  @FunctionalInterface
  public interface IntBiFunction<V, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param i
     * @param v the third function argument
     * @return the function result
     */
    R apply(final int i, final V v);

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default <W> IntBiFunction<V, W> andThen(final Function<? super R, ? extends W> after) {
        Objects.requireNonNull(after);
        return (final int i, final V v) -> after.apply(apply(i, v));
    }
  }

  /**
   *
   * @param <V>
   * @param <R>
   */
  @FunctionalInterface
  public interface Int2BiFunction<V, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param i
     * @param v the third function argument
     * @return the function result
     */
    R apply(final V v, final int i);

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <W>
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default <W> Int2BiFunction<V, W> andThen(final Function<? super R, ? extends W> after) {
        Objects.requireNonNull(after);
        return (final V v, final int i) -> after.apply(apply(v, i));
    }
  }
  
  /**
   *
   * @param <V>
   * @param <R>
   */
  @FunctionalInterface
  public interface Long2BiFunction<V, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param i
     * @param v the third function argument
     * @return the function result
     */
    R apply(final V v, final long i);

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <W>
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default <W> Long2BiFunction<V, W> andThen(final Function<? super R, ? extends W> after) {
        Objects.requireNonNull(after);
        return (final V v, final long i) -> after.apply(apply(v, i));
    }
  }

  /**
   *
   * @param <V>
   * @param <R>
   */
  @FunctionalInterface
  public interface Double2BiFunction<V, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param i
     * @param v the third function argument
     * @return the function result
     */
    R apply(final V v, final double i);

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <W>
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default <W> Double2BiFunction<V, W> andThen(final Function<? super R, ? extends W> after) {
        Objects.requireNonNull(after);
        return (final V v, final double i) -> after.apply(apply(v, i));
    }
  }

  /**
   *
   * @param <V>
   */
  @FunctionalInterface
  public interface IntBiPredicate<V> {
    /**
     * Applies this function to the given arguments.
     *
     * @param i
     * @param v the third function argument
     * @return the function result
     */
    boolean test(final int i, final V v);

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default IntBiPredicate<V> andThen(final IntBiPredicate<? super V> after) {
        Objects.requireNonNull(after);
        return (final int i, final V v) -> test(i, v) && after.test(i, v);
    }
  }

  /**
   *
   * @param <V>
   */
  @FunctionalInterface
  public interface IntBiConsumer<V> {
    /**
     * Applies this function to the given arguments.
     *
     * @param i
     * @param v the third function argument
     * @return the function result
     */
    void accept(final int i, final V v);

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default IntBiConsumer<V> andThen(final IntBiConsumer<? super V> after) {
        Objects.requireNonNull(after);
        return (final int i, final V v) -> { accept(i, v); after.accept(i, v); };
    }
  }

  /**
   *
   */
  @FunctionalInterface
  public interface IntComparator {
    int compare(final int i0, final int i1);
  }

  public static <T, U> U mapOptOrElse(final Optional<T> opt, final Function<T, U> f, final Supplier<U> g) {
    if (opt.isPresent()) {
      return f.apply(opt.get());
    }
    else {
      return g.get();
    }
  }

  /**
   *
   * @param <A>
   * @param a
   * @param b
   * @return
   */
  public static <A> A functionShouldNotBeCalled(final A a, final A b) {
    throw new AssertionError("Should never get here.  The stream was sequential.");
  }

  /**
   *
   * @param <A>
   * @param x
   * @param y
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <A> int comparator(final A x, final A y) {
    return ((Comparable<? super A>) x).compareTo(y);
  }

  /**
   *
   * @param <A>
   * @param x
   * @param y
   * @return
   */
  public static <A> A maxSelector(final A x, final A y) {
    return comparator(x, y) >= 0 ? x : y;
  }

  /**
   *
   * @param <A>
   * @param x
   * @param y
   * @return
   */
  public static <A> A minSelector(final A x, final A y) {
    return comparator(x, y) >= 0 ? y : x;
  }

  private static <T, S extends BaseStream<T, S>> void checkSequentialStream(final BaseStream<T, S> s) {
    if (s.isParallel()) {
      throw new AssertionError("Reduce() can only be called on sequential streams.");
    }
  }

  /**
   *
   * @param <U>
   * @param s
   * @param identity
   * @param accumulator
   * @return
   */
  public static <U> U reduce(final IntStream s, final U identity, final Int2BiFunction<U, U> accumulator) {
    checkSequentialStream(s);
    final Ref<U> u = new Ref<>(identity);
    s.forEach((int x) -> { u.r = accumulator.apply(u.r, x); });
    return u.r;
  }

  /**
   *
   * @param <U>
   * @param s
   * @param identity
   * @param accumulator
   * @return
   */
  public static <U> U reduce(final LongStream s, final U identity, final Long2BiFunction<U, U> accumulator) {
    checkSequentialStream(s);
    final Ref<U> u = new Ref<>(identity);
    s.forEach((long x) -> { u.r = accumulator.apply(u.r, x); });
    return u.r;
  }

  /**
   *
   * @param <U>
   * @param s
   * @param identity
   * @param accumulator
   * @return
   */
  public static <U> U reduce(final DoubleStream s, final U identity, final Double2BiFunction<U, U> accumulator) {
    checkSequentialStream(s);
    final Ref<U> u = new Ref<>(identity);
    s.forEach((double x) -> { u.r = accumulator.apply(u.r, x); });
    return u.r;
  }
}
