/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcalculator;

import javax.swing.AbstractButton;


/**
 *
 * @author User
 */
class Status {
    private boolean shift = false;
    private boolean hyp = false;
    private boolean bracket = false;
    private boolean memory = false;
    private boolean error = false;
    private StatusDisplay statusDisplay;
    AbstractButton btnStore[];

    Status(StatusDisplay statusDisplay, AbstractButton btnStore[]) {
        this.statusDisplay = statusDisplay;
        this.btnStore = btnStore;

    }

    boolean isShift() {
        return shift;
    }

    void switchShift() {
        shift = !shift;
        if (shift) {
            statusDisplay.onShift();
        } else {
            statusDisplay.offShift();
        }
    }

    public void onShift() {
        shift = true;
        statusDisplay.onShift();
    }

    void offShift() {
        shift = false;
        statusDisplay.offShift();
    }

    boolean isHyp() {
        return hyp;
    }

    void switchHyp() {
        hyp = !hyp;
        if (hyp) {
            statusDisplay.onHyp();
        } else {
            statusDisplay.offHyp();
        }
    }

    void onHyp() {
        hyp = true;
        statusDisplay.onHyp();
    }

    void offHyp() {
        hyp = false;
        statusDisplay.offHyp();
    }


    public boolean isBracket() {
        return bracket;
    }

    public void setBracket(boolean bracket) {
        this.bracket = bracket;
        if (bracket) {
            statusDisplay.onBracket();
        } else {
            statusDisplay.offBracket();
        }
    }

    void onMemory() {
        memory = true;
        statusDisplay.onMemory();
    }

    void offMemory() {
        memory = false;
        statusDisplay.offMemory();
    }

    void onError() {
        error = true;
        statusDisplay.onError();
        for (AbstractButton btn : btnStore) {
            btn.setEnabled(false);
        }
        btnStore[4].setEnabled(true);
        btnStore[5].setEnabled(true);
    }

    void offError() {
        error = false;
        statusDisplay.offError();
        for (AbstractButton btn : btnStore) {
            btn.setEnabled(true);
        }
    }
}
