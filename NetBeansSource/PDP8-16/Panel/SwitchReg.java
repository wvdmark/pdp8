/*
 * SwitchReg.java
 *
 * Created on July 13, 2004, 1:46 AM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Panel;

import java.util.*;
import java.beans.*;


public class SwitchReg extends javax.swing.JPanel {

    /** Creates new form SwitchReg */
    public SwitchReg() {
        initComponents();
        swreg = 0;
        swrlisteners = new PropertyChangeSupport(this);

        swr0.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                swrPropertyChange(evt);
            }
        });
        swr1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                swrPropertyChange(evt);
            }
        });
        swr2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                swrPropertyChange(evt);
            }
        });
        swr3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                swrPropertyChange(evt);
            }
        });
        swr4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                swrPropertyChange(evt);
            }
        });
        swr5.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                swrPropertyChange(evt);
            }
        });
        swr6.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                swrPropertyChange(evt);
            }
        });
        swr7.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                swrPropertyChange(evt);
            }
        });
        swr8.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                swrPropertyChange(evt);
            }
        });
        swr9.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                swrPropertyChange(evt);
            }
        });
        swr10.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                swrPropertyChange(evt);
            }
        });
        swr11.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                swrPropertyChange(evt);
            }
        });

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel6 = new javax.swing.JPanel();
        swr10 = new Panel.Switch();
        text10 = new javax.swing.JLabel();
        swr9 = new Panel.Switch();
        text9 = new javax.swing.JLabel();
        swr11 = new Panel.Switch();
        text11 = new javax.swing.JLabel();
        text6 = new javax.swing.JLabel();
        text7 = new javax.swing.JLabel();
        text8 = new javax.swing.JLabel();
        swr6 = new Panel.Switch();
        swr7 = new Panel.Switch();
        swr8 = new Panel.Switch();
        text0 = new javax.swing.JLabel();
        text1 = new javax.swing.JLabel();
        text2 = new javax.swing.JLabel();
        swr0 = new Panel.Switch();
        swr1 = new Panel.Switch();
        swr2 = new Panel.Switch();
        text3 = new javax.swing.JLabel();
        text4 = new javax.swing.JLabel();
        text5 = new javax.swing.JLabel();
        swr3 = new Panel.Switch();
        swr4 = new Panel.Switch();
        swr5 = new Panel.Switch();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255)), new javax.swing.border.CompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)), new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255)))));
        jPanel6.setMaximumSize(new java.awt.Dimension(318, 72));
        jPanel6.setMinimumSize(new java.awt.Dimension(159, 36));
        swr10.setInitial(false);
        swr10.setMaximumSize(new java.awt.Dimension(26, 44));
        swr10.setName("1");
        swr10.setToggle(true);
        swr10.setTruepos(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 2;
        jPanel6.add(swr10, gridBagConstraints);

        text10.setBackground(new java.awt.Color(193, 132, 29));
        text10.setFont(new java.awt.Font("Dialog", 0, 8));
        text10.setForeground(new java.awt.Color(255, 255, 255));
        text10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text10.setText("10");
        text10.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255)));
        text10.setMaximumSize(new java.awt.Dimension(26, 12));
        text10.setMinimumSize(new java.awt.Dimension(13, 6));
        text10.setPreferredSize(new java.awt.Dimension(26, 12));
        text10.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 1;
        jPanel6.add(text10, gridBagConstraints);

        swr9.setInitial(false);
        swr9.setMaximumSize(new java.awt.Dimension(26, 44));
        swr9.setName("2");
        swr9.setToggle(true);
        swr9.setTruepos(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 2;
        jPanel6.add(swr9, gridBagConstraints);

        text9.setBackground(new java.awt.Color(193, 132, 29));
        text9.setFont(new java.awt.Font("Dialog", 0, 8));
        text9.setForeground(new java.awt.Color(255, 255, 255));
        text9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text9.setText("9");
        text9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255)));
        text9.setMaximumSize(new java.awt.Dimension(26, 12));
        text9.setMinimumSize(new java.awt.Dimension(13, 6));
        text9.setPreferredSize(new java.awt.Dimension(26, 12));
        text9.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 1;
        jPanel6.add(text9, gridBagConstraints);

        swr11.setInitial(false);
        swr11.setMaximumSize(new java.awt.Dimension(26, 44));
        swr11.setName("0");
        swr11.setToggle(true);
        swr11.setTruepos(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 2;
        jPanel6.add(swr11, gridBagConstraints);

        text11.setBackground(new java.awt.Color(193, 132, 29));
        text11.setFont(new java.awt.Font("Dialog", 0, 8));
        text11.setForeground(new java.awt.Color(255, 255, 255));
        text11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text11.setText("11");
        text11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255)));
        text11.setMaximumSize(new java.awt.Dimension(26, 12));
        text11.setMinimumSize(new java.awt.Dimension(13, 6));
        text11.setPreferredSize(new java.awt.Dimension(26, 12));
        text11.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 1;
        jPanel6.add(text11, gridBagConstraints);

        text6.setBackground(new java.awt.Color(193, 77, 28));
        text6.setFont(new java.awt.Font("Dialog", 0, 8));
        text6.setForeground(new java.awt.Color(255, 255, 255));
        text6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text6.setText("6");
        text6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255)));
        text6.setMaximumSize(new java.awt.Dimension(26, 12));
        text6.setMinimumSize(new java.awt.Dimension(13, 6));
        text6.setPreferredSize(new java.awt.Dimension(26, 12));
        text6.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        jPanel6.add(text6, gridBagConstraints);

        text7.setBackground(new java.awt.Color(193, 77, 28));
        text7.setFont(new java.awt.Font("Dialog", 0, 8));
        text7.setForeground(new java.awt.Color(255, 255, 255));
        text7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text7.setText("7");
        text7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255)));
        text7.setMaximumSize(new java.awt.Dimension(26, 12));
        text7.setMinimumSize(new java.awt.Dimension(13, 6));
        text7.setPreferredSize(new java.awt.Dimension(26, 12));
        text7.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        jPanel6.add(text7, gridBagConstraints);

        text8.setBackground(new java.awt.Color(193, 77, 28));
        text8.setFont(new java.awt.Font("Dialog", 0, 8));
        text8.setForeground(new java.awt.Color(255, 255, 255));
        text8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text8.setText("8");
        text8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255)));
        text8.setMaximumSize(new java.awt.Dimension(26, 12));
        text8.setMinimumSize(new java.awt.Dimension(13, 6));
        text8.setPreferredSize(new java.awt.Dimension(26, 12));
        text8.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        jPanel6.add(text8, gridBagConstraints);

        swr6.setBackground(new java.awt.Color(193, 77, 28));
        swr6.setToolTipText("");
        swr6.setInitial(false);
        swr6.setMaximumSize(new java.awt.Dimension(26, 44));
        swr6.setName("5");
        swr6.setToggle(true);
        swr6.setTruepos(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        jPanel6.add(swr6, gridBagConstraints);

        swr7.setBackground(new java.awt.Color(193, 77, 28));
        swr7.setInitial(false);
        swr7.setMaximumSize(new java.awt.Dimension(26, 44));
        swr7.setName("4");
        swr7.setToggle(true);
        swr7.setTruepos(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        jPanel6.add(swr7, gridBagConstraints);

        swr8.setBackground(new java.awt.Color(193, 77, 28));
        swr8.setInitial(false);
        swr8.setMaximumSize(new java.awt.Dimension(26, 44));
        swr8.setName("3");
        swr8.setToggle(true);
        swr8.setTruepos(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        jPanel6.add(swr8, gridBagConstraints);

        text0.setBackground(new java.awt.Color(193, 77, 28));
        text0.setFont(new java.awt.Font("Dialog", 0, 8));
        text0.setForeground(new java.awt.Color(255, 255, 255));
        text0.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text0.setText("0");
        text0.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255)));
        text0.setMaximumSize(new java.awt.Dimension(26, 12));
        text0.setMinimumSize(new java.awt.Dimension(13, 6));
        text0.setPreferredSize(new java.awt.Dimension(26, 12));
        text0.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel6.add(text0, gridBagConstraints);

        text1.setBackground(new java.awt.Color(193, 77, 28));
        text1.setFont(new java.awt.Font("Dialog", 0, 8));
        text1.setForeground(new java.awt.Color(255, 255, 255));
        text1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text1.setText("1");
        text1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255)));
        text1.setMaximumSize(new java.awt.Dimension(26, 12));
        text1.setMinimumSize(new java.awt.Dimension(13, 6));
        text1.setPreferredSize(new java.awt.Dimension(26, 12));
        text1.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel6.add(text1, gridBagConstraints);

        text2.setBackground(new java.awt.Color(193, 77, 28));
        text2.setFont(new java.awt.Font("Dialog", 0, 8));
        text2.setForeground(new java.awt.Color(255, 255, 255));
        text2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text2.setText("2");
        text2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255)));
        text2.setMaximumSize(new java.awt.Dimension(26, 12));
        text2.setMinimumSize(new java.awt.Dimension(13, 6));
        text2.setPreferredSize(new java.awt.Dimension(26, 12));
        text2.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel6.add(text2, gridBagConstraints);

        swr0.setBackground(new java.awt.Color(193, 77, 28));
        swr0.setInitial(false);
        swr0.setMaximumSize(new java.awt.Dimension(26, 44));
        swr0.setName("11");
        swr0.setToggle(true);
        swr0.setTruepos(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel6.add(swr0, gridBagConstraints);

        swr1.setBackground(new java.awt.Color(193, 77, 28));
        swr1.setInitial(false);
        swr1.setMaximumSize(new java.awt.Dimension(26, 44));
        swr1.setName("10");
        swr1.setToggle(true);
        swr1.setTruepos(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanel6.add(swr1, gridBagConstraints);

        swr2.setBackground(new java.awt.Color(193, 77, 28));
        swr2.setInitial(false);
        swr2.setMaximumSize(new java.awt.Dimension(26, 44));
        swr2.setName("9");
        swr2.setToggle(true);
        swr2.setTruepos(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPanel6.add(swr2, gridBagConstraints);

        text3.setBackground(new java.awt.Color(193, 132, 29));
        text3.setFont(new java.awt.Font("Dialog", 0, 8));
        text3.setForeground(new java.awt.Color(255, 255, 255));
        text3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text3.setText("3");
        text3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255)));
        text3.setMaximumSize(new java.awt.Dimension(26, 12));
        text3.setMinimumSize(new java.awt.Dimension(13, 6));
        text3.setPreferredSize(new java.awt.Dimension(26, 12));
        text3.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        jPanel6.add(text3, gridBagConstraints);

        text4.setBackground(new java.awt.Color(193, 132, 29));
        text4.setFont(new java.awt.Font("Dialog", 0, 8));
        text4.setForeground(new java.awt.Color(255, 255, 255));
        text4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text4.setText("4");
        text4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255)));
        text4.setMaximumSize(new java.awt.Dimension(26, 12));
        text4.setMinimumSize(new java.awt.Dimension(13, 6));
        text4.setPreferredSize(new java.awt.Dimension(26, 12));
        text4.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        jPanel6.add(text4, gridBagConstraints);

        text5.setBackground(new java.awt.Color(193, 132, 29));
        text5.setFont(new java.awt.Font("Dialog", 0, 8));
        text5.setForeground(new java.awt.Color(255, 255, 255));
        text5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text5.setText("5");
        text5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255)));
        text5.setMaximumSize(new java.awt.Dimension(26, 12));
        text5.setMinimumSize(new java.awt.Dimension(13, 6));
        text5.setPreferredSize(new java.awt.Dimension(26, 12));
        text5.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        jPanel6.add(text5, gridBagConstraints);

        swr3.setInitial(false);
        swr3.setMaximumSize(new java.awt.Dimension(26, 44));
        swr3.setName("8");
        swr3.setToggle(true);
        swr3.setTruepos(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        jPanel6.add(swr3, gridBagConstraints);

        swr4.setInitial(false);
        swr4.setMaximumSize(new java.awt.Dimension(26, 44));
        swr4.setName("7");
        swr4.setToggle(true);
        swr4.setTruepos(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        jPanel6.add(swr4, gridBagConstraints);

        swr5.setInitial(false);
        swr5.setMaximumSize(new java.awt.Dimension(26, 44));
        swr5.setName("6");
        swr5.setToggle(true);
        swr5.setTruepos(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        jPanel6.add(swr5, gridBagConstraints);

        jLabel1.setBackground(new java.awt.Color(193, 78, 28));
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 8));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SWITCH REGISTER");
        jLabel1.setMaximumSize(new java.awt.Dimension(312, 10));
        jLabel1.setMinimumSize(new java.awt.Dimension(156, 5));
        jLabel1.setPreferredSize(new java.awt.Dimension(312, 10));
        jLabel1.setOpaque(true);
        jLabel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jLabel1ComponentResized(evt);
            }
        });
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 12;
        jPanel6.add(jLabel1, gridBagConstraints);

        add(jPanel6, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        int bitcount = 0;
        int i = 0;
        for (i=0; i<12; i++) {
            if (((1 << i) & swreg) != 0) bitcount++;
        }
        if (((bitcount>6) | (bitcount==0)) & (bitcount!=12)) {
            swr0.setPos(true);swr1.setPos(true);swr2.setPos(true);swr3.setPos(true);swr4.setPos(true);swr5.setPos(true);
            swr6.setPos(true);swr7.setPos(true);swr8.setPos(true);swr9.setPos(true);swr10.setPos(true);swr11.setPos(true);
        } else {
            swr0.setPos(false);swr1.setPos(false);swr2.setPos(false);swr3.setPos(false);swr4.setPos(false);swr5.setPos(false);
            swr6.setPos(false);swr7.setPos(false);swr8.setPos(false);swr9.setPos(false);swr10.setPos(false);swr11.setPos(false);
        }
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jLabel1ComponentResized
        // TODO add your handling code here:
        if (jLabel1.getWidth()<300) {
            jLabel1.setFont(new java.awt.Font("Dialog", 0, 4));
            text0.setFont(new java.awt.Font("Dialog", 0, 4));
            text1.setFont(new java.awt.Font("Dialog", 0, 4));
            text2.setFont(new java.awt.Font("Dialog", 0, 4));
            text3.setFont(new java.awt.Font("Dialog", 0, 4));
            text4.setFont(new java.awt.Font("Dialog", 0, 4));
            text5.setFont(new java.awt.Font("Dialog", 0, 4));
            text6.setFont(new java.awt.Font("Dialog", 0, 4));
            text7.setFont(new java.awt.Font("Dialog", 0, 4));
            text8.setFont(new java.awt.Font("Dialog", 0, 4));
            text9.setFont(new java.awt.Font("Dialog", 0, 4));
            text10.setFont(new java.awt.Font("Dialog", 0, 4));
            text11.setFont(new java.awt.Font("Dialog", 0, 4));
        } else {
            jLabel1.setFont(new java.awt.Font("Dialog", 0, 8));
            text0.setFont(new java.awt.Font("Dialog", 0, 8));
            text1.setFont(new java.awt.Font("Dialog", 0, 8));
            text2.setFont(new java.awt.Font("Dialog", 0, 8));
            text3.setFont(new java.awt.Font("Dialog", 0, 8));
            text4.setFont(new java.awt.Font("Dialog", 0, 8));
            text5.setFont(new java.awt.Font("Dialog", 0, 8));
            text6.setFont(new java.awt.Font("Dialog", 0, 8));
            text7.setFont(new java.awt.Font("Dialog", 0, 8));
            text8.setFont(new java.awt.Font("Dialog", 0, 8));
            text9.setFont(new java.awt.Font("Dialog", 0, 8));
            text10.setFont(new java.awt.Font("Dialog", 0, 8));
            text11.setFont(new java.awt.Font("Dialog", 0, 8));
        }
    }//GEN-LAST:event_jLabel1ComponentResized
    @Override
    public void addPropertyChangeListener(PropertyChangeListener pl) {
        swrlisteners.addPropertyChangeListener(pl);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener pl) {
        swrlisteners.removePropertyChangeListener(pl);
    }

    private void swrPropertyChange(java.beans.PropertyChangeEvent evt) {
        Boolean objswr = (Boolean) evt.getNewValue();
        boolean valswr = objswr.booleanValue();
        int mask = Integer.parseInt(evt.getPropertyName());
        int oldreg = swreg;
        if (valswr) {
            swreg |= 1 << mask;
        } else {
            swreg &= ~ (1 << mask);
        }
        //System.out.println( swreg );        // TODO add your handling code here:
        swrlisteners.firePropertyChange(this.getName(),oldreg,swreg);

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel6;
    private Panel.Switch swr0;
    private Panel.Switch swr1;
    private Panel.Switch swr10;
    private Panel.Switch swr11;
    private Panel.Switch swr2;
    private Panel.Switch swr3;
    private Panel.Switch swr4;
    private Panel.Switch swr5;
    private Panel.Switch swr6;
    private Panel.Switch swr7;
    private Panel.Switch swr8;
    private Panel.Switch swr9;
    private javax.swing.JLabel text0;
    private javax.swing.JLabel text1;
    private javax.swing.JLabel text10;
    private javax.swing.JLabel text11;
    private javax.swing.JLabel text2;
    private javax.swing.JLabel text3;
    private javax.swing.JLabel text4;
    private javax.swing.JLabel text5;
    private javax.swing.JLabel text6;
    private javax.swing.JLabel text7;
    private javax.swing.JLabel text8;
    private javax.swing.JLabel text9;
    // End of variables declaration//GEN-END:variables
    private int swreg;
    transient protected PropertyChangeSupport swrlisteners;

}
