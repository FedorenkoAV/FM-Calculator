/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcalculator;

/**
 * Created by User on 11.06.2017. Объект этого класса отвечает за переключение
 * едениц измерения углов. Само значение едениц измерения хранится в файле
 * настроек. При создании объекта необходимо указать объект отвечающий за
 * настройки и объект отвечающий за отображение едениц измерения углов на
 * статусном дисплее.
 */
class Angle {

    private static final String TAG = "Angle";

    static final int DEG = 0;
    static final int RAD = 1;
    static final int GRAD = 2;

    private String angleUnits[] = new String[3];

    private StatusDisplay statusDisplay;
    private Preferences preferences;

    Angle(Preferences preferences, StatusDisplay statusDisplay) {
        angleUnits[DEG] = "⁰";
        angleUnits[RAD] = "rad";
        angleUnits[GRAD] = "g";
        this.statusDisplay = statusDisplay;
        this.preferences = preferences;
        Log.d(TAG, "statusDisplay: " + statusDisplay);
        Log.d(TAG, "preferences: " + preferences);
        displayAngle(getAngle());
    }

    /**
     *
     * @return - возвращает текущие еденицы измерения углов.
     */
    int getAngle() {
        int angle = preferences.getAngleUnit();
        Log.d(TAG, "Получаем значение едениц измерения угла из настроек: " + angleUnits[angle]);
        return angle;
    }

    private void setAngle(int angle) {
        preferences.setAngleUnit(angle);
        Log.d(TAG, "Занесли значение едениц измерения угла в настройки: " + angleUnits[angle]);
        displayAngle(angle);
    }

    public String toString(int angle) {
        return angleUnits[angle];
    }

    private void displayAngle(int angle) {
        switch (angle) {
            case (DEG):
                statusDisplay.offRad();
                statusDisplay.offGrad();
                statusDisplay.onDeg();
                break;
            case (RAD):
                statusDisplay.offDeg();
                statusDisplay.offGrad();
                statusDisplay.onRad();
                break;
            case (GRAD):
                statusDisplay.offDeg();
                statusDisplay.offRad();
                statusDisplay.onGrad();
                break;
        }
    }

    /**
     * Переключает по кругу еденицы измерения углов DEG->RAD->GRAD->DEG.
     */
    void switchAngle() {
        int angle = preferences.getAngleUnit();
        if (angle < GRAD) {
            angle++;
        } else {
            angle = DEG;
        }
        setAngle(angle);
    }
}