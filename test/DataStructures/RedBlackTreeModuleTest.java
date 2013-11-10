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
import java.util.Random;
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
//      showGraph(t);
//      waitTime(1);

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
        IntStream.range(0, i).forEach(n -> assertTrue(q.contains(n)));
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
        assertTrue(t.contains(perm2[i]));
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
    
    assertTrue(t1.contains(10));
    assertFalse(t2.contains(10));
    assertTrue(t3.contains(10));

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
      final int low = -200, high = 200;
      final int size = 20;

      final int[] perm = Numeric.randomPermuation(low, high, size, rng);
      IntStream.range(0, numIters).forEach(x -> {
        final ArrayList<Pair<Integer, Integer>> v =
                Arrays.stream(perm)
                        .mapToObj(i -> Pair.create(i, i))
                        .collect(Collectors.toCollection(ArrayList::new));

        final Tree<Integer, Integer> t = RedBlackTreeModule.fromRandomArray(v);

        // Checking tree validity also checks its height.
        checkRedBlackTreeProperties(t);
        
        assertTrue(IntStream.range(0, numIters).allMatch(idx -> t.contains(perm[idx])));
      });
    }
  }

  @Test
  public void testFilterAndFilteri() {
    System.out.println("filterAndFilteri");

    final int N = 40;
    final Tree<Integer, Integer> t0 = RedBlackTreeModule.fromRandomArray(
            IntStream.range(0, N)
                     .mapToObj(i -> Pair.create(i, i))
                     .collect(Collectors.toCollection(ArrayList::new)));
    final Tree<Integer, Integer> t1 = t0.filteri((k, v) -> (k & 1) == 1);

    checkRedBlackTreeProperties(t0);
    checkRedBlackTreeProperties(t1);

    assertEquals(N, t0.size());
    assertEquals(N / 2, t1.size());
    
    IntStream.range(0, N).forEach(n -> {
      if ((n & 1) == 0) {
        assertTrue(t0.contains(n) && t1.contains(n));
      }
      else {
        assertTrue(t0.contains(n) && ! t1.contains(n));
      }
    });
  }

  @Test
  public void testPartitionAndPartitioni() {
    System.out.println("partitionAndPartitioni");

    final int N = 40;
    final Tree<Integer, Integer> t0 = RedBlackTreeModule.fromRandomArray(
            IntStream.range(0, N)
                     .mapToObj(i -> Pair.create(i, i))
                     .collect(Collectors.toCollection(ArrayList::new)));

    final Pair<Tree<Integer, Integer>, Tree<Integer, Integer>> p = t0.partitioni((k, v) -> (k & 1) == 1);
    final Tree<Integer, Integer> t1 = p.mx1, t2 = p.mx2;

    checkRedBlackTreeProperties(t0);
    checkRedBlackTreeProperties(t1);
    checkRedBlackTreeProperties(t2);
    
    assertEquals(N, t0.size());
    assertEquals(N / 2, t1.size());
    assertEquals(N / 2, t2.size());

    IntStream.range(0, N).forEach(n -> {
      if ((n & 1) == 1) {
        assertTrue(t0.contains(n) && t1.contains(n) && ! t2.contains(n));
      }
      else {
        assertTrue(t0.contains(n) && ! t1.contains(n) && t2.contains(n));
      }
    });
  }
}
