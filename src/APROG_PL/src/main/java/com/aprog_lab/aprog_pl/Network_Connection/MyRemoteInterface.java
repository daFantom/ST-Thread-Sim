package com.aprog_lab.aprog_pl.Network_Connection;

import java.rmi.RemoteException;

/**
 *
 * @author Emanuel Baciu
 */
    public interface MyRemoteInterface extends java.rmi.Remote
    {
        public boolean stop() throws RemoteException;
        public boolean resume() throws RemoteException;
        public String operationToOffer2(String msg) throws RemoteException;
    }
