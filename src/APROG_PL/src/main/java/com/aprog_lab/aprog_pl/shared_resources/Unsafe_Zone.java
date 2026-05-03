package com.aprog_lab.aprog_pl.shared_resources;

import com.aprog_lab.aprog_pl.GUI.GUI1_Server;
import com.aprog_lab.aprog_pl.threads.Demogorgon;
import com.aprog_lab.aprog_pl.threads.Child;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Emanuel Baciu
 */
public class Unsafe_Zone
{
    private final String zone_name;
    private CopyOnWriteArrayList<Child> avail_children;
    private CopyOnWriteArrayList<Demogorgon> avail_demos;
    private AtomicInteger captured;
    private GUI1_Server ifc;
    private logManager log;
    private AtomicBoolean labBlackout;
    
    public Unsafe_Zone(String name, GUI1_Server p_ifc, logManager p_log)
    {
        ifc = p_ifc;
        zone_name = name;
        labBlackout = new AtomicBoolean(false);
        avail_children = new CopyOnWriteArrayList<>();
        avail_demos = new CopyOnWriteArrayList<>();
        if(zone_name.equals("HIVE"))
        {
            captured = new AtomicInteger(0);
        }
        log = p_log;
    }

// ================================ ENTERING METHODS ================================
    /* Unfinished method to add a child to a specific zone.
    
    */
    public void enterUZChild(Child c)
    {
        if(log.getPlaying())
        {
            synchronized(this)
            {
                if(!avail_children.contains(c))
                {
                    avail_children.add(c);
                    ifc.refreshZoneStats();
                    //System.out.println("Child: "+c.getID()+" has entered unsafe zone: "+zone_name);  
                } 
            }
        }
        else
        {
            log.waitLog();
            enterUZChild(c);
        }
    }
    
    /* Unfinished method to add a child to a specific zone.
    
    */
    public void enterUZDemo(Demogorgon d)
    {
        if(log.getPlaying())
        {
            synchronized(this)
            {
                if(!avail_demos.contains(d))
                {
                    avail_demos.add(d);
                    ifc.refreshZoneStats();
                    //System.out.println("Demogorgon: "+d.getID()+" has entered unsafe zone: "+zone_name);  
                }
            }
        }
        else
        {
            log.waitLog();
            enterUZDemo(d);
        }
    }
    
// ================================ EXITING METHODS ================================
    
    /* Unfinished method to remove a child from a specific zone.
    
    */
    public void exitUZChild(Child c)
    {
        if(log.getPlaying())
        {
            synchronized(this)
            {
                if(avail_children.contains(c))
                {
                    avail_children.remove(c);
                    ifc.refreshZoneStats();
                    //System.out.println("Child: "+c.getID()+" has exited unsafe zone: "+zone_name);
                }  
            }
        }
        else
        {
            log.waitLog();
            exitUZChild(c);
        } 
    }
    
    /* Unfinished method to remove a child from a specific zone.
    
    */
    public void exitUZDemo(Demogorgon d)
    {
        if(log.getPlaying())
        {
            synchronized(this)
            {
                if(avail_demos.contains(d))
                {
                    avail_demos.remove(d);
                    ifc.refreshZoneStats();
                    //System.out.println("Demogorgon: "+d.getID()+" has exited unsafe zone: "+zone_name);    
                }    
            } 
        }
        else
        {
            log.waitLog();
            exitUZDemo(d);
        }
    }
    
    /* Child availability checker
    
    */
    public synchronized boolean hasChildren()
    {
        return (!avail_children.isEmpty());
    }
    
    
    /*  Method used by Demogorgons, used for attacking a random available child.

    
    */
    public synchronized boolean attackChild(double p, Demogorgon d)
    {
        boolean hasAttacked = false;
        if(log.getPlaying())
        {
            if(!avail_children.isEmpty())
            {
                int selected_child = (int) (Math.random() * avail_children.size());
                Child target = avail_children.get(selected_child);
                if(p<=0.33333333333 && !target.isAttacked())
                {
                    hasAttacked = true;
                    target.gotAttacked();
                    log.logWrite("Demogorgon "+d.getID()+" attacked child "+target.getID()+". (captures: "+d.getLocalCaptured()+")");
                }
                else if(target.isAttacked())
                {
                    //System.out.println("CHILD: "+target.getID()+" ALREADY ATTACKED");     // DEBUG
                    hasAttacked = false;
                }
                else
                {
                    //System.out.println("MISSED ATTACK ON CHILD: "+target.getID());        // DEBUG
                    hasAttacked = false;
                }   
            }
            else
            {
                hasAttacked= false;
            }
        }
        else
        {
            log.waitLog();
            hasAttacked = false;
        }  
        return hasAttacked;
    }
    
    /* Method used by children and EventManager
    
    */
    public void capture(String id)
    {
        try
        {
            synchronized(this)
            {
                captured.incrementAndGet();
                ifc.refreshCounters();
                System.out.println("Child: "+id+" has been captured");
                log.logWrite("Child: "+id+" has been captured");
                wait();
                log.logWrite("Child: "+id+" has been released");
            }
        }
        catch(InterruptedException ie)
        {
            System.out.println("IE at Unsafe_zone->capture()");
        }
    }
    
    public void save()
    {
        synchronized(this)
        {
            if(captured.get()>0)
            {
                captured.decrementAndGet();
                notify();
                ifc.refreshCounters();
            }
        }
    }
    
    /*
    
    */
    public String getName()
    {
        return zone_name;
    }
    /*
    
    */
    public synchronized int getAmountChildren()
    {
        return avail_children.size();
    }
    /*
    
    */
    public synchronized void showDemos()
    {
        System.out.println("============== "+zone_name+" ==============");
        for(int i=0;i<avail_demos.size();i++)
        {
            System.out.println(avail_demos.get(i).getID());
        }
    }
    
    /*
    
    */
    public synchronized CopyOnWriteArrayList<Child> getAvailChildren()
    {
        
        return avail_children;
    }
    
    /*
    
    */
    public synchronized CopyOnWriteArrayList<Demogorgon> getAvailDemos()
    {
        return avail_demos;
    }
    
    /*
    
    */
    public int getCapturedChildren()
    {
        return captured.get();
    }
    
    public void disableUnsafeZonesDemos()
    {
        labBlackout.compareAndSet(false, true);
    }
    
    public void enableUnsafeZonesDemos()
    {
        labBlackout.compareAndSet(true, false);
    }
    
    public boolean getBlocked()
    {
        return labBlackout.get();
    }
    
}
