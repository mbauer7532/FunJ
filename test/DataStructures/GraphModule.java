/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.StructureGraphic.v1.DSutils;

/**
 *
 * @author Neo
 */
public final class GraphModule {
  final static int sLength = 44;
  final static int sWidth = 20;

  public static <K extends Comparable<K>, V> void showGraph(final PersistentMap<K, V, ?> pm) {
    DSutils.show(pm, sLength, sWidth);

    return;
  }

  public static void waitTime(final int secs) {
    try {
      Thread.sleep(secs * 1000);
    }
    catch (InterruptedException ex) {
      Logger.getLogger(IntMapModuleTest.class.getName()).log(Level.SEVERE, null, ex);
    }

    return;
  }
}
