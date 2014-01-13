/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import DataStructures.TuplesModule;
import DataStructures.TuplesModule.Pair;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
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

    {
      final Optional<Integer> i = Optional.empty();
      final Boolean res = Functionals.mapOptOrElse(i, y -> false, () -> true);
      assertEquals(Boolean.valueOf(true), res);
    }
    {
      final Optional<Integer> i = Optional.of(3);
      final Boolean res = Functionals.mapOptOrElse(i, y -> y > 2, () -> false);
      assertEquals(Boolean.valueOf(true), res);
    }
    {
      final Optional<Integer> i = Optional.of(1);
      final Boolean res = Functionals.mapOptOrElse(i, y -> y > 2, () -> true);
      assertEquals(Boolean.valueOf(false), res);
    }
  }

  /**
   * Test of functionShouldNotBeCalled method, of class Functionals.
   */
  @Test
  public void testFunctionShouldNotBeCalled() {
    System.out.println("functionShouldNotBeCalled");

    boolean exceptionWasThrown = false;
    try {
      Functionals.functionShouldNotBeCalled(Integer.valueOf(1), Integer.valueOf(2));
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);
  }

  /**
   * Test of comparator method, of class Functionals.
   */
  @Test
  public void testComparator() {
    System.out.println("comparator");

    final int res0 = Functionals.comparator(Integer.valueOf(1), Integer.valueOf(2));
    final int res1 = Functionals.comparator(Integer.valueOf(2), Integer.valueOf(2));
    final int res2 = Functionals.comparator(Integer.valueOf(2), Integer.valueOf(1));

    assertTrue(res0 < 0);
    assertTrue(res1 == 0);
    assertTrue(res2 > 0);
  }

  /**
   * Test of maxSelector method, of class Functionals.
   */
  @Test
  public void testMaxSelector() {
    System.out.println("maxSelector");

    final Integer res0 = Functionals.maxSelector(Integer.valueOf(1), Integer.valueOf(2));
    final Integer res1 = Functionals.maxSelector(Integer.valueOf(2), Integer.valueOf(2));
    final Integer res2 = Functionals.maxSelector(Integer.valueOf(2), Integer.valueOf(1));
    
    final Integer expected = Integer.valueOf(2);
    assertEquals(expected, res0);
    assertEquals(expected, res1);
    assertEquals(expected, res2);
  }

  /**
   * Test of minSelector method, of class Functionals.
   */
  @Test
  public void testMinSelector() {
    System.out.println("minSelector");

    final Integer res0 = Functionals.minSelector(Integer.valueOf(1), Integer.valueOf(2));
    final Integer res1 = Functionals.minSelector(Integer.valueOf(1), Integer.valueOf(1));
    final Integer res2 = Functionals.minSelector(Integer.valueOf(2), Integer.valueOf(1));

    final Integer expected = Integer.valueOf(1);
    assertEquals(expected, res0);
    assertEquals(expected, res1);
    assertEquals(expected, res2);
  }

  /**
   * Test of reduce method, of class Functionals.
   */
  @Test
  public void testReduce_3args_1() {
    System.out.println("reduce");

    {
      final Integer res = Functionals.reduce(IntStream.empty(), 47, (acc, i) -> acc + i);
      assertEquals(Integer.valueOf(47), res);
    }
    {
      final Integer res = Functionals.reduce(IntStream.of(1, 2, 3, 4), 0, (acc, i) -> acc + i);
      assertEquals(Integer.valueOf(1 + 2 + 3 + 4), res);
    }
  }

  /**
   * Test of reduce method, of class Functionals.
   */
  @Test
  public void testReduce_3args_2() {
    System.out.println("reduce");

    {
      final Double res = Functionals.reduce(DoubleStream.empty(), 47.0, (acc, d) -> acc + d);
      assertEquals(Double.valueOf(47.0), res);
    }
    {
      final Double res = Functionals.reduce(DoubleStream.of(1.0, 2.0, 3.0, 4.0), 0.0, (acc, d) -> acc + d);
      assertEquals(Double.valueOf(1.0 + 2.0 + 3.0 + 4.0), res);
    }
  }

  /**
   * Test of reduce method, of class Functionals.
   */
  @Test
  public void testReduce_3args_3() {
    System.out.println("reduce");

    {
      final Long res = Functionals.reduce(LongStream.empty(), 47l, (acc, l) -> acc + l);
      assertEquals(Long.valueOf(47L), res);
    }
    {
      final Long res = Functionals.reduce(LongStream.of(1l, 2l, 3l, 4l), 0l, (acc, l) -> acc + l);
      assertEquals(Long.valueOf(1l + 2l + 3l + 4l), res);
    }
  }

  /**
   * Test of reduce method, of class Functionals.
   */
  @Test
  public void testReduce_3args_4() {
    System.out.println("reduce");

    {
      final Integer res = Functionals.reduce(IntStream.empty().boxed(), 47, (acc, i) -> acc + i);
      assertEquals(Integer.valueOf(47), res);
    }
    {
      final Integer res = Functionals.reduce(IntStream.of(1, 2, 3, 4).boxed(), 0, (acc, i) -> acc + i);
      assertEquals(Integer.valueOf(1 + 2 + 3 + 4), res);
    }
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
