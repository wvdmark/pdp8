/*
 * Knob.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */


package Panel;

import java.awt.*;
import java.awt.event.*;
//import java.awt.geom.*;
import javax.swing.*;
//import javax.swing.event.*;
import java.beans.*;


public class Knob extends JComponent {
    private final static float PI = (float) 3.1415;
    private final static float MULTIP = 180 / PI;

    private float start ;
    private float length ;
    private float start_ang;
    private float length_ang;
    private float size;
    private int middlex;
    private int middley;
    private float mult;
    private float a2;
    private int ticks = 6;
    private Color back;
    private Color border_color;

    private final static Dimension MIN_SIZE = new Dimension(50, 50);
    private final static Dimension PREF_SIZE = new Dimension(100, 100);

    // Set the antialiasing to get the right look!
    private final static RenderingHints AALIAS =
    new RenderingHints(RenderingHints.KEY_ANTIALIASING,
    RenderingHints.VALUE_ANTIALIAS_ON);
    Stroke drawingStroke = new BasicStroke(2);

    //private ChangeEvent changeEvent = null;
    //private EventListenerList listenerList = new EventListenerList();
    transient protected PropertyChangeSupport knoblisteners;

    private float ang = start_ang;
    private float val;
    private float startVal;
    private double lastAng;

    public Knob() {


        setPreferredSize(PREF_SIZE);
        knoblisteners = new PropertyChangeSupport(this);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
                float xpos = me.getX()-middlex;
                float ypos = me.getY()-middley;
                double ang = Math.atan2(-ypos, xpos);
                if (ang<0){ang += 2*PI; }
                double diff = start_ang - ang;
                setValue((float) ((diff / length_ang)));
            }
        });
    }


    public float getValue() {
        return val;
    }

    public void setValue(float val) {
        if (val < 0) val = 0;
        if (val > 1) val = 1;
        float oldval = this.val;
        val = (float) Math.floor(val*(ticks-1)+0.5)/(ticks-1);
        this.val = val;
        ang = start_ang - length_ang * val;
        repaint();
        knoblisteners.firePropertyChange(this.getName(),(int) (oldval*(ticks-1)),(int) ((this.val)*(ticks-1)));
    }

    public float getStart(){
        return start;
    }

    public void setStart(float start){
        this.start = start;
        start_ang = (start/360)*PI*2;
        ang = start_ang;
        repaint();
    }

    public float getLength(){
        return length;
    }

    public void setLength(float length){
        this.length = length;
        length_ang = (length/360)*PI*2;
        repaint();
    }

    public int getTicks(){
        return ticks;
    }

    public void setTicks(int ticks){
        this.ticks = ticks;
        repaint();
    }

    @Override
    public Color getBackground(){
        return back;
    }

    @Override
    public void setBackground(Color back){
        this.back = back;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener pl) {
        knoblisteners.addPropertyChangeListener(pl);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener pl) {
        knoblisteners.removePropertyChangeListener(pl);
    }

    // Paint the DKnob
    @Override
    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        middlex = width / 2;
        middley = height / 2;
        if (width>height) {
            size = height * 10 / 12;
        } else {
            size = width * 10 / 12;
        }
        //cornerx = middlex - size/2;
        //cornery = middley - size/2;

        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setBackground(back);
            g2d.addRenderingHints(AALIAS);
            g2d.setStroke(drawingStroke);
        }

        g.setColor(back);
        g.fillRect(0,0,width,height);
        paintBorder(g);

        g.setColor(Color.white);
        // Paint the "markers"
        for (int tick = 0; tick < ticks; tick += 1) {
            a2 = start_ang - tick * length_ang / (ticks-1);
            float x = (float) Math.cos(a2);
            float y = (float) Math.sin(a2);
            if ( Math.abs(x)>Math.abs(y)) {
                mult = Math.abs(middlex / x);
            }else {
                mult = Math.abs(middley / y);
            }
            g.drawLine(middlex, middley, middlex+(int)(x*mult), middley-(int)(y*mult));
        }

        g.setColor(Color.white);
        int s1 = (int) (size * 0.5); int s2 = s1 *2;
        g.fillOval(middlex-s1, middley-s1, s2, s2);

        g.setColor(Color.black);
        int s3 = (int) (size * 0.45); int s4 = s3 *2;
        if (s4>=s2){s1 -=1;s4 = s3 * 2;}
        g.fillOval(middlex-s3, middley-s3, s4, s4);

        double x = middlex + (size/2 * Math.cos(ang));
        double y = middley - (size/2 * Math.sin(ang));
        g.setColor(Color.white);
        g.drawLine(middlex, middley, (int)x, (int)y);

        g.setColor(Color.LIGHT_GRAY);
        int s5 = (int) (size * 0.3); int s6 = s5 *2;
        g.fillOval(middlex-s5, middley-s5, s6, s6);
    }
}
