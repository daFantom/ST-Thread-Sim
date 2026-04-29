package com.aprog_lab.aprog_pl.threads;

import com.aprog_lab.aprog_pl.events.*;
import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
import java.time.Duration;

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
    
    public EventManager(BlackoutEvent pbe, StormEvent pse, ElevenSavesEvent pese, HiveMindEvent phme)
    {
        be = pbe;
        se = pse;
        ese = pese;
        hme = phme;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                Thread.sleep((int)((Math.random()*30000)+30000));
                int pickedEvent = 3; // (int)(Math.random()*4);
                switch(pickedEvent)
                {
                    case 0 ->
                    {
                        System.out.println("========= LABORATORY BLACKOUT EVENT HAS STARTED =========");
                        be.disablePortalsEvent();
                        Thread.sleep((int)((Math.random()*5000)+5000));
                        be.enablePortalsEvent();
                        System.out.println("========= LABORATORY BLACKOUT EVENT HAS FINISHED =========");
                    }

                    case 1 ->
                    {
                        System.out.println("========= STORM EVENT HAS STARTED =========");
                        se.setStorm();
                        Thread.sleep((int)((Math.random()*5000)+5000));
                        se.setStorm();
                        System.out.println("========= STORM EVENT HAS FINISHED =========");
                    }

                    case 2 ->
                    {
                        System.out.println("========= ELEVEN'S INTERVENTION EVENT HAS STARTED =========");
                        ese.setStatus();
                        int duration = (int)((Math.random()*5)+5);
                        for(int i=0;i<duration;i++)
                        {
                            Thread.sleep(1000);                                 
                            ese.saveChild();
                            
                        }
                        ese.setStatus();
                        ese.enableDemos();
                        System.out.println("========= ELEVEN'S INTERVENTION EVENT HAS FINISHED =========");
                    }

                    case 3 ->
                    {
                        System.out.println("========= HIVE MIND EVENT HAS STARTED =========");
                        hme.setStatus();
                        Thread.sleep((int)((Math.random()*5000)+5000));
                        //hme.showDemos();
                        hme.setStatus();
                        System.out.println("========= HIVE MIND EVENT HAS FINISHED =========");
                    }
                }

            }
            catch(Exception e)
            {
                System.out.println("Exception: "+e.getMessage());
            }
        }
        

    }
}
