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
 */
public interface PersistentQueue<V, Q extends PersistentQueue<V, Q>> {
  boolean isEmpty();

  V head();

  int length();

  Q tail();

  ArrayList<V> take(final int cnt);

  Q drop(final int cnt);

  Q addLast(final V x);

  boolean contains(final V v);

  boolean notContains(final V v);
}
