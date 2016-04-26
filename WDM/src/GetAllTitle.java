
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.*;



public class GetAllTitle {
		static FileWriter fileWriter;
		static String Text_Path="/Users/Zhengkoi/Desktop/zhwiki-20141009-pages-articles-multistream.xml";
	  public static int readFileByLines(String fileName) {
		  Pattern pattern = Pattern.compile("<title>.*</title>");
	        File file = new File(fileName);
	        BufferedReader reader = null;
	        int line = 0;
	        try {
	            reader = new BufferedReader(new FileReader(file));
	            String tempString = null;
	            line = 1;
	            while ((tempString = reader.readLine()) != null) {
	            	Matcher matcher = pattern.matcher(tempString);
	            	if (matcher.find())
	            	{
	            		line++;
	            		String str = matcher.group();
	            		PrintFile(str.substring(7, str.length()-8));
	            	}
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
	public static void PrintFile(String article_ID)
	{
		try {    
            fileWriter.write(article_ID+"\n");                  
  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
	}
	public static void main(String argu[])
	{
		try {  
			fileWriter = new FileWriter("result.txt");
		} catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
		System.out.println(readFileByLines(Text_Path));
	}
}
