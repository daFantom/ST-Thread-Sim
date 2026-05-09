package com.aprog_lab.aprog_pl.threads;

import com.aprog_lab.aprog_pl.shared_resources.Portal;
import com.aprog_lab.aprog_pl.shared_resources.Safe_Zone;
import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
import com.aprog_lab.aprog_pl.events.StormEvent;
import com.aprog_lab.aprog_pl.shared_resources.LoggManager;
import java.util.ArrayList;

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
    private String status;                                                             // To check whether a child is entering or exiting a portal.
    private AtomicBoolean attacked;
    private StormEvent storm;
    private LoggManager log;
    
    public Child(String pid, ArrayList<Safe_Zone> psz, ArrayList<Unsafe_Zone> puz,ArrayList<Portal> pportals, StormEvent pstorm, LoggManager p_log)
    {
        id = pid;
        sz = psz;
        uz = puz;
        portals = pportals;
        status = "Entering";
        attacked = new AtomicBoolean(false);
        storm = pstorm;
        log = p_log;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                if(log.getPlaying())
                {
                    sz.get(0).enterSafeZone(this);                                      // sz[0] = Hawkin's Main street, they will all enter here upon creation.
                    Thread.sleep((int)( (Math.random()*2000)+3000) );                   // Waits for 3 to 5 seconds before entering Bayer's Basement.
                    sz.get(0).exitSafeZone(this);
                    sz.get(1).enterSafeZone(this);                                      // sz[1]=Bayer's Basement. They enter.
                    
// ===================================== Portal selection mechanism =============================
                    Thread.sleep((int)( (Math.random()*1000)+1000) );                   // { Choosing portal...
                    int selected_portal = (int) (Math.random()*4);                      // { Selected portal random 0-3
                    sz.get(1).exitSafeZone(this);                                       // Leaves Byer's Basement
                    portals.get(selected_portal).enterPortalQueue(this, status);        // Enters the selected portal. Also corresponds to unsafe zone.
                    status = "Exiting";                                                 // Change status so they can get inserted into exitQueue->Portal class.
                    Thread.sleep(1000);                                                 // Going through portal...
                    uz.get(selected_portal).enterUZChild(this);                         // Arrives at unsafe zone after getting through the portal.
// ===================================== Attacked mechanism =====================================
                    if(attacked.get())                                                  // If not attacked, they move normally. Otherwise, move to HIVE and wait until saved.
                    {
                        goToHive(this, selected_portal);
                        removeAttacked();
                    }
                    else
                    {
                        //System.out.println("Child: "+id+" has passed portal "+selected_portal);       // DEBUG
                        //System.out.println("Child: "+id+". Doing stuff...");                          // DEBUG
                        Thread.sleep((int)((Math.random()*2000)+3000));                 // Collecting vecna blood...
                        if(attacked.get())                                              // Check if they were attacked while collecting blood. If so, same thing as before.
                        {
                            goToHive(this, selected_portal);
                            removeAttacked();
                        }
                        else
                        {
                            uz.get(selected_portal).exitUZChild(this);
                            portals.get(selected_portal).enterPortalQueue(this, status);
                        }
// ===================================== Exiting and deploying blood mechanism ===================
                        sz.get(2).enterSafeZone(this);                                          // { Returns to WSQK Radio
                        sz.get(2).incrementBloodCount();                                        // { to deposit the collected blood
                        if(storm.isStorm())
                        {
                            //System.out.println("Storm active: doubled blood gathering.");
                            sz.get(2).incrementBloodCount();                                    // If there is an ongoing storm, increments once more as to "double" the amount of collected blood.
                        }
                        status = "Entering";
                        Thread.sleep((int) ((Math.random()*2000)+2000));                        // Waiting to rest.
                        sz.get(2).exitSafeZone(this);
                    }
                }
                else
                {
                    log.waitLog();                                                              // If the porgram was stopped, waits inside of the class'
                }
            }
            catch(InterruptedException ie)
            {
                System.out.println("IE at Child->run()");
            }   
        }
    }
    
    /* ==================== CHILD STATUS GETTER ====================
        -   DEBUGGING PURPOSES
    */
    public String getStatus()
    {
        return status;
    }
    
    /* ==================== CHILD ATTACKED GETTER ====================
        -   Returns whether a child was attacked or not, usually done whenever a demogorgon wants to attack a child.
    */
    public boolean isAttacked()
    {
        return attacked.get();
    }
    
    /* ==================== CHILD ID GETTER ====================
        -   Returns the ID of a child. Usually done for GUI stat refreshing.
    */
    public String getID()
    {
        return id;
    }
    
    /* ==================== CHILD ATTACKED SETTERS ====================
        -   Sets the attacked AtomicBoolean to true if false.
        -   Otherwise, sets it to false if true [removedAttacked()]
    */
    public void gotAttacked()
    {
        attacked.compareAndSet(false, true);
    }
    
    public void removeAttacked()
    {
        attacked.compareAndSet(true, false);
    }
    
    /* ==================== CHILD MIND CONTROL METHOD ====================
        -   Only called inside of the child itself. Checks on run() method if attacked and calls this method to go to the HIVE unsafe zone.
        -   Essentially makes the child leave the current zone and makes it enter the unsafe zone directly.
        -   Then, increments the captured amount with hive.capture(id). The id is used for printing reasons, AKA Debug.
    */
    public void goToHive(Child c, int portal)
    {
        Unsafe_Zone hive = uz.get(4);
        try
        {
            uz.get(portal).exitUZChild(c);                        
            hive.enterUZChild(c);
            Thread.sleep((int)((Math.random()*500)+500));                           // Getting carried to the HIVE (simulation)
            status = "Entering";
            hive.capture(id);
        }
        catch(InterruptedException ie)
        {
            System.out.println("IE at Child -> goToHive()");
        }
    }
}
