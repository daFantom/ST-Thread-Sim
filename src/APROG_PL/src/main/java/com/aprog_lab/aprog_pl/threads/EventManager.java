package com.aprog_lab.aprog_pl;

/**
 *
 * @author Emanuel Baciu
 */
public class EventManager extends Thread
{
    private BlackoutEvent be;
    private StormEvent se;
    
    public EventManager(BlackoutEvent pbe, StormEvent pse)
    {
        be=pbe;
        se = pse;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                Thread.sleep((int)((Math.random()*30000)+30000));
                int pickedEvent = 1; // (int)(Math.random()*4);
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
