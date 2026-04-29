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
    private int child_counter, total_counter;                                                         // Used for keeping track of each captured child from a single Demogorgon.
    private ArrayList<Unsafe_Zone> uz;                                                // uz[0]=Forest, uz[1]=Lab, uz[2]=Mall, uz[3]=Sewer, uz[4]=HIVE
    private VecnaChecker vc;
    
    public Demogorgon(String pid, ArrayList<Unsafe_Zone> puz, VecnaChecker pvc)
    {
        id = pid;
        child_counter = 0;
        uz = puz;
        vc = pvc;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                int zone = (int)(Math.random()*3);
                Unsafe_Zone actual_uz = uz.get(zone);
                actual_uz.enterUnsafeSafeZoneDemo(this);
                // Attacking start
                if(actual_uz.hasChildren())
                {
                    //System.out.println("Children detected");                      // DEBUG
                    double prob = Math.random();
                    Thread.sleep((int)((Math.random()*1000)+500));                  // Attacking
                    if(actual_uz.attackChild(prob))
                    {
                        //System.out.println("Child attacked");                     // DEBUG
                        Thread.sleep((int)((Math.random()*500)+500));
                        child_counter++;
                        total_counter++;
                        vc.spawnDemo(this, child_counter);
                    }
                    // Failed otherwise
                    actual_uz.exitUnsafeSafeZoneDemo(this);
                }
                else
                {
                    Thread.sleep((int)( (Math.random()*1000) + 4000 ));             // If there are no children, wanders around and eventually to another UZ.
                    actual_uz.exitUnsafeSafeZoneDemo(this);
                }

            }
            catch(InterruptedException ie)
            {
                
            }
        }
        
    }
    
    public String getID()
    {
        return id;
    }
    
    public void emptyChildren()
    {
        child_counter = 0;
    }
}
