/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import Utils.Functionals;
import Utils.Numeric;
import Utils.Ref;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
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
public class HMRealTimeQueueTest {
    public HMRealTimeQueueTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of empty method, of class HMRealTimeQueue.
   */
  @Test
  public void testEmpty() {
    System.out.println("empty");

    final HMRealTimeQueue<Integer> q = HMRealTimeQueue.empty();
    assertTrue(q.isEmpty());
    assertEquals(0, q.size());
    assertFalse(q.addLast(1).isEmpty());
  }

  /**
   * Test of isEmpty method, of class HMRealTimeQueue.
   */
  @Test
  public void testIsEmpty() {
    System.out.println("isEmpty");

    final HMRealTimeQueue<Integer> q = HMRealTimeQueue.empty();
    assertTrue(q.isEmpty());
    assertEquals(0, q.size());
    assertFalse(q.addLast(1).isEmpty());
    assertFalse(q.addLast(1).addLast(2).isEmpty());
    assertFalse(q.addLast(1).addLast(2).addLast(3).isEmpty());
  }

  /**
   * Test of singleton method, of class HMRealTimeQueue.
   */
  @Test
  public void testSingleton() {
    System.out.println("singleton");
    
    HMRealTimeQueue<Integer> q = HMRealTimeQueue.singleton(1);
    assertFalse(q.isEmpty());
    assertEquals(1, q.size());
    assertEquals(Integer.valueOf(1), q.head());
  }

  /**
   * Test of size method, of class HMRealTimeQueue.
   */
  @Test
  public void testSize() {
    System.out.println("size");

    final Random rng = new Random(12371);
    final Ref<Integer> len = new Ref<>(Integer.valueOf(0));

    final int cnt = 10_000;
    final int level = 5_000;

    final Ref<HMRealTimeQueue<Integer>> q = new Ref<>(HMRealTimeQueue.empty());
    IntStream.range(0, cnt).forEach(n -> {
      final int k = Numeric.randomInt(1, level, rng);
      if (k <= level / 2) {
        if (len.r > 0) {
          --len.r;
          q.r = q.r.tail();
        }
      }
      else {
        ++len.r;
        q.r = q.r.addLast(k);
      }
      assertEquals(len.r, Integer.valueOf(q.r.size()));
    });
  }

  /**
   * Test of addLast method, of class HMRealTimeQueue.
   */
  @Test
  public void testAddLast() {
    System.out.println("addLast");

    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.singleton(1);
      assertFalse(q.isEmpty());
      assertEquals(1, q.size());
      assertEquals(Integer.valueOf(1), q.head());
    }
    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.singleton(1).addLast(2);
      assertFalse(q.isEmpty());
      assertEquals(2, q.size());
      assertEquals(Integer.valueOf(1), q.head());
      assertEquals(Integer.valueOf(2), q.tail().head());
      assertTrue(q.tail().tail().isEmpty());
    }
    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.of(1, 2, 3, 4, 5);
      assertEquals(Integer.valueOf(1), q.head());
      assertEquals(Integer.valueOf(2), q.tail().head());
      assertEquals(Integer.valueOf(3), q.tail().tail().head());
      assertEquals(Integer.valueOf(4), q.tail().tail().tail().head());
      assertEquals(Integer.valueOf(5), q.tail().tail().tail().tail().head());
    }
  }

  /**
   * Test of drop method, of class HMRealTimeQueue.
   */
  @Test
  public void testDrop() {
    System.out.println("drop");

    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.empty();
      assertTrue(q.isEmpty());
      boolean exceptionWasThrown = false;
      try {
        final HMRealTimeQueue<Integer> qp = q.drop(1);
      }
      catch (AssertionError ae) { exceptionWasThrown = true;}
      assertTrue(exceptionWasThrown);
    }
    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.singleton(1);
      assertFalse(q.isEmpty());
      boolean exceptionWasThrown = false;
      try {
        final HMRealTimeQueue<Integer> qp = q.drop(2);
      }
      catch (AssertionError ae) { exceptionWasThrown = true;}
      assertTrue(exceptionWasThrown);
    }
    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.singleton(1);
      assertFalse(q.isEmpty());
      assertTrue(q.drop(1).isEmpty());
      assertFalse(q.addLast(2).isEmpty());
      assertTrue(q.addLast(2).drop(2).isEmpty());
    }
  }

  /**
   * Test of take method, of class HMRealTimeQueue.
   */
  @Test
  public void testTake() {
    System.out.println("take");

    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.empty();
      assertTrue(q.isEmpty());
      boolean exceptionWasThrown = false;
      try {
        final ArrayList<Integer> list = q.take(1);
      }
      catch (AssertionError ae) { exceptionWasThrown = true;}
      assertTrue(exceptionWasThrown);
    }
    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.singleton(1);
      assertFalse(q.isEmpty());
      assertEquals(1, q.size());
      boolean exceptionWasThrown = false;
      try {
        final ArrayList<Integer> list = q.take(2);
      }
      catch (AssertionError ae) { exceptionWasThrown = true;}
      assertTrue(exceptionWasThrown);
    }
    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.singleton(1);
      assertFalse(q.isEmpty());
      assertEquals(1, q.size());
      boolean exceptionWasThrown = false;
      try {
        final ArrayList<Integer> list = q.take(1);
        assertEquals(1, list.size());
      }
      catch (AssertionError ae) { exceptionWasThrown = true;}
      assertFalse(exceptionWasThrown);
    }
    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.of(1,2,3,4,5);
      assertFalse(q.isEmpty());
      assertEquals(5, q.size());
      boolean exceptionWasThrown = false;
      try {
        final ArrayList<Integer> list = q.take(5);
        assertEquals(5, list.size());
      }
      catch (AssertionError ae) { exceptionWasThrown = true;}
      assertFalse(exceptionWasThrown);
    }
  }

  /**
   * Test of fromArray method, of class HMRealTimeQueue.
   */
  @Test
  public void testFromArray() {
    System.out.println("fromArray");

    {
      final ArrayList<Integer> v = new ArrayList<>();
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.fromArray(v);
      assertTrue(q.isEmpty());
    }
    {
      final ArrayList<Integer> v = new ArrayList<>();
      v.add(1);
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.fromArray(v);
      assertFalse(q.isEmpty());
      assertEquals(1, q.size());
      assertEquals(Integer.valueOf(1), q.head());
    }
    {
      final ArrayList<Integer> v = new ArrayList<>();
      v.add(1); v.add(2); v.add(3); v.add(4);
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.fromArray(v);
      assertFalse(q.isEmpty());
      assertEquals(4, q.size());
      assertEquals(Integer.valueOf(1), q.head());
      assertEquals(Integer.valueOf(2), q.tail().head());
      assertEquals(Integer.valueOf(3), q.tail().tail().head());
      assertEquals(Integer.valueOf(4), q.tail().tail().tail().head());
      assertTrue(q.tail().tail().tail().tail().isEmpty());
    }
  }

  /**
   * Test of fromStream method, of class HMRealTimeQueue.
   */
  @Test
  public void testFromStream() {
    System.out.println("fromStream");

    {
      final ArrayList<Integer> v = new ArrayList<>();
      final Stream<Integer> s = v.stream();
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.fromStream(s);
      assertTrue(q.isEmpty());
    }
    {
      final ArrayList<Integer> v = new ArrayList<>();
      v.add(1);
      final Stream<Integer> s = v.stream();
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.fromStream(s);
      assertFalse(q.isEmpty());
      assertEquals(1, q.size());
      assertEquals(Integer.valueOf(1), q.head());
    }
    {
      final ArrayList<Integer> v = new ArrayList<>();
      v.add(1); v.add(2); v.add(3); v.add(4);
      final Stream<Integer> s = v.stream();
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.fromStream(s);
      assertFalse(q.isEmpty());
      assertEquals(4, q.size());
      assertEquals(Integer.valueOf(1), q.head());
      assertEquals(Integer.valueOf(2), q.tail().head());
      assertEquals(Integer.valueOf(3), q.tail().tail().head());
      assertEquals(Integer.valueOf(4), q.tail().tail().tail().head());
      assertTrue(q.tail().tail().tail().tail().isEmpty());
    }
  }

  /**
   * Test of of method, of class HMRealTimeQueue.
   */
  @Test
  public void testOf() {
    System.out.println("of");

    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.of();
      assertTrue(q.isEmpty());
    }
    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.of(1);
      assertFalse(q.isEmpty());
      assertTrue(q.tail().isEmpty());
    }
    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.of(1, 2);
      assertFalse(q.isEmpty());
      assertEquals(Integer.valueOf(1), q.head());
      assertEquals(Integer.valueOf(2), q.tail().head());
      assertTrue(q.tail().tail().isEmpty());
    }
  }

  /**
   * Test of head method, of class HMRealTimeQueue.
   */
  @Test
  public void testHead() {
    System.out.println("head");

    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.of();
      boolean exceptionWasThrown = false;
      try {
        final Integer i = q.head();
      }
      catch (AssertionError ae) { exceptionWasThrown = true; }
      assertTrue(exceptionWasThrown);
    }
    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.of(1);
      boolean exceptionWasThrown = false;
      try {
        final Integer i = q.head();
        assertEquals(1, i.intValue());
      }
      catch (AssertionError ae) { exceptionWasThrown = true; }
      assertFalse(exceptionWasThrown);
    }
  }

  /**
   * Test of tail method, of class HMRealTimeQueue.
   */
  @Test
  public void testTail() {
    System.out.println("tail");

    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.of();
      boolean exceptionWasThrown = false;
      try {
        final HMRealTimeQueue<Integer> qt = q.tail();
      }
      catch (AssertionError ae) { exceptionWasThrown = true; }
      assertTrue(exceptionWasThrown);
    }
    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.of(1);
      boolean exceptionWasThrown = false;
      try {
        final HMRealTimeQueue<Integer> qt = q.tail();
        assertTrue(qt.isEmpty());
      }
      catch (AssertionError ae) { exceptionWasThrown = true; }
      assertFalse(exceptionWasThrown);
    }
  }

  /**
   * Test of tail method, of class HMRealTimeQueue.
   */
  @Test
  public void testNthTail() {
    System.out.println("nthTail");

    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.of();
      boolean exceptionWasThrown = false;
      try {
        final HMRealTimeQueue<Integer> qt = q.nthTail(0);
      }
      catch (AssertionError ae) { exceptionWasThrown = true; }
      assertFalse(exceptionWasThrown);
    }
    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.of();
      boolean exceptionWasThrown = false;
      try {
        final HMRealTimeQueue<Integer> qt = q.nthTail(1);
      }
      catch (AssertionError ae) { exceptionWasThrown = true; }
      assertTrue(exceptionWasThrown);
    }
    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.of(1);
      boolean exceptionWasThrown = false;
      try {
        final HMRealTimeQueue<Integer> qt = q.nthTail(1);
        assertTrue(qt.isEmpty());
      }
      catch (AssertionError ae) { exceptionWasThrown = true; }
      assertFalse(exceptionWasThrown);
    }
    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
      boolean exceptionWasThrown = false;
      HMRealTimeQueue<Integer> qt = null;
      try {
        qt = q.nthTail(9);
        assertFalse(qt.isEmpty());
      }
      catch (AssertionError ae) { exceptionWasThrown = true; }
      assertFalse(exceptionWasThrown);
      assertTrue(qt != null);
      assertEquals(Integer.valueOf(10), qt.head());
      assertTrue(qt.tail().isEmpty());
    }
  }

  /**
   * Test of contains method, of class HMRealTimeQueue.
   */
  @Test
  public void testContains() {
    System.out.println("contains");

    final Random rng = new Random(12371);

    final int iters = 10;
    final int nItemsInQueue = 500;

    IntStream.range(0, iters).forEach(n -> {
      final int[] v = Numeric.randomPermutation(1, nItemsInQueue, nItemsInQueue, rng);
      final HMRealTimeQueue<Integer> q = Functionals.reduce(Arrays.stream(v), HMRealTimeQueue.empty(), (qp, e) -> qp.addLast(e));

      Arrays.stream(v).forEach(i -> { assertTrue(q.contains(i)); });

      final HMRealTimeQueue<Integer> qp = q.tail();
      Arrays.stream(v).forEach(i -> {
        if (i == v[0]) {
          assertTrue(qp.notContains(i));
        }
        else {
          assertTrue(qp.contains(i));
        }
      });
    });
  }

  /**
   * Test of notContains method, of class HMRealTimeQueue.
   */
  @Test
  public void testNotContains() {
    System.out.println("notContains");

    final Random rng = new Random(12371);

    final int iters = 10;
    final int nItemsInQueue = 500;

    IntStream.range(0, iters).forEach(n -> {
      final int[] v = Numeric.randomPermutation(1, nItemsInQueue, nItemsInQueue, rng);
      final HMRealTimeQueue<Integer> q = Functionals.reduce(Arrays.stream(v), HMRealTimeQueue.empty(), (qp, e) -> qp.addLast(e));

      Arrays.stream(v).forEach(i -> { assertTrue(q.contains(i)); });

      final HMRealTimeQueue<Integer> qp = q.nthTail(nItemsInQueue / 2);
      Arrays.stream(v, 0, nItemsInQueue / 2).forEach(i -> { assertTrue(qp.notContains(i)); });
      Arrays.stream(v, nItemsInQueue / 2, nItemsInQueue).forEach(i -> { assertTrue(qp.contains(i)); });
    });
  }
}
