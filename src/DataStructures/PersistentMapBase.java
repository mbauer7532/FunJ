/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
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
    return minElementPair().map(Pair::getFirst);
  }

  @Override
  public final Optional<K> maxKey() {
    return maxElementPair().map(Pair::getFirst);
  }

  @Override
  public final Optional<K> higherKey(final K key) {
    return higherPair(key).map(Pair::getFirst);
  }

  @Override
  public final Optional<K> lowerKey(final K key) {
    return lowerPair(key).map(Pair::getFirst);
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
  public final ArrayList<Pair<K, V>> keyValuePairs() {
    final ArrayList<Pair<K, V>> kvs = new ArrayList<>();
    appi((k, v) -> { kvs.add(Pair.create(k, v)); });

    return kvs;
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

  protected ArrayList<Pair<K, V>> getElementsSatisfyingPredicate(final BiPredicate<K, V> f) {
    final ArrayList<Pair<K, V>> kvs = new ArrayList<>();

    appi((k, v) -> {
      if (f.test(k, v)) {
        kvs.add(Pair.create(k, v));
      }
    });

    return kvs;
  }

  protected Pair<ArrayList<Pair<K, V>>, ArrayList<Pair<K, V>>> splitElemsAccordingToPredicate(final BiPredicate<K, V> f) {
    final ArrayList<Pair<K, V>> v0 = new ArrayList<>(), v1 = new ArrayList<>();

    appi((k, v) -> {
      final Pair<K, V> p = Pair.create(k, v);
      if (f.test(k, v)) {
        v0.add(p);
      }
      else {
        v1.add(p);
      }
    });

    return Pair.create(v0, v1);
  }

  protected <W> ArrayList<Pair<K, W>> selectNonEmptyOptionalElements(final BiFunction<K, V, Optional<W>> f) {
    final ArrayList<Pair<K, W>> vws = new ArrayList<>();

    appi((k, v) -> {
      final Optional<W> opt = f.apply(k, v);
      if (opt.isPresent()) {
        vws.add(Pair.create(k, opt.get()));
      }
    });

    return vws;
  }

  protected static <K extends Comparable<K>, V> ArrayList<Pair<K, V>> mergeArrays(
          final BiFunction<V, V, V> f,
          final ArrayList<Pair<K, V>> v0,
          final ArrayList<Pair<K, V>> v1) {
    final int s0 = v0.size();
    final int s1 = v1.size();
    final int len = s0 + s1;

    final ArrayList<Pair<K, V>> destVec = new ArrayList<>(len);

    if (v0.get(s0 - 1).mx1.compareTo(v1.get(0).mx1) < 0) {
      destVec.addAll(v0);
      destVec.addAll(v1);
    }
    else if (v0.get(0).mx1.compareTo(v1.get(s1 - 1).mx1) > 0) {
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
          final Pair<K, V> e0 = v0.get(idx0), e1 = v1.get(idx1), e;
          final int res = e0.mx1.compareTo(e1.mx1);

          if (res < 0) {
            e = e0;
            ++idx0;
          } else if (res > 0) {
            e = e1;
            ++idx1;
          } else {
            e = Pair.create(e0.mx1, f.apply(e0.mx2, e1.mx2));
            ++idx0;
            ++idx1;
          }

          destVec.add(e);
        }
      }
    }

    return destVec;
  }

  protected abstract K getKey();
  protected abstract V getValue();
  protected abstract Pair<K, V> getKeyValuePair();
  protected abstract M getLeft();
  protected abstract M getRight();
  
  private static <K extends Comparable<K>, V, M extends PersistentMapBase<K, V, M>> Optional<Pair<K, V>> makeBoundPair(final PersistentMapBase<K, V, M> candidate) {
    return candidate == null
            ? Optional.empty()
            : Optional.of(candidate.getKeyValuePair());
  }

  public Optional<Pair<K, V>> lowerPair(final K key) {
    PersistentMapBase<K, V, M> node = this;
    PersistentMapBase<K, V, M> candidate = null;

    while (! node.isEmpty()) {
      int res = key.compareTo(node.getKey());
      if (res > 0) {
        candidate = node;
        node = node.getRight();
      }
      else if (res < 0) {
        node = node.getLeft();
      }
      else {
         final Optional<Pair<K, V>> p = node.getLeft().maxElementPair();
         if (p.isPresent()) {
           return p;
         }
         else {
           break;
         }
      }
    }

    return makeBoundPair(candidate);
  }

  public Optional<Pair<K, V>> higherPair(final K key) {
    PersistentMapBase<K, V, M> node = this;
    PersistentMapBase<K, V, M> candidate = null;

    while (! node.isEmpty()) {
      int res = key.compareTo(node.getKey());
      if (res > 0) {
        node = node.getRight();
      }
      else if (res < 0) {
        candidate = node;
        node = node.getLeft();
      }
      else {
        final Optional<Pair<K, V>> p = node.getRight().minElementPair();
        if (p.isPresent()) {
           return p;
         }
         else {
           break;
         }
      }
    }

    return makeBoundPair(candidate);
  }
}
