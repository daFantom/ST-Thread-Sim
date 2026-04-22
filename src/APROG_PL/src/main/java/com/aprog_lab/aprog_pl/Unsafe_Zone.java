package com.aprog_lab.aprog_pl;

import java.util.ArrayList;

/**
 *
 * @author Emanuel Baciu
 */
public class Unsafe_Zone
{
    String zone_name;
    ArrayList<String> avail_children, avail_demos;
    
    public Unsafe_Zone(String name)
    {
        zone_name = name;
        avail_children = new ArrayList<>();
        avail_demos = new ArrayList<>();
    }
}
