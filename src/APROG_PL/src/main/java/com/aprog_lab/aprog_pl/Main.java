package com.aprog_lab.aprog_pl;

import java.util.concurrent.CyclicBarrier;
/**
 *
 * @author Emanuel Baciu
 */
public class Main {

    public static void main(String[] args) {
        
        try
        {
            int idn = 0;
            new Demogorgon("D"+String.format("%04d",idn), 0).start();
            for(int i=0; i<1500; i++)
            {
                Thread.sleep((int)((Math.random()*1.5)+0.5));                       // SHOULD wait between 0.5 and 2 seconds.
                String child_id = "C"+String.format("%04d", idn);
                new Child(child_id).start();
                idn++;
            }
            
        }
        catch(InterruptedException ie)
        {
            System.out.println("Interrupted Exception -> main()");
        }
    }
    
}
