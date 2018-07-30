/*
 * DTReel.java
 *
 * Created on May 29, 20018, 11:20 PM
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

public class DTReel extends javax.swing.JComponent {
    
    private Devices.Dectape dt;
    private Turn doit;
    private Thread update;
    private int unit = 0;
    private int size;
    private boolean right = false;
    private int oldwidth=0;
    private int oldheight=0;
    private boolean first = false;
    //private int pos = 0;
    public double start_ang = 0;
    private int reel_x;
    private int reel_y;
    private int plate_top_y;
    private int guide_bottom_y;
    private int guide_left_x;
    private int guide_right_x;
    private int hub_radius;
    private int reel_radius;
    private int head_left_x;
    private int head_top_y;
    private int head_top_y1;
    private int head_bottom_y;
    private double angle_1;
    private double angle_2;
    private double angle;
    private double dist_guide_reel;
    private Color back;
    private Color dark;
    private int local_line;
    private int reel_line;
    private int tape_radius;
    
    private final static float PI = (float)Math.PI;
    private final static float MULTIP = PI/180;
    private final static Dimension MIN_SIZE = new Dimension(68, 104);
    private final static Dimension PREF_SIZE = new Dimension(140, 224);
    private final static float REEL_CENTER_X = 0.50f;
    private final static float REEL_CENTER_Y = 0.65f;
    private final static float PLATE_TOP_Y = 0.03f;
    private final static float GUIDE_BOTTOM_Y = 0.34f;
    private final static float GUIDE_LEFT_X = 0.30f;
    private final static float GUIDE_RIGHT_X = 0.83f;
    private final static float HUB_RADIUS = 0.27f;
    private final static float REEL_RADIUS = 0.40f;
    private final static float HEAD_LEFT_X = 0.88f;
    private final static float HEAD_TOP_Y = 0.01f;
    private final static float HEAD_TOP_Y1 = 0.05f;
    private final static float HEAD_BOTTOM_Y = 0.30f;
    private final static double MIL = 0.0015d;
    
    // Set the antialiasing to get the right look!
    private final static RenderingHints AALIAS =
    new RenderingHints(RenderingHints.KEY_ANTIALIASING,
    RenderingHints.VALUE_ANTIALIAS_ON);
    Stroke defaultStroke = new BasicStroke(1);
    Stroke hubStroke = new BasicStroke(2);
    
    
    /** Creates a new instance of DTReel */
    public DTReel() {
        setPreferredSize(PREF_SIZE);
        setMinimumSize(MIN_SIZE);
        setBackground(new java.awt.Color(100, 100, 100));
        doit = new Turn();
    }
    
    
    public void startDt(Devices.Dectape dt, String name) {
        this.dt = dt;
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
    
    public double getPos() {
        return start_ang;
    }
    
    public void setPos(double angle) {
        this.start_ang = angle;
        repaint();
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
        int width = getWidth();
        int height = getHeight();
        Polygon head = new Polygon();
        if (width!=oldwidth|height!=oldheight) {
            oldwidth = width;
            oldheight=height;
            reel_x = (int) (width * REEL_CENTER_X);
            reel_y = (int) (height * REEL_CENTER_Y);
            if ((width-reel_x)>(height-reel_y)) {
                reel_radius = (int) ((height-reel_y) * REEL_RADIUS * 2);
                hub_radius = (int) ((height-reel_y) * HUB_RADIUS * 2);
            } else {
                reel_radius = (int) ((width-reel_x) * REEL_RADIUS * 2);
                hub_radius = (int) ((width-reel_x) * HUB_RADIUS * 2);
            }
            plate_top_y = (int)(height*PLATE_TOP_Y);
            guide_bottom_y = (int)(height*GUIDE_BOTTOM_Y);
            //guide_left_x = (int)(width*GUIDE_LEFT_X);
            guide_right_x = (int)(width*GUIDE_RIGHT_X);
            guide_left_x = (int)(width*GUIDE_RIGHT_X-height*GUIDE_BOTTOM_Y);
            head_left_x = (int)(width*HEAD_LEFT_X);
            head_top_y = (int)(height*HEAD_TOP_Y);
            head_top_y1 = (int)(height*HEAD_TOP_Y1);
            head_bottom_y = (int)(height*HEAD_BOTTOM_Y);
            angle_1 = Math.atan((double)(guide_right_x-reel_x)/(double)(reel_y-guide_bottom_y));
            dist_guide_reel = Math.sqrt((guide_right_x-reel_x)*(guide_right_x-reel_x) + (reel_y-guide_bottom_y)*(reel_y-guide_bottom_y));
        }
        
        if (right) reel_line = local_line; else if (first) reel_line = dt.tapesize[unit] - local_line;
        double windings = ( Math.sqrt( 1 + (MIL*reel_line / (double)(150*PI*hub_radius*4.5/width) ) ) - 1) / (2*MIL);
        tape_radius = (int)((windings*MIL + 1)*hub_radius);
        start_ang = windings/2;
        
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
        g.setColor(Color.gray);
        g.fill3DRect(guide_left_x, plate_top_y, guide_right_x - guide_left_x, guide_bottom_y - plate_top_y, true);
        g.setColor(Color.lightGray);
        head.addPoint(head_left_x, head_top_y1);
        head.addPoint(width, head_top_y);
        head.addPoint(width, head_bottom_y);
        head.addPoint(head_left_x, head_bottom_y);
        head.addPoint(head_left_x, head_top_y1);
        g.fillPolygon(head);
        if (first==true && dt.td8e.tape[unit]!=null) {
            g.setColor(Color.red);
            g.drawLine(guide_right_x,head_top_y,width,head_top_y);
            g.setColor(Color.red);
            angle_2 = Math.asin((tape_radius-(guide_right_x-guide_left_x))/dist_guide_reel);
            angle = angle_1+angle_2;
            int gtx = (int) ((guide_right_x-guide_left_x)*Math.cos(angle)*0.98);
            int gty = (int) ((guide_right_x-guide_left_x)*Math.sin(angle)*0.98);
            double ll = dist_guide_reel*Math.cos(angle_2);
            int ex = (int) (-ll*Math.sin(angle));
            int ey = (int) (ll*Math.cos(angle));
            g.drawLine(guide_right_x-gtx,guide_bottom_y-gty,guide_right_x-gtx+ex,guide_bottom_y-gty+ey);
        }
        g.setColor(Color.darkGray);
        g.fillArc(guide_left_x, 0, 2* (guide_right_x-guide_left_x), 2* (guide_right_x-guide_left_x),90,90);
        g.setColor(Color.white);
        g.drawArc(guide_left_x, 0, 2* (guide_right_x-guide_left_x), 2* (guide_right_x-guide_left_x),90,90);
        if (first==true && dt.td8e.tape[unit]!=null) {
            g.setColor(Color.red);
            g.fillOval(reel_x-tape_radius, reel_y-tape_radius, tape_radius*2, tape_radius*2);
            g.setColor(new java.awt.Color(255, 255, 255, 200));
            g.fillOval(reel_x-reel_radius, reel_y-reel_radius, reel_radius*2, reel_radius*2);
        }
        g.setColor(Color.black);
        g.fillOval(reel_x-hub_radius, reel_y-hub_radius, hub_radius*2, hub_radius*2);
        
        g.setColor(new java.awt.Color(50, 50, 80));
        // Paint the "hub"
        g.setStroke(hubStroke);
        for (int spoke = 0; spoke < 6; spoke += 1) {
            Spoke(g, start_ang + spoke * PI / 3);
        }
    }
    
    private void Spoke(Graphics2D g, double angle) {
        //if (right) angle = -angle;
        Polygon p = new Polygon();
        double x = Math.cos(angle);
        double y = Math.sin(angle);
        double x1 = Math.cos(angle - 25 * MULTIP);
        double y1 = Math.sin(angle - 25 * MULTIP);
        double x2 = Math.cos(angle + 25 * MULTIP);
        double y2 = Math.sin(angle + 25 * MULTIP);
        p.addPoint(reel_x - (int)(x*hub_radius*0.1),  reel_y + (int)(y*hub_radius*0.1));
        p.addPoint(reel_x + (int)(x1*hub_radius*0.5), reel_y - (int)(y1*hub_radius*0.5));
        p.addPoint(reel_x + (int)(x*hub_radius),      reel_y - (int)(y*hub_radius));
        p.addPoint(reel_x + (int)(x2*hub_radius*0.5), reel_y - (int)(y2*hub_radius*0.5));
        p.addPoint(reel_x - (int)(x*hub_radius*0.1),  reel_y + (int)(y*hub_radius*0.1));
        g.fillPolygon(p);
    }
    
    
    public class Turn implements Runnable {
        int sleep = 20;
        public void run() {
            while (true) {
                try {
                    if (dt.local[unit] & dt.td8e.tape[unit]!=null) {
                        if (right) {
                            if (dt.direction[unit]==1 & dt.td8e.line[unit] < (dt.tapesize[unit]-33300))  dt.td8e.line[unit] += 33300*sleep/1e3;
                            if (dt.direction[unit]==-1 & dt.td8e.line[unit] > 33300) dt.td8e.line[unit] -= 33300*sleep/1e3;
                        }
                    }
                    if (local_line != dt.td8e.line[unit]) {
                        local_line = dt.td8e.line[unit];
                        repaint();
                        sleep = 20;
                    }
                    Thread.sleep(sleep);
                    sleep++;
                    
                } catch (InterruptedException e) {System.out.println("Turn error" + e);}
            }
        }
        
        public Turn() {
        }
        
    }
};



