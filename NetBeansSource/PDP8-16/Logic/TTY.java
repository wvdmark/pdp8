/*
 * TTY.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Logic;

import java.io.*;

public class TTY implements Device, Constants {
    
    public static int DevId03 = 03;
    public static int DevId04 = 04;
    
    public BusRegMem data;
    public Devices.Terminal console;
    
    private int punchbyte=0;
    private boolean tlsflag= false;
    private boolean punchflag = false;
    private int readerbyte=0;
    private volatile boolean krbflag = false;
    private boolean readerflag = false;
    private boolean KCCFlag = false;
    private boolean ttyinten = true;
    private boolean terminal = true;
    
    private OutputStream out;
    private Thread output;
    private InputStream in;
    private Thread input;
    
    protected Object lockTSF = new Object();
    protected Object lockKRB = new Object();
    
    /** Creates a new instance of Tty */
    public TTY(BusRegMem data) {
        this.data = data;
        console = new Devices.Terminal(this);
        VirListener vl = new VirListener() {
            public void actionPerformed() {
                System.out.println("Timer");
            }
        };
        
        //new VirTimer(data.virqueue,100000,vl).start();
    }
    
    public void Decode(int devcode, int opcode) {
        if (devcode==3) {
            synchronized (in) {
                switch (opcode) {
                    case KCF: krbflag=false; clearIntReq(); break;
                    case KSF: break;
                    case KCC: data.c0c1c2 = INBUSTOAC; break;
                    case KRS: data.c0c1c2 = INACORBUSTOAC; break;
                    case KIE: ttyinten = ((data.ac&01)>0) ?true: false; break; //interrupt req on AC11
                    case KRB: data.c0c1c2 = INBUSTOAC; break;
                }
            }
        } else if (devcode==4) {
            synchronized (lockTSF) {
                switch (opcode) {
                    case TFL: tlsflag=true; break;
                    case TSF: break;
                    case TCF: data.c0c1c2 = OUTACTOBUS; tlsflag=false; clearIntReq(); break;
                    case TPC: data.c0c1c2 = OUTACTOBUS; break;
                    case TSK: break;
                    case TLS: data.c0c1c2 = OUTACTOBUS; tlsflag=false; clearIntReq(); break;
                }
            }
        }
    }
    
    public void Execute(int devcode, int opcode) {
        if (devcode==3) {
            synchronized (lockKRB) {
                switch (opcode) {
                    case KCF: break;
                    case KSF: data.skipbus= (krbflag)?true: false; break;
                    case KCC: data.data = 0; readerbyte = 0; krbflag=false; clearIntReq(); readerflag = true; KCCFlag = true; break;
                    case KRS: data.data |= readerbyte; break;
                    case KIE:  setIntReq(); break;
                    case KRB: data.data = readerbyte; krbflag=false; clearIntReq(); readerflag=true; break;
                }
            }
        } else if (devcode==4) {
            synchronized (lockTSF) {
                switch (opcode) {
                    case TFL: data.setIntReq(DevId03,ttyinten); break; //also called SPF
                    case TSF: data.skipbus= (tlsflag)?true: false; break;
                    case TCF: break;
                    case TPC: punchbyte = data.data; tlsflag=false; clearIntReq();punchflag = true; lockTSF.notifyAll(); break;
                    case TSK: data.skipbus= ((krbflag | tlsflag) & ttyinten)?true: false;break;
                    case TLS: punchbyte = data.data; tlsflag=false; clearIntReq();punchflag = true; lockTSF.notifyAll(); break;
                }
            }
        }
    }
    
    public void clearIntReq() {
        if (krbflag == false & tlsflag == false) {
            data.setIntReq(DevId03,false);
        }
    }
    
    public void setIntReq() {
        if (krbflag == true | tlsflag == true) {
            data.setIntReq(DevId03,ttyinten);
        }
    }
    
    public void ClearFlags(int devcode) {
        krbflag = false;
        tlsflag = false;
        punchbyte = 0;
        readerbyte = 0;
        data.setIntReq(DevId03,false);
        ttyinten = true;
    }
    
    public void setInputOutputStream(InputStream in, OutputStream out){
        this.in=in;
        this.out=out;
        this.input=new Thread(new ReadInput(),"TTY-Input");
        //this.input.setPriority(Thread.MAX_PRIORITY);
        this.input.start();
        this.output=new Thread(new WriteOutput(),"TTY-Output");
        this.output.start();
    }
    
    public void Interrupt(int command) {
    }
    
    public void setReader(boolean state) {
        byte[] flushy = new byte[1024];
        int len;
        if (state) {
            terminal = false;
        } else {
            try {
                while (in.available()>0) {
                    len = in.read(flushy,0,1024);
                }
            } catch (Exception e) {System.out.println("setReader=>" + e);}
            terminal = true;
        }
        ClearFlags(DevId03);
    }
    
    public void ClearRun(boolean run) {
    }
    
    public void CloseDev(int devcode) {
    }
    
    public void ClearPower(int devcode) {
    }
    
    public class ReadInput implements Runnable {
        public void run() {
            while (true) {
                try {
                    synchronized (lockKRB) {
                        //while (!readerflag) {in.wait();}
                        if ((readerflag | (terminal & !krbflag) ) && in.available()>0) {
                            readerbyte = in.read();
                            if (terminal) readerbyte = readerbyte | 0200;
                            readerflag = false;
                            //if (KCCFlag) Thread.sleep(8);
                            KCCFlag = false;
                            krbflag = true;
                            data.setIntReq(DevId03,ttyinten);
                        }
                    }
                    Thread.sleep(1);
                } catch (Exception e) {System.out.println("ReadInput=>" + e);}
            }
        }
    }
    
    public class WriteOutput implements Runnable {
        public void run() {
            while (true) {
                try {
                    synchronized (lockTSF) {
                        while (!punchflag) {lockTSF.wait();}
                        out.write(punchbyte);
                        out.flush();
                        punchflag = false;
                        lockTSF.wait(8); //Thread.sleep(8);
                        tlsflag = true;
                        data.setIntReq(DevId03,ttyinten);
                    }
                } catch (Exception e) {}
            }
        }
    }
}
