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

    @Override
    public boolean equals(final Object obj) {
      if (obj instanceof Pair) {
        @SuppressWarnings("unchecked")
        final Pair<T1, T2> p = (Pair<T1, T2>) obj;
        return mx1.equals(p.mx1)
                && mx2.equals(p.mx2);
      }
      else {
        return false;
      }
    }

    @Override
    public int hashCode() {
      return mx1.hashCode() + mx2.hashCode();
    }

    public T1 getFirst()  { return mx1; }
    public T2 getSecond() { return mx2; }
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

    public final T1 mx1;
    public final T2 mx2;
    public final T3 mx3;

    @Override
    public String toString() {
      return String.format("(%s, %s, %s)",
                            mx1.toString(),
                            mx2.toString(),
                            mx3.toString());
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj instanceof Triple) {
        @SuppressWarnings("unchecked")
        final Triple<T1, T2, T3> p = (Triple<T1, T2, T3>) obj;
        return mx1.equals(p.mx1)
                && mx2.equals(p.mx2)
                && mx3.equals(p.mx3);
      }
      else {
        return false;
      }
    }
    
    @Override
    public int hashCode() {
      return mx1.hashCode()
              + mx2.hashCode()
              + mx3.hashCode();
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

    public final T1 mx1;
    public final T2 mx2;
    public final T3 mx3;
    public final T4 mx4;

    @Override
    public String toString() {
      return String.format("(%s, %s, %s, %s)",
                            mx1.toString(),
                            mx2.toString(),
                            mx3.toString(),
                            mx4.toString());
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj instanceof Triple) {
        @SuppressWarnings("unchecked")
        final Tuple4<T1, T2, T3, T4> p = (Tuple4<T1, T2, T3, T4>) obj;
        return mx1.equals(p.mx1)
                && mx2.equals(p.mx2)
                && mx3.equals(p.mx3)
                && mx4.equals(p.mx4);
      }
      else {
        return false;
      }
    }
    
    @Override
    public int hashCode() {
      return mx1.hashCode()
              + mx2.hashCode()
              + mx3.hashCode()
              + mx4.hashCode();
    }
  }

  public static final class AssocPair<K extends Comparable<K>, V>
                              extends PersistentMapEntry<K, V> {
    public static <T1 extends Comparable<T1>, T2> AssocPair<T1, T2> create(final T1 x1, final T2 x2) {
      return new AssocPair<>(x1, x2);
    }

    private AssocPair(final K x1, final V x2) {
      mKey   = x1;
      mValue = x2;
    }

    private final K mKey;
    private final V mValue;

    @Override
    public String toString() {
      return String.format("(%s, %s)",
                           mKey.toString(),
                           mValue.toString());
    }

    @Override
    public K getKey() {
      return mKey;
    }

    @Override
    public V getValue() {
      return mValue;
    }
  }

  public static final class AssocIntPair<V> extends PersistentMapIntEntry<V> {
    public static <T> AssocIntPair<T> create(final int i, final T x) {
      return new AssocIntPair<>(i, x);
    }

    private AssocIntPair(final int i, final V x) {
      mKey   = i;
      mValue = x;
    }

    private final int mKey;
    private final V mValue;

    @Override
    public String toString() {
      return String.format("(%d, %s)", mKey, mValue.toString());
    }

    @Override
    public int getKey() {
      return mKey;
    }

    @Override
    public V getValue() {
      return mValue;
    }
  }
}
