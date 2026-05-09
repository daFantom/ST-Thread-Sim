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
    
    /* ============= STATUS SETTER =============
        -   Used by EventManager thread to enable or disable the event.
    */
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
    
    /* ============= STATUS GETTER =============
        -   Used by demogorgon and child threads in order to act in a way or another 
            (halve their attack rate or double their blood gaining, respectively).
    */
    public boolean isStorm()
    {
        return storm_status.get();
    }
}
