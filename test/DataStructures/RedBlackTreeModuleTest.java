/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.RedBlackTreeModule.Tree;
import DataStructures.TuplesModule.Pair;
import Utils.Numeric;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
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
public class RedBlackTreeModuleTest {
  public RedBlackTreeModuleTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  private static <K extends Comparable<K>, V> void checkRedBlackTreeProperties(final Tree<K, V> t) {
    final Pair<Boolean, String> res = RedBlackTreeModule.verifyRedBlackProperties(t);

    if (! res.mx1) {
      assertTrue(res.mx2, false);
    }
  }

  /**
   * Test of verifyRedBlackProperties method, of class RedBlackTreeModule.
   */
  @Test
  public void testVerifyMapPropertiesForSimpleCases() {
//    PersistentMapTest.verifyMapPropertiesForSimpleCases(sRedBlackTreeFactory);
  }

  @Test
  public void testVerifyRedBlackPropertiesRandomSample() {
//    PersistentMapTest.verifyMapPropertiesRandomSample(sRedBlackTreeFactory);
  }

}
