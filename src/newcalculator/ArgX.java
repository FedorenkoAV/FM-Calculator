package newcalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * Created by User on 12.06.2017.
 * ArgX это переменная, в которой в текстовом виде хранятся мантисса и экспонента
 */

class ArgX {
    private StringBuilder mantissaIntegerPart; //Целая часть мантиссы
    private StringBuilder mantissaFractionalPart; //Дробная часть мантиссы
    private boolean mantissaSign;// Знак мантиссы
    private StringBuilder exponent; //Экспоненциальная часть числа
    private boolean exponentSign; //Знак экспоненты
    private boolean isExponent;// Флаг наличия экспоненциальной части числа
    private boolean isMantissaFractionalPart;//Флаг наличия дробной части мантиссы
    private boolean editable;// Число можно редактировать
    private boolean virginity;// Число еще не редактировали


    private static final String TAG = "ArgX";


    ArgX() {
        mantissaIntegerPart = new StringBuilder("");
        mantissaFractionalPart = new StringBuilder("");
        exponent = new StringBuilder("");
        isExponent = false;
        mantissaSign = false;
        exponentSign = false;
        isMantissaFractionalPart = false;
        editable = true;
        virginity = true;
        Log.d(TAG, "Создали новый пустой ArgX");
    }

    ArgX(double number) {
//        mantissaIntegerPart = new StringBuilder("");
//        mantissaFractionalPart = new StringBuilder("");
//        exponent = new StringBuilder("");
//        StringBuilder sb = new StringBuilder("");
//        StringBuilder sb1 = new StringBuilder("");
//        StringBuilder sb2 = new StringBuilder("");
//        StringBuilder sb3 = new StringBuilder("");
//        sb.append(number); //В sb у нас все число, начинаем его пилить
//        Log.d(TAG, "Начинаем пилить число " + sb);
///*//        1. Отделяем мантиссу от экспоненты
//*           2. Отделяем знак от экспоненты
//*           3. Отделяем знак от мантиссы
//*           4. Отделяем дробную часть от мантиссы
//*           5. У нас должна остаться целая часть мантиссы*/
//        sb1.append(getExponentSB(sb)); // В sb1 экспонента со знаком
//        Log.d(TAG, "Экспонента со знаком " + sb1);
//        if (sb1.length() == 0) { // Если нет экспоненты
//            isExponent = false;
//            exponentSign = false;
//        } else {
//            isExponent = true;
//            Log.d(TAG, "isExponent " + true);
//            exponentSign = getNumberSignSB(sb1);
//            Log.d(TAG, "exponentSign " + exponentSign);
//            exponent.append(getUnSignNumberSB(sb1));
//            Log.d(TAG, "exponent " + exponent);
//        }
//        Log.d(TAG, "Начинаем пилить мантиссу");
//        sb2.append(getMantissaSB(sb)); // В sb2 вся мантисса со знаком
//        Log.d(TAG, "Вся мантисса со знаком " + sb2);
//        mantissaSign = getNumberSignSB(sb2);
//        sb3.append(getUnSignNumberSB(sb2)); //В sb3 вся мантисса без знака
//        Log.d(TAG, "Вся мантисса без знака " + sb3);
//        mantissaFractionalPart.append(getMantissaFractionalPartSB(sb3));
//        isMantissaFractionalPart = true;
//        if (mantissaFractionalPart.length() == 0) {
//            isMantissaFractionalPart = false;
//            mantissaFractionalPart.append("0");
//        }
//        mantissaIntegerPart.append(getMantissaIntPartSB(sb3));
//        if (getMantissaIntegerPart().length() == 0) {
//            mantissaIntegerPart.append("0");
//        }
//        Log.d(TAG, "Создали новый ArgX");
//        Log.d(TAG, "Мантисса со знаком: " + mantissaSign);
//        Log.d(TAG, "Целая часть мантиссы: " + mantissaIntegerPart);
//        Log.d(TAG, "Дробная часть мантиссы: " + mantissaFractionalPart);
//
//        Log.d(TAG, "Экспонента со знаком: " + exponentSign);
//        Log.d(TAG, "Экспонента: " + exponent);
//        Log.d(TAG, "Есть экспонента: " + isExponent);
//        Log.d(TAG, "Есть дробная часть мантиссы: " + isMantissaFractionalPart);
        setDouble(number);
    }


    void setDouble(double number) {
        StringBuilder sb = new StringBuilder("");
        sb.append(number); //В sb у нас все число, начинаем его пилить
        setFromStringBuilder(sb);
    }


    void setFromString(String numberStr) {
        StringBuilder sb = new StringBuilder(numberStr);
        setFromStringBuilder(sb);
    }


    void setFromStringBuilder(StringBuilder sb) {  //В sb у нас все число, начинаем его пилить
        mantissaIntegerPart = new StringBuilder("");
        mantissaFractionalPart = new StringBuilder("");
        exponent = new StringBuilder("");
        StringBuilder sb1 = new StringBuilder("");
        StringBuilder sb2 = new StringBuilder("");
        StringBuilder sb3 = new StringBuilder("");
        int index = sb.indexOf(","); //Заменяем запятую на точку
        if (index != -1) {
            sb.replace(index, index + 1, ".");
        }

        Log.d(TAG, "Начинаем пилить число " + sb);
/*//        1. Отделяем мантиссу от экспоненты
*           2. Отделяем знак от экспоненты
*           3. Отделяем знак от мантиссы
*           4. Отделяем дробную часть от мантиссы
*           5. У нас должна остаться целая часть мантиссы*/
        sb1.append(getExponentSB(sb)); // В sb1 экспонента со знаком
        Log.d(TAG, "Вся экспонента вместе со знаком " + sb1);
        if (sb1.length() == 0) { // Если нет экспоненты
            Log.d(TAG, "Нет экспоненты.");
            Log.d(TAG, "exponent = " + exponent);
            isExponent = false;
            exponentSign = false;
        } else {
            isExponent = true;
            Log.d(TAG, "isExponent " + true);
            exponentSign = getNumberSignSB(sb1);
            Log.d(TAG, "exponentSign " + exponentSign);
            exponent.append(getUnSignNumberSB(sb1));
            Log.d(TAG, "exponent " + exponent);
        }
        Log.d(TAG, "Начинаем пилить мантиссу");
        sb2.append(getMantissaSB(sb)); // В sb2 вся мантисса со знаком
        Log.d(TAG, "Вся мантисса со знаком " + sb2);
        mantissaSign = getNumberSignSB(sb2);
        sb3.append(getUnSignNumberSB(sb2)); //В sb3 вся мантисса без знака
        Log.d(TAG, "Вся мантисса без знака " + sb3);
        mantissaFractionalPart.append(getMantissaFractionalPartSB(sb3));
        isMantissaFractionalPart = true;
        if (mantissaFractionalPart.length() == 0) {
            isMantissaFractionalPart = false;
            mantissaFractionalPart.append("0");
        }
        mantissaIntegerPart.append(getMantissaIntPartSB(sb3));
        if (getMantissaIntegerPart().length() == 0) {
            mantissaIntegerPart.append("0");
        }
        Log.d(TAG, "Создали новый ArgX");
        Log.d(TAG, "Мантисса со знаком: " + mantissaSign);
        Log.d(TAG, "Целая часть мантиссы: " + mantissaIntegerPart);
        Log.d(TAG, "Дробная часть мантиссы: " + mantissaFractionalPart);

        Log.d(TAG, "Экспонента со знаком: " + exponentSign);
        Log.d(TAG, "Экспонента: " + exponent);
        Log.d(TAG, "Есть экспонента: " + isExponent);
        Log.d(TAG, "Есть дробная часть мантиссы: " + isMantissaFractionalPart);
        virginity = false;
        editable = false;
    }


    void setMantissaIntegerPart(StringBuilder mantissaIntegerPart) {
        this.mantissaIntegerPart = mantissaIntegerPart;
        virginity = false;
    }

    StringBuilder getMantissaIntegerPart() {
        return mantissaIntegerPart;
    }


    void setMantissaFractionalPart(StringBuilder mantissaFractionalPart) {
        this.mantissaFractionalPart = mantissaFractionalPart;
    }

    StringBuilder getMantissaFractionalPart() {
        return mantissaFractionalPart;
    }


    void setSign(boolean sign) {
        this.mantissaSign = sign;
    }


    boolean isSign() {
        return mantissaSign;
    }

    void setExponent(StringBuilder exponent) {
        this.exponent = exponent;
    }

    StringBuilder getExponent() {
        if (isExponent()) {
            return exponent;
        }
        return new StringBuilder("0");
    }

    void setExponentSign(boolean exponentSign) {
        this.exponentSign = exponentSign;
    }

    boolean isExponentSign() {
        return exponentSign;
    }

    void setIsExponent(boolean exponent) {
        isExponent = exponent;
    }

    boolean isExponent() {
        return isExponent;
    }

    boolean isMantissaFractionalPart() {
        return isMantissaFractionalPart;
    }

    void setIsMantissaFractionalPart(boolean mantissaFractionalPart) {
        isMantissaFractionalPart = mantissaFractionalPart;
        virginity = false;
    }


    boolean isEditable() {
        return this.editable;
    }


//    void setEditable(boolean editable) {
//        this.editable = editable;
//    }

    double getMantissaIntegerPartinDouble() {
        Log.d(TAG, "Целая часть мантиссы: " + mantissaIntegerPart);
        if (mantissaIntegerPart.length() == 0) {
            return 0.0;
        } else {
            return Double.parseDouble(mantissaIntegerPart.toString());
        }
    }

    double getMantissaFractionalPartinDouble() {
        Log.d(TAG, "Дробная часть мантиссы: " + mantissaFractionalPart);
        if (mantissaFractionalPart.length() == 0) {
            return 0.0;
        } else {
            return Double.parseDouble("0." + mantissaFractionalPart.toString());
        }
    }

//    public long getMantissaFractionalPartinLong() {
//        Log.d(TAG, "Дробная часть мантиссы: " + mantissaFractionalPart);
//        if (mantissaFractionalPart.length() == 0) {
//            return 0;
//        } else {
//            return Long.parseLong(toDecimalFormat(mantissaFractionalPart));
//        }
//    }


//    private double getUnsignExponent() {
//        Log.d(TAG, "Экспонента: " + exponent);
//        if (exponent.length() == 0) {
//            return 0.0;
//        } else {
//            return Double.parseDouble(exponent.toString());
//        }
//    }
//
//    private double getUnsignMantissa() {
//        StringBuilder tmp1 = mantissaIntegerPart;
//        StringBuilder tmp2 = mantissaFractionalPart;
//        String tmp;
//        if (tmp1.length() == 0) {
//            tmp1.append('0');
//        }
//        if (tmp2.length() == 0) {
//            tmp2.append('0');
//        }
//        tmp = tmp1 + "." + tmp2;
//        Log.d(TAG, "UnsignMantissa: " + tmp);
//        return Double.parseDouble(tmp);
//    }

//    private double getSignExponent() {
//        Double tmp = getUnsignExponent();
//        if (exponentSign) {
//            tmp = -tmp;
//        }
//        return tmp;
//    }
//
//    private double getSignMantissa() {
//        Double tmp = getUnsignMantissa();
//        if (mantissaSign) {
//            tmp = -tmp;
//        }
//        return tmp;
//    }


    double getArgX() {
        Double number = 0.0;
        try {
            number = Double.parseDouble(getArgXSB().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, "Вылет с ошибкой: " + e);
        }
        editable = false;
        return number;
    }

    StringBuilder getArgXSB() {
        StringBuilder str = new StringBuilder("");  //Создаем пустую строку
        if (mantissaSign) { //Если мантисса с минусом, то добавляем сначала его
            str.append('-');
            Log.d(TAG, "В str добавили минус мантиссе: " + str);
        }
        //        Обрабатываем целую часть мантиссы
        if (mantissaIntegerPart.length() < 1) { //Если целая часть мантиссы пустая, то
            str.append('0'); // добавляем 0
        } else {
            str.append(mantissaIntegerPart);// Добавляем целую часть мантиссы
        }
        Log.d(TAG, "В str добавили целую часть мантиссы : " + str);

        //        Обрабатываем дробную часть мантиссы
        str.append('.'); //сначала добавляем точку
        if (!isMantissaFractionalPart) { // Если нет дробной части мантиссы, то
            str.append('0'); // добавляем 0
        } else { // а если есть дробная часть мантиссы, то
            str.append(mantissaFractionalPart);//добавляем дробную часть мантиссы
        }
        Log.d(TAG, "В str добавили дробную часть мантиссы : " + str);

        //        Обрабатываем экспоненциальную часть числа
        if (isExponent) { //Если есть экспоненциальная часть, то
            str.append('E');//сначала добавляем 'E'
            Log.d(TAG, "В str добавили E: " + str);
            if (exponentSign) {//Если экспонента с минусом, то здесь добавляем его
                str.append('-');
                Log.d(TAG, "В str добавили минус экспоненте: " + str);
            }

            str.append(exponent);//затем добавляем саму экспоненту
            Log.d(TAG, "В str добавили экспоненту: " + str);
        }
        Log.d(TAG, "В результате в str: " + str);
        getMantissaSB(str);
        getExponentSB(str);
        return str;
    }


//    private int getIndexOfE(StringBuilder SBNumber) {
//        Log.d(TAG, "Ищем индекс буквы E " + SBNumber);
//
//        int index;
//        int counter = 0;
//
//        index = SBNumber.lastIndexOf("e");
//
//        while (index != -1) { //Заменяем все e на E
//            SBNumber.insert(index, 'E');
//        }
//        index = SBNumber.lastIndexOf("E"); // Определяем количество букв E
//        while (index != -1) {
//            counter++;
//        }
//        if (counter == 0) { //Если counter = 0, то экспоненты нет
//            return 0;
//        }
//        if (counter > 1) { //Если counter > 1, то число плохое
//            return -1;
//        }
//
//        return index;
//    }

    /**
     * Функция принимает строковую переменную, содержащую число в
     * экспоненциальной форме и возвращает строковую переменную, содержащую
     * мантиссу числа. Если число не в экспоненциальной форме, то
     * функция возвращает строковую переменную без изменений
     *
     * @param SBNumber - строковая переменная, содержащая число в экспоненциальной форме.
     * @return SBMantissa - строковая переменная, содержащая мантиссу числа
     */
    private StringBuilder getMantissaSB(StringBuilder SBNumber) {
        StringBuilder SBMantissa = new StringBuilder("");
        int expIndex = SBNumber.indexOf("E"); //Ищем букву E
        if (expIndex != -1) { // Если есть экспоненциальная часть числа, то отделяем мантиссу от экспоненты
            SBMantissa.append(SBNumber.substring(0, expIndex));
        } else {
            SBMantissa.append(SBNumber);
        }
        Log.d(TAG, "SB Мантисса: " + SBMantissa);
        return SBMantissa;
    }

    /**
     * Функция принимает строковую переменную, содержащую число в
     * экспоненциальной форме и возвращает строковую переменную, содержащую
     * экспоненциальную часть числа. Если число не в экспоненциальной форме, то
     * функция возвращает ""
     *
     * @param SBNumber - строковая переменная, содержащая число в экспоненциальной форме.
     * @return SBExponent - строковая переменная, содержащая экспоненциальную часть числа.
     */
    private StringBuilder getExponentSB(StringBuilder SBNumber) {
        StringBuilder SBExponent = new StringBuilder("");
        int expIndex = SBNumber.indexOf("E"); //Ищем букву E
        if (expIndex != -1) { // Если есть экспоненциальная часть числа, то отделяем экспоненту от мантиссы
            SBExponent.append(SBNumber.substring(expIndex + 1, SBNumber.length()));
        }
        Log.d(TAG, "SB Экспонента: " + SBExponent);
        return SBExponent;
    }

    private StringBuilder getMantissaFractionalPartSB(StringBuilder mantissa) {
        int pointIndex;
        StringBuilder fracPart = new StringBuilder("");
        pointIndex = mantissa.indexOf(".");
        if (pointIndex != -1) {
            fracPart.append(mantissa.substring(pointIndex + 1, mantissa.length()));
        }
        return fracPart;
    }

    private StringBuilder getMantissaIntPartSB(StringBuilder mantissa) {
        int pointIndex;
        StringBuilder intPart = new StringBuilder("");
        pointIndex = mantissa.indexOf(".");
        if (pointIndex == -1) {//если нет точки, то вся мантисса и есть целая часть
            intPart.append(mantissa);
        } else {
            intPart.append(mantissa.substring(0, pointIndex));
        }
        return intPart;
    }


    private boolean getNumberSignSB(StringBuilder number) {
        return number.indexOf("-") != -1;
    }


    private StringBuilder getUnSignNumberSB(StringBuilder number) {
        if (!getNumberSignSB(number)) { //Если нет минуса, возвращаем то же число
            return number;
        }
        number.deleteCharAt(0);
        Log.d(TAG, "После удаления минуса:" + number);
        return number;
    }

    StringBuilder getRoundedMantissaFracPart(int scale, boolean withZeros) {
        Log.d(TAG, "Округляем дробную часть мантиссы");
        double num = getMantissaFractionalPartinDouble(); //Берем дробную часть мантиссы
        Log.d(TAG, "Здесь дробная часть мантиссы : " + num);
        BigDecimal newBigDecimal = new BigDecimal(num).setScale(scale, RoundingMode.HALF_UP);// Округляем ее
        Log.d(TAG, "newBigDecimal: " + newBigDecimal);
        num = newBigDecimal.doubleValue(); // num это наша округленная мантисса без нулей в конце
        Log.d(TAG, "Дробная часть мантиссы после округления: " + num);
        StringBuilder numSB = new StringBuilder("");
        numSB.append(fracParttoDecimalFormat(num));
        Log.d(TAG, "Дробная часть мантиссы после нормализации: " + numSB);
        StringBuilder newSB = new StringBuilder("");
        if (withZeros) {
            boolean zero = false;
            if (newBigDecimal.toString().equals("0E-7")) {
                newSB.append("0.0000000");
                zero = true;
            }
            if (newBigDecimal.toString().equals("0E-8")) {
                newSB.append("0.00000000");
                zero = true;
            }
            if (newBigDecimal.toString().equals("0E-9")) {
                newSB.append("0.000000000");
                zero = true;
            }
            if (!zero) {
                newSB.append((newBigDecimal));
            }
        } else {
            newSB.append(numSB);
        }
        newSB.delete(0, 2);//Удаляем '0' и '.'
        Log.d(TAG, "Дробная часть мантиссы после округления: " + newSB);
        return newSB;
    }

    private StringBuilder fracParttoDecimalFormat(Object srtDigit) {
        NumberFormat sciForm = new DecimalFormat("0.###############");
        return new StringBuilder(sciForm.format(srtDigit));
    }


    boolean isVirgin() {
        return virginity;

    }


    void setNotVirgin() {
        virginity = false;
    }
}
