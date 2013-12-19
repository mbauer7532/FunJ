/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;
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
//
//    System.out.printf("%s\n", l0.toString());
//    System.out.printf("%s\n", l1.toString());
//    System.out.printf("%s\n", l2.toString());
//    System.out.printf("%s\n", l3.toString());
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

    final LinkedList<Integer> lempty = LinkedList.empty();
    assertEquals(lempty.append(l1), l1.append(lempty));
    assertEquals(lempty.append(l1), l1);
    assertEquals(lempty.append(l1).length(), l1.length());
  }

  /**
   * Test of head method, of class LinkedList.
   */
  @Test
  public void testHead() {
    System.out.println("head");
//    LinkedList instance = null;
//    Object expResult = null;
//    Object result = instance.head();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of tail method, of class LinkedList.
   */
  @Test
  public void testTail() {
    System.out.println("tail");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.tail();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of last method, of class LinkedList.
   */
  @Test
  public void testLast() {
    System.out.println("last");
//    LinkedList instance = null;
//    Object expResult = null;
//    Object result = instance.last();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of init method, of class LinkedList.
   */
  @Test
  public void testInit() {
    System.out.println("init");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.init();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of isEmpty method, of class LinkedList.
   */
  @Test
  public void testIsEmpty() {
    System.out.println("isEmpty");
//    LinkedList instance = null;
//    boolean expResult = false;
//    boolean result = instance.isEmpty();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of length method, of class LinkedList.
   */
  @Test
  public void testLength() {
    System.out.println("length");
//    LinkedList instance = null;
//    int expResult = 0;
//    int result = instance.length();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of forEach method, of class LinkedList.
   */
  @Test
  public void testForEach() {
    System.out.println("forEach");
//    Consumer action = null;
//    LinkedList instance = null;
//    instance.forEach(action);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of map method, of class LinkedList.
   */
  @Test
  public void testMap() {
    System.out.println("map");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.map(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of reverse method, of class LinkedList.
   */
  @Test
  public void testReverse() {
    System.out.println("reverse");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.reverse();
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of intersperse method, of class LinkedList.
   */
  @Test
  public void testIntersperse() {
    System.out.println("intersperse");
//    Object a = null;
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.intersperse(a);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of foldl method, of class LinkedList.
   */
  @Test
  public void testFoldl() {
    System.out.println("foldl");
//    LinkedList instance = null;
//    Object expResult = null;
//    Object result = instance.foldl(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of foldl1 method, of class LinkedList.
   */
  @Test
  public void testFoldl1() {
    System.out.println("foldl1");
//    LinkedList instance = null;
//    Object expResult = null;
//    Object result = instance.foldl1(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of foldr method, of class LinkedList.
   */
  @Test
  public void testFoldr() {
    System.out.println("foldr");
//    LinkedList instance = null;
//    Object expResult = null;
//    Object result = instance.foldr(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of foldr1 method, of class LinkedList.
   */
  @Test
  public void testFoldr1() {
    System.out.println("foldr1");
//    LinkedList instance = null;
//    Object expResult = null;
//    Object result = instance.foldr1(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of any method, of class LinkedList.
   */
  @Test
  public void testAny() {
    System.out.println("any");
//    LinkedList instance = null;
//    boolean expResult = false;
//    boolean result = instance.any(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of all method, of class LinkedList.
   */
  @Test
  public void testAll() {
    System.out.println("all");
//    LinkedList instance = null;
//    boolean expResult = false;
//    boolean result = instance.all(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
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
//    int n = 0;
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.take(n);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of drop method, of class LinkedList.
   */
  @Test
  public void testDrop() {
    System.out.println("drop");
//    int n = 0;
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.drop(n);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of splitAt method, of class LinkedList.
   */
  @Test
  public void testSplitAt() {
    System.out.println("splitAt");
//    int n = 0;
//    LinkedList instance = null;
////    TuplesModule.Pair<LinkedList<A>, LinkedList<A>> expResult = null;
////    TuplesModule.Pair<LinkedList<A>, LinkedList<A>> result = instance.splitAt(n);
////    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of takeWhile method, of class LinkedList.
   */
  @Test
  public void testTakeWhile() {
    System.out.println("takeWhile");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.takeWhile(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of dropWhile method, of class LinkedList.
   */
  @Test
  public void testDropWhile() {
    System.out.println("dropWhile");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.dropWhile(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
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
//    Object a = null;
//    LinkedList instance = null;
//    boolean expResult = false;
//    boolean result = instance.elem(a);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of notElem method, of class LinkedList.
   */
  @Test
  public void testNotElem() {
    System.out.println("notElem");
//    Object a = null;
//    LinkedList instance = null;
//    boolean expResult = false;
//    boolean result = instance.notElem(a);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of find method, of class LinkedList.
   */
  @Test
  public void testFind() {
    System.out.println("find");
//    LinkedList instance = null;
//    Optional expResult = null;
//    Optional result = instance.find(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of filter method, of class LinkedList.
   */
  @Test
  public void testFilter() {
    System.out.println("filter");
//    LinkedList instance = null;
//    LinkedList expResult = null;
//    LinkedList result = instance.filter(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of partition method, of class LinkedList.
   */
  @Test
  public void testPartition() {
    System.out.println("partition");
//    LinkedList instance = null;
//    TuplesModule.Pair<LinkedList<A>, LinkedList<A>> expResult = null;
//    TuplesModule.Pair<LinkedList<A>, LinkedList<A>> result = instance.partition(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
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
//    LinkedList instance = null;
//    LinkedList<LinkedList<A>> expResult = null;
//    LinkedList<LinkedList<A>> result = instance.subsequences(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
  }

  /**
   * Test of permutations method, of class LinkedList.
   */
  @Test
  public void testPermutations() {
    System.out.println("permutations");
//    LinkedList instance = null;
//    LinkedList<LinkedList<A>> expResult = null;
//    LinkedList<LinkedList<A>> result = instance.permutations(null);
//    assertEquals(expResult, result);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
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
}
