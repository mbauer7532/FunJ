/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

/**
 *
 * @author Neo
 */
public final class TuplesModule {
  public static final class Pair<T1, T2> {
    public T1 mFirst;
    public T2 mSecond;

    @Override
    public String toString() {
      return String.format("(%s, %s)",
                           mFirst.toString(),
                           mSecond.toString());
    }
  }

  public static final class Triple<T1, T2, T3> {
    public T1 mFirst;
    public T2 mSecond;
    public T3 mThird;

    @Override
    public String toString() {
      return String.format("(%s, %s, %s)",
                           mFirst.toString(),
                           mSecond.toString(),
                           mThird.toString());
    }
  }

  public static final class AssocPair<T1 extends Comparable<T1>, T2> {
    public T1 mKey;
    public T2 mValue;

    @Override
    public String toString() {
      return String.format("(%s, %s)",
                           mKey.toString(),
                           mValue.toString());
    }
  }
}
