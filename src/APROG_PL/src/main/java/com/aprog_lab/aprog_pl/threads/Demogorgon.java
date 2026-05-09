package com.aprog_lab.aprog_pl.threads;

import com.aprog_lab.aprog_pl.events.ElevenSavesEvent;
import com.aprog_lab.aprog_pl.events.StormEvent;
import com.aprog_lab.aprog_pl.events.HiveMindEvent;
import com.aprog_lab.aprog_pl.shared_resources.LogManager;
import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
import com.aprog_lab.aprog_pl.shared_resources.VecnaChecker;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Emanuel Baciu
 */

public class Demogorgon extends Thread
{
    private String id;                                                                // If "D0000", Alpha Demogorgon (First thread), otherwise, just a Demogorgon.
    private AtomicInteger child_counter, total_counter;                               // Used for keeping track of each captured child from a single Demogorgon.
    private int attack_time;                                                          // Attack interval, changed if UPSIDE DOWN STORM event is active.
    private ArrayList<Unsafe_Zone> uz;                                                // uz[0]=Forest, uz[1]=Lab, uz[2]=Mall, uz[3]=Sewer, uz[4]=HIVE
    private VecnaChecker vc;                                                         // {
    private StormEvent storm;                                                        // { Events, mostly checks their status.
    private ElevenSavesEvent elevenEvent;                                            // {
    private HiveMindEvent hme;                                                       // {
    private Unsafe_Zone actual_uz;
    private int zone;
    private LogManager log;
    
    public Demogorgon(String pid, ArrayList<Unsafe_Zone> puz, VecnaChecker pvc, StormEvent pstorm, ElevenSavesEvent pese, HiveMindEvent phme, LogManager p_log)
    {
        id = pid;
        child_counter = new AtomicInteger(0);
        total_counter = new AtomicInteger(0);
        uz = puz;
        vc = pvc;
        storm = pstorm;
        elevenEvent = pese;
        hme = phme;
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
                    if(!elevenEvent.getStatus())
                    {
                        if(hme.getStatus())
                        {
                            actual_uz = hme.getMostChildrenZone();                   // If the HiveMindEvent is ongoing, uses a special method inside of the class to get the zone.
                            //System.out.println("HIVE MIND EVENT ACTIVE, MOVING TO: "+actual_uz.getName());
                        }
                        else
                        {
                            zone = (int)(Math.random()*4);                           // Otherwise, chooses an unsafe zone randomly and gets is from the ArrayList.
                            actual_uz = uz.get(zone);
                        }
                        actual_uz.enterUZDemo(this);                                 // Enters the zone (appends itself into the unsafe zone's COWAL).
                        // Attacking start
                        if(actual_uz.getBlocked())
                        {
                            while(actual_uz.getBlocked())
                            {
                                if(actual_uz.hasChildren() && !elevenEvent.getStatus())
                                {
                                    if(performAttackChild(actual_uz))                       // If the Lab Blackout event is ongoing, keeps attacking until the event finishes.
                                    {
                                        Thread.sleep((int)((Math.random()*500)+500));
                                        child_counter.getAndIncrement();                    // If successful, increments local counter (for spawning new demogorgons)
                                        total_counter.getAndIncrement();                    // and total counter (ranking purposes).
                                        vc.spawnDemo(this, child_counter);                  // Tries to spawn a new demogorgon.
                                        log.updateRanking(this);                            // Update the ranking of the demogorgons.
                                    }
                                }
                            }
                            actual_uz.exitUZDemo(this);                                     // Removes itself from the unsafe zone's COWAL. Simulates the exit of an unsafe zone.
                        }
                        else
                        {
                            if(actual_uz.hasChildren() && !elevenEvent.getStatus())         // If the unsafe zones aren't blocked, tries to atack, same thing as above.
                            {
                                if(performAttackChild(actual_uz))
                                {
                                    Thread.sleep((int)((Math.random()*500)+500));
                                    child_counter.getAndIncrement();
                                    total_counter.getAndIncrement();
                                    vc.spawnDemo(this, child_counter);
                                    log.updateRanking(this);                                
                                }
                                actual_uz.exitUZDemo(this);
                            }
                            else
                            {
                                Thread.sleep((int)( (Math.random()*1000) + 4000 ));             // If there are no children, wanders around and eventually to enter another UZ.
                                actual_uz.exitUZDemo(this);                                     // Exits current the unsafe zone
                            }
                        }
                    }
                    else
                    {
                        elevenEvent.disableDemo();                                              // If the Eleven's Intervention event is happening, gets blocked inside of the class' monitor.
                    }   
                }
                else
                {
                    log.waitLog();                                                              // If the program is stopped, waits inside of the class' monitor.
                }
            }
            catch(InterruptedException ie)
            {
                System.out.println("IE at Demogorgon->run()");
            } 
        }
        
    }
    
    /* ==================== ID GETTER METHOD ====================
        -   Mostly debugging and GUI refreshing purposes.
        -   Returns the ID of the demogorgon.
    */
    public String getID()
    {
        return id;
    }
    
    /* ==================== CHILDREN CAPUTRED RESETTING METHOD ====================
        -   After capturing 8 children, spawns another demogorgon using the VecnaChecker class and gets it's local captures set to 0.
    */
    public void emptyChildren()
    {
        child_counter.set(0);
    }
    
    /* ==================== LOCAL CAPTURED CHILDREN METHOD ====================
        -   Used by the VecnaChecker class to get the local captured children of a demogorgon.
    */
    public int getLocalCaptured()
    {
        return child_counter.get();
    }
    
    /* ==================== TOTAL CHILDREN CAPTURED METHOD ====================
        -   GUI refreshing purposes.
        -   Returns the total amount of children captured from a demogorgon. 
    */
    public int getTotalCaptured()
    {
        return total_counter.get();
    }
    
    /* ==================== DEMOGORGON ATTACKING METHOD ====================
        -   Sequence of steps whenever a demogorgon tries to attack. Moved from run() method for a cleaner code.
        -   Returns a boolean if done correctly, otherwise missed.
        -   Essentially creates the probability to check if it attacks or misses.
            Afterwards it checks whether the storm event is happening to halve its attack rate or not.
            Tries to attack the child calling the unsafe zone's method and returns the boolean depending if it has attacked or not.
    */
    public boolean performAttackChild(Unsafe_Zone actual_uz)
    {
        boolean done = false;
        try
        {
                //System.out.println("Children detected");                      // DEBUG
                double prob = Math.random();
                if(storm.isStorm() && !elevenEvent.getStatus())
                {
                    //System.out.println("Storm active: halving attack time ratio");    // DEBUG
                    attack_time = (int)(((Math.random()*1000)+500)/2);
                }
                else
                {
                    attack_time = (int)((Math.random()*1000)+500);
                }
                Thread.sleep(attack_time);                  // Attacking                
                if(actual_uz.attackChild(prob, this) && !elevenEvent.getStatus())
                {
                    //System.out.println("Child attacked");                     // DEBUG
                    done=true;
                }
                else
                {
                    done = false;
                }
        }
        catch(InterruptedException ie)
        {
            System.out.println("IE at Demogorgon->performAttackChild()");
        }
        return done;
    }
}
