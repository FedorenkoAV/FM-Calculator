/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcalculator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Федоренко Александр
 */
public class ParamsFile {
    
    private static final String TAG = "ParamsFile";
     
    Properties ht = new Properties(); 
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
    String name, number; 
    FileInputStream fin = null; 
    boolean changed = false;
    String fileName;
    
    public ParamsFile (String fName) {
        fileName = fName;
        openFile (fileName);
    }
    
    private void openFile (String fName) {        
        // Пытаемся открыть файл. 
        try {            
            fin = new FileInputStream(fName); 
        } catch (FileNotFoundException ex) { 
            // игнорируем отсутствующий файл
        }
        
        /* Если файл уже есть, загружаем данные. */ 
        try {            
            if (fin != null) {                
                ht.load(fin); 
                fin.close(); 
            } 
        } catch (IOException ex) { 
            Log.d(TAG, "Ошибка чтения файла."); 
        }        
    }
    
    public String getProperty (String var, String def) {        
        return ht.getProperty(var, def);        
    }
    
    public int getProperty (String var, int def) {        
        return Integer.parseInt(ht.getProperty(var, Integer.toString(def)));        
    }
    
    public double getProperty (String var, double def) {        
        return Double.parseDouble(ht.getProperty(var, Double.toString(def)));        
    }
    
    public void setProperty (String var, String def) {        
        ht.put(var, def);
    }
    
    public void setProperty (String var, int def) {
        
        ht.put(var, Integer.toString(def));
    }
    
    public void setProperty (String var, double def) {        
        ht.put(var, Double.toString(def));
    }
    
    public void savePropertyToFile () throws FileNotFoundException, IOException {        
        try (FileOutputStream fout = new FileOutputStream(fileName)) {
            ht.store(fout, "Property");
        } 
            
        
    }    
}