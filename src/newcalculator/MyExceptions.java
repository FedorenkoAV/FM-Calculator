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
public class MyExceptions extends Throwable {

    final static int NONE = 0;
    final static int DIVIDE_BY_ZERO = 1;
    final static int NOT_INTEGER = 2;
    final static int TOO_BIG = 3;
    final static int TOO_SMALL = 4;
    final static int NEGATIVE = 5;
    final static int INFINITY = 6;
    final static int NEGATIVE_INFINITY = 7;
    final static int NOT_NUMBER = 8;
    final static int REDIRECT = 9;

    String exp[] = {"", "Деление на ноль.", "Ожидается целое число.", "Очень большое число.", "Очень маленькое число.", "Ожидается положительное число.", "Бесконечность.", "Минус бесконечность.", "Ожидается число.", "Запрос был перенаправлен по другому адресу."};

    private String msg = "";
    private int reason;


    MyExceptions(int reason) {
        this.reason = reason;
    }

    MyExceptions(int reason, String msg) {
        this.reason = reason;
        this.msg = msg;
    }

    String getReason() {
        return exp[reason];
    }

    String getMsg() {
        return msg;
    }
}
