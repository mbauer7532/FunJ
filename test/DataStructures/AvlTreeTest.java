/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import java.util.ArrayList;
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
    final int sleepSeconds = 100;
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
    System.out.println("empty");
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
  public void testSingleton() {
    /*
    System.out.println("singleton");
    Object key = null;
    Object value = null;
    AvlTree.NodeBase expTree null;
    AvlTree.NodeBase result Treee.singleton(key, value);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
    */
  }

}
