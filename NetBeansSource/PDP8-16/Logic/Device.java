/*
 * Device.java
 *
 * Created on January 1, 2005, 0:00 PM
 */

/**
 *
 * @author  wvdmark@computer.org
 */

package Logic;

public interface Device {
    
    
    public void Decode(int devcode, int opcode);
    public void Execute(int devcode, int opcode);
    public void ClearFlags(int devcode);
    public void ClearRun(boolean run);
    public void CloseDev(int devcode);
    public void ClearPower(int devcode);
    
    public void Interrupt(int command);
    public static final int INTINPROG = 1;
    public static final int JMPJMS    = 2;
    public static final int USERMODE  = 3;
    public static final int MAPIFR    = 4;
    public static final int MAPDFR    = 5;
}
