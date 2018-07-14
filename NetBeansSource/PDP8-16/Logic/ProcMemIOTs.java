/*
 * ProcMemIOTs.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Logic;

//import java.io.*;

public class ProcMemIOTs implements Device, Constants {
    
    public static final int DevId00 = 000;
    public static final int DevId16 = 016;
    public static final int DevId17 = 017;
    public static final int DevId20 = 020;
    public static final int DevId21 = 021;
    public static final int DevId22 = 022;
    public static final int DevId23 = 023;
    public static final int DevId24 = 024;
    public static final int DevId25 = 025;
    public static final int DevId26 = 026;
    public static final int DevId27 = 027;
    public static final int DevId30 = 030;
    public static final int DevId31 = 031;
    public static final int DevId32 = 032;
    public static final int DevId33 = 033;
    public static final int DevId34 = 034;
    public static final int DevId35 = 035;
    public static final int DevId36 = 036;
    public static final int DevId37 = 037;
    
    private BusRegMem data;
    private int field;
    //KM8-E --- KT8A
    //public int ifr = 0; //Instruction field register in BusRegMem
    //public int ibr = 0; //Instruction bank register in BusRegMem
    private int ifb;      //Instruction field buffer
    private int ibb;      //Instruction bank buffer
    //public int dfr = 0; //Date field register in BusRegMem
    //public int dbr = 0; //Date bank register in BusRegMem
    //public boolean usermode;//User mode register in BusRegMem
    private boolean umb;  //User mode buffer
    private int ifs;      //Instruction field save register - part of SFR
    private int ibs = 0;  //Instruction bank save register - part of SFR
    private int dfs;      //Data field save register - part of SFR
    private int dbs = 0;  //Data bank save register - part of SFR
    private boolean ums;  //User mode save register - part of SFR
    private boolean uint;
    private int     KTMode  = 0;  //Extended mode reg KT8A
    private boolean KTEMM   = false;
    private int     KTEN59  = 0;
    private boolean KTERVF  = false;
    private boolean KTDIOI  = false;
    private boolean KTEBM   = false;
    private boolean KTMaint = false;
    private boolean KTFatal = false;
    private int Mflag = 0;
    private int maintreg = 0;
    private int relreg = 0;
    private int usreg = 0;
    private int [] brkmap = new int[13];    
    private int lastbrk = 0;
    /*multi8 variables */
    //private boolean mmena = false;
    private int trapinst;
    private int[] relmem = new int[8];
    private long trapreg;
    private int trapdev;
    private static final byte[] iottable = { 
      /* multi8: 0:never trap,-1:always trap , 1:conditional trap*/
        1,   1,  1,  1,  1,  1,  0,  1, 
      /*SKON,ION,IOF,SRQ,GTF,RTF,SGT,CAF                                         00    */
                         1,1,1,1,1,1,1,1,  1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  /* 01-03 */
        1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  /* 04-07 */
        1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  /* 10-13 */
        1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  /* 14-17 */
        -1,  1,  1,  1,  -1,  -1, -1, -1,  -1,  1,  1,  1,  0,   -1, -1, -1,  /* 20-21 */
      /*LXM, CDF,CIF,CDI,CINT,XDF,XIF,XDI  GTS, CDF,CIF,CDI,RDF, XDF,XIF,XDI           */
      /*CTI                   RTM                                SKME       OLD Multi8 */                       
        -1,  1,  1,  1,  0,   -1, -1, -1,  -1,  1,  1,  1,  -1,  -1, -1, -1,  /* 22-23 */
      /*RTS, CDF,CIF,CDI,RIF, XDF,XIF,XDI  RXM, CDF,CIF,CDI,RIB, XDF,XIF,XDI           */
      /*                      SKMM                               LTM        OLD Multi8 */
        -1,  1,  1,  1,  -1,  -1, -1, -1,  -1,  1,  1,  1,  -1,  -1, -1, -1,  /* 24-25 */
      /*LRR, CDF,CIF,CDI,RMF, XDF,XIF,XDI  RRR, CDF,CIF,CDI,SINT,XDF,XIF,XDI           */
      /*                      LRM                                NOP        OLD Multi8 */
        -1,  1,  1,  1,  -1,  -1, -1, -1,  -1,  1,  1,  1,  -1,  -1, -1, -1,  /* 26-27 */
      /*LUSR,CDF,CIF,CDI,CUF, XDF,XIF,XDI  RUSR,CDF,CIF,CDI,SUF, XDF,XIF,XDI           */
      /*                      SMME                               CMME       OLD Multi8 */    
        -1,  -1, -1, -1, -1,  -1, -1, -1,  1,   -1, -1, -1, -1,  -1, -1, -1,  /* 30-31 */
      /*NOP, ZDF,ZIF,ZDI,NOP ,YDF,YIF,XDI  NOP, ZDF,ZIF,ZDI,NOP, YDF,YIF,YDI           */
      /*CTI              RTM                                SKME            NEW Multi8 */                       
        1,   -1, -1, -1, -1,  -1, -1, -1,  1,   -1, -1, -1,  -1, -1, -1, -1,  /* 32-33 */
      /*NOP, ZDF,ZIF,ZDI,NOP ,YDF,YIF,YDI  NOP, ZDF,ZIF,ZDI,NOP, YDF,YIF,YDI           */
      /*                 SKMM                               LTM             NEW Multi8 */                       
        1,   -1, -1, -1, -1,  -1, -1, -1,  1,   -1, -1, -1,  1,  -1, -1, -1,  /* 34-35 */
      /*NOP, ZDF,ZIF,ZDI,NOP ,YDF,YIF,XDI  NOP, ZDF,ZIF,ZDI,NOP, YDF,YIF,YDI           */
      /*                 LRM                                NOP             NEW Multi8 */                       
        1,   -1, -1, -1, -1,  -1, -1, -1,  1,   -1, -1, -1,  -1, -1, -1, -1,  /* 36-37 */
      /*NOP, ZDF,ZIF,ZDI,NOP ,YDF,YIF,YDI  NOP, ZDF,ZIF,ZDI,NOP, YDF,YIF,YDI           */
      /*                 SMME                               CMME            NEW Multi8 */                       
        1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  /* 40-43 */
        1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  /* 44-47 */
        1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  /* 50-53 */
        1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  /* 54-57 */
        1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  /* 60-63 */
        1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  /* 64-67 */
        1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  /* 70-73 */
        1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1,  1,1,1,1,1,1,1,1, 1,1,1,1,1,1,1,1   /* 74-77 */
    };
    /* CDF=62F1,CIF=62F2,CDI=62F3, XDF=62F5,XIF=62F6,XDI=62F7  
       ZDF=63F1,ZIF=63F2,XDI=63F3, YDF=63F5,YIF=63F6,YDI=63F7 4 * 8 F = 32 FIELDS OF 4K*/
    
    /** Creates a new instance of ProcMemIOT
     * @param data */
    public ProcMemIOTs(BusRegMem data) {
        this.data = data;
    }
    
    public void Decode(int devcode, int opcode) {
        if (devcode==0) {
            switch (opcode) {
                case SKON: break;
                case ION: break;
                case IOF: break;
                case SRQ: break;
                case GTF: data.c0c1c2 = INBUSTOAC; break;
                case RTF: data.c0c1c2 = OUTACTOBUS; break;
                case SGT: break;
                case CAF: break;
            } 
        } else if (devcode==017) {
            switch (opcode) {
                case LBM:  data.c0c1c2 = OUTACTOBUSAC0; break;
                case RBM:  data.c0c1c2 = INACORBUSTOAC; break;
                case RLB:  data.c0c1c2 = INBUSTOAC; break;
                case RMR:  data.c0c1c2 = INBUSTOAC; break;
                case MBC:  data.c0c1c2 = OUTACTOBUSAC0; break;
                case RACA: data.c0c1c2 = INACORBUSTOAC; break;
                case RACB: data.c0c1c2 = INACORBUSTOAC; break;
                case RACC: data.c0c1c2 = INACORBUSTOAC; break;
            }
        } else if (devcode>=020 & devcode<=027) {
            field = devcode & 07;
            switch (opcode) {
                case MXX0: switch (field) {
                    case LXM: data.c0c1c2 = OUTACTOBUSAC0; break;
                    case GTS: data.c0c1c2 = INBUSTOAC; break;
                    case RTS: data.c0c1c2 = OUTACTOBUSAC0; break;
                    case RXM: data.c0c1c2 = INBUSTOAC; break;
                    case LRR: data.c0c1c2 = OUTACTOBUSAC0; break;
                    case RRR: data.c0c1c2 = INBUSTOAC; break;
                    case LUSR:data.c0c1c2 = OUTACTOBUSAC0; break;
                    case RUSR:data.c0c1c2 = INBUSTOAC; break;
                } break;
                case CDF: data.c0c1c2 = OUTACTOBUS; break;
                case CIF: data.c0c1c2 = OUTACTOBUS; break;
                case CDI: data.c0c1c2 = OUTACTOBUS; break;
                case MXX4: switch (field) {
                    case CINT:break;
                    case RDF: data.c0c1c2 = INACORBUSTOAC; break;
                    case RIF: data.c0c1c2 = INACORBUSTOAC; break;
                    case RIB: data.c0c1c2 = INACORBUSTOAC; break;
                    case RMF: break;
                    case SINT:break;
                    case CUF: break;
                    case SUF: break;
                } break;
                case XDF: data.c0c1c2 = OUTACTOBUS; break;
                case XIF: data.c0c1c2 = OUTACTOBUS; break;
                case XDI: data.c0c1c2 = OUTACTOBUS; break;
            }
        } else if (devcode>=030 & devcode<=037) {
            field = devcode & 07;
            switch (opcode) {
                case MXX0: switch (field) {
                    case CTI: data.c0c1c2 = OUTACTOBUS; break; /* multi8 cd to if */
                } break;
                case CDF: data.c0c1c2 = OUTACTOBUS; break;
                case CIF: data.c0c1c2 = OUTACTOBUS; break;
                case CDI: data.c0c1c2 = OUTACTOBUS; break;
                case MXX4: switch (field) {
                    case RTM: data.c0c1c2 = INBUSTOAC; break;
                    case SKME:break;
                    case SKMM:break;
                    case LTM: data.c0c1c2 = OUTACTOBUSAC0; break;
                    case LRM: data.c0c1c2 = OUTACTOBUSAC0; break;
                    case SMME:break;
                    case CMME:break;
                } break;
                case XDF: data.c0c1c2 = OUTACTOBUS; break;
                case XIF: data.c0c1c2 = OUTACTOBUS; break;
                case XDI: data.c0c1c2 = OUTACTOBUS; break;
            }
        }
    }
    
    public void Execute(int devcode, int opcode) {
        if (devcode==0) {
            switch (opcode) {
                case SKON:data.skipbus= data.intena; data.setInterruptOff(); break;
                case ION: data.intena=true; break;
                case IOF: data.setInterruptOff(); break;
                case SRQ: data.skipbus= (data.intreq>0); break;
                case GTF: {
                    if (data.link>0) data.data |= 04000;
                    if (data.gtf>0) data.data |= 02000;
                    if (data.intreq>0) data.data |= 01000;
                    if (KTEMM & KTFatal) data.data |= 00400;
                    //if (data.intinhibit) data.data |= 00400; //pdp-8/e only on front-panel
                    if (data.intena) data.data |= 00200;
                    if (ums) data.data |= 00100;
                    data.data |= (ifs << 3) + dfs;
                } break;
                case RTF: {
                    data.link = (data.data & 04000) >> 11;
                    data.gtf  = (data.data & 02000) >> 10;
                    data.intinhibit = true;
                    data.intena = true;
                    umb = ((data.data & 00100)>0);
                    ifb = (data.data & 00070) >> 3;
                    data.dfr = data.data & 00007;
                } break;
                case SGT: data.skipbus = (data.gtf>0); break;
                case CAF: data.ClearAllFlags(); break;
            }
        } else if (KTEMM & (devcode==017)) {
            switch (opcode) {
                case LBM: brkmap[(data.data&074)>>2] = data.data&03;break;
                case RBM: data.data |= brkmap[(data.data&074)>>2];break;
                case RLB: data.data = lastbrk; break;
                case RMR: data.data = maintreg; break;
                case MBC: lastbrk = setBRK(data.data)<<2; 
                          maintreg=(KTEBM & lastbrk<(13<<2))?setMR(brkmap[lastbrk>>2]):02100;break;
                case RACA: data.data = setRAC(1,data.data); break;
                case RACB: data.data = setRAC(2,data.data); break;
                case RACC: data.data = setRAC(3,data.data); break;
            }
        } else if (devcode>=020 & devcode<=027) {
            field = devcode & 07;
            switch (opcode) {
                case MXX0: switch (field) {
                    case LXM: {
                        setMode(data.data);
                    } break;
                    case GTS: {
                        if (data.link>0) data.data |= 04000;
                        data.data |= (ibs<<9) + (dbs<<7);
                        if (ums) data.data |= 00100;
                        data.data |= (ifs<<3) + dfs;
                    } break;
                    case RTS: {
                        //data.link = (data.data & 04000) >> 11;
                        //data.gtf  = (data.data & 02000) >> 10;
                        data.intinhibit = true;
                        data.intena = true;
                        ibb = (data.data & 03000) >> 9; 
                        data.dbr = (data.data & 00600) >> 7; 
                        umb = ((data.data & 00100)>0);
                        ifb = (data.data & 00070) >> 3;
                        data.dfr = data.data & 00007;
                    } break;
                    case RXM: data.data = KTMode; break;
                    case LRR: relreg = data.data&00037; break;
                    case RRR: data.data = relreg&00037; break;
                    case LUSR:usreg = data.data&00077; break; //40 is legal
                    case RUSR:data.data = usreg&00037; break;
                } break;
                case CDF: 
                case CIF: 
                case CDI: setCDI(opcode,0,field); break;
                case MXX4: switch (field) {
                    case CINT:uint = false; data.setIntReq(DevId20,uint); KTFatal= false; break;
                    case RDF: data.data |= setRDI(true); break; 
                    case RIF: data.data |= setRDI(false);break; 
                    case RIB: data.data |= (ibs<<9) + (dbs<<7) + (ifs<<3) + dfs; if (ums) data.data |= 00100; break;
                    case RMF: ifb = ifs; data.dfr = dfs; umb = ums; data.intinhibit = true; 
                              ibb = ibs; data.dbr = dbs; break;
                    case SINT:data.skipbus= uint; break;
                    case CUF: umb = false; data.usermode=false; uint = false; data.setIntReq(DevId20,uint); KTFatal= false; break;
                    case SUF: umb = true; data.intinhibit = true;  break;
                } break;
                case XDF: 
                case XIF: 
                case XDI: setCDI(opcode,1,field); break;
            }
        } else if (devcode>=030 & devcode<=037) {
            field = devcode & 07;
            switch (opcode) {
                case MXX0: switch (field) {
                    case CTI: data.dfr = data.ifr; /* multi8 cd to if */
                } break;
                case CDF: 
                case CIF: 
                case CDI: setCDI(opcode,2,field); break;
                case MXX4: switch (field) {
                    case RTM: data.data = trapinst; break;
                    case SKME:data.skipbus= data.mmena; break;
                    case SKMM:data.skipbus= dm8e; break;
                    case LTM:  {
                        trapdev = (data.data&0770)>>3;
                        if ((data.data&01)>0) {
                            trapreg |=  (1 << trapdev);
                        } else {
                            trapreg &= ~(1 << trapdev);
                        }
                    } break;
                    case LRM: relmem[data.data&07]=(data.data&070)>>3; break;
                    case SMME:if (dm8e) {data.mmena = true;} break;
                    case CMME:data.mmena = false; break;
                } break;
                case XDF: 
                case XIF: 
                case XDI: setCDI(opcode,3,field); break;
            }
        }
    }
    
    public void Interrupt(int command) {
        switch (command) {
            case INTINPROG: {
                ifs = ifb; data.ifr = 0; ifb = 0; data.ema = 0;
                ibs = ibb; data.ibr = 0; ibb = 0; 
                dfs = data.dfr; data.dfr = 0;
                dbs = data.dbr; data.dbr = 0;
                ums = umb; data.usermode = false; umb = false;
            } break;
            case JMPJMS: {
                if (Mflag!=0) {
                    int bnk;
                    if (Mflag>0) bnk=ibs; else bnk= data.dbr;
                    maintreg = setMR(bnk);
                    maintreg |= (Mflag>0)?ifs:data.dfr;
                    Mflag = 0;
                }
                if (data.state==DEFER & data.ir!=JMP & data.ir!=JMS) {
                    if (data.mmena & data.usermode) {
                        data.ema = relmem[data.dfr]<<12;
                    } else {
                        data.ema = ((data.dbr<<3) + data.dfr) << 12;
                    }
                    Mflag = -1;
                }
                if (data.ir==JMP | data.ir==JMS) {
                    if ((data.state==FETCH & (data.md&00400)==0) | data.state==DEFER) {//KT new fld
                        if (data.intinhibit) {
                            data.ifr = ifb;
                            data.ibr = ibb;
                            data.usermode = umb;
                            if (KTMaint) {
                                Mflag = 1;
                                uint = true;
                                data.setIntReq(DevId20,uint);
                            }
                            data.intinhibit = false;
                        }
                    }
                }
            } break;
            case USERMODE: {
                int hotst = data.md & 07407;
                if (hotst==07402 | hotst==07404 | hotst==07406) setTrap();
                if (data.ir==IOT) {
                    if (data.mmena) {
                        trapdev = (data.md&0770)>>3;
                        if (iottable[data.md&0777]<0) setTrap(); //always trap
                        else if    ( (trapreg & (1 << trapdev))==0 ) { //request trap
                            if (iottable[data.md&0777]!=0) setTrap(); //if conditional trap untrap
                        }
                    } else {
                        boolean trapit=true,trapcdi=false,iscdi=false;
                        if (KTEMM) {
                            int cdival=(data.md&0700);
                            if ((cdival==0200) | (cdival==0300)) cdival=data.md&0007; else cdival=0;
                            if ( (cdival!=0) & (cdival!=4) ) iscdi=true; //ALL CDF CIF
                            if (iscdi){ 
                                cdival = ((data.md&00070)>>3)+ ((data.md&00100)>>2) + ((data.md&00004)<<1);
                                if (cdival >= usreg) trapcdi=true;
                            } 
                            if (KTDIOI) trapit=trapcdi; else if (iscdi) trapit=trapcdi;
                            if (KTERVF & (data.md==06214 | data.md==06224) ) trapit=false; //RDF, RIF No trap 
                        }
                        if (trapit) setTrap();//normal no mm8 no KT trap
                                
                    } 
                }
            } break;
            case MAPIFR: {
                if (data.mmena & data.usermode) {
                    data.ema = relmem[data.ifr]<<12;
                } else {
                    data.ema = ((data.ibr<<3) + data.ifr) << 12;    
                }
            } break;
            case MAPDFR: {
                if (data.mmena & data.usermode) {
                    data.ema = relmem[data.dfr]<<12;
                } else {
                    data.ema = ((data.dbr<<3) + data.dfr) << 12;    
                }
            } break;
        }
    }
    
    private void setTrap() {
        if (data.timsha==false) {
            uint=false; 
            return;
        }
        trapinst = data.md;
        uint = true;
        data.setIntReq(DevId20,uint);
        if (data.ir==IOT) {
            data.iopause = false; 
        } else {
            data.md = data.md & 07771;
        }
    }
    
    private void setCDI(int opcode,int bank, int field) {
        int bk = bank&KTEN59;
        if (bk!=bank) return;
        int bf = (bk<<3) + field;
        if (data.usermode) bf = bf + relreg;
        int b = (bf&030)>>3;
        int f = (bf&007);
        opcode = opcode&03;
        if (opcode==CDF | opcode==CDI) {
            data.dbr = b; 
            data.dfr = f;            
        }
        if (opcode==CIF | opcode==CDI) {
            ibb = b; 
            ifb = f; 
            data.intinhibit = true;          
        }
    }
    
    private void setMode(int mode) {
        KTMode  =  mode&07740;
        KTEMM   = (mode&04000)>0;
        KTEN59  = (mode&03000)>>9;
        KTERVF  = (mode&00400)>0;
        KTDIOI  = (mode&00200)>0;
        KTEBM   = (mode&00100)>0;
        KTMaint = (mode&00040)>0;
    }
  
    private int setRDI(boolean rdfrif) { //rdfrif=true RDF
        int dfx = (data.dbr<<3) + data.dfr;
        int ifx = (data.ibr<<3) + data.ifr;
        if (KTERVF & data.usermode) {
            dfx = dfx - relreg;
            ifx = ifx - relreg;
        }
        if (rdfrif) {
            return ((dfx&020)<<2) + ((dfx&010)>>1) + ((dfx&07)<<3); 
        } else {
            return ((ifx&020)<<2) + ((ifx&010)>>1) + ((ifx&07)<<3);  
        }
     }
    
    private int setBRK(int prio) {
        int i=0;
        while (((prio&04000)==0) & (i<12)) {
            prio = prio<<1;
            i++;
        } 
        return i;
    }
    
    private int setMR(int bnk) {
        int mr=02100;
        switch (bnk) {
            case 0:mr = 02100;break;
            case 1:mr = 01040;break;
            case 2:mr = 00420;break;
            case 3:mr = 00210;break;
        }
        return mr;
    }
    
    private int setRAC(int abc,int val) {
        int a,cde,b,ret=0;
        switch (abc) {
            case 1: {
                a =   (val&00100)>>6;
                b =   (val&00004)>>2;
                cde = (val&00070)>>3;
                ret = (a<<8) + (b<<7) + cde;
            } break;
            case 2: {
                a =   (val&00400)>>8;
                b =   (val&00200)>>7;
                cde = (val&00007);
                ret = (a<<10) + (b<<9) + (cde<<3);
            } break;
            case 3: {
                a =   (val&02000)>>10;
                b =   (val&01000)>>9;
                cde = (val&00070)>>3;
                ret = (a<<6) + (b<<2) + (cde<<3);
            } break;
        }
        return ret;
    }
  
    public void ClearFlags(int devcode) {
        data.ac = 0;
        data.link = 0;
        data.setIntReq(DevId00,false);
        uint = false;
        data.setIntReq(DevId20,uint);
        data.setInterruptOff();
        //setMode(0);  //Extended mode reg KT8A
        maintreg = 0;
        lastbrk = 017;
        Mflag = 0;
    }
    
    public void ClearRun(boolean run) {
    }
    
    public void CloseDev(int devcode) {
    }
    
    public void ClearPower(int devcode) {
        ifb = 0;
        ibb = 0;
        umb = false;
        usreg = 0;
        relreg = 0;
        lastbrk = 017;
        setMode(0);  //Extended mode reg KT8A
    }
   
}
