package com.aprog_lab.aprog_pl.threads;

import com.aprog_lab.aprog_pl.events.*;
import com.aprog_lab.aprog_pl.shared_resources.LoggManager;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author Emanuel Baciu
 */
public class EventManager extends Thread
{
    private final BlackoutEvent be;
    private final StormEvent se;
    private final ElevenSavesEvent ese;
    private final HiveMindEvent hme;
    private final AtomicReference<String> status;
    private final LoggManager log;
    
    public EventManager(BlackoutEvent pbe, StormEvent pse, ElevenSavesEvent pese, HiveMindEvent phme, LoggManager p_log)
    {
        be = pbe;
        se = pse;
        ese = pese;
        hme = phme;
        status = new AtomicReference("None");
        log = p_log;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                if(log.getPlaying())                                                // If the program is stopped, it will wait on the respecitve class' monitor.
                {
                    Thread.sleep((int)((Math.random()*30000))+30000);               // Otherwise, waits from 30-60 seconds
                    int pickedEvent = (int)(Math.random()*4);                       // and chooses an event out of the 4 available.
                    switch(pickedEvent)
                    {
                        case 0 ->
                        {
                            /* ==================== LABORATORY BLACKOUT EVENT ====================
                                -   Sets the current class' status to BLACKOUT (for checking and GUI purposes)
                                -   Checks again whether the simulation is stopped (for safety).
                                -   Uses special method calling to disable the passing through portals and between unsafe zones.
                                -   Waits from 5 - 10 seconds and restores everything. In other words, opens the portals and the unsafe zones
                                    and changes the status to 'None'.
                                - Checks whether the simulation is stopped again before re-enabling just in case it was done while waiting.
                                -   Stores the event start and end in the log file.
                            */
                            log.logWrite("GLOBAL EVENT: LABORATORY BLACKOUT EVENT HAS STARTED");
                            status.compareAndSet("None", "BLACKOUT");
                            
                            if(!log.getPlaying()) log.waitLog();
                            
                            be.disablePortalsEvent();
                            be.disableUnsafe_ZonesDemos();
                            Thread.sleep((int)((Math.random()*5000)+5000));
                            
                            if(!log.getPlaying()) log.waitLog();
                            
                            be.enablePortalsEvent();
                            be.enableUnsafe_ZonesDemos();
                            status.compareAndSet("BLACKOUT", "None");
                            log.logWrite("GLOBAL EVENT: LABORATORY BLACKOUT EVENT HAS FINISHED");
                        }

                        case 1 ->
                        {
                            /* ==================== STORM EVENT ====================
                                -   Sets the current class' status to STORM (for checking and GUI purposes)
                                -   Checks again whether the simulation is stopped (for safety).
                                -   Sets to true a special variable on the respective Event class for demogorgons and children to check.
                                -   The changes from this event are explained on their respective classes. (Demogorgon and Child).
                                -   Waits from 5 - 10 seconds and restores everything. In other words, set the special variable to false
                                    and changes the status to 'None'.
                                -   Checks whether the simulation is stopped again before setting the variable just in case it was done while waiting.
                                -   Stores the event start and end in the log file.
                            */
                            log.logWrite("GLOBAL EVENT: STORM EVENT HAS STARTED");
                            status.compareAndSet("None", "STORM");
                            
                            if(!log.getPlaying()) log.waitLog();
                            
                            se.setStorm();
                            Thread.sleep((int)((Math.random()*5000)+5000));
                            
                            if(!log.getPlaying()) log.waitLog();
                            
                            se.setStorm();
                            status.compareAndSet("STORM", "None");
                            log.logWrite("GLOBAL EVENT: STORM EVENT HAS FINISHED");
                        }

                        case 2 ->
                        {
                            /* ==================== ELEVEN'S INTERVENTION EVENT ====================
                                -   Sets the current class' status to ELEVEN (for checking and GUI purposes)
                                -   Checks again whether the simulation is stopped (for safety).
                                -   Sets to true a special variable on the respective Event class for demogorgons to check.
                                -   The changes from this event are explained on their respective classes. (Demogorgon).
                                -   Calculates the duration of this event an saves as much children as the duration implies using
                                    the saveChild() method inside of the class.
                                -   Checks whether the simulation is stopped again before setting the variable and enabling
                                    the demogorgons just in case it was done while waiting.
                                -   Stores the event start and end in the log file.
                            */
                            log.logWrite("GLOBAL EVENT: ELEVEN'S INTERVENTION EVENT HAS STARTED");
                            status.compareAndSet("None", "ELEVEN");
                            
                            if(!log.getPlaying()) log.waitLog();
                            
                            ese.setStatus();
                            
                            int duration = (int)((Math.random()*5)+5);
                            for(int i=0;i<duration;i++)
                            {
                                if(!log.getPlaying()) log.waitLog();
                                
                                Thread.sleep(1000);                                 
                                ese.saveChild();

                            }
                            
                            if(!log.getPlaying()) log.waitLog();
                            
                            ese.setStatus();
                            status.compareAndSet("ELEVEN", "None");
                            ese.enableDemos();
                            log.logWrite("GLOBAL EVENT: ELEVEN'S INTERVENTION EVENT HAS FINISHED");
                        }

                        case 3 ->
                        {
                            /* ==================== HIVEMIND EVENT ====================
                                -   Sets the current class' status to HIVEMIND (for checking and GUI purposes)
                                -   Checks again whether the simulation is stopped (for safety).
                                -   Sets to true a special variable on the respective Event class for demogorgons to check.
                                -   The changes from this event are explained on their respective classes. (Demogorgon).
                                -   Checks whether the simulation is stopped again before setting the variable and enabling
                                    the demogorgons just in case it was done while waiting.
                                -   Stores the event start and end in the log file.
                            */
                            log.logWrite("GLOBAL EVENT: HIVE MIND EVENT HAS STARTED");
                            if(!log.getPlaying()) log.waitLog();
                            hme.setStatus();
                            status.compareAndSet("None", "HIVEMIND");
                            Thread.sleep((int)((Math.random()*5000)+5000));
                            //hme.showDemos();                                          // DEBUG                
                            
                            if(!log.getPlaying()) log.waitLog();
                            
                            hme.setStatus();
                            status.compareAndSet("HIVEMIND", "None");
                            log.logWrite("GLOBAL EVENT: HIVE MIND EVENT HAS FINISHED");
                        }
                    }   
                }
                else
                {
                    log.waitLog();                                         // If the simulation was stopped before starting, waits inside of the respective monitor.
                }
            }
            catch(InterruptedException ie)
            {
                System.out.println("IE at EventManager->run(): "+ie.getMessage());
            }
        }
    }
    
    /* ================== STATUS GETTER ==================
        -   Returns the current value of the ongoing event.
        -   If no event is ongoing, returns 'None'.
        -   Used by most threads and classes.
    */
    public String getStatus()
    {
        return status.get();
    }
    
}
