
package com.aprog_lab.aprog_pl.shared_resources;

import Interfaces.Interface1;
import com.aprog_lab.aprog_pl.threads.Child;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

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

    private Queue<Child> exitQueue;
    private Queue<Child> enterQueue;
    private Semaphore exitSem, enterSem;
    private AtomicBoolean blocked;
    private CopyOnWriteArrayList<Child> entering, leaving;
    private Interface1 ifc;
    private Logger log;
    
    public Portal(String pname, Safe_Zone psz, Unsafe_Zone puz, CyclicBarrier pcb, Interface1 p_ifc, Logger p_log)
    {
        portal_name = pname;
        sz_connect = psz;
        uz_connect = puz;
        cb = pcb;
        exitQueue = new LinkedBlockingQueue();
        enterQueue = new LinkedBlockingQueue();
        exitSem = new Semaphore(0);
        enterSem = new Semaphore(0);
        blocked = new AtomicBoolean(false);
        entering = new CopyOnWriteArrayList<>();
        leaving = new CopyOnWriteArrayList<>();
        ifc = p_ifc;
        log = p_log;
    }
    
    
    public void enterPortalQueue(Child c, String status)
    {
        try
        {
            if(log.getPlaying())
            {
                if(status.equals("Exiting"))
                {
                    //System.out.println("Child: "+c.getID()+" is exiting using -> Portal: "+portal_name);
                    exitQueue.offer(c);
                    exitSem.acquire();
                    synchronized(this)
                    {
                        while(blocked.get())
                        {
                            wait();
                        }
                    }
                    //System.out.println("Child: "+id+" is entering portal...");            //DEBUG
                    leaving.add(c);
                    ifc.refreshPortalStats();
                    cb.await();
                    leaving.remove(c);
                    ifc.refreshPortalStats();
                }
                else
                {
                    //System.out.println("Child: "+c.getID()+" is entering -> Portal: "+portal_name);
                    enterQueue.offer(c);
                    enterSem.acquire();
                    synchronized(this)
                    {
                        while(blocked.get())
                        {
                            wait();
                        }
                    }
                    //System.out.println("Child: "+id+" is entering portal...");            //DEBUG
                    entering.add(c);
                    ifc.refreshPortalStats();
                    cb.await();
                    entering.remove(c);
                    ifc.refreshPortalStats();
                }                
            }
            else
            {
                log.waitLog();
                enterPortalQueue(c, status);
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
    public synchronized void openPortal()
    {
        if(!exitQueue.isEmpty() && cb.getParties()>0 && !blocked.get())
        {
            exitQueue.poll();
            exitSem.release();
            // Mostrar interfaz
            notifyAll();
            
        }
        else if(!enterQueue.isEmpty() && cb.getParties()>0 && !blocked.get())
        {
            enterQueue.poll();
            enterSem.release();
            // Mostrar en interfaz
            notifyAll();
        }
    }
    
    /*
    
    */
    public synchronized void enablePortal()
    {
        blocked.compareAndSet(true, false);
        notifyAll();
    }
    
    /*
    
    */
    public void disablePortal()
    {
        blocked.compareAndSet(false, true);
    }
    
    /*
    
    */
    public CopyOnWriteArrayList<Child> getEntering()
    {
        return entering;
    }
    
    /*
    
    */
    public CopyOnWriteArrayList<Child> getLeaving()
    {
        return leaving;
    }
    
}

