/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.IntMapModule.IntMapFactory;
import DataStructures.TuplesModule.AssocIntPair;
import DataStructures.TuplesModule.Pair;
import Utils.Functionals;
import Utils.Ref;
import java.util.Optional;
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
    
    final int low = -3;
    final int high = 3;

    final IntMapModule.Tree<Integer> t1
            = Functionals.reduce(IntStream.rangeClosed(low, high),
                                 IntMapModule.<Integer> empty(),
                                 (t, i) -> t.insert(i, Integer.valueOf(i)));
  }

  @Test
  public void testPartitionAndPartitioni1() {
    System.out.printf("PartitionAndPartitioni\n");

    final boolean xx = false;
    
    final IntMapFactory<Integer> mapFactory = IntMapModule.makeFactory();
    {
      final int N = 3200000;
      final int halfN = N / 2;
      final IntMapModule.Tree<Integer> t0 = mapFactory.fromStream(
              IntStream.range(0, N)
                       .mapToObj(i -> AssocIntPair.create(i, i)));

      final Pair<IntMapModule.Tree<Integer>, IntMapModule.Tree<Integer>> p = t0.partitioni((k, v) -> (k & 1) == 1);
      final IntMapModule.Tree<Integer> t1 = p.mx1, t2 = p.mx2;

      assertEquals(N, t0.size());
      assertEquals(halfN, t1.size());
      assertEquals(halfN, t2.size());

      IntStream.range(0, N).forEach(n -> {
        if ((n & 1) == 1) {
          assertTrue(t0.containsKey(n) && t1.containsKey(n) && ! t2.containsKey(n));
        }
        else {
          assertTrue(t0.containsKey(n) && ! t1.containsKey(n) && t2.containsKey(n));
        }
      });
    }
    {
      final IntMapModule.Tree<Integer> pm = mapFactory.singleton(10, 20);
      final Pair<IntMapModule.Tree<Integer>, IntMapModule.Tree<Integer>> ms = pm.partitioni((k, v) -> v != 20);

      final IntMapModule.Tree<Integer> pm1 = ms.mx1, pm2 = ms.mx2;

      assertTrue(! pm.isEmpty());
      assertTrue(pm1.isEmpty());
      assertTrue(! pm2.isEmpty());
    }
  }

  @Test
  public void testPartitionAndPartitioni2() {
    System.out.printf("PartitionAndPartitioni2\n");

    final IntMapFactory<Integer> mapFactory = IntMapModule.makeFactory();
    {
      final int N = 10; //3200000;

      final IntMapModule.Tree<Integer> t0 = mapFactory.fromStream(
              IntStream.range(0, N)
                       .mapToObj(i -> AssocIntPair.create(i, i)));

      final Functionals.IntBiPredicate<Integer> pred = (k, v) -> k > N - 4;

      final Pair<IntMapModule.Tree<Integer>, IntMapModule.Tree<Integer>> p = t0.partitioni(pred);

      final IntMapModule.Tree<Integer> t1 = p.mx1, t2 = p.mx2;
      
      System.out.printf("size0 = %d\n", t1.size());
      System.out.printf("size1 = %d\n", t2.size());
    }
  }
}
