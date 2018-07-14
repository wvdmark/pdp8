/*
 * Example
 *
 * Created on January 1, 2007, 0:00 PM
 */
/**
 *
 * @author  wvdmark@computer.org
 */
package Logic;

import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class LPT implements Device, Constants {

    public static int DevId57 = 057;
    public static int DevId66 = 066;
    //public static int DevId65 = 065;
    private int curdev = DevId57;
    public BusRegMem data;
    public Devices.LE8 le8;
    private boolean lptintena = false;
    public boolean lptflag = false;
    public boolean errflag = false;
    private boolean readflag = false;
    public StringBuilder buffer;
    //public int line = 0;
    private int readbuf = 0;
    //private String doctext ;
    private byte[] docbytes = null;

    private boolean docLoaded = false;
    private int docpos = 0;
    private int doclength = 0;
    public boolean lptlinewise = false;


    private VirTimer lpttim;
    private VirTimer lptflush;
    private VirTimer readtim;
    public boolean lptonline = true;
    private int lptlines = 0;
    private int lptpos = 0;
    private int lptcnt = 0;
    public boolean lptfast = false;

    /** Creates a new instance of XXX */
    public LPT(BusRegMem data) {
        this.data = data;
        le8 = new Devices.LE8(this);
        le8.setVisible(true);
        buffer = new StringBuilder(256);

        VirListener lptaction = new VirListener() {

            public void actionPerformed() {
                //System.out.println("LPT");
                setLPT(true);
            }
        };
        lpttim = new VirTimer(data.virqueue, LPTLACHR, lptaction);
        lpttim.setRepeats(false);

        VirListener doflush = new VirListener() {

            public void actionPerformed() {
                //System.out.println("LPT");
                setFlush();
            }
        };
        lptflush = new VirTimer(data.virqueue, LPTFLUSH, doflush);
        lptflush.setRepeats(false);

    }

    public void Decode(int devcode, int opcode) {
        if (devcode == DevId66) {
            switch (opcode) {
                case PSXX:
                    break;
                case PSKF:
                    break;
                case PCLF:
                    break;
                case PSKE:
                    break;
                case PSTB:
                    break;
                case PSIE:
                    break;
                case PCLP:
                    break;
                case PCIE:
                    break;
            }
        }
        /*
        if (devcode == DevId65) {
            switch (opcode) {
                case RSKF:
                    break;
                case RRRB: data.c0c1c2 = INACORBUSTOAC;
                    break;
                case RSKE:
                    break;
                case RRFC:
                    break;
                case RFRB: data.c0c1c2 = INACORBUSTOAC;
                    break;
            }
        }
         */
        if (devcode == DevId57) {
            switch (opcode) {
                case DBST:
                    break;
                case DBSK: ;
                    break;
                case DBRD: data.c0c1c2 = INBUSTOAC;
                    break;
                case DBCF:
                    break;
                case DBTD:
                    break;
                case DBSE:
                    break;
                case DBCE:
                    break;
                case DBSS:
                    break;
            }
        }
    }

    public void Execute(int devcode, int opcode) {
        curdev = devcode;
        if (devcode == DevId66) {
            switch (opcode) {
                case PSXX:
                    setLPT(false);
                    lptClear();
                    break;
                case PSKF:
                    data.skipbus = (lptflag) ? true : false;
                    break;
                case PCLF:
                    setLPT(false);
                    break;
                case PSKE:
                    data.skipbus = !lptonline; //(errflag) ? true : false;
                    break;
                case PSTB:
                    lptChar( data.data&0177 );
                    break;
                case PSIE:
                    lptintena = true;
                    setIntReq();
                    break;
                case PCLP:
                    setLPT(false);
                    lptChar( data.data&0177 );
                    break;
                case PCIE:
                    lptintena = false;
                    clearIntReq();
                    break;
            }
        }
        /*
        if (devcode == DevId65) {
            switch (opcode) {
                case RSKF:
                    data.skipbus = readflag;
                    break;
                case RRRB:
                    data.data |= readbuf;
                    break;
                case RSKE:
                    data.skipbus = readerr;
                    break;
                case RRFC:
                    readflag = false;
                    getChar();
                    break;
                case RFRB:
                    data.data |= readbuf;
                    readflag = false;
                    getChar();
                    break;
            }
        }
         */
        if (devcode == DevId57) {
            switch (opcode) {
                case DBST:
                    data.skipbus = lptflag;
                    if (lptflag) setLPT(false);
                    break;
                case DBSK: 
                    data.skipbus = readflag;
                    break;
                case DBRD: 
                    data.data = readbuf;
                    break;
                case DBCF:
                    readflag = false;
                    getChar();
                    break;
                case DBTD:
                    lptChar( (~data.data)&0177 );
                    break;
                case DBSE:
                    lptintena = true;
                    setIntReq();
                    break;
                case DBCE:
                    lptintena = false;
                    clearIntReq();
                    break;
                case DBSS:
                    //setLPT(false);
                    //lptClear();
                    break;
            }
        }
    }

    private void lptChar(int lptdata) {
        if (setLine(lptdata) == 0) {
            if (curdev==DevId57) {
                lpttim.setInitialDelay(LPTLACHR);
            } else {
                switch (lptdata) {
                    case 012:
                        lpttim.setInitialDelay(LPTLESLW);
                        break;
                    case 014:
                        lpttim.setInitialDelay(LPTLEFRF);
                        break;
                    default:
                        lpttim.setInitialDelay(LPTLEIMM);
                        if (lptcnt%24==0) lpttim.setInitialDelay(LPTLEPRT);
                }
            }
            lpttim.start();
            lptcnt += 1;
            if (lptflush.expirationTime < data.pdp8time + lpttim.expirationTime) {
                lptflush.restart();
            }
        } 
    }

    private void lptClear() {
        le8.clearEditor();
        lpttim.start();
        lptlinewise = false;
        lptfast = true;
    }

    private void setLPT(boolean set) {
        if (set) {
            lptflag = true;
            setIntReq();
        } else {
            lpttim.stop();
            lptflag = false;
            errflag = false;
            clearIntReq();
        }
    }

    private int setLine(int newbyte) {
        if (!lptonline) {
            return -1;
        }
        if (newbyte==0 | newbyte == 032) {
            return 0;
        }
        if (lptpos == 0377 | newbyte == 0177) {
            buffer = new StringBuilder(300);
            lptpos = 0;
            return 0;
        }
        if (newbyte == 007) {
            Toolkit.getDefaultToolkit().beep(); 
            return 0;
        }
        if (newbyte == 010) {
            if (buffer.length()>0) {
                buffer.deleteCharAt(buffer.length() - 1);
                lptpos -= 1;
            }
            return 0;
        }
        if (newbyte == 015) {
            lptpos = 0;
            lptcnt = 0;
            return 0;
        }
        if (newbyte != 012 && lptpos<buffer.length()) {
            if (newbyte != 040) {
                buffer.deleteCharAt(lptpos);
                buffer.insert(lptpos,(char) newbyte);
            }
            lptpos += 1;
        } else if (newbyte != 012) {
            buffer.append((char) newbyte);
            lptpos += 1;
        }
        if (newbyte == 014 || newbyte == 012) {
            buffer.append(eol);
            le8.addEditor(buffer);
            buffer = new StringBuilder(300);
            lptpos = 0;
            lptcnt = 0;
            if (lptlinewise) le8.setEditor();
            lptlines += 1;
        }
        if (newbyte == 014 || lptlines > 100) {
            if (!lptfast) le8.setEditor();
            lptlines = 0;
        }
        return 0;
    }

    private void getChar() {
        Document doc;
        String doctext;
        clearIntReq();
        if (!docLoaded) {
            doc = le8.getDocument();
            try {
                doctext = doc.getText(0, doc.getLength());
                docbytes = doctext.replace("\n", "\r\n").getBytes();
            } catch (BadLocationException ex) {
                Logger.getLogger(LPT.class.getName()).log(Level.SEVERE, null, ex);
            }
            doclength = docbytes.length;
            if (doclength == 0) {
                readbuf = 04000;
            } else {
                docLoaded = true;
                docpos = 0;
            }
        }
        if (docLoaded) {
            if (docpos < doclength) {
                readbuf = docbytes[docpos];
                docpos += 1;
            } else {
                readbuf = 04000;
                docLoaded = false;
                docbytes = null;
            }
        }
        readflag = true;
        setIntReq();
    }


    private void setFlush() {
        le8.addEditor(buffer);
        buffer = new StringBuilder(300);
        le8.setEditor();
        lptfast = false;
        lptpos = 0;
    }

    public void clearIntReq() {
        if (lptintena==false) data.setIntReq(curdev, false);
        else if(lptflag == false & readflag == false & errflag == false) {
            data.setIntReq(curdev, false);
        }
    }

    public void setIntReq() {
        if (lptintena==false) data.setIntReq(curdev, false);
        else if(lptflag == true | readflag == true | errflag == true) {
            data.setIntReq(curdev, true);
        }
        else data.setIntReq(curdev, false);
    }

    public void ClearFlags(int devcode) {
        lpttim.stop();
        lptflush.stop();
        lptflag = false;
        errflag = false;
        readflag = false;
        lptintena = false;
        lptonline = true;
        data.setIntReq(DevId66, lptintena);
    }

    public void Interrupt(int command) {
    }

    public void ClearRun(boolean run) {
    }

    public void CloseDev(int devcode) {
    }
    
    public void ClearPower(int devcode) {
    }
}
