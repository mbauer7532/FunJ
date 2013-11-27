/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.Numeric;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;
import static org.junit.Assert.*;

/**
 *
 * @author Neo
 */
public class PersistentMapTest {
  private static <K extends Comparable<K>, V> void checkMapProperties(final PersistentMap<K, V, ?> t) {
    final Pair<Boolean, String> res = t.verifyMapProperties();

    if (! res.mx1) {
      assertTrue(res.mx2, false);
    }
  }
   
  public static void verifyMapPropertiesForSimpleCases(final PersistentMapFactory mapFactory) {
    System.out.printf("verifyBasicMapProperties(%s)\n", mapFactory.getMapName());
    {
      final PersistentMap<String, Integer, ?> t = mapFactory.empty();
      checkMapProperties(t);
    }
    {
      final PersistentMap<String, Integer, ?> t = mapFactory.singleton("hi", 2);
      checkMapProperties(t);
    }
    {
      final PersistentMap<String, Integer, ?> t = mapFactory.singleton("hi", 2).insert((x, y) -> x, "there", 23);
      checkMapProperties(t);
    }
    {
      final PersistentMap<String, Integer, ?> t = mapFactory.singleton("hi", 2).insert("there", 23);
      checkMapProperties(t);
    }
    {
      PersistentMap<Integer, Integer, ?> t = mapFactory.empty();

      final int N = 10;
      ArrayList<PersistentMap<Integer, Integer, ?>> a = new ArrayList<>();
      for (int i = 0; i != N; ++i) {
        a.add(t);
        t = t.insert(i, i);
        assertEquals(i + 1, t.size());
        checkMapProperties(t);
      }
      a.add(t);
 
      for (int i = 0; i != a.size(); ++i) {
        final PersistentMap<Integer, Integer, ?> q = a.get(i);

        assertEquals(i, q.size());
        checkMapProperties(t);
        IntStream.range(0, i).forEach(n -> assertTrue(q.containsKey(n)));
      }

      for (int i = 0; i != N; ++i) {
        t = t.remove(i);
        
        assertEquals(N - i - 1, t.size());
        checkMapProperties(t);
        // After each removal also check that none of the stored trees has it's size changed.
        IntStream.range(0, a.size()).forEach(n -> assertEquals(n, a.get(n).size()));
      }
    }
  }

  public static void verifyMapPropertiesRandomSample(final PersistentMapFactory mapFactory) {
    System.out.printf("verifyMapPropertiesRandomSample(%s)\n", mapFactory.getMapName());

    final long seed = 125332;
    final Random rng = new Random(seed);

    final int numIters = 25;
    final int low = 1, high = 600;
    final int size = high;
    IntStream.range(0, numIters).forEach(x -> {
      final int[] perm1 = Numeric.randomPermuation(low, high, size, rng);
      assertEquals(size, perm1.length);

      PersistentMap<Integer, Integer, ?> t = mapFactory.empty();
      for (int i = 0; i != size; ++i) {
        assertEquals(i, t.size());
        checkMapProperties(t);
        t = t.insert(perm1[i], perm1[i]);
      }
      assertEquals(size, t.size());
      checkMapProperties(t);

      final int[] perm2 = Numeric.randomPermuation(low, high, size, rng);

      for (int i = 0; i != size; ++i) {
        assertTrue(t.containsKey(perm2[i]));
        t = t.remove(perm2[i]);
        assertTrue(! t.containsKey(perm2[i]));
        assertEquals(size - i - 1, t.size());
        checkMapProperties(t);
      }
      assertTrue(t.isEmpty());
    });
  }
}
