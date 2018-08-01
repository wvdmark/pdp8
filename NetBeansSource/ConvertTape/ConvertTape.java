/*
 * ConvertTape.java
 *
 * Created on 20 July 2010
 */

/**
 *
 * @author  wvdmark
 */
import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.RandomAccessFile;

public class ConvertTape extends JFrame implements ActionListener {

    private JTextArea displayArea;
    private javax.swing.JFileChooser chooser;
    private JMenuBar mb;
    private RandomAccessFile file1 = null;
    private RandomAccessFile file2 = null;
    private int line = 0;
    private int maxblock;
    private int blocksize;
    private boolean type8 = true;
    private boolean dummy = false;
    private boolean dumptape = false;
    private boolean toDJG = false;
    private int wper12line = 0;
    private int bperword = 0;
    private int offset = 0;
    private long cs;
    private long csi;
    private long[] buffer = new long[128];
    private Convert convert;
    private Thread tconvert;
    private JMenuItem mount1o;
    private JMenuItem mount2o;
    private static final String newline = "\n";
    private static final int ONE_BLOCK_LINES = (129 * 2 / 3 + 10) * 6;                //576
    private static final int ONE_BLOCK_LINES_10 = (128 * 2 + 10) * 6;                 //1596
    private static final int BLOCK_LINES = 02702 * ONE_BLOCK_LINES;                   //1474*..=849024
    private static final int BLOCK_LINES_10 = 01102 * ONE_BLOCK_LINES_10;             //578*.. =922488
    private static final int EXPAND_LINES = 99 * 2 * 6;                               //1188
    private static final int ENDZONE_LINES = 4096 * 2 * 6;                            //49152
    private static final int TOTAL_LINES = 2 * ENDZONE_LINES + 2 * EXPAND_LINES + BLOCK_LINES;//949704
    private static final int TOTAL_LINES_10 = 2 * ENDZONE_LINES + 2 * EXPAND_LINES + BLOCK_LINES_10;//1023180
    private static final int TAPE_LINES = 0x100000;                                   //1048576
    private static final int FORMAT_FIRST_LINE = (TAPE_LINES - TOTAL_LINES) / 2;      //49436
    private static final int FORMAT_FIRST_LINE_10 = (TAPE_LINES - TOTAL_LINES_10) / 2;//12704
    private static final int FIRST_LINE = FORMAT_FIRST_LINE + ENDZONE_LINES / 2;      //74012
    private static final int FIRST_LINE_10 = FORMAT_FIRST_LINE_10 + ENDZONE_LINES / 2;//37280
    private static final int REV_END_ZONE = 05555;      //80 88 08 80 88 08
    private static final int EXPAND = 02525;            //08 08 08 08 08 08
    private static final int BLOCK_REV_GUARD = 02632;   //08 08 80 08 80 80
    private static final int LOCK_REV_CHECK = 01010;    //00 80 00 00 80 00 last byte checksum
    private static final int REV_FIN_REV_PREFIN = 01010;//00 80 00 00 80 00
    private static final int DATA_MARK = 07070;         //88 80 00 88 80 00
    private static final int PREFIN_FIN = 07373;        //88 80 88 88 80 88
    private static final int CHECK_REV_LOCK = 07373;    //88 80 88 88 80 88
    private static final int GUARD_REV_BLOCK = 05145;   //80 80 08 80 08 08
    //private static final int EXPAND = 02525;          //08 08 08 08 08 08
    private static final int END_ZONE = 02222;          //08 00 80 08 00 80

    public ConvertTape() {

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
        this.setTitle("ConvertTape");

        mb = new JMenuBar();
        JMenu unit0 = new JMenu("Input");
        JMenuItem mount0 = new JMenuItem("Open PDP-8 or PDP-10 DJG dectape file");
        mount0.addActionListener(this);
        mount0.setName("0");
        unit0.add(mount0);
        JMenuItem mount1 = new JMenuItem("Open dumptd8e dectape file");
        mount1.addActionListener(this);
        mount1.setName("1");
        unit0.add(mount1);
        JMenuItem mount2 = new JMenuItem("Create blank PDP-8 PXG file => Format");
        mount2.addActionListener(this);
        mount2.setName("2");
        unit0.add(mount2);
        JMenuItem mount3 = new JMenuItem("Create blank PDP-10 PXG file => Format");
        mount3.addActionListener(this);
        mount3.setName("3");
        unit0.add(mount3);
        JMenuItem mount4 = new JMenuItem("Open PDP-8 or PDP-10 PXG tape file for convert to DJG");
        mount4.addActionListener(this);
        mount4.setName("4");
        unit0.add(mount4);
        mb.add(unit0);
        JMenu unit1 = new JMenu("Output");
        mount1o = new JMenuItem("Open new PXG format dectape file");
        mount1o.addActionListener(this);
        mount1o.setName("5");
        unit1.add(mount1o);
        mount1o.setEnabled(false);
        mount2o = new JMenuItem("Open new DJG format dectape file");
        mount2o.addActionListener(this);
        mount2o.setName("6");
        unit1.add(mount2o);
        mount2o.setEnabled(false);
        JMenuItem convert1 = new JMenuItem("Convert!");
        convert1.addActionListener(this);
        convert1.setName("7");
        unit1.add(convert1);
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
        //tconvert = new Thread(convert,"Convert");
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
                openFile1();
                break;
            case 2:
                dumptape = false;
                openFileDummy(true);
                break;
            case 3:
                dumptape = false;
                openFileDummy(false);
                break;
            case 4:
                openFile1b();
                break;
            case 5:
                openFile2();
                break;
            case 6:
                openFile2b();
                break;
            case 7:
                tconvert = new Thread(convert, "Convert");
                tconvert.start();
                break;
        }
    }

    private void openFileDummy(boolean fmt8) {
        dummy = true;
        if (fmt8) {
            maxblock = 02702;
            offset = 0;
            blocksize = 43;
            wper12line = 3;
            bperword = 2;
            type8 = true;
        } else {
            maxblock = 01102;
            offset = 1;
            blocksize = 128;
            wper12line = 2;
            bperword = 4;
            type8 = false;
        }
        mount1o.setEnabled(true);
        mount2o.setEnabled(false);
    }

    private void openFile1() {
        dummy = false;
        mount1o.setEnabled(false);
        mount2o.setEnabled(false);
        chooser.addChoosableFileFilter(new DJGFileFilter());
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
                        if (file.length() < 287433) {
                            displayInfo("INFILE", "File is not large enough! (not dumptd8e file?)");
                        } else {
                            maxblock = (int) ((file.length() - 3) / (129 + 1) / 3 * 2);
                            offset = 0;
                            displayInfo("INFILE", "Input file '" + file.getName() + "' successfully allocated");
                            type8 = true;
                            mount1o.setEnabled(true);
                        }
                    } else {
                        if (file.length() < 380292) {
                            displayInfo("INFILE", "File is not large enough! (not DJG file?)");
                        } else if (file.length() >= 589824) {
                            maxblock = 01102;
                            offset = 0;
                            blocksize = 128;
                            wper12line = 2;
                            bperword = 4;
                            type8 = false;
                            displayInfo("INFILE", "PDP-10 Input file '" + file.getName() + "' successfully allocated");
                            mount1o.setEnabled(true);
                        } else {
                            //maxblock = (int) (file.length() / 129 / 2);
                            maxblock = 02702;
                            offset = 0;
                            blocksize = 43;
                            wper12line = 3;
                            bperword = 2;
                            type8 = true;
                            displayInfo("INFILE", "PDP-8 Input file '" + file.getName() + "' successfully allocated");
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
        mount1o.setEnabled(false);
        mount2o.setEnabled(false);
        chooser.addChoosableFileFilter(new PXGFileFilter());
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
                    boolean test = true;
                    try {
                        if (file1.read() != 0x74) {
                            test = false;
                        }
                        if (file1.read() != 0x75) {
                            test = false;
                        }
                        if (file1.read() != 0x35) {
                            test = false;
                        }
                        if (file1.read() != 0x36) {
                            test = false;
                        }
                        if (test) {
                            maxblock = 02702;
                            offset = 0;
                            blocksize = 43;
                            wper12line = 3;
                            bperword = 2;
                            toDJG = true;
                            type8 = true;
                            displayInfo("INFILE", "Input file '" + file.getName() + "' successfully allocated");
                            mount2o.setEnabled(true);
                            if (file1.read() != 0x2d) {
                                test = false;
                            }
                            if (file1.read() != 0x31) {
                                test = false;
                            }
                            if (file1.read() != 0x30) {
                                test = false;
                            }
                            if (test) {
                                maxblock = 01102;
                                offset = 0;
                                blocksize = 128;
                                wper12line = 2;
                                bperword = 4;
                                type8 = false;
                            }
                            if (type8) {
                                displayInfo("INFILE", "PDP8 Input file '" + file.getName() + "' successfully allocated");
                            } else {
                                displayInfo("INFILE", "PDP10 Input file '" + file.getName() + "' successfully allocated");
                            }

                        } else {
                            System.out.println("Wrong filetype: not tu56 or tu56-10");
                        }
                    } catch (java.io.IOException e) {
                        System.out.println(e);
                    }
                }
            }
        } else {
            System.out.println("No file selected");
        }
    }

    public class Convert implements Runnable {

        public void run() {
            if ((file1 == null & !dummy) | file2 == null) {
                displayInfo("CONVERT", "No in- or outfile allocated!");
            } else {
                displayInfo("CONVERT", "Starting conversion");
                if (toDJG) {
                    for (long block = 0; block < maxblock; block++) {
                        readPXG(block + offset);
                        writeDJG(block);
                        if (block % 100 == 0) {
                            displayInfo("CONVERT", "Blocks up to " + block + " converted");
                        }
                    }
                } else {
                    endzone(true);
                    expandzone();
                    csi = 0;
                    for (long block = 0; block < maxblock; block++) {
                        if (dumptape) {
                            readdump(block);
                        } else {
                            readblock(block);
                        }
                        blockzone(block + offset);
                        if (block % 100 == 0) {
                            displayInfo("CONVERT", "Blocks up to " + block + " converted");
                        }
                    }
                    expandzone();
                    endzone(false);
                }
                displayInfo("CONVERT", "Conversion done!");
                closeFile();
                displayInfo("CONVERT", "Files closed");
            }
        }
    }

    private void endzone(boolean start) {
        int mask;
        for (int i = 0; i < 4096; i++) {
            if (start) {
                mask = REV_END_ZONE;
            } else {
                mask = END_ZONE;
            }
            for (int j = 11; j >= 0; j--) {
                setMark(mask >> j);
                line += 1;
            }
        }
    }

    private void expandzone() {
        int mask;
        for (int i = 0; i < 99; i++) {
            mask = EXPAND;
            for (int j = 11; j >= 0; j--) {
                setMark(mask >> j);
                line += 1;
            }
        }
    }

    private void blockzone(long block) {
        int mask;
        long data;
        int i;
        mask = EXPAND;
        for (int j = 11; j >= 0; j--) {
            setMark(mask >> j);
            line += 1;
        }

        mask = BLOCK_REV_GUARD;
        data = block << 18;
        for (int j = 11; j >= 0; j--) {
            setLine(data >> (j * 3));
            setMark(mask >> j);
            line += 1;
        }

        mask = LOCK_REV_CHECK;
        data = 077777777; //supposed to be good for pdp-6? was 077
        for (int j = 11; j >= 0; j--) {
            setLine(data >> (j * 3));
            setMark(mask >> j);
            line += 1;
        }

        i = 0;
        mask = REV_FIN_REV_PREFIN;
        data = buffer[i];
        for (int j = 11; j >= 0; j--) {
            setLine(data >> (j * 3));
            setMark(mask >> j);
            line += 1;
        }

        while (i < (blocksize - 2)) {
            i += 1;
            mask = DATA_MARK;
            data = buffer[i];
            for (int j = 11; j >= 0; j--) {
                setLine(data >> (j * 3));
                setMark(mask >> j);
                line += 1;
            }
        }
        i += 1;
        mask = PREFIN_FIN;
        data = buffer[i];
        for (int j = 11; j >= 0; j--) {
            setLine(data >> (j * 3));
            setMark(mask >> j);
            line += 1;
        }

        mask = CHECK_REV_LOCK;
        data = cs << 24;
        for (int j = 11; j >= 0; j--) {
            setLine(data >> (j * 3));
            setMark(mask >> j);
            line += 1;
        }

        mask = GUARD_REV_BLOCK;
        data = (~block) << 18;
        for (int j = 11; j >= 0; j--) {
            setLine(data >> (33 - j * 3));
            setMark(mask >> j);
            line += 1;
        }
    }

    private void readPXG(long block) {
        long data;
        int i;
        searchMark(BLOCK_REV_GUARD);
        data = readLines();
        if (block != (data >> 18)) {
            displayInfo("CONVERT", "Wrong block: " + block);
        }

        searchMark(LOCK_REV_CHECK);
        data = readLines();
        if ((077 != (data & 077)) & (0 != (data & 077))) {
            displayInfo("CONVERT", "Wrong reverse checksum!");
        }

        i = 0;
        searchMark(REV_FIN_REV_PREFIN);
        data = readLines();
        buffer[i] = data;

        while (i < (blocksize - 2)) {
            i += 1;
            searchMark(DATA_MARK);
            data = readLines();
            buffer[i] = data;
        }

        i += 1;
        searchMark(PREFIN_FIN);
        data = readLines();
        buffer[i] = data;

        searchMark(CHECK_REV_LOCK);
        data = readLines();
        cs = data >> 24;

        searchMark(GUARD_REV_BLOCK);
        int obverse = 0;
        data = readLines();
        for (int j = 0; j < 6; j++) {
            obverse |= (((~data) >> (15 - j * 3)) & 07) << (j * 3);
        }
        if (block != obverse) {
            displayInfo("CONVERT", "Wrong block!");
        }

        searchMark(EXPAND);

    }

    private void readblock(long block) {
        int word;
        long endian;
        int shift = 36 / wper12line;
        int mask = ~(-1 << shift);
        try {
            if (!dummy) {
                file1.seek(wper12line * bperword * blocksize * block);
            }
            cs = 0;
            for (int i = 0; i < blocksize; i++) {
                buffer[i] = 0;
                for (int j = wper12line; j > 0; j--) {
                    endian = 0;
                    if (!dummy) {
                        for (int k = 0; k < bperword; k++) {
                            word = file1.read();
                            endian |= word << (k * 8);
                        }
                    }
                    buffer[i] |= (endian & mask) << ((j - 1) * shift);
                }
                for (int m = 0; m < 6; m++) {
                    cs ^= (buffer[i] >>> (m * 6)) & 077;
                }
            }
            cs = (cs << 6) & 07700;
        } catch (java.io.IOException e) {
            System.out.println(e);
        }

    }

    private void writeDJG(long block) {
        int word;
        long endian;
        int shift = 36 / wper12line;
        int mask = ~(-1 << shift);
        try {
            file2.seek(wper12line * bperword * blocksize * block);
            for (int i = 0; i < blocksize; i++) {
                for (int j = wper12line; j > 0; j--) {
                    endian = (buffer[i] >>> ((j - 1) * shift)) & mask;
                    for (int k = 0; k < bperword; k++) {
                        word = (int) ((endian >>> (k * 8)) & 0xff);
                        file2.write(word);
                    }
                }
            }
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
    }

    private void readdump(long block) {
        long word3;
        int blockmark;
        try {
            file1.seek(130 / 2 * 3 * block);
            blockmark = file1.read();
            for (int i = 0; i < 43; i += 2) { //22 times
                word3 = (long) file1.read() << 32;
                word3 |= (long) file1.read() << 24;
                word3 |= (long) file1.read() << 16;
                word3 |= (long) file1.read() << 8;
                word3 |= (long) file1.read() << 0;
                buffer[i] = word3 >> 4;
                if (i < 42) { //21 times
                    word3 = (word3 & 0xf) << 32;
                    word3 |= (long) file1.read() << 24;
                    word3 |= (long) file1.read() << 16;
                    word3 |= (long) file1.read() << 8;
                    word3 |= (long) file1.read() << 0;
                    buffer[i + 1] = word3;
                }

            }
            if (blockmark != 0377) {
                displayInfo("CONVERT", "Bad block " + block + " in input");
            }
            cs = 0;
            for (int i = 0; i < 129; i++) {
                int mod = i % 3;
                word3 = (buffer[i / 3] >> mod * 12) & 0xfffL;
                csi += word3;
                csi &= 07777;
                cs ^= word3 & 077;
                cs ^= (word3 >> 6) & 077;
                cs &= 077;
            }

            cs = (cs << 6) & 07700;
            if (block == maxblock - 1) {
                blockmark = file1.read();
                csi +=
                        (long) file1.read();
                csi +=
                        (long) file1.read() << 8;
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

    private void setMark(int nextline) {
        int oldbyte;
        int newbyte;
        try {
            file2.seek(line / 2);
            oldbyte = file2.read();
            if (oldbyte < 0) {
                oldbyte = 0;
            }
            if (line % 2 == 0) {
                newbyte = (oldbyte & 0x7f) | ((nextline << 7) & 0x80);
            } else {
                newbyte = (oldbyte & 0xf7) | ((nextline << 3) & 0x08);
            }
            file2.seek(line / 2);
            file2.write(newbyte);
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
    }

    private void setLine(long nextline) {
        int oldbyte;
        int newbyte;
        try {
            file2.seek(line / 2);
            oldbyte = file2.read();
            if (oldbyte < 0) {
                oldbyte = 0;
            }
            if (line % 2 == 0) {
                newbyte = (int) ((oldbyte & 0x8f) | ((nextline & 07) << 4));
            } else {
                newbyte = (int) ((oldbyte & 0xf8) | (nextline & 07));
            }
            file2.seek(line / 2);
            file2.write(newbyte);
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
    }

    private long readLines() {
        int nextbyte;
        long nextdata;
        nextdata = 0;
        try {
            for (int i = 0; i < 6; i++) {
                nextdata = nextdata << 6;
                nextbyte = file1.read();
                nextdata = nextdata | ((nextbyte & 0x70) >> 1) | ((nextbyte & 0x07));
            }
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
        return nextdata;
    }

    private void searchMark(int mark) {
        int nextbyte;
        int nextmark;
        nextmark = 0;
        try {
            do {
                nextmark = (nextmark << 2) & 07777;
                nextbyte = file1.read();
                if (nextbyte == -1) {
                    displayInfo("CONVERT", "Mark not found");
                    System.exit(0);
                }
                nextmark = nextmark | ((nextbyte & 0x80) >> 6) | ((nextbyte & 0x08) >> 3);
            } while (nextmark != mark);
            file1.seek(file1.getFilePointer() - 6);
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
    }

    private void openFile2() {
        chooser.resetChoosableFileFilters();
        chooser.addChoosableFileFilter(new PXGFileFilter());
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
                    if (test.compareTo("PXG") == 0) {
                        if (file.isFile()) {
                            if (file.canWrite()) {
                                //file2 = new java.io.RandomAccessFile(file,"rw");
                                displayInfo("OUTFILE", "Overwrite, not implemented!");
                            } else {
                                displayInfo("OUTFILE", "Overwrite, not implemented!");
                            }
                        } else {
                            file2 = new java.io.RandomAccessFile(file, "rw");
                            displayInfo("OUTFILE", "Output file '" + file.getName() + "' successfully allocated");
                        }
                    } else {
                        displayInfo("OUTFILE", "Please use *.pxg extension!");
                    }
                } catch (java.io.IOException e) {
                    System.out.println(e);
                }
                if (file2 != null) {
                    if (file.length() != 0) {
                        displayInfo("OUTFILE", "File is not empty? huh?");
                    } else {
                        try {
                            file2.write(0x74); //t
                            file2.write(0x75); //u
                            file2.write(0x35); //5
                            file2.write(0x36); //6
                            if (!type8) {
                                file2.write(0x2d); //-
                                file2.write(0x31); //1
                                file2.write(0x30); //0
                            }

                            file2.seek(TAPE_LINES / 2 - 1);
                            file2.write(0);
                        } catch (java.io.IOException e) {
                            System.out.println(e);
                        }

                        if (type8) {
                            line = FORMAT_FIRST_LINE;
                        } else {
                            line = FORMAT_FIRST_LINE_10;
                        }
                    }
                }
            } else {
                System.out.println("No file selected");
            }
        }
    }

    private void openFile2b() {
        chooser.resetChoosableFileFilters();
        chooser.addChoosableFileFilter(new DJGFileFilter());
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
                    if (test.compareTo("DJG") == 0 | test.compareTo("TU56") == 0 | test.compareTo("BIN") == 0) {
                        if (file.isFile()) {
                            if (file.canWrite()) {
                                //file2 = new java.io.RandomAccessFile(file,"rw");
                                displayInfo("OUTFILE", "Overwrite, not implemented!");
                            } else {
                                displayInfo("OUTFILE", "Overwrite, not implemented!");
                            }
                        } else {
                            file2 = new java.io.RandomAccessFile(file, "rw");
                            displayInfo("OUTFILE", "Output file '" + file.getName() + "' successfully allocated");
                        }
                    } else {
                        displayInfo("OUTFILE", "Please use *.djg or *.tu56 or *.bin extension!");
                    }
                } catch (java.io.IOException e) {
                    System.out.println(e);
                }
                if (file2 != null) {
                    if (file.length() != 0) {
                        displayInfo("OUTFILE", "File is not empty? huh?");
                    } else {
                        try {
                            file2.seek(maxblock * blocksize * wper12line * bperword - 1);
                            //file2.seek(02702 * 2 * 129 - 1);
                            file2.write(0);
                        } catch (java.io.IOException e) {
                            System.out.println(e);
                        }
                    }
                }
            } else {
                System.out.println("No file selected");
            }
        }
    }

    public void closeFile() {
        if (file1 != null) {
            try {
                file1.close();
            } catch (java.io.IOException e) {
            }
            file1 = null;
        }

        if (file2 != null) {
            try {
                file2.close();
            } catch (java.io.IOException e) {
            }
            file2 = null;
            mount1o.setEnabled(false);
            mount2o.setEnabled(false);
        }
    }

    protected void displayInfo(String e, String s) {
        displayArea.append(e + ":    " + s + newline);
        System.out.println(e + ":    " + s);
    }

    private static class PXGFileFilter
            extends javax.swing.filechooser.FileFilter {

        public boolean accept(java.io.File file) {
            if (file == null) {
                return false;
            }
            return file.isDirectory() || file.getName().toLowerCase().endsWith(".pxg");
        }

        public String getDescription() {
            return "Dectape files (*.pxg)";
        }
    }

    private static class DJGFileFilter extends javax.swing.filechooser.FileFilter {

        public boolean accept(java.io.File file) {
            if (file == null) {
                return false;
            }
            return file.isDirectory() || file.getName().toLowerCase().endsWith(".djg") || file.getName().toLowerCase().endsWith(".tu56") || file.getName().toLowerCase().endsWith(".bin");
        }

        public String getDescription() {
            return "Dectape files (*.djg or *.tu56 or *.bin)";
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        JFrame myframe = new ConvertTape();
        myframe.setVisible(true);
    }

    private void exitForm(java.awt.event.WindowEvent evt) {
        displayInfo("CONVERT", "Closing window");
        System.exit(0);
    }
}
