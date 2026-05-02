package com.aprog_lab.aprog_pl.threads;

import com.aprog_lab.aprog_pl.events.*;
import com.aprog_lab.aprog_pl.shared_resources.Logger;
import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author Emanuel Baciu
 */
public class EventManager extends Thread
{
    private BlackoutEvent be;
    private StormEvent se;
    private ElevenSavesEvent ese;
    private HiveMindEvent hme;
    private AtomicReference<String> status;
    private Logger log;
    
    public EventManager(BlackoutEvent pbe, StormEvent pse, ElevenSavesEvent pese, HiveMindEvent phme, Logger p_log)
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
                if(log.getPlaying())
                {
                    Thread.sleep((int)((Math.random()*30000))+30000);
                    int pickedEvent = (int)(Math.random()*4);
                    switch(pickedEvent)
                    {
                        case 0 ->
                        {
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
                            log.logWrite("GLOBAL EVENT: HIVE MIND EVENT HAS STARTED");
                            if(!log.getPlaying()) log.waitLog();
                            hme.setStatus();
                            status.compareAndSet("None", "HIVEMIND");
                            Thread.sleep((int)((Math.random()*5000)+5000));
                            //hme.showDemos();
                            
                            if(!log.getPlaying()) log.waitLog();// DEBUG
                            
                            hme.setStatus();
                            status.compareAndSet("HIVEMIND", "None");
                            log.logWrite("GLOBAL EVENT: HIVE MIND EVENT HAS FINISHED");
                        }
                    }   
                }
                else
                {
                    log.waitLog();
                }
            }
            catch(InterruptedException ie)
            {
                System.out.println("IE at EventManager->run(): "+ie.getMessage());
            }
        }
    }
    
    public String getStatus()
    {
        return status.get();
    }
    
}
