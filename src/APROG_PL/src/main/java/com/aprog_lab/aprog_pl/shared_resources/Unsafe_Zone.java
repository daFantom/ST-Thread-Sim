package com.aprog_lab.aprog_pl.shared_resources;

import com.aprog_lab.aprog_pl.Interfaces.Interface1;
import com.aprog_lab.aprog_pl.threads.Demogorgon;
import com.aprog_lab.aprog_pl.threads.Child;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
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
    private Interface1 ifc;
    private Logger log;
    
    public Unsafe_Zone(String name, Interface1 p_ifc, Logger p_log)
    {
        ifc = p_ifc;
        zone_name = name;
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
                    ifc.refreshStats();
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
                    ifc.refreshStats();
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
                    ifc.refreshStats();
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
                    ifc.refreshStats();
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
                if(p<=0.3333333 && !target.isAttacked())
                {
                    hasAttacked = true;
                    target.gotAttacked();
                    log.logWrite("Demogorgon "+d.getID()+" attacked child "+target.getID()+". (captures: "+d.getLocalCaptured()+")");
                    return hasAttacked;
                }
                else if(target.isAttacked())
                {
                    //System.out.println("CHILD: "+target.getID()+" ALREADY ATTACKED");     // DEBUG
                    hasAttacked = false;
                    return hasAttacked;
                }
                else
                {
                    //System.out.println("MISSED ATTACK ON CHILD: "+target.getID());        // DEBUG
                    hasAttacked = false;
                    return hasAttacked;
                }   
            }
            else
            {
                return hasAttacked;
            }
        }
        else
        {
            log.waitLog();
            return hasAttacked;
        }  

    }
    
    /* Method used by children and EventManager
    
    */
    public void capture(String id)
    {
        if(!id.equals("Eleven"))
        {
            try
            {
                captured.incrementAndGet();
                synchronized(this)
                {
                    ifc.refreshCounters();
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
        else
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
    
}
