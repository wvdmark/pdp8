/*
 * BusLamps.java
 *
 * Created on January 1, 2005, 0:00 PM
 */
/**
 *
 * @author  wvdmark@computer.org
 */
package Panel;

public class StateLamps extends javax.swing.JPanel {

    /** Creates new form maLamps */
    public StateLamps() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        stateLamp0 = new Panel.XLamp();
        stateLamp1 = new Panel.XLamp();
        stateLamp2 = new Panel.XLamp();
        stateLamp3 = new Panel.XLamp();
        stateLamp4 = new Panel.XLamp();
        stateLamp5 = new Panel.XLamp();
        stateLamp6 = new Panel.XLamp();
        stateLamp7 = new Panel.XLamp();
        stateLamp8 = new Panel.XLamp();
        stateLamp9 = new Panel.XLamp();
        stateLamp10 = new Panel.XLamp();
        stateLamp11 = new Panel.XLamp();

        setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        setMaximumSize(new java.awt.Dimension(312, 12));
        setMinimumSize(new java.awt.Dimension(156, 6));
        setPreferredSize(new java.awt.Dimension(312, 12));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        setLayout(new java.awt.GridBagLayout());

        stateLamp0.setBackground(new java.awt.Color(193, 77, 28));
        stateLamp0.setForeground(new java.awt.Color(0, 0, 0));
        stateLamp0.setDoubleBuffered(true);
        stateLamp0.setEdge(3);
        stateLamp0.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        stateLamp0.setMaximumSize(new java.awt.Dimension(26, 12));
        stateLamp0.setMinimumSize(new java.awt.Dimension(13, 6));
        stateLamp0.setPreferredSize(new java.awt.Dimension(26, 12));
        stateLamp0.setState(false);
        stateLamp0.setText("F");
        add(stateLamp0, new java.awt.GridBagConstraints());

        stateLamp1.setBackground(new java.awt.Color(193, 77, 28));
        stateLamp1.setDoubleBuffered(true);
        stateLamp1.setEdge(3);
        stateLamp1.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        stateLamp1.setMaximumSize(new java.awt.Dimension(26, 12));
        stateLamp1.setMinimumSize(new java.awt.Dimension(13, 6));
        stateLamp1.setPreferredSize(new java.awt.Dimension(26, 12));
        stateLamp1.setState(false);
        stateLamp1.setText("D");
        add(stateLamp1, new java.awt.GridBagConstraints());

        stateLamp2.setBackground(new java.awt.Color(193, 77, 29));
        stateLamp2.setDoubleBuffered(true);
        stateLamp2.setEdge(3);
        stateLamp2.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        stateLamp2.setMaximumSize(new java.awt.Dimension(26, 12));
        stateLamp2.setMinimumSize(new java.awt.Dimension(13, 6));
        stateLamp2.setPreferredSize(new java.awt.Dimension(26, 12));
        stateLamp2.setState(false);
        stateLamp2.setText("E");
        add(stateLamp2, new java.awt.GridBagConstraints());

        stateLamp3.setBackground(new java.awt.Color(193, 132, 28));
        stateLamp3.setDoubleBuffered(true);
        stateLamp3.setEdge(3);
        stateLamp3.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        stateLamp3.setMaximumSize(new java.awt.Dimension(26, 12));
        stateLamp3.setMinimumSize(new java.awt.Dimension(13, 6));
        stateLamp3.setPreferredSize(new java.awt.Dimension(26, 12));
        stateLamp3.setState(false);
        stateLamp3.setText("IR0");
        add(stateLamp3, new java.awt.GridBagConstraints());

        stateLamp4.setBackground(new java.awt.Color(193, 132, 28));
        stateLamp4.setDoubleBuffered(true);
        stateLamp4.setEdge(3);
        stateLamp4.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        stateLamp4.setMaximumSize(new java.awt.Dimension(26, 12));
        stateLamp4.setMinimumSize(new java.awt.Dimension(13, 6));
        stateLamp4.setPreferredSize(new java.awt.Dimension(26, 12));
        stateLamp4.setState(false);
        stateLamp4.setText("IR1");
        add(stateLamp4, new java.awt.GridBagConstraints());

        stateLamp5.setBackground(new java.awt.Color(193, 132, 28));
        stateLamp5.setDoubleBuffered(true);
        stateLamp5.setEdge(3);
        stateLamp5.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        stateLamp5.setMaximumSize(new java.awt.Dimension(26, 12));
        stateLamp5.setMinimumSize(new java.awt.Dimension(13, 6));
        stateLamp5.setPreferredSize(new java.awt.Dimension(26, 12));
        stateLamp5.setState(false);
        stateLamp5.setText("IR2");
        add(stateLamp5, new java.awt.GridBagConstraints());

        stateLamp6.setBackground(new java.awt.Color(193, 77, 28));
        stateLamp6.setDoubleBuffered(true);
        stateLamp6.setEdge(3);
        stateLamp6.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        stateLamp6.setMaximumSize(new java.awt.Dimension(26, 12));
        stateLamp6.setMinimumSize(new java.awt.Dimension(13, 6));
        stateLamp6.setPreferredSize(new java.awt.Dimension(26, 12));
        stateLamp6.setState(false);
        stateLamp6.setText("MDD");
        add(stateLamp6, new java.awt.GridBagConstraints());

        stateLamp7.setBackground(new java.awt.Color(193, 77, 28));
        stateLamp7.setDoubleBuffered(true);
        stateLamp7.setEdge(3);
        stateLamp7.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        stateLamp7.setMaximumSize(new java.awt.Dimension(26, 12));
        stateLamp7.setMinimumSize(new java.awt.Dimension(13, 6));
        stateLamp7.setPreferredSize(new java.awt.Dimension(26, 12));
        stateLamp7.setState(false);
        stateLamp7.setText("BDC");
        add(stateLamp7, new java.awt.GridBagConstraints());

        stateLamp8.setBackground(new java.awt.Color(193, 77, 28));
        stateLamp8.setDoubleBuffered(true);
        stateLamp8.setEdge(3);
        stateLamp8.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        stateLamp8.setMaximumSize(new java.awt.Dimension(26, 12));
        stateLamp8.setMinimumSize(new java.awt.Dimension(13, 6));
        stateLamp8.setPreferredSize(new java.awt.Dimension(26, 12));
        stateLamp8.setState(false);
        stateLamp8.setText("SW");
        add(stateLamp8, new java.awt.GridBagConstraints());

        stateLamp9.setBackground(new java.awt.Color(193, 132, 28));
        stateLamp9.setDoubleBuffered(true);
        stateLamp9.setEdge(3);
        stateLamp9.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        stateLamp9.setMaximumSize(new java.awt.Dimension(26, 12));
        stateLamp9.setMinimumSize(new java.awt.Dimension(13, 6));
        stateLamp9.setPreferredSize(new java.awt.Dimension(26, 12));
        stateLamp9.setState(false);
        stateLamp9.setText("PAU");
        add(stateLamp9, new java.awt.GridBagConstraints());

        stateLamp10.setBackground(new java.awt.Color(193, 132, 28));
        stateLamp10.setDoubleBuffered(true);
        stateLamp10.setEdge(3);
        stateLamp10.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        stateLamp10.setMaximumSize(new java.awt.Dimension(26, 12));
        stateLamp10.setMinimumSize(new java.awt.Dimension(13, 6));
        stateLamp10.setPreferredSize(new java.awt.Dimension(26, 12));
        stateLamp10.setState(false);
        stateLamp10.setText("BRKP");
        add(stateLamp10, new java.awt.GridBagConstraints());

        stateLamp11.setBackground(new java.awt.Color(193, 132, 28));
        stateLamp11.setDoubleBuffered(true);
        stateLamp11.setEdge(3);
        stateLamp11.setFont(new java.awt.Font("Dialog", 1, 8)); // NOI18N
        stateLamp11.setMaximumSize(new java.awt.Dimension(26, 12));
        stateLamp11.setMinimumSize(new java.awt.Dimension(13, 6));
        stateLamp11.setPreferredSize(new java.awt.Dimension(26, 12));
        stateLamp11.setState(false);
        stateLamp11.setText("BRKC");
        add(stateLamp11, new java.awt.GridBagConstraints());
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if (this.getWidth() < 300) {
            stateLamp0.setFont(new java.awt.Font("Dialog", 1, 4));
            stateLamp1.setFont(new java.awt.Font("Dialog", 1, 4));
            stateLamp2.setFont(new java.awt.Font("Dialog", 1, 4));
            stateLamp3.setFont(new java.awt.Font("Dialog", 1, 4));
            stateLamp4.setFont(new java.awt.Font("Dialog", 1, 4));
            stateLamp5.setFont(new java.awt.Font("Dialog", 1, 4));
            stateLamp6.setFont(new java.awt.Font("Dialog", 1, 4));
            stateLamp7.setFont(new java.awt.Font("Dialog", 1, 4));
            stateLamp8.setFont(new java.awt.Font("Dialog", 1, 4));
            stateLamp9.setFont(new java.awt.Font("Dialog", 1, 4));
            stateLamp10.setFont(new java.awt.Font("Dialog", 1, 4));
            stateLamp11.setFont(new java.awt.Font("Dialog", 1, 4));
        } else {
            stateLamp0.setFont(new java.awt.Font("Dialog", 1, 8));
            stateLamp1.setFont(new java.awt.Font("Dialog", 1, 8));
            stateLamp2.setFont(new java.awt.Font("Dialog", 1, 8));
            stateLamp3.setFont(new java.awt.Font("Dialog", 1, 8));
            stateLamp4.setFont(new java.awt.Font("Dialog", 1, 8));
            stateLamp5.setFont(new java.awt.Font("Dialog", 1, 8));
            stateLamp6.setFont(new java.awt.Font("Dialog", 1, 8));
            stateLamp7.setFont(new java.awt.Font("Dialog", 1, 8));
            stateLamp8.setFont(new java.awt.Font("Dialog", 1, 8));
            stateLamp9.setFont(new java.awt.Font("Dialog", 1, 8));
            stateLamp10.setFont(new java.awt.Font("Dialog", 1, 8));
            stateLamp11.setFont(new java.awt.Font("Dialog", 1, 8));
       }
        // TODO add your handling code here:
    }//GEN-LAST:event_formComponentResized

    /**
     * Setter for property state.
     * @param state New value of property state.
     */
    public void setState(int state) {
        if (this.state != state) {
            this.state = state;
            stateLamp0.setState(state & 04000);
            stateLamp1.setState(state & 02000);
            stateLamp2.setState(state & 01000);
            stateLamp3.setState(state & 00400);
            stateLamp4.setState(state & 00200);
            stateLamp5.setState(state & 00100);
            stateLamp6.setState(state & 00040);
            stateLamp7.setState(state & 00020);
            stateLamp8.setState(state & 00010);
            stateLamp9.setState(state & 00004);
            stateLamp10.setState(state & 00002);
            stateLamp11.setState(state & 00001);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Panel.XLamp stateLamp0;
    private Panel.XLamp stateLamp1;
    private Panel.XLamp stateLamp10;
    private Panel.XLamp stateLamp11;
    private Panel.XLamp stateLamp2;
    private Panel.XLamp stateLamp3;
    private Panel.XLamp stateLamp4;
    private Panel.XLamp stateLamp5;
    private Panel.XLamp stateLamp6;
    private Panel.XLamp stateLamp7;
    private Panel.XLamp stateLamp8;
    private Panel.XLamp stateLamp9;
    // End of variables declaration//GEN-END:variables
    /**
     * Holds value of property state.
     */
    private int state = -1;
}
