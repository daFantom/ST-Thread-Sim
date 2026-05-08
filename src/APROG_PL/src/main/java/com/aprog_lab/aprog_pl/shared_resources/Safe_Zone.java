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
    private logManager log;
    
    public Safe_Zone(String name, GUI1_Manager p_ifc_mng, logManager p_log)
    {
        zone_name = name;
        avail_children = new CopyOnWriteArrayList<>();                                         // Children actively wandering the zone.
        ifc_mng = p_ifc_mng;
        if(zone_name.equals("WSQK Radio"))                                          // Blood count is initialized only for the WSQK Radio safezone. Otherwise, unused.
        {
            bloodCount = new AtomicInteger(0);
        }
        log = p_log;
    }
    
    /* Test method.
    
    */
    public String getName()
    {
        return zone_name;
    }
    
    /* Unfinished method to add a child to a specific zone.
    
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
                    //System.out.println("Child: "+c.getID()+" has entered safezone: "+zone_name);
                }
            }
        }
        else
        {
            log.waitLog();
            enterSafeZone(c);
        }
    }
    
    /* Unfinished method to remove a child from a specific zone.
    
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
                    //System.out.println("Child: "+c.getID()+" has exited safezone: "+zone_name);
                }
            }
        }
        else
        {
            log.waitLog();
            exitSafeZone(c);
        }
    }
    
    /* Unfinished method for incrementing the blood counter
    
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
    
    /* Unfinished method for incrementing the blood counter
    
    */
    public void decrementBloodCount()
    {
        if(this.getName().equals("WSQK Radio") && log.getPlaying())
        {
            bloodCount.decrementAndGet();                                           // The counter gets incremented for each child that escaped.
            ifc_mng.refreshCounters();
        }
        else
        {
            log.waitLog();
            incrementBloodCount();
        }
    }
    
    /*
    
    */
    public CopyOnWriteArrayList<Child> getAvailChildren()
    {
        return avail_children;
    }
    
    /*
    
    */
    public int getBlood()
    {
        return bloodCount.get();
    }
}
