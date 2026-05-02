package com.aprog_lab.aprog_pl.shared_resources;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Emanuel Baciu
 */
public class Logger
{
    private AtomicBoolean playing;
    
    public Logger()
    {
        playing = new AtomicBoolean(true);
    }
    
    /*
    
    */
    public void stop()
    {
        if(playing.get())
        {
            playing.compareAndSet(true, false);
        }
    }
    /*
    
    */
    public synchronized void resume()
    {
        if(!playing.get())
        {
            playing.compareAndSet(false, true);
            notifyAll();
        }
    }
    
    
    public boolean getPlaying()
    {
        return playing.get();
    }
    
    public synchronized void waitLog()
    {
        try
        {
            wait();
        }
        catch(InterruptedException ie)
        {
            System.out.println("IE at Logger->waitLog()");
        }
    }
}
