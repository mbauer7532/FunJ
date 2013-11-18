/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import org.StructureGraphic.v1.DSutils;
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
public class IntMapModuleTest {
  public IntMapModuleTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of empty method, of class IntMapModule.
   */
  @Test
  public void testEmpty() {
    System.out.println("empty");

    final IntMapModule.Tree<Integer> t0 = IntMapModule.empty();
    final IntMapModule.Tree<Integer> t1 = t0.insert((e1, e2) -> e1, 1, 10);
    final IntMapModule.Tree<Integer> t2 = t1.insert((e1, e2) -> e1, 2, 20);
    
    assertTrue(t0.isEmpty());
    assertFalse(t1.isEmpty());
    assertFalse(t2.isEmpty());
  }

  /**
   * Test of singleton method, of class IntMapModule.
   */
  @Test
  public void testSingleton() {
    System.out.println("singleton");

    final IntMapModule.Tree<Integer> t0 = IntMapModule.singleton(5, 500);
    final Optional<Integer> res0 = t0.get(5);
    final Optional<Integer> res1 = t0.get(4);
    
    assertEquals(1, t0.size());

    assertTrue(res0.isPresent());
    assertFalse(res1.isPresent());

    assertEquals(500, (int)res0.get());
  }
  
  @Test
  public void testInsert() {
    System.out.println("insert");
    
    final int low = 0;
    final int high = 16;
    
    final IntMapModule.Tree<Integer> t0 = IntMapModule.empty();

    final IntMapModule.Tree<Integer> t1
            = IntStream.rangeClosed(low, high)
                       .boxed()
                       .reduce(t0,
                               ((t, ii) -> t.insert((z1, z2) -> z1, ii, ii)),
                               (tt1, tt2) -> null);

    AtomicReference<IntMapModule.Tree<Integer>> refToTree = new AtomicReference<>(t0);
    IntStream.rangeClosed(low, high)
             .collect((() -> null),
                     (IntMapModule.Tree<Integer> tree, int i) -> { refToTree.set(refToTree.get().insert((z1, z2) -> z1, i, i)); },
                     (tt1, tt2) -> {});
    final IntMapModule.Tree<Integer> t2 = refToTree.get();

    final int length = 44;
    final int width = 20;
    DSutils.show(t1, length, width);
    DSutils.show(t2, length, width);
    final int sleepSeconds = 1;
    try {
      Thread.sleep(sleepSeconds * 1000);
    } catch (InterruptedException ex) {
      Logger.getLogger(IntMapModuleTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
