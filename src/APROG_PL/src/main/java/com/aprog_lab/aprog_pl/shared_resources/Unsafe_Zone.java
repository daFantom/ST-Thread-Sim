package com.aprog_lab.aprog_pl.shared_resources;

import com.aprog_lab.aprog_pl.GUI_Initializers.GUI1_Manager;
import com.aprog_lab.aprog_pl.threads.Demogorgon;
import com.aprog_lab.aprog_pl.threads.Child;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Emanuel Baciu
 */
public class Unsafe_Zone
{
    private final String zone_name;
    private final CopyOnWriteArrayList<Child> avail_children;
    private final CopyOnWriteArrayList<Demogorgon> avail_demos;
    private AtomicInteger captured;
    private final GUI1_Manager ifc_mng;
    private final LogManager log;
    private final AtomicBoolean labBlackout;
    
    public Unsafe_Zone(String name, GUI1_Manager p_ifc_mng, LogManager p_log)
    {
        ifc_mng = p_ifc_mng;
        zone_name = name;
        labBlackout = new AtomicBoolean(false);
        avail_children = new CopyOnWriteArrayList<>();
        avail_demos = new CopyOnWriteArrayList<>();
        if(zone_name.equals("HIVE"))
        {
            captured = new AtomicInteger(0);
        }
        log = p_log;
    }

    /* ================================ CHILD ENTERING METHOD ================================
        -   Whenever a child enters, it will check whether the program is stopped or not.
        -   If the program status is set to "running" (boolean), it will try to add itself on an COWAL (CopyOnWriteArrayList).
        -   Check if the children already is inside to avoid duplicates and refreshes it's corresponging GUI stats.
        -   If the program is stopped, it will enter on a monitor waiting queue inside of the LogManager class until resumed.
        -   Once awaken, it will try to enter again.
    */
    public void enterUZChild(Child c)
    {
        if(log.getPlaying())
        {
            synchronized(this)
            {
                if(!avail_children.contains(c))
                {
                    avail_children.add(c);
                    ifc_mng.refreshZoneStats();
                    //System.out.println("Child: "+c.getID()+" has entered unsafe zone: "+zone_name);  
                } 
            }
        }
        else
        {
            log.waitLog();
            enterUZChild(c);
        }
    }
    
    /* ================================ DEMOGORGON ENTERING METHOD ================================
        -   Whenever a demogorgon enters, it will check whether the program is stopped or not.
        -   If the program status is set to "running" (boolean), it will try to add itself on an COWAL (CopyOnWriteArrayList).
        -   Check if the demogorgon already is inside to avoid duplicates and refreshes the stats on the GUI.
        -   If the program is stopped, it will enter on a monitor waiting queue inside of the LogManager class until resumed.
        -   Once awaken, it will try to enter again.  
    */
    public void enterUZDemo(Demogorgon d)
    {
        if(log.getPlaying())
        {
            synchronized(this)
            {
                if(!avail_demos.contains(d))
                {
                    avail_demos.add(d);
                    ifc_mng.refreshZoneStats();
                    //System.out.println("Demogorgon: "+d.getID()+" has entered unsafe zone: "+zone_name);  
                }
            }
        }
        else
        {
            log.waitLog();
            enterUZDemo(d);
        }
    }
    
    /* ================================ CHILD EXITING METHOD ================================
        -   Whenever a child leaves, it will again check whether the program is stopped or not.
        -   Check if the child is inside, remove it (if race condition, which shouldn't, doesn't do anythin anyway as it doesnt throw an exception) and refreshes the stats on the GUI.
        -   If the program is stopped, it will wait.
        -   Once awaken, it will try to leave again.  
    */
    public void exitUZChild(Child c)
    {
        if(log.getPlaying())
        {
            synchronized(this)
            {
                if(avail_children.contains(c))
                {
                    avail_children.remove(c);
                    ifc_mng.refreshZoneStats();
                    //System.out.println("Child: "+c.getID()+" has exited unsafe zone: "+zone_name);
                }  
            }
        }
        else
        {
            log.waitLog();
            exitUZChild(c);
        } 
    }
    
    /* ================================ DEMOGORGON EXITING METHOD ================================
        -   Whenever a demogorgon leaves, it will again check whether the program is stopped or not.
        -   Check if the demogorgon is inside, remove it and refreshes the stats on the GUI.
        -   If the program is stopped, it will wait.
        -   Once awaken, it will try to leave again.  
    */
    public void exitUZDemo(Demogorgon d)
    {
        if(log.getPlaying())
        {
            synchronized(this)
            {
                if(avail_demos.contains(d))
                {
                    avail_demos.remove(d);
                    ifc_mng.refreshZoneStats();
                    //System.out.println("Demogorgon: "+d.getID()+" has exited unsafe zone: "+zone_name);    
                }    
            } 
        }
        else
        {
            log.waitLog();
            exitUZDemo(d);
        }
    }
    
    /* ================================ CHILDREN AVAILABILITY METHOD ================================
        -   Used by demogorgons to check whether an unsafe zone has children or not.
        -   Returns whether the COWAL avail_children is empty or not.
    */
    public synchronized boolean hasChildren()
    {
        return (!avail_children.isEmpty());
    }
    
    
    /* ================================ DEMOGORGON ATTACK METHOD ================================
        -   Demogorgons will try to attack a random child from all of the current ones inside of the COWAL (even if it leaves while doing so).
        -   Calculates a probability inside of its run() method and sends it as a parameter. If this probability is lower than or equal to 1/3, the attack is done successfully.
        -   The attack sets a special variable inside of the Child so it can enter the HIVE unsafe zone and get captured.
    */
    public synchronized boolean attackChild(double p, Demogorgon d)
    {
        boolean hasAttacked = false;          // To check whether it has been done, useful for the Demogorgon thread.
        if(log.getPlaying())                  // Checks whether the program is stopped.
        {
            if(!avail_children.isEmpty())
            {
                int selected_child = (int) (Math.random() * avail_children.size()); // Gets a random child.
                Child target = avail_children.get(selected_child);
                if(p<=0.33333333333 && !target.isAttacked())                        // Checks whether it can attack and if the child isn't attacked yet.
                {
                    hasAttacked = true;
                    target.gotAttacked();
                    log.logWrite("Demogorgon "+d.getID()+" attacked child "+target.getID()+". (captures: "+d.getLocalCaptured()+")");
                }
                else if(target.isAttacked())
                {
                    //System.out.println("CHILD: "+target.getID()+" ALREADY ATTACKED");     // DEBUG
                    hasAttacked = false;      // Due to the fast movement of the threads, it could attack the same child again, hence, it ommits it and marks as missed.
                }
                else
                {
                    //System.out.println("MISSED ATTACK ON CHILD: "+target.getID());        // DEBUG
                    hasAttacked = false;     // Otherwise it has failed the attack.
                }   
            }
        }
        else
        {
            log.waitLog();
            hasAttacked = false;
        }  
        return hasAttacked;
    }
    
    /* ================================ CHILD CAPTURING METHOD ================================
        -   Child threads use this method to get captured and wait inside of the monitor of the specific unsafe zone (HIVE).
        -   Increments the caputred AtomicInteger, refreshes the stats and logs the capture inside of the logging file.
    */
    public void capture(String id)
    {
        try
        {
            synchronized(this)
            {
                captured.incrementAndGet();
                ifc_mng.refreshCounters();
                System.out.println("Child: "+id+" has been captured");
                log.logWrite("Child: "+id+" has been captured");
                wait();
                log.logWrite("Child: "+id+" has been released");
            }
        }
        catch(InterruptedException ie)
        {
            System.out.println("IE at Unsafe_zone->capture()");
        }
    }
    
    /* ================================ CHILD SAVING METHOD ================================
        -   Called from the EventManager thread using the ElevenSavesEvent class as a middleman.
        -   Wakes up the first child inside of the monitor's waiting queue, decreemnts the captured AtomicInteger.
        -   The decrease of blood is done inside of the ElevenSavesEvent saving method.
    */
    public void save()
    {
        synchronized(this)
        {
            if(captured.get()>0)
            {
                captured.decrementAndGet();
                
                notify();
                ifc_mng.refreshCounters();
            }
        }
    }
    
    /* ================================ NAME GETTER METHOD ================================
        -   Barely used, mostly for debugging. Returns the name of the unsafe zone/
    */
    public String getName()
    {
        return zone_name;
    }
    
    /* ================================ CHILDREN AMOUNT GETTER METHOD ================================
        -   Returns the amount of children inside of the unsafe zone using the COWAL's size,
    */
    public synchronized int getAmountChildren()
    {
        return avail_children.size();
    }
    
    /* ================================ SHOW DEMOGORGONS METHOD ================================
        -   DEBUG METHOD. Used to check whether the demogorgons get removed from the unsafe zones.
    */
    public synchronized void showDemos()
    {
        System.out.println("============== "+zone_name+" ==============");
        for(int i=0;i<avail_demos.size();i++)
        {
            System.out.println(avail_demos.get(i).getID());
        }
    }
    
    /* ================================ AVAILABLE CHILDREN AND DEMOGORGONS METHODS ================================
        -   Returns the COWAL's for the current children and demogorgons inside of the unsafe zone.
        -   WARNING: although it's a return value, it can still get updated while it is being managed.
        -   The respective class hadles it to not get IndexOutOfBounds exception.
    */
    public synchronized CopyOnWriteArrayList<Child> getAvailChildren()
    {
        
        return avail_children;
    }
    
    public synchronized CopyOnWriteArrayList<Demogorgon> getAvailDemos()
    {
        return avail_demos;
    }
    /* ================================ CAPTURED CHILDREN AMOUNT METHOD ================================
        -   Used mostly for stat refresh reasons. Returns the amount of captured children inside of the HIVE.
    */
    public int getCapturedChildren()
    {
        return captured.get();
    }
    
    /* ================================ DEMOGORGON MOVING ENABLER DISABLER METHOD ================================
        -   Used whenever the Lab Blackout event occurs. Lets the demogorgons know they can't move to another unsafe zone.
        -   Makes them attack until they can move, even if no children are available.
    */
    public void disableUnsafeZonesDemos()
    {
        labBlackout.compareAndSet(false, true);
    }
    
    public void enableUnsafeZonesDemos()
    {
        labBlackout.compareAndSet(true, false);
    }
    
    /* ================================ BLOCKED STATUS GETTER METHOD ================================
        -   Used by demogorgons to check whether they can move or not.
        -   Pure checking usage.
    */
    public boolean getBlocked()
    {
        return labBlackout.get();
    }
    
}
