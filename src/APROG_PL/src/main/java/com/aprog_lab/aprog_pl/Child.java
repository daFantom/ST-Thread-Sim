package com.aprog_lab.aprog_pl;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
/**
 *
 * @author Emanuel Baciu
 */
public class Child extends Thread {
    
    private String id;                                                                  // Used for identifying each child.  
    private ArrayList<Unsafe_Zone> uz;                                                 // uz[0]=Forest, uz[1]=Lab, uz[2]=Mall, uz[3]=Sewer. We don't use uz[4].
    private ArrayList<Safe_Zone> sz;                                                   // sz[0]=Main Street, sz[1]=Basement, sz[2]=WSQK Radio.
    private ArrayList<Portal> portals;
    private String status;
    private AtomicBoolean attacked;
    
    public Child(String pid, ArrayList<Safe_Zone> psz, ArrayList<Unsafe_Zone> puz,ArrayList<Portal> pportals)
    {
        id = pid;
        sz = psz;
        uz = puz;
        portals = pportals;
        status = "Entering";
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            sz.get(0).enterSafeZone(id);                                              // sz[0] = Hawkin's Main street, they will all enter here upon creation.
            try
            {
                Thread.sleep((int)( (Math.random()*2000)+3000) );                   // Waits for 3 to 5 seconds before entering Bayer's Basement.
                sz.get(1).enterSafeZone(id);                                         // sz[1]=Bayer's Basement. They enter.
                Thread.sleep((int)( (Math.random()*1000)+1000) );                   // Choosing portal...
                // Portal selection mechanism
                int selected_portal = 0;
                portals.get(selected_portal).enterPortalQueue(id, status);
                status = "Exiting";                                                     // Change status so they can get inserted into exitQueue->Portal class.
                Thread.sleep(1000);                                                    // Going through portal...
                
                // ====== DEBUG ======
//                System.out.println("Child: "+id+" has passed portal "+selected_portal);
//                System.out.println("Child: "+id+". Doing stuff...");
//                Thread.sleep(5000);
                // Upside down stuff...
                portals.get(selected_portal).enterPortalQueue(id, status);
                status = "Entering";
                sz.get(0).enterSafeZone(id);
                sz.get(2).enterSafeZone(id);                                          // If they can leave (check var attacked), they'll go right to WSQK Radio. Otherwise, waiting forever until saved.
                Thread.sleep((int) ((Math.random()*2000)+2000));

            }
            catch(InterruptedException ie)
            {
                System.out.println("IE at Child->run()");
            }   
        }
    }
    
    public String getStatus()
    {
        return status;
    }
}
