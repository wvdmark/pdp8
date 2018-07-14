/*
 * Switch.java
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


public class Switch extends JComponent {

    private int size;
    private boolean start = true;
    private boolean pos = true;
    private boolean truepos = true;
    private boolean toggle = false;
    private Color back;
    private Color dark;

    private final static Dimension MIN_SIZE = new Dimension(13, 22);
    private final static Dimension PREF_SIZE = new Dimension(26, 44);

    // Set the antialiasing to get the right look!
    private final static RenderingHints AALIAS =
    new RenderingHints(RenderingHints.KEY_ANTIALIASING,
    RenderingHints.VALUE_ANTIALIAS_ON);
    Stroke drawingStroke = new BasicStroke(1);

    transient protected PropertyChangeSupport pchglisteners;

    public Switch() {

        setPreferredSize(PREF_SIZE);
        setMinimumSize(MIN_SIZE);
        setBackground(new java.awt.Color(193, 132, 29));
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
                if (toggle){toggle();}
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if (!toggle){depress(true);}
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (!toggle){depress(false);}
            }
        });

        // Handle focus so that the knob gets the correct focus highlighting.
        addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                repaint();
            }
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
        pchglisteners = new PropertyChangeSupport(this);
    }

    private void depress(boolean press) {
        boolean newpos;
        if (press) {
            newpos = !start;
            setPos(newpos);
            //repaint();
        }else {
            setPos(start);
        }
    }

    private void toggle() {
        setPos(!pos);
    }

    public boolean getPos() {
        return pos;
    }

    public void setPos(boolean pos) {
        boolean oldvalue = this.pos;
        this.pos = pos;
        repaint();
        pchglisteners.firePropertyChange(this.getName(),truepos^oldvalue,truepos^this.pos);
    }

    public boolean getToggle() {
        return toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    public boolean getInitial(){
        return start;
    }

    public void setInitial(boolean start){
        this.start = start;
        this.pos = start;
        repaint();
    }

    public void setTruepos( boolean truepos) {
        this.truepos = truepos;
    }

    public boolean getTruepos( ) {
        return truepos;
    }

    @Override
    public Color getBackground(){
        return back;
    }

    @Override
    public void setBackground(Color back){
        this.back = back;
        this.dark = back.darker();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener pl) {
        pchglisteners.addPropertyChangeListener(pl);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener pl) {
        pchglisteners.removePropertyChangeListener(pl);
    }

    // Paint the DKnob
    @Override
    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2d = (Graphics2D) g;
        //g2d.setBackground(Color.BLACK);
        g2d.addRenderingHints(AALIAS);
        //g2d.setStroke(drawingStroke);
        //paintBorder(g);
        if (pos) {
            g.setColor(back);
            Polygon p1 = new Polygon();
            p1.addPoint(0, 0);
            p1.addPoint(width-1, 0);
            p1.addPoint((int)(width*0.80f), (int)(height*0.25));
            p1.addPoint((int)(width*0.20f), (int)(height*0.25));
            p1.addPoint(0,0);
            g.fillPolygon(p1);
            g2d.setColor(Color.WHITE);
            g2d.drawPolygon(p1);
            g.setColor(back);
            Polygon p2 = new Polygon();
            p2.addPoint((int)(width*0.80f), (int)(height*0.25));
            p2.addPoint((int)(width*0.80f), (int)(height*0.40));
            p2.addPoint((int)(width*0.20f), (int)(height*0.40));
            p2.addPoint((int)(width*0.20f), (int)(height*0.25));
            g.fillPolygon(p2);
            g2d.setColor(Color.WHITE);
            g2d.drawPolygon(p2);
            g.setColor(dark);
            Polygon p3 = new Polygon();
            p3.addPoint(0, 0);
            p3.addPoint((int)(width*0.20f), (int)(height*0.25));
            p3.addPoint((int)(width*0.20f), (int)(height*0.40));
            p3.addPoint(0, (int)(height*0.50));
            p3.addPoint(0,0);
            g.fillPolygon(p3);
            g2d.setColor(Color.WHITE);
            g2d.drawPolygon(p3);
            g.setColor(dark);
            Polygon p4 = new Polygon();
            p4.addPoint(width-1,0);
            p4.addPoint(width-1, (int)(height*0.50));
            p4.addPoint((int)(width*0.80f), (int)(height*0.40));
            p4.addPoint((int)(width*0.80f), (int)(height*0.25));
            p4.addPoint(width-1,0);
            g.fillPolygon(p4);
            g2d.setColor(Color.WHITE);
            g2d.drawPolygon(p4);
            g.setColor(dark);
            Polygon p5 = new Polygon();
            p5.addPoint(width-1, (int)(height*0.50));
            p5.addPoint(width-1,height-1);
            p5.addPoint(0,height-1);
            p5.addPoint(0, (int)(height*0.50));
            p5.addPoint((int)(width*0.20f), (int)(height*0.40));
            p5.addPoint((int)(width*0.80f), (int)(height*0.40));
            p5.addPoint(width-1, (int)(height*0.50));
            g.fillPolygon(p5);
            g2d.setColor(Color.WHITE);
            g2d.drawPolygon(p5);
            Polygon p6 = new Polygon();
            p6.addPoint(width-1, (int)(height*0.50));
            p6.addPoint(0, (int)(height*0.50));
            p6.addPoint((int)(width*0.20f), (int)(height*0.40));
            p6.addPoint((int)(width*0.80f), (int)(height*0.40));
            p6.addPoint(width-1, (int)(height*0.50));
            g2d.setColor(Color.WHITE);
            g2d.drawPolygon(p6);
        } else {
            g.setColor(back);
            Polygon p1 = new Polygon();
            p1.addPoint(0, 0);
            p1.addPoint(width-1, 0);
            p1.addPoint((int)(width*0.80f), (int)(height*0.80));
            p1.addPoint((int)(width*0.20f), (int)(height*0.80));
            p1.addPoint(0,0);
            g.fillPolygon(p1);
            g2d.setColor(Color.WHITE);
            g2d.drawPolygon(p1);
            g.setColor(back);
            Polygon p2 = new Polygon();
            p2.addPoint((int)(width*0.80f), (int)(height*0.80));
            p2.addPoint((int)(width*0.80f), (int)(height*0.90));
            p2.addPoint((int)(width*0.20f), (int)(height*0.90));
            p2.addPoint((int)(width*0.20f), (int)(height*0.80));
            g.fillPolygon(p2);
            g2d.setColor(Color.WHITE);
            g2d.drawPolygon(p2);
            g.setColor(dark);
            Polygon p3 = new Polygon();
            p3.addPoint(0, 0);
            p3.addPoint((int)(width*0.20f), (int)(height*0.80));
            p3.addPoint((int)(width*0.20f), (int)(height*0.90));
            p3.addPoint(0, height-1);
            p3.addPoint(0,0);
            g.fillPolygon(p3);
            g2d.setColor(Color.WHITE);
            g2d.drawPolygon(p3);
            g.setColor(dark);
            Polygon p4 = new Polygon();
            p4.addPoint(width-1,0);
            p4.addPoint(width-1, height-1);
            p4.addPoint((int)(width*0.80f), (int)(height*0.90));
            p4.addPoint((int)(width*0.80f), (int)(height*0.80));
            p4.addPoint(width-1,0);
            g.fillPolygon(p4);
            g2d.setColor(Color.WHITE);
            g2d.drawPolygon(p4);
            g.setColor(dark);
            Polygon p5 = new Polygon();
            p5.addPoint(width-1, (int)(height*0.50));
            p5.addPoint(width-1,height-1);
            p5.addPoint(0,height-1);
            p5.addPoint(0, (int)(height*0.50));
            p5.addPoint((int)(width*0.20f), (int)(height*0.90));
            p5.addPoint((int)(width*0.80f), (int)(height*0.90));
            p5.addPoint(width-1, (int)(height*0.50));
            g.fillPolygon(p5);
            g2d.setColor(Color.WHITE);
            g2d.drawPolygon(p5);

        }
        paintBorder(g);
    }
}
