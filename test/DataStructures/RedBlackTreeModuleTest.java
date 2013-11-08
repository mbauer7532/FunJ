/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.RedBlackTreeModule.Tree;
import DataStructures.TuplesModule.Pair;
import Utils.Numeric;
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

  /**
   * Test of verifyRedBlackProperties method, of class RedBlackTreeModule.
   */
  @Test
  public void testVerifyRedBlackProperties() {
    System.out.println("verifyRedBlackProperties");
    final int length = 44;
    final int width = 20;
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
      System.out.printf("I am here.\n");
      Tree<Integer, Integer> t = RedBlackTreeModule.empty();
      final int N = 3;
      for (int i = 0; i != N; ++i) {
        t = t.insert(i, i);
        System.out.printf("Length = %d\n", t.size());
      }
      DSutils.show(t, length, width);
      
      RedBlackTreeModule.verifyRedBlackProperties(t);
      t = t.remove(1);
      RedBlackTreeModule.verifyRedBlackProperties(t);

      System.out.printf("Length = %d\n", t.size());
      DSutils.show(t, length, width);

      final int sleepSeconds = 1;
      try {
        Thread.sleep(sleepSeconds * 1000);
      } catch (InterruptedException ex) {
        Logger.getLogger(IntMapModuleTest.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  static void checkRedBlackTreeStatus(final Tree<Integer, Integer> t) {
    final Pair<Boolean, String> res = RedBlackTreeModule.verifyRedBlackProperties(t);

    if (! res.mx1) {
      final int length = 44;
      final int width = 20;
      DSutils.show(t, length, width);
      
      final int sleepSeconds = 1;
      try {
        Thread.sleep(sleepSeconds * 1000);
      } catch (InterruptedException ex) {
        Logger.getLogger(IntMapModuleTest.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      assertTrue(res.mx2, false);
    }
  }
  
   @Test
  public void testVerifyRedBlackPropertiesRandomSample() {
    System.out.println("verifyRedBlackPropertiesRandomSample");

    final int length = 44;
    final int width = 20;

    final long seed = 125332;
    final Random rng = new Random(seed);

    final int numIters = 1;
    IntStream.range(0, numIters).forEach(x -> {
      final int low = 1, high = 100;
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

      //DSutils.show(t, length, width);

      final int[] perm2 = Numeric.randomPermuation(low, high, size, rng);

      for (int i = 0; i != size/2; ++i) {
        System.out.printf("i = %d\n", i);
        t = t.remove(perm2[i]);
        
        checkRedBlackTreeStatus(t);
        assertEquals(size - i - 1, t.size());
      }
      DSutils.show(t, length, width);
//      int i = -1;
//      t = t.remove(perm2[++i]);
//      DSutils.show(t, length, width);
//      RedBlackTreeModule.verifyRedBlackProperties(t);
//
//      t = t.remove(perm2[++i]);
//      DSutils.show(t, length, width);
//
//      t = t.remove(perm2[++i]);
//      DSutils.show(t, length, width);
//
//      t = t.remove(perm2[++i]);
//      DSutils.show(t, length, width);

      final int sleepSeconds = 100;
      try {
        Thread.sleep(sleepSeconds * 1000);
      } catch (InterruptedException ex) {
        Logger.getLogger(IntMapModuleTest.class.getName()).log(Level.SEVERE, null, ex);
      }
    });
  }
}
