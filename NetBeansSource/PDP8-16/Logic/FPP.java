/*
 * FPP-8A EMULATOR
 *
 * Created on January 1, 2014, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */


package Logic;
import java.util.Arrays;

public class FPP implements Device, Constants {
    
    public static int DevId55 = 055;
    public static int DevId56 = 056;
    
    public BusRegMem data;

    private boolean FPPFlag = false;
    private boolean FPPRun = false;
    private boolean FPPPaus = false;
    private boolean FPPStep = false;
    private boolean FPPintena = false;
    private boolean FPPdebug = false;
    private boolean FPPmaint = false;
    private boolean FPPmrun = false;
    private int FPPBuf = 0;
    private int FPPPnt = 0;
    private int FPPmpc = 0;
    private static final int[] FPPmcode = {
017000000,
017010000,
017027777,
017030000,
017040001,
017057776,
017060002,
017077773,
017100003,
017117772,
017120014,
017137764,
017142000,
017157751,
017163777,
017177750,
017207705,
013337776, //Ver 50, 0000 for Ver 0
013340000,
013357776,
013367776,
013370000,
013407772,
013417772,
013427764,
013437764,
013447751,
013457751,
017217773,
013147773,
013150000,
013167776,
013170001,
013207776,
013217772,
013220003,
013237772,
013247764,
013250014,
013267764,
013277751,
013302000,
013317751,
013327773,
017220002,
017237773,
017240001,
017250002,
017267705,
017270003,
017300014,
017312000,
017323777,
017337777,
017343777,
017357705,
017367750,
017373771,
017402015,
017410001,
017427775,
017431377,
017440000,
017450000,
017464000,
017470030,
017500110,
017510660,
017522066,
017531200,
017540000,
017551031,
017560000,
017570001,
017605033,
017614002,
017620004,
017630011,
001650022,
017647777,
017657777,
017667777,
001657776,
017670000,
017700000,
017710030,
017720000,
017730030,
017740030,
000010030,
000000000};
    private static long hibit = 0x8000000000000000L;
    private enum Mode {
        DP(2,1,0x0000010000000000L,0xffffff0000000000L),
        FP(3,0,0x0000010000000000L,0xffffff0000000000L),
        EP(6,0,0x0000000000000010L,0xfffffffffffffff0L);
        private int val;
        private int offs;
        private long lobit;
        private long mask;
        private Mode(int val,int offs,long lobit,long mask) {
            this.val = val;
            this.offs = offs;
            this.lobit = lobit;
            this.mask = mask;
        }
        public int val() {
            return this.val;
        }
        public int offs() {
            return this.offs;
        }
        public long lobit() {
            return this.lobit;
        }
        public long mask() {
            return this.mask;
        }
    };
    private enum Stat {
        trapExit(02000),
        hltExit(01000),
        divZero(00400),
        DPOverflow(00200),
        expOverflow(00100),
        expUnderflow(00040),
        FMInstr(00020),
        lockout(00010),
        FPPPaus(00002),
        FPPRun(00001),
        none(00000);
        private int val;
        private Stat(int val) {
            this.val = val;
        }
        public int val() {
            return this.val;
        }
     };
    private Mode mode = Mode.FP;
    private boolean underExit = false;
    private boolean APT4K = false;
    private boolean APTFast = false;
    private boolean lockout = false;
    private boolean trapExit = false;
    private boolean hltExit = false;
    private boolean divZero = false;
    private boolean DPOverflow = false;
    private boolean expOverflow = false;
    private boolean expUnderflow = false;
    private boolean FMInstr = false;
    private boolean APTDump = false;
    private Thread FPPThread;

/*
 *	  0   1   2   3   4   5   6   7   8   9  10  11
 *	+-----------------------------------------------+
 *  0   |   OpAdd   |  BaseReg  |   Index   |    FPC    |
 *	|-----------------------------------------------|
 *  1   |                      FPC                      |
 *	|-----------------------------------------------|
 *  2   |                     Index                     |
 *	|-----------------------------------------------|
 *  3   |                    BaseReg                    |
 *	|-----------------------------------------------|
 *  4   |                     OpAdd                     |
 *	|-----------------------------------------------|
 *  5   |                  FAC Exponent                 |
 *	|-----------------------------------------------|
 *  6   |                   FAC Word1                   |
 *	|-----------------------------------------------|
 *  7   |                   FAC Word2                   |
 *	|-----------------------------------------------|
 *  8   |                   FAC Word3                   |
 *	|-----------------------------------------------|
 *  9   |                   FAC Word4                   |
 *	|-----------------------------------------------|
 *  10  |                   FAC Word5                   |
 *	+-----------------------------------------------+
 *	  0   1   2   3   4   5   6   7   8   9  10  11
 */
    private int APTP;
    private APT a;
    private FAC f;
    private FAC o;
    private FAC t;


    /** Creates a new instance of XXX */
    public FPP(BusRegMem data) {
        this.data = data;
        a = new APT();
        f = new FAC();
        o = new FAC();
        t = new FAC();
        FPPThread=new Thread(new FPProc(),"FPP Thread");
        //FPPThread.setPriority(9);
        FPPThread.start();
    }


    public void Decode(int devcode, int opcode) {
        if (devcode == DevId55) {
            switch (opcode) {
                case FPINT:
                    break;
                case FPICL:
                    break;
                case FPCOM: data.c0c1c2 = OUTACTOBUS;
                    break;
                case FPHLT:
                    break;
                case FPST:  data.c0c1c2 = OUTACTOBUS;
                    break;
                case FPRST: data.c0c1c2 = INBUSTOAC;
                    break;
                case FPIST: data.c0c1c2 = INBUSTOAC;
                    break;
             }
        }
        if (devcode == DevId56) {
            switch (opcode) {
                case FMODE: data.c0c1c2= OUTACTOBUSAC0;
                    break;
                case FMRB: data.c0c1c2 = INBUSTOAC;
                     break;
                case FMRP: data.c0c1c2 = INBUSTOAC;
                     break;
                case FMDO: 
                    break;
                case FPEP: data.c0c1c2 = OUTACTOBUSAC0;
                break;
            }
        }
    }
    
    public void Execute(int devcode, int opcode) {
             if (devcode == DevId55) {
                switch (opcode) {
                    case FFST:
                        FPPmrun = true; FPPPnt = 0;
                        FPPmpc = (077770000&FPPmcode[FPPPnt])>>>12;
                        FPPBuf = 000007777&FPPmcode[FPPPnt];
                        break;
                    case FPINT: data.skipbus = FPPFlag;
                        break;
                    case FPICL: ClearFlags(DevId55);
                        break;
                    case FPCOM: setCommand(data.data);
                        break;
                    case FPHLT: setHalt();
                        break;
                    case FPST:  data.skipbus = setStart(data.data);
                        break;
                    case FPRST: data.data = getStatus();
                        break;
                    case FPIST: data.skipbus = FPPFlag; if (FPPFlag) {
                        data.data = getStatus(); clearStatus(); 
                        FPPFlag= false; FPPmaint = false; clearIntReq();}
                        break;
                 }
            }
            if (devcode == DevId56) {
                switch (opcode) {
                    case FMODE:
                        FPPmaint = true; lockout=true; 
                        FPPBuf = data.data; FPPmpc = 0; FPPPnt = 0;
                        break;
                    case FMRB:
                        data.data = FPPBuf;
                        break;
                    case FMRP:
                        data.data = FPPmpc;
                        break;
                    case FMDO:
                        if (FPPmaint) {
                            FPPPnt += 1;
                            FPPmpc = (077770000&FPPmcode[FPPPnt])>>>12;
                            FPPBuf = 000007777&FPPmcode[FPPPnt];
                            if (FPPPnt==FPPmcode.length-2) {
                                FPPmaint = false;
                                FPPmrun = false;
                                FPPFlag = true;
                            }
                        }
                        break;
                    case FPEP:
                        mode = (!FPPRun & ((data.data & 04000) > 0)) ? Mode.EP :Mode.FP;
                        break;
                }
        }
    }
    
    private void setCommand(int command) {
        if (!FPPRun & !FPPFlag) {
            mode      = ((command & 04000) > 0) ? Mode.DP : Mode.FP;
            underExit = ((command & 02000) > 0);
            APT4K     = ((command & 01000) > 0);
            FPPintena = ((command & 00400) > 0);
            APTFast   = ((command & 00360) == 00360);
            lockout   = ((command & 00010) > 0);
            APTP      =  (command & 00007) << 12;
        }
    }
    
    private void setStatus(Stat stat) {
        switch (stat) {
            case trapExit:      trapExit=true;      break;
            case hltExit:       hltExit=true;       break;
            case divZero:       divZero=true;       break;
            case DPOverflow:    DPOverflow=true;    break;
            case expOverflow:   expOverflow=true;   break;
            case expUnderflow:  expUnderflow=true;  break;
            case FMInstr:       FMInstr=true;       break;
            case lockout:       lockout=true;       break;
        }
     }

    private void clearStatus() {
        trapExit    =false;
        hltExit     =false;
        divZero     =false;
        DPOverflow  =false;
        expOverflow =false;
        expUnderflow=false;
        FMInstr     =false;
    }
    
    private int getCommand() {
        int command = 0;
        command |= (mode==Mode.DP)?04000:0;
        command |= (underExit)    ?02000:0;
        command |= (APT4K)        ?01000:0;
        command |= (FPPintena)    ?00400:0;
        command |= (APTFast)      ?00360:0;
        command |= (lockout)      ?00010:0;
        command |= (APTP>>>12)    &00007;
        return command;
    }
    
    private int getStatus() {
        int statreg = 0;
        statreg |= (mode==Mode.DP)?04000:0;                 //4000
        statreg |= (trapExit)     ?Stat.trapExit.val:0;     //2000
        statreg |= (hltExit)      ?Stat.hltExit.val:0;      //1000
        statreg |= (divZero)      ?Stat.divZero.val:0;      //0400
        statreg |= (DPOverflow)   ?Stat.DPOverflow.val:0;   //0200
        statreg |= (expOverflow)  ?Stat.expOverflow.val:0;  //0100
        statreg |= (expUnderflow) ?Stat.expUnderflow.val:0; //0040
        statreg |= (FMInstr)      ?Stat.FMInstr.val:0;      //0020
        statreg |= (lockout)      ?Stat.lockout.val:0;      //0010
        statreg |= (mode==Mode.EP)?00004:0;                 //0004
        statreg |= (FPPPaus)      ?Stat.FPPPaus.val:0;      //0002
        statreg |= (FPPRun)       ?Stat.FPPRun.val:0;       //0001
        statreg |= (FPPmrun)      ?Stat.FPPRun.val:0;
        
        return statreg;
    }

    private boolean setStart(int aptp) {
        if (FPPRun & FPPPaus) {
            FPPPaus = false;
            return true;
        }
        else if(!FPPRun & !FPPFlag & data.FPPenable) {
            APTP = APTP | (aptp&07777);
            a.load(APTP);
            a.setOpadd(a.getFPC());
            clearStatus();
            FPPRun = true;
            data.FPPRunning = true;
            FPPPaus = false;
            APTDump = true;
            return true;
        }
        else return false;
    }

    private void setHalt() {
        if (FPPRun) {
            //FPPRun = false;
            if (FPPPaus) {
                a.setFPC(a.getFPC()-1);
            }
        } else {
            //FPPRun = true;
            FPPStep = true;
        }
        setStatus(Stat.hltExit);
        Thread.yield();
    }

    public void clearIntReq() {
        if (FPPintena==false) data.setIntReq(DevId55, false);
        if (FPPFlag == false) {
            data.setIntReq(DevId55,false);
        }
    }
    
    public void setIntReq() {
        if (FPPintena==false) data.setIntReq(DevId55, false);
        if (FPPFlag == true) {
            data.setIntReq(DevId55,FPPintena);
        }
    }
    
    public void dump(Stat x) {
        if (APTDump) a.store(APTP);
        setStatus(x);
        FPPRun = false;
        //data.FPPRunning = false;
        FPPFlag = true;
        APTDump = false;
        setIntReq();
    }
    
   public void ClearFlags(int devcode) {
        FPPFlag  = false;
        FPPRun   = false;
        FPPStep  = false;
        data.FPPRunning = false;
        FPPPaus  = false;
        FPPintena= false;
        FPPmaint = false;
        FPPmrun  = false;
        FPPmpc   = 0;
        FPPBuf   = 0; 
        FPPPnt   = 0;
        mode     = Mode.FP;
        clearStatus();
        clearIntReq();
    }
    
    public void Interrupt(int command) {
    }
    
    public void ClearRun(boolean run) {
    }
    
    public void CloseDev(int devcode) {
    }
    
    public void ClearPower(int devcode) {
        mode = Mode.FP;
    }

    public class FPProc implements Runnable {
        Integer addr;
        public void run() {
            while (true) {
                if (!data.run) FPPRun = false; 
                while (data.run & FPPRun & !FPPPaus) {
                    FMInstr = false;
                    int ins = a.getInstr(); 
                    int opc = (ins&07000)>>9;
                    int mod = (ins&00600)>>7;
                    if (opc==6 & mod==0) {opc=16;mod=2;} //LEA IMUL
                    if (opc==7 & mod==0) {opc=17;mod=3;} //LEAI IMULI
                    int ext = (ins&00170)>>3;
                    int xr  = (ins&00070)>>3;
                    int fxy = (ins&00007);
                    int bas = (ins&00177);
                    int inc = (ins&00100)>>6;
                    if (FPPdebug) System.out.printf(" %2s  %04o ",mode,ins);
                    if ((a.getFPC()-1==0014262) & (ins == 006212) & f.reg[1]==04000) {
                        lockout = false;
                    }
                    switch (mod) {
                        case 0:
                            switch (opc) {
                                case 0:
                                    switch (ext) {
                                        case 0: 
                                            fmt3(fxy); break;
                                        case 1: case 2: case 3: case 4:
                                        case 5: 
                                            fmt3x(ext,fxy); break;
                                        case 010: case 011: 
                                            fmt2(opc,ext,fxy); break;
                                        default: if (FPPdebug) System.out.print("EXT " + ext);
                                    } break;
                                case 1:
                                    fmt2(opc,ext,fxy); break;
                                case 2:
                                    addr = a.getInstr() | (fxy<<12);
                                    if (FPPdebug) System.out.printf(" AD %05o ",addr);
                                    if (FPPdebug) System.out.printf("JNX X=%01o:%04o",xr,a.readIndex(xr)+inc);
                                    a.writeIndex(xr, a.readIndex(xr)+inc);
                                    if (a.readIndex(xr)>0) a.setFPC(addr);
                                    break;
                                case 3:
                                case 4:
                                    fmt2(opc,ext,fxy); break;
                                case 5:
                                    if (FPPdebug) System.out.print("LTR");
                                    if (cond(ext)) {
                                        f.m = f.one;
                                        f.e = 1;
                                    } else {
                                        f.m = 0L;
                                        f.e = 0;
                                    }
                                    break;
                                default: System.out.print("OPC " + opc);
                            } break;
                        case 1: //SINGLE-WORD DIRECT
                            addr = a.getBase()+(3*bas);
                            oper(addr,opc,true);
                            break;
                        case 2: //DOUBLE-WORD DIRECT
                            addr = a.getInstr() | (fxy<<12);
                            if (FPPdebug) System.out.printf(" AD %05o ",addr);
                            a.writeIndex(xr, a.readIndex(xr)+inc);
                            if (xr>0) addr = addr + mode.val()*a.readIndex(xr);
                            oper(addr,opc,false);
                            break;
                        case 3: //SINGLE-WORD INDIRECT
                            o.load(a.getBase()+(3*fxy),Mode.FP,false);
                            addr = (int) ((o.m)>>>40)&077777;
                            a.writeIndex(xr, a.readIndex(xr)+inc);
                            if (xr>0) addr = addr + mode.val()*a.readIndex(xr);
                            if (FPPdebug) System.out.print("%");
                            oper(addr,opc,false);
                            break;
                       default: System.out.print("MOD " + mod);
                    }
                    if (FPPStep || FPPmaint) {
                        FPPRun  = false;
                        data.FPPRunning = false;
                        FPPStep = false;
                    }
                }
                if (hltExit) {
                    dump(Stat.hltExit);
                }
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                    System.out.println("FPProc=>" + e);
                }
            }
        }
    }
    
    private void fmt2(int opc,int ext,int fxy) {
        int addr = 0;
        switch (opc) {
            case 0:
                addr = a.getInstr();
                if (FPPdebug) System.out.printf(" AD %05o ",addr);
                switch (ext) {
                    case 010:
                        a.writeIndexOpa(fxy,addr);
                        if (FPPdebug) System.out.printf("LDX X=%01o:%04o",fxy,addr);
                        break;
                    case 011:
                        addr = (addr+a.readIndex(fxy))&07777;
                        a.writeIndexOpa(fxy,addr);
                        if (FPPdebug) System.out.printf("ADX X=%01o:%04o",fxy,addr);
                        break;
                    default: System.out.print("FMT2 " + "OPC="+ opc + " EXT="+ext);
                } break;
            case 1:
                addr = a.getInstr() | (fxy<<12);
                if (FPPdebug) System.out.printf(" AD %05o ",addr);
                switch (ext) {
                    case 0: case 1: case 2: case 3: 
                    case 4: case 5: case 6: case 7:
                        if (FPPdebug) System.out.printf("BR %05o",addr);
                        if (cond(ext)) a.setFPC(addr); break;
                    case 010:
                        if (FPPdebug) System.out.printf("SETX %05o ",addr);
                        a.setIndex(addr);break;
                    case 011: 
                        if (FPPdebug) System.out.printf("SETB %05o ",addr);
                        a.setBase(addr);break;
                    case 012: 
                        if (FPPdebug) System.out.printf("JSA %05o ",addr+2);
                        a.write(addr, 01030+(a.getFPC()>>12));
                        a.write(addr+1, a.getFPC());
                        a.setFPC(addr+2);
                        a.setOpadd(addr+1);
                        break;
                    case 013: 
                        if (FPPdebug) System.out.printf("JSR %05o ",addr);
                        a.write(a.getBase()+1, 01030+(a.getFPC()>>12));//JSR
                        a.write(a.getBase()+2, a.getFPC());
                        a.setFPC(addr);
                        a.setOpadd(addr);
                        break;
                    default: System.out.print("FMT2 " + "OPC="+ opc + " EXT="+ext);
                } break;
            case 3:
            case 4:
                addr = a.getInstr() | (fxy<<12);
                if (FPPdebug) System.out.printf(" AD %05o ",addr);
                if (FPPdebug) System.out.print("TRAP");
                a.setOpadd(addr);
                dump(Stat.trapExit); break;
            default: System.out.print("FMT2 " + "OPC="+opc);
        }
    }

    private void fmt3(int fxy) {
        switch (fxy) {
            case 0:
                if (FPPdebug) System.out.print("FEXIT");
                if (APTDump) dump(Stat.none);
                break;
            case 1:
                if (FPPdebug) System.out.print("FPAUSE");
                FPPPaus = true;
                break;
            case 2:
                if (FPPdebug) System.out.print("FCLA");
                f.m = 0L;
                f.s = 1;
                if (mode!=Mode.DP) f.e = 0;
                break;
            case 3:
                if (FPPdebug) System.out.print("FNEG");
                f.m = -f.m;
                f.s = -f.s;
                break;
            case 4:
                if (FPPdebug) System.out.print("FNORM");
                if (mode!=Mode.DP) f.mnorm();
                break;
            case 5:
                if (FPPdebug) System.out.print("STARTF");
                if (mode==Mode.EP) f.m = f.m&Mode.FP.mask;
                mode = Mode.FP;
                break;
            case 6:
                if (FPPdebug) System.out.print("STARTD");
                if (mode==Mode.EP) f.m = f.m&Mode.DP.mask;
                mode = Mode.DP;
                break;
            case 7:
                if (FPPdebug) System.out.print("JAC");
                a.setFPC((int) (f.m>>40));
                break;
            default:
                System.out.print("FMT3 " + "FXY="+fxy);
        }

    }

    private void fmt3x(int ext,int fxy) {
        int tmp,shift,diff;
        switch (ext) {
            case 1:
                if (fxy!=0) {
                    tmp = a.readIndexOpa(fxy);
                    tmp = (tmp<<20)>>20;
                } else tmp = 027;
                if (FPPdebug) System.out.printf("ALN X=%01o:%04o",fxy,tmp&07777);
                if (mode!=Mode.DP) {
                    diff = tmp - f.e;
                    f.e = tmp;
                } else diff = tmp;
                f.mshift(diff);
                break;
            case 2:
                f.copyTo(t);
                if (mode!=Mode.DP) t.mshift(027-t.e);
                tmp = (int) ((t.m)>>40);
                a.writeIndex(fxy,tmp);
                if (FPPdebug) System.out.printf("ATX X=%01o:%04o",fxy,tmp&07777);
                break;
            case 3:
                tmp = a.readIndexOpa(fxy);
                if (FPPdebug) System.out.printf("XTA X=%01o:%04o",fxy,tmp);
                f.m = ((long)(tmp))<<52;
                f.m = f.m>>12;
                f.e = 027; 
                if (mode!=Mode.DP) f.mnorm();
                break;
            case 4:
                if (FPPdebug) System.out.print("FNOP");
                break;
            case 5:
                if (FPPdebug) System.out.print("STARTE");
                if (mode!=Mode.EP) f.m &= Mode.FP.mask;
                mode=Mode.EP;
                break;
            default:
                System.out.print("FMT3X " + "EXT="+ext + "FXY="+fxy);
        }

    }
    
    private void oper(int addr,int opc, boolean single) {
        boolean sub = false;
        if (mode==Mode.DP) {
            switch (opc) {
            case 0:
                if (FPPdebug) System.out.printf("DP-FLDA %05o",addr);
                o.load(addr,mode,single);
                o.copyTo(f);
                break;
            case 2:
                if (FPPdebug) System.out.printf("DP-FSUB %05o",addr);
                sub = true;
                //no break
            case 1:
                if (FPPdebug & !sub) System.out.printf("DP-FADD %05o",addr);
                o.load(addr,mode,single);
                if (sub) o.m = -o.m;
                f.m = f.m + o.m;
                f.mround();
                break;
            case 3:
                if (FPPdebug) System.out.printf("DP-FDIV %05o",addr);
                o.load(addr,mode,single);
                if (o.m==0L) {dump(Stat.divZero); break;}
                if (o.s<0) o.m = -o.m;
                if (f.s<0) f.m = -f.m;
                f.mdiv(o);
                if ((f.m<<2) == 0L) {
                    f.m=f.m<<1;
                    dump(Stat.DPOverflow);
                    break;
                } else {
                    f.m=f.m<<2;
                    f.mround();
                }
                break;
            case 4:
                if (FPPdebug) System.out.printf("DP-FMUL %05o",addr);
                o.load(addr,mode,single);
                if ((o.m<<1)!=0L) {
                    if (o.s<0) o.m = -o.m;
                    if (f.s<0) f.m = -f.m;
                    f.mmul(o);
                    f.mround();
                } else {
                    dump(Stat.DPOverflow); 
                }
                break;
            case 5:
                if (FPPdebug) System.out.printf("DP-FADDM %05o",addr);
                setStatus(Stat.FMInstr);
                o.load(addr,mode,single);
                t.clear();
                f.copyTo(t);
                t.m = t.m + o.m;
                t.mround();
                t.store(addr,mode,single);
                break;
            case 6:
                if (FPPdebug) System.out.printf("DP-FSTA %05o",addr);
                f.copyTo(o);
                o.store(addr,mode,single);
                break;
            case 7:
                if (FPPdebug) System.out.printf("DP-FMULM %05o",addr);
                setStatus(Stat.FMInstr);
                o.load(addr,mode,single);
                t.clear();
                f.copyTo(t);
                if (t.m==Mode.EP.lobit) {t.m=0L;t.e=0;}
                if ((o.m<<1)!=0L) {
                    if (o.s<0) o.m = -o.m;
                    if (t.s<0) t.m = -t.m;
                    t.mmul(o);
                    t.mround();
                    t.store(addr,mode,single);
                }
                else {
                    f.store(addr,mode,single);
                    dump(Stat.DPOverflow); 
                }
                break;
            case 16:
                if (FPPdebug)System.out.printf("IMUL %05o",addr);
                sub = true;
            case 17:
                if (FPPdebug & !sub) System.out.printf("IMULI %05o",addr);
                o.load(addr,mode,single);
                if (o.s<0) o.m = -o.m;
                if (f.s<0) f.m = -f.m;
                f.m = (f.m>>40) * (o.m>>40);
                f.m=f.m<<40;
                f.mround();
                break;
            default: System.out.print("OPE DP OPC=" + opc);
            }
        } else {
        switch (opc) {
            case 0:
                if (FPPdebug) System.out.printf("FLDA %05o",addr);
                o.load(addr,mode,single);
                o.copyTo(f);
                break;
            case 2:
                sub = true;
                //no break
            case 1:
                if (FPPdebug & sub) System.out.printf("FSUB %05o",addr);
                if (FPPdebug & !sub) System.out.printf("FADD %05o",addr);
                o.load(addr,mode,single);
                if (sub) o.m = -o.m;
                f.madd(o);
                if (f.mnorm()) f.mround();
                if (f.e > 2047) {
                    dump(Stat.expOverflow);
                } else if (f.e < -2048) {
                    if (underExit) dump(Stat.expUnderflow);
                    else f.clear();
                }
                break;
            case 3:
                if (FPPdebug) System.out.printf("FDIV %05o",addr);
                o.load(addr,mode,single);
                if (o.m==0L) {dump(Stat.divZero); break;}
                if (o.s<0) o.m = -o.m;
                o.mnorm();
                if (f.s<0) f.m = -f.m;
                f.mnorm();
                f.mdiv(o);
                if (f.mnorm()) f.mround();
                break;
            case 4:
                if (FPPdebug) System.out.printf("FMUL %05o",addr);
                o.load(addr,mode,single);
                if (o.m==Mode.EP.lobit) {o.m=0L;o.e=0;}
                o.mnorm();
                if (o.s<0) o.m = -o.m;
                f.mnorm();
                if (f.s<0) f.m = -f.m;
                f.mmul(o);
                if (f.mnorm()) f.mround();
                break;
            case 5:
                if (FPPdebug) System.out.printf("FADDM %05o",addr);
                setStatus(Stat.FMInstr);
                o.load(addr,mode,single);
                t.clear();
                f.copyTo(t);
                t.madd(o);
                if (t.mnorm()) t.mround();
                if (t.e > 2047) {
                    dump(Stat.expOverflow);
                } else if (t.e < -2048) {
                    if (underExit) dump(Stat.expUnderflow);
                    else t.clear();
                }
                t.store(addr,mode,single);
                break;
            case 6:
                if (FPPdebug) System.out.printf("FSTA %05o",addr);
                f.copyTo(o);
                o.store(addr,mode,single);
                break;
            case 7:
                if (FPPdebug) System.out.printf("FMULM %05o",addr);
                setStatus(Stat.FMInstr);
                o.load(addr,mode,single);
                t.clear();
                f.copyTo(t);
                if (o.m==Mode.EP.lobit) {o.m=0L;o.e=0;}
                o.mnorm();
                if (o.s<0) o.m = -o.m;
                t.mnorm();
                if (t.s<0) t.m = -t.m;
                t.mmul(o);
                if (t.mnorm()) t.mround();
                t.store(addr,mode,single);
                break;
            case 16:
                if (FPPdebug) System.out.printf("LEA %05o",addr);
                sub = true;
            case 17:
                if (FPPdebug & !sub) System.out.printf("LEAI %05o",addr);
                f.m = ((long) addr)<<40;
                f.e = 0;
                f.s = 1;
                a.setOpadd(addr);
                mode = Mode.DP;
                break;
            default: System.out.print("OPE " + opc);
            }
        }
    }
    
    private boolean cond(int cond) {
        long fm = f.m & (mode.mask);
        switch (cond) {
            case   0: if (FPPdebug) System.out.print(" JEQ"); return (fm==0) ;
            case   1: if (FPPdebug) System.out.print(" JGE"); return (fm>=0) ;
            case   2: if (FPPdebug) System.out.print(" JLE"); return (fm<=0) ;
            case   3: if (FPPdebug) System.out.print(" JA "); return true     ;
            case   4: if (FPPdebug) System.out.print(" JNE"); return (fm!=0) ;
            case   5: if (FPPdebug) System.out.print(" JLT"); return (fm<0)  ;
            case   6: if (FPPdebug) System.out.print(" JGT"); return (fm>0)  ;
            case   7: if (FPPdebug) System.out.print(" JAL"); return (f.e>027);
        }
        return false;
    }
    
class APT {
    private int[] reg;
    public APT () {
    reg = new int[] { 0, //  OpAdd BaseReg Index FPC
                      0, //  FPC
                      0, //  Index
                      0, //  Basereg
                      0};//  Opadd
    }
    public int getInstr() {
        int addr = getFPC();
        int instr = data.mem.getf(addr);
        if (FPPdebug) System.out.printf("%nFPC %05o",addr);
        setFPC(addr+1);
        return instr;
    }
    public int read(int addr) {
        setOpadd(addr);
        return data.mem.getf(addr);
    }
    public void write(int addr, int word) {
        setOpadd(addr);
        data.mem.setf(addr,word);
    }
    public int getFPC() {
        return (reg[1] & 07777) | ((reg[0] & 00007)<<12);
    }
    public void setFPC(int fpc) {
        reg[0] = ((fpc >> 12) & 00007) | (reg[0] & 07770);
        reg[1] = fpc & 07777;
    }
    public int readIndex(int xr) {
        return data.mem.getf(getIndex()+xr);
    }
    public void writeIndex(int xr, int word) {
        data.mem.setf(getIndex()+xr,word);
    }
    public int readIndexOpa(int xr) {
        return read(getIndex()+xr);
    }
    public void writeIndexOpa(int xr, int word) {
        write(getIndex()+xr,word);
    }
    public int getIndex() {
        return (reg[2] & 07777) | ((reg[0] & 00070)<<9);
    }
    public void setIndex(int ind) {
        reg[0] = ((ind >> 9) & 00070) | (reg[0] & 07707);
        reg[2] = ind & 07777;
    }
    public int getBase() {
        return (reg[3] & 07777) | ((reg[0] & 00700)<<6);
    }
    public void setBase(int base) {
        reg[0] = ((base >> 6) & 00700) | (reg[0] & 07077);
        reg[3] = base & 07777;
    }
    public int getOpadd() {
        return (reg[4] & 07777) | ((reg[0] & 07000)<<3);
    }
    public void setOpadd(int opadd) {
        reg[0] = ((opadd >> 3) & 07000) | (reg[0] & 00777);
        reg[4] = opadd & 07777;
    }
    public void load(int addr) {
        if (!APTFast) {
            data.mem.copy_from(reg, 0, addr, reg.length );
            f.load(addr+reg.length,Mode.EP, false);
        } else {
            data.mem.copy_from(reg, 0, addr, 2 );
        }
    }
    public void store(int addr) {
        if (!APTFast) {
            data.mem.copy_to(reg, 0, addr, reg.length );
            f.store(addr+reg.length,Mode.EP, false);
        } else {
            data.mem.copy_to(reg, 0, addr, 2);
        }
    }
}

class FAC {
    private int[] reg;
    public Long m;
    public int e;
    public int s;
    public long one = 0X4000000000000000L;
    
    public FAC () {
    reg=new int[] { 0, //  Fac Exponent
                    0, //  Fac Word1
                    0, //  Fac Word2
                    0, //  Fac Word3
                    0, //  Fac Word4
                    0};//  Fac Word5
    }
    
    public void load(int addr, Mode model, boolean single) {
        a.setOpadd(addr+2-mode.offs());
        Arrays.fill(reg, 0);
        Mode mod = (single & model==Mode.DP)?Mode.FP:model;
        data.mem.copy_from(reg, mod.offs(), addr, mod.val() );
        if (mode!=Mode.DP) e  = (reg[0]<<20)>>20; else e = 0;
        m  = 0L;
        m  |= ((long) reg[1] & 07777)<<52;
        m  |= ((long) reg[2] & 07777)<<40;
        m  |= ((long) reg[3] & 07777)<<28;
        m  |= ((long) reg[4] & 07777)<<16;
        m  |= ((long) reg[5] & 07777)<<04;
        s = (m<0L)?-1:+1;
    }
    
    public void store(int addr, Mode modes, boolean single) {
        a.setOpadd(addr+2);
        save(modes);
        Mode mod = (single & modes==Mode.DP)?Mode.FP:modes;
        data.mem.copy_to(reg, mod.offs(), addr, mod.val() );
    }
    
    public void copyTo(FAC x) {
        save(mode);
        //Arrays.fill(x.reg, 0);
        System.arraycopy( reg, mode.offs(), x.reg, mode.offs(), mode.val() );
        x.e  = ((x.reg[0])<<20)>>20;
        x.m  = 0L;
        x.m  |= ((long) x.reg[1] & 07777)<<52;
        x.m  |= ((long) x.reg[2] & 07777)<<40;
        x.m  |= ((long) x.reg[3] & 07777)<<28;
        x.m  |= ((long) x.reg[4] & 07777)<<16;
        x.m  |= ((long) x.reg[5] & 07777)<<04;
        x.s = (x.m<0L)?-1:+1;
    }
    
    public void clear() {
        Arrays.fill(reg, 0);
        e = 0;
        m = 0L;
        s = 1;
    }

    public void save(Mode mod) {
        if (mod!=Mode.DP) reg[0]  = e&07777; else reg[0] = 0;
        reg[1]  = (int) ((m>>>52) & 07777);
        reg[2]  = (int) ((m>>>40) & 07777);
        reg[3]  = (int) ((m>>>28) & 07777);
        reg[4]  = (int) ((m>>>16) & 07777);
        reg[5]  = (int) ((m>>>4 ) & 07777);
    }
    
    public boolean mnorm() {
        if (m==0) {
            e=0;
            return false;
        }
        while ( ((m ^ (m<<1))&hibit)==0) {
            m = m<<1;
            e = e-1;
        }
        if ((m<<1)==0) {
            m = m>>1;
            e = e+1;
            return false;
        }
        return true;
    }
    
    public void mround() {
        switch (mode) {
            case DP:
                m = m&Mode.DP.mask;
                break;
            case EP:
                m = m&Mode.EP.mask;
                break;
            case FP:
                m = m + ((m&(mode.lobit>>1))<<1);
                m = m&Mode.FP.mask;
                break;
        }
    }
    
    public void mshift(int diff) {
        int shift;
        shift = Math.min(63, Math.abs(diff));
        if (diff>0) m = m>>shift;
        else m = m<<shift;
        m = m&mode.mask;
        if (m==0) e = 0;
    }

    public void madd(FAC x) { //o.e x.e f.e e
        if (x.e>e) {
            mshift(x.e-e);
            e = x.e;
        } else {
            x.mshift(e-x.e);
        }
        m = (x.m>>1) + (m>>1);
        if (m==0L) e = 0;
        else e = e+1;
    }

    public void mmul(FAC x) {
        int i; 
        Long m_tm = 0L;
        Long m_ta = m & (mode.mask);
        Long m_op = x.m & (mode.mask);
        int ie = (mode.val+mode.offs-1)*12+4;
        for (i=0;i<ie;i++) {
            if ( (m_ta & (mode.lobit>>4) )!=0) {
                m_tm = m_tm + m_op;
            }
            m_tm = m_tm>>>1;
            m_ta = m_ta>>>1;
        }
        e = e + x.e+1;
        m = (s*x.s<0)?-m_tm:m_tm;
        s = (m<0L)?-1:+1;
    }
    
    public void mdiv(FAC x) {
        int i = 0; 
        Long m_tm = 0L;
        Long m_ta = m & (mode.mask);
        Long m_op = x.m & (mode.mask);
        m_ta = m_ta - m_op;
        int ie = (mode.val+mode.offs-1)*12-1+4;
        do {
            if (m_ta<0) {
                m_ta = (m_ta<<1) | ((m_tm<0)?1L:0L);
                m_tm = m_tm << 1;
                //m_tm += 0L;
                m_ta = m_ta + m_op;
            } else {
                m_ta = (m_ta<<1) | ((m_tm<0)?1L:0L);
                m_tm = m_tm << 1;
                m_tm = m_tm + (mode.lobit>>4);
                m_ta = m_ta - m_op;
            }
            i++;
        } while (i<ie);
        if (m_ta<0) m_ta = m_ta + m_op;
        m = (x.s*s<0)?-m_tm:m_tm;
        s = (m<0L)?-1:+1;
        e = e - x.e + 1;
    }
    }

}
