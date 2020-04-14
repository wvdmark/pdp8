/*
 * Panel8.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Panel;

import javax.swing.*;
import java.awt.event.*;

public class Panel8 extends javax.swing.JPanel implements MouseListener, ActionListener {

    boolean dummy;
    /** Creates new form Test */
    public Panel8() {
        initComponents();

        sw.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Boolean objswr = (Boolean) evt.getNewValue();
                boolean valswr = objswr.booleanValue();
                panelLogic.setSw(valswr);
            }
        });
        swEal.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Boolean objswr = (Boolean) evt.getNewValue();
                boolean valswr = objswr.booleanValue();
                panelLogic.setEal(valswr);
            }
        });
        swAl.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Boolean objswr = (Boolean) evt.getNewValue();
                boolean valswr = objswr.booleanValue();
                panelLogic.setAl(valswr);
            }
        });
        swClear.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Boolean objswr = (Boolean) evt.getNewValue();
                boolean valswr = objswr.booleanValue();
                panelLogic.setClear(valswr);
            }
        });
        swCont.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Boolean objswr = (Boolean) evt.getNewValue();
                boolean valswr = objswr.booleanValue();
                panelLogic.setCont(valswr);
            }
        });
        swExam.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Boolean objswr = (Boolean) evt.getNewValue();
                boolean valswr = objswr.booleanValue();
                panelLogic.setExam(valswr);
            }
        });
        swHalt.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Boolean objswr = (Boolean) evt.getNewValue();
                boolean valswr = objswr.booleanValue();
                panelLogic.setHalt(valswr);
            }
        });
        swSingStep.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Boolean objswr = (Boolean) evt.getNewValue();
                boolean valswr = objswr.booleanValue();
                panelLogic.setSingStep(valswr);
            }
        });
        swDep.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Boolean objswr = (Boolean) evt.getNewValue();
                boolean valswr = objswr.booleanValue();
                panelLogic.setDep(valswr);
            }
        });
        swReg.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Integer objswr = (Integer) evt.getNewValue();
                int valswr = objswr.intValue();
                panelLogic.setSwitchReg(valswr);
            }
        });
        knobSelect.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Integer objswr = (Integer) evt.getNewValue();
                int valswr = objswr.intValue();
                panelLogic.setSelect(valswr);
            }
        });
        knobPower.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Integer objswr = (Integer) evt.getNewValue();
                int valswr = objswr.intValue();
                panelLogic.setPower(valswr);
            }
        });
        addMouseListener(this);
        jboot = new JMenu("Boot");
        JMenuItem si3040 = new JMenuItem("SI3040 Disk Unit 0");
        si3040.addActionListener(this);
        si3040.setName("0");
        jboot.add(si3040);
        JMenuItem si3040_1 = new JMenuItem("SI3040 Disk Unit 1");
        si3040_1.addActionListener(this);
        si3040_1.setName("1");
        jboot.add(si3040_1);
        JMenuItem td8e = new JMenuItem("TD8E Tape");
        td8e.addActionListener(this);
        td8e.setName("2");
        jboot.add(td8e);
        JMenuItem binlo = new JMenuItem("BIN loader low speed");
        binlo.addActionListener(this);
        binlo.setName("3");
        jboot.add(binlo);
        JMenuItem binhi = new JMenuItem("BIN loader hi speed");
        binhi.addActionListener(this);
        binhi.setName("4");
        jboot.add(binhi);
        JMenuItem rimlo = new JMenuItem("RIM loader low speed");
        rimlo.addActionListener(this);
        rimlo.setName("5");
        jboot.add(rimlo);
        JMenuItem rimhi = new JMenuItem("RIM loader hi speed");
        rimhi.addActionListener(this);
        rimhi.setName("6");
        jboot.add(rimhi);
        JMenuItem binpu = new JMenuItem("BIN punch hi speed");
        binpu.addActionListener(this);
        binpu.setName("7");
        jboot.add(binpu);
        jPopupMenu1.add(jboot);
        jPopupMenu1.addSeparator();
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem pdpspeed = new JRadioButtonMenuItem("PDP8 Speed");
        pdpspeed.addActionListener(this);
        pdpspeed.setName("8");
        group.add(pdpspeed);
        jPopupMenu1.add(pdpspeed);
        JRadioButtonMenuItem fullspeed = new JRadioButtonMenuItem("Full Speed");
        fullspeed.addActionListener(this);
        fullspeed.setName("9");
        fullspeed.setSelected(true);
        group.add(fullspeed);
        jPopupMenu1.add(fullspeed);
        jPopupMenu1.addSeparator();
        ButtonGroup group2 = new ButtonGroup();
        JRadioButtonMenuItem estyle = new JRadioButtonMenuItem("E style");
        estyle.addActionListener(this);
        estyle.setName("10");
        estyle.setSelected(true);
        group2.add(estyle);
        jPopupMenu1.add(estyle);
        JRadioButtonMenuItem istyle = new JRadioButtonMenuItem("I style");
        istyle.addActionListener(this);
        istyle.setName("11");
        group2.add(istyle);
        jPopupMenu1.add(istyle);
        jPopupMenu1.addSeparator();
        JCheckBoxMenuItem timshadis = new JCheckBoxMenuItem("Time Share",true);
        timshadis.addActionListener(this);
        timshadis.setName("12");
        jPopupMenu1.add(timshadis);
        JCheckBoxMenuItem fppena = new JCheckBoxMenuItem("FPP",true);
        fppena.addActionListener(this);
        fppena.setName("13");
        jPopupMenu1.add(fppena);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPopupMenu1 = new javax.swing.JPopupMenu();
        fill1 = new javax.swing.JLabel();
        fill2l = new javax.swing.JLabel();
        text2Type = new javax.swing.JLabel();
        text3Company = new javax.swing.JLabel();
        fill2r = new javax.swing.JLabel();
        text4MemAddr = new javax.swing.JLabel();
        panelLampsSelector = new javax.swing.JPanel();
        fill4l = new javax.swing.JLabel();
        text4Ema = new javax.swing.JLabel();
        fill4Ma0_2 = new javax.swing.JLabel();
        fill4Ma3_4 = new javax.swing.JLabel();
        fill4Ma6_8 = new javax.swing.JLabel();
        fill4Ma9_11 = new javax.swing.JLabel();
        fill4r1 = new javax.swing.JLabel();
        text4Run = new javax.swing.JLabel();
        fill4r2 = new javax.swing.JLabel();
        xmaLamps = new Panel.XmaLamps();
        emaLamps = new Panel.EmaLamps();
        maLamps = new Panel.MaLamps();
        fill5r1 = new javax.swing.JLabel();
        runLamp = new Panel.Lamp();
        fill5r2 = new javax.swing.JLabel();
        fill6l1 = new javax.swing.JLabel();
        fill6l2 = new javax.swing.JLabel();
        fill6Bus0_2 = new javax.swing.JLabel();
        fill6Bus3_5 = new javax.swing.JLabel();
        fill6Bus6_8 = new javax.swing.JLabel();
        fill6Bus9_11 = new javax.swing.JLabel();
        fill6r1 = new javax.swing.JLabel();
        fill6r2 = new javax.swing.JLabel();
        fill6r3 = new javax.swing.JLabel();
        panel6Selector = new javax.swing.JPanel();
        textState = new javax.swing.JLabel();
        textStatus = new javax.swing.JLabel();
        textRegAc = new javax.swing.JLabel();
        textRegMd = new javax.swing.JLabel();
        textRegMq = new javax.swing.JLabel();
        textRegBus = new javax.swing.JLabel();
        knobSelect = new Panel.Knob();
        stateLamps = new Panel.StateLamps();
        statusLamps = new Panel.StatusLamps();
        mdLamps = new Panel.MdLamps();
        dataLamps = new Panel.DataLamps();
        acLamps = new Panel.AcLamps();
        mqLamps = new Panel.MqLamps();
        fill6rbis = new javax.swing.JLabel();
        busLamps = new Panel.BusLamps();
        fill7 = new javax.swing.JLabel();
        fill8l1 = new javax.swing.JLabel();
        panel8Switches = new javax.swing.JPanel();
        panelPower = new javax.swing.JPanel();
        textOff = new javax.swing.JLabel();
        textPower = new javax.swing.JLabel();
        textLock = new javax.swing.JLabel();
        knobPower = new Panel.Knob();
        fill8r3 = new javax.swing.JLabel();
        fill8l2 = new javax.swing.JLabel();
        fill8l3 = new javax.swing.JLabel();
        panelSwitch = new javax.swing.JPanel();
        sw = new Panel.Switch();
        textSwitch = new javax.swing.JLabel();
        panelAlEal = new javax.swing.JPanel();
        textAddrLoad = new javax.swing.JLabel();
        swAl = new Panel.Switch();
        textExtdAddrLoad = new javax.swing.JLabel();
        swEal = new Panel.Switch();
        fill8r1 = new javax.swing.JLabel();
        panelStartExamHaltSS = new javax.swing.JPanel();
        textStart = new javax.swing.JLabel();
        textClear = new javax.swing.JLabel();
        textCont = new javax.swing.JLabel();
        swClear = new Panel.Switch();
        swCont = new Panel.Switch();
        textExam = new javax.swing.JLabel();
        swSingStep = new Panel.Switch();
        textHalt = new javax.swing.JLabel();
        swHalt = new Panel.Switch();
        textSingStep = new javax.swing.JLabel();
        swExam = new Panel.Switch();
        fill8r2 = new javax.swing.JLabel();
        panelDep = new javax.swing.JPanel();
        textDep = new javax.swing.JLabel();
        swDep = new Panel.Switch();
        swReg = new Panel.SwitchReg();
        fill8r4 = new javax.swing.JLabel();
        fill9 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(new java.awt.Color(255, 51, 204));
        setMaximumSize(new java.awt.Dimension(800, 362));
        setMinimumSize(new java.awt.Dimension(400, 181));
        setPreferredSize(new java.awt.Dimension(800, 362));
        setLayout(new java.awt.GridBagLayout());

        fill1.setBackground(new java.awt.Color(0, 0, 0));
        fill1.setIconTextGap(40);
        fill1.setMaximumSize(new java.awt.Dimension(800, 40));
        fill1.setMinimumSize(new java.awt.Dimension(400, 20));
        fill1.setOpaque(true);
        fill1.setPreferredSize(new java.awt.Dimension(800, 40));
        fill1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                fill1ComponentResized(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        add(fill1, gridBagConstraints);

        fill2l.setBackground(new java.awt.Color(0, 0, 0));
        fill2l.setIconTextGap(40);
        fill2l.setMaximumSize(new java.awt.Dimension(36, 46));
        fill2l.setMinimumSize(new java.awt.Dimension(18, 23));
        fill2l.setPreferredSize(new java.awt.Dimension(36, 46));
        fill2l.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        add(fill2l, gridBagConstraints);

        text2Type.setBackground(new java.awt.Color(193, 77, 28));
        text2Type.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        text2Type.setForeground(new java.awt.Color(255, 255, 255));
        text2Type.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        text2Type.setText("     | d | i | g | i | t | a | l |   pdp8/e");
        text2Type.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        text2Type.setMaximumSize(new java.awt.Dimension(728, 32));
        text2Type.setMinimumSize(new java.awt.Dimension(364, 16));
        text2Type.setOpaque(true);
        text2Type.setPreferredSize(new java.awt.Dimension(728, 32));
        text2Type.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        add(text2Type, gridBagConstraints);

        text3Company.setBackground(new java.awt.Color(193, 132, 29));
        text3Company.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        text3Company.setForeground(new java.awt.Color(255, 255, 255));
        text3Company.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        text3Company.setText("              digital equipment corporation maynard massachusetts");
        text3Company.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        text3Company.setMaximumSize(new java.awt.Dimension(728, 8));
        text3Company.setMinimumSize(new java.awt.Dimension(364, 4));
        text3Company.setOpaque(true);
        text3Company.setPreferredSize(new java.awt.Dimension(728, 8));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        add(text3Company, gridBagConstraints);

        fill2r.setBackground(new java.awt.Color(0, 0, 0));
        fill2r.setMaximumSize(new java.awt.Dimension(36, 46));
        fill2r.setMinimumSize(new java.awt.Dimension(18, 23));
        fill2r.setPreferredSize(new java.awt.Dimension(36, 46));
        fill2r.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 2;
        add(fill2r, gridBagConstraints);

        text4MemAddr.setBackground(new java.awt.Color(0, 0, 0));
        text4MemAddr.setFont(new java.awt.Font("SansSerif", 0, 9)); // NOI18N
        text4MemAddr.setForeground(new java.awt.Color(255, 255, 255));
        text4MemAddr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text4MemAddr.setText("MEMORY ADDRESS");
        text4MemAddr.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        text4MemAddr.setMaximumSize(new java.awt.Dimension(800, 20));
        text4MemAddr.setMinimumSize(new java.awt.Dimension(400, 10));
        text4MemAddr.setOpaque(true);
        text4MemAddr.setPreferredSize(new java.awt.Dimension(800, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        add(text4MemAddr, gridBagConstraints);

        panelLampsSelector.setMaximumSize(new java.awt.Dimension(800, 130));
        panelLampsSelector.setLayout(new java.awt.GridBagLayout());

        fill4l.setBackground(new java.awt.Color(0, 0, 0));
        fill4l.setMaximumSize(new java.awt.Dimension(110, 10));
        fill4l.setMinimumSize(new java.awt.Dimension(55, 5));
        fill4l.setOpaque(true);
        fill4l.setPreferredSize(new java.awt.Dimension(110, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelLampsSelector.add(fill4l, gridBagConstraints);

        text4Ema.setBackground(new java.awt.Color(193, 132, 29));
        text4Ema.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        text4Ema.setForeground(new java.awt.Color(255, 255, 255));
        text4Ema.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text4Ema.setText("EMA");
        text4Ema.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        text4Ema.setMaximumSize(new java.awt.Dimension(78, 10));
        text4Ema.setMinimumSize(new java.awt.Dimension(39, 5));
        text4Ema.setOpaque(true);
        text4Ema.setPreferredSize(new java.awt.Dimension(78, 10));
        panelLampsSelector.add(text4Ema, new java.awt.GridBagConstraints());

        fill4Ma0_2.setBackground(new java.awt.Color(193, 77, 28));
        fill4Ma0_2.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        fill4Ma0_2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fill4Ma0_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        fill4Ma0_2.setMaximumSize(new java.awt.Dimension(78, 10));
        fill4Ma0_2.setMinimumSize(new java.awt.Dimension(39, 5));
        fill4Ma0_2.setOpaque(true);
        fill4Ma0_2.setPreferredSize(new java.awt.Dimension(78, 10));
        panelLampsSelector.add(fill4Ma0_2, new java.awt.GridBagConstraints());

        fill4Ma3_4.setBackground(new java.awt.Color(193, 132, 29));
        fill4Ma3_4.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        fill4Ma3_4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fill4Ma3_4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        fill4Ma3_4.setMaximumSize(new java.awt.Dimension(78, 10));
        fill4Ma3_4.setMinimumSize(new java.awt.Dimension(39, 5));
        fill4Ma3_4.setOpaque(true);
        fill4Ma3_4.setPreferredSize(new java.awt.Dimension(78, 10));
        panelLampsSelector.add(fill4Ma3_4, new java.awt.GridBagConstraints());

        fill4Ma6_8.setBackground(new java.awt.Color(193, 77, 28));
        fill4Ma6_8.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        fill4Ma6_8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fill4Ma6_8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        fill4Ma6_8.setMaximumSize(new java.awt.Dimension(78, 10));
        fill4Ma6_8.setMinimumSize(new java.awt.Dimension(39, 5));
        fill4Ma6_8.setPreferredSize(new java.awt.Dimension(78, 10));
        fill4Ma6_8.setOpaque(true);
        panelLampsSelector.add(fill4Ma6_8, new java.awt.GridBagConstraints());

        fill4Ma9_11.setBackground(new java.awt.Color(193, 132, 29));
        fill4Ma9_11.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        fill4Ma9_11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fill4Ma9_11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        fill4Ma9_11.setMaximumSize(new java.awt.Dimension(78, 10));
        fill4Ma9_11.setMinimumSize(new java.awt.Dimension(39, 5));
        fill4Ma9_11.setOpaque(true);
        fill4Ma9_11.setPreferredSize(new java.awt.Dimension(78, 10));
        panelLampsSelector.add(fill4Ma9_11, new java.awt.GridBagConstraints());

        fill4r1.setBackground(new java.awt.Color(0, 0, 0));
        fill4r1.setMaximumSize(new java.awt.Dimension(70, 10));
        fill4r1.setMinimumSize(new java.awt.Dimension(35, 5));
        fill4r1.setOpaque(true);
        fill4r1.setPreferredSize(new java.awt.Dimension(70, 10));
        panelLampsSelector.add(fill4r1, new java.awt.GridBagConstraints());

        text4Run.setBackground(new java.awt.Color(193, 132, 29));
        text4Run.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        text4Run.setForeground(new java.awt.Color(255, 255, 255));
        text4Run.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text4Run.setText("RUN");
        text4Run.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        text4Run.setMaximumSize(new java.awt.Dimension(26, 10));
        text4Run.setMinimumSize(new java.awt.Dimension(13, 5));
        text4Run.setOpaque(true);
        text4Run.setPreferredSize(new java.awt.Dimension(26, 10));
        panelLampsSelector.add(text4Run, new java.awt.GridBagConstraints());

        fill4r2.setBackground(new java.awt.Color(0, 0, 0));
        fill4r2.setMaximumSize(new java.awt.Dimension(204, 10));
        fill4r2.setMinimumSize(new java.awt.Dimension(102, 5));
        fill4r2.setOpaque(true);
        fill4r2.setPreferredSize(new java.awt.Dimension(204, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panelLampsSelector.add(fill4r2, gridBagConstraints);
        panelLampsSelector.add(xmaLamps, new java.awt.GridBagConstraints());

        emaLamps.setName("ema"); // NOI18N
        panelLampsSelector.add(emaLamps, new java.awt.GridBagConstraints());

        maLamps.setName("ma"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 4;
        panelLampsSelector.add(maLamps, gridBagConstraints);

        fill5r1.setBackground(new java.awt.Color(0, 0, 0));
        fill5r1.setMaximumSize(new java.awt.Dimension(70, 10));
        fill5r1.setMinimumSize(new java.awt.Dimension(35, 5));
        fill5r1.setOpaque(true);
        fill5r1.setPreferredSize(new java.awt.Dimension(70, 10));
        panelLampsSelector.add(fill5r1, new java.awt.GridBagConstraints());

        runLamp.setMaximumSize(new java.awt.Dimension(26, 10));
        runLamp.setMinimumSize(new java.awt.Dimension(13, 5));
        runLamp.setName("run"); // NOI18N
        runLamp.setPreferredSize(new java.awt.Dimension(26, 10));
        panelLampsSelector.add(runLamp, new java.awt.GridBagConstraints());

        fill5r2.setBackground(new java.awt.Color(0, 0, 0));
        fill5r2.setMaximumSize(new java.awt.Dimension(204, 10));
        fill5r2.setMinimumSize(new java.awt.Dimension(102, 5));
        fill5r2.setOpaque(true);
        fill5r2.setPreferredSize(new java.awt.Dimension(204, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panelLampsSelector.add(fill5r2, gridBagConstraints);

        fill6l1.setBackground(new java.awt.Color(0, 0, 0));
        fill6l1.setMaximumSize(new java.awt.Dimension(110, 108));
        fill6l1.setMinimumSize(new java.awt.Dimension(55, 54));
        fill6l1.setOpaque(true);
        fill6l1.setPreferredSize(new java.awt.Dimension(110, 108));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 3;
        panelLampsSelector.add(fill6l1, gridBagConstraints);

        fill6l2.setBackground(new java.awt.Color(0, 0, 0));
        fill6l2.setMaximumSize(new java.awt.Dimension(78, 108));
        fill6l2.setMinimumSize(new java.awt.Dimension(39, 54));
        fill6l2.setOpaque(true);
        fill6l2.setPreferredSize(new java.awt.Dimension(78, 108));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panelLampsSelector.add(fill6l2, gridBagConstraints);

        fill6Bus0_2.setBackground(new java.awt.Color(0, 0, 0));
        fill6Bus0_2.setMaximumSize(new java.awt.Dimension(78, 22));
        fill6Bus0_2.setMinimumSize(new java.awt.Dimension(39, 11));
        fill6Bus0_2.setOpaque(true);
        fill6Bus0_2.setPreferredSize(new java.awt.Dimension(78, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panelLampsSelector.add(fill6Bus0_2, gridBagConstraints);

        fill6Bus3_5.setBackground(new java.awt.Color(0, 0, 0));
        fill6Bus3_5.setMaximumSize(new java.awt.Dimension(78, 22));
        fill6Bus3_5.setMinimumSize(new java.awt.Dimension(39, 11));
        fill6Bus3_5.setOpaque(true);
        fill6Bus3_5.setPreferredSize(new java.awt.Dimension(78, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panelLampsSelector.add(fill6Bus3_5, gridBagConstraints);

        fill6Bus6_8.setBackground(new java.awt.Color(0, 0, 0));
        fill6Bus6_8.setMaximumSize(new java.awt.Dimension(78, 22));
        fill6Bus6_8.setMinimumSize(new java.awt.Dimension(39, 11));
        fill6Bus6_8.setOpaque(true);
        fill6Bus6_8.setPreferredSize(new java.awt.Dimension(78, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panelLampsSelector.add(fill6Bus6_8, gridBagConstraints);

        fill6Bus9_11.setBackground(new java.awt.Color(0, 0, 0));
        fill6Bus9_11.setMaximumSize(new java.awt.Dimension(78, 22));
        fill6Bus9_11.setMinimumSize(new java.awt.Dimension(39, 11));
        fill6Bus9_11.setOpaque(true);
        fill6Bus9_11.setPreferredSize(new java.awt.Dimension(78, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panelLampsSelector.add(fill6Bus9_11, gridBagConstraints);

        fill6r1.setBackground(new java.awt.Color(0, 0, 0));
        fill6r1.setMaximumSize(new java.awt.Dimension(70, 32));
        fill6r1.setMinimumSize(new java.awt.Dimension(35, 16));
        fill6r1.setOpaque(true);
        fill6r1.setPreferredSize(new java.awt.Dimension(70, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        panelLampsSelector.add(fill6r1, gridBagConstraints);

        fill6r2.setBackground(new java.awt.Color(0, 0, 0));
        fill6r2.setMaximumSize(new java.awt.Dimension(26, 32));
        fill6r2.setMinimumSize(new java.awt.Dimension(13, 16));
        fill6r2.setOpaque(true);
        fill6r2.setPreferredSize(new java.awt.Dimension(26, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        panelLampsSelector.add(fill6r2, gridBagConstraints);

        fill6r3.setBackground(new java.awt.Color(0, 0, 0));
        fill6r3.setMaximumSize(new java.awt.Dimension(204, 32));
        fill6r3.setMinimumSize(new java.awt.Dimension(102, 16));
        fill6r3.setOpaque(true);
        fill6r3.setPreferredSize(new java.awt.Dimension(204, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 2;
        panelLampsSelector.add(fill6r3, gridBagConstraints);

        panel6Selector.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        panel6Selector.setMaximumSize(new java.awt.Dimension(408, 76));
        panel6Selector.setMinimumSize(new java.awt.Dimension(204, 38));
        panel6Selector.setPreferredSize(new java.awt.Dimension(408, 76));
        panel6Selector.setLayout(new java.awt.GridBagLayout());

        textState.setBackground(new java.awt.Color(0, 0, 0));
        textState.setFont(new java.awt.Font("Dialog", 0, 7)); // NOI18N
        textState.setForeground(new java.awt.Color(255, 255, 255));
        textState.setText(" STATE ");
        textState.setMaximumSize(new java.awt.Dimension(40, 12));
        textState.setMinimumSize(new java.awt.Dimension(20, 6));
        textState.setOpaque(true);
        textState.setPreferredSize(new java.awt.Dimension(40, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panel6Selector.add(textState, gridBagConstraints);

        textStatus.setBackground(new java.awt.Color(0, 0, 0));
        textStatus.setFont(new java.awt.Font("Dialog", 0, 7)); // NOI18N
        textStatus.setForeground(new java.awt.Color(255, 255, 255));
        textStatus.setText(" STATUS");
        textStatus.setMaximumSize(new java.awt.Dimension(40, 12));
        textStatus.setMinimumSize(new java.awt.Dimension(20, 6));
        textStatus.setOpaque(true);
        textStatus.setPreferredSize(new java.awt.Dimension(40, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panel6Selector.add(textStatus, gridBagConstraints);

        textRegAc.setBackground(new java.awt.Color(0, 0, 0));
        textRegAc.setFont(new java.awt.Font("Dialog", 0, 7)); // NOI18N
        textRegAc.setForeground(new java.awt.Color(255, 255, 255));
        textRegAc.setText(" AC ");
        textRegAc.setMaximumSize(new java.awt.Dimension(40, 12));
        textRegAc.setMinimumSize(new java.awt.Dimension(20, 6));
        textRegAc.setOpaque(true);
        textRegAc.setPreferredSize(new java.awt.Dimension(40, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panel6Selector.add(textRegAc, gridBagConstraints);

        textRegMd.setBackground(new java.awt.Color(0, 0, 0));
        textRegMd.setFont(new java.awt.Font("Dialog", 0, 7)); // NOI18N
        textRegMd.setForeground(new java.awt.Color(255, 255, 255));
        textRegMd.setText(" MD ");
        textRegMd.setMaximumSize(new java.awt.Dimension(40, 12));
        textRegMd.setMinimumSize(new java.awt.Dimension(20, 6));
        textRegMd.setOpaque(true);
        textRegMd.setPreferredSize(new java.awt.Dimension(40, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        panel6Selector.add(textRegMd, gridBagConstraints);

        textRegMq.setBackground(new java.awt.Color(0, 0, 0));
        textRegMq.setFont(new java.awt.Font("Dialog", 0, 7)); // NOI18N
        textRegMq.setForeground(new java.awt.Color(255, 255, 255));
        textRegMq.setText(" MQ  ");
        textRegMq.setMaximumSize(new java.awt.Dimension(40, 12));
        textRegMq.setMinimumSize(new java.awt.Dimension(20, 6));
        textRegMq.setOpaque(true);
        textRegMq.setPreferredSize(new java.awt.Dimension(40, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        panel6Selector.add(textRegMq, gridBagConstraints);

        textRegBus.setBackground(new java.awt.Color(0, 0, 0));
        textRegBus.setFont(new java.awt.Font("Dialog", 0, 7)); // NOI18N
        textRegBus.setForeground(new java.awt.Color(255, 255, 255));
        textRegBus.setText(" BUS  ");
        textRegBus.setMaximumSize(new java.awt.Dimension(40, 12));
        textRegBus.setMinimumSize(new java.awt.Dimension(20, 6));
        textRegBus.setOpaque(true);
        textRegBus.setPreferredSize(new java.awt.Dimension(40, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        panel6Selector.add(textRegBus, gridBagConstraints);

        knobSelect.setBackground(new java.awt.Color(0, 0, 0));
        knobSelect.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        knobSelect.setLength(110.0F);
        knobSelect.setMaximumSize(new java.awt.Dimension(52, 72));
        knobSelect.setMinimumSize(new java.awt.Dimension(26, 36));
        knobSelect.setName("select"); // NOI18N
        knobSelect.setPreferredSize(new java.awt.Dimension(52, 72));
        knobSelect.setStart(235.0F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 6;
        panel6Selector.add(knobSelect, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panel6Selector.add(stateLamps, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panel6Selector.add(statusLamps, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panel6Selector.add(mdLamps, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        panel6Selector.add(dataLamps, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panel6Selector.add(acLamps, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panel6Selector.add(mqLamps, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelLampsSelector.add(panel6Selector, gridBagConstraints);

        fill6rbis.setBackground(new java.awt.Color(0, 0, 0));
        fill6rbis.setMaximumSize(new java.awt.Dimension(204, 76));
        fill6rbis.setMinimumSize(new java.awt.Dimension(102, 38));
        fill6rbis.setOpaque(true);
        fill6rbis.setPreferredSize(new java.awt.Dimension(204, 76));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 2;
        panelLampsSelector.add(fill6rbis, gridBagConstraints);

        busLamps.setName("bus"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        panelLampsSelector.add(busLamps, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(panelLampsSelector, gridBagConstraints);

        fill7.setBackground(new java.awt.Color(0, 0, 0));
        fill7.setIconTextGap(40);
        fill7.setMaximumSize(new java.awt.Dimension(800, 24));
        fill7.setMinimumSize(new java.awt.Dimension(400, 12));
        fill7.setOpaque(true);
        fill7.setPreferredSize(new java.awt.Dimension(800, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        add(fill7, gridBagConstraints);

        fill8l1.setBackground(new java.awt.Color(0, 0, 0));
        fill8l1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fill8l1.setMaximumSize(new java.awt.Dimension(36, 72));
        fill8l1.setMinimumSize(new java.awt.Dimension(18, 36));
        fill8l1.setOpaque(true);
        fill8l1.setPreferredSize(new java.awt.Dimension(36, 72));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(fill8l1, gridBagConstraints);

        panel8Switches.setBackground(new java.awt.Color(153, 153, 153));
        panel8Switches.setMaximumSize(new java.awt.Dimension(728, 72));
        panel8Switches.setMinimumSize(new java.awt.Dimension(364, 36));
        panel8Switches.setPreferredSize(new java.awt.Dimension(728, 72));
        panel8Switches.setLayout(new java.awt.GridBagLayout());

        panelPower.setBackground(new java.awt.Color(0, 0, 0));
        panelPower.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)))));
        panelPower.setMaximumSize(new java.awt.Dimension(84, 72));
        panelPower.setMinimumSize(new java.awt.Dimension(42, 36));
        panelPower.setLayout(new java.awt.GridBagLayout());

        textOff.setBackground(new java.awt.Color(193, 77, 28));
        textOff.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        textOff.setForeground(new java.awt.Color(255, 255, 255));
        textOff.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textOff.setText("OFF");
        textOff.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        textOff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        textOff.setMaximumSize(new java.awt.Dimension(26, 66));
        textOff.setMinimumSize(new java.awt.Dimension(13, 33));
        textOff.setOpaque(true);
        textOff.setPreferredSize(new java.awt.Dimension(26, 66));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        panelPower.add(textOff, gridBagConstraints);

        textPower.setBackground(new java.awt.Color(193, 132, 28));
        textPower.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        textPower.setForeground(new java.awt.Color(255, 255, 255));
        textPower.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textPower.setText("PW");
        textPower.setToolTipText("POWER");
        textPower.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        textPower.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        textPower.setMaximumSize(new java.awt.Dimension(26, 22));
        textPower.setMinimumSize(new java.awt.Dimension(13, 11));
        textPower.setOpaque(true);
        textPower.setPreferredSize(new java.awt.Dimension(26, 22));
        panelPower.add(textPower, new java.awt.GridBagConstraints());

        textLock.setBackground(new java.awt.Color(193, 77, 28));
        textLock.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        textLock.setForeground(new java.awt.Color(255, 255, 255));
        textLock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textLock.setText("LOCK");
        textLock.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        textLock.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        textLock.setMaximumSize(new java.awt.Dimension(26, 66));
        textLock.setMinimumSize(new java.awt.Dimension(13, 33));
        textLock.setOpaque(true);
        textLock.setPreferredSize(new java.awt.Dimension(26, 66));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        panelPower.add(textLock, gridBagConstraints);

        knobPower.setBackground(new java.awt.Color(193, 132, 29));
        knobPower.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        knobPower.setLength(100.0F);
        knobPower.setMaximumSize(new java.awt.Dimension(26, 44));
        knobPower.setMinimumSize(new java.awt.Dimension(13, 22));
        knobPower.setName("power"); // NOI18N
        knobPower.setPreferredSize(new java.awt.Dimension(26, 44));
        knobPower.setStart(140.0F);
        knobPower.setTicks(3);
        knobPower.setValue(0.5F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panelPower.add(knobPower, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panel8Switches.add(panelPower, gridBagConstraints);

        fill8r3.setBackground(new java.awt.Color(0, 0, 0));
        fill8r3.setFont(new java.awt.Font("Dialog", 0, 7)); // NOI18N
        fill8r3.setForeground(new java.awt.Color(255, 255, 255));
        fill8r3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fill8r3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        fill8r3.setMaximumSize(new java.awt.Dimension(16, 72));
        fill8r3.setMinimumSize(new java.awt.Dimension(6, 36));
        fill8r3.setOpaque(true);
        fill8r3.setPreferredSize(new java.awt.Dimension(16, 72));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        panel8Switches.add(fill8r3, gridBagConstraints);

        fill8l2.setBackground(new java.awt.Color(0, 0, 0));
        fill8l2.setFont(new java.awt.Font("Dialog", 0, 7)); // NOI18N
        fill8l2.setForeground(new java.awt.Color(255, 255, 255));
        fill8l2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fill8l2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        fill8l2.setMaximumSize(new java.awt.Dimension(18, 72));
        fill8l2.setMinimumSize(new java.awt.Dimension(8, 36));
        fill8l2.setOpaque(true);
        fill8l2.setPreferredSize(new java.awt.Dimension(18, 72));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panel8Switches.add(fill8l2, gridBagConstraints);

        fill8l3.setBackground(new java.awt.Color(0, 0, 0));
        fill8l3.setFont(new java.awt.Font("Dialog", 0, 7)); // NOI18N
        fill8l3.setForeground(new java.awt.Color(255, 255, 255));
        fill8l3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fill8l3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        fill8l3.setMaximumSize(new java.awt.Dimension(18, 72));
        fill8l3.setMinimumSize(new java.awt.Dimension(8, 36));
        fill8l3.setOpaque(true);
        fill8l3.setPreferredSize(new java.awt.Dimension(18, 72));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panel8Switches.add(fill8l3, gridBagConstraints);

        panelSwitch.setBackground(new java.awt.Color(0, 0, 0));
        panelSwitch.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)))));
        panelSwitch.setMaximumSize(new java.awt.Dimension(32, 72));
        panelSwitch.setMinimumSize(new java.awt.Dimension(16, 36));
        panelSwitch.setLayout(new java.awt.GridBagLayout());

        sw.setMaximumSize(new java.awt.Dimension(24, 44));
        sw.setMinimumSize(new java.awt.Dimension(12, 22));
        sw.setName("sw"); // NOI18N
        sw.setPreferredSize(new java.awt.Dimension(24, 44));
        sw.setToggle(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panelSwitch.add(sw, gridBagConstraints);

        textSwitch.setBackground(new java.awt.Color(193, 132, 29));
        textSwitch.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        textSwitch.setForeground(new java.awt.Color(255, 255, 255));
        textSwitch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textSwitch.setText("SW");
        textSwitch.setToolTipText("SWITCH");
        textSwitch.setMaximumSize(new java.awt.Dimension(24, 22));
        textSwitch.setMinimumSize(new java.awt.Dimension(12, 11));
        textSwitch.setOpaque(true);
        textSwitch.setPreferredSize(new java.awt.Dimension(24, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panelSwitch.add(textSwitch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panel8Switches.add(panelSwitch, gridBagConstraints);

        panelAlEal.setBackground(new java.awt.Color(0, 0, 0));
        panelAlEal.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)))));
        panelAlEal.setMaximumSize(new java.awt.Dimension(58, 72));
        panelAlEal.setMinimumSize(new java.awt.Dimension(29, 36));
        panelAlEal.setLayout(new java.awt.GridBagLayout());

        textAddrLoad.setBackground(new java.awt.Color(193, 77, 28));
        textAddrLoad.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        textAddrLoad.setForeground(new java.awt.Color(255, 255, 255));
        textAddrLoad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textAddrLoad.setText("AL");
        textAddrLoad.setToolTipText("ADDR LOAD");
        textAddrLoad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        textAddrLoad.setMaximumSize(new java.awt.Dimension(26, 22));
        textAddrLoad.setMinimumSize(new java.awt.Dimension(12, 11));
        textAddrLoad.setOpaque(true);
        textAddrLoad.setPreferredSize(new java.awt.Dimension(24, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        panelAlEal.add(textAddrLoad, gridBagConstraints);

        swAl.setBackground(new java.awt.Color(193, 77, 28));
        swAl.setMaximumSize(new java.awt.Dimension(24, 44));
        swAl.setMinimumSize(new java.awt.Dimension(12, 22));
        swAl.setName("al"); // NOI18N
        swAl.setPreferredSize(new java.awt.Dimension(24, 44));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        panelAlEal.add(swAl, gridBagConstraints);

        textExtdAddrLoad.setBackground(new java.awt.Color(193, 132, 29));
        textExtdAddrLoad.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        textExtdAddrLoad.setForeground(new java.awt.Color(255, 255, 255));
        textExtdAddrLoad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textExtdAddrLoad.setText("EAL");
        textExtdAddrLoad.setToolTipText("EXTD ADDR LOAD");
        textExtdAddrLoad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        textExtdAddrLoad.setMaximumSize(new java.awt.Dimension(24, 22));
        textExtdAddrLoad.setMinimumSize(new java.awt.Dimension(12, 11));
        textExtdAddrLoad.setOpaque(true);
        textExtdAddrLoad.setPreferredSize(new java.awt.Dimension(24, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        panelAlEal.add(textExtdAddrLoad, gridBagConstraints);

        swEal.setMaximumSize(new java.awt.Dimension(24, 44));
        swEal.setMinimumSize(new java.awt.Dimension(12, 22));
        swEal.setName("eal"); // NOI18N
        swEal.setPreferredSize(new java.awt.Dimension(24, 44));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        panelAlEal.add(swEal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        panel8Switches.add(panelAlEal, gridBagConstraints);

        fill8r1.setBackground(new java.awt.Color(0, 0, 0));
        fill8r1.setFont(new java.awt.Font("Dialog", 0, 7)); // NOI18N
        fill8r1.setForeground(new java.awt.Color(255, 255, 255));
        fill8r1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fill8r1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        fill8r1.setMaximumSize(new java.awt.Dimension(16, 72));
        fill8r1.setMinimumSize(new java.awt.Dimension(6, 36));
        fill8r1.setPreferredSize(new java.awt.Dimension(16, 72));
        fill8r1.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        panel8Switches.add(fill8r1, gridBagConstraints);

        panelStartExamHaltSS.setBackground(new java.awt.Color(0, 0, 0));
        panelStartExamHaltSS.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)))));
        panelStartExamHaltSS.setMaximumSize(new java.awt.Dimension(136, 72));
        panelStartExamHaltSS.setMinimumSize(new java.awt.Dimension(68, 36));
        panelStartExamHaltSS.setLayout(new java.awt.GridBagLayout());

        textStart.setBackground(new java.awt.Color(193, 77, 28));
        textStart.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        textStart.setForeground(new java.awt.Color(255, 255, 255));
        textStart.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textStart.setText("START");
        textStart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        textStart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        textStart.setMaximumSize(new java.awt.Dimension(48, 10));
        textStart.setMinimumSize(new java.awt.Dimension(24, 5));
        textStart.setOpaque(true);
        textStart.setPreferredSize(new java.awt.Dimension(48, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        panelStartExamHaltSS.add(textStart, gridBagConstraints);

        textClear.setBackground(new java.awt.Color(193, 77, 28));
        textClear.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        textClear.setForeground(new java.awt.Color(255, 255, 255));
        textClear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textClear.setText("CL");
        textClear.setToolTipText("CLEAR");
        textClear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        textClear.setMaximumSize(new java.awt.Dimension(24, 12));
        textClear.setMinimumSize(new java.awt.Dimension(12, 6));
        textClear.setOpaque(true);
        textClear.setPreferredSize(new java.awt.Dimension(24, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panelStartExamHaltSS.add(textClear, gridBagConstraints);

        textCont.setBackground(new java.awt.Color(193, 132, 29));
        textCont.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        textCont.setForeground(new java.awt.Color(255, 255, 255));
        textCont.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textCont.setText("CO");
        textCont.setToolTipText("CONT");
        textCont.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        textCont.setMaximumSize(new java.awt.Dimension(24, 12));
        textCont.setMinimumSize(new java.awt.Dimension(12, 6));
        textCont.setOpaque(true);
        textCont.setPreferredSize(new java.awt.Dimension(24, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panelStartExamHaltSS.add(textCont, gridBagConstraints);

        swClear.setBackground(new java.awt.Color(193, 77, 28));
        swClear.setMaximumSize(new java.awt.Dimension(24, 44));
        swClear.setMinimumSize(new java.awt.Dimension(12, 22));
        swClear.setName("clear"); // NOI18N
        swClear.setPreferredSize(new java.awt.Dimension(24, 44));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panelStartExamHaltSS.add(swClear, gridBagConstraints);

        swCont.setMaximumSize(new java.awt.Dimension(24, 44));
        swCont.setMinimumSize(new java.awt.Dimension(12, 22));
        swCont.setName("cont"); // NOI18N
        swCont.setPreferredSize(new java.awt.Dimension(24, 44));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panelStartExamHaltSS.add(swCont, gridBagConstraints);

        textExam.setBackground(new java.awt.Color(193, 77, 28));
        textExam.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        textExam.setForeground(new java.awt.Color(255, 255, 255));
        textExam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textExam.setText("EXAM");
        textExam.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        textExam.setMaximumSize(new java.awt.Dimension(24, 22));
        textExam.setMinimumSize(new java.awt.Dimension(12, 11));
        textExam.setOpaque(true);
        textExam.setPreferredSize(new java.awt.Dimension(24, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        panelStartExamHaltSS.add(textExam, gridBagConstraints);

        swSingStep.setBackground(new java.awt.Color(193, 77, 28));
        swSingStep.setMaximumSize(new java.awt.Dimension(24, 44));
        swSingStep.setMinimumSize(new java.awt.Dimension(12, 22));
        swSingStep.setName("singstep"); // NOI18N
        swSingStep.setPreferredSize(new java.awt.Dimension(24, 44));
        swSingStep.setToggle(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        panelStartExamHaltSS.add(swSingStep, gridBagConstraints);

        textHalt.setBackground(new java.awt.Color(193, 132, 29));
        textHalt.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        textHalt.setForeground(new java.awt.Color(255, 255, 255));
        textHalt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textHalt.setText("HALT");
        textHalt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        textHalt.setMaximumSize(new java.awt.Dimension(24, 22));
        textHalt.setMinimumSize(new java.awt.Dimension(12, 11));
        textHalt.setOpaque(true);
        textHalt.setPreferredSize(new java.awt.Dimension(24, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        panelStartExamHaltSS.add(textHalt, gridBagConstraints);

        swHalt.setMaximumSize(new java.awt.Dimension(24, 44));
        swHalt.setMinimumSize(new java.awt.Dimension(12, 22));
        swHalt.setName("halt"); // NOI18N
        swHalt.setPreferredSize(new java.awt.Dimension(24, 44));
        swHalt.setToggle(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        panelStartExamHaltSS.add(swHalt, gridBagConstraints);

        textSingStep.setBackground(new java.awt.Color(193, 77, 28));
        textSingStep.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        textSingStep.setForeground(new java.awt.Color(255, 255, 255));
        textSingStep.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textSingStep.setText("SS");
        textSingStep.setToolTipText("SING STEP");
        textSingStep.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        textSingStep.setMaximumSize(new java.awt.Dimension(24, 22));
        textSingStep.setMinimumSize(new java.awt.Dimension(12, 10));
        textSingStep.setOpaque(true);
        textSingStep.setPreferredSize(new java.awt.Dimension(24, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        panelStartExamHaltSS.add(textSingStep, gridBagConstraints);

        swExam.setBackground(new java.awt.Color(193, 77, 28));
        swExam.setMaximumSize(new java.awt.Dimension(24, 44));
        swExam.setMinimumSize(new java.awt.Dimension(12, 22));
        swExam.setName("exam"); // NOI18N
        swExam.setPreferredSize(new java.awt.Dimension(24, 44));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        panelStartExamHaltSS.add(swExam, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        panel8Switches.add(panelStartExamHaltSS, gridBagConstraints);

        fill8r2.setBackground(new java.awt.Color(0, 0, 0));
        fill8r2.setFont(new java.awt.Font("Dialog", 0, 7)); // NOI18N
        fill8r2.setForeground(new java.awt.Color(255, 255, 255));
        fill8r2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fill8r2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        fill8r2.setMaximumSize(new java.awt.Dimension(16, 72));
        fill8r2.setMinimumSize(new java.awt.Dimension(6, 36));
        fill8r2.setPreferredSize(new java.awt.Dimension(16, 72));
        fill8r2.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        panel8Switches.add(fill8r2, gridBagConstraints);

        panelDep.setBackground(new java.awt.Color(0, 0, 0));
        panelDep.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)))));
        panelDep.setMaximumSize(new java.awt.Dimension(32, 72));
        panelDep.setMinimumSize(new java.awt.Dimension(16, 36));
        panelDep.setLayout(new java.awt.GridBagLayout());

        textDep.setBackground(new java.awt.Color(193, 132, 29));
        textDep.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        textDep.setForeground(new java.awt.Color(255, 255, 255));
        textDep.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textDep.setText("DEP");
        textDep.setToolTipText("DEPOSIT");
        textDep.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        textDep.setMaximumSize(new java.awt.Dimension(24, 22));
        textDep.setMinimumSize(new java.awt.Dimension(12, 11));
        textDep.setOpaque(true);
        textDep.setPreferredSize(new java.awt.Dimension(24, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        panelDep.add(textDep, gridBagConstraints);

        swDep.setInitial(false);
        swDep.setMaximumSize(new java.awt.Dimension(24, 44));
        swDep.setMinimumSize(new java.awt.Dimension(12, 22));
        swDep.setName("dep"); // NOI18N
        swDep.setPreferredSize(new java.awt.Dimension(24, 44));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panelDep.add(swDep, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        panel8Switches.add(panelDep, gridBagConstraints);

        swReg.setToolTipText("");
        swReg.setMaximumSize(new java.awt.Dimension(318, 72));
        swReg.setName("swr"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        panel8Switches.add(swReg, gridBagConstraints);

        add(panel8Switches, new java.awt.GridBagConstraints());

        fill8r4.setBackground(new java.awt.Color(0, 0, 0));
        fill8r4.setMaximumSize(new java.awt.Dimension(36, 72));
        fill8r4.setMinimumSize(new java.awt.Dimension(18, 36));
        fill8r4.setPreferredSize(new java.awt.Dimension(36, 72));
        fill8r4.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(fill8r4, gridBagConstraints);

        fill9.setBackground(new java.awt.Color(0, 0, 0));
        fill9.setMaximumSize(new java.awt.Dimension(800, 32));
        fill9.setMinimumSize(new java.awt.Dimension(400, 16));
        fill9.setPreferredSize(new java.awt.Dimension(800, 32));
        fill9.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        add(fill9, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    public  Logic.PanelLogic panelLogic;

    public void setLogic(Logic.PanelLogic panelLogic) {
        this.panelLogic = panelLogic;
    }


                    private void fill1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_fill1ComponentResized
                        // TODO add your handling code here:

                        if (fill1.getWidth()<500) {
                            text2Type.setFont(new java.awt.Font("SansSerif", 1, 10));
                            text3Company.setFont(new java.awt.Font("SansSerif", 0, 5));
                            text4MemAddr.setFont(new java.awt.Font("SansSerif", 0, 4));
                            text4Ema.setFont(new java.awt.Font("Dialog", 0, 4));
                            text4Run.setFont(new java.awt.Font("Dialog", 0, 4));
                            textState.setFont(new java.awt.Font("Dialog", 0, 4));
                            textStatus.setFont(new java.awt.Font("Dialog", 0, 4));
                            textRegAc.setFont(new java.awt.Font("Dialog", 0, 4));
                            textRegMd.setFont(new java.awt.Font("Dialog", 0, 4));
                            textRegMq.setFont(new java.awt.Font("Dialog", 0, 4));
                            textRegBus.setFont(new java.awt.Font("Dialog", 0, 4));
                            textOff.setFont(new java.awt.Font("Dialog", 0, 4));
                            textPower.setFont(new java.awt.Font("Dialog", 0, 4));
                            textLock.setFont(new java.awt.Font("Dialog", 0, 4));
                            textSwitch.setFont(new java.awt.Font("Dialog", 0, 4));
                            textAddrLoad.setFont(new java.awt.Font("Dialog", 0, 4));
                            textExtdAddrLoad.setFont(new java.awt.Font("Dialog", 0, 4));
                            textStart.setFont(new java.awt.Font("Dialog", 0, 4));
                            textClear.setFont(new java.awt.Font("Dialog", 0, 4));
                            textCont.setFont(new java.awt.Font("Dialog", 0, 4));
                            textExam.setFont(new java.awt.Font("Dialog", 0, 4));
                            textDep.setFont(new java.awt.Font("Dialog", 0, 4));
                            textHalt.setFont(new java.awt.Font("Dialog", 0, 4));
                            textSingStep.setFont(new java.awt.Font("Dialog", 0, 4));

                        }else {
                            text2Type.setFont(new java.awt.Font("SansSerif", 1, 20));
                            text3Company.setFont(new java.awt.Font("SansSerif", 0, 10));
                            text4MemAddr.setFont(new java.awt.Font("SansSerif", 0, 8));
                            text4Ema.setFont(new java.awt.Font("Dialog", 0, 8));
                            text4Run.setFont(new java.awt.Font("Dialog", 0, 8));
                            textState.setFont(new java.awt.Font("Dialog", 0, 8));
                            textStatus.setFont(new java.awt.Font("Dialog", 0, 8));
                            textRegAc.setFont(new java.awt.Font("Dialog", 0, 8));
                            textRegMd.setFont(new java.awt.Font("Dialog", 0, 8));
                            textRegMq.setFont(new java.awt.Font("Dialog", 0, 8));
                            textRegBus.setFont(new java.awt.Font("Dialog", 0, 8));
                            textOff.setFont(new java.awt.Font("Dialog", 0, 8));
                            textPower.setFont(new java.awt.Font("Dialog", 0, 8));
                            textLock.setFont(new java.awt.Font("Dialog", 0, 8));
                            textSwitch.setFont(new java.awt.Font("Dialog", 0, 8));
                            textAddrLoad.setFont(new java.awt.Font("Dialog", 0, 8));
                            textExtdAddrLoad.setFont(new java.awt.Font("Dialog", 0, 8));
                            textStart.setFont(new java.awt.Font("Dialog", 0, 8));
                            textClear.setFont(new java.awt.Font("Dialog", 0, 8));
                            textCont.setFont(new java.awt.Font("Dialog", 0, 8));
                            textExam.setFont(new java.awt.Font("Dialog", 0, 8));
                            textDep.setFont(new java.awt.Font("Dialog", 0, 8));
                            textHalt.setFont(new java.awt.Font("Dialog", 0, 8));
                            textSingStep.setFont(new java.awt.Font("Dialog", 0, 8));
                        }

    }//GEN-LAST:event_fill1ComponentResized

    public void mouseClicked(java.awt.event.MouseEvent e) {
        if (e.isPopupTrigger()) {
            jPopupMenu1.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public void mouseEntered(java.awt.event.MouseEvent e) {
    }

    public void mouseExited(java.awt.event.MouseEvent e) {
    }

    public void mousePressed(java.awt.event.MouseEvent e) {
        if (e.isPopupTrigger()) {
            jPopupMenu1.show(e.getComponent(),e.getX(),e.getY());
        }
    }

    public void mouseReleased(java.awt.event.MouseEvent e) {
        if (e.isPopupTrigger()) {
            jPopupMenu1.show(e.getComponent(),e.getX(),e.getY());
        }
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        AbstractButton x = (AbstractButton)(e.getSource());
        Integer val =  new Integer(Integer.parseInt(x.getName()));
        panelLogic.menuContext(val.intValue(),x.isSelected());
        repaint();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public Panel.AcLamps acLamps;
    public Panel.BusLamps busLamps;
    public Panel.DataLamps dataLamps;
    public Panel.EmaLamps emaLamps;
    private javax.swing.JLabel fill1;
    private javax.swing.JLabel fill2l;
    private javax.swing.JLabel fill2r;
    private javax.swing.JLabel fill4Ma0_2;
    private javax.swing.JLabel fill4Ma3_4;
    private javax.swing.JLabel fill4Ma6_8;
    private javax.swing.JLabel fill4Ma9_11;
    private javax.swing.JLabel fill4l;
    private javax.swing.JLabel fill4r1;
    private javax.swing.JLabel fill4r2;
    private javax.swing.JLabel fill5r1;
    private javax.swing.JLabel fill5r2;
    private javax.swing.JLabel fill6Bus0_2;
    private javax.swing.JLabel fill6Bus3_5;
    private javax.swing.JLabel fill6Bus6_8;
    private javax.swing.JLabel fill6Bus9_11;
    private javax.swing.JLabel fill6l1;
    private javax.swing.JLabel fill6l2;
    private javax.swing.JLabel fill6r1;
    private javax.swing.JLabel fill6r2;
    private javax.swing.JLabel fill6r3;
    private javax.swing.JLabel fill6rbis;
    private javax.swing.JLabel fill7;
    private javax.swing.JLabel fill8l1;
    private javax.swing.JLabel fill8l2;
    private javax.swing.JLabel fill8l3;
    private javax.swing.JLabel fill8r1;
    private javax.swing.JLabel fill8r2;
    private javax.swing.JLabel fill8r3;
    private javax.swing.JLabel fill8r4;
    private javax.swing.JLabel fill9;
    private javax.swing.JPopupMenu jPopupMenu1;
    public Panel.Knob knobPower;
    private Panel.Knob knobSelect;
    public Panel.MaLamps maLamps;
    public Panel.MdLamps mdLamps;
    public Panel.MqLamps mqLamps;
    private javax.swing.JPanel panel6Selector;
    private javax.swing.JPanel panel8Switches;
    private javax.swing.JPanel panelAlEal;
    private javax.swing.JPanel panelDep;
    private javax.swing.JPanel panelLampsSelector;
    private javax.swing.JPanel panelPower;
    private javax.swing.JPanel panelStartExamHaltSS;
    private javax.swing.JPanel panelSwitch;
    public Panel.Lamp runLamp;
    public Panel.StateLamps stateLamps;
    public Panel.StatusLamps statusLamps;
    private Panel.Switch sw;
    private Panel.Switch swAl;
    private Panel.Switch swClear;
    private Panel.Switch swCont;
    public Panel.Switch swDep;
    private Panel.Switch swEal;
    private Panel.Switch swExam;
    private Panel.Switch swHalt;
    protected Panel.SwitchReg swReg;
    private Panel.Switch swSingStep;
    private javax.swing.JLabel text2Type;
    private javax.swing.JLabel text3Company;
    private javax.swing.JLabel text4Ema;
    private javax.swing.JLabel text4MemAddr;
    private javax.swing.JLabel text4Run;
    private javax.swing.JLabel textAddrLoad;
    private javax.swing.JLabel textClear;
    private javax.swing.JLabel textCont;
    private javax.swing.JLabel textDep;
    private javax.swing.JLabel textExam;
    private javax.swing.JLabel textExtdAddrLoad;
    private javax.swing.JLabel textHalt;
    private javax.swing.JLabel textLock;
    private javax.swing.JLabel textOff;
    private javax.swing.JLabel textPower;
    private javax.swing.JLabel textRegAc;
    private javax.swing.JLabel textRegBus;
    private javax.swing.JLabel textRegMd;
    private javax.swing.JLabel textRegMq;
    private javax.swing.JLabel textSingStep;
    private javax.swing.JLabel textStart;
    private javax.swing.JLabel textState;
    private javax.swing.JLabel textStatus;
    private javax.swing.JLabel textSwitch;
    public Panel.XmaLamps xmaLamps;
    // End of variables declaration//GEN-END:variables
    JMenu jboot;
}