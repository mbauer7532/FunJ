/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import DataStructures.TuplesModule;
import DataStructures.TuplesModule.Pair;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
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
public class FunctionalsTest {
  
  public FunctionalsTest() {}
  
  @BeforeClass
  public static void setUpClass() {}
  
  @AfterClass
  public static void tearDownClass() {}
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}

  /**
   * Test of mapOptOrElse method, of class Functionals.
   */
  @Test
  public void testMapOptOrElse() {
    System.out.println("mapOptOrElse");
//    Object expResult = null;
//    Object result = Functionals.mapOptOrElse(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of functionShouldNotBeCalled method, of class Functionals.
   */
  @Test
  public void testFunctionShouldNotBeCalled() {
    System.out.println("functionShouldNotBeCalled");
//    Object a = null;
//    Object b = null;
//    Object expResult = null;
//    Object result = Functionals.functionShouldNotBeCalled(a, b);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of comparator method, of class Functionals.
   */
  @Test
  public void testComparator() {
    System.out.println("comparator");
//    Object x = null;
//    Object y = null;
//    int expResult = 0;
//    int result = Functionals.comparator(x, y);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of maxSelector method, of class Functionals.
   */
  @Test
  public void testMaxSelector() {
    System.out.println("maxSelector");
//    Object x = null;
//    Object y = null;
//    Object expResult = null;
//    Object result = Functionals.maxSelector(x, y);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of minSelector method, of class Functionals.
   */
  @Test
  public void testMinSelector() {
    System.out.println("minSelector");
//    Object x = null;
//    Object y = null;
//    Object expResult = null;
//    Object result = Functionals.minSelector(x, y);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of reduce method, of class Functionals.
   */
  @Test
  public void testReduce_3args_1() {
    System.out.println("reduce");
//    Object expResult = null;
//    Object result = Functionals.reduce(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of reduce method, of class Functionals.
   */
  @Test
  public void testReduce_3args_2() {
    System.out.println("reduce");
//    Object expResult = null;
//    Object result = Functionals.reduce(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of reduce method, of class Functionals.
   */
  @Test
  public void testReduce_3args_3() {
    System.out.println("reduce");
//    Object expResult = null;
//    Object result = Functionals.reduce(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of reduce method, of class Functionals.
   */
  @Test
  public void testReduce_3args_4() {
    System.out.println("reduce");
//    Object expResult = null;
//    Object result = Functionals.reduce(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of zip method, of class Functionals.
   */
  @Test
  public void testZip() {
    System.out.println("zip");

    {
      final Stream<Integer> s0 = IntStream.range(0, 0).boxed();
      final Stream<Integer> s1 = IntStream.range(0, 5).boxed();

      final ArrayList<Pair<Integer, Integer>> v = Functionals.zip(s0, s1).collect(Collectors.toCollection(ArrayList::new));
      assertEquals(0, v.size());
    }
    {
      final Stream<Integer> s0 = IntStream.range(0, 5).boxed();
      final Stream<Integer> s1 = IntStream.range(10, 100).boxed();

      final ArrayList<Pair<Integer, Integer>> v = Functionals.zip(s0, s1).collect(Collectors.toCollection(ArrayList::new));
      assertEquals(5, v.size());
      assertEquals("[(0, 10), (1, 11), (2, 12), (3, 13), (4, 14)]", v.toString());
    }
    {
      final Stream<Integer> s1 = IntStream.range(0, 5).boxed();
      final Stream<Integer> s0 = IntStream.range(10, 100).boxed();

      final ArrayList<Pair<Integer, Integer>> v = Functionals.zip(s0, s1).collect(Collectors.toCollection(ArrayList::new));
      assertEquals(5, v.size());
      assertEquals("[(10, 0), (11, 1), (12, 2), (13, 3), (14, 4)]", v.toString());
    }
  }
  
  @Test
  public void testZipWithIntStream() {
    System.out.println("zipWithIntStream");

    {
      final ArrayList<Pair<Integer, Integer>> v = Functionals.zip(IntStream.range(0, 5), IntStream.range(10, 100).boxed()).collect(Collectors.toCollection(ArrayList::new));
      assertEquals(5, v.size());
      assertEquals("[(0, 10), (1, 11), (2, 12), (3, 13), (4, 14)]", v.toString());
    }
    {
      final ArrayList<Pair<Integer, Integer>> v = Functionals.zip(IntStream.range(10, 100).boxed(), IntStream.range(0, 5)).collect(Collectors.toCollection(ArrayList::new));
      assertEquals(5, v.size());
      assertEquals("[(10, 0), (11, 1), (12, 2), (13, 3), (14, 4)]", v.toString());
    }
    {
      final ArrayList<Pair<Integer, Integer>> v = Functionals.zip(IntStream.range(0, 5), IntStream.range(10, 100)).collect(Collectors.toCollection(ArrayList::new));
      assertEquals(5, v.size());
      assertEquals("[(0, 10), (1, 11), (2, 12), (3, 13), (4, 14)]", v.toString());
    }
  }
}
