/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import DataStructures.AvlTreeModuleTest;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    /*
    System.out.println("randomInt");
    int low = 0;
    int high = 0;
    int expResult = 0;
    int result = Numeric.randomInt(low, high);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
    */
  }

  /**
   * Test of randomArray method, of class Numeric.
   */
  @Test
  public void testRandomArray() {
/*
    System.out.println("randomArray");
    int low = 0;
    int high = 0;
    int size = 0;
    int[] expResult = null;
    int[] result = Numeric.randomArray(low, high, size);
    assertArrayEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
    */
  }

  /**
   * Test of log method, of class Numeric.
   */
  @Test
  public void testLog() {
/*
    System.out.println("log");
    double x = 0.0;
    double base = 0.0;
    double expResult = 0.0;
    double result = Numeric.log(x, base);
    assertEquals(expResult, result, 0.0);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
*/
  }

 @Test
  public void testJFreeChart() {
    final int low = 1;
    final int high = 4;
    final int size = 2;
    final int N = 18000;

    final Map<Integer, Integer> m = new TreeMap<>();
    for (int i = 0; i != N; ++i) {
      final int[] a = Numeric.randomArray(low, high, size);
      int r = Arrays.stream(a).reduce(0, (n, x) -> n * 10 + x);
      Integer ii = m.get(r);
      m.put(r, ii == null ? 1 : ii + 1);
    }

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    m.keySet().stream().forEach((i) -> {
      Integer freq = m.get(i);
      double dfreq = freq == null ? 0.0 : freq.doubleValue();
      dataset.addValue(dfreq, "R", Integer.toString(i));
    });

    JFreeChart chart = ChartFactory.createBarChart(
            "Bar Chart Demo", // chart title
            "Category", // domain axis label
            "Value", // range axis label
            dataset, // data
            PlotOrientation.VERTICAL, // orientation
            true, // include legend
            true, // tooltips?
            false // URLs?
    );
    ChartPanel chartPanel = new ChartPanel(chart, false);
    chartPanel.setPreferredSize(new Dimension(500, 270));

    ApplicationFrame demo = new ApplicationFrame("Bar Demo 1");
    demo.setContentPane(chartPanel);

    demo.pack();
    RefineryUtilities.centerFrameOnScreen(demo);
    demo.setVisible(true);

    final int secs = 100;
    try {
      Thread.sleep(secs * 1000);
    } catch (InterruptedException ex) {
      Logger.getLAvlTreeModuleTesteeTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
