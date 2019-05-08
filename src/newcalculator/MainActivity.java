/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcalculator;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author User
 */
public class MainActivity extends javax.swing.JFrame {

    private static final String TAG = "MainActivity";

    public final static int TOAST = 0;
    public final static int STATUS_DISPLAY = 1;
    public final static int STATUS = 2;
    public final static int MODE = 3;
    public final static int ANGLE = 4;
    public final static int MEMORY = 5;
    public final static int MAIN_DISPLAY = 6;
    public final static int STACK_CALCULATOR = 7;
    public final static int EDIT_X = 8;
    public final static int CPLX = 9;
    public final static int SD = 10;
    public final static int INPUT_DRIVER = 11;
    public final static int MAIN_ACTIVITY = 12;
    public final static int PREFERENCES = 13;
    public final static int EDIT_X_BIN = 14;
    public final static int EDIT_X_OCT = 15;
    public final static int EDIT_X_HEX = 16;
    public final static int PROTOCOL = 17;

    private Mode mode;

    Font sevenSegmentFont1;
    Font sevenSegmentFont2;
    Font sevenSegmentFont3;
    Font modeFont;
    Font memoryFont;
    Font errorFont;

    Color lcdBGColor = Color.LIGHT_GRAY;
    Color lcdFontColor = Color.BLACK;

    public final int FRAME_HEIGHT = 700;
    public final int FRAME_WIDTH = 328;

    public static final Font DEFAULT_FONT = new Font("Tahoma", 0, 36);
    public static final Font SMALL_FONT = new Font("Tahoma", 0, 24);
    public static final Font SMALLEST_FONT = new Font("Tahoma", 0, 18);
    public static final Font MODE_FONT = new Font("Tahoma", 0, 8);

    int xCoord = 0;
    int yCoord = 0;
    Point startPoint;

    ArrayList<Image> imageList;
    ProtocolJFrame protocol;
    CustomToast customToast;
    Angle angle;
    StackCalculator stackCalculator;
    MainDisplay mainDisplay;
    EditX editX;

    Status status;
    MemoryStore memoryStore;
    InputDriver inputDriver;

    Object objStore[] = new Object[18];

    JTextField display;

    Preferences preferences;

    // ЭТО КОНСТРУКТОР!!!
    /**
     * Creates new form NewJFrame
     */
    public MainActivity() {
        System.out.println("Это релизная версия. Логов не будет.");
        Log.d(TAG, "Начнем");        
        modeFont = MODE_FONT;

        // Теперь создаем рабочие шрифты из тех, что лежат в ресурсах
        // Если нужного шрифта нет в ресурсах, то выводим сообщение об ошибке и
        // Продолжаем работать на системных шрифтах    
        try {
            modeFont = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("fonts/tahoma.ttf")).deriveFont(Font.PLAIN, 8);

            memoryFont = modeFont.deriveFont(Font.BOLD + Font.ITALIC, 16);

            errorFont = modeFont.deriveFont(Font.BOLD + Font.ITALIC, 18);
        } catch (FileNotFoundException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        } catch (FontFormatException | IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }

        Image img1 = new ImageIcon(this.getClass().getResource("icons/Иконка.png")).getImage();
        Image img2 = new ImageIcon(this.getClass().getResource("icons/Иконка32.png")).getImage();
        Image img3 = new ImageIcon(this.getClass().getResource("icons/Иконка48.png")).getImage();
        Image img4 = new ImageIcon(this.getClass().getResource("icons/Иконка256.png")).getImage();

        imageList = new ArrayList<>();
        imageList.add(img1);
        imageList.add(img2);
        imageList.add(img3);
        imageList.add(img4);

        //          Создаем объект Preferences который будет читать и записывать настройки в файл
        preferences = new Preferences();
        Log.d(TAG, "Создали объект preferences, который будет читать и записывать настройки в файл." + preferences);
        objStore[PREFERENCES] = preferences;
        xCoord = preferences.getXCoord();
        int screenSizeWidth = getScreenSizeWidth();
        int screenSizeHeight = getScreenSizeHeight();
        Log.d(TAG, "screenSizeHeight = " + screenSizeHeight);
        Log.d(TAG, "screenSizeWidth = " + screenSizeWidth);
        if (xCoord >= screenSizeWidth) {
            xCoord = getCentrWidth();
            Log.d(TAG, "Координата X выходит за гницы экрана, меняем ее на центр экрана: " + xCoord);
        }
        yCoord = preferences.getYCoord();
        if (yCoord >= screenSizeHeight) {
            yCoord = getCentrHeight();
            Log.d(TAG, "Координата Y выходит за гницы экрана, меняем ее на центр экрана: " + yCoord);
        }
        startPoint = new Point(xCoord, yCoord);
        Log.d(TAG, "Стартовая точка: " + startPoint);

        initComponents(); // Рисуем все

//        Activity activity = MainActivity.this;
        Activity activity = new Activity();
        Log.d(TAG, "Activity activity = MainActivity.this создана.");

        objStore[MAIN_ACTIVITY] = activity;

        //        Создаем свой тост 
        customToast = new CustomToast(activity, "В разработке");

        objStore[TOAST] = customToast;
        Log.d(TAG, "Создали свой тост");

        protocol = new ProtocolJFrame(this);
        objStore[PROTOCOL] = protocol;
        protocol.pack();
        protocol.setLocationRelativeTo(null);
//        protocol.setVisible(true);

        //        Создаем объект statusDisplay, который будет управлять отображением меток статуса и режима работы на дисплее
        JLabel statusDisplayLabStore[] = {jLabelShift, jLabelHyp, jLabelDeg, jLabelRad, jLabelGrad, jLabelOpenBracket, jLabelBin, jLabelOct, jLabelHex, jLabelCplx, jLabelSD, jLabelMemory, jLabelError};
        StatusDisplay statusDisplay = new StatusDisplay(statusDisplayLabStore);
        objStore[STATUS_DISPLAY] = statusDisplay;

        Log.d(TAG, "Создали объект statusDisplay, который будет управлять отображением меток статуса и режима работы на дисплее");

//        Создаем объект status, который будет менять статусы и режимы работы
        status = new Status(statusDisplay);
        Log.d(TAG, "Создали объект status, который будет менять статусы и режимы работы" + status);
        objStore[STATUS] = status;
//        Создаем объект mode, который будет менять режим работы на CPLX и SD
        mode = new Mode(statusDisplay);
        Log.d(TAG, "Создали объект mode, который будет менять режим работы на CPLX и SD");
        objStore[MODE] = mode;

//        Создаем объект angle, который будет менять удуницы измерения углов
        angle = new Angle(preferences, statusDisplay);
        Log.d(TAG, "Создали объект angle, который будет менять удуницы измерения углов");
        objStore[ANGLE] = angle;

//        Создаем объект memoryStore, который будет работать с памятью
        memoryStore = new MemoryStore(preferences, status, protocol);
        Log.d(TAG, "Создали объект memoryStore, который будет работать с памятью");
        objStore[MEMORY] = memoryStore;

//        Создаем объект mainDisplay, который будет выводить цифровую информацию
        mainDisplay = new MainDisplay(jTextField1, preferences);
        Log.d(TAG, "Создали объект mainDisplay, который будет выводить цифровую информацию");
        objStore[MAIN_DISPLAY] = mainDisplay;

//        Создаем объект stackCalculator, который будет выполнять все вычисления
        stackCalculator = new StackCalculator(angle, protocol);
        Log.d(TAG, "Создали объект stackCalculator, который будет выполнять все вычисления");
        objStore[STACK_CALCULATOR] = stackCalculator;

//        Создаем объект editX, который будет отвечать за ввод всего
        editX = new EditX(objStore);
        Log.d(TAG, "Создали объект editX, который будет отвечать за ввод всего");
        objStore[EDIT_X] = editX;

//        Создаем объект - обработчик нажатия кнопок и вешаем его на кнопки
        inputDriver = new InputDriver(objStore); //инициализируем clickListener
        Log.d(TAG, "Создали объект - обработчик нажатия кнопок и вешаем его на кнопки");
        objStore[INPUT_DRIVER] = inputDriver;
        for (int i = 0; i < objStore.length; i++) {

            Log.d(TAG, "В objStore[" + i + "]: " + objStore[i]);
        }

        jButtonOnC.doClick(); // Нажимаем кнопку OnC        
    }

    void pasteFromClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable clipData = clipboard.getContents(null);
        String s = "";

        if (clipData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                s = (String) clipData.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException ex) {
                customToast.setToastText("Ошибка вставки из буфера обмена.");
                customToast.show();
                return;
            }
        }
        inputDriver.pasteFromClipboard(s);
//        customToast.setToastText("Число вставлено из буфера обмена");
//        customToast.show();
    }

    void copyToClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection clipData;
        String sb = inputDriver.copyToClipboard().toString();
        Log.d(TAG, "В буфер обмена отправляю: " + sb);
        protocol.println(TAG + "MainActivity. В буфер обмена отправляю: " + sb);
        clipData = new StringSelection(sb);
        clipboard.setContents(clipData, clipData);
//        customToast.setToastText("Число скопировано в буфер обмена");
//        customToast.show();
    }

    private int getScreenSizeHeight() {
        int screenSizeHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        // Определяем высоту панели задач
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;
        //protocol.println ("Высота панели задач: " + taskBarSize);
        screenSizeHeight = screenSizeHeight - taskBarSize;
        return screenSizeHeight;
    }

    private int getScreenSizeWidth() {
        int screenSizeWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        return screenSizeWidth;
    }

    private int getCentrHeight() {
        int screenSizeHeight = getScreenSizeHeight();
        //int frameSizeHeight = frame.getPreferredSize().height;        
        //protocol.println ("Высота фрейма: " + frameSizeHeight);
        if (screenSizeHeight < FRAME_HEIGHT) {
            return 0;
        }
        return (screenSizeHeight - FRAME_HEIGHT) / 2;
    }

    private int getCentrWidth() {
        int screenSizeWidth = getScreenSizeWidth();
        //int frameSizeWidth = frame.getPreferredSize().width;
        //protocol.println ("Ширина фрейма: " + frameSizeWidth);
        if (screenSizeWidth < FRAME_WIDTH) {
            return 0;
        }
        return (screenSizeWidth - FRAME_WIDTH) / 2;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jPanelMain = new javax.swing.JPanel();
        jPanelDisplay = new javax.swing.JPanel();
        jLabelMemory = new javax.swing.JLabel();
        jLabelError = new javax.swing.JLabel();
        jLabelShift = new javax.swing.JLabel();
        jLabelHyp = new javax.swing.JLabel();
        jLabelDeg = new javax.swing.JLabel();
        jLabelRad = new javax.swing.JLabel();
        jLabelGrad = new javax.swing.JLabel();
        jLabelOpenBracket = new javax.swing.JLabel();
        jLabelBin = new javax.swing.JLabel();
        jLabelOct = new javax.swing.JLabel();
        jLabelHex = new javax.swing.JLabel();
        jLabelCplx = new javax.swing.JLabel();
        jLabelSD = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButtonB = new javax.swing.JButton();
        jButtonXtoM = new javax.swing.JButton();
        jButtonDel = new javax.swing.JButton();
        jButtonDrg = new javax.swing.JButton();
        jButtonA = new javax.swing.JButton();
        jButtonCloseBracket = new javax.swing.JButton();
        jButtonTan = new javax.swing.JButton();
        jButtonX2 = new javax.swing.JButton();
        jButtonOpenBracket = new javax.swing.JButton();
        jButtonCos = new javax.swing.JButton();
        jButtonLog = new javax.swing.JButton();
        jButtonSQR = new javax.swing.JButton();
        jButtonSin = new javax.swing.JButton();
        jButtonOnC = new javax.swing.JButton();
        jToggleButtonShift = new javax.swing.JToggleButton();
        jButtonSci = new javax.swing.JButton();
        jButtonExp = new javax.swing.JButton();
        jButtonDegConv = new javax.swing.JButton();
        jButtonXPowY = new javax.swing.JButton();
        jButtonLn = new javax.swing.JButton();
        jButtonOff = new javax.swing.JButton();
        jToggleButtonHyp = new javax.swing.JToggleButton();
        jLabelLine = new javax.swing.JLabel();
        jLabelLine2 = new javax.swing.JLabel();
        jLabelLine3 = new javax.swing.JLabel();
        jLabelLine4 = new javax.swing.JLabel();
        jLabel1DivX = new javax.swing.JLabel();
        jLabelRtoP = new javax.swing.JLabel();
        jLabelPtoR = new javax.swing.JLabel();
        jLabelXtoY = new javax.swing.JLabel();
        jLabelNEX = new javax.swing.JLabel();
        jLabelXEX = new javax.swing.JLabel();
        jLabelPI = new javax.swing.JLabel();
        jLabelXSqrY = new javax.swing.JLabel();
        jLabelCbrt = new javax.swing.JLabel();
        jLabelToDeg = new javax.swing.JLabel();
        jLabelEX = new javax.swing.JLabel();
        jLabel10PowX = new javax.swing.JLabel();
        jLabelCPLX = new javax.swing.JLabel();
        jLabelFix = new javax.swing.JLabel();
        jLabelDrgTo = new javax.swing.JLabel();
        jLabelArcSin = new javax.swing.JLabel();
        jLabelArcCos = new javax.swing.JLabel();
        jLabelArcTan = new javax.swing.JLabel();
        jLabelArcHyp = new javax.swing.JLabel();
        jLabelSdKey = new javax.swing.JLabel();
        jPanelKeys2 = new javax.swing.JPanel();
        jButton0 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButtonCE = new javax.swing.JButton();
        jButtonMR = new javax.swing.JButton();
        jButtonCalc = new javax.swing.JButton();
        jButtonMPlus = new javax.swing.JButton();
        jButtonMult = new javax.swing.JButton();
        jButtonDiv = new javax.swing.JButton();
        jButtonPlus = new javax.swing.JButton();
        jButtonDot = new javax.swing.JButton();
        jButtonSign = new javax.swing.JButton();
        jButtonMinus = new javax.swing.JButton();
        jLabelRnd = new javax.swing.JLabel();
        jLabelPercent = new javax.swing.JLabel();
        jLabelFactorial = new javax.swing.JLabel();
        jLabelDecKey = new javax.swing.JLabel();
        jLabelHexKey = new javax.swing.JLabel();
        jLabelOctKey = new javax.swing.JLabel();
        jLabelBinKey = new javax.swing.JLabel();
        jLabelDataDel = new javax.swing.JLabel();
        jLabelBiKvOtkl = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jMenuItemPaste = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jCheckBoxMenuItemProtocol = new javax.swing.JCheckBoxMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItemAbout = new javax.swing.JMenuItem();

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Копировать");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem5);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("Вставить");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem6);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        jMenuItem7.setText("Очистить");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem7);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("-=FM=- calculator");
        setBackground(new java.awt.Color(50, 50, 58));
        setIconImages(imageList);
        setLocation(startPoint);
        setMinimumSize(new java.awt.Dimension(346, 700));
        setPreferredSize(new java.awt.Dimension(346, 500));
        setResizable(false);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                formWindowDeiconified(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelMain.setBackground(new java.awt.Color(55, 54, 59));
        jPanelMain.setFocusable(false);
        jPanelMain.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jPanelMainComponentShown(evt);
            }
        });
        jPanelMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelDisplay.setBackground(new java.awt.Color(29, 29, 31));
        jPanelDisplay.setFocusable(false);
        jPanelDisplay.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelMemory.setBackground(new java.awt.Color(218, 220, 219));
        jLabelMemory.setFont(memoryFont);
        jLabelMemory.setForeground(lcdBGColor);
        jLabelMemory.setText("M");
        jLabelMemory.setFocusable(false);
        jLabelMemory.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanelDisplay.add(jLabelMemory, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 50, 22, 20));

        jLabelError.setFont(errorFont);
        jLabelError.setForeground(lcdBGColor);
        jLabelError.setText("E");
        jLabelError.setFocusable(false);
        jLabelError.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabelError.setPreferredSize(new java.awt.Dimension(8, 14));
        jPanelDisplay.add(jLabelError, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 80, 22, 20));

        jLabelShift.setBackground(new java.awt.Color(218, 220, 219));
        jLabelShift.setFont(modeFont);
        jLabelShift.setForeground(lcdBGColor);
        jLabelShift.setText("SHIFT");
        jLabelShift.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabelShift.setFocusable(false);
        jPanelDisplay.add(jLabelShift, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 40, -1, -1));

        jLabelHyp.setBackground(new java.awt.Color(218, 220, 219));
        jLabelHyp.setFont(modeFont);
        jLabelHyp.setForeground(lcdBGColor);
        jLabelHyp.setText("HYP");
        jLabelHyp.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabelHyp.setFocusable(false);
        jPanelDisplay.add(jLabelHyp, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));

        jLabelDeg.setBackground(new java.awt.Color(218, 220, 219));
        jLabelDeg.setFont(modeFont);
        jLabelDeg.setForeground(lcdBGColor);
        jLabelDeg.setText("DEG");
        jLabelDeg.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabelDeg.setFocusable(false);
        jPanelDisplay.add(jLabelDeg, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 40, -1, -1));

        jLabelRad.setBackground(new java.awt.Color(218, 220, 219));
        jLabelRad.setFont(modeFont);
        jLabelRad.setForeground(lcdBGColor);
        jLabelRad.setText("RAD");
        jLabelRad.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabelRad.setFocusable(false);
        jPanelDisplay.add(jLabelRad, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, -1, -1));

        jLabelGrad.setBackground(new java.awt.Color(218, 220, 219));
        jLabelGrad.setFont(modeFont);
        jLabelGrad.setForeground(lcdBGColor);
        jLabelGrad.setText("GRAD");
        jLabelGrad.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabelGrad.setFocusable(false);
        jPanelDisplay.add(jLabelGrad, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 40, -1, -1));

        jLabelOpenBracket.setFont(modeFont);
        jLabelOpenBracket.setForeground(lcdBGColor);
        jLabelOpenBracket.setText("( )");
        jLabelOpenBracket.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabelOpenBracket.setFocusable(false);
        jPanelDisplay.add(jLabelOpenBracket, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 12, -1));

        jLabelBin.setFont(modeFont);
        jLabelBin.setForeground(lcdBGColor);
        jLabelBin.setText("BIN");
        jLabelBin.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabelBin.setFocusable(false);
        jPanelDisplay.add(jLabelBin, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, -1, -1));

        jLabelOct.setFont(modeFont);
        jLabelOct.setForeground(lcdBGColor);
        jLabelOct.setText("OCT");
        jLabelOct.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabelOct.setFocusable(false);
        jPanelDisplay.add(jLabelOct, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, -1, -1));

        jLabelHex.setFont(modeFont);
        jLabelHex.setForeground(lcdBGColor);
        jLabelHex.setText("HEX");
        jLabelHex.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabelHex.setFocusable(false);
        jPanelDisplay.add(jLabelHex, new org.netbeans.lib.awtextra.AbsoluteConstraints(225, 40, -1, -1));

        jLabelCplx.setFont(modeFont);
        jLabelCplx.setForeground(lcdBGColor);
        jLabelCplx.setText("CPLX");
        jLabelCplx.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabelCplx.setFocusable(false);
        jPanelDisplay.add(jLabelCplx, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, -1, -1));

        jLabelSD.setFont(modeFont);
        jLabelSD.setForeground(lcdBGColor);
        jLabelSD.setText("SD");
        jLabelSD.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabelSD.setFocusable(false);
        jPanelDisplay.add(jLabelSD, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, -1, -1));

        jTextField1.setBackground(Color.LIGHT_GRAY);
        jTextField1.setFont(sevenSegmentFont1);
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField1.setComponentPopupMenu(jPopupMenu1);
        jTextField1.setFocusable(false);
        jPanelDisplay.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 36, 290, 70));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/Logo.png"))); // NOI18N
        jLabel1.setFocusable(false);
        jPanelDisplay.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanelMain.add(jPanelDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 310, 120));

        jPanel1.setBackground(new java.awt.Color(81, 84, 101));
        jPanel1.setFocusable(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButtonB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/b.png"))); // NOI18N
        jButtonB.setFocusable(false);
        jButtonB.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonB.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/b_grey.png"))); // NOI18N
        jButtonB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonB, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, -1, -1));

        jButtonXtoM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/XtoM.png"))); // NOI18N
        jButtonXtoM.setFocusable(false);
        jButtonXtoM.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonXtoM.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/XtoM_grey.png"))); // NOI18N
        jButtonXtoM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXtoMActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonXtoM, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 170, -1, -1));

        jButtonDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/del.png"))); // NOI18N
        jButtonDel.setFocusable(false);
        jButtonDel.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonDel.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/del_grey.png"))); // NOI18N
        jButtonDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDelActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 74, -1, -1));

        jButtonDrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/drg.png"))); // NOI18N
        jButtonDrg.setFocusable(false);
        jButtonDrg.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonDrg.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/drg_grey.png"))); // NOI18N
        jButtonDrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDrgActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDrg, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 74, -1, -1));

        jButtonA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/a.png"))); // NOI18N
        jButtonA.setFocusable(false);
        jButtonA.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonA.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/a_grey.png"))); // NOI18N
        jButtonA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonA, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, -1, -1));

        jButtonCloseBracket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/Скобка закр.png"))); // NOI18N
        jButtonCloseBracket.setFocusable(false);
        jButtonCloseBracket.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonCloseBracket.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/Скобка закр_grey.png"))); // NOI18N
        jButtonCloseBracket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseBracketActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonCloseBracket, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 170, -1, -1));

        jButtonTan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/tan.png"))); // NOI18N
        jButtonTan.setFocusable(false);
        jButtonTan.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonTan.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/tan_grey.png"))); // NOI18N
        jButtonTan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTanActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonTan, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 74, -1, -1));

        jButtonX2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/power.png"))); // NOI18N
        jButtonX2.setFocusable(false);
        jButtonX2.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonX2.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/power_grey.png"))); // NOI18N
        jButtonX2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonX2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonX2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, -1));

        jButtonOpenBracket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/Скобка откр.png"))); // NOI18N
        jButtonOpenBracket.setFocusable(false);
        jButtonOpenBracket.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonOpenBracket.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/Скобка откр_grey.png"))); // NOI18N
        jButtonOpenBracket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenBracketActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonOpenBracket, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 170, -1, -1));

        jButtonCos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/cos.png"))); // NOI18N
        jButtonCos.setFocusable(false);
        jButtonCos.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonCos.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/cos_grey.png"))); // NOI18N
        jButtonCos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCosActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonCos, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 74, -1, -1));

        jButtonLog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/log.png"))); // NOI18N
        jButtonLog.setFocusable(false);
        jButtonLog.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonLog.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/log_grey.png"))); // NOI18N
        jButtonLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonLog, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 122, -1, -1));

        jButtonSQR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/sqr.png"))); // NOI18N
        jButtonSQR.setFocusable(false);
        jButtonSQR.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonSQR.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/sqr_grey.png"))); // NOI18N
        jButtonSQR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSQRActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonSQR, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 122, -1, -1));

        jButtonSin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/sin.png"))); // NOI18N
        jButtonSin.setFocusable(false);
        jButtonSin.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonSin.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/sin_grey.png"))); // NOI18N
        jButtonSin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSinActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonSin, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 74, -1, -1));

        jButtonOnC.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButtonOnC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/on.png"))); // NOI18N
        jButtonOnC.setFocusable(false);
        jButtonOnC.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonOnC.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/on_grey.png"))); // NOI18N
        jButtonOnC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOnCActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonOnC, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 26, -1, -1));

        jToggleButtonShift.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jToggleButtonShift.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/shift.png"))); // NOI18N
        jToggleButtonShift.setFocusable(false);
        jToggleButtonShift.setPreferredSize(new java.awt.Dimension(37, 20));
        jToggleButtonShift.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/shift_grey.png"))); // NOI18N
        jToggleButtonShift.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonShiftActionPerformed(evt);
            }
        });
        jPanel1.add(jToggleButtonShift, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 26, -1, -1));

        jButtonSci.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/sci.png"))); // NOI18N
        jButtonSci.setFocusable(false);
        jButtonSci.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonSci.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/sci_grey.png"))); // NOI18N
        jButtonSci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSciActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonSci, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 74, -1, -1));

        jButtonExp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/exp.png"))); // NOI18N
        jButtonExp.setFocusable(false);
        jButtonExp.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonExp.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/exp_grey.png"))); // NOI18N
        jButtonExp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExpActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonExp, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 122, -1, -1));

        jButtonDegConv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/degree1.png"))); // NOI18N
        jButtonDegConv.setFocusable(false);
        jButtonDegConv.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonDegConv.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/degree1_grey.png"))); // NOI18N
        jButtonDegConv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDegConvActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDegConv, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 122, -1, -1));

        jButtonXPowY.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/Xy.png"))); // NOI18N
        jButtonXPowY.setFocusable(false);
        jButtonXPowY.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonXPowY.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/Xy_grey.png"))); // NOI18N
        jButtonXPowY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXPowYActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonXPowY, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 122, -1, -1));

        jButtonLn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/ln.png"))); // NOI18N
        jButtonLn.setFocusable(false);
        jButtonLn.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonLn.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/ln_grey.png"))); // NOI18N
        jButtonLn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLnActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonLn, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 122, -1, -1));

        jButtonOff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/off.png"))); // NOI18N
        jButtonOff.setFocusable(false);
        jButtonOff.setPreferredSize(new java.awt.Dimension(37, 20));
        jButtonOff.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/off_grey.png"))); // NOI18N
        jButtonOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOffActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonOff, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 26, -1, -1));

        jToggleButtonHyp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/hyp.png"))); // NOI18N
        jToggleButtonHyp.setFocusable(false);
        jToggleButtonHyp.setPreferredSize(new java.awt.Dimension(37, 20));
        jToggleButtonHyp.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/hyp_grey.png"))); // NOI18N
        jToggleButtonHyp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonHypActionPerformed(evt);
            }
        });
        jPanel1.add(jToggleButtonHyp, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 26, -1, -1));

        jLabelLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/Линия.png"))); // NOI18N
        jLabelLine.setFocusable(false);
        jPanel1.add(jLabelLine, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 192, 308, 16));

        jLabelLine2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/Линия.png"))); // NOI18N
        jLabelLine2.setFocusable(false);
        jPanel1.add(jLabelLine2, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 144, -1, 14));

        jLabelLine3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/Линия.png"))); // NOI18N
        jLabelLine3.setFocusable(false);
        jPanel1.add(jLabelLine3, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 96, -1, 14));

        jLabelLine4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/Линия.png"))); // NOI18N
        jLabelLine4.setFocusable(false);
        jPanel1.add(jLabelLine4, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 50, -1, 14));

        jLabel1DivX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/1X.png"))); // NOI18N
        jLabel1DivX.setFocusable(false);
        jPanel1.add(jLabel1DivX, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, -1));

        jLabelRtoP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/RtoP.png"))); // NOI18N
        jLabelRtoP.setFocusable(false);
        jPanel1.add(jLabelRtoP, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 160, -1, -1));

        jLabelPtoR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/PtoR.png"))); // NOI18N
        jLabelPtoR.setFocusable(false);
        jPanel1.add(jLabelPtoR, new org.netbeans.lib.awtextra.AbsoluteConstraints(114, 160, -1, -1));

        jLabelXtoY.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/X-Y.png"))); // NOI18N
        jLabelXtoY.setFocusable(false);
        jPanel1.add(jLabelXtoY, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 160, -1, -1));

        jLabelNEX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/nEx3.png"))); // NOI18N
        jLabelNEX.setFocusable(false);
        jPanel1.add(jLabelNEX, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 158, -1, -1));

        jLabelXEX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/xEx3.png"))); // NOI18N
        jLabelXEX.setFocusable(false);
        jPanel1.add(jLabelXEX, new org.netbeans.lib.awtextra.AbsoluteConstraints(258, 156, -1, -1));

        jLabelPI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/PI2.png"))); // NOI18N
        jLabelPI.setFocusable(false);
        jPanel1.add(jLabelPI, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 112, -1, -1));

        jLabelXSqrY.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/XsqrY4.png"))); // NOI18N
        jLabelXSqrY.setFocusable(false);
        jPanel1.add(jLabelXSqrY, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 108, -1, -1));

        jLabelCbrt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/cbrt2.png"))); // NOI18N
        jLabelCbrt.setFocusable(false);
        jPanel1.add(jLabelCbrt, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 108, -1, -1));

        jLabelToDeg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/toDeg4.png"))); // NOI18N
        jLabelToDeg.setFocusable(false);
        jPanel1.add(jLabelToDeg, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 110, -1, -1));

        jLabelEX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/Ex4.png"))); // NOI18N
        jLabelEX.setFocusable(false);
        jPanel1.add(jLabelEX, new org.netbeans.lib.awtextra.AbsoluteConstraints(212, 108, -1, -1));

        jLabel10PowX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/10x3.png"))); // NOI18N
        jLabel10PowX.setFocusable(false);
        jPanel1.add(jLabel10PowX, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 106, -1, -1));

        jLabelCPLX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/CPLX3.png"))); // NOI18N
        jLabelCPLX.setFocusable(false);
        jPanel1.add(jLabelCPLX, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 62, -1, -1));

        jLabelFix.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/FIX2.png"))); // NOI18N
        jLabelFix.setFocusable(false);
        jPanel1.add(jLabelFix, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 62, -1, -1));

        jLabelDrgTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/DRGto2.png"))); // NOI18N
        jLabelDrgTo.setFocusable(false);
        jPanel1.add(jLabelDrgTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 62, -1, -1));

        jLabelArcSin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/arcsin2.png"))); // NOI18N
        jLabelArcSin.setFocusable(false);
        jPanel1.add(jLabelArcSin, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 62, -1, -1));

        jLabelArcCos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/arccos2.png"))); // NOI18N
        jLabelArcCos.setFocusable(false);
        jPanel1.add(jLabelArcCos, new org.netbeans.lib.awtextra.AbsoluteConstraints(214, 62, -1, -1));

        jLabelArcTan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/arctan2.png"))); // NOI18N
        jLabelArcTan.setFocusable(false);
        jPanel1.add(jLabelArcTan, new org.netbeans.lib.awtextra.AbsoluteConstraints(264, 62, -1, -1));

        jLabelArcHyp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/arcHYP2.png"))); // NOI18N
        jLabelArcHyp.setFocusable(false);
        jPanel1.add(jLabelArcHyp, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 14, -1, -1));

        jLabelSdKey.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/SD2.png"))); // NOI18N
        jLabelSdKey.setFocusable(false);
        jPanel1.add(jLabelSdKey, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 14, -1, -1));

        jPanelMain.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 310, 204));

        jPanelKeys2.setBackground(new java.awt.Color(81, 84, 101));
        jPanelKeys2.setFocusable(false);
        jPanelKeys2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/0.png"))); // NOI18N
        jButton0.setFocusable(false);
        jButton0.setPreferredSize(new java.awt.Dimension(50, 33));
        jButton0.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/0_grey.png"))); // NOI18N
        jButton0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton0ActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButton0, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 184, -1, -1));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/1.png"))); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setPreferredSize(new java.awt.Dimension(50, 33));
        jButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/1_grey.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 128, -1, -1));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/2.png"))); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setPreferredSize(new java.awt.Dimension(50, 33));
        jButton2.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/2_grey.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 128, -1, -1));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/3.png"))); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setPreferredSize(new java.awt.Dimension(50, 33));
        jButton3.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/3_grey.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 128, -1, -1));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/4.png"))); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setPreferredSize(new java.awt.Dimension(50, 33));
        jButton4.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/4_grey.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 72, -1, -1));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/5.png"))); // NOI18N
        jButton5.setMnemonic('5');
        jButton5.setFocusable(false);
        jButton5.setPreferredSize(new java.awt.Dimension(50, 33));
        jButton5.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/5_grey.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 72, -1, -1));

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/6.png"))); // NOI18N
        jButton6.setFocusable(false);
        jButton6.setPreferredSize(new java.awt.Dimension(50, 33));
        jButton6.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/6_grey.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 72, -1, -1));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/7.png"))); // NOI18N
        jButton7.setFocusable(false);
        jButton7.setPreferredSize(new java.awt.Dimension(50, 33));
        jButton7.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/7_grey.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 16, -1, -1));

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/8.png"))); // NOI18N
        jButton8.setFocusable(false);
        jButton8.setPreferredSize(new java.awt.Dimension(50, 33));
        jButton8.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/8_grey.png"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 16, -1, -1));

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/9.png"))); // NOI18N
        jButton9.setFocusable(false);
        jButton9.setPreferredSize(new java.awt.Dimension(50, 33));
        jButton9.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/9_grey.png"))); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 16, -1, -1));

        jButtonCE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/CE.png"))); // NOI18N
        jButtonCE.setFocusable(false);
        jButtonCE.setPreferredSize(new java.awt.Dimension(50, 33));
        jButtonCE.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/CE_grey.png"))); // NOI18N
        jButtonCE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCEActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButtonCE, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 184, -1, -1));

        jButtonMR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/MR.png"))); // NOI18N
        jButtonMR.setFocusable(false);
        jButtonMR.setPreferredSize(new java.awt.Dimension(50, 33));
        jButtonMR.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/MR_grey.png"))); // NOI18N
        jButtonMR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMRActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButtonMR, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 16, -1, -1));

        jButtonCalc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/calc.png"))); // NOI18N
        jButtonCalc.setFocusable(false);
        jButtonCalc.setPreferredSize(new java.awt.Dimension(50, 33));
        jButtonCalc.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/calc_grey.png"))); // NOI18N
        jButtonCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCalcActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButtonCalc, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 184, -1, -1));

        jButtonMPlus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/memoryPlus.png"))); // NOI18N
        jButtonMPlus.setFocusable(false);
        jButtonMPlus.setPreferredSize(new java.awt.Dimension(50, 33));
        jButtonMPlus.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/memoryPlus_grey.png"))); // NOI18N
        jButtonMPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMPlusActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButtonMPlus, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 16, -1, -1));

        jButtonMult.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/multiply.png"))); // NOI18N
        jButtonMult.setFocusable(false);
        jButtonMult.setPreferredSize(new java.awt.Dimension(50, 33));
        jButtonMult.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/multiply_grey.png"))); // NOI18N
        jButtonMult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMultActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButtonMult, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 72, -1, -1));

        jButtonDiv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/divide.png"))); // NOI18N
        jButtonDiv.setFocusable(false);
        jButtonDiv.setPreferredSize(new java.awt.Dimension(50, 33));
        jButtonDiv.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/divide_grey.png"))); // NOI18N
        jButtonDiv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDivActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButtonDiv, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 72, -1, -1));

        jButtonPlus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/plus.png"))); // NOI18N
        jButtonPlus.setFocusable(false);
        jButtonPlus.setPreferredSize(new java.awt.Dimension(50, 33));
        jButtonPlus.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/plus_grey.png"))); // NOI18N
        jButtonPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlusActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButtonPlus, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 128, -1, -1));

        jButtonDot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/dot.png"))); // NOI18N
        jButtonDot.setFocusable(false);
        jButtonDot.setPreferredSize(new java.awt.Dimension(50, 33));
        jButtonDot.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/dot_grey.png"))); // NOI18N
        jButtonDot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDotActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButtonDot, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 184, -1, -1));

        jButtonSign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/sign.png"))); // NOI18N
        jButtonSign.setFocusable(false);
        jButtonSign.setPreferredSize(new java.awt.Dimension(50, 33));
        jButtonSign.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/sign_grey.png"))); // NOI18N
        jButtonSign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSignActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButtonSign, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 184, -1, -1));

        jButtonMinus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/minus.png"))); // NOI18N
        jButtonMinus.setFocusable(false);
        jButtonMinus.setPreferredSize(new java.awt.Dimension(50, 33));
        jButtonMinus.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/buttons/minus_grey.png"))); // NOI18N
        jButtonMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMinusActionPerformed(evt);
            }
        });
        jPanelKeys2.add(jButtonMinus, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 128, -1, -1));

        jLabelRnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/RND.png"))); // NOI18N
        jLabelRnd.setFocusable(false);
        jPanelKeys2.add(jLabelRnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(84, 174, -1, -1));

        jLabelPercent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/Проценты2.png"))); // NOI18N
        jLabelPercent.setFocusable(false);
        jPanelKeys2.add(jLabelPercent, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 172, -1, -1));

        jLabelFactorial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/Факториал2.png"))); // NOI18N
        jLabelFactorial.setFocusable(false);
        jPanelKeys2.add(jLabelFactorial, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 172, -1, -1));

        jLabelDecKey.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/DEC.png"))); // NOI18N
        jLabelDecKey.setFocusable(false);
        jPanelKeys2.add(jLabelDecKey, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 116, -1, -1));

        jLabelHexKey.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/HEX.png"))); // NOI18N
        jLabelHexKey.setFocusable(false);
        jPanelKeys2.add(jLabelHexKey, new org.netbeans.lib.awtextra.AbsoluteConstraints(262, 116, -1, -1));

        jLabelOctKey.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/OCT.png"))); // NOI18N
        jLabelOctKey.setFocusable(false);
        jPanelKeys2.add(jLabelOctKey, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 60, -1, -1));

        jLabelBinKey.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/BIN.png"))); // NOI18N
        jLabelBinKey.setFocusable(false);
        jPanelKeys2.add(jLabelBinKey, new org.netbeans.lib.awtextra.AbsoluteConstraints(264, 60, -1, -1));

        jLabelDataDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/Data_DEL.png"))); // NOI18N
        jLabelDataDel.setFocusable(false);
        jPanelKeys2.add(jLabelDataDel, new org.netbeans.lib.awtextra.AbsoluteConstraints(188, 6, -1, -1));

        jLabelBiKvOtkl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/Labels/Би_кв_откл.png"))); // NOI18N
        jLabelBiKvOtkl.setFocusable(false);
        jPanelKeys2.add(jLabelBiKvOtkl, new org.netbeans.lib.awtextra.AbsoluteConstraints(252, 4, -1, -1));

        jPanelMain.add(jPanelKeys2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 374, 310, 230));

        getContentPane().add(jPanelMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 336, 646));

        jMenuBar1.setFocusable(false);
        jMenuBar1.setPreferredSize(new java.awt.Dimension(300, 21));

        jMenu1.setText("Файл");
        jMenu1.setFocusable(false);

        jMenuItemExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/icons/logout-16.png"))); // NOI18N
        jMenuItemExit.setText("Выход");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemExit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Правка");
        jMenu2.setFocusable(false);

        jMenuItemCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/icons/copy.png"))); // NOI18N
        jMenuItemCopy.setText("Копировать");
        jMenuItemCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopyActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemCopy);

        jMenuItemPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newcalculator/icons/paste.png"))); // NOI18N
        jMenuItemPaste.setText("Вставить");
        jMenuItemPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPasteActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemPaste);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Окно");
        jMenu3.setFocusable(false);

        jCheckBoxMenuItemProtocol.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jCheckBoxMenuItemProtocol.setText("Протокол");
        jCheckBoxMenuItemProtocol.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxMenuItemProtocolItemStateChanged(evt);
            }
        });
        jCheckBoxMenuItemProtocol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemProtocolActionPerformed(evt);
            }
        });
        jMenu3.add(jCheckBoxMenuItemProtocol);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Справка");
        jMenu4.setFocusable(false);

        jMenuItemAbout.setText("О программе");
        jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAboutActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemAbout);

        jMenuBar1.add(Box.createHorizontalGlue());

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX):
                inputDriver.button08();
                break;
            case (Mode.BIN):
                break;
            case (Mode.OCT):
                break;
            case (Mode.HEX):
                inputDriver.button08Hex();
                break;
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX):
                inputDriver.button07();
                break;
            case (Mode.BIN):
                break;
            case (Mode.OCT):
                inputDriver.button07Oct();
                break;
            case (Mode.HEX):
                inputDriver.button07Hex();
                break;
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX):
                inputDriver.button06();
                break;
            case (Mode.BIN):
                break;
            case (Mode.OCT):
                inputDriver.button06Oct();
                break;
            case (Mode.HEX):
                inputDriver.button06Hex();
                break;
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:  
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX):
                inputDriver.button05();
                break;
            case (Mode.BIN):
                break;
            case (Mode.OCT):
                inputDriver.button05Oct();
                break;
            case (Mode.HEX):
                inputDriver.button05Hex();
                break;
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX):
                inputDriver.button04();
                break;
            case (Mode.BIN):
                break;
            case (Mode.OCT):
                inputDriver.button04Oct();
                break;
            case (Mode.HEX):
                inputDriver.button04Hex();
                break;
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButtonMinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMinusActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonMinus();
                }
                break;
                case (Mode.BIN):
                    inputDriver.buttonMinusBin();
                    break;
                case (Mode.OCT):
                    inputDriver.buttonMinusOct();
                    break;
                case (Mode.HEX):
                    inputDriver.buttonMinusHex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonMinusActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX):
                inputDriver.button03();
                break;
            case (Mode.BIN):
                break;
            case (Mode.OCT):
                inputDriver.button03Oct();
                break;
            case (Mode.HEX):
                inputDriver.button03Hex();
                break;
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX):
                inputDriver.button02();
                break;
            case (Mode.BIN):
                break;
            case (Mode.OCT):
                inputDriver.button02Oct();
                break;
            case (Mode.HEX):
                inputDriver.button02Hex();
                break;
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButtonSignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSignActionPerformed
        // TODO add your handling code here:
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX): {
                inputDriver.buttonSign();
            }
            break;
            case (Mode.BIN):
                inputDriver.buttonSignBin();
                break;
            case (Mode.OCT):
                inputDriver.buttonSignOct();
                break;
            case (Mode.HEX):
                inputDriver.buttonSignHex();
                break;
        }
    }//GEN-LAST:event_jButtonSignActionPerformed

    private void jButtonDotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDotActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonDot();
                }
                break;
                case (Mode.BIN):
                    break;
                case (Mode.OCT):
                    break;
                case (Mode.HEX):
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonDotActionPerformed

    private void jButtonPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlusActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonPlus();
                }
                break;
                case (Mode.BIN):
                    inputDriver.buttonPlusBin();
                    break;
                case (Mode.OCT):
                    inputDriver.buttonPlusOct();
                    break;
                case (Mode.HEX):
                    inputDriver.buttonPlusHex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonPlusActionPerformed

    private void jButtonDivActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDivActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonDiv();
                }
                break;
                case (Mode.BIN):
                    inputDriver.buttonDivBin();
                    break;
                case (Mode.OCT):
                    inputDriver.buttonDivOct();
                    break;
                case (Mode.HEX):
                    inputDriver.buttonDivHex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonDivActionPerformed

    private void jButtonMultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMultActionPerformed
        // TODO add your handling code here:        
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonMult();
                }
                break;
                case (Mode.BIN):
                    inputDriver.buttonMultBin();
                    break;
                case (Mode.OCT):
                    inputDriver.buttonMultOct();
                    break;
                case (Mode.HEX):
                    inputDriver.buttonMultHex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonMultActionPerformed

    private void jButtonMPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMPlusActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonMem();
                }
                break;
                case (Mode.BIN):
                    inputDriver.buttonMemBin();
                    break;
                case (Mode.OCT):
                    inputDriver.buttonMemOct();
                    break;
                case (Mode.HEX):
                    inputDriver.buttonMemHex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonMPlusActionPerformed

    private void jButtonCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCalcActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonCalc();
                }
                break;
                case (Mode.BIN):
                    inputDriver.buttonCalcBin();
                    break;
                case (Mode.OCT):
                    inputDriver.buttonCalcOct();
                    break;
                case (Mode.HEX):
                    inputDriver.buttonCalcHex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonCalcActionPerformed

    private void jButtonMRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMRActionPerformed
        // TODO add your handling code here:
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX):
                inputDriver.buttonMR();
                break;
            case (Mode.BIN):
                inputDriver.buttonMRBin();
                break;
            case (Mode.OCT):
                inputDriver.buttonMROct();
                break;
            case (Mode.HEX):
                inputDriver.buttonMRHex();
                break;
        }
    }//GEN-LAST:event_jButtonMRActionPerformed

    private void jButtonCEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCEActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonCE();
                }
                break;
                case (Mode.BIN):
                    inputDriver.buttonCEBin();
                    break;
                case (Mode.OCT):
                    inputDriver.buttonCEOct();
                    break;
                case (Mode.HEX):
                    inputDriver.buttonCEHex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonCEActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX):
                inputDriver.button01();
                break;
            case (Mode.BIN):
                inputDriver.button01Bin();
                break;
            case (Mode.OCT):
                inputDriver.button01Oct();
                break;
            case (Mode.HEX):
                inputDriver.button01Hex();
                break;
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton0ActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.button00();
                }
                break;
                case (Mode.BIN):
                    inputDriver.button00Bin();
                    break;
                case (Mode.OCT):
                    inputDriver.button00Oct();
                    break;
                case (Mode.HEX):
                    inputDriver.button00Hex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton0ActionPerformed

    private void jButtonOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOffActionPerformed
        // TODO add your handling code here:
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX):
            case (Mode.BIN):
            case (Mode.OCT):
            case (Mode.HEX):
                inputDriver.buttonOff();
                break;
        }
    }//GEN-LAST:event_jButtonOffActionPerformed

    private void jButtonLnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLnActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonLn();
                }
                break;
                case (Mode.BIN):
                    break;
                case (Mode.OCT):
                    break;
                case (Mode.HEX):
                    inputDriver.buttonLnHex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonLnActionPerformed

    private void jButtonXPowYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXPowYActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonXpowY();
                }
                break;
                case (Mode.BIN):
                    break;
                case (Mode.OCT):
                    break;
                case (Mode.HEX):
                    inputDriver.buttonXpowYHex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonXPowYActionPerformed

    private void jButtonDegConvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDegConvActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonToRad();
                }
                break;
                case (Mode.BIN):
                    break;
                case (Mode.OCT):
                    break;
                case (Mode.HEX):
                    inputDriver.buttonToRadHex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonDegConvActionPerformed

    private void jButtonExpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExpActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonExp();
                }
                break;
                case (Mode.BIN):
                    break;
                case (Mode.OCT):
                    break;
                case (Mode.HEX):
                    inputDriver.buttonExpHex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonExpActionPerformed

    private void jToggleButtonShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonShiftActionPerformed
        // TODO add your handling code here:
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX):
            case (Mode.BIN):
            case (Mode.OCT):
            case (Mode.HEX):
                inputDriver.buttonShift();
                break;
        }
    }//GEN-LAST:event_jToggleButtonShiftActionPerformed

    private void jButtonOnCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOnCActionPerformed
        // TODO add your handling code here:
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX):
            case (Mode.BIN):
            case (Mode.OCT):
            case (Mode.HEX):
                inputDriver.buttonOnC();
                break;
        }
    }//GEN-LAST:event_jButtonOnCActionPerformed

    private void jButtonSinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSinActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonSin();
                }
                break;
                case (Mode.BIN):
                    break;
                case (Mode.OCT):
                    break;
                case (Mode.HEX):
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonSinActionPerformed

    private void jButtonSQRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSQRActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonSqr();
                }
                break;
                case (Mode.BIN):
                    break;
                case (Mode.OCT):
                    break;
                case (Mode.HEX):
                    inputDriver.buttonSqrHex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonSQRActionPerformed

    private void jButtonLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonLog();
                }
                break;
                case (Mode.BIN):
                    break;
                case (Mode.OCT):
                    break;
                case (Mode.HEX):
                    inputDriver.buttonLogHex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonLogActionPerformed

    private void jButtonCosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCosActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonCos();
                }
                break;
                case (Mode.BIN):
                    break;
                case (Mode.OCT):
                    break;
                case (Mode.HEX):
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonCosActionPerformed

    private void jButtonX2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonX2ActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonX2();
                }
                break;
                case (Mode.BIN):
                    break;
                case (Mode.OCT):
                    break;
                case (Mode.HEX):
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonX2ActionPerformed

    private void jButtonTanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTanActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonTan();
                }
                break;
                case (Mode.BIN):
                    break;
                case (Mode.OCT):
                    break;
                case (Mode.HEX):
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonTanActionPerformed

    private void jButtonDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDelActionPerformed
        // TODO add your handling code here:
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX):
                inputDriver.buttonDel();
                break;
            case (Mode.BIN):
                inputDriver.buttonDelBin();
                break;
            case (Mode.OCT):
                inputDriver.buttonDelOct();
                break;
            case (Mode.HEX):
                inputDriver.buttonDelHex();
                break;
        }
    }//GEN-LAST:event_jButtonDelActionPerformed

    private void jButtonXtoMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXtoMActionPerformed
        // TODO add your handling code here:

        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX): {
                inputDriver.buttonXtoM();
            }
            break;
            case (Mode.BIN):
                inputDriver.buttonXtoMBin();
                break;
            case (Mode.OCT):
                inputDriver.buttonXtoMOct();
                break;
            case (Mode.HEX):
                inputDriver.buttonXtoMHex();
                break;
        }

    }//GEN-LAST:event_jButtonXtoMActionPerformed

    private void jToggleButtonHypActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonHypActionPerformed
        // TODO add your handling code here:        
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX):
            case (Mode.BIN):
            case (Mode.OCT):
            case (Mode.HEX):
                inputDriver.buttonHyp();
                break;
        }
    }//GEN-LAST:event_jToggleButtonHypActionPerformed

    private void jButtonSciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSciActionPerformed
        // TODO add your handling code here:        
        switch (mode.getMode()) {
            case (Mode.DEC):
            case (Mode.SD):
            case (Mode.COMPLEX): {
                inputDriver.buttonSci();
            }
            break;
            case (Mode.BIN):
                break;
            case (Mode.OCT):
                break;
            case (Mode.HEX):
                break;
        }
    }//GEN-LAST:event_jButtonSciActionPerformed

    private void jButtonDrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDrgActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonDrg();
                }
                break;
                case (Mode.BIN):
                    inputDriver.buttonDrgBin();
                    break;
                case (Mode.OCT):
                    inputDriver.buttonDrgOct();
                    break;
                case (Mode.HEX):
                    inputDriver.buttonDrgHex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonDrgActionPerformed

    private void jButtonAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonA();
                }
                break;
                case (Mode.BIN):
                    break;
                case (Mode.OCT):
                    break;
                case (Mode.HEX):
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonAActionPerformed

    private void jButtonBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonB();
                }
                break;
                case (Mode.BIN):
                    break;
                case (Mode.OCT):
                    break;
                case (Mode.HEX):
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonBActionPerformed

    private void jButtonOpenBracketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenBracketActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonOpenBracket();
                }
                break;
                case (Mode.BIN):
                    inputDriver.buttonOpenBracketBin();
                    break;
                case (Mode.OCT):
                    inputDriver.buttonOpenBracketOct();
                    break;
                case (Mode.HEX):
                    inputDriver.buttonOpenBracketHex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonOpenBracketActionPerformed

    private void jButtonCloseBracketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseBracketActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {
                    inputDriver.buttonCloseBracket();
                }
                break;
                case (Mode.BIN):
                    break;
                case (Mode.OCT):
                    break;
                case (Mode.HEX):
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonCloseBracketActionPerformed

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        // TODO add your handling code here:        

    }//GEN-LAST:event_formComponentMoved

    private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAboutActionPerformed
        AboutJFrame aboutJFrame = new AboutJFrame();
        aboutJFrame.pack();
        aboutJFrame.setLocationRelativeTo(null);
        aboutJFrame.setVisible(true);
    }//GEN-LAST:event_jMenuItemAboutActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        // TODO add your handling code here:
        preferences.setXCoord(getLocation().x);
        preferences.setYCoord(getLocation().y);
        System.exit(0);
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jCheckBoxMenuItemProtocolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemProtocolItemStateChanged
        // TODO add your handling code here:
        if (jCheckBoxMenuItemProtocol.getState()) {
            int mainWindowXCoord = this.getLocation().x;
            int mainWindowYCoord = this.getLocation().y;
            int mainWindowWidth = this.getWidth();
            int mainWindowHeight = this.getHeight();
            int protocolXcoord = mainWindowXCoord + mainWindowWidth;
//            int protocolXcoord = mainWindowXCoord + mainWindowWidth - this.getInsets().left - this.getInsets().right;
            int protocolYcoord = mainWindowYCoord;
            protocol.setLocation(protocolXcoord, protocolYcoord);
            protocol.setSize(protocol.getWidth(), mainWindowHeight);
            protocol.setVisible(true);
        } else {
            protocol.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBoxMenuItemProtocolItemStateChanged

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void formWindowDeiconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeiconified
        // TODO add your handling code here:
//        Log.d(TAG, "WindowDeiconified");

    }//GEN-LAST:event_formWindowDeiconified

    private void jMenuItemPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPasteActionPerformed
        // TODO add your handling code here:
        pasteFromClipboard();
    }//GEN-LAST:event_jMenuItemPasteActionPerformed

    private void jMenuItemCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCopyActionPerformed
        // TODO add your handling code here:
        copyToClipboard();
    }//GEN-LAST:event_jMenuItemCopyActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
        jToggleButtonShift.setSelected(false);
        jLabelShift.setForeground(lcdBGColor);
        switch (evt.getKeyChar()) {
            case '0':
                jButton0.doClick();
                break;
            case '1':
                jButton1.doClick();
                break;
            case '2':
                jButton2.doClick();
                break;
            case '3':
                jButton3.doClick();
                break;
            case '4':
                jButton4.doClick();
                break;
            case '5':
                jButton5.doClick();
                break;
            case '6':
                jButton6.doClick();
                break;
            case '7':
                jButton7.doClick();
                break;
            case '8':
                jButton8.doClick();
                break;
            case '9':
                jButton9.doClick();
                break;
            case 'A':
                jButtonExp.doClick();
                break;
            case 'B':
                jButtonXPowY.doClick();
                break;
            case 'C':
                jButtonSQR.doClick();
                break;
            case 'D':
                jButtonDegConv.doClick();
                break;
            case 'E':
                jButtonLn.doClick();
                break;
            case 'F':
                jButtonLog.doClick();
                break;
            case 'a':
                jButtonExp.doClick();
                break;
            case 'b':
                jButtonXPowY.doClick();
                break;
            case 'c':
                jButtonSQR.doClick();
                break;
            case 'd':
                jButtonDegConv.doClick();
                break;
            case 'e':
                jButtonLn.doClick();
                break;
            case 'f':
                jButtonLog.doClick();
                break;
            case 'Ф':
                jButtonExp.doClick();
                break;
            case 'И':
                jButtonXPowY.doClick();
                break;
            case 'С':
                jButtonSQR.doClick();
                break;
            case 'В':
                jButtonDegConv.doClick();
                break;
            case 'У':
                jButtonLn.doClick();
                break;
            case 'А':
                jButtonLog.doClick();
                break;
            case 'ф':
                jButtonExp.doClick();
                break;
            case 'и':
                jButtonXPowY.doClick();
                break;
            case 'с':
                jButtonSQR.doClick();
                break;
            case 'в':
                jButtonDegConv.doClick();
                break;
            case 'у':
                jButtonLn.doClick();
                break;
            case 'а':
                jButtonLog.doClick();
                break;
            case '+':
                jButtonPlus.doClick();
                break;
            case '-':
                jButtonMinus.doClick();
                break;
            case '*':
                jButtonMult.doClick();
                break;
            case '/':
                jButtonDiv.doClick();
                break;
            case '=':
                jButtonCalc.doClick();
                break;
            case '.':
                jButtonDot.doClick();
                break;
            case ',':
                jButtonDot.doClick();
                break;
            case 10: //Enter
                jButtonCalc.doClick();
                break;
            case 8: //backspace
                jButtonDel.doClick();
                break;
            case 27: //escape
                jButtonOnC.doClick();
                break;
            case 127: //delete
                jButtonCE.doClick();
                break;
        }

    }//GEN-LAST:event_formKeyPressed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        jButtonCE.doClick();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        copyToClipboard();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        pasteFromClipboard();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:        
        preferences.setXCoord(getLocation().x);
        preferences.setYCoord(getLocation().y);
    }//GEN-LAST:event_formWindowClosing

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        try {
            switch (mode.getMode()) {
                case (Mode.DEC):
                case (Mode.SD):
                case (Mode.COMPLEX): {

                    inputDriver.button09();

                }
                break;
                case (Mode.BIN):
                    break;
                case (Mode.OCT):
                    break;
                case (Mode.HEX):
                    inputDriver.button09Hex();
                    break;
            }
        } catch (MyExceptions ex) {
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
        // TODO add your handling code here:    
//        Log.d(TAG, "WindowStateChanged: " + evt);
        if (jCheckBoxMenuItemProtocol.getState() && evt.getNewState() == 0) {
            protocol.setVisible(true);
        }
        if (jCheckBoxMenuItemProtocol.getState() && evt.getNewState() == 1) {
            protocol.setVisible(false);
        }
    }//GEN-LAST:event_formWindowStateChanged

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        // TODO add your handling code here:
        Log.d(TAG, "WindowGainedFocus: " + evt);
        if (evt.getOppositeWindow() == null) {
            Log.d(TAG, "OppositeWindow() = null");
            if (jCheckBoxMenuItemProtocol.getState() && evt.getNewState() == 0) {
                Log.d(TAG, "NewState() == 0");
                protocol.setVisible(true);
            }
            if (jCheckBoxMenuItemProtocol.getState() && evt.getNewState() == 1) {
                Log.d(TAG, "NewState() == 1");
                protocol.setVisible(false);
            }
        }
    }//GEN-LAST:event_formWindowGainedFocus

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
//        Log.d(TAG, "ComponentShown: " + evt);
    }//GEN-LAST:event_formComponentShown

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        // TODO add your handling code here: 
//        Log.d(TAG, "FocusGained: " + evt);
    }//GEN-LAST:event_formFocusGained

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
//        Log.d(TAG, "WindowActivated: " + evt.getClass());
//        Log.d(TAG, "WindowActivated: " + evt.);
//                if (jCheckBoxMenuItem1.getState() ) {
//            protocol.setVisible(true);
//        }
    }//GEN-LAST:event_formWindowActivated

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
//        Log.d(TAG, "WindowOpened: " + evt);
    }//GEN-LAST:event_formWindowOpened

    private void jPanelMainComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanelMainComponentShown
        // TODO add your handling code here:
//        Log.d(TAG, "ComponentShown: " + evt);        
//        if (jCheckBoxMenuItem1.getState() ) {
//            protocol.setVisible(true);
//        }
    }//GEN-LAST:event_jPanelMainComponentShown

    private void jCheckBoxMenuItemProtocolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemProtocolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxMenuItemProtocolActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
//            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainActivity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainActivity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainActivity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainActivity.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainActivity calculator = new MainActivity();
                calculator.pack();
                //newcalculator.setLocationRelativeTo(null);                
                calculator.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton0;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonA;
    private javax.swing.JButton jButtonB;
    private javax.swing.JButton jButtonCE;
    private javax.swing.JButton jButtonCalc;
    private javax.swing.JButton jButtonCloseBracket;
    private javax.swing.JButton jButtonCos;
    private javax.swing.JButton jButtonDegConv;
    private javax.swing.JButton jButtonDel;
    private javax.swing.JButton jButtonDiv;
    private javax.swing.JButton jButtonDot;
    private javax.swing.JButton jButtonDrg;
    private javax.swing.JButton jButtonExp;
    private javax.swing.JButton jButtonLn;
    private javax.swing.JButton jButtonLog;
    private javax.swing.JButton jButtonMPlus;
    private javax.swing.JButton jButtonMR;
    private javax.swing.JButton jButtonMinus;
    private javax.swing.JButton jButtonMult;
    private javax.swing.JButton jButtonOff;
    private javax.swing.JButton jButtonOnC;
    private javax.swing.JButton jButtonOpenBracket;
    private javax.swing.JButton jButtonPlus;
    private javax.swing.JButton jButtonSQR;
    private javax.swing.JButton jButtonSci;
    private javax.swing.JButton jButtonSign;
    private javax.swing.JButton jButtonSin;
    private javax.swing.JButton jButtonTan;
    private javax.swing.JButton jButtonX2;
    private javax.swing.JButton jButtonXPowY;
    private javax.swing.JButton jButtonXtoM;
    public javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemProtocol;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10PowX;
    private javax.swing.JLabel jLabel1DivX;
    private javax.swing.JLabel jLabelArcCos;
    private javax.swing.JLabel jLabelArcHyp;
    private javax.swing.JLabel jLabelArcSin;
    private javax.swing.JLabel jLabelArcTan;
    private javax.swing.JLabel jLabelBiKvOtkl;
    private javax.swing.JLabel jLabelBin;
    private javax.swing.JLabel jLabelBinKey;
    private javax.swing.JLabel jLabelCPLX;
    private javax.swing.JLabel jLabelCbrt;
    private javax.swing.JLabel jLabelCplx;
    private javax.swing.JLabel jLabelDataDel;
    private javax.swing.JLabel jLabelDecKey;
    private javax.swing.JLabel jLabelDeg;
    private javax.swing.JLabel jLabelDrgTo;
    private javax.swing.JLabel jLabelEX;
    private javax.swing.JLabel jLabelError;
    private javax.swing.JLabel jLabelFactorial;
    private javax.swing.JLabel jLabelFix;
    private javax.swing.JLabel jLabelGrad;
    private javax.swing.JLabel jLabelHex;
    private javax.swing.JLabel jLabelHexKey;
    private javax.swing.JLabel jLabelHyp;
    private javax.swing.JLabel jLabelLine;
    private javax.swing.JLabel jLabelLine2;
    private javax.swing.JLabel jLabelLine3;
    private javax.swing.JLabel jLabelLine4;
    private javax.swing.JLabel jLabelMemory;
    private javax.swing.JLabel jLabelNEX;
    private javax.swing.JLabel jLabelOct;
    private javax.swing.JLabel jLabelOctKey;
    private javax.swing.JLabel jLabelOpenBracket;
    private javax.swing.JLabel jLabelPI;
    private javax.swing.JLabel jLabelPercent;
    private javax.swing.JLabel jLabelPtoR;
    private javax.swing.JLabel jLabelRad;
    private javax.swing.JLabel jLabelRnd;
    private javax.swing.JLabel jLabelRtoP;
    private javax.swing.JLabel jLabelSD;
    private javax.swing.JLabel jLabelSdKey;
    private javax.swing.JLabel jLabelShift;
    private javax.swing.JLabel jLabelToDeg;
    private javax.swing.JLabel jLabelXEX;
    private javax.swing.JLabel jLabelXSqrY;
    private javax.swing.JLabel jLabelXtoY;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItemAbout;
    private javax.swing.JMenuItem jMenuItemCopy;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemPaste;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelDisplay;
    private javax.swing.JPanel jPanelKeys2;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton jToggleButtonHyp;
    private javax.swing.JToggleButton jToggleButtonShift;
    // End of variables declaration//GEN-END:variables
}
