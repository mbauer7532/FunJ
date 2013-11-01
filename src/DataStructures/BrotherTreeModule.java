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
public final class BrotherTreeModule {
  public abstract static class Node<K extends Comparable<K>, V> {
    public Node<K, V> insert(final K a, final V v) {
      return root(ins(a, v));
    }

    protected abstract Node<K, V> ins(final K a, final V v);
  }
  
  private static class N0<K extends Comparable<K>, V> extends Node<K, V> {
    @SuppressWarnings("unchecked")
    static final <K extends Comparable<K>, V> N0<K, V> cN0() { return (N0<K, V>) sN0; }

    static final N0<? extends Comparable<?>, ?> sN0 = new N0<>();

    @Override
    public Node<K, V> ins(final K a, final V v) {
      return cL2(a, v);
    }
  }

  private static class N1<K extends Comparable<K>, V> extends Node<K, V> {
    private static <K extends Comparable<K>, V> N1<K, V> cN1(final Node<K, V> t) {
      return new N1<>(t);
    }

    private N1(final Node<K, V> t) {
      mt = t;
    }

    private final Node<K, V> mt;

    @Override
    public Node<K, V> ins(final K a, final V v) {
      return n1(mt.insert(a, v));
    }
  }

  private static class N2<K extends Comparable<K>, V> extends Node<K, V> {
    private static <K extends Comparable<K>, V> N2<K, V> cN2(final Node<K, V> t1, final K a1, final V v1, final Node<K, V> t2) {
      return new N2<>(t1, t2, a1, v1);
    }

    private N2(final Node<K, V> t1, final Node<K, V> t2, final K a1, final V v1) {
      mt1 = t1;
      mt2 = t2;
      ma1 = a1;
      mv1 = v1;
    }

    private final Node<K, V> mt1;
    private final Node<K, V> mt2;
    private final K ma1;
    private final V mv1;

    @Override
    public Node<K, V> ins(final K a, final V v) {
      final int res = a.compareTo(ma1);
      if (res < 0) {
        return n2_ins(mt1.insert(a, v), ma1, mv1, mt2);
      }
      else if (res > 0) {
        return n2_ins(mt1, ma1, mv1, mt2.insert(a, v));
      }
      else {
        return cN2(mt1, a, v, mt2);
      }
    }
  }

  private static class N3<K extends Comparable<K>, V> extends Node<K, V> {
    private static <K extends Comparable<K>, V> N3<K, V> cN3(
            final Node<K, V> t1, final Node<K, V> t2, final Node<K, V> t3,
            final K a1, final V v1, final K a2, final V v2) {
      return new N3<>(t1, t2, t3, a1, v1, a2, v2);
    }

    private N3(final Node<K, V> t1, final Node<K, V> t2, final Node<K, V> t3,
               final K a1, final V v1, final K a2, final V v2) {
      mt1 = t1;
      mt2 = t2;
      mt3 = t3;
      ma1 = a1;
      mv1 = v1;
      ma2 = a2;
      mv2 = v2;
    }

    private final Node<K, V> mt1;
    private final Node<K, V> mt2;
    private final Node<K, V> mt3;
    private final K ma1;
    private final V mv1;
    private final K ma2;
    private final V mv2;

    @Override
    public Node<K, V> ins(final K a, final V v) {
      throw new AssertionError("Error in tree structure.  N3 notes encountered where it shouldn't be.");
    }
  }

  private static class L2<K extends Comparable<K>, V> extends Node<K, V> {
    private static <K extends Comparable<K>, V> L2<K, V> cL2(final K a1, final V v1) {
      return new L2<>(a1, v1);
    }

    private L2(final K a1, final V v1) {
      ma1 = a1;
      mv1 = v1;
    }

    private final K ma1;
    private final V mv1;

    @Override
    public Node<K, V> ins(final K a, final V v) {
      throw new AssertionError("Error in tree structure.  N3 notes encountered where it shouldn't be.");
    }
  }

  private static <K extends Comparable<K>, V> N0<K, V> cN0() { return N0.cN0(); }
  private static <K extends Comparable<K>, V> N1<K, V> cN1(final Node<K, V> t) { return N1.cN1(t); }
  private static <K extends Comparable<K>, V> N2<K, V> cN2(final Node<K, V> t1, final K a1, final V v1, final Node<K, V> t2) { return N2.cN2(t1, a1, v1, t2); }
  private static <K extends Comparable<K>, V> N3<K, V> cN3(final Node<K, V> t1, final K a1, final V v1, final Node<K, V> t2, final K a2, final V v2, final Node<K, V> t3) { return N3.cN3(t1, t2, t3, a1, v1, a2, v2); }
  private static <K extends Comparable<K>, V> L2<K, V> cL2(final K a, final V v) { return L2.cL2(a, v); }

  private static <K extends Comparable<K>, V> Node<K, V> root(final Node<K,V> t) {
    final Node<K, V> n1Tree = n1(t);

    if (n1Tree instanceof N1) {
      final N1<K, V> n1t = (N1<K, V>) t;
      return n1t.mt;
    }
    else {
      return n1Tree;
    }
  }

  private static <K extends Comparable<K>, V> Node<K, V> n1(final Node<K,V> t) {
    final Node<K, V> cn0 = cN0();

    if (t instanceof L2) {
      final L2<K, V> l2t = (L2<K, V>) t;
      final K a1 = l2t.ma1;
      final V v1 = l2t.mv1;
      
      return cN2(cn0, a1, v1, cn0);
    }
    else if (t instanceof N3) {
      final N3<K, V> n3t = (N3<K, V>) t;
      final Node<K, V> t1 = n3t.mt1, t2 = n3t.mt2, t3 = n3t.mt3;
      final K a1 = n3t.ma1, a2 = n3t.ma2;
      final V v1 = n3t.mv1, v2 = n3t.mv2;

      return cN2(cN2(t1, a1, v1, t2), a2, v2, cN1(t3));
    }
    else {
      return cN1(t);
    }
  }

  private static <K extends Comparable<K>, V> Node<K, V> n2_ins(final Node<K,V> left, final K a, final V v, final Node<K, V> right) {
    final Node<K, V> cn0 = cN0();

    if (left instanceof L2) {
      final L2<K, V> lt = (L2<K, V>) left;
      final Node<K, V> t1 = right;
      final K a1 = lt.ma1, a2 = a;
      final V v1 = lt.mv1, v2 = v;

      return cN3(cn0, a1, v1, cn0, a2, v2, t1);
    }

    if (left instanceof N3) {
      if (right instanceof N1) {
         final N3<K, V> n3t = (N3<K, V>) left;
         final Node<K, V> t1 = n3t.mt1, t2 = n3t.mt2, t3 = n3t.mt3;
         final K a1 = n3t.ma1, a2 = n3t.ma2, a3 = a;
         final V v1 = n3t.mv1, v2 = n3t.mv2, v3 = v;

         final N1<K, V> n1t = (N1<K, V>) right;
         final Node<K, V> t4 = n1t.mt;

         return cN2(cN2(t1, a1, v1, t2), a2, v2, cN2(t3, a3, v3, t4));
      }
      else if (right instanceof N2)
      {
        final N3<K, V> n3t = (N3<K, V>) left;
        final Node<K, V> t1 = n3t.mt1, t2 = n3t.mt2, t3 = n3t.mt3;
        final K a1 = n3t.ma1, a2 = n3t.ma2, a3 = a;
        final V v1 = n3t.mv1, v2 = n3t.mv2, v3 = v;
        final Node<K, V> t4 = right;

        return cN3(cN2(t1, a1, v1, t2), a2, v2, cN1(t3), a3, v3, t4);
      }
    }

    if (right instanceof L2) {
      final L2<K, V> rt = (L2<K, V>) right;
      final Node<K, V> t1 = left;
      final K a1 = a, a2 = rt.ma1;
      final V v1 = v, v2 = rt.mv1;

      return cN3(t1, a1, v1, cn0, a2, v2, cn0);
    }

    if (right instanceof N3) {
      if (left instanceof N1) {
         final N3<K, V> n3t = (N3<K, V>) right;
         final Node<K, V> t2 = n3t.mt1, t3 = n3t.mt2, t4 = n3t.mt3;
         final K a1 = a, a2 = n3t.ma1, a3 = n3t.ma2;
         final V v1 = v, v2 = n3t.mv1, v3 = n3t.mv2;

         final N1<K, V> n1t = (N1<K, V>) left;
         final Node<K, V> t1 = n1t.mt;

         return cN2(cN2(t1, a1, v1, t2), a2, v2, cN2(t3, a3, v3, t4));
      }
      else if (left instanceof N2)
      {
        final N3<K, V> n3t = (N3<K, V>) right;
        final Node<K, V> t2 = n3t.mt1, t3 = n3t.mt2, t4 = n3t.mt3;
        final K a1 = a, a2 = n3t.ma1, a3 = n3t.ma2;
        final V v1 = v, v2 = n3t.mv1, v3 = n3t.mv2;
        final Node<K, V> t1 = left;

        return cN3(cN2(t1, a1, v1, t2), a2, v2, cN1(t3), a3, v3, t4);
      }
    }

    final Node<K, V> t1 = left, t2 = right;
    final K a1 = a;
    final V v1 = v;

    return cN2(t1, a1, v1, t2);
  }

  private static <K extends Comparable<K>, V> Node<K, V> n2_del(final Node<K,V> left, final K a, final V v, final Node<K, V> right) {
    final Node<K, V> cn0 = cN0();

    boolean leftIsN1;
    if ((leftIsN1 = (left instanceof N1)) && (right instanceof N1)) {
      final Node<K, V> t1 = ((N1<K, V>) left).mt;
      final Node<K, V> t2 = ((N1<K, V>) right).mt;

      return c1(t1, a, v, t2);
    }
    else if (leftIsN1 && (right instanceof N2)) {
      final N1<K, V> ln1 = (N1<K, V>) left;
      final N2<K, V> rn2 = (N2<K, V>) right;

      if (ln1.mt instanceof N1) {
        if (rn2.mt2 instanceof N2) {
          if (rn2.mt1 instanceof N1) {
            final Node<K, V> t1 = ((N1<K, V>) ln1.mt).mt;
            final Node<K, V> t2 = ((N1<K, V>) rn2.mt1).mt;
            final Node<K, V> t3 = rn2.mt2;
            final K a1 = a, a2 = rn2.ma1;
            final V v1 = v, v2 = rn2.mv1;

            return c2(t1, a1, v1, t2, a2, v2, t3);
          }
          else if (rn2.mt1 instanceof N2) {
            final Node<K, V> t1 = ln1.mt;
            final Node<K, V> t2 = rn2.mt1;
            final Node<K, V> t3 = rn2.mt2;
            final K a1 = a, a2 = rn2.ma1;
            final V v1 = v, v2 = rn2.mv1;
            
            return c4(t1, a1, v1, t2, a2, v2, t3);
          }
        }
        else if (rn2.mt2 instanceof N1) {
          if (rn2.mt1 instanceof N2) {
            final N2<K, V> z1 = (N2<K, V>) rn2.mt1;
            final N1<K, V> z2 = (N1<K, V>) rn2.mt2;

            final Node<K, V> t1 = ((N1<K, V>) ln1.mt).mt;
            final Node<K, V> t2 = z1.mt1;
            final Node<K, V> t3 = z1.mt2;
            final Node<K, V> t4 = z2.mt;
            final K a1 = a, a2 = z1.ma1, a3 = rn2.ma1;
            final V v1 = v, v2 = z1.mv1, v3 = rn2.mv1;
            
            return c3(t1, a1, v1, t2, a2, v2, t3, a3, v3, t4);
          }
        }
      }
    }
    else if ((left instanceof N2) && (right instanceof N1)) {
      final N2<K, V> ln2 = (N2<K, V>) left;
      final N1<K, V> rn1 = (N1<K, V>) right;

      if (rn1.mt instanceof N1) {
        if (ln2.mt2 instanceof N1) {
          if (ln2.mt1 instanceof N2) {
            final Node<K, V> t1 = ln2.mt1;
            final Node<K, V> t2 = ((N1<K, V>) ln2.mt2).mt;
            final Node<K, V> t3 = ((N1<K, V>) rn1.mt).mt;
            final K a1 = ln2.ma1, a2 = a;
            final V v1 = ln2.mv1, v2 = v;

            return c6(t1, a1, v1, t2, a2, v2, t3);
          }
        }
        else if (ln2.mt2 instanceof N2) {
          if (ln2.mt1 instanceof N1) {
            final N2<K, V> z = (N2<K, V>) ln2.mt2;
            final Node<K, V> t1 = ((N1<K, V>) ln2.mt1).mt;
            final Node<K, V> t2 = z.mt1;
            final Node<K, V> t3 = z.mt2;
            final Node<K, V> t4 = ((N1<K, V>) rn1.mt).mt;
            final K a1 = ln2.ma1, a2 = z.ma1, a3 = a;
            final V v1 = ln2.mv1, v2 = z.mv1, v3 = v;

            return c5(t1, a1, v1, t2, a2, v2, t3, a3, v3, t4);
          }
          else if (ln2.mt1 instanceof N2) {
            final Node<K, V> t1 = ln2.mt1;
            final Node<K, V> t2 = ln2.mt2;
            final Node<K, V> t3 = rn1.mt;
            final K a1 = ln2.ma1, a2 = a;
            final V v1 = ln2.mv1, v2 = v;
            
            return c7(t1, a1, v1, t2, a2, v2, t3);
          }
        }
      }
    }

    return c8(left, a, v, right);
  }

  // Case (1)
  private static <K extends Comparable<K>, V> Node<K, V> c1(final Node<K, V> t1, final K a, final V v, final Node<K, V> t2) {
    return cN1(cN2(t1, a, v, t2));
  }
  // Case (2)
  private static <K extends Comparable<K>, V> Node<K, V> c2(final Node<K, V> t1, final K a1, final V v1, final Node<K, V> t2, final K a2, final V v2, final Node<K, V> t3) {
    return cN1(cN2(cN2(t1, a1, v1, t2), a2, v2, t3));
  }
  // Case (3)
  private static <K extends Comparable<K>, V> Node<K, V> c3(final Node<K, V> t1, final K a1, final V v1, final Node<K, V> t2, final K a2, final V v2, final Node<K, V> t3, final K a3, final V v3, final Node<K, V> t4) {
    return cN1(cN2(cN2(t1, a1, v1, t2), a2, v2, cN2(t3, a3, v3, t4)));
  }
  // Case (4)
  private static <K extends Comparable<K>, V> Node<K, V> c4(final Node<K, V> t1, final K a1, final V v1, final Node<K, V> t2, final K a2, final V v2, final Node<K, V> t3) {
    return cN2(cN2(t1, a1, v1, t2), a2, v2, cN1(t3));
  }
  // Case (5)
  private static <K extends Comparable<K>, V> Node<K, V> c5(final Node<K, V> t1, final K a1, final V v1, final Node<K, V> t2, final K a2, final V v2, final Node<K, V> t3, final K a3, final V v3, final Node<K, V> t4) {
    return c3(t1, a1, v1, t2, a2, v2, t3, a3, v3, t4);
  }
  // Case (6)
  private static <K extends Comparable<K>, V> Node<K, V> c6(final Node<K, V> t1, final K a1, final V v1, final Node<K, V> t2, final K a2, final V v2, final Node<K, V> t3) {
    return cN1(cN2(t1, a1, v1, cN2(t2, a2, v2, t3)));
  }
  // Case (7)
  private static <K extends Comparable<K>, V> Node<K, V> c7(final Node<K, V> t1, final K a1, final V v1, final Node<K, V> t2, final K a2, final V v2, final Node<K, V> t3) {
    return cN2(cN1(t1), a1, v1, (cN2(t2, a2, v2, t3)));
  }
  // Case (8)
  private static <K extends Comparable<K>, V> Node<K, V> c8(final Node<K, V> t1, final K a, final V v, final Node<K, V> t2) {
    return cN2(t1, a, v, t2);
  }
}
