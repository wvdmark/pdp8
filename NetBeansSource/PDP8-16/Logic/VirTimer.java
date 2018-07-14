/*
 * VirTimer.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Logic;


import java.io.PrintStream;


public class VirTimer {
    
    public int getDelay() {
        return delay;
    }
    
    public int getInitialDelay() {
        return initialDelay;
    }
    
    public void post() {
        if(virlistener != null)
            virlistener.actionPerformed();
        
    }
    
    public void restart() {
        stop();
        start();
    }
    
    public void start() {
        virqueue.addTimer(this, (long)getInitialDelay());
    }
    
    public void stop() {
        virqueue.removeTimer(this);
    }
    
    public boolean isRepeats() {
        return repeats;
    }
    
    public boolean isRunning() {
        return virqueue.containsTimer(this);
    }
    
    public void setDelay(int i) {
        if(i < 0) {
            throw new IllegalArgumentException("Invalid delay: " + i);
        } else {
            delay = i;
            return;
        }
    }
    
    public void setInitialDelay(int i) {
        if(i < 0) {
            throw new IllegalArgumentException("Invalid initial delay: " + i);
        } else {
            initialDelay = i;
            return;
        }
    }
    
    
    public void setRepeats(boolean flag) {
        repeats = flag;
    }
    
    public VirTimer(VirQueue virqueue, int i, VirListener virlistener) {
        this.virqueue = virqueue;
        repeats = true;
        delay = i;
        initialDelay = i;
        if(virlistener != null)
            this.virlistener = virlistener;
    }
    
    
    int initialDelay;
    int delay;
    boolean repeats;
    long expirationTime;
    boolean running;
    VirQueue virqueue;
    VirListener virlistener;
    VirTimer nextTimer;
}

