/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;


import Utils.Functionals.IntComparator;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 *
 * @author Neo
 * @param <V>
 */
public abstract class PersistentMapIntEntry<V> {
  /**
   * Returns the key corresponding to this entry.
   *
   * @return the key corresponding to this entry
   */
  public abstract int getKey();

  /**
   * Returns the value corresponding to this entry.
   *
   * @return the value corresponding to this entry
   * @throws IllegalStateException implementations may, but are not
   *         required to, throw this exception if the entry has been
   *         removed from the backing map.
   */
  public abstract V getValue();

  /**
   * Compares the specified object with this entry for equality.
   * Returns <tt>true</tt> if the given object is also a map entry and
   * the two entries represent the same mapping.
   *
   * @param o object to be compared for equality with this map entry
   * @return <tt>true</tt> if the specified object is equal to this map
   *         entry
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof PersistentMapEntry) {
      @SuppressWarnings("unchecked")
      final PersistentMapIntEntry<V> p = (PersistentMapIntEntry<V>) obj;

      return getKey() == p.getKey() && getValue().equals(p.getValue());
    }
    else {
      return false;
    }
  }

  /**
   * Returns the hash code value for this map entry.  The hash code
   * of a map entry <tt>e</tt> is defined to be: <pre>
   *     (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
   *     (e.getValue()==null ? 0 : e.getValue().hashCode())
   * </pre>
   * This ensures that <tt>e1.equals(e2)</tt> implies that
   * <tt>e1.hashCode()==e2.hashCode()</tt> for any two Entries
   * <tt>e1</tt> and <tt>e2</tt>, as required by the general
   * contract of <tt>Object.hashCode</tt>.
   *
   * @return the hash code value for this map entry
   * @see Object#hashCode()
   * @see Object#equals(Object)
   * @see #equals(Object)
   */
  @Override
  public int hashCode() {
    return getKey() + getValue().hashCode();
  }

  /**
   * Returns a comparator that compares {@link Entry} in natural order on key.
   *
   * <p>The returned comparator is serializable and throws {@link
   * NullPointerException} when comparing an entry with a null key.
   *
   * @param  <K> the {@link Comparable} type of then map keys
   * @param  <V> the type of the map values
   * @return a comparator that compares {@link Entry} in natural order on key.
   * @see Comparable
   * @since 1.8
   */
  public static <K extends Comparable<? super K>, V> Comparator<PersistentMapIntEntry<V>> comparingByKey() {
    return (Comparator<PersistentMapIntEntry<V>> & Serializable)
            (c1, c2) -> c1.getKey() -  c2.getKey();
  }

  /**
   * Returns a comparator that compares {@link Entry} in natural order on value.
   *
   * <p>The returned comparator is serializable and throws {@link
   * NullPointerException} when comparing an entry with null values.
   *
   * @param <K> the type of the map keys
   * @param <V> the {@link Comparable} type of the map values
   * @return a comparator that compares {@link Entry} in natural order on value.
   * @see Comparable
   * @since 1.8
   */
  public static <K, V extends Comparable<? super V>> Comparator<PersistentMapIntEntry<V>> comparingByValue() {
    return (Comparator<PersistentMapIntEntry<V>> & Serializable)
            (c1, c2) -> c1.getValue().compareTo(c2.getValue());
  }

  /**
   * Returns a comparator that compares {@link Entry} by key using the given
   * {@link Comparator}.
   *
   * <p>The returned comparator is serializable if the specified comparator
   * is also serializable.
   *
   * @param  <V> the type of the map values
   * @param  cmp the key {@link Comparator}
   * @return a comparator that compares {@link Entry} by the key.
   * @since 1.8
   */
  public static <V> Comparator<PersistentMapIntEntry<V>> comparingByKey(IntComparator cmp) {
    Objects.requireNonNull(cmp);
    return (Comparator<PersistentMapIntEntry<V>> & Serializable)
            (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
  }

  /**
   * Returns a comparator that compares {@link Entry} by value using the given
   * {@link Comparator}.
   *
   * <p>The returned comparator is serializable if the specified comparator
   * is also serializable.
   *
   * @param  <K> the type of the map keys
   * @param  <V> the type of the map values
   * @param  cmp the value {@link Comparator}
   * @return a comparator that compares {@link Entry} by the value.
   * @since 1.8
   */
  public static <K, V> Comparator<PersistentMapIntEntry<V>> comparingByValue(Comparator<? super V> cmp) {
    Objects.requireNonNull(cmp);
    return (Comparator<PersistentMapIntEntry<V>> & Serializable)
            (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
  }
}
