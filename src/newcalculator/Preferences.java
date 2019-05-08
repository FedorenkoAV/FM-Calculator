/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcalculator;

import java.io.File;
import java.io.IOException;
import org.ini4j.Wini;

/**
 *
 * @author User
 */
public class Preferences {

    private static final String TAG = "Preferences";

    File paramsFile = new File("params.ini");
    ParamsFile iniFile;
    Wini ini = null;

    Preferences() {
        if (!paramsFile.exists()) {
            try {
                paramsFile.createNewFile();
            } catch (IOException ex) {
            }
        }
        try {
            ini = new Wini(new File(paramsFile.getName()));
        } catch (IOException ex) {
        }
        Log.d(TAG, "Создали новый Preferences");
    }

    double getMemory() {
        return ini.get("Memory", "Memory", double.class);
    }

    void setMemory(double memoryValue) {
        ini.put("Memory", "Memory", memoryValue);
        Log.d(TAG, "Занесли значение памяти в настройки: " + memoryValue);
        store();
    }

    int getFixMode() {
        return ini.get("FixMode", "FixMode", int.class);
    }

    void setFixMode(int fixModeScale) {
        ini.put("FixMode", "FixMode", fixModeScale);
        Log.d(TAG, "Занесли значение режима округления в настройки: " + fixModeScale);
        store();
    }

    int getAngleUnit() {
        return ini.get("AngleUnit", "AngleUnit", int.class);
    }

    void setAngleUnit(int angleUnit) {
        ini.put("AngleUnit", "AngleUnit", angleUnit);
        Log.d(TAG, "Занесли значение едениц измерения угла в настройки: " + angleUnit);
        store();
    }

    int getXCoord() {
        return ini.get("Coordinates", "x", int.class);
    }

    void setXCoord(int xCoord) {
        ini.put("Coordinates", "x", xCoord);
        Log.d(TAG, "Занесли значение едениц измерения угла в настройки: " + xCoord);
        store();
    }

    int getYCoord() {
        return ini.get("Coordinates", "y", int.class);
    }

    void setYCoord(int yCoord) {
        ini.put("Coordinates", "y", yCoord);
        Log.d(TAG, "Занесли значение едениц измерения угла в настройки: " + yCoord);
        store();
    }

    void store() {
        try {
            ini.store();
        } catch (IOException ex) {
        }
        Log.d(TAG, "Зафиксировали изменения в файле настроек.");
    }
}
