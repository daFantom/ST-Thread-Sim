package com.aprog_lab.aprog_pl.threads;

import com.aprog_lab.aprog_pl.shared_resources.logManager;
import com.aprog_lab.aprog_pl.shared_resources.Portal;
import java.util.ArrayList;

/**
 *
 * @author Emanuel Baciu
 */
public class PortalManager extends Thread{
    ArrayList<Portal> portals;
    private logManager log;
    
    public PortalManager(ArrayList<Portal> pportals, logManager p_log)
    {
        portals = pportals;
        log = p_log;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                if(log.getPlaying())
                {
                    Thread.sleep(1000);
                    for(int i=0;i<portals.size();i++)
                    {
                        portals.get(i).openPortal();
                    }   
                }
                else
                {
                    log.waitLog();
                }
            }
            catch(InterruptedException ie)
            {
                System.out.println("IE at PortalManager->run()");
            }
        }
    }
}
