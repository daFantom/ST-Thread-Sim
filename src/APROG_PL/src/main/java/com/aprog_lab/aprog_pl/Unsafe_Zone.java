package com.aprog_lab.aprog_pl;

import java.util.ArrayList;

/**
 *
 * @author Emanuel Baciu
 */
public class Unsafe_Zone
{
    private final String zone_name;
    private ArrayList<String> avail_children, avail_demos;
    
    public Unsafe_Zone(String name)
    {
        zone_name = name;
        avail_children = new ArrayList<>();
        avail_demos = new ArrayList<>();
    }
    
    public String getName()
    {
        return zone_name;                                                               // Testing method.
    }
    
    // Unfinished method to add a child to a specific zone.
    public void enterUnsafeSafeZone(String id)
    {
        if(id.contains("C"))
        {
            avail_children.add(id);
            System.out.println("Child: "+id+" has entered unsafe zone: "+zone_name);   
        }
        else
        {
            avail_demos.add(id);
            System.out.println("Demogorgon: "+id+" has entered unsafe zone: "+zone_name);
        }
    }
    
    // Unfinished method to remove a child from a specific zone.
    public void exitUnsafeSafeZone(String id)
    {
        if(id.contains("C"))
        {
            avail_children.remove(id);
            System.out.println("Child: "+id+" has exited unsafe zone: "+zone_name);   
        }
        else
        {
            avail_demos.remove(id);
            System.out.println("Demogorgon: "+id+" has exited unsafe zone: "+zone_name);
        }
    }
}
