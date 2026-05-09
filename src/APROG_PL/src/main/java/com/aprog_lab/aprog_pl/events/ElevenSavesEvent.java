package com.aprog_lab.aprog_pl.events;
import com.aprog_lab.aprog_pl.shared_resources.Safe_Zone;
import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
import java.util.concurrent.atomic.AtomicBoolean;
/**
 *
 * @author Emanuel Baciu
 */
public class ElevenSavesEvent {
    private final Unsafe_Zone hive;
    private final AtomicBoolean status;
    private final Safe_Zone radio_station;
    
    public ElevenSavesEvent(Unsafe_Zone p_hive, Safe_Zone p_wsqk)
    {
        hive = p_hive;
        radio_station = p_wsqk;
        status = new AtomicBoolean(false);
    }
    /* ============= CHILD SAVING METHOD =============
        -   When called by EventManager thread, notifies a slept children inside of the HIVE unsafezone.
        -   Also decrements the counter of the WSQK Radio safe zone by one.
        -   Called as many times as supposed duration (couldn't think of anything better).
    */
    public void saveChild()
    {
        hive.save();
        radio_station.decrementBloodCount();
    }
    
    /* ============= STATUS SETTER METHOD =============
        -   Sets the status to true or false depending on whether the event has started or finished.
        -   Used for the checking the current event on other threads and shared variables.
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
    
    /* ============= STATUS GETTER METHOD =============
        -   Return the current value of the status.
        -   Called from other threads and shared variables to check the status.
    */
    public boolean getStatus()
    {
        return status.get();
    }
    
    /* ============= DEMOGORGON DISABLING METHOD =============
        -   Used by demogorgons after checking the status of the event.
        -   If true, they will wait until the event finishes on the class' monitor.
    */
    public void disableDemo()
    {
        try
        {
            synchronized(this)
            {
                //System.out.println("DEMO STOPPED");                               // DEBUG
                wait();
            }
        }
        catch(InterruptedException ie)
        {
            System.out.println("IE at ElevenSavesEvent -> disableDemo()");
        }
    }
    
    /* ============= DEMOGORGON ENABLING METHOD =============
        -   Used by EventManager thread to wake up all demogorgons and resume the program.
        -   Only called after the event is over.
    */
    public void enableDemos()
    {
        synchronized(this)
        {
            notifyAll();
        }
    }
}
