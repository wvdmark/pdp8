/*
 * PanelLogic.java
 *
 * Created on January 1, 2005, 0:00 PM
 */
/**
 *
 * @author  wvdmark@computer.org
 */
package Logic;

import java.awt.Color;

public class PanelLogic extends Thread implements Constants {

    /** Creates a new instance of PanelLogic
     * @param data
     * @param name */
    public PanelLogic(BusRegMem data, String name) {
        super(name);
        this.data = data;
        power = 1;
    }
    public BusRegMem data;
    private Panel.Panel8 panel;
    protected int select = 0;
    protected int power;
    protected boolean eal;
    protected boolean al;
    protected boolean clear;
    protected boolean cont;
    protected boolean exam;
    protected boolean dep;

    public void menuContext(int menu, boolean sel) {
        if (data.devices[PowerBoot.DevId10] != null) {
        switch (menu) {
            case 0: data.devices[SI3040.DevId50].Interrupt(SIUNIT0);
                    data.devices[PowerBoot.DevId10].Interrupt(SIBOOT); break;
            case 1: data.devices[SI3040.DevId50].Interrupt(SIUNIT1);
                    data.devices[PowerBoot.DevId10].Interrupt(SIBOOT); break;
            case 2: data.devices[PowerBoot.DevId10].Interrupt(TD8EBOOT); break;
            case 3: data.devices[PowerBoot.DevId10].Interrupt(BINLOADLO); break;
            case 4: data.devices[PowerBoot.DevId10].Interrupt(BINLOADHI); break;
            case 5: data.devices[PowerBoot.DevId10].Interrupt(RIMLOADLO); break;
            case 6: data.devices[PowerBoot.DevId10].Interrupt(RIMLOADHI); break;
            case 7: data.devices[PowerBoot.DevId10].Interrupt(BINPUNCHHI); break;
            }
        } 
        switch (menu) {
            case 8:  setSpeed(false); break;
            case 9:  setSpeed(true); break;
            case 10: setStyle(true); break;
            case 11: setStyle(false); break;
            case 12: setTimSha(sel); break;
            case 13: setFPPena(sel); break;
            }
    }

    public void setSpeed(boolean speed) {
        data.speed = speed;
    }

    public void setStyle(boolean style) {
        data.style = style;
    }

    public void setTimSha(boolean timsha) {
        data.timsha = timsha;
    }

    public void setFPPena(boolean fppena) {
        data.FPPenable = fppena;
    }

    public void setPanel(Panel.Panel8 panel) {
        this.panel = panel;
    }

    public void setSwitchReg(int value) {
        data.swr = value;
        //report("SwitchReg",value);
    }

    public void setSelect(int value) {
        select = value;
        report("Select", value);
    }

    public void setPower(int value) {
        power = value;
        report("Power", value);
        if (data.devices[PowerBoot.DevId10] != null) {
            data.devices[PowerBoot.DevId10].Interrupt(power<<16);
        }
        //if (power == 0) {
            //data.ClearAllFlags();
            //data.ClearAllPower();
        //}

    }

    public void setSw(boolean state) {
        data.sw = state;
        String bdev = data.getProp("BootDev");
        if (!data.run & bdev!=null) {
            if (state) {
                int boot = Integer.parseInt(bdev);
                if (data.devices[PowerBoot.DevId10] != null) data.devices[PowerBoot.DevId10].Interrupt(boot<<16);
            }
        }
        report("Sw", state);
    }

    public void setEal(boolean state) {
        if (!data.run) {
            if (state) {
                if (data.devices[ProcMemIOTs.DevId20]!=null)
                    data.devices[ProcMemIOTs.DevId20].ClearPower(ProcMemIOTs.DevId20);
                data.state = FETCH;
                data.data = data.swr&03377; //for KT else 00077
                data.ifr = (data.data & 00070) >> 3;
                data.ibr = (data.data & 03000) >> 9;
                //data.ema =  data.ifr << 12;
                data.ema = ((data.ibr<<3) + data.ifr) << 12;    
                data.dfr = (data.data & 00007);
                data.dbr = (data.data & 00300) >> 6;
                data.mmena = false; //switch off Multi8
            }
            eal = state;
            report("Eal", state);
        }
    }

    public void setAl(boolean state) {
        if (!data.run) {
            if (state) {
                data.state = FETCH;
                data.data = data.swr;
                data.cpma = data.data;
                data.ma = data.cpma;
            }
            al = state;
            report("Al", state);
        }
    }

    public void setClear(boolean state) {
        clear = state;
        if (!data.run) {
            if (state) {
                data.ac = 0;
                data.link = 0;
                data.ClearAllFlags();
            }
            report("Clear", state);
        }
    }

    public void setCont(boolean state) {
        cont = state;
        if (!data.run) {
            if (state) {
                data.msirdis = false; //temp solution
                data.run = true;
            }
            report("Cont", state);
        }
    }

    public void setExam(boolean state) {
        if (!data.run) {
            if (state) {
                data.msirdis = true;
                data.keyctrl = true;
                data.bkdctrl = true;
                data.malctrl = false;
                data.mddir = false;
                data.run = true;
            }
            exam = state;
            report("Exam", state);
        }
    }

    public void setHalt(boolean state) {
        data.halt = state;
        report("Halt", state);
    }

    public void setSingStep(boolean state) {
        data.singstep = state;
        report("SingStep", state);
    }

    public void setDep(boolean state) {
        if (!data.run) {
            if (state) {
                data.msirdis = true;
                data.keyctrl = true;
                data.bkdctrl = false;
                data.malctrl = false;
                data.mddir = true;
                data.data = data.swr;
                data.run = true;
            }
            dep = state;
            report("Dep", state);
        }
    }

    private void report(String name, boolean state) {
        //System.out.println(name+"->"+state);
    }

    private void report(String name, int value) {
        //System.out.println(name+"=>"+value);
    }

    private int makeStatus() {
        int status = 0;
        if (data.link == 1) {
            status |= 04000;
        }
        if (data.gtf > 0) {
            status |= 02000;
        }
        if (data.intreq > 0) {
            status |= 01000;
        }
        if (data.intinhibit) {
            status |= 00400;
        }
        if (data.intena) {
            status |= 00200;
        }
        if (data.usermode) {
            status |= 00100;
        }
        status |= (data.ifr << 3) & 00070;
        status |= (data.dfr) & 00007;
        return status;
    }

    private int makeState() {
        int status = 0;
        if (data.state == FETCH) {
            status |= 04000;
        }
        if (data.state == DEFER) {
            status |= 02000;
        }
        if (data.state == EXEC) {
            status |= 01000;
        }
        status |= (data.ir << 6) & 00700;
        if (!data.mddir) {
            status |= 00040;
        }
        if (data.bkdctrl) {
            status |= 00020;
        }
        if (data.sw) {
            status |= 00010;
        }
        if (data.iopause) {
            status |= 00004;
        }
        //break in prog                  00002
        //break cycle                    00001

        return status;
    }

    @Override
    public void run() {
        while (true) {
            if (data.FPPRunning==true) {
                panel.runLamp.setColor(Color.RED);
            } else {
                panel.runLamp.setColor(Color.YELLOW);
            }
            panel.runLamp.setState(data.run);
            //if (data.run & power==0) panel.knobPower.setValue((float) 0.5);
            if (power < 2) {
                switch (select) {
                    case 0:
                        panel.busLamps.setState(data.data);
                        break;
                    case 1:
                        panel.busLamps.setState(data.mq);
                        break;
                    case 2:
                        panel.busLamps.setState(data.md);
                        break;
                    case 3:
                        panel.busLamps.setState(data.ac);
                        break;
                    case 4:
                        panel.busLamps.setState(makeStatus());
                        break;
                    case 5:
                        panel.busLamps.setState(makeState());
                        break;
                }
                panel.maLamps.setState(data.ma);
                panel.xmaLamps.setState(data.ema >> 15);
                panel.emaLamps.setState(data.ema >> 12);
                if (!data.style) {
                    panel.stateLamps.setState(makeState());
                    panel.statusLamps.setState(makeStatus());
                    panel.acLamps.setState(data.ac);
                    panel.mdLamps.setState(data.md);
                    panel.mqLamps.setState(data.mq);
                    panel.dataLamps.setState(data.data);
                } else {
                    panel.stateLamps.setState(0);
                    panel.statusLamps.setState(0);
                    panel.acLamps.setState(0);
                    panel.mdLamps.setState(0);
                    panel.mqLamps.setState(0);
                    panel.dataLamps.setState(0);
                }
            } else {
                panel.busLamps.setState(0);
                panel.maLamps.setState(0);
                panel.xmaLamps.setState(0);
                panel.emaLamps.setState(0);
                panel.stateLamps.setState(0);
                panel.statusLamps.setState(0);
                panel.acLamps.setState(0);
                panel.mdLamps.setState(0);
                panel.mqLamps.setState(0);
                panel.dataLamps.setState(0);
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
            //System.out.println("Hello");
        }
    }

}
