/*
 * Lamp.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Devices;

import java.awt.*;
import java.awt.event.*;
//import javax.swing.*;
//import javax.swing.event.*;

public class Lamp extends javax.swing.JComponent {
    
    private boolean state;
    private Color lampColor;
    private double width;
    private double height;
    private double size;
    // Set the antialiasing to get the right look!
    private final static RenderingHints AALIAS =
    new RenderingHints(RenderingHints.KEY_ANTIALIASING,
    RenderingHints.VALUE_ANTIALIAS_ON);
    
    
    public Lamp(){
        setState(true);
        // Handle focus so that the knob gets the correct focus highlighting.
        addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                repaint();
            }
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }
    @Override
    public void setPreferredSize(java.awt.Dimension dim) {
        width = (int) dim.getWidth();
        height = (int) dim.getHeight();
        super.setPreferredSize(dim);
    }
    
    //public void addChangeListener(ChangeListener cl) {
    //    listenerList.add(ChangeListener.class, cl);
    //}
    
    //public void removeChangeListener(ChangeListener cl) {
    //    listenerList.remove(ChangeListener.class, cl);
    //}
    public void setState(int state) {
        if (state>0) {
            setState(true);
        } else {
            
            setState(false);
        }
    }
    public void setState(boolean state) {
        if (this.state == state) {
            return;
        }
        
        this.state = state;
        if (state == true) {
            this.lampColor = Color.WHITE;
        } else {
            this.lampColor = Color.gray;
        }
        repaint();
    }
    
    public boolean getState() {
        return state;
    }
    
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.addRenderingHints(AALIAS);
        
        width = getWidth();
        height = getHeight();
        if (width>height) {
            size = height * 0.5;
        }else {
            size = width * 0.5;
        }
        g.setColor(Color.darkGray);
        g.fillRect(0,0,(int) width,(int) height);
        g.setColor(this.lampColor);
        g.fillOval((int)((width)/2-(size)/2),(int)((height)/2-(size)/2),(int) size,(int) size);
    }
    
}
