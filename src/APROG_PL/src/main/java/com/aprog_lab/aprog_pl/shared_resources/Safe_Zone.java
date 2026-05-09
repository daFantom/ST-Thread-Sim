package com.aprog_lab.aprog_pl.shared_resources;

import com.aprog_lab.aprog_pl.GUI_Initializers.GUI1_Manager;
import com.aprog_lab.aprog_pl.threads.Child;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Emanuel Baciu
 */
public class Safe_Zone
{
    private final String zone_name;
    private CopyOnWriteArrayList<Child> avail_children;
    private AtomicInteger bloodCount;
    private GUI1_Manager ifc_mng;
    private LogManager log;
    
    public Safe_Zone(String name, GUI1_Manager p_ifc_mng, LogManager p_log)
    {
        zone_name = name;
        avail_children = new CopyOnWriteArrayList<>();                               // Children actively wandering the zone.
        ifc_mng = p_ifc_mng;
        if(zone_name.equals("WSQK Radio"))                                          // Blood count is initialized only for the WSQK Radio safezone. Otherwise, unused.
        {
            bloodCount = new AtomicInteger(0);
        }
        log = p_log;
    }
    
    /* ============== DEBUG METHOD ==============
        -   Only used for debugging. I don't even remember why I made it, but I leave it just in case.
    */
    public String getName()
    {
        return zone_name;
    }
    
    /* ============== CHILD ENTERING METHOD ==============
        -   Checks whether the program should be running or not. Otherwise, the child will start waiting on the respective class' monitor
            and try to enter again after released.
        -   If it is running, the child checks if it's already inside of the zone (to avoid duplicates)
            and exclusively adds itself to the COWAL and refreshes the GUI stats.
    */
    public void enterSafeZone(Child c)
    {
        if(log.getPlaying())
        {
            if(!avail_children.contains(c))
            {
                synchronized(this)
                {
                    avail_children.add(c);
                    ifc_mng.refreshZoneStats();
                    //System.out.println("Child: "+c.getID()+" has entered safezone: "+zone_name);  // DEBUG
                }
            }
        }
        else
        {
            log.waitLog();
            enterSafeZone(c);
        }
    }
    
    /* ============== CHILD EXITING METHOD ==============
        -   Checks whether the program should be running or not. Otherwise, the child will start waiting on the respective class' monitor
            and try to enter again after released.
        -   If it is running, the child checks if it's already inside of the zone (to avoid not found errors?) and
            removes itself from the COWAL while refreshing the stats right after.
    */    
    public void exitSafeZone(Child c)
    {
        if(log.getPlaying())
        {
            if(avail_children.contains(c))
            {
                synchronized(this)
                {
                    avail_children.remove(c);
                    ifc_mng.refreshZoneStats();
                    //System.out.println("Child: "+c.getID()+" has exited safezone: "+zone_name);       // DEBUG
                }
            }
        }
        else
        {
            log.waitLog();
            exitSafeZone(c);
        }
    }
    
    /* ============== BLOOD INCREMENT METHOD ==============
        -   Checks whether the program should be running or not. Otherwise, the child will start waiting on the respective class' monitor
        -   Only works if the name of the zone is 'WSQK Radio'. Simply increments an AtomicInteger.
    */ 
    public void incrementBloodCount()
    {
        if(this.getName().equals("WSQK Radio") && log.getPlaying())
        {
            bloodCount.incrementAndGet();                                           // The counter gets incremented for each child that escaped.
            ifc_mng.refreshCounters();
        }
        else
        {
            log.waitLog();
            incrementBloodCount();
        }
    }
    
    /* ============== BLOOD DECREMENT METHOD ==============
        -   Checks whether the program should be running or not.
        -   Only works if the name of the zone is 'WSQK Radio'. Simply decrements an AtomicInteger.
        -   Only used whenever Eleven's Intervention event is active, for it should decrement the counter for each
            saved child (so weirddd).
    */ 
    public void decrementBloodCount()
    {
        if(this.getName().equals("WSQK Radio") && log.getPlaying())
        {
            bloodCount.decrementAndGet();                                           // The counter gets decremented for each child that was saved.
            ifc_mng.refreshCounters();
        }
        else
        {
            log.waitLog();
            incrementBloodCount();
        }
    }
    
    /* ============== CURRENT CHILDREN INSIDE GETTER ==============
        -   Returns the available children inside of the current safe zone.
        -   Used for GUI display.
    */ 
    public CopyOnWriteArrayList<Child> getAvailChildren()
    {
        return avail_children;
    }
    
    /* ============== CURRENT BLOOD COUNT GETTER ==============
        -   Returns the current blood amount inside of the WSQK Radio, no other safe zone should use this
            method as the AtomicInteger is never intialized.
        -   Used for GUI display.
    */ 
    public int getBlood()
    {
        return bloodCount.get();
    }
}
