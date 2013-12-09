/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.Functionals.IntBiConsumer;
import Utils.Functionals.IntBiFunction;
import Utils.Functionals.IntBiPredicate;
import Utils.Functionals.IntTriFunction;
import java.util.ArrayList;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.StructureGraphic.v1.DSTreeNode;

/**
 *
 * @author Neo
 * @param <V>
 * @param <M>
 */
public interface PersistentMapInt<V, M extends PersistentMapInt<V, M>> extends DSTreeNode {
  /**
   *
   * @param key
   * @return
   */
  public boolean containsKey(final int key);

  /**
   *
   * @param value
   * @return
   */
  public boolean containsValue(final V value);

  /**
   *
   * @param key
   * @return
   */
  public Optional<V> get(final int key);

  /**
   *
   * @param key
   * @param def
   * @return
   */
  public V getOrElse(final int key, final V def);

  /**
   *
   * @param key
   * @param other
   * @return
   */
  public V getOrElseSupplier(final int key, final Supplier<V> other);

  /**
   *
   * @return
   */
  public boolean isEmpty();

  /**
   *
   * @return
   */
  public int size();

  
  /**
   *
   * @return
   */
  public int height();

  /**
   *
   * @param <W>
   * @param f
   * @return
   */
  public <W> PersistentMapInt<W, ?> map(final Function<V, W> f);

  /**
   *
   * @param <W>
   * @param f
   * @return
   */
  public <W> PersistentMapInt<W, ?> mapi(final IntBiFunction<V, W> f);

  /**
   *
   * @param f
   */
  public void app(final Consumer<V> f);

  /**
   *
   * @param f
   */
  public void appi(final IntBiConsumer<V> f);

  /**
   *
   * @param <W>
   * @param f
   * @param w
   * @return
   */
  public <W> W foldl(final BiFunction<V, W, W> f, final W w);

  /**
   *
   * @param <W>
   * @param f
   * @param w
   * @return
   */
  public <W> W foldr(final BiFunction<V, W, W> f, final W w);

  /**
   *
   * @param f
   * @return
   */
  public M filter(final Predicate<V> f);

  /**
   *
   * @param f
   * @return
   */
  public M filteri(final IntBiPredicate<V> f);

  /**
   *
   * @param f
   * @return
   */
  public Pair<M, M> partition(final Predicate<V> f);

  /**
   *
   * @param f
   * @return
   */
  public Pair<M, M> partitioni(final IntBiPredicate<V> f);

  /**
   *
   * @param <W>
   * @param f
   * @return
   */
  public <W> PersistentMapInt<W, ?> mapPartial(final Function<V, Optional<W>> f);

  /**
   *
   * @param <W>
   * @param f
   * @return
   */
  public <W> PersistentMapInt<W, ?> mapPartiali(final IntBiFunction<V, Optional<W>> f);

  /**
   *
   * @param key
   * @param value
   * @return
   */
  public M insert(final int key, final V value);

  /**
   *
   * @param f
   * @param key
   * @param value
   * @return
   */
  public M insert(final BiFunction<V, V, V> f, final int key, final V value);

  /**
   *
   * @param key
   * @return
   */
  public M remove(final int key);

  /**
   *
   * @return
   */
  public int[] keys();

  /**
   *
   * @return
   */
  public ArrayList<PersistentMapIntEntry<V>> keyValuePairs();

  /**
   *
   * @param f
   * @param t
   * @return
   */
  public M merge(final BiFunction<V, V, V> f, final PersistentMapInt<V, M> t);

  /**
   *
   * @param key
   * @return
   */
  public OptionalInt lowerKey(final int key);

  /**
   *
   * @param key
   * @return
   */
  public Optional<PersistentMapIntEntry<V>> lowerPair(final int key);

  /**
   *
   * @param key
   * @return
   */
  public OptionalInt higherKey(final int key);

  /**
   *
   * @param key
   * @return
   */
  public Optional<PersistentMapIntEntry<V>> higherPair(final int key);

  /**
   *
   * @return
   */
  public OptionalInt minKey();

  /**
   *
   * @return
   */
  public OptionalInt maxKey();

  /**
   *
   * @param <W>
   * @param f
   * @param w
   * @return
   */
  public <W> W foldli(final IntTriFunction<V, W, W> f, final W w);

  /**
   *
   * @param <W>
   * @param f
   * @param w
   * @return
   */
  public <W> W foldri(final IntTriFunction<V, W, W> f, final W w);

  /**
   *
   * @return
   */
  public Optional<PersistentMapIntEntry<V>> minElementPair();

  /**
   *
   * @return
   */
  public Optional<PersistentMapIntEntry<V>> maxElementPair();

  /**
   *
   * @param m
   * @return
   */
  @Override
  public boolean equals(final Object m);

  // Each map has to do it's own verification.  Used in testing.
  /**
   *
   * @return
   */
  public Pair<Boolean, String> verifyMapProperties();
}
