/*
 * Constants.java
 *
 * Created on January 1, 2005, 0:00 PM
 */
/**
 *
 * @author  wvdmark@computer.org
 */
package Logic;

public interface Constants {

    public static final float VERSION = (float) 3.00;
    public static final int FETCH = 1;
    public static final int DEFER = 2;
    public static final int EXEC = 3;
    public static final int AND = 0;
    public static final int TAD = 1;
    public static final int ISZ = 2;
    public static final int DCA = 3;
    public static final int JMS = 4;
    public static final int JMP = 5;
    public static final int IOT = 6;
    public static final int OPR = 7;

    public static final int OUTACTOBUS = 0; //definitions for c0c1c2: c2=4, c1=2, c0=1
    public static final int OUTACTOBUSAC0 = 1;
    public static final int INACORBUSTOAC = 2;
    public static final int INBUSTOAC = 3;
    public static final int INPCPLUSBUSTOPC = 5;
    public static final int INBUSTOPC = 7;

    public static final int SKON = 00; //600X
    public static final int ION  = 01; //X
    public static final int IOF  = 02;
    public static final int SRQ  = 03;
    public static final int GTF  = 04;
    public static final int RTF  = 05;
    public static final int SGT  = 06;
    public static final int CAF  = 07;

    public static final int LBM  = 00; //617X
    public static final int RBM  = 01; //X
    public static final int RLB  = 02;
    public static final int RMR  = 03;
    public static final int MBC  = 04;
    public static final int RACA = 05;
    public static final int RACB = 06;
    public static final int RACC = 07;
    
    public static final int CDF  = 01; //62X3,62X7,63X3,63X7
    public static final int CIF  = 02;
    public static final int CDI  = 03;
    public static final int XDF  = 05; //X F=FIELD
    public static final int XIF  = 06;
    public static final int XDI  = 07;

    public static final int MXX0 = 00; //62X0 AND 63X0
    public static final int CTI  = 00; //6300 Multi8 cd to if
    public static final int LXM  = 00; //62X0 KT8A
    public static final int GTS  = 01; 
    public static final int RTS  = 02; 
    public static final int RXM  = 03; 
    public static final int LRR  = 04; 
    public static final int RRR  = 05; 
    public static final int LUSR = 06; 
    public static final int RUSR = 07; 
    
    public static final int MXX4 = 04; //62X4
    public static final int CINT = 00; //X
    public static final int RDF  = 01;
    public static final int RIF  = 02;
    public static final int RIB  = 03;
    public static final int RMF  = 04;
    public static final int SINT = 05;
    public static final int CUF  = 06;
    public static final int SUF  = 07;
    
    public static final boolean dm8e = true;    /*multi8 dm8e available */

    public static final int MXX5 = 05; //62X5 Old
    public static final int RTM  = 00; //63X4 multi8 memory management group
    public static final int SKME = 01; //X
    public static final int SKMM = 02;
    public static final int LTM  = 03;
    public static final int LRM  = 04;
    //public static final int XXX  = 05; //not used
    public static final int SMME = 06;
    public static final int CMME = 07;

    public static final int SWAB = 07431; /* EAE instructions */
    public static final int SWBA = 07447;
    public static final int SCL  = 01;
    public static final int ASC  = 01;
    public static final int MUY  = 02;
    public static final int DVI  = 03;
    public static final int NMI  = 04;
    public static final int SHL  = 05;
    public static final int ASR  = 06;
    public static final int LSR  = 07;
    public static final int SCA  = 010; /* mode b */
    public static final int DAD  = 011;
    public static final int DST  = 012;
    //public static final int SWBA= 013; 
    public static final int DPSZ = 014;
    public static final int DPIC = 015;
    public static final int DCM  = 016;
    public static final int SAM  = 017;

    public static final int KCF  = 00; /* tty */
    public static final int KSF  = 01;
    public static final int KCC  = 02;
    public static final int KRS  = 04;
    public static final int KIE  = 05;
    public static final int KRB  = 06;
    public static final int TFL  = 00;
    public static final int TSF  = 01;
    public static final int TCF  = 02;
    public static final int TPC  = 04;
    public static final int TSK  = 05;
    public static final int TLS  = 06;

    public static final int SDSS = 01; /* TD8E */
    public static final int SDST = 02;
    public static final int SDSQ = 03;
    public static final int SDLC = 04;
    public static final int SDLD = 05;
    public static final int SDRC = 06;
    public static final int SDRD = 07;
    public static final int UTSDEL = 1400000;
    public static final int SLFDEL = 250; //166; 
    public static final int BLOCK_LINES = 02702 * (129 * 2 / 3 + 10) * 6;
    public static final int BLOCK_LINES_10 = 01102 * (128 * 2 + 10) * 6;
    public static final int EXPAND_LINES = 100 * 2 * 6;
    public static final int ENDZONE_LINES = 4096 * 2 * 6;
    public static final int TOTAL_LINES = 2 * ENDZONE_LINES + 2 * EXPAND_LINES + BLOCK_LINES;
    public static final int TOTAL_LINES_10 = 2 * ENDZONE_LINES + 2 * EXPAND_LINES + BLOCK_LINES_10;
    public static final int TAPE_LINES = 0x100000;
    public static final int FORMAT_FIRST_LINE = (TAPE_LINES - TOTAL_LINES) / 2;
    public static final int FORMAT_FIRST_LINE_10 = (TAPE_LINES - TOTAL_LINES_10) / 2;
    public static final int FIRST_LINE = FORMAT_FIRST_LINE + ENDZONE_LINES / 2;
    public static final int FIRST_LINE_10 = FORMAT_FIRST_LINE_10 + ENDZONE_LINES / 2;

    public static final int RPE = 00; /* PTR PTP Reader/Punch */
    public static final int RSF = 01;
    public static final int RRB = 02;
    public static final int RFC = 04;
    public static final int RBF = 06;
    public static final int PCE = 00;
    public static final int PSF = 01;
    public static final int PCF = 02;
    public static final int PPC = 04;
    public static final int PLS = 06;
    public static final int PUNFAST = 20000;  //10 times faster
    public static final int PUNDEL  = 200000; //50 cps
    public static final int RDRINI  = 2000;   //really is immediate
    public static final int RDRFAST = 3333;   //10 times faster
    public static final int RDRDEL  = 33333;  //33333=300cps;
    public static final int RDRSTOP = 400000; //25 cps

    public static final int DSDD = 01; /* SI3040 disk */
    public static final int DLCR = 02;
    public static final int DRCR = 03;
    public static final int DCSR = 04;
    public static final int DRSR = 05;
    public static final int DLSS = 06;
    public static final int DRSS = 07;
    public static final int DSDE = 01;
    public static final int DLSR = 02;
    public static final int DSRR = 03;
    public static final int DLTR = 04;
    public static final int DLTW = 05;
    public static final int DRTR = 06;
    public static final int DWCA = 07;
    public static final int SIUNIT0 = 00;
    public static final int SIUNIT1 = 01;

    public static final int CLZE = 00; /* DK8-EP Real Time programmable clock */
    public static final int CLSK = 01;
    public static final int CLOE = 02;
    public static final int CLAB = 03;
    public static final int CLEN = 04;
    public static final int CLSA = 05;
    public static final int CLBA = 06;
    public static final int CLCA = 07;

    public static final int PSXX = 00; /* LE8 lineprinter */
    public static final int PSKF = 01;
    public static final int PCLF = 02;
    public static final int PSKE = 03;
    public static final int PSTB = 04;
    public static final int PSIE = 05;
    public static final int PCLP = 06;
    public static final int PCIE = 07;
    public static final int LPTLEIMM = 100; //IMMEDIATE
    public static final int LPTLEPRT = 400000; //40 MS SEGMENT PRINT
    public static final int LPTLESLW = 200000; //20 MS LINEFEED
    public static final int LPTLEFRF = 10000000; //1 SECOND FF
    public static final int LPTFLUSH = 10000000; //1 SECOND

    public static final int DBST = 00; /* LA180 lineprinter, skip out clear */
    public static final int DBSK = 01; /* skip in ready */
    public static final int DBRD = 02; /* read data in */
    public static final int DBCF = 03; /* clear in ready */
    public static final int DBTD = 04; /* write data out */
    public static final int DBSE = 05; /* int ena */
    public static final int DBCE = 06; /* int dis */
    public static final int DBSS = 07; /* strobe out */
    public static final int LPTLACHR = 60000; //150 CPS

    public static final int SBE = 01; /* Power fail, skip battery empty */
    public static final int SPL = 02; /* skip power low */
    public static final int CAL = 03; /* clear AC low */
    public static final int PWDEL = 1000; //1 second
    public static final int BLA  = 0x8000;
    public static final int BLXA = 0x4000;
    public static final int BDEP = 0x2000;
    public static final int BST  = 0x1000;
    public static final int PWDOWN    = 0x00000;
    public static final int PWUP      = 0x10000;
    public static final int SIBOOT    = 0x30000;
    public static final int TD8EBOOT  = 0x40000;
    public static final int BINLOADHI = 0x50000;
    public static final int BINLOADLO = 0x60000;
    public static final int RIMLOADHI = 0x70000;
    public static final int RIMLOADLO = 0x80000;
    public static final int BINPUNCHHI= 0x90000;

    public static final int FFST  = 00; /* FPP, start maint, device code 55 */
    public static final int FPINT = 01; /* skip if FPP flag */
    public static final int FPICL = 02; /* FPP init */
    public static final int FPCOM = 03; /* load command FPP */
    public static final int FPHLT = 04; /* FPP exit */
    public static final int FPST  = 05; /* FPP start */
    public static final int FPRST = 06; /* read status FPP */
    public static final int FPIST = 07; /* skip and read status FPP */
    
    public static final int FMODE = 01; /* FPP, enter maint, device code 56 */
    public static final int FMRB  = 03; /* FPP Maint, read buffer */
    public static final int FMRP  = 04; /* FPP Maint, read muPC */
    public static final int FMDO  = 05; /* FPP Maint, execute 1 */
    public static final int FPEP  = 07; /* select FPP EP mode, device code 56*/
    
    public static final int FPMode  = 0;
    public static final int DPMode  = 1; /* Operation modes*/
    public static final int EPMode  = 2;


    public static final String eol = System.getProperty("line.separator");
    public static final String LOREMIPSUM =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit." + eol
            + "Fusce mollis elit rhoncus leo pretium posuere. " + eol
            + "Cras pulvinar turpis id dui placerat molestie." + eol
            + "Vivamus in augue sit amet lacus lobortis vehicula et id elit. " + eol
            + "In hac habitasse platea dictumst." + eol
            + "Quisque vehicula metus vitae leo ultricies tristique. " + eol
            + "Integer tincidunt, mauris sit amet malesuada" + eol
            + "venenatis, enim urna ullamcorper tortor, " + eol
            + "vel posuere metus nulla sed turpis. Aenean tellus velit," + eol
            + "eleifend non lacinia pretium, rhoncus at massa. " + eol
            + "Sed risus magna, adipiscing iaculis congue sit amet," + eol
            + "imperdiet et ligula." + eol;
}
