package com.aprog_lab.aprog_pl;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Emanuel Baciu
 */
public class Safe_Zone
{
    private final String zone_name;
    private ArrayList<String> avail_children;
    private AtomicInteger bloodCount;
    
    public Safe_Zone(String name)
    {
        zone_name = name;
        avail_children = new ArrayList<>();                                         // Children actively wandering the zone.
        if(zone_name.equals("WSQK Radio"))                                          // Blood count is initialized only for the WSQK Radio safezone. Otherwise, unused.
        {
            bloodCount = new AtomicInteger(0);
        }
    }
    
    // Test method.
    public String getName()
    {
        return zone_name;
    }
    
    // Unfinished method to add a child to a specific zone.
    public void enterSafeZone(String cid)
    {
        avail_children.add(cid);
        System.out.println("Child: "+cid+" has entered safezone: "+zone_name);
    }
    
    // Unfinished method to remove a child from a specific zone.
    public void exitSafeZone(String cid)
    {
        avail_children.remove(cid);
        System.out.println("Child: "+cid+" has exited safezone: "+zone_name);
    }
    
    // Unfinished method for incrementing the blood counter
    public void incrementBloodCount()
    {
        if(this.getName().equals("WSQK Radio"))
        {
            bloodCount.incrementAndGet();                                           // The counter gets incremented for each child that escaped.
        }
        else
        {
            System.out.println("Debug: This method won't do anything in this"
                    + " zone");
        }
    }
}
