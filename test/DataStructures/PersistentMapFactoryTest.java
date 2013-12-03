/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import Utils.Numeric;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
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
public class PersistentMapFactoryTest {
  
  public PersistentMapFactoryTest() {
  }
  
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

  /**
   * Test of empty method, of class PersistentMapFactory.
   */
  @Test
  public void testEmpty() {

  }

  /**
   * Test of singleton method, of class PersistentMapFactory.
   */
  @Test
  public void testSingleton() {

  }

  private static final int sMapSize = 600;

  /**
   * Test of fromArray method, of class PersistentMapFactory.
   */
  private static void testFromStreamArrayImpl(final Class<?> c) {
    System.out.printf("FromStreamArray(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);

    final long seed = 12332713;
    final Random rng = new Random(seed);

    final int N = sMapSize;
    IntStream.rangeClosed(0, N).forEach(y -> {

      final int[] perm = Numeric.randomPermuation(0, y, y + 1, rng);

      final Stream<TuplesModule.Pair<Integer, Integer>> streamIncreasing  = IntStream.rangeClosed(0, y).mapToObj(x -> TuplesModule.Pair.create(x, x));
      final Stream<TuplesModule.Pair<Integer, Integer>> streamDecreasing  = IntStream.rangeClosed(0, y).mapToObj(x -> TuplesModule.Pair.create(y - x, y - x));
      final Stream<TuplesModule.Pair<Integer, Integer>> streamPermutation = Arrays.stream(perm).mapToObj(x -> TuplesModule.Pair.create(x, x));
      
      final ArrayList<TuplesModule.Pair<Integer, Integer>> arrayIncreasing  = IntStream.rangeClosed(0, y).mapToObj(x -> TuplesModule.Pair.create(x, x)).collect(Collectors.toCollection(ArrayList::new));
      final ArrayList<TuplesModule.Pair<Integer, Integer>> arrayDecreasing  = IntStream.rangeClosed(0, y).mapToObj(x -> TuplesModule.Pair.create(y - x, y - x)).collect(Collectors.toCollection(ArrayList::new));
      final ArrayList<TuplesModule.Pair<Integer, Integer>> arrayPermutation = Arrays.stream(perm).mapToObj(x -> TuplesModule.Pair.create(x, x)).collect(Collectors.toCollection(ArrayList::new));

      final PersistentMap<Integer, Integer, ?> mapFromStreamIncreasing  = mapFactory.fromStream(streamIncreasing);
      final PersistentMap<Integer, Integer, ?> mapFromStreamDecreasing  = mapFactory.fromStream(streamDecreasing);
      final PersistentMap<Integer, Integer, ?> mapFromStreamPermutation = mapFactory.fromStream(streamPermutation);

      final PersistentMap<Integer, Integer, ?> mapFromArrayIncreasing  = mapFactory.fromArray(arrayIncreasing);
      final PersistentMap<Integer, Integer, ?> mapFromArrayDecreasing  = mapFactory.fromArray(arrayDecreasing);
      final PersistentMap<Integer, Integer, ?> mapFromArrayPermutation = mapFactory.fromArray(arrayPermutation);

      TestUtils.checkMapProperties(mapFromStreamIncreasing);
      TestUtils.checkMapProperties(mapFromStreamDecreasing);
      TestUtils.checkMapProperties(mapFromStreamPermutation);

      TestUtils.checkMapProperties(mapFromArrayIncreasing);
      TestUtils.checkMapProperties(mapFromArrayDecreasing);
      TestUtils.checkMapProperties(mapFromArrayPermutation);
    });
  }

  @Test
  public void testFromStreamArray() {
    TestUtils.performTest(PersistentMapFactoryTest::testFromStreamArrayImpl);
  }

  private static void testFromStrictlyIncreasingStreamArrayImpl(final Class<?> c) {
    System.out.printf("FromStrictlyIncreasingStreamArray(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);

    final int N = sMapSize;
    IntStream.rangeClosed(0, N).forEach(y -> {

      final Stream<TuplesModule.Pair<Integer, Integer>> stream = IntStream.rangeClosed(0, y).mapToObj(x -> TuplesModule.Pair.create(x, x));

      final ArrayList<TuplesModule.Pair<Integer, Integer>> v =
              IntStream.rangeClosed(0, y)
                       .mapToObj(x -> TuplesModule.Pair.create(x, x))
                       .collect(Collectors.toCollection(ArrayList::new));

      final PersistentMap<Integer, Integer, ?> mapFromStream = mapFactory.fromStrictlyIncreasingStream(stream);
      final PersistentMap<Integer, Integer, ?> mapFromArray  = mapFactory.fromStrictlyIncreasingArray(v);

      TestUtils.checkMapProperties(mapFromStream);
      TestUtils.checkMapProperties(mapFromArray);
    });
  }

  @Test
  public void testFromStrictlyIncreasingStreamArray() {
    TestUtils.performTest(PersistentMapFactoryTest::testFromStrictlyIncreasingStreamArrayImpl);
  }

  private static void testFromStrictlyDecreasingStreamArrayImpl(final Class<?> c) {
    System.out.printf("FromStrictlyDecreasingStreamArray(%s)\n", c.getName());

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> mapFactory = TestUtils.makeFactory(c);
    
    final int N = sMapSize;
    IntStream.rangeClosed(0, N).forEach(y -> {

      final Stream<TuplesModule.Pair<Integer, Integer>> stream =
              IntStream.rangeClosed(0, y)
                       .map(x -> y + 1 - x)
                       .mapToObj(x -> TuplesModule.Pair.create(x, x));

      final ArrayList<TuplesModule.Pair<Integer, Integer>> v =
              IntStream.rangeClosed(0, y)
                       .map(x -> y + 1 - x)
                       .mapToObj(x -> TuplesModule.Pair.create(x, x))
                       .collect(Collectors.toCollection(ArrayList::new));

      final PersistentMap<Integer, Integer, ?> mapFromStream = mapFactory.fromStrictlyDecreasingStream(stream);
      final PersistentMap<Integer, Integer, ?> mapFromArray  = mapFactory.fromStrictlyDecreasingArray(v);

      TestUtils.checkMapProperties(mapFromStream);
      TestUtils.checkMapProperties(mapFromArray);
    });
  }

  @Test
  public void testFromStrictlyDecreasingStreamArray() {
    TestUtils.performTest(PersistentMapFactoryTest::testFromStrictlyDecreasingStreamArrayImpl);
  }
}
