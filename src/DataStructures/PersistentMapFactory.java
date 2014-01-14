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

  public M empty();

  public M singleton(final K key, final V value);

  public M fromStream(final Stream<PersistentMapEntry<K, V>> stream);

  public M fromStrictlyIncreasingStream(final Stream<PersistentMapEntry<K, V>> stream);

  public M fromStrictlyDecreasingStream(final Stream<PersistentMapEntry<K, V>> stream);

  public M fromArray(final ArrayList<PersistentMapEntry<K, V>> v);

  public M fromStrictlyIncreasingArray(final ArrayList<PersistentMapEntry<K, V>> v);

  public M fromStrictlyDecreasingArray(final ArrayList<PersistentMapEntry<K, V>> v);
}
