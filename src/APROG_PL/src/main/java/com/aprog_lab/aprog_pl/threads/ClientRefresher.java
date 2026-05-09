package com.aprog_lab.aprog_pl.threads;

import com.aprog_lab.aprog_pl.GUI.GUI2_Client;
import java.rmi.RemoteException;
import com.aprog_lab.aprog_pl.Network_Connection.RemoteInterface;

/**
 *
 * @author Emanuel Baciu
 */
public class ClientRefresher extends Thread {
    
    private RemoteInterface roi;
    private GUI2_Client gui2;
    
    public ClientRefresher(RemoteInterface p_roi, GUI2_Client p_gui2)
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
                gui2.refreshEvent(roi.getActiveEvent());                                                    // Refreshes the current event using the remote object.
                gui2.refreshLocations(roi.getUnsafeZonesAmountChildren(), roi.getUnsafeZonesAmountDemos()); // Refreshes the amount of children and demogorgons inside of each unsafe zone using the remote object.
                gui2.refreshPortals(roi.getPortalsChildrenAmountEntering(), roi.getPortalsChildrenAmountLeaving()); // Refreshes the amount of children waiting for each portal (enter / exit) using the remote object.
                gui2.refreshHawkings(roi.getAvailChildrenHawkings());   // Refreshes the amount of children available in Hawking's Main Street safezone using the remote object.
                gui2.refreshDemoRankings(roi.getDemoRankings());        // Refreshes the demogorgon ranking using the remoe object.
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
}
