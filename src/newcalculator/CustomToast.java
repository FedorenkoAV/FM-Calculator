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
public class CustomToast {    
    
    public final static int IC_WARNING_AMBER = 0;
    public final static int IC_ERROR_RED_64PT = 1;
    
    private String argToPrint;
    private int imageOnToast = IC_WARNING_AMBER;
    
    CustomToast(Activity activity, String textOnToast) {   
        setToastText (textOnToast);
        
    }
    
    void show() {
        javax.swing.JOptionPane.showMessageDialog(null, argToPrint);
    }

    void setToastText(String newText) {
        argToPrint = newText;        
    }

    void setToastImage(int imageOnToast) {
        this.imageOnToast = imageOnToast; // устанавливаем на него картинку из ресурсов
    }
//
//    void setToastGravity (int toastGravity, int xOffset, int yOffset) {
//        toast.setGravity(toastGravity, xOffset, yOffset);
//    }
//
//    void setToastMargin (float horizontalMargin, float verticalMargin) {
//        toast.setMargin(horizontalMargin, verticalMargin);
//    }
    
}
