/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.RedBlackTreeModule.Tree;
import java.util.logging.Level;
import java.util.logging.Logger;
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
      //RedBlackTreeModule.verifyRedBlackProperties(t);
    }
    {
      System.out.printf("I am here.\n");
      Tree<Integer, Integer> t = RedBlackTreeModule.empty();
      final int N = 100;
      for (int i = 0; i != N; ++i) {
        System.out.printf("Length = %d\n", t.size());
        t = t.insert((x, y) -> x, i, i);
      }
      DSutils.show(t, length, width);
      t = t.remove(33);
      //RedBlackTreeModule.verifyRedBlackProperties(t);
      

      System.out.printf("Length = %d", t.size());
      DSutils.show(t, length, width);

      final int sleepSeconds = 100;
      try {
        Thread.sleep(sleepSeconds * 1000);
      } catch (InterruptedException ex) {
        Logger.getLogger(IntMapModuleTest.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
  }
}
