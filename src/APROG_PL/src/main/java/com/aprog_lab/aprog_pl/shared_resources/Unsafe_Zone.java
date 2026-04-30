package com.aprog_lab.aprog_pl.shared_resources;

import Interfaces.Interface1;
import com.aprog_lab.aprog_pl.threads.Demogorgon;
import com.aprog_lab.aprog_pl.threads.Child;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Emanuel Baciu
 */
public class Unsafe_Zone
{
    private final String zone_name;
    private ArrayList<Child> avail_children;
    private ArrayList<Demogorgon> avail_demos;
    private AtomicInteger captured;
    private Interface1 ifc;
    
    public Unsafe_Zone(String name, Interface1 p_ifc)
    {
        ifc = p_ifc;
        zone_name = name;
        avail_children = new ArrayList<>();
        avail_demos = new ArrayList<>();
        if(zone_name.equals("HIVE"))
        {
            captured = new AtomicInteger(0);
        }
    }

// ================================ ENTERING METHODS ================================
    /* Unfinished method to add a child to a specific zone.
    
    */
    public void enterUZChild(Child c)
    {
        synchronized(this)
        {
            if(!avail_children.contains(c))
            {
                avail_children.add(c);
                ifc.refreshStats();
                System.out.println("Child: "+c.getID()+" has entered unsafe zone: "+zone_name); 
            }     
        }
    }
    
    /* Unfinished method to add a child to a specific zone.
    
    */
    public void enterUZDemo(Demogorgon d)
    {
        synchronized(this)
        {
            if(!avail_demos.contains(d))
            {
                avail_demos.add(d);
                ifc.refreshStats();
                System.out.println("Demogorgon: "+d.getID()+" has entered unsafe zone: "+zone_name); 
            }
        }
    }
    
// ================================ EXITING METHODS ================================
    
    /* Unfinished method to remove a child from a specific zone.
    
    */
    public void exitUZChild(Child c)
    {
        synchronized(this)
        {
            if(avail_children.contains(c))
            {
                avail_children.remove(c);
                ifc.refreshStats();
                System.out.println("Child: "+c.getID()+" has exited unsafe zone: "+zone_name); 
            }   
        }  
    }
    
    /* Unfinished method to remove a child from a specific zone.
    
    */
    public void exitUZDemo(Demogorgon d)
    {
        synchronized(this)
        {
            if(avail_demos.contains(d))
            {
                avail_demos.remove(d);
                ifc.refreshStats();
                System.out.println("Demogorgon: "+d.getID()+" has exited unsafe zone: "+zone_name); 
            }  
        }
    }
    
    /* Child availability checker
    
    */
    public synchronized boolean hasChildren()
    {
        return (!avail_children.isEmpty());
    }
    
    
    /*  Method used by Demogorgons, used for attacking a random available child.
        ||WIP||
    
    */
    public synchronized boolean attackChild(double p)
    {
        boolean hasAttacked = false;
        if(!avail_children.isEmpty())
        {
            int selected_child = (int) (Math.random() * avail_children.size());
            Child target = avail_children.get(selected_child);
            if(p<=0.3333333 && !target.isAttacked())
            {
                hasAttacked = true;
                target.gotAttacked();
                //System.out.println("SUCCESSFULLY ATTACKED CHILD: "+target.getID());   // DEBUG
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
    
    /* Method used by children
    
    */
    public void capture(String id)
    {
        if(!id.equals("Eleven"))
        {
            try
            {
                captured.incrementAndGet();
                ifc.refreshStats();
                synchronized(this)
                {
                    //System.out.println("A CHILD HAS BEEN CAPTURED!!!!!");                 // DEBUG
                    wait();
                    //System.out.println("A CHILD HAS BEEN RELEASED!!!!!");                 // DEBUG
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
                notify();
                captured.decrementAndGet();
                ifc.refreshStats();
            }
        }
    }
    
    // DEBUG METHOD
    public String getName()
    {
        return zone_name;
    }
    
    public int getAmountChildren()
    {
        return avail_children.size();
    }
    
    public void showDemos()
    {
        System.out.println("============== "+zone_name+" ==============");
        for(int i=0;i<avail_demos.size();i++)
        {
            System.out.println(avail_demos.get(i).getID());
        }
    }
    
    public ArrayList<Child> getAvailChildren()
    {
        return avail_children;
    }
    
    public ArrayList<Demogorgon> getAvailDemos()
    {
        return avail_demos;
    }
    
    public int getCapturedChildren()
    {
        return captured.get();
    }
}
