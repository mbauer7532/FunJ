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

/**
 *
 * @author Neo
 */
public interface PersistentMap<K extends Comparable<K>, V, M extends PersistentMap<K, V, M>> {
  public boolean containsKey(final K key);

  public V getOrElse(final K key, final V def);

  public V getOrElseSupplier(final K key, final Supplier<V> other);

  public <W> PersistentMap<K, W, ?> map(final Function<V, W> f);

  public void app(final Consumer<V> f);

  public <W> W foldl(final BiFunction<V, W, W> f, final W w);

  public <W> W foldr(final BiFunction<V, W, W> f, final W w);

  public M filter(final Predicate<V> f);

  public M filteri(final BiPredicate<K, V> f);

  public Pair<M, M> partition(final Predicate<V> f);

  public Pair<M, M> partitioni(final BiPredicate<K, V> f);

  public <W> PersistentMap<K, W, ?> mapPartial(final Function<V, Optional<W>> f);

  public <W> PersistentMap<K, W, ?> mapPartiali(final BiFunction<K, V, Optional<W>> f);

  public M insert(final BiFunction<V, V, V> f, final K key, final V value);

  public M insert(final K key, final V value);

  public M remove(final K key);

  public ArrayList<K> keys();

  public ArrayList<Pair<K, V>> keyValuePairs();

  public M merge(final BiFunction<V, V, V> f, final M t);

  public Optional<K> lowerKey(final K key);

  public Optional<Pair<K, V>> lowerPair(final K key);

  public Optional<K> higherKey(final K key);

  public Optional<Pair<K, V>> higherPair(final K key);

  public Optional<K> minKey();

  public Optional<K> maxKey();

  public boolean isEmpty();

  public Optional<V> get(final K key);

  public boolean containsValue(final V value);

  public int size();

  public int height();

  public void appi(final BiConsumer<K, V> f);

  public <W> PersistentMap<K, W, ?> mapi(final BiFunction<K, V, W> f);

  public <W> W foldli(final TriFunction<K, V, W, W> f, final W w);

  public <W> W foldri(final TriFunction<K, V, W, W> f, final W w);

  public Optional<Pair<K, V>> minElementPair();

  public Optional<Pair<K, V>> maxElementPair();
}
