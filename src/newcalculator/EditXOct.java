package newcalculator;

import java.text.DecimalFormatSymbols;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

/**
 * Created by User on 20.07.2017.
 */
class EditXOct {
    private static final String TAG = "EditXOct";

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

    private boolean fixModeNumberWait;

    private ArgXOct argX;

    private boolean newInput;
    private boolean calcpress;
    private boolean calc;

    private ArgXOct memory;
    Object objStore[];

    public EditXOct(Object objStore[]) {
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
        for (int i = 0; i < objStore.length; i++) {

            L.d(TAG, "В objStore[" + i + "]: " + objStore[i]);
        }
        customToast = new CustomToast(activity, "Проба");
        L.d(TAG, "Создали CustomToast");
        status.offError();
        L.d(TAG, "Выключили ошибку");
        newInput = true;
        calcpress = false;
        newArg();
        L.d(TAG, "Создали новый ArgXOct.");
//        makeArg();
//        L.d(TAG, "Отобразили на дисплее.");
        L.d(TAG, "EditXOct успешно создан!");
    }

    public EditXOct(Object objStore[], int intNumber) {
        this(objStore);
        argX.setNumber(intNumber);
        makeArg();
        L.d(TAG, "Отобразили на дисплее.");
        L.d(TAG, "EditXOct успешно создан!");
    }

    public EditXOct(Object objStore[], long longNumber) {
        this(objStore);
        argX.setNumber(longNumber);
        makeArg();
        L.d(TAG, "Отобразили на дисплее.");
        L.d(TAG, "EditXOct успешно создан!");
    }

    public EditXOct(Object objStore[], double doubleNumber) {
        this(objStore);
        argX.setNumber(doubleNumber);
        makeArg();
        L.d(TAG, "Отобразили на дисплее.");
        L.d(TAG, "EditXOct успешно создан!");
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
        makeArg();
    }

    private void newArg() {
        newInput = true;
        argX = new ArgXOct();
    }

    void add(char pressedKey) {
        if (!argX.isEditable()) { // Если флаг Editable выключен, то нужно создать новое пустое число
            stackCalculator.setResult(false);
            newArg();
        }
        if (((argX.getNumber().length()) >= (21)) ) {

            customToast.setToastText("Превышено максимальное число цифр");
            customToast.setToastImage(CustomToast.IC_WARNING_AMBER);
            customToast.show();
            return;
        }
        if (argX.getNumber().length() < 1 && pressedKey == '0') { //Если числа нет и нажат '0', то отображаем ничего не делаем
            makeArg();
            return;
        }
        argX.setFromStringBuilder(argX.getNumber().append(pressedKey));
        argX.setNotVirgin();
        newInput = true;
        makeArg();
    }

    void setNumber(int intNumber) {
        argX.setNumber(intNumber);
        L.d(TAG, "В argXOct записали: " + intNumber);
        makeArg();
        L.d(TAG, "Отобразили на дисплее: " + argX.getNumber());
    }

    void setNumber(long longNumber) {
        argX.setNumber(longNumber);
        L.d(TAG, "В argXOct записали: " + longNumber);
        makeArg();
        L.d(TAG, "Отобразили на дисплее: " + argX.getNumber());
    }

    void setNumber(double doubleNumber) {
        argX.setNumber(doubleNumber);
        L.d(TAG, "В argXOct записали: " + doubleNumber);
        makeArg();
        L.d(TAG, "Отобразили на дисплее: " + argX.getNumber());
    }

    void x_to_y() throws MyExceptions {
        argX = new ArgXOct(stackCalculator.x_to_y(argX.getDouble()));
        newInput = true;
        calcpress = true;
        makeArg();
    }

    void plus() throws MyExceptions {
        argX = new ArgXOct(calculatorSelector(argX.getDouble(), stackCalculator.PLUS));
        newInput = true;
        calcpress = false;
//        L.d(TAG, "Флаг Result: " + stackCalculator.isResult());
        makeArg();
    }

    void minus() throws MyExceptions {
        argX = new ArgXOct(calculatorSelector(argX.getDouble(), stackCalculator.MINUS));
        newInput = true;
        calcpress = false;
//        L.d(TAG, "Флаг Result: " + stackCalculator.isResult());
        makeArg();
    }

    void mult() throws MyExceptions {
        argX = new ArgXOct(calculatorSelector(argX.getDouble(), stackCalculator.MULTIPLY));
        newInput = true;
        calcpress = false;
//        L.d(TAG, "Флаг Result: " + stackCalculator.isResult());
        makeArg();
    }

    void div() throws MyExceptions {
        argX = new ArgXOct(calculatorSelector(argX.getDouble(), stackCalculator.DIVIDE));
        newInput = true;
        calcpress = false;
//        L.d(TAG, "Флаг Result: " + stackCalculator.isResult());
        makeArg();
    }

    void calc() throws MyExceptions {
        argX = new ArgXOct(calcAll(argX.getDouble()));
        newInput = true;
        calcpress = true;
//        L.d(TAG, "Флаг Result: " + stackCalculator.isResult());
        makeArg();

    }

    void del() {
        if (!argX.isEditable()) {
            return;
        }
        if (argX.getNumber().length() > 0) {
            L.d(TAG, "Длина числа больше нуля.");
            L.d(TAG, "Удаляем один символ.");
            argX.setFromStringBuilder(argX.getNumber().deleteCharAt(argX.getNumber().length() - 1));
        } else {
            L.d(TAG, "Длина целой части числа равна нулю, ничего удалять не будем.");
            newInput = true;
        }
        makeArg();
    }

    void sign() {
        argX.setSign(!argX.isSign());
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
        return stackCalculatorSelector(currentNumber, currentOperation);
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
        setMemory(argX.getDouble());
        newInput = true;
        calcpress = true;
    }

    void readMemory() {
        argX.setNumber(getMemory());
        makeArg();
        newInput = true;
        calcpress = true;
    }
    
    void clearMemory() {
        setMemory(0);
        newInput = true;
        calcpress = true;
    }

    void memoryPlus() throws MyExceptions {
        double tmp1 = argX.getDouble();
        L.d(TAG, "Набрано: " + tmp1 + " M+");
        if (!stackCalculator.isOperationStackEmpty()) {
            tmp1 = calcAll(tmp1);
        }
        L.d(TAG, "Посчитано: " + tmp1);
        argX.setNumber(tmp1);
        makeArg();
        L.d(TAG, "Прибавляю к памяти: " + tmp1);
        double tmp2 = getMemory();
        double tmp3 = tmp1 + tmp2;
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

    void toDec() {
        editX = (EditX) objStore[MainActivity.EDIT_X];
        if (editX == null) {
            editX = new EditX(objStore);
            L.d(TAG, "Создали объект editX, который будет отвечать за ввод всего");
            objStore[MainActivity.EDIT_X] = editX;
        }
        L.d(TAG, "Переключаемся в DEC режим.");
        double doubleNumber = argX.getDouble();
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
        double doubleNumber = argX.getDouble();
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
        double doubleNumber = argX.getDouble();
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
        double doubleNumber = argX.getDouble();
        L.d(TAG, "В HEX режим передаем число: " + doubleNumber);
        editXHex.setNumber(doubleNumber);
    }


    StringBuilder copyToClipboard() {
        StringBuilder sb = new StringBuilder("");

        if (argX.isSign()) { // Если есть минус, то меняем отображаемое число
            L.d(TAG, "Есть минус, меняем отображаемое число.");
            sb.append(Long.toBinaryString(argX.getLong()));
        } else {
            sb.append(argX.getNumber());// Иначе просто добавляем число
            L.d(TAG, "Добавили число.");
            L.d(TAG, sb.toString());
        }
        L.d(TAG, "В буфер обмена отправляю: " + sb);
        return sb;
    }


    void pasteFromClipboard(String str) {
        L.d(TAG, "Из буфера обмена получено: " + str);
        StringBuilder sb = (createDouble(str));
        if (sb.length() == 0) {
            customToast.setToastText("Попытка вставки из буфера обмена закончилась неудачей.");
            customToast.setToastImage(CustomToast.IC_WARNING_AMBER);
            customToast.show();
            return;
        }
        argX.setNumber(sb);
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
     * <p>Checks whether the String a valid Java number.</p>
     * <p>
     * <p>Valid numbers include scientific notation and
     * numbers marked with a type qualifier (e.g. 123L).</p>
     * <p>
     * <p><code>null</code> and empty/blank {@code String} will return
     * <code>false</code>.</p>
     *
     * @param str the <code>String</code> to check
     * @return <code>true</code> if the string is a correctly formatted number
     * @since 3.5 the code supports the "+" suffix on numbers except for integers in Java 1.6
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
}
