package com.aprog_lab.aprog_pl.threads;

import com.aprog_lab.aprog_pl.events.*;

/**
 *
 * @author Emanuel Baciu
 */
public class EventManager extends Thread
{
    private BlackoutEvent be;
    private StormEvent se;
    private ElevenSavesEvent ese;
    
    public EventManager(BlackoutEvent pbe, StormEvent pse, ElevenSavesEvent pese)
    {
        be=pbe;
        se = pse;
        ese=pese;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                Thread.sleep((int)((Math.random()*30000)+30000));
                int pickedEvent = 2; // (int)(Math.random()*4);
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
                        //  ELEVENS INTERVENTION
                    }

                    case 3 ->
                    {
                        // THE HIVE MIND
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
