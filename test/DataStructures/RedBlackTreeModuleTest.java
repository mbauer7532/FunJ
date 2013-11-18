/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.RedBlackTreeModule.Tree;
import DataStructures.TuplesModule.Pair;
import Utils.Numeric;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.StructureGraphic.v1.DSutils;
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
public class RedBlackTreeModuleTest {
  public RedBlackTreeModuleTest() {}
  
  @BeforeClass
  public static void setUpClass() {}
  
  @AfterClass
  public static void tearDownClass() {}
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}

  final static int sLength = 44;
  final static int sWidth = 20;

  private static <K extends Comparable<K>, V> void showGraph(final Tree<K, V> t) {
    DSutils.show(t, sLength, sWidth);

    return;
  }

  private static void waitTime(final int secs) {
    try {
      Thread.sleep(secs * 1000);
    }
    catch (InterruptedException ex) {
      Logger.getLogger(IntMapModuleTest.class.getName()).log(Level.SEVERE, null, ex);
    }

    return;
  }

  private static <K extends Comparable<K>, V> void checkRedBlackTreeProperties(final Tree<K, V> t) {
    final Pair<Boolean, String> res = RedBlackTreeModule.verifyRedBlackProperties(t);

    if (! res.mx1) {
      assertTrue(res.mx2, false);
    }
  }

  /**
   * Test of verifyRedBlackProperties method, of class RedBlackTreeModule.
   */
  @Test
  public void testVerifyRedBlackProperties() {
    System.out.println("verifyRedBlackProperties");
    {
      final Tree<String, Integer> t = RedBlackTreeModule.empty();
      checkRedBlackTreeProperties(t);
    }
    {
      final Tree<String, Integer> t = RedBlackTreeModule.singleton("hi", 2);
      checkRedBlackTreeProperties(t);
    }
    {
      final Tree<String, Integer> t = RedBlackTreeModule.singleton("hi", 2).insert((x, y) -> x, "there", 23);
      checkRedBlackTreeProperties(t);
    }
    {
      final Tree<String, Integer> t = RedBlackTreeModule.singleton("hi", 2).insert("there", 23);
      checkRedBlackTreeProperties(t);
    }
    {
      Tree<Integer, Integer> t = RedBlackTreeModule.empty();

      final int N = 10;
      ArrayList<Tree<Integer, Integer>> a = new ArrayList<>();
      for (int i = 0; i != N; ++i) {
        a.add(t);
        t = t.insert(i, i);
        assertEquals(i + 1, t.size());
        checkRedBlackTreeProperties(t);
      }
      a.add(t);
 
      for (int i = 0; i != a.size(); ++i) {
        final Tree<Integer, Integer> q = a.get(i);
        
        assertEquals(i, q.size());
        checkRedBlackTreeProperties(t);
        IntStream.range(0, i).forEach(n -> assertTrue(q.containsKey(n)));
      }

      for (int i = 0; i != N; ++i) {
        t = t.remove(i);
        
        assertEquals(N - i - 1, t.size());
        checkRedBlackTreeProperties(t);
        // After each removal also check that none of the stored trees has it's size changed.
        IntStream.range(0, a.size()).forEach(n -> assertEquals(n, a.get(n).size()));
      }
    }
  }

  @Test
  public void testVerifyRedBlackPropertiesRandomSample() {
    System.out.println("verifyRedBlackPropertiesRandomSample");

    final long seed = 125332;
    final Random rng = new Random(seed);

    final int numIters = 20;
    final int low = 1, high = 512;
    final int size = high;
    IntStream.range(0, numIters).forEach(x -> {
      final int[] perm1 = Numeric.randomPermuation(low, high, size, rng);
      assertEquals(size, perm1.length);

      Tree<Integer, Integer> t = RedBlackTreeModule.empty();
      for (int i = 0; i != size; ++i) {
        assertEquals(i, t.size());
        checkRedBlackTreeProperties(t);
        t = t.insert(perm1[i], perm1[i]);
      }
      assertEquals(size, t.size());
      checkRedBlackTreeProperties(t);

      final int[] perm2 = Numeric.randomPermuation(low, high, size, rng);

      for (int i = 0; i != size; ++i) {
        assertTrue(t.containsKey(perm2[i]));
        t = t.remove(perm2[i]);

        checkRedBlackTreeProperties(t);
        assertEquals(size - i - 1, t.size());
      }
      assertTrue(t.isEmpty());
    });
  }
  
  @Test
  public void testRedBlackTreeInvariants() {
    System.out.println("redBlackTreeInvariants");
    
    final Tree<Integer, Integer> t1 = RedBlackTreeModule.singleton(10, 10);
    assertFalse(t1.isEmpty());

    final Tree<Integer, Integer> t2 = t1.remove(10);
    assertFalse(t1.isEmpty());
    assertTrue(t2.isEmpty());

    final Tree<Integer, Integer> t3 = t1.remove(11);
    assertFalse(t1.isEmpty());
    assertTrue(t2.isEmpty());
    assertFalse(t3.isEmpty());
    
    assertTrue(t1.containsKey(10));
    assertFalse(t2.containsKey(10));
    assertTrue(t3.containsKey(10));

    assertTrue(t1 == t3);
    assertTrue(t1 != t2);
  }

  @Test
  public void testFromStrictlyIncreasingArray() {
    System.out.println("fromStrictlyIncreasingArray");

    final int N = 42;
    IntStream.rangeClosed(0, N).forEach(y -> {
      final ArrayList<Pair<Integer, Integer>> v =
              IntStream.rangeClosed(1, y)
                       .mapToObj(x -> Pair.create(x, x))
                       .collect(Collectors.toCollection(ArrayList::new));

      final Tree<Integer, Integer> resTree = RedBlackTreeModule.fromStrictlyIncreasingArray(v);

      checkRedBlackTreeProperties(resTree);
    });
  }

  @Test
  public void testFromStrictlyDecreasingArray() {
    System.out.println("fromStrictlyDecreasingArray");

    final int N = 42;
    IntStream.rangeClosed(0, N).forEach(y -> {
      final ArrayList<Pair<Integer, Integer>> v =
              IntStream.rangeClosed(1, y)
                       .map(x -> y + 1 - x)
                       .mapToObj(x -> Pair.create(x, x))
                       .collect(Collectors.toCollection(ArrayList::new));

      final Tree<Integer, Integer> resTree = RedBlackTreeModule.fromStrictlyDecreasingArray(v);

      checkRedBlackTreeProperties(resTree);
    });
  }

  @Test
  public void testRedBlackTreeDepthTreeSize() {
    System.out.println("redBlackTreeDepthTreeSize");

    // Small trees
    {
      final Tree<Integer, Integer> t0 = RedBlackTreeModule.empty();
      final Tree<Integer, Integer> t1 = RedBlackTreeModule.singleton(1, 1);
      final Tree<Integer, Integer> t2 = t1.insert(2, 2);
      final Tree<Integer, Integer> t3 = t2.insert(3, 3);

//      showGraph(t0); showGraph(t1); showGraph(t2); showGraph(t3);

      assertEquals(0, t0.size());
      assertEquals(1, t1.size());
      assertEquals(2, t2.size());
      assertEquals(3, t3.size());

      assertEquals(0, t0.height());
      assertEquals(1, t1.height());
      assertEquals(2, t2.height());
      assertEquals(2, t3.height());
    }
    // Let's try some large trees
    {
      final long seed = 125332;
      final Random rng = new Random(seed);

      final int numIters = 10;
      final int low = -3000, high = 2000;
      final int size = 2000;

      final int[] perm = Numeric.randomPermuation(low, high, size, rng);
      IntStream.range(0, numIters).forEach(x -> {
        final ArrayList<Pair<Integer, Integer>> v =
                Arrays.stream(perm)
                        .mapToObj(i -> Pair.create(i, i))
                        .collect(Collectors.toCollection(ArrayList::new));

        final Tree<Integer, Integer> t = RedBlackTreeModule.fromArray(v);

        // Checking tree validity also checks its height.
        checkRedBlackTreeProperties(t);

        assertTrue(Arrays.stream(perm).allMatch(t::containsKey));
      });
    }
  }

  @Test
  public void testFilterAndFilteri() {
    System.out.println("filterAndFilteri");

    {
      final int N = 40;
      final int halfN = N / 2;
      final Tree<Integer, Integer> t0 = RedBlackTreeModule.fromArray(
              IntStream.range(0, N)
                      .mapToObj(i -> Pair.create(i, i))
                      .collect(Collectors.toCollection(ArrayList::new)));
      final Tree<Integer, Integer> t1 = t0.filteri((k, v) -> (k & 1) == 1);

      checkRedBlackTreeProperties(t0);
      checkRedBlackTreeProperties(t1);

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
      final PersistentMap<Integer, Integer, ?> pm0 = RedBlackTreeModule.singleton(10, 20);
      final PersistentMap<Integer, Integer, ?> pm1 = pm0.filter(v -> v != 20);
      final PersistentMap<Integer, Integer, ?> pm2 = pm0.filteri((k, v) -> k + v != 30);

      assertTrue(! pm0.isEmpty());
      assertTrue(pm1.isEmpty());
      assertTrue(pm2.isEmpty());
    }
  }

  @Test
  public void testPartitionAndPartitioni() {
    System.out.println("partitionAndPartitioni");

    {
      final int N = 40;
      final int halfN = N / 2;
      final Tree<Integer, Integer> t0 = RedBlackTreeModule.fromArray(
              IntStream.range(0, N)
                       .mapToObj(i -> Pair.create(i, i))
                       .collect(Collectors.toCollection(ArrayList::new)));

      final Pair<Tree<Integer, Integer>, Tree<Integer, Integer>> p = t0.partitioni((k, v) -> (k & 1) == 1);
      final Tree<Integer, Integer> t1 = p.mx1, t2 = p.mx2;

      checkRedBlackTreeProperties(t0);
      checkRedBlackTreeProperties(t1);
      checkRedBlackTreeProperties(t2);

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
      final PersistentMap<Integer, Integer, ?> pm = RedBlackTreeModule.singleton(10, 20);
      final Pair<? extends PersistentMap<Integer, Integer, ?>, ? extends PersistentMap<Integer, Integer, ?>> ms = pm.partition(v -> v != 20);
      final PersistentMap<Integer, Integer, ?> pm1 = ms.mx1, pm2 = ms.mx2;

      assertTrue(! pm.isEmpty());
      assertTrue(pm1.isEmpty());
      assertTrue(! pm2.isEmpty());
    }
  }
  
  @Test
  public void testMinMaxElement() {
    System.out.println("minMaxElement");
    {
      final int N = 40;
      final Tree<Integer, Integer> t = RedBlackTreeModule.fromArray(
              IntStream.rangeClosed(-N, N)
                      .mapToObj(i -> Pair.create(i, i))
                      .collect(Collectors.toCollection(ArrayList::new)));

      assertEquals(Pair.create(-N, -N), t.minElementPair().get());
      assertEquals(Pair.create(N, N), t.maxElementPair().get());
    }
    { // And one assymetric one.
      final int N = 40;
      final Tree<Integer, Integer> t = RedBlackTreeModule.fromArray(
              IntStream.rangeClosed(-N, 2 * N)
                      .mapToObj(i -> Pair.create(i, i))
                      .collect(Collectors.toCollection(ArrayList::new)));

      assertEquals(Pair.create(-N, -N), t.minElementPair().get());
      assertEquals(Pair.create(2 * N, 2 * N), t.maxElementPair().get());
    }

    final Tree<Integer, Integer> t0 = RedBlackTreeModule.empty();
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

  private static void mergeAux(final int i0, final int i1, final int j0, final int j1) {
    final Tree<Integer, Integer> t0 = RedBlackTreeModule.fromStrictlyIncreasingArray(
            IntStream.rangeClosed(i0, i1)
                    .mapToObj(i -> Pair.create(i, 7 * i))
                    .collect(Collectors.toCollection(ArrayList::new)));

    final Tree<Integer, Integer> t1 = RedBlackTreeModule.fromStrictlyIncreasingArray(
            IntStream.rangeClosed(j0, j1)
                    .mapToObj(i -> Pair.create(i, 13 * i))
                    .collect(Collectors.toCollection(ArrayList::new)));

    final BiFunction<Integer, Integer, Integer> f = (x1, x2) -> x1 - x2;
    final Tree<Integer, Integer> tres = t0.merge(f, t1);

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

    checkRedBlackTreeProperties(tres);

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

  @Test
  public void testMerge1() {
    System.out.println("Merge1");

    final int N = 6;
    // Assume the two ranges are r1, r2
    // Case 1: r1 < r2
    mergeAux(-N, N, -N - 3 * N, N - 3 * N);
    // Case 2: r1 <= r2
    mergeAux(-N, N, -N - N / 2 , N - N / 2);
    // Case 3a: r2 subset r1
    mergeAux(-N, N, -N + 1, N - 1);
    // Case 3b: r1 subset r2
    mergeAux(-N - 1, N + 1, -N, N);
    // Case 4: r1 >= r2
    mergeAux(-N + N / 2, N + N / 2, -N , N);
    // Case 4: r1 > r2
    mergeAux(-N + 3 * N, N + 3 * N, -N , N);
  }

  @Test
  public void testMerge2() {
    System.out.println("Merge2");

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

      mergeAux(i0, i1, j0, j1);
    });
  }

  @Test
  public void testContainsValue() {
    System.out.println("containsValue");

    final int Low = 1, High = 40;
    final int Gap = High - Low;

    final Tree<Integer, Integer> t = RedBlackTreeModule.fromStrictlyIncreasingArray(
            IntStream.range(Low, High).mapToObj(i -> Pair.create(i, 2 * i))
                                      .collect(Collectors.toCollection(ArrayList::new)));

    IntStream.range(Low, High).forEach(n -> { assertTrue(t.containsValue(2 * n)); });
    IntStream.range(Low, High).forEach(n -> { assertFalse(t.containsValue(-2 * n)); });
    IntStream.range(High, High + Gap).forEach(n -> { assertFalse(t.containsValue(-2 * n)); });
  }

  @Test
  public void testLowerPair() {
    System.out.println("lowerPair");

    final int N = 40;
    final ArrayList<Pair<Integer, Integer>> v =
            IntStream.range(0, N)
                     .mapToObj(i -> Pair.create(2 * i, 2 * i))
                     .collect(Collectors.toCollection(ArrayList::new));
    final Tree<Integer, Integer> t = RedBlackTreeModule.fromArray(v);
  
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
  public void testHigherPair() {
    System.out.println("higherPair");

    final int N = 40;
    final ArrayList<Pair<Integer, Integer>> v =
            IntStream.range(0, N)
                     .mapToObj(i -> Pair.create(2 * i, 2 * i))
                     .collect(Collectors.toCollection(ArrayList::new));
    final Tree<Integer, Integer> t = RedBlackTreeModule.fromArray(v);
  
    IntStream.range(1, 2 * N - 2).forEach(n -> {
      assertEquals(n + 1 + (1 - (n & 1)), t.higherKey(n).get().intValue());
    });

    assertEquals(2, t.higherKey(0).get().intValue());
    assertEquals(0, t.higherKey(-1).get().intValue());

    assertEquals(2 * N - 2, t.higherKey(2 * N - 4).get().intValue());
    assertEquals(2 * N - 2, t.higherKey(2 * N - 3).get().intValue());
    assertEquals(Optional.empty(), t.higherKey(2 * N - 2));
    assertEquals(Optional.empty(), t.higherKey(2 * N - 1));
    assertEquals(Optional.empty(), t.higherKey(2 * N - 0));
  }

  private static <K, V> Optional<Pair<K, V>> toPair(final Map.Entry<K, V> e) {
    return e == null ? Optional.empty() : Optional.of(Pair.create(e.getKey(), e.getValue()));
  }

  @Test
  public void testLowerHigherPairRandomInputs() {
    System.out.println("lowerHigherPairRandomInputs");

    final long seed = 1253327;
    final Random rng = new Random(seed);

    final int N = 500;
    final int low = -50, high = 600;
    final int size = 180;

    IntStream.range(0, N).forEach(x -> {
      final int[] perm1 = Numeric.randomPermuation(low, high, size, rng);

      final ArrayList<Pair<Integer, Integer>> v =
              Arrays.stream(perm1)
                    .mapToObj(i -> Pair.create(i, i))
                    .collect(Collectors.toCollection(ArrayList::new));
      final Tree<Integer, Integer> pm = RedBlackTreeModule.fromArray(v);
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
  public void testMapPartialPartiali() {
    System.out.println("mapPartialPartiali");

    final long seed = 12332713;
    final Random rng = new Random(seed);

    final int N = 40;
    final int low = -50, high = 600;
    final int size = 180;
    
    IntStream.range(0, N).forEach(x -> {
      final int[] perm1 = Numeric.randomPermuation(low, high, size, rng);

      final ArrayList<Pair<Integer, Integer>> v =
              Arrays.stream(perm1)
                    .mapToObj(i -> Pair.create(i, i))
                    .collect(Collectors.toCollection(ArrayList::new));
      final Tree<Integer, Integer> t = RedBlackTreeModule.fromArray(v);

      final Tree<Integer, Integer> tEven = t.mapPartial(n -> Optional.ofNullable((n & 1) == 0 ? n : null));
      final Tree<Integer, Integer> tOdd  = t.mapPartial(n -> Optional.ofNullable((n & 1) == 1 ? n : null));

      final int tSize = t.size();
      final int tEvenSize = tEven.size();
      final int tOddSize = tOdd.size();

      assertEquals(size, tSize);
      assertEquals(tSize, tEvenSize + tOddSize);

      Arrays.stream(perm1).forEach(n -> {
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
  public void testMinKeyMaxKey() {
    System.out.println("minKeyMaxKey");

    final long seed = 12332713;
    final Random rng = new Random(seed);

    final int N = 100;
    final int low = -50, high = 600;
    final int size = 180;

    IntStream.range(0, N).forEach(x -> {
      final int[] perm = Numeric.randomPermuation(low, high, size, rng);

      final ArrayList<Pair<Integer, Integer>> v =
              Arrays.stream(perm)
                    .mapToObj(i -> Pair.create(i, i))
                    .collect(Collectors.toCollection(ArrayList::new));
      final Tree<Integer, Integer> t = RedBlackTreeModule.fromArray(v);

      final int minExpected = Arrays.stream(perm).min().getAsInt();
      final int maxExpected = Arrays.stream(perm).max().getAsInt();

      final int min = t.minKey().get().intValue();
      final int max = t.maxKey().get().intValue();

      assertEquals(minExpected, min);
      assertEquals(maxExpected, max);
    });
  }

  @Test
  public void testMinElementPairMaxElementPair() {
    System.out.println("minElementPairMaxElementPair");

    final long seed = 12332713;
    final Random rng = new Random(seed);

    final int N = 100;
    final int low = -50, high = 600;
    final int size = 180;

    IntStream.range(0, N).forEach(x -> {
      final int[] perm = Numeric.randomPermuation(low, high, size, rng);

      final ArrayList<Pair<Integer, Integer>> v =
              Arrays.stream(perm)
                    .mapToObj(i -> Pair.create(i, 2 * i))
                    .collect(Collectors.toCollection(ArrayList::new));
      final Tree<Integer, Integer> t = RedBlackTreeModule.fromArray(v);

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
}
