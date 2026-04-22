package com.aprog_lab.aprog_pl;

import java.util.ArrayList;

/**
 *
 * @author Emanuel Baciu
 */
public class Safe_Zone
{
    String zone_name;
    ArrayList<String> avail_children;
    
    public Safe_Zone(String name)
    {
        zone_name = name;
        avail_children = new ArrayList<String>();
    }
}
