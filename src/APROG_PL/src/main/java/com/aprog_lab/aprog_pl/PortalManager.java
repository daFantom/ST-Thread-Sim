package com.aprog_lab.aprog_pl;

import java.util.ArrayList;

/**
 *
 * @author Emanuel Baciu
 */
public class PortalManager extends Thread{
    ArrayList<Portal> portals;
    
    public PortalManager(ArrayList<Portal> pportals)
    {
        portals = pportals;
    }
    
    public void run()
    {
        while(true)
        {
            try
            {
                Thread.sleep(1000);
                
                for(int i=0;i<portals.size();i++)
                {
                    portals.get(i).openPortal();
                }
            }
            catch(InterruptedException ie)
            {
                System.out.println("IE at PortalManager->run()");
            }
        }
    }
}
