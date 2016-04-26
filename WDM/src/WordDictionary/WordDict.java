package WordDictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class WordDict {
	
	static String DataSet = "bin/tagged_text_UTF-8.txt";
	static String WordList = "bin/WordDictFile";
	static int wordSize = 0;
	static HashMap<String,WordCount> WordPool = new HashMap<String,WordCount>();
	public static int readFileByLines(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            line = 1;
            while ((tempString = reader.readLine()) != null) {
            	String[] List = tempString.split(" ");
            	for (int i = 0;i<List.length;i++)
            	{
            		if (WordPool.containsKey(List[i]))
            		{
            			WordCount wc = WordPool.get(List[i]);
            			if (wc.TextID!=line)
            			{
            				wc.TextID = line;
            				wc.Textnum++;
            			}
            			wc.num++;
            		}
            		else
            		{
            			WordCount wc = new WordCount();
            			wc.num = 1;
            			wc.Textnum  = 1;
            			wc.TextID = line;
            			wc.wordID = wordSize++;
            			WordPool.put(List[i], wc);
            		}
            	}
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
	static public void main(String[] args) throws IOException
	{
		FileWriter fw = new FileWriter(WordList);
		readFileByLines(DataSet);
		Iterator<HashMap.Entry<String,WordCount>> iter = WordPool.entrySet().iterator();
        while (iter.hasNext()) {
        	HashMap.Entry<String,WordCount> entry = (HashMap.Entry<String,WordCount>) iter.next();
        	String key = entry.getKey();
        	WordCount val = entry.getValue();
        	fw.write(key+" "+String.valueOf(val.num)+" "+String.valueOf(val.Textnum)+" "+String.valueOf(val.wordID)+"\n");
        }
        fw.close();
	}
}
