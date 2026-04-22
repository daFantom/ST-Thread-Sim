package com.aprog_lab.aprog_pl;

import java.util.concurrent.CyclicBarrier;
/**
 *
 * @author Emanuel Baciu
 */

public class Demogorgon extends Thread
{
    private String id;                                                                  // If "D0000", Alpha Demogorgon (First thread), otherwise, just a Demogorgon
    private int child_counter;                                                         // Used for keeping track of each captured child from a single Demogorgon
    
    public Demogorgon(String pid, int pcounter)
    {
        id = pid;
        child_counter = pcounter;
    }
    
    @Override
    public void run()
    {
        System.out.println(id);
    }
}
