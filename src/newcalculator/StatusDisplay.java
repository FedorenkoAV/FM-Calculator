/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcalculator;


import java.awt.Color;
import javax.swing.JLabel;

/**
 *
 * @author User
 */
public class StatusDisplay {
    public final int SHIFT = 0;
    public final int HYP = 1;
    public final int DEG = 2;
    public final int RAD = 3;
    public final int GRAD = 4;
    public final int BRCKET = 5;
    public final int BIN = 6;
    public final int OCT = 7;
    public final int HEX = 8;
    public final int CPLX = 9;
    public final int SD = 10;
    public final int MEMORY = 11;
    public final int ERROR = 12;
    
    Color lcdBGColor = Color.LIGHT_GRAY ;
    Color lcdFontColor = Color.BLACK ;

    private JLabel shift;
    private JLabel hyp;
    private JLabel deg;
    private JLabel rad;
    private JLabel grad;
    private JLabel bracket;
    private JLabel bin;
    private JLabel oct;
    private JLabel hex;
    private JLabel cplx;
    private JLabel sd;
    private JLabel memory;
    private JLabel error;
    int backgroundColor;
    int fontColor;
    


//    StatusDisplay(JLabel shift, JLabel hyp, JLabel deg, JLabel rad, JLabel grad, JLabel bracket, JLabel bin, JLabel oct, JLabel hex, JLabel cplx, JLabel sd) {
//
//        this.shift = shift;
//        this.hyp = hyp;
//        this.deg = deg;
//        this.rad = rad;
//        this.grad = grad;
//        this.bracket = bracket;
//        this.bin = bin;
//        this.oct = oct;
//        this.hex = hex;
//        this.cplx = cplx;
//        this.sd = sd;
////        backgroundColor = ContextCompat.getColor(context, R.color.colorLCDBackground);
////        fontColor = ContextCompat.getColor(context, R.color.colorLCDFont);
//        offShift();
//        offHyp();
//        offDeg();
//        offRad();
//        offGrad();
//        offBracket();
//        offBin();
//        offOct();
//        offHex();
//        offCplx();
//        offSd();
//    }

    StatusDisplay(JLabel statusDisplayLabStore[]) {

        this.shift = statusDisplayLabStore[SHIFT];
        this.hyp = statusDisplayLabStore[HYP];
        this.deg = statusDisplayLabStore[DEG];
        this.rad = statusDisplayLabStore[RAD];
        this.grad = statusDisplayLabStore[GRAD];
        this.bracket = statusDisplayLabStore[BRCKET];
        this.bin = statusDisplayLabStore[BIN];
        this.oct = statusDisplayLabStore[OCT];
        this.hex = statusDisplayLabStore[HEX];;
        this.cplx = statusDisplayLabStore[CPLX];
        this.sd = statusDisplayLabStore[SD];
        this.memory = statusDisplayLabStore[MEMORY];
        this.error = statusDisplayLabStore[ERROR];
//        backgroundColor = ContextCompat.getColor(context, R.color.colorLCDBackground);
//        fontColor = ContextCompat.getColor(context, R.color.colorLCDFont);
        offShift();
        offHyp();
        offDeg();
        offRad();
        offGrad();
        offBracket();
        offBin();
        offOct();
        offHex();
        offCplx();
        offSd();
    }

    void onShift() {
        shift.setForeground(lcdFontColor);        
    }

    void offShift() {
        shift.setForeground(lcdBGColor);        
    }

    void onHyp() {
        hyp.setForeground(lcdFontColor);
    }

    void offHyp() {
        hyp.setForeground(lcdBGColor);
    }

    void onDeg() {
        deg.setForeground(lcdFontColor);
    }

    void offDeg() {
        deg.setForeground(lcdBGColor);
    }

    void onRad() {
        rad.setForeground(lcdFontColor);
    }

    void offRad() {
        rad.setForeground(lcdBGColor);
    }

    void onGrad() {
        grad.setForeground(lcdFontColor);
    }

    void offGrad() {
        grad.setForeground(lcdBGColor);
    }

    void onBracket() {
        bracket.setForeground(lcdFontColor);
    }

    void offBracket() {
        bracket.setForeground(lcdBGColor);
    }

    void onBin() {
        bin.setForeground(lcdFontColor);
    }

    void offBin() {
        bin.setForeground(lcdBGColor);
    }

    void onOct() {
        oct.setForeground(lcdFontColor);
    }

    void offOct() {
        oct.setForeground(lcdBGColor);
    }

    void onHex() {
        hex.setForeground(lcdFontColor);
    }

    void offHex() {
        hex.setForeground(lcdBGColor);
    }

    void onCplx() {
        cplx.setForeground(lcdFontColor);
    }

    void offCplx() {
        cplx.setForeground(lcdBGColor);
    }

    void onSd() {
        sd.setForeground(lcdFontColor);
    }

    void offSd() {
        sd.setForeground(lcdBGColor);
    }

    void onMemory() {
        memory.setForeground(lcdFontColor);
    }

    void offMemory() {
        memory.setForeground(lcdBGColor);
    }

    void onError() {
        error.setForeground(lcdFontColor);
    }

    void offError() {
        error.setForeground(lcdBGColor);
    }
}
