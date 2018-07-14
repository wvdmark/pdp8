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

public class XLamp extends javax.swing.JComponent implements java.io.Serializable {

    private boolean state;
    private Color lampColor;
    private Color backColor;
    private String ourText = " ";
    private Font ourFont;
    private int width;
    private int height;
    private int size;
    private int edge = 0;
    // Set the antialiasing to get the right look!
    private final static RenderingHints AALIAS =
            new RenderingHints(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

    public XLamp() {
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

    public void setState(int state) {
        if (state > 0) {
            setState(true);
        } else {
            setState(false);
        }
    }

    @Override
    public void setBackground(Color backc) {
        this.backColor = backc;
    }

    @Override
    public Color getBackground() {
        return this.backColor;
    }

    public void setText(String ourtext) {
        if (this.ourText.equals(ourtext)) {
            return;
        }
        this.ourText = ourtext;
        repaint();
    }

    public String getText() {
        return this.ourText;
    }

    public void setEdge(int edge) {
        this.edge = edge;
        repaint();
    }

    public int getEdge() {
        return this.edge;
    }

    @Override
    public void setFont(Font ourfont) {
        this.ourFont = ourfont;

    }

    @Override
    public Font getFont() {
        return this.ourFont;
    }

    public void setState(boolean state) {
        if (this.state == state) {
            return;
        }

        this.state = state;
        if (state == true) {
            this.lampColor = Color.ORANGE;
        } else {
            this.lampColor = backColor;
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
        if (width > height) {
            size = (int) ((float) height * 0.7);
        } else {
            size = (int) ((float) width * 0.7);
        }
        FontMetrics fm = g.getFontMetrics(ourFont);
        int sw = fm.stringWidth(ourText);
        int sh = fm.getAscent();
        g.setColor(this.backColor);
        g.fillRect(0, 0, width, height);
        g.setColor(this.lampColor);
        g.fillOval((int) ((float) (width) / 2 - (float) (size) / 2), (int) ((float) (height) / 2 - (float) (size) / 2), size, size);
        g.setColor(Color.WHITE);
        g.setFont(this.ourFont);
        g.drawString(ourText, (width - sw) / 2, (height + sh) / 2 - 1);
        if (edge == 1 || edge == 3) {
            g.drawLine(0, 0, 0, height - 1);
            g.drawLine(width - 1 , 0, width - 1, height - 1);
        }
        if (edge == 2 || edge == 3) {
            //g.drawLine(0, 0, width - 1, 0);
            g.drawLine(0, height - 1, width - 1, height - 1);
        }
    }
}
