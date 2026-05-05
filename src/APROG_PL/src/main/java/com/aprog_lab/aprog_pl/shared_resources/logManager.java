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
public class logManager
{
    private AtomicBoolean playing;
    private ArrayList<Demogorgon> ranking;
    //private PrintWriter writer;
    private Logger logWriter;
    private FileHandler fileHandler;
    //private Path path;
    // Custom format
    //private DateTimeFormatter formatter;
    //private LocalDateTime dateTime;
    
    public logManager()
    {
        playing = new AtomicBoolean(true);
        ranking = new ArrayList<>();
        logWriter = Logger.getLogger(logManager.class.getName());
        logWriter.setUseParentHandlers(false);
        ranking = new ArrayList<>();
        
        //formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        //path = Paths.get("..\\..\\docs\\hawkings.txt");
        
        try
        {
            //writer = new PrintWriter(new FileWriter(path.toString()));
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
    
    /*
    
    */
    public void stop()
    {
        if(playing.get())
        {
            playing.compareAndSet(true, false);
            logWrite("Stopped program.");
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
            logWrite("Resumed program.");
        }
    }
    
    /*
    
    */
    public boolean getPlaying()
    {
        return playing.get();
    }
    
    /*
    
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
    
    /*
    
    */
    public synchronized void logWrite(String textContent)
    {
        //dateTime = LocalDateTime.now();
        //String logContent = (dateTime.format(formatter)+" "+textContent);
        //writer.println(logContent);
        //writer.close();
        logWriter.info(textContent);
        
    }
    
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
    
    public synchronized ArrayList<Demogorgon> getDemoRanking()
    {
        return ranking;
    }
}
