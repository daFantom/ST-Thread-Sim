package com.aprog_lab.aprog_pl;

import java.util.ArrayList;

/**
 *
 * @author Emanuel Baciu
 */
public class VecnaChecker {
    private int total_demos;
    private ArrayList<Unsafe_Zone> uzs;
    
    public VecnaChecker(int p_total_demos, ArrayList<Unsafe_Zone> puzs)
    {
        total_demos = p_total_demos;
        uzs = puzs;
    }
    /*
    
    */
    public void spawnDemo(int amount_children)
    {
        if((amount_children%8)==0)
        {
            System.out.println("A new demogorgon has been Spawned!");
            new Demogorgon("D"+String.format("%04d",total_demos), 0, uzs, this).start();
        }
    }
}
