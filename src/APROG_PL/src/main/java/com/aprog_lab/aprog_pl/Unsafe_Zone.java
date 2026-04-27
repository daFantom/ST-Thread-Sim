package com.aprog_lab.aprog_pl;

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
    
    public Unsafe_Zone(String name)
    {
        zone_name = name;
        avail_children = new ArrayList<>();
        avail_demos = new ArrayList<>();
        if(zone_name.equals("HIVE"))
        {
            captured = new AtomicInteger(0);
        }
    }
  
    // Unfinished method to add a child to a specific zone.
    public void enterUnsafeSafeZoneChild(Child c)
    {
        if(c.getID().contains("C"))
        {
            avail_children.add(c);
            System.out.println("Child: "+c.getID()+" has entered unsafe zone: "+zone_name);   
        }
    }
    
    // Unfinished method to add a child to a specific zone.
    public void enterUnsafeSafeZoneDemo(Demogorgon d)
    {
        if(d.getID().contains("D"))
        {
            avail_demos.add(d);
            System.out.println("Child: "+d.getID()+" has entered unsafe zone: "+zone_name);   
        }
    }
    
    // Unfinished method to remove a child from a specific zone.
    public void exitUnsafeSafeZoneChild(Child c)
    {
        if(c.getID().contains("C"))
        {
            avail_children.remove(c);
            System.out.println("Child: "+c.getID()+" has exited unsafe zone: "+zone_name);   
        }
    }
    
    // Unfinished method to remove a child from a specific zone.
    public void exitUnsafeSafeZoneDemo(Demogorgon d)
    {
        if(d.getID().contains("D"))
        {
            avail_demos.remove(d);
            System.out.println("Child: "+d.getID()+" has exited unsafe zone: "+zone_name);   
        }
    }
    
    // Child availability checker
    public synchronized boolean hasChildren()
    {
        return !avail_children.isEmpty();
    }
    
    
    /*  Method used by Demogorgons, used for attacking a random available child.
        ||WIP||
    
    */
    public synchronized boolean attackChild()
    {
        boolean hasAttacked = false;
        try
        {
            Thread.sleep((int)((Math.random()*1000)+500));
            int selected_child = (int) (Math.random() * avail_children.size());
            Child target = avail_children.get(selected_child);
            Random randObj = new Random();
            float p = 1/3;
            if(randObj.nextFloat()<=(p))
            {
                hasAttacked = true;
                target.gotAttacked();
            }
        }
        catch(InterruptedException ie)
        {
            System.out.println("IE at Unsafe_Zone->Attack");
        }
        finally
        {
            return hasAttacked;
        }
    }
    
    // Method used by children
    public void capture()
    {
        try
        {
            captured.incrementAndGet();
            synchronized(this)
            {
                wait();
            }
        }
        catch(InterruptedException ie)
        {
            System.out.println("IE at Unsafe_zone->capture()");
        }
    }
    
    // DEBUG METHOD
    public String getName()
    {
        return zone_name;
    }
}
