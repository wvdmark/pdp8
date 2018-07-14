/*
 * VT100.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Devices;

import java.io.InputStream;
//import java.io.IOException;

public class VT100 extends Emulator{
    
    private int term_width=82;
    private int term_height=24;
    
    private int x=0;
    private int y=0;
    
    private int char_width;
    private int char_height;
    
    private java.awt.Color fground = null;
    private java.awt.Color bground = null;
    private java.awt.Color bfground = null;
    private java.awt.Color bbground = null;
    
    private byte b;
    private char c;
    private String arch;
    
    private int region_y1=1;
    private int region_y2=term_height;
    
    private int[] intarg = new int[16];
    private int intargi = 0;
    private boolean gotdigits = false;
    
    private boolean on_off = false;
    private boolean ignore = false;
    private boolean punch_flag = false;
    private boolean loop_flag = false;
    
    private boolean cursor_appl = false;
    private boolean col132 = false;
    private boolean smooth = false;
    private boolean reverse = false;
    private boolean org_rel = false;
    private boolean wrap = false;
    private boolean repeat = false;
    private boolean interlace = false;
    private boolean keypad = false;
    private boolean ansi = false;
    private boolean newline = false;
    private int char_attr = 0;
    private int char_set = 0;
    private boolean g0_graph = false;
    private boolean g1_graph = false;
    private boolean[] tab ;
    
    
    private char[] vt100_graphics = {
        0x0040, 0x2666, 0x2592, 0x2409, 0x240c, 0x240d, 0x240a, 0x00b0,
        0x00b1, 0x2424, 0x240b, 0x2518, 0x2510, 0x250c, 0x2514, 0x253c,
        0x2594, 0x2580, 0x2500, 0x2584, 0x2581, 0x251c, 0x2524, 0x2534,
        0x252c, 0x2502, 0x2264, 0x2265, 0x03a0, 0x2260, 0x00a3, 0x00b7
    /*
    Octal   Uni     ASCII   Special                 Octal   Uni     ASCII   Special
    Code    Code    graphic graphic                 code    Code    graphic graphic
    ---------------------------------------------------------------------------------------
    0137    0x0040  _       Blank                   0157    0x2594  o       Horiz Line - scan 1
    0140    0x2666  \       Diamond                 0160    0x2580  p       Horiz Line - scan 3
    0141    0x2592  a       Checkerboard            0161    0x2500  q       Horiz Line - scan 5
    0142    0x2409  b       Digraph: HT             0162    0x2584  r       Horiz Line - scan 7
    0143    0x240c  c       Digraph: FF             0163    0x2581  s       Horiz Line - scan 9
    0144    0x240d  d       Digraph: CR             0164    0x251c  t       Left "T" (|-)
    0145    0x240a  e       Digraph: LF             0165    0x2524  u       Right "T" (-|)
    0146    0x00b0  f       Degree Symbol           0166    0x2534  v       Bottom "T" (|_)
    0147    0x00b1  g       � Symbol                0167    0x252c  w       Top "T" (T)
    0150    0x2424  h       Digraph: NL             0170    0x2502  x       Vertical Bar (|)
    0151    0x240b  i       Digraph: VT             0171    0x2264  y       Less/Equal (<_)
    0152    0x2518  j       Lower-right corner _|   0172    0x2265  z       Grtr/Egual (>_)
    0153    0x2510  k       Upper-right corner      0173    0x03a0  {       Pi symbol
    0154    0x250c  l       Upper-left corner       0174    0x2260  |       Not equal (=/)
    0155    0x2514  m       Lower-left corner |_    0175    0x00a3  }       UK pound symbol
    0156    0x253c  n       Crossing lines (+)      0176    0x00b7  ~       Centered dot
     */
    };
    
    public VT100(Term term, InputStream in){
        super(term, in);
        this.term=term;
        bfground=term.getFGround();
        bbground=term.getBGround();
    }
    
    public void setPunch(){
        punch_flag=true;
    }
    
    public void setLoopback(boolean loop) {
        loop_flag = loop;
    }
    
    public void Start(){
        
        term_width=term.getColumnCount();
        term_height=term.getRowCount();
        
        char_width=term.getCharWidth();
        char_height=term.getCharHeight();
        
        int rx=0;
        int ry=0;
        int w=0;
        int h=0;
        
        x=0;
        y=char_height;
        
        try{
            while(true){
                
                b=getChar();
                
                //c = (char) ((b & 0177) + (b<0?128:0));
                c = (char) (b & 0177);
                
                if (loop_flag) {
                    int[] loo=new int[1];
                    loo[0] = b;
                    term.sendKeySeq(loo);
                    continue;
                }
                if (punch_flag) {
                    int intbyte = b & 0177 + (b<0?128:0);
                    
                    punch_flag =  term.sendOutByte(intbyte);
                    continue;
                }
                
                b = (byte) (b & 0177);
                char_width=term.getCharWidth();
                char_height=term.getCharHeight();
                ry=y;
                rx=x;
                
                if(b==0) {
                    continue;
                }
                
                if(b==0x1b){ //ESC
                    b=getChar();
                    
                    if (b==0x5c) { //end special sequence '\'
                        ignore = false;
                        continue;
                    }
                    
                    if(b=='<'){   // enter ANSII (VT100) mode
                        ansi = true;
                        continue;
                    }
                    if(b=='>'){   // enter keypad numeric mode
                        keypad = false;
                        continue;
                    }
                    if(b=='='){   // enter keypad application mode
                        keypad = true;
                        continue;
                    }
                    if(b=='H'){   // HTS: set horizontal tabs
                        tab[x/char_width] = true;
                        continue;
                    }
                    if(b=='P'){   // DCS: Device control string: ignore until '\'
                        ignore = true;
                        continue;
                    }
                    if(b=='#'){   // double width options
                        b=getChar(); // has numeric args 3-8
                        term.draw_cursor();
                        if (b=='6') term.setFont(term.DOUBLE_W);
                        if (b=='3') term.setFont(term.DOUBLE_T);
                        if (b=='4') term.setFont(term.DOUBLE_B);
                        if (b=='5') term.setFont(term.SINGLE_W_H);
                        term.setBGround(term.getBGround());
                        term.draw_cursor();
                        continue;
                    }
                    if(b=='('){   // G0 Sets Sequence
                        b=getChar(); // has args a b 0 1 2
                        switch (b) {
                            case 'A':
                            case 'B': g0_graph = false; break;
                            case '0':
                            case '1':
                            case '2': g0_graph = true; break;
                        }
                        continue;
                    }
                    if(b==')'){   // G1 Sets Sequence
                        b=getChar(); // has args a b 0 1 2
                        switch (b) {
                            case 'A':
                            case 'B': g1_graph = false; break;
                            case '0':
                            case '1':
                            case '2': g1_graph = true; break;
                        }
                        continue;
                    }
                    if(b=='M'){   // RI: Reverse index, scroll text down
                        term.draw_cursor();
                        term.scroll_window((region_y1-1)*char_height,(region_y2-1)*char_height,char_height, bground, smooth);
                        term.draw_cursor();
                        continue;
                    }
                    if(b=='D'){  // IND: Index down, like line-feed, scroll text up
                        term.draw_cursor();
                        term.scroll_window((region_y1)*char_height,(region_y2)*char_height,-char_height, bground, smooth);
                        term.draw_cursor();
                        continue;
                    }
                    
                    if(b!='['){
                        err("ESC", b) ;
                        pushChar(b);
                        continue;
                    }
                    
                    getDigits();
                    b=getChar();
                    
                    if(b=='A'){ //cursor up
                        term.draw_cursor();
                        if(!gotdigits){ intarg[0]=1;}
                        y-=(intarg[0])*char_height;
                        if (y<=0) y=char_height;
                        term.setCursor(x, y);
                        term.draw_cursor();
                        continue;
                    }
                    if(b=='B'){ //cursor down
                        term.draw_cursor();
                        if(!gotdigits){ intarg[0]=1;}
                        y+=(intarg[0])*char_height;
                        if (y>term_height*char_height) y=term_height*char_height;
                        term.setCursor(x, y);
                        term.draw_cursor();
                        continue;
                    }
                    if(b=='C'){ //Cursor right
                        term.draw_cursor();
                        if(!gotdigits){ intarg[0]=1;}
                        x+=(intarg[0])*char_width;
                        if (x>=term_width*char_width) x = (term_width-1)*char_width;
                        term.setCursor(x, y);
                        term.draw_cursor();
                        continue;
                    }
                    if(b=='D'){ //cursor left
                        term.draw_cursor();
                        if(!gotdigits){ intarg[0]=1;}
                        x-=intarg[0]*char_width;
                        if(x<0)x=0;
                        term.setCursor(x, y);
                        term.draw_cursor();
                        continue;
                    }
                    if(b=='H' | b=='f'){ //CUP: cursor position, also HVP:
                        term.draw_cursor();
                        if(!gotdigits) intarg[0]=intarg[1]=1;
                        if (intarg[0]==0) intarg[0]=1;
                        if (intarg[1]==0) intarg[1]=1;
                        x=(intarg[1]-1)*char_width;
                        y=intarg[0]*char_height;
                        term.setCursor(x, y);
                        term.draw_cursor();
                        continue;
                    }
                    if(b=='J'){ //clear to bottom 0; clear top 1; clear all 2
                        int ystart = 0; int yend = term_height*char_height;
                        if (intarg[0]==0) ystart = y-char_height;
                        if (intarg[0]==1) yend = y;
                        term.draw_cursor();
                        term.clear_area(0, ystart, term_width*char_width, yend);
                        term.redraw(0, ystart-char_height, term_width*char_width,
                        yend-ystart+char_height);
                        term.draw_cursor();
                        continue;
                    }
                    if(b=='K'){ //erase end of line 0; erase begin 1; erase all line 2
                        int xstart = 0; int xend = term_width*char_width;
                        if (intarg[0]==0) xstart = x;
                        if (intarg[0]==1) xend = x+char_width;
                        term.draw_cursor();
                        term.clear_area(xstart, y-char_height, xend, y);
                        term.redraw(xstart, y-char_height, xend-xstart, char_height);
                        term.draw_cursor();
                        continue;
                    }
                    
                    if(b=='R'){   // DSR: Device status report
                        System.out.println("VT100-Cursor Position-Wrong direction");
                        continue;
                    }
                    
                    if(b=='c'){   // Power reset, well..., not really
                        reset();
                        continue;
                    }
                    
                    if(b=='g'){   // Reset tabs, 0:at cursor, 3:all
                        if (intarg[0]==0) tab[x/char_width] = false;
                        else {
                            for (x=0;x<term_width;x++) {
                                tab[x]=false;
                            }
                        }
                        continue;
                    }
                    
                    if(b=='h' | b=='l'){
                        //getDigits();
                        //b=getChar();
                        if (b=='h') on_off = true; else on_off = false; //h or L lower case
                        term.draw_cursor();
                        switch (intarg[0]) {
                            case -1: cursor_appl = on_off; break;
                            case -2: ansi = on_off ; break;
                            case -3: col132 = on_off; break; //not impl
                            case -4: smooth = on_off; break;
                            case -5: reverse = on_off; break;
                            case -6: org_rel = on_off; break;
                            case -7: wrap = on_off; break;
                            case -8: repeat = on_off; break; //not impl
                            case -9: interlace = on_off;break; //not impl
                            case 20: newline = on_off; break;
                        }
                        if (reverse) {
                            fground = bbground;
                            bground = bfground;
                        } else{
                            fground = bfground;
                            bground = bbground;
                        }
                        term.setFGround(fground);
                        term.setBGround(bground);
                        term.clear_area(0, 0, term_width*char_width, term_height*char_height);
                        term.redraw(0, 0, term_width*char_width, term_height*char_height);
                        term.draw_cursor();
                        continue;
                    }
                    
                    if(b=='m'){
                        for (int i=0; i<=intargi; i++) {
                            if(intarg[i]==0){ // no attributes
                                term.draw_cursor();
                                term.setFont(term.NORMAL);
                                term.draw_cursor();
                                term.setFGround(fground);
                                term.setBGround(bground);
                                continue;
                            }
                            if(intarg[i]==1){ // bold
                                term.setFont(term.BOLD);
                                continue;
                            }
                            if(intarg[i]==4){ // underscore
                                term.setFont(term.UNDER);
                                continue;
                            }
                            if(intarg[i]==5){ // blink
                                term.setFGround(java.awt.Color.cyan);
                                //term.setFont(term.BLINK);
                                continue;
                            }
                            if(intarg[i]==7){ // rev
                                term.setFont(term.REVERSE);
                                term.setFGround(bground);
                                term.setBGround(fground);
                                continue;
                            }
                        }
                        continue;
                    }
                    
                    if(b=='n'){   // DSR: Device status report
                        if (intarg[0]==0) System.out.println("VT100-Ready-Wrong direction");
                        if (intarg[0]==5) term.sendKeySeq(getDSR());
                        if (intarg[0]==6) {
                            String seq = (char)(0x1b) + "[";
                            seq = seq + String.valueOf(x/char_width+1) + ";" + String.valueOf(y/char_height) + "R";
                            //term.sendKeySeq(seq.getBytes());
                        }
                        continue;
                    }
                    
                    if(b=='r'){
                        if (!gotdigits) {intarg[0]=1; intarg[1]=term_height;}
                        if (intarg[1]==0) intarg[1]=term_height;
                        if (intarg[1]<=intarg[0]) intarg[1]=intarg[0]+1;
                        region_y1=intarg[0];
                        region_y2=intarg[1];
                        continue;
                    }
                    if(b=='q'){   // leds
                        if(!gotdigits){ intarg[0]=0;}
                        for (int i=0; i<=intargi; i++) {
                            term.setLEDs(intarg[i]);
                            try { Thread.sleep(100);
                            } catch (Exception e) {}
                        }
                        continue;
                    }
                    
                    err("ESC [", b) ;
                    continue;
                }
                
                if (ignore) { //during special sequence
                    continue;
                }
                
                if(b==0x03){ //ETX: ignore
                    continue;
                }
                
                if(b==0x07){ //BEL: bell, beep
                    term.beep();
                    continue;
                }
                
                if(b==0x08){ //BS: Backspace
                    term.draw_cursor();
                    x -= char_width;
                    if(x<0){
                        y-=char_height;
                        if (y>0) x=(term_width-1)*char_width;
                        else  {x=0; y=char_height;}
                    }
                    term.setCursor(x, y);
                    term.draw_cursor();
                    continue;
                }
                
                if(b==0x09){    // HT: Horizontal tab
                    int t;
                    try  {
                        for (t=x/char_width+1;t<term_width;t++) if (tab[t]==true) break;
                        x=t*char_width;
                    } catch (Exception e) {System.out.println(e);}
                    //x=(((x/char_width)/tab+1)*tab*char_width);
                    if(x>=term_width*char_width){
                        x=0;
                        y+=char_height;
                        if(y>region_y2*char_height) addLine();
                    }
                    term.draw_cursor();
                    term.setCursor(x, y);
                    term.draw_cursor();
                    continue;
                }
                
                if(b==0x0e){ //SO: shift out, use G1
                    char_set = 1;
                    continue;
                }
                if(b==0x0f){ //SI: shift in, use G0
                    char_set = 0;
                    continue;
                }
                
                if(b==0x0d){ //CR: return
                    term.draw_cursor();
                    term.setFont(term.SINGLE_W_H);
                    x=0;
                    term.setCursor(x, y);
                    term.draw_cursor();
                    //if (arch.startsWith("Mac")) pushChar((byte) 0x0a); //append LF to CR
                    continue;
                }
                
                if(b!=0x0a & b!=0x0b & b!=0x0c){   // LF:  VT, FF
                    if (b>=0x00 & b<0x1f) err("CTL", b) ;
                    if(x>=term_width*char_width){
                        x=0;
                        y+=char_height;
                        if(y>region_y2*char_height) addLine();
                        rx=x;
                        ry=y;
                    }
                    
                    term.draw_cursor();
                    
                    if((b&0x80)!=0){
                        term.clear_area(x, y-char_height, x+char_width*2, y);
                        byte[] foo=new byte[2];
                        foo[0]=b;
                        foo[1]=getChar();
                        term.drawString(new String(foo, 0, 2, "EUC-JP"), x, y);
                        x+=char_width;
                        x+=char_width;
                        w=char_width*2;
                        h=char_height;
                    } else {
                        //pushChar(b);
                        term.clear_area(x, y-char_height, x+char_width, y);
                        char[] b2=new char[1];
                        //b = getChar();
                        //if (graphics && 0x5f <= b && b <= 0x7e) {
                        if ((0x5f <= b && b <= 0x7e) & (char_set==0 & g0_graph | char_set==1 & g1_graph)) {
                            b2[0] = vt100_graphics[b-0x5f];
                        } else b2[0]= c;
                        term.drawChars(b2, 0, 1, x, y);
                        rx = x;
                        ry = y;
                        x+=(char_width);
                        w=char_width;
                        h=char_height;
                    }
                    term.redraw(rx, ry-char_height,  w, h);
                    term.setCursor(x, y);
                    term.draw_cursor();
                } else { // LF: Linefeed, VT: vertical tab, FF: formfeed
                    term.draw_cursor();
                    if (b==0x0c)  { //FF
                        //x=0;
                        //y=char_height;
                        //term.clear_area(x, y-char_height,
                        //term_width*char_width, term_height*char_height);
                        //term.redraw(x, y-char_height,
                        //term_width*char_width-x,
                        //term_height*char_height-y+char_height);
                    } else { //LF and VT
                        if (newline==true) { //CRLF (need this for Unix systems LF only)
                            term.clear_area(x, y-char_height, x+char_width, y);
                            x=0;
                        }
                        y+=char_height;
                    }
                    term.setCursor(x, y);
                    term.draw_cursor();
                }
                
                //addLine();
                if(y==(region_y2+1)*char_height) addLine();
            }
        }
        catch(Exception e){}
    }
    
    private void err(String what, byte b) {
        System.out.println(what + ": " + ((char) b) + ":" + Integer.toHexString(b&0xff));
    }
    
    private void getDigits() { //return negative arrax elements for each ?
        intargi=0;
        intarg[0] = 0;
        gotdigits = false;
        boolean qm = false;
        while(true){
            try {
                b=getChar();
                if (b=='?') {
                    qm = true;
                    continue;
                }
                if(b==';'){
                    if (qm) intarg[intargi] = -intarg[intargi];
                    qm = false;
                    intargi++;
                    intarg[intargi]=0;
                    continue;
                }
                if('0'<=b && b<='9'){
                    intarg[intargi]=intarg[intargi]*10+(b-'0');
                    gotdigits = true;
                    continue;
                }
                if (qm) intarg[intargi] = -intarg[intargi];
                pushChar(b);
                break;
            } catch(Exception e){}
            
        }
    }
    private void addLine() {
        term.draw_cursor();
        y-=char_height;
        term.scroll_window((region_y1)*char_height,(region_y2)*char_height,-char_height, bground, smooth);
        term.setCursor(x, y);
        term.draw_cursor();
    }
    
    private static int[] ENTER={(int)0x0d};
    private static int[] BS={(int)0x7f};
    private static int[] DEL={(int)0x7f};
    private static int[] ESC={(int)0x1b};
    private static int[] UP={(int)0x1b, (int)'[', (int)'A'};
    private static int[] DOWN={(int)0x1b, (int)'[', (int)'B'};
    private static int[] RIGHT={(int)0x1b, (int)'[', (int)'C'};
    private static int[] LEFT={(int)0x1b, (int)'[', (int)'D'};
    private static int[] F1={(int)0x1b, (int)'[', (int)'P'};
    private static int[] F2={(int)0x1b, (int)'[', (int)'Q'};
    private static int[] F3={(int)0x1b, (int)'[', (int)'R'};
    private static int[] F4={(int)0x1b, (int)'[', (int)'S'};
    private static int[] F5={(int)0x1b, (int)'[', (int)'t'};
    private static int[] F6={(int)0x1b, (int)'[', (int)'u'};
    private static int[] F7={(int)0x1b, (int)'[', (int)'v'};
    private static int[] F8={(int)0x1b, (int)'[', (int)'I'};
    private static int[] F9={(int)0x1b, (int)'[', (int)'w'};
    private static int[] F10={(int)0x1b, (int)'[', (int)'x'};
    private static int[] DSR={(int)0x1b, (int)'[', (int)'0', (int)'n'};
    private static int[] CPR={(int)0x1b, (int)'['}; //, (int)'0', (int)'0', (int)';', (int)'0', (int)'0', (int)'R'};
    
    public int[] getCodeENTER(){ return ENTER; }
    public int[] getCodeBS(){ return BS; }
    public int[] getCodeDEL(){ return DEL; }
    public int[] getCodeESC(){ return ESC; }
    public int[] getCodeUP(){ return UP; }
    public int[] getCodeDOWN(){ return DOWN; }
    public int[] getCodeRIGHT(){ return RIGHT; }
    public int[] getCodeLEFT(){ return LEFT; }
    public int[] getCodeF1(){ return F1; }
    public int[] getCodeF2(){ return F2; }
    public int[] getCodeF3(){ return F3; }
    public int[] getCodeF4(){ return F4; }
    public int[] getCodeF5(){ return F5; }
    public int[] getCodeF6(){ return F6; }
    public int[] getCodeF7(){ return F7; }
    public int[] getCodeF8(){ return F8; }
    public int[] getCodeF9(){ return F9; }
    public int[] getCodeF10(){ return F10; }
    public int[] getDSR(){ return DSR; }
    public int[] getCPR(){ return CPR; }
    
    public void reset(){
        arch = System.getProperty("os.name");
        term_width=term.getColumnCount();
        term_height=term.getRowCount();
        char_width=term.getCharWidth();
        char_height=term.getCharHeight();
        
        fground = bfground;
        term.setFGround(fground);
        bground = bbground;
        term.setBGround(bground);
        
        cursor_appl = false;
        ansi = false;
        col132 = false;
        smooth = false;
        reverse = false;
        org_rel = false;
        wrap = false;
        repeat = false;
        interlace = false;
        newline = false;
        
        tab = new boolean[term_width];
        for (x=0;x<term_width;x++) {
            if (x%8==0) tab[x]=true;
            else tab[x]=false;
        }
        
        term.setFont(term.NORMAL);
        char_set = 0;
        g0_graph = false;
        x=0;
        y=char_height;
        region_y1=1;
        region_y2=term_height;
        term.clear_area(x, y-char_height,
        term_width*char_width, term_height*char_height);
        term.redraw(x, y-char_height,
        term_width*char_width-x,
        term_height*char_height-y+char_height);
        term.setCursor(x, y);
        term.draw_cursor();
    }
    
    
}
