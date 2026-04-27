package com.aprog_lab.aprog_pl;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
/**
 *
 * @author Emanuel Baciu
 */

public class Demogorgon extends Thread
{
    private String id;                                                                  // If "D0000", Alpha Demogorgon (First thread), otherwise, just a Demogorgon.
    private int child_counter;                                                         // Used for keeping track of each captured child from a single Demogorgon.
    private ArrayList<Unsafe_Zone> uz;                                                // uz[0]=Forest, uz[1]=Lab, uz[2]=Mall, uz[3]=Sewer, uz[4]=HIVE (Demo exclusive)
    private VecnaChecker vc;
    
    public Demogorgon(String pid, int pcounter, ArrayList<Unsafe_Zone> puz, VecnaChecker pvc)
    {
        id = pid;
        child_counter = pcounter;
        uz = puz;
        vc = pvc;
    }
    
    @Override
    public void run()
    {
        int zone = (int)(Math.random()*3);
        Unsafe_Zone actual_uz = uz.get(zone);
        actual_uz.enterUnsafeSafeZoneDemo(this);
        // Attacking start
        if(actual_uz.hasChildren())
        {
            if(actual_uz.attackChild())
            {
                child_counter++;
            }
            // Failed otherwise
        }
        // Attacking end
        vc.spawnDemo(child_counter);
        
    }
    
    public String getID()
    {
        return id;
    }
}
