package com.aprog_lab.aprog_pl.Network_Connection;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Emanuel Baciu
 */
    public interface MyRemoteInterface extends java.rmi.Remote
    {
        public boolean stop() throws RemoteException;
        public boolean resume() throws RemoteException;
        public ArrayList<Integer> getUnsafeZonesAmountChildren() throws RemoteException;
        public ArrayList<Integer> getUnsafeZonesAmountDemos() throws RemoteException;
        public ArrayList<Integer> getPortalsChildrenAmountEntering() throws RemoteException;
        public ArrayList<Integer> getPortalsChildrenAmountLeaving() throws RemoteException;
        public ArrayList<String> getDemoRankings() throws RemoteException;
        public int getAvailChildrenHawkings() throws RemoteException;
        public String getActiveEvent() throws RemoteException;
    }
