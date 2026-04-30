package com.aprog_lab.aprog_pl.threads;

import com.aprog_lab.aprog_pl.events.*;
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
    
    public EventManager(BlackoutEvent pbe, StormEvent pse, ElevenSavesEvent pese, HiveMindEvent phme)
    {
        be = pbe;
        se = pse;
        ese = pese;
        hme = phme;
        status = new AtomicReference("None");
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                Thread.sleep((int)((Math.random()*30000)+30000));
                int pickedEvent = (int)(Math.random()*4);
                switch(pickedEvent)
                {
                    case 0 ->
                    {
                        System.out.println("========= LABORATORY BLACKOUT EVENT HAS STARTED =========");
                        status.compareAndSet("None", "BLACKOUT");
                        be.disablePortalsEvent();
                        Thread.sleep((int)((Math.random()*5000)+5000));
                        be.enablePortalsEvent();
                        status.compareAndSet("BLACKOUT", "None");
                        System.out.println("========= LABORATORY BLACKOUT EVENT HAS FINISHED =========");
                    }

                    case 1 ->
                    {
                        System.out.println("========= STORM EVENT HAS STARTED =========");
                        status.compareAndSet("None", "STORM");
                        se.setStorm();
                        Thread.sleep((int)((Math.random()*5000)+5000));
                        se.setStorm();
                        status.compareAndSet("STORM", "None");
                        System.out.println("========= STORM EVENT HAS FINISHED =========");
                    }

                    case 2 ->
                    {
                        System.out.println("========= ELEVEN'S INTERVENTION EVENT HAS STARTED =========");
                        status.compareAndSet("None", "ELEVEN");
                        ese.setStatus();
                        int duration = (int)((Math.random()*5)+5);
                        for(int i=0;i<duration;i++)
                        {
                            Thread.sleep(1000);                                 
                            ese.saveChild();
                            
                        }
                        ese.setStatus();
                        status.compareAndSet("ELEVEN", "None");
                        ese.enableDemos();
                        System.out.println("========= ELEVEN'S INTERVENTION EVENT HAS FINISHED =========");
                    }

                    case 3 ->
                    {
                        System.out.println("========= HIVE MIND EVENT HAS STARTED =========");
                        hme.setStatus();
                        status.compareAndSet("None", "HIVEMIND");
                        Thread.sleep((int)((Math.random()*5000)+5000));
                        //hme.showDemos();                                                          // DEBUG
                        hme.setStatus();
                        status.compareAndSet("HIVEMIND", "None");
                        System.out.println("========= HIVE MIND EVENT HAS FINISHED =========");
                    }
                }

            }
            catch(InterruptedException ie)
            {
                System.out.println("IE at EventManager->run(): "+ie.getMessage());
            }
        }
        

    }
}
