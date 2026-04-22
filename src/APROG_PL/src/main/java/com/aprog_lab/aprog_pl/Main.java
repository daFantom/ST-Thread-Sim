package com.aprog_lab.aprog_pl;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
/**
 *
 * @author Emanuel Baciu
 */
public class Main {

    public static void main(String[] args) {
        ArrayList<Unsafe_Zone> uz = new ArrayList<>();                              // Children and Demogorgons share this ArrayList.
        ArrayList<Safe_Zone> sz = new ArrayList<>();                                // ONLY children are allowed to use this ArrayList.
        
        Unsafe_Zone Forest = new Unsafe_Zone("Forest");
        Unsafe_Zone Lab = new Unsafe_Zone("Laboratory");
        Unsafe_Zone Mall = new Unsafe_Zone("Shopping Mall");
        Unsafe_Zone Sewer = new Unsafe_Zone("Sewer");
        Unsafe_Zone Hive = new Unsafe_Zone("HIVE");
        
        Safe_Zone ms = new Safe_Zone("Hawkin's Main Street");
        Safe_Zone bb = new Safe_Zone("Bayer's Basement");
        Safe_Zone radio = new Safe_Zone("WSQK Radio");
        
        uz.add(Forest); uz.add(Lab); uz.add(Mall); uz.add(Sewer); uz.add(Hive);
        sz.add(ms); sz.add(bb); sz.add(radio);
        
        // Initial threads (Alpha Demog and Children) creation.
        try
        {
            int idn = 0;                                                               // Used for Children ID (Mostly)
            new Demogorgon("D"+String.format("%04d",idn), 0, uz).start();          // Formatted ID for the Alpha Demogorgon (D0000)
            for(int i=0; i<1; i++)
            {
                Thread.sleep((int)(Math.random()*1.5+0.5));                         // SHOULD wait between 0.5 and 2 seconds.
                String child_id = "C"+String.format("%04d", idn);                   // Formatted ID for children
                new Child(child_id, sz, uz).start();
                idn++;
            }
            
        }
        catch(InterruptedException ie)
        {
            System.out.println("Interrupted Exception -> main()");
        }
    }
    
}
