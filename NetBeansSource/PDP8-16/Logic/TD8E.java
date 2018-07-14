/*
 * TD8E.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Logic;


import java.io.RandomAccessFile;


public class TD8E implements Device, Constants {
    
    public static int DevId77 = 077;
    
    public Devices.Dectape dectape;
    public BusRegMem data;
    private int[] datreg = new int[4];
    private int mtrreg = 0;
    private boolean timerr = false;
    private boolean wloerr = false;
    private boolean selerr = false;
    private int dly = 0;
    private int unit = 0;
    private boolean go = false;
    private boolean write = false;
    private boolean forward = true;
    private boolean slfflag = false;
    private boolean qlfflag = false;
    private boolean utsflag = false;
    private boolean dtp0 = false;
    private boolean dtp1 = false;
    private int qlfcnt = 0;
    //tu56 signals
    public boolean[] wlo = {true,true};
    public boolean[] sel = {false,false};
    public boolean wtm = false;
    public RandomAccessFile[] tape = {null,null};
    public int[] line = {0,0};
    
    private VirTimer utstim;
    private VirTimer slftim;
    
    
    /** Creates a new instance of TD8E */
    public TD8E(BusRegMem data) {
        this.data = data;
        dectape = new Devices.Dectape(this);
        dectape.setVisible(true);
        
        VirListener uts = new VirListener() {
            public void actionPerformed() {
                //System.out.println("UTS");
                setUTS(true);
            }
        };
        utstim = new VirTimer(data.virqueue,UTSDEL,uts);
        utstim.setRepeats(false);
        
        VirListener slf = new VirListener() {
            public void actionPerformed() {
                setSLF(true);
            }
        };
        slftim = new VirTimer(data.virqueue,SLFDEL,slf);
    }
    
    public void Decode(int devcode, int opcode) {
        switch (opcode) {
            case SDSS: break;
            case SDST: break;
            case SDSQ: break;
            case SDLC: data.c0c1c2 = OUTACTOBUSAC0; break;
            case SDLD: data.c0c1c2 = OUTACTOBUS; break;
            case SDRC: data.c0c1c2 = INBUSTOAC; break;
            case SDRD: data.c0c1c2 = INBUSTOAC; break;
        }
    }
    
    public void Execute(int devcode, int opcode) {
        switch (opcode) {
            case SDSS: data.skipbus= (slfflag)?true: false; break;
            case SDST: data.skipbus= (timerr)?true: false; break;
            case SDSQ: data.skipbus= (qlfflag)?true: false; break;
            case SDLC: setCommand(data.data); break;
            case SDLD: checkTime("SDLD"); setData(data.data); setSLF(false); break;
            case SDRC: checkTime("SDRC"); data.data = getCommand(); setSLF(false);break;
            case SDRD: checkTime("SDRD"); data.data = getData(); setSLF(false); break;
        }
    }
    
    private void setCommand(int xdata) {
        int comreg;
        comreg = xdata&07400;
        dly = (getCommand()&07000) ^ (comreg&07000);
        unit    =  (comreg&04000)>>11;
        forward = ((comreg&02000)!=0)?false:true;
        go      = ((comreg&01000)!=0)?true:false;
        write   = ((comreg&00400)!=0)?true:false;
        if (wtm) {
            setUTS(true);
        } else if (dly>0) {
            setUTS(false);
            if (go) utstim.restart();
            mtrreg = 0;
        }
        setTimerr(false);
        selerr = (!sel[unit]); // | (sel[0])&(sel[1]); -> unit are fixed 0,1
        wloerr = wlo[unit];
        if (selerr | wloerr) write = false; //turn off write enable
        dectape.selLamp(unit);
    }
    
    private int getCommand() {
        int comreg;
        comreg = unit<<11;
        comreg |= (forward?0:1)<<10;
        comreg |= (go?1:0)<<9;
        comreg |= (write?1:0)<<8;
        comreg |= (wloerr?1:0)<<7;
        comreg |= ((selerr|timerr)?1:0)<<6;
        comreg |= mtrreg&077;
        return comreg;
    }
    
    public void setWtm(boolean set){
        if (set) {
            wtm=true;
            setUTS(true);
        } else {
            wtm=false;
            setUTS(false);
        }
    }
    
    private void setUTS(boolean set){
        if (set) {
            mtrreg = 0;
            utsflag = true;
            //if (forward) line[unit] -= 500; else line[unit] += 500;
            slftim.setRepeats(true);
            slftim.start();
            dtp0 = false;
        } else {
            utstim.stop();
            utsflag = false;
            mtrreg = 0;
            slftim.setRepeats(false);
            slftim.stop();
            setSLF(false);
        }
    }
    
    private void setSLF(boolean set) {
        int nextline;
        if (set) {
            if (dtp0) {
                dtp0 = false;
                dtp1 = true;
            } else {
                dtp0 = true;
                dtp1 = false;
            }
            if ((qlfflag & ((dtp1 & !write) | (dtp0 & write)))) setTimerr(true);
            if (dtp1) {
                slfflag = !slfflag;
                qlfcnt += 1;
                if (qlfcnt == 4) {
                    qlfflag = true;
                    qlfcnt = 0;
                }
                if (wtm) {
                    if (write) {
                        setMark((datreg[0]<<1)&010);
                        shiftData(0);
                        if (forward) line[unit] += 1; else line[unit] -= 1;
                    }
                } else {
                    nextline = getLine();
                    if (nextline>=0) {
                        shiftMark(nextline);
                        if (write) {
                            setLine(datreg[0]&07);
                            shiftData(0);
                        } else {
                            shiftData(nextline);
                        }
                        if (forward) line[unit] += 1; else line[unit] -= 1;
                    } else {
                        mtrreg = 022;//end-zone: kludge to give error in os/8
                        setUTS(false);
                        line[unit] = FIRST_LINE;
                    }
                }
                //if ((line[unit]%100000==0) & (line[unit]!=0)) System.out.println("Line="+line[unit]);
            } else {
            }
        } else {
            slfflag = false;
            qlfflag = false;
            qlfcnt = 0;
        }
    }
    
    private void checkTime(String cmd) {
        if ((dT()<0) & dtp0 & write) {
            System.out.println(cmd + " dtp0err " + dT() + String.format(": %05o", data.pc));
            setTimerr(true);
        }
        if ((dT()<0) & dtp1 & !write) {
            System.out.println(cmd + " dtp1err " + dT() + String.format(": %05o", data.pc));
            setTimerr(true);
        }
    }

    private int dT() {
        if (slftim.isRunning())
        return (int) (slftim.expirationTime - data.pdp8time);
        else return 0;
    }

    private void setTimerr(boolean set) {
        if (set) {
            timerr = true;
            write = false;
        } else {
            timerr = false;
        }
    }
    
    private void setData(int data) {
        datreg[0] = (data&07000) >> 9;
        datreg[1] = (data&00700) >> 6;
        datreg[2] = (data&00070) >> 3;
        datreg[3] = (data&00007);
    }
    
    private int getData() {
        return (datreg[0]<<9) | (datreg[1]<<6) | (datreg[2]<<3) | (datreg[3]);
    }
    
    private void shiftData(int nextline) {
        datreg[0] = datreg[1];
        datreg[1] = datreg[2];
        datreg[2] = datreg[3];
        if (!write) datreg[3] = nextline&07; else datreg[3] = 0;
    }
    
    private void shiftMark(int nextline) {
        if (utsflag) {
            mtrreg = 077 & ((mtrreg<<1) | ((nextline>>3)&01));
        }
    }
    
    private int getLine() {
        int nextline =0;
        int newbyte;
        if (tape[unit]==null | !sel[unit] | line[unit]<0 | line[unit]>TAPE_LINES) return -1;
        try {
            tape[unit].seek(line[unit]/2);
            newbyte = tape[unit].read();
            if (newbyte<0) newbyte = 0;
            nextline = (line[unit]%2==0)?(newbyte>>4)&0x0f:newbyte&0x0f;
            if (!forward) nextline = (~nextline)&0x0f;
        }
        catch(java.io.IOException e){ System.out.println("getLine: " + e);
        }
        return nextline;
    }
    
    private void setMark(int nextline) {
        int oldbyte;
        int newbyte;
        if (tape[unit]==null | !sel[unit]) return;
        try {
            tape[unit].seek(line[unit]/2);
            oldbyte = tape[unit].read();
            if (oldbyte<0) oldbyte = 0;
            if (line[unit]%2==0) newbyte = (oldbyte&0x08) | ((nextline<<4)&0x80);
            else newbyte = (oldbyte&0x80) | (nextline&0x08);
            tape[unit].seek(line[unit]/2);
            tape[unit].write(newbyte);
        }
        catch(java.io.IOException e){ System.out.println("setMark: " + e);
        }
    }
    
    private void setLine(int nextline) {
        int oldbyte;
        int newbyte;
        if (tape[unit]==null | !sel[unit]) return;
        if (!forward) nextline = (~nextline)&0x07;
        try {
            tape[unit].seek(line[unit]/2);
            oldbyte = tape[unit].read();
            if (oldbyte<0) oldbyte = 0;
            if (line[unit]%2==0) newbyte = (oldbyte&0x8f) | (nextline<<4);
            else newbyte = (oldbyte&0xf8) | nextline;
            tape[unit].seek(line[unit]/2);
            tape[unit].write(newbyte);
        }
        catch(java.io.IOException e){ System.out.println("setLine: " + e);
        }
    }
    
    public void ClearFlags(int devcode) {
        data.setIntReq(DevId77,false);
        if (!wtm) setUTS(false);
        setTimerr(false);
        unit = 0;
        forward = true;
        go = false;
        write = false;
        dly = 0;
    }
    
    
    public void Interrupt(int command) {
    }
    
    public void ClearRun(boolean run) {
        if (!run) {
            go = false;
            write = false;
        }
    }
    
    public void CloseDev(int devcode) {
        dectape.closeTape(0);
        dectape.closeTape(1);
    }
    
    public void ClearPower(int devcode) {
    }
    
}
