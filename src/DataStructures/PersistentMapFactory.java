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
  public M empty();

  public M singleton(final K key, final V value);

  public M fromStrictlyIncreasingStream(final Stream<Pair<K, V>> stream);

  public M fromStream(final Stream<Pair<K, V>> stream);

  public M fromStrictlyDecreasingStream(final Stream<Pair<K, V>> stream);

  public M fromStrictlyIncreasingArray(final ArrayList<Pair<K, V>> v);

  public M fromStrictlyDecreasingArray(final ArrayList<Pair<K, V>> v);

  public M fromArray(final ArrayList<Pair<K, V>> v);
}
