package com.aprog_lab.aprog_pl.threads;

import com.aprog_lab.aprog_pl.Network_Connection.MyRemoteInterface;
import java.rmi.RemoteException;

/**
 *
 * @author Emanuel Baciu
 */
public class ClientRefresher extends Thread {
    
    MyRemoteInterface roi;
    public ClientRefresher(MyRemoteInterface p_roi)
    {
        roi = p_roi;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                Thread.sleep(1000);
                roi.operationToOffer2("Test");
            }
            catch(RemoteException | InterruptedException re)
            {
                System.out.println("Exception at ClientRefresher->run()");
            } 
        }
    }
    
    /*
    
    */
    public void refreshDemoRankings()
    {
    }
    
    /*
    
    */
    public void refreshHawkingsChildren()
    {
    }
    
    /*
    
    */
    public void refreshChildrenInPortals()
    {
    }
    
    /*
    
    */
    public void refreshEventStatus()
    {
        
    }
    
    /*
    
    */
    public void refreshLocations()
    {
        
    }
}
