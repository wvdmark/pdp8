/*
 * PTape.java
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

public class PTape extends javax.swing.JComponent {

    private Devices.PtrPtp pt;
    private Turn doit;
    private int unit = 0;
    private boolean right = false;
    private boolean first = false;
    private int width;
    private int height;
    private int twidth;
    private int diam;
    private Color back;
    private Color dark;
    private int local_line;
    private int tape_line;
    private int layers;
    private double start;
    private double pos;
    private int hole;
    private int[] holes = new int[80];
    //private int tape_radius;
    private Thread update;

    private final static float PI = (float)Math.PI;
    private final static float MULTIP = PI/180;
    private final static Dimension MIN_SIZE = new Dimension(74, 76);
    private final static Dimension PREF_SIZE = new Dimension(148, 148);

    // Set the antialiasing to get the right look!
    private final static RenderingHints AALIAS =
    new RenderingHints(RenderingHints.KEY_ANTIALIASING,
    RenderingHints.VALUE_ANTIALIAS_ON);
    Stroke defaultStroke = new BasicStroke(1);
    Stroke tapeStroke = new BasicStroke(1);


    /** Creates a new instance of DTReel */
    public PTape() {
        setPreferredSize(PREF_SIZE);
        setMinimumSize(MIN_SIZE);
        setBackground(new java.awt.Color(100, 100, 100));
        for (int i=0;i<80;i++) holes[i] = 0;
        doit = new Turn();
    }


    public void startPT(Devices.PtrPtp pt,String name) {
        this.pt = pt;
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

    public boolean getRight(){
        return right;
    }

    public void setRight(boolean right){
        this.right = right;
        repaint();
    }

    @Override
    public Color getBackground(){
        return back;
    }

    @Override
    public void setBackground(Color back){
        this.back = back;
        this.dark = back.darker().darker();
    }

    // Paint the DKnob
    @Override
    public void paint(Graphics g1d) {

        width = getWidth();
        height = getHeight();

        if (unit==0) {
            if (!right) tape_line = local_line-100; else if (first) tape_line = pt.tapesize[unit] - local_line;

            Graphics2D g = (Graphics2D) g1d;
            AffineTransform a = AffineTransform.getScaleInstance(-1,1);
            a.translate(-width,0);
            if (right) g.transform(a);
            g.setBackground(back);
            g.addRenderingHints(AALIAS);
            g.setStroke(defaultStroke);
            g.setColor(back);
            g.fillRect(0,0,width,height);
            paintBorder(g);
            if (first==true && pt.pc8e.tape[unit]!=null) {
                pt.pDiodes.setBackground(Color.white);
                g.setStroke(tapeStroke);
                layers = tape_line/80; 
                if (layers%2==0) {
                    start = -110;
                    pos = - tape_line%80;
                } else {
                    start = -190;
                    pos = tape_line%80;
                }
                for (int i=0;i<=layers;i++) {
                    diam = width*2 - i;
                    if (i%2==0) g.setColor(Color.white); else g.setColor(back);
                    if (i==layers) g.drawArc(width-diam/2, height/4 - diam/2, diam, diam, (int)start, (int)pos);
                    else g.drawArc(width-diam/2, height/4 - diam/2, diam, diam, -110, -80);
                }
                double posa = (start + pos)*MULTIP;
                int plusx = (int) (Math.cos(posa)*diam/2);
                int plusy = (int) (Math.sin(posa)*diam/2);
                g.setColor(Color.white);
                g.drawLine(width, height/4, width + plusx, height/4 - plusy);
            } else if (first==true) {
                pt.pDiodes.setBackground(back);
            }
        }
        if (unit==1) {
            twidth = width*4/5;
            tape_line = local_line;
            Graphics2D g = (Graphics2D) g1d;
            g.setStroke(defaultStroke);
            g.setColor(back);
            g.fillRect(0,0,width,height);
            if (first==true && pt.pc8e.tape[unit]!=null) {
                layers = tape_line/80;
                hole = tape_line%80;
                for (int i=0;i<layers;i++) {
                    diam = width*2 - i;
                    if (i%2==0) g.setColor(Color.white); else g.setColor(back);
                    g.drawLine(width/10, 160 + i, width/10+twidth, 160 + i);
                }
                if (layers%2==0) g.setColor(Color.white); else g.setColor(dark);
                g.fillRect(width/10, 0, twidth, hole*2);
                if (layers%2==1) g.setColor(Color.white); else g.setColor(dark);
                for (int i=0;i<hole;i++) Holes(g,i);
                g.fillRect(width/10, hole*2, twidth, 160-hole*2);
                if (layers%2==0) g.setColor(Color.white); else g.setColor(dark);
                if (layers>0) for (int i=hole;i<80;i++) Holes(g,i);
            }
        }
    }

    private void Holes(Graphics2D g, int i) {
        g.fillOval(width/10+twidth*6/10, i*2, 1,1);
        if ((holes[i]>>0&01)!=0)g.fillOval(width/10+twidth*9/10, i*2, 2,2);
        if ((holes[i]>>1&01)!=0)g.fillOval(width/10+twidth*8/10, i*2, 2,2);
        if ((holes[i]>>2&01)!=0)g.fillOval(width/10+twidth*7/10, i*2, 2,2);
        if ((holes[i]>>3&01)!=0)g.fillOval(width/10+twidth*5/10, i*2, 2,2);
        if ((holes[i]>>4&01)!=0)g.fillOval(width/10+twidth*4/10, i*2, 2,2);
        if ((holes[i]>>5&01)!=0)g.fillOval(width/10+twidth*3/10, i*2, 2,2);
        if ((holes[i]>>6&01)!=0)g.fillOval(width/10+twidth*2/10, i*2, 2,2);
        if ((holes[i]>>7&01)!=0)g.fillOval(width/10+twidth*1/10, i*2, 2,2);
    }

    public void setHole(int data) {
        if (unit==1) {
            holes[pt.pc8e.line[unit]%80] = data;
        }
    }


    public class Turn implements Runnable {
        int sleep = 20;
        int inc;
        public void run() {
            while (true) {
                try {
                    if (pt.local[unit] & pt.pc8e.tape[unit]!=null) {
                        if (unit==0) inc = 300; else inc = 50;
                        if (right) {
                            if (pt.direction[unit]==1 & (pt.pc8e.line[unit] < (pt.tapesize[unit]-inc)) )  pt.pc8e.line[unit] += inc*sleep/1e3;
                            if (pt.direction[unit]==-1 & (pt.pc8e.line[unit] > inc) ) pt.pc8e.line[unit] -= inc*sleep/1e3;
                        }
                    }
                    if (local_line != pt.pc8e.line[unit]) {
                        local_line = pt.pc8e.line[unit];
                        //System.out.println(local_line);
                        repaint();
                        sleep=20;
                    }
                    Thread.sleep(sleep);
                    sleep++;

                } catch (InterruptedException e) {System.out.println("Feed error" + e);}
            }
        }

        public Turn() {
        }

    }
};



