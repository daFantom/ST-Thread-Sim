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
    private ArrayList<Unsafe_Zone> uz;
    
    public Demogorgon(String pid, int pcounter, ArrayList<Unsafe_Zone> puz)
    {
        id = pid;
        child_counter = pcounter;
        uz = puz;
    }
    
    @Override
    public void run()
    {
        int zone = (int)(Math.random()*3);
        uz.get(zone).enterUnsafeSafeZone(id);
    }
}
