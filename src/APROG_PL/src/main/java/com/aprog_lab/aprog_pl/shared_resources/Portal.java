
package com.aprog_lab.aprog_pl.shared_resources;

import com.aprog_lab.aprog_pl.GUI_Initializers.GUI1_Manager;
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
    private GUI1_Manager ifc_mng;
    private LoggManager log;
    
    public Portal(String pname, CyclicBarrier pcb, GUI1_Manager p_ifc_mng, LoggManager p_log)
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
        ifc_mng = p_ifc_mng;
        log = p_log;
    }
    
    
    /* ==================== ENTERING OR EXITING A PORTAL METHOD ====================
        -   Used only by children. They introduce themselves (for better handling of other mechanisms)
            and introduce their current status. In other words, whether they are entering or exiting.
        -   If they are entering, they will add themselves into a LinkedBlockingQueue and wait inside
            the respective semaphore queue until they are notified by the PortalManager thread.
        -   This supposedly simulates the sequential passing of children through a portal, even though
            it is not fully shown in the GUI due to the speed of refreshing.
        -   In both cases (exiting or leaving) they will check whether the portal is blocked or not.
            If so, they will safely stay inside of the queue and cannot be attacked (due to how it's implemented)
        -   Once they are set free from the semaphore, they will add themselves temporally into a COWAL
            respective to their action (enter or exit) to show on the GUI. The total sum of these COWAL's should be
            the amount of space available for the cyclic barrier.
        -   After they pass, they will remove themselves from their respective COWAL.
    
    */
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
                    ifc_mng.refreshPortalStats();
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
                    ifc_mng.refreshPortalStats();
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
    
        -   This method is only used by the PortalManager Class. Essentially, checks
            whether there are children waiting on the exitQueue queue first
            (to simulate priority) and, if true, releases the first child on
            the exitSem semaphore's queue and removes it from the queue. Supposing it's the same one.
        -   Otherwise, does the same thing but for any child waiting on the enterSem
            semaphore and deletes its ID from the enterQueue ArrayList.
        -   Additional notifyAll() in case a child gets block righ after the Lab blackout event finishes so it doesn't
            stay blocked until the event happens again.
    */
    public synchronized void openPortal()
    {
        if(!exitQueue.isEmpty() && cb.getParties()>0 && !blocked.get())
        {
            exitQueue.poll();
            exitSem.release();
            notifyAll();
            
        }
        else if(!enterQueue.isEmpty() && cb.getParties()>0 && !blocked.get())
        {
            enterQueue.poll();
            enterSem.release();
            notifyAll();
        }
    }
    
    /* =============== EVENT HANDLING METHODS ===============
        -   Used by the Lab Blackout event class originating from the EventManager thread.
        -   Essentially sets a value to check for the children to check whether the portals are blocked (true) or not (false).
        -   When the event finishes, notifies all children stuck inside of this class' monitor. (Also done inside of the open portal
            method just in case any coincidence happens.
    */
    public synchronized void enablePortal()
    {
        blocked.compareAndSet(true, false);
        notifyAll();
    }
    
    public void disablePortal()
    {
        blocked.compareAndSet(false, true);
    }
    
    /* ================== CHILDREN ENTERING, LEAVING AND AMOUNT OF CHILDREN ON EACH QUEUE GETTERS ==================
        -   Each method does as the name suggests. The COWAL returning methods return the children who are
            on the wait line to pass through the portal inside of the cyclic barrier.
        -   The queue-related methods just return the amount of children waiting in the semaphore to pass through the portal.
        -   These following methods are used mostly for the GUIs.
    */
    public CopyOnWriteArrayList<Child> getEntering()
    {
        return entering;
    }

    public CopyOnWriteArrayList<Child> getLeaving()
    {
        return leaving;
    }
    
    public int getChildrenEnterQueueAmount()
    {
        return enterQueue.size();
    }

    public int getChildrenLeavingQueueAmount()
    {
        return exitQueue.size();
    }
    
}

