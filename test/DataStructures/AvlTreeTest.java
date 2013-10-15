/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

/**
 *
 * @author Neo
 */
public class AvlTreeTest {
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

  public void graphTree(AvlTree.Tree<Integer, String> t) {
    Graph graph = new SingleGraph("Graph");
    graph.addAttribute("ui.stylesheet", "url('file:///D:\\Users\\Neo\\Documents\\NetBeansProjects\\FunJ\\src\\DataStructures\\graphNode.stylesheet')");

    t.graph(graph);
    graph.display();
    final int sleepSeconds = 1;
    try {
      Thread.sleep(sleepSeconds * 1000);
    } catch (InterruptedException ex) {
      Logger.getLogger(AvlTreeTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Test of empty method, of class AvlTree.
   */
  @Test
  public void testEmpty() {
    System.out.println("Empty");

    final int N = 5;

    List<AvlTree.Tree<Integer, String>> at = new ArrayList<>();

    AvlTree.Tree<Integer, String> t = AvlTree.empty();
    at.add(t);
    for (int i = 0; i != N; ++i) {
      t = t.put(i * 10, "hi");
      at.add(t);
    }

    for (int i = 0, cnt = at.size(); i != cnt; ++i) {
      System.out.printf("i = %d\n", i);
      if (i == at.size()-1)
        graphTree(at.get(i));
      assertEquals(at.get(i).size(), i);
    }
  }
  /**
   * Test of singleton method, of class AvlTree.
   */
  @Test
  public void testRandomTreeDepth() {
    System.out.println("RandomTreeDepth");

    final int m = -1000;
    final int n = 1000;
    final int Experiments = 500;
    final int N = 400;

    for (int i = 0; i != Experiments; ++i) {
      final int[] data = Utils.Numeric.randomArray(m, n, N);
      final AvlTree.Tree<Integer, Integer> t0 = AvlTree.empty();
/*
      AvlTree.Tree<Integer, Integer> t1 = Arrays.stream(data)
                                          .boxed()
                                          .reduce(t0, ((AvlTree.Tree<Integer, Integer> tree, Integer x) -> tree.put(x, x)));
*/
      AvlTree.Tree<Integer, Integer> t2 = Arrays.stream(data)
                                          .boxed()
                                          .reduce(t0,
                                                  ((AvlTree.Tree<Integer, Integer> tx, Integer x) -> tx.put(x, x)),
                                                  ((z1, z2) -> z1));
/*
      for (int j = 0; j != N; ++j) {
        final int r = Utils.Numeric.randomInt(m, n);
        t = t.put(r, r);
      }
*/

      final int expectedSize = N;
      final double expectedDepth = AvlTree.expectedDepth(N);

      final int avlTreeSize = t2.size();
      final int avlTreeDepth = t2.depth();

      System.out.println("Hi there.");
      System.out.println(expectedSize);
      System.out.println(avlTreeSize);
      System.out.println();
      System.out.println(avlTreeDepth);
      System.out.println(expectedDepth);

      assertEquals(expectedSize, avlTreeSize);
      assertTrue((double) avlTreeDepth < expectedDepth);
    }
  }
}
