package com.aprog_lab.aprog_pl.events;

import com.aprog_lab.aprog_pl.shared_resources.Portal;
import java.util.ArrayList;

/**
 *
 * @author Emanuel Baciu
 */
public class BlackoutEvent
{
    private ArrayList<Portal> portals;
    
    public BlackoutEvent(ArrayList<Portal> pportals)
    {
        portals = pportals;
    }
    
    /*
    
    */
    public void disablePortalsEvent()
    {
        for(int i=0;i<portals.size();i++)
        {
            portals.get(i).disablePortal();
        }
    }
    
    /*
    
    */
    public void enablePortalsEvent()
    {
        for(int i=0;i<portals.size();i++)
        {
            portals.get(i).enablePortal();
        }
    }
}
