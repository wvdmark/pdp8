/*
 * DK8EP Real time clock
 *
 * Created on May 1, 2007, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Logic;


public class DK8EP implements Device, Constants {
    
    public static int DevId13 = 013;
    
    public BusRegMem data;
    
    private VirTimer clktim;
    private int clkreg;
    private int clkbuf;
    private int clkcnt;
    private int clkstat;
    private int clkmode;
    private int clkrate;
    private boolean clkflag;
    private boolean clkinhib;
    private boolean clkinten;
    private boolean clkena;
    
    
    /** Creates a new instance of DK8EP */
    public DK8EP(BusRegMem data) {
        this.data = data;
        
        VirListener clk = new VirListener() {
            public void actionPerformed() {
                setCLK(true);
            }
        };
        clktim = new VirTimer(data.virqueue,10,clk);
        clktim.setRepeats(true);
    }
    
    public void Decode(int devcode, int opcode) {
        switch (opcode) {
            case CLZE: data.c0c1c2 = OUTACTOBUS; break;
            case CLSK: break;
            case CLOE: data.c0c1c2 = OUTACTOBUS; break;
            case CLAB: data.c0c1c2 = OUTACTOBUS; break;
            case CLEN: data.c0c1c2 = INBUSTOAC; break;
            case CLSA: data.c0c1c2 = INBUSTOAC; break;
            case CLBA: data.c0c1c2 = INBUSTOAC; break;
            case CLCA: data.c0c1c2 = INBUSTOAC; break;
        }
    }
    
    public void Execute(int devcode, int opcode) {
        switch (opcode) {
            case CLZE: clkreg &= (~data.data)&07777; setCommand(clkreg);break;
            case CLSK: data.skipbus = clkflag; break;
            case CLOE: clkreg |= (data.data)&07777; setCommand(clkreg); break;
            case CLAB: clkbuf = data.data&07777; clkcnt = clkbuf; break;
            case CLEN: data.data = clkreg; break;
            case CLSA: data.data = getStat(); break;
            case CLBA: data.data = clkbuf; break;
            case CLCA: clkbuf = clkcnt; data.data = clkbuf; break;
        }
    }
    
    private void setCommand(int ena) {
        int delay;
        clkena  = ((ena&04000)!=0)?true:false;
        clkmode =  (ena&03000) >> 9;
        clkrate =  (ena&00700) >> 6;
        clktim.stop();
        switch (clkrate) {
            case 2: delay = 100000; break; //100 Hz
            case 3: delay = 10000; break; //1kHz
            case 4: delay = 1000; break; //10 kHz
            case 5: delay = 100; break; //100 kHz
            case 6: delay = 10; break; //1 Mhz
            default:delay = 0; break;
        }
        if (delay>0) {
            clktim.setInitialDelay(delay);
            clktim.setDelay(delay);
            clktim.start();
        }
        clkinhib= ((ena&00020)!=0)?true:false;
        clkinten= ((ena&00010)!=0)?true:false;
    }
    
    private int getStat() {
        if (clkena) {
            clkstat = (clkflag)?04000:0;
            setClkFlag(false);
        }
        return clkstat;
    }
    
    private void setCLK(boolean set){
        if (!clkinhib) {
            clkcnt++;
            if (clkcnt>4095) {
                switch (clkmode) {
                    case 0: clkcnt = 0; break;
                    case 1: clkcnt = clkbuf; break;
                    case 2: clkcnt = 0; break;
                    case 3: clkcnt = 0; break;
                }
                setClkFlag(true);
            }
        }
    }
    
    public void setClkFlag(boolean on) {
        if (on == true) {
            clkflag = true;
            if (clkena) {
                data.setIntReq(DevId13,clkinten);
            }
        } else {
            clkflag = false;
            data.setIntReq(DevId13,false);
        }
    }
    
    public void ClearFlags(int devcode) {
        clkreg  = 0;
        clkbuf  = 0;
        clkstat = 0;
        clkmode = 0;
        clkrate = 0;
        setClkFlag(false);
        clkinhib= false;
        clkinten= false;
        clkena  = false;
        clktim.stop();
        
    }
    
    public void Interrupt(int command) {
    }
    
    public void ClearRun(boolean run) {
    }
    
    public void CloseDev(int devcode) {
    }
    
    public void ClearPower(int devcode) {
    }
    
}
