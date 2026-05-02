package com.aprog_lab.aprog_pl.shared_resources;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Emanuel Baciu
 */
public class Logger
{
    private AtomicBoolean playing;
    private PrintWriter writer;
    private Path path;
    // Custom format
    private DateTimeFormatter formatter;
    private LocalDateTime dateTime;
    
    public Logger()
    {
        playing = new AtomicBoolean(true);
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        path = Paths.get("..\\..\\docs\\hawkings.txt");
        try
        {
            writer = new PrintWriter(new FileWriter(path.toString()));
        }
        catch(FileNotFoundException fnfe)
        {
            System.out.println("File not found.");
        }
        catch(IOException ioe)
        {
            System.out.println("File not found.");
        }
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
        dateTime = LocalDateTime.now();
        String logContent = (dateTime.format(formatter)+" "+textContent);
        writer.println(logContent);
    }
}
