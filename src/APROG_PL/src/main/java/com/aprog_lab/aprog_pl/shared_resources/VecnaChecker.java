package com.aprog_lab.aprog_pl.shared_resources;

import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
import com.aprog_lab.aprog_pl.threads.Demogorgon;
import com.aprog_lab.aprog_pl.events.StormEvent;
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
    
    public VecnaChecker(int p_total_demos, ArrayList<Unsafe_Zone> puzs, StormEvent pse, ElevenSavesEvent pese)
    {
        total_demos = p_total_demos;
        uzs = puzs;
        se=pse;
        ese = pese;
    }
    /*
    
    */
    public synchronized void spawnDemo(Demogorgon demo, int capped_children)
    {
        int can_spawn = capped_children%8;
        if(can_spawn==0)
        {
            System.out.println("A new demogorgon has been Spawned!");
            new Demogorgon("D"+String.format("%04d",total_demos), uzs, this, se, ese).start();
            total_demos++;
            demo.emptyChildren();

        }
        else
        {
            System.out.println("VECNA: Demogorgon: "+demo.getID()+", not enough children, only provided "+capped_children);
        }
    }
}
