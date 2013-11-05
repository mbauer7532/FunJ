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
    public static <T1, T2> Pair<T1, T2> create(final T1 x1, final T2 x2) {
      return new Pair<>(x1, x2);
    }

    private Pair(final T1 x1, final T2 x2) {
      mx1 = x1;
      mx2 = x2;
    }

    public T1 mx1;
    public T2 mx2;

    @Override
    public String toString() {
      return String.format("(%s, %s)",
                            mx1.toString(),
                            mx2.toString());
    }
  }

  public static final class Triple<T1, T2, T3> {
    public static <T1, T2, T3> Triple<T1, T2, T3> create(final T1 x1, final T2 x2, final T3 x3) {
      return new Triple<>(x1, x2, x3);
    }

    private Triple(final T1 x1, final T2 x2, final T3 x3) {
      mx1 = x1;
      mx2 = x2;
      mx3 = x3;
    }

    public T1 mx1;
    public T2 mx2;
    public T3 mx3;

    @Override
    public String toString() {
      return String.format("(%s, %s, %s)",
                            mx1.toString(),
                            mx2.toString(),
                            mx3.toString());
    }
  }
  
  public static final class Tuple4<T1, T2, T3, T4> {
    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> create(final T1 x1, final T2 x2, final T3 x3, final T4 x4) {
      return new Tuple4<>(x1, x2, x3, x4);
    }

    private Tuple4(final T1 x1, final T2 x2, final T3 x3, final T4 x4) {
      mx1 = x1;
      mx2 = x2;
      mx3 = x3;
      mx4 = x4;
    }

    public T1 mx1;
    public T2 mx2;
    public T3 mx3;
    public T4 mx4;

    @Override
    public String toString() {
      return String.format("(%s, %s, %s, %s)",
                            mx1.toString(),
                            mx2.toString(),
                            mx3.toString(),
                            mx4.toString());
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
