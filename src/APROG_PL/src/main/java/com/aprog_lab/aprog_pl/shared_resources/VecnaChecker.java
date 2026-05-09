package com.aprog_lab.aprog_pl.shared_resources;

import com.aprog_lab.aprog_pl.threads.Demogorgon;
import com.aprog_lab.aprog_pl.events.StormEvent;
import com.aprog_lab.aprog_pl.events.HiveMindEvent;
import com.aprog_lab.aprog_pl.events.ElevenSavesEvent;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Emanuel Baciu
 */
public class VecnaChecker {
    private int total_demos;
    private ArrayList<Unsafe_Zone> uzs;
    private StormEvent se;
    private ElevenSavesEvent ese;
    private HiveMindEvent hme;
    private LoggManager log;
    
    public VecnaChecker(int p_total_demos, ArrayList<Unsafe_Zone> puzs, StormEvent pse, ElevenSavesEvent pese, HiveMindEvent p_hme, LoggManager p_log)
    {
        total_demos = p_total_demos;
        uzs = puzs;
        se = pse;
        ese = pese;
        hme = p_hme;
        log = p_log;
    }
    /* ================ DEMOGORGON SPAWNER ================
        -   Receives a demogorgon and it's current local captured children.
        -   If the local captured children is 8, it will spawn a new Demogorgon thread with it's respective parameters.
        -   That is why many things need to be passed inside of the constructor.
        -   Once a demgorgon is spawned from another one, sets the calling demogorgon's local child counter to 0.
        -   total_demos variable is used for the demogorgon's ID's. Initially 1.
        -   Writes the spawning of a new demogorgon on the log, AKA hawkings.txt file.
    */
    public synchronized void spawnDemo(Demogorgon demo, AtomicInteger capped_children)
    {
        int can_spawn = capped_children.get()%8;
        if(can_spawn==0)
        {
            log.logWrite("A new demogorgon has been Spawned!");
            new Demogorgon("D"+String.format("%04d",total_demos), uzs, this, se, ese, hme, log).start();
            total_demos++;
            demo.emptyChildren();                   

        }
    }
}
