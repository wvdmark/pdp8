/*
 * Term.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Devices;

public interface Term{
    
    public static final int NORMAL = 00;
    public static final int BOLD= 01;
    public static final int UNDER = 02;
    public static final int BLINK = 04;
    public static final int REVERSE = 010;
    public static final int SINGLE_W_H = 020;
    public static final int DOUBLE_W = 040;
    public static final int DOUBLE_T = 0100; //double height top
    public static final int DOUBLE_B = 0200; //double height bottom
    
    int getRowCount();
    int getColumnCount();
    int getCharWidth();
    int getCharHeight();
    
    void setFont(int attr);
    void setCursor(int x, int y);
    void setLEDs(int x);
    void clear();
    void draw_cursor();
    void redraw(int x, int y, int width, int height);
    
    void clear_area(int x1, int y1, int x2, int y2);
    //void scroll_area(int x, int y, int w, int h, int dx, int dy, Object background);
    void scroll_window(int to, int from, int dy, java.awt.Color background, boolean scroll_mode);
    void drawBytes(byte[] buf, int s, int len, int x, int y);
    void drawChars(char[] buf, int s, int len, int x, int y);
    void drawString(String str, int x, int y);
    void beep();
    void sendKeySeq(int[] keyseq);
    boolean sendOutByte(int outbyte);

    
    void setFGround(java.awt.Color foreground);
    void setBGround(java.awt.Color background);
    java.awt.Color getFGround();
    java.awt.Color getBGround();
}
