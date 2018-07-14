/*
 * Example
 *
 * Created on January 1, 2007, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */


package Logic;


public class Example implements Device, Constants {
    
    public static int DevId44 = 044; /* 44 just for fun */
    
    public BusRegMem data;
    
    private VirTimer xxxtim;
    private boolean flag;
    private boolean intena;
    
    
    /** Creates a new instance of XXX */
    public Example(BusRegMem data) {
        this.data = data;
        
        VirListener xxx = new VirListener() {
            public void actionPerformed() {
                System.out.println("XXX");
                setXXX(true);
            }
        };
        xxxtim = new VirTimer(data.virqueue,UTSDEL,xxx);
        xxxtim.setRepeats(false);
    }
    
    public void Decode(int devcode, int opcode) {
        switch (opcode) {
        }
    }
    
    public void Execute(int devcode, int opcode) {
        switch (opcode) {
        }
    }
    
    private void setCommand(int xdata) {
    }
    
    private int getCommand() {
        return 0;
    }
    
    private void setXXX(boolean set){
    }
    
     public void clearIntReq() {
        if (flag == false) {
            data.setIntReq(DevId44,false);
        }
    }
    
    public void setIntReq() {
        if (flag == true) {
            data.setIntReq(DevId44,intena);
        }
    }
    
   public void ClearFlags(int devcode) {
        data.setIntReq(DevId44,false);
    }
    
    public void Interrupt(int command) {
    }
    
    public void ClearRun(boolean run) {
    }
    
    public void CloseDev(int devcode) {
    }
    
    public void ClearPower(int devcode) {
    }
    
}
