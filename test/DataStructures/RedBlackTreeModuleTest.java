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
  public void testVerifyMapPropertiesForSimpleCases() {
//    PersistentMapTest.verifyMapPropertiesForSimpleCases(sRedBlackTreeFactory);
  }

  @Test
  public void testVerifyRedBlackPropertiesRandomSample() {
//    PersistentMapTest.verifyMapPropertiesRandomSample(sRedBlackTreeFactory);
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
}
