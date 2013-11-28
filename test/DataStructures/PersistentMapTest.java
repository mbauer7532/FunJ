/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.Pair;
import Utils.Numeric;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
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
public class PersistentMapTest {
  private static final List<PersistentMapFactory> mMapFactories
          = Arrays.asList(new PersistentMapFactory[]
                               {
                                 RedBlackTreeModule.makeFactory(),
                                 AvlTreeModule.makeFactory(),
                                 BrotherTreeModule.makeFactory()
                               });
  
  public PersistentMapTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  private static void performTest(final Consumer<PersistentMapFactory> testFn) {
    mMapFactories.forEach(testFn);
  }

  private static <K extends Comparable<K>, V> void checkMapProperties(final PersistentMap<K, V, ?> t) {
    final Pair<Boolean, String> res = t.verifyMapProperties();

    if (! res.mx1) {
//      GraphModule.showGraph(t);
//      GraphModule.waitTime(1000);
      assertTrue(res.mx2, false);
    }
  }

  private static void verifyMapPropertiesForSimpleCasesImpl(final PersistentMapFactory mapFactory) {
    System.out.printf("verifyBasicMapProperties(%s)\n", mapFactory.getMapName());
    {
      final PersistentMap<String, Integer, ?> t = mapFactory.empty();
      checkMapProperties(t);
    }
    {
      final PersistentMap<String, Integer, ?> t = mapFactory.singleton("hi", 2);
      checkMapProperties(t);
    }
    {
      final PersistentMap<String, Integer, ?> t = mapFactory.singleton("hi", 2).insert((x, y) -> x, "there", 23);
      checkMapProperties(t);
    }
    {
      final PersistentMap<String, Integer, ?> t = mapFactory.singleton("hi", 2).insert("there", 23);
      checkMapProperties(t);
    }
    {
      PersistentMap<Integer, Integer, ?> t = mapFactory.empty();

      final int N = 10;
      ArrayList<PersistentMap<Integer, Integer, ?>> a = new ArrayList<>();
      for (int i = 0; i != N; ++i) {
        a.add(t);
        t = t.insert(i, i);
        assertEquals(i + 1, t.size());
        checkMapProperties(t);
      }
      a.add(t);
 
      for (int i = 0; i != a.size(); ++i) {
        final PersistentMap<Integer, Integer, ?> q = a.get(i);

        assertEquals(i, q.size());
        checkMapProperties(t);
        IntStream.range(0, i).forEach(n -> assertTrue(q.containsKey(n)));
      }

      for (int i = 0; i != N; ++i) {
        t = t.remove(i);
        
        assertEquals(N - i - 1, t.size());
        checkMapProperties(t);
        // After each removal also check that none of the stored trees has it's size changed.
        IntStream.range(0, a.size()).forEach(n -> assertEquals(n, a.get(n).size()));
      }
    }
  }

  @Test
  public void verifyMapPropertiesForSimpleCases() {
    performTest(PersistentMapTest::verifyMapPropertiesForSimpleCasesImpl);
  }

  private static void verifyMapPropertiesRandomSampleImpl(final PersistentMapFactory mapFactory) {
    System.out.printf("verifyMapPropertiesRandomSample(%s)\n", mapFactory.getMapName());

    final long seed = 12532731;
    final Random rng = new Random(seed);

    final int numIters = 20;
    final int low = 1, high = 32*32*32*32/8;
    final int size = high/2;

    int[] s = new int[3];
    s[1] = Integer.MIN_VALUE;
    s[2] = Integer.MAX_VALUE;

    IntStream.range(0, numIters).forEach(x -> {
      final int[] perm1 = Numeric.randomPermuation(low, high, size, rng);
      assertEquals(size, perm1.length);
      System.out.println(x);
      PersistentMap<Integer, Integer, ?> t = mapFactory.empty();
      for (int i = 0; i != size; ++i) {
        //assertEquals(i, t.size());
        //checkMapProperties(t);
        t = t.insert(perm1[i], perm1[i]);
      }
      //assertEquals(size, t.size());
      //checkMapProperties(t);
      final int h = t.height();
      s[0] += h;
      s[1] = Math.max(s[1], h);
      s[2] = Math.min(s[2], h);

      final int[] perm2 = Numeric.randomPermuation(0, size - 1, size, rng);

      for (int i = 0; i != size; ++i) {
        final int n = perm1[perm2[i]];
        assertTrue(t.containsKey(n));
        t = t.remove(n);
        assertTrue(! t.containsKey(n));
//        assertEquals(size - i - 1, t.size());
//        checkMapProperties(t);
      }
    });
    
    System.out.println("Average height: " + s[0] / (double) numIters);
    System.out.println("Max height: " + s[1]);
    System.out.println("Min height: " + s[2]);
  }

  @Test
  public void verifyMapPropertiesRandomSample() {
    performTest(PersistentMapTest::verifyMapPropertiesRandomSampleImpl);
  }

  @Test
  public void avlTest() {
    long startTime = System.nanoTime();
    
    final PersistentMapFactory mapFactory = AvlTreeModule.makeFactory();
    System.out.printf("%s\n", mapFactory.getMapName());

    final long seed = 12532731;
    final Random rng = new Random(seed);

    final int numIters = 20;
    final int low = 1, high = 32*32*32*32/8;
    final int size = high/2;

    int[] s = new int[3];
    s[1] = Integer.MIN_VALUE;
    s[2] = Integer.MAX_VALUE;

    IntStream.range(0, numIters).forEach(x -> {
      final int[] perm1 = Numeric.randomPermuation(low, high, size, rng);
      assertEquals(size, perm1.length);
      PersistentMap<Integer, Integer, ?> t = mapFactory.empty();
      for (int i = 0; i != size; ++i) {
        t = t.insert(perm1[i], perm1[i]);
      }

      final int h = t.height();
      s[0] += h;
      s[1] = Math.max(s[1], h);
      s[2] = Math.min(s[2], h);

      final int[] perm2 = Numeric.randomPermuation(0, size - 1, size, rng);

      for (int i = 0; i != size; ++i) {
        final int n = perm1[perm2[i]];
        assertTrue(t.containsKey(n));
        t = t.remove(n);
        assertTrue(! t.containsKey(n));
//        assertEquals(size - i - 1, t.size());
//        checkMapProperties(t);
      }
    });

//    System.out.println("Average height: " + s[0] / (double) numIters);
//    System.out.println("Max height: " + s[1]);
//    System.out.println("Min height: " + s[2]);
    
    long stopTime = System.nanoTime();
    long d = TimeUnit.SECONDS.convert((stopTime - startTime), TimeUnit.MILLISECONDS);
    System.out.printf("TimeSpent = %d\n", d);
  }

  @Test
  public void brotherTest() {
    long startTime = System.nanoTime();
    
    final PersistentMapFactory mapFactory = BrotherTreeModule.makeFactory();
    System.out.printf("%s\n", mapFactory.getMapName());

    final long seed = 12532731;
    final Random rng = new Random(seed);

    final int numIters = 20;
    final int low = 1, high = 32*32*32*32/8;
    final int size = high/2;

    int[] s = new int[3];
    s[1] = Integer.MIN_VALUE;
    s[2] = Integer.MAX_VALUE;

    IntStream.range(0, numIters).forEach(x -> {
      final int[] perm1 = Numeric.randomPermuation(low, high, size, rng);
      assertEquals(size, perm1.length);
      PersistentMap<Integer, Integer, ?> t = mapFactory.empty();
      for (int i = 0; i != size; ++i) {
        t = t.insert(perm1[i], perm1[i]);
      }

      final int h = t.height();
      s[0] += h;
      s[1] = Math.max(s[1], h);
      s[2] = Math.min(s[2], h);

      final int[] perm2 = Numeric.randomPermuation(0, size - 1, size, rng);

      for (int i = 0; i != size; ++i) {
        final int n = perm1[perm2[i]];
        assertTrue(t.containsKey(n));
        t = t.remove(n);
        assertTrue(! t.containsKey(n));
//        assertEquals(size - i - 1, t.size());
//        checkMapProperties(t);
      }
    });

//    System.out.println("Average height: " + s[0] / (double) numIters);
//    System.out.println("Max height: " + s[1]);
//    System.out.println("Min height: " + s[2]);
    
    long stopTime = System.nanoTime();
    long d = TimeUnit.SECONDS.convert((stopTime - startTime), TimeUnit.MILLISECONDS);
    System.out.printf("TimeSpent = %d\n", d);
  }

  @Test
  public void rbTest() {
    long startTime = System.nanoTime();
    
    final PersistentMapFactory mapFactory = RedBlackTreeModule.makeFactory();
    System.out.printf("%s\n", mapFactory.getMapName());

    final long seed = 12532731;
    final Random rng = new Random(seed);

    final int numIters = 20;
    final int low = 1, high = 32*32*32*32/8;
    final int size = high/2;

    int[] s = new int[3];
    s[1] = Integer.MIN_VALUE;
    s[2] = Integer.MAX_VALUE;

    IntStream.range(0, numIters).forEach(x -> {
      final int[] perm1 = Numeric.randomPermuation(low, high, size, rng);
      assertEquals(size, perm1.length);
      PersistentMap<Integer, Integer, ?> t = mapFactory.empty();
      for (int i = 0; i != size; ++i) {
        t = t.insert(perm1[i], perm1[i]);
      }

      final int h = t.height();
      s[0] += h;
      s[1] = Math.max(s[1], h);
      s[2] = Math.min(s[2], h);

      final int[] perm2 = Numeric.randomPermuation(0, size - 1, size, rng);

      for (int i = 0; i != size; ++i) {
        final int n = perm1[perm2[i]];
        assertTrue(t.containsKey(n));
        t = t.remove(n);
        assertTrue(! t.containsKey(n));
//        assertEquals(size - i - 1, t.size());
//        checkMapProperties(t);
      }
    });

//    System.out.println("Average height: " + s[0] / (double) numIters);
//    System.out.println("Max height: " + s[1]);
//    System.out.println("Min height: " + s[2]);
    
    long stopTime = System.nanoTime();
    long d = TimeUnit.SECONDS.convert((stopTime - startTime), TimeUnit.MILLISECONDS);
    System.out.printf("TimeSpent = %d\n", d);
  }

}
