package newcalculator;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;

import java.util.List;
import java.util.Stack;

/**
 * Created by User on 25.06.2017.
 */
class ComplexStackCalculator {
    private static final String TAG = "ComplexStackCalculator";

    final int NOP = 0b000;
    final int PLUS = 0b010;
    final int MINUS = 0b011;
    final int MULTIPLY = 0b100;
    final int DIVIDE = 0b101;
    final int POWER = 0b110;
    final int X_SQR_Y = 0b111;

    private final char operations[] = {'=', ')', '+', '-', '×', '÷', '^', '√'};

    private Stack<Complex> complexStack;
    private Stack<Integer> operationStack;
    
    private ProtocolJFrame protocol;

    private boolean result;
    
    private ComplexFormat complexFormat;


    ComplexStackCalculator(ProtocolJFrame protocol) {
        this.protocol = protocol;        
        restart();
        Log.d(TAG, "Создали новый ComplexStackCalculator.");
    }

    void restart() {
//        protocol.cls();
        protocol.println("Complex mode.");
//        autoConstantOperation = NOP;
//        secondOpToProtocol(0.0);
//        autoConstant(0.0);

        complexStack = new Stack<>();
        operationStack = new Stack<>();
        
        complexFormat = new ComplexFormat();
//        stackForNumberStack = new Stack<>();
//        stackForOperationStack = new Stack<>();
//        autoConstantNumberStack = new Stack<>();
//        autoConstantOperationStack = new Stack<>();

        result = false;
    }

    private Complex cplxPlus(Complex cplxNum1, Complex cplxNum2) {
        protocol.print(cplxNum1);
        protocol.print(" + ");
        protocol.print(cplxNum2);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.add(cplxNum2);
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    private Complex cplxMinus(Complex cplxNum1, Complex cplxNum2) {
        protocol.print(cplxNum1);
        protocol.print(" - ");
        protocol.print(cplxNum2);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.subtract(cplxNum2);
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    private Complex cplxMultiply(Complex cplxNum1, Complex cplxNum2) {
        protocol.print(cplxNum1);
        protocol.print(" * ");
        protocol.print(cplxNum2);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.multiply(cplxNum2);
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    private Complex cplxDivide(Complex cplxNum1, Complex cplxNum2) {
        protocol.print(cplxNum1);
        protocol.print(" ÷ ");
        protocol.print(cplxNum2);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.divide(cplxNum2);
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    private Complex cplxPower(Complex cplxNum1, Complex cplxNum2) {
        protocol.print(cplxNum1);
        protocol.print(" ^ ");
        protocol.print(cplxNum2);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.pow(cplxNum2);
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    private List cplxSqr(Complex cplxNum1, int n) {
        switch (n) {
            case 2:
                protocol.print("√");
                break;
            case 3:
                protocol.print("³√");
                break;
            default:
                protocol.print(n);
                protocol.print("√");
        }
        protocol.print(cplxNum1);
        protocol.print(" = ");
        List cplxNumList;
        cplxNumList = cplxNum1.nthRoot(n);
        protocol.println(cplxNumList);
        return cplxNumList;
    }

    Complex cplxSin(Complex cplxNum1) {
        protocol.print("sin");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.sin();
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxCos(Complex cplxNum1) {
        protocol.print("cos");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.cos();
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxTan(Complex cplxNum1) {
        protocol.print("tan");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.tan();
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxAsin(Complex cplxNum1) {
        protocol.print("arcsin");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.asin();
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxAcos(Complex cplxNum1) {
        protocol.print("arccos");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.acos();
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxAtan(Complex cplxNum1) {
        protocol.print("arctan");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.atan();
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxSinh(Complex cplxNum1) {
        protocol.print("sh");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.sinh();
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxCosh(Complex cplxNum1) {
        protocol.print("ch");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.cosh();
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxTanh(Complex cplxNum1) {
        protocol.print("tanh");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.tanh();
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxArsh(Complex cplxNum) {
        protocol.print("Arsh");
        protocol.print(cplxNum);
        protocol.print(" = ");
        double re = cplxNum.getReal();
        Log.d(TAG, "Re: " + re);
        double im = cplxNum.getImaginary();
        Log.d(TAG, "Im: " + im);
        Complex cplxNum1 = new Complex(re, im);
        Log.d(TAG, "cplxNum1: " + cplxNum1);
        cplxNum1 = cplxNum1.multiply(cplxNum1);
        Log.d(TAG, "cplxNum1^2: " + cplxNum1);
        cplxNum1 = cplxNum1.add(1);
        Log.d(TAG, "cplxNum1^2 + 1: " + cplxNum1);
        cplxNum1 = cplxNum1.sqrt();
        Log.d(TAG, "sqrt(cplxNum1^2 + 1): " + cplxNum1);
        cplxNum1 = cplxPlus(cplxNum, cplxNum1);
        Log.d(TAG, "cplxNum + sqrt(cplxNum1^2 + 1): " + cplxNum1);
        cplxNum1 = cplxNum1.log();
        Log.d(TAG, "Ln(cplxNum + sqrt(cplxNum1^2 + 1)): " + cplxNum1);
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxArch(Complex cplxNum) {
        protocol.print("Arch");
        protocol.print(cplxNum);
        protocol.print(" = ");
        double re = cplxNum.getReal();
        Log.d(TAG, "Re: " + re);
        double im = cplxNum.getImaginary();
        Log.d(TAG, "Im: " + im);
        Complex cplxNum1 = new Complex(re, im);
        Log.d(TAG, "cplxNum1: " + cplxNum1);
        cplxNum1 = cplxNum1.multiply(cplxNum1);
        Log.d(TAG, "cplxNum1^2: " + cplxNum1);
        cplxNum1 = cplxNum1.subtract(1);
        Log.d(TAG, "cplxNum1^2 - 1: " + cplxNum1);
        cplxNum1 = cplxNum1.sqrt();
        Log.d(TAG, "sqrt(cplxNum1^2 - 1): " + cplxNum1);
        cplxNum1 = cplxPlus(cplxNum, cplxNum1);
        Log.d(TAG, "cplxNum + sqrt(cplxNum1^2 - 1): " + cplxNum1);
        cplxNum1 = cplxNum1.log();
        Log.d(TAG, "Ln(cplxNum + sqrt(cplxNum1^2 - 1)): " + cplxNum1);
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxArth(Complex cplxNum) {
        protocol.print("Arth");
        protocol.print(cplxNum);
        protocol.print(" = ");
        double re = cplxNum.getReal();
        Log.d(TAG, "Re: " + re);
        double im = cplxNum.getImaginary();
        Log.d(TAG, "Im: " + im);
        Complex cplxNum1 = new Complex(re, im);
        Complex cplxNum2 = new Complex(re, im);
        Log.d(TAG, "cplxNum1: " + cplxNum1);
        Log.d(TAG, "cplxNum2: " + cplxNum2);
        cplxNum1 = cplxNum1.add(1);
        Log.d(TAG, "cplxNum1 + 1: " + cplxNum1);
        cplxNum2 = cplxNum2.subtract(1);
        Log.d(TAG, "cplxNum2 - 1: " + cplxNum2);
        cplxNum2 = cplxNum2.multiply(-1);
        Log.d(TAG, "1 - cplxNum2: " + cplxNum2);
        cplxNum1 = cplxNum1.divide(cplxNum2);
        Log.d(TAG, "(1 + cplxNum1)/(1 - cplxNum2): " + cplxNum1);
        cplxNum1 = cplxNum1.log();
        Log.d(TAG, "Ln((1 + cplxNum1)/(1 - cplxNum2)): " + cplxNum1);
        cplxNum1 = cplxNum1.multiply(0.5);
        Log.d(TAG, "0.5 * Ln((1 + cplxNum1)/(1 - cplxNum2)): " + cplxNum1);
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxExp(Complex cplxNum1) {
        protocol.print("e^");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.exp();
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxLog(Complex cplxNum1) {
        protocol.print("Ln");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.log();
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxLog10(Complex cplxNum1) {
        protocol.print("Log");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        cplxNum1 = cplxDivide(cplxNum1.log(), new Complex(Math.log(10), 0));
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxSqrt(Complex cplxNum1) {
        protocol.print("√");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.sqrt();
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxCbrt(Complex cplxNum1) {
        Log.d(TAG, "Вычисляем корень 3-ей степени из " + cplxNum1);
        Log.d(TAG, "Будет 3 результата:");
//        protocol.print("³√");
//        protocol.print(cplxNum1);
//        protocol.print(" = ");
        List cplxNumList;
        cplxNumList = cplxSqr(cplxNum1, 3);
        for (int i = 0; i < cplxNumList.size(); i++) {
            Log.d(TAG, (i + 1) + ": " + cplxNumList.get(i));
        }
//        protocol.println(cplxNumList);
        return (Complex) cplxNumList.get(0);
    }

    Complex cplx10x(Complex cplxNum1) {
        protocol.print("10^");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        Complex num10 = new Complex(10, 0);
        cplxNum1 = num10.pow(cplxNum1);
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxPower2(Complex cplxNum1) {
        protocol.print(cplxNum1);
        protocol.print("^2 = ");
        cplxNum1 = cplxNum1.pow(2.0);
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxReciprocal(Complex cplxNum1) {
        protocol.print("1/");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.reciprocal();
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex cplxNegate(Complex cplxNum1) {
        protocol.print("-");
        protocol.print(cplxNum1);
        protocol.print(" = ");
        cplxNum1 = cplxNum1.negate();
        protocol.println(cplxNum1);
        return cplxNum1;
    }

    Complex complexStackCalculator(Complex curCplxNum, int currentOperation) {
        Log.d(TAG, "complexStackCalculator запущен");
        int prevOperation;
        Complex prevCplxNumber;
        int currentPrioritet;
        int prevPrioritet;
        // Пункт №1 если не было операций - нужно запомнить введенное значение и введенную операцию
        if (complexStack.empty()) { //Если стек чисел пуст
            if (currentOperation != NOP) { //Если была любая операция кроме NOP
                Log.d(TAG, "Еще не было операций, заношу значения в новый стек чисел и в новый стек операций");
                complexStack.push(curCplxNum);
                //secondOp (currentNumber);                
                Log.d(TAG, "В стек чисел заношу: " + curCplxNum);
                operationStack.push(currentOperation);
                Log.d(TAG, "В стек операций заношу: " + currentOperation);

            }
            if (currentOperation == NOP) { //Если была операция NOP
                Log.d(TAG, "Вычисления с использованием постоянных в режиме работы с комплексными числами нет");
                //curCplxNum = autoConstantCalculator2 (curCplxNum);
                //printCalc(currentNumber);
            }
            curCplxNum = Complex.ZERO;
            return curCplxNum;
        }
        // Здесь уже ясно, что в стеке чисел что-то есть
//        if (curCplxNum.equals(Complex.ZERO) && !calcpress) { //В стеке чисел и операций уже что-то есть, новое число не вводили, а нажали на операцию, значит в стеке чисел - автоконстанта
//            //Log.d(TAG, "В стеке чисел и операций уже что-то есть, новое число не вводили, а нажали на операцию, значит в стеке чисел - автоконстанта");
//            Log.d(TAG, "Вычислений с использованием постоянных в режиме работы с комплексными числами нет");
//            //autoConstantOperation2 = operationStack.pop();
//            //cplxAautoConstant(complexStack.pop());
//            //Log.d(TAG, "Размер стека операций = "+operationStack.size());
//            //Log.d(TAG, "Размер стека чисел = "+numberStack2.size());
//            curCplxNum = Complex.ZERO;
//            return curCplxNum;
//        }

        Log.d(TAG, "Размер стека операций = " + operationStack.size());
        Log.d(TAG, "Размер стека комплексных чисел = " + complexStack.size());
        prevOperation = operationStack.peek(); // Смотрим какая была предыдущая операция
        currentPrioritet = currentOperation >>> 1; //Вычисляем приоритет        
        prevPrioritet = prevOperation >>> 1; //Вычисляем приоритет
        prevCplxNumber = complexStack.peek();
        Log.d(TAG, "Предыдущая операция была: " + prevOperation + " ее приоритет: " + prevPrioritet);
        Log.d(TAG, "Текущая операция: " + currentOperation + " ее приоритет: " + currentPrioritet);
        Log.d(TAG, "Первое комплексное число: " + prevCplxNumber);
        Log.d(TAG, "Второе комплексное число: " + curCplxNum);
        if (prevPrioritet < currentPrioritet) { // Пункт №2 если предыдущая операция более низкого приоритета - занести в стек число и операцию
            Log.d(TAG, "У текущей операции приоритет выше, чем у предыдущей, заношу все в стек");
            complexStack.push(curCplxNum);
            //secondOp (curCplxNum);            
            Log.d(TAG, "В стек чисел заношу: " + curCplxNum);
            operationStack.push(currentOperation);
            Log.d(TAG, "В стек операций заношу: " + currentOperation);
            return curCplxNum;
        }
        // Здесь уже ясно, что предыдущая операция такого же или более высокого приоритета, т.е. Пункт №3        
        do {
            prevOperation = operationStack.pop();
            prevCplxNumber = complexStack.pop();
            //secondOp (curCplxNum);            
            switch (prevOperation) {
                case PLUS:
                    Log.d(TAG, prevCplxNumber + " + " + curCplxNum + " = ");
                    curCplxNum = cplxPlus(prevCplxNumber, curCplxNum);
                    break;
                case MINUS:
                    Log.d(TAG, prevCplxNumber + " - " + curCplxNum + " = ");
                    curCplxNum = cplxMinus(prevCplxNumber, curCplxNum);
                    break;
                case MULTIPLY:
                    Log.d(TAG, prevCplxNumber + " × " + curCplxNum + " = ");
                    curCplxNum = cplxMultiply(prevCplxNumber, curCplxNum);
                    break;
                case DIVIDE:
                    Log.d(TAG, prevCplxNumber + " ÷ " + curCplxNum + " = ");
                    curCplxNum = cplxDivide(prevCplxNumber, curCplxNum);
                    break;
                case POWER:
                    Log.d(TAG, prevCplxNumber + " ^ " + curCplxNum + " = ");
                    curCplxNum = cplxPower(prevCplxNumber, curCplxNum);
                    break;
                case X_SQR_Y:
                    Log.d(TAG, "Вычисляем корень " + (int) curCplxNum.getReal() + " степени из " + prevCplxNumber);
                    Log.d(TAG, "Будет " + (int) curCplxNum.getReal() + " результата(ов):");
                    List cplxNumList;
//                    protocol.print((int) curCplxNum.getReal() + "√");
//                    protocol.print(prevCplxNumber);
//                    protocol.print(" = ");
                    cplxNumList = cplxSqr(prevCplxNumber, (int) curCplxNum.getReal());
//                    protocol.println(cplxNumList);
                    curCplxNum = (Complex) cplxNumList.get(0);
                    for (int i = 0; i < cplxNumList.size(); i++) {
                        Log.d(TAG, (i + 1) + ": " + cplxNumList.get(i));
                    }

                    break;
                case NOP:
                    break;
            }
            Log.d(TAG, curCplxNum.toString());
        }
        while (!operationStack.empty() && (operationStack.peek() >>> 1) >= currentPrioritet); // если в стеке операций еще что-то есть и оно более высокого приоритета, то повторить

        //operationSwitcher2 (prevOperation, currentOperation); // Передаем в operationSwitcher предыдущую операцию и текущую операцию                                               
        if (!operationStack.empty()) { //Стек будет не пуст, если предыдущая операция оказалась более низкого приоритета
            prevOperation = operationStack.peek();
            prevPrioritet = prevOperation >>> 1;
            Log.d(TAG, "Предыдущая операция была: " + prevOperation);
            Log.d(TAG, "Её приоритет: " + prevPrioritet);
        }
        Log.d(TAG, "Размер стека операций = " + operationStack.size());
        Log.d(TAG, "Размер стека комплексных чисел = " + complexStack.size());
        //Log.d(TAG, currentNumber);

        if (currentOperation != NOP) { //Если была любая операция кроме NOP, то заносим значения в стек чисел и в стек операций
            Log.d(TAG, "Вычисления еще не окончены. Заношу значения в стек чисел и в стек операций");
            complexStack.push(curCplxNum);
            //secondOp (currentNumber);            
            Log.d(TAG, "В стек чисел заношу: " + curCplxNum);
            operationStack.push(currentOperation);
            Log.d(TAG, "В стек операций заношу: " + currentOperation);

        }
        //printCalc(currentNumber);
        Log.d(TAG, "------------------------------------------------");
        return curCplxNum;
    }
}
