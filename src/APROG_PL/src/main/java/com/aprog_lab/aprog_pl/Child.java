package com.aprog_lab.aprog_pl;

import java.util.concurrent.CyclicBarrier;
/**
 *
 * @author Emanuel Baciu
 */
public class Child extends Thread {
    
    private String id;                                                                  // Used for identifying each child.  
    
    public Child(String pid)
    {
        id = pid;
    }
    
    @Override
    public void run()
    {
        System.out.println(id);
    }
}
