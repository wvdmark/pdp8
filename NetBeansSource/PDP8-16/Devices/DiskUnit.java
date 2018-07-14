/*
 * DiskUnit.java
 *
 * Created on May 29, 2005, 11:20 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Devices;

import java.awt.*;
//import java.awt.event.*;
import java.awt.geom.*;
//import java.beans.*;

public class DiskUnit extends javax.swing.JComponent {

    private Devices.Disk4043 d43;
    private Heads doit;
    private int unit = 0;
    private boolean first = false;
    private int width;
    private int height;
    private int pos;
    private Color back;
    private Color dark;
    private int local_track;
    private Thread update;

    private final static Dimension MIN_SIZE = new Dimension(150, 20);
    private final static Dimension PREF_SIZE = new Dimension(300, 40);

    // Set the antialiasing to get the right look!
    private final static RenderingHints AALIAS =
    new RenderingHints(RenderingHints.KEY_ANTIALIASING,
    RenderingHints.VALUE_ANTIALIAS_ON);
    Stroke defaultStroke = new BasicStroke(1);
    Stroke diskStroke = new BasicStroke(2);
    Stroke headStroke = new BasicStroke(4);


    /** Creates a new instance of DiskUnit */
    public DiskUnit() {
        setPreferredSize(PREF_SIZE);
        setMinimumSize(MIN_SIZE);
        setBackground(new java.awt.Color(100, 100, 100));
        doit = new Heads();
    }

    public void startUnit(Devices.Disk4043 disk, String name) {
        this.d43 = disk;
        first = true;
        update = new Thread(doit, name);
        update.start();
    }

    public int getUnit() {
        return this.unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    @Override
    public Color getBackground(){
        return back;
    }

    @Override
    public void setBackground(Color back){
        this.back = back;
        this.dark = back.darker().darker();
        repaint();
    }

    // Paint the Unit
    @Override
    public void paint(Graphics g1d) {

        width = getWidth();
        height = getHeight();

        Graphics2D g = (Graphics2D) g1d;
        g.setStroke(defaultStroke);
        g.setColor(back);
        g.fillRect(0,0,width,height);
        g.setColor(Color.black);
        g.setStroke(diskStroke);
        g.drawLine(0, 0, width, 0);
        g.drawLine(0, height, width, height);
        if (first==true) {
            if (d43.si3040.sel[unit][0]==true | d43.si3040.sel[unit][1]==true) {
                g.setStroke(diskStroke);
                g.setColor(new java.awt.Color(193, 132, 28));
                g.fillRect(width*7/20, height*1/10, width*1/10, height*8/10);
                g.setColor(new java.awt.Color(193, 73, 28));
                if (d43.si3040.sel[unit][0]==true)
                    g.drawLine(width/10, height*3/10, width*7/10, height*3/10);
                if (d43.si3040.sel[unit][1]==true)
                    g.drawLine(width/10, height*7/10, width*7/10, height*7/10);
                g.setColor(Color.black);
                g.setStroke(diskStroke);
                g.drawLine(width*15/20, height*2/20, width, height*2/20);
                g.drawLine(width*15/20, height*18/20, width, height*18/20);
                pos = (local_track*width*3/(20*0630)) + (width/40);
                AffineTransform a = AffineTransform.getTranslateInstance(-pos,0);
                g.transform(a);
                g.setColor(new java.awt.Color(153, 53, 0));
                g.setStroke(diskStroke);
                g.fillRect(width*18/20, height*3/20, width*1/10, height*14/20);
                g.setColor(Color.white);
                g.drawLine(width*13/20, height*2/10, width*18/20, height*2/10);
                g.drawLine(width*13/20, height*4/10, width*18/20, height*4/10);
                g.drawLine(width*13/20, height*6/10, width*18/20, height*6/10);
                g.drawLine(width*13/20, height*8/10, width*18/20, height*8/10);
                g.setStroke(headStroke);
                g.drawLine(width*13/20, height*2/10, width*14/20, height*2/10);
                g.drawLine(width*13/20, height*4/10, width*14/20, height*4/10);
                g.drawLine(width*13/20, height*6/10, width*14/20, height*6/10);
                g.drawLine(width*13/20, height*8/10, width*14/20, height*8/10);
            }
        }
    }

    public class Heads implements Runnable {
        int sleep = 20;
        public void run() {
            while (true) {
                try {
                    if (local_track != d43.si3040.track[unit]) {
                        local_track = d43.si3040.track[unit];
                        repaint();
                        sleep=20;
                    }
                    Thread.sleep(sleep);
                    sleep++;

                } catch (InterruptedException e) {System.out.println("Track error" + e);}
            }
        }

        public Heads() {
        }

    }
};



