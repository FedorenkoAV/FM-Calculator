/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Properties;

/**
 *
 * @author Федоренко Александр
 */
public class INIFile {
    Properties iniProperty = new Properties();
    String iniFileName;
    
    /**
     *
     * @param f ссылка на INI файл
     * @throws IOException
     */
    public INIFile(File f) throws IOException {
        this(f.getPath()); // Вызвает INIFile(String fname) - другую версию конструктора этого класса  
    }
    
    /**
     *
     * @param fname ссылка на INI файл
     * @throws IOException
     */
    public INIFile(String fname) throws IOException {
        iniFileName = fname;
        openFile (iniFileName);
    }
    
    private void openFile (String fname) throws IOException {
        try {
            RandomAccessFile raf = new RandomAccessFile(fname, "rw");
            String section = "";
            String line;
            
            while((line = raf.readLine()) != null) {                
                if( line.startsWith(";") ) continue;
                if( line.startsWith("#") ) continue;
                if( line.startsWith("--") ) continue;
                if( line.startsWith("[") ) {
                    section = line.substring(1, line.lastIndexOf("]")).trim();                        
                    continue;
                }
                addProperty(section, line);
            }
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(INIFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void addProperty(String section, String line) {        
        int equalIndex = line.indexOf("=");

        if( equalIndex > 0 ) {            
            String name = section + '.' + line.substring(0, equalIndex).trim();
            String value = line.substring(equalIndex+1).trim();
            iniProperty.put(name, value);            
        }
    }

    /**
     *
     * @param section секция
     * @param var значение
     * @param def значение которое будет установлено, если праметр не быдет найден
     * @return
     */
    public String getProperty (String section, String var, String def) {        
        return iniProperty.getProperty(section+'.'+var, def);        
    }

    /**
     *
     * @param section
     * @param var
     * @param def
     * @return
     */
    public int getProperty (String section, String var, int def) {
        String sval = getProperty(section, var, Integer.toString(def)); //переводит числовое значение в строковое и вызывает перезагруженный метод
        return Integer.decode(sval);
    }

    /**
     *
     * @param section
     * @param var
     * @param def
     * @return
     */
    public boolean getProperty (String section, String var, boolean def) {
        String sval = getProperty (section, var, def ? "True":"False");
        return sval.equalsIgnoreCase("Yes") || sval.equalsIgnoreCase("True");
    }
    
    /**
     *
     * @param section
     * @param var
     * @param def
     * @return
     */
    public int getProperty (String section, String var, double def) {
        String sval = getProperty(section, var, Double.toString(def)); //переводит числовое значение в строковое и вызывает перезагруженный метод
        return Integer.decode(sval);
    }
    
    /**
     *
     * @param section
     * @param var
     * @param def
     */
    public void setProperty (String section, String var, String def) {
        String name = section + '.' + var;        
        iniProperty.put(name, def);
    }
    
    /**
     *
     * @param section
     * @param var
     * @param def
     */
    public void setProperty (String section, String var, int def) {
        String name = section + '.' + var;        
        iniProperty.put(name, def);
    }
    
    /**
     *
     * @param section
     * @param var
     * @param def
     */
    public void setProperty (String section, String var, boolean def) {
        String name = section + '.' + var;        
        iniProperty.put(name, def);
    }
    
    /**
     *
     * @param section
     * @param var
     * @param def
     */
    public void setProperty (String section, String var, double def) {
        String name = section + '.' + var;        
        iniProperty.put(name, def);
    }
    
    /**
     *
     */
    public void savePropertyToFile () throws IOException {
        try {
            RandomAccessFile raf = new RandomAccessFile(iniFileName, "rw");
            String section = "";
            String line;
            //iniProperty.forEach(action);
                    
            while((line = raf.readLine()) != null) {                
                if( line.startsWith(";") ) continue;
                if( line.startsWith("#") ) continue;
                if( line.startsWith("--") ) continue;
                if( line.startsWith("[") ) {
                    section = line.substring(1, line.lastIndexOf("]")).trim();                        
                    continue;
                }
                addProperty(section, line);
            }
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(INIFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
