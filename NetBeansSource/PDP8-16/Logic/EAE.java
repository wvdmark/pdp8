/*
 * EAE.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Logic;

public class EAE implements Constants {
    
    public EAE(BusRegMem data) {
        this.data = data;
    }
    
    BusRegMem data;
    
    public int Decode(int instr) {
        long longreg;
        int temp;
        //System.out.println("EAE: " + Integer.toOctalString(instr));
        if (instr==SWAB) {
            data.modeb = true;
            return data.link<<12;
        }
        if (instr==SWBA) {
            data.modeb = false;
            data.gtf = 0;
            return data.link<<12;
        }
        if (!data.modeb) {
            if ((instr&00040)!=0) data.data |= data.sc; //SCA bit microprogram in mode a
            switch ((instr>>1)&07) {
                case SCL: {
                    data.md = data.mem.get(data.ema, data.ma+1);
                    data.sc = (~data.md)&037;
                    data.skip = true;
                    data.pdp8time += 14;
                } break;
                case MUY: {
                    data.md = data.mem.get(data.ema, data.ma+1);
                    longreg = (data.mq * data.md) + data.data;
                    data.link = 0;
                    data.data = (int) (longreg>>12)&07777;
                    data.mq   = (int) (longreg)&07777;
                    data.sc = 014;
                    data.skip = true;
                    data.pdp8time += 62;
                } break;
                case DVI: {
                    data.md = data.mem.get(data.ema, data.ma+1);
                    if (data.data-data.md<0) {
                        longreg = (data.data<<12) | (data.mq);
                        data.mq = (int) (longreg / data.md)&07777;
                        data.data = (int) (longreg - (long) (data.mq * data.md));
                        data.link = 0;
                        data.sc = 015;
                    } else {
                        data.mq = ((data.mq << 1) + 1) & 07777 ;
                        //data.data = data.data;
                        data.link = 1;
                        data.sc = 01 ;
                    }
                    data.skip = true;
                    data.pdp8time += 62;
                } break;
                case NMI: {
                    longreg = getLong();
                    for (data.sc = 0 ; (longreg & 0100000000) == ((longreg & 0040000000) << 1)
                    && ((longreg & 037777777)!=0) ; data.sc++)
                        longreg <<= 1 ;
                    setLong(longreg);
                    data.pdp8time += (3*data.sc);
                } break;
                case SHL: {
                    data.md = data.mem.get(data.ema, data.ma+1);
                    data.sc = (data.md+1)&077; //allow 32bit shift
                    longreg = getLong();
                    longreg <<= data.sc;
                    setLong(longreg);
                    data.sc = 0;
                    data.skip = true;
                    data.pdp8time += (14+3*data.sc);
                } break;
                case ASR: {
                    data.md = data.mem.get(data.ema, data.ma+1);
                    data.sc = (data.md+1)&077; //allow 32bit shift
                    data.link = 2; //kludge to signal sign prop
                    longreg = getLong();
                    longreg >>= data.sc;
                    setLong(longreg);
                    data.sc = 0;
                    data.skip = true;
                    data.pdp8time += (14+3*data.sc);
                } break;
                case LSR: {
                    data.md = data.mem.get(data.ema, data.ma+1);
                    data.sc = (data.md+1)&077; //allow 32bit shift
                    data.link = 0;
                    longreg = getLong();
                    longreg >>>= data.sc;
                    setLong(longreg);
                    data.sc = 0;
                    data.skip = true;
                    data.pdp8time += (14+3*data.sc);
                } break;
            }
            data.gtf = 0;
        } else {
            switch ((((instr&00040)>>1)+(instr&0016))>>1) {
                case ASC: {
                    data.sc = (data.data)&037;
                    data.data = 0;
                } break;
                case MUY: {
                    data.ma = chkAuto(data.ma+1);
                    data.md = data.mem.get(data.ema, data.ma);
                    longreg = (data.mq * data.md) + data.data;
                    data.link = 0;
                    data.data = (int) (longreg>>12)&07777;
                    data.mq   = (int) (longreg)&07777;
                    data.sc = 014;
                    data.skip = true;
                    data.pdp8time += 62;
                } break;
                case DVI: {
                    data.ma = chkAuto(data.ma+1);
                    data.md = data.mem.get(data.ema, data.ma);
                    if (data.data-data.md<0) {
                        longreg = (data.data<<12) | (data.mq);
                        data.mq = (int) (longreg / data.md)&07777;
                        data.data = (int) (longreg - (long) (data.mq * data.md));
                        data.link = 0;
                        data.sc = 015;
                    } else {
                        data.mq = ((data.mq << 1) + 1) & 07777 ;
                        //data.data = data.data;
                        data.link = 1;
                        data.sc = 01 ;
                    }
                    data.skip = true;
                    data.pdp8time += 62;
                } break;
                case NMI: {
                    longreg = (data.link<<24) | (data.data<<12) | (data.mq);
                    for (data.sc = 0 ; (longreg & 0040000000) == ((longreg & 0020000000) << 1)
                    && ((longreg & 017777777)!=0) ; data.sc++)
                        longreg <<= 1 ;
                    data.link = (int) (longreg>>24)&00001;
                    data.data = (int) (longreg>>12)&07777;
                    data.mq   = (int) (longreg)&07777;
                    data.pdp8time += (3*data.sc);
                } break;
                case SHL: {
                    data.md = data.mem.get(data.ema, data.ma+1);
                    data.sc = (data.md)&037;
                    longreg = (data.link<<25) | (data.data<<13) | (data.mq<<1);
                    longreg <<= data.sc;
                    data.link = (int) (longreg>>25)&00001;
                    data.data = (int) (longreg>>13)&07777;
                    data.mq =   (int) (longreg>>1)&07777;
                    data.sc = 037;
                    data.skip = true;
                    data.pdp8time += (17+3*data.sc);
                } break;
                case ASR: {
                    data.md = data.mem.get(data.ema, data.ma+1);
                    data.sc = (data.md)&037;
                    long tl = ((data.data&04000)>0)?0x7FFFFFFFFFL:0;
                    longreg = (tl<<25) | (data.data<<13) | (data.mq<<1)| data.gtf;
                    longreg >>= data.sc;
                    data.link = (int) (longreg>>25)&00001;
                    data.data = (int) (longreg>>13)&07777;
                    data.mq =   (int) (longreg>>01)&07777;
                    data.gtf = (int) (longreg)&00001;
                    data.sc = 037;
                    data.skip = true;
                    data.pdp8time += (17+3*data.sc);
                } break;
                case LSR: {
                    data.md = data.mem.get(data.ema, data.ma+1);
                    data.sc = (data.md)&037;
                    data.link = 0;
                    longreg = (data.data<<13) | (data.mq)<<1 | data.gtf;
                    longreg >>>= data.sc;
                    data.data = (int) (longreg>>13)&07777;
                    data.mq   = (int) (longreg>>1)&07777;
                    data.gtf  = (int) (longreg)&00001;
                    data.sc = 037;
                    data.skip = true;
                    data.pdp8time += (17+3*data.sc);
                } break;
                case SCA: {
                    data.data |= data.sc;
                } break;
                case DAD: {
                    data.ma = chkAuto(data.ma+1);
                    long longreg1 = data.mem.get(data.ema, data.ma+1)<<12 | data.mem.get(data.ema, data.ma);
                    longreg = (data.data<<12) | (data.mq);
                    longreg = longreg + longreg1;
                    data.link = (int) (longreg>>24)&00001;
                    data.data = (int) (longreg>>12)&07777;
                    data.mq   = (int) (longreg)&07777;
                    data.skip = true;
                    data.pdp8time += 40;
                } break;
                case DST: {
                    data.ma = chkAuto(data.ma+1);
                    data.mem.set(data.ema, data.ma+1, data.data);
                    data.mem.set(data.ema, data.ma, data.mq);
                    data.skip = true;
                    data.pdp8time += 40;
                } break;
                case 013: { //should'nt get here
                    data.modeb = false;
                    data.gtf = 0;
                } break;
                case DPSZ: {
                    if ((data.data | data.mq)==0) data.skip = true;
                } break;
                case DPIC: {
                    longreg = (data.mq<<12) | (data.data); //mq and data inverted because mql and mqa set
                    longreg++;
                    data.link = (int) (longreg>>24)&00001;
                    data.data = (int) (longreg>>12)&07777;
                    data.mq   = (int) (longreg)&07777;
                    data.pdp8time += 4;
                } break;
                case DCM: {
                    longreg = (data.mq<<12) | (data.data); //mq and data inverted because mql and mqa set
                    longreg = ((~longreg)&077777777)+1;
                    data.link = (int) (longreg>>24)&00001;
                    data.data = (int) (longreg>>12)&07777;
                    data.mq   = (int) (longreg)&07777;
                    data.pdp8time += 4;
                } break;
                case SAM: {
                    temp = data.data;
                    data.data = data.mq + (temp ^ 07777) + 1 ;
                    boolean gtfb = (temp <= data.mq) ^ (((temp ^ data.mq) >> 11)>0) ;
                    data.link = ((data.data)&010000)>>12;
                    data.data = data.data&07777;
                    data.gtf = (gtfb)?1:0;
                } break;
            }
        }
        return data.link<<12;
    }
    
    private long getLong() {
        long tl = data.link;
        if (data.link>1) tl = ((data.data&04000)>0)?0x7FFFFFFFFFL:0;
        return (tl<<25) | (data.data<<13) | (data.mq<<1);
    }
    
    private void setLong(long longreg) {
        data.link = (int) (longreg>>25)&00001;
        data.data = (int) (longreg>>13)&07777;
        data.mq =   (int) (longreg>>01)&07777;
        if (data.modeb) data.gtf = (int) (longreg)&00001;
    }
    
    private int chkAuto(int marg) {
        if ((marg & 07770) == 00010) 
        data.mem.set(data.ema, marg, data.mem.get(data.ema, marg)+1);
        int darg = data.mem.get(data.ema, marg); //Save before switching Data Field
        data.devices[ProcMemIOTs.DevId20].Interrupt(data.devices[ProcMemIOTs.DevId20].MAPDFR);
        return darg;
    }
    
}
