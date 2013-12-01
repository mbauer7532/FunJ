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
import java.util.concurrent.TimeUnit;
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
                                 BrotherTreeModule.makeFactory()
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
        final PersistentMap<Integer, Integer, ?> m = a.get(i);

        assertEquals(i, m.size());
        checkMapProperties(t);
        IntStream.range(0, i).forEach(n -> assertTrue(m.containsKey(n)));
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

    final long seed = 12532731;
    final Random rng = new Random(seed);

    final int numIters = 10;
    final int low = 1, high = 2 * 1024;
    final int size = high/2;

    int[] s = new int[3];
    s[1] = Integer.MIN_VALUE;
    s[2] = Integer.MAX_VALUE;

    IntStream.range(0, numIters).forEach(x -> {
      final int[] perm1 = Numeric.randomPermuation(low, high, size, rng);
      assertEquals(size, perm1.length);
      PersistentMap<Integer, Integer, ?> t = mapFactory.empty();
      for (int i = 0; i != size; ++i) {
        assertEquals(i, t.size());
        checkMapProperties(t);
        final Integer n = perm1[i];
        t = t.insert(n, n);
      }
      assertEquals(size, t.size());
      checkMapProperties(t);
      final int h = t.height();
      s[0] += h;
      s[1] = Math.max(s[1], h);
      s[2] = Math.min(s[2], h);

      final int[] perm2 = Numeric.randomPermuation(0, size - 1, size, rng);

      for (int i = 0; i != size; ++i) {
        final int n = perm1[perm2[i]];
        assertTrue(t.containsKey(n));
        t = t.remove(n);
        assertTrue(! t.containsKey(n));
        assertEquals(size - i - 1, t.size());
        checkMapProperties(t);
      }
    });

    final double worstCaseHeight = 2.0 * Numeric.log((double)size, 2.0); // aproximately -- if larger I would worry.

    final double averageHeight = s[0] / (double) numIters;
    final double maxHeight = s[1];
    final double minHeight = s[2];

    assertTrue(averageHeight < worstCaseHeight);
    assertTrue(maxHeight < worstCaseHeight);
    assertTrue(minHeight < worstCaseHeight);
/*
    System.out.println("Worst case height: " + worstCaseHeight);
    System.out.println("Average height: " + averageHeight);
    System.out.println("Max height: " + maxHeight);
    System.out.println("Min height: " + minHeight);
*/
  }

  @Test
  public void verifyMapPropertiesRandomSample() {
    performTest(PersistentMapTest::verifyMapPropertiesRandomSampleImpl);
  }

  private static void verifyMapPropertiesMonotoneInputsImpl(final PersistentMapFactory mapFactory) {
    System.out.printf("verifyMapPropertiesMonotoneInputs(%s)\n", mapFactory.getMapName());

    final long seed = 12532731;
    final Random rng = new Random(seed);

    final int numIters = 10;
    final int low = 1, high = 2 * 1024;
    final int size = high/2;

    final int[] increasingVec = IntStream.range(0, size).toArray();
    final int[] decreasingVec = IntStream.range(0, size).map(n -> size - 1 - n).toArray();

    PersistentMap<Integer, Integer, ?> t0 = mapFactory.empty();
    PersistentMap<Integer, Integer, ?> t1 = mapFactory.empty();

    for (int i = 0; i != size; ++i) {
      final Integer n0 = increasingVec[i];
      final Integer n1 = decreasingVec[i];
      t0 = t0.insert(n0, n0);
      t1 = t1.insert(n1, n1);
    }

    assertEquals(size, t0.size());
    assertEquals(size, t1.size());

    checkMapProperties(t0);
    checkMapProperties(t1);

    final int h0 = t0.height();
    final int h1 = t1.height();

    final double worstCaseHeight = 2.0 * Numeric.log((double)size, 2.0);
    assertTrue(h0 < worstCaseHeight);
    assertTrue(h1 < worstCaseHeight);

    final PersistentMap<Integer, Integer, ?> fullMap0 = t0;
    final PersistentMap<Integer, Integer, ?> fullMap1 = t1;

    // Let's remove all the elements from the two tables checking as we go.
    { // First we remove the elements in an increasing fashion.
      for (int i = 0; i != size; ++i) {
        final int n = increasingVec[i];
        
        assertTrue(t0.containsKey(n));
        assertTrue(t1.containsKey(n));

        t0 = t0.remove(n);
        t1 = t1.remove(n);

        assertTrue(! t0.containsKey(n));
        assertTrue(! t1.containsKey(n));

        assertEquals(size - i - 1, t0.size());
        assertEquals(size - i - 1, t1.size());
        
        checkMapProperties(t0);
        checkMapProperties(t1);
      }
      assertTrue(t0.isEmpty());
      assertTrue(t1.isEmpty());
    }
    t0 = fullMap0;
    t1 = fullMap1;
    { // And now we remove them in a decreasing fashion.
      for (int i = 0; i != size; ++i) {
        final int n = increasingVec[i];
        
        assertTrue(t0.containsKey(n));
        assertTrue(t1.containsKey(n));

        t0 = t0.remove(n);
        t1 = t1.remove(n);

        assertTrue(! t0.containsKey(n));
        assertTrue(! t1.containsKey(n));

        assertEquals(size - i - 1, t0.size());
        assertEquals(size - i - 1, t1.size());
        
        checkMapProperties(t0);
        checkMapProperties(t1);
      }
      assertTrue(t0.isEmpty());
      assertTrue(t1.isEmpty());
    }

    System.out.println("Worst case height: " + worstCaseHeight);
    System.out.println("Actual height (increasing): " + h0);
    System.out.println("Actual height (decreasing): " + h1);
  }

  @Test
  public void verifyMapPropertiesMonotoneInputs() {
    performTest(PersistentMapTest::verifyMapPropertiesMonotoneInputsImpl);
  }

  private static void getBenchmarkImpl(final PersistentMapFactory mapFactory) {
    //System.out.printf("%s\n", mapFactory.getMapName());

    final long seed = 12532731;
    final Random rng = new Random(seed);

    final int numIters = 200;
    final int low = 1, high = 2*32*32*32;
    final int size = high/2;

    final int[] perm1 = Numeric.randomPermuation(low, high, size, rng);
    assertEquals(size, perm1.length);
    PersistentMap<Integer, Integer, ?> t = mapFactory.empty();
    for (int i = 0; i != size; ++i) {
      t = t.insert(perm1[i], perm1[i]);
    }

    final PersistentMap<Integer, Integer, ?> t2 = t;

    IntStream.range(0, 50).forEach(x -> {
      final int[] perm2 = Numeric.randomPermuation(0, size - 1, size, rng);

      for (int i = 0; i != size; ++i) {
        final int n = perm1[perm2[i]];
        assertTrue(t2.containsKey(n));
      }
    });

    long startTime = System.nanoTime();
    IntStream.range(0, numIters).forEach(x -> {
      final int[] perm2 = Numeric.randomPermuation(0, size - 1, size, rng);

      for (int i = 0; i != size; ++i) {
        final int n = perm1[perm2[i]];
        assertTrue(t2.containsKey(n));
      }
    });
    long stopTime = System.nanoTime();

    long d = TimeUnit.MILLISECONDS.convert((stopTime - startTime), TimeUnit.NANOSECONDS);
    System.out.printf("%d ", d);
  }
  
  @Test
  public void getBenchmark() {
    System.out.println(" RB  Avl  Bro");
    performTest(PersistentMapTest::getBenchmarkImpl);
    System.out.println();
    performTest(PersistentMapTest::getBenchmarkImpl);
    System.out.println();
    performTest(PersistentMapTest::getBenchmarkImpl);
    System.out.println();
    performTest(PersistentMapTest::getBenchmarkImpl);
    System.out.println();
    performTest(PersistentMapTest::getBenchmarkImpl);
    System.out.println();
    performTest(PersistentMapTest::getBenchmarkImpl);
    System.out.println();
  }
}
