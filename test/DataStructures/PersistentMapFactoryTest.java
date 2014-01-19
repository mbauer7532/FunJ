/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import DataStructures.TuplesModule.AssocPair;
import Utils.ArrayUtils;
import Utils.Numeric;
import java.util.ArrayList;
import java.util.Arrays;
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
public class PersistentMapFactoryTest {
  
  public PersistentMapFactoryTest() {}
  
  @BeforeClass
  public static void setUpClass() {}
  
  @AfterClass
  public static void tearDownClass() {}
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}

  /**
   * Test of empty method, of class PersistentMapFactory.
   */
  @Test
  public void testEmpty() {}

  private static final int sMapSize = 600;

   private static void testSimpleConstructorsImpl(final Class<?> c) {
    System.out.printf("verifyBasicMapProperties(%s)\n", c.getName());
    
    final PersistentMapFactory<String, Integer, ? extends PersistentMap<String, Integer, ?>> stringMapFactory = TestUtils.makeFactory(c);
    {
      final PersistentMap<String, Integer, ?> t = stringMapFactory.empty();
      TestUtils.checkMapProperties(t);
    }
    {
      final PersistentMap<String, Integer, ?> t = stringMapFactory.singleton("hi", 2);
      TestUtils.checkMapProperties(t);
    }
    {
      final PersistentMap<String, Integer, ?> t = stringMapFactory.singleton("hi", 2).insert((x, y) -> x, "there", 23);
      TestUtils.checkMapProperties(t);
    }
    {
      final PersistentMap<String, Integer, ?> t = stringMapFactory.singleton("hi", 2).insert("there", 23);
      TestUtils.checkMapProperties(t);
    }

    final PersistentMapFactory<Integer, Integer, ? extends PersistentMap<Integer, Integer, ?>> intMapFactory = TestUtils.makeFactory(c);
    {
      PersistentMap<Integer, Integer, ?> t = intMapFactory.empty();

      final int N = 10;
      ArrayList<PersistentMap<Integer, Integer, ?>> a = new ArrayList<>();
      for (int i = 0; i != N; ++i) {
        a.add(t);
        t = t.insert(i, i);
        assertEquals(i + 1, t.size());
        TestUtils.checkMapProperties(t);
      }
      a.add(t);
 
      for (int i = 0; i != a.size(); ++i) {
        final PersistentMap<Integer, Integer, ?> m = a.get(i);

        assertEquals(i, m.size());
        TestUtils.checkMapProperties(t);
        IntStream.range(0, i).forEach(n -> assertTrue(m.containsKey(n)));
      }

      for (int i = 0; i != N; ++i) {
        t = t.remove(i);
        
        assertEquals(N - i - 1, t.size());
        TestUtils.checkMapProperties(t);
        // After each removal also check that none of the stored trees has it's size changed.
        IntStream.range(0, a.size()).forEach(n -> assertEquals(n, a.get(n).size()));
      }
    }
  }

  @Test
  public void verifyMapPropertiesForSimpleCases() {
    TestUtils.performTest(PersistentMapFactoryTest::testSimpleConstructorsImpl);
  }

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

      final int[] perm = Numeric.randomPermutation(0, y, y + 1, rng);

      final Stream<PersistentMapEntry<Integer, Integer>> streamIncreasing  = IntStream.rangeClosed(0, y).mapToObj(x -> AssocPair.create(x, x));
      final Stream<PersistentMapEntry<Integer, Integer>> streamDecreasing  = IntStream.rangeClosed(0, y).mapToObj(x -> AssocPair.create(y - x, y - x));
      final Stream<PersistentMapEntry<Integer, Integer>> streamPermutation = Arrays.stream(perm).mapToObj(x -> AssocPair.create(x, x));

      final ArrayList<PersistentMapEntry<Integer, Integer>> arrayIncreasing  = ArrayUtils.toArrayList(IntStream.rangeClosed(0, y).mapToObj(x -> AssocPair.create(x, x)));
      final ArrayList<PersistentMapEntry<Integer, Integer>> arrayDecreasing  = ArrayUtils.toArrayList(IntStream.rangeClosed(0, y).mapToObj(x -> AssocPair.create(y - x, y - x)));
      final ArrayList<PersistentMapEntry<Integer, Integer>> arrayPermutation = ArrayUtils.toArrayList(Arrays.stream(perm).mapToObj(x -> AssocPair.create(x, x)));

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

      final Stream<PersistentMapEntry<Integer, Integer>> stream = IntStream.rangeClosed(0, y).mapToObj(x -> AssocPair.create(x, x));

      final ArrayList<PersistentMapEntry<Integer, Integer>> v =
              ArrayUtils.toArrayList(IntStream.rangeClosed(0, y)
                                              .mapToObj(x -> AssocPair.create(x, x)));

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

      final Stream<PersistentMapEntry<Integer, Integer>> stream =
              IntStream.rangeClosed(0, y)
                       .map(x -> y + 1 - x)
                       .mapToObj(x -> AssocPair.create(x, x));

      final ArrayList<PersistentMapEntry<Integer, Integer>> v =
              ArrayUtils.toArrayList(IntStream.rangeClosed(0, y)
                                              .map(x -> y + 1 - x)
                                              .mapToObj(x -> AssocPair.create(x, x)));

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
