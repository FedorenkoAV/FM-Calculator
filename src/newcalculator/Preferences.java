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
        boolean newFile = false;
        if (!paramsFile.exists()) {
            try {
                paramsFile.createNewFile();
            } catch (IOException ex) {
            }
            newFile = true;
        }
        try {
            ini = new Wini(new File(paramsFile.getName()));
        } catch (IOException ex) {
        }
        if (newFile) {
            setAngleUnitPrivate(0);
            setMemoryPrivate(0.0);
            setFixModePrivate(-1);
        }
        L.d(TAG, "Создали новый Preferences");
    }

    double getMemory() {
        return ini.get("Memory", "Memory", double.class);
    }

    void setMemory(double memoryValue) {
        setMemoryPrivate (memoryValue);
    }
    
    private void setMemoryPrivate(double memoryValue) {
        ini.put("Memory", "Memory", memoryValue);
        L.d(TAG, "Занесли значение памяти в настройки: " + memoryValue);
        store();
    }

    int getFixMode() {
        return ini.get("FixMode", "FixMode", int.class);
    }

    void setFixMode(int fixModeScale) {
        setFixModePrivate (fixModeScale);
    }
    
    private void setFixModePrivate(int fixModeScale) {
        ini.put("FixMode", "FixMode", fixModeScale);
        L.d(TAG, "Занесли значение режима округления в настройки: " + fixModeScale);
        store();
    }

    int getAngleUnit() {
        return ini.get("AngleUnit", "AngleUnit", int.class);
    }

    void setAngleUnit(int angleUnit) {
        setAngleUnitPrivate (angleUnit);
    }
    
    private void setAngleUnitPrivate(int angleUnit) {
        ini.put("AngleUnit", "AngleUnit", angleUnit);
        L.d(TAG, "Занесли значение едениц измерения угла в настройки: " + angleUnit);
        store();
    }

    int getXCoord() {
        return ini.get("Coordinates", "x", int.class);
    }

    void setXCoord(int xCoord) {
        ini.put("Coordinates", "x", xCoord);
        L.d(TAG, "Занесли значение едениц измерения угла в настройки: " + xCoord);
        store();
    }

    int getYCoord() {
        return ini.get("Coordinates", "y", int.class);
    }

    void setYCoord(int yCoord) {
        ini.put("Coordinates", "y", yCoord);
        L.d(TAG, "Занесли значение едениц измерения угла в настройки: " + yCoord);
        store();
    }

    void store() {
        try {
            ini.store();
        } catch (IOException ex) {
        }
        L.d(TAG, "Зафиксировали изменения в файле настроек.");
    }
}
