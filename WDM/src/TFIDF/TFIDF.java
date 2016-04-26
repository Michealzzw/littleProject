package TFIDF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import WordDictionary.WordCount;

public class TFIDF {
	static String WordList = "bin/WordDictFile";
	static String _TFIDF_out = "bin/Sentences2TFIDF/Question_";
	static String _Content_out = "bin/Sentences2TFIDF/Content_";
	static String Topic_out =  "bin/Sentences2TFIDF/Question";
	static String Topic_in = "bin/tagged_question_Utf8.txt";
	static String ResultFileDictionary_Path = "bin/result_string/";
	static HashMap<String,WordCount> WordPool = new HashMap<String,WordCount>();
	static FileWriter fw , fc ;
	public static int readWordDictByLines(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            line = 1;
            while ((tempString = reader.readLine()) != null) {
            	String[] List = tempString.split(" ");
            	WordCount wc = new WordCount();
            	wc.wordID = Integer.parseInt(List[2]);
            	wc.Textnum = Integer.parseInt(List[1]);
            	WordPool.put(List[0],wc);
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
		//readWordDictByLines(WordList);
		readResultFileDictionary();
		readTopicFile(new File(Topic_in));
	}
	public static void readTopicFile(File file) throws IOException {
		HashMap<String,Integer> now;
		fw = new FileWriter(Topic_out);
        BufferedReader reader = null;
        Pattern pattern = Pattern.compile(" ");
        int paragraphId = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
            	
            	String[] List = pattern.split(tempString);
            	now = new HashMap<String,Integer>();
            	for (int j = 0;j<List.length;j++)
            	{
            		if (now.containsKey(List[j]))
           				now.put(List[j], now.get(List[j])+1);
           			else
           				now.put(List[j],1);
           		}
           	
           		fw.write(String.valueOf(paragraphId)+" : ");
        		Iterator<HashMap.Entry<String,Integer>> iter = now.entrySet().iterator();
          	      while (iter.hasNext()) {
          	       	HashMap.Entry<String,Integer> entry = (HashMap.Entry<String,Integer>) iter.next();
          	       	String key = entry.getKey();
          	       	int val = entry.getValue();
          	       	fw.write(key+" "+val+" ");
          	      }
         	    fw.write("\n");
            	
            	paragraphId++;
            	
            }
            fw.close();
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
	}
	public static void readResultFileDictionary() throws IOException {
		File directory = new File(ResultFileDictionary_Path);
        File[] filesFile = directory.listFiles(new MyFileFilter(".*.txt._Utf8"));
        if (filesFile == null) return;
        for (int j = 0; j < filesFile.length; j++) {
          readResultFile(filesFile[j]);
        }
	}
	public static void readResultFile(File file) throws IOException {
		Pattern patternDigit = Pattern.compile("[0-9]+");
		Pattern patternSentence = Pattern.compile("(。|？|！|[.!?])+");
		Matcher md = patternDigit.matcher(file.getName());
		md.find();
		HashMap<String,Integer> now,pre,pre_pre;
		fw = new FileWriter(_TFIDF_out+md.group());
		fc = new FileWriter(_Content_out+md.group());
        BufferedReader reader = null;
        Pattern pattern = Pattern.compile(" ");
        int paragraphId = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
            	String[] sent = patternSentence.split(tempString);
            	pre = null;
            	pre_pre = null;
            	now = null;
            	int SentId = 0;
            	if (sent.length<=3)
            	{
            		String[] List = pattern.split(tempString);
            		now = new HashMap<String,Integer>();
            		for (int j = 0;j<List.length;j++)
            		{
            			if (now.containsKey(List[j]))
            				now.put(List[j], now.get(List[j])+1);
            			else
            				now.put(List[j],1);
            		}
            		fc.write(String.valueOf(paragraphId)+"_"+String.valueOf(SentId)+" "+
        					tempString+"\n");
        			fw.write(String.valueOf(paragraphId)+"_"+String.valueOf(SentId)+" : ");
        			
        			HashMap<String,Integer> merge = mergeSentenceTF(now,pre,pre_pre);
        			Iterator<HashMap.Entry<String,Integer>> iter = merge.entrySet().iterator();
        	        while (iter.hasNext()) {
        	        	HashMap.Entry<String,Integer> entry = (HashMap.Entry<String,Integer>) iter.next();
        	        	String key = entry.getKey();
        	        	int val = entry.getValue();
        	        	fw.write(key+" "+val+" ");
        	        }
        	        fw.write("\n");
        			SentId++;
            	}
            	else
            	for (int i = 0;i<sent.length;i++)
            	{
            		String[] List = pattern.split(sent[i]);
            		now = new HashMap<String,Integer>();
            		for (int j = 0;j<List.length;j++)
            		{
            			if (now.containsKey(List[j]))
            				now.put(List[j], now.get(List[j])+1);
            			else
            				now.put(List[j],1);
            		}
            		if (pre_pre!=null&&now.size()>2)
            		{
            			fc.write(String.valueOf(paragraphId)+"_"+String.valueOf(SentId)+" "+
            					sent[i-2]+" "+sent[i-1]+" "+sent[i]+"\n");
            			fw.write(String.valueOf(paragraphId)+"_"+String.valueOf(SentId)+" : ");
            			
            			HashMap<String,Integer> merge = mergeSentenceTF(now,pre,pre_pre);
            			Iterator<HashMap.Entry<String,Integer>> iter = merge.entrySet().iterator();
            	        while (iter.hasNext()) {
            	        	HashMap.Entry<String,Integer> entry = (HashMap.Entry<String,Integer>) iter.next();
            	        	String key = entry.getKey();
            	        	int val = entry.getValue();
            	        	fw.write(key+" "+val+" ");
            	        }
            	        fw.write("\n");
            			SentId++;
            		}
            		pre_pre = pre;
            		pre = now;
            		now = null;
            	}
            	
            	paragraphId++;
            	
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
        fw.close();
        fc.close();
	}
	static HashMap<String,Integer> mergeSentenceTF(HashMap<String,Integer> now,HashMap<String,Integer> pre,HashMap<String,Integer> pre_pre)
	{
		HashMap<String,Integer> result = new HashMap<String,Integer> ();
		mergeWordVector(result,now);
		mergeWordVector(result,pre);
		mergeWordVector(result,pre_pre);
		return result;
	}
	static void mergeWordVector(HashMap<String,Integer> result,HashMap<String,Integer> now)
	{
		if (now==null) return;
		Iterator<HashMap.Entry<String,Integer>> iter = now.entrySet().iterator();
        while (iter.hasNext()) {
        	HashMap.Entry<String,Integer> entry = (HashMap.Entry<String,Integer>) iter.next();
        	String key = entry.getKey();
        	int val = entry.getValue();
        	if (result.containsKey(key))
        		result.put(key, result.get(key)+val);
        	else
        		result.put(key, val);
        }
	}
}
