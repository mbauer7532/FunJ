/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import java.util.Objects;
import java.util.function.Supplier;

/**
 *
 * @author Neo
 * @param <A>
 */
public class Susp<A> {
  private A ma;
  private Supplier<A> mSup;

  private Susp(final Supplier<A> sup) {
    mSup = Objects.requireNonNull(sup);
  }

  public static <A> Susp<A> create(final Supplier<A> sup) {
    return new Susp<>(sup);
  }

  public A eval() {
    if (mSup == null) {
      return ma;
    }
    else {
      ma = mSup.get();
      mSup = null;
      return ma;
    }
  }
}
