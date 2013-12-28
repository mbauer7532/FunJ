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

  @Override
  public String toString() {
    return String.format("Ref(%s)", r.toString());
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Ref) {
      @SuppressWarnings("unchecked")
      final Ref<A> ref = (Ref<A>) obj;
      return r.equals(ref.r);
    }
    else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return 47 + r.hashCode();
  }
}
