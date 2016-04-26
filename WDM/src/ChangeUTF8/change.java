package ChangeUTF8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class change {
	public static int readFileByLines(String fileName) throws IOException {
        File file = new File(fileName);
        FileWriter fw = new FileWriter(fileName+"_Unicode");
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            line = 1;
            while ((tempString = reader.readLine()) != null) {
            	fw.write(tempString+"\n");
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return line; 
    }
}
