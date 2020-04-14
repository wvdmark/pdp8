/*
 * Lamp.java
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
//import javax.swing.*;
//import javax.swing.event.*;

public class Lamp extends javax.swing.JComponent {

    private boolean state;
    private Color lampColor;
    private Color newColor;
    private int width;
    private int height;
    private int size;
    // Set the antialiasing to get the right look!
    private final static RenderingHints AALIAS =
    new RenderingHints(RenderingHints.KEY_ANTIALIASING,
    RenderingHints.VALUE_ANTIALIAS_ON);


    public Lamp(){
        setState(true);
        setColor(Color.YELLOW);
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
            this.lampColor = this.newColor;
        } else {
            this.lampColor = Color.BLACK;
        }
        repaint();
    }

    public boolean getState() {
        return state;
    }

    public void setColor(Color newcolor) {
        this.newColor = newcolor;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.addRenderingHints(AALIAS);

        width = getWidth();
        height = getHeight();
        if (width>height) {
            size = (int) ((float)height * 0.7);
        }else {
            size = (int) ((float)width * 0.7 );
        }
        g.setColor(Color.BLACK);
        g.fillRect(0,0,width,height);
        g.setColor(this.lampColor);
        g.fillOval((int)((float)(width)/2-(float)(size)/2),(int)((float)(height)/2-(float)(size)/2),size,size);
    }

}
