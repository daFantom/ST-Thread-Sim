
package com.aprog_lab.aprog_pl.shared_resources;

import com.aprog_lab.aprog_pl.GUI.GUI1_Server;
import com.aprog_lab.aprog_pl.threads.Child;
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
    private CyclicBarrier cb;

    private Queue<Child> exitQueue;
    private Queue<Child> enterQueue;
    private Semaphore exitSem, enterSem;
    private AtomicBoolean blocked;
    private CopyOnWriteArrayList<Child> entering, leaving;
    private GUI1_Server ifc;
    private logManager log;
    
    public Portal(String pname, CyclicBarrier pcb, GUI1_Server p_ifc, logManager p_log)
    {
        portal_name = pname;
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
                    //System.out.println("Child: "+c.getID()+" is exiting using -> Portal: "+portal_name);      // DEBUG
                    exitQueue.offer(c);
                    exitSem.acquire();
                    synchronized(this)
                    {
                        while(blocked.get())
                        {
                            wait();
                        }
                    }
                    //System.out.println("Child: "+id+" is entering portal...");                                // DEBUG
                    leaving.add(c);
                    ifc.refreshPortalStats();
                    cb.await();
                    leaving.remove(c);

                }
                else
                {
                    //System.out.println("Child: "+c.getID()+" is entering -> Portal: "+portal_name);           // DEBUG
                    enterQueue.offer(c);
                    enterSem.acquire();
                    synchronized(this)
                    {
                        while(blocked.get())
                        {
                            wait();
                        }
                    }
                    //System.out.println("Child: "+id+" is entering portal...");                                // DEBUG
                    entering.add(c);
                    ifc.refreshPortalStats();
                    cb.await();
                    entering.remove(c);
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
    
    /*
    
    */
    public int getChildrenEnterQueueAmount()
    {
        return enterQueue.size();
    }
    /*
    
    */
    public int getChildrenLeavingQueueAmount()
    {
        return exitQueue.size();
    }
    
}

