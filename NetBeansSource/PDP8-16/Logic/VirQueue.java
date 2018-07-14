/*
 * VirQueue.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Logic;

public class VirQueue {
    
    public VirQueue(BusRegMem data) {
        this.data = data;
    }
    
    public void postExpiredTimers() {
        long l1;
        do {
            VirTimer timer = firstTimer;
            if(timer == null)
                return;
            long l = data.pdp8time;
            l1 = timer.expirationTime - l;
            if(l1 <= 0L) {
                removeTimer(timer);
                timer.post();
                if(timer.isRepeats())
                    addTimer(timer, (long)timer.getDelay());
            }
        } while(l1 <= 0L);
    }
    
    
    void removeTimer(VirTimer timer) {
        if(!timer.running)
            return;
        VirTimer timer1 = null;
        VirTimer timer2 = firstTimer;
        boolean flag = false;
        do {
            if(timer2 == null)
                break;
            if(timer2 == timer) {
                flag = true;
                break;
            }
            timer1 = timer2;
            timer2 = timer2.nextTimer;
        } while(true);
        if(!flag)
            return;
        if(timer1 == null)
            firstTimer = timer.nextTimer;
        else
            timer1.nextTimer = timer.nextTimer;
        timer.expirationTime = 0L;
        timer.nextTimer = null;
        timer.running = false;
    }
    
    boolean containsTimer(VirTimer timer) {
        return timer.running;
    }
    
    void addTimer(VirTimer timer, long l) {
        long l1;
        l1 = data.pdp8time + l;
        if(timer.running)
            return;
        VirTimer timer1 = null;
        VirTimer timer2;
        for(timer2 = firstTimer; timer2 != null && timer2.expirationTime <= l1; timer2 = timer2.nextTimer)
            timer1 = timer2;
        
        if(timer1 == null)
            firstTimer = timer;
        else
            timer1.nextTimer = timer;
        timer.expirationTime = l1;
        timer.nextTimer = timer2;
        timer.running = true;
    }
    
    
    VirTimer firstTimer;
    BusRegMem data;
}
