/*
 * SI3040.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Logic;


import java.awt.Color;
import java.io.RandomAccessFile;


public class SI3040 implements Device, Constants {
    
    public static int DevId50 = 050;
    public static int DevId51 = 051;
    
    public Devices.Disk4043 disk4043;
    public BusRegMem data;
    /*
Control Register:
The Control Register (12 bits) contains the busy and done flags to indicate the current status
of a controller operation, the disk unit select bits used to select one of the four ports for
a data transfer, the extended memory address field for DMA transfers, the interrupt and format
enable bits, and a read disk bit to indicate the direction of the DMA transfer taking place.
The specific bit assignments are as follows:
     
BIT		FUNCTION
     
0,1,2		Extended Memory Address: Contains the three most significant bits of the
                computer memory address for DMA transfers.
3,4 		Unused: Will always read as zeros.
5		Interrupt Enable: When set, this bit will enable the controller to generate an
                interrupt request when the done bit gets set.
6		Format Enable: When set, this bit will allow a sector to be formatted.
     
7,8		Disk Unit Select: A two bit field selecting one of four ports for a data
                transfer.
9		Read Disk Enable: This bit, which is automatically controlled by the
                controller, is used to indicate the direction of a data transfer. When set the
                controller is reading from the disk and when clear, the controller is writing
                onto the disk.
10		Busy Flag: Indicates the controller is busy trans-ferring data to or from the
                selected disk.
11		Done Flag: When set, this bit indicates that either
                (1) a data transfer operation has been completed or
                (2) an independent seek operation has been completed while there is no data
                transfer active to any of the other three ports.
     */
    private int ctrlreg;
    private int dema = 0;
    private boolean intena = false;
    private boolean frmena = false;
    private int unit = 0;
    private boolean rw = false;
    private boolean busyflag = false;
    private boolean doneflag = false;
    public int siunit = SIUNIT0;  //allow booting SI3040 on unit 1
    /*
Status Register:
The Status Register (12 bits) includes seven error-indicator bits and two disk-operation bits.
A total of nine errors are sensed by the controller. They have been divided into three equal
groups so that a two-bit field may describe which of the three errors has occurred or that none
of the three has occurred. The complete bit assignments are as follows:
     
BIT		FUNCTION
     
0		Error Flag. Indicates that an error occurred.
1,2	00	No Format, Select, or Timing Errors.
        01	Format Error. Indicates an attempt to format a disk without having the format
                switch in the FORMAT position, or to format a fixed-head disk.
        10	Select Error. Disk command assigned to a port that either has no disk or has a
                disk which is not condition to operate.
        11	Timing Error. Computer did not respond in time to receive or supply a data word
                from/to the Controller.
3,4	00	No Logical Address Interlock, Address Verification or Seek Incomplete Errors.
        01	Logical Address Interlock. Indicates that the track address given to the
                Controller is greater than the maximum track value for the disk selected.
        10	Address Verification Error: Indicates that the Controller has detected a
                difference between the Track Address Word in the sector it is attempting to
                read or write and the track address it expects to find there. The Controller
                initiates an automatic restore on that disk.
        11	Seek Incomplete Error: Indicates a hardware malfunction in the disk. The
                Controller initiates an automatic restore on that disk.
5,6	00	No Write Lock Out, Write Check or Cyclic Redundancy Check Errors.
        01	Write Lock Out Error: Indicates an error occurred when the program attempted to
                write onto a write-protected sector. The format switch can override the protect
                capability in the moving-head disk.
        10	Write Check Error: Indicates a malfunction in the disk that makes it incapable
                of writing data.
        11	Cyclic Redundancy Check Error: Indicates that an error was detected in the
                cyclic redundancy check word when reading back the data from the disk.
7,8,9		Unused: Will always read as zeros.
10		Busy Flag: Same bit as in the Control Register.
11		Done Flag: Same bit as in the Control Register.
     */
    private int statreg;
    private boolean errorflag = false;
    private boolean selerr = false;
    private boolean frmerr = false;
    private boolean adrerr = false;
    private boolean logerr = false;
    private boolean wloerr = false;
    //private boolean busyflag = false;
    //private boolean doneflag = false;
    /*
Seek Status Register:
The Seek Status Register, used to hold the status and results of all seek operations, is
composed of four 3-bit registers, each of which is associated with a disk port. In each 3-bit
register, two bits are used to indicate the results of the last seek on the port while the
third bit is a dynamic indication of whether the port is ready to receive a seek command. This
register may only be read back to the accumulator, while the two bits used to indicate the seek
results are automatically cleared upon a seek being initiated on that port. Those ports not
having disks connected to them will have their 3 bit register read back as a 7(8). The complete
bit assignments are as follows:
     
BIT		FUNCTION
     
0-2		Status register corresponding to disk port 3.
3-5		Status register corresponding to disk port 2.
6-8		Status register corresponding to disk port 1.
9-11		Status register corresponding to disk port 0.
     
0,3,6,9		Indicates that a Hardware Seek Error has occurred. This can occur from either
                seeking to a non-existent cyclinder (i.e., a Logical Address Interlock Error)
                or if the disk was unsuccessful in seeking to a legal cyclinder number
                (i.e. a Seek Incomplete Error). If a Seek Incomplete does occur, the controller
                will automatically generate a restore command to the disk.
1,4,7,10	Indicates that a Busy Error has occurred. This can occur from initiating a seek
                on a port when it was either active doing a data transfer or was not in a
                condition to receive a seek operation. In either case, the seek command is
                ignored when this error occurs.
2,5,8,11	A dynamic indicator for whether the port is in a condition to receive a seek
                command. A zero indicates it is ready , while a one indicates that either the
                head is presently moving or that the drive is not ready.
     */
    private int seekstatreg;
    //private boolean[] seekerr = {false,true,true,true};
    //private boolean[] busyerr = {false,true,true,true};
    //private boolean[] busy = {false,true,true,true};
    private boolean[] seekerr = {false,false,false,false};
    private boolean[] busyerr = {false,false,false,false};
    private boolean[] busy = {false,false,false,false};
    /*
Seek Address Register:
The Seek Address Register is a 12-bit register used to temporarily hold the port and cylinder
address while an overlap seek is initiated. It is also used as a communication path for those
program I/O instructions entering the Track Address Register. The Seek Address Register may not
be read back into the accumulator, and is always free to receive programmed information without
any timing restrictions. The specific bit assignments are as follows:
     
     
BIT		FUNCTION
     
     
0,1		A two-bit field used to select the port on which the overlap seek operation is
                to be initiated.
2-11		The track address to which the head will move when overlap seek is executed.
                This track address is composed of a cylinder address (bits 2-10) and a head
                select bit (bit 11) for moveable-head disks and is a straight track address for
                fixed-head disks.
     */
    private int seekreg;
    private int seekunit;
    /*
Track Address Register:
The Track Address Register is a 12-bit register used to hold the track address for read and
write operations. It is entered with the starting track address when the read or write
operation is initiated, and is automatically updated when data transfers require going across
track boundaries. The specific bit assignments are as follows:
     
BIT		FUNCTION
0		Unused: Will always be read as a zero.
1		Disk Select: A zero will select the non-removable disk and a one will select
                the removable disk for a moveable-head disk. This bit should be a zero for the
                fixed-head disk.
2-11		The track address to or from which the controller will transfer data. This
                track address is composed of a cylinder address (bits 2-10) and a head select
                bit (bit 11) for moveable disks and a straight track address for fixed-head
                disks.
     */
    private int trackreg;
    private int removable = 0;
    /*
Sector Address Register:
The Sector Address Register is used to hold the sector address for read and write operations.
It is a 4-bit register which is loaded with the starting sector address prior to the initiation
of the read or write operation, and is automatically updated for data transfers which require
going across sector boundaries.
     
BIT		FUNCTION
0-7		Unused: Will always be read as zeros.
8-11		Sector Address: A four-bit field which provides 16 sectors per track.
     
The 4-bit Sector Address Register should be viewed as the least significant part of continuous
disk address, where the Track Address Register is the most significant part. Thus, when the
automatic updating increments the Sector Address Register back to zero, it will also increment
the least significant (head select) bit of the Track Address Register. With this addressing
scheme, only multi-sector transfers going across cylinder boundaries will loose a revolution
while the head moves.
     */
    private int sectreg;
    private int wcreg;
    private int addrreg;
    private int diskaddr;
    private int sectcntreg;
    private boolean pseudobusy;
    
    public boolean fmton = false;
    private boolean wlock = false;
    
    public boolean[][] sel = {{false,false},{false,false},{false,false},{false,false}};
    public RandomAccessFile[][] disk = {{null,null},{null,null},{null,null},{null,null}};
    public int[] track = {0,0,0,0};
    
    private VirTimer puntim;
    private VirTimer rdrtim;
    private VirTimer stoptim;
    
    /** Creates a new instance of SI3040 */
    public SI3040(BusRegMem data) {
        this.data = data;
        disk4043 = new Devices.Disk4043(this);
        disk4043.setVisible(true);
        
        VirListener punaction = new VirListener() {
            public void actionPerformed() {
                //setPUN(true);
            }
        };
        puntim = new VirTimer(data.virqueue,PUNDEL,punaction);
        puntim.setRepeats(false);
        
        VirListener rdraction = new VirListener() {
            public void actionPerformed() {
                //setRDR(true);
            }
        };
        rdrtim = new VirTimer(data.virqueue,RDRDEL,rdraction);
        rdrtim.setRepeats(false);
        
        VirListener stopaction = new VirListener() {
            public void actionPerformed() {
                //setSTOP(true);
            }
        };
        stoptim = new VirTimer(data.virqueue,RDRSTOP,stopaction);
        stoptim.setRepeats(false);
    }
    
    public void Decode(int devcode, int opcode) {
        if (devcode==DevId50) {
            switch (opcode) {
                case DSDD: break;
                case DLCR: data.c0c1c2 = OUTACTOBUS; break;
                case DRCR: data.c0c1c2 = INBUSTOAC; break;
                case DCSR: break;
                case DRSR: data.c0c1c2 = INBUSTOAC; break;
                case DLSS: data.c0c1c2 = OUTACTOBUS; break;
                case DRSS: data.c0c1c2 = INBUSTOAC; break;
            }
        }
        if (devcode==DevId51) {
            switch (opcode) {
                case DSDE: break;
                case DLSR: data.c0c1c2 = OUTACTOBUS; break;
                case DSRR: data.c0c1c2 = INBUSTOAC; break;
                case DLTR: data.c0c1c2 = OUTACTOBUS; break;
                case DLTW: data.c0c1c2 = OUTACTOBUS; break;
                case DRTR: data.c0c1c2 = INBUSTOAC; break;
                case DWCA: data.c0c1c2 = OUTACTOBUS; break;
            }
        }
    }
    
    public void Execute(int devcode, int opcode) {
        int temp = data.data;
        if (devcode==DevId50) {
            switch (opcode) {
                case DSDD: data.skipbus = doneflag; break;
                case DLCR: ctrlreg = data.data; setCtrl(); break;
                case DRCR: getCtrl(); data.data = ctrlreg; break;
                case DCSR: statreg = 0; setStat(); break;
                case DRSR: getStat(); data.data = statreg; break;
                case DLSS: seekreg = data.data; goSeek(); break;
                case DRSS: getSeekStat(); data.data = seekstatreg; break;
            }
        }
        if (devcode==DevId51) {
            switch (opcode) {
                case DSDE: data.skipbus = errorflag; break;
                case DLSR: sectreg = data.data & 017; break;
                case DSRR: data.data = sectreg; break;
                case DLTR: trackreg = data.data; goRead(); break;
                case DLTW: trackreg = data.data; goWrite(); break;
                case DRTR: data.data = trackreg; break;
                case DWCA: seekreg = data.data; setWcAdr(); break;
            }
        }
        //if (opcode!=1) System.out.println(devcode + ":" + opcode + "<" + Integer.toOctalString(temp) + ">" + Integer.toOctalString(data.data));
    }
    
    public void clearIntReq() {
        if (doneflag == false ) {
            data.setIntReq(DevId50,false);
        }
    }
    
    public void setIntReq() {
        if (doneflag == true) data.setIntReq(DevId50,intena);
        else data.setIntReq(DevId50, false);
    }
    
    private void setCtrl() {
        dema = (ctrlreg & 07000) << 3;
        if ((ctrlreg & 00100) > 0) intena = true; else intena = false;
        if ((ctrlreg & 00040) > 0) {
            frmena = true;
        } else frmena = false;
        unit = ((ctrlreg & 00030) >> 3) + siunit;
        if ((ctrlreg & 00004) > 0) rw = true; else rw = false;
        if ((ctrlreg & 00002) > 0) busyflag = true; else busyflag = false;
        if ((ctrlreg & 00001) > 0) doneflag = true; else doneflag = false;
        setIntReq();
        if (siunit==1) disk4043.diskUnit0.setBackground(new java.awt.Color(00, 00, 00));
        else disk4043.diskUnit0.setBackground(new java.awt.Color(100, 100, 100));
    }
    
    private void getCtrl() {
        ctrlreg = dema >> 3;
        if (intena) ctrlreg |= 00100;
        if (frmena) ctrlreg |= 00040;
        ctrlreg |= ( ( (unit>0)?unit-siunit:unit) << 3) & 00030;
        if (rw) ctrlreg |= 00004;
        if (busyflag) ctrlreg |= 00002;
        if (doneflag) ctrlreg |= 00001;
    }
    
    private void setStat() {
        if ((statreg & 04000) > 0) errorflag = true; else errorflag = false;
        if ((statreg & 02000) > 0) selerr = true; else selerr = false;
        if ((statreg & 01000) > 0) frmerr = true; else frmerr = false;
        if ((statreg & 00400) > 0) adrerr = true; else adrerr = false;
        if ((statreg & 00200) > 0) logerr = true; else logerr = false;
        if ((statreg & 00040) > 0) wloerr = true; else wloerr = false;
        if ((statreg & 00002) > 0) busyflag = true; else busyflag = false;
        if ((statreg & 00001) > 0) doneflag = true; else doneflag = false;
        setIntReq();
    }
    
    private void getStat() {
        statreg = 0;
        if (errorflag) statreg |= 04000;
        if (selerr)    statreg |= 02000;
        if (frmerr)    statreg |= 01000;
        if (adrerr)    statreg |= 00400;
        if (logerr)    statreg |= 00200;
        if (wloerr)    statreg |= 00040;
        if (busyflag)  statreg |= 00002;
        if (doneflag)  statreg |= 00001;
    }
    
    private void getSeekStat() {
        seekstatreg = 0;
        for (int i=0;i<4;i++) {
            if (seekerr[i]) seekstatreg |= 04 << i*3;
            if (busyerr[i]) seekstatreg |= 02 << i*3;
            if (busy[i]) seekstatreg |= 01 << i*3;
        }
    }
    
    private void goSeek() {
        seekunit = (seekreg&06000) >> 10;
        seekreg = seekreg&01777;
        if (sel[seekunit][1] | sel[seekunit][0]) {
            seekerr[seekunit] = false;
            busyerr[seekunit] = false;
        }
        setBlock(false,seekunit,seekreg);
    }
    
    private void goRead() {
        pseudobusy=false;
        removable = (trackreg&02000) >> 10;
        do {
            setBlock(true,unit,trackreg&01777);
            if (readSect()<0) break;
        } while (wcreg>0);
    }
    
    private void goWrite() {
        pseudobusy=false;
        removable = (trackreg&02000) >> 10;
        do {
            setBlock(true,unit,trackreg&01777);
            if (writeSect()<0) break;
        } while (wcreg>0);
    }
    
    private void setWcAdr() {
        trackreg = seekreg;
        addrreg = trackreg;
        wcreg = data.mem.get(dema, addrreg);
        addrreg = data.mem.get(dema, addrreg+1);
        pseudobusy = true;
    }
    
    private void setBlock(boolean rdwr,int aunit,int block) {
        int taw;
        if (sel[aunit][1] | sel[aunit][0]) {
            doneflag=true;
        } else {
            selerr = true;
            errorflag=true;
            return;
        }
        if (block==0) {
            seekerr[aunit]=false;
            busyerr[aunit]=false;
        }
        if (block>0627) {
            logerr = true;
            errorflag=true;
            seekerr[aunit] = true;
            busyflag = false;
            return;
        }
        if ( pseudobusy ) {
            doneflag=false;
            if ((aunit==unit) | busy[aunit]) {
                busyerr[aunit] = true;
                busyflag = true;
            }
        }
        diskaddr = (block << 4) | sectreg;
        track[aunit] = block;
        if (rdwr & !frmena & sel[aunit][removable]) {
            try {
                disk[aunit][removable].seek(30+diskaddr*(384+33));
                taw = disk[unit][removable].readUnsignedShort() >> 4;
                //if (block != (taw & 03777) ) {
                if (block != (taw & 01777) ) { //allow mounting fixed on removable
                    adrerr = true;
                    errorflag=true;
                }
                if ((taw & 04000) >0) wlock = true; else wlock = false;
                
            }
            catch(java.io.IOException e){ System.out.println(e);
            }
        }
    }
    
    private int readSect() {
        int byte1,byte2,byte3;
        int cnt = 256;
        int offs = 33;
        if (!sel[unit][removable]) {
            selerr = true;
            errorflag=true;
            return -1;
        }
        if (wcreg==0) cnt=0;
        try {
            disk[unit][removable].seek(offs+diskaddr*(384+33));
            while ( (wcreg>0) & (cnt>0) ) {
                byte1 = disk[unit][removable].read();
                byte2 = disk[unit][removable].read();
                byte3 = disk[unit][removable].read();
                if (byte1<0 | byte2<0 | byte3<0) return -1;
                //data.mem.setp(addrreg, (byte1 << 4) );
                //data.memory[ema | addrreg] = byte1 << 4;
                data.mem.set(dema, addrreg, (byte1 << 4) | (byte2 >> 4) );
                //data.memory[ema | addrreg] = (byte1 << 4) | (byte2 >> 4);
                addrreg++;
                wcreg--; cnt--;
                if (wcreg>0) {
                    //data.mem.set(ema, addrreg, (byte2 & 017) << 8);
                    //data.memory[ema | addrreg] = (byte2 & 017) << 8;
                    data.mem.set(dema, addrreg, ((byte2 & 017) << 8) | byte3 );
                    //data.memory[ema | addrreg] |= byte3;
                    addrreg++;
                    wcreg--; cnt--;
                }
            }
        }
        catch(java.io.IOException e){ System.out.println(e);
        }
        diskaddr++;
        sectreg = diskaddr & 017;
        trackreg = (diskaddr >> 4) | (removable << 10);
        track[unit] = trackreg & 01777;
        return 0;
    }
    
    private int writeSect() {
        int byte1,byte2,byte3 = 0;
        int cnt = 256;
        int offs = 33;
        if (!sel[unit][removable]) {
            selerr = true;
            errorflag=true;
            return -1;
        }
        if (frmena) {
            if (fmton) {
                offs = 0;
            } else {
                frmerr = true;
                errorflag=true;
                return -1;
            }
        }
        if (wlock & !fmton) {
            wloerr = true;
            errorflag=true;
            return -1;
        }
        if (wcreg==0) cnt=0;
        try {
            disk[unit][removable].seek(offs+diskaddr*(384+33));
            while ( (wcreg>0) & (cnt>0) ) {
                byte1 = data.mem.get(dema, addrreg) >> 4;
                //byte1 = data.memory[ema | addrreg] >> 4;
                disk[unit][removable].write(byte1);
                byte2 = (data.mem.get(dema, addrreg) & 017) << 4;
                addrreg++;
                //byte2 = (data.memory[ema | addrreg++] & 017) << 4;
                wcreg--; cnt--;
                if (wcreg>0) {
                    byte2 |= (data.mem.get(dema, addrreg) >> 8);
                    //byte2 |= data.memory[ema | addrreg] >> 8;
                    disk[unit][removable].write(byte2);
                    byte3 = data.mem.get(dema, addrreg) & 0377;
                    addrreg++;
                    //byte3 = data.memory[ema | addrreg++] & 0377;
                    disk[unit][removable].write(byte3);
                    wcreg--; cnt--;
                } else
                    disk[unit][removable].write(byte2);
            }
            while ( cnt>0 ) {
                cnt--;
                cnt--;
                disk[unit][removable].write(0);
                disk[unit][removable].write(0);
                disk[unit][removable].write(0);
            }
        }
        catch(java.io.IOException e){ System.out.println(e);
        }
        if (!frmena) {
            diskaddr++;
            sectreg = diskaddr & 017;
            trackreg = (diskaddr >> 4) | (removable << 10);
        }
        track[unit] = trackreg & 01777;
        return 0;
    }
    
    
    public void ClearFlags(int devcode) {
        ctrlreg = 0;
        dema = 0;
        intena = false;
        frmena = false;
        unit = siunit;
        rw = false;
        doneflag=false;
        busyflag = false;
        
        statreg = 0;
        errorflag=false;
        selerr=false;
        frmerr=false;
        adrerr=false;
        logerr=false;
        wloerr=false;
        
        seekstatreg = 0;
        seekreg = 0;
        sectreg = 0;
        trackreg = 0;
        addrreg = 0;
        data.setIntReq(DevId50,intena);
    }
    
    
    public void Interrupt(int command) {
        siunit=command;
    }
    
    public void ClearRun(boolean run) {
        if (!run) {
        }
    }
    
    public void CloseDev(int devcode) {
        for (int u=0;u<2;u++) {
            for (int r=0;r<2;r++) {
                if (sel[u][r]) {
                    disk4043.closeDisk(u,r,true);
                }
            }
        }
     }
    
    public void ClearPower(int devcode) {
    }
    
}
