package com.aprog_lab.aprog_pl.Network_Connection;

import com.aprog_lab.aprog_pl.shared_resources.logManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Emanuel Baciu
 */
    public class RemoteObjectImplementation extends UnicastRemoteObject implements MyRemoteInterface
    {
        logManager log;
        
        public RemoteObjectImplementation(logManager p_log) throws RemoteException
        {
            log=p_log;
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
        public String operationToOffer2(String msg) throws RemoteException
        {
            return msg+": ANSWERED";
        }
    }
