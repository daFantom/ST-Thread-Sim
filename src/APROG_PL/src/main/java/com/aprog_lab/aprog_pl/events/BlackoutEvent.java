package com.aprog_lab.aprog_pl.events;

import com.aprog_lab.aprog_pl.shared_resources.Portal;
import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
import java.util.ArrayList;

/**
 *
 * @author Emanuel Baciu
 */
public class BlackoutEvent
{
    private ArrayList<Portal> portals;
    private ArrayList<Unsafe_Zone> uzs;
    
    public BlackoutEvent(ArrayList<Portal> pportals, ArrayList<Unsafe_Zone> p_uzs)
    {
        portals = pportals;
        uzs = p_uzs;
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
    
    /*
    
    */
    public void disableUnsafe_ZonesDemos()
    {
        for(int i=0;i<uzs.size();i++)
        {
            uzs.get(i).disableUnsafeZonesDemos();
        }
    }
    
    /*
    
    */
    public void enableUnsafe_ZonesDemos()
    {
        for(int i=0;i<uzs.size();i++)
        {
            uzs.get(i).enableUnsafeZonesDemos();
        }
    }
}
