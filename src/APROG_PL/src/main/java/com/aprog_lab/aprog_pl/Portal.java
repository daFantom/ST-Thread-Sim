
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

    private Queue<String> exitQueue;
    private Queue<String> enterQueue;
    private Semaphore exitSem, enterSem;
    
    public Portal(String pname, Safe_Zone psz, Unsafe_Zone puz, CyclicBarrier pcb)
    {
        portal_name = pname;
        sz_connect = psz;
        uz_connect = puz;
        cb = pcb;
        exitQueue = new LinkedBlockingQueue();
        enterQueue = new LinkedBlockingQueue();
        exitSem = new Semaphore(0);
        enterSem = new Semaphore(0);
    }
    
    
    public void enterPortalQueue(String id, String status)
    {
        try
        {
            if(status.equals("Exiting"))
            {
                System.out.println("Child: "+id+" has entered ExitQueue");
                exitQueue.offer(id);
                exitSem.acquire();
                //System.out.println("Child: "+id+" is entering portal...");            //DEBUG
                cb.await();
            }
            else
            {
                System.out.println("Child: "+id+" has entered EnterQueue");
                enterQueue.offer(id);
                enterSem.acquire();
                //System.out.println("Child: "+id+" is entering portal...");            //DEBUG
                cb.await();
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
    
    /* ====== SPECIAL THREAD ACCESS METHOD ======
    
    This method is only used by the PortalManager Class. Essentially, checks
    whether there are children waiting on the exitQueue queue first
    (to simulate priority) and, if true, releases the first child on
    the exitSem semaphore's queue and removes its ID from the ArrayList.
    Otherwise, does the same thing but for any child waiting on the enterSem
    semaphore and deletes its ID from the enterQueue ArrayList.
    */
    public void enablePortal()
    {
        if(!exitQueue.isEmpty() && cb.getParties()>0)
        {
            exitQueue.poll();
            exitSem.release();
            
        }
        else if(!enterQueue.isEmpty() && cb.getParties()>0)
        {
            enterQueue.poll();
            enterSem.release();
        }
    }
}
