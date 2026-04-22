package com.aprog_lab.aprog_pl;

import java.util.ArrayList;

/**
 *
 * @author Emanuel Baciu
 */
public class Safe_Zone
{
    private final String zone_name;
    private ArrayList<String> avail_children;
    
    public Safe_Zone(String name)
    {
        zone_name = name;
        avail_children = new ArrayList<>();
    }

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
}
