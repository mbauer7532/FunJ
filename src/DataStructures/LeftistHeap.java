/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import Utils.ArrayUtils;
import Utils.Ref;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.StructureGraphic.v1.DSTreeNode;

/**
 *
 * @author Neo
 * @param <V>
 */
public final class LeftistHeap<V extends Comparable<V>> implements PersistentHeap<V, LeftistHeap<V>> {
  private final int mRank;
  private final V mVal;
  private final LeftistHeap<V> mLeft;
  private final LeftistHeap<V> mRight;

  private LeftistHeap(final int rank, final V val, final LeftistHeap<V> left, final LeftistHeap<V> right) {
    mRank = rank;
    mVal = val;
    mLeft = left;
    mRight = right;
  }

  private static final LeftistHeap<? extends Comparable<?>> sEmptyLeftistHeap = new LeftistHeap<>(0, null, null, null);

  @SuppressWarnings("unchecked")
  public static <V extends Comparable<V>> LeftistHeap<V> empty() {
    return (LeftistHeap<V>) sEmptyLeftistHeap;
  }

  private static <V extends Comparable<V>> LeftistHeap<V> create(final int rank, final V val, final LeftistHeap<V> left, final LeftistHeap<V> right) {
    return new LeftistHeap<>(rank, val, left, right);
  }

  public static <V extends Comparable<V>> LeftistHeap<V> singleton(final V val) {
    final LeftistHeap<V> e = empty();
    return create(1, val, e, e);
  }

  private static <V extends Comparable<V>> boolean isEmptyImpl(final LeftistHeap<V> h) {
    return h.mRank == 0;
  }

  @Override
  public boolean isEmpty() {
    return isEmptyImpl(this);
  }

  private static <V extends Comparable<V>> V findMinImpl(final LeftistHeap<V> h) {
    if (isEmptyImpl(h)) {
      throw new AssertionError("Attempted to get the min element of an empty heap.");
    }
    else {
      return h.mVal;
    }
  }

  @Override
  public V findMin() {
    return findMinImpl(this);
  }

  private static <V extends Comparable<V>> LeftistHeap<V> makeT(final V x, final LeftistHeap<V> a, final LeftistHeap<V> b) {
    final int rank;
    final LeftistHeap<V> left, right;

    if (a.mRank >= b.mRank) {
      rank = b.mRank;
      left = a;
      right = b;
    }
    else {
      rank = a.mRank;
      left = b;
      right = a;
    }

    return create(rank + 1, x, left, right);
  }

  private static <V extends Comparable<V>> LeftistHeap<V> mergeImpl(final LeftistHeap<V> h1, final LeftistHeap<V> h2) {
    if (isEmptyImpl(h1)) {
      return h2;
    }
    else if (isEmptyImpl(h2)) {
      return h1;
    }
    else {
      final LeftistHeap<V> left, right;
      final V x = h1.mVal, y = h2.mVal, z;
      final int res = x.compareTo(y);

      if (res <= 0) {
        z = x;
        left = h1.mLeft;
        right = mergeImpl(h1.mRight, h2);
      }
      else {
        z = y;
        left = h2.mLeft;
        right = mergeImpl(h1, h2.mRight);
      }
      return makeT(z, left, right);
    }
  }

  @Override
  public LeftistHeap<V> merge(final LeftistHeap<V> h) {
    return mergeImpl(this, h);
  }

  private static <V extends Comparable<V>> LeftistHeap<V> insertImpl(final LeftistHeap<V> h, final V val) {
    return mergeImpl(singleton(val), h);
  }

  @Override
  public LeftistHeap<V> insert(final V val) {
    return insertImpl(this, val);
  }

  @Override
  public LeftistHeap<V> deleteMin() {
    return mergeImpl(mLeft, mRight);
  }

  private static <V extends Comparable<V>> LeftistHeap<V> deleteMinImpl(final LeftistHeap<V> h, final Ref<V> v) {
    v.r = h.mVal;
    return mergeImpl(h.mLeft, h.mRight);
  }

  @Override
  public LeftistHeap<V> deleteMin(final Ref<V> v) {
    return deleteMinImpl(this, v);
  }

  private static <V extends Comparable<V>> int sizeImpl(final LeftistHeap<V> h) {
    return isEmptyImpl(h) ? 0 : 1 + sizeImpl(h.mLeft) + sizeImpl(h.mRight);
  }

  @Override
  public int size() {
    return sizeImpl(this);
  }

  public static <V extends Comparable<V>> LeftistHeap<V> fromStream(final Stream<V> s) {
    final ArrayDeque<LeftistHeap<V>> q = new ArrayDeque<>();
    s.forEach(elem -> { q.addLast(singleton(elem)); });

    if (q.isEmpty()) {
      return empty();
    }
    else {
      LeftistHeap<V> h1, h2;
      do {
        h1 = q.getFirst();
        if (q.isEmpty()) {
          return h1;
        }
        h2 = q.getFirst();
        q.addLast(mergeImpl(h1, h2));
      } while (true);
    }
  }

  public static <V extends Comparable<V>> LeftistHeap<V> fromArray(final ArrayList<V> v) {
    return fromStream(v.stream());
  }

  private static <V extends Comparable<V>> ArrayList<V> toArrayListImpl(final LeftistHeap<V> h) {
    final ArrayList<V> v = new ArrayList<>();

    for (V val : h) { v.add(val); }
    return v;
  }

  public ArrayList<V> toArrayList() {
    return toArrayListImpl(this);
  }

  private static <V extends Comparable<V>> ArrayList<V> toAscArrayListImpl(final LeftistHeap<V> h) {
    final ArrayList<V> v = new ArrayList<>();
    final Ref<V> minRef = new Ref<>();

    LeftistHeap<V> heap = h;
    while (! isEmptyImpl(heap)) {
      heap = deleteMinImpl(heap, minRef);
      v.add(minRef.r);
    }

    return v;
  }

  public ArrayList<V> toAscArrayList() {
    return toAscArrayListImpl(this);
  }

  private static <V extends Comparable<V>> ArrayList<V> toDescArrayListImpl(final LeftistHeap<V> h) {
    final ArrayList<V> v = toAscArrayListImpl(h);
    final int len = v.size();
    final int lastIdx = len - 1;
    final int half = len / 2;

    IntStream.range(0, half).forEach(i -> { ArrayUtils.swap(v, i, lastIdx - i); });

    return v;
  }

  public ArrayList<V> toDescArrayList() {
    return toDescArrayListImpl(this);
  }

  private static final class LeftistHeapIterator<V extends Comparable<V>> implements Iterator<V> {
    private final ArrayDeque<LeftistHeap<V>> mQueue;

    public LeftistHeapIterator(final LeftistHeap<V> h) {
      mQueue = new ArrayDeque<>();
      mQueue.addLast(h);
    }

    @Override
    public boolean hasNext() {
      return ! mQueue.isEmpty();
    }

    @Override
    public V next() {
      final LeftistHeap<V> h = mQueue.removeFirst(); // Remove first with throw an exception if queue is empty.
      mQueue.addLast(h.mLeft);
      mQueue.addLast(h.mRight);

      return h.mVal;
    }
  }

  private static final class LeftistHeapSpliterator<V extends Comparable<V>> implements Spliterator<V> {
    private final ArrayDeque<LeftistHeap<V>> mQueue;

    public LeftistHeapSpliterator(final LeftistHeap<V> h) {
      mQueue = new ArrayDeque<>();
      mQueue.addLast(h);
    }

    @Override
    public boolean tryAdvance(final Consumer<? super V> action) {
      if (mQueue.isEmpty()) {
        return false;
      }
      else {
        action.accept(mQueue.removeFirst().mVal);
        return true;
      }
    }

    @Override
    public Spliterator<V> trySplit() {
      return null;
    }

    @Override
    public void forEachRemaining(Consumer<? super V> action) {
      while (! mQueue.isEmpty()) {
        action.accept(mQueue.removeFirst().mVal);
      }
    }

    @Override
    public long estimateSize() {
      return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
      return SIZED | NONNULL | IMMUTABLE | CONCURRENT;
    }
  }


  @Override
  public Iterator<V> iterator() {
    return new LeftistHeapIterator<>(this);
  }
  
  @Override
  public DSTreeNode[] DSgetChildren() {
    return new DSTreeNode[] { mLeft, mRight };
  }

  @Override
  public Object DSgetValue() {
    return mVal.toString();
  }

  @Override
  public Color DSgetColor() {
    return isEmptyImpl(this) ? Color.RED : Color.BLACK;
  }

  @Override
  public Stream<V> stream() {
    return StreamSupport.stream(new LeftistHeapSpliterator<V>(this), false);
  }
}
