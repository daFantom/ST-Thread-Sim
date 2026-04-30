package com.aprog_lab.aprog_pl.threads;

import com.aprog_lab.aprog_pl.shared_resources.Portal;
import com.aprog_lab.aprog_pl.shared_resources.Safe_Zone;
import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
import com.aprog_lab.aprog_pl.events.StormEvent;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
/**
 *
 * @author Emanuel Baciu
 */
public class Child extends Thread {
    
    private String id;                                                                  // Used for identifying each child.  
    private ArrayList<Unsafe_Zone> uz;                                                 // uz[0]=Forest, uz[1]=Lab, uz[2]=Mall, uz[3]=Sewer, uz[4]=HIVE;
    private ArrayList<Safe_Zone> sz;                                                   // sz[0]=Main Street, sz[1]=Basement, sz[2]=WSQK Radio.
    private ArrayList<Portal> portals;
    private String status;
    private AtomicBoolean attacked;
    private StormEvent storm;
    
    public Child(String pid, ArrayList<Safe_Zone> psz, ArrayList<Unsafe_Zone> puz,ArrayList<Portal> pportals, StormEvent pstorm)
    {
        id = pid;
        sz = psz;
        uz = puz;
        portals = pportals;
        status = "Entering";
        attacked = new AtomicBoolean(false);
        storm = pstorm;
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
                sz.get(0).exitSafeZone(id);
                sz.get(1).enterSafeZone(id);                                         // sz[1]=Bayer's Basement. They enter.
                Thread.sleep((int)( (Math.random()*1000)+1000) );                   // Choosing portal...
                // Portal selection mechanism
                int selected_portal = (int) (Math.random()*4);
                sz.get(1).exitSafeZone(id);
                portals.get(selected_portal).enterPortalQueue(id, status);
                status = "Exiting";                                                     // Change status so they can get inserted into exitQueue->Portal class.
                Thread.sleep(1000);                                                    // Going through portal...
                uz.get(selected_portal).enterUZChild(this);                           // Goes to the unsafe zone DEBUG
                
                if(attacked.get())                                                      // If not attacked (check attacked), they can use portal. Otherwise, waiting forever until saved.
                {
                    
                    uz.get(selected_portal).exitUZChild(this);
                    status = "Entering";
                    uz.get(4).enterUZChild(this);
                    uz.get(4).capture(id);
                    // would use a semaphore or sumn.
                }
                else
                {
                    //System.out.println("Child: "+id+" has passed portal "+selected_portal);       // DEBUG
                    //System.out.println("Child: "+id+". Doing stuff...");                           // DEBUG
                    Thread.sleep((int)((Math.random()*2000)+3000));                       // Collecting vecna blood...
                    if(attacked.get())                                                      // If not attacked (check attacked), they can use portal. Otherwise, waiting forever until saved.
                    {
                        uz.get(selected_portal).exitUZChild(this);
                        status = "Entering";
                        uz.get(4).enterUZChild(this);
                        Thread.sleep((int)((Math.random()*500)+500));
                        uz.get(4).capture(id);
                        // would use a semaphore or sumn.
                    }
                    uz.get(selected_portal).exitUZChild(this);
                    portals.get(selected_portal).enterPortalQueue(id, status);
                    status = "Entering";
                    sz.get(0).enterSafeZone(id);                                            // Return to Hawking Street.
                    sz.get(0).exitSafeZone(id);
                    sz.get(2).enterSafeZone(id);                                            // Go to WSKQ Radio.
                    sz.get(2).incrementBloodCount();                                        // Deposit blood.
                    if(storm.isStorm())
                    {
                        System.out.println("Storm active: doubled blood gathering.");
                        sz.get(2).incrementBloodCount();                                    // If there is an ongoing storm, increments once more as to "double" the amount of collected blood.
                    }
                    Thread.sleep((int) ((Math.random()*2000)+2000));
                    sz.get(2).exitSafeZone(id);
                }

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
    
    public boolean isAttacked()
    {
        return attacked.get();
    }
    
    public String getID()
    {
        return id;
    }
    
    public void gotAttacked()
    {
        attacked.compareAndSet(false, true);
    }
}
