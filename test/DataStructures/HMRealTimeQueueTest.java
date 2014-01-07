/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import Utils.Numeric;
import Utils.Ref;
import java.util.ArrayList;
import java.util.Random;
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
    assertEquals(0, q.length());
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
    assertEquals(0, q.length());
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
    assertEquals(1, q.length());
    assertEquals(Integer.valueOf(1), q.head());
  }

  /**
   * Test of length method, of class HMRealTimeQueue.
   */
  @Test
  public void testLength() {
    System.out.println("length");

    final Random rng = new Random(12371);
    final Ref<Integer> len = new Ref<>(Integer.valueOf(0));

    final int cnt = 100_000;
    final int level = 5000;

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
      assertEquals(len.r, Integer.valueOf(q.r.length()));
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
      assertEquals(1, q.length());
      assertEquals(Integer.valueOf(1), q.head());
    }
    {
      final HMRealTimeQueue<Integer> q = HMRealTimeQueue.singleton(1).addLast(2);
      assertFalse(q.isEmpty());
      assertEquals(2, q.length());
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
      assertEquals(1, q.length());
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
      assertEquals(1, q.length());
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
      assertEquals(5, q.length());
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

    
  }

  /**
   * Test of fromStream method, of class HMRealTimeQueue.
   */
  @Test
  public void testFromStream() {
    System.out.println("fromStream");

    
  }

  /**
   * Test of of method, of class HMRealTimeQueue.
   */
  @Test
  public void testOf() {
    System.out.println("of");

    
  }

  /**
   * Test of head method, of class HMRealTimeQueue.
   */
  @Test
  public void testHead() {
    System.out.println("head");

    
  }

  /**
   * Test of tail method, of class HMRealTimeQueue.
   */
  @Test
  public void testTail() {
    System.out.println("tail");

    
  }

  /**
   * Test of contains method, of class HMRealTimeQueue.
   */
  @Test
  public void testContains() {
    System.out.println("contains");

    
  }

  /**
   * Test of notContains method, of class HMRealTimeQueue.
   */
  @Test
  public void testNotContains() {
    System.out.println("notContains");

    
  }
}
