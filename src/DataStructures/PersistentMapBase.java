/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author Neo
 */
public abstract class PersistentMapBase<K extends Comparable<K>, V, M extends PersistentMapBase<K, V, M>>
                      implements PersistentMap<K, V, M> {
  @Override
    public final boolean containsKey(final K key) {
      return get(key).isPresent();
    }

    @Override
    public final V getOrElse(final K key, final V def) {
      return get(key).orElse(def);
    }

    @Override
    public final V getOrElseSupplier(final K key, final Supplier<V> other) {
      return get(key).orElseGet(other);
    }

    @Override
    public final void app(final Consumer<V> f) {
      appi((k, v) -> f.accept(v));

      return;
    }

    @Override
    public final Optional<K> minKey() {
      return minElementPair().map(Pair::getFirst);
    }

    @Override
    public final Optional<K> maxKey() {
      return maxElementPair().map(Pair::getFirst);
    }

    @Override
    public final Optional<K> higherKey(final K key) {
      return higherPair(key).map(Pair::getFirst);
    }

    @Override
    public final Optional<K> lowerKey(final K key) {
      return lowerPair(key).map(Pair::getFirst);
    }

    @Override
    public final <W> W foldl(final BiFunction<V, W, W> f, final W w) {
      return foldli((k, v, z) -> f.apply(v, z), w);
    }

    @Override
    public final <W> W foldr(final BiFunction<V, W, W> f, final W w) {
      return foldri((k, v, z) -> f.apply(v, z), w);
    }

    @Override
    public final ArrayList<K> keys() {
      final ArrayList<K> ks = new ArrayList<>();
      appi((k, v) -> { ks.add(k); });
      return ks;
    }

    @Override
    public final ArrayList<Pair<K, V>> keyValuePairs() {
      final ArrayList<Pair<K, V>> kvs = new ArrayList<>();
      appi((k, v) -> { kvs.add(Pair.create(k, v)); });

      return kvs;
    }
}
