package com.aprog_lab.aprog_pl.Network_Connection;

import com.aprog_lab.aprog_pl.shared_resources.Portal;
import com.aprog_lab.aprog_pl.shared_resources.Safe_Zone;
import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
import com.aprog_lab.aprog_pl.shared_resources.logManager;
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
        private logManager log;
        private ArrayList<Unsafe_Zone> uzs;
        private EventManager em;
        private Safe_Zone hawkings;
        private ArrayList<Portal> portals;
        
        public RemoteObjectImplementation(logManager p_log, ArrayList<Unsafe_Zone> p_uzs, EventManager p_em, Safe_Zone p_hawkings, ArrayList<Portal> p_portals) throws RemoteException
        {
            log = p_log;
            uzs = p_uzs;
            em = p_em;
            hawkings = p_hawkings;
            portals = p_portals;
        }
        
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
        
        @Override
        public synchronized String getActiveEvent() throws RemoteException
        {
            return em.getStatus();
        }
        
        @Override
        public ArrayList<Integer> getUnsafeZonesAmountChildren()
        {
            ArrayList<Integer> amountChildrenUZ = new ArrayList<>();
            for (Unsafe_Zone uz : uzs)
            {
                amountChildrenUZ.add(uz.getAvailChildren().size());
            }
            return amountChildrenUZ;
        }
        
        @Override
        public ArrayList<Integer> getUnsafeZonesAmountDemos()
        {
            ArrayList<Integer> amountDemosUZ = new ArrayList<>();
            for (Unsafe_Zone uz : uzs)
            {
                amountDemosUZ.add(uz.getAvailDemos().size());
            }
            return amountDemosUZ;
        }
        
        @Override
        public ArrayList<Integer> getPortalsChildrenAmountEntering()
        {
            ArrayList<Integer> portalChildrenQuantitiesEntering = new ArrayList<>();
            for (Portal p : portals)
            {
                portalChildrenQuantitiesEntering.add(p.getChildrenEnterQueueAmount());
            }
            return portalChildrenQuantitiesEntering;
        }
        
        @Override
        public ArrayList<Integer> getPortalsChildrenAmountLeaving()
        {
            ArrayList<Integer> portalChildrenQuantitiesLeaving = new ArrayList<>();
            for (Portal p : portals)
            {
                portalChildrenQuantitiesLeaving.add(p.getChildrenLeavingQueueAmount());
            }
            return portalChildrenQuantitiesLeaving;
        }
        
        @Override
        public synchronized int getAvailChildrenHawkings()
        {
            return hawkings.getAvailChildren().size();
        }
    }
