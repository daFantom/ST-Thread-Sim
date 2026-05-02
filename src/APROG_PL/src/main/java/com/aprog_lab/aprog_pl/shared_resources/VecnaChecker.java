package com.aprog_lab.aprog_pl.shared_resources;

import com.aprog_lab.aprog_pl.threads.Demogorgon;
import com.aprog_lab.aprog_pl.events.StormEvent;
import com.aprog_lab.aprog_pl.events.HiveMindEvent;
import com.aprog_lab.aprog_pl.events.ElevenSavesEvent;
import java.util.ArrayList;

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
    private Logger log;
    
    public VecnaChecker(int p_total_demos, ArrayList<Unsafe_Zone> puzs, StormEvent pse, ElevenSavesEvent pese, HiveMindEvent p_hme, Logger p_log)
    {
        total_demos = p_total_demos;
        uzs = puzs;
        se = pse;
        ese = pese;
        hme = p_hme;
        log = p_log;
    }
    /*
    
    */
    public synchronized void spawnDemo(Demogorgon demo, int capped_children)
    {
        int can_spawn = capped_children%8;
        if(can_spawn==0)
        {
            log.logWrite("A new demogorgon has been Spawned!");
            new Demogorgon("D"+String.format("%04d",total_demos), uzs, this, se, ese, hme, log).start();
            total_demos++;
            demo.emptyChildren();

        }
        else
        {
            //System.out.println("VECNA: Demogorgon: "+demo.getID()+", not enough children, only provided "+capped_children);
        }
    }
}
