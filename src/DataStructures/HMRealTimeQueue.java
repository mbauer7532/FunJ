/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import Utils.Functionals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Neo
 * @param <V>
 */
public class HMRealTimeQueue<V> implements PersistentQueue<V, HMRealTimeQueue<V>> {
  private static class RotationState<V> {}

  private static final class Idle<V> extends RotationState<V> {
    @SuppressWarnings("unchecked")
    public static <V> Idle<V> create() { return (Idle<V>) sIdleRotationState; }
    private static final Idle<?> sIdleRotationState = new Idle<>();

    @Override
    public String toString() {
      return "Idle";
    }
  }

  private static final class Reversing<V> extends RotationState<V> {
    public static <V> Reversing<V> create(final int ok, final LinkedList<V> f, final LinkedList<V> fp, final LinkedList<V> r, final LinkedList<V> rp) {
      return new Reversing<>(ok, f, fp, r, rp);
    }

    private Reversing(final int ok, final LinkedList<V> f, final LinkedList<V> fp, final LinkedList<V> r, final LinkedList<V> rp) {
      mok = ok;
      mf  = f;
      mfp = fp;
      mr  = r;
      mrp = rp;
    }

    @Override
    public String toString() {
      return String.format("Reversing(ok = %d, f = %s, fp = %s, r = %s, rp = %s)", mok, mf.toString(), mfp.toString(), mr.toString(), mrp.toString());
    }

    public final int mok;
    public final LinkedList<V> mf;
    public final LinkedList<V> mfp;
    public final LinkedList<V> mr;
    public final LinkedList<V> mrp;
  }

  private static final class Appending<V> extends RotationState<V> {
    public static <V> Appending<V> create(final int ok, final LinkedList<V> fp, final LinkedList<V> rp) {
      return new Appending<>(ok, fp, rp);
    }

    private Appending(final int ok, final LinkedList<V> fp, final LinkedList<V> rp) {
      mok = ok;
      mfp = fp;
      mrp = rp;
    }
    
    @Override
    public String toString() {
      return String.format("Appending(ok = %d, fp = %s, rp = %s)", mok, mfp.toString(), mrp.toString());
    }

    public final int mok;
    public final LinkedList<V> mfp;
    public final LinkedList<V> mrp;
  }

  private static final class Done<V> extends RotationState<V> {
    public static <V> Done<V> create(final LinkedList<V> r) {
      return new Done<>(r);
    }

    private Done(final LinkedList<V> r) {
      mr = r;
    }

    @Override
    public String toString() {
      return String.format("Done(r = %s)", mr.toString());
    }

    public final LinkedList<V> mr;
  }

  private static <V> RotationState<V> exec2(final RotationState<V> istate) {
    RotationState<V> state = istate;

    if (state instanceof Reversing) {
      final Reversing<V> revState = (Reversing<V>) state;
      final LinkedList<V> f  = revState.mf;
      final LinkedList<V> r  = revState.mr;
      final LinkedList<V> fp = revState.mfp;
      final LinkedList<V> rp = revState.mrp;
      final int ok           = revState.mok;

      if (f.isEmpty() && r.isSingleton()) {
        state = Appending.create(ok, fp, rp.cons(r.head()));
      }
      else {
        state = Reversing.create(ok + 1, f.tail(), fp.cons(f.head()), r.tail(), rp.cons(r.head()));
      }
    }
    else if (state instanceof Appending) {
      final Appending<V> appState = (Appending<V>) state;
      final int ok = appState.mok;
      if (ok == 0) {
        state = Done.create(appState.mrp);
      }
      else {
        final LinkedList<V> fp = appState.mfp;

        if (! fp.isEmpty()) {
          state = Appending.create(ok - 1, fp.tail(), appState.mrp.cons(fp.head()));
        }
        else {
          return state;
        }
      }
    }
    else {
      return state;
    }

    if (state instanceof Reversing) {
      final Reversing<V> revState = (Reversing<V>) state;
      final LinkedList<V> f  = revState.mf;
      final LinkedList<V> r  = revState.mr;
      final LinkedList<V> fp = revState.mfp;
      final LinkedList<V> rp = revState.mrp;
      final int ok           = revState.mok;

      if (f.isEmpty() && r.isSingleton()) {
        state = Appending.create(ok, fp, rp.cons(r.head()));
      }
      else {
        state = Reversing.create(ok + 1, f.tail(), fp.cons(f.head()), r.tail(), rp.cons(r.head()));
      }
    }
    else if (state instanceof Appending) {
      final Appending<V> appState = (Appending<V>) state;
      final int ok = appState.mok;
      if (ok == 0) {
        state = Done.create(appState.mrp);
      }
      else {
        final LinkedList<V> fp = appState.mfp;

        if (! fp.isEmpty()) {
          state = Appending.create(ok - 1, fp.tail(), appState.mrp.cons(fp.head()));
        }
      }
    }

    return state;
  }

  private static <V> HMRealTimeQueue<V> exec2(final int lenf, final LinkedList<V> f, final RotationState<V> state, final int lenr, final LinkedList<V> r) {
    final RotationState<V> s, newState = exec2(state);
    final LinkedList<V> newf;

    if (newState instanceof Done) {
      newf = ((Done<V>) newState).mr;
      s = Idle.create();
    }
    else {
      newf = f;
      s = newState;
    }

    return create(lenf, newf, s, lenr, r);
  }

  private static <V> RotationState<V> invalidate(final RotationState<V> state) {
    if (state instanceof Reversing) {
      final Reversing<V> revState = (Reversing<V>) state;
      final LinkedList<V> f  = revState.mf;
      final LinkedList<V> r  = revState.mr;
      final LinkedList<V> fp = revState.mfp;
      final LinkedList<V> rp = revState.mrp;
      final int ok           = revState.mok;

      return Reversing.create(ok - 1, f, fp, r, rp);
    }
    else {
      if (state instanceof Appending) {
        final Appending<V> appState = (Appending<V>) state;
        final int ok = appState.mok;
        final LinkedList<V> rp = appState.mrp;

        if (ok == 0 && ! rp.isEmpty()) {
          return Done.create(rp.tail());
        }
        else {
          return Appending.create(ok - 1, appState.mfp, rp);
        }
      }
    }

    return state;
  }

  private static <V> HMRealTimeQueue<V> check(final int lenf, final LinkedList<V> f, final RotationState<V> state, final int lenr, final LinkedList<V> r) {
    if (lenr <= lenf) {
      return exec2(lenf, f, state, lenr, r);
    }
    else {
      final LinkedList<V> e = LinkedList.empty();
      return exec2(lenf + lenr, f, Reversing.create(0, f, e, r, e), 0, e);
    }
  }

  private static final HMRealTimeQueue<?> sEmptyHMRealTimeQueue = HMRealTimeQueue.create(0, LinkedList.empty(), Idle.create(), 0, LinkedList.empty());

  @SuppressWarnings("unchecked")
  public static <V> HMRealTimeQueue<V> empty() {
    return (HMRealTimeQueue<V>) sEmptyHMRealTimeQueue;
  }

  private static <V> boolean isEmptyImpl(final HMRealTimeQueue<V> q) { return q.mlenf == 0; }

  @Override
  public boolean isEmpty() {
    return isEmptyImpl(this);
  }

  public static <V> HMRealTimeQueue<V> singleton(final V x) {
    return HMRealTimeQueue.<V> empty().addLast(x);
  }

  @Override
  public int length() {
    return mlenf + mlenr;
  }

  @Override
  public HMRealTimeQueue<V> addLast(final V x) {
    return check(mlenf, mf, mState, mlenr + 1, mr.cons(x));
  }

  @Override
  public HMRealTimeQueue<V> drop(final int cnt) {
    if (cnt < 0) {
      throw new AssertionError("cnt cannt be less than zero in drop().");
    }
    else {
      return Functionals.reduce(IntStream.range(0, cnt), this, (q, i) -> q.tail());
    }
  }

  @Override
  public ArrayList<V> take(final int cnt) {
    if (cnt < 0) {
      throw new AssertionError("cnt cannt be less than zero in take().");
    }
    else {
      HMRealTimeQueue<V> q = this;
      final ArrayList<V> v = new ArrayList<>();
      for (int i = 0; i != cnt; ++i) {
        v.add(headImpl(q));
        q = tailImpl(q);
      }

      return v;
    }
  }

  public static <V> HMRealTimeQueue<V> fromArray(final ArrayList<V> vs) {
    return fromStream(vs.stream());
  }

  public static <V> HMRealTimeQueue<V> fromStream(final Stream<V> s) {
    return Functionals.reduce(s, empty(), (q, e) -> q.addLast(e));
  }

  @SafeVarargs
  public static <V> HMRealTimeQueue<V> of(final V ... v) {
    return fromStream(Arrays.stream(v));
  }

  private static <V> V headImpl(final HMRealTimeQueue<V> q) {
    return q.mf.head();
  }

  @Override
  public V head() {
    return headImpl(this);
  }

  private static <V> HMRealTimeQueue<V> tailImpl(final HMRealTimeQueue<V> q) {
    return check(q.mlenf - 1, q.mf.tail(), invalidate(q.mState), q.mlenr, q.mr);
  }

  @Override
  public HMRealTimeQueue<V> tail() {
    return tailImpl(this);
  }

  @Override
  public boolean contains(final V v) {
    return mf.contains(v) || mr.contains(v);
  }

  @Override
  public boolean notContains(final V v) {
    return mf.notContains(v) && mr.notContains(v);
  }

  private HMRealTimeQueue(final int lenf,
                          final LinkedList<V> f,
                          final RotationState<V> state,
                          final int lenr,
                          final LinkedList<V> r) {
    mlenf  = lenf;
    mf     = f;
    mState = state;
    mlenr  = lenr;
    mr     = r;
  }

  private static <V> HMRealTimeQueue<V> create(final int lenf,
                                               final LinkedList<V> f,
                                               final RotationState<V> state,
                                               final int lenr,
                                               final LinkedList<V> r) {
    return new HMRealTimeQueue<>(lenf, f, state, lenr, r);
  }

  @Override
  public String toString() {
    return String.format("[sz = %d, lenf = %d, f = %s, State = %s, lenr = %d, r = %s]",
                         mlenf + mlenr,
                         mlenf,
                         mf.toString(),
                         mState.toString(),
                         mlenr,
                         mr.toString());
  }

  @Override
  public boolean equals(final Object obj) {
    if (! (obj instanceof PersistentQueue)) {
      return false;
    }
    else {
      PersistentQueue<V, ?> la = this;
      @SuppressWarnings("unchecked")
      PersistentQueue<V, ?> lb = (PersistentQueue<V, ?>) obj;

      while (! la.isEmpty() && ! lb.isEmpty()) {
        if (! la.head().equals(lb.head())) {
          return false;
        }
        la = la.tail();
        lb = lb.tail();
      }
 
      return la.isEmpty() && lb.isEmpty();
    }
  }

  private static <V> int hashCodeImpl(final HMRealTimeQueue<V> list) {
    HMRealTimeQueue<V> t = list;
    int hashRes = 42; // the answer...

    while (! isEmptyImpl(t)) {
      hashRes += headImpl(t).hashCode();
      t = tailImpl(t);
    }

    return hashRes;
  }

  @Override
  public int hashCode() {
    return hashCodeImpl(this);
  }

  private final int mlenf;
  private final LinkedList<V> mf;
  private final RotationState<V> mState;
  private final int mlenr;
  private final LinkedList<V> mr;
}
