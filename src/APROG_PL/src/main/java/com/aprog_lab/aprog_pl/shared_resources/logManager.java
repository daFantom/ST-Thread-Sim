package com.aprog_lab.aprog_pl.shared_resources;

import com.aprog_lab.aprog_pl.threads.Demogorgon;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.*;

/**
 *
 * @author Emanuel Baciu
 */
public class LogManager
{
    private AtomicBoolean playing;
    private ArrayList<Demogorgon> ranking;
    private Logger logWriter;
    private FileHandler fileHandler;
;
    
    public LogManager()
    {
        playing = new AtomicBoolean(true);
        ranking = new ArrayList<>();
        logWriter = Logger.getLogger(LogManager.class.getName());
        logWriter.setUseParentHandlers(false);
        ranking = new ArrayList<>();
        
        try
        {
            fileHandler = new FileHandler("..\\..\\docs\\hawkings.txt", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logWriter.addHandler(fileHandler);
        }
        catch(FileNotFoundException fnfe)
        {
            System.out.println("File not found.");
        }
        catch(IOException ioe)
        {
            System.out.println("IO Exception.");
        }
    }
    
    /* ================ PROGRAM STOPPED AND RESUME ================
        -   Sets the playing variable to true or false in order to stop or resume the program.
        -   If resumed from stopped, notifies all waiting threads to continue the program.
    */
    public void stop()
    {
        if(playing.get())
        {
            playing.compareAndSet(true, false);
            logWrite("Stopped program.");
        }
    }
    
    public synchronized void resume()
    {
        if(!playing.get())
        {
            playing.compareAndSet(false, true);
            notifyAll();
            logWrite("Resumed program.");
        }
    }
    
    /* ================ PLAYING GETTER ================
        -   Returns the value of the AtomicBoolean playing variable. Used for stopping or resuming the program.
    */
    public boolean getPlaying()
    {
        return playing.get();
    }
    
    /* ================ MONITOR WAITING METHOD ================
        -   Makes all threads who call this method wait inside of this class' monitor as a way of stopping the program.
    */
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
    
    /* ================ LOG WRITING METHOD ================
        -   Writes a log inside of the hawkings.txt file with the textContent String.
    */
    public synchronized void logWrite(String textContent)
    {
        logWriter.info(textContent);
    }
    
    /* ================ RANKING UPDATE METHOD ================
        -   Used by demogorgons to update their ranking basing off their total children capturing.
    */
    public synchronized void updateRanking(Demogorgon d)
    {
        boolean changed = false;
        if(ranking.size() < 3 && !ranking.contains(d))
        {
            ranking.add(d);
        }

        for(int i=0;i<ranking.size();i++)
        {
            if((d.getTotalCaptured()>=ranking.get(i).getTotalCaptured()) && !changed)
            {
                ranking.set(i, d);
                changed = true;
            }

        }
    }
    
    /* ================ RANKING GETTER METHOD ================
        -   Returns the demogorgon ranking. Used for displaying purposes.
    */
    public synchronized ArrayList<Demogorgon> getDemoRanking()
    {
        return ranking;
    }
}
