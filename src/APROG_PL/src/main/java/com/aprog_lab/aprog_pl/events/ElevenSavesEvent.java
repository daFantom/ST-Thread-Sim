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
    /*
    
    */
    public void saveChild()
    {
        hive.save();
        radio_station.decrementBloodCount();
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
    /*
    
    */
    public void disableDemo()
    {
        try
        {
            synchronized(this)
            {
                System.out.println("DEMO STOPPED");
                wait();
            }
        }
        catch(InterruptedException ie)
        {
            System.out.println("IE at ElevenSavesEvent -> disableDemo()");
        }

    }
    /*
    
    */
    public void enableDemos()
    {
        synchronized(this)
        {
            notifyAll();
        }
    }
}
