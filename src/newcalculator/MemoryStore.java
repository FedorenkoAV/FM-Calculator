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
public class MemoryStore {
    private static final String TAG = "MemoryStore";

    private Status status;
    private Preferences preferences;
    private ProtocolJFrame protocol;

    MemoryStore(Preferences preferences, Status status, ProtocolJFrame protocol) {
        this.status = status;
        this.preferences = preferences;
        this.protocol = protocol;
        Log.d(TAG, "status: " + status);
        Log.d(TAG, "preferences: " + preferences);
        displayMemory(getMemory());
    }

    double getMemory() {
        double memory = preferences.getMemory();
        Log.d(TAG, "Получаем значение памяти из настроек: " + memory);
        protocol.setMemory(memory);
        protocol.println ("Получаем значение из памяти: " + memory);
        return memory;
    }

    void setMemory(double memory) {
        preferences.setMemory(memory);
        displayMemory(memory);
        protocol.setMemory(memory);
        protocol.println ("Заносим новое значение в память: " + memory);
        Log.d(TAG, "Теперь в памяти: " + memory);
    }

    private void displayMemory(double memory){
        if (memory != 0) {
            status.onMemory();
        } else {
            status.offMemory();
        }
    }
}
