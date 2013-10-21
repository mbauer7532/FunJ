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
import org.StructureGraphic.v1.DSutils;
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
public class AvlTreeModuleTest {
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

  public void graphTree(AvlTreeModule.Tree<Integer, String> t) {
    Graph graph = new SingleGraph("Graph");
    graph.addAttribute("ui.stylesheet", "url('file:///D:\\Users\\Neo\\Documents\\NetBeansProjects\\FunJ\\src\\DataStructures\\graphNode.stylesheet')");

    t.graph(graph);
    graph.display();
    final int sleepSeconds = 1;
    try {
      Thread.sleep(sleepSeconds * 1000);
    } catch (InterruptedException ex) {
      Logger.getLogger(AvlTreeModuleTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Test of empty method, of AvlTreeModulevlTree.
   */
  @Test
  public void testEmpty() {
    System.out.println("Empty");

    final int N = 10;
    final List<AvlTreeModule.Tree<Integer, String>> at = new ArrayList<>();

    AvlTreeModule.Tree<Integer, String> t = AvlTreeModule.empty();
    at.add(t);
    for (int i = 0; i != N; ++i) {
      t = t.put(i * 10, "hi");
      at.add(t);
    }

    for (int i = 0, cnt = at.size(); i != cnt; ++i) {
      assertEquals(at.get(i).size(), i);
    }

    graphTree(t);
    DSutils.show(t, 80, 60);
    DSutils.show(at.get(N-1), 80, 60);
    final int sleepSeconds = 1;
    try {
      Thread.sleep(sleepSeconds * 1000);
    } catch (InterruptedException ex) {
      Logger.getLogger(AvlTreeModuleTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Test AvlTreeModuleleton method, of class AvlTree.
   */
  @Test
  public void testRandomTreeDepth() {
    System.out.println("RandomTreeDepth");

    final int m = -1000;
    final int n = 1000;
    final int Experiments = 500;
    final int N = 400;

    for (int i = 0; i != Experiments; ++i) {
      final int[] data = Utils.Numeric.randomPermuation(m, n, N);
      AvlTreeModule.Tree<Integer, Integer> t0 = AvlTreeModule.empty();

      AvlTreeModule.Tree<Integer, Integer> t
              = Arrays.stream(data)
                      .boxed()
                      .reduce(t0,
                              ((AvlTreeModule.Tree<Integer, Integer> tx, Integer x) -> tx.put(x, x)),
                              ((AvlTreeModule.Tree<Integer, Integer> z1, AvlTreeModule.Tree<Integer, Integer> z2) -> null));

      final int expectedSize = N;
      final double expectedDepth = AvlTreeModule.expectedDepth(N);

      final int avlTreeSize = t.size();
      final int avlTreeDepth = t.depth();

      assertEquals(expectedSize, avlTreeSize);
      assertTrue(((double) avlTreeDepth) < expectedDepth);
    }
  }
}
