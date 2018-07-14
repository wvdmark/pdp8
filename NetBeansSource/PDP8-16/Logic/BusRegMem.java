/*
 * BusRegMem.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Logic;

import java.util.*;
import java.io.*;

public class BusRegMem implements Constants{
    
    /** Creates a new instance of BusRegMem */
    public BusRegMem() {
        String homeDir = System.getProperty("user.home");        
        fprops = new File(homeDir + "/PDP8.properties");
        props = new Properties();
        InputStream prin = null;
        if (fprops.exists()) {
            try {
                prin = new BufferedInputStream(new FileInputStream( fprops.getPath() ));
            }
            catch (FileNotFoundException e) {}
            try {
                props.load(prin);
                prin.close();
            }
            catch (IOException e) {}
        }
        props.put("init", "initial entry");
        
        //memory = new int[4096*8];
        mem = new MemAccess();
        devices = new Device[64];
        virqueue = new Logic.VirQueue(this);
        devices[ProcMemIOTs.DevId00] = new ProcMemIOTs(this);
        eae = new Logic.EAE(this);
        devices[PC8E.DevId01] = new PC8E(this);
        devices[PC8E.DevId02] = devices[PC8E.DevId01];
        devices[TTY.DevId03] = new TTY(this);
        devices[TTY.DevId04] = devices[TTY.DevId03];
        devices[PowerBoot.DevId10] = new PowerBoot(this);
        devices[DK8EP.DevId13] = new DK8EP(this);
        devices[ProcMemIOTs.DevId16] = devices[ProcMemIOTs.DevId00]; //nop KT8A
        devices[ProcMemIOTs.DevId17] = devices[ProcMemIOTs.DevId00]; //nop KT8A
        devices[ProcMemIOTs.DevId20] = devices[ProcMemIOTs.DevId00];
        devices[ProcMemIOTs.DevId21] = devices[ProcMemIOTs.DevId00];
        devices[ProcMemIOTs.DevId22] = devices[ProcMemIOTs.DevId00];
        devices[ProcMemIOTs.DevId23] = devices[ProcMemIOTs.DevId00];
        devices[ProcMemIOTs.DevId24] = devices[ProcMemIOTs.DevId00];
        devices[ProcMemIOTs.DevId25] = devices[ProcMemIOTs.DevId00];
        devices[ProcMemIOTs.DevId26] = devices[ProcMemIOTs.DevId00];
        devices[ProcMemIOTs.DevId27] = devices[ProcMemIOTs.DevId00];
        devices[ProcMemIOTs.DevId30] = devices[ProcMemIOTs.DevId00];
        devices[ProcMemIOTs.DevId31] = devices[ProcMemIOTs.DevId00];
        devices[ProcMemIOTs.DevId32] = devices[ProcMemIOTs.DevId00];
        devices[ProcMemIOTs.DevId33] = devices[ProcMemIOTs.DevId00];
        devices[ProcMemIOTs.DevId34] = devices[ProcMemIOTs.DevId00];
        devices[ProcMemIOTs.DevId35] = devices[ProcMemIOTs.DevId00];
        devices[ProcMemIOTs.DevId36] = devices[ProcMemIOTs.DevId00];
        devices[ProcMemIOTs.DevId37] = devices[ProcMemIOTs.DevId00];
        devices[SI3040.DevId50] = new SI3040(this);
        devices[SI3040.DevId51] = devices[SI3040.DevId50];
        devices[FPP.DevId55] = new FPP(this);
        devices[FPP.DevId56] = devices[FPP.DevId55];
        devices[LPT.DevId57] = new LPT(this);
        devices[LPT.DevId66] = devices[LPT.DevId57];
        devices[TD8E.DevId77] = new TD8E(this);
        
        ma = cpma = pc = 07777;
        
        inimem = new int[02000];
        inimem[00200]=07200;
        inimem[00201]=01211;
        inimem[00202]=00212;
        inimem[00203]=06046;
        inimem[00204]=06041;
        inimem[00205]=05204;
        inimem[00206]=02211;
        inimem[00207]=05200;
        inimem[00210]=05200;
        inimem[00211]=00000;
        inimem[00212]=00177;
        
        inimem[00400]=07200;
        inimem[00401]=01222;
        inimem[00402]=03220;
        inimem[00403]=01223;
        inimem[00404]=03221;
        inimem[00405]=02221;
        inimem[00406]=05205;
        inimem[00407]=02220;
        inimem[00410]=05203;
        inimem[00411]=02224;
        inimem[00412]=01224;
        inimem[00413]=07421;
        inimem[00414]=05200;
        inimem[00420]=00000; //sec counter
        inimem[00421]=00000; //millisec counter
        inimem[00422]=06030; //1000
        inimem[00423]=07371; //millisec value
        inimem[00424]=00000; //total seconds counter
        
        inimem[00600]=06032; //echo routine
        inimem[00601]=06031;
        inimem[00602]=05201;
        inimem[00603]=06036;
        inimem[00604]=06046;
        inimem[00605]=06041;
        inimem[00606]=05205;
        inimem[00607]=07000;
        inimem[00610]=05201;
        inimem[00611]=05201;
        inimem[00612]=00377;

        inimem[01000]=01111;
        inimem[01001]=00001;
        inimem[01002]=00002;
        inimem[01003]=00003;
        inimem[01004]=00004;
        inimem[01005]=00002;
        inimem[01006]=03110;
        inimem[01007]=03755;
        inimem[01010]=02421;
        inimem[01011]=00264;
        inimem[01012]=03016;

        inimem[01020]=01111;
        inimem[01021]=00001;
        inimem[01022]=00002;
        inimem[01023]=00003;
        inimem[01024]=00004;
        inimem[01025]=00002;
        inimem[01026]=04667;
        inimem[01027]=04022;
        inimem[01030]=05356;
        inimem[01031]=07513;
        inimem[01032]=04762;

        inimem[01040]=00001;
        inimem[01041]=02000;
        inimem[01042]=00000;
        inimem[01043]=00000;
        inimem[01044]=00000;
        inimem[01045]=00000;

        mem.copy_to( inimem, 0, 0, 01045);
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR)-28-1970; //-Epoch for same weekdays
        mem.set(010000, 07666,  ((today.get(Calendar.MONTH)+1)<<8) +
                                (today.get(Calendar.DAY_OF_MONTH)<<3) +
                                (year % 8));  //set OS/8 Date
        mem.set(000000, 07777,  (year/8)<<7); //set BIPCCL

        long st = System.currentTimeMillis();
        float k=1;
        long loops = 10000000;
        for (long i=0;i<loops;i++) {
            k += k;
        }
        looptime = (float) (System.currentTimeMillis()-st) * (float) (1000) / (float) (loops);
        System.out.println("PDP8 Emulator. Version: " + VERSION + " on " + System.getProperty("os.name"));
        System.out.println("Computer float add time " + Math.floor(looptime*1E6)/1E3 + " ns");
    } 
    
    //Emulator timing
    public long pdp8time;
    public long pdp8inst;
    public VirQueue virqueue;
    
    //Bus
    public int ma = 0;
    public int ema = 0;
    public int data = 0;
    public int md = 0;
    public int swr = 0;
    public int ir = 0;
    public int state = FETCH; //fetch=1,defer=2,execute=3
    public int fset = FETCH; // we use it also for d and e
    
    public boolean run=false;
    public boolean mddir=false; //true = write
    public boolean msirdis=false;
    public boolean keyctrl=false;
    public boolean bkdctrl=false;
    public boolean malctrl=false;
    public boolean intena=false;
    public boolean intdelay=false;
    public boolean intinprog=false;
    public long    intreq=0;
    public boolean skip=false;
    public boolean sw=false;
    public boolean iopause=false;
    public boolean lastxfer=true;
    public boolean busstrobe=false;
    public boolean skipbus=false;
    public int     c0c1c2=0;
    
    //Processor Registers
    public int ac = 0;
    public int link = 0;
    public int mq = 0;
    public int pc;
    public int cpma;
    public int mb;
    
    //EAE
    public EAE eae;
    public boolean modeb;
    public int sc;
    public int gtf;
    
    //KM8-E --- KT8A
    public int ifr = 0; //Instruction field register
    public int ibr = 0; //Instruction bank register
    public int dfr = 0; //Date field register
    public int dbr = 0; //Date bank register
    public boolean timsha = true;
    public boolean usermode = false;
    public boolean intinhibit = false;
    //Multi8 MMU
    public boolean mmena = false;
    
    //Other internal
    public boolean singstep = false;
    public boolean halt = false;
    
    //Speed
    public float looptime;
    public boolean speed = true;

    //Style if registers like pdp8-E or pdp8-I

    public boolean style = true;
    
    //Devices
    public Device[] devices;
    //public PowerBoot pwb;
    //public VirTimer pwbtim;
    
    //Properties
    public File fprops;
    public Properties props;
    
    //Memory
    public int[] inimem;
    public MemAccess mem;
    
    class MemAccess {

        private int[] memory;

        public MemAccess() {
            memory = new int[4096 * 32];
        }

        public void clear() {
            memory = new int[4096 * 32];
        }
        
        public synchronized int get(int eaddr, int addr) {
            return memory[eaddr | (addr&07777)]&07777;
        }

        public synchronized void set(int eaddr,int addr, int content) {
            memory[eaddr | (addr&07777)] = content&07777;
        }
        public synchronized int getf(int addr) {
            int eaddr = addr&070000;
            return memory[eaddr | (addr&07777)]&07777;
        }

        public synchronized void setf(int addr, int content) {
            int eaddr = addr&070000;
            memory[eaddr | (addr&07777)] = content&07777;
        }
        public synchronized void copy_to(int[] src, int srcPos, int destPos, int length) {
            for (int i=0;i<length;i++) {
                memory[destPos+i]=src[srcPos+i]&07777;
            }
        }
        public synchronized void copy_from(int[] targ, int targPos, int srcPos, int length) {
            for (int i=0;i<length;i++) {
                targ[targPos+i]=memory[srcPos+i]&07777;
            }
        }
    }
    
    public void setProp(String key,String value) {
        props.put(key,value);
    }
    
    public void setProp(String key) {
        props.remove(key);
    }
    
    public String getProp(String key) {
        return (String) props.get(key);
    }
    
    public void setIntReq(int devid, boolean intreq) { //64 bits for all devices
        if (intreq) {
            this.intreq |= 1 << devid;
        } else {
            this.intreq &= ~(1 << devid);
        }
    }
    
    public void setInterruptOff() {
        intena = false;
        intdelay = false;
        intinprog = false;
    }
    
    public void ClearAllFlags() {
        for (int devc=0;devc<=63;devc++) {
            if (devices[devc]!=null) devices[devc].ClearFlags(devc);
        }
        modeb = false; //mode = a
        gtf = 0;
    }
    
    public void ClearAllRun(boolean run) {
        for (int devc=0;devc<=63;devc++) {
            if (devices[devc]!=null) devices[devc].ClearRun(run);
        }
        modeb = false;
        gtf = 0;
    }
    
    public void CloseAllDevs() {
        FileOutputStream prout = null;
        OutputStream out = null;
        for (int devc=0;devc<=63;devc++) {
            if (devices[devc]!=null) devices[devc].CloseDev(devc);
        }
        try {
            String fprname = fprops.getPath();
            prout = new FileOutputStream( fprname );
            out = new BufferedOutputStream(prout);
        }
        catch (FileNotFoundException e) {System.out.println("Exception Occurred " + e.getMessage() );}
        try {
            //OutputStream out = new BufferedOutputStream(prout);
            //new FileOutputStream(fprops.getPath()));
            props.store(out,"PDP8 Properties");
            out.close();
        }
        catch (IOException e) {}
        System.exit(0);
    }
    
    public void ClearAllPower() {
        for (int devc=0;devc<=63;devc++) {
            if (devices[devc]!=null) devices[devc].ClearPower(devc);
        }
            run = false;
            ac = 0;
            link = 0;
            msirdis = false;
            ifr = 0; //Instruction field register
            ibr = 0; //Instruction bank register
            dfr = 0; //Date field register
            dbr = 0; //Date bank register
            usermode = false;
}
}




