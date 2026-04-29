package com.aprog_lab.aprog_pl;

/**
 *
 * @author Emanuel Baciu
 */
public class EventManager extends Thread
{
    private BlackoutEvent be;
    
    public EventManager(BlackoutEvent pbe)
    {
        be=pbe;
    }
    
    @Override
    public void run()
    {
        try
        {
            Thread.sleep((int)((Math.random()*30000)+30000));
            int pickedEvent = 0; // (int)(Math.random()*4);
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
                    // UPSIDE DOWN STORM
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
