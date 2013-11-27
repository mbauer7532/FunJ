/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.BrotherTreeModule;
import DataStructures.TuplesModule.Pair;
import Utils.Numeric;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.StructureGraphic.v1.DSutils;
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
public class AvlTreeModuleTest {
  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

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
      t = t.insert(i * 10, "hi");
      at.add(t);
    }

    for (int i = 0, cnt = at.size(); i != cnt; ++i) {
      assertEquals(at.get(i).size(), i);
    }

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

    final long seed = 12345678;
    final Random rng = new Random(seed);

    final int m = -1000;
    final int n = 1000;
    final int Experiments = 500;
    final int N = 512;

    for (int i = 0; i != Experiments; ++i) {
      final int[] data = Utils.Numeric.randomPermuation(m, n, N, rng);
      AvlTreeModule.Tree<Integer, Integer> t0 = AvlTreeModule.empty();

      AvlTreeModule.Tree<Integer, Integer> t
              = Arrays.stream(data)
                      .boxed()
                      .reduce(t0,
                              ((AvlTreeModule.Tree<Integer, Integer> tx, Integer x) -> tx.insert(x, x)),
                              ((AvlTreeModule.Tree<Integer, Integer> z1, AvlTreeModule.Tree<Integer, Integer> z2) -> null));

      final int expectedSize = N;
      final double expectedHeight = 2 * 9;

      final int avlTreeSize = t.size();
      final int avlTreeHeight = t.height();

      assertEquals(expectedSize, avlTreeSize);
      assertTrue(avlTreeHeight < expectedHeight);
    }
  }

  @Test
  public void testBrother() {
    System.out.println("Brother");

    final int low = 36;
    final int high = 36;

    IntStream.rangeClosed(low, high).forEach(n -> {
      final ArrayList<Pair<Integer, Integer>> v =
              IntStream.rangeClosed(1, n)
                       .mapToObj(i -> Pair.create(n + 1 - i, n + 1 - i))
                       .collect(Collectors.toCollection(ArrayList::new));
      final PersistentMap<Integer, Integer, BrotherTreeModule.Tree<Integer,Integer>> pm
              = BrotherTreeModule.fromArray(v);

      //GraphModule.showGraph(pm);
      //GraphModule.waitTime(1);
    });
    //GraphModule.waitTime(1);
  }

  private static <K, V> Optional<Pair<K, V>> toPair(final Map.Entry<K, V> e) {
    return e == null ? Optional.empty() : Optional.of(Pair.create(e.getKey(), e.getValue()));
  }

  @Test
  public void testLowerHigherPairRandomInputs() {
    System.out.println("lowerHigherPairRandomInputs");

    final long seed = 1253327;
    final Random rng = new Random(seed);

    final int N = 500;
    final int low = -50, high = 600;
    final int size = 180;

    IntStream.range(0, N).forEach(x -> {
      final int[] perm1 = Numeric.randomPermuation(low, high, size, rng);

      final ArrayList<Pair<Integer, Integer>> v =
              Arrays.stream(perm1)
                    .mapToObj(i -> Pair.create(i, i))
                    .collect(Collectors.toCollection(ArrayList::new));
      final BrotherTreeModule.Tree<Integer, Integer> pm = BrotherTreeModule.fromArray(v);
      final TreeMap<Integer, Integer> tm = new TreeMap<>();
      v.stream().forEach(p -> tm.put(p.mx1, p.mx2));

      Numeric.randomSet(low - 200, high + 200, 500, rng).forEach(n -> {
        final Optional<Pair<Integer, Integer>>
                tmlowerExpected  = toPair(tm.lowerEntry(n)),
                pmLower          = pm.lowerPair(n),
                tmHigherExpected = toPair(tm.higherEntry(n)),
                pmHigher         = pm.higherPair(n);

        if (! tmlowerExpected.equals(pmLower)) {
 //         final Optional<Pair<Integer, Integer>> zz = pm.lowerPair(n);
          GraphModule.showGraph(pm);
          System.out.println("n = " + n);
          System.out.println("Expected = " + tmlowerExpected);
          System.out.println("Actual = " + pmLower);
          GraphModule.waitTime(2000);
        }
        assertEquals(tmlowerExpected, pmLower);
        assertEquals(tmHigherExpected, pmHigher);
      });
    });
  }
}
