package newcalculator;

/**
 * Created by User on 21.07.2017.
 * ArgX это переменная, в которой в текстовом виде хранится число в шестнадцатиричной форме в дополнительном коде
 * (число отдельно, знак отдельно)
 */
class ArgXHex {
    private static final String TAG = "ArgXHex";

    private StringBuilder number;// здесь будет храниться число
    private boolean sign;// Знак числа
    private boolean editable;// Число можно редактировать
    private boolean virginity;// Число еще не редактировали

    ArgXHex() {
        number = new StringBuilder("");
        sign = false;
        editable = true;
        virginity = true;
        L.d(TAG, "Создали новый пустой ArgXHex");
    }

//    ArgXHex(long longNumber) {
//        setNumber(longNumber);
//        L.d(TAG, "Создали новый НЕпустой ArgXHex");
//    }
//
//    ArgXHex(int intNumber) {
//        setNumber(intNumber);
//        L.d(TAG, "Создали новый НЕпустой ArgXHex");
//    }

    ArgXHex(double doubleNumber) {
        setNumber(doubleNumber);
        L.d(TAG, "Создали новый НЕпустой ArgXHex");
    }

    public void setNumber(StringBuilder sbNumber) {
        number = new StringBuilder(sbNumber);
        editable = false;
        virginity = false;
    }

    public void setNumber(int intNumber) {
        setSign(false);
        if (intNumber < 0) {
            setSign(true);
            intNumber = -intNumber;
        }
        number = new StringBuilder(Integer.toHexString(intNumber));
        editable = false;
        virginity = false;
    }

    public void setNumber(long longNumber) {
        setSign(false);
        if (longNumber < 0) {
            setSign(true);
            longNumber = -longNumber;
        }
        number = new StringBuilder(Long.toHexString(longNumber));
        editable = false;
        virginity = false;
    }

    public void setNumber(double doubleNumber) {
        long longNumber = (long) doubleNumber;
        setNumber(longNumber);
    }


    public StringBuilder getNumber() {
        return number;
    }

    double getDouble(int byteLength) {
        double doubleNumber;
        doubleNumber = (double) getLong(byteLength);
        L.d(TAG, "После преобразования в double получили: " + doubleNumber);
        return doubleNumber;
    }

    long getLong(int byteLength) {
        long longNumber = 0;
        L.d(TAG, "В ArgXHex лежит: " + number);
        if (number.length() == 0) {
            return 0;
        }
        StringBuilder num = new StringBuilder("");
        num.append(number);
        int curDigit;
        boolean sign = isSign();
        curDigit = Character.digit(num.charAt(0), 16);
        if ((num.length() == byteLength*2) && (curDigit > 7)) {//Если у нас число максимальной длинны и первый символ больше 7, то у нас отрицательное число
            L.d(TAG, "У нас число максимальной длинны и первый символ больше 7, т.е. " + num + " это отрицательное число.");
            sign = !sign;//Инвертируем знак
            L.d(TAG, "Инвертируем знак sign = " + sign);
            L.d(TAG, "Инвертируем цифры числа побитово.");
            for (int i = 0; i < num.length(); i++) {
                switch (num.charAt(i)) {
                    case '0':
                        num.setCharAt(i, 'F');
                        break;
                    case '1':
                        num.setCharAt(i, 'E');
                        break;
                    case '2':
                        num.setCharAt(i, 'D');
                        break;
                    case '3':
                        num.setCharAt(i, 'C');
                        break;
                    case '4':
                        num.setCharAt(i, 'B');
                        break;
                    case '5':
                        num.setCharAt(i, 'A');
                        break;
                    case '6':
                        num.setCharAt(i, '9');
                        break;
                    case '7':
                        num.setCharAt(i, '8');
                        break;
                    case '8':
                        num.setCharAt(i, '7');
                        break;
                    case '9':
                        num.setCharAt(i, '6');
                        break;
                    case 'A':
                        num.setCharAt(i, '5');
                        break;
                    case 'B':
                        num.setCharAt(i, '4');
                        break;
                    case 'C':
                        num.setCharAt(i, '3');
                        break;
                    case 'D':
                        num.setCharAt(i, '2');
                        break;
                    case 'E':
                        num.setCharAt(i, '1');
                        break;
                    case 'F':
                        num.setCharAt(i, '0');
                        break;
                    case 'a':
                        num.setCharAt(i, '5');
                        break;
                    case 'b':
                        num.setCharAt(i, '4');
                        break;
                    case 'c':
                        num.setCharAt(i, '3');
                        break;
                    case 'd':
                        num.setCharAt(i, '2');
                        break;
                    case 'e':
                        num.setCharAt(i, '1');
                        break;
                    case 'f':
                        num.setCharAt(i, '0');
                        break;
                }
            }
            L.d(TAG, "Положительное число в прямом коде: " + num);
            longNumber = Long.parseLong(num.toString(), 16);
            L.d(TAG, "Преобразовали к типу long: " + longNumber);
            longNumber++;
            L.d(TAG, "Увеличили на еденицу: " + longNumber);
        } else {
            try {
                longNumber = Long.parseLong(num.toString(), 16);

            } catch (Exception e) {
                L.d(TAG, "При преобразовании String в long произошла ошибка: " + e);
                StackTraceElement[] stackTraceElements = e.getStackTrace();

                for (int i = 0; i < stackTraceElements.length; i++) {
                    L.d(TAG, i + ": " + stackTraceElements[i].toString());
                }
            }
        }
        if (sign) {
            longNumber = -1 * longNumber;
        }
        L.d(TAG, "После преобразования в long получили: " + longNumber);
        return longNumber;
    }

    void setFromStringBuilder(StringBuilder num) {
        number = num;
    }

    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
        L.d(TAG, "sign: " + this.sign);
    }

    boolean isEditable() {
        return editable;
    }

//    public void setEditable(boolean editable) {
//        this.editable = editable;
//    }

    boolean isVirgin() {
        return virginity;
    }

    void setNotVirgin() {
        virginity = false;
    }
}
