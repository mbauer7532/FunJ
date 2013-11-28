/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.Functionals.TriFunction;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.StructureGraphic.v1.DSTreeNode;

/**
 *
 * @author Neo
 * @param <K>
 * @param <V>
 * @param <M>
 */
public interface PersistentMap<K extends Comparable<K>, V, M extends PersistentMap<K, V, M>>
         extends DSTreeNode {

  /**
   *
   * @param key
   * @return
   */
  public boolean containsKey(final K key);

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
  public Optional<V> get(final K key);

  /**
   *
   * @param key
   * @param def
   * @return
   */
  public V getOrElse(final K key, final V def);

  /**
   *
   * @param key
   * @param other
   * @return
   */
  public V getOrElseSupplier(final K key, final Supplier<V> other);

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
   * @param <W>
   * @param f
   * @return
   */
  public <W> PersistentMap<K, W, ?> map(final Function<V, W> f);

  /**
   *
   * @param <W>
   * @param f
   * @return
   */
  public <W> PersistentMap<K, W, ?> mapi(final BiFunction<K, V, W> f);

  /**
   *
   * @param f
   */
  public void app(final Consumer<V> f);

  /**
   *
   * @param f
   */
  public void appi(final BiConsumer<K, V> f);

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
  public M filteri(final BiPredicate<K, V> f);

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
  public Pair<M, M> partitioni(final BiPredicate<K, V> f);

  /**
   *
   * @param <W>
   * @param f
   * @return
   */
  public <W> PersistentMap<K, W, ?> mapPartial(final Function<V, Optional<W>> f);

  /**
   *
   * @param <W>
   * @param f
   * @return
   */
  public <W> PersistentMap<K, W, ?> mapPartiali(final BiFunction<K, V, Optional<W>> f);

  /**
   *
   * @param key
   * @param value
   * @return
   */
  public M insert(final K key, final V value);

  /**
   *
   * @param f
   * @param key
   * @param value
   * @return
   */
  public M insert(final BiFunction<V, V, V> f, final K key, final V value);

  /**
   *
   * @param key
   * @return
   */
  public M remove(final K key);

  /**
   *
   * @return
   */
  public ArrayList<K> keys();

  /**
   *
   * @return
   */
  public ArrayList<Pair<K, V>> keyValuePairs();

  /**
   *
   * @param f
   * @param t
   * @return
   */
  public M merge(final BiFunction<V, V, V> f, final M t);

  /**
   *
   * @param key
   * @return
   */
  public Optional<K> lowerKey(final K key);

  /**
   *
   * @param key
   * @return
   */
  public Optional<Pair<K, V>> lowerPair(final K key);

  /**
   *
   * @param key
   * @return
   */
  public Optional<K> higherKey(final K key);

  /**
   *
   * @param key
   * @return
   */
  public Optional<Pair<K, V>> higherPair(final K key);

  /**
   *
   * @return
   */
  public Optional<K> minKey();

  /**
   *
   * @return
   */
  public Optional<K> maxKey();

  /**
   *
   * @param <W>
   * @param f
   * @param w
   * @return
   */
  public <W> W foldli(final TriFunction<K, V, W, W> f, final W w);

  /**
   *
   * @param <W>
   * @param f
   * @param w
   * @return
   */
  public <W> W foldri(final TriFunction<K, V, W, W> f, final W w);

  /**
   *
   * @return
   */
  public Optional<Pair<K, V>> minElementPair();

  /**
   *
   * @return
   */
  public Optional<Pair<K, V>> maxElementPair();

  /**
   *
   * @return
   */
  public int height();
  
  // Each map has to do it's own verification.  Used in testing.
  /**
   *
   * @return
   */
    public Pair<Boolean, String> verifyMapProperties();
}
