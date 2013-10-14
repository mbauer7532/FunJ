/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

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
public class AvlTreeTest {

  public AvlTreeTest() {
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
   * Test of empty method, of class AvlTree.
   */
  @Test
  public void testEmpty() {
    System.out.println("empty");
    AvlTree.NodeBase expResult = null;
    AvlTree.NodeBase result = AvlTree.empty();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of singleton method, of class AvlTree.
   */
  @Test
  public void testSingleton() {
    System.out.println("singleton");
    Object key = null;
    Object value = null;
    AvlTree.NodeBase expResult = null;
    AvlTree.NodeBase result = AvlTree.singleton(key, value);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
