/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 *
 * @author Neo
 * @param <K>
 * @param <V>
 * @param <M>
 */
public interface PersistentMapFactory<K extends Comparable<K>, V, M extends PersistentMap<K, V, M>> {
  public String getMapName();

  public PersistentMap<K, V, M> empty();

  public PersistentMap<K, V, M> singleton(final K key, final V value);

  public PersistentMap<K, V, M> fromStream(final Stream<PersistentMapEntry<K, V>> stream);

  public PersistentMap<K, V, M> fromStrictlyIncreasingStream(final Stream<PersistentMapEntry<K, V>> stream);

  public PersistentMap<K, V, M> fromStrictlyDecreasingStream(final Stream<PersistentMapEntry<K, V>> stream);

  public PersistentMap<K, V, M> fromArray(final ArrayList<PersistentMapEntry<K, V>> v);

  public PersistentMap<K, V, M> fromStrictlyIncreasingArray(final ArrayList<PersistentMapEntry<K, V>> v);

  public PersistentMap<K, V, M> fromStrictlyDecreasingArray(final ArrayList<PersistentMapEntry<K, V>> v);
}
