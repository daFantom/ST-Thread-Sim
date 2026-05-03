package com.aprog_lab.aprog_pl.Network_Connection;

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
        
        public RemoteObjectImplementation(logManager p_log, ArrayList<Unsafe_Zone> p_uzs, EventManager p_em) throws RemoteException
        {
            log=p_log;
            uzs=p_uzs;
            em = p_em;
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
        public String getActiveEvent() throws RemoteException
        {
            return em.getStatus();
        }
        
        @Override
        public ArrayList<Unsafe_Zone> getUnsafeZones()
        {
            return uzs;
        }
    }
