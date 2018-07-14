/*
 * LE8.java
 *
 * Created on 1 Mai 2012
 */
package Devices;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 *
 * @author  wvdmark
 */
public class LE8 extends javax.swing.JFrame implements Logic.Constants {

    public Logic.LPT lpt;
    public StringBuilder docStringBuilder;
    private File fileName = new File("");
    private LE8Search search;
    private Document editorPaneDocument;
    protected UndoHandler undoHandler = new UndoHandler();
    protected UndoManager undoManager = new UndoManager();
    private UndoAction undoAction = null;
    private RedoAction redoAction = null;
    private boolean printHeaders = true;

    /** Creates new form TextEditorGUI */
    public LE8(Logic.LPT lpt) {
        this.lpt = lpt;
        setTitle("LPT");
        docStringBuilder = new StringBuilder(1000);
        initComponents();
        search = new LE8Search();
        // undo and redo
        editorPaneDocument = editPane.getDocument();
        editorPaneDocument.addUndoableEditListener(undoHandler);
        editPane.setFont(new Font("Monospaced", Font.BOLD, 12));

        KeyStroke undoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke redoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK);

        undoAction = new UndoAction();
        editPane.getInputMap().put(undoKeystroke, "undoKeystroke");
        editPane.getActionMap().put("undoKeystroke", undoAction);

        redoAction = new RedoAction();
        editPane.getInputMap().put(redoKeystroke, "redoKeystroke");
        editPane.getActionMap().put("redoKeystroke", redoAction);
    }

    // java undo and redo action classes
    class UndoHandler implements UndoableEditListener {

        public void undoableEditHappened(UndoableEditEvent e) {
            undoManager.addEdit(e.getEdit());
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        }
    }

    class UndoAction extends AbstractAction {

        public UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.undo();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
            }
            updateUndoState();
            redoAction.updateRedoState();
        }

        protected void updateUndoState() {
            if (undoManager.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, undoManager.getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }

        }
    }

    class RedoAction extends AbstractAction {

        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.redo();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
            }
            updateRedoState();
            undoAction.updateUndoState();
        }

        protected void updateRedoState() {
            if (undoManager.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, undoManager.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }

    class RandomText {

        String text = null;
        int pos = 0;
        String eol = "\n";
        String ff = "\u000C";
        String tab = "\u0009";
        String rub = "\u007F";
        String empty = "\u0000";

        public RandomText() {
            text = editPane.getText();
            pos = 0;
        }

        public void setPointer(long to) {
            pos = (int) to;
        }

        public long getPointer() {
            return pos;
        }

        public String readLine() {
            String result = null;
            if (text.startsWith(ff, pos)) {
                pos += 1;
                return ff;
            }
            int index = text.indexOf(eol, pos);
            if (index >= 0) {
                result = text.substring(pos, index);
                pos = index + eol.length();
                result = result.replace(empty, "");
                result = result.replace(rub, "");
            }
            if ((result != null) && (result.indexOf(tab) >= 0)) {
                int postab = 0;
                String restab = "";
                for (int i = 0; i < result.length(); ++i) {
                    String strtab = result.substring(i, i + 1);
                    if (strtab.equals(tab)) {
                        do {
                            restab = restab + " ";
                            postab += 1;
                        } while (postab % 8 != 0);
                    } else {
                        restab = restab + strtab;
                        postab += 1;
                    }
                }
                return restab;
            }
            return result;
        }

        public void close() {
            text = null;
        }
    }

    public class FilePrintHelper {

        List<Entry> pageinfo;

        public FilePrintHelper() {
            pageinfo = new ArrayList<Entry>();
        }

        public void createPage(int page) {
            for (int i = pageinfo.size(); i <= page; ++i) {
                pageinfo.add(new Entry());
            }
        }

        public boolean knownPage(int page) {
            return page < pageinfo.size();
        }

        public long getFileOffset(int page) {
            Entry entry = pageinfo.get(page);
            return entry.fileoffset;
        }

        public void setFileOffset(int page, long fileoffset) {
            Entry entry = pageinfo.get(page);
            entry.fileoffset = fileoffset;
        }

        class Entry {

            public long fileoffset;

            public Entry() {
                this.fileoffset = -1;
            }
        }
    }

    public class TextPrinter implements Printable {
        //---Konstanten--------------------------------------

        private static final int RESMUL = 4;
        private static final int FONTSIZE = 9;
        private static final char FF = '\u000C';
        //---Membervariablen---------------------------------
        private PrinterJob pjob;
        private PageFormat pageformat;
        private FilePrintHelper fph;
        private String fname = "LP02-LA180-Output";
        private RandomText in;
        private Paper pa;

        //---Konstruktoren-----------------------------------
        public TextPrinter() {
            this.pjob = PrinterJob.getPrinterJob();
        }

        //---Öffentliche Methoden----------------------------
        public boolean setupPageFormat() {
            PageFormat defaultPF = pjob.defaultPage();
            pjob.setJobName(fname);
            pa = defaultPF.getPaper();
            double mm10 = (10 / 25.4) * 72;
            pa.setImageableArea(mm10, mm10, pa.getWidth() - 2 * mm10, pa.getHeight() - 2 * mm10);
            defaultPF.setPaper(pa);
            this.pageformat = pjob.pageDialog(defaultPF);
            pjob.setPrintable(this, this.pageformat);
            return (this.pageformat != defaultPF);
        }

        public boolean setupJobOptions() {
            return pjob.printDialog();
        }

        public void printFile()
                throws PrinterException, IOException {
            fph = new FilePrintHelper();
            in = new RandomText();
            pjob.print();
            in.close();
        }

        //---Implementierung von Printable-------------------
        public int print(Graphics g, PageFormat pf, int page)
                throws PrinterException {
            int ret = PAGE_EXISTS;
            String line = null;
            if (fph.knownPage(page)) {
                in.setPointer(fph.getFileOffset(page));
                line = in.readLine();
            } else {
                long offset = in.getPointer();
                line = in.readLine();
                if (line == null) {
                    ret = NO_SUCH_PAGE;
                } else {
                    fph.createPage(page);
                    fph.setFileOffset(page, offset);
                }
            }
            if (ret == PAGE_EXISTS) {
                Graphics2D g2 = (Graphics2D) g;
                g2.scale(1.0 / RESMUL, 1.0 / RESMUL);
                g2.setColor(Color.black);
                g2.setFont(new Font("Monospaced", Font.BOLD, FONTSIZE * RESMUL));
                int yd = g2.getFontMetrics().getHeight();
                g2.setFont(new Font("Monospaced", Font.BOLD, FONTSIZE * RESMUL));
                int ypos = (int) pf.getImageableY() * RESMUL;
                int xpos = ((int) pf.getImageableX()) * RESMUL;
                int ymax = ypos + (int) pf.getImageableHeight() * RESMUL - yd;
                ypos += yd;
                if (printHeaders) {
                    g.drawString(fname + ", Page " + (page + 1), xpos, ypos);
                    g.drawLine(
                            xpos,
                            ypos + 6 * RESMUL,
                            xpos + (int) pf.getImageableWidth() * RESMUL,
                            ypos + 6 * RESMUL);
                    ypos += 2 * yd;
                }
                g2.setFont(new Font("Monospaced", Font.BOLD, FONTSIZE * RESMUL));
                while (line != null) {
                    if (line.charAt(0) == FF) {
                        if ((page > 0) || (ypos > yd * 10)) {
                            break;
                        }
                        line = in.readLine();
                        continue;
                    }
                    g.drawString(line, xpos, ypos);
                    ypos += yd;
                    if (ypos >= ymax) {
                        break;
                    }
                    line = in.readLine();
                }
            }
            return ret;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        editPane = new javax.swing.JEditorPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        powerButton = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        lineButton = new javax.swing.JRadioButton();
        onlineButton = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        clearButton = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        loadMenu = new javax.swing.JMenuItem();
        saveMenu = new javax.swing.JMenuItem();
        saveAsMenu = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        selectAllMenu = new javax.swing.JMenuItem();
        copyMenu = new javax.swing.JMenuItem();
        cutMenu = new javax.swing.JMenuItem();
        pasteMenu = new javax.swing.JMenuItem();
        undoMenu = new javax.swing.JMenuItem();
        redoMenu = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        findMenu = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        powerMenuItem = new javax.swing.JCheckBoxMenuItem();
        onlineMenuItem = new javax.swing.JCheckBoxMenuItem();
        lineMenu = new javax.swing.JCheckBoxMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        clearMenu = new javax.swing.JMenuItem();
        exampleTextMenu = new javax.swing.JMenuItem();
        headerMenu1 = new javax.swing.JCheckBoxMenuItem();
        printMenu = new javax.swing.JMenuItem();

        setTitle("LP02/LA180");
        setBackground(new java.awt.Color(153, 153, 153));
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setForeground(new java.awt.Color(102, 102, 102));
        setMinimumSize(new java.awt.Dimension(900, 650));
        setName("LE8"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setHorizontalScrollBar(null);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(302, 202));

        editPane.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        editPane.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        editPane.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        editPane.setMargin(new java.awt.Insets(3, 10, 3, 10));
        editPane.setMinimumSize(new java.awt.Dimension(350, 20));
        editPane.setPreferredSize(new java.awt.Dimension(350, 200));
        jScrollPane1.setViewportView(editPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 350;
        gridBagConstraints.ipady = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setMinimumSize(new java.awt.Dimension(75, 10));
        jPanel1.setPreferredSize(new java.awt.Dimension(75, 10));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 165, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 90;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(525, 0, 0, 0);
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setPreferredSize(new java.awt.Dimension(500, 10));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1005, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 300;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel2, gridBagConstraints);

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));
        jPanel3.setMinimumSize(new java.awt.Dimension(75, 60));
        jPanel3.setPreferredSize(new java.awt.Dimension(90, 60));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        powerButton.setBackground(new java.awt.Color(153, 153, 153));
        powerButton.setFont(new java.awt.Font("Tahoma", 0, 10));
        powerButton.setSelected(true);
        powerButton.setText("Power");
        powerButton.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        powerButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        powerButton.setMaximumSize(new java.awt.Dimension(65, 21));
        powerButton.setMinimumSize(new java.awt.Dimension(65, 21));
        powerButton.setPreferredSize(new java.awt.Dimension(65, 21));
        powerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                powerButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(powerButton, gridBagConstraints);

        jRadioButton2.setBackground(new java.awt.Color(153, 153, 153));
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(jRadioButton2, gridBagConstraints);

        lineButton.setBackground(new java.awt.Color(153, 153, 153));
        lineButton.setFont(new java.awt.Font("Tahoma", 0, 10));
        lineButton.setText("Lines");
        lineButton.setMaximumSize(new java.awt.Dimension(65, 21));
        lineButton.setMinimumSize(new java.awt.Dimension(65, 21));
        lineButton.setPreferredSize(new java.awt.Dimension(65, 21));
        lineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lineButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(lineButton, gridBagConstraints);

        onlineButton.setBackground(new java.awt.Color(153, 153, 153));
        onlineButton.setFont(new java.awt.Font("Tahoma", 0, 10));
        onlineButton.setSelected(true);
        onlineButton.setText("Online");
        onlineButton.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        onlineButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        onlineButton.setMaximumSize(new java.awt.Dimension(65, 21));
        onlineButton.setMinimumSize(new java.awt.Dimension(65, 21));
        onlineButton.setPreferredSize(new java.awt.Dimension(65, 21));
        onlineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onlineButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(onlineButton, gridBagConstraints);

        jRadioButton5.setBackground(new java.awt.Color(153, 153, 153));
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(jRadioButton5, gridBagConstraints);

        clearButton.setBackground(new java.awt.Color(153, 153, 153));
        clearButton.setFont(new java.awt.Font("Tahoma", 0, 10));
        clearButton.setText("Clear");
        clearButton.setMaximumSize(new java.awt.Dimension(65, 21));
        clearButton.setMinimumSize(new java.awt.Dimension(65, 21));
        clearButton.setPreferredSize(new java.awt.Dimension(65, 21));
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel3.add(clearButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel3, gridBagConstraints);

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));
        jPanel4.setPreferredSize(new java.awt.Dimension(30, 10));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(525, 0, 0, 0);
        getContentPane().add(jPanel4, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18));
        jLabel1.setText("DEC LP02/LA180 Printer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(22, 400, 0, 0);
        getContentPane().add(jLabel1, gridBagConstraints);

        jMenu1.setText("File");
        jMenu1.setPreferredSize(new java.awt.Dimension(60, 19));

        loadMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        loadMenu.setText("Open");
        loadMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMenuActionPerformed(evt);
            }
        });
        jMenu1.add(loadMenu);

        saveMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenu.setText("Save");
        saveMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuActionPerformed(evt);
            }
        });
        jMenu1.add(saveMenu);

        saveAsMenu.setText("Save as...");
        saveAsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuActionPerformed(evt);
            }
        });
        jMenu1.add(saveAsMenu);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenu2.setToolTipText("");
        jMenu2.setPreferredSize(new java.awt.Dimension(60, 19));

        selectAllMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        selectAllMenu.setText("Select All");
        selectAllMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllMenuActionPerformed(evt);
            }
        });
        jMenu2.add(selectAllMenu);

        copyMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyMenu.setText("Copy");
        copyMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyMenuActionPerformed(evt);
            }
        });
        jMenu2.add(copyMenu);

        cutMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        cutMenu.setText("Cut");
        cutMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cutMenuActionPerformed(evt);
            }
        });
        jMenu2.add(cutMenu);

        pasteMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        pasteMenu.setText("Paste");
        pasteMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteMenuActionPerformed(evt);
            }
        });
        jMenu2.add(pasteMenu);

        undoMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        undoMenu.setText("Undo");
        undoMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoMenuActionPerformed(evt);
            }
        });
        jMenu2.add(undoMenu);

        redoMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        redoMenu.setText("Redo");
        redoMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoMenuActionPerformed(evt);
            }
        });
        jMenu2.add(redoMenu);
        jMenu2.add(jSeparator1);

        findMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        findMenu.setText("Find");
        findMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findMenuActionPerformed(evt);
            }
        });
        jMenu2.add(findMenu);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Printer");
        jMenu3.setPreferredSize(new java.awt.Dimension(60, 19));

        powerMenuItem.setSelected(true);
        powerMenuItem.setText("Power");
        powerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                powerMenuItemActionPerformed(evt);
            }
        });
        jMenu3.add(powerMenuItem);

        onlineMenuItem.setSelected(true);
        onlineMenuItem.setText("Online");
        onlineMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onlineMenuItemActionPerformed(evt);
            }
        });
        jMenu3.add(onlineMenuItem);

        lineMenu.setText("Lines");
        lineMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lineMenuActionPerformed(evt);
            }
        });
        jMenu3.add(lineMenu);
        jMenu3.add(jSeparator2);

        clearMenu.setText("Clear");
        clearMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearMenuActionPerformed(evt);
            }
        });
        jMenu3.add(clearMenu);

        exampleTextMenu.setText("Example Text");
        exampleTextMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exampleTextMenuActionPerformed(evt);
            }
        });
        jMenu3.add(exampleTextMenu);

        headerMenu1.setSelected(true);
        headerMenu1.setText("Headers");
        headerMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                headerMenu1ActionPerformed(evt);
            }
        });
        jMenu3.add(headerMenu1);

        printMenu.setText("Print²");
        printMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printMenuActionPerformed(evt);
            }
        });
        jMenu3.add(printMenu);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void copyMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyMenuActionPerformed
    editPane.copy();
}//GEN-LAST:event_copyMenuActionPerformed

private void cutMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cutMenuActionPerformed
    editPane.cut();
}//GEN-LAST:event_cutMenuActionPerformed

private void pasteMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteMenuActionPerformed
    editPane.paste();
}//GEN-LAST:event_pasteMenuActionPerformed

private void selectAllMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllMenuActionPerformed
    editPane.selectAll();
}//GEN-LAST:event_selectAllMenuActionPerformed

private void exampleTextMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exampleTextMenuActionPerformed
    editPane.setText(LOREMIPSUM);
    clearButton.setSelected(false);
}//GEN-LAST:event_exampleTextMenuActionPerformed

private void saveMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuActionPerformed
    BufferedWriter writer;
    try {
        writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(editPane.getText());
        writer.close();
    } catch (IOException ioe) {
        editPane.setText("Sorry. Can't write file.");
    }
}//GEN-LAST:event_saveMenuActionPerformed

private void loadMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadMenuActionPerformed
    JFileChooser fileChooser = new JFileChooser();
    String dfile = lpt.data.getProp("LE8-Openfile");
    if (dfile != null) {
        fileChooser.setSelectedFile(new java.io.File(dfile));
    }
    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

        BufferedReader reader;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fileName = fileChooser.getSelectedFile();
            reader = new BufferedReader(new FileReader(fileName));
            while (reader.ready()) {
                stringBuilder.append(reader.readLine()).append(eol);
            }
            reader.close();
            editPane.setText(stringBuilder.toString());
            lpt.data.setProp("LE8-Openfile", fileName.getCanonicalPath());
            clearButton.setSelected(false);
        } catch (IOException ioe) {
            editPane.setText("Sorry. Can't open file.");
        }
    }
}//GEN-LAST:event_loadMenuActionPerformed

private void saveAsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuActionPerformed
    JFileChooser fileChooser = new JFileChooser();
    String dfile = lpt.data.getProp("LE8-Openfile");
    if (dfile != null) {
        fileChooser.setSelectedFile(new java.io.File(dfile));
    }
    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        BufferedWriter writer;
        try {
            fileName = fileChooser.getSelectedFile();
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(editPane.getText());
            writer.close();
            lpt.data.setProp("LE8-Openfile", fileName.getCanonicalPath());
        } catch (IOException ioe) {
            editPane.setText("Sorry. Can't write file.");
        }
    }
}//GEN-LAST:event_saveAsMenuActionPerformed

private void clearMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearMenuActionPerformed
    clearEditor();
}//GEN-LAST:event_clearMenuActionPerformed

private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
    clearEditor();
}//GEN-LAST:event_clearButtonActionPerformed

private void onlineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onlineButtonActionPerformed
        setOnline(onlineButton.isSelected());
        /*
    if (onlineButton.isSelected()) {
        lpt.lptonline = true;
        lpt.errflag = false;
        //lpt.lptflag = false;
        lpt.clearIntReq();
    } else {
        lpt.lptonline = false;
        lpt.errflag = true;
        lpt.lptflag = true;
        lpt.setIntReq();
    }
    onlineMenuItem.setSelected(onlineButton.isSelected());
         */
}//GEN-LAST:event_onlineButtonActionPerformed

private void findMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findMenuActionPerformed
    search.setTextComponent(editPane);
    search.setVisible(true);
    search.setLocation(this.getLocation());
}//GEN-LAST:event_findMenuActionPerformed

private void powerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_powerButtonActionPerformed
    setPower(powerButton.isSelected());
    /*if (!powerButton.isSelected()) {
    onlineMenuItem.setSelected(false);
    onlineButton.setSelected(false);
    }
    powerMenuItem.setSelected(powerButton.isSelected());*/
}//GEN-LAST:event_powerButtonActionPerformed

private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
    jRadioButton2.setSelected(false);
}//GEN-LAST:event_jRadioButton2ActionPerformed

private void lineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lineButtonActionPerformed
    lpt.lptlinewise = lineButton.isSelected();
    lineMenu.setSelected(lineButton.isSelected());
    lpt.lptfast = false;
}//GEN-LAST:event_lineButtonActionPerformed

private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
    jRadioButton5.setSelected(false);
}//GEN-LAST:event_jRadioButton5ActionPerformed

private void undoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoMenuActionPerformed
    undoAction.actionPerformed(evt);
}//GEN-LAST:event_undoMenuActionPerformed

private void redoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoMenuActionPerformed
    redoAction.actionPerformed(evt);
}//GEN-LAST:event_redoMenuActionPerformed

private void printMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printMenuActionPerformed

    TextPrinter sfp = new TextPrinter();
    if (sfp.setupPageFormat()) {
        if (sfp.setupJobOptions()) {
            try {
                sfp.printFile();
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }
}//GEN-LAST:event_printMenuActionPerformed

private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    lpt.data.CloseAllDevs();
}//GEN-LAST:event_formWindowClosing

    private void lineMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lineMenuActionPerformed
        if (lineButton.isSelected()) {
            lineButton.setSelected(false);
            lineMenu.setSelected(false);
        } else {
            lineButton.setSelected(true);
            lineMenu.setSelected(true);
        }
    }//GEN-LAST:event_lineMenuActionPerformed

    private void onlineMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onlineMenuItemActionPerformed
        setOnline(!onlineButton.isSelected());
        /*
        if (onlineButton.isSelected()) {
            onlineButton.setSelected(false);
            onlineMenuItem.setSelected(false);
        } else {
            onlineButton.setSelected(true);
            onlineMenuItem.setSelected(true);
        } */
    }//GEN-LAST:event_onlineMenuItemActionPerformed

    private void headerMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_headerMenu1ActionPerformed
        printHeaders = headerMenu1.isSelected();
    }//GEN-LAST:event_headerMenu1ActionPerformed

    private void powerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_powerMenuItemActionPerformed
        setPower(!powerButton.isSelected());
        /*
        if (powerButton.isSelected()) {
        powerButton.setSelected(false);
        powerMenuItem.setSelected(false);
        onlineButton.setSelected(false);
        onlineMenuItem.setSelected(false);
        } else {
        powerButton.setSelected(true);
        powerMenuItem.setSelected(true);
        } */
    }//GEN-LAST:event_powerMenuItemActionPerformed

    private void setPower(boolean on) {
        if (on) {
            powerButton.setSelected(true);
            powerMenuItem.setSelected(true);
        } else {
            powerButton.setSelected(false);
            powerMenuItem.setSelected(false);
            setOnline(false);
        }
    }

    private void setOnline(boolean on) {
        if (on & powerButton.isSelected()) {
            onlineButton.setSelected(true);
            onlineMenuItem.setSelected(true);
            lpt.lptonline = true;
            lpt.errflag = false;
            //lpt.lptflag = false;
            lpt.clearIntReq();
        } else {
            onlineButton.setSelected(false);
            onlineMenuItem.setSelected(false);
            lpt.lptonline = false;
            lpt.errflag = true;
            lpt.lptflag = true;
            lpt.setIntReq();
        }

    }

    public void clearEditor() {
        editPane.setText("");
        docStringBuilder.delete(0, docStringBuilder.length());
        clearButton.setSelected(true);
        lineButton.setSelected(false);
    }

    public void addEditor(StringBuilder arg) {
        docStringBuilder.append(arg);
    }

    public void setEditor() {
        editPane.setText(docStringBuilder.toString());
        clearButton.setSelected(false);
        editPane.setCaretPosition(this.editPane.getDocument().getLength());
    }

    public Document getDocument() {
        return editPane.getDocument();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton clearButton;
    private javax.swing.JMenuItem clearMenu;
    private javax.swing.JMenuItem copyMenu;
    private javax.swing.JMenuItem cutMenu;
    private javax.swing.JEditorPane editPane;
    private javax.swing.JMenuItem exampleTextMenu;
    private javax.swing.JMenuItem findMenu;
    private javax.swing.JCheckBoxMenuItem headerMenu1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    public javax.swing.JRadioButton lineButton;
    private javax.swing.JCheckBoxMenuItem lineMenu;
    private javax.swing.JMenuItem loadMenu;
    private javax.swing.JRadioButton onlineButton;
    private javax.swing.JCheckBoxMenuItem onlineMenuItem;
    private javax.swing.JMenuItem pasteMenu;
    private javax.swing.JRadioButton powerButton;
    private javax.swing.JCheckBoxMenuItem powerMenuItem;
    private javax.swing.JMenuItem printMenu;
    private javax.swing.JMenuItem redoMenu;
    private javax.swing.JMenuItem saveAsMenu;
    private javax.swing.JMenuItem saveMenu;
    private javax.swing.JMenuItem selectAllMenu;
    private javax.swing.JMenuItem undoMenu;
    // End of variables declaration//GEN-END:variables
}
