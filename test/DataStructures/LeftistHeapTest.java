/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import Utils.Numeric;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Stream;
import org.StructureGraphic.v1.DSTreeNode;
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
public class LeftistHeapTest {
  
  public LeftistHeapTest() {}
  
  @BeforeClass
  public static void setUpClass() {}
  
  @AfterClass
  public static void tearDownClass() {}
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}

  /**
   * Test of empty method, of class LeftistHeap.
   */
  @Test
  public void testEmpty() {
    System.out.println("empty");

    LeftistHeap<Integer> h = LeftistHeap.empty();
    assertTrue(h.isEmpty());
    boolean exceptionWasThrown = false;
    try {
      final Integer i = h.findMin();
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);
  }

  /**
   * Test of singleton method, of class LeftistHeap.
   */
  @Test
  public void testSingleton() {
    System.out.println("singleton");

    LeftistHeap<Integer> h = LeftistHeap.singleton(5);
    assertTrue(! h.isEmpty());
    assertEquals(1, h.size());
    assertEquals(Integer.valueOf(5), h.findMin());
  }

  /**
   * Test of isEmpty method, of class LeftistHeap.
   */
  @Test
  public void testIsEmpty() {
    System.out.println("isEmpty");

    LeftistHeap<Integer> h0 = LeftistHeap.empty();
    LeftistHeap<Integer> h1 = LeftistHeap.singleton(5);

    assertTrue(h0.isEmpty());
    assertFalse(h1.isEmpty());
  }

  /**
   * Test of findMin method, of class LeftistHeap.
   */
  @Test
  public void testFindMin() {
    System.out.println("findMin");

    LeftistHeap<Integer> h = LeftistHeap.singleton(5);
    assertEquals(Integer.valueOf(5), h.findMin());
    h = h.insert(4);
    assertEquals(Integer.valueOf(4), h.findMin());
    h = h.insert(3);
    assertEquals(Integer.valueOf(3), h.findMin());
    h = h.insert(6);
    assertEquals(Integer.valueOf(3), h.findMin());
    h = h.insert(3);
    assertEquals(Integer.valueOf(3), h.findMin());
    h = h.insert(9);
    assertEquals(Integer.valueOf(3), h.findMin());
    h = h.insert(0);
    assertEquals(Integer.valueOf(0), h.findMin());
  }

  /**
   * Test of merge method, of class LeftistHeap.
   */
  @Test
  public void testMerge() {
    System.out.println("merge");

    final LeftistHeap<Integer> h0 = LeftistHeap.of(4,3,5,7);
    final LeftistHeap<Integer> h1 = LeftistHeap.of(1,-3,9,2);

    final LeftistHeap<Integer> res0 = h0.merge(h1);
    final LeftistHeap<Integer> res1 = h1.merge(h0);
    
    GraphModule.showGraph(h0);
    GraphModule.showGraph(h1);
    GraphModule.showGraph(res0);
    GraphModule.showGraph(res0);

    final int[] v = Numeric.randomPermutation(0, 5000, 200, new Random(123));
    GraphModule.showGraph(LeftistHeap.fromStream(Arrays.stream(v).boxed()));
    GraphModule.waitTime(100);
  }

  /**
   * Test of insert method, of class LeftistHeap.
   */
  @Test
  public void testInsert() {
    System.out.println("insert");
//    Object val = null;
//    LeftistHeap instance = null;
//    LeftistHeap expResult = null;
//    LeftistHeap result = instance.insert(val);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of deleteMin method, of class LeftistHeap.
   */
  @Test
  public void testDeleteMin_0args() {
    System.out.println("deleteMin");
//    LeftistHeap instance = null;
//    LeftistHeap expResult = null;
//    LeftistHeap result = instance.deleteMin();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of deleteMin method, of class LeftistHeap.
   */
  @Test
  public void testDeleteMin_Ref() {
    System.out.println("deleteMin");
//    LeftistHeap instance = null;
//    LeftistHeap expResult = null;
//    LeftistHeap result = instance.deleteMin(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of size method, of class LeftistHeap.
   */
  @Test
  public void testSize() {
    System.out.println("size");
//    LeftistHeap instance = null;
//    int expResult = 0;
//    int result = instance.size();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of fromStream method, of class LeftistHeap.
   */
  @Test
  public void testFromStream() {
    System.out.println("fromStream");
//    LeftistHeap expResult = null;
//    LeftistHeap result = LeftistHeap.fromStream(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of fromArray method, of class LeftistHeap.
   */
  @Test
  public void testFromArray() {
    System.out.println("fromArray");
//    LeftistHeap expResult = null;
//    LeftistHeap result = LeftistHeap.fromArray(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of toArrayList method, of class LeftistHeap.
   */
  @Test
  public void testToArrayList() {
    System.out.println("toArrayList");
//    LeftistHeap instance = null;
//    ArrayList expResult = null;
//    ArrayList result = instance.toArrayList();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of toAscArrayList method, of class LeftistHeap.
   */
  @Test
  public void testToAscArrayList() {
    System.out.println("toAscArrayList");
//    LeftistHeap instance = null;
//    ArrayList expResult = null;
//    ArrayList result = instance.toAscArrayList();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of toDescArrayList method, of class LeftistHeap.
   */
  @Test
  public void testToDescArrayList() {
    System.out.println("toDescArrayList");
//    LeftistHeap instance = null;
//    ArrayList expResult = null;
//    ArrayList result = instance.toDescArrayList();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of iterator method, of class LeftistHeap.
   */
  @Test
  public void testIterator() {
    System.out.println("iterator");
//    LeftistHeap instance = null;
//    Iterator expResult = null;
//    Iterator result = instance.iterator();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of DSgetChildren method, of class LeftistHeap.
   */
  @Test
  public void testDSgetChildren() {
    System.out.println("DSgetChildren");
//    LeftistHeap instance = null;
//    DSTreeNode[] expResult = null;
//    DSTreeNode[] result = instance.DSgetChildren();
//    assertArrayEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of DSgetValue method, of class LeftistHeap.
   */
  @Test
  public void testDSgetValue() {
    System.out.println("DSgetValue");
//    LeftistHeap instance = null;
//    Object expResult = null;
//    Object result = instance.DSgetValue();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of DSgetColor method, of class LeftistHeap.
   */
  @Test
  public void testDSgetColor() {
    System.out.println("DSgetColor");
//    LeftistHeap instance = null;
//    Color expResult = null;
//    Color result = instance.DSgetColor();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of stream method, of class LeftistHeap.
   */
  @Test
  public void testStream() {
    System.out.println("stream");
//    LeftistHeap instance = null;
//    Stream expResult = null;
//    Stream result = instance.stream();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }  
}
