/*
 * DTSwitch.java
 *
 * Created on May 29, 2005, 11:20 PM
 */
/**
 *
 * @author  wvdmark@computer.org
 */
package Devices;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
//import javax.swing.*;
//import javax.swing.event.*;
import java.beans.*;

public class DTSwitch extends javax.swing.JComponent {

    private int size;
    private int start = 0;
    private int pos = 0;
    private boolean pos3;
    private boolean mom;
    private boolean horizontal;
    private Color back;
    private Color dark;
    private AffineTransform a;
    private final static Dimension MIN_SIZE = new Dimension(13, 22);
    private final static Dimension PREF_SIZE = new Dimension(26, 44);
    // Set the antialiasing to get the right look!
    private final static RenderingHints AALIAS =
            new RenderingHints(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    Stroke drawingStroke = new BasicStroke(1);
    transient protected PropertyChangeSupport pchglisteners;

    /** Creates a new instance of DTSwitch */
    public DTSwitch() {

        setPreferredSize(PREF_SIZE);
        setMinimumSize(MIN_SIZE);
        setBackground(new java.awt.Color(100, 100, 100));
        addMouseListener(new MouseAdapter() {
/*
            @Override
            public void mouseClicked(MouseEvent me) {
                toggle(me);
            }
*/
            @Override
            public void mousePressed(MouseEvent me) {
                toggle(me);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                toggle(me);
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

    private void toggle(MouseEvent me) {
        if (me.getID() == MouseEvent.MOUSE_RELEASED & mom) {
            if (pos3) setPos(0);
            if (!pos3) setPos(2);
            return;
        }
        int tpos;
        int height;
        int click;
        if (horizontal) {
            click = me.getX();
            height = getWidth();
        } else {
            click = me.getY();
            height = getHeight();
        }
        if (pos3) {
            if (click < height / 3) {
                tpos = 2;
            } else if (click > 2 * height / 3) {
                tpos = 1;
            } else {
                tpos = 0;
            }
        } else {
            if (click < height / 2) {
                tpos = 2;
            } else {
                tpos = 1;
            }
        }
        setPos(tpos);
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        int oldvalue = this.pos;
        this.pos = pos;
        repaint();
        pchglisteners.firePropertyChange(this.getName(), oldvalue, this.pos);
    }

    public boolean getPos3() {
        return pos3;
    }

    public void setPos3(boolean pos3) {
        this.pos3 = pos3;
        repaint();
    }

    public boolean getMomentary() {
        return mom;
    }

    public void setMomentary(boolean mom) {
        this.mom = mom;
        repaint();
    }

    public boolean getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean hor) {
        this.horizontal = hor;
        repaint();
    }

    public int getInitial() {
        return start;
    }

    public void setInitial(int start) {
        this.start = start;
        this.pos = start;
        repaint();
    }

    @Override
    public Color getBackground() {
        return back;
    }

    @Override
    public void setBackground(Color back) {
        this.back = back;
        this.dark = back.darker().darker();
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
        a = AffineTransform.getRotateInstance(Math.PI / 2.0);
        a.scale(1, -1);
        if (horizontal) {
            g2d.transform(a);
            height = getWidth();
            width = getHeight();
        }
        g2d.addRenderingHints(AALIAS);
        if (pos == 0) {
            OneSide(g, 0, 0, width, height / 2, 0.125f);
            OneSide(g, 0, height / 2, width, height / 2, 0.875f);
        } else if (pos == 2) {
            OneSide(g, 0, 0, width, height / 2 - height / 8, 0.07f);
            OneSide(g, 0, height / 2 - height / 8, width, height / 2 + height / 8, 0.7f);
        } else if (pos == 1) {
            OneSide(g, 0, 0, width, height / 2 + height / 8, 0.3f);
            OneSide(g, 0, height / 2 + height / 8, width, height / 2 - height / 8, 0.93f);
        }
        paintBorder(g);
    }

    private void OneSide(Graphics g, int x, int y, int width, int height, float pos) {
        Graphics2D g2d = (Graphics2D) g;
        if (pos < 0.5f) {
            g.setColor(dark);
        } else {
            g.setColor(back);
        }
        Polygon p1 = new Polygon();
        p1.addPoint(x, y);
        p1.addPoint(x + width - 1, y);
        p1.addPoint((int) (x + width * 0.75), (int) (y + height * pos));
        p1.addPoint((int) (x + width * 0.25), (int) (y + height * pos));
        p1.addPoint(x, y);
        g.fillPolygon(p1);
        g2d.setColor(Color.WHITE);
        g.drawPolygon(p1);
        g.setColor(back);
        if (pos > 0.5f) {
            g.setColor(dark);
        } else {
            g.setColor(back);
        }
        Polygon p2 = new Polygon();
        p2.addPoint((int) (x + width * 0.75), (int) (y + height * pos));
        p2.addPoint(x + width - 1, y + height);
        p2.addPoint(x, y + height);
        p2.addPoint((int) (x + width * 0.25), (int) (y + height * pos));
        p2.addPoint((int) (x + width * 0.75), (int) (y + height * pos));
        g.fillPolygon(p2);
        g2d.setColor(Color.WHITE);
        g.drawPolygon(p2);
        g.setColor(dark);
        Polygon p3 = new Polygon();
        p3.addPoint(x, y);
        p3.addPoint((int) (x + width * 0.25), (int) (y + height * pos));
        p3.addPoint(x, y + height);
        p3.addPoint(x, y);
        g.fillPolygon(p3);
        g2d.setColor(Color.WHITE);
        g.drawPolygon(p3);
        g.setColor(dark);
        Polygon p4 = new Polygon();
        p4.addPoint(x + width - 1, y);
        p4.addPoint(x + width - 1, y + height);
        p4.addPoint((int) (x + width * 0.75), (int) (y + height * pos));
        p4.addPoint(x + width - 1, y);
        g.fillPolygon(p4);
        g2d.setColor(Color.WHITE);
        g.drawPolygon(p4);

    }
}
