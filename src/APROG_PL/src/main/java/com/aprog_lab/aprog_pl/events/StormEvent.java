package com.aprog_lab.aprog_pl.events;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Emanuel Baciu
 */
public class StormEvent {
    private AtomicBoolean storm_status;
    
    public StormEvent()
    {
        storm_status = new AtomicBoolean(false);
    }
    
    public void setStorm()
    {
        if(storm_status.get())
        {
            storm_status.compareAndSet(true, false);
        }
        else
        {
            storm_status.compareAndSet(false, true);
        }
    }
    
    public boolean isStorm()
    {
        return storm_status.get();
    }
}
