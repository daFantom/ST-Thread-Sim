package com.aprog_lab.aprog_pl;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
/**
 *
 * @author Emanuel Baciu
 */
public class Child extends Thread {
    
    private String id;                                                                  // Used for identifying each child.  
    private ArrayList<Unsafe_Zone> uz;                                                 // uz[0]=Forest, uz[1]=Lab, uz[2]=Mall, uz[3]=Sewer. We don't use uz[4].
    private ArrayList<Safe_Zone> sz;                                                   // sz[0]=Main Street, sz[1]=Basement, sz[2]=WSQK Radio.
    
    public Child(String pid, ArrayList<Safe_Zone> psz, ArrayList<Unsafe_Zone> puz)
    {
        id = pid;
        sz = psz;
        uz = puz;
    }
    
    @Override
    public void run()
    {
        sz.get(0).enterSafeZone(id);                                                      // sz[0] = Hawkin's Main street, they will all enter here.
        try
        {
            Thread.sleep((int)( (Math.random()*2)+3) );                                 // Waits for 3 to 5 seconds before trying to enter another zone.
            sz.get(1).enterSafeZone(id);
                                            
        }
        catch(InterruptedException ie)
        {
            System.out.println("IE at Child->run()");
        }
    }
}
