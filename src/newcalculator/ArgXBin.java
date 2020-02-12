package newcalculator;

/**
 * Created by User on 29.06.2017.
 * ArgX это переменная, в которой в текстовом виде хранится число в двоичной форме в дополнительном коде
 * (число отдельно, знак отдельно)
 */
class ArgXBin {
    
    private static final String TAG = "ArgXBin";

    private StringBuilder number;// здесь будет храниться число
    private boolean sign;// Знак числа
    private boolean editable;// Число можно редактировать
    private boolean virginity;// Число еще не редактировали

    ArgXBin() {
        number = new StringBuilder("");
        sign = false;
        editable = true;
        virginity = true;
        L.d(TAG, "Создали новый пустой ArgXBin");
    }

//    ArgXBin(long longNumber) {
//        setNumber(longNumber);
//        L.d(TAG, "Создали новый НЕпустой ArgXBin");
//    }
//
//    ArgXBin(int intNumber) {
//        setNumber(intNumber);
//        L.d(TAG, "Создали новый НЕпустой ArgXBin");
//    }

    ArgXBin(double doubleNumber) {
        setNumber(doubleNumber);
        L.d(TAG, "Создали новый НЕпустой ArgXBin");
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
        number = new StringBuilder(Integer.toBinaryString(intNumber));
        editable = false;
        virginity = false;
    }

    public void setNumber(long longNumber) {
        setSign(false);
        if (longNumber < 0) {
            setSign(true);
            longNumber = -longNumber;
        }
        number = new StringBuilder(Long.toBinaryString(longNumber));
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
        L.d(TAG, "В ArgXBin лежит: " + number);
        if (number.length() == 0) {
            return 0;
        }
        StringBuilder num = new StringBuilder("");
        num.append(number);
        int curDigit;
        boolean sign = isSign();
        curDigit = Character.digit(num.charAt(0), 2);
        if ((num.length() == byteLength * 8) && (curDigit > 0)) {//Если у нас число максимальной длинны и первый символ больше 0, то у нас отрицательное число
            L.d(TAG, "У нас число максимальной длинны и первый символ больше 0, т.е. " + num + " это отрицательное число.");
            sign = !sign;//Инвертируем знак
            L.d(TAG, "Инвертируем знак sign = " + sign);
            L.d(TAG, "Инвертируем цифры числа побитово.");
            for (int i = 0; i < num.length(); i++) {
                switch (num.charAt(i)) {
                    case '0':
                        num.setCharAt(i, '1');
                        break;
                    case '1':
                        num.setCharAt(i, '0');
                        break;
                }
            }
            L.d(TAG, "Положительное число в прямом коде: " + num);
            longNumber = Long.parseLong(num.toString(), 2);
            L.d(TAG, "Преобразовали к типу long: " + longNumber);
            longNumber++;
            L.d(TAG, "Увеличили на еденицу: " + longNumber);
        } else {
            try {
                longNumber = Long.parseLong(num.toString(), 2);

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

    void setFromStringBuilder(StringBuilder number) {
        this.number = number;
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
