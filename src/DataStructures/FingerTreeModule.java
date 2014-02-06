/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import Utils.Susp;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author Neo
 */
public class FingerTreeModule {
  /**
   *
   * @param <A>
   */
  public static interface Monoid<A> {
    public A empty();

    public A plus(final A a0, final A a1);
  }

  private static final int sZeroTag  = 0;
  private static final int sOneTag   = 1;
  private static final int sTwoTag   = 2;
  private static final int sThreeTag = 3;
  private static final int sFourTag  = 4;

  private static abstract class Digit<A> {
    private final int mTag;

    public abstract Digit<A> incFirst(final A a);
    public abstract Digit<A> incLast(final A a);
    public abstract A getFirst();

    public abstract Digit<A> decFirst();
    public abstract Digit<A> decLast();
    public abstract A getLast();

    protected Digit(final int tag) {
      mTag = tag;
    }
  }

  private static <A> A except() { throw new AssertionError("aa"); }

  private static final class Zero<A> extends Digit<A> {
    private Zero() { super(sZeroTag); }
    private static final Zero<?> sZero = new Zero<>();

    @SuppressWarnings("unchecked")
    public static <A> Zero<A> create() {
      return (Zero<A>) sZero;
    }

    @Override
    public One<A> incFirst(final A a) {
      return except();
    }

    @Override
    public Two<A> incLast(final A a) {
      return except();
    }

    @Override
    public A getFirst() {
      return except();
    }

    @Override
    public Digit<A> decFirst() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Digit<A> decLast() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public A getLast() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  }

  private static final class One<A> extends Digit<A> {
    private One(final A a) {
      super(sOneTag);
      ma0 = a;
    }

    public static <A> One<A> create(final A a0) {
      return new One<>(a0);
    }

    @Override
    public Two<A> incFirst(final A a) {
      return Two.create(a, ma0);
    }

    @Override
    public Two<A> incLast(final A a) {
      return Two.create(ma0, a);
    }

    @Override
    public A getFirst() {
      return ma0;
    }

    @Override
    public Digit<A> decFirst() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Digit<A> decLast() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public A getLast() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public final A ma0;
  }

  private static final class Two<A> extends Digit<A> {
    private Two(final A a0, final A a1) {
      super(sTwoTag);
      ma0 = a0;
      ma1 = a1;
    }

    public static <A> Two<A> create(final A a0, final A a1) {
      return new Two<>(a0, a1);
    }

    @Override
    public Three<A> incFirst(final A a) {
      return Three.create(a, ma0, ma1);
    }

    @Override
    public Three<A> incLast(final A a) {
      return Three.create(ma0, ma1, a);
    }

    @Override
    public A getFirst() {
      return ma0;
    }

    @Override
    public Digit<A> decFirst() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Digit<A> decLast() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public A getLast() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public final A ma0;
    public final A ma1;
  }

  private static final class Three<A> extends Digit<A> {
    private Three(final A a0, final A a1, final A a2) {
      super(sThreeTag);
      ma0 = a0;
      ma1 = a1;
      ma2 = a2;
    }

    public static <A> Three<A> create(final A a0, final A a1, final A a2) {
      return new Three<>(a0, a1, a2);
    }

    @Override
    public Four<A> incFirst(final A a) {
      return Four.create(a, ma0, ma1, ma2);
    }

    @Override
    public Four<A> incLast(final A a) {
      return Four.create(ma0, ma1, ma2, a);
    }

    @Override
    public A getFirst() {
      return ma0;
    }

    @Override
    public Digit<A> decFirst() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Digit<A> decLast() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public A getLast() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public final A ma0;
    public final A ma1;
    public final A ma2;
  }

  private static final class Four<A> extends Digit<A> {
    private Four(final A a0, final A a1, final A a2, final A a3) {
      super(sFourTag);
      ma0 = a0;
      ma1 = a1;
      ma2 = a2;
      ma3 = a3;
    }

    public static <A> Four<A> create(final A a0, final A a1, final A a2, final A a3) {
      return new Four<>(a0, a1, a2, a3);
    }

    @Override
    public Three<A> incFirst(final A a) {
      throw new AssertionError("incFirst() must never be called on a Four object.");
    }

    @Override
    public Three<A> incLast(final A a) {
      throw new AssertionError("incLast() must never be called on a Four object.");
    }

    @Override
    public A getFirst() {
      return ma0;
    }

    @Override
    public Digit<A> decFirst() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Digit<A> decLast() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public A getLast() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public final A ma0;
    public final A ma1;
    public final A ma2;
    public final A ma3;
  }

  private static final int sNode2Tag = 2;
  private static final int sNode3Tag = 3;

  private static abstract class Node<A> {
    protected Node(final int tag) {
      mTag = tag;
    }

    public final int mTag;
  }

  private static final class Node2<A> extends Node<A> {
    private Node2(final A a0, final A a1) {
      super(sNode2Tag);
      ma0 = a0;
      ma1 = a1;
    }

    public static <A> Node2<A> create(final A a0, final A a1) {
      return new Node2<>(a0, a1);
    }

    public final A ma0;
    public final A ma1;
  }

  private static final class Node3<A> extends Node<A> {
    private Node3(final A a0, final A a1, final A a2) {
      super(sNode3Tag);
      ma0 = a0;
      ma1 = a1;
      ma2 = a2;
    }

    public static <A> Node3<A> create(final A a0, final A a1, final A a2) {
      return new Node3<>(a0, a1, a2);
    }

    public final A ma0;
    public final A ma1;
    public final A ma2;
  }

  private static final int sEmptyTag  = 0;
  private static final int sSingleTag = 1;
  private static final int sDeepTag   = 2;

  private static abstract class FingerTree<A> { //implements Reducer<A> {
    protected FingerTree(final int tag) {
      mTag = tag;
    }

    public abstract FingerTree<A> addFirst(final A a);
    public abstract FingerTree<A> addLast(final A a);
    public abstract boolean isEmpty();
    public abstract A head();
    public abstract FingerTree<A> tail();

    public final int mTag;
    
    protected boolean isEmptyNode() { return mTag == sEmptyTag; }
    protected boolean isSingleNode() { return mTag == sSingleTag; }
    protected boolean isDeepNode() { return mTag == sDeepTag; }
  }

  private static final class Empty<A> extends FingerTree<A> {
    private Empty() {
      super(sEmptyTag);
    }

    private static final Empty<?> sEmptyNode = new Empty<>();
    private static final Susp<?> sEmptyNodeSusp = Susp.create(() -> sEmptyNode);

    @SuppressWarnings("unchecked")
    public static <A> Empty<A> create() {
      return (Empty<A>) sEmptyNode;
    }

    @SuppressWarnings("unchecked")
    public static <A> Susp<FingerTree<A>> createSusp() {
      return (Susp<FingerTree<A>>) sEmptyNodeSusp;
    }

    @Override
    public FingerTree<A> addFirst(final A a) {
      return Single.create(a);
    }

    @Override
    public FingerTree<A> addLast(final A a) {
      return Single.create(a);
    }

    @Override
    public boolean isEmpty() {
      return true;
    }

    @Override
    public A head() {
      throw new AssertionError("Cannot take the head() of an empty list.");
    }

    @Override
    public FingerTree<A> tail() {
      throw new AssertionError("Cannot take the tail() of an empty list.");
    }
  }

  private static final class Single<A> extends FingerTree<A> {
    private Single(final A a) {
      super(sSingleTag);
      ma = a;
    }

    public static <A> Single<A> create(final A a) {
      return new Single<>(a);
    }

    public final A ma;

    @Override
    public FingerTree<A> addFirst(final A a) {
      return Deep.create(One.create(a), Empty.createSusp(), One.create(ma));
    }

    @Override
    public Deep<A> addLast(final A a) {
      return Deep.create(One.create(ma), Empty.createSusp(), One.create(a));
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public A head() {
      return ma;
    }

    @Override
    public FingerTree<A> tail() {
      return Empty.create();
    }
  }

  private static final class Deep<A> extends FingerTree<A> {
    private Deep(final Digit<A> da0, final Susp<FingerTree<Node<A>>> node, final Digit<A> da1) {
      super(sSingleTag);
      mda0  = da0;
      mNode = node;
      mda1  = da1;
    }

    public static <A> Deep<A> create(final Digit<A> da0,
                                     final Susp<FingerTree<Node<A>>> node,
                                     final Digit<A> da1) {
      return new Deep<>(da0, node, da1);
    }

    public final Digit<A> mda0;
    public final Susp<FingerTree<Node<A>>> mNode;
    public final Digit<A> mda1;

    @Override
    public FingerTree<A> addFirst(final A a) {
      if (mda0 instanceof Four) {
        final Four<A> f = (Four<A>) mda0;
        return Deep.create(Two.create(a, f.ma0),
                           new Susp<>(() -> mNode.addFirst(Node3.create(f.ma1, f.ma2, f.ma3))),
                           mda1);
      }
      else {
        return Deep.create(mda0.incFirst(a), mNode, mda1);
      }
    }

    @Override
    public Deep<A> addLast(final A a) {
      if (mda1 instanceof Four) {
        final Four<A> f = (Four<A>) mda1;
        return Deep.create(mda0,
                           new Susp<>(() -> mNode.addLast(Node3.create(f.ma0, f.ma1, f.ma2))),
                           Two.create(f.ma3, a));
      }
      else {
        return Deep.create(mda0, mNode, mda1.incLast(a));
      }
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public A head() {
      return mda0.getFirst();
    }

    private static class ListLazy<A> {}

    private static class NilLazy<A> extends ListLazy<A> {
      private static final NilLazy<?> sNilLazy = new NilLazy<>();
      private static final Susp<?> sNilLazySusp = Susp.create(() -> sNilLazy);

      @SuppressWarnings("unchecked")
      public static <A> ListLazy<A> create() {
        return (NilLazy<A>) sNilLazy;
      }

      @SuppressWarnings("unchecked")
      public static <A> Susp<ListLazy<A>> createSusp() {
        return (Susp<ListLazy<A>>) sNilLazySusp;
      }
    }

    private static class ConsLazy<A> extends ListLazy<A> {
      private final Susp<A> mCar;
      private final Susp<FingerTree<A>> mCdr;

      private ConsLazy(final Susp<A> car, final Susp<FingerTree<A>> cdr) {
        mCar = car;
        mCdr = cdr;
      }

      public static <A> ListLazy<A> create(final Susp<A> car, final Susp<FingerTree<A>> cdr) {
        return new ConsLazy<>(car, cdr);
      }
    }

    private static <A> ListLazy<A> viewL(final FingerTree<A> t) {
      if (t.isEmptyNode()) {
        return NilLazy.create();
      }
      else if (t.isSingleNode()) {
        @SuppressWarnings("unchecked")
        final A a = ((Single<A>)t).ma;
        return ConsLazy.create(Susp.create(() -> a), Empty.createSusp());
      }
      else {
        @SuppressWarnings("unchecked")
        final Deep<A> d = (Deep<A>) t;
        return ConsLazy.create(
                Susp.create(() -> d.mda0.getFirst()),
                Susp.create(() -> deepL(d.mda0.decFirst(), d.mNode, d.mda1)));
      }
    }

    private static <A> FingerTree<A> deepL(final Digit<A> d0, final Susp<FingerTree<Node<A>>> t, final Digit<A> d1) {
      if (d0 instanceof Zero) {
        final ListLazy<Node<A>> l = viewL(t.eval());
        if (l instanceof NilLazy) {
          return toTree(d1);
        }
        else {
          final ConsLazy<Node<A>> c = (ConsLazy<Node<A>>) l;
          final Digit<A> d = toDigit(c.mCar.eval());
          return Deep.create(d, c.mCdr, d1);
        }
      }
      else {
        return Deep.create(d0, t, d1);
      }
    }

    private static <A> FingerTree<A> toTree(Digit<A> d) {
      return null;
    }

    private static <A> Digit<A> toDigit(final Node<A> a) {
      return null;
    }

    @Override
    public FingerTree<A> tail() {
      final ListLazy<A> l = viewL(this);
      if (l instanceof ConsLazy) {
        final ConsLazy<A> c = (ConsLazy<A>) l;
        return c.mCdr.eval();
      }
      else {
        throw new AssertionError("Cannot get the tail() on an empty sequence.");
      }
    }
  }
}
