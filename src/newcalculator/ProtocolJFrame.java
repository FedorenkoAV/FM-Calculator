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
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.commons.math3.complex.Complex;

class ProtocolJFrame extends javax.swing.JFrame {

    private static final String TAG = "ProtocolJFrame";

    public final int NOP2 = 0b000;
    public final int PLUS2 = 0b010;
    public final int MINUS2 = 0b011;
    public final int MULTIPLY2 = 0b100;
    public final int DIVIDE2 = 0b101;
    public final int POWER2 = 0b110;
    public final int X_SQR_Y2 = 0b111;

    private int xCoord = 0;
    private int yCoord = 0;
    private javax.swing.JTextPane protocolWindow;
    
    private Style bold = null; // стиль заголовка
    private Style normal = null; // стиль текста

    private final String STYLE_heading = "heading",
            STYLE_normal = "normal",
            FONT_style = "Monospaced";

    Font modeFont;

    MainActivity parent;
    
    ComplexFormat complexFormat;

    /**
     * Creates new form ProtocolJFrame
     */
    ProtocolJFrame(MainActivity parent) {
        this.parent = parent;

        initComponents();
        protocolWindow = this.jTextPaneLog;
        Properties properties = System.getProperties();
        complexFormat = new ComplexFormat();
        println("Java Runtime Environment version: " + properties.getProperty("java.version"));
        println("Java Runtime Environment vendor: " + properties.getProperty("java.vendor"));
        println("Java vendor URL: " + properties.getProperty("java.vendor.url"));
        println("Java installation directory: " + properties.getProperty("java.home"));
        println("Java Virtual Machine specification version: " + properties.getProperty("java.vm.specification.version"));
        println("Java Virtual Machine specification vendor: " + properties.getProperty("java.vm.specification.vendor"));
        println("Java Virtual Machine specification name: " + properties.getProperty("java.vm.specification.name"));
        println("Java Virtual Machine implementation version: " + properties.getProperty("java.vm.version"));
        println("Java Virtual Machine implementation vendor: " + properties.getProperty("java.vm.vendor"));
        println("Java Virtual Machine implementation name: " + properties.getProperty("java.vm.name"));
        println("Java Runtime Environment specification version: " + properties.getProperty("java.specification.version"));
        println("Java Runtime Environment specification vendor: " + properties.getProperty("java.specification.vendor"));
        println("Java Runtime Environment specification name: " + properties.getProperty("java.specification.name"));
        println("Java class format version number: " + properties.getProperty("java.class.version"));
        println("Java class path: " + properties.getProperty("java.class.path"));
        println("List of paths to search when loading libraries: " + properties.getProperty("java.library.path"));
        println("Default temp file path: " + properties.getProperty("java.io.tmpdir"));
        println("Name of JIT compiler to use: " + properties.getProperty("java.compiler"));
        println("Path of extension directory or directories: " + properties.getProperty("java.ext.dirs"));
        println("Operating system name: " + properties.getProperty("os.name"));
        println("Operating system architecture: " + properties.getProperty("os.arch"));
        println("Operating system version: " + properties.getProperty("os.version"));
        println("File separator: " + properties.getProperty("file.separator"));
        println("Path separator: " + properties.getProperty("path.separator"));
        println("Line separator: " + properties.getProperty("line.separator"));
        println("User's account name: " + properties.getProperty("user.name"));
        println("User's home directory: " + properties.getProperty("user.home"));
        println("User's current working directory: " + properties.getProperty("user.dir"));
        
        Map<String, String> map = System.getenv();
        map.entrySet().forEach(this::println);
//        Set set = map.entrySet();
//        set.
//        for (String string : set) {
//            System.out.println();
//        }
        
        
        
        
    }

//    ProtocolJFrame(int x, int y) {
//        xCoord = x;
//        yCoord = y;
//        initComponents();
//        protocolWindow = this.jTextArea2;
//    }
//
//    ProtocolJFrame(javax.swing.JTextArea protocolWin) {
//        initComponents();
//        protocolWindow = protocolWin;
//    }
//
//    ProtocolJFrame(int x, int y, javax.swing.JTextArea protocolWin) {
//        xCoord = x;
//        yCoord = y;
//        initComponents();
//        protocolWindow = protocolWin;
//    }
    private void cls() {
        protocolWindow.setText("");
    }

    void println() {
        appendText("\n");
    }

    final void println(String msg) {
        appendText(msg + "\n");
    }

    void println(int integerNum) {
        appendText(integerNum + "\n");
    }

    void println(double doubleNum) {
        appendText(doubleNum + "\n");
    }

    void println(Complex complex) {
        appendText("(" + complexFormat.format(complex) + ")\n");
    }
    
    void println (List cplxNumList) {        
        Complex complex = (Complex) cplxNumList.get(0);
        for (int i = 0; i < cplxNumList.size(); i++) {
            complex = (Complex) cplxNumList.get(i);
            appendText("\n(" + complexFormat.format(complex) + ")");
        }        
        appendText("\n");
    }

    void println(Object someObj) {
        String strToPrint = someObj.toString();
        appendText(strToPrint + "\n");
    }

    void print() {
        appendText("");
    }

    void print(String msg) {
        //protocol.jTextArea1.append(msg); 
        appendText(msg);
    }

    void print(int integerNum) {
        appendText(integerNum + "");
    }

    void print(double doubleNum) {
        appendText(doubleNum + "");
    }

    void print(Complex complex) {
        appendText("(" + complexFormat.format(complex) + ")");        
    }
    
    void print (List cplxNumList) {
        Complex complex = (Complex) cplxNumList.get(0);
        for (int i = 0; i < cplxNumList.size(); i++) {
            complex = (Complex) cplxNumList.get(i);
            appendText("\n+(" + complexFormat.format(complex) + ")");
        }

    }

    void print(Object someObj) {
        String strToPrint = someObj.toString();
        appendText(strToPrint);
    }

    void setMemory(double numberInMemory) {
        jTextMemDisp.setText(String.valueOf(numberInMemory));
    }

    void setSecondOp(double numberInSecondOp) {
        jTextFieldSecondOp.setText(String.valueOf(numberInSecondOp));
    }

    void setA(double numberInA) {
        jTextFieldADisp.setText(String.valueOf(numberInA));
    }

    void setB(double numberInB) {
        jTextFieldBDisp.setText(String.valueOf(numberInB));
    }

    void toCplxMode() {
        jLabelADisp.setText("Re:");
        jLabelBDisp.setText("Im:");
    }

    void fromCplxMode() {
        jLabelADisp.setText("A:");
        jLabelBDisp.setText("B:");
    }

    void setAutoConstant(int autoConstantOperation, double number) {
        String autoConstantString = "";
        switch (autoConstantOperation) {
            case PLUS2:
                autoConstantString = "+";
                break;
            case MINUS2:
                autoConstantString = "-";
                break;
            case MULTIPLY2:
                autoConstantString = "×";
                break;
            case DIVIDE2:
                autoConstantString = "÷";
                break;
            case POWER2:
                autoConstantString = "^";
                break;
            case X_SQR_Y2:
                autoConstantString = "√";
                break;
            case NOP2:
                break;
        }
        //protocol.println ("autoConstantString = " + autoConstantString);
        //protocol.println ("autoConstantOperation2 = " + autoConstantOperation2);
        autoConstantString = autoConstantString.concat(String.valueOf(number));
        jTextFieldYDisp.setText(autoConstantString);
    }
    
    private void appendText(String str) {

        insertText(jTextPaneLog, str, normal);

    }

    private void appendBoldText(String str) {
        insertText(jTextPaneLog, str + "\r\n" + jTextPaneLog.getContentType() + "\r\n", bold);
    }

    /**
     * Процедура формирования стилей редактора
     *
     * @param editor редактор
     */
    private void createStyles(JTextPane editor) {
        // Создание стилей
        normal = editor.addStyle(STYLE_normal, null);
        StyleConstants.setFontFamily(normal, FONT_style);
        StyleConstants.setFontSize(normal, 12);
        // Наследуем свойстdо FontFamily
        bold = editor.addStyle(STYLE_heading, normal);
//        StyleConstants.setFontSize(heading, 12);
        StyleConstants.setBold(bold, true);
        StyleConstants.setForeground(bold, Color.red);
    }

    /**
     * Процедура добавления в редактор строки определенного стиля
     *
     * @param editor редактор
     * @param string строка
     * @param style стиль
     */
    private void insertText(JTextPane editor, String string, Style style) {
        try {
            Document doc = editor.getDocument();
            doc.insertString(doc.getLength(), string, style);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelProtocol = new javax.swing.JPanel();
        jLabelMemDisp = new javax.swing.JLabel();
        jTextMemDisp = new javax.swing.JTextField();
        jLabelSecondOp = new javax.swing.JLabel();
        jTextFieldSecondOp = new javax.swing.JTextField();
        jLabelADisp = new javax.swing.JLabel();
        jTextFieldADisp = new javax.swing.JTextField();
        jLabelBDisp = new javax.swing.JLabel();
        jTextFieldBDisp = new javax.swing.JTextField();
        jLabelYDisp = new javax.swing.JLabel();
        jTextFieldYDisp = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPaneLog = new javax.swing.JTextPane();
        jButton10 = new javax.swing.JButton();

        setTitle("Протокол");
        setAutoRequestFocus(false);
        setFocusable(false);
        setMinimumSize(new java.awt.Dimension(645, 310));
        setPreferredSize(new java.awt.Dimension(850, 700));
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanelProtocol.setBackground(new java.awt.Color(55, 54, 59));
        jPanelProtocol.setFocusable(false);
        jPanelProtocol.setPreferredSize(new java.awt.Dimension(850, 668));

        jLabelMemDisp.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelMemDisp.setForeground(new java.awt.Color(255, 255, 255));
        jLabelMemDisp.setText("Содержимое памяти:");
        jLabelMemDisp.setFocusable(false);

        jTextMemDisp.setEditable(false);
        jTextMemDisp.setBackground(Color.LIGHT_GRAY);

        jLabelSecondOp.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelSecondOp.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSecondOp.setText("Второй операнд:");
        jLabelSecondOp.setFocusable(false);

        jTextFieldSecondOp.setBackground(Color.LIGHT_GRAY);

        jLabelADisp.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelADisp.setForeground(new java.awt.Color(255, 255, 255));
        jLabelADisp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelADisp.setText("A:");
        jLabelADisp.setFocusable(false);

        jTextFieldADisp.setBackground(Color.LIGHT_GRAY);

        jLabelBDisp.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelBDisp.setForeground(new java.awt.Color(255, 255, 255));
        jLabelBDisp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelBDisp.setText("B:");
        jLabelBDisp.setFocusable(false);

        jTextFieldBDisp.setBackground(Color.LIGHT_GRAY);

        jLabelYDisp.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelYDisp.setForeground(new java.awt.Color(255, 255, 255));
        jLabelYDisp.setText("Автокостанта:");
        jLabelYDisp.setFocusable(false);

        jTextFieldYDisp.setBackground(Color.LIGHT_GRAY);

        jPanel3.setBackground(new java.awt.Color(29, 29, 31));
        jPanel3.setToolTipText("");
        jPanel3.setFocusable(false);

        new SmartScroller(jScrollPane1);
        jScrollPane1.setViewportView(jTextPaneLog);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
        );

        jButton10.setBackground(java.awt.Color.black);
        jButton10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Очистить");
        jButton10.setFocusable(false);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelProtocolLayout = new javax.swing.GroupLayout(jPanelProtocol);
        jPanelProtocol.setLayout(jPanelProtocolLayout);
        jPanelProtocolLayout.setHorizontalGroup(
            jPanelProtocolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProtocolLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelProtocolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelProtocolLayout.createSequentialGroup()
                        .addGroup(jPanelProtocolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelProtocolLayout.createSequentialGroup()
                                .addGroup(jPanelProtocolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextMemDisp)
                                    .addComponent(jLabelMemDisp, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelProtocolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldSecondOp)
                                    .addComponent(jLabelSecondOp, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelProtocolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldADisp)
                                    .addComponent(jLabelADisp, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelProtocolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldBDisp)
                                    .addComponent(jLabelBDisp, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 212, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanelProtocolLayout.createSequentialGroup()
                        .addGroup(jPanelProtocolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelYDisp, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldYDisp, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelProtocolLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jButton10)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanelProtocolLayout.setVerticalGroup(
            jPanelProtocolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProtocolLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelProtocolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelProtocolLayout.createSequentialGroup()
                        .addGroup(jPanelProtocolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelMemDisp)
                            .addComponent(jLabelADisp)
                            .addComponent(jLabelBDisp))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelProtocolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextMemDisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldADisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldBDisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelProtocolLayout.createSequentialGroup()
                        .addComponent(jLabelSecondOp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldSecondOp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelYDisp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldYDisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10)
                .addContainerGap())
        );

        getContentPane().add(jPanelProtocol, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        cls();

    }//GEN-LAST:event_jButton10ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        parent.jCheckBoxMenuItemProtocol.setSelected(false);

    }//GEN-LAST:event_formWindowClosing

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ProtocolJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ProtocolJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ProtocolJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ProtocolJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ProtocolJFrame().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10;
    private javax.swing.JLabel jLabelADisp;
    private javax.swing.JLabel jLabelBDisp;
    private javax.swing.JLabel jLabelMemDisp;
    private javax.swing.JLabel jLabelSecondOp;
    private javax.swing.JLabel jLabelYDisp;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelProtocol;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldADisp;
    private javax.swing.JTextField jTextFieldBDisp;
    private javax.swing.JTextField jTextFieldSecondOp;
    private javax.swing.JTextField jTextFieldYDisp;
    private javax.swing.JTextField jTextMemDisp;
    private javax.swing.JTextPane jTextPaneLog;
    // End of variables declaration//GEN-END:variables
}
