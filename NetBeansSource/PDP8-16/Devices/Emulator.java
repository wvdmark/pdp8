/*
 * Emulator.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Devices;

import java.io.InputStream;
import java.io.IOException;

public abstract class Emulator{
    Term term=null;
    InputStream in=null;
    
    public Emulator(Term term, InputStream in){
        this.term=term;
        this.in=in;
    }
    
    public abstract void setPunch();
    public abstract void setLoopback(boolean x);
    
    public abstract void Start();
    public abstract int[] getCodeRETURN();
    public abstract int[] getCodeBS();
    public abstract int[] getCodeDEL();
    public abstract int[] getCodeESC();
    public abstract int[] getCodeSubtract();
    public abstract int[] getCodeComma();
    public abstract int[] getCodeDecimal();
    public abstract int[] getCodeENTER();
    public abstract int[] getCodeNP1();
    public abstract int[] getCodeNP2();
    public abstract int[] getCodeNP3();
    public abstract int[] getCodeNP4();
    public abstract int[] getCodeNP5();
    public abstract int[] getCodeNP6();
    public abstract int[] getCodeNP7();
    public abstract int[] getCodeNP8();
    public abstract int[] getCodeNP9();
    public abstract int[] getCodeUP();
    public abstract int[] getCodeDOWN();
    public abstract int[] getCodeRIGHT();
    public abstract int[] getCodeLEFT();
    public abstract int[] getCodeF1();
    public abstract int[] getCodeF2();
    public abstract int[] getCodeF3();
    public abstract int[] getCodeF4();
    
    public abstract void reset();
    
    byte[] buf=new byte[1024];
    int bufs=0;
    int buflen=0;
    
    byte getChar() throws java.io.IOException {
        if(buflen==0){
            fillBuf();
        }
        buflen--;
        return (byte) (buf[bufs++] & 0377);
    }
    
    void fillBuf() throws java.io.IOException {
        buflen=bufs=0;
        buflen=in.read(buf, bufs, buf.length-bufs);
        if(buflen<=0){
            buflen=0;
            throw new IOException("fillBuf");
        }
    }
    void pushChar(byte foo) throws java.io.IOException {
        buflen++;
        buf[--bufs]=foo;
    }
    int getASCII(int len) throws java.io.IOException {
        if(buflen==0){
            fillBuf();
        }
        if(len>buflen)len=buflen;
        int foo=len;
        byte tmp;
        while(len>0){
            tmp=buf[bufs++];
            if(0x20<=tmp && tmp<=0x7f){
                buflen--;
                len--;
                continue;
            }
            bufs--;
            break;
        }
        return foo-len;
    }
}
