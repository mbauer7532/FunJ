/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import java.util.ArrayList;

/**
 *
 * @author Neo
 * @param <V>
 * @param <Q>
 */
public interface PersistentQueue<V, Q extends PersistentQueue<V, Q>> {
  public boolean isEmpty();

  public V head();

  public int length();

  public Q tail();

  public Q nthTail(final int n);

  public ArrayList<V> take(final int cnt);

  public Q drop(final int cnt);

  public Q addLast(final V x);

  public boolean contains(final V v);

  public boolean notContains(final V v);
}
