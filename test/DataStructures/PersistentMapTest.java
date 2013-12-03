/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.Numeric;
import Utils.Ref;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
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
  public PersistentMapTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  private static void testMapPropertiesRandomSampleImpl(final Class<?> c) {
    System.out.printf("verifyMapPropertiesRandomSample(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);
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
        TestUtils.checkMapProperties(t);
        final Integer n = perm1[i];
        t = t.insert(n, n);
      }
      assertEquals(size, t.size());
      TestUtils.checkMapProperties(t);
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
        TestUtils.checkMapProperties(t);
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
  public void testMapPropertiesRandomSample() {
    TestUtils.performTest(PersistentMapTest::testMapPropertiesRandomSampleImpl);
  }

  private static void testGetOrElseImpl(final Class<?> c) {
    System.out.printf("GetOrElse(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);

    final PersistentMap<Integer, Integer, ?> m = mapFactory.singleton(1, 2);

    final Optional<Integer> k0 = m.get(1);
    final Optional<Integer> k1 = m.get(2);
    
    assertTrue(k0.isPresent());
    assertTrue(k0.get().equals(2));
    assertFalse(k1.isPresent());
    
    final Integer k2 = m.getOrElse(1, 3);
    final Integer k3 = m.getOrElse(2, 3);
    
    assertEquals(k2, new Integer(2));
    assertEquals(k3, new Integer(3));
    
    final Ref<Boolean> ref = new Ref<>();

    final Integer k4 = m.getOrElseSupplier(1, () -> { ref.r = Boolean.TRUE; return 3; });
    assertTrue(ref.r == null);
    assertEquals(k4, new Integer(2));
    final Integer k5 = m.getOrElseSupplier(2, () -> { ref.r = Boolean.TRUE; return 3; });
    assertTrue(ref.r != null);
    assertTrue(ref.r);
    assertEquals(k5, new Integer(3));
  }

  @Test
  public void testGetOrElse() {
    TestUtils.performTest(PersistentMapTest::testGetOrElseImpl);
  }

  private static void verifyMapPropertiesMonotoneInputsImpl(final Class<?> c) {
    System.out.printf("verifyMapPropertiesMonotoneInputs(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);
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

    TestUtils.checkMapProperties(t0);
    TestUtils.checkMapProperties(t1);

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
        
        TestUtils.checkMapProperties(t0);
        TestUtils.checkMapProperties(t1);
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
        
        TestUtils.checkMapProperties(t0);
        TestUtils.checkMapProperties(t1);
      }
      assertTrue(t0.isEmpty());
      assertTrue(t1.isEmpty());
    }
/*
    System.out.println("Worst case height: " + worstCaseHeight);
    System.out.println("Actual height (increasing): " + h0);
    System.out.println("Actual height (decreasing): " + h1);
*/
  }

  @Test
  public void verifyMapPropertiesMonotoneInputs() {
    TestUtils.performTest(PersistentMapTest::verifyMapPropertiesMonotoneInputsImpl);
  }

  private static void getBenchmarkImpl(final Class<?> c) {
    // System.out.printf("getBenchmarkImpl(%s)\n", c.getName());

    final long seed = 12532731;
    final Random rng = new Random(seed);

    final int numIters = 200;
    final int low = 1, high = 2*32*32*32;
    final int size = high/2;

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);

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
//    System.out.println(" RB  Avl  Bro");
//    TestUtils.performTest(PersistentMapTest::getBenchmarkImpl);
//    System.out.println();
//    TestUtils.performTest(PersistentMapTest::getBenchmarkImpl);
//    System.out.println();
//    TestUtils.performTest(PersistentMapTest::getBenchmarkImpl);
//    System.out.println();
//    TestUtils.performTest(PersistentMapTest::getBenchmarkImpl);
//    System.out.println();
//    TestUtils.performTest(PersistentMapTest::getBenchmarkImpl);
//    System.out.println();
//    TestUtils.performTest(PersistentMapTest::getBenchmarkImpl);
//    System.out.println();
  }

  private static void testFilterAndFilteriImpl(final Class<?> c) {
    System.out.printf("FilterAndFilteri(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);
    {
      final int N = 40;
      final int halfN = N / 2;
      final PersistentMap<Integer, Integer, ?> t0 = mapFactory.fromStream(
              IntStream.range(0, N)
                      .mapToObj(i -> Pair.create(i, i)));
      final PersistentMap<Integer, Integer, ?> t1 = t0.filteri((k, v) -> (k & 1) == 1);

      TestUtils.checkMapProperties(t0);
      TestUtils.checkMapProperties(t1);

      assertEquals(N, t0.size());
      assertEquals(halfN, t1.size());
    
      IntStream.range(0, N).forEach(n -> {
        if ((n & 1) == 0) {
          assertTrue(t0.containsKey(n) && ! t1.containsKey(n));
        }
        else {
          assertTrue(t0.containsKey(n) && t1.containsKey(n));
        }
      });
    }
    {
      final PersistentMap<Integer, Integer, ?> pm0 = mapFactory.singleton(10, 20);
      final PersistentMap<Integer, Integer, ?> pm1 = pm0.filter(v -> v != 20);
      final PersistentMap<Integer, Integer, ?> pm2 = pm0.filteri((k, v) -> k + v != 30);

      assertTrue(! pm0.isEmpty());
      assertTrue(pm1.isEmpty());
      assertTrue(pm2.isEmpty());
    }
  }

  @Test
  public void testFilterAndFilteri() {
    TestUtils.performTest(PersistentMapTest::testFilterAndFilteriImpl);
  }

  private static <M extends PersistentMap<Integer, Integer, M>> void testPartitionAndPartitioniImpl(final Class<?> c) {
    System.out.printf("PartitionAndPartitioni(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);
    {
      final int N = 40;
      final int halfN = N / 2;
      final PersistentMap<Integer, Integer, ?> t0 = mapFactory.fromStream(
              IntStream.range(0, N)
                       .mapToObj(i -> Pair.create(i, i)));

      final Pair<? extends PersistentMap<Integer, Integer, ?>, ? extends PersistentMap<Integer, Integer, ?>> p = t0.partitioni((k, v) -> (k & 1) == 1);
      final PersistentMap<Integer, Integer, ?> t1 = p.mx1, t2 = p.mx2;

      TestUtils.checkMapProperties(t0);
      TestUtils.checkMapProperties(t1);
      TestUtils.checkMapProperties(t2);

      assertEquals(N, t0.size());
      assertEquals(halfN, t1.size());
      assertEquals(halfN, t2.size());

      IntStream.range(0, N).forEach(n -> {
        if ((n & 1) == 1) {
          assertTrue(t0.containsKey(n) && t1.containsKey(n) && ! t2.containsKey(n));
        }
        else {
          assertTrue(t0.containsKey(n) && ! t1.containsKey(n) && t2.containsKey(n));
        }
      });
    }
    {
      final PersistentMap<Integer, Integer, ?> pm = mapFactory.singleton(10, 20);
      final Pair<? extends PersistentMap<Integer, Integer, ?>, ? extends PersistentMap<Integer, Integer, ?>> ms = pm.partition(v -> v != 20);
      final PersistentMap<Integer, Integer, ?> pm1 = ms.mx1, pm2 = ms.mx2;

      assertTrue(! pm.isEmpty());
      assertTrue(pm1.isEmpty());
      assertTrue(! pm2.isEmpty());
    }
  }

  @Test
  public void testPartitionAndPartitioni() {
    TestUtils.performTest(PersistentMapTest::testPartitionAndPartitioniImpl);
  }

  private static void testMinMaxElementImpl(final Class<?> c) {
    System.out.printf("MinMaxElement(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);
    {
      final int N = 40;
      final PersistentMap<Integer, Integer, ?> t = mapFactory.fromStream(
              IntStream.rangeClosed(-N, N)
                      .mapToObj(i -> Pair.create(i, i)));

      assertEquals(Pair.create(-N, -N), t.minElementPair().get());
      assertEquals(Pair.create(N, N), t.maxElementPair().get());
    }
    { // And one assymetric one.
      final int N = 40;
      final PersistentMap<Integer, Integer, ?> t = mapFactory.fromStream(
              IntStream.rangeClosed(-N, 2 * N)
                      .mapToObj(i -> Pair.create(i, i)));

      assertEquals(Pair.create(-N, -N), t.minElementPair().get());
      assertEquals(Pair.create(2 * N, 2 * N), t.maxElementPair().get());
    }

    final PersistentMap<Integer, Integer, ?> t0 = mapFactory.empty();
    try {
      final Optional<Pair<Integer, Integer>> p = t0.minElementPair();
      fail("Internal error.  The empty tree must not have a minimum element.");
    }
    catch (AssertionError e) {}

    try {
      final Optional<Pair<Integer, Integer>> p = t0.maxElementPair();
      fail("Internal error.  The empty tree must not have a maximum element.");
    }
    catch (AssertionError e) {}
  }

  @Test
  public void testMinMaxElement() {
    TestUtils.performTest(PersistentMapTest::testMinMaxElementImpl);
  }

  private static <M extends PersistentMap<Integer, Integer, M>>
  void mergeAux(final int i0, final int i1, final int j0, final int j1, final PersistentMapFactory<Integer, Integer, M> mapFactory) {
    final PersistentMap<Integer, Integer, M> t0 = mapFactory.fromStrictlyIncreasingStream(
            IntStream.rangeClosed(i0, i1)
                     .mapToObj(i -> Pair.create(i, 7 * i)));

    final PersistentMap<Integer, Integer, M> t1 = mapFactory.fromStrictlyIncreasingStream(
            IntStream.rangeClosed(j0, j1)
                     .mapToObj(i -> Pair.create(i, 13 * i)));

    final BiFunction<Integer, Integer, Integer> f = (x1, x2) -> x1 - x2;
    final PersistentMap<Integer, Integer, M> tres = t0.merge(f, t1);

    final int numElems0 = i1 - i0 + 1;
    final int numElems1 = j1 - j0 + 1;
    final int numElemsMerged;
    if (j1 < i0 || i1 < j0) {
      numElemsMerged = numElems0 + numElems1;
    }
    else {
      numElemsMerged = Math.max(i1, j1) - Math.min(i0, j0) + 1;
    }

    assertEquals(numElems0, t0.size());
    assertEquals(numElems1, t1.size());
    assertEquals(numElemsMerged, tres.size());

    TestUtils.checkMapProperties(tres);

    final ArrayList<Pair<Integer, Integer>> kv0 = t0.keyValuePairs();
    final ArrayList<Pair<Integer, Integer>> kv1 = t1.keyValuePairs();

    kv0.stream().forEach((final Pair<Integer, Integer> p) -> {
      final Optional<Integer> p1 = t1.get(p.mx1);

      final Integer expectedRes = p1.isPresent() ? f.apply(p.mx2, p1.get()) : p.mx2;
      assertEquals(expectedRes, tres.get(p.mx1).get());
    });

    kv1.stream().forEach((final Pair<Integer, Integer> p) -> {
      final Optional<Integer> p0 = t0.get(p.mx1);

      final Integer expectedRes = p0.isPresent() ? f.apply(p0.get(), p.mx2) : p.mx2;
      assertEquals(expectedRes, tres.get(p.mx1).get());
    });
  }

  private static void testMerge1Impl(final Class<?> c) {
    System.out.printf("Merge1(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);
    final int N = 6;
    // Assume the two ranges are r1, r2
    // Case 1: r1 < r2
    mergeAux(-N, N, -N - 3 * N, N - 3 * N, mapFactory);
    // Case 2: r1 <= r2
    mergeAux(-N, N, -N - N / 2 , N - N / 2, mapFactory);
    // Case 3a: r2 subset r1
    mergeAux(-N, N, -N + 1, N - 1, mapFactory);
    // Case 3b: r1 subset r2
    mergeAux(-N - 1, N + 1, -N, N, mapFactory);
    // Case 4: r1 >= r2
    mergeAux(-N + N / 2, N + N / 2, -N , N, mapFactory);
    // Case 4: r1 > r2
    mergeAux(-N + 3 * N, N + 3 * N, -N , N, mapFactory);
  }

  @Test
  public void testMerge1() {
    TestUtils.performTest(PersistentMapTest::testMerge1Impl);
  }

  private static void testMerge2Impl(final Class<?> c) {
    System.out.printf("Merge2(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);
    final long seed = 125332;
    final Random rng = new Random(seed);

    final int N = 1000;
    final int low = -50, high = 60;
    IntStream.range(0, N).forEach(i -> {
      int i0 = Numeric.randomInt(low, high, rng);
      int i1 = Numeric.randomInt(low, high, rng);
      if (i0 > i1) {
        final int z = i0;
        i0 = i1;
        i1 = z;
      }

      int j0 = Numeric.randomInt(low, high, rng);
      int j1 = Numeric.randomInt(low, high, rng);
      if (j0 > j1) {
        final int z = j0;
        j0 = j1;
        j1 = z;
      }

      mergeAux(i0, i1, j0, j1, mapFactory);
    });
  }
  
  @Test
  public void testMerge2() {
    TestUtils.performTest(PersistentMapTest::testMerge2Impl);
  }

  private static void testContainsValueImpl(final Class<?> c) {
    System.out.printf("ContainsValue(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);

    final int low = 1, high = 40;
    final int gap = high - low;

    final PersistentMap<Integer, Integer, ?> t = TestUtils.makeMapfromIncreasing(mapFactory, IntStream.range(low, high).toArray(), i -> Pair.create(i, 2 * i));

    IntStream.range(low, high).forEach(n -> { assertTrue(t.containsValue(2 * n)); });
    IntStream.range(low, high).forEach(n -> { assertFalse(t.containsValue(-2 * n)); });
    IntStream.range(high, high + gap).forEach(n -> { assertFalse(t.containsValue(-2 * n)); });
  }

  @Test
  public void testContainsValue() {
    TestUtils.performTest(PersistentMapTest::testContainsValueImpl);
  }

  private static void testLowerPairImpl(final Class<?> c) {
    System.out.printf("LowerPair(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);

    final int N = 40;
    final PersistentMap<Integer, Integer, ?> t = mapFactory.fromStream(
            IntStream.range(0, N)
                     .mapToObj(i -> Pair.create(2 * i, 2 * i)));

    IntStream.range(1, 2 * N - 2).forEach(n -> {
      assertEquals(n - 1 - (1 - (n & 1)), t.lowerKey(n).get().intValue());
    });

    assertEquals(Optional.empty(), t.lowerKey(0));
    assertEquals(Optional.empty(), t.lowerKey(-1));

    assertEquals(2 * N - 6, t.lowerKey(2 * N - 4).get().intValue());
    assertEquals(2 * N - 4, t.lowerKey(2 * N - 3).get().intValue());
    assertEquals(2 * N - 4, t.lowerKey(2 * N - 2).get().intValue());
    assertEquals(2 * N - 2, t.lowerKey(2 * N - 1).get().intValue());
    assertEquals(2 * N - 2, t.lowerKey(2 * N).get().intValue());
  }

  @Test
  public void testLowerPair() {
    TestUtils.performTest(PersistentMapTest::testLowerPairImpl);
  }
  
  private static void testHigherPairImpl(final Class<?> c) {
    System.out.printf("HigherPair(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);

    final int N = 40;
    final PersistentMap<Integer, Integer, ?> t = mapFactory.fromStream(
            IntStream.range(0, N)
                     .mapToObj(i -> Pair.create(2 * i, 2 * i)));

    IntStream.range(1, 2 * N - 2).forEach(n -> {
      assertEquals(n + 1 + (1 - (n & 1)), t.higherKey(n).get().intValue());
    });

    assertEquals(2, t.higherKey(0).get().intValue());
    assertEquals(0, t.higherKey(-1).get().intValue());

    assertEquals(2 * N - 2, t.higherKey(2 * N - 4).get().intValue());
    assertEquals(2 * N - 2, t.higherKey(2 * N - 3).get().intValue());

    final Optional<Integer> e = Optional.empty();
    assertEquals(e, t.higherKey(2 * N - 2));
    assertEquals(e, t.higherKey(2 * N - 1));
    assertEquals(e, t.higherKey(2 * N - 0));
  }

  @Test
  public void testHigherPair() {
    TestUtils.performTest(PersistentMapTest::testHigherPairImpl);
  }

  private static <K, V> Optional<Pair<K, V>> toPair(final Map.Entry<K, V> e) {
    return e == null ? Optional.empty() : Optional.of(Pair.create(e.getKey(), e.getValue()));
  }

  private static void testLowerHigherPairRandomInputsImpl(final Class<?> c) {
    System.out.printf("LowerHigherPairRandomInputs(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);

    final long seed = 1253327;
    final Random rng = new Random(seed);

    final int N = 500;
    final int low = -50, high = 600;
    final int size = 180;

    IntStream.range(0, N).forEach(x -> {
      final int[] perm = Numeric.randomPermuation(low, high, size, rng);

      final ArrayList<Pair<Integer, Integer>> v =
              Arrays.stream(perm)
                    .mapToObj(i -> Pair.create(i, i))
                    .collect(Collectors.toCollection(ArrayList::new));

      final PersistentMap<Integer, Integer, ?> pm = mapFactory.fromArray(v);
      final TreeMap<Integer, Integer> tm = new TreeMap<>();

      v.stream().forEach(p -> tm.put(p.mx1, p.mx2));

      Numeric.randomSet(low - 200, high + 200, 500, rng).forEach(n -> {
        final Optional<Pair<Integer, Integer>>
                tmlowerExpected  = toPair(tm.lowerEntry(n)),
                pmLower          = pm.lowerPair(n),
                tmHigherExpected = toPair(tm.higherEntry(n)),
                pmHigher         = pm.higherPair(n);

        assertEquals(tmlowerExpected, pmLower);
        assertEquals(tmHigherExpected, pmHigher);
      });
    });
  }

  @Test
  public void testLowerHigherPairRandomInputs() {
    TestUtils.performTest(PersistentMapTest::testLowerHigherPairRandomInputsImpl);
  }

  private static void testMapPartialPartialiImpl(final Class<?> c) {
    System.out.printf("MapPartialPartiali(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);

    final long seed = 12332713;
    final Random rng = new Random(seed);

    final int N = 40;
    final int low = -50, high = 600;
    final int size = 180;
    
    IntStream.range(0, N).forEach(x -> {
      final int[] perm = Numeric.randomPermuation(low, high, size, rng);

      final PersistentMap<Integer, Integer, ?> t = TestUtils.makeMap(mapFactory, perm, i -> Pair.create(i, i));

      final PersistentMap<Integer, Integer, ?> tEven = t.mapPartial(n -> Optional.ofNullable((n & 1) == 0 ? n : null));
      final PersistentMap<Integer, Integer, ?> tOdd  = t.mapPartial(n -> Optional.ofNullable((n & 1) == 1 ? n : null));

      final int tSize = t.size();
      final int tEvenSize = tEven.size();
      final int tOddSize = tOdd.size();

      assertEquals(size, tSize);
      assertEquals(tSize, tEvenSize + tOddSize);

      Arrays.stream(perm).forEach(n -> {
        assertTrue(t.containsKey(n));
        if ((n & 1) == 0) {
          assertTrue(tEven.containsKey(n) && ! tOdd.containsKey(n));
        }
        else {
          assertTrue(! tEven.containsKey(n) && tOdd.containsKey(n));
        }
      });
    });
  }

  @Test
  public void testMapPartialPartiali() {
    TestUtils.performTest(PersistentMapTest::testMapPartialPartialiImpl);
  }

  private static void testMinKeyMaxKeyImpl(final Class<?> c) {
    System.out.printf("MinKeyMaxKey(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);

    final long seed = 12332713;
    final Random rng = new Random(seed);

    final int N = 100;
    final int low = -50, high = 600;
    final int size = 180;

    IntStream.range(0, N).forEach(x -> {
      final int[] perm = Numeric.randomPermuation(low, high, size, rng);

      final PersistentMap<Integer, Integer, ?> t = TestUtils.makeMap(mapFactory, perm, i -> Pair.create(i, i));

      final int minExpected = Arrays.stream(perm).min().getAsInt();
      final int maxExpected = Arrays.stream(perm).max().getAsInt();

      final int min = t.minKey().get().intValue();
      final int max = t.maxKey().get().intValue();

      assertEquals(minExpected, min);
      assertEquals(maxExpected, max);
    });
  }

  @Test
  public void testMinKeyMaxKey() {
    TestUtils.performTest(PersistentMapTest::testMinKeyMaxKeyImpl);
  }

  private static void testMinElementPairMaxElementPairImpl(final Class<?> c) {
    System.out.printf("MinElementPairMaxElementPair(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);

    final long seed = 12332713;
    final Random rng = new Random(seed);

    final int N = 100;
    final int low = -50, high = 600;
    final int size = 180;

    IntStream.range(0, N).forEach(x -> {
      final int[] perm = Numeric.randomPermuation(low, high, size, rng);

      final PersistentMap<Integer, Integer, ?> t = TestUtils.makeMap(mapFactory, perm, i -> Pair.create(i, 2 * i));

      final int minExpected = Arrays.stream(perm).min().getAsInt();
      final int maxExpected = Arrays.stream(perm).max().getAsInt();

      final Pair<Integer, Integer> min = t.minElementPair().get();
      final Pair<Integer, Integer> max = t.maxElementPair().get();

      assertEquals(minExpected, min.mx1.intValue());
      assertEquals(2 * minExpected, min.mx2.intValue());

      assertEquals(maxExpected, max.mx1.intValue());
      assertEquals(2 * maxExpected, max.mx2.intValue());
    });
  }

  @Test
  public void testMinElementPairMaxElementPair() {
    TestUtils.performTest(PersistentMapTest::testMinElementPairMaxElementPairImpl);
  }

  private static void testRemoveImpl(final Class<?> c) {
    System.out.printf("Remove(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);

    final PersistentMap<Integer, Integer, ?> t1 = mapFactory.singleton(10, 10);
    assertFalse(t1.isEmpty());
    TestUtils.checkMapProperties(t1);

    final PersistentMap<Integer, Integer, ?> t2 = t1.remove(10);
    assertFalse(t1.isEmpty());
    assertTrue(t2.isEmpty());
    TestUtils.checkMapProperties(t2);

    final PersistentMap<Integer, Integer,?> t3 = t1.remove(11);
    assertFalse(t1.isEmpty());
    assertTrue(t2.isEmpty());
    assertFalse(t3.isEmpty());
    TestUtils.checkMapProperties(t3);

    assertTrue(t1.containsKey(10));
    assertFalse(t2.containsKey(10));
    assertTrue(t3.containsKey(10));

    assertTrue(t1 == t3);
    assertTrue(t1 != t2);
  }

  @Test
  public void testRemove() {
    TestUtils.performTest(PersistentMapTest::testRemoveImpl);
  }

  private static void testTreeHeightOfMapImpl(final Class<?> c) {
    System.out.printf("TreeHeightOfMap(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);
    // Small trees
    {
      final PersistentMap<Integer, Integer, ?> t0 = mapFactory.empty();
      final PersistentMap<Integer, Integer, ?> t1 = mapFactory.singleton(1, 1);
      final PersistentMap<Integer, Integer, ?> t2 = t1.insert(2, 2);

      assertEquals(0, t0.size());
      assertEquals(1, t1.size());
      assertEquals(2, t2.size());

      assertEquals(0, t0.height());
      assertEquals(1, t1.height());
      assertEquals(2, t2.height());
    }
    // Let's try some large trees
    {
      final long seed = 125332;
      final Random rng = new Random(seed);

      final int numIters = 20;
      final int low = -3000, high = 2000;
      final int size = 4000;

      final int[] perm = Numeric.randomPermuation(low, high, size, rng);
      IntStream.range(0, numIters).forEach(x -> {
        final PersistentMap<Integer, Integer, ?> t =
                mapFactory.fromStream(
                        Arrays.stream(perm).mapToObj(i -> Pair.create(i, i)));
        // Checking tree validity also checks its height.
        TestUtils.checkMapProperties(t);

        assertTrue(Arrays.stream(perm).allMatch(t::containsKey));
      });
    }
  }

  @Test
  public void testTreeHeightOfMap() {
    TestUtils.performTest(PersistentMapTest::testTreeHeightOfMapImpl);
  }

  private static void testFoldliFoldriImpl(final Class<?> c) {
    System.out.printf("FoldliFoldri(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);
    
    {
      final int N = 40;
      final PersistentMap<Integer, Integer, ?> m = TestUtils.makeMapfromIncreasing(mapFactory, IntStream.range(0, N).toArray(), n -> Pair.create(n, 2 * n));

      final Integer resFoldl  = m.foldl((n, acc) -> n + acc, 0);
      final Integer resFoldli = m.foldli((idx, n, acc) -> idx + n + acc, 0);

      final Integer resFoldr  = m.foldr((n, acc) -> n + acc, 0);
      final Integer resFoldri = m.foldri((idx, n, acc) -> idx + n + acc, 0);

      assertEquals(resFoldl,  new Integer((0 + 2 * N - 2) * N / 2));  // Sum of arithmetic progresssion (first + last) * n / 2
      assertEquals(resFoldli, new Integer((0 + 3 * N - 3) * N / 2));

      assertEquals(resFoldr,  new Integer((0 + 2 * N - 2) * N / 2));  // Sum of arithmetic progresssion (first + last) * n / 2
      assertEquals(resFoldri, new Integer((0 + 3 * N - 3) * N / 2));
    }
    {
      final int N = 40;
      final PersistentMap<Integer, Integer, ?> m = TestUtils.makeMapfromIncreasing(mapFactory, IntStream.rangeClosed(1, N).toArray(), n -> Pair.create(n, 2 * n));

      final Optional<Integer> resFoldl  = m.foldl((n, acc) -> acc.isPresent() ? acc : Optional.of(n), Optional.empty());
      final Optional<Integer> resFoldli = m.foldli((idx, n, acc) -> acc.isPresent() ? acc : Optional.of(idx + n), Optional.empty());

      final Optional<Integer> resFoldr  = m.foldr((n, acc) -> acc.isPresent() ? acc : Optional.of(n), Optional.empty());
      final Optional<Integer> resFoldri = m.foldri((idx, n, acc) -> acc.isPresent() ? acc : Optional.of(idx + n), Optional.empty());

      assertEquals(resFoldl,  Optional.of(2 * 1));
      assertEquals(resFoldli, Optional.of(1 + 2 * 1));

      assertEquals(resFoldr,  Optional.of(2 * N));
      assertEquals(resFoldri, Optional.of(N + 2 * N));
    }
}

  @Test
  public void testFoldliFoldri() {
    TestUtils.performTest(PersistentMapTest::testFoldliFoldriImpl);
  }
}
