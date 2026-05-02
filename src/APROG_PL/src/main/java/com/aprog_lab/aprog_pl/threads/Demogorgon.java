package com.aprog_lab.aprog_pl.threads;

import com.aprog_lab.aprog_pl.events.ElevenSavesEvent;
import com.aprog_lab.aprog_pl.events.StormEvent;
import com.aprog_lab.aprog_pl.events.HiveMindEvent;
import com.aprog_lab.aprog_pl.shared_resources.Logger;
import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
import com.aprog_lab.aprog_pl.shared_resources.VecnaChecker;
import java.util.ArrayList;

/**
 *
 * @author Emanuel Baciu
 */

public class Demogorgon extends Thread
{
    private String id;                                                                  // If "D0000", Alpha Demogorgon (First thread), otherwise, just a Demogorgon.
    private int child_counter, total_counter;                                         // Used for keeping track of each captured child from a single Demogorgon.
    private int attack_time;                                                            // Attack interval, changed if UPSIDE DOWN STORM event is active.
    private ArrayList<Unsafe_Zone> uz;                                                // uz[0]=Forest, uz[1]=Lab, uz[2]=Mall, uz[3]=Sewer, uz[4]=HIVE
    private VecnaChecker vc;
    private StormEvent storm;
    private ElevenSavesEvent elevenEvent;
    private HiveMindEvent hme;
    private Unsafe_Zone actual_uz;
    private int zone;
    private Logger log;
    
    public Demogorgon(String pid, ArrayList<Unsafe_Zone> puz, VecnaChecker pvc, StormEvent pstorm, ElevenSavesEvent pese, HiveMindEvent phme, Logger p_log)
    {
        id = pid;
        child_counter = 0;
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
                            actual_uz = hme.getMostChildrenZone();
                            //System.out.println("HIVE MIND EVENT ACTIVE, MOVING TO: "+actual_uz.getName());
                        }
                        else
                        {
                            zone = (int)(Math.random()*4);
                            actual_uz = uz.get(zone);
                        }
                        actual_uz.enterUZDemo(this);
                        // Attacking start
                        if(actual_uz.hasChildren() && !elevenEvent.getStatus())
                        {
                            //System.out.println("Children detected");                      // DEBUG
                            double prob = Math.random();
                            if(storm.isStorm()&&!elevenEvent.getStatus())
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
                                Thread.sleep((int)((Math.random()*500)+500));
                                child_counter++;
                                total_counter++;
                                vc.spawnDemo(this, child_counter);
                            }

                            // Failed otherwise
                            actual_uz.exitUZDemo(this);
                        }
                        else
                        {
                            Thread.sleep((int)( (Math.random()*1000) + 4000 ));             // If there are no children, wanders around and eventually to another UZ.
                            actual_uz.exitUZDemo(this);
                        }
                    }
                    else
                    {
                        elevenEvent.disableDemo();
                    }   
                }
                else
                {
                    log.waitLog();
                }
            }
            catch(InterruptedException ie)
            {
                System.out.println("IE at Demogorgon->run()");
            } 
        }
        
    }
    
    /*
    
    */
    public String getID()
    {
        return id;
    }
    
    /*
    
    */
    public void emptyChildren()
    {
        child_counter = 0;
    }
    
    /*
    
    */
    public int getLocalCaptured()
    {
        return child_counter;
    }
}
