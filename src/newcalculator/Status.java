/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcalculator;

/**
 *
 * @author User
 */
public class Status {
    private boolean shift = false;
    private boolean hyp = false;
    private boolean bracket = false;
    private boolean memory = false;
    private boolean error = false;
    private StatusDisplay statusDisplay;

    Status(StatusDisplay statusDisplay) {
        this.statusDisplay = statusDisplay;

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
    }

    void offError() {
        error = false;
        statusDisplay.offError();
    }
}
