
package com.aprog_lab.aprog_pl.shared_resources;

import Interfaces.Interface1;
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
    private boolean blocked;
    private ArrayList<String> entering, leaving;
    private Interface1 ifc;
    
    public Portal(String pname, Safe_Zone psz, Unsafe_Zone puz, CyclicBarrier pcb, Interface1 p_ifc)
    {
        portal_name = pname;
        sz_connect = psz;
        uz_connect = puz;
        cb = pcb;
        exitQueue = new LinkedBlockingQueue();
        enterQueue = new LinkedBlockingQueue();
        exitSem = new Semaphore(0);
        enterSem = new Semaphore(0);
        blocked = false;
        entering = new ArrayList<>();
        leaving = new ArrayList<>();
        ifc = p_ifc;
    }
    
    
    public void enterPortalQueue(String id, String status)
    {
        try
        {
            if(status.equals("Exiting"))
            {
                System.out.println("Child: "+id+" has entered ExitQueue -> Portal: "+portal_name);
                exitQueue.offer(id);
                exitSem.acquire();
                synchronized(this)
                {
                    while(blocked)
                    {
                        wait();
                    }
                }
                //System.out.println("Child: "+id+" is entering portal...");            //DEBUG
                leaving.add(id);
                ifc.refreshStats();
                cb.await();
                leaving.remove(id);
            }
            else
            {
                System.out.println("Child: "+id+" has entered EnterQueue -> Portal: "+portal_name);
                enterQueue.offer(id);
                enterSem.acquire();
                synchronized(this)
                {
                    while(blocked)
                    {
                        wait();
                    }
                }
                //System.out.println("Child: "+id+" is entering portal...");            //DEBUG
                entering.add(id);
                ifc.refreshStats();
                cb.await();
                entering.remove(id);
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
        if(!exitQueue.isEmpty() && cb.getParties()>0 && !blocked)
        {
            exitQueue.poll();
            exitSem.release();
            // Mostrar interfaz
            ifc.refreshStats();
            notifyAll();
            
        }
        else if(!enterQueue.isEmpty() && cb.getParties()>0 && !blocked)
        {
            enterQueue.poll();
            enterSem.release();
            // Mostrar en interfaz
            ifc.refreshStats();
            notifyAll();
        }
    }
    
    
    public synchronized void enablePortal()
    {
        blocked = false;
        notifyAll();
    }
    
    public void disablePortal()
    {
        blocked = true;
    }
    
    public ArrayList<String> getEntering()
    {
        return entering;
    }
    
    public ArrayList<String> getLeaving()
    {
        return leaving;
    }
}

