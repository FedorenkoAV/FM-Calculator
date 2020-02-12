package newcalculator;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JTextField;

/**
 * Класс MainDisplay это связующее звено между внутренним устройством и внешним
 * миром. Объекту этого класса передается ссылка на объект который будут
 * отбражать выходные данные Этот класс нужно переписывать/редактировать заново
 * для каждого нового устройства
 */
class MainDisplay {

    private static final String TAG = "MainDisplay";

    private static final int S_TEXT_SIZE = 33;
    private static final int M_TEXT_SIZE = 40;
    private static final int L_TEXT_SIZE = 46;

    private static final int M_TEXT_LENGTH = 15;
    private static final int L_TEXT_LENGTH = 13;

    private JTextField mainDisplay;
    int exponentLength = 2; // Эту переменную нужно перенести в файл настроек
    int numberLength = 18;// Эту переменную нужно перенести в файл настроек
//    int byteLength = 5;// Эту переменную нужно перенести в файл настроек
    int byteLengthBin = 1;
    int byteLengthOct = 1;
    int byteLengthHex = 5;
    private StringBuilder mainDisplayTmp;
    private float mainTextSize;

    private boolean fixMode;
    private boolean withZeros;
    private boolean sciMode;
    private NumberFormat normForm;
    private NumberFormat sciForm;

    private Preferences preferences;

    public static final Font DEFAULT_FONT = new Font("Tahoma", 0, L_TEXT_SIZE);
    public static final Font SMALL_FONT = new Font("Tahoma", 0, M_TEXT_SIZE);
    public static final Font SMALLEST_FONT = new Font("Tahoma", 0, S_TEXT_SIZE);

    Font sevenSegmentFont1;
    Font sevenSegmentFont2;
    Font sevenSegmentFont3;

    MainDisplay(JTextField mainDisplay, Preferences preferences) {        
        this.mainDisplay = mainDisplay;
        this.preferences = preferences;
        sevenSegmentFont1 = DEFAULT_FONT;   // На всякий случай, рабочими шрифтами
        sevenSegmentFont2 = SMALL_FONT;     // делаем шрифты установленные в системе
        sevenSegmentFont3 = SMALLEST_FONT;

        mainTextSize = mainDisplay.getFont().getSize();
//        int backgroundColor = ContextCompat.getColor(context, R.color.colorLCDBackground);
//        int fontColor = ContextCompat.getColor(context, R.color.colorLCDFont);

        fixMode = false;
        withZeros = false;

        // Теперь создаем рабочие шрифты из тех, что лежат в ресурсах
        // Если нужного шрифта нет в ресурсах, то выводим сообщение об ошибке и
        // Продолжаем работать на системных шрифтах
        try {
            sevenSegmentFont1 = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("fonts/7segment.ttf")).deriveFont(Font.PLAIN, L_TEXT_SIZE);

            sevenSegmentFont2 = sevenSegmentFont1.deriveFont(Font.PLAIN, M_TEXT_SIZE);
            sevenSegmentFont3 = sevenSegmentFont1.deriveFont(Font.PLAIN, S_TEXT_SIZE);
        } catch (FileNotFoundException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        } catch (FontFormatException | IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }

        setFixModeScale(getFixModeScale());

        sciMode = false;
        normForm = new DecimalFormat("##############.##############");
        switch (exponentLength) {
            case 1:
                sciForm = new DecimalFormat("0.##############E0");
                break;
            case 2:
                sciForm = new DecimalFormat("0.##############E00");
                break;
            case 3:
                sciForm = new DecimalFormat("0.##############E000");
                break;
        }
        L.d(TAG, "MainDisplay создан");
        L.d(TAG, "mainTextSize = " + mainTextSize);
    }

    void setFixModeScale(int fixModeScale) {
        preferences.setFixMode(fixModeScale);
        L.d(TAG, "Занесли значение режима округления в настройки: " + fixModeScale);
        setFixModeFlags(fixModeScale);
    }

    int getFixModeScale() {
        int fixModeScale = preferences.getFixMode();
        L.d(TAG, "Получаем значение режима округления из настроек: " + fixModeScale);
        setFixModeFlags(fixModeScale);
        return fixModeScale;
    }

    private void setFixModeFlags(int fixModeScale) {
        fixMode = true;
        withZeros = true;
        if (fixModeScale == -1) {
            fixMode = false;
            withZeros = false;
        }
    }

    void switchSciMode() {
        sciMode = !sciMode;
    }

    void offSciMode() {
        sciMode = false;
    }

    public void onSciMode() {
        sciMode = true;
    }

    void printArgX(ArgX argXOriginal) {
        L.d(TAG, "Начинаем собирать строку для отображения на экране.");
        if (argXOriginal.isVirgin()) {
            printVirginArgX(argXOriginal);
            return;
        }
        if (argXOriginal.isEditable()) {
            printEditableArgX(argXOriginal);
            return;
        }
        ArgX argX = new ArgX();

        if (sciMode
                || (Long.parseLong(argXOriginal.getExponent().toString())
                + argXOriginal.getMantissaFractionalPart().length()
                + argXOriginal.getMantissaIntegerPart().length()) > numberLength) {// Если работаем в режиме SCI или
            // если значение экспоненты больше чем длины числа которую можно отобразить на экране или
            // если
            L.d(TAG, "Exponent: " + Long.parseLong(argXOriginal.getExponent().toString()));
            L.d(TAG, "MantissaFractionalPart length: " + argXOriginal.getMantissaFractionalPart().length());
            L.d(TAG, "MantissaIntegerPart length: " + argXOriginal.getMantissaIntegerPart().length());
            L.d(TAG, "Работаем в режиме SCI");
            StringBuilder sb = new StringBuilder(argXOriginal.getArgXSB());
            L.d(TAG, "Взяли argXOriginal: " + sb);
            double num = Double.parseDouble(sb.toString());
            String str = null;
            try {
                str = sciForm.format(num);
            } catch (Exception e) {
                L.d(TAG, "Ошибка: " + e);
            }
            L.d(TAG, "Преобразовали к формату SCI: " + str);
            argX.setFromString(str);
            L.d(TAG, "argX собран для режима SCI");
        } else {
            L.d(TAG, "Работаем в режиме NORMAL");
            StringBuilder sb = new StringBuilder(argXOriginal.getArgXSB());
            L.d(TAG, "Взяли argXOriginal: " + sb);
            double num = Double.parseDouble(sb.toString());
            String str = null;
            try {
                str = normForm.format(num);
            } catch (Exception e) {
                L.d(TAG, "Ошибка: " + e);
            }
            L.d(TAG, "Преобразовали к формату NORMAL: " + str);
            argX.setFromString(str);
            L.d(TAG, "argX собран для режима NORMAL");
        }

        mainDisplayTmp = new StringBuilder("");
        if (argX.isSign()) { // Если есть минус перед мантиссой, то добавляем его
            mainDisplayTmp.append("-");
            L.d(TAG, "Добавили минус перед мантиссой.");
            L.d(TAG, mainDisplayTmp.toString());
        }
        if (argX.getMantissaIntegerPart().length() == 0) { //Если нет целой части мантиссы, то выводим 0
            mainDisplayTmp.append("0");
            L.d(TAG, "Нет целой части мантиссы, добавили ноль.");
            L.d(TAG, mainDisplayTmp.toString());
        } else {
            mainDisplayTmp.append(argX.getMantissaIntegerPart());// Теперь добавляем целую часть мантиссы
            L.d(TAG, "Добавили целую часть мантиссы.");
            L.d(TAG, mainDisplayTmp.toString());
        }

        mainDisplayTmp.append(".");// Всегда добавляем точку в конец целой части
        L.d(TAG, "Добавили точку в конец целой части.");
        L.d(TAG, mainDisplayTmp.toString());
        if (argX.isMantissaFractionalPart() || fixMode) { // Если есть дробная часть мантиссы, то добавляем ее после точки, если включен режим FIX, то дополняем нулями

            L.d(TAG, "Есть дробная часть мантиссы.");

            if (argX.getMantissaFractionalPartinDouble() != 0.0 || fixMode) {// и если нет дробной части мантиссы то ничего не добавляем
                int scale = numberLength - argX.getMantissaIntegerPart().length();
                if (argX.isExponent()) {
                    scale = scale - exponentLength;
                }
                withZeros = false;
                if (fixMode) {
                    scale = getFixModeScale();
                    withZeros = true;
                }

                mainDisplayTmp.append(argX.getRoundedMantissaFracPart(scale, withZeros));//Добавляем округленную мантиссу
            }
            L.d(TAG, "Добавили дробную часть мантиссы.");
            L.d(TAG, mainDisplayTmp.toString());

        }

        if (argX.isExponent()) { // Если есть экспонента, то добавляем и ее
            L.d(TAG, "Добавляем экспоненциальную часть.");

            if (argX.isExponentSign()) { // Если есть минус перед экспонентой, то добавляем его
                mainDisplayTmp.append("-");
                L.d(TAG, "Добавили минус перед экспонентой.");
                L.d(TAG, mainDisplayTmp.toString());
            } else {
                mainDisplayTmp.append(" ");
                L.d(TAG, "Минуса нет, добавили пробел перед экспонентой.");
                L.d(TAG, mainDisplayTmp.toString());
            }
            //Добавляем ведущие нули
            for (int i = exponentLength - argX.getExponent().length(); i > 0; i--) {
                mainDisplayTmp.append("0");
                L.d(TAG, "Добавили ноль.");
                L.d(TAG, mainDisplayTmp.toString());
            }
            mainDisplayTmp.append(argX.getExponent());// Теперь добавляем экспоненту
            L.d(TAG, "Добавили экспоненту.");
            L.d(TAG, mainDisplayTmp.toString());
        }
        L.d(TAG, "В результате на дисплей выводим: " + mainDisplayTmp.toString());

//        if (mainDisplayTmp.length() < 12) {
//            mainDisplay.setFont(sevenSegmentFont1);
//        }
//
//        if (mainDisplayTmp.length() >= 12) {
//            mainDisplay.setFont(sevenSegmentFont2);
//        }
//
//        if (mainDisplayTmp.length() > 20) {
//            mainDisplay.setFont(sevenSegmentFont3);
//        }
        setTextSize(mainDisplayTmp.length());

        mainDisplay.setText("" + mainDisplayTmp);
    }

    private void printEditableArgX(ArgX argX) {
        L.d(TAG, "Начинаем собирать редактируемую строку для отображения на экране.");
        mainDisplayTmp = new StringBuilder("");
        if (argX.isSign()) { // Если есть минус перед мантиссой, то добавляем его
            mainDisplayTmp.append("-");
            L.d(TAG, "Добавили минус перед мантиссой.");
            L.d(TAG, mainDisplayTmp.toString());
        }
        if (argX.getMantissaIntegerPart().length() == 0) { //Если нет целой части мантиссы, то выводим 0
            mainDisplayTmp.append("0");
            L.d(TAG, "Нет целой части мантиссы, добавили ноль.");
            L.d(TAG, mainDisplayTmp.toString());
        } else {
            mainDisplayTmp.append(argX.getMantissaIntegerPart());// Теперь добавляем целую часть мантиссы
            L.d(TAG, "Добавили целую часть мантиссы.");
            L.d(TAG, mainDisplayTmp.toString());
        }
        mainDisplayTmp.append(".");// Всегда добавляем точку в конец целой части
        L.d(TAG, "Добавили точку в конец целой части.");
        L.d(TAG, mainDisplayTmp.toString());
        if (argX.isMantissaFractionalPart()) { // Если есть дробная часть мантиссы, то добавляем ее после точки
            L.d(TAG, "Есть дробная часть мантиссы.");

            //Добавляем дробную часть мантиссы
            mainDisplayTmp.append(argX.getMantissaFractionalPart());

        }
        if (argX.isExponent()) { // Если есть экспонента, то добавляем и ее
            L.d(TAG, "Добавляем экспоненциальную часть.");

            if (argX.isExponentSign()) { // Если есть минус перед экспонентой, то добавляем его
                mainDisplayTmp.append("-");
                L.d(TAG, "Добавили минус перед экспонентой.");
                L.d(TAG, mainDisplayTmp.toString());
            } else {
                mainDisplayTmp.append(" ");
                L.d(TAG, "Минуса нет, добавили пробел перед экспонентой.");
                L.d(TAG, mainDisplayTmp.toString());
            }
            //Добавляем ведущие нули
            for (int i = exponentLength - argX.getExponent().length(); i > 0; i--) {
                mainDisplayTmp.append("0");
                L.d(TAG, "Добавили ноль.");
                L.d(TAG, mainDisplayTmp.toString());
            }
            mainDisplayTmp.append(argX.getExponent());// Теперь добавляем экспоненту
            L.d(TAG, "Добавили экспоненту.");
            L.d(TAG, mainDisplayTmp.toString());
        }
        L.d(TAG, "В результате на дисплей выводим: " + mainDisplayTmp.toString());

//        if (mainDisplayTmp.length() < 12) {
//            mainDisplay.setFont(sevenSegmentFont1);
//        }
//
//        if (mainDisplayTmp.length() >= 12) {
//            mainDisplay.setFont(sevenSegmentFont2);
//        }
//
//        if (mainDisplayTmp.length() > 20) {
//            mainDisplay.setFont(sevenSegmentFont3);
//
//        }
        setTextSize(mainDisplayTmp.length());

        mainDisplay.setText("" + mainDisplayTmp);
    }

    private void printVirginArgX(ArgX argXOrigin) {
        L.d(TAG, "Начинаем собирать пустую строку для отображения на экране.");
        ArgX argX = new ArgX();
        if (sciMode) {
            L.d(TAG, "Работаем в режиме SCI");
            StringBuilder sb = new StringBuilder(argXOrigin.getArgXSB());
            L.d(TAG, "Взяли argXOriginal: " + sb);
            double num = Double.parseDouble(sb.toString());
            String str = null;
            try {
                str = sciForm.format(num);
            } catch (Exception e) {
                L.d(TAG, "Ошибка: " + e);
            }
            L.d(TAG, "Преобразовали к формату SCI: " + str);
            argX.setFromString(str);
            L.d(TAG, "argX собран для режима SCI");
        } else {
            L.d(TAG, "Работаем в режиме NORMAL");
            StringBuilder sb = new StringBuilder(argXOrigin.getArgXSB());
            L.d(TAG, "Взяли argXOriginal: " + sb);
            double num = Double.parseDouble(sb.toString());
            String str = null;
            try {
                str = normForm.format(num);
            } catch (Exception e) {
                L.d(TAG, "Ошибка: " + e);
            }
            L.d(TAG, "Преобразовали к формату NORMAL: " + str);
            argX.setFromString(str);
            L.d(TAG, "argX собран для режима NORMAL");
        }

        mainDisplayTmp = new StringBuilder("");
        mainDisplayTmp.append("0");
        L.d(TAG, "Нет целой части мантиссы, добавили ноль.");
        L.d(TAG, mainDisplayTmp.toString());

        mainDisplayTmp.append(".");// Всегда добавляем точку в конец целой части
        L.d(TAG, "Добавили точку в конец целой части.");
        L.d(TAG, mainDisplayTmp.toString());

        //если включен режим FIX, то дополняем нулями
        if (fixMode) {
            mainDisplayTmp.append(argX.getRoundedMantissaFracPart(getFixModeScale(), withZeros));//Добавляем округленную мантиссу
            L.d(TAG, "Включен режим FIX, поэтому добавляем нули в дробную часть.");
            L.d(TAG, mainDisplayTmp.toString());
        }
        if (argX.isExponent()) { // Если есть экспонента, то добавляем и ее
            L.d(TAG, "Добавляем экспоненциальную часть.");

            if (argX.isExponentSign()) { // Если есть минус перед экспонентой, то добавляем его
                mainDisplayTmp.append("-");
                L.d(TAG, "Добавили минус перед экспонентой.");
                L.d(TAG, mainDisplayTmp.toString());
            } else {
                mainDisplayTmp.append(" ");
                L.d(TAG, "Минуса нет, добавили пробел перед экспонентой.");
                L.d(TAG, mainDisplayTmp.toString());
            }
            //Добавляем ведущие нули
            for (int i = exponentLength - argX.getExponent().length(); i > 0; i--) {
                mainDisplayTmp.append("0");
                L.d(TAG, "Добавили ноль.");
                L.d(TAG, mainDisplayTmp.toString());
            }
            mainDisplayTmp.append(argX.getExponent());// Теперь добавляем экспоненту
            L.d(TAG, "Добавили экспоненту.");
            L.d(TAG, mainDisplayTmp.toString());
        }
        mainDisplay.setFont(sevenSegmentFont1);
        L.d(TAG, "В результате на дисплей выводим: " + mainDisplayTmp.toString());
        mainDisplay.setText("" + mainDisplayTmp);

    }

    void printArgX(ArgXBin argX) {
        L.d(TAG, "Начинаем собирать ArgXBin строку для отображения на экране.");
        L.d(TAG, "В ArgXBin строка: " + argX.getNumber());
        L.d(TAG, "Знак: " + argX.isSign());
        L.d(TAG, "Редактируемое: " + argX.isEditable());
        L.d(TAG, "Пустое: " + argX.isVirgin());
        if (argX.isVirgin()) {
            printVirginArgX(argX);
            return;
        }
        if (argX.isEditable()) {
            printEditableArgX(argX);
            return;
        }

        mainDisplayTmp = new StringBuilder("");

        if (argX.getNumber().length() == 0) { //Если нет числа, то выводим 0
            mainDisplayTmp.append("0");
            L.d(TAG, "Нет числа, добавили ноль.");
            L.d(TAG, mainDisplayTmp.toString());
        } else {
            L.d(TAG, "sign: " + argX.isSign());
            if (argX.isSign()) { // Если есть минус, то меняем отображаемое число
                L.d(TAG, "Есть минус, меняем отображаемое число: " + mainDisplayTmp);
                mainDisplayTmp.append(argX.getNumber());// Добавляем число
                while (mainDisplayTmp.length() < byteLengthBin * 8) {//пока число короче чем необходимо, дополняем нулями
                    mainDisplayTmp.insert(0, '0');
                }
                L.d(TAG, "Добавили впереди нулей: " + mainDisplayTmp);
                L.d(TAG, "Инвертируем цифры числа побитово.");
                for (int i = 0; i < mainDisplayTmp.length(); i++) {
                    switch (mainDisplayTmp.charAt(i)) {
                        case '0':
                            mainDisplayTmp.setCharAt(i, '1');
                            break;
                        case '1':
                            mainDisplayTmp.setCharAt(i, '0');
                            break;
                    }
                }
                L.d(TAG, "Положительное число в прямом коде: " + mainDisplayTmp);
                long longNumber = Long.parseLong(mainDisplayTmp.toString(), 2);
                L.d(TAG, "Преобразовали к типу long: " + longNumber);
                longNumber++;
                L.d(TAG, "Увеличили на еденицу: " + longNumber);
                mainDisplayTmp = new StringBuilder(Long.toBinaryString(longNumber));
                L.d(TAG, "Преобразовали к типу StringBuilder: " + mainDisplayTmp);
            } else {
                mainDisplayTmp.append(argX.getNumber());// Иначе просто добавляем число
                L.d(TAG, "Добавили число.");
                L.d(TAG, mainDisplayTmp.toString());
            }
        }

        mainDisplayTmp.append(".");// Всегда добавляем точку в конец
        L.d(TAG, "Добавили точку в конец.");
        L.d(TAG, mainDisplayTmp.toString());

        L.d(TAG, "В результате на дисплей выводим: " + mainDisplayTmp.toString());

//        if (mainDisplayTmp.length() < 12) {
//            mainDisplay.setFont(sevenSegmentFont1);
//        }
//
//        if (mainDisplayTmp.length() >= 12) {
//            mainDisplay.setFont(sevenSegmentFont2);
//        }
//
//        if (mainDisplayTmp.length() > 20) {
//            mainDisplay.setFont(sevenSegmentFont3);
//
//        }
        setTextSize(mainDisplayTmp.length());
        
        mainDisplay.setText("" + mainDisplayTmp);

    }

    private void printEditableArgX(ArgXBin argX) {
        L.d(TAG, "Начинаем собирать редактируемую ArgXBin строку для отображения на экране.");
        mainDisplayTmp = new StringBuilder("");
        if (argX.getNumber().length() == 0) { //Если нет числа, то выводим 0
            mainDisplayTmp.append("0");
            L.d(TAG, "Нет числа, добавили ноль.");
            L.d(TAG, mainDisplayTmp.toString());
        } else {
            L.d(TAG, "sign: " + argX.isSign());
            if (argX.isSign()) { // Если есть минус, то меняем отображаемое число
                L.d(TAG, "Есть минус, меняем отображаемое число.");
                mainDisplayTmp.append(Long.toBinaryString(argX.getLong(byteLengthBin)));
            } else {
                mainDisplayTmp.append(argX.getNumber());// Иначе просто добавляем число
                L.d(TAG, "Добавили число.");
                L.d(TAG, mainDisplayTmp.toString());
            }
        }
        mainDisplayTmp.append(".");// Всегда добавляем точку в конец
        L.d(TAG, "Добавили точку в конец.");
        L.d(TAG, mainDisplayTmp.toString());

        L.d(TAG, "В результате на дисплей выводим: " + mainDisplayTmp.toString());

//        if (mainDisplayTmp.length() < 12) {
//            mainDisplay.setFont(sevenSegmentFont1);
//        }
//
//        if (mainDisplayTmp.length() >= 12) {
//            mainDisplay.setFont(sevenSegmentFont2);
//        }
//
//        if (mainDisplayTmp.length() > 20) {
//            mainDisplay.setFont(sevenSegmentFont3);
//
//        }
        setTextSize(mainDisplayTmp.length());
        
        mainDisplay.setText("" + mainDisplayTmp);
    }

    private void printVirginArgX(ArgXBin argXOrigin) {
        L.d(TAG, "Начинаем собирать пустую ArgXBin строку для отображения на экране.");

        mainDisplayTmp = new StringBuilder("");
        mainDisplayTmp.append("0");
        L.d(TAG, "Нет числа, добавили ноль.");
        L.d(TAG, mainDisplayTmp.toString());

        mainDisplayTmp.append(".");// Всегда добавляем точку в конец целой части
        L.d(TAG, "Добавили точку в конец.");
        L.d(TAG, mainDisplayTmp.toString());

        mainDisplay.setFont(sevenSegmentFont1);
        L.d(TAG, "В результате на дисплей выводим: " + mainDisplayTmp.toString());
        mainDisplay.setText("" + mainDisplayTmp);

    }

    void printArgX(ArgXOct argX) {
        L.d(TAG, "Начинаем собирать ArgXOct строку для отображения на экране.");
        L.d(TAG, "В ArgXOct строка: " + argX.getNumber());
        L.d(TAG, "Знак: " + argX.isSign());
        L.d(TAG, "Редактируемое: " + argX.isEditable());
        L.d(TAG, "Пустое: " + argX.isVirgin());
        if (argX.isVirgin()) {
            printVirginArgX(argX);
            return;
        }
        if (argX.isEditable()) {
            printEditableArgX(argX);
            return;
        }

        mainDisplayTmp = new StringBuilder("");

        if (argX.getNumber().length() == 0) { //Если нет числа, то выводим 0
            mainDisplayTmp.append("0");
            L.d(TAG, "Нет числа, добавили ноль.");
            L.d(TAG, mainDisplayTmp.toString());
        } else {
            L.d(TAG, "sign: " + argX.isSign());
            if (argX.isSign()) { // Если есть минус, то меняем отображаемое число
                L.d(TAG, "Есть минус, меняем отображаемое число: " + mainDisplayTmp);
                mainDisplayTmp.append(argX.getNumber());// Добавляем число
                while (mainDisplayTmp.length() < byteLengthOct * 8 / 3) {//пока число короче чем необходимо, дополняем нулями
                    mainDisplayTmp.insert(0, '0');
                }
                L.d(TAG, "Добавили впереди нулей: " + mainDisplayTmp);
                L.d(TAG, "Инвертируем цифры числа побитово.");
                for (int i = 0; i < mainDisplayTmp.length(); i++) {
                    switch (mainDisplayTmp.charAt(i)) {
                        case '0':
                            mainDisplayTmp.setCharAt(i, '7');
                            break;
                        case '1':
                            mainDisplayTmp.setCharAt(i, '6');
                            break;
                        case '2':
                            mainDisplayTmp.setCharAt(i, '5');
                            break;
                        case '3':
                            mainDisplayTmp.setCharAt(i, '4');
                            break;
                        case '4':
                            mainDisplayTmp.setCharAt(i, '3');
                            break;
                        case '5':
                            mainDisplayTmp.setCharAt(i, '2');
                            break;
                        case '6':
                            mainDisplayTmp.setCharAt(i, '1');
                            break;
                        case '7':
                            mainDisplayTmp.setCharAt(i, '0');
                            break;
                    }
                }
                L.d(TAG, "Положительное число в прямом коде: " + mainDisplayTmp);
                long longNumber = Long.parseLong(mainDisplayTmp.toString(), 8);
                L.d(TAG, "Преобразовали к типу long: " + longNumber);
                longNumber++;
                L.d(TAG, "Увеличили на еденицу: " + longNumber);
                mainDisplayTmp = new StringBuilder(Long.toOctalString(longNumber));
                L.d(TAG, "Преобразовали к типу StringBuilder: " + mainDisplayTmp);

            } else {
                mainDisplayTmp.append(argX.getNumber());// Иначе просто добавляем число
                L.d(TAG, "Добавили число.");
                L.d(TAG, mainDisplayTmp.toString());
            }
        }

        mainDisplayTmp.append(".");// Всегда добавляем точку в конец
        L.d(TAG, "Добавили точку в конец.");
        L.d(TAG, mainDisplayTmp.toString());

        L.d(TAG, "В результате на дисплей выводим: " + mainDisplayTmp.toString());

//        if (mainDisplayTmp.length() < 12) {
//            mainDisplay.setFont(sevenSegmentFont1);
//        }
//
//        if (mainDisplayTmp.length() >= 12) {
//            mainDisplay.setFont(sevenSegmentFont2);
//        }
//
//        if (mainDisplayTmp.length() > 20) {
//            mainDisplay.setFont(sevenSegmentFont3);
//
//        }
        setTextSize(mainDisplayTmp.length());

        mainDisplay.setText("" + mainDisplayTmp);
    }

    private void printEditableArgX(ArgXOct argX) {
        L.d(TAG, "Начинаем собирать редактируемую ArgXOct строку для отображения на экране.");
        mainDisplayTmp = new StringBuilder("");
        if (argX.getNumber().length() == 0) { //Если нет числа, то выводим 0
            mainDisplayTmp.append("0");
            L.d(TAG, "Нет числа, добавили ноль.");
            L.d(TAG, mainDisplayTmp.toString());
        } else {
            L.d(TAG, "sign: " + argX.isSign());
            if (argX.isSign()) { // Если есть минус, то меняем отображаемое число
                L.d(TAG, "Есть минус, меняем отображаемое число.");
                mainDisplayTmp.append(Long.toOctalString(argX.getLong()));
            } else {
                mainDisplayTmp.append(argX.getNumber());// Иначе просто добавляем число
                L.d(TAG, "Добавили число.");
                L.d(TAG, mainDisplayTmp.toString());
            }
        }
        mainDisplayTmp.append(".");// Всегда добавляем точку в конец
        L.d(TAG, "Добавили точку в конец.");
        L.d(TAG, mainDisplayTmp.toString());

        L.d(TAG, "В результате на дисплей выводим: " + mainDisplayTmp.toString());

//        if (mainDisplayTmp.length() < 12) {
//            mainDisplay.setFont(sevenSegmentFont1);
//        }
//
//        if (mainDisplayTmp.length() >= 12) {
//            mainDisplay.setFont(sevenSegmentFont2);
//        }
//
//        if (mainDisplayTmp.length() > 20) {
//            mainDisplay.setFont(sevenSegmentFont3);
//
//        }
        setTextSize(mainDisplayTmp.length());

        mainDisplay.setText("" + mainDisplayTmp);
    }

    private void printVirginArgX(ArgXOct argXOrigin) {
        L.d(TAG, "Начинаем собирать пустую ArgXOct строку для отображения на экране.");

        mainDisplayTmp = new StringBuilder("");
        mainDisplayTmp.append("0");
        L.d(TAG, "Нет числа, добавили ноль.");
        L.d(TAG, mainDisplayTmp.toString());

        mainDisplayTmp.append(".");// Всегда добавляем точку в конец целой части
        L.d(TAG, "Добавили точку в конец.");
        L.d(TAG, mainDisplayTmp.toString());

        mainDisplay.setFont(sevenSegmentFont1);
        L.d(TAG, "В результате на дисплей выводим: " + mainDisplayTmp.toString());
        mainDisplay.setText("" + mainDisplayTmp);

    }

    void printArgX(ArgXHex argX) {
        L.d(TAG, "Начинаем собирать ArgXHex строку для отображения на экране.");
        L.d(TAG, "В ArgXHex строка: " + argX.getNumber());
        L.d(TAG, "Знак: " + argX.isSign());
        L.d(TAG, "Редактируемое: " + argX.isEditable());
        L.d(TAG, "Пустое: " + argX.isVirgin());
        if (argX.isVirgin()) {
            printVirginArgX(argX);
            return;
        }
        if (argX.isEditable()) {
            printEditableArgX(argX);
            return;
        }

        mainDisplayTmp = new StringBuilder("");

        if (argX.getNumber().length() == 0) { //Если нет числа, то выводим 0
            mainDisplayTmp.append("0");
            L.d(TAG, "Нет числа, добавили ноль.");
            L.d(TAG, mainDisplayTmp.toString());
        } else {
            L.d(TAG, "sign: " + argX.isSign());
            if (argX.isSign()) { // Если есть минус, то меняем отображаемое число
                L.d(TAG, "Есть минус, меняем отображаемое число: " + mainDisplayTmp);
                mainDisplayTmp.append(argX.getNumber());// Добавляем число
                while (mainDisplayTmp.length() < byteLengthHex * 2) {//пока число короче чем необходимо, дополняем нулями
                    mainDisplayTmp.insert(0, '0');
                }
                L.d(TAG, "Добавили впереди нулей: " + mainDisplayTmp);
                L.d(TAG, "Инвертируем цифры числа побитово.");
                for (int i = 0; i < mainDisplayTmp.length(); i++) {
                    switch (mainDisplayTmp.charAt(i)) {
                        case '0':
                            mainDisplayTmp.setCharAt(i, 'F');
                            break;
                        case '1':
                            mainDisplayTmp.setCharAt(i, 'E');
                            break;
                        case '2':
                            mainDisplayTmp.setCharAt(i, 'D');
                            break;
                        case '3':
                            mainDisplayTmp.setCharAt(i, 'C');
                            break;
                        case '4':
                            mainDisplayTmp.setCharAt(i, 'B');
                            break;
                        case '5':
                            mainDisplayTmp.setCharAt(i, 'A');
                            break;
                        case '6':
                            mainDisplayTmp.setCharAt(i, '9');
                            break;
                        case '7':
                            mainDisplayTmp.setCharAt(i, '8');
                            break;
                        case '8':
                            mainDisplayTmp.setCharAt(i, '7');
                            break;
                        case '9':
                            mainDisplayTmp.setCharAt(i, '6');
                            break;
                        case 'A':
                            mainDisplayTmp.setCharAt(i, '5');
                            break;
                        case 'B':
                            mainDisplayTmp.setCharAt(i, '4');
                            break;
                        case 'C':
                            mainDisplayTmp.setCharAt(i, '3');
                            break;
                        case 'D':
                            mainDisplayTmp.setCharAt(i, '2');
                            break;
                        case 'E':
                            mainDisplayTmp.setCharAt(i, '1');
                            break;
                        case 'F':
                            mainDisplayTmp.setCharAt(i, '0');
                            break;
                        case 'a':
                            mainDisplayTmp.setCharAt(i, '5');
                            break;
                        case 'b':
                            mainDisplayTmp.setCharAt(i, '4');
                            break;
                        case 'c':
                            mainDisplayTmp.setCharAt(i, '3');
                            break;
                        case 'd':
                            mainDisplayTmp.setCharAt(i, '2');
                            break;
                        case 'e':
                            mainDisplayTmp.setCharAt(i, '1');
                            break;
                        case 'f':
                            mainDisplayTmp.setCharAt(i, '0');
                            break;
                    }
                }
                L.d(TAG, "Положительное число в прямом коде: " + mainDisplayTmp);
                long longNumber = Long.parseLong(mainDisplayTmp.toString(), 16);
                L.d(TAG, "Преобразовали к типу long: " + longNumber);
                longNumber++;
                L.d(TAG, "Увеличили на еденицу: " + longNumber);
                mainDisplayTmp = new StringBuilder(Long.toHexString(longNumber));
                L.d(TAG, "Преобразовали к типу StringBuilder: " + mainDisplayTmp);

            } else {
                mainDisplayTmp.append(argX.getNumber());// Иначе просто добавляем число
                L.d(TAG, "Добавили число.");
                L.d(TAG, mainDisplayTmp.toString());
            }
        }
        for (int i = 0; i < mainDisplayTmp.length(); i++) {
            switch (mainDisplayTmp.charAt(i)) {
                case 'a':
                    mainDisplayTmp.setCharAt(i, 'A');
                    break;
                case 'B':
                    mainDisplayTmp.setCharAt(i, 'b');
                    break;
                case 'c':
                    mainDisplayTmp.setCharAt(i, 'C');
                    break;
                case 'D':
                    mainDisplayTmp.setCharAt(i, 'd');
                    break;
                case 'e':
                    mainDisplayTmp.setCharAt(i, 'E');
                    break;
                case 'f':
                    mainDisplayTmp.setCharAt(i, 'F');
                    break;
            }
        }

        mainDisplayTmp.append(".");// Всегда добавляем точку в конец
        L.d(TAG, "Добавили точку в конец.");
        L.d(TAG, mainDisplayTmp.toString());

        L.d(TAG, "В результате на дисплей выводим: " + mainDisplayTmp.toString());

//        if (mainDisplayTmp.length() < 12) {
//            mainDisplay.setFont(sevenSegmentFont1);
//        }
//
//        if (mainDisplayTmp.length() >= 12) {
//            mainDisplay.setFont(sevenSegmentFont2);
//        }
//
//        if (mainDisplayTmp.length() > 20) {
//            mainDisplay.setFont(sevenSegmentFont3);
//
//        }
        setTextSize(mainDisplayTmp.length());

        mainDisplay.setText("" + mainDisplayTmp);
    }

    private void printEditableArgX(ArgXHex argX) {
        L.d(TAG, "Начинаем собирать редактируемую ArgXHex строку для отображения на экране.");
        mainDisplayTmp = new StringBuilder("");
        if (argX.getNumber().length() == 0) { //Если нет числа, то выводим 0
            mainDisplayTmp.append("0");
            L.d(TAG, "Нет числа, добавили ноль.");
            L.d(TAG, mainDisplayTmp.toString());
        } else {
            L.d(TAG, "sign: " + argX.isSign());
            if (argX.isSign()) { // Если есть минус, то меняем отображаемое число
                L.d(TAG, "Есть минус, меняем отображаемое число.");
                mainDisplayTmp.append(Long.toHexString(argX.getLong(byteLengthHex)));
            } else {
                mainDisplayTmp.append(argX.getNumber());// Иначе просто добавляем число
                L.d(TAG, "Добавили число.");
                L.d(TAG, mainDisplayTmp.toString());
            }
        }
        mainDisplayTmp.append(".");// Всегда добавляем точку в конец
        L.d(TAG, "Добавили точку в конец.");
        L.d(TAG, mainDisplayTmp.toString());

        L.d(TAG, "В результате на дисплей выводим: " + mainDisplayTmp.toString());

//        if (mainDisplayTmp.length() < 12) {
//            mainDisplay.setFont(sevenSegmentFont1);
//        }
//
//        if (mainDisplayTmp.length() >= 12) {
//            mainDisplay.setFont(sevenSegmentFont2);
//        }
//
//        if (mainDisplayTmp.length() > 20) {
//            mainDisplay.setFont(sevenSegmentFont3);
//
//        }
        setTextSize(mainDisplayTmp.length());

        mainDisplay.setText("" + mainDisplayTmp);
    }

    private void printVirginArgX(ArgXHex argXOrigin) {
        L.d(TAG, "Начинаем собирать пустую ArgXHex строку для отображения на экране.");
        mainDisplayTmp = new StringBuilder("");
        mainDisplayTmp.append("0");
        L.d(TAG, "Нет числа, добавили ноль.");
        L.d(TAG, mainDisplayTmp.toString());

        mainDisplayTmp.append(".");// Всегда добавляем точку в конец целой части
        L.d(TAG, "Добавили точку в конец.");
        L.d(TAG, mainDisplayTmp.toString());

        mainDisplay.setFont(sevenSegmentFont1);
        L.d(TAG, "В результате на дисплей выводим: " + mainDisplayTmp.toString());
        mainDisplay.setText("" + mainDisplayTmp);

    }

    private void setTextSize(int textLength) {
        if (textLength <= L_TEXT_LENGTH) {
            L.d(TAG, "Длинна строки меньше " + L_TEXT_LENGTH);
            mainDisplay.setFont(sevenSegmentFont1);
            L.d(TAG, "Установлен размер текста " + L_TEXT_SIZE);
            return;
        }

        if (textLength > L_TEXT_LENGTH  && textLength <= M_TEXT_LENGTH) {
            L.d(TAG, "Длинна строки больше " + L_TEXT_LENGTH);
            mainDisplay.setFont(sevenSegmentFont2);
            L.d(TAG, "Установлен размер текста " + M_TEXT_SIZE);
            return;
        }

        if (textLength > M_TEXT_LENGTH) {
            L.d(TAG, "Длинна строки больше " + M_TEXT_LENGTH);
            mainDisplay.setFont(sevenSegmentFont3);
            L.d(TAG, "Установлен размер текста " + S_TEXT_SIZE);            
        }
    }
}
