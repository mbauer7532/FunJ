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
public interface PersistentMapFactory {
  public String getMapName();

  public <K extends Comparable<K>, V> PersistentMap<K, V, ?> empty();

  public <K extends Comparable<K>, V> PersistentMap<K, V, ?> singleton(final K key, final V value);

  public <K extends Comparable<K>, V> PersistentMap<K, V, ?> fromStrictlyIncreasingStream(final Stream<Pair<K, V>> stream);

  public <K extends Comparable<K>, V> PersistentMap<K, V, ?> fromStream(final Stream<Pair<K, V>> stream);

  public <K extends Comparable<K>, V> PersistentMap<K, V, ?> fromStrictlyDecreasingStream(final Stream<Pair<K, V>> stream);

  public <K extends Comparable<K>, V> PersistentMap<K, V, ?> fromStrictlyIncreasingArray(final ArrayList<Pair<K, V>> v);

  public <K extends Comparable<K>, V> PersistentMap<K, V, ?> fromStrictlyDecreasingArray(final ArrayList<Pair<K, V>> v);

  public <K extends Comparable<K>, V> PersistentMap<K, V, ?> fromArray(final ArrayList<Pair<K, V>> v);
}
