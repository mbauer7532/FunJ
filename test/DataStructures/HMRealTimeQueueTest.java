/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import java.util.ArrayList;
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
public class HMRealTimeQueueTest {
    public HMRealTimeQueueTest() {}
  
  @BeforeClass
  public static void setUpClass() {}
  
  @AfterClass
  public static void tearDownClass() {}
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}

  /**
   * Test of empty method, of class HMRealTimeQueue.
   */
  @Test
  public void testEmpty() {
    System.out.println("empty");
    HMRealTimeQueue expResult = null;
    HMRealTimeQueue result = HMRealTimeQueue.empty();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of isEmpty method, of class HMRealTimeQueue.
   */
  @Test
  public void testIsEmpty() {
    System.out.println("isEmpty");
    HMRealTimeQueue instance = null;
    boolean expResult = false;
    boolean result = instance.isEmpty();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of singleton method, of class HMRealTimeQueue.
   */
  @Test
  public void testSingleton() {
    System.out.println("singleton");
    
    HMRealTimeQueue<Integer> q = HMRealTimeQueue.singleton(1);
    assertFalse(q.isEmpty());
    assertEquals(1, q.length());
    assertEquals(Integer.valueOf(1), q.head());

    q = q.addLast(2);
    assertFalse(q.isEmpty());
    assertEquals(2, q.length());
    assertEquals(Integer.valueOf(1), q.head());
    assertEquals(Integer.valueOf(2), q.tail().head());
    assertTrue(q.tail().tail().isEmpty());

    q = HMRealTimeQueue.of(1, 2, 3, 4, 5);
    assertEquals(Integer.valueOf(1), q.head());
    assertEquals(Integer.valueOf(2), q.tail().head());
    assertEquals(Integer.valueOf(3), q.tail().tail().head());
    assertEquals(Integer.valueOf(4), q.tail().tail().tail().head());
    assertEquals(Integer.valueOf(5), q.tail().tail().tail().tail().head());
  }

  /**
   * Test of length method, of class HMRealTimeQueue.
   */
  @Test
  public void testLength() {
    System.out.println("length");
    HMRealTimeQueue instance = null;
    int expResult = 0;
    int result = instance.length();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addLast method, of class HMRealTimeQueue.
   */
  @Test
  public void testAddLast() {
    System.out.println("addLast");
    Object x = null;
    HMRealTimeQueue instance = null;
    HMRealTimeQueue expResult = null;
    HMRealTimeQueue result = instance.addLast(x);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of drop method, of class HMRealTimeQueue.
   */
  @Test
  public void testDrop() {
    System.out.println("drop");
    int cnt = 0;
    HMRealTimeQueue instance = null;
    HMRealTimeQueue expResult = null;
    HMRealTimeQueue result = instance.drop(cnt);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of take method, of class HMRealTimeQueue.
   */
  @Test
  public void testTake() {
    System.out.println("take");
    int cnt = 0;
    HMRealTimeQueue instance = null;
    ArrayList expResult = null;
    ArrayList result = instance.take(cnt);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of fromArray method, of class HMRealTimeQueue.
   */
  @Test
  public void testFromArray() {
    System.out.println("fromArray");
    HMRealTimeQueue expResult = null;
    HMRealTimeQueue result = HMRealTimeQueue.fromArray(null);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of fromStream method, of class HMRealTimeQueue.
   */
  @Test
  public void testFromStream() {
    System.out.println("fromStream");
    HMRealTimeQueue expResult = null;
    HMRealTimeQueue result = HMRealTimeQueue.fromStream(null);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of of method, of class HMRealTimeQueue.
   */
  @Test
  public void testOf() {
    System.out.println("of");
    Object[] v = null;
    HMRealTimeQueue expResult = null;
    HMRealTimeQueue result = HMRealTimeQueue.of(v);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of head method, of class HMRealTimeQueue.
   */
  @Test
  public void testHead() {
    System.out.println("head");
    HMRealTimeQueue instance = null;
    Object expResult = null;
    Object result = instance.head();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of tail method, of class HMRealTimeQueue.
   */
  @Test
  public void testTail() {
    System.out.println("tail");
    HMRealTimeQueue instance = null;
    HMRealTimeQueue expResult = null;
    HMRealTimeQueue result = instance.tail();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of contains method, of class HMRealTimeQueue.
   */
  @Test
  public void testContains() {
    System.out.println("contains");
    Object v = null;
    HMRealTimeQueue instance = null;
    boolean expResult = false;
    boolean result = instance.contains(v);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of notContains method, of class HMRealTimeQueue.
   */
  @Test
  public void testNotContains() {
    System.out.println("notContains");
    Object v = null;
    HMRealTimeQueue instance = null;
    boolean expResult = false;
    boolean result = instance.notContains(v);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
