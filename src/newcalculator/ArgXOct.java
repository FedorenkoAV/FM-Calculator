package newcalculator;

/**
 * Created by User on 21.07.2017.
 * ArgX это переменная, в которой в текстовом виде хранится число в восьмеричной форме в дополнительном коде
 * (число отдельно, знак отдельно)
 */
public class ArgXOct {
    private static final String TAG = "ArgXOct";

    private StringBuilder number;// здесь будет храниться число
    private boolean sign;// Знак числа
    private boolean editable;// Число можно редактировать
    private boolean virginity;// Число еще не редактировали

    ArgXOct() {
        number = new StringBuilder("");
        sign = false;
        editable = true;
        virginity = true;
        L.d(TAG, "Создали новый пустой ArgXOct");
    }

//    ArgXOct(long longNumber) {
//        setNumber(longNumber);
//        L.d(TAG, "Создали новый НЕпустой ArgXOct");
//    }
//
//    ArgXOct(int intNumber) {
//        setNumber(intNumber);
//        L.d(TAG, "Создали новый НЕпустой ArgXOct");
//    }

    ArgXOct(double doubleNumber) {
        setNumber(doubleNumber);
        L.d(TAG, "Создали новый НЕпустой ArgXOct");
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
            intNumber = - intNumber;
        }
        number = new StringBuilder(Integer.toOctalString(intNumber));
        editable = false;
        virginity = false;
    }

    public void setNumber(long longNumber) {
        setSign(false);
        if (longNumber < 0) {
            setSign(true);
            longNumber = - longNumber;
        }
        number = new StringBuilder(Long.toOctalString(longNumber));
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

    public double getDouble() {
        double doubleNumber;
        doubleNumber = (double) getLong();
        L.d(TAG, "После преобразования в double получили: " + doubleNumber);
        return doubleNumber;
    }

    public long getLong() {
        long longNumber = 0;
        L.d(TAG, "В ArgXOct лежит: " + number);
        try {
            longNumber = Long.parseLong(number.toString(), 8);
        } catch (Exception e) {
//            customToast.setToastText("Произошла неизвестная ошибка: " + e);
//            customToast.show();
            L.d(TAG, "При старте приложения произошла ошибка: " + e);
            StackTraceElement[] stackTraceElements = e.getStackTrace();

            for (int i = 0; i < stackTraceElements.length; i++) {
                L.d(TAG, i + ": " + stackTraceElements[i].toString());
            }
        }

        if (isSign()) {
            longNumber = -1 * longNumber;
        }
        L.d(TAG, "После преобразования в long получили: " + longNumber);
        return longNumber;
    }

    public int getInt() {
        int intNumber;
        intNumber = Integer.parseInt(number.toString(), 8);
        return intNumber;
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
