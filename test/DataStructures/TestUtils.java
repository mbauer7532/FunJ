/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Neo
 */
public class TestUtils {

  private static final Class<?>[] sMapClasses = {
    RedBlackTreeModule.class,
    AvlTreeModule.class,
    BrotherTreeModule.class
  };

  static <K extends Comparable<K>, V> void performTest(final Consumer<Class<?>> testFn) {
    Arrays.stream(sMapClasses).forEach(testFn);
  }

  @SuppressWarnings("unchecked")
  static <K extends Comparable<K>, V, M extends PersistentMap<K, V, M>>
  PersistentMapFactory<K, V, M> makeFactory(final Class<?> c) {

    final Method m;
    try {
      m = c.getMethod("makeFactory");
    }
    catch (NoSuchMethodException | SecurityException ex) {
      Logger.getLogger(PersistentMapTest.class.getName()).log(Level.SEVERE, null, ex);
      throw new AssertionError("Should never happen: " + ex);
    }

    final PersistentMapFactory<K, V, M> factory;
    try {
      factory = (PersistentMapFactory<K, V, M>) m.invoke(null);
    }
    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
      Logger.getLogger(PersistentMapTest.class.getName()).log(Level.SEVERE, null, ex);
      throw new AssertionError("Should never happen: " + ex);
    }

    return factory;
  }

  static PersistentMap<Integer, Integer, ?> makeMap(
          final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory,
          final int[] perm,
          final IntFunction<TuplesModule.Pair<Integer, Integer>> f) {
    return mapFactory.fromStream(Arrays.stream(perm).mapToObj(f));
  }

  static PersistentMap<Integer, Integer, ?> makeMapfromIncreasing(
          final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory,
          final int[] perm,
          final IntFunction<TuplesModule.Pair<Integer, Integer>> f) {
    return mapFactory.fromStrictlyIncreasingStream(Arrays.stream(perm).mapToObj(f));
  }

  static <K extends Comparable<K>, V> void checkMapProperties(final PersistentMap<K, V, ?> t) {
    final TuplesModule.Pair<Boolean, String> res = t.verifyMapProperties();

    if (! res.mx1) {
//      GraphModule.showGraph(t);
//      GraphModule.waitTime(1000);
      assertTrue(res.mx2, false);
    }
  }
}
