/*
 * Terminal.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Devices;

//import Devices.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
//import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;
//import java.awt.image.renderable.*;
//import java.text.*;

public class Terminal extends JPanel implements KeyListener, ActionListener, ItemListener, Term {
    static String COPYRIGHT=
    "PDP8 - VT52/VT100 3.0\nCopyright (C) 2004-2020 W. van der Mark\n"+
    "This software is licensed under GNU LGPL.";
    
    private OutputStream out;
    private SendOutput output;
    private SendFileInput fileinput;
    private Thread fileinth;
    private InputStream in;
    private ReceiveInput input;
    private ReceiveFileOutput fileoutput;
    private Emulator emulator=null;
    
    
    private JFrame frame;
    private Devices.VT100Lamps lamps;
    private BufferedImage img;
    private BufferedImage background;
    private Graphics2D cursor_graphics;
    private Graphics2D graphics;
    private java.awt.Color bground=Color.white;
    private java.awt.Color fground=Color.black;
    private java.awt.Component term_area=null;
    private java.awt.Font primfont;
    private java.awt.Font font;
    
    private int term_width=82;
    private int term_height=24;
    
    private int x=0;
    private int y=0;
    private int descent=0;
    
    private int char_width;
    private int char_height;
    private boolean underline = false;
    private int save_width;
    private int style = Font.PLAIN;
    private AffineTransform at = new AffineTransform();
    private int clip = 0;
    private int lampState = 0100; // online
    
    private boolean antialiasing=true;
    private int line_space=1;
    
    private Logic.TTY channel;
    
    public Terminal(Logic.TTY channel){
        this.channel = channel;
        font = new java.awt.Font("Monospaced",Font.PLAIN,12);
        at.setToScale(1,1); //7.0/9.0,1);
        primfont = font.deriveFont(Font.PLAIN, at);
        font = primfont.deriveFont(Font.PLAIN);
        
        img=new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        graphics=(Graphics2D)(img.getGraphics());
        graphics.setFont(font); {
            FontMetrics fo = graphics.getFontMetrics();
            descent=fo.getDescent();
            char_width=(int)(fo.charWidth((char)'W'));
            save_width = char_width;
            char_height=(int)(fo.getHeight())+(line_space*2);
            descent+=line_space;
        }
        img.flush();
        graphics.dispose();
        
        background=new BufferedImage(char_width, char_height, BufferedImage.TYPE_INT_RGB); {
            Graphics2D foog=(Graphics2D)(background.getGraphics());
            foog.setColor(bground);
            foog.fillRect(0, 0, char_width, char_height);
            foog.dispose();
        }
        
        img=new BufferedImage(getTermWidth(), getTermHeight(), BufferedImage.TYPE_INT_RGB);
        graphics=(Graphics2D)(img.getGraphics());
        graphics.setFont(font);
        
        clear();
        
        cursor_graphics=(Graphics2D)(img.getGraphics());
        cursor_graphics.setColor(fground);
        cursor_graphics.setXORMode(bground);
        
        setAntiAliasing(antialiasing);
        
        term_area=this;
        
        JPanel panel=this;
        panel.setPreferredSize(new Dimension(getTermWidth(), getTermHeight()));
        panel.setSize(getTermWidth(), getTermHeight());
        panel.setFocusable(true);
        panel.enableInputMethods(true);
        panel.setFocusTraversalKeysEnabled(false);
        
        Terminal term = this;
        
        frame=new JFrame("VT100 Terminal");
        
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent evt){
                exitForm(evt);
            }
        });
        
        lamps = new Devices.VT100Lamps();
        java.awt.Dimension padim = lamps.setPanel(term);
        JMenuBar mb=term.getJMenuBar();
        frame.setJMenuBar(mb);
        frame.getContentPane().add(lamps, java.awt.BorderLayout.CENTER);
        frame.setBackground(java.awt.Color.darkGray);
        frame.pack();
        term.setVisible(true);
        frame.setVisible(true);
        setLEDs(0);
        term.setFrame(term);
        
        try{
            out=new PipedOutputStream();
            in=new PipedInputStream();
            channel.setInputOutputStream((InputStream)(new PipedInputStream((PipedOutputStream)out)),
            (OutputStream) new PipedOutputStream((PipedInputStream)in));
        }
        catch(Exception e){
            //e.printStackTrace();
        }
        output = new SendOutput();
        new Thread(output,"Terminal-Output").start();
        emulator=new VT100(this, in);
        input = new ReceiveInput();
        new Thread(input,"Terminal-Input").start();
        fileinput = new SendFileInput();
        fileoutput = new ReceiveFileOutput();
    }
    
    public void setFrame(java.awt.Component term_area){
        this.term_area=term_area;
    }
    
    @Override
    public void paintComponent(Graphics g){
        g.drawImage(img, 0, 0, term_area);
    }
    
    @Override
    public void update(Graphics g){
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
    }
    
    @Override
    public void processKeyEvent(KeyEvent e){
        //System.out.println(e);
        int id=e.getID();
        if(id == KeyEvent.KEY_PRESSED) { keyPressed(e); }
        else if(id == KeyEvent.KEY_RELEASED) {}
        else if(id == KeyEvent.KEY_TYPED) { keyTyped(e); }
    }
    
    int[] obuffer=new int[3];
    public void keyPressed(KeyEvent e){
        //System.out.println(e);
        int keycode=e.getKeyCode();
        int location=e.getKeyLocation();
        int[] code=null;
        switch(keycode){
            case KeyEvent.VK_CONTROL:
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_ALT:
            case KeyEvent.VK_ALT_GRAPH:
            case KeyEvent.VK_CAPS_LOCK:
                return;
            case KeyEvent.VK_BACK_SPACE:
                code=emulator.getCodeBS();
                break;
            case KeyEvent.VK_DELETE:
                code=emulator.getCodeDEL();
                break;
            case KeyEvent.VK_ESCAPE:
                code=emulator.getCodeESC();
                break;
            case KeyEvent.VK_SUBTRACT:
                code=emulator.getCodeSubtract(); 
                break;
            case KeyEvent.VK_ADD:
                code=emulator.getCodeComma(); //numpad plus used for ,
                break;
            case KeyEvent.VK_DECIMAL:
                code=emulator.getCodeDecimal(); 
                break;
            case KeyEvent.VK_ENTER:
                if (location==KeyEvent.KEY_LOCATION_NUMPAD)
                code=emulator.getCodeENTER();
                else
                code=emulator.getCodeRETURN();
                break;
            case KeyEvent.VK_NUMPAD1:
                code=emulator.getCodeNP1(); 
                break;
            case KeyEvent.VK_NUMPAD2:
                code=emulator.getCodeNP2(); 
                break;
            case KeyEvent.VK_NUMPAD3:
                code=emulator.getCodeNP3(); 
                break;
            case KeyEvent.VK_NUMPAD4:
                code=emulator.getCodeNP4(); 
                break;
            case KeyEvent.VK_NUMPAD5:
                code=emulator.getCodeNP5(); 
                break;
            case KeyEvent.VK_NUMPAD6:
                code=emulator.getCodeNP6(); 
                break;
            case KeyEvent.VK_NUMPAD7:
                code=emulator.getCodeNP7(); 
                break;
            case KeyEvent.VK_NUMPAD8:
                code=emulator.getCodeNP8(); 
                break;
            case KeyEvent.VK_NUMPAD9:
                code=emulator.getCodeNP9(); 
                break;
            case KeyEvent.VK_UP:
                code=emulator.getCodeUP();
                break;
            case KeyEvent.VK_DOWN:
                code=emulator.getCodeDOWN();
                break;
            case KeyEvent.VK_RIGHT:
                code=emulator.getCodeRIGHT();
                break;
            case KeyEvent.VK_LEFT:
                code=emulator.getCodeLEFT();
                break;
            case KeyEvent.VK_F1:
                code=emulator.getCodeF1();
                break;
            case KeyEvent.VK_F2:
                code=emulator.getCodeF2();
                break;
            case KeyEvent.VK_F3:
                code=emulator.getCodeF3();
                break;
            case KeyEvent.VK_F4:
                code=emulator.getCodeF4();
                break;
        }
        if(code!=null){
            output.setOutput(code,code.length);
            e.consume();
            return;
        }
        char keychar=e.getKeyChar();
    }
    
    public void keyTyped(KeyEvent e){
        char keychar=e.getKeyChar();
        int modifiers = e.getModifiers();
        int xx = modifiers & InputEvent.CTRL_MASK;
        if (!Character.isISOControl(keychar) & keychar<0200 | xx!=0) {
            obuffer[0]=(int)(e.getKeyChar());
            output.setOutput(obuffer,1);
        }
    }
    
    public void sendKeySeq(int[] keyseq){
        output.setOutput(keyseq,keyseq.length);
    }
    
    public boolean sendOutByte(int outbyte){
        return fileoutput.sendByte(outbyte);
    }
    
    public int getTermWidth(){ return char_width*term_width; }
    public int getTermHeight(){ return char_height*term_height; }
    public int getCharWidth(){ return char_width; }
    public int getCharHeight(){ return char_height; }
    public int getColumnCount(){ return term_width; }
    public int getRowCount(){ return term_height; }
    
    public void clear(){
        graphics.setColor(bground);
        graphics.fillRect(0, 0, char_width*term_width, char_height*term_height);
        graphics.setColor(fground);
    }
    
    public void setCursor(int x, int y){
        this.x=x;
        this.y=y;
    }
    
    public void setLEDs(int x){
        if (x==0) lampState &= 0760;
        if (x>0) lampState |= 1 << (x-1);
        if (x<0) lampState &= ~(1 << (-x-1));
        lamps.setState(lampState); //new
    }
    
    public void draw_cursor(){
        cursor_graphics.fillRect(x, y-char_height, char_width, char_height);
        term_area.repaint(x, y-char_height, char_width, char_height);
    }
    
    public void redraw(int x, int y, int width, int height){
        term_area.repaint(x, y, width, height);
    }
    
    public void clear_area(int x1, int y1, int x2, int y2){
        for(int i=y1; i<y2; i+=char_height){
            for(int j=x1; j<x2; j+=char_width){
                graphics.drawImage(background, j, i, term_area);
            }
        }
    }
    
    public void setFont(int attr) {
        if (attr==NORMAL) {
            //at.setToScale(1,1);
            style = Font.PLAIN;
            underline = false;
            //char_width = save_width;
            //clip = 0;
        }
        if (attr == BOLD) style = Font.BOLD;
        if (attr == UNDER) underline = true;
        if (attr==SINGLE_W_H) {
            at.setToScale(1,1);
            char_width = save_width;
            clip = 0;
        }
        if (attr == DOUBLE_W) {
            at.setToScale(2,1);
            char_width = 2*save_width;
            clip = 0;
        }
        if (attr == DOUBLE_T) {
            at.setToScale(2,2);
            char_width = 2*save_width;
            clip = char_height/2;
        }
        if (attr == DOUBLE_B) {
            at.setToScale(2,2);
            char_width = 2*save_width;
            clip = -char_height/2;
        }
        font = primfont.deriveFont(style, at);
        graphics.setFont(font);
    }
    
    
    public void scroll_window(int top, int bot, int dy, java.awt.Color back, boolean smooth){
        int w = getTermWidth();
        if (smooth) {
            java.awt.Color tcolor = graphics.getColor();
            graphics.setColor(back);
            try {
                if (dy>0) {
                    for (int yt=top;yt<top+dy;yt++) {
                        graphics.drawLine(0,yt,w,yt);
                        graphics.copyArea(0, yt, w, bot-top, 0, 1);
                        term_area.repaint(0, yt, w, yt+bot-top);
                        Thread.sleep(10);
                    }
                } else {
                    for (int yt=top;yt>top+dy;yt--) {
                        graphics.drawLine(0,yt+bot-top,w,yt+bot-top);
                        graphics.copyArea(0, yt, w, bot-top, 0, -1);
                        term_area.repaint(0, yt, w, yt+bot-top);
                        Thread.sleep(10);
                    }
                }
            } catch (Exception e) {}
            graphics.setColor(tcolor);
        } else {
            //java.awt.Color tcolor = getBGround();
            //setBGround(back);
            graphics.copyArea(0, top, w, bot-top, 0, dy);
            if (dy>0) clear_area(0, top, w, top+char_height); else clear_area(0, bot-char_height, w, bot);
            redraw(0, top-char_height, w, bot+char_height);
            //setBGround(tcolor);
        }
    }
    
    public void drawBytes(byte[] buf, int s, int len, int x, int y){
        graphics.drawBytes(buf, s, len, x, y-descent);
    }
    
    public void drawChars(char[] buf, int s, int len, int x, int y) {
        graphics.clipRect(x,y-char_height, char_width, char_height);
        graphics.drawChars(buf, s, len, x, y-descent+clip);
        if (underline) graphics.drawLine(x, y-2, x + len*char_width, y-2);
        graphics.setClip(null);
    }
    
    public void drawString(String str, int x, int y){
        graphics.drawString(str, x, y-descent);
    }
    
    public void beep(){
        Toolkit.getDefaultToolkit().beep();
    }
    
    /** Ignores key released events. */
    public void keyReleased(KeyEvent event){}
    
    public void setLineSpace(int foo){this.line_space=foo;}
    
    public void setAntiAliasing(boolean foo){
        if(graphics==null) return;
        antialiasing=foo;
        java.lang.Object mode=foo?
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON:
            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
            RenderingHints hints=
            new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, mode);
            graphics.setRenderingHints(hints);
    }
    
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if(action.equals("Open Reader...")){
            openReaderActionPerformed( e);
        }
        else if(action.equals("Close Reader")){
            fileinput.setFileInput(null);
            channel.setReader(false);
            emulator.reset();
            return;
        }
        else if(action.equals("Open Punch...")){
            openPunchActionPerformed( e);
        }
        else if(action.equals("Close Punch")){
            fileoutput.setFileOutput(null);
        }
        else if(action.equals("Loop")){
            // item change
        }
        else if(action.equals("About...")){
            JOptionPane.showMessageDialog(this, COPYRIGHT);
            return;
        }
        else if (action.equals("Quit")){
            quit();
        }
    }
    
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();
        if (e.getStateChange()==ItemEvent.SELECTED) emulator.setLoopback(true);
        else emulator.setLoopback(false);
    }
    
    
    private void openReaderActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        chooser.addChoosableFileFilter(new ImageFileFilter());
        int option = chooser.showOpenDialog(this);
        if (option == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();
            if (file != null) {
                channel.setReader(true);
                fileinput.setFileInput(file);
                fileinth = new Thread(fileinput,"Terminal-Reader");
                fileinth.start();
            } else System.out.println("No file selected");
        }
    }
    
    private void openPunchActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        chooser.addChoosableFileFilter(new ImageFileFilter());
        int option = chooser.showOpenDialog(this);
        if (option == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();
            if (file != null) {
                fileoutput.setFileOutput(file);
                new Thread(fileoutput,"Terminal-Punch").start();
                emulator.setPunch();
            }
        }
    }
    
    public JMenuBar getJMenuBar(){
        JMenuBar mb=new JMenuBar();
        JMenu m;
        JMenuItem mi;
        
        mb.setBackground(Color.darkGray);
        mb.setBorder(null);
        
        m=new JMenu("Reader - Punch");
        m.setBackground(Color.darkGray);
        m.setForeground(Color.white);
        m.setBorder(null);
        mi=new JMenuItem("Open Reader...");
        mi.addActionListener(this);
        mi.setActionCommand("Open Reader...");
        mi.setBackground(Color.darkGray);
        mi.setForeground(Color.white);
        mi.setBorder(null);
        m.add(mi);
        mi=new JMenuItem("Close Reader");
        mi.addActionListener(this);
        mi.setActionCommand("Close Reader");
        mi.setBackground(Color.darkGray);
        mi.setForeground(Color.white);
        mi.setBorder(null);
        m.add(mi);
        m.addSeparator();
        mi=new JMenuItem("Open Punch...");
        mi.addActionListener(this);
        mi.setActionCommand("Open Punch...");
        mi.setBackground(Color.darkGray);
        mi.setForeground(Color.white);
        mi.setBorder(null);
        m.add(mi);
        mi=new JMenuItem("Close Punch");
        mi.addActionListener(this);
        mi.setActionCommand("Close Punch");
        mi.setBackground(Color.darkGray);
        mi.setForeground(Color.white);
        mi.setBorder(null);
        m.add(mi);
        m.addSeparator();
        mi=new JMenuItem("Quit");
        mi.addActionListener(this);
        mi.setActionCommand("Quit");
        mi.setBackground(Color.darkGray);
        mi.setForeground(Color.white);
        mi.setBorder(null);
        m.add(mi);
        mb.add(m);
        
        m=new JMenu("Test - About");
        m.setBackground(Color.darkGray);
        m.setForeground(Color.white);
        m.setBorder(null);
        mi=new JCheckBoxMenuItem("Loopback");
        mi.addItemListener(this);
        mi.setActionCommand("Loopback");
        mi.setBackground(Color.darkGray);
        mi.setForeground(Color.white);
        mi.setBorder(null);
        m.add(mi);
        m.addSeparator();
        mi=new JMenuItem("About...");
        mi.addActionListener(this);
        mi.setActionCommand("About...");
        mi.setBackground(Color.darkGray);
        mi.setForeground(Color.white);
        mi.setBorder(null);
        m.add(mi);
        mb.add(m);
        
        return mb;
    }
    
    private void exitForm(java.awt.event.WindowEvent evt) {
        channel.data.CloseAllDevs();
        //System.exit(0);
    }
    
    public void quit(){
    }
    
    public void setFGround(java.awt.Color f){
        fground = f;
        graphics.setColor(f);
    }
    
    public void setBGround(java.awt.Color b){
        bground = b;
        background=new BufferedImage(char_width, char_height, BufferedImage.TYPE_INT_RGB); {
            Graphics2D foog=(Graphics2D)(background.getGraphics());
            foog.setColor(b);
            foog.fillRect(0, 0, char_width, char_height);
            foog.dispose();
        }
    }
    
    public java.awt.Color getFGround(){ return fground; }
    
    public java.awt.Color getBGround(){ return bground; }
    
    public class SendOutput implements Runnable {
        private int[] code= new int[4];
        private int codelen;
        private int temp;
        private File file;
        private FileInputStream filein;
        public void run() {
            while (true) {
                synchronized(this){
                    try {
                        while (codelen==0) wait();
                        for (int i=0;i<codelen;i++) {
                            temp = (code[i] & 0x7F) + (code[i]<0?128:0);
                            out.write(code[i]);
                            out.flush();
                        }
                        //code = null;
                        codelen = 0;
                    } catch(Exception e){System.out.println("SendOutput=>" + e);}
                }
            }
        }
        public void setOutput(int [] codein, int length) {
            synchronized(this) {
                code = codein;
                codelen=length;
                notifyAll();
            }
        }
    };
    
    public class SendFileInput implements Runnable {
        private byte[] code=null;
        private int codelen;
        private File file;
        private FileInputStream filein;
        public void run() {
            try {
                setLEDs(5);
                filein = new FileInputStream(file);
                code = new byte[512];
                while ((codelen= filein.read(code))>0) {
                    //System.out.println(codelen);
                    out.write(code, 0,  codelen);
                }
                filein.close();
                file = null;
                setLEDs(-5);
            } catch (IOException e) {
                file = null;
                setLEDs(-5);}
        }
        
        public void setFileInput(File fileinput) {
            if (fileinput==null) {
                if (fileinth!=null) fileinth.interrupt();
            } else file = fileinput;
        }
    };
    
    public class ReceiveInput implements Runnable {
        public void run() {
            while (true) {
                emulator.reset();
                emulator.Start();
            }
        }
    };
    
    public class ReceiveFileOutput implements Runnable {
        private File file;
        public int outbyte = -1;
        private BufferedOutputStream streamout;
        public void run() {
            try {
                setLEDs(5);
                streamout = new BufferedOutputStream(new FileOutputStream(file));
                while (file!=null) {
                    try {
                        if (outbyte>=0) {
                            streamout.write(outbyte);
                            outbyte = -1;
                        } else {
                            Thread.sleep(1);
                        }
                    } catch (InterruptedException e) {System.out.println("File output error" + e);}
                }
                streamout.flush();
                streamout.close();
                file = null;
                setLEDs(-5);
            } catch (IOException e) {}
        }
        
        public void setFileOutput(File fileout) {
            file = fileout;
        }
        public boolean sendByte(int sendbyte) {
            while (true) {
                try {
                    if (outbyte==-1) {
                        outbyte = sendbyte;
                        return true;
                    } else if (file==null) {
                        return false;
                    }
                    else Thread.sleep(1);
                } catch (InterruptedException e) {}
            }
        }
    };
    
    /** Define custom file filter for acceptable image files.
     */
    private static class ImageFileFilter extends javax.swing.filechooser.FileFilter {
        
        public boolean accept(java.io.File file) {
            if (file == null)
                return false;
            return file.isDirectory() || file.getName().toLowerCase().endsWith(".pt")
            || file.getName().toLowerCase().endsWith(".bin") ;
        }
        
        public String getDescription() {
            return "Reader-Punch files (*.pt,*.bin)";
        }
        
    }
    
}

