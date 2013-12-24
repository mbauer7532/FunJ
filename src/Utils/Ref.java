/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

/**
 *
 * @author Neo
 * @param <A>
 */
public class Ref<A> {
  public Ref() { this(null); }
  public Ref(final A a) { r = a; }

  public A r;
}
