/*
 * ConvertDisk.java
 *
 * Created on 03 May 2010, 23:43
 */

/**
 *
 * @author  wvdmark
 */
/*
 * Swing version
 */
import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.RandomAccessFile;

public class ConvertDisk extends JFrame implements ActionListener {

    private JTextArea displayArea;
    private javax.swing.JFileChooser chooser;
    private JMenuBar mb;
    private RandomAccessFile file1 = null;
    private RandomAccessFile file2 = null;
    private int filesize1 = 0;
    private int filesize2 = 0;
    private int maxblock;
    private int unit;
    private boolean dummy = false;
    private boolean dumptape = false;
    private boolean toRK05 = false;
    private long csi;
    private long[] buffer = new long[128];
    private Convert convert;
    private Thread tconvert;
    private static final String newline = "\n";
    private JMenuItem mount1o;
    private JMenuItem mount2o;

    public ConvertDisk() {
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(375, 125));

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);
        setContentPane(contentPane);
        this.setSize(600, 330);
        this.setVisible(true);
        this.setTitle("ConvertDisk");

        mb = new JMenuBar();
        JMenu unit0 = new JMenu("Input");
        JMenuItem mount0 = new JMenuItem("Open 256w/blk RK05 disk file");
        mount0.addActionListener(this);
        mount0.setName("0");
        unit0.add(mount0);
        JMenuItem mount1 = new JMenuItem("Open dumpM43 disk file, unit A");
        mount1.addActionListener(this);
        mount1.setName("1");
        unit0.add(mount1);
        JMenuItem mount2 = new JMenuItem("Open dumpM43 disk file, unit B");
        mount2.addActionListener(this);
        mount2.setName("2");
        unit0.add(mount2);
        JMenuItem mount3 = new JMenuItem("Open M43 disk file for convert to RK05");
        mount3.addActionListener(this);
        mount3.setName("3");
        unit0.add(mount3);
        JMenuItem mount4 = new JMenuItem("Create blank M43 disk => Format");
        mount4.addActionListener(this);
        mount4.setName("4");
        mount4.setEnabled(true);
        unit0.add(mount4);
        mb.add(unit0);
        JMenu unit1 = new JMenu("Output");
        mount1o = new JMenuItem("Open new M43 format disk file");
        mount1o.addActionListener(this);
        mount1o.setName("5");
        unit1.add(mount1o);
        mount1o.setEnabled(false);
        mount2o = new JMenuItem("Open new RK05 format disk file");
        mount2o.addActionListener(this);
        mount2o.setName("6");
        unit1.add(mount2o);
        mount2o.setEnabled(false);
        JMenuItem mconvert = new JMenuItem("Convert!");
        mconvert.addActionListener(this);
        mconvert.setName("7");
        unit1.add(mconvert);
        mb.add(unit1);
        this.setJMenuBar(mb);

        chooser = new javax.swing.JFileChooser();

        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        convert = new Convert();
    }

    /** Handle the button click. */
    public void actionPerformed(ActionEvent e) {
        AbstractButton x = (AbstractButton) (e.getSource());
        int val = Integer.parseInt(x.getName());
        switch (val & 07) {
            case 0:
                dumptape = false;
                openFile1();
                break;
            case 1:
                dumptape = true;
                unit = 0;
                openFile1();
                break;
            case 2:
                dumptape = true;
                unit = 1;
                openFile1();
                break;
            case 3:
                openFile1b();
                break;
            case 4:
                dumptape = false;
                openFileDummy();
                break;
            case 5:
                openFile2();
        toRK05 = false;
                break;
            case 6:
                toRK05 = true;
                openFile2b();
                break;
            case 7:
                tconvert = new Thread(convert, "Convert");
                tconvert.start();
                break;
        }
    }

    private void openFileDummy() {
        dummy = true;
        maxblock = 06300;
        mount1o.setEnabled(true);
        displayInfo("INFILE", "Prepared for format 2*6300 blocks");
    }

    private void openFile1() {
        dummy = false;
        if (dumptape == false) {
            chooser.addChoosableFileFilter(new RK05FileFilter());
        } else {
            chooser.addChoosableFileFilter(new DumpFileFilter());
        }
        int option = chooser.showOpenDialog(this);
        if (option == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();
            if (file != null) {
                try {
                    if (file.isFile()) {
                        if (file.canRead()) {
                            file1 = new java.io.RandomAccessFile(file, "r");
                        } else {
                            displayInfo("INFILE", "File cannot be read!");
                        }
                    } else {
                        displayInfo("INFILE", "No file selected!");
                    }
                } catch (java.io.IOException e) {
                    System.out.println(e);
                }
                if (file1 != null) {
                    if (dumptape) {
                        if (file.length() < 1250483) {
                            displayInfo("INFILE", "File is not large enough! (not dumpM43 file?)");
                        } else {
                            maxblock = (int) ((file.length() - 3) / ((256 / 2 * 3) + 1));
                            displayInfo("INFILE", "Dump Input file '" + file.getName() + "' successfully allocated, blocks: " + maxblock);
                            mount1o.setEnabled(true);
                        }
                    } else {
                        if (file.length() < 2494464) {
                            displayInfo("INFILE", "File is not large enough! (not RK05 file?)");
                        } else {
                            maxblock = (int) (file.length() / (256 / 2 * 3) / 2);
                            displayInfo("INFILE", "RK05 Input file '" + file.getName() + "' successfully allocated, blocks: " + maxblock);
                            mount1o.setEnabled(true);
                        }
                    }
                }
            } else {
                System.out.println("No file selected");
            }
        }
    }

    private void openFile1b() {
        dummy = false;
        chooser.addChoosableFileFilter(new M43FileFilter());
        int option = chooser.showOpenDialog(this);
        if (option == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();
            if (file != null) {
                try {
                    if (file.isFile()) {
                        if (file.canRead()) {
                            file1 = new java.io.RandomAccessFile(file, "r");
                        } else {
                            displayInfo("INFILE", "File cannot be read!");
                        }
                    } else {
                        displayInfo("INFILE", "No file selected!");
                    }
                } catch (java.io.IOException e) {
                    System.out.println(e);
                }
                if (file1 != null) {
                    if (file.length() < 2722176) {
                        displayInfo("INFILE", "File is not large enough! (not M43 file?)");
                    } else {
                        maxblock = (int) (file.length() / ((256 / 2 * 3) + 33) / 2);
                        displayInfo("INFILE", "M43 Input file '" + file.getName() + "' successfully allocated, blocks: " + maxblock);
                        mount2o.setEnabled(true);
                    }
                }
            } else {
                System.out.println("No file selected");
            }
        }
    }

    public class Convert implements Runnable {

        public void run() {
            if ((file1 == null & !dummy) | file2 == null) {
                displayInfo("CONVERT", "No in- or outfile allocated!");
            } else {
                displayInfo("CONVERT", "Starting conversion");
                csi = 0;
                if (dumptape) {
                    for (long block = 0; block < maxblock; block++) {
                        readdump(block);
                        writeblock(block);
                        if (block % 160 == 0) {
                            displayInfo("CONVERT", "Blocks up to " + block + " converted");
                        }
                    }
                } else if (dummy) {
                    for (unit = 0; unit < 2; unit++) {
                        for (long block = 0; block < maxblock; block++) {
                            writeblock(block);
                            if (block % 160 == 0) {
                                displayInfo("CONVERT", "Blocks up to " + block + " unit " + unit + " written");
                            }
                        }
                    }
                } else {
                    convertRK05(toRK05);
                }
                displayInfo("CONVERT", "Conversion done!");
                closeFile(toRK05);
                displayInfo("CONVERT", "Files closed");
            }
        }
    }

    /**
    0    3    6    9    12   15   18   21   24   27   30   33 /byte position
    0    2    4    6    8    10   12   14   16   18   20   22 /12-bit word
    0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 tttt data
    0000 0000 0000 0000 0000 0000 0000 0000 0000 4000 0000
    1    3    5    7    9    11   13   15   17   19   21      /12-bit word
    32-bit word from byte 28: x08 00 tt t0 - 1 byte undetermined - data
    ttt bit pattern: wdt ttt ttt ttt w:write lock d:drive t:track
     */
    private void writeblock(long block) {
        try {
            file2.seek(28 + ((unit * 06300 + block) * (384 + 33)));
            file2.writeInt((8 << 24) + (((int) (unit * 06300 + block) / 16) << 4));
            file2.seek(33 + ((unit * 06300 + block) * (384 + 33)));
            for (int b = 0; b < 128; b++) {
                file2.write((byte) (buffer[b] >> 16) & 0377);
                file2.write((byte) (buffer[b] >> 8) & 0377);
                file2.write((byte) (buffer[b]) & 0377);
            }
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
    }

    private void convertRK05(boolean toRK05) {
        int word;
        try {
            if (toRK05) {
                for (int u = 0; u < 2; u++) {
                    for (int block = 0; block < 0313; block++) {
                        for (int sect = 0; sect < 16; sect++) {
                            file1.seek(33 + (((u * 0314 + block) << 4) | sect) * (384 + 33));
                            file2.seek((((u * 0313 + block) << 4) | sect) * (384));
                            for (int b = 0; b < 384; b++) {
                                file2.write(file1.read());
                            }
                        }
                        if (block % 16 == 0) {
                            displayInfo("CONVERT", "Blocks up to " + block * 16 + " converted");
                        }
                    }
                }
                displayInfo("CONVERT", "Bytes up to " + file2.getFilePointer() + " converted");

            } else {
                for (int u = 0; u < 2; u++) {
                    for (int block = 0; block < 0313; block++) {
                        for (int sect = 0; sect < 16; sect++) {
                            file1.seek((((u * 0313 + block) << 4) | sect) * (384));
                            file2.seek(28 + (((u * 0314 + block) << 4) | sect) * (384 + 33));
                            file2.writeInt((8 << 24) + ((u * 0314 + block) << 4));
                            file2.seek(33 + (((u * 0314 + block) << 4) | sect) * (384 + 33));
                            for (int b = 0; b < 384; b++) {
                                file2.write(file1.read());
                            }
                        }
                        if (block % 16 == 0) {
                            displayInfo("CONVERT", "Blocks up to " + block * 16 + " converted");
                        }
                    }
                }
                displayInfo("CONVERT", "Bytes up to " + file2.getFilePointer() + " converted");
            }
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
    }

    private void readdump(long block) {
        long word3;
        int blockmark;
        try {
            file1.seek(((256 * 3 / 2) + 1) * block);
            blockmark = file1.read();
            for (int i = 0; i < 128; i++) {
                word3 = (long) file1.read() << 16;
                word3 |= (long) file1.read() << 8;
                word3 |= (long) file1.read() << 0;
                buffer[i] = word3;
            }
            if (blockmark != 0377) {
                displayInfo("CONVERT", "Bad block " + block + " in input");
            }
            for (int i = 0; i < 256; i++) {
                int mod = i % 2;
                word3 = (buffer[i / 2] >> mod * 12) & 0xfffL;
                csi += word3;
                csi &= 07777;
            }
            if (block == maxblock - 1) {
                blockmark = file1.read();
                csi += (long) file1.read();
                csi += (long) file1.read() << 8;
                if ((csi & 07777) == 0) {
                    displayInfo("CONVERT", "Correct checksum in input");
                } else {
                    displayInfo("CONVERT", "Bad checksum in input");
                }
            }
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
    }

    private void openFile2() {
        chooser.resetChoosableFileFilters();
        chooser.addChoosableFileFilter(new M43FileFilter());
        int option = chooser.showSaveDialog(this);
        if (option == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();
            if (file != null) {
                try {
                    String name = file.getName();
                    String test;
                    if (name.lastIndexOf(".") > 0) {
                        test = name.substring(name.lastIndexOf(".") + 1).toUpperCase();
                    } else {
                        test = "";
                    }
                    if (test.compareTo("M43") == 0) {
                        if (file.isFile()) {
                            if (file.canWrite()) {
                                file2 = new java.io.RandomAccessFile(file, "rw");
                            } else {
                                displayInfo("OUTFILE", "Output file not writeable!");
                            }
                        } else {
                            file2 = new java.io.RandomAccessFile(file, "rw");
                            displayInfo("OUTFILE", "Output file '" + file.getName() + "' successfully allocated");
                        }
                    } else {
                        displayInfo("OUTFILE", "Please use *.m43 extension!");
                    }
                } catch (java.io.IOException e) {
                    System.out.println(e);
                }
                if (file2 != null) {
                    if (file.length() != 0) {
                        displayInfo("OUTFILE", "Updating with second unit...");
                    }
                }
            } else {
                System.out.println("No file selected");
            }
        }
    }

    private void openFile2b() {
        chooser.resetChoosableFileFilters();
        chooser.addChoosableFileFilter(new RK05FileFilter());
        int option = chooser.showSaveDialog(this);
        if (option == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();
            if (file != null) {
                try {
                    String name = file.getName();
                    String test;
                    if (name.lastIndexOf(".") > 0) {
                        test = name.substring(name.lastIndexOf(".") + 1).toUpperCase();
                    } else {
                        test = "";
                    }
                    if (test.compareTo("RK05") == 0) {
                        if (file.isFile()) {
                            if (file.canWrite()) {
                                file2 = new java.io.RandomAccessFile(file, "rw");
                            } else {
                                displayInfo("OUTFILE", "Output file not writeable!");
                            }
                        } else {
                            file2 = new java.io.RandomAccessFile(file, "rw");
                            displayInfo("OUTFILE", "Output file '" + file.getName() + "' successfully allocated");
                        }
                    } else {
                        displayInfo("OUTFILE", "Please use *.rk05 extension!");
                    }
                } catch (java.io.IOException e) {
                    System.out.println(e);
                }
            } else {
                System.out.println("No file selected");
            }
        }
    }

    public void closeFile(boolean toRK05) {
        if (file1 != null) {
            try {
                file1.close();
            } catch (java.io.IOException e) {
            }
            file1 = null;
            filesize1 = 0;
        }
        if (file2 != null) {
            try {
                if (toRK05) {
                    file2.seek((2 * 06260 * 384) - 1);
                } else {
                    file2.seek((2 * 06300) * (384 + 33) - 1);
                }
                file2.writeByte(0);
                file2.close();
            } catch (java.io.IOException e) {
            }
            mount1o.setEnabled(false);
            mount2o.setEnabled(false);
            file2 = null;
            filesize2 = 0;
        }
    }

    protected void displayInfo(String e, String s) {
        displayArea.append(e + ":    " + s + newline);
    }

    private static class M43FileFilter extends javax.swing.filechooser.FileFilter {

        public boolean accept(java.io.File file) {
            if (file == null) {
                return false;
            }
            return file.isDirectory() || file.getName().toLowerCase().endsWith(".m43");
        }

        public String getDescription() {
            return "M43 disk files (*.m43)";
        }
    }

    private static class RK05FileFilter extends javax.swing.filechooser.FileFilter {

        public boolean accept(java.io.File file) {
            if (file == null) {
                return false;
            }
            return file.isDirectory() || file.getName().toLowerCase().endsWith(".rk05");
        }

        public String getDescription() {
            return "RK05 files (*.rk05)";
        }
    }

    private static class DumpFileFilter extends javax.swing.filechooser.FileFilter {

        public boolean accept(java.io.File file) {
            if (file == null) {
                return false;
            }
            return file.isDirectory() || file.getName().toLowerCase().endsWith(".bin");
        }

        public String getDescription() {
            return "DUMP files (*.bin)";
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        JFrame myframe = new ConvertDisk();
        myframe.setVisible(true);
    }

    private void exitForm(java.awt.event.WindowEvent evt) {
        System.exit(0);
    }
}
