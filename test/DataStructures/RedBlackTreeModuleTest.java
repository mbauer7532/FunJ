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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
  public RedBlackTreeModuleTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  final static int sLength = 44;
  final static int sWidth = 20;

  private static <K extends Comparable<K>, V> void showGraph(final Tree<K, V> t) {
    DSutils.show(t, sLength, sWidth);

    return;
  }

  private static void waitTime(final int secs) {
      try {
        Thread.sleep(secs * 1000);
      } catch (InterruptedException ex) {
        Logger.getLogger(IntMapModuleTest.class.getName()).log(Level.SEVERE, null, ex);
      }
  
      return;
  }

  /**
   * Test of verifyRedBlackProperties method, of class RedBlackTreeModule.
   */
  @Test
  public void testVerifyRedBlackProperties() {
    System.out.println("verifyRedBlackProperties");
/*
    {
      final Tree<String, Integer> t = RedBlackTreeModule.empty();
      RedBlackTreeModule.verifyRedBlackProperties(t);
    }
    {
      final Tree<String, Integer> t = RedBlackTreeModule.singleton("hi", 2);
      RedBlackTreeModule.verifyRedBlackProperties(t);
    }
    */
    {
      final Tree<String, Integer> t = RedBlackTreeModule.singleton("hi", 2).insert((x, y) -> x, "there", 23);
      RedBlackTreeModule.verifyRedBlackProperties(t);
    }
    {
      //System.out.printf("I am here.\n");
      Tree<Integer, Integer> t = RedBlackTreeModule.empty();
      final int N = 3;
      for (int i = 0; i != N; ++i) {
        t = t.insert(i, i);
        System.out.printf("Length = %d\n", t.size());
      }
      //showGraph(t);
      
      RedBlackTreeModule.verifyRedBlackProperties(t);
      t = t.remove(1);
      RedBlackTreeModule.verifyRedBlackProperties(t);

      System.out.printf("Length = %d\n", t.size());
      //showGraph(t);

      //final int sleepSeconds = 1;
      //waitTime(sleepSeconds);
    }
  }

  static void checkRedBlackTreeStatus(final Tree<Integer, Integer> t) {
    final Pair<Boolean, String> res = RedBlackTreeModule.verifyRedBlackProperties(t);

    if (! res.mx1) {
      showGraph(t);
//      final int secs = 1;
//      waitTime(secs);
      
      assertTrue(res.mx2, false);
    }
  }
  
  @Test
  public void testVerifyRedBlackPropertiesRandomSample() {
    System.out.println("verifyRedBlackPropertiesRandomSample");

    final long seed = 125332;
    final Random rng = new Random(seed);

    final int numIters = 10;
    IntStream.range(0, numIters).forEach(x -> {
      final int low = 1, high = 1024;
      final int size = high;
      final int[] perm1 = Numeric.randomPermuation(low, high, size, rng);
      assertEquals(size, perm1.length);

      Tree<Integer, Integer> t = RedBlackTreeModule.empty();
      for (int i = 0; i != size; ++i) {
        assertEquals(i, t.size());
        checkRedBlackTreeStatus(t);
        t = t.insert(perm1[i], perm1[i]);
      }
      assertEquals(size, t.size());
      checkRedBlackTreeStatus(t);
      System.out.printf("Full: Size = %d Depth = %d\n", t.size(), t.depth());

      // showGraph(t);

      final int[] perm2 = Numeric.randomPermuation(low, high, size, rng);

      for (int i = 0; i != size/2; ++i) {
        t = t.remove(perm2[i]);
        
        checkRedBlackTreeStatus(t);
        assertEquals(size - i - 1, t.size());
      }
      System.out.printf("Half: Size = %d Depth = %d\n", t.size(), t.depth());
//      showGraph(t);
//      int i = -1;
//      t = t.remove(perm2[++i]);
//      showGraph(t);
//      RedBlackTreeModule.verifyRedBlackProperties(t);
//
//      t = t.remove(perm2[++i]);
//      showGraph(t);
//
//      t = t.remove(perm2[++i]);
//      showGraph(t);
//
//      t = t.remove(perm2[++i]);
//      showGraph(t);

//      final int sleepSeconds = 100;
//      waitTime(sleepSeconds);
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

    IntStream.rangeClosed(1, 16).forEach(y -> {
      final ArrayList<Pair<Integer, Integer>> v = new ArrayList<>();
      IntStream.rangeClosed(1, y).forEach(x -> v.add(Pair.create(x, x)));
      final Tree<Integer, Integer> resTree = RedBlackTreeModule.fromStrictlyIncreasingArray(v);

      //showGraph(resTree);
      //waitTime(1);
      checkRedBlackTreeStatus(resTree);
    });
  }

  @Test
  public void testFromStrictlyDecreasingArray() {
    System.out.println("fromStrictlyDecreasingArray");

    IntStream.rangeClosed(1, 16).forEach(y -> {
      final ArrayList<Pair<Integer, Integer>> v = new ArrayList<>();
      IntStream.rangeClosed(1, y).map(x -> y + 1 - x).forEach(x -> v.add(Pair.create(x, x)));
      final Tree<Integer, Integer> resTree = RedBlackTreeModule.fromStrictlyDecreasingArray(v);

      //showGraph(resTree);
      //waitTime(1);
      checkRedBlackTreeStatus(resTree);
    });
  }
}
