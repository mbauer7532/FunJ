/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import Utils.Functionals;
import java.util.ArrayList;
import java.util.Arrays;
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

    public final LinkedList<V> mr;
  }

  private static <V> RotationState<V> exec(final RotationState<V> state) {
    if (state instanceof Reversing) {
      final Reversing<V> revState = (Reversing<V>) state;
      final LinkedList<V> f  = revState.mf;
      final LinkedList<V> r  = revState.mr;
      final LinkedList<V> fp = revState.mfp;
      final LinkedList<V> rp = revState.mrp;
      final int ok           = revState.mok;

      if (f.isEmpty() && r.isSingleton()) {
        return Appending.create(ok, fp, rp.cons(r.head()));
      }
      else {
        return Reversing.create(ok + 1, f, fp.cons(f.head()), r, rp.cons(r.head()));
      }
    }
    else if (state instanceof Appending) {
      final Appending<V> appState = (Appending<V>) state;
      final int ok = appState.mok;
      if (ok == 0) {
        return Done.create(appState.mrp);
      }
      else {
        final LinkedList<V> fp = appState.mfp;

        if (! fp.isEmpty()) {
          return Appending.create(ok - 1, fp.tail(), appState.mrp.cons(fp.head()));
        }
      }
    }

    return state;
  }

  private static <V> HMRealTimeQueue<V> exec2(final HMRealTimeQueue<V> q) {
    final RotationState<V> s, newState = exec(exec(q.mState));
    final LinkedList<V> newf;

    if (newState instanceof Done) {
      newf = ((Done<V>) newState).mr;
      s = Idle.create();
    }
    else {
      newf = q.mf;
      s = newState;
    }
    return create(q.mlenf, newf, s, q.mlenr, q.mr);
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

  private static <V> HMRealTimeQueue<V> check(final HMRealTimeQueue<V> q) {
    final int lenf = q.mlenf;
    final int lenr = q.mlenr;

    if (lenr <= lenf) {
      return exec2(q);
    }
    else {
      final LinkedList<V> e = LinkedList.empty();
      final LinkedList<V> f = q.mf;
      final LinkedList<V> r = q.mr;

      final RotationState<V> newState = Reversing.create(0, f, e, r, e);
      return exec2(HMRealTimeQueue.create(lenf + lenr, f, newState, 0, e));
    }
  }

  private static final HMRealTimeQueue<?> sEmptyHMRealTimeQueue = HMRealTimeQueue.create(0, LinkedList.empty(), Idle.create(), 0, LinkedList.empty());

  @SuppressWarnings("unchecked")
  public static <V> HMRealTimeQueue<V> empty() {
    return (HMRealTimeQueue<V>) sEmptyHMRealTimeQueue;
  }

  @Override
  public boolean isEmpty() {
    return mlenf == 0;
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
    return check(HMRealTimeQueue.create(mlenf, mf, mState, mlenr + 1, mr.cons(x)));
  }

  @Override
  public HMRealTimeQueue<V> drop(final int cnt) {
    if (cnt < 0) {
      throw new AssertionError("cnt cannt be less than zero in drop().");
    }
    else {
      HMRealTimeQueue<V> q = this;
      for (int i = 0; i != cnt; ++i) {
        q = q.tail();
      }

      return q;
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
        v.add(q.head());
        q = q.tail();
      }

      return v;
    }
  }

  public static <V> HMRealTimeQueue<V> fromArray(final ArrayList<V> vs) {
    return fromStream(vs.stream());
  }

  public static <V> HMRealTimeQueue<V> fromStream(final Stream<V> s) {
    return s.reduce(empty(), (q, e) -> q.addLast(e), Functionals::functionShouldNotBeCalled);
  }

  @SafeVarargs
  public static <V> HMRealTimeQueue<V> of(final V ... v) {
    return fromStream(Arrays.stream(v));
  }

  @Override
  public V head() {
    return mf.head();
  }

  @Override
  public HMRealTimeQueue<V> tail() {
    return check(HMRealTimeQueue.create(mlenf - 1,
                                        mf.tail(),
                                        invalidate(mState),
                                        mlenr,
                                        mr));
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

  private final int mlenf;
  private final LinkedList<V> mf;
  private final RotationState<V> mState;
  private final int mlenr;
  private final LinkedList<V> mr;
}
