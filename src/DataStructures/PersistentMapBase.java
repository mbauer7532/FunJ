/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.AssocPair;
import DataStructures.TuplesModule.Pair;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 *
 * @author Neo
 */
public abstract class PersistentMapBase<K extends Comparable<K>, V, M extends PersistentMapBase<K, V, M>>
                      implements PersistentMap<K, V, M> {
  @Override
  public final boolean containsKey(final K key) {
    return get(key).isPresent();
  }

  @Override
  public final M insert(final K key, final V value) {
    return insert((v0, v1) -> v1, key, value);
  }

  @Override
  public final V getOrElse(final K key, final V def) {
    return get(key).orElse(def);
  }

  @Override
  public final V getOrElseSupplier(final K key, final Supplier<V> other) {
    return get(key).orElseGet(other);
  }

  @Override
  public final void app(final Consumer<V> f) {
    appi((k, v) -> f.accept(v));

    return;
  }

  @Override
  public final Optional<K> minKey() {
    return minElementPair().map(PersistentMapEntry::getKey);
  }

  @Override
  public final Optional<K> maxKey() {
    return maxElementPair().map(PersistentMapEntry::getKey);
  }

  @Override
  public final Optional<K> higherKey(final K key) {
    return higherPair(key).map(PersistentMapEntry::getKey);
  }

  @Override
  public final Optional<K> lowerKey(final K key) {
    return lowerPair(key).map(PersistentMapEntry::getKey);
  }

  @Override
  public final <W> W foldl(final BiFunction<V, W, W> f, final W w) {
    return foldli((k, v, z) -> f.apply(v, z), w);
  }

  @Override
  public final <W> W foldr(final BiFunction<V, W, W> f, final W w) {
    return foldri((k, v, z) -> f.apply(v, z), w);
  }

  @Override
  public final ArrayList<K> keys() {
    final ArrayList<K> ks = new ArrayList<>();
    appi((k, v) -> { ks.add(k); });

    return ks;
  }

  @Override
  public abstract <W> PersistentMapBase<K, W, ?> mapi(final BiFunction<K, V, W> f);

  @Override
  public final Pair<M, M> partition(final Predicate<V> f) {
    return partitioni((k, v) -> f.test(v));
  }

  @Override
  public final M filter(final Predicate<V> f) {
    return filteri((k, v) -> f.test(v));
  }

  @Override
  public final boolean equals(final Object m) {
    if (this == m) {
      return true;
    }
    if (! (m instanceof PersistentMapBase)) {
      return false;
    }
    // This is probably not the best implentation.  I need to rewrite this when I do the one-node rewrite of the maps...
    // It does too much allocation.
    // One possibility is to use iterators or streams which I don't have yet.
    final PersistentMapBase<K, V, ?> m1 = this;
    @SuppressWarnings("unchecked")
    final PersistentMapBase<K, V, ?> m2 = (PersistentMapBase<K, V, ?>) m;

    final ArrayList<PersistentMapEntry<K, V>> keyValues1 = m1.keyValuePairs();
    final ArrayList<PersistentMapEntry<K, V>> keyValues2 = m2.keyValuePairs();
    final int l1 = m1.size();
    final int l2 = m2.size();

    return l1 == l2 && IntStream.range(0, l1).parallel().allMatch(idx -> keyValues1.get(idx).equals(keyValues2.get(idx)));
  }

  // This is probably not the best implentation.  I need to rewrite this when I do the one-node rewrite of the maps...
  // It does too much allocation.
  // One possibility is to use iterators or streams which I don't have yet.
  @Override
  public int hashCode() {
    final ArrayList<PersistentMapEntry<K, V>> keyValues = keyValuePairs();
    final int emptyMapHashCode = 11;

    return keyValues.parallelStream().reduce(emptyMapHashCode,
                                             (acc, p) -> acc + p.hashCode(),
                                             (u1, u2) -> u1 + u2);
  }

  protected static <K extends Comparable<K>, V> ArrayList<PersistentMapEntry<K, V>> mergeArrays(
          final BiFunction<V, V, V> f,
          final ArrayList<PersistentMapEntry<K, V>> v0,
          final ArrayList<PersistentMapEntry<K, V>> v1) {
    final int s0 = v0.size();
    final int s1 = v1.size();
    final int len = s0 + s1;

    final ArrayList<PersistentMapEntry<K, V>> destVec = new ArrayList<>(len);

    if (v0.get(s0 - 1).getKey().compareTo(v1.get(0).getKey()) < 0) {
      destVec.addAll(v0);
      destVec.addAll(v1);
    }
    else if (v0.get(0).getKey().compareTo(v1.get(s1 - 1).getKey()) > 0) {
      destVec.addAll(v1);
      destVec.addAll(v0);
    }
    else {
      int idx0 = 0, idx1 = 0;
      for (int i = 0; i != len; ++i) {
        if (idx0 == s0) {
          IntStream.range(idx1, s1).forEach(j -> { destVec.add(v1.get(j)); });
          break;
        }
        else if (idx1 == s1) {
          IntStream.range(idx0, s0).forEach(j -> { destVec.add(v0.get(j)); });
          break;
        }
        else {
          final PersistentMapEntry<K, V> e0 = v0.get(idx0), e1 = v1.get(idx1), e;
          final int res = e0.getKey().compareTo(e1.getKey());

          if (res < 0) {
            e = e0;
            ++idx0;
          } else if (res > 0) {
            e = e1;
            ++idx1;
          } else {
            e = AssocPair.create(e0.getKey(), f.apply(e0.getValue(), e1.getValue()));
            ++idx0;
            ++idx1;
          }

          destVec.add(e);
        }
      }
    }

    return destVec;
  }

  protected ArrayList<PersistentMapEntry<K, V>> getElementsSatisfyingPredicate(final BiPredicate<K, V> f) {
    final ArrayList<PersistentMapEntry<K, V>> kvs = new ArrayList<>();

    appEntry(n -> {
      if (f.test(n.getKey(), n.getValue())) {
        kvs.add(new EntryRef<>(n));
      }
    });
    
    return kvs;
  }

  protected Pair<ArrayList<PersistentMapEntry<K, V>>, ArrayList<PersistentMapEntry<K, V>>> splitElemsAccordingToPredicate(final BiPredicate<K, V> f) {
    final ArrayList<PersistentMapEntry<K, V>> v0 = new ArrayList<>(), v1 = new ArrayList<>();

    appEntry(n -> { (f.test(n.getKey(), n.getValue()) ? v0 : v1).add(new EntryRef<>(n)); });

    return Pair.create(v0, v1);
  }

  protected <W> ArrayList<PersistentMapEntry<K, W>> selectNonEmptyOptionalElements(final BiFunction<K, V, Optional<W>> f) {
    final ArrayList<PersistentMapEntry<K, W>> vws = new ArrayList<>();

    appEntry(n -> {
      final K key = n.getKey();
      final Optional<W> opt = f.apply(key, n.getValue());
      opt.ifPresent(v -> { vws.add(AssocPair.create(key, v)); });
    });

    return vws;
  }
  
  @Override
  public final ArrayList<PersistentMapEntry<K, V>> keyValuePairs() {
    final ArrayList<PersistentMapEntry<K, V>> kvs = new ArrayList<>();
    appEntry(n -> { kvs.add(new EntryRef<>(n)); });

    return kvs;
  }

  public abstract void appEntry(final Consumer<PersistentMapBase<K, V, M>> f);

  protected abstract K getKey();

  protected abstract V getValue();

  public static final class EntryRef<K extends Comparable<K>, V, M extends PersistentMapBase<K, V, M>> extends PersistentMapEntry<K, V> {
    public EntryRef(final PersistentMapBase<K, V, M> node) {
      mNode = node;
    }

    @Override
    public K getKey() { return mNode.getKey(); }

    @Override
    public V getValue() { return mNode.getValue(); }

    private final PersistentMapBase<K, V, M> mNode;
  }
}
