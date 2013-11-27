/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

/**
 *
 * @author Neo
 */
public class PersistentMapTest {
  public static void verifyMapPropertiesForSimpleCases() {
    System.out.println("verifyRedBlackProperties");
    {
      final Tree<String, Integer> t = RedBlackTreeModule.empty();
      checkRedBlackTreeProperties(t);
    }
    {
      final Tree<String, Integer> t = RedBlackTreeModule.singleton("hi", 2);
      checkRedBlackTreeProperties(t);
    }
    {
      final Tree<String, Integer> t = RedBlackTreeModule.singleton("hi", 2).insert((x, y) -> x, "there", 23);
      checkRedBlackTreeProperties(t);
    }
    {
      final Tree<String, Integer> t = RedBlackTreeModule.singleton("hi", 2).insert("there", 23);
      checkRedBlackTreeProperties(t);
    }
    {
      Tree<Integer, Integer> t = RedBlackTreeModule.empty();

      final int N = 10;
      ArrayList<Tree<Integer, Integer>> a = new ArrayList<>();
      for (int i = 0; i != N; ++i) {
        a.add(t);
        t = t.insert(i, i);
        assertEquals(i + 1, t.size());
        checkRedBlackTreeProperties(t);
      }
      a.add(t);
 
      for (int i = 0; i != a.size(); ++i) {
        final Tree<Integer, Integer> q = a.get(i);
        
        assertEquals(i, q.size());
        checkRedBlackTreeProperties(t);
        IntStream.range(0, i).forEach(n -> assertTrue(q.containsKey(n)));
      }

      for (int i = 0; i != N; ++i) {
        t = t.remove(i);
        
        assertEquals(N - i - 1, t.size());
        checkRedBlackTreeProperties(t);
        // After each removal also check that none of the stored trees has it's size changed.
        IntStream.range(0, a.size()).forEach(n -> assertEquals(n, a.get(n).size()));
      }
    }
  }
}
