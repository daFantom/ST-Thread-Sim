
package com.aprog_lab.aprog_pl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Emanuel Baciu
 */

public class Portal
{
    private String portal_name;
    private Safe_Zone sz_connect;
    private Unsafe_Zone uz_connect;
    private CyclicBarrier cb;
//    private Queue<Child> portalQueue = new PriorityQueue(new Comparator<Child>()
//        {
//            @Override
//            public int compare(Child c1, Child c2)
//            {
//                return c1.getStatus().compareTo(c2.getStatus());
//            }
//        }
//    );
    private Queue<Child> exitQueue = new LinkedBlockingQueue();
    private Queue<Child> enterQueue = new LinkedBlockingQueue();
    Semaphore exitSem, enterSem;
    
    public Portal(String pname, Safe_Zone psz, Unsafe_Zone puz, CyclicBarrier pcb)
    {
        portal_name = pname;
        sz_connect = psz;
        uz_connect = puz;
        cb = pcb;
        exitSem = new Semaphore(0);
        enterSem = new Semaphore(0);
    }
    
    
    public void enterPortalQueue(String status)
    {
        try
        {
            if(status.equals("Exiting"))
            {
                exitSem.acquire(); cb.await();
            }
            else
            {
                enterSem.acquire(); cb.await();
            }
        }
        catch(InterruptedException ie)
        {
            System.out.println("IE at Portal-> enterPortalQueue(String)");
        }
        catch(BrokenBarrierException bbe)
        {
            System.out.println("BBE at Portal-> enterPortalQueue(String)");
        }
    }
    
    public void enablePortal()
    {
    }
    
    
}
