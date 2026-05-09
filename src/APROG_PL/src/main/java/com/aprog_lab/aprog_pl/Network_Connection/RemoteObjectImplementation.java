package com.aprog_lab.aprog_pl.Network_Connection;

import com.aprog_lab.aprog_pl.shared_resources.Portal;
import com.aprog_lab.aprog_pl.shared_resources.Safe_Zone;
import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
import com.aprog_lab.aprog_pl.shared_resources.LogManager;
import com.aprog_lab.aprog_pl.threads.Demogorgon;
import com.aprog_lab.aprog_pl.threads.EventManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author Emanuel Baciu
 */
    public class RemoteObjectImplementation extends UnicastRemoteObject implements MyRemoteInterface
    {
        private LogManager log;
        private ArrayList<Unsafe_Zone> uzs;
        private EventManager em;
        private Safe_Zone hawkings;
        private ArrayList<Portal> portals;
        
        public RemoteObjectImplementation(LogManager p_log, ArrayList<Unsafe_Zone> p_uzs, EventManager p_em, Safe_Zone p_hawkings, ArrayList<Portal> p_portals) throws RemoteException
        {
            log = p_log;
            uzs = p_uzs;
            em = p_em;
            hawkings = p_hawkings;
            portals = p_portals;
        }
        
        /* ================ PROGRAM STOPPING / RESUME MECHANISM ================
            -   Calls upon the LogManager object to change a variable used for checking whether the program is stopped or not.
        */
        @Override
        public boolean stop() throws RemoteException
        {
            boolean done;
            if(log.getPlaying())
            {
                log.stop();
                done = true;
                System.out.println(log.getPlaying());
            }
            else
            {
                done = false;
            }
            return done;
        }
        
        @Override
        public boolean resume() throws RemoteException
        {
            boolean done;
            if(!log.getPlaying())
            {
                log.resume();
                System.out.println(log.getPlaying());
                done = true;
            }
            else
            {
                done = false;
            }
            return done;
        }
        
        /* ================ ACTIVE EVENT GETTER ================
            -   Gets the current active event by accesing the "status" variable inside of the EventManager thread.
        */
        @Override
        public synchronized String getActiveEvent() throws RemoteException
        {
            return em.getStatus();
        }
        
        /* ================ ZONE AND PORTAL GETTERS ================
            -   Returns the amount of children or demogorgons inside an unsafe zone. 
                The amount of children in a safe zone
                Or the entering / exiting children in a portal.
            -   Stores them in an Integer Array List (portals and unsafe zones) where each position references each unsafe zone or portal.
            -   For the amount of children in a safe zone, returns an int with the amount.
        */
        @Override
        public ArrayList<Integer> getUnsafeZonesAmountChildren() throws RemoteException
        {
            ArrayList<Integer> amountChildrenUZ = new ArrayList<>();
            for (Unsafe_Zone uz : uzs)
            {
                if(uz.getName().equals("HIVE"))
                {
                    amountChildrenUZ.add(uz.getCapturedChildren());
                }
                else
                {
                    amountChildrenUZ.add(uz.getAvailChildren().size());
                }
            }
            return amountChildrenUZ;
        }
        
        @Override
        public ArrayList<Integer> getUnsafeZonesAmountDemos() throws RemoteException
        {
            ArrayList<Integer> amountDemosUZ = new ArrayList<>();
            for (Unsafe_Zone uz : uzs)
            {
                amountDemosUZ.add(uz.getAvailDemos().size());
            }
            return amountDemosUZ;
        }
        
        @Override
        public ArrayList<Integer> getPortalsChildrenAmountEntering() throws RemoteException
        {
            ArrayList<Integer> portalChildrenQuantitiesEntering = new ArrayList<>();
            for (Portal p : portals)
            {
                portalChildrenQuantitiesEntering.add(p.getChildrenEnterQueueAmount());
            }
            return portalChildrenQuantitiesEntering;
        }
        
        @Override
        public ArrayList<Integer> getPortalsChildrenAmountLeaving() throws RemoteException
        {
            ArrayList<Integer> portalChildrenQuantitiesLeaving = new ArrayList<>();
            for (Portal p : portals)
            {
                portalChildrenQuantitiesLeaving.add(p.getChildrenLeavingQueueAmount());
            }
            return portalChildrenQuantitiesLeaving;
        }
        
        @Override
        public synchronized int getAvailChildrenHawkings() throws RemoteException
        {
            return hawkings.getAvailChildren().size();
        }
        
        /* ================ DEMOGORGON RANKING GETTERS ================
            -   Gets the ranking of demogorgons from the LogManager class and returns an String ArrayList with the available ID's of each demogorgon.
        */
        @Override
        public synchronized ArrayList<String> getDemoRankings() throws RemoteException
        {
            ArrayList<String> demoRank = new ArrayList<>();
            for(Demogorgon d : log.getDemoRanking())
            {
                demoRank.add(d.getID());
            }
            return demoRank;
        }
    }
