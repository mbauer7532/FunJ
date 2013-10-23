/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.IntStream;

/**
 *
 * @author Neo
 */
public class Numeric {

  /**
   *
   * @param low
   * @param high
   * @return Function returns a random integer in the range [low, high].
   */
  public static int randomInt(final int low, final int high) {
    if (low > high)
      throw new AssertionError("Low must be less than or equal to high.");
    return low + (int)(Math.random() * (double) (high - low + 1));
  }

  public static Set<Integer> randomSet(
          final int low,
          final int high,
          final int size) {
    if (low > high) {
      throw new AssertionError("Low must be less than or equal to high.");
    }
    if (size <= 0) {
      throw new AssertionError("Size must be greater than zero.");
    }
    if (high - low + 1 < size) {
      throw new AssertionError("Not enough unique elements in range.");
    }

    // Floyd's algorithm.
    final int j = high - size + 1;
    final Set<Integer> hset = new HashSet<>();

    for (int i = 0; i != size; ++i) {
      final int h = j + i;
      final int r = randomInt(low, h);

      final boolean alreadyThere = ! hset.add(r);
      if (alreadyThere) {
        hset.add(h);
      }
    }

    return hset;
  }

  public static void swap(final int[] arr, final int i, final int j) {
    final int t = arr[i];
    arr[i] = arr[j];
    arr[j] = t;
  }

  public static int[] randomPermuation(
          final int low,
          final int high,
          final int size) {
    final int[] array = new int[size];
    randomPermutation(low, high, array);

    return array;
  }

  public static void randomPermutation(
          final int low,
          final int high,
          final int[] array) {
    final int size = array.length;
    final int lastIdx = size - 1;
    final Iterator<Integer> it = randomSet(low, high, size).iterator();

    IntStream.range(0, size).forEach(i -> { array[i] = it.next(); });
    IntStream.range(0, lastIdx).forEach(n -> { swap(array, n, randomInt(n, lastIdx)); });

    return;
  }

  public static final double sGoldenRatio = (Math.sqrt(5.0) + 1.0) / 2.0;

  public static double log(final double x, final double base) {
    return Math.log(x) / Math.log(base);
  }

  public static boolean isEqWithinTolerance(
          final double d1,
          final double d2,
          final double tol) {
    final double absError = Math.abs(d1 - d2);
    return absError <= tol;
  }

  public static boolean isRelativeEqWithinTolerance(
          final double d1,
          final double d2,
          final double tol) {
    final double relError = Math.abs((d1 - d2) / d2);
    return relError <= tol;
  }

  // Hacker's delight 2nd edition
  private static final int u = 42; // This quantity is not used.
  private static final int[] nlzTab = new int[]
    {32, 31, u, 16, u, 30, 3, u, 15, u, u, u, 29, 10, 2, u,
     u, u, 12, 14, 21, u, 19, u, u, 28, u, 25, u, 9, 1, u,
     17, u, 4, u, u, u, 11, u, 13, 22, 20, u, 26, u, u, 18,
     5, u, u, 23, u, 27, u, 6, u, 24, 7, u, 8, u, 0, u};

  public static int nlz(final int y) {
    int x = y;
    x |= x >>> 1; // Propagate leftmost
    x |= x >>> 2; // 1-bit to the right.
    x |= x >>> 4;
    x |= x >>> 8;
    x |= x >>> 16;
    x *= 0x06EB14F9; // Multiplier is 7*255**3.

    return nlzTab[x >>> 26];
  }

  private static int highestBitDirect(final int y) {
    if (y == 0) {
      return -1;    // A sentinel value.
    }
    else {
      return 1 << (31 - nlz(y));
    }
  }

  private static final int[] sHighestBitCache = createHighestBitCache();

  private static final int sHighestBitCacheSize = 0x65336;
  private static final int sLog2HighestBitCacheSize = 16;
  
  private static int[] createHighestBitCache() {
    return IntStream.range(0, sHighestBitCacheSize)
                    .map(Numeric::highestBitDirect)
                    .toArray();
  }

  public static int highestBit(final int y) {
    final int high = y >>> sLog2HighestBitCacheSize;

    if (high > 0) {
      return sHighestBitCache[high] << 16;
    }
    else {
      if (y == 0) {
        throw new AssertionError("Zero does not have a highest bit.");
      }

      return sHighestBitCache[y];
    }
  }
}
