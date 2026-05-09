package com.aprog_lab.aprog_pl.GUI_Initializers;

import com.aprog_lab.aprog_pl.GUI.GUI1_Server;
import com.aprog_lab.aprog_pl.Network_Connection.RemoteRefresher;
import com.aprog_lab.aprog_pl.events.BlackoutEvent;
import com.aprog_lab.aprog_pl.events.ElevenSavesEvent;
import com.aprog_lab.aprog_pl.events.HiveMindEvent;
import com.aprog_lab.aprog_pl.events.StormEvent;
import com.aprog_lab.aprog_pl.shared_resources.Portal;
import com.aprog_lab.aprog_pl.shared_resources.Safe_Zone;
import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
import com.aprog_lab.aprog_pl.shared_resources.VecnaChecker;
import com.aprog_lab.aprog_pl.shared_resources.LoggManager;
import com.aprog_lab.aprog_pl.threads.Child;
import com.aprog_lab.aprog_pl.threads.Demogorgon;
import com.aprog_lab.aprog_pl.threads.EventManager;
import com.aprog_lab.aprog_pl.threads.PortalManager;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Emanuel Baciu
 */
public class GUI1_Manager {
    
    private Unsafe_Zone Forest, Lab, Mall, Sewer, Hive;
    private Safe_Zone ms, bb, radio;
    private Portal p1, p2, p3, p4;
    private ArrayList<Unsafe_Zone> uz;
    private ArrayList<Safe_Zone> sz;
    private ArrayList<Portal> portals;
    private EventManager em;
    private LoggManager log;
    private RemoteRefresher r_obj;
    private BlackoutEvent be;
    private StormEvent se;
    private ElevenSavesEvent ese;
    private HiveMindEvent hme;
    private VecnaChecker vc;
    private GUI1_Server gui1;
    
    public GUI1_Manager(GUI1_Server p_gui1)
    {
        gui1 = p_gui1;
    }
    
    /*
    
    */
    public boolean initRMI()
    {
        boolean connectionAcquired = false;
        try
        {
            r_obj = new RemoteRefresher(log, uz, em, sz.get(0), portals);
            LocateRegistry.createRegistry(1099);
            Naming.rebind("//localhost/RemoteObjectCreatedForDemonstration", r_obj);
            connectionAcquired = true;
            System.out.println("Remote object registered");
            
        }
        catch(MalformedURLException | RemoteException e)
        {
            System.out.println("Error: "+e.getMessage());
        }
        
        return connectionAcquired;
    }
    
    /*
    
    */
    public void init()
    {
        boolean done = true;                                                        // To check whether the program has been initiated correctly. Used for showing the GUI.
        log = new LoggManager();                                                     // Log manager, writes specific actions and events on a TXT file.
// ===================== SAFE AND UNSAFE ZONE INITIALIZATION =====================
        Forest = new Unsafe_Zone("Forest", this, log);
        Lab = new Unsafe_Zone("Laboratory", this, log);
        Mall = new Unsafe_Zone("Shopping Mall", this, log);
        Sewer = new Unsafe_Zone("Sewer", this, log);
        Hive = new Unsafe_Zone("HIVE", this, log);

        ms = new Safe_Zone("Hawkin's Main Street", this, log);                       
        bb = new Safe_Zone("Bayer's Basement", this, log);
        radio = new Safe_Zone("WSQK Radio", this, log);
        
        uz = new ArrayList<>();                              // Children and Demogorgons share this ArrayList.
        sz = new ArrayList<>();                              // ONLY children are allowed to use this ArrayList.
        portals = new ArrayList<>();                         // Children and PortalManager use this ArrayList.
        
        // ===================== SAFE AND UNSAFE ZONE ADDITION =====================
        uz.add(Forest); uz.add(Lab); uz.add(Mall); uz.add(Sewer); uz.add(Hive);
        sz.add(ms); sz.add(bb); sz.add(radio);
        
        
// ===================== PORTAL INITIALIZATION =====================

        p1 = new Portal("ForestPortal", new CyclicBarrier(2), this, log);
        p2 = new Portal("LabPortal", new CyclicBarrier(3), this, log);
        p3 = new Portal("MallPortal", new CyclicBarrier(4), this, log);
        p4 = new Portal("SewerPortal", new CyclicBarrier(2), this, log);
        
        // ===================== PORTAL ADDITION =====================
        portals.add(p1); portals.add(p2); portals.add(p3); portals.add(p4);
        
// ===================== EVENT-RELATED OBJECT INITIALIZATION =====================
        be = new BlackoutEvent(portals, uz);
        se = new StormEvent();
        ese = new ElevenSavesEvent(uz.get(4), sz.get(2));
        hme = new HiveMindEvent(uz);
        
        em = new EventManager(be, se, ese, hme, log);
        em.start();
        
        // ===================== PORTAL MANAGER =====================
        new PortalManager(portals, log).start();

// ===================== VECNA CHECKER INITIALIZATION =====================
        vc = new VecnaChecker(1, uz, se, ese, hme, log);
        if(initRMI())
        {
// ===================== CHILDREN & DEMOGORGON INITIALIZATION =====================

            // Initial threads (Alpha Demog and Children) creation.
            try
            {
                int idn = 0;                                                                               // Used for Children and Demogorgon ID.
                new Demogorgon("D"+String.format("%04d",idn), uz, vc, se, ese, hme, log).start();          // Formats the ID for the Alpha Demogorgon (D0000).
                for(int i=0; i<1500; i++)
                {
                    Thread.sleep((int)(Math.random()*1.5+0.5));                                            // SHOULD wait between 0.5 and 2 seconds.
                    String child_id = "C"+String.format("%04d", idn);                                      // Formats the ID for children.
                    new Child(child_id, sz, uz, portals,se, log).start();
                    idn++;
                }
            }
            catch(InterruptedException ie)
            {
                System.out.println("Interrupted Exception -> GUI1 constructor");
                done = false;
            } 
        }
        else
        {
            System.out.println("Object Implementation interruped, the server couldn't start.");
            done = false;
        }
        // If the program has initiated correctly, shows the GUI. Otherwise it finishes.
        if(done)
        {
            gui1.setVisible(true);
        }
    }
 
    /* ========== PORTAL REFRESHER METHOD ==========
       -    Works as connector between the interface and the thread calls.
       -    Does as the name implies.
    */
    public void refreshPortalStats()
    {
        gui1.refreshPortalStats(portals);
    }
    
    /* ========== ZONE AND EVENT STATUS REFRESHER METHOD ==========
       -    Works as connector between the interface and the thread calls.
       -    Does as the name implies
    */
    public void refreshZoneStats()
    {
        gui1.refreshZoneStats(uz, sz, em);
    }
    
    /* ========== COUNTER REFRESHER METHOD ==========
       -    Works as connector between the interface and the thread calls.
       -    Does as the name implies
    */
    public void refreshCounters()
    {
        gui1.refreshCounters(sz, uz);
    }
}
