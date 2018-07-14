/*
 * PC8E.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Logic;

import java.io.RandomAccessFile;


public class PC8E implements Device, Constants {
    
    public static int DevId01 = 001;
    public static int DevId02 = 002;
    
    public Devices.PtrPtp ptrptp;
    public BusRegMem data;
    private int unit = 0;
    private boolean rdrflag = false;
    private boolean rdrrun = false;
    public boolean fetching = false;
    public boolean sevenbit = false;
    public boolean punhispeed = true;
    public boolean rdrhispeed = true;
    private boolean punflag = false;
    private boolean prpintena = true;
    private int rdrbuf = 0;
    public boolean[] havetape = {false,false};
    public boolean[] sel = {false,false};
    public RandomAccessFile[] tape = {null,null};
    public int[] line = {0,0};
    
    private VirTimer puntim;
    private VirTimer rdrtim;
    private VirTimer stoptim;
    
    
    /** Creates a new instance of PC8E */
    public PC8E(BusRegMem data) {
        this.data = data;
        ptrptp = new Devices.PtrPtp(this);
        ptrptp.setVisible(true);
        
        VirListener punaction = new VirListener() {
            public void actionPerformed() {
                unit=1;
                setPUN(true);
            }
        };
        puntim = new VirTimer(data.virqueue,PUNDEL,punaction);
        puntim.setRepeats(false);
        
        VirListener rdraction = new VirListener() {
            public void actionPerformed() {
                unit=0;
                setRDR(true);
            }
        };
        rdrtim = new VirTimer(data.virqueue,RDRDEL,rdraction);
        rdrtim.setRepeats(false);
        
        VirListener stopaction = new VirListener() {
            public void actionPerformed() {
                unit=0;
                setSTOP(true);
            }
        };
        stoptim = new VirTimer(data.virqueue,RDRSTOP,stopaction);
        stoptim.setRepeats(false);
    }
    
    public void Decode(int devcode, int opcode) {
        if (devcode==1) {
            unit=0;
            switch (opcode) {
                case RPE: break;
                case RSF: break;
                case RRB: data.c0c1c2 = INACORBUSTOAC; break;
                case RFC: break;
                case RBF: data.c0c1c2 = INACORBUSTOAC; break;
            }
        }
        if (devcode==2) {
            unit=1;
            switch (opcode) {
                case PCE: break;
                case PSF: break;
                case PCF: break;
                case PPC: break;
                case PLS: break;
            }
        }
    }
    
    public void Execute(int devcode, int opcode) {
        if (devcode==1) {
            unit = 0;
            switch (opcode) {
                case RPE: prpintena = true; setIntReq(); break;
                case RSF: data.skipbus= (rdrflag)?true: false; break;
                case RRB: data.data |= rdrbuf; setRDR(false); break;
                case RFC: setRDR(false); getChar(); break;
                case RBF: data.data |= rdrbuf; setRDR(false); getChar(); break;
            }
        }
        if (devcode==2) {
            unit = 1;
            switch (opcode) {
                case PCE: prpintena = false; clearIntReq(); break;
                case PSF: data.skipbus= (punflag)?true: false; break;
                case PCF: setPUN(false); break;
                case PPC: punChar(data.data); break;
                case PLS: setPUN(false); punChar(data.data); break;
            }
        }
    }
    
    public void clearIntReq() {
        if (rdrflag == false & punflag == false) {
            data.setIntReq(DevId01,false);
        }
    }
    
    public void setIntReq() {
        if (rdrflag == true | punflag == true) {
            data.setIntReq(DevId01,prpintena);
        }
    }
    
    private void getChar() {
        if (havetape[unit]) {
            if (!fetching) {
                rdrtim.setInitialDelay(RDRINI);
                rdrtim.start();
            }
            rdrrun = true;
        } else {
            rdrtim.stop();
            rdrrun = false;
        }
    }
    
    private void setRDR(boolean set) {
        int speed;
        if (set) {
            if (rdrrun) {
                rdrrun = false;
                rdrbuf = getLine();
                if (rdrbuf>=0) {
                    rdrflag= true;
                    setIntReq();
                    if (rdrhispeed) speed = RDRFAST;
                    else speed = RDRDEL;
                    rdrtim.setInitialDelay(speed);
                    rdrtim.start();
                    fetching = true;
                }
                else rdrbuf = 0377;
            } else {
                rdrtim.stop();
                stoptim.start();
                fetching = true;
            }
        } else {
            rdrflag = false;
            clearIntReq();
        }
    }
    
    private void setSTOP(boolean set) {
        fetching = false;
        if (rdrrun) {
            rdrrun = false;
            rdrbuf = getLine();
            if (rdrbuf>=0) {
                rdrflag= true;
                setIntReq();
            }
            else rdrbuf = 0377;
        }
    }
    
    private void punChar(int data) {
        int punchbyte;
        int speed;
        if (sevenbit) punchbyte = data&0177;
        else punchbyte = data&0377;
        ptrptp.pPunch1.setHole(punchbyte);
        if (setLine(punchbyte)==0) {
            if (punhispeed) speed = PUNFAST; else speed = PUNDEL;
            puntim.setInitialDelay(speed);
            puntim.start();
        }
    }
    
    private void setPUN(boolean set) {
        if (set) {
            punflag = true;
            //line[unit] +=1;
            setIntReq();
        } else {
            puntim.stop();
            punflag = false;
            clearIntReq();
        }
    }
    
    private int getLine() {
        int newbyte = 0;
        if (!havetape[unit] | !sel[unit]) return -1;
        try {
            tape[unit].seek(line[unit]);
            newbyte = tape[unit].read();
            if (newbyte<0) havetape[unit] = false;
            else line[unit] +=1;
        }
        catch(java.io.IOException e){ System.out.println(e);
        }
        return newbyte;
    }
    
    
    private int setLine(int newbyte) {
        if (!havetape[unit] | !sel[unit]) return -1;
        try {
            tape[unit].seek(line[unit]);
            tape[unit].write(newbyte);
            line[unit] +=1;
        }
        catch(java.io.IOException e){ System.out.println(e);
        }
        return 0;
    }
    
    public void ClearFlags(int devcode) {
        rdrtim.stop();
        stoptim.stop();
        rdrflag=false;
        puntim.stop();
        punflag=false;
        fetching = false;
        rdrrun = false;
        data.setIntReq(DevId01,false);
        prpintena = true;
    }
    
    
    public void Interrupt(int command) {
    }
    
    public void ClearRun(boolean run) {
        if (!run) {
        }
    }
    
    public void CloseDev(int devcode) {
        ptrptp.closeTape(0);
        ptrptp.closeTape(1);
    }
    
    public void ClearPower(int devcode) {
    }
    
}
