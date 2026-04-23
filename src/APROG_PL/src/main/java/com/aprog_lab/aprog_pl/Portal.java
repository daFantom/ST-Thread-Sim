
package com.aprog_lab.aprog_pl;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author Emanuel Baciu
 */

public class Portal {
    private Queue<Child> portalQueue = new PriorityQueue(new Comparator<Child>()
    {
        @Override
        public int compare(Child c1, Child c2)
        {
            return compare(c1, c2);
        }
    }
    );
}
