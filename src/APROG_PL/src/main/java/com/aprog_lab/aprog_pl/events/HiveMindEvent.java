package com.aprog_lab.aprog_pl.events;

import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Emanuel Baciu
 */
public class HiveMindEvent
{
    private ArrayList<Unsafe_Zone> uzs;
    private AtomicBoolean status;
    
    public HiveMindEvent(ArrayList<Unsafe_Zone> p_uzs)
    {
        uzs = p_uzs;
        status = new AtomicBoolean(false);
    }
    
    /*
    
    */
    public Unsafe_Zone getMostChildrenZone()
    {
        int max = 0;
        int idx = 0;
        for(int i=0;i<uzs.size()-1;i++)
        {
            int amount = uzs.get(i).getAmountChildren();
            if(amount>max)
            {
                max = amount;
                idx = i;
            }
        }
        return uzs.get(idx);
    }
    
    /*
    
    */
    public void setStatus()
    {
        if(status.get())
        {
            status.compareAndSet(true, false);
        }
        else
        {
            status.compareAndSet(false, true);
        }
    }
    
    /*
    
    */
    public boolean getStatus()
    {
        return status.get();
    }
    
    
    public void showDemos()
    {
        for(int i=0;i<uzs.size()-1;i++)
        {
            System.out.println("ZONE: "+uzs.get(i).getName());
            uzs.get(i).showDemos();
        }
    }
}
