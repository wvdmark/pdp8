/*
 * Main8.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */


public class Main8 extends javax.swing.JFrame {

    /** Creates new form Main8 */
    public Main8() {
        initComponents();
        data = new Logic.BusRegMem();
        panelLogic = new Logic.PanelLogic(data,"Panel");
        panel8.setLogic(panelLogic);
        panelLogic.setPanel(panel8);
        panelLogic.start();
        proc = new Logic.Proc(data,"Processor");
        proc.start();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel8 = new Panel.Panel8();

        setTitle("PDP-8/E");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());
        getContentPane().add(panel8, new java.awt.GridBagConstraints());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        data.CloseAllDevs();
        //System.exit(0);
    }//GEN-LAST:event_exitForm

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new Main8().setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public Panel.Panel8 panel8;
    // End of variables declaration//GEN-END:variables
    public Logic.PanelLogic panelLogic;
    public Logic.BusRegMem data;
    public Logic.Proc proc;
}