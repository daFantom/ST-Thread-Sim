package com.aprog_lab.aprog_pl.threads;

import com.aprog_lab.aprog_pl.GUI.GUI2_Client;
import com.aprog_lab.aprog_pl.Network_Connection.MyRemoteInterface;
import java.rmi.RemoteException;

/**
 *
 * @author Emanuel Baciu
 */
public class ClientRefresher extends Thread {
    
    private MyRemoteInterface roi;
    private GUI2_Client gui2;
    
    public ClientRefresher(MyRemoteInterface p_roi, GUI2_Client p_gui2)
    {
        roi = p_roi;
        gui2 = p_gui2;
    }
    
    @Override
    public void run()
    {
        try
        {
            while(true)
            {
                Thread.sleep(500);
                gui2.refreshEvent(roi.getActiveEvent());
                gui2.refreshLocations(roi.getUnsafeZonesAmountChildren(), roi.getUnsafeZonesAmountDemos());
                gui2.refreshPortals(roi.getPortalsChildrenAmountEntering(), roi.getPortalsChildrenAmountLeaving());
                gui2.refreshHawkings(roi.getAvailChildrenHawkings());
                gui2.refreshDemoRankings(roi.getDemoRankings());
            }
        }
        catch(RemoteException re)
        {
            System.out.println("Error: "+re.getMessage());
        }
        catch(InterruptedException ie)
        {
            System.out.println("Error: "+ie.getMessage());
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
