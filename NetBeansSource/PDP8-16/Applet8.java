/*
 * Applet8.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */
//package PDP8;

public class Applet8 extends javax.swing.JApplet {

    /** Initializes the applet Applet8 */
    public void init() {
        initComponents();
        data = new Logic.BusRegMem();
        panelLogic = new Logic.PanelLogic(data,"Panel");
        panel8.setLogic(panelLogic);
        panelLogic.setPanel(panel8);
        panelLogic.start();
        System.out.println("Panel="+panelLogic.getName());
        proc = new Logic.Proc(data,"Processor");
        proc.start();
        System.out.println("Proc="+proc.getName());
    }

    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents

        getContentPane().setLayout(new java.awt.GridBagLayout());

    }//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public Panel.Panel8 panel8;
    public Logic.PanelLogic panelLogic;
    public Logic.BusRegMem data;
    public Logic.Proc proc;

}
