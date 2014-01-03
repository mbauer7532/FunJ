/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import Utils.Ref;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;
import org.StructureGraphic.v1.DSTreeNode;

/**
 *
 * @author Neo
 * @param <V>
 */
public interface PersistentHeap<V extends Comparable<V>, H extends PersistentHeap<V, H>> extends Iterable<V>, DSTreeNode {
  public boolean isEmpty();

  public V findMin();

  public H insert(final V val);

  public H deleteMin();

  public H deleteMin(final Ref<V> v);

  public H merge(final H heap);

  public int size();

  @Override
  public Iterator<V> iterator();

  public Stream<V> stream();

  public ArrayList<V> toAscArrayList();

  public ArrayList<V> toDescArrayList();
}
