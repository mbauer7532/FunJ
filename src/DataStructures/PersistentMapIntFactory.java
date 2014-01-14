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
 * @param <V>
 * @param <M>
 */
public interface PersistentMapIntFactory<V, M extends PersistentMapInt<V, M>> {
  public String getMapName();

  public M empty();

  public M singleton(final int key, final V value);

  public M fromStream(final Stream<PersistentMapIntEntry<V>> stream);

  public M fromStrictlyIncreasingStream(final Stream<PersistentMapIntEntry<V>> stream);

  public M fromStrictlyDecreasingStream(final Stream<PersistentMapIntEntry<V>> stream);

  public M fromArray(final ArrayList<PersistentMapIntEntry<V>> v);

  public M fromStrictlyIncreasingArray(final ArrayList<PersistentMapIntEntry<V>> v);

  public M fromStrictlyDecreasingArray(final ArrayList<PersistentMapIntEntry<V>> v);
}
