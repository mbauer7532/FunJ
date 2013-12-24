/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.Ref;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.stream.Collectors;
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
public class LinkedListTest {
  public LinkedListTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of empty method, of class LinkedList.
   */
  @Test
  public void testEmpty() {
    System.out.println("empty");

    final LinkedList<Integer> l0 = LinkedList.empty();
    final LinkedList<Integer> l1 = l0.cons(1);
    final LinkedList<Integer> l2 = l1.cons(2);
    final LinkedList<Integer> l3 = l2.cons(3);

    assertTrue(l0.isEmpty());
    assertFalse(l1.isEmpty());
    assertFalse(l2.isEmpty());
    assertFalse(l3.isEmpty());
  }

  /**
   * Test of cons method, of class LinkedList.
   */
  @Test
  public void testCons() {
    System.out.println("cons");
    final LinkedList<Integer> l = LinkedList.<Integer> empty().cons(3).cons(2).cons(1);
    
    assertEquals((Integer) 1, l.head());
    assertEquals((Integer) 2, l.tail().head());
    assertEquals((Integer) 3, l.tail().tail().head());
    assertTrue(l.tail().tail().tail().isEmpty());
  }

  /**
   * Test of append method, of class LinkedList.
   */
  @Test
  public void testAppend() {
    System.out.println("append");
    final LinkedList<Integer> l1 = LinkedList.<Integer> empty().cons(2).cons(1).cons(0);
    final LinkedList<Integer> l2 = LinkedList.<Integer> empty().cons(5).cons(4).cons(3);
    final LinkedList<Integer> l3 = l1.append(l2);

    assertEquals(l3.length(), 6);
    IntStream.range(0, 6).forEach(i -> { assertEquals((Integer) i, l3.nth(i)); });

    final LinkedList<Integer> e = LinkedList.empty();
    assertEquals(e.append(l1), l1.append(e));
    assertEquals(e.append(l1), l1);
    assertEquals(l1.append(e), l1);
    assertEquals(e.append(l1).length(), l1.length());
    assertEquals(e.append(l1).append(e).append(e).length(), l1.length());
  }

  /**
   * Test of head method, of class LinkedList.
   */
  @Test
  public void testHead() {
    System.out.println("head");

    final LinkedList<Integer> e = LinkedList.empty();
    boolean exceptionWasThrown = false;
    try {
      final Integer n = e.head();
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);

    final LinkedList<Integer> oneElem = e.cons(1);
    assertEquals(oneElem.head(), (Integer) 1);
    assertEquals(oneElem.cons(2).head(), (Integer) 2);
  }

  /**
   * Test of tail method, of class LinkedList.
   */
  @Test
  public void testTail() {
    System.out.println("tail");

    final LinkedList<Integer> e = LinkedList.empty();
    boolean exceptionWasThrown = false;
    try {
      final LinkedList<Integer> ls = e.tail();
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);
    
    final LinkedList<Integer> oneElem = e.cons(1);
    assertEquals(oneElem.tail(), e);
    assertEquals(oneElem.cons(2).tail(), oneElem);
  }

  /**
   * Test of last method, of class LinkedList.
   */
  @Test
  public void testLast() {
    System.out.println("last");

    {
      final LinkedList<Integer> list = LinkedList.fromStream(IntStream.rangeClosed(0, 10).boxed());
      assertEquals(list.last(), (Integer) 10);
    }
    {
      final LinkedList<Integer> list = LinkedList.<Integer> empty().cons(1);
      assertEquals(list.last(), (Integer) 1);
    }
    {
      boolean exceptionWasThrown = false;
      try {
        final LinkedList<Integer> list = LinkedList.<Integer> empty();
        final Integer i = list.last();
      }
      catch (AssertionError ae) { exceptionWasThrown = true; }
      assertTrue(exceptionWasThrown);
    }
  }
  /**
   * Test of init method, of class LinkedList.
   */
  @Test
  public void testInit() {
    System.out.println("init");
   {
      final LinkedList<Integer> list = LinkedList.fromStream(IntStream.rangeClosed(0, 10).boxed());
      assertEquals(list.init(), LinkedList.fromStream(IntStream.rangeClosed(0, 9).boxed()));
      System.out.println(list.init());
    }
    {
      final LinkedList<Integer> list = LinkedList.<Integer> empty().cons(1);
      assertEquals(list.init(), LinkedList.<Integer> empty());
    }
    {
      boolean exceptionWasThrown = false;
      try {
        final LinkedList<Integer> list = LinkedList.<Integer> empty();
        final LinkedList<Integer> i = list.init();
      }
      catch (AssertionError ae) { exceptionWasThrown = true; }
      assertTrue(exceptionWasThrown);
    }
  }

  /**
   * Test of isEmpty method, of class LinkedList.
   */
  @Test
  public void testIsEmpty() {
    System.out.println("isEmpty");
    final LinkedList<Integer> e = LinkedList.empty();
    assertTrue(e.isEmpty());
    assertFalse(e.addFirst(1).isEmpty());
    assertTrue(e.addLast(2).addFirst(1).tail().tail().isEmpty());
    assertFalse(e.addLast(2).addFirst(1).tail().isEmpty());
  }

  /**
   * Test of length method, of class LinkedList.
   */
  @Test
  public void testLength() {
    System.out.println("length");
    final LinkedList<Integer> e = LinkedList.empty();
    assertEquals(e.length(), 0);
    assertEquals(e.cons(0).length(), 1);
    
    assertEquals(LinkedList.fromStream(IntStream.rangeClosed(1, 10).boxed()).length(), 10);
  }

  /**
   * Test of forEach method, of class LinkedList.
   */
  @Test
  public void testForEach() {
    System.out.println("forEach");

    final LinkedList<Integer> e = LinkedList.empty();
    final Ref<Integer> r = new Ref<>(0);
    e.forEach(n -> { r.r = 1; });
    assertTrue(r.r == 0);

    final Ref<String> s = new Ref("");
    LinkedList.fromStream(IntStream.range(1, 10).boxed()).forEach(n -> s.r = s.r + n.toString());
    assertEquals(s.r, "123456789");
  }

  /**
   * Test of map method, of class LinkedList.
   */
  @Test
  public void testMap() {
    System.out.println("map");

    final LinkedList<Integer> e = LinkedList.empty();
    final LinkedList<Integer> eMaped = e.map(n -> n * n);
    assertEquals(e, eMaped);
    assertTrue(eMaped.isEmpty());

    assertEquals(e.cons(1), e.cons(1).map(n -> n));

    final LinkedList<Integer> ls = LinkedList.fromStream(IntStream.rangeClosed(1,10).boxed());
    final  LinkedList<Integer> lsMaped = ls.map(n -> n * n);

    assertEquals(ls.length(), 10);
    assertEquals(lsMaped.length(), 10);
    assertEquals(lsMaped.toString(), "[1, 4, 9, 16, 25, 36, 49, 64, 81, 100]");
  }

  /**
   * Test of reverse method, of class LinkedList.
   */
  @Test
  public void testReverse() {
    System.out.println("reverse");

    final LinkedList<Integer> e = LinkedList.empty();
    final LinkedList<Integer> eReversed = e.reverse();
    assertEquals(e, eReversed);
    assertTrue(eReversed.isEmpty());

    assertEquals(e.cons(1), e.cons(1).reverse());
    
    final LinkedList<Integer> ls = LinkedList.fromStream(IntStream.rangeClosed(1,10).boxed());
    final  LinkedList<Integer> lsReversed = ls.reverse();

    assertEquals(ls.length(), 10);
    assertEquals(lsReversed.length(), 10);
    assertEquals(lsReversed.toString(), "[10, 9, 8, 7, 6, 5, 4, 3, 2, 1]");
  }

  /**
   * Test of intersperse method, of class LinkedList.
   */
  @Test
  public void testIntersperse() {
    System.out.println("intersperse");

    final LinkedList<Integer> e = LinkedList.empty();
    assertEquals(e, e.intersperse(0));
    assertEquals(e.cons(2), e.cons(2).intersperse(0));
    assertEquals(e.cons(2).cons(0).cons(3), e.cons(2).cons(3).intersperse(0));

    final LinkedList<Integer> ls = LinkedList.fromStream(IntStream.rangeClosed(1,10).boxed());
    final  LinkedList<Integer> lsReversed = ls.reverse().intersperse(0);
    assertEquals(lsReversed.toString(), "[10, 0, 9, 0, 8, 0, 7, 0, 6, 0, 5, 0, 4, 0, 3, 0, 2, 0, 1]");
  }

  /**
   * Test of foldl method, of class LinkedList.
   */
  @Test
  public void testFoldl() {
    System.out.println("foldl");

    final LinkedList<Integer> e = LinkedList.empty();
    assertEquals("x", e.foldl((res, i) -> Integer.toString(i) + res, "x"));
    assertEquals("1x", e.cons(1).foldl((res, i) -> Integer.toString(i) + res, "x"));
    assertEquals("21x", e.cons(2).cons(1).foldl((res, i) -> Integer.toString(i) + res, "x"));

    assertEquals((Integer) 123456789,
                 LinkedList.fromStream(IntStream.range(1, 10).boxed()).foldl((res, i) -> res * 10 + i, 0));
  }

  /**
   * Test of foldl1 method, of class LinkedList.
   */
  @Test
  public void testFoldl1() {
    System.out.println("foldl1");

    final LinkedList<Integer> e = LinkedList.empty();
    boolean exceptionWasThrown = false;
    try {
      final Integer s = e.foldl1((res, i) -> i * 10 + res);
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);

    final Integer ii = e.cons(7).foldl1((res, i) -> i * 10 + res);
    assertEquals((Integer) 7, ii);

    assertEquals((Integer) 123456789,
                 LinkedList.fromStream(IntStream.range(1, 10).boxed()).foldl1((res, i) -> res * 10 + i));
  }

  /**
   * Test of foldr method, of class LinkedList.
   */
  @Test
  public void testFoldr() {
    System.out.println("foldr");

    final LinkedList<Integer> e = LinkedList.empty();
    assertEquals("x", e.foldr((i, res) -> Integer.toString(i) + res, "x"));
    assertEquals("1x", e.cons(1).foldr((i, res) -> Integer.toString(i) + res, "x"));
    assertEquals("12x", e.cons(2).cons(1).foldr((i,res) -> Integer.toString(i) + res, "x"));

    assertEquals((Integer) 987654321,
                 LinkedList.fromStream(IntStream.range(1, 10).boxed()).foldr((i, res) -> res * 10 + i, 0));
  }

  /**
   * Test of foldr1 method, of class LinkedList.
   */
  @Test
  public void testFoldr1() {
    System.out.println("foldr1");

    final LinkedList<Integer> e = LinkedList.empty();
    boolean exceptionWasThrown = false;
    try {
      final Integer s = e.foldr1((i, res) -> i * 10 + res);
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);

    final Integer ii = e.cons(7).foldr1((i, res) -> i * 10 + res);
    assertEquals((Integer) 7, ii);

    assertEquals((Integer) 987654321,
                 LinkedList.fromStream(IntStream.range(1, 10).boxed()).foldr1((i, res) -> res * 10 + i));
  }

  /**
   * Test of any method, of class LinkedList.
   */
  @Test
  public void testAny() {
    System.out.println("any");

    final LinkedList<Integer> e = LinkedList.empty();
    assertFalse(e.any(n -> n % 2 == 0));
    assertTrue(e.cons(2).any(n -> n % 2 == 0));
    assertTrue(e.cons(1).cons(2).cons(1).any(n -> n % 2 == 0));
    assertTrue(e.cons(1).cons(1).cons(2).any(n -> n % 2 == 0));
    assertTrue(e.cons(2).cons(1).cons(1).any(n -> n % 2 == 0));
    assertFalse(e.cons(1).cons(1).cons(1).any(n -> n % 2 == 0));
  }

  /**
   * Test of all method, of class LinkedList.
   */
  @Test
  public void testAll() {
    System.out.println("all");

    final LinkedList<Integer> e = LinkedList.empty();
    assertTrue(e.all(n -> n % 2 == 0));
    assertTrue(e.cons(2).all(n -> n % 2 == 0));
    assertFalse(e.cons(1).cons(2).cons(1).all(n -> n % 2 == 0));
    assertFalse(e.cons(2).cons(1).cons(2).all(n -> n % 2 == 0));
    assertFalse(e.cons(2).cons(1).cons(1).all(n -> n % 2 == 0));
    assertTrue(e.cons(2).cons(2).cons(2).all(n -> n % 2 == 0));
  }

  /**
   * Test of scanl method, of class LinkedList.
   */
  @Test
  public void testScanl() {
    System.out.println("scanl");
//    LinkedList instance = null;
//    LinkedList expResult = null;
////    LinkedList result = instance.scanl(null);
////    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of scanl1 method, of class LinkedList.
   */
  @Test
  public void testScanl1() {
    System.out.println("scanl1");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.scanl1(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of scanr method, of class LinkedList.
   */
  @Test
  public void testScanr() {
    System.out.println("scanr");
//    LinkedList instance = null;
//    LinkedList expResult = null;
////    LinkedList result = instance.scanr(null);
////    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of scanr1 method, of class LinkedList.
   */
  @Test
  public void testScanr1() {
    System.out.println("scanr1");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.scanr1(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of mapAccumL method, of class LinkedList.
   */
  @Test
  public void testMapAccumL() {
    System.out.println("mapAccumL");
//    LinkedList instance = null;
//    TuplesModule.Pair expResult = null;
////    TuplesModule.Pair result = instance.mapAccumL(null);
////    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of mapAccumR method, of class LinkedList.
   */
  @Test
  public void testMapAccumR() {
    System.out.println("mapAccumR");
//    LinkedList instance = null;
//    TuplesModule.Pair expResult = null;
////    TuplesModule.Pair result = instance.mapAccumR(null);
////    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of take method, of class LinkedList.
   */
  @Test
  public void testTake() {
    System.out.println("take");

    final LinkedList<Integer> e = LinkedList.empty();
    assertEquals(e, e.take(1));
    assertEquals(e.cons(1), e.cons(2).cons(1).take(1));
    assertEquals(e.cons(3).cons(2).cons(1), e.cons(3).cons(2).cons(1).take(3));
    assertEquals(e.cons(1), e.cons(1).take(10));
    
    boolean exceptionWasThrown = false;
    try {
      e.take(-1);
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);
  }

  /**
   * Test of drop method, of class LinkedList.
   */
  @Test
  public void testDrop() {
    System.out.println("drop");

    final LinkedList<Integer> e = LinkedList.empty();
    assertEquals(e, e.drop(1));
    assertEquals(e.cons(2), e.cons(2).cons(1).drop(1));
    assertEquals(e, e.cons(3).cons(2).cons(1).drop(3));
    assertEquals(e.cons(3), e.cons(3).cons(2).cons(1).drop(2));
    assertEquals(e, e.cons(1).drop(10));
    
    boolean exceptionWasThrown = false;
    try {
      e.drop(-1);
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);
  }

  /**
   * Test of splitAt method, of class LinkedList.
   */
  @Test
  public void testSplitAt() {
    System.out.println("splitAt");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      final Pair<LinkedList<Integer>, LinkedList<Integer>> p = e.splitAt(0);
      assertEquals(p.mx1, e);
      assertEquals(p.mx2, e);
    }
    {
      final Pair<LinkedList<Integer>, LinkedList<Integer>> p = e.splitAt(1);
      assertEquals(p.mx1, e);
      assertEquals(p.mx2, e);
    }
    {
      final LinkedList<Integer> ls = e.cons(1);
      final Pair<LinkedList<Integer>, LinkedList<Integer>> p0 = ls.splitAt(0);
      final Pair<LinkedList<Integer>, LinkedList<Integer>> p1 = ls.splitAt(1);
      assertEquals(p0.mx1, e);
      assertEquals(p0.mx2, ls);
      assertEquals(p1.mx1, ls);
      assertEquals(p1.mx2, e);
    }
    {
      final LinkedList<Integer> ls = e.cons(2).cons(1);
      final Pair<LinkedList<Integer>, LinkedList<Integer>> p0 = ls.splitAt(0);
      final Pair<LinkedList<Integer>, LinkedList<Integer>> p1 = ls.splitAt(1);
      final Pair<LinkedList<Integer>, LinkedList<Integer>> p2 = ls.splitAt(2);

      assertEquals(p0.mx1, e);
      assertEquals(p0.mx2, ls);
      assertEquals(p1.mx1, e.cons(1));
      assertEquals(p1.mx2, e.cons(2));
      assertEquals(p2.mx1, ls);
      assertEquals(p2.mx2, e);
    }
    {
      final int low = 5, high = 500;
      final int cnt = high - low + 1;
      final LinkedList<Integer> ls = LinkedList.fromStream(IntStream.rangeClosed(low, high).boxed());
      for (int n = 0; n != cnt + 1; ++n) {
        final Pair<LinkedList<Integer>, LinkedList<Integer>> p = ls.splitAt(n);
        assertEquals(p.mx1.length(), n);
        assertEquals(p.mx2.length(), cnt - n);
      }
    }
    {
      final Pair<LinkedList<Integer>, LinkedList<Integer>> p = e.cons(1).splitAt(-1);
      assertEquals(p.mx1, e);
      assertEquals(p.mx2, e.cons(1));
    }
  }

  /**
   * Test of takeWhile method, of class LinkedList.
   */
  @Test
  public void testTakeWhile() {
    System.out.println("takeWhile");
    
    final LinkedList<Integer> e = LinkedList.empty();
    {
      assertEquals(e, e.takeWhile(n -> true));
      assertEquals(e, e.takeWhile(n -> false));
    }
    {
      assertEquals(e.cons(1), e.cons(1).takeWhile(n -> n == 1));
      assertEquals(e, e.cons(1).takeWhile(n -> n == 2));
    }
    {
      final LinkedList<Integer> ls = LinkedList.fromStream(IntStream.range(0, 10).boxed());
      
      assertEquals(e, ls.takeWhile(n -> false));
      assertEquals(ls, ls.takeWhile(n -> true));
      assertEquals(e.cons(1).cons(0), ls.takeWhile(n -> n < 2));
    }
  }

  /**
   * Test of dropWhile method, of class LinkedList.
   */
  @Test
  public void testDropWhile() {
    System.out.println("dropWhile");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      assertEquals(e, e.dropWhile(n -> true));
      assertEquals(e, e.dropWhile(n -> false));
    }
    {
      assertEquals(e, e.cons(1).dropWhile(n -> n == 1));
      assertEquals(e.cons(1), e.cons(1).dropWhile(n -> n == 2));
    }
    {
      final LinkedList<Integer> ls = LinkedList.fromStream(IntStream.range(0, 10).boxed());
      assertEquals(ls, ls.dropWhile(n -> false));
      assertEquals(e, ls.dropWhile(n -> true));
      assertEquals(ls.tail().tail(), ls.dropWhile(n -> n < 2));
    }
  }

  /**
   * Test of dropWhileEnd method, of class LinkedList.
   */
  @Test
  public void testDropWhileEnd() {
    System.out.println("dropWhileEnd");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.dropWhileEnd(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of spanByPredicate method, of class LinkedList.
   */
  @Test
  public void testSpanByPredicate() {
    System.out.println("spanByPredicate");
//    LinkedList instance = null;
//    TuplesModule.Pair<LinkedList<A>, LinkedList<A>> expResult = null;
//    TuplesModule.Pair<LinkedList<A>, LinkedList<A>> result = instance.spanByPredicate(null);
//    assertEquals(expResult, result);
//    TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of breakByPredicate method, of class LinkedList.
   */
  @Test
  public void testBreakByPredicate() {
    System.out.println("breakByPredicate");
//    LinkedList instance = null;
//    TuplesModule.Pair<LinkedList<A>, LinkedList<A>> expResult = null;
//    TuplesModule.Pair<LinkedList<A>, LinkedList<A>> result = instance.breakByPredicate(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of stripPrefix method, of class LinkedList.
   */
  @Test
  public void testStripPrefix() {
    System.out.println("stripPrefix");
//    LinkedList instance = null;
//    Optional<LinkedList<A>> expResult = null;
//    Optional<LinkedList<A>> result = instance.stripPrefix(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of group method, of class LinkedList.
   */
  @Test
  public void testGroup() {
    System.out.println("group");
//    LinkedList instance = null;
//    LinkedList<LinkedList<A>> expResult = null;
//    LinkedList<LinkedList<A>> result = instance.group();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of groupBy method, of class LinkedList.
   */
  @Test
  public void testGroupBy() {
    System.out.println("groupBy");
//    LinkedList instance = null;
//    LinkedList<LinkedList<A>> expResult = null;
//    LinkedList<LinkedList<A>> result = instance.groupBy(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of inits method, of class LinkedList.
   */
  @Test
  public void testInits() {
    System.out.println("inits");
//    LinkedList instance = null;
//    LinkedList<LinkedList<A>> expResult = null;
//    LinkedList<LinkedList<A>> result = instance.inits();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of tails method, of class LinkedList.
   */
  @Test
  public void testTails() {
    System.out.println("tails");
//    LinkedList instance = null;
//    LinkedList<LinkedList<A>> expResult = null;
//    LinkedList<LinkedList<A>> result = instance.tails();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of isPrefixOf method, of class LinkedList.
   */
  @Test
  public void testIsPrefixOf() {
    System.out.println("isPrefixOf");
//    LinkedList instance = null;
//    boolean expResult = false;
//    boolean result = instance.isPrefixOf(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of isSuffixOf method, of class LinkedList.
   */
  @Test
  public void testIsSuffixOf() {
    System.out.println("isSuffixOf");
//    LinkedList instance = null;
//    boolean expResult = false;
//    boolean result = instance.isSuffixOf(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of isInfixOf method, of class LinkedList.
   */
  @Test
  public void testIsInfixOf() {
    System.out.println("isInfixOf");
//    LinkedList instance = null;
//    boolean expResult = false;
//    boolean result = instance.isInfixOf(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of elem method, of class LinkedList.
   */
  @Test
  public void testElem() {
    System.out.println("elem");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      assertFalse(e.elem(1));
      assertFalse(e.cons(0).elem(1));
      assertTrue(e.cons(1).elem(1));
      assertTrue(e.cons(1).cons(2).elem(1));
      assertTrue(e.cons(1).cons(2).elem(2));
    }
    {
      final LinkedList<Integer> ls = LinkedList.fromStream(IntStream.range(0, 100).boxed());
      IntStream.range(0, 100).forEach(n -> assertTrue(ls.elem(n)));

      assertFalse(ls.elem(-1));
      assertFalse(ls.elem(100));
    }
  }

  /**
   * Test of notElem method, of class LinkedList.
   */
  @Test
  public void testNotElem() {
    System.out.println("notElem");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      assertTrue(e.notElem(1));
      assertTrue(e.cons(0).notElem(1));
      assertFalse(e.cons(1).notElem(1));
      assertFalse(e.cons(1).cons(2).notElem(1));
      assertFalse(e.cons(1).cons(2).notElem(2));
    }
    {
      final LinkedList<Integer> ls = LinkedList.fromStream(IntStream.range(0, 100).boxed());
      IntStream.range(0, 100).forEach(n -> assertFalse(ls.notElem(n)));

      assertTrue(ls.notElem(-1));
      assertTrue(ls.notElem(100));
    }
  }

  /**
   * Test of find method, of class LinkedList.
   */
  @Test
  public void testFind() {
    System.out.println("find");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      final Optional<Integer> res = e.find(n -> true);
      assertEquals(Optional.empty(), res);
    }
    {
      final LinkedList<Integer> ls = e.cons(2).cons(1);
      assertEquals(Integer.valueOf(2), ls.find(n -> n == 2).get());
      assertEquals(Integer.valueOf(1), ls.find(n -> n == 1).get());
      assertFalse(ls.find(n -> n == 0).isPresent());
    }
    {
      final LinkedList<Integer> ls = LinkedList.fromStream(IntStream.range(0, 100).boxed());
      IntStream.range(0, 100).forEach(n -> assertTrue(ls.find(x -> x == n).isPresent()));

      assertFalse(ls.find(x -> x == -1).isPresent());
      assertFalse(ls.find(x -> x == 100).isPresent());
    }
  }

  /**
   * Test of filter method, of class LinkedList.
   */
  @Test
  public void testFilter() {
    System.out.println("filter");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      assertEquals(e, e.filter(n -> true));
      assertEquals(e, e.filter(n -> false));
    }
    {
      final LinkedList<Integer> ls = e.cons(2).cons(1);
      assertEquals(e.cons(2), ls.filter(n -> n == 2));
      assertEquals(e.cons(1), ls.filter(n -> n == 1));
      assertEquals(e, ls.filter(n -> n == 3));
      assertEquals(ls, ls.filter(n -> n != 3));
    }
    {
      final LinkedList<Integer> ls = LinkedList.fromStream(IntStream.range(0, 100).boxed());

      assertTrue(ls.filter(x -> x < 50).length() == 50);
      assertTrue(ls.filter(x -> x >= 50).length() == 50);
    }
  }

  /**
   * Test of partition method, of class LinkedList.
   */
  @Test
  public void testPartition() {
    System.out.println("partition");

  }

  /**
   * Test of nth method, of class LinkedList.
   */
  @Test
  public void testNth() {
    System.out.println("nth");
//    int n = 0;
//    LinkedList instance = null;
//    Object expResult = null;
//    Object result = instance.nth(n);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of elemIndex method, of class LinkedList.
   */
  @Test
  public void testElemIndex() {
    System.out.println("elemIndex");
//    Object a = null;
//    LinkedList instance = null;
//    OptionalInt expResult = null;
//    OptionalInt result = instance.elemIndex(a);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of elemIndices method, of class LinkedList.
   */
  @Test
  public void testElemIndices() {
    System.out.println("elemIndices");
//    Object a = null;
//    LinkedList instance = null;
//    LinkedList<Integer> expResult = null;
//    LinkedList<Integer> result = instance.elemIndices(a);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of findIndex method, of class LinkedList.
   */
  @Test
  public void testFindIndex() {
    System.out.println("findIndex");
//    LinkedList instance = null;
//    OptionalInt expResult = null;
//    OptionalInt result = instance.findIndex(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of findIndices method, of class LinkedList.
   */
  @Test
  public void testFindIndices() {
    System.out.println("findIndices");
//    LinkedList instance = null;
//    LinkedList<Integer> expResult = null;
//    LinkedList<Integer> result = instance.findIndices(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of nub method, of class LinkedList.
   */
  @Test
  public void testNub() {
    System.out.println("nub");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.nub();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of nubBy method, of class LinkedList.
   */
  @Test
  public void testNubBy() {
    System.out.println("nubBy");
//    Comparator cmp = null;
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.nubBy(cmp);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of delete method, of class LinkedList.
   */
  @Test
  public void testDelete() {
    System.out.println("delete");
//    Object a = null;
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.delete(a);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of deleteBy method, of class LinkedList.
   */
  @Test
  public void testDeleteBy() {
    System.out.println("deleteBy");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.deleteBy(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of deleteFirstBy method, of class LinkedList.
   */
  @Test
  public void testDeleteFirstBy() {
    System.out.println("deleteFirstBy");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.deleteFirstBy(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }
  /**
   * Test of listDiff method, of class LinkedList.
   */
  @Test
  public void testListDiff() {
    System.out.println("listDiff");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.listDiff(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of union method, of class LinkedList.
   */
  @Test
  public void testUnion() {
    System.out.println("union");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.union(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of unionBy method, of class LinkedList.
   */
  @Test
  public void testUnionBy() {
    System.out.println("unionBy");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.unionBy(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of intersect method, of class LinkedList.
   */
  @Test
  public void testIntersect() {
    System.out.println("intersect");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.intersect(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of intersectBy method, of class LinkedList.
   */
  @Test
  public void testIntersectBy() {
    System.out.println("intersectBy");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.intersectBy(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of sort method, of class LinkedList.
   */
  @Test
  public void testSort() {
    System.out.println("sort");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.sort();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of sortBy method, of class LinkedList.
   */
  @Test
  public void testSortBy() {
    System.out.println("sortBy");
//    Comparator cmp = null;
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.sortBy(cmp);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of insert method, of class LinkedList.
   */
  @Test
  public void testInsert() {
    System.out.println("insert");
//    Object a = null;
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.insert(a);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of insertBy method, of class LinkedList.
   */
  @Test
  public void testInsertBy() {
    System.out.println("insertBy");
//    Object a = null;
//    Comparator cmp = null;
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.insertBy(a, cmp);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of max method, of class LinkedList.
   */
  @Test
  public void testMax() {
    System.out.println("max");
//    LinkedList instance = null;
//    Object expResult = null;
//    Object result = instance.max();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of maxBy method, of class LinkedList.
   */
  @Test
  public void testMaxBy() {
    System.out.println("maxBy");
//    Comparator cmp = null;
//    LinkedList instance = null;
//    Object expResult = null;
//    Object result = instance.maxBy(cmp);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of min method, of class LinkedList.
   */
  @Test
  public void testMin() {
    System.out.println("min");
//    LinkedList instance = null;
//    Object expResult = null;
//    Object result = instance.min();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of minBy method, of class LinkedList.
   */
  @Test
  public void testMinBy() {
    System.out.println("minBy");
//    Comparator cmp = null;
//    LinkedList instance = null;
//    Object expResult = null;
//    Object result = instance.minBy(cmp);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of subsequences method, of class LinkedList.
   */
  @Test
  public void testSubsequences() {
    System.out.println("subsequences");
    {
      assertEquals(LinkedList.<Integer> empty().subsequences().toString(), "[[]]");
    }
    {
      assertEquals(LinkedList.<Integer> empty().cons(1).subsequences().toString(), "[[], [1]]");
    }
    {
      final LinkedList<String> ls = LinkedList.fromStream(Stream.of("a", "b", "c"));
      final LinkedList<LinkedList<String>> subseqs = ls.subsequences();
      assertEquals(subseqs.toString(), "[[], [a], [b], [a, b], [c], [a, c], [b, c], [a, b, c]]");
    }
  }

  /**
   * Test of permutations method, of class LinkedList.
   */
  @Test
  public void testPermutations() {
    System.out.println("permutations");
    {
      assertEquals("[[]]", LinkedList.<Integer> empty().permutations().toString());
    }
    {
      assertEquals("[[1]]", LinkedList.<Integer> empty().cons(1).permutations().toString());
    }
    {
      final LinkedList<String> ls = LinkedList.fromStream(Stream.of("a", "b", "c"));
      final LinkedList<LinkedList<String>> subseqs = ls.permutations();
      assertEquals(
              "[[b, c, a], [b, a, c], [a, b, c], [c, b, a], [c, a, b], [a, c, b]]",
              subseqs.toString());
      assertEquals(subseqs.length(), 3 * 2 * 1);
    }
    {
      final LinkedList<String> ls = LinkedList.fromStream(Stream.of("a", "b", "c", "d"));
      final LinkedList<LinkedList<String>> subseqs = ls.permutations();
      assertEquals(
              "[[b, d, c, a], [b, d, a, c], [b, a, d, c], [a, b, d, c], [d, b, c, a], [d, b, a, c], [d, a, b, c], [a, d, b, c], [d, c, b, a], [d, c, a, b], [d, a, c, b], [a, d, c, b], [b, c, d, a], [b, c, a, d], [b, a, c, d], [a, b, c, d], [c, b, d, a], [c, b, a, d], [c, a, b, d], [a, c, b, d], [c, d, b, a], [c, d, a, b], [c, a, d, b], [a, c, d, b]]",
              subseqs.toString());
      assertEquals(subseqs.length(), 4 * 3 * 2 * 1);
    }
  }

  /**
   * Test of zipWith method, of class LinkedList.
   */
  @Test
  public void testZipWith() {
    System.out.println("zipWith");
//    LinkedList expResult = null;
//    LinkedList result = LinkedList.zipWith(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of zip method, of class LinkedList.
   */
  @Test
  public void testZip() {
    System.out.println("zip");
//    LinkedList<TuplesModule.Pair<A, B>> expResult = null;
//    LinkedList<TuplesModule.Pair<A, B>> result = LinkedList.zip(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of replicate method, of class LinkedList.
   */
  @Test
  public void testReplicate() {
    System.out.println("replicate");
//    int n = 0;
//    Object a = null;
//    LinkedList expResult = null;
//    LinkedList result = LinkedList.replicate(n, a);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of unzip method, of class LinkedList.
   */
  @Test
  public void testUnzip() {
    System.out.println("unzip");
//    TuplesModule.Pair<LinkedList<A>, LinkedList<B>> expResult = null;
//    TuplesModule.Pair<LinkedList<A>, LinkedList<B>> result = LinkedList.unzip(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of unfoldr method, of class LinkedList.
   */
  @Test
  public void testUnfoldr() {
    System.out.println("unfoldr");
//    LinkedList expResult = null;
//    LinkedList result = LinkedList.unfoldr(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of concat method, of class LinkedList.
   */
  @Test
  public void testConcat() {
    System.out.println("concat");
//    LinkedList expResult = null;
//    LinkedList result = LinkedList.concat(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of concatMap method, of class LinkedList.
   */
  @Test
  public void testConcatMap() {
    System.out.println("concatMap");
//    LinkedList expResult = null;
//    LinkedList result = LinkedList.concatMap(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of intercalate method, of class LinkedList.
   */
  @Test
  public void testIntercalate() {
    System.out.println("intercalate");
//    LinkedList expResult = null;
//    LinkedList result = LinkedList.intercalate(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of transpose method, of class LinkedList.
   */
  @Test
  public void testTranspose() {
    System.out.println("transpose");
//    LinkedList<LinkedList<A>> expResult = null;
//    LinkedList<LinkedList<A>> result = LinkedList.transpose(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of toString method, of class LinkedList.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
//    LinkedList instance = null;
//    String expResult = "";
//    String result = instance.toString();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of equals method, of class LinkedList.
   */
  @Test
  public void testEquals() {
    System.out.println("equals");

    final LinkedList<Integer> e = LinkedList.empty();
    assertTrue(e.equals(e));
    assertTrue(e.cons(1).equals(e.cons(1)));
    assertFalse(e.cons(1).equals(e));
    assertFalse(e.equals(e.cons(1)));
    assertFalse(e.cons(2).cons(1).equals(e.cons(1)));
    assertTrue(e.cons(2).cons(1).equals(e.cons(2).cons(1)));
    assertFalse(e.cons(2).cons(1).equals(e.cons(1).cons(2)));
  }

  /**
   * Test of hashCode method, of class LinkedList.
   */
  @Test
  public void testHashCode() {
    System.out.println("hashCode");
//    LinkedList instance = null;
//    int expResult = 0;
//    int result = instance.hashCode();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of addFirst method, of class LinkedList.
   */
  @Test
  public void testAddFirst() {
    System.out.println("addFirst");
//    Object a = null;
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.addFirst(a);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of addLast method, of class LinkedList.
   */
  @Test
  public void testAddLast() {
    System.out.println("addLast");
//    Object a = null;
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.addLast(a);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of toArray method, of class LinkedList.
   */
  @Test
  public void testToArray() {
    System.out.println("toArray");
//    ArrayList expResult = null;
//    ArrayList result = LinkedList.toArray(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of fromStream method, of class LinkedList.
   */
  @Test
  public void testFromStream() {
    System.out.println("fromStream");
//    LinkedList expResult = null;
//    LinkedList result = LinkedList.fromStream(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  
  /**
   * Test of fromArray method, of class LinkedList.
   */
  @Test
  public void testFromArray_ArrayList() {
    System.out.println("fromArray");
//    LinkedList expResult = null;
//    LinkedList result = LinkedList.fromArray(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of fromArray method, of class LinkedList.
   */
  @Test
  public void testFromArray_3args() {
    System.out.println("fromArray");
//    LinkedList expResult = null;
//    LinkedList result = LinkedList.fromArray(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }


  /**
   * Test of flatMap method, of class LinkedList.
   */
  @Test
  public void testFlatMap() {
    System.out.println("flatMap");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.flatMap(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of iterator method, of class LinkedList.
   */
  @Test
  public void testIterator() {
    System.out.println("iterator");
    
    final int high = 5000;
    LinkedList<Integer> list = LinkedList.fromStream(IntStream.rangeClosed(1, high).mapToObj(n -> n));
    
    int n = 1;
    for (Integer i : list) {
      assertEquals(n, i.intValue());
      ++n;
    }
  }

  /**
   * Test of stream method, of class LinkedList.
   */
  @Test
  public void testStream() {
    System.out.println("stream");

    final int high = 5000;
    LinkedList<Integer> list = LinkedList.fromStream(IntStream.rangeClosed(1, high).mapToObj(n -> 1));
    ArrayList<Integer> v = list.stream().map(n -> n * n * n * n * n * n).map(n -> n + 1).map(n -> n - 1).map(n -> n).collect(Collectors.toCollection(ArrayList::new));
    
    assertEquals(high, v.size());
  }
}
