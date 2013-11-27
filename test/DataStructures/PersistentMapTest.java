/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.Numeric;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Neo
 */
public class PersistentMapTest {
  private static final List<PersistentMapFactory> mMapFactories
          = Arrays.asList(new PersistentMapFactory[]
                               {
                                 RedBlackTreeModule.makeFactory(),
                                 AvlTreeModule.makeFactory(),
                                // BrotherTreeModule.makeFactory()
                               });
  
  public PersistentMapTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  private static void performTest(final Consumer<PersistentMapFactory> testFn) {
    mMapFactories.forEach(testFn);
  }

  private static <K extends Comparable<K>, V> void checkMapProperties(final PersistentMap<K, V, ?> t) {
    final Pair<Boolean, String> res = t.verifyMapProperties();

    if (! res.mx1) {
//      GraphModule.showGraph(t);
//      GraphModule.waitTime(1000);
      assertTrue(res.mx2, false);
    }
  }

  private static void verifyMapPropertiesForSimpleCasesImpl(final PersistentMapFactory mapFactory) {
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

  @Test
  public void verifyMapPropertiesForSimpleCases() {
    performTest(PersistentMapTest::verifyMapPropertiesForSimpleCasesImpl);
  }

  private static void verifyMapPropertiesRandomSampleImpl(final PersistentMapFactory mapFactory) {
    System.out.printf("verifyMapPropertiesRandomSample(%s)\n", mapFactory.getMapName());

    PersistentMap<Integer, Integer, RedBlackTreeModule.Tree<Integer, Integer>> q = RedBlackTreeModule.empty();
    
    final long seed = 1253273;
    final Random rng = new Random(seed);

    final int numIters = 40;
    final int low = 1, high = 800;
    final int size = high / 2;

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

      final int[] perm2 = Numeric.randomPermuation(0, size - 1, size, rng);

      for (int i = 0; i != size; ++i) {
        final int n = perm1[perm2[i]];
        assertTrue(t.containsKey(n));
        t = t.remove(n);
        assertTrue(! t.containsKey(n));
        assertEquals(size - i - 1, t.size());
        checkMapProperties(t);
      }
      assertTrue(t.isEmpty());
    });
  }

  @Test
  public void verifyMapPropertiesRandomSample() {
    performTest(PersistentMapTest::verifyMapPropertiesRandomSampleImpl);
  }
}
