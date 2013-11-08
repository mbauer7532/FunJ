/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
public class ArrayUtilsTest {
  
  public ArrayUtilsTest() {
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

  private static List<Integer> toBoxedList(final int[] a) {
    return Arrays.stream(a).boxed().collect(Collectors.toList());
  }

  /**
   * Test of isMonotone method, of class ArrayUtils.
   */
  @Test
  public void testIsMonotoneStrictlyMonotone() {
    System.out.println("isMonotoneStrictlyMonotone");

    final int[] a1  = new int[0];
    final int[] a2  = new int[] { 1 };
    final int[] a3  = new int[] { 1, 2 };
    final int[] a4  = new int[] { 2, 1 };
    final int[] a5  = new int[] { 1, 2, 3 };
    final int[] a6  = new int[] { 1, 3, 2 };
    final int[] a7  = new int[] { 2, 1, 3 };
    final int[] a8  = new int[] { 1, 1 };
    final int[] a9  = new int[] { 1, 2, 2 };
    final int[] a10 = new int[] { 1, 1, 2 };

    assertTrue(ArrayUtils.isMonotone(toBoxedList(a1)));
    assertTrue(ArrayUtils.isMonotone(toBoxedList(a2)));
    assertTrue(ArrayUtils.isMonotone(toBoxedList(a3)));
    assertFalse(ArrayUtils.isMonotone(toBoxedList(a4)));
    assertTrue(ArrayUtils.isMonotone(toBoxedList(a5)));
    assertFalse(ArrayUtils.isMonotone(toBoxedList(a6)));
    assertFalse(ArrayUtils.isMonotone(toBoxedList(a7)));
    assertTrue(ArrayUtils.isMonotone(toBoxedList(a8)));
    assertTrue(ArrayUtils.isMonotone(toBoxedList(a9)));
    assertTrue(ArrayUtils.isMonotone(toBoxedList(a10)));

    assertTrue(ArrayUtils.isStrictlyMonotone(toBoxedList(a1)));
    assertTrue(ArrayUtils.isStrictlyMonotone(toBoxedList(a2)));
    assertTrue(ArrayUtils.isStrictlyMonotone(toBoxedList(a3)));
    assertFalse(ArrayUtils.isStrictlyMonotone(toBoxedList(a4)));
    assertTrue(ArrayUtils.isStrictlyMonotone(toBoxedList(a5)));
    assertFalse(ArrayUtils.isStrictlyMonotone(toBoxedList(a6)));
    assertFalse(ArrayUtils.isStrictlyMonotone(toBoxedList(a7)));
    assertFalse(ArrayUtils.isStrictlyMonotone(toBoxedList(a8)));
    assertFalse(ArrayUtils.isStrictlyMonotone(toBoxedList(a9)));
    assertFalse(ArrayUtils.isStrictlyMonotone(toBoxedList(a10)));
  }
}
