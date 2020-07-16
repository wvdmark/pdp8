/*
 * Example
 *
 * Created on January 1, 2013, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */


package Logic;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class PowerBoot implements Device, Constants {

    public static int DevId10 = 010;

    public BusRegMem data;

    //public VirTimer pwbtim;
    private boolean pwfailflag = false;
    //private boolean intena = true;
    private boolean pwgone = false;
    private Timer pwtimer;
    private TimerTask pwtask;

    @SuppressWarnings("PointlessBitwiseExpression")
    private static final int[] boottable = {
        PWUP        | BLA      | 00200, //POWER UP RESTART FOR TEST
        PWUP        | BLXA|BST | 00000, //

        SIBOOT      | BLA      | 00000, //SI3040 BOOTSTRAP
        SIBOOT      | BLXA     | 00000, //
        SIBOOT      | BDEP     | 06502, //      DLCR	/ASSURE DATA FIELD 0 (ALSO WORD COUNT)
        SIBOOT      | BDEP     | 00000, //		/START CORE ADDRESS
        SIBOOT      | BDEP     | 06517, //      DWCA	/SET WC, CA FROM 0 AND 1
        SIBOOT      | BDEP     | 06512, //      DLSR	/SECTOR 0
        SIBOOT      | BDEP     | 06514, //      DLTR	/READ FROM TRACK ZERO, UNIT 0
        SIBOOT      | BDEP     | 05005, //      JMP .	/WAIT FOR DSDD;JMP .-1 OVERLAY
        SIBOOT      | BLA|BST  | 00000,
/*
        TD8EBOOT    | BLA      | 07300, //TD8E BOOTSTRAP
        TD8EBOOT    | BLXA     | 00000, //
        TD8EBOOT    | BDEP     | 01312, //K1000,  TAD GET     /PUT DRIVE IN REVERSE
        TD8EBOOT    | BDEP     | 04312, //	  JMS GET     /LOOK FOR END ZONE
        TD8EBOOT    | BDEP     | 04312, //	  JMS GET     /LOOK FOR 31 CODE
        TD8EBOOT    | BDEP     | 06773, //RD,	  SDSQ        /NOW READ ALL INTO CORE
        TD8EBOOT    | BDEP     | 05303, //	  JMP .-1
        TD8EBOOT    | BDEP     | 06777, //	  SDRD        /READ 12 BIT WORD
        TD8EBOOT    | BDEP     | 03726, //	  DCA I WCT   /AND PUT IT IN CORE
        TD8EBOOT    | BDEP     | 02326, //	  ISZ WCT
        TD8EBOOT    | BDEP     | 05303, //	  JMP RD      /LOOP UNTIL FIELD 0
        TD8EBOOT    | BDEP     | 05732, //	  JMP I STRT  /IS LOADED, THEN START
        TD8EBOOT    | BDEP     | 02000, //GET,	  2000
        TD8EBOOT    | BDEP     | 01300, //	  TAD K1000   /SET MOTION &DIRECTION
        TD8EBOOT    | BDEP     | 06774, //	  SDLC
        TD8EBOOT    | BDEP     | 06771, //BSRCH,  SDSS        /WAIT FOR 22 OR 31 CODE
        TD8EBOOT    | BDEP     | 05315, //	  JMP .-1     /22 IS END ZONE, 31 IS
        TD8EBOOT    | BDEP     | 06776, //	  SDRC        /4 WORDS BEFORE DATA
        TD8EBOOT    | BDEP     | 00331, //	  AND K77     /IS THIS WHAT WE WANT?
        TD8EBOOT    | BDEP     | 01327, //	  TAD BM22    /THIS GETS INCREMENTED
        TD8EBOOT    | BDEP     | 07640, //	  SZA CLA     /IF YES, RETURN.
        TD8EBOOT    | BDEP     | 05315, //	  JMP BSRCH   /NO.KEEP LOOKING.
        TD8EBOOT    | BDEP     | 02321, //	  ISZ .-3     /LOOK FOR NEXT IN LIST
        TD8EBOOT    | BDEP     | 05712, //	  JMP I GET
        TD8EBOOT    | BDEP     | 07354, //WCT,	  7354        /START LOADING CORE AT 7354
        TD8EBOOT    | BDEP     | 07756, //BM22,	  -22         /THE OTHER BOOTSTRAP GETS
        TD8EBOOT    | BDEP     | 07747, //	  -31         /LOADED AT 7400.
        TD8EBOOT    | BDEP     | 00077, //K77,	  77
        TD8EBOOT    | BDEP     | 07400, //STRT,	  7400
        TD8EBOOT    | BLA|BST  | 07300, //
*/
        TD8EBOOT    | BLA      | 07300, //TD8E BOOTSTRAP
        TD8EBOOT    | BLXA     | 00000, //
        TD8EBOOT    | BDEP     | 01737, //        TAD I BATCCL/GET DATE EXTENSION
        TD8EBOOT    | BDEP     | 03340, //        DCA DATTMP  /SAVE
        TD8EBOOT    | BDEP     | 01316, //K1000,  TAD GET     /PUT DRIVE IN REVERSE
        TD8EBOOT    | BDEP     | 04316, //        JMS GET     /LOOK FOR END ZONE
        TD8EBOOT    | BDEP     | 04316, //        JMS GET     /LOOK FOR 31 CODE
        TD8EBOOT    | BDEP     | 06773, //RD,     SDSQ        /NOW READ ALL INTO CORE
        TD8EBOOT    | BDEP     | 05305, //        JMP .-1
        TD8EBOOT    | BDEP     | 06777, //        SDRD        /READ 12 BIT WORD
        TD8EBOOT    | BDEP     | 03732, //        DCA I WCT   /AND PUT IT IN CORE
        TD8EBOOT    | BDEP     | 02332, //        ISZ WCT
        TD8EBOOT    | BDEP     | 05305, //        JMP RD      /LOOP UNTIL FIELD 0
        TD8EBOOT    | BDEP     | 01340, //        TAD DATTMP
        TD8EBOOT    | BDEP     | 03737, //        DCA I BATCCL/RESTORE DATE EXTENSION
        TD8EBOOT    | BDEP     | 05736, //        JMP I STRT  /IS LOADED, THEN START
        TD8EBOOT    | BDEP     | 02000, //GET,    2000        /7400 UNIT,REVERSE,GO,WRITE
        TD8EBOOT    | BDEP     | 01302, //        TAD K1000   /SET MOTION &DIRECTION
        TD8EBOOT    | BDEP     | 06774, //        SDLC
        TD8EBOOT    | BDEP     | 06771, //BSRCH,  SDSS        /WAIT FOR 22 OR 31 CODE
        TD8EBOOT    | BDEP     | 05321, //        JMP .-1     /22 IS END ZONE, 31 IS
        TD8EBOOT    | BDEP     | 06776, //        SDRC        /4 WORDS BEFORE DATA
        TD8EBOOT    | BDEP     | 00335, //        AND K77     /IS THIS WHAT WE WANT
        TD8EBOOT    | BDEP     | 01333, //        TAD BM22    /THIS GETS INCREMENTED
        TD8EBOOT    | BDEP     | 07640, //        SZA CLA     /IF YES, RETURN.
        TD8EBOOT    | BDEP     | 05321, //        JMP BSRCH   /NO.KEEP LOOKING.
        TD8EBOOT    | BDEP     | 02325, //        ISZ .-3     /LOOK FOR NEXT IN LIST
        TD8EBOOT    | BDEP     | 05716, //        JMP I GET
        TD8EBOOT    | BDEP     | 07354, //WCT,    7354        /START LOADING CORE AT 7354
        TD8EBOOT    | BDEP     | 07756, //BM22,   -22         /THE OTHER BOOTSTRAP GETS
        TD8EBOOT    | BDEP     | 07747, //        -31         /LOADED AT 7400.
        TD8EBOOT    | BDEP     | 00077, //K77,    77
        TD8EBOOT    | BDEP     | 07400, //STRT,   7400        /TDBOOT
        TD8EBOOT    | BDEP     | 07777, //BATCCL, 7777
        TD8EBOOT    | BDEP     | 00000, //DATTMP, 0000
        TD8EBOOT    | BLA|BST  | 07300, //
        
        BINLOADLO   | BLA      | 07612, //LOW SPEED BIN LOADER
        BINLOADLO   | BLXA     | 00000, //
        BINLOADLO   | BDEP     | 07777, //SWITCH, 7777    /IS NEGATIVE FOR LOW SPEED
        BINLOADLO   | BDEP     | 00000, //MEMTEM, 0
        BINLOADLO   | BDEP     | 00000, //CHAR,   0
        BINLOADLO   | BDEP     | 00000, //CHKSUM, 0
        BINLOADLO   | BDEP     | 00000, //ORIGIN, 0
        BINLOADLO   | BDEP     | 01212, //
        BINLOADLO   | BDEP     | 07402, //
        BINLOADLO   | BDEP     | 07402, //
        BINLOADLO   | BDEP     | 07402, //
        BINLOADLO   | BDEP     | 07402, //
        BINLOADLO   | BDEP     | 07402, //
        BINLOADLO   | BDEP     | 07402, //
        BINLOADLO   | BDEP     | 00000, //BEGG,   0
        BINLOADLO   | BDEP     | 03212, //        DCA SWITCH  /SET SWITCH
        BINLOADLO   | BDEP     | 04260, //        JMS READ    /GET A CHARACTER
        BINLOADLO   | BDEP     | 01300, //        TAD M376    /TEST FOR 377
        BINLOADLO   | BDEP     | 07750, //        SPA SNA CLA
        BINLOADLO   | BDEP     | 05237, //        JMP .+4     /NO
        BINLOADLO   | BDEP     | 02212, //        ISZ SWITCH  /YES, COMPLEMENT SWITCH
        BINLOADLO   | BDEP     | 07040, //        CMA
        BINLOADLO   | BDEP     | 05227, //        JMP BEGG+1
        BINLOADLO   | BDEP     | 01212, //        TAD SWITCH  /NOT 377
        BINLOADLO   | BDEP     | 07640, //        SZA CLA     /IS SWITCH SET?
        BINLOADLO   | BDEP     | 05230, //        JMP BEGG+2  /YES, IGNORE
        BINLOADLO   | BDEP     | 01214, //        TAD CHAR    /NO, TEST FOR CODE
        BINLOADLO   | BDEP     | 00274, //        AND MASK    /TYPES
        BINLOADLO   | BDEP     | 01341, //        TAD M200
        BINLOADLO   | BDEP     | 07510, //        SPA
        BINLOADLO   | BDEP     | 02226, //        ISZ BEGG    /DATA OR ORIGIN
        BINLOADLO   | BDEP     | 07750, //        SPA SNA CLA
        BINLOADLO   | BDEP     | 05626, //        JMP I BEGG  /DATA, ORIGIN OR L/T
        BINLOADLO   | BDEP     | 01214, //        TAD CHAR    /FIELD SETTING
        BINLOADLO   | BDEP     | 00256, //        AND FMASK
        BINLOADLO   | BDEP     | 01257, //        TAD CHANGE
        BINLOADLO   | BDEP     | 03213, //        DCA MEMTEM
        BINLOADLO   | BDEP     | 05230, //        JMP BEGG+2  /CONTINUE INPUT
        BINLOADLO   | BDEP     | 00070, //FMASK,  70
        BINLOADLO   | BDEP     | 06201, //CHANGE, CDF
        BINLOADLO   | BDEP     | 00000, //READ,   0
        BINLOADLO   | BDEP     | 00000, //        0
        BINLOADLO   | BDEP     | 06031, //LOR,    KSF         /WAIT FOR FLAG
        BINLOADLO   | BDEP     | 05262, //        JMP .-1
        BINLOADLO   | BDEP     | 06036, //        KRB
        BINLOADLO   | BDEP     | 03214, //        DCA CHAR
        BINLOADLO   | BDEP     | 01214, //        TAD CHAR
        BINLOADLO   | BDEP     | 05660, //        JMP I READ
        BINLOADLO   | BDEP     | 06011, //HIR,    RSF
        BINLOADLO   | BDEP     | 05270, //        JMP .-1
        BINLOADLO   | BDEP     | 06016, //        RRB RFC
        BINLOADLO   | BDEP     | 05265, //        JMP LOR+3
        BINLOADLO   | BDEP     | 00300, //MASK,   300
        BINLOADLO   | BDEP     | 04343, //BEND,   JMS ASSEMB
        BINLOADLO   | BDEP     | 07041, //        CIA
        BINLOADLO   | BDEP     | 01215, //        TAD CHKSUM
        BINLOADLO   | BDEP     | 07402,//M376,    HLT
        BINLOADLO   | BDEP     | 06032,//BEGIN,   KCC
        BINLOADLO   | BDEP     | 06014,//         RFC
        BINLOADLO   | BDEP     | 06214,//         RDF
        BINLOADLO   | BDEP     | 01257,//         TAD CHANGE
        BINLOADLO   | BDEP     | 03213,//         DCA MEMTEM  /SAVE FIELD INSTRUCTION
        BINLOADLO   | BDEP     | 01212,//         TAD SWITCH  /WAS 07604 CLA OSR
        BINLOADLO   | BDEP     | 07700,//         SMA CLA
        BINLOADLO   | BDEP     | 01353,//         TAD HIRI
        BINLOADLO   | BDEP     | 01352,//         TAD LORI
        BINLOADLO   | BDEP     | 03261,//         DCA READ+1
        BINLOADLO   | BDEP     | 04226,//         JMS BEGG
        BINLOADLO   | BDEP     | 05313,//         JMP .-1     /IGNORE LEADER
        BINLOADLO   | BDEP     | 03215,//GO,      DCA CHKSUM
        BINLOADLO   | BDEP     | 01213,//         TAD MEMTEM
        BINLOADLO   | BDEP     | 03336,//         DCA MEMFLD
        BINLOADLO   | BDEP     | 01214,//         TAD CHAR
        BINLOADLO   | BDEP     | 03376,//         DCA WORD1
        BINLOADLO   | BDEP     | 04260,//         JMS READ
        BINLOADLO   | BDEP     | 03355,//         DCA WORD2
        BINLOADLO   | BDEP     | 04226,//         JMS BEGG    /LOOK AHEAD
        BINLOADLO   | BDEP     | 05275,//         JMP BEND    /TRAILER, END
        BINLOADLO   | BDEP     | 04343,//         JMS ASSEMB
        BINLOADLO   | BDEP     | 07420,//         SNL
        BINLOADLO   | BDEP     | 05336,//         JMP MEMFLD
        BINLOADLO   | BDEP     | 03216,//         DCA ORIGIN
        BINLOADLO   | BDEP     | 01376,//CHEX,    TAD WORD1
        BINLOADLO   | BDEP     | 01355,//         TAD WORD2
        BINLOADLO   | BDEP     | 01215,//         TAD CHKSUM
        BINLOADLO   | BDEP     | 05315,//         JMP GO
        BINLOADLO   | BDEP     | 06201,//MEMFLD,  CDF
        BINLOADLO   | BDEP     | 03616,//         DCA I ORIGIN
        BINLOADLO   | BDEP     | 02216,//         ISZ ORIGIN
        BINLOADLO   | BDEP     | 07600,//M200,    7600
        BINLOADLO   | BDEP     | 05332,//         JMP CHEX
        BINLOADLO   | BDEP     | 00000,//ASSEMB,  0
        BINLOADLO   | BDEP     | 01376,//         TAD WORD1
        BINLOADLO   | BDEP     | 07106,//         CLL RTL
        BINLOADLO   | BDEP     | 07006,//         RTL
        BINLOADLO   | BDEP     | 07006,//         RTL
        BINLOADLO   | BDEP     | 01355,//         TAD WORD2
        BINLOADLO   | BDEP     | 05743,//         JMP I ASSEMB
        BINLOADLO   | BDEP     | 05262,//LORI,    JMP LOR
        BINLOADLO   | BDEP     | 00006,//HIRI,    HIR-LOR
        BINLOADLO   | BDEP     | 00000,//         0
        BINLOADLO   | BDEP     | 00000,//WORD2,   0           /WORD1=7776
        //BINLOADLO   | BDEP     | 05301,//	  JMP BEGIN   /DO NOT USE -> BIPCCL
        BINLOADLO   | BLA|BST  | 07701,//LOW SPEED BIN LOADER

        BINLOADHI   | BLA      | 07612, //HI SPEED BIN LOADER
        BINLOADHI   | BLXA     | 00000, //
        BINLOADHI   | BDEP     | 00000, //SWITCH,  0    	/IS ZERO FOR HI SPEED
        BINLOADHI   | BDEP     | 00000, //MEMTEM,  0
        BINLOADHI   | BDEP     | 00000, //CHAR,    0
        BINLOADHI   | BDEP     | 00000, //CHKSUM,  0
        BINLOADHI   | BDEP     | 00000, //ORIGIN,  0
        BINLOADHI   | BDEP     | 01212, //
        BINLOADHI   | BDEP     | 07402, //
        BINLOADHI   | BDEP     | 07402, //
        BINLOADHI   | BDEP     | 07402, //
        BINLOADHI   | BDEP     | 07402, //
        BINLOADHI   | BDEP     | 07402, //
        BINLOADHI   | BDEP     | 07402, //
        BINLOADHI   | BDEP     | 00000, //BEGG,    0
        BINLOADHI   | BDEP     | 03212, //         DCA SWITCH  /SET SWITCH
        BINLOADHI   | BDEP     | 04260, //         JMS READ    /GET A CHARACTER
        BINLOADHI   | BDEP     | 01300, //         TAD M376    /TEST FOR 377
        BINLOADHI   | BDEP     | 07750, //         SPA SNA CLA
        BINLOADHI   | BDEP     | 05237, //         JMP .+4     /NO
        BINLOADHI   | BDEP     | 02212, //         ISZ SWITCH  /YES, COMPLEMENT SWITCH
        BINLOADHI   | BDEP     | 07040, //         CMA
        BINLOADHI   | BDEP     | 05227, //         JMP BEGG+1
        BINLOADHI   | BDEP     | 01212, //         TAD SWITCH  /NOT 377
        BINLOADHI   | BDEP     | 07640, //         SZA CLA     /IS SWITCH SET?
        BINLOADHI   | BDEP     | 05230, //         JMP BEGG+2  /YES, IGNORE
        BINLOADHI   | BDEP     | 01214, //         TAD CHAR    /NO, TEST FOR CODE
        BINLOADHI   | BDEP     | 00274, //         AND MASK    /TYPES
        BINLOADHI   | BDEP     | 01341, //         TAD M200
        BINLOADHI   | BDEP     | 07510, //         SPA
        BINLOADHI   | BDEP     | 02226, //         ISZ BEGG    /DATA OR ORIGIN
        BINLOADHI   | BDEP     | 07750, //         SPA SNA CLA
        BINLOADHI   | BDEP     | 05626, //         JMP I BEGG  /DATA, ORIGIN OR L/T
        BINLOADHI   | BDEP     | 01214, //         TAD CHAR    /FIELD SETTING
        BINLOADHI   | BDEP     | 00256, //         AND FMASK
        BINLOADHI   | BDEP     | 01257, //         TAD CHANGE
        BINLOADHI   | BDEP     | 03213, //         DCA MEMTEM
        BINLOADHI   | BDEP     | 05230, //         JMP BEGG+2  /CONTINUE INPUT
        BINLOADHI   | BDEP     | 00070, //FMASK,   70
        BINLOADHI   | BDEP     | 06201, //CHANGE,  CDF
        BINLOADHI   | BDEP     | 00000, //READ,    0
        BINLOADHI   | BDEP     | 00000, //         0
        BINLOADHI   | BDEP     | 06031, //LOR,     KSF         /WAIT FOR FLAG
        BINLOADHI   | BDEP     | 05262, //         JMP .-1
        BINLOADHI   | BDEP     | 06036, //         KRB
        BINLOADHI   | BDEP     | 03214, //         DCA CHAR
        BINLOADHI   | BDEP     | 01214, //         TAD CHAR
        BINLOADHI   | BDEP     | 05660, //         JMP I READ
        BINLOADHI   | BDEP     | 06011, //HIR,     RSF
        BINLOADHI   | BDEP     | 05270, //         JMP .-1
        BINLOADHI   | BDEP     | 06016, //         RRB RFC
        BINLOADHI   | BDEP     | 05265, //         JMP LOR+3
        BINLOADHI   | BDEP     | 00300, //MASK,    300
        BINLOADHI   | BDEP     | 04343, //BEND,    JMS ASSEMB
        BINLOADHI   | BDEP     | 07041, //         CIA
        BINLOADHI   | BDEP     | 01215, //         TAD CHKSUM
        BINLOADHI   | BDEP     | 07402,//M376,    HLT
        BINLOADHI   | BDEP     | 06032,//BEGIN,   KCC
        BINLOADHI   | BDEP     | 06014,//         RFC
        BINLOADHI   | BDEP     | 06214,//         RDF
        BINLOADHI   | BDEP     | 01257,//         TAD CHANGE
        BINLOADHI   | BDEP     | 03213,//         DCA MEMTEM  /SAVE FIELD INSTRUCTION
        BINLOADHI   | BDEP     | 01212,//         TAD SWITCH  /WAS 07604 CLA OSR
        BINLOADHI   | BDEP     | 07700,//         SMA CLA
        BINLOADHI   | BDEP     | 01353,//         TAD HIRI
        BINLOADHI   | BDEP     | 01352,//         TAD LORI
        BINLOADHI   | BDEP     | 03261,//         DCA READ+1
        BINLOADHI   | BDEP     | 04226,//         JMS BEGG
        BINLOADHI   | BDEP     | 05313,//         JMP .-1     /IGNORE LEADER
        BINLOADHI   | BDEP     | 03215,//GO,      DCA CHKSUM
        BINLOADHI   | BDEP     | 01213,//         TAD MEMTEM
        BINLOADHI   | BDEP     | 03336,//         DCA MEMFLD
        BINLOADHI   | BDEP     | 01214,//         TAD CHAR
        BINLOADHI   | BDEP     | 03376,//         DCA WORD1
        BINLOADHI   | BDEP     | 04260,//         JMS READ
        BINLOADHI   | BDEP     | 03355,//         DCA WORD2
        BINLOADHI   | BDEP     | 04226,//         JMS BEGG    /LOOK AHEAD
        BINLOADHI   | BDEP     | 05275,//         JMP BEND    /TRAILER, END
        BINLOADHI   | BDEP     | 04343,//         JMS ASSEMB
        BINLOADHI   | BDEP     | 07420,//         SNL
        BINLOADHI   | BDEP     | 05336,//         JMP MEMFLD
        BINLOADHI   | BDEP     | 03216,//         DCA ORIGIN
        BINLOADHI   | BDEP     | 01376,//CHEX,    TAD WORD1
        BINLOADHI   | BDEP     | 01355,//         TAD WORD2
        BINLOADHI   | BDEP     | 01215,//         TAD CHKSUM
        BINLOADHI   | BDEP     | 05315,//         JMP GO
        BINLOADHI   | BDEP     | 06201,//MEMFLD,  CDF
        BINLOADHI   | BDEP     | 03616,//         DCA I ORIGIN
        BINLOADHI   | BDEP     | 02216,//         ISZ ORIGIN
        BINLOADHI   | BDEP     | 07600,//M200,    7600
        BINLOADHI   | BDEP     | 05332,//         JMP CHEX
        BINLOADHI   | BDEP     | 00000,//ASSEMB,  0
        BINLOADHI   | BDEP     | 01376,//         TAD WORD1
        BINLOADHI   | BDEP     | 07106,//         CLL RTL
        BINLOADHI   | BDEP     | 07006,//         RTL
        BINLOADHI   | BDEP     | 07006,//         RTL
        BINLOADHI   | BDEP     | 01355,//         TAD WORD2
        BINLOADHI   | BDEP     | 05743,//         JMP I ASSEMB
        BINLOADHI   | BDEP     | 05262,//LORI,    JMP LOR
        BINLOADHI   | BDEP     | 00006,//HIRI,    HIR-LOR
        BINLOADHI   | BDEP     | 00000,//         0
        BINLOADHI   | BDEP     | 00000,//WORD2,   0           /WORD1=7776
        //BINLOADHI   | BDEP     | 05301,//	  JMP BEGIN   /DO NOT USE -> BIPCCL
        BINLOADHI   | BLA|BST  | 07701, //HI SPEED BIN LOADER

        RIMLOADLO   | BLA      | 07756, //LOW SPEED RIM LOADER
        RIMLOADLO   | BLXA     | 00000,
        RIMLOADLO   | BDEP     | 06032, //BEG,  KCC         /CLEAR AC AND FLAG
        RIMLOADLO   | BDEP     | 06031, //RSF2, KSF         /SKIP IF FLAG=1
        RIMLOADLO   | BDEP     | 05357, //      JMP .-1     /LOOKING FOR CHAR
        RIMLOADLO   | BDEP     | 06036, //RCC1, KRB         /READ BUFFER
        RIMLOADLO   | BDEP     | 07106, //      CLL RTL     /CH8 IN AC0
        RIMLOADLO   | BDEP     | 07006, //      RTL         /CHECKING FOR LEADER
        RIMLOADLO   | BDEP     | 07510, //      SPA         /FOUND LEADER
        RIMLOADLO   | BDEP     | 05374, //      JMP TEMP-2  /OK, CH7 IN LINK
        RIMLOADLO   | BDEP     | 07006, //      RTL
        RIMLOADLO   | BDEP     | 06031, //RSF3, KSF
        RIMLOADLO   | BDEP     | 05367, //      JMP .-1
        RIMLOADLO   | BDEP     | 06034, //RCC2, KRS         /READ, NO CLEAR
        RIMLOADLO   | BDEP     | 07420, //      SNL         /CHECKING FOR ADDRESS
        RIMLOADLO   | BDEP     | 03776, //      DCA I TEMP  /STORE CONTENTS
        RIMLOADLO   | BDEP     | 03376, //      DCA TEMP    /STORE ADDRESS
        RIMLOADLO   | BDEP     | 05356, //      JMP BEG     /NEXT WORD
        RIMLOADLO   | BDEP     | 00000, //TEMP, 0
        RIMLOADLO   | BLA|BST  | 07756, //LOW SPEED RIM LOADER START

        RIMLOADHI   | BLA      | 07756, //HIGH SPEED RIM LOADER
        RIMLOADHI   | BLXA     | 00000,
        RIMLOADHI   | BDEP     | 06014, //BEG,  RCF         /CLEAR AC AND FLAG
        RIMLOADHI   | BDEP     | 06011, //      RSF         /SKIP IF FLAG=1
        RIMLOADHI   | BDEP     | 05357, //      JMP .-1     /LOOKING FOR CHAR
        RIMLOADHI   | BDEP     | 06016, //      RCC         /READ BUFFER
        RIMLOADHI   | BDEP     | 07106, //      CLL RTL     /CH8 IN AC0
        RIMLOADHI   | BDEP     | 07006, //      RTL         /CHECKING FOR LEADER
        RIMLOADHI   | BDEP     | 07510, //      SPA         /FOUND LEADER
        RIMLOADHI   | BDEP     | 05374, //      JMP TEMP-2  /OK
        RIMLOADHI   | BDEP     | 07006, //      RTL         /NO, CH7 IN LINK
        RIMLOADHI   | BDEP     | 06011, //      RSF
        RIMLOADHI   | BDEP     | 05367, //      JMP .-1
        RIMLOADHI   | BDEP     | 06016, //      RCC         /READ, NO CLEAR
        RIMLOADHI   | BDEP     | 07420, //      SNL         /CHECKING FOR ADDRESS
        RIMLOADHI   | BDEP     | 03776, //      DCA I TEMP  /STORE CONTENTS
        RIMLOADHI   | BDEP     | 03376, //      DCA TEMP    /STORE ADDRESS
        RIMLOADHI   | BDEP     | 05357, //      JMP BEG+1   /NEXT WORD
        RIMLOADHI   | BDEP     | 00000, //TEMP, 0
        RIMLOADHI   | BLA|BST  | 07756, //HIGH SPEED RIM LOADER START

        BINPUNCHHI  | BLA      | 07465,//BINARY PUNCH HI SPEED
        BINPUNCHHI  | BLXA     | 00000,//
        BINPUNCHHI  | BDEP     | 01374,// BPUN,	TAD C200    /WAS CLA CLL /
        BINPUNCHHI  | BDEP     | 06026,//	PLS         /INITIAL PUNCH
        BINPUNCHHI  | BDEP     | 03366,//	DCA CKSM    /CLEAR CHECK-SUM
        BINPUNCHHI  | BDEP     | 04330,//	JMS PLOT    /GO PUNCH LEADER CODES
        BINPUNCHHI  | BDEP     | 07402,//	HLT         /SET SWITCHES=NUMBER OF BLOCKS
        BINPUNCHHI  | BDEP     | 07604,//	LAS
        BINPUNCHHI  | BDEP     | 07041,//	CIA
        BINPUNCHHI  | BDEP     | 03367,//	DCA NB      /STORE MINUS NUMBER OF BLOCKS
        BINPUNCHHI  | BDEP     | 07402,// NXBL, HLT         /SET SWITCHES=INITIAL ADDRESS OF BLOCK
        BINPUNCHHI  | BDEP     | 07604,//	LAS
        BINPUNCHHI  | BDEP     | 03370,//	DCA IA
        BINPUNCHHI  | BDEP     | 07402,//	HLT         /SET SWITCHES=FINAL ADDRESS OF BLOCK
        BINPUNCHHI  | BDEP     | 07604,//	LAS
        BINPUNCHHI  | BDEP     | 07001,//	IAC
        BINPUNCHHI  | BDEP     | 03371,//	DCA FA
        BINPUNCHHI  | BDEP     | 01370,//	TAD IA
        BINPUNCHHI  | BDEP     | 07120,//	STL         /TO PUNCH IA AS ORIGIN
        BINPUNCHHI  | BDEP     | 04341,// PUNL, JMS BINP    /GO PUNCH WORD AS TWO LINES OF TAPE
        BINPUNCHHI  | BDEP     | 01370,//	TAD IA
        BINPUNCHHI  | BDEP     | 07041,//	CIA
        BINPUNCHHI  | BDEP     | 01371,//	TAD FA      /AC=FA-IA
        BINPUNCHHI  | BDEP     | 07650,//	SNA CLA     /WAS IT LAST WORD OF BLOCK?
        BINPUNCHHI  | BDEP     | 05320,//	JMP .+5     /IT WAS THE LAST WORD
        BINPUNCHHI  | BDEP     | 01770,//	TAD IIA     /GET WORD TO PUNCH
        BINPUNCHHI  | BDEP     | 07100,//	CLL         /NOT AN ORIGIN
        BINPUNCHHI  | BDEP     | 02370,//	ISZ IA      /JUST INDEX IA
        BINPUNCHHI  | BDEP     | 05306,//	JMP PUNL
        BINPUNCHHI  | BDEP     | 02367,//	ISZ NB      /IS THERE ANOTHER BLOCK?
        BINPUNCHHI  | BDEP     | 05275,//	JMP NXBL    /HANDLE NEXT BLOCK
        BINPUNCHHI  | BDEP     | 01366,//	TAD CKSM
        BINPUNCHHI  | BDEP     | 07100,//	CLL
        BINPUNCHHI  | BDEP     | 04341,//	JMS BINP    /GO PUNCH CHECK SUM
        BINPUNCHHI  | BDEP     | 04330,//	JMS PLOT    /GO PUNCH TRAILER CODES
        BINPUNCHHI  | BDEP     | 07402,//	HLT         /DONE
        BINPUNCHHI  | BDEP     | 05265,//	JMP BPUN
        BINPUNCHHI  | BDEP     | 00000,// PLOT, 0
        BINPUNCHHI  | BDEP     | 07300,//	CLA CLL
        BINPUNCHHI  | BDEP     | 01372,//	TAD M212    /TO PUNCH 212 OCTAL LEADER TRAILER CODES
        BINPUNCHHI  | BDEP     | 03373,//	DCA  CTR1
        BINPUNCHHI  | BDEP     | 01374,//	TAD  C200   /LEADER  TRAILER  CODE
        BINPUNCHHI  | BDEP     | 04361,//	JMS PUN     /PUNCH  C  (AC)
        BINPUNCHHI  | BDEP     | 02373,//	ISZ CTR1    /ANOTHER  L-T  CODE  OK  NOT?
        BINPUNCHHI  | BDEP     | 05335,//	JMP .-2     /GO PUNCH ANOTHER
        BINPUNCHHI  | BDEP     | 05730,//	JMP I PLOT  /EXIT
        BINPUNCHHI  | BDEP     | 00000,// BINP, 0
        BINPUNCHHI  | BDEP     | 03375,//	DCA TEMl
        BINPUNCHHI  | BDEP     | 01375,//	TAD TEM1
        BINPUNCHHI  | BDEP     | 07012,//	RTR
        BINPUNCHHI  | BDEP     | 07012,//	RTR
        BINPUNCHHI  | BDEP     | 07012,//	RTR
        BINPUNCHHI  | BDEP     | 00376,//	AND SL7     /FIRST TWO OCTAL DIGITS IN AC 5-11
        BINPUNCHHI  | BDEP     | 04361,//	JMS PUN     /PUNCH C (AC)
        BINPUNCHHI  | BDEP     | 01366,//	TAD CKSM
        BINPUNCHHI  | BDEP     | 03366,//	DCA CKSM
        BINPUNCHHI  | BDEP     | 01375,//	TAD TEMl
        BINPUNCHHI  | BDEP     | 00377,//	AND SL6     /LAST TWO OCTAL DIGITS IN AC 6-11
        BINPUNCHHI  | BDEP     | 04361,//	JMS PUN     /PUNCH C (AC)
        BINPUNCHHI  | BDEP     | 01366,//	TAD CKSM
        BINPUNCHHI  | BDEP     | 03366,//	DCA CKSM
        BINPUNCHHI  | BDEP     | 05741,//	JMP I BINP  /EXIT
        BINPUNCHHI  | BDEP     | 00000,// PUN, 	0           /ROUTINE TO PUNCH C (AC)
        BINPUNCHHI  | BDEP     | 06021,//	PSF         /AND EXIT WITH C (AC) UNALTERED
        BINPUNCHHI  | BDEP     | 05362,//	JMP .-1
        BINPUNCHHI  | BDEP     | 06026,//	PLS         /PUNCH IT
        BINPUNCHHI  | BDEP     | 05761,//	JMP I PUN   /EXIT
        BINPUNCHHI  | BDEP     | 00000,// CKSM, 0
        BINPUNCHHI  | BDEP     | 00000,// NB, 	0
        BINPUNCHHI  | BDEP     | 00000,// IA, 	0
        BINPUNCHHI  | BDEP     | 00000,// FA, 	0
        BINPUNCHHI  | BDEP     | 07566,// M212, -212
        BINPUNCHHI  | BDEP     | 00000,// CTR1, 0
        BINPUNCHHI  | BDEP     | 00200,// C200, 200
        BINPUNCHHI  | BDEP     | 00000,// TEM1, 0
        BINPUNCHHI  | BDEP     | 00177,// SL7, 	177
        BINPUNCHHI  | BDEP     | 00077,// SL6,	77
        BINPUNCHHI  | BLA|BST  | 07465 //BINARY PUNCH HI SPEED START
    };


    /** Creates a new instance of XXX */
    public PowerBoot(BusRegMem data) {
        this.data = data;

        pwtimer = new Timer();
    } 
    
    class PWTask extends TimerTask {
        public void run() {
            System.out.println("Power failed");
            setpwb(true);
            }
    }

    public void Decode(int devcode, int opcode) {
        switch (opcode) {
                case SBE: break;
                case SPL: break;
                case CAL: break;
        }
    }

    public void Execute(int devcode, int opcode) {
        switch (opcode) {
                case SBE: break; //what battery?
                case SPL: data.skipbus = (pwfailflag | pwgone); break;
                case CAL: pwfailflag = false; clearIntReq(); break;
        }
    }

    private void setpwb(boolean set){
        if (set) {
            data.ClearAllFlags();
            data.ClearAllPower();
            pwgone = true;
        }
    }

     public void clearIntReq() {
            data.setIntReq(DevId10,false);
    }

    public void setIntReq() {
        if (pwfailflag == true) {
            data.setIntReq(DevId10,true);
        }
    }

   public void ClearFlags(int devcode) {
        pwfailflag = false;
        pwgone = false;
        data.setIntReq(DevId10,false);
    }

    public void Interrupt(int command) {
        if (command==PWDOWN & !pwgone) {
            pwfailflag=true;
            pwtimer.schedule(new PWTask(), PWDEL);
            setIntReq();
       }
        if (command==PWUP & pwgone) Bootit(command);
        else if (command>PWUP & !data.run) Bootit(command);
        if (command==PWDOWN) pwgone=true; else pwgone=false;
    }

    private void Bootit (int device) {
        if ((device>>16)>2) {
            Calendar today = Calendar.getInstance();
            int year = today.get(Calendar.YEAR)-1970; 
            data.mem.set(010000, 07666,  ((today.get(Calendar.MONTH)+1)<<8) +
                                    (today.get(Calendar.DAY_OF_MONTH)<<3) +
                                    (year % 8));  //set OS/8 Date
            data.mem.set(000000, 07777,  (year/8)<<7); //set BIPCCL
        }
        for (int entry : boottable) {
            if ((entry&0xF0000)==device) {
                if ( (entry&BLA) ==BLA)  BootLA(entry&07777);
                if ( (entry&BLXA)==BLXA) BootLXA(entry&07777);
                if ( (entry&BDEP)==BDEP) BootDEP(entry&07777);
                if ( (entry&BST) ==BST)  {
                    BootST();
                    if ((device>>16)>2) data.setProp("BootDev", "" + (device>>16));
                }
            }
        }
    }

    private void BootLA(int word) {
        data.state = FETCH;
        data.data = word;
        data.cpma = data.data;
        data.ma = data.cpma;
    }

    private void BootLXA(int word) {
        data.state = FETCH;
        data.ifr = (word & 070) >> 3;
        data.ema = data.ifr << 12;
        data.dfr = (word & 007);
        data.mmena = false; //switch off Multi8
    }

    private void BootDEP(int word) {
        data.ma = data.cpma;
        data.pc = (data.cpma + 1) & 07777;
        data.mb = word;
        data.mem.set(data.ema, data.ma, data.mb);
        //data.memory[data.ema | data.ma] = data.mb;
        data.cpma = data.pc;
        data.ma = data.cpma;
    }

    private void BootST() {
        data.ClearAllFlags();
        data.ClearAllPower();
        data.msirdis = false; //temp solution
        data.run = true;
    }

    public void ClearRun(boolean run) {
    }

    public void CloseDev(int devcode) {
    }
    
    public void ClearPower(int devcode) {
    }

}
