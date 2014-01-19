/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.ArrayUtils;
import Utils.Functionals;
import Utils.Numeric;
import Utils.Ref;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
public class LeftistHeapTest {
  
  public LeftistHeapTest() {}
  
  @BeforeClass
  public static void setUpClass() {}
  
  @AfterClass
  public static void tearDownClass() {}
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}

  /**
   * Test of empty method, of class LeftistHeap.
   */
  @Test
  public void testEmpty() {
    System.out.println("empty");

    LeftistHeap<Integer> h = LeftistHeap.empty();
    assertTrue(h.isEmpty());
    boolean exceptionWasThrown = false;
    try {
      final Integer i = h.findMin();
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);
  }

  /**
   * Test of singleton method, of class LeftistHeap.
   */
  @Test
  public void testSingleton() {
    System.out.println("singleton");

    LeftistHeap<Integer> h = LeftistHeap.singleton(5);
    assertTrue(! h.isEmpty());
    assertEquals(1, h.size());
    assertEquals(Integer.valueOf(5), h.findMin());
  }

  /**
   * Test of isEmpty method, of class LeftistHeap.
   */
  @Test
  public void testIsEmpty() {
    System.out.println("isEmpty");

    LeftistHeap<Integer> h0 = LeftistHeap.empty();
    LeftistHeap<Integer> h1 = LeftistHeap.singleton(5);

    assertTrue(h0.isEmpty());
    assertFalse(h1.isEmpty());
  }

  /**
   * Test of findMin method, of class LeftistHeap.
   */
  @Test
  public void testFindMin() {
    System.out.println("findMin");

    {
      final LeftistHeap<Integer> h = LeftistHeap.empty();
      boolean exceptionWasThrown = false;
      try {
        final Integer ii = h.findMin();
      }
      catch (AssertionError ae) { exceptionWasThrown = true; }
      assertTrue(exceptionWasThrown);
    }
    {
      LeftistHeap<Integer> h = LeftistHeap.singleton(5);
      assertEquals(Integer.valueOf(5), h.findMin());
      h = h.insert(4);
      assertEquals(Integer.valueOf(4), h.findMin());
      h = h.insert(3);
      assertEquals(Integer.valueOf(3), h.findMin());
      h = h.insert(6);
      assertEquals(Integer.valueOf(3), h.findMin());
      h = h.insert(3);
      assertEquals(Integer.valueOf(3), h.findMin());
      h = h.insert(9);
      assertEquals(Integer.valueOf(3), h.findMin());
      h = h.insert(0);
      assertEquals(Integer.valueOf(0), h.findMin());
    }
  }
  /**
   * Test of merge method, of class LeftistHeap.
   */
  @Test
  public void testMerge() {
    System.out.println("merge");

    {
      final LeftistHeap<Integer> h0 = LeftistHeap.of(4,3,5,7);
      final LeftistHeap<Integer> h1 = LeftistHeap.of(1,-3,9,2);

      final LeftistHeap<Integer> res0 = h0.merge(h1);
      final LeftistHeap<Integer> res1 = h1.merge(h0);

      h0.forEach(n -> assertTrue(res0.contains(n) && res1.contains(n)));
      h1.forEach(n -> assertTrue(res0.contains(n) && res1.contains(n)));

      assertTrue(res0.equals(res1) && res1.equals(res0));
      assertEquals(res0.hashCode(), res1.hashCode());

      LeftistHeap<Integer> g0 = res0, g1 = res1;
      final Ref<Integer> r0 = new Ref<>(), r1 = new Ref<>();
      while (g0.isNotEmpty() && g1.isNotEmpty()) {
        g0 = g0.deleteMin(r0);
        g1 = g1.deleteMin(r1);

        assertEquals(r0.r, r1.r);
      }

      assertTrue(g0.isEmpty() && g1.isEmpty());
    }
  }

  private static void checkHeapProperties(final PersistentHeap<Integer, ?> heap,
                                          final Stream<Integer> present,
                                          final Stream<Integer> absent) {
    present.forEach(v -> { assertTrue(heap.contains(v)); });
    absent.forEach(v -> { assertTrue(heap.notContains(v)); });

    PersistentHeap<Integer, ?> h = heap;
    int maxSoFar = Integer.MIN_VALUE;
    final Ref<Integer> r = new Ref<>();
    while (h.isNotEmpty()) {
      h = h.deleteMin(r);
      assertTrue(r.r > maxSoFar);
      maxSoFar = r.r;
    }
  }

  /**
   * Test of insert method, of class LeftistHeap.
   */
  @Test
  public void testInsert() {
    System.out.println("insert");

    final Random rng = new Random(12371);

    final int cnt = 1000;
    final int low = 0, high = 500;
    final int[] v = new int[high - low + 1];

    IntStream.range(0, cnt).forEach(n -> {
      Numeric.randomPermutation(low, high, v, rng);
      final LeftistHeap<Integer> heap = Functionals.reduce(Arrays.stream(v), LeftistHeap.empty(), (h, i) -> h.insert(i));

      checkHeapProperties(heap, Arrays.stream(v).boxed(), Stream.empty());
    });
  }

  /**
   * Test of deleteMin method, of class LeftistHeap.
   */
  @Test
  public void testDeleteMin() {
    System.out.println("deleteMin");

    {
      final LeftistHeap<Integer> h = LeftistHeap.empty();
      boolean exceptionWasThrown = false;
      try {
        final LeftistHeap<Integer> hp = h.deleteMin();
      }
      catch (AssertionError ae) { exceptionWasThrown = true; }
      assertTrue(exceptionWasThrown);
    }
    {
      final LeftistHeap<Integer> h0 = LeftistHeap.of(1, 2, 3);
      final LeftistHeap<Integer> h1 = LeftistHeap.of(1, 3, 2);
      final LeftistHeap<Integer> h2 = LeftistHeap.of(2, 3, 1);
      final LeftistHeap<Integer> h3 = LeftistHeap.of(2, 1, 3);
      final LeftistHeap<Integer> h4 = LeftistHeap.of(3, 1, 2);
      final LeftistHeap<Integer> h5 = LeftistHeap.of(3, 2, 1);

      final Integer two = Integer.valueOf(2);
      assertEquals(two, h0.deleteMin().findMin());
      assertEquals(two, h1.deleteMin().findMin());
      assertEquals(two, h2.deleteMin().findMin());
      assertEquals(two, h3.deleteMin().findMin());
      assertEquals(two, h4.deleteMin().findMin());
      assertEquals(two, h5.deleteMin().findMin());
    }
    {
      final Random rng = new Random(12371);

      final int cnt = 100;
      final int low = 0, high = 500;              // Inclusive
      final int[] v = new int[high - low + 1];

      IntStream.range(0, cnt).forEach(n -> {
        Numeric.randomPermutation(low, high, v, rng);
        final LeftistHeap<Integer> heap =
                Functionals.reduce(Arrays.stream(v),
                                   LeftistHeap.empty(),
                                   (h, i) -> h.insert(i));

        checkHeapProperties(heap,
                            Arrays.stream(v).boxed(),
                            Stream.empty());

        final ArrayList<Integer> sortedV = ArrayUtils.toArrayList(Arrays.stream(v).boxed());
        sortedV.sort(null);

        final int delUpTo = Numeric.randomInt(low, high + 1, rng);

        final ArrayList<Integer> vMin = new ArrayList<>(), vMax = new ArrayList<>();
        final Ref<Integer> ref = new Ref<>();

        final LeftistHeap<Integer> delHeap =
                Functionals.reduce(Arrays.stream(v, 0, delUpTo),
                                   heap,
                                   (h, i) -> {
                                     final LeftistHeap<Integer> hh = h.deleteMin(ref);
                                     vMin.add(ref.r);
                                     return hh;
                                   });

        Arrays.stream(v).forEach(m -> { if (! vMin.contains(m)) { vMax.add(m); }});
        checkHeapProperties(delHeap, vMax.stream(), vMin.stream());

        final Ref<Integer> count = new Ref<>(Integer.valueOf(0));
        Functionals.zip(sortedV.stream(), vMin.stream(), Pair::create).peek(p -> { ++count.r; }).forEach(p -> { assertEquals(p.mx1, p.mx2); });
        assertEquals(Integer.valueOf(vMin.size()), count.r);
      });
    }
  }

  /**
   * Test of size method, of class LeftistHeap.
   */
  @Test
  public void testSize() {
    System.out.println("size");

    {
      final LeftistHeap<Integer> h0 = LeftistHeap.empty();
      assertEquals(0, h0.size());
      final LeftistHeap<Integer> h1 = h0.insert(1);
      assertEquals(1, h1.size());
      final LeftistHeap<Integer> h2 = h1.insert(2);
      assertEquals(2, h2.size());
    }
    {
      final LeftistHeap<Integer> h = LeftistHeap.of(1, 2, 3, 4, 5).merge(LeftistHeap.of(6, 7, 8, 9, 10));
      assertEquals(10, h.size());
    }
  }

  /**
   * Test of fromStream method, of class LeftistHeap.
   */
  @Test
  public void testFromStream() {
    System.out.println("fromStream");

    {
      final LeftistHeap<Integer> h = LeftistHeap.fromStream(IntStream.range(0, 0).boxed());
      assertTrue(h.isEmpty());
    }
    {
      final LeftistHeap<Integer> h = LeftistHeap.fromStream(IntStream.range(0, 10).boxed());
      assertFalse(h.isEmpty());
      assertEquals(10, h.size());
    }
  }

  /**
   * Test of fromArray method, of class LeftistHeap.
   */
  @Test
  public void testFromArray() {
    System.out.println("fromArray");

    {
      final ArrayList<Integer> v = new ArrayList<>();
      final LeftistHeap<Integer> h = LeftistHeap.fromArray(v);
      assertTrue(h.isEmpty());
    }
    {
      final ArrayList<Integer> v = ArrayUtils.toArrayList(IntStream.range(0, 10).boxed());
      final LeftistHeap<Integer> h = LeftistHeap.fromArray(v);
      assertFalse(h.isEmpty());
      assertEquals(10, h.size());
    }
  }

  /**
   * Test of toArrayList method, of class LeftistHeap.
   */
  @Test
  public void testToArrayList() {
    System.out.println("toArrayList");

    {
      final LeftistHeap<Integer> h = LeftistHeap.empty();
      final ArrayList<Integer> v = h.toArrayList();
      assertEquals(new ArrayList<>(), v);
    }
    {
      final LeftistHeap<Integer> h = LeftistHeap.of(1);
      final ArrayList<Integer> v = h.toArrayList();
      final ArrayList<Integer> expected = new ArrayList<>();
      expected.add(1);
      assertEquals(expected, v);
    }
    {
      final LeftistHeap<Integer> h = LeftistHeap.of(1, 2, 3, 4);
      final ArrayList<Integer> v = h.toArrayList();
      final ArrayList<Integer> expected = new ArrayList<>();
      expected.add(1); expected.add(2); expected.add(3); expected.add(4);
      assertEquals(expected, v);
    }
  }

  /**
   * Test of stream method, of class LeftistHeap.
   */
  @Test
  public void testStream() {
    System.out.println("stream");

    {
      final LeftistHeap<Integer> h = LeftistHeap.empty();
      final Stream<Integer> s = h.stream();
      final ArrayList<Integer> v = ArrayUtils.toArrayList(s);
      assertTrue(v.isEmpty());
    }
    {
      final LeftistHeap<Integer> h = LeftistHeap.of(10);
      final Stream<Integer> s = h.stream();
      final ArrayList<Integer> v = ArrayUtils.toArrayList(s);
      assertFalse(v.isEmpty());
      assertEquals(1, v.size());
      assertEquals(Integer.valueOf(10), v.get(0));
    }
    {
      final LeftistHeap<Integer> h = LeftistHeap.of(10, 20, 30, 40);
      final Stream<Integer> s = h.stream();
      final ArrayList<Integer> v = ArrayUtils.toArrayList(s);
      assertFalse(v.isEmpty());
      assertEquals(4, v.size());

      assertEquals(Integer.valueOf(10), v.get(0));
      assertEquals(Integer.valueOf(20), v.get(1));
      assertEquals(Integer.valueOf(30), v.get(2));
      assertEquals(Integer.valueOf(40), v.get(3));
    }
  }

  /**
   * Test of stream method, of class LeftistHeap.
   */
  @Test
  public void testOrderedStream() {
    System.out.println("orderedStream");

    {
      final LeftistHeap<Integer> h = LeftistHeap.empty();
      final Stream<Integer> s = h.orderedStream();
      final ArrayList<Integer> v = ArrayUtils.toArrayList(s);
      assertTrue(v.isEmpty());
    }
    {
      final LeftistHeap<Integer> h = LeftistHeap.of(10);
      final Stream<Integer> s = h.orderedStream();
      final ArrayList<Integer> v = ArrayUtils.toArrayList(s);
      assertFalse(v.isEmpty());
      assertEquals(1, v.size());
      assertEquals(Integer.valueOf(10), v.get(0));
    }
    {
      final LeftistHeap<Integer> h = LeftistHeap.of(20, 10, 0, -40, 30);
      final Stream<Integer> s = h.orderedStream();
      final ArrayList<Integer> v = ArrayUtils.toArrayList(s);
      assertFalse(v.isEmpty());
      assertEquals(5, v.size());

      assertEquals(Integer.valueOf(-40), v.get(0));
      assertEquals(Integer.valueOf(0), v.get(1));
      assertEquals(Integer.valueOf(10), v.get(2));
      assertEquals(Integer.valueOf(20), v.get(3));
      assertEquals(Integer.valueOf(30), v.get(4));
    }
  }

  /**
   * Test of toAscArrayList method, of class LeftistHeap.
   */
  @Test
  public void testToAscArrayList() {
    System.out.println("toAscArrayList");

    {
      final LeftistHeap<Integer> h = LeftistHeap.empty();
      final ArrayList<Integer> v = h.toAscArrayList();
      assertTrue(v.isEmpty());
    }
    {
      final LeftistHeap<Integer> h = LeftistHeap.of(10);
      final ArrayList<Integer> v = h.toAscArrayList();
      assertFalse(v.isEmpty());
      assertEquals(1, v.size());
      assertEquals(Integer.valueOf(10), v.get(0));
    }
    {
      final LeftistHeap<Integer> h = LeftistHeap.of(30, 10, 40, 20);
      final ArrayList<Integer> v = h.toAscArrayList();
      assertFalse(v.isEmpty());
      assertEquals(4, v.size());

      assertEquals(Integer.valueOf(10), v.get(0));
      assertEquals(Integer.valueOf(20), v.get(1));
      assertEquals(Integer.valueOf(30), v.get(2));
      assertEquals(Integer.valueOf(40), v.get(3));
    }
  }

  /**
   * Test of toDescArrayList method, of class LeftistHeap.
   */
  @Test
  public void testToDescArrayList() {
    System.out.println("toDescArrayList");

    {
      final LeftistHeap<Integer> h = LeftistHeap.empty();
      final ArrayList<Integer> v = h.toDescArrayList();
      assertTrue(v.isEmpty());
    }
    {
      final LeftistHeap<Integer> h = LeftistHeap.of(10);
      final ArrayList<Integer> v = h.toDescArrayList();
      assertFalse(v.isEmpty());
      assertEquals(1, v.size());
      assertEquals(Integer.valueOf(10), v.get(0));
    }
    {
      final LeftistHeap<Integer> h = LeftistHeap.of(30, 10, 40, 20);
      final ArrayList<Integer> v = h.toDescArrayList();
      assertFalse(v.isEmpty());
      assertEquals(4, v.size());

      assertEquals(Integer.valueOf(40), v.get(0));
      assertEquals(Integer.valueOf(30), v.get(1));
      assertEquals(Integer.valueOf(20), v.get(2));
      assertEquals(Integer.valueOf(10), v.get(3));
    }
  }

  /**
   * Test of iterator method, of class LeftistHeap.
   */
  @Test
  public void testIterator() {
    System.out.println("iterator");

    {
      final LeftistHeap<Integer> h = LeftistHeap.empty();
      int s = 0;
      for(Integer i : h) { s += i; }
      assertEquals(0, s);

      final Iterator<Integer> it = h.iterator();
      assertFalse(it.hasNext());
    }
    {
      final LeftistHeap<Integer> h = LeftistHeap.of(10);
      int s = 0;
      for (Integer i : h) { s += i; }
      assertEquals(10, s);

      final Iterator<Integer> it = h.iterator();
      assertTrue(it.hasNext());
      assertEquals(Integer.valueOf(10), it.next());
      assertFalse(it.hasNext());
    }
    {
      final LeftistHeap<Integer> h = LeftistHeap.of(30, 10, 40, 20);
      int s = 0;
      for (Integer i : h) { s += i; }
      assertEquals(10 + 20 + 30 + 40, s);
    }
  }
}
