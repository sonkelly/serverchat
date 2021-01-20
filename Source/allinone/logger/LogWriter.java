/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Vostro 3450
 */
public class LogWriter {

    public String fileName;
    public FileWriter out;

    public LogWriter(String file) {
        fileName = file;
    }
    public void write(String mess, String type) throws IOException {
        out = new FileWriter(fileName, true);
         String message = now() + " - [" + fileName + "]" + "["+type+"] " + mess+"\n";
        out.write(message);
        out.close();
    }
    public void write(String mess) throws IOException {
        out = new FileWriter(fileName, true);
        out.write(mess);
        out.close();
    }
    
    
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    public String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
}
