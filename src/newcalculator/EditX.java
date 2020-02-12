/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcalculator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.math3.complex.Complex;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 *
 * @author User
 */
public class EditX {

    private static final String TAG = "EditX";

    private CustomToast customToast;
    private StatusDisplay statusDisplay;
    private Status status;
    private Mode mode;
    private Angle angle;
    private MemoryStore memoryStore;
    private MainDisplay mainDisplay;
    private StackCalculator stackCalculator;
    private EditX editX;
    private EditXBin editXBin;
    private EditXOct editXOct;
    private EditXHex editXHex;
    private ComplexStackCalculator complexStackCalculator;
    private StatisticMode statisticMode;
    private InputDriver inputDriver;
    private MainActivity activity;
    private ProtocolJFrame protocol;

    private boolean fixModeNumberWait;

    private ArgX argX;
    private ArgX argA;
    private ArgX argB;

    private boolean newInput;
    private boolean calcpress;
    private boolean calc;

    private ArgX memory;

    Object objStore[];

    EditX(Object objStore[]) {
        this.objStore = objStore;
        statusDisplay = (StatusDisplay) objStore[MainActivity.STATUS_DISPLAY];
        status = (Status) objStore[MainActivity.STATUS];
        mode = (Mode) objStore[MainActivity.MODE];
        angle = (Angle) objStore[MainActivity.ANGLE];
        memoryStore = (MemoryStore) objStore[MainActivity.MEMORY];
        mainDisplay = (MainDisplay) objStore[MainActivity.MAIN_DISPLAY];
        stackCalculator = (StackCalculator) objStore[MainActivity.STACK_CALCULATOR];
        editX = (EditX) objStore[MainActivity.EDIT_X];
        editXBin = (EditXBin) objStore[MainActivity.EDIT_X_BIN];
        editXOct = (EditXOct) objStore[MainActivity.EDIT_X_OCT];
        editXHex = (EditXHex) objStore[MainActivity.EDIT_X_HEX];
        complexStackCalculator = (ComplexStackCalculator) objStore[MainActivity.CPLX];
        statisticMode = (StatisticMode) objStore[MainActivity.SD];
        inputDriver = (InputDriver) objStore[MainActivity.INPUT_DRIVER];
        activity = (MainActivity) objStore[MainActivity.MAIN_ACTIVITY];
        protocol = (ProtocolJFrame) objStore[MainActivity.PROTOCOL];
        for (int i = 0; i < objStore.length; i++) {

            L.d(TAG, "В objStore[" + i + "]: " + objStore[i]);
        }
        customToast = new CustomToast(activity, "Проба");
        L.d(TAG, "Создали CustomToast");
        status.offError();
        L.d(TAG, "Выключили ошибку");
//        memory = new ArgX(memoryStore.getMemory());
//        L.d(TAG, "Создали memory");
//        setMemory(memory.getArgX());
//        L.d(TAG, "Включили отображение памяти, если нужно.");
        newInput = true;
        calcpress = false;
        fixModeNumberWait = false;
        newArg();
        L.d(TAG, "Создали новый ArgX.");
        argA = new ArgX();
        argB = new ArgX();
        makeArg();
        L.d(TAG, "Отобразили на дисплее.");
        L.d(TAG, "EditX успешно создан!");
    }

    void restart() {
        status.offError();
        status.setBracket(false);
        stackCalculator.restart();
        mainDisplay.offSciMode();
        newArg();
        calcpress = false;
        newInput = true;
        calc = false;
        argA = new ArgX();
        argB = new ArgX();
        makeArg();
    }

    private void newArg() {
        newInput = true;
        argX = new ArgX();
    }

    void add(char pressedKey) {
        mainDisplay.offSciMode();
        if (fixModeNumberWait) { //Если нажали на кнопку FIX
            L.d(TAG, "Устанавливаем режим округления");
            fixModeNumberWait = false; // выключаем режим ожидания
            L.d(TAG, "argX.isVirgin(): " + argX.isVirgin());
            L.d(TAG, "stackCalculator.isResult(): " + stackCalculator.isResult());
            if (!argX.isEditable() || !stackCalculator.isResult()) { // И если еще ничего не вводили или выведен ответ
                L.d(TAG, "Еще ничего не вводили или выведен ответ");
//                if (argX.isVirgin()) {
//
//                }
                mainDisplay.setFixModeScale(Character.digit(pressedKey, 10)); // то включаем режим FIX
                L.d(TAG, "Включаем режим FIX, выводим старое число в новом формате и выходим");
                makeArg();// выводим старое число в новом формате
                return; //и выходим
            }
        }
        if (!argX.isEditable()) { // Если флаг Editable выключен, то нужно создать новое пустое число
            stackCalculator.setResult(false);
            newArg();
        }
        if (((argX.getMantissaIntegerPart().length() + argX.getMantissaFractionalPart().length() + argX.getExponent().length()) >= mainDisplay.numberLength) && (!argX.isExponent())) {

            customToast.setToastText("Превышено максимальное число цифр");
            customToast.setToastImage(CustomToast.IC_WARNING_AMBER);
            customToast.show();
            return;
        }
        if (!argX.isExponent()) { //Если флаг argX.isExponent выключен, то редактируем мантиссу
            if (!argX.isMantissaFractionalPart()) { //Если флаг argX.isMantissaFractionalPart выключен, то редактируем целую часть мантиссы
                if (argX.getMantissaIntegerPart().length() < 1 && pressedKey == '0') { //Если мантисса пустая и нажат '0', то отображаем ничего не делаем
                    makeArg();
                    return;
                }
                if (argX.getMantissaIntegerPart().length() >= 15) { //Если дробная часть мантиссы уже 15 символов, то ничего не делаем
                    customToast.setToastText("Превышено максимальное число цифр целой части.");
                    customToast.setToastImage(CustomToast.IC_WARNING_AMBER);
                    customToast.show();
                    return;
                }
//                StringBuilder sb = argX.getMantissaIntegerPart();
//                sb.append(pressedKey);
//                argX.setMantissaIntegerPart(sb);
                argX.setMantissaIntegerPart(argX.getMantissaIntegerPart().append(pressedKey));

            } else { //Иначе редактируем дробную часть мантиссы
                if (argX.getMantissaFractionalPart().length() >= 16) { //Если дробная часть мантиссы уже 16 символов, то ничего не делаем
                    customToast.setToastText("Превышено максимальное число цифр дробной части.");
                    customToast.setToastImage(CustomToast.IC_WARNING_AMBER);
                    customToast.show();
                    return;
                }
                argX.setMantissaFractionalPart(argX.getMantissaFractionalPart().append(pressedKey));

            }
        } else {            //Иначе редактируем экспоненту
            if (argX.getExponent().length() > mainDisplay.exponentLength - 1) {

//                StringBuilder sb = argX.getExponent();
//                sb.deleteCharAt(0);
//                argX.setExponent(sb);
                argX.setExponent(argX.getExponent().deleteCharAt(0));

            }
            argX.setExponent(argX.getExponent().append(pressedKey));
        }
        argX.setNotVirgin();
        newInput = true;
        makeArg();
    }

    void setNumber(double doubleNumber) {
        argX.setDouble(doubleNumber);
        makeArg();
        L.d(TAG, "Отобразили на дисплее.");
    }

    void setFixMode() {
        fixModeNumberWait = true;
    }

    void switchSciMode() {
        if (!argX.isEditable() || argX.isVirgin()) {
            mainDisplay.switchSciMode();
            makeArg();
        }
    }

    void fromDeg() throws MyExceptions {
        int fixModeSave = mainDisplay.getFixModeScale();
        mainDisplay.setFixModeScale(6);
        argX = new ArgX(stackCalculator.fromDeg(argX));
        calcpress = true;
        newInput = true;
        makeArg();
        mainDisplay.setFixModeScale(fixModeSave);
    }

    void toDeg() throws MyExceptions {
        argX = new ArgX(stackCalculator.toDeg(argX));
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void sin() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult;//; // 
            cplxResult = complexStackCalculator.cplxSin(cplxNum);
            L.d(TAG, "sin(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.sin(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void arsh() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; //; // 
            cplxResult = complexStackCalculator.cplxArsh(cplxNum);
            L.d(TAG, "Arsh(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.arsh(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void asin() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; //; // 
            cplxResult = complexStackCalculator.cplxAsin(cplxNum);
            L.d(TAG, "asin(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.asin(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void sinh() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; //; // 
            cplxResult = complexStackCalculator.cplxSinh(cplxNum);
            L.d(TAG, "sinh(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.sinh(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void cos() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; //; // 
            cplxResult = complexStackCalculator.cplxCos(cplxNum);
            L.d(TAG, "cos(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.cos(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void arch() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; //; // 
            cplxResult = complexStackCalculator.cplxArch(cplxNum);
            L.d(TAG, "arch(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.arch(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void acos() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; // 
            cplxResult = complexStackCalculator.cplxAcos(cplxNum);
            L.d(TAG, "acos(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.acos(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void cosh() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; // 
            cplxResult = complexStackCalculator.cplxCosh(cplxNum);
            L.d(TAG, "cosh(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.cosh(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void tan() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; // 
            cplxResult = complexStackCalculator.cplxTan(cplxNum);
            L.d(TAG, "tan(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.tan(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void arth() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; // 
            cplxResult = complexStackCalculator.cplxArth(cplxNum);
            L.d(TAG, "arth(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.arth(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void atan() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; // 
            cplxResult = complexStackCalculator.cplxAtan(cplxNum);
            L.d(TAG, "atan(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.atan(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void tanh() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; // 
            cplxResult = complexStackCalculator.cplxTanh(cplxNum);
            L.d(TAG, "tanh(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.tanh(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void sqrt() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; // 
            cplxResult = complexStackCalculator.cplxSqrt(cplxNum);
            L.d(TAG, "sqrt(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.sqrt(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void cbrt() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; // 
            cplxResult = complexStackCalculator.cplxCbrt(cplxNum);
            L.d(TAG, "cbrt(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.cbrt(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void ln() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; // 
            cplxResult = complexStackCalculator.cplxLog(cplxNum);
            L.d(TAG, "ln(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.ln(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void exponent() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; // 
            cplxResult = complexStackCalculator.cplxExp(cplxNum);
            L.d(TAG, "exp(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.exp(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void log() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; // 
            cplxResult = complexStackCalculator.cplxLog10(cplxNum);
            L.d(TAG, "Log(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.log(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void _10x() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; // 
            cplxResult = complexStackCalculator.cplx10x(cplxNum);
            L.d(TAG, "10^(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator._10x(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void x2() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; // 
            cplxResult = complexStackCalculator.cplxPower2(cplxNum);
            L.d(TAG, "(" + cplxNum + ")^2 = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator.x2(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void _1_div_x() throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            Complex cplxResult; // 
            cplxResult = complexStackCalculator.cplxReciprocal(cplxNum);
            L.d(TAG, "1/(" + cplxNum + ") = " + cplxResult);
            argA = new ArgX(cplxResult.getReal());
            argB = new ArgX(cplxResult.getImaginary());
            argX = new ArgX(argA.getArgX());
        } else {
            argX = new ArgX(stackCalculator._1_div_x(argX.getArgX()));
        }
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void toA() {
        if (argX.isEditable() || argB.isVirgin()) {//Если вводили число и нажали "A", то заносим введенное число в argA
            argA = new ArgX(argX.getArgX());
            L.d(TAG, "заносим введенное число в argA.");
            newInput = true;
        } else {//иначе выводим на экран, то что лежит в argA
            argX = new ArgX(argA.getArgX());
            L.d(TAG, "выводим на экран, то что лежит в argA.");
        }
        makeArg();
    }

    void toB() {
        if (argX.isEditable() || argB.isVirgin()) {//Если вводили число и нажали "B", то заносим введенное число в argA
            argB = new ArgX(argX.getArgX());
            L.d(TAG, "заносим введенное число в argB.");
            newInput = true;
        } else {//иначе выводим на экран, то что лежит в argB
            argX = new ArgX(argB.getArgX());
            L.d(TAG, "выводим на экран, то что лежит в argB.");
        }
        makeArg();
    }

    void r_to_p() throws MyExceptions {
        L.d(TAG, "Будем переводить радиальные координаты в полярные.");
        if (argA.isVirgin()) {
            argA.setDouble(0.0);
            L.d(TAG, "argA было пустым, заносим в argA 0.0");
        }
        if (argB.isVirgin()) {
            argB.setDouble(0.0);
            L.d(TAG, "argB было пустым, заносим в argB 0.0");
        }
        stackCalculator.r_to_p(argA, argB);
        argX = new ArgX(argA.getArgX());
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void p_to_r() throws MyExceptions {
        L.d(TAG, "Будем переводить полярные координаты в радиальные.");
        if (argA.isVirgin()) {
            argA.setDouble(0.0);
            L.d(TAG, "argA было пустым, заносим в argA 0.0");
        }
        if (argB.isVirgin()) {
            argB.setDouble(0.0);
            L.d(TAG, "argB было пустым, заносим в argB 0.0");
        }
        stackCalculator.p_to_r(argA, argB);
        argX = new ArgX(argA.getArgX());
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void changeAngleUnit() throws MyExceptions {
        argX = new ArgX(stackCalculator.changeAngleUnit(argX.getArgX()));
        newInput = true;
        makeArg();
    }

    void x_to_y() throws MyExceptions {
        argX = new ArgX(stackCalculator.x_to_y(argX.getArgX()));
        newInput = true;
        calcpress = true;
        makeArg();
    }

    void random() throws MyExceptions {
        argX = new ArgX(stackCalculator.random());
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void maxValue() throws MyExceptions {
        argX = new ArgX(stackCalculator.maxValue());
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void minValue() throws MyExceptions {
        argX = new ArgX(stackCalculator.minValue());
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void minNormal() throws MyExceptions {
        argX = new ArgX(stackCalculator.minNormal());
        calcpress = true;
        newInput = true;
        makeArg();
    }
    
    void dbm_to_w() {
        argX = new ArgX(stackCalculator.dbm_to_w(argX.getArgX()));
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void w_to_dbm() {
        argX = new ArgX(stackCalculator.w_to_dbm(argX.getArgX()));
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void factorial() throws MyExceptions {
        argX = new ArgX(stackCalculator.factorial(argX));
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void percent() throws MyExceptions {
        argX = new ArgX(stackCalculator.percent(argX.getArgX()));
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void pi() throws MyExceptions {
        argX = new ArgX(stackCalculator.pi());
        calcpress = true;
        newInput = true;
        makeArg();
    }
    
    void dollar(String currency_rate) {
//        argX = new ArgX(currency_rate);
        argX.setFromString(currency_rate);
        calcpress = true;
        newInput = true;
        makeArg();
    }

    void plus() throws MyExceptions {
        argX = new ArgX(calculatorSelector(argX.getArgX(), stackCalculator.PLUS));
        newInput = true;
        calcpress = false;
//        L.d(TAG, "Флаг Result: " + stackCalculator.isResult());
        makeArg();
    }

    void minus() throws MyExceptions {
        argX = new ArgX(calculatorSelector(argX.getArgX(), stackCalculator.MINUS));
        newInput = true;
        calcpress = false;
//        L.d(TAG, "Флаг Result: " + stackCalculator.isResult());
        makeArg();
    }

    void mult() throws MyExceptions {
        argX = new ArgX(calculatorSelector(argX.getArgX(), stackCalculator.MULTIPLY));
        newInput = true;
        calcpress = false;
//        L.d(TAG, "Флаг Result: " + stackCalculator.isResult());
        makeArg();
    }

    void div() throws MyExceptions {
        argX = new ArgX(calculatorSelector(argX.getArgX(), stackCalculator.DIVIDE));
        newInput = true;
        calcpress = false;
//        L.d(TAG, "Флаг Result: " + stackCalculator.isResult());
        makeArg();
    }

    void power() throws MyExceptions {
        argX = new ArgX(calculatorSelector(argX.getArgX(), stackCalculator.POWER));
        newInput = true;
        calcpress = false;
//        L.d(TAG, "Флаг Result: " + stackCalculator.isResult());
        makeArg();
    }

    void x_sqr_y() throws MyExceptions {
        argX = new ArgX(calculatorSelector(argX.getArgX(), stackCalculator.X_SQR_Y));
        newInput = true;
        calcpress = false;
//        L.d(TAG, "Флаг Result: " + stackCalculator.isResult());
        makeArg();
    }

    void calc() throws MyExceptions {
        argX = new ArgX(calcAll(argX.getArgX()));
        newInput = true;
        calcpress = true;
//        L.d(TAG, "Флаг Result: " + stackCalculator.isResult());
        makeArg();

    }

    void del() {
        if (!argX.isEditable()) {
            return;
        }
        if (!argX.isExponent()) { //Если флаг EXP выключен, то редактируем мантиссу
            L.d(TAG, "Будем редактировать мантиссу");
            if (!argX.isMantissaFractionalPart()) { //Если флаг argX.isMantissaFractionalPart выключен, то редактируем целую часть мантиссы
                L.d(TAG, "Будем редактировать целую часть мантиссы");
                mantissaIntegerPartDel();
            } else { //Иначе редактируем дробную часть мантиссы
                L.d(TAG, "Будем редактировать дробную часть мантиссы");
                if (argX.getMantissaFractionalPart().length() > 0) { // Если длина дробной части мантиссы больше нуля, то удаляем один символ
                    L.d(TAG, "Удаляем один символ");
                    argX.setMantissaFractionalPart(argX.getMantissaFractionalPart().deleteCharAt(argX.getMantissaFractionalPart().length() - 1));
                    if (argX.getMantissaFractionalPart().length() == 0) { //Если после очередного удаления длина дробной части мантиссы стала равна нулю, то поднимаем флаг
                        L.d(TAG, "После очередного удаления длина дробной части мантиссы стала равна нулю");
//                        argX.isMantissaFractionalPart = false; //Нужно ли удалять точку, если дробная часть мантиссы уже закончилась
                    }
                } else { //длина дробной части мантиссы равна нулю (или меньше)
                    L.d(TAG, "Длина дробной части мантиссы равна нулю (или меньше)");
                    L.d(TAG, "Убираем точку и редактируем целую часть мантиссы");
                    argX.setIsMantissaFractionalPart(false);  //отмечаем это и редактируем целую часть мантиссы
                    mantissaIntegerPartDel();
                }
            }

        } else {            //Иначе редактируем экспоненту
            if (argX.getExponent().length() > 0) {// Если длина экспоненты больше нуля, то удаляем один символ

//                StringBuilder sb = argX.getExponent();
//                sb.deleteCharAt(argX.getExponent().length() - 1);
//                argX.setExponent(sb);
                argX.setExponent(argX.getExponent().deleteCharAt(argX.getExponent().length() - 1));

                if (argX.getExponent().length() == 0) { //Если после очередного удаления длина экспоненты стала равна нулю, то обнуляем флаг знака
                    L.d(TAG, "После очередного удаления длина экспоненты стала равна нулю, обнуляем флаг знака");
                    argX.setExponentSign(false);
                }
            }

        }
        makeArg();
    }

    void openBracket() {
        if (!stackCalculator.isOperationStackEmpty() || (stackCalculator.isNumberStackEmpty()) & newInput) {      // Открываем скобку только в случае, если в стеке операций что-то есть (+,-,*,/,корень, степень) или если стек чисел пуст
            L.d(TAG, "Скобка открыта");
            status.setBracket(true);
//            if (operationStack.empty()) {
//                protocol.print("(");
//            } else {
//                protocol.print (numberStack.peek()+"");
//                protocol.print (operationStack.peek()+"");
//                protocol.print("(");
//            }
            //protocol.print (numberStack.peek());

            //operation = OPEN_BRACKET;
            stackCalculator.stacksInStacks();
            //stackCalculator ();
            newArg();
            newInput = true;
            calcpress = false;
            calc = false;
            makeArg();
        }
    }

    void closeBracket() throws MyExceptions {
        //operation = NOP;        // При закрытии скобки, вычисляем все в пределах одного стека, и восстанавливаем старые стеки
        double tmp = argX.getArgX();
        L.d(TAG, "Набрано: " + tmp + ")");
        if (!stackCalculator.isStackForNumberStackEmpty() && !stackCalculator.isStackForOperationStackEmpty()) {
            tmp = calculatorSelector(tmp, stackCalculator.NOP);
            stackCalculator.restoreStacks();

            //autoConstant = numberStack2
            L.d(TAG, "Восстановлены старые стеки операций и чисел");
        }
        if (stackCalculator.isStackForNumberStackEmpty() && stackCalculator.isStackForOperationStackEmpty()) {
            status.setBracket(false);
            L.d(TAG, "Скобка закрыта");
        }
        L.d(TAG, "=");

        if (stackCalculator.isOperationStackEmpty()) {
            L.d(TAG, "" + tmp);
        }
        argX = new ArgX(tmp);
        newInput = true;
        calcpress = true;
        calc = false;
        makeArg();
    }

    private void mantissaIntegerPartDel() {
        if (argX.getMantissaIntegerPart().length() > 0) {
            L.d(TAG, "Длина целой части мантиссы больше нуля");
            L.d(TAG, "Удаляем один символ");

//            StringBuilder sb = argX.getMantissaIntegerPart();
//            sb.deleteCharAt(argX.getMantissaIntegerPart().length() - 1);
//            argX.setMantissaIntegerPart(sb);
            argX.setMantissaIntegerPart(argX.getMantissaIntegerPart().deleteCharAt(argX.getMantissaIntegerPart().length() - 1));

            if (argX.getMantissaIntegerPart().length() == 0) { //Если после очередного удаления длина целой части мантиссы стала равна нулю, то обнуляем флаг знака
                L.d(TAG, "После очередного удаления длина целой части мантиссы стала равна нулю, обнуляем флаг знака");
                argX.setSign(false);
            }
        } else {
            L.d(TAG, "Длина целой части мантиссы равна нулю, ничего удалять не будем");
            newInput = true;
        }

    }

    private void numberPartDel(StringBuilder stringBuilder) {
        if (stringBuilder.length() > 0) {
            L.d(TAG, "Длина строки больше нуля");
            L.d(TAG, "Удаляем один символ");
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        } else {
            L.d(TAG, "Длина строки равна нулю, ничего удалять не будем");
        }
    }

    void sign() {
//        if (!argX.editable) {
//            argX.setSign(!argX.isSign()); //Если число нередактируемое (результат предыдущих вычислений, то меняем знак мантиссы
//        }
        if (!argX.isExponent() || !argX.isEditable()) { //Если флаг EXP выключен, то редактируем мантиссу

            argX.setSign(!argX.isSign());
        } else {            //Иначе редактируем экспоненту

            argX.setExponentSign(!argX.isExponentSign());
        }
        makeArg();
    }

    void dot() {
        if (fixModeNumberWait) { //Если нажали на кнопку FIX
            fixModeNumberWait = false; // выключаем режим ожидания
            if (argX.isVirgin() || !argX.isEditable()) { // И если еще ничего не вводили или выведен ответ
                mainDisplay.setFixModeScale(-1); // то включаем режим FIX
                makeArg();// выводим старое число в новом формате
                return; //и выходим
            }
        }
        if (!argX.isEditable()) {
            newArg();
        }
        if (argX.isExponent() || argX.isMantissaFractionalPart()) { //Если флаг argX.isExponent включен или дробная часть мантиссы уже есть, то ничего не делаем
            return;
        }
        argX.setIsMantissaFractionalPart(true);
        makeArg();
    }

    void ce() {
        newArg();
        newInput = true;
        calcpress = false;
        stackCalculator.setResult(false);
        mainDisplay.offSciMode();
        makeArg();
    }

    void exp() {
        if (!argX.isEditable()) {
            return;
        }
        L.d(TAG, "Переходим к вводу экспоненты");
        if (!argX.isExponent()) {
            if ((argX.getMantissaIntegerPart().length() + argX.getMantissaFractionalPart().length() + argX.getExponent().length()) >= (mainDisplay.numberLength - argX.getExponent().length() - 1)) {
                customToast.setToastText("Не хватает места для ввода экспоненты, удалите несколько символов");
                customToast.setToastImage(CustomToast.IC_WARNING_AMBER);
                customToast.show();
                return;
            }
        }

        if (argX.isVirgin()) {

//            StringBuilder sb = argX.getMantissaIntegerPart();
//            sb.append('1');
//            argX.setMantissaIntegerPart(sb);
            argX.setMantissaIntegerPart(argX.getMantissaIntegerPart().append('1'));
            argX.setExponent(argX.getExponent().append('0'));
            newInput = false;
        }
        argX.setIsExponent(true);
        makeArg();
    }

    private void makeArg() {
//        if (argX.getArgX())
        mainDisplay.printArgX(argX);
    }

    private void makeArg(ArgX argN) {
        mainDisplay.printArgX(argN);
    }

    private double calcAll(double number) throws MyExceptions {
        while (!stackCalculator.isStackForNumberStackEmpty() && !stackCalculator.isOperationStackEmpty()) { // Пока стеки стеков не опустеют, продолжаем вычислять
            number = calculatorSelector(number, stackCalculator.NOP);
            L.d(TAG, "Посчитано: " + number);
            //printCalc (tmp+"");
            stackCalculator.oldStacksRestore();
        }
        if (stackCalculator.isStackForNumberStackEmpty() && stackCalculator.isStackForOperationStackEmpty()) {
            status.setBracket(false);
            L.d(TAG, "Скобка закрыта");
        }
        //operation = NOP;

        number = calculatorSelector(number, stackCalculator.NOP);
        return number;
    }

    private double calculatorSelector(double currentNumber, int currentOperation) throws MyExceptions {
        if (mode.getMode() == mode.COMPLEX && !argA.isVirgin()) {  //Если работаем с комплексными числами и уже что-то введено, то вызываем complexStackCalculator
//            if (argA.isVirgin()) {
//                argA.setDouble(0.0);
//                L.d(TAG, "argA было пустым, заносим в argA 0.0");
//            }
            if (argB.isVirgin()) {
                argB.setDouble(0.0);
                L.d(TAG, "argB было пустым, заносим в argB 0.0");
            }
            Complex cplxNum = new Complex(argA.getArgX(), argB.getArgX());
            cplxNum = complexStackCalculator.complexStackCalculator(cplxNum, currentOperation);
            currentNumber = cplxNum.getReal();
            argA = new ArgX(currentNumber);
            argB = new ArgX(cplxNum.getImaginary());
        } else {
            currentNumber = stackCalculatorSelector(currentNumber, currentOperation);
        }
        return currentNumber;
    }

    private double stackCalculatorSelector(double currentNumber, int currentOperation) throws MyExceptions {
        mainDisplay.offSciMode();
        if (stackCalculator.isNumberStackEmpty()) {//Если стек чисел пуст
            L.d(TAG, "Стек чисел пуст");
            if (currentOperation != stackCalculator.NOP) {//Если была любая операция кроме NOP, т. е. кроме "=" или закрытия скобки, то заносим все в стек
                L.d(TAG, "Еще не было операций, заношу значения в новый стек чисел и в новый стек операций");
                calc = false;
                currentNumber = stackCalculator.putInStack(currentNumber, currentOperation);
            }
            if (currentOperation == stackCalculator.NOP) {//Если была операция NOP, т. е. "=" или закрытие скобки
                L.d(TAG, "Стек чисел пуст и было нажато = , здесь должны быть вычисления с использованием постоянных");
                currentNumber = stackCalculator.calcAutoConstant(currentNumber);
                calc = true;
            }
            return currentNumber;
        }
        // Здесь уже ясно, что в стеке чисел что-то есть
        L.d(TAG, "В стеках чисел и операций уже что-то есть");
//        L.d(TAG, "argX.isVirgin(): " + argX.isVirgin());
//        L.d(TAG, "stackCalculator.isResult(): " + stackCalculator.isResult());

//        if (!stackCalculator.isResult()) { //В стеке чисел и операций уже что-то есть, новое число не вводили, а нажали на операцию, значит в стеке чисел - автоконстанта
//            L.d(TAG, "Но, новое число не вводили, а нажали на операцию, значит в стеке чисел - автоконстанта");
//            currentNumber = stackCalculator.setAutoConstant(currentNumber);
//            calc = false;
//            return currentNumber;
//        }
        L.d(TAG, "Считаем!");
        currentNumber = stackCalculator.calc(currentNumber, currentOperation); //Считаем
        calc = true;
        if (Double.isInfinite(currentNumber)) {
            throw new MyExceptions(MyExceptions.TOO_BIG);
        }
//        if (currentNumber < 0 && Double.isInfinite(currentNumber)) {
//            throw new MyExceptions(MyExceptions.TOO_SMALL);
//        }
        if (Double.isNaN(currentNumber)) {
            throw new MyExceptions(MyExceptions.NOT_NUMBER);
        }
        return currentNumber;
    }

    void x_to_m() {
        setMemory(argX.getArgX());
        newInput = true;
        calcpress = true;
    }

    void readMemory() {
        argX.setDouble(getMemory());
        makeArg();
        newInput = true;
        calcpress = true;
    }
    
    void clearMemory() {
        setMemory(0.0);
        newInput = true;
        calcpress = true;
    }

    void memoryPlus() throws MyExceptions {
        double tmp1 = argX.getArgX();
        L.d(TAG, "Набрано: " + tmp1 + " M+");
        if (!stackCalculator.isOperationStackEmpty()) {
            tmp1 = calcAll(tmp1);
        }
        L.d(TAG, "Посчитано: " + tmp1);
        argX.setDouble(tmp1);
        makeArg();
//        plus();
        L.d(TAG, "Прибавляю к памяти: " + tmp1);
        double tmp2 = getMemory();
        //argX.setDouble(tmp2);
//        double tmp3;
        double tmp3 = tmp1 + tmp2;
        //tmp3 = tmp1 + tmp2;
        setMemory(tmp3);
        newInput = true;
        calcpress = true;
    }

    private void setMemory(double mem) {
        memoryStore.setMemory(mem);
        L.d(TAG, "Теперь в памяти: " + mem);
    }

    private double getMemory() {
        double mem;
        mem = memoryStore.getMemory();
        return mem;
    }

    void startSDMode() {
        mode.switchSD();
        if (mode.getMode() == mode.SD) {
            statisticMode = new StatisticMode(protocol);
            argX.setDouble(statisticMode.getStackSize());

        } else {
            newArg();
        }
        makeArg();
        newInput = true;
        calcpress = true;
    }

    void putDataToStack() throws MyExceptions {
        L.d(TAG, "argX.isVirgin(): " + argX.isVirgin());
        L.d(TAG, "argX.isEditable(): " + argX.isEditable());
        if (!argX.isEditable() && !stackCalculator.isResult()) { //Если argX не редактируемое, значит ничего не ввели, значит ничего не делаем.
            L.d(TAG, "argX не редактируемое, значит ничего не ввели, значит ничего не делаем.");
            return;
        }
        double currentNum = argX.getArgX();
        L.d(TAG, "currentNum = " + currentNum);
        if (stackCalculator.isMultiply()) { //Если было нажато УМНОЖИТЬ
            double dataValue = stackCalculator.getNumberForStatistic();// берем число из стека калькулятора
            int i = (int) currentNum;// введенное число - это число раз, которое нужно положить число в стек
            argX.setDouble(statisticMode.pushMultipleNumberToStack(dataValue, i));//здесь кладем много значений в SD стек и выводим размер стека
            L.d(TAG, "Число " + dataValue + " положено в стек " + currentNum + " раз.");
        } else {
            argX.setDouble(statisticMode.singleNumberToStack(currentNum)); //здесь кладем одно текущее число в SD стек и выводим размер стека
            L.d(TAG, "В стек положено число " + currentNum);

        }
        newInput = true;
        makeArg();
    }

    void deleteDataFromStack() throws MyExceptions {
        //Если SD стек пуст
        if (statisticMode.isStackEmpty()) {
            L.d(TAG, "Стек пуст, ничего не делаем.");
            return;
        }

        //Если ничего не вводили, то удаляем одно последнее значение
        if (!argX.isEditable() && !stackCalculator.isResult()) {
            argX.setDouble(statisticMode.deleteTopNumber());
            L.d(TAG, "Ничего не вводили, удаляем одно последнее значение.");
            newInput = true;
            makeArg();
            return;
        }
        // если что-то введено,  то
        double currentNum = argX.getArgX();
        if (stackCalculator.isMultiply()) {//Если было нажато УМНОЖИТЬ
            double dataValue = stackCalculator.getNumberForStatistic();// берем число из стека калькулятора
            int i = (int) currentNum;// введенное число - это число раз, которое нужно удалить число из стека
            argX.setDouble(statisticMode.deleteMultipleNumberFromStack(dataValue, i));//здесь удаляем много значений из SD стека и выводим размер стека
            L.d(TAG, "Число " + dataValue + " удалено из стека " + currentNum + " раз.");
        } else {//Если в стеке операций не умножить, ищем в стеке введенное число и удаляем его из стека
            argX.setDouble(statisticMode.deleteSingleNumber(currentNum)); //здесь кладем одно текущее число в SD стек и выводим размер стека
            L.d(TAG, "В стек положено число " + currentNum);
        }
        newInput = true;
        makeArg();
    }

    void average() {
        argX.setDouble(statisticMode.average());
        newInput = true;
        makeArg();
    }

    void stackSize() {
        argX.setDouble(statisticMode.getStackSize());
        newInput = true;
        makeArg();
    }

    void total() {
        argX.setDouble(statisticMode.totalOfAllData());
        newInput = true;
        makeArg();
    }

    void totalSquare() {
        argX.setDouble(statisticMode.totalOfSquareOfAllData());
        newInput = true;
        makeArg();
    }

    void sampleStandartDeviation() {
        argX.setDouble(statisticMode.sampleStandartDeviation());
        newInput = true;
        makeArg();
    }

    void populationStandartDeviation() {
        argX.setDouble(statisticMode.populationStandartDeviation());
        newInput = true;
        makeArg();
    }

    void startCplxMode() {
        mode.switchCplx();
        if (mode.getMode() == mode.COMPLEX) {
            complexStackCalculator = new ComplexStackCalculator(protocol);
            protocol.toCplxMode();
        } else {
            protocol.fromCplxMode();
        }
        newArg();
        makeArg();
        newInput = true;
        calcpress = true;
    }

    StringBuilder copyToClipboard() {
        StringBuilder sb = new StringBuilder("");
        DecimalFormatSymbols dfs;
        Locale defLocale = Locale.getDefault();
        dfs = new DecimalFormatSymbols(defLocale);
        if (argX.isSign()) {//Если есть знак, то добавляем MinusSign
            sb.append(dfs.getMinusSign());
            L.d(TAG, "Есть знак, добавили MinusSign: " + sb);
            protocol.println("Есть знак, добавили MinusSign: " + sb);
        }
        sb.append(argX.getMantissaIntegerPart());//Добавляем целую часть мантиссы
        L.d(TAG, "Добавили целую часть мантиссы: " + sb);
        protocol.println("Добавили целую часть мантиссы: " + sb);
        if (argX.isMantissaFractionalPart()) {//Если есть дробная часть мантиссы, то добавляем DecimalSeparator
            sb.append(dfs.getDecimalSeparator());
            L.d(TAG, "Есть дробная часть мантиссы, добавили DecimalSeparator: " + sb);
            protocol.println("Есть дробная часть мантиссы, добавили DecimalSeparator: " + sb);
        }
        sb.append(argX.getMantissaFractionalPart());//Добавляем дробную часть мантиссы
        L.d(TAG, "Добавили дробную часть мантиссы: " + sb);
        protocol.println("Добавили дробную часть мантиссы: " + sb);
        if (argX.isExponent()) {//Если есть экспоненциальная часть, то добавляем ExponentSeparator
            sb.append(dfs.getExponentSeparator());
            L.d(TAG, "Есть экспоненциальная часть, добавили ExponentSeparator: " + sb);
            protocol.println("Есть экспоненциальная часть, добавили ExponentSeparator: " + sb);
            if (argX.isExponentSign()) {//Если есть знак у экспоненциальной части, то добавляем MinusSign
                sb.append(dfs.getMinusSign());
                L.d(TAG, "Есть знак у экспоненциальной части, добавили MinusSign: " + sb);
                protocol.println("Есть знак у экспоненциальной части, добавили MinusSign: " + sb);
            }
            sb.append(argX.getExponent());
            L.d(TAG, "Добавили экспоненциальную часть: " + sb);
            protocol.println("Добавили экспоненциальную часть: " + sb);
//        sb.append(argX.getArgXSB());
        } else {
            L.d(TAG, "Нет экспоненциальной части.");
            protocol.println(TAG + "Нет экспоненциальной части.");
        }
        L.d(TAG, "В буфер обмена отправляю: " + sb);
        protocol.println(TAG + "В буфер обмена отправляю: " + sb);
        return sb;
    }

    void pasteFromClipboard(String str) {
        L.d(TAG, "Из буфера обмена получено: " + str);
        protocol.println("Из буфера обмена получено: " + str);
        StringBuilder sb = (createDouble(str));
        if (sb.length() == 0) {
            customToast.setToastText("Попытка вставки из буфера обмена закончилась неудачей.");
            customToast.setToastImage(CustomToast.IC_WARNING_AMBER);
            customToast.show();
            return;
        }
        argX.setFromStringBuilder(sb);
        makeArg();
    }

    private StringBuilder createDouble(final String str) {
        L.d(TAG, "Получена строка: " + str);
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            int charCode = (int) ch;
            L.d(TAG, "Символ [" + i + "]: " + ch + " его код: " + charCode);
        }
        if (str.isEmpty() || str == null) {//Если строка пустая, то выходим нулем
            L.d(TAG, "Строка пустая, выходим с нулем.");
            return new StringBuilder("");
        }
//        final char[] chars = str.toCharArray();
        StringBuilder sb = new StringBuilder(str);
        int exponentSeparatorCounter = 0;
        int curIndex;

        // Удаляем GroupingSeparator
        L.d(TAG, "Удаляем GroupingSeparator.");
        int groupingSeparatorCounter = 0;
        curIndex = sb.indexOf("" + '\u00A0');
        while (curIndex != -1) {
            groupingSeparatorCounter++;
            sb.deleteCharAt(curIndex);
            curIndex = sb.indexOf("" + '\u00A0');
        }

        curIndex = sb.indexOf("'");
        while (curIndex != -1) {
            groupingSeparatorCounter++;
            sb.deleteCharAt(curIndex);
            curIndex = sb.indexOf("'");
        }

        curIndex = sb.indexOf("" + '\u2019');
        while (curIndex != -1) {
            groupingSeparatorCounter++;
            sb.deleteCharAt(curIndex);
            curIndex = sb.indexOf("" + '\u2019');
        }

        curIndex = sb.indexOf("" + '\u066c');
        while (curIndex != -1) {
            groupingSeparatorCounter++;
            sb.deleteCharAt(curIndex);
            curIndex = sb.indexOf("" + '\u066c');
        }

        curIndex = sb.indexOf(" ");
        while (curIndex != -1) {
            groupingSeparatorCounter++;
            sb.deleteCharAt(curIndex);
            curIndex = sb.indexOf(" ");
        }

        //Приводим Exponent Separator к общему виду
        L.d(TAG, "Приводим Exponent Separator к общему виду.");

        for (int i = 0; i < sb.length(); i++) {
            char ch = str.charAt(i);
            if (ch == 'E') {
                exponentSeparatorCounter++;
            }
        }

        curIndex = sb.indexOf("اس");
        while (curIndex != -1) {
            sb.replace(curIndex, curIndex + 2, "E");
            exponentSeparatorCounter++;
            curIndex = sb.indexOf("اس");
        }
        curIndex = sb.indexOf("×10^");
        while (curIndex != -1) {
            sb.replace(curIndex, curIndex + 4, "E");
            exponentSeparatorCounter++;
            curIndex = sb.indexOf("×10^");
        }
        curIndex = sb.indexOf("*10^");
        while (curIndex != -1) {
            sb.replace(curIndex, curIndex + 4, "E");
            exponentSeparatorCounter++;
            curIndex = sb.indexOf("*10^");
        }
        curIndex = sb.indexOf("x10^");
        while (curIndex != -1) {
            sb.replace(curIndex, curIndex + 4, "E");
            exponentSeparatorCounter++;
            curIndex = sb.indexOf("x10^");
        }
        curIndex = sb.indexOf("X10^");
        while (curIndex != -1) {
            sb.replace(curIndex, curIndex + 4, "E");
            exponentSeparatorCounter++;
            curIndex = sb.indexOf("X10^");
        }
        curIndex = sb.indexOf("×۱۰^");
        while (curIndex != -1) {
            sb.replace(curIndex, curIndex + 4, "E");
            exponentSeparatorCounter++;
            curIndex = sb.indexOf("×۱۰^");
        }
        curIndex = sb.indexOf("Е");
        while (curIndex != -1) {
            sb.replace(curIndex, curIndex + 1, "E");
            exponentSeparatorCounter++;
            curIndex = sb.indexOf("E");
        }

        curIndex = sb.indexOf("e");
        while (curIndex != -1) {
            sb.replace(curIndex, curIndex + 1, "E");
            exponentSeparatorCounter++;
            curIndex = sb.indexOf("e");
        }

        if (exponentSeparatorCounter > 1) {
            L.d(TAG, "Ошибка. ExponentSeparator был найден " + exponentSeparatorCounter + " раз.");
            return new StringBuilder("");
        }

        //Удаляем + перед экспоненциальной частью, если он есть
        curIndex = sb.indexOf("E+");
        while (curIndex != -1) {
            L.d(TAG, "Удаляем + перед экспоненциальной частью.");
            sb.replace(curIndex, curIndex + 2, "E");
            curIndex = sb.indexOf("E+");
        }

        // Приводим MinusSign к общему виду
        L.d(TAG, "Приводим MinusSign к общему виду.");
        curIndex = sb.indexOf("" + '\u2212');
        while (curIndex != -1) {
            sb.replace(curIndex, curIndex + 1, "-");
            curIndex = sb.indexOf("" + '\u2212');
        }

        // Приводим ZeroDigit к общему виду
        L.d(TAG, "Приводим ZeroDigit к общему виду.");
        curIndex = sb.indexOf("" + '\u0660');
        while (curIndex != -1) {
            sb.replace(curIndex, curIndex + 1, "0");
            curIndex = sb.indexOf("" + '\u0660');
        }

        curIndex = sb.indexOf("" + '\u06F0');
        while (curIndex != -1) {
            sb.replace(curIndex, curIndex + 1, "0");
            curIndex = sb.indexOf("" + '\u06F0');
        }

        curIndex = sb.indexOf("" + '\u0966');
        while (curIndex != -1) {
            sb.replace(curIndex, curIndex + 1, "0");
            curIndex = sb.indexOf("" + '\u0966');
        }

        curIndex = sb.indexOf("" + '\u09E6');
        while (curIndex != -1) {
            sb.replace(curIndex, curIndex + 1, "0");
            curIndex = sb.indexOf("" + '\u09E6');
        }

        curIndex = sb.indexOf("" + '\u1040');
        while (curIndex != -1) {
            sb.replace(curIndex, curIndex + 1, "0");
            curIndex = sb.indexOf("" + '\u1040');
        }

        curIndex = sb.indexOf("" + '\u0F20');
        while (curIndex != -1) {
            sb.replace(curIndex, curIndex + 1, "0");
            curIndex = sb.indexOf("" + '\u0F20');
        }

        curIndex = sb.indexOf("\n");
        while (curIndex != -1) {
            L.d(TAG, "Удаляем перевод строки.");
            sb.deleteCharAt(curIndex);
            curIndex = sb.indexOf("\n");
        }

        int arabicDecimalSeparatorCounter = 0;
        int dotCounter = 0;
        int commaCounter = 0;

        for (int i = 0; i < sb.length(); i++) {
            switch (sb.charAt(i)) {
                case 1643:
                    arabicDecimalSeparatorCounter++;
                    break;
                case '.':
                    dotCounter++;
                    break;
                case ',':
                    commaCounter++;
                    break;
            }
        }
        L.d(TAG, "Число arabicDecimalSeparator = " + arabicDecimalSeparatorCounter);
        L.d(TAG, "Число точек = " + dotCounter);
        L.d(TAG, "Число запятых = " + commaCounter);

        if (groupingSeparatorCounter > 0) {//groupingSeparator уже удален, будем менять DecimalSeparator на точку
            L.d(TAG, "groupingSeparator уже удален, будем менять DecimalSeparator на точку.");
            if ((arabicDecimalSeparatorCounter + dotCounter + commaCounter) > 1) {
                L.d(TAG, "Ошибка. groupingSeparator уже был удален, DecimalSeparator может быть только один.");
                return new StringBuilder("");
            }
            if (arabicDecimalSeparatorCounter != 0) {
                L.d(TAG, "Меняем ArabicDecimalSeparator на точку.");
                int aDSIndex = sb.indexOf("" + '\u066B');
                sb.replace(aDSIndex, aDSIndex + 1, ".");
            } else if (commaCounter != 0) {
                L.d(TAG, "Меняем запятую на точку.");
                int commaIndex = sb.indexOf(",");
                sb.replace(commaIndex, commaIndex + 1, ".");
            } else {
                L.d(TAG, "DecimalSeparator это точка, оставляем как есть.");
            }
        } else {
            if (arabicDecimalSeparatorCounter > 1) {
                L.d(TAG, "Ошибка. ArabicDecimalSeparator был найден " + arabicDecimalSeparatorCounter + " раз.");
                return new StringBuilder("");
            }
            if (arabicDecimalSeparatorCounter == 1) {
                L.d(TAG, "Был найден ArabicDecimalSeparator, значит он и есть DecimalSeparator удаляем все точки и запятые.");
                curIndex = sb.indexOf(".");
                while (curIndex != -1) {
                    sb.deleteCharAt(curIndex);
                    curIndex = sb.indexOf(".");
                }
                curIndex = sb.indexOf(",");
                while (curIndex != -1) {
                    sb.deleteCharAt(curIndex);
                    curIndex = sb.indexOf(",");
                }
                int aDSIndex = sb.indexOf("" + '\u066B');
                sb.replace(aDSIndex, aDSIndex + 1, ".");
            }
            //Здесь уже ясно, что число ArabicDecimalSeparator = 0
            L.d(TAG, "Здесь уже ясно, что число ArabicDecimalSeparator = 0.");
            if (dotCounter == 0) {
                if (commaCounter == 0) {//у нас целое число без GroupingSeparator и без DecimalSeparator
                    L.d(TAG, "У нас целое число без GroupingSeparator и без DecimalSeparator.");
                }
                if (commaCounter == 1) {//Запятая либо DecimalSeparator, либо GroupingSeparator
                    L.d(TAG, "Запятая либо DecimalSeparator, либо GroupingSeparator.");
                    int expIndex = sb.indexOf("E");
                    int commaIndex = sb.indexOf(",");
                    if (expIndex == -1) {
                        expIndex = sb.length();
                    }
                    if ((expIndex - commaIndex) != 4) { //запятая отстоит от знака экспоненты не на 4 символа - следовательно запятая это DecimalSeparator, меняем ее на точку
                        L.d(TAG, "Запятая отстоит от знака экспоненты не на 4 символа - следовательно запятая это DecimalSeparator, меняем ее на точку.");
                        sb.replace(commaIndex, commaIndex + 1, ".");
                    } else {//запятая отстоит от знака экспоненты на 4 символа - определяем кто есть кто из настроек локали
                        L.d(TAG, "Запятая отстоит от знака экспоненты на 4 символа - определяем кто есть кто из настроек локали.");
                        DecimalFormatSymbols dfs;
                        Locale defLocale = Locale.getDefault();
                        dfs = new DecimalFormatSymbols(defLocale);
                        switch (dfs.getDecimalSeparator()) {
                            case '.':// В данной локали DecimalSeparator это точка, значит считаем, что запятая это GroupingSeparator. Удаляем ее.
                                L.d(TAG, "В данной локали DecimalSeparator это точка, значит считаем, что запятая это GroupingSeparator. Удаляем ее.");
                                sb.deleteCharAt(commaIndex);
                                break;
                            case ',':// В данной локали DecimalSeparator это запятая. Меняем запятую на точку
                                L.d(TAG, "В данной локали DecimalSeparator это запятая. Меняем запятую на точку.");
                                sb.replace(commaIndex, commaIndex + 1, ".");
                                break;
                        }
                    }
                }
                if (commaCounter > 1) {//Запятая это GroupingSeparator, просто удаляем все запятые
                    L.d(TAG, "Запятая это GroupingSeparator, просто удаляем все запятые.");
                    curIndex = sb.indexOf(",");
                    while (curIndex != -1) {
                        sb.deleteCharAt(curIndex);
                        curIndex = sb.indexOf(",");
                    }
                }
            }
            if (dotCounter == 1) {
                if (commaCounter == 0) {//Точка либо DecimalSeparator, либо GroupingSeparator
                    L.d(TAG, "Точка либо DecimalSeparator, либо GroupingSeparator.");
                    int expIndex = sb.indexOf("E");
                    int dotIndex = sb.indexOf(".");
                    if (expIndex == -1) {
                        expIndex = sb.length();
                    }
                    if ((expIndex - dotIndex) == 4) {//точка отстоит от знака экспоненты на 4 символа - определяем кто есть кто из настроек локали
                        L.d(TAG, "Точка отстоит от знака экспоненты на 4 символа - определяем кто есть кто из настроек локали.");
                        DecimalFormatSymbols dfs;
                        Locale defLocale = Locale.getDefault();
                        dfs = new DecimalFormatSymbols(defLocale);
                        switch (dfs.getDecimalSeparator()) {
                            case '.':// В данной локали DecimalSeparator это точка
                                L.d(TAG, "В данной локали DecimalSeparator это точка.");
                                break;
                            case ',':// В данной локали DecimalSeparator это запятая. Удаляем точку
                                L.d(TAG, "В данной локали DecimalSeparator это запятая. Удаляем точку.");
                                sb.deleteCharAt(dotIndex);
                                break;
                        }
                    } else { //точка отстоит от знака экспоненты не на 4 символа - следовательно точка это DecimalSeparator
                        L.d(TAG, "Точка отстоит от знака экспоненты не на 4 символа - следовательно точка это DecimalSeparator.");
                    }
                }
                if (commaCounter == 1) {//В этом случае, кто правее тот и DecimalSeparator
                    L.d(TAG, "Число точек и число запятых равно 1. В этом случае, кто правее тот и DecimalSeparator.");
                    int dotIndex = sb.indexOf(".");
                    int commaIndex = sb.indexOf(",");
                    if (dotIndex > commaIndex) { // точка правее, удаляем запятую
                        L.d(TAG, "Точка правее, удаляем запятую.");
                        sb.deleteCharAt(commaIndex);
                    } else { // иначе запятая правее, удаляем точку и меняем запятую на точку
                        L.d(TAG, "Запятая правее, удаляем точку и меняем запятую на точку.");
                        sb.deleteCharAt(dotIndex);
                        curIndex = sb.indexOf(",");
                        sb.replace(curIndex, curIndex + 1, ".");
                    }
                }
                if (commaCounter > 1) {//у нас DecimalSeparator это точка, удаляем запятые
                    L.d(TAG, "У нас DecimalSeparator это точка, удаляем запятые.");
                    curIndex = sb.indexOf(",");
                    while (curIndex != -1) {
                        sb.deleteCharAt(curIndex);
                        curIndex = sb.indexOf(",");
                    }
                }
            }
            if (dotCounter > 1) {
                if (commaCounter == 0) {//Точка GroupingSeparator, просто удаляем точки
                    L.d(TAG, "Точка это GroupingSeparator, просто удаляем точки.");
                    curIndex = sb.indexOf(".");
                    while (curIndex != -1) {
                        sb.deleteCharAt(curIndex);
                        curIndex = sb.indexOf(".");
                    }
                }
                if (commaCounter == 1) {//у нас DecimalSeparator это запятая, удаляем точки и меняем запятую на точку
                    L.d(TAG, "У нас DecimalSeparator это запятая, удаляем точки и меняем запятую на точку.");
                    curIndex = sb.indexOf(".");
                    while (curIndex != -1) {
                        sb.deleteCharAt(curIndex);
                        curIndex = sb.indexOf(".");
                    }
                    curIndex = sb.indexOf(",");
                    sb.replace(curIndex, curIndex + 1, ".");

                }
                if (commaCounter > 1) {//ошибка
                    L.d(TAG, "Ошибка. Число точек и число запятых не может одновременно быть больше 1.");
                    return new StringBuilder("");
                }

            }
        }
        if (!isCreatable("" + sb)) {
            return new StringBuilder("");
        }
        L.d(TAG, "Возвращаем преобразованную строку: " + sb);
        for (int i = 0; i < sb.length(); i++) {
            char ch = sb.charAt(i);
            int charCode = (int) ch;
            L.d(TAG, "Символ [" + i + "]: " + ch + " его код: " + charCode);
        }
        return sb;
    }

    /**
     * <p>
     * Checks whether the String a valid Java number.</p>
     * <p>
     * <p>
     * Valid numbers include scientific notation and numbers marked with a type
     * qualifier (e.g. 123L).</p>
     * <p>
     * <p>
     * <code>null</code> and empty/blank {@code String} will return
     * <code>false</code>.</p>
     *
     * @param str the <code>String</code> to check
     * @return <code>true</code> if the string is a correctly formatted number
     * @since 3.5 the code supports the "+" suffix on numbers except for
     * integers in Java 1.6
     */
    private boolean isCreatable(final String str) {
        L.d(TAG, "isCreatable() запущен, на входе строка: " + str);
        if (StringUtils.isEmpty(str)) { //Если строка пустая, то выходим с отрицательным ответом
            L.d(TAG, "Строка пустая, выходим с отрицательным ответом.");
            return false;
        }
        final char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false; //Запрещаем символы знака '+' или '-'
        boolean foundDigit = false;
        // deal with any possible sign up front
        final int start = chars[0] == '-' || chars[0] == '+' ? 1 : 0;//Если наша строка начинается с '-' или с '+', то start = 1, иначе start = 0
        final boolean hasLeadingPlusSign = start == 1 && chars[0] == '+';//hasLeadingPlusSign = true если первый символ '+'
//        if (sz > start + 1 && chars[start] == '0') { // leading 0 Если первый символ 0
//            if (chars[start + 1] == 'x' || chars[start + 1] == 'X') { // leading 0x/0X . Если второй символ x или X, т.е. в начале строки 0x/0X, то значит ввыдено число в шестнадцатиричном формате
//                int i = start + 2;
//                if (i == sz) {
//                    return false; // Если str == "0x", , то выходим с отрицательным ответом
//                }
//                // checking hex (it can't be anything else) Проверяем правильность HEX строки
//                for (; i < chars.length; i++) {
//                    if ((chars[i] < '0' || chars[i] > '9')
//                            && (chars[i] < 'a' || chars[i] > 'f')
//                            && (chars[i] < 'A' || chars[i] > 'F')) {
//                        return false;
//                    }
//                }
//                return true;//Да, это правильное HEX число
//            } else if (Character.isDigit(chars[start + 1])) {
//                // leading 0, but not hex, must be octal Первый символ 0, но это не HEX число, значит это OCT число
//                int i = start + 1;
//                for (; i < chars.length; i++) {
//                    if (chars[i] < '0' || chars[i] > '7') {
//                        return false;
//                    }
//                }
//                return true;//Да, это правильное OCT число
//            }
//        }
        sz--; // don't want to loop to the last char, check it afterwords Не хочу делать цикл до последнего символа, проверю его впоследствии
        // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to    Цикл до предпоследнего символа или до последнего символа, если нам нужна еще одна цифра
        // make a valid number (e.g. chars[0..5] = "1234E")                                 для создания правильного числа
        while (i < sz || i < sz + 1 && allowSigns && !foundDigit) {
            if (chars[i] >= '0' && chars[i] <= '9') {//Если нашли цифру
                L.d(TAG, "Нашли цифру: " + chars[i]);
                foundDigit = true;
                allowSigns = false; //Запрещаем символы знака '+' или '-'

            } else if (chars[i] == '.') {//Если нашли точку
                L.d(TAG, "Нашли точку: " + chars[i]);
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    L.d(TAG, "Две точки или точка в экспоненциальной части, выходим с отрицательным ответом.");
                    return false;//Если две точки или точка в экспоненциальной части, то выходим с отрицательным ответом
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {//Если нашли 'e' или 'E'
                L.d(TAG, "Нашли 'e' или 'E': " + chars[i]);
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    L.d(TAG, "Два символа 'E', выходим с отрицательным ответом.");
                    return false;//Если два символа 'E', то выходим с отрицательным ответом
                }
                if (!foundDigit) {
                    L.d(TAG, "Нашли 'E', но цифр еще небыло, выходим с отрицательным ответом.");
                    return false;//Если нашли 'E', но цифр еще небыло, то выходим с отрицательным ответом
                }
                hasExp = true;
                allowSigns = true;//так как нашли 'e' или 'E', то снова разрешаем сиволы знака '+' или '-'
            } else if (chars[i] == '+' || chars[i] == '-') {//Если нашли символы знака '+' или '-'
                L.d(TAG, "Нашли символы знака '+' или '-': " + chars[i]);
                if (!allowSigns) {
                    L.d(TAG, "Символы знака запрещены, выходим с отрицательным ответом.");
                    return false;//Если символы знака запрещены, то выходим с отрицательным ответом
                }
                allowSigns = false;//Запрещаем символы знака '+' или '-'
                foundDigit = false; // we need a digit after the E Нам нужна цифра после E
            } else {
                L.d(TAG, "Ничего подходящего не нашли, выходим с отрицательным ответом.");
                return false;// Если ничего подходящего не нашли, то выходим с отрицательным ответом
            }
            i++;
        }
        if (i < chars.length) {//Если остался еще символ
            if (chars[i] >= '0' && chars[i] <= '9') {//остался символ цифры
                L.d(TAG, "Нашли цифру: " + chars[i]);
                if (SystemUtils.IS_JAVA_1_6 && hasLeadingPlusSign && !hasDecPoint) {//Если у нас JAVA 1.6 и если первый символ '+' и если нет десятичной точки,
                    L.d(TAG, "У нас JAVA 1.6 и первый символ '+' и нет десятичной точки, выходим с отрицательным ответом.");
                    return false;// то выходим с отрицательным ответом
                }
                // no type qualifier, OK
                L.d(TAG, "Все в порядке - число пригодно, число без указаня типа.");
                return true;//Все в порядке - число пригодно, число без указаня типа
            }
            if (chars[i] == 'e' || chars[i] == 'E') {//Если нашли 'e' или 'E'
                L.d(TAG, "Нашли 'e' или 'E': " + chars[i]);
                // can't have an E at the last byte
                L.d(TAG, "Не может быть E в последнем символе, выходим с отрицательным ответом.");
                return false;// то выходим с отрицательным ответом
            }
            if (chars[i] == '.') {//Если нашли точку
                L.d(TAG, "Нашли точку: " + chars[i]);
                if (hasDecPoint || hasExp) {//Если две точки или точка в экспоненциальной части, то выходим с отрицательным ответом
                    L.d(TAG, "Две точки или точка в экспоненциальной части, выходим с отрицательным ответом.");
                    // two decimal points or dec in exponent
                    return false;
                }
                // single trailing decimal point after non-exponent is ok
                if (foundDigit) {
                    L.d(TAG, "Нашли точку в конце числа без экспоненциальной части, это нормально.");
                } else {
                    L.d(TAG, "Нашли точку, но не было цифр, это ненормально, выходим с отрицательным ответом.");
                }
                return foundDigit;//Если нашли точку в конце число без экспоненциальной части, то это нормально, а если нашли точку, но небыло цифр, то это ненормально
            }
            if (!allowSigns //              Если    символы знака '+' или '-' запрещены (символы знака разрешены только после E)
                    && (chars[i] == 'd'//           и (на конце  'd' или 'D' или 'f' или или 'F')
                    || chars[i] == 'D'//
                    || chars[i] == 'f'
                    || chars[i] == 'F')) {
                L.d(TAG, "Нашли символ типа с плавающей точкой не после символа 'E': " + chars[i]);
                if (foundDigit) {
                    L.d(TAG, "До этого были найдены цифры, возвращаем true.");
                } else {
                    L.d(TAG, "До этого не были найдены цифры, возвращаем false.");
                }
                return foundDigit;// Возвращаем true если были цифры и возвращем false если цифр небыло
            }
            if (chars[i] == 'l'//Если на конце 'l' или 'L'
                    || chars[i] == 'L') {
                L.d(TAG, "Нашли символ типа long: " + chars[i]);
                // not allowing L with an exponent or decimal point
                if (foundDigit && !hasExp && !hasDecPoint) {
                    L.d(TAG, "До этого были найдены цифры, нет экспоненциальной части и нет десятичной точки - возвращаем true.");
                } else {
                    L.d(TAG, "До этого не были найдены цифры, либо была найдена экспоненциальная часть, либо есть десятичная точка - возвращаем false.");
                }
                return foundDigit && !hasExp && !hasDecPoint;// Возвращаем true если были цифры, нет экспоненциальной части и нет десятичной точки
            }
            // last character is illegal
            L.d(TAG, "Последний символ неправильный, возвращаем false.");
            return false;//последний символ нераспознан
        }
        // allowSigns is true iff the val ends in 'E'                               allowSigns = true только после E
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass  Возвращаем true только если allowSigns = false и если были цифры,
        //                                                                          чтобы убедиться, что такие странные вещи, как '.' И '1E-' не проходит
        if (!allowSigns && foundDigit) {
            L.d(TAG, "Символ знака запрещен и были цифры - возвращаем true.");
        } else {
            L.d(TAG, "Проверка не пройдена - возвращаем false.");
        }
        return !allowSigns && foundDigit;
    }

    void toDec() {
        editX = (EditX) objStore[MainActivity.EDIT_X];
        if (editX == null) {
            editX = new EditX(objStore);
            L.d(TAG, "Создали объект editX, который будет отвечать за ввод всего");
            objStore[MainActivity.EDIT_X] = editX;
        }
        L.d(TAG, "Переключаемся в DEC режим.");
        double doubleNumber = argX.getArgX();
        L.d(TAG, "В DEC режим передаем число: " + doubleNumber);
        editX.setNumber(doubleNumber);
    }

    void toBin() {
        editXBin = (EditXBin) objStore[MainActivity.EDIT_X_BIN];
        if (editXBin == null) {
            editXBin = new EditXBin(objStore);
            L.d(TAG, "Создали объект editXBin, который будет отвечать за ввод двоичных чисел");
            objStore[MainActivity.EDIT_X_BIN] = editXBin;
        }
        L.d(TAG, "Переключаемся в BIN режим.");
        double doubleNumber = argX.getArgX();
        L.d(TAG, "В BIN режим передаем число: " + doubleNumber);
        editXBin.setNumber(doubleNumber);
    }

    void toOct() {
        editXOct = (EditXOct) objStore[MainActivity.EDIT_X_OCT];
        if (editXOct == null) {
            editXOct = new EditXOct(objStore);
            L.d(TAG, "Создали объект editXOct, который будет отвечать за ввод восьмиричных чисел");
            objStore[MainActivity.EDIT_X_OCT] = editXOct;
        }
        L.d(TAG, "Переключаемся в OCT режим.");
        double doubleNumber = argX.getArgX();
        L.d(TAG, "В OCT режим передаем число: " + doubleNumber);
        editXOct.setNumber(doubleNumber);
    }

    void toHex() {
        editXHex = (EditXHex) objStore[MainActivity.EDIT_X_HEX];
        if (editXHex == null) {
            editXHex = new EditXHex(objStore);
            L.d(TAG, "Создали объект editXHex, который будет отвечать за ввод шестнадцатиричных чисел");
            objStore[MainActivity.EDIT_X_HEX] = editXHex;
        }
        L.d(TAG, "Переключаемся в HEX режим.");
        double doubleNumber = argX.getArgX();
        L.d(TAG, "В HEX режим передаем число: " + doubleNumber);
        editXHex.setNumber(doubleNumber);
    }
}
