/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import java.util.Iterator;

/**
 *
 * @author Neo
 * @param <V>
 */
public interface PersistentHeap<V extends Comparable<V>> extends Iterable<V> {
  public boolean isEmpty();

  public V findMin();

  public LeftistHeap<V> insert(final V val);

  public LeftistHeap<V> deleteMin();

  public int size();

  @Override
  public Iterator<V> iterator();
}
