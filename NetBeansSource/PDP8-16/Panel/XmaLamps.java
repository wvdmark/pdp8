/*
 * EmaLamps.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Panel;

public class XmaLamps extends javax.swing.JPanel {

    /** Creates new form emaLamps */
    public XmaLamps() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        emaLamp0 = new Panel.Lamp();
        emaLamp1 = new Panel.Lamp();
        emaLamp2 = new Panel.Lamp();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(110, 10));
        setMinimumSize(new java.awt.Dimension(55, 5));
        setPreferredSize(new java.awt.Dimension(110, 10));
        setLayout(new java.awt.GridBagLayout());

        emaLamp0.setMaximumSize(new java.awt.Dimension(26, 10));
        emaLamp0.setMinimumSize(new java.awt.Dimension(13, 5));
        emaLamp0.setPreferredSize(new java.awt.Dimension(26, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        add(emaLamp0, gridBagConstraints);

        emaLamp1.setMaximumSize(new java.awt.Dimension(26, 10));
        emaLamp1.setMinimumSize(new java.awt.Dimension(13, 5));
        emaLamp1.setPreferredSize(new java.awt.Dimension(26, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        add(emaLamp1, gridBagConstraints);

        emaLamp2.setMaximumSize(new java.awt.Dimension(26, 10));
        emaLamp2.setMinimumSize(new java.awt.Dimension(13, 5));
        emaLamp2.setPreferredSize(new java.awt.Dimension(26, 10));
        add(emaLamp2, new java.awt.GridBagConstraints());

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setMaximumSize(new java.awt.Dimension(32, 10));
        jLabel1.setMinimumSize(new java.awt.Dimension(16, 5));
        jLabel1.setOpaque(true);
        jLabel1.setPreferredSize(new java.awt.Dimension(32, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(jLabel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    public void setState(int state) {
        this.state = state;
        emaLamp0.setState(state&04);
        emaLamp1.setState(state&02);
        emaLamp2.setState(state&01);
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Panel.Lamp emaLamp0;
    private Panel.Lamp emaLamp1;
    private Panel.Lamp emaLamp2;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
    private int state=-1;

}
