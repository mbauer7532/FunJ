/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class ArrayUtilsTest {

  public ArrayUtilsTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  private static List<Integer> toBoxedList(final int[] a) {
    return ArrayUtils.toArrayList(Arrays.stream(a).boxed());
  }

  /**
   * Test of isIncreasing method, of class ArrayUtils.
   */
  @Test
  public void testIsIncreasing() {
    System.out.println("isIncreasing");

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

    assertTrue(ArrayUtils.isIncreasing(toBoxedList(a1)));
    assertTrue(ArrayUtils.isIncreasing(toBoxedList(a2)));
    assertTrue(ArrayUtils.isIncreasing(toBoxedList(a3)));
    assertFalse(ArrayUtils.isIncreasing(toBoxedList(a4)));
    assertTrue(ArrayUtils.isIncreasing(toBoxedList(a5)));
    assertFalse(ArrayUtils.isIncreasing(toBoxedList(a6)));
    assertFalse(ArrayUtils.isIncreasing(toBoxedList(a7)));
    assertTrue(ArrayUtils.isIncreasing(toBoxedList(a8)));
    assertTrue(ArrayUtils.isIncreasing(toBoxedList(a9)));
    assertTrue(ArrayUtils.isIncreasing(toBoxedList(a10)));

    assertTrue(ArrayUtils.isStrictlyIncreasing(toBoxedList(a1)));
    assertTrue(ArrayUtils.isStrictlyIncreasing(toBoxedList(a2)));
    assertTrue(ArrayUtils.isStrictlyIncreasing(toBoxedList(a3)));
    assertFalse(ArrayUtils.isStrictlyIncreasing(toBoxedList(a4)));
    assertTrue(ArrayUtils.isStrictlyIncreasing(toBoxedList(a5)));
    assertFalse(ArrayUtils.isStrictlyIncreasing(toBoxedList(a6)));
    assertFalse(ArrayUtils.isStrictlyIncreasing(toBoxedList(a7)));
    assertFalse(ArrayUtils.isStrictlyIncreasing(toBoxedList(a8)));
    assertFalse(ArrayUtils.isStrictlyIncreasing(toBoxedList(a9)));
    assertFalse(ArrayUtils.isStrictlyIncreasing(toBoxedList(a10)));
  }

   /**
   * Test of isDecreasing method, of class ArrayUtils.
   */

  @Test
  public void testIsDecreasing() {
    System.out.println("isDecreasing");

    final int[] a1  = new int[0];
    final int[] a2  = new int[] { 1 };
    final int[] a3  = new int[] { 2, 1 };
    final int[] a4  = new int[] { 1, 2 };
    final int[] a5  = new int[] { 3, 2, 1 };
    final int[] a6  = new int[] { 2, 3, 1 };
    final int[] a7  = new int[] { 3, 1, 2 };
    final int[] a8  = new int[] { 1, 1 };
    final int[] a9  = new int[] { 2, 2, 1 };
    final int[] a10 = new int[] { 2, 1, 1 };

    assertTrue(ArrayUtils.isDecreasing(toBoxedList(a1)));
    assertTrue(ArrayUtils.isDecreasing(toBoxedList(a2)));
    assertTrue(ArrayUtils.isDecreasing(toBoxedList(a3)));
    assertFalse(ArrayUtils.isDecreasing(toBoxedList(a4)));
    assertTrue(ArrayUtils.isDecreasing(toBoxedList(a5)));
    assertFalse(ArrayUtils.isDecreasing(toBoxedList(a6)));
    assertFalse(ArrayUtils.isDecreasing(toBoxedList(a7)));
    assertTrue(ArrayUtils.isDecreasing(toBoxedList(a8)));
    assertTrue(ArrayUtils.isDecreasing(toBoxedList(a9)));
    assertTrue(ArrayUtils.isDecreasing(toBoxedList(a10)));

    assertTrue(ArrayUtils.isStrictlyDecreasing(toBoxedList(a1)));
    assertTrue(ArrayUtils.isStrictlyDecreasing(toBoxedList(a2)));
    assertTrue(ArrayUtils.isStrictlyDecreasing(toBoxedList(a3)));
    assertFalse(ArrayUtils.isStrictlyDecreasing(toBoxedList(a4)));
    assertTrue(ArrayUtils.isStrictlyDecreasing(toBoxedList(a5)));
    assertFalse(ArrayUtils.isStrictlyDecreasing(toBoxedList(a6)));
    assertFalse(ArrayUtils.isStrictlyDecreasing(toBoxedList(a7)));
    assertFalse(ArrayUtils.isStrictlyDecreasing(toBoxedList(a8)));
    assertFalse(ArrayUtils.isStrictlyDecreasing(toBoxedList(a9)));
    assertFalse(ArrayUtils.isStrictlyDecreasing(toBoxedList(a10)));
  }

    /**
   * Test of swap method, of class Numeric.
   */
  @Test
  public void testSwap() {
    System.out.println("swap");

    {
      final int siz = 3;
      final int[] arr = new int[siz];
      IntStream.range(0, siz).forEach(i -> { arr[i] = i; });

      IntStream.range(0, siz).forEach(i -> { assertEquals(arr[i], i); });

      {
        ArrayUtils.swap(arr, 0, 1);
        assertEquals(arr[0], 1);
        assertEquals(arr[1], 0);
        assertEquals(arr[2], 2);
      }
      {
        ArrayUtils.swap(arr, 1, 2);
        assertEquals(arr[0], 1);
        assertEquals(arr[1], 2);
        assertEquals(arr[2], 0);
      }
    }
    {
      final int siz = 3;
      final ArrayList<Integer> arr = new ArrayList<>(siz);
      IntStream.range(0, siz).forEach(i -> { arr.add(i); });

      IntStream.range(0, siz).forEach(i -> { assertEquals(Integer.valueOf(i), arr.get(i)); });

      {
        ArrayUtils.swap(arr, 0, 1);
        assertEquals(Integer.valueOf(1), arr.get(0));
        assertEquals(Integer.valueOf(0), arr.get(1));
        assertEquals(Integer.valueOf(2), arr.get(2));
      }
      {
        ArrayUtils.swap(arr, 1, 2);
        assertEquals(Integer.valueOf(1), arr.get(0));
        assertEquals(Integer.valueOf(2), arr.get(1));
        assertEquals(Integer.valueOf(0), arr.get(2));
      }
    }
  }
}
