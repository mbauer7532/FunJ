/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Stream;

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
      throw new RuntimeException("Low must be less than or equal to high.");
    return low + (int)(Math.random() * (double) (high - low + 1));
  }

  public static int[] randomArray(final int low, final int high, final int size) {
    if (low > high) {
      throw new RuntimeException("Low must be less than or equal to high.");
    }
    if (size <= 0) {
      throw new RuntimeException("Size must be greater than zero.");
    }
    if (high - low + 1 < size) {
      throw new RuntimeException("Not enough unique elements in range.");
    }

    // Floyd's algorithm.
    final int j = high - size + 1;
    final HashSet<Integer> hset = new HashSet<>();
    final int[] arr = new int[size];

    for (int i = 0; i != size; ++i) {
      final int h = j + i;
      final int r = randomInt(low, h);

      final boolean added = hset.add(r);
      if (added) {
        arr[i] = r;
      }
      else {
        hset.add(h);
        arr[i] = h;
      }
    }

    return arr;
  }

  public static double goldenRatio = (Math.sqrt(5.0) + 1.0) / 2.0;

  public static double log(final double x, final double base) {
    return Math.log(x) / Math.log(base);
  }
}
