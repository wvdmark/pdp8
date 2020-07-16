/*
 * Proc.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Logic;

//import java.util.*;

public class Proc extends Thread implements Constants {
    
    /** Creates a new instance of Proc */
    public Proc(BusRegMem data, String name) {
        super(name);
        this.data = data; 
        virqueue = data.virqueue;
    }
    
    private boolean[] bit = new boolean[12];
    public BusRegMem data;
    public VirQueue virqueue;
    private long starttime;
    private long pdp8time;
    private long pdp8inst;
    private long avgadj = 0;
    private boolean current;
    private boolean indirect;
    private int lac;
    private int md;
    
    @Override
    public void run() {
        while (true) {
            while (data.run) {
                //Thread.yield();
                if (data.msirdis) {
                    Break();
                } else {
                    if (data.fset==FETCH & data.cpma==05203) {
                       data.run = true;
                    }
                    if (data.intinprog) data.setInterruptOff();
                    //if (data.fset==FETCH) data.ema = data.ifr << 12;
                    if (data.fset==FETCH) {
                        if (data.devices[ProcMemIOTs.DevId20]!=null)
                            data.devices[ProcMemIOTs.DevId20].Interrupt(data.devices[ProcMemIOTs.DevId20].MAPIFR);
                        else data.ema = data.ifr << 12;
                    }
                    data.state = data.fset;
                    switch (data.state) {
                        case FETCH: {Fetch();break;}
                        case DEFER: {Defer();break;}
                        case EXEC:  {Execute();break;}
                    }
                }
                if (data.fset==FETCH & data.halt==true) {
                    data.run=false;
                }
                if (data.singstep==true) data.run=false;

                data.pdp8inst++;
                float k=1;
                for (long i=0;i<avgadj;i++) {
                    k += k;
                }
                if (data.pdp8time-pdp8time>= 10000000) { //1E6 mmsec pdp8 time
                    float elaps = (System.currentTimeMillis()-starttime) * 1000; //mmsecs elapsed
                    int adj = (int) ( (1.0E6 -elaps) / data.looptime / (data.pdp8inst-pdp8inst) ) ;
                    avgadj = (adj)*2 + avgadj; if (avgadj<0 || data.speed==true) avgadj = 0;
                    //System.out.println("el:" + elaps + " dinst:" + (data.pdp8inst-pdp8inst) + " dtime:" + (data.pdp8time-pdp8time) + " adj:" + adj + " aadj:" +avgadj);
                    starttime = System.currentTimeMillis();
                    pdp8time = data.pdp8time; pdp8inst = data.pdp8inst;
                }
                virqueue.postExpiredTimers();
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {System.out.println("Unknown error");}
            starttime = System.currentTimeMillis();
            pdp8time = data.pdp8time; pdp8inst = data.pdp8inst;
            data.ClearAllRun(data.run);
        }
    }
    
    private void Break() {
        if (data.keyctrl) {
            data.ma = data.cpma;
            data.pc = (data.cpma+1)&07777;
        }
        data.mb = (data.bkdctrl) ? (data.data + data.md) & 07777 : data.data ;
        if (data.mddir) data.mem.set(data.ema, data.ma,data.mb);
        else data.md = data.mem.get(data.ema, data.ma);
        if (data.keyctrl) data.run =false;
        if (data.malctrl) {
        } else {
            data.cpma = (data.keyctrl) ? data.pc : 0;
            data.ma = data.cpma;
            data.fset = FETCH;
        }
    }
    
    private void Fetch(){
        data.pdp8time += 12;
        data.skip = false;
        data.ma = data.cpma;
        data.pc = (data.cpma+1)&07777;
        if (data.intena) data.intdelay=true;
        data.data = data.ac;
        lac = data.ac + (data.link << 12);
        data.mddir = false;
        data.md = data.mem.get(data.ema, data.ma);
        data.ir = data.md >> 9;
        data.iopause = true;
        if (data.usermode) {
            if (data.devices[ProcMemIOTs.DevId20]!=null)
                data.devices[ProcMemIOTs.DevId20].Interrupt(data.devices[ProcMemIOTs.DevId20].USERMODE);
        }
        md = data.md;
        indirect = ((md&00400)!=0) ? true: false;
        current = ((md&00200)!=0) ? true: false;
        if (data.devices[ProcMemIOTs.DevId20]!=null)
            data.devices[ProcMemIOTs.DevId20].Interrupt(data.devices[ProcMemIOTs.DevId20].JMPJMS);
        switch (data.ir) {
            case AND:
            case TAD:
            case ISZ:
            case DCA:
            case JMS: {
                data.cpma = md&0177;
                if (current) data.cpma = data.cpma + (data.ma & 07600);
                data.ma = data.cpma;
                if (indirect) data.fset=DEFER; else data.fset=EXEC;
                break;
            }
            case JMP: {
                data.pc = md&0177;
                if (current) data.pc = data.pc + (data.ma & 07600);
                if (indirect) {
                    data.cpma = md&0177;
                    if (current) data.cpma = data.cpma + (data.ma & 07600);
                    data.ma = data.cpma;
                    data.fset=DEFER;
                } else CheckInt();
                break;
            }
            case IOT: {
                if (data.iopause) { //data.iopause = true;
                    int devcode = (md & 00770) >> 3;
                    int opcode = md & 00007;
                    if (data.devices[devcode]!=null) {
                        do {
                            data.data = 0;
                            data.c0c1c2 = OUTACTOBUS;
                            data.devices[devcode].Decode(devcode, opcode);
                            if (data.c0c1c2==OUTACTOBUS | data.c0c1c2==OUTACTOBUSAC0 | data.c0c1c2==INACORBUSTOAC) data.data = data.ac;
                            data.devices[devcode].Execute(devcode, opcode);
                            data.skip=data.skipbus; data.skipbus=false;
                            if (data.c0c1c2==OUTACTOBUSAC0) data.ac = 0;
                            if (data.c0c1c2==INACORBUSTOAC | data.c0c1c2==INBUSTOAC) data.ac = data.data;
                            if (data.c0c1c2==INPCPLUSBUSTOPC) data.pc = (data.pc + data.data) & 07777;
                            if (data.c0c1c2==INBUSTOPC) data.pc = data.data;
                            if (data.busstrobe) data.lastxfer = true;
                            data.busstrobe= false;
                            data.iopause = false;
                        } while (!data.lastxfer);
                    } else {
                        System.out.println("Illegal IOT: " + Integer.toOctalString(md) + " at: " + Integer.toOctalString(data.ema | data.ma) ); //data.run=false;
                    }
                }
                CheckInt();
                break;
            }
            case OPR: {
                if ((md&00400)==0) { //Group 1
                    if ((md&00200)!=0) lac = lac & 010000; //cla
                    if ((md&00100)!=0) lac = lac &  07777; //cll
                    if ((md&00040)!=0) lac = lac ^  07777; //cma
                    if ((md&00020)!=0) lac = lac ^ 010000; //cml
                    if ((md&00001)!=0) lac = (lac + 1) & 017777; //iac, careful overflow to 20000!
                    if ((md&00002)!=0) { //rotate 2
                        if ((md&00004)!=0) { //left
                            if ((md&00010)!=0) { //right (illegal)
                                lac = (lac & 010000) + (data.md & 0177); if (current) lac = lac + (data.ma & 07600);
                            } else lac = ((lac << 2) | (lac >> 11)) & 017777; //rtl
                        } else { //not left
                            if ((md&00010)!=0) { //right
                                lac = ((lac >> 2) | (lac << 11)) & 017777; //rtr
                            } else lac = (lac & 010000) | ((lac >> 6) & 077) | ((lac & 077) << 6); //bsw
                        }
                    } else { //rotate 1
                        if ((md&00004)!=0) { //left
                            if ((md&00010)!=0) { //right (illegal)
                                lac = (lac & 010000) | ((lac & data.md) & 07777);
                            } else lac = ((lac << 1) | (lac >> 12)) & 017777; //ral
                        } else { //not left
                            if ((md&00010)!=0) { //right
                                lac = ((lac >> 1) | (lac << 12)) & 017777; //rar
                            } else {} //nop
                        }
                    }
                    data.data = lac & 07777;
                } else {
                    if ((md&00001)==0) { //group 2
                        if ((md&00100)!=0) if ((lac& 04000)  > 0) data.skip = true; //sma
                        if ((md&00040)!=0) if ((lac& 07777) == 0) data.skip = true; //sza
                        if ((md&00020)!=0) if ((lac&010000)  > 0) data.skip = true; //snl
                        if ((md&00010)!=0) data.skip = !data.skip;                  //reverse (skp)
                        if ((md&00200)!=0) lac = lac & 010000;                      //cla
                        if ((md&00004)!=0) lac = lac | data.swr;                    //osr
                        if ((md&00002)!=0) {
                            data.run = false;   //halt
                        }
                        data.data = lac & 07777;
                    } else { //group 3
                        data.data=0;
                        if (((md&00200)==0) & ((md&00020)==0)) data.data = lac & 07777;         // !cla, !mql
                        if ((md&00100)!=0)                     data.data = data.data | data.mq; // mqa
                        if (((md&00200)==0) & ((md&00020)!=0)) data.mq = lac & 07777;           // !cla, mql
                        if (((md&00200)!=0) & ((md&00020)!=0)) data.mq = 0;                     // cla, mql = cam
                        if ((md&00056)!=0) lac = data.eae.Decode(md);
                        lac = (lac&010000) + data.data;
                    } //end else group 2
                } //end else group 1
                data.ac = lac&07777;
                data.link = (lac & 010000) >>12;
                CheckInt();
            } //end case = opr
        } //end switch
    }
    
    private void Defer(){ 
        data.pdp8time += 12;
        data.ma = data.cpma;
        data.mddir=false;
        data.md = data.mem.get(data.ema, data.ma);
        data.mb = (data.md + 1)&07777;
        if ((data.ma & 07770) == 010) {
            data.pdp8time += 2;
            data.md = data.mb;
            data.mddir=true;
            data.mem.set(data.ema, data.ma, data.md);
        }
        if (data.ir==JMP) {
            data.pc = data.md;
            if (data.devices[ProcMemIOTs.DevId20]!=null)
                data.devices[ProcMemIOTs.DevId20].Interrupt(data.devices[ProcMemIOTs.DevId20].JMPJMS);
            CheckInt();
        } else {
            if (data.devices[ProcMemIOTs.DevId20]!=null)
                data.devices[ProcMemIOTs.DevId20].Interrupt(data.devices[ProcMemIOTs.DevId20].JMPJMS);
            data.cpma = data.md;
            data.ma = data.cpma;
            data.fset = EXEC;
        }
    }
    
    private void Execute(){
        data.pdp8time += 14;
        data.ma = data.cpma;
        data.mddir=false;
        data.md = data.mem.get(data.ema, data.ma);
        data.mb = data.md;
        switch (data.ir) {
            case AND: {
                data.ac = data.ac & data.mb;
                break;
            }
            case TAD: {
                lac = data.ac + (data.link << 12) + data.mb;
                data.ac = lac&07777;
                data.link = (lac & 010000)>>12;
                break;
            }
            case ISZ: {
                data.mb = data.md +1;
                if ((data.mb&010000)>0) {
                    data.skip=true ;
                }
                data.mb = data.mb & 07777;
                data.mddir=true;
                data.mem.set(data.ema, data.ma,data.mb);
                break;
            }
            case DCA: {
                data.data = data.ac;
                data.mb = data.data;
                data.mddir=true;
                data.mem.set(data.ema, data.ma, data.mb);
                data.ac = 0;
                break;
            }
            case JMS: {
                if (data.skip) {
                    data.mb = (data.pc + 1) & 07777;
                    data.skip=false;
                } else data.mb = data.pc;
                data.mddir=true;
                if (data.devices[ProcMemIOTs.DevId20]!=null)
                    data.devices[ProcMemIOTs.DevId20].Interrupt(data.devices[ProcMemIOTs.DevId20].MAPIFR);
                else data.ema = data.ifr << 12;
                data.mem.set(data.ema, data.ma, data.mb);
                data.pc = (data.ma+1) & 07777; //cpma dis, rom add ?
                break;
            }
        }
        CheckInt();
    }
    
    private void CheckInt(){
        if (!data.intinhibit & data.intdelay & (data.intreq>0)) data.intinprog = true;
        if (data.intinprog) {
            data.ir = JMS;
            data.cpma = 0;
            data.ma = data.cpma;
            if (data.devices[ProcMemIOTs.DevId20]!=null)
                data.devices[ProcMemIOTs.DevId20].Interrupt(data.devices[ProcMemIOTs.DevId20].INTINPROG);
            data.fset = EXEC;
        } else {
            data.cpma = data.pc;
            if (data.skip) data.cpma = (data.pc +1)&07777;
            data.ma = data.cpma;
            data.fset = FETCH;
        }
    }
    
    
} //end Class Proc

