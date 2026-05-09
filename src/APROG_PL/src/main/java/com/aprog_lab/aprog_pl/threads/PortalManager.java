package com.aprog_lab.aprog_pl.threads;

import com.aprog_lab.aprog_pl.shared_resources.LogManager;
import com.aprog_lab.aprog_pl.shared_resources.Portal;
import java.util.ArrayList;

/**
 *
 * @author Emanuel Baciu
 */
public class PortalManager extends Thread{
    ArrayList<Portal> portals;
    private LogManager log;
    
    public PortalManager(ArrayList<Portal> pportals, LogManager p_log)
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
                if(log.getPlaying())                                                 // Checks whether the program is stopped or not. If so, waits in a monitor queue.
                {
                    Thread.sleep(1000);                                              
                    for(int i=0;i<portals.size();i++)
                    {
                        portals.get(i).openPortal();                                 // Each second it lets a child pass into the cyclic barrier simulated as portal. Exiting ones get prio.
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
