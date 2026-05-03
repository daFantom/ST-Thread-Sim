package com.aprog_lab.aprog_pl.Network_Connection;

import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
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
        public ArrayList<Unsafe_Zone> getUnsafeZones() throws RemoteException;
        public String getActiveEvent() throws RemoteException;
    }
