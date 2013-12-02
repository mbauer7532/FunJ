/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 *
 * @author Neo
 */
public interface PersistentMapFactory<K extends Comparable<K>, V, M extends PersistentMap<K, V, M>> {
  public String getMapName();

  public PersistentMap<K, V, M> empty();

  public PersistentMap<K, V, M> singleton(final K key, final V value);

  public PersistentMap<K, V, M> fromStream(final Stream<Pair<K, V>> stream);

  public PersistentMap<K, V, M> fromStrictlyIncreasingStream(final Stream<Pair<K, V>> stream);

  public PersistentMap<K, V, M> fromStrictlyDecreasingStream(final Stream<Pair<K, V>> stream);

  public PersistentMap<K, V, M> fromArray(final ArrayList<Pair<K, V>> v);

  public PersistentMap<K, V, M> fromStrictlyIncreasingArray(final ArrayList<Pair<K, V>> v);

  public PersistentMap<K, V, M> fromStrictlyDecreasingArray(final ArrayList<Pair<K, V>> v);
}
