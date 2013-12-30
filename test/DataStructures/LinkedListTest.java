/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.Ref;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;
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

  private static Stream<Integer> makeStream(final int from, final int to) {
    return IntStream.rangeClosed(from, to).boxed();
  }

  private static LinkedList<Integer> makeList(final int from, final int to) {
    return LinkedList.fromStream(makeStream(from, to));
  }

  private static void println(final String s) {
    System.out.println(s);
  }

  /**
   * Test of empty method, of class LinkedList.
   */
  @Test
  public void testEmpty() {
    println("empty");

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
    println("cons");
    final LinkedList<Integer> l = LinkedList.<Integer> empty().cons(3).cons(2).cons(1);
    
    assertEquals(Integer.valueOf(1), l.head());
    assertEquals(Integer.valueOf(2), l.tail().head());
    assertEquals(Integer.valueOf(3), l.tail().tail().head());
    assertTrue(l.tail().tail().tail().isEmpty());
  }

  /**
   * Test of append method, of class LinkedList.
   */
  @Test
  public void testAppend() {
    println("append");
    final LinkedList<Integer> l1 = LinkedList.of(0, 1, 2);
    final LinkedList<Integer> l2 = LinkedList.of(3, 4, 5);
    final LinkedList<Integer> l3 = l1.append(l2);

    assertEquals(l3.length(), 6);
    IntStream.range(0, 6).forEach(i -> { assertEquals(Integer.valueOf(i), l3.nth(i)); });

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
    println("head");

    final LinkedList<Integer> e = LinkedList.empty();
    boolean exceptionWasThrown = false;
    try {
      final Integer n = e.head();
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);

    final LinkedList<Integer> oneElem = e.cons(1);
    assertEquals(oneElem.head(), Integer.valueOf(1));
    assertEquals(oneElem.cons(2).head(), Integer.valueOf(2));
  }

  /**
   * Test of tail method, of class LinkedList.
   */
  @Test
  public void testTail() {
    println("tail");

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
    println("last");

    {
      final LinkedList<Integer> list = makeList(0, 10);
      assertEquals(list.last(), Integer.valueOf(10));
    }
    {
      final LinkedList<Integer> list = LinkedList.singleton(1);
      assertEquals(list.last(), Integer.valueOf(1));
    }
    {
      boolean exceptionWasThrown = false;
      try {
        final LinkedList<Integer> list = LinkedList.empty();
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
    println("init");
   {
      final LinkedList<Integer> list = makeList(0, 10);
      assertEquals(list.init(), makeList(0, 9));
    }
    {
      final LinkedList<Integer> list = LinkedList.singleton(1);
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
    println("isEmpty");
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
    println("length");
    final LinkedList<Integer> e = LinkedList.empty();
    assertEquals(e.length(), 0);
    assertEquals(e.cons(0).length(), 1);
    
    assertEquals(10, makeList(1, 10).length());
  }

  /**
   * Test of forEach method, of class LinkedList.
   */
  @Test
  public void testForEach() {
    println("forEach");

    final LinkedList<Integer> e = LinkedList.empty();
    final Ref<Integer> r = new Ref<>(0);
    e.forEach(n -> { r.r = 1; });
    assertTrue(r.r == 0);

    final Ref<String> s = new Ref<>("");
    LinkedList.fromStream(IntStream.range(1, 10).boxed()).forEach(n -> s.r = s.r + n.toString());
    assertEquals(s.r, "123456789");
  }

  /**
   * Test of map method, of class LinkedList.
   */
  @Test
  public void testMap() {
    println("map");

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
    println("reverse");

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
    println("intersperse");

    final LinkedList<Integer> e = LinkedList.empty();
    assertEquals(e, e.intersperse(0));
    assertEquals(e.cons(2), e.cons(2).intersperse(0));
    assertEquals(e.cons(2).cons(0).cons(3), e.cons(2).cons(3).intersperse(0));

    final LinkedList<Integer> ls = makeList(1, 10);
    final  LinkedList<Integer> lsReversed = ls.reverse().intersperse(0);
    assertEquals(lsReversed.toString(), "[10, 0, 9, 0, 8, 0, 7, 0, 6, 0, 5, 0, 4, 0, 3, 0, 2, 0, 1]");
  }

  /**
   * Test of foldl method, of class LinkedList.
   */
  @Test
  public void testFoldl() {
    println("foldl");

    final LinkedList<Integer> e = LinkedList.empty();
    assertEquals("x", e.foldl((res, i) -> Integer.toString(i) + res, "x"));
    assertEquals("1x", e.cons(1).foldl((res, i) -> Integer.toString(i) + res, "x"));
    assertEquals("21x", e.cons(2).cons(1).foldl((res, i) -> Integer.toString(i) + res, "x"));

    assertEquals(Integer.valueOf(123456789), makeList(1, 9).foldl((res, i) -> res * 10 + i, 0));
  }

  /**
   * Test of foldl1 method, of class LinkedList.
   */
  @Test
  public void testFoldl1() {
    println("foldl1");

    final LinkedList<Integer> e = LinkedList.empty();
    boolean exceptionWasThrown = false;
    try {
      final Integer s = e.foldl1((res, i) -> i * 10 + res);
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);

    final Integer ii = e.cons(7).foldl1((res, i) -> i * 10 + res);
    assertEquals(Integer.valueOf(7), ii);

    assertEquals(Integer.valueOf(123456789), makeList(1, 9).foldl1((res, i) -> res * 10 + i));
  }

  /**
   * Test of foldr method, of class LinkedList.
   */
  @Test
  public void testFoldr() {
    println("foldr");

    final LinkedList<Integer> e = LinkedList.empty();
    assertEquals("x", e.foldr((i, res) -> Integer.toString(i) + res, "x"));
    assertEquals("1x", e.cons(1).foldr((i, res) -> Integer.toString(i) + res, "x"));
    assertEquals("12x", e.cons(2).cons(1).foldr((i,res) -> Integer.toString(i) + res, "x"));

    assertEquals(Integer.valueOf(987654321), makeList(1, 9).foldr((i, res) -> res * 10 + i, 0));
  }

  /**
   * Test of foldr1 method, of class LinkedList.
   */
  @Test
  public void testFoldr1() {
    println("foldr1");

    final LinkedList<Integer> e = LinkedList.empty();
    boolean exceptionWasThrown = false;
    try {
      final Integer s = e.foldr1((i, res) -> i * 10 + res);
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);

    final Integer ii = e.cons(7).foldr1((i, res) -> i * 10 + res);
    assertEquals(Integer.valueOf(7), ii);

    assertEquals(Integer.valueOf(987654321), makeList(1, 9).foldr1((i, res) -> res * 10 + i));
  }

  /**
   * Test of any method, of class LinkedList.
   */
  @Test
  public void testAny() {
    println("any");

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
    println("all");

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
    println("scanl");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      final LinkedList<Integer> res = e.scanl((aux, i) -> aux + i, 0);
      assertEquals(LinkedList.singleton(0), res);
    }
    {
      final int low = 1, high = 5;
      final LinkedList<Integer> ls = makeList(low, high);
      final LinkedList<Integer> res = ls.scanl((acc, i) -> acc * 10 + i, low - 1);
      assertEquals(LinkedList.of(0, 1, 12, 123, 1234, 12345), res);
      assertEquals(ls.foldl((acc, i) -> acc * 10 + i, low - 1), res.last());
    }
    {
      final int low = 1, high = 5;
      final LinkedList<Integer> ls = makeList(low, high);
      final LinkedList<Integer> res = ls.scanl((acc, i) -> acc + i, low - 1);
      assertEquals(LinkedList.of(0,1,3,6,10,15), res);
    }
  }
  /**
   * Test of scanl1 method, of class LinkedList.
   */
  @Test
  public void testScanl1() {
    println("scanl1");

    {
      final LinkedList<Integer> e = LinkedList.empty();
      final LinkedList<Integer> res = e.scanl1((acc, i) -> acc + i);
      assertEquals(e, res);
    }
    {
      final int low = 1, high = 5;
      final LinkedList<Integer> ls = makeList(low, high);
      final LinkedList<Integer> res = ls.scanl1((acc, i) -> acc * 10 + i);
      assertEquals(LinkedList.of(1, 12, 123, 1234, 12345), res);
    }
    {
      final int low = 1, high = 5;
      final LinkedList<Integer> ls = makeList(low, high);
      final LinkedList<Integer> res = ls.scanl1((acc, i) -> acc + i);

      assertEquals(LinkedList.of(1,3,6,10,15), res);
    }
  }

  /**
   * Test of scanr method, of class LinkedList.
   */
  @Test
  public void testScanr() {
    println("scanr");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      final LinkedList<Integer> res = e.scanr((i, acc) -> acc + i, 0);
      assertEquals(LinkedList.singleton(0), res);
    }
    {
      final int low = 1, high = 5;
      final LinkedList<Integer> ls = makeList(low, high);
      final LinkedList<Integer> res = ls.scanr((i, acc) -> acc * 10 + i, low - 1);
      assertEquals(LinkedList.of(54321, 5432, 543, 54, 5, 0), res);
      assertEquals(ls.foldr((i, acc) -> acc * 10 + i, low - 1), res.head());
    }
    {
      final int low = 1, high = 5;
      final LinkedList<Integer> ls = makeList(low, high);
      final LinkedList<Integer> res = ls.scanr((i, acc) -> acc + i, low - 1);

      assertEquals(LinkedList.of(15,14,12,9,5,0), res);
    }
  }

  /**
   * Test of scanr1 method, of class LinkedList.
   */
  @Test
  public void testScanr1() {
    println("scanr1");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      final LinkedList<Integer> res = e.scanr1((i, acc) -> acc + i);
      assertEquals(e, res);
    }
    {
      final int low = 1, high = 5;
      final LinkedList<Integer> ls = makeList(low, high);
      final LinkedList<Integer> res = ls.scanr1((i, acc) -> acc * 10 + i);
      assertEquals(LinkedList.of(54321, 5432, 543, 54, 5), res);
      assertEquals(ls.foldr1((i, acc) -> acc * 10 + i), res.head());
    }
    {
      final int low = 1, high = 5;
      final LinkedList<Integer> ls = makeList(low, high);
      final LinkedList<Integer> res = ls.scanr1((i, acc) -> acc + i);

      assertEquals(LinkedList.of(15,14,12,9,5), res);
    }
  }

  /**
   * Test of mapAccumL method, of class LinkedList.
   */
  @Test
  public void testMapAccumL() {
    println("mapAccumL");

    {
      final LinkedList<Integer> e = LinkedList.empty();
      final Pair<Integer, List<Integer, ?>> res = e.mapAccumL((acc, x) -> Pair.create(acc, x), -1);
      final Pair<Integer, List<Integer, ?>> expected = Pair.create(-1, e);

      assertEquals(expected, res);
    }
    {
      final LinkedList<String> ls = LinkedList.singleton("neo");
      final Pair<Integer, List<Ref<String>, ?>> res = ls.mapAccumL((acc, x) -> Pair.create(acc + x.length(), new Ref<>(x)), -1);
      final Pair<Integer, List<Ref<String>, ?>> expected = Pair.create(2, LinkedList.of(new Ref<>("neo")));

      assertEquals(expected, res);
    }
    {
      final LinkedList<String> ls = LinkedList.of("a", "bb", "ccc", "dddd");
      final Pair<Integer, List<Ref<String>, ?>> res = ls.mapAccumL((acc, x) -> Pair.create(acc - x.length(), new Ref<>(x)), 0);
      final Pair<Integer, List<Ref<String>, ?>> expected = Pair.create(0 - 1 - 2 - 3 - 4, ls.map(x -> new Ref<>(x)));

      assertEquals(expected, res);
    }
    {
      final LinkedList<String> ls = LinkedList.of("a", "bb", "ccc", "dddd");
      final Pair<Integer, List<Ref<String>, ?>> res = ls.mapAccumL((acc, x) -> Pair.create(acc + x.length(), new Ref<>(x)), 0);
      final Pair<Integer, List<Ref<String>, ?>> expected = Pair.create(0 + 1 + 2 + 3 + 4, ls.map(x -> new Ref<>(x)));

      assertEquals(expected, res);
    }
    {
      final LinkedList<String> ls = LinkedList.of("a", "bb", "ccc", "dddd");
      final Pair<LinkedList<String>, List<Ref<String>, ?>> res = ls.mapAccumL((acc, x) -> Pair.create(acc.cons(x), new Ref<>(x)), LinkedList.empty());
      final Pair<LinkedList<String>, List<Ref<String>, ?>> expected = Pair.create(ls.reverse(), ls.map(x -> new Ref<>(x)));

      assertEquals(expected, res);
    }
  }

  /**
   * Test of mapAccumR method, of class LinkedList.
   */
  @Test
  public void testMapAccumR() {
    println("mapAccumR");

    {
      final LinkedList<Integer> e = LinkedList.empty();
      final Pair<Integer, List<Integer, ?>> res = e.mapAccumR((acc, x) -> Pair.create(acc, x), -1);
      final Pair<Integer, List<Integer, ?>> expected = Pair.create(-1, e);

      assertEquals(expected, res);
    }
    {
      final LinkedList<String> ls = LinkedList.singleton("neo");
      final Pair<Integer, List<Ref<String>, ?>> res = ls.mapAccumR((acc, x) -> Pair.create(acc + x.length(), new Ref<>(x)), -1);
      final Pair<Integer, List<Ref<String>, ?>> expected = Pair.create(2, LinkedList.of(new Ref<>("neo")));

      assertEquals(expected, res);
    }
    {
      final LinkedList<String> ls = LinkedList.of("a", "bb", "ccc", "dddd");
      final Pair<Integer, List<Ref<String>, ?>> res = ls.mapAccumR((acc, x) -> Pair.create(acc - x.length(), new Ref<>(x)), 0);
      final Pair<Integer, List<Ref<String>, ?>> expected = Pair.create(0 - 1 - 2 - 3 - 4, ls.map(x -> new Ref<>(x)));

      assertEquals(expected, res);
    }
    {
      final LinkedList<String> ls = LinkedList.of("a", "bb", "ccc", "dddd");
      final Pair<Integer, List<Ref<String>, ?>> res = ls.mapAccumR((acc, x) -> Pair.create(acc + x.length(), new Ref<>(x)), 0);
      final Pair<Integer, List<Ref<String>, ?>> expected = Pair.create(0 + 1 + 2 + 3 + 4, ls.map(x -> new Ref<>(x)));

      assertEquals(expected, res);
    }
    {
      final LinkedList<String> ls = LinkedList.of("a", "bb", "ccc", "dddd");
      final Pair<LinkedList<String>, List<Ref<String>, ?>> res = ls.mapAccumR((acc, x) -> Pair.create(acc.cons(x), new Ref<>(x)), LinkedList.empty());
      final Pair<LinkedList<String>, List<Ref<String>, ?>> expected = Pair.create(ls, ls.map(x -> new Ref<>(x)));

      assertEquals(expected, res);
    }
  }

  /**
   * Test of take method, of class LinkedList.
   */
  @Test
  public void testTake() {
    println("take");

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
    println("drop");

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
    println("splitAt");

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
    println("takeWhile");
    
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
    println("dropWhile");

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
    println("dropWhileEnd");

    {
      final LinkedList<Integer> ls = LinkedList.empty();
      assertEquals(ls, ls.dropWhileEnd(n -> true));
      assertEquals(ls, ls.dropWhileEnd(n -> false));
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1,2,3,4,5);
      assertEquals(LinkedList.of(1,2,3,4,5), ls.dropWhileEnd(n -> n > 5));
      assertEquals(LinkedList.of(1,2,3,4), ls.dropWhileEnd(n -> n > 4));
      assertEquals(LinkedList.of(1,2,3), ls.dropWhileEnd(n -> n > 3));
      assertEquals(LinkedList.of(1,2), ls.dropWhileEnd(n -> n > 2));
      assertEquals(LinkedList.of(1), ls.dropWhileEnd(n -> n > 1));
      assertEquals(LinkedList.of(), ls.dropWhileEnd(n -> n > 0));
    }
  }

  /**
   * Test of spanByPredicate method, of class LinkedList.
   */
  @Test
  public void testSpanBy() {
    println("spanBy");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      assertEquals(Pair.create(e, e), e.spanBy(n -> true));
      assertEquals(Pair.create(e, e), e.spanBy(n -> false));
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1);
      assertEquals(Pair.create(LinkedList.of(1), e), ls.spanBy(n -> n == 1));
      assertEquals(Pair.create(e, LinkedList.of(1)), ls.spanBy(n -> n != 1));
    }
    {
      final LinkedList<Integer> ls = makeList(1, 10);
      assertEquals(Pair.create(makeList(1, 5), makeList(6, 10)), ls.spanBy(n -> n < 6));
      assertEquals(Pair.create(e, makeList(1, 10)), ls.spanBy(n -> n > 6));
      assertEquals(Pair.create(LinkedList.singleton(1), makeList(2, 10)), ls.spanBy(n -> n == 1));
      assertEquals(Pair.create(LinkedList.of(1, 2), makeList(3, 10)), ls.spanBy(n -> n == 1 || n == 2));
    }
  }

  /**
   * Test of breakByPredicate method, of class LinkedList.
   */
  @Test
  public void testBreakBy() {
    println("breakBy");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      assertEquals(Pair.create(e, e), e.breakBy(n -> true));
      assertEquals(Pair.create(e, e), e.breakBy(n -> false));
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1);
      assertEquals(Pair.create(LinkedList.of(1), e), ls.breakBy(n -> n != 1));
      assertEquals(Pair.create(e, LinkedList.of(1)), ls.breakBy(n -> n == 1));
    }
    {
      final LinkedList<Integer> ls = makeList(1, 10);
      assertEquals(Pair.create(makeList(1, 5), makeList(6, 10)), ls.breakBy(n -> n >= 6));
      assertEquals(Pair.create(e, makeList(1, 10)), ls.breakBy(n -> n <= 6));
      assertEquals(Pair.create(LinkedList.singleton(1), makeList(2, 10)), ls.breakBy(n -> n != 1));
      assertEquals(Pair.create(LinkedList.of(1, 2), makeList(3, 10)), ls.breakBy(n -> n != 1 && n != 2));
    }
  }

  /**
   * Test of stripPrefix method, of class LinkedList.
   */
  @Test
  public void testStripPrefix() {
    println("stripPrefix");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      final Optional<LinkedList<Integer>> res = e.stripPrefix(LinkedList.empty());
      assertEquals(Optional.of(e), res);
    }
    {
      final Optional<LinkedList<Integer>> res = e.stripPrefix(LinkedList.singleton(1));
      assertEquals(Optional.empty(), res);
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1,2,3,4);
      final Optional<LinkedList<Integer>> res1 = ls.stripPrefix(LinkedList.of(1));
      final Optional<LinkedList<Integer>> res2 = ls.stripPrefix(LinkedList.of(1,2));
      final Optional<LinkedList<Integer>> res3 = ls.stripPrefix(LinkedList.of(1,2,3));
      final Optional<LinkedList<Integer>> res4 = ls.stripPrefix(LinkedList.of(1,2,3,4));
      final Optional<LinkedList<Integer>> res5 = ls.stripPrefix(LinkedList.of(1,2,3,4,5));

      assertEquals(Optional.of(LinkedList.of(2,3,4)), res1);
      assertEquals(Optional.of(LinkedList.of(3,4)), res2);
      assertEquals(Optional.of(LinkedList.of(4)), res3);
      assertEquals(Optional.of(LinkedList.empty()), res4);
      assertEquals(Optional.empty(), res5);
    }
  }
  /**
   * Test of group method, of class LinkedList.
   */
  @Test
  public void testGroup() {
    println("group");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      final LinkedList<Integer> ls = e;
      final LinkedList<LinkedList<Integer>> lsRes = ls.group();
      assertEquals(0, lsRes.length());
      assertEquals(e, lsRes);
    }
    {
      final LinkedList<Integer> ls = e.cons(1);
      final LinkedList<LinkedList<Integer>> lsRes = ls.group();
      assertEquals(1, lsRes.length());
      assertEquals(ls, lsRes.head());
    }
    {
      final LinkedList<Integer> ls = e.cons(1).cons(1);
      final LinkedList<LinkedList<Integer>> lsRes = ls.group();
      assertEquals(1, lsRes.length());
      assertEquals(ls, lsRes.head());
    }
    {
      final LinkedList<Integer> ls = e.cons(1).cons(1).cons(2);
      final LinkedList<LinkedList<Integer>> lsRes = ls.group();

      println(lsRes.toString());

      assertEquals(2, lsRes.length());
      assertEquals(LinkedList.of(2), lsRes.nth(0));
      assertEquals(LinkedList.of(1, 1), lsRes.nth(1));
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1, 2, 2, 3, 3, 3, 5, 6, 6, 2, 2, 1, 3, 3, 4, 5);
      final LinkedList<LinkedList<Integer>> lsRes = ls.group();

      println(lsRes.toString());

      assertEquals(10, lsRes.length());
      assertEquals(LinkedList.of(1), lsRes.nth(0));
      assertEquals(LinkedList.of(2, 2), lsRes.nth(1));
      assertEquals(LinkedList.of(3, 3, 3), lsRes.nth(2));
      assertEquals(LinkedList.of(5), lsRes.nth(3));
      assertEquals(LinkedList.of(6, 6), lsRes.nth(4));
      assertEquals(LinkedList.of(2, 2), lsRes.nth(5));
      assertEquals(LinkedList.of(1), lsRes.nth(6));
      assertEquals(LinkedList.of(3, 3), lsRes.nth(7));
      assertEquals(LinkedList.of(4), lsRes.nth(8));
      assertEquals(LinkedList.of(5), lsRes.nth(9));
    }
  }

  /**
   * Test of groupBy method, of class LinkedList.
   */
  @Test
  public void testGroupBy() {
    println("groupBy");
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
    println("inits");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      assertEquals(LinkedList.singleton(e), e.inits());
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1);
      assertEquals(LinkedList.of(e, LinkedList.of(1)), ls.inits());
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1,2);
      assertEquals(LinkedList.of(e, LinkedList.of(1), LinkedList.of(1,2)), ls.inits());
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1,2,3);
      assertEquals(LinkedList.of(e, LinkedList.of(1), LinkedList.of(1,2), LinkedList.of(1,2,3)), ls.inits());
    }
  }

  /**
   * Test of tails method, of class LinkedList.
   */
  @Test
  public void testTails() {
    println("tails");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      assertEquals(LinkedList.singleton(e), e.tails());
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1);
      assertEquals(LinkedList.of(LinkedList.of(1), e), ls.tails());
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1,2);
      assertEquals(LinkedList.of(LinkedList.of(1,2), LinkedList.of(2), e), ls.tails());
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1,2,3);
      assertEquals(LinkedList.of(LinkedList.of(1,2,3), LinkedList.of(2,3), LinkedList.of(3), e), ls.tails());
    }
  }

  /**
   * Test of isPrefixOf method, of class LinkedList.
   */
  @Test
  public void testIsPrefixOf() {
    println("isPrefixOf");

    final LinkedList<Integer> e = LinkedList.empty();
    assertTrue(e.isPrefixOf(e));
    assertFalse(e.cons(1).isPrefixOf(e));
    assertTrue(e.isPrefixOf(e.cons(1)));

    final LinkedList<Integer> ls = LinkedList.of(1,2,3);

    assertTrue(LinkedList.of(1).isPrefixOf(ls));
    assertFalse(LinkedList.of(2).isPrefixOf(ls));
    assertFalse(LinkedList.of(3).isPrefixOf(ls));

    assertTrue(LinkedList.of(1,2).isPrefixOf(ls));
    assertTrue(LinkedList.of(1,2,3).isPrefixOf(ls));
    assertFalse(LinkedList.of(1,2,3,4).isPrefixOf(ls));
  }

  /**
   * Test of isSuffixOf method, of class LinkedList.
   */
  @Test
  public void testIsSuffixOf() {
    println("isSuffixOf");

    final LinkedList<Integer> e = LinkedList.empty();
    assertTrue(e.isSuffixOf(e));
    assertFalse(e.cons(1).isSuffixOf(e));
    assertTrue(e.isSuffixOf(e.cons(1)));

    final LinkedList<Integer> ls = LinkedList.of(1,2,3);

    assertTrue(LinkedList.of(3).isSuffixOf(ls));
    assertFalse(LinkedList.of(2).isSuffixOf(ls));
    assertFalse(LinkedList.of(1).isSuffixOf(ls));

    assertTrue(LinkedList.of(2,3).isSuffixOf(ls));
    assertTrue(LinkedList.of(1,2,3).isSuffixOf(ls));
    assertFalse(LinkedList.of(1,2,3,4).isSuffixOf(ls));
  }

  /**
   * Test of isInfixOf method, of class LinkedList.
   */
  @Test
  public void testIsInfixOf() {
    println("isInfixOf");

    final LinkedList<Integer> e = LinkedList.empty();
    assertTrue(e.isInfixOf(e));
    assertFalse(e.cons(1).isInfixOf(e));
    assertTrue(e.isInfixOf(e.cons(1)));

    final LinkedList<Integer> ls = LinkedList.of(1,2,3,4);

    assertTrue(LinkedList.of(1).isInfixOf(ls));
    assertTrue(LinkedList.of(2).isInfixOf(ls));
    assertTrue(LinkedList.of(3).isInfixOf(ls));
    assertTrue(LinkedList.of(4).isInfixOf(ls));

    assertTrue(LinkedList.of(1,2).isInfixOf(ls));
    assertTrue(LinkedList.of(2,3).isInfixOf(ls));
    assertTrue(LinkedList.of(3,4).isInfixOf(ls));
    assertTrue(LinkedList.of(1,2,3).isInfixOf(ls));
    assertTrue(LinkedList.of(2,3,4).isInfixOf(ls));
    assertTrue(LinkedList.of(1,2,3,4).isInfixOf(ls));

    assertFalse(LinkedList.of(1,3).isInfixOf(ls));
    assertFalse(LinkedList.of(2,4).isInfixOf(ls));
    assertFalse(LinkedList.of(3,4,5).isInfixOf(ls));
    assertFalse(LinkedList.of(4,5).isInfixOf(ls));
    assertFalse(LinkedList.of(5).isInfixOf(ls));
  }

  /**
   * Test of elem method, of class LinkedList.
   */
  @Test
  public void testElem() {
    println("elem");

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
    println("notElem");

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
    println("find");

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
    println("filter");

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
    println("partition");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      final Pair<LinkedList<Integer>, LinkedList<Integer>> p0 = e.partition(n -> false);
      final Pair<LinkedList<Integer>, LinkedList<Integer>> p1 = e.partition(n -> true);

      assertEquals(e, p0.mx1);
      assertEquals(e, p0.mx2);
      assertEquals(e, p1.mx1);
      assertEquals(e, p1.mx2);
    }
    {
      final LinkedList<Integer> ls = e.cons(2).cons(1);
      final Pair<LinkedList<Integer>, LinkedList<Integer>> p0 = ls.partition(n -> n <= 1);
      final Pair<LinkedList<Integer>, LinkedList<Integer>> p1 = ls.partition(n -> n > 1);

      assertEquals(LinkedList.singleton(1), p0.mx1);
      assertEquals(LinkedList.singleton(2), p0.mx2);
      assertEquals(LinkedList.singleton(2), p1.mx1);
      assertEquals(LinkedList.singleton(1), p1.mx2);
    }
    {
      final LinkedList<Integer> ls = makeList(0, 99);
      final Pair<LinkedList<Integer>, LinkedList<Integer>> p = ls.partition(n -> n < 50);

      assertEquals(makeList(0, 49), p.mx1);
      assertEquals(makeList(50, 99), p.mx2);
    }
  }

  /**
   * Test of nth method, of class LinkedList.
   */
  @Test
  public void testNth() {
    println("nth");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      boolean exceptionWasThrown = false;
      try {
        final Integer m = e.cons(1).nth(-1);
      }
      catch (AssertionError ae) { exceptionWasThrown = true; }
      assertTrue(exceptionWasThrown);
    }
    {
      boolean exceptionWasThrown = false;
      try {
        final Integer m = e.nth(0);
      }
      catch (AssertionError ae) { exceptionWasThrown = true; }
      assertTrue(exceptionWasThrown);
    }
    {
      final int low = 0, high = 5;
      final LinkedList<Integer> ls = makeList(low, high);
      IntStream.rangeClosed(low, high).forEach(i -> { assertEquals(Integer.valueOf(i), ls.nth(i)); });
    }
  }

  /**
   * Test of elemIndex method, of class LinkedList.
   */
  @Test
  public void testElemIndex() {
    println("elemIndex");
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
    println("elemIndices");
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
    println("findIndex");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      final OptionalInt i = e.findIndex(n -> true);
      assertFalse(i.isPresent());
    }
    {
      final LinkedList<Integer> ls = makeList(-1, -1);
      final OptionalInt i = ls.findIndex(n -> n == -1);
      assertEquals(OptionalInt.of(0), i);
    }
    {
      final LinkedList<Integer> ls = makeList(-5, -1);
      final OptionalInt i = ls.findIndex(n -> n == -1);

      IntStream.rangeClosed(-5, -1).forEach(n -> { assertEquals(OptionalInt.of(n + 5), ls.findIndex(j -> j == n)); });
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1,2,3,2);
      assertEquals(OptionalInt.of(1), ls.findIndex(n -> n == 2));
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1,2,3,2);
      assertEquals(OptionalInt.of(2), ls.findIndex(n -> n > 2));
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1,2,3,2);
      assertEquals(OptionalInt.empty(), ls.findIndex(n -> n < 0));
    }
  }

  /**
   * Test of findIndices method, of class LinkedList.
   */
  @Test
  public void testFindIndices() {
    println("findIndices");
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
    println("nub");
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
    println("nubBy");
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
    println("delete");

    {
      final LinkedList<Integer> e = LinkedList.empty();
      final LinkedList<Integer> res = e.delete(1);
      final LinkedList<Integer> expected = e;

      assertEquals(expected, res);
    }
    {
      final LinkedList<Integer> ls = LinkedList.singleton(1);
      final LinkedList<Integer> res1 = ls.delete(1);
      final LinkedList<Integer> res2 = ls.delete(2);
      final LinkedList<Integer> expected1 = LinkedList.empty();
      final LinkedList<Integer> expected2 = LinkedList.singleton(1);

      assertEquals(expected1, res1);
      assertEquals(expected2, res2);
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1,2,3,2,5);
      final LinkedList<Integer> res1 = ls.delete(1);
      final LinkedList<Integer> res2 = ls.delete(2);
      final LinkedList<Integer> res3 = ls.delete(3);
      final LinkedList<Integer> res4 = ls.delete(5);
      final LinkedList<Integer> res5 = ls.delete(2).delete(2);

      final LinkedList<Integer> expected1 = LinkedList.of(2,3,2,5);
      final LinkedList<Integer> expected2 = LinkedList.of(1,3,2,5);
      final LinkedList<Integer> expected3 = LinkedList.of(1,2,2,5);
      final LinkedList<Integer> expected4 = LinkedList.of(1,2,3,2);
      final LinkedList<Integer> expected5 = LinkedList.of(1,3,5);

      assertEquals(expected1, res1);
      assertEquals(expected2, res2);
      assertEquals(expected3, res3);
      assertEquals(expected4, res4);
      assertEquals(expected5, res5);
    }
  }

  /**
   * Test of deleteBy method, of class LinkedList.
   */
  @Test
  public void testDeleteBy() {
    println("deleteBy");
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
    println("deleteFirstBy");
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
    println("listDiff");
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
    println("union");
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
    println("unionBy");
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
    println("intersect");
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
    println("intersectBy");
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
    println("sort");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      final LinkedList<Integer> eSorted = e.sort();
      assertEquals(e, eSorted);
    }
    {
      final LinkedList<Integer> ls = e.cons(1);
      final LinkedList<Integer> lsSorted = ls.sort();
      assertEquals(ls, lsSorted);
    }
    {
      final LinkedList<Integer> ls = e.cons(1).cons(-5).cons(5).cons(6).cons(2);
      final LinkedList<Integer> lsSorted = ls.sort();
      final LinkedList<Pair<Integer, Integer>> lsZipped = LinkedList.zip(lsSorted, lsSorted.tail());
      assertTrue(lsZipped.all(p -> p.mx1 <= p.mx2));
    }
  }

  /**
   * Test of sortBy method, of class LinkedList.
   */
  @Test
  public void testSortBy() {
    println("sortBy");

      final LinkedList<Integer> e = LinkedList.empty();
    {
      final LinkedList<Integer> eSorted = e.sortBy(LinkedListTest::cmp);
      assertEquals(e, eSorted);
    }
    {
      final LinkedList<Integer> ls = e.cons(1);
      final LinkedList<Integer> lsSorted = ls.sortBy(LinkedListTest::cmp);
      assertEquals(ls, lsSorted);
    }
    {
      final LinkedList<Integer> ls = e.cons(1).cons(-5).cons(5).cons(6).cons(2);
      final LinkedList<Integer> lsSorted = ls.sortBy(LinkedListTest::cmp);
      final LinkedList<Pair<Integer, Integer>> lsZipped = LinkedList.zip(lsSorted, lsSorted.tail());
      assertTrue(lsZipped.all(p -> p.mx1 <= p.mx2));
    }
    {
      final LinkedList<Integer> ls = e.cons(1).cons(-5).cons(5).cons(6).cons(2);
      final LinkedList<Integer> lsSorted = ls.sortBy(LinkedListTest::negCmp);
      final LinkedList<Pair<Integer, Integer>> lsZipped = LinkedList.zip(lsSorted, lsSorted.tail());
      assertTrue(lsZipped.all(p -> p.mx1 >= p.mx2));
    }
  }

  /**
   * Test of insert method, of class LinkedList.
   */
  @Test
  public void testInsert() {
    println("insert");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      final LinkedList<Integer> res = e.insert(1);
      assertEquals(LinkedList.singleton(1), res);
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(2,4,6);
      final LinkedList<Integer> res1 = ls.insert(1);
      final LinkedList<Integer> res2 = ls.insert(2);
      final LinkedList<Integer> res3 = ls.insert(3);
      final LinkedList<Integer> res4 = ls.insert(4);
      final LinkedList<Integer> res5 = ls.insert(5);
      final LinkedList<Integer> res6 = ls.insert(6);
      final LinkedList<Integer> res7 = ls.insert(7);

      final LinkedList<Integer> expected1 = LinkedList.of(1,2,4,6);
      final LinkedList<Integer> expected2 = LinkedList.of(2,2,4,6);
      final LinkedList<Integer> expected3 = LinkedList.of(2,3,4,6);
      final LinkedList<Integer> expected4 = LinkedList.of(2,4,4,6);
      final LinkedList<Integer> expected5 = LinkedList.of(2,4,5,6);
      final LinkedList<Integer> expected6 = LinkedList.of(2,4,6,6);
      final LinkedList<Integer> expected7 = LinkedList.of(2,4,6,7);

      assertEquals(expected1, res1);
      assertEquals(expected2, res2);
      assertEquals(expected3, res3);
      assertEquals(expected4, res4);
      assertEquals(expected5, res5);
      assertEquals(expected6, res6);
      assertEquals(expected7, res7);
    }
    {
      final LinkedList<Integer> ls = makeList(1, 5);
      final LinkedList<Integer> res1 = ls.insert(2);
      final LinkedList<Integer> res2 = ls.insert(3);

      assertNotSame(ls.tail(), res1.tail());
      assertSame(ls.tail(), res1.tail().tail());
    }
  }

  /**
   * Test of insertBy method, of class LinkedList.
   */

  private static int insertByCmp(final Integer x, final Integer y) { return Math.abs(x - y) <= 1 ? 0 : (x > y ? 1 : -1); }

  @Test
  public void testInsertBy() {
    println("insertBy");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      final LinkedList<Integer> res = e.insertBy(1, LinkedListTest::insertByCmp);
      assertEquals(LinkedList.singleton(1), res);
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(2,4,6);
      final LinkedList<Integer> res0 = ls.insertBy(0, LinkedListTest::insertByCmp);
      final LinkedList<Integer> res1 = ls.insertBy(1, LinkedListTest::insertByCmp);
      final LinkedList<Integer> res2 = ls.insertBy(2, LinkedListTest::insertByCmp);
      final LinkedList<Integer> res3 = ls.insertBy(3, LinkedListTest::insertByCmp);
      final LinkedList<Integer> res4 = ls.insertBy(4, LinkedListTest::insertByCmp);
      final LinkedList<Integer> res5 = ls.insertBy(5, LinkedListTest::insertByCmp);
      final LinkedList<Integer> res6 = ls.insertBy(6, LinkedListTest::insertByCmp);
      final LinkedList<Integer> res7 = ls.insertBy(7, LinkedListTest::insertByCmp);
      final LinkedList<Integer> res8 = ls.insertBy(8, LinkedListTest::insertByCmp);

      final LinkedList<Integer> expected0 = LinkedList.of(0,2,4,6);
      final LinkedList<Integer> expected1 = LinkedList.of(1,2,4,6);
      final LinkedList<Integer> expected2 = LinkedList.of(2,2,4,6);
      final LinkedList<Integer> expected3 = LinkedList.of(3,2,4,6);
      final LinkedList<Integer> expected4 = LinkedList.of(2,4,4,6);
      final LinkedList<Integer> expected5 = LinkedList.of(2,5,4,6);
      final LinkedList<Integer> expected6 = LinkedList.of(2,4,6,6);
      final LinkedList<Integer> expected7 = LinkedList.of(2,4,7,6);
      final LinkedList<Integer> expected8 = LinkedList.of(2,4,6,8);

      assertEquals(expected0, res0);
      assertEquals(expected1, res1);
      assertEquals(expected2, res2);
      assertEquals(expected3, res3);
      assertEquals(expected4, res4);
      assertEquals(expected5, res5);
      assertEquals(expected6, res6);
      assertEquals(expected7, res7);
      assertEquals(expected8, res8);
    }
    {
      final LinkedList<Integer> ls = makeList(1, 5);
      final LinkedList<Integer> res1 = ls.insertBy(2, LinkedListTest::insertByCmp);
      final LinkedList<Integer> res2 = ls.insertBy(3, LinkedListTest::insertByCmp);

      assertSame(ls, res1.tail());
      assertSame(ls.tail(), res2.tail().tail());
    }
  }

  private static int cmp(final Integer x, final Integer y) { return x > y ? 1 : x < y ? -1 : 0; }
  private static int negCmp(final Integer x, final Integer y) { return -cmp(x, y); }

  /**
   * Test of max method, of class LinkedList.
   */
  @Test
  public void testMax() {
    println("max");

    final LinkedList<Integer> e = LinkedList.empty();
    boolean exceptionWasThrown = false;
    try {
      final Integer m = e.max();
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);

    {
      final LinkedList<Integer> ls = LinkedList.of(2, 3, 1, 5);
      assertEquals(Integer.valueOf(5), ls.max());
    }
    {
      final LinkedList<Integer> ls =  LinkedList.of(2, 3, 5, 1);
      assertEquals(Integer.valueOf(5), ls.max());
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(2, 5, 3, 1);
      assertEquals(Integer.valueOf(5), ls.max());
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(5, 2, 3, 1);
      assertEquals(Integer.valueOf(5), ls.max());
    }
  }

  /**
   * Test of maxBy method, of class LinkedList.
   */
  @Test
  public void testMaxBy() {
    println("maxBy");

    final LinkedList<Integer> e = LinkedList.empty();
    boolean exceptionWasThrown = false;
    try {
      final Integer m = e.maxBy(LinkedListTest::cmp);
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);

    {
      final LinkedList<Integer> ls = LinkedList.of(2, 3, 1, 5);
      assertEquals(Integer.valueOf(5), ls.maxBy(LinkedListTest::cmp));
      assertEquals(Integer.valueOf(1), ls.maxBy(LinkedListTest::negCmp));
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(2, 3, 5, 1);
      assertEquals(Integer.valueOf(5), ls.maxBy(LinkedListTest::cmp));
      assertEquals(Integer.valueOf(1), ls.maxBy(LinkedListTest::negCmp));
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(2, 5, 3, 1);
      assertEquals(Integer.valueOf(5), ls.maxBy(LinkedListTest::cmp));
      assertEquals(Integer.valueOf(1), ls.maxBy(LinkedListTest::negCmp));
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(5, 2, 3, 1);
      assertEquals(Integer.valueOf(5), ls.maxBy(LinkedListTest::cmp));
      assertEquals(Integer.valueOf(1), ls.maxBy(LinkedListTest::negCmp));
    }
}

  /**
   * Test of min method, of class LinkedList.
   */
  @Test
  public void testMin() {
    println("min");

    final LinkedList<Integer> e = LinkedList.empty();
    boolean exceptionWasThrown = false;
    try {
      final Integer m = e.min();
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);

    {
      final LinkedList<Integer> ls = LinkedList.of(2, 3, 5, 1);
      assertEquals(Integer.valueOf(1), ls.min());
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(2, 3, 1, 5);
      assertEquals(Integer.valueOf(1), ls.min());
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(2, 1, 3, 5);
      assertEquals(Integer.valueOf(1), ls.min());
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1, 2, 3, 5);
      assertEquals(Integer.valueOf(1), ls.min());
    }
  }

  /**
   * Test of minBy method, of class LinkedList.
   */
  @Test
  public void testMinBy() {
    println("minBy");

    final LinkedList<Integer> e = LinkedList.empty();
    boolean exceptionWasThrown = false;
    try {
      final Integer m = e.minBy(LinkedListTest::cmp);
    }
    catch (AssertionError ae) { exceptionWasThrown = true; }
    assertTrue(exceptionWasThrown);

    {
      final LinkedList<Integer> ls = LinkedList.of(2, 3, 5, 1);
      assertEquals(Integer.valueOf(1), ls.minBy(LinkedListTest::cmp));
      assertEquals(Integer.valueOf(5), ls.minBy(LinkedListTest::negCmp));
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(2, 3, 1, 5);
      assertEquals(Integer.valueOf(1), ls.minBy(LinkedListTest::cmp));
      assertEquals(Integer.valueOf(5), ls.minBy(LinkedListTest::negCmp));
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(2, 1, 3, 5);
      assertEquals(Integer.valueOf(1), ls.minBy(LinkedListTest::cmp));
      assertEquals(Integer.valueOf(5), ls.minBy(LinkedListTest::negCmp));
    }
    {
      final LinkedList<Integer> ls = LinkedList.of(1, 2, 3, 5);
      assertEquals(Integer.valueOf(1), ls.minBy(LinkedListTest::cmp));
      assertEquals(Integer.valueOf(5), ls.minBy(LinkedListTest::negCmp));
    }
  }

  /**
   * Test of subsequences method, of class LinkedList.
   */
  @Test
  public void testSubsequences() {
    println("subsequences");
    {
      assertEquals("[[]]", LinkedList.<Integer> empty().subsequences().toString());
    }
    {
      assertEquals("[[], [1]]", LinkedList.<Integer> empty().cons(1).subsequences().toString());
    }
    {
      final LinkedList<String> ls = LinkedList.of("a", "b", "c");
      final LinkedList<LinkedList<String>> subseqs = ls.subsequences();
      assertEquals("[[], [a], [b], [a, b], [c], [a, c], [b, c], [a, b, c]]", subseqs.toString());
    }
  }

  /**
   * Test of permutations method, of class LinkedList.
   */
  @Test
  public void testPermutations() {
    println("permutations");
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
      assertEquals(3 * 2 * 1, subseqs.length());
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
    println("zipWith");
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
    println("zip");
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
    println("replicate");
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
    println("unzip");
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
    println("unfoldr");
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
    println("concat");
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
    println("concatMap");
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
    println("intercalate");
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
    println("transpose");

    {
      final LinkedList<LinkedList<Integer>> expected = LinkedList.empty();
      final LinkedList<LinkedList<Integer>> res = LinkedList.transpose(LinkedList.<LinkedList<Integer>> empty());
      
      assertEquals(expected, res);
    }
    {
      final LinkedList<LinkedList<Integer>> ls = LinkedList.of(LinkedList.of(1, 2, 3), LinkedList.of(4, 5, 6));
      final LinkedList<LinkedList<Integer>> expected = LinkedList.of(LinkedList.of(1, 4), LinkedList.of(2, 5), LinkedList.of(3, 6));
      final LinkedList<LinkedList<Integer>> res = LinkedList.transpose(ls);

      assertEquals(expected, res);
    }
    {
      final LinkedList<LinkedList<Integer>> ls = LinkedList.of(LinkedList.of(1, 2, 3), LinkedList.of(5), LinkedList.of(6, 7));
      final LinkedList<LinkedList<Integer>> expected = LinkedList.of(LinkedList.of(1, 5, 6), LinkedList.of(2, 7), LinkedList.of(3));
      final LinkedList<LinkedList<Integer>> res = LinkedList.transpose(ls);

      assertEquals(expected, res);
    }
    {
      final LinkedList<LinkedList<Integer>> ls = LinkedList.of(LinkedList.of(1), LinkedList.of(2), LinkedList.of(3, 4, 5, 6));
      final LinkedList<LinkedList<Integer>> expected = LinkedList.of(LinkedList.of(1, 2, 3), LinkedList.of(4), LinkedList.of(5), LinkedList.of(6));
      final LinkedList<LinkedList<Integer>> res = LinkedList.transpose(ls);

      assertEquals(expected, res);
    }
    {
      final LinkedList<LinkedList<Integer>> ls = LinkedList.of(LinkedList.of(1), LinkedList.empty(), LinkedList.of(3, 4, 5, 6));
      final LinkedList<LinkedList<Integer>> expected = LinkedList.of(LinkedList.of(1, 3), LinkedList.of(4), LinkedList.of(5), LinkedList.of(6));
      final LinkedList<LinkedList<Integer>> res = LinkedList.transpose(ls);

      assertEquals(expected, res);
    }
  }

  /**
   * Test of toString method, of class LinkedList.
   */
  @Test
  public void testToString() {
    println("toString");

    assertEquals("[]", LinkedList.<Integer> empty().toString());
    assertEquals("[1]", LinkedList.singleton(1).toString());
    assertEquals("[1, 2]", LinkedList.singleton(2).cons(1).toString());
  }

  /**
   * Test of equals method, of class LinkedList.
   */
  @Test
  public void testEquals() {
    println("equals");

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
    println("hashCode");

    final LinkedList<Integer> e = LinkedList.empty();

    assertEquals(e.hashCode(), LinkedList.singleton(1).tail().hashCode());
    assertEquals(e.cons(1).hashCode(), LinkedList.of(1).hashCode());
    assertTrue(e.hashCode() != e.cons(1).hashCode());
  }

  /**
   * Test of addFirst method, of class LinkedList.
   */
  @Test
  public void testAddFirst() {
    println("addFirst");

    final LinkedList<Integer> e = LinkedList.empty();
    assertEquals(LinkedList.singleton(1), e.addFirst(1));
    assertEquals(makeList(-1, 5), makeList(0, 5).addFirst(-1));
    assertEquals(makeList(-2, 5), makeList(0, 5).addFirst(-1).addFirst(-2));
  }

  /**
   * Test of addLast method, of class LinkedList.
   */
  @Test
  public void testAddLast() {
    println("addLast");

    final LinkedList<Integer> e = LinkedList.empty();
    assertEquals(LinkedList.singleton(1), e.addLast(1));
    assertEquals(makeList(0, 6), makeList(0, 5).addLast(6));
    assertEquals(makeList(0, 7), makeList(0, 5).addLast(6).addLast(7));
  }

  /**
   * Test of toArray method, of class LinkedList.
   */
  @Test
  public void testToArray() {
    println("toArray");

    final LinkedList<Integer> e = LinkedList.empty();
    {
      assertEquals(new ArrayList<>(), e.toArray());
    }

    {
      final ArrayList<Integer> v =
              Arrays.stream(new int[] {1,2,3})
                    .boxed()
                    .collect(Collectors.toCollection(ArrayList::new));

      assertEquals(v, e.cons(3).cons(2).cons(1).toArray());
    }
  }

  /**
   * Test of fromStream method, of class LinkedList.
   */
  @Test
  public void testFromStream() {
    println("fromStream");

    final LinkedList<Integer> ls = makeList(0, 5); // Function uses fromStream()
    for (int i = 0; i != 6; ++i) {
      assertEquals(Integer.valueOf(i), ls.nth(i));
    }
  }

  /**
   * Test of fromArray method, of class LinkedList.
   */
  @Test
  public void testFromArray_ArrayList() {
    println("fromArray");
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
    println("fromArray");
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
    println("flatMap");
    
    final LinkedList<Integer> e = LinkedList.empty();
    {
      assertEquals(e, LinkedList.flatMap(e, x -> e.cons(x)));
    }
    {
      final LinkedList<Integer> ls = makeList(0, 5);
      final LinkedList<Integer> res = LinkedList.flatMap(ls, i -> makeList(0, i));
      assertEquals(1 + 2 + 3 + 4 + 5 + 6, res.length());

      LinkedList<Integer> t = res;
      for (int i = 0; i != 6; ++i) {
        for (int j = 0; j != i + 1; ++j) {
          assertEquals(j, t.head().intValue());
          t = t.tail();
        }
      }
    }
  }

  /**
   * Test of iterator method, of class LinkedList.
   */
  @Test
  public void testIterator() {
    println("iterator");
    
    final int high = 5000;
    LinkedList<Integer> list = makeList(1, high);
    
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
    println("stream");

    final int high = 5000;
    LinkedList<Integer> list = LinkedList.fromStream(IntStream.rangeClosed(1, high).mapToObj(n -> 1));
    ArrayList<Integer> v = list.stream().map(n -> n * n * n * n * n * n).map(n -> n + 1).map(n -> n - 1).map(n -> n).collect(Collectors.toCollection(ArrayList::new));
    
    assertEquals(high, v.size());
  }
}
