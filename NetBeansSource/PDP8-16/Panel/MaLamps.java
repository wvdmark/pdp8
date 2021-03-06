/*
 * MaLamps.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Panel;

public class MaLamps extends javax.swing.JPanel {

    /** Creates new form maLamps */
    public MaLamps() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        maLamp0 = new Panel.Lamp();
        maLamp1 = new Panel.Lamp();
        maLamp2 = new Panel.Lamp();
        maLamp3 = new Panel.Lamp();
        maLamp4 = new Panel.Lamp();
        maLamp5 = new Panel.Lamp();
        maLamp6 = new Panel.Lamp();
        maLamp7 = new Panel.Lamp();
        maLamp8 = new Panel.Lamp();
        maLamp9 = new Panel.Lamp();
        maLamp10 = new Panel.Lamp();
        maLamp11 = new Panel.Lamp();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(312, 10));
        setMinimumSize(new java.awt.Dimension(156, 5));
        setPreferredSize(new java.awt.Dimension(312, 10));
        maLamp0.setMaximumSize(new java.awt.Dimension(26, 10));
        maLamp0.setMinimumSize(new java.awt.Dimension(13, 5));
        maLamp0.setPreferredSize(new java.awt.Dimension(26, 10));
        add(maLamp0, new java.awt.GridBagConstraints());

        maLamp1.setMaximumSize(new java.awt.Dimension(26, 10));
        maLamp1.setMinimumSize(new java.awt.Dimension(13, 5));
        maLamp1.setPreferredSize(new java.awt.Dimension(26, 10));
        add(maLamp1, new java.awt.GridBagConstraints());

        maLamp2.setMaximumSize(new java.awt.Dimension(26, 10));
        maLamp2.setMinimumSize(new java.awt.Dimension(13, 5));
        maLamp2.setPreferredSize(new java.awt.Dimension(26, 10));
        add(maLamp2, new java.awt.GridBagConstraints());

        maLamp3.setMaximumSize(new java.awt.Dimension(26, 10));
        maLamp3.setMinimumSize(new java.awt.Dimension(13, 5));
        maLamp3.setPreferredSize(new java.awt.Dimension(26, 10));
        add(maLamp3, new java.awt.GridBagConstraints());

        maLamp4.setMaximumSize(new java.awt.Dimension(26, 10));
        maLamp4.setMinimumSize(new java.awt.Dimension(13, 5));
        maLamp4.setPreferredSize(new java.awt.Dimension(26, 10));
        add(maLamp4, new java.awt.GridBagConstraints());

        maLamp5.setMaximumSize(new java.awt.Dimension(26, 10));
        maLamp5.setMinimumSize(new java.awt.Dimension(13, 5));
        maLamp5.setPreferredSize(new java.awt.Dimension(26, 10));
        add(maLamp5, new java.awt.GridBagConstraints());

        maLamp6.setMaximumSize(new java.awt.Dimension(26, 10));
        maLamp6.setMinimumSize(new java.awt.Dimension(13, 5));
        maLamp6.setPreferredSize(new java.awt.Dimension(26, 10));
        add(maLamp6, new java.awt.GridBagConstraints());

        maLamp7.setMaximumSize(new java.awt.Dimension(26, 10));
        maLamp7.setMinimumSize(new java.awt.Dimension(13, 5));
        maLamp7.setPreferredSize(new java.awt.Dimension(26, 10));
        add(maLamp7, new java.awt.GridBagConstraints());

        maLamp8.setMaximumSize(new java.awt.Dimension(26, 10));
        maLamp8.setMinimumSize(new java.awt.Dimension(13, 5));
        maLamp8.setPreferredSize(new java.awt.Dimension(26, 10));
        add(maLamp8, new java.awt.GridBagConstraints());

        maLamp9.setMaximumSize(new java.awt.Dimension(26, 10));
        maLamp9.setMinimumSize(new java.awt.Dimension(13, 5));
        maLamp9.setPreferredSize(new java.awt.Dimension(26, 10));
        add(maLamp9, new java.awt.GridBagConstraints());

        maLamp10.setMaximumSize(new java.awt.Dimension(26, 10));
        maLamp10.setMinimumSize(new java.awt.Dimension(13, 5));
        maLamp10.setPreferredSize(new java.awt.Dimension(26, 10));
        add(maLamp10, new java.awt.GridBagConstraints());

        maLamp11.setMaximumSize(new java.awt.Dimension(26, 10));
        maLamp11.setMinimumSize(new java.awt.Dimension(13, 5));
        maLamp11.setPreferredSize(new java.awt.Dimension(26, 10));
        add(maLamp11, new java.awt.GridBagConstraints());

    }//GEN-END:initComponents

    public void setState(int state) {
        this.state = state;
        maLamp0.setState(state&04000);
        maLamp1.setState(state&02000);
        maLamp2.setState(state&01000);
        maLamp3.setState(state&00400);
        maLamp4.setState(state&00200);
        maLamp5.setState(state&00100);
        maLamp6.setState(state&00040);
        maLamp7.setState(state&00020);
        maLamp8.setState(state&00010);
        maLamp9.setState(state&00004);
        maLamp10.setState(state&00002);
        maLamp11.setState(state&00001);
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Panel.Lamp maLamp0;
    private Panel.Lamp maLamp1;
    private Panel.Lamp maLamp10;
    private Panel.Lamp maLamp11;
    private Panel.Lamp maLamp2;
    private Panel.Lamp maLamp3;
    private Panel.Lamp maLamp4;
    private Panel.Lamp maLamp5;
    private Panel.Lamp maLamp6;
    private Panel.Lamp maLamp7;
    private Panel.Lamp maLamp8;
    private Panel.Lamp maLamp9;
    // End of variables declaration//GEN-END:variables
    private int state=-1;

}
