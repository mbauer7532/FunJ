/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utils;

import java.util.Iterator;
import java.util.List;
import java.util.function.IntPredicate;

/**
 *
 * @author Neo
 */
public class ArrayUtils {
  private static <T extends Comparable<T>> boolean consecutiveElemsHaveProperty(
          final List<T> lst,
          final IntPredicate pred) {
    if (lst.isEmpty()) {
      return true;
    }
    else {
      final Iterator<T> it = lst.iterator();
      T x1 = it.next();

      while (it.hasNext()) {
        final T x2 = it.next();
        final int res = x1.compareTo(x2);
        if (! pred.test(res)) {
          return false;
        }
        x1 = x2;
      }

      return true;
    }
  }

  public static <T extends Comparable<T>> boolean isIncreasing(final List<T> lst) {
    return consecutiveElemsHaveProperty(lst, res -> res <= 0);
  }

  public static <T extends Comparable<T>> boolean isStrictlyIncreasing(final List<T> lst) {
    return consecutiveElemsHaveProperty(lst, res -> res < 0);
  }

  public static <T extends Comparable<T>> boolean isDecreasing(final List<T> lst) {
    return consecutiveElemsHaveProperty(lst, res -> res >= 0);
  }

  public static <T extends Comparable<T>> boolean isStrictlyDecreasing(final List<T> lst) {
    return consecutiveElemsHaveProperty(lst, res -> res > 0);
  }
}