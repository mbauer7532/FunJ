/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Neo
 */
public class NumericTest {

  public NumericTest() {
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
   * Test of randomInt method, of class Numeric.
   */
  @Test
  public void testRandomInt() {
    System.out.println("randomInt");

    final long seed = 12345678;
    final Random rng = new Random(seed);

    final int N = 2000000;
    final double tol = 1.0e-2;
    {
      int low = -5;
      int high = 6;
      final double expResult = ((double)(high + low)) / 2;
      final double res = IntStream.range(0, N)
                                  .parallel()
                                  .mapToDouble(_n -> (double) Numeric.randomInt(low, high, rng))
                                  .average()
                                  .getAsDouble();
      assertTrue(Numeric.isRelativeEqWithinTolerance(expResult, res, tol));
    }
    {
      int low = 0;
      int high = 9;
      final double expResult = 1.0 / (double)(high - low + 1);

      final int[] arr = new int[10];
      IntStream.range(0, N)
               .forEach(_n -> { ++arr[Numeric.randomInt(low, high, rng)]; });
      Arrays.stream(arr)
            .mapToDouble(n -> ((double) n) / N)
            .forEach(res -> {
              assertTrue(Numeric.isRelativeEqWithinTolerance(expResult, res, tol));
            });
    }
  }

  /**
   * Test of log method, of class Numeric.
   */
  @Test
  public void testLog() {
    System.out.println("log");

    final double[] arr = new double[] { 1.0, 2.0, 3.0, 4.0 };
    final double base = Math.exp(1.0);
    final double tol = 1.0e-14;
    Arrays.stream(arr)
          .forEach(d -> {
            final double res = Numeric.log(d, base);
            final double expected = Math.log(d);
            assertTrue(Numeric.isEqWithinTolerance(expected, res, tol));
          });
  }

 @Test
  public void testJFreeChart() {
    final int low = 1;
    final int high = 4;
    final int size = 3;
    final int N = 18000;

    final long seed = 12345678;
    final Random rng = new Random(seed);

    final Map<Integer, Integer> m = new TreeMap<>();
    for (int i = 0; i != N; ++i) {
      final Set<Integer> a = Numeric.randomSet(low, high, size, rng);
      final int r = a.stream().reduce(0, (n, x) -> n * 10 + x);
      final Integer ii = m.get(r);
      m.put(r, ii == null ? 1 : ii + 1);
    }

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    m.entrySet().forEach(e -> {
      dataset.addValue(e.getValue().doubleValue(), "R", e.getKey().toString());
    });

    final JFreeChart chart = ChartFactory.createBarChart(
            "Bar Chart Demo", // chart title
            "Category", // domain axis label
            "Value", // range axis label
            dataset, // data
            PlotOrientation.VERTICAL, // orientation
            true, // include legend
            true, // tooltips?
            false // URLs?
    );
    final ChartPanel chartPanel = new ChartPanel(chart, false);
    chartPanel.setPreferredSize(new Dimension(500, 270));

    ApplicationFrame demo = new ApplicationFrame("Bar Demo 1");
    demo.setContentPane(chartPanel);

    demo.pack();
    RefineryUtilities.centerFrameOnScreen(demo);
    demo.setVisible(true);

    final int secs = 1;
    try {
      Thread.sleep(secs * 1000);
    } catch (InterruptedException ex) {
      Logger.getLogger(NumericTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Test of randomSet method, of class Numeric.
   */
  @Test
  public void testRandomSet() {
    /*
    System.out.println("randomSet");
    int low = 0;
    int high = 0;
    int size = 0;
    Set<Integer> expResult = null;
    Set<Integer> result = Numeric.randomSet(low, high, size);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
    */
  }

  /**
   * Test of randomPermuation method, of class Numeric.
   */
  @Test
  public void testRandomPermuation() {
    System.out.println("randomPermuation");

    final int low = 1;
    final int high = 4;
    final int size = 2;
    final int N = 100000;

    final long seed = 12345678;
    final Random rng = new Random(seed);

    final Map<Integer, Integer> m = new TreeMap<>();
    for (int i = 0; i != N; ++i) {
      final int[] a = Numeric.randomPermuation(low, high, size, rng);
      final int r = Arrays.stream(a).reduce(0, (n, x) -> n * 10 + x);
      final Integer ii = m.get(r);
      m.put(r, ii == null ? 1 : ii + 1);
    }

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    m.entrySet().forEach(e -> {
      dataset.addValue(e.getValue().doubleValue(), "R", e.getKey().toString());
    });

    final JFreeChart chart = ChartFactory.createBarChart(
            "Bar Chart Demo", // chart title
            "Category", // domain axis label
            "Value", // range axis label
            dataset, // data
            PlotOrientation.VERTICAL, // orientation
            true, // include legend
            true, // tooltips?
            false // URLs?
    );
    final ChartPanel chartPanel = new ChartPanel(chart, false);
    chartPanel.setPreferredSize(new Dimension(500, 270));

    final ApplicationFrame demo = new ApplicationFrame("Bar Demo 1");
    demo.setContentPane(chartPanel);

    demo.pack();
    RefineryUtilities.centerFrameOnScreen(demo);
    demo.setVisible(true);

    final int secs = 1;
    try {
      Thread.sleep(secs * 1000);
    } catch (InterruptedException ex) {
      Logger.getLogger(NumericTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Test of swap method, of class Numeric.
   */
  @Test
  public void testSwap() {
    System.out.println("swap");

    final int siz = 3;
    final int[] arr = new int[siz];
    IntStream.range(0, siz).forEach(i -> { arr[i] = i; });

    IntStream.range(0, siz).forEach(i -> { assertEquals(arr[i], i); });

    {
      Numeric.swap(arr, 0, 1);
      assertEquals(arr[0], 1);
      assertEquals(arr[1], 0);
      assertEquals(arr[2], 2);
    }

    {
      Numeric.swap(arr, 1, 2);
      assertEquals(arr[0], 1);
      assertEquals(arr[1], 2);
      assertEquals(arr[2], 0);
   }
  }

  /**
   * Test of isEqWithinTolerance method, of class Numeric.
   */
  @Test
  public void testIsEqWithinTolerance() {
    System.out.println("isEqWithinTolerance");

    final double tol = 1.0;
    final double epsilon = 1.0e-6;
    final double x1 = 5.0;
    final double x2 = 6.0;

    assertTrue(Numeric.isEqWithinTolerance(x1, x2, tol));
    assertTrue(Numeric.isEqWithinTolerance(x2, x1, tol));
    assertFalse(Numeric.isEqWithinTolerance(x1, x2, tol - epsilon));
    assertFalse(Numeric.isEqWithinTolerance(x2, x1, tol - epsilon));
  }

  /**
   * Test of isRelativeEqWithinTolerance method, of class Numeric.
   */
  @Test
  public void testIsRelativeEqWithinTolerance() {
    System.out.println("isRelativeEqWithinTolerance");

    final double epsilon = 1.0e-10;
    final double x1 = 6.0;
    final double x2 = 5.0;

    assertTrue(Numeric.isRelativeEqWithinTolerance(x1, x2, 1.0 / 5.0 + epsilon));
    assertTrue(Numeric.isRelativeEqWithinTolerance(x2, x1, 1.0 / 6.0 + epsilon));
    assertFalse(Numeric.isRelativeEqWithinTolerance(x1, x2, 1.0 / 5.0 - epsilon));
    assertFalse(Numeric.isRelativeEqWithinTolerance(x2, x1, 1.0 / 6.0 - epsilon));
  }

  private static int nlz(final int m) {
    int n = m;
    if (n == 0) {
      return 32;
    }
    else {
      int res = 0;
      while (n > 0) {
        n = n << 1;
        ++res;
      }

      return res;
    }
  }
  /**
   * Test of nlz method, of class Numeric.
   */
  @Test
  public void testNlz() {
    System.out.println("nlz");

    int[] xs = new int[]
      {-1, 0, 1, 2, 3, 4,
       Integer.MAX_VALUE - 1, Integer.MAX_VALUE, Integer.MAX_VALUE + 1};

    Arrays.stream(xs).forEach(x -> { assertEquals(nlz(x), Numeric.nlz(x)); });

    assertEquals(31, Numeric.nlz(1));
    
    return;
  }

  /**
   * Test of highestBit method, of class Numeric.
   */
  @Test
  public void testHighestBit() {
    System.out.println("highestBit");
    
    {
      final int[] expected 
              = {1,
                2, 2,
                4, 4, 4, 4,
                8, 8, 8, 8, 8, 8, 8, 8,
                16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16,
                32};
      final int[] res = IntStream.rangeClosed(1, 32).map(n -> Numeric.highestBit(n)).toArray();
      assertArrayEquals(expected, res);
    }
    // Test powers of 2.  Should be the identity function.
    {
      final int[] array = IntStream.range(0, 32).map(n -> 1 << n).toArray();
      assertArrayEquals(array, Arrays.stream(array).map(n -> Numeric.highestBit(n)).toArray());
    }
    // Test powers of 2 minus 1.
    {
      final int[] array = IntStream.range(1, 32).map(n -> 1 << n).toArray();
      final int[] arrayMinusOne = Arrays.stream(array).map(n -> n - 1).toArray();
      final int[] expected = Arrays.stream(arrayMinusOne).map(n -> n & ~ (n >>> 1)).toArray();
      assertArrayEquals(expected, Arrays.stream(arrayMinusOne).map(n -> Numeric.highestBit(n)).toArray());
    }

    return;
  }
}
