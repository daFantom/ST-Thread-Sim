package com.aprog_lab.aprog_pl;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
/**
 *
 * @author Emanuel Baciu
 */
public class Main {

    public static void main(String[] args)
    {
        ArrayList<Unsafe_Zone> uz = new ArrayList<>();                              // Children and Demogorgons share this ArrayList.
        ArrayList<Safe_Zone> sz = new ArrayList<>();                                // ONLY children are allowed to use this ArrayList.
        ArrayList<Portal> portals = new ArrayList<>();                              // Children and PortalManager use this ArrayList.
        
// =====================SAFE AND UNSAFE ZONE INITIALIZATION =====================
        Unsafe_Zone Forest = new Unsafe_Zone("Forest");
        Unsafe_Zone Lab = new Unsafe_Zone("Laboratory");
        Unsafe_Zone Mall = new Unsafe_Zone("Shopping Mall");
        Unsafe_Zone Sewer = new Unsafe_Zone("Sewer");
        Unsafe_Zone Hive = new Unsafe_Zone("HIVE");
        
        Safe_Zone ms = new Safe_Zone("Hawkin's Main Street");                       
        Safe_Zone bb = new Safe_Zone("Bayer's Basement");
        Safe_Zone radio = new Safe_Zone("WSQK Radio");
        
        // =====================SAFE AND UNSAFE ZONE ADDITION =====================
        uz.add(Forest); uz.add(Lab); uz.add(Mall); uz.add(Sewer); uz.add(Hive);
        sz.add(ms); sz.add(bb); sz.add(radio);

// ===================== PORTAL INITIALIZATION =====================
        Portal p1 = new Portal("ForestPortal", bb, Forest, new CyclicBarrier(2));
        Portal p2 = new Portal("LabPortal", bb, Lab, new CyclicBarrier(3));
        Portal p3 = new Portal("MallPortal", bb, Mall, new CyclicBarrier(4));
        Portal p4 = new Portal("SewerPortal", bb, Sewer, new CyclicBarrier(2));

        // ===================== PORTAL ADDITION =====================
        portals.add(p1); portals.add(p2); portals.add(p3); portals.add(p4);
        
        // ===================== PORTAL MANAGER =====================
        new PortalManager(portals).start();
        

        
// ===================== EVENT-RELATED OBJECT INITIALIZATION =====================
        BlackoutEvent be = new BlackoutEvent(portals);
        StormEvent se = new StormEvent();
        
        new EventManager(be, se).start();

// ===================== VECNA CHECKER INITIALIZATION =====================
        VecnaChecker vc = new VecnaChecker(1, uz, se);
        
// ===================== CHILDREN & DEMOGORGON INITIALIZATION =====================

        // Initial threads (Alpha Demog and Children) creation.
        try
        {
            int idn = 0;                                                               // Used for Children ID and Alpha Demogorgon.
            new Demogorgon("D"+String.format("%04d",idn), uz, vc, se).start();          // Formatted ID for the Alpha Demogorgon (D0000)
            for(int i=0; i<10; i++)
            {
                Thread.sleep((int)(Math.random()*1.5+0.5));                         // SHOULD wait between 0.5 and 2 seconds.
                String child_id = "C"+String.format("%04d", idn);                   // Formatted ID for children
                new Child(child_id, sz, uz, portals,se).start();
                idn++;
            }
            
        }
        catch(InterruptedException ie)
        {
            System.out.println("Interrupted Exception -> main()");
        }
    }
    
}
