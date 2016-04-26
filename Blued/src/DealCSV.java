import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

public class DealCSV {
	static String file_path="blued.csv";
	static String spilt_path="split.txt";
	static Vector<String> tag_Name = new Vector<String>();
	static Vector<Vector<String>> Data = new Vector<Vector<String>>();
	static HashMap<String , Integer> WordPool = new HashMap<String , Integer>();
	static Vector<Vector<HashMap<String , Integer>>> Data_Table = new Vector<Vector<HashMap<String , Integer>>>();
	static Vector<Vector> spilt_Key = new Vector<Vector>();
	static Vector<Integer> tag_type = new Vector<Integer>(); 
	static Vector<String> WordOrder = new Vector<String >(); 
	static Pattern sp = Pattern.compile(",");
	static int KeyNo = 0;
	static int NumOfTag;
	static public void main(String argu[]) throws IOException
	{
		readSpiltByLines(spilt_path);
		readFileByLines(file_path);
		
    	FileWriter fw = new FileWriter("全部.txt");
    	
    	List<HashMap.Entry<String, Integer>> infoIds =
    		    new ArrayList<HashMap.Entry<String, Integer>>(WordPool.entrySet());

    		//排序前    		

    		//排序
    		Collections.sort(infoIds, new Comparator<HashMap.Entry<String, Integer>>() {   
    		    public int compare(HashMap.Entry<String, Integer> o1, HashMap.Entry<String, Integer> o2) {      
    		        //return (o2.getValue() - o1.getValue()); 
    		        return (o1.getKey()).toString().compareTo(o2.getKey());
    		    }
    		});
    		for (int i = 0; i < infoIds.size(); i++) {
    		    String id = infoIds.get(i).toString();
    		    WordOrder.addElement( infoIds.get(i).getKey());
    		    fw.write((id)+"  ");
    		}
    	fw.write("\n");
    	fw.close();
    	fw = new FileWriter("全部.csv");
    	
    		for (int i = 0; i < infoIds.size(); i++) {
    			fw.write(infoIds.get(i).getKey()+","+infoIds.get(i).getValue()+",");
    		}
    	fw.write("\n");
    	fw.close();
    	for (int i  =0;i<NumOfTag;i++)
			PrintALL(i); 
	}
	public static void readSpiltByLines(String fileName)
	{
	     File file = new File(fileName);
	        BufferedReader reader = null;
	        int line = 0;
	        try {
	            reader = new BufferedReader(new FileReader(file));
	            String tempString = null;
	            line = 0;
	            while ((tempString = reader.readLine()) != null) {
	                String[] List = tempString.split(",");
	                Data_Table.addElement(new Vector<HashMap<String , Integer>>());
	                if (List[0].equals("K"))
	                {
	                	KeyNo = line;
	                	tag_type.addElement(0);
	                	spilt_Key.addElement(new Vector<Object>());
	                }
	                else
	                if (List[0].equals("C"))
	                {
	                	tag_type.addElement(1);
	                	spilt_Key.addElement(new Vector<Double>());	                	
	                	for (int i = 2;i<List.length;i++)
	                	{
	                		spilt_Key.elementAt(line).addElement(Double.parseDouble(List[i]));
	                		Data_Table.elementAt(line).add(new HashMap<String , Integer>());
	                	}
	                	Data_Table.elementAt(line).add(new HashMap<String , Integer>());
	                }
	                else
	                if (List[0].equals("D"))
	                {
	                	tag_type.addElement(2);
	                	spilt_Key.addElement(new Vector<Integer>());
	                	for (int i = 2;i<List.length;i++)
	                	{
	                		spilt_Key.elementAt(line).addElement(Integer.parseInt(List[i]));
	                		Data_Table.elementAt(line).add(new HashMap<String , Integer>());
	                	}
	                }
	                else
	                {
	                	tag_type.addElement(-1);
	                	spilt_Key.addElement(new Vector<Object>());
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
	}
    public static int readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            tempString = reader.readLine();            
            String[] tN = tempString.split(",");
            NumOfTag = tN.length;
            for (int i = 0;i<tN.length;i++) tag_Name.addElement(tN[i]);
            line = 0;
            while ((tempString = reader.readLine()) != null) {
                String[] List = tempString.split(",");
                List[5] = String.valueOf(Double.parseDouble(List[5])*10000);
                Data.addElement(new Vector<String>());
                String[] key = List[KeyNo].split("、");
                if (!List[KeyNo].equals("999"))
                {
	                for (int i = 0;i<List.length;i++)
	                {
	                	Data.elementAt(line).addElement(List[i]);
	                }
                	for (int tagNo = 0;tagNo<List.length;tagNo++)
                	if (tag_type.elementAt(tagNo)>0)
                	{
                		HashMap<String , Integer> temp = null;
                		if (tag_type.elementAt(tagNo)==1)
                		{
                			int q;
                			for ( q = 0;q<spilt_Key.elementAt(tagNo).size();q++)
                			{
                				if (Double.parseDouble(List[tagNo])<=(Double)(spilt_Key.elementAt(tagNo).elementAt(q)))
                				{
	                				temp = Data_Table.elementAt(tagNo).elementAt(q);
	                				break;
                				}
                			}
                			if (temp == null)
            					temp = Data_Table.elementAt(tagNo).elementAt(q);
                		}
                		else
                		{
                			int q;
                			for ( q = 0;q<spilt_Key.elementAt(tagNo).size();q++)
                			{
                				if (Integer.parseInt(List[tagNo])==(Integer)(spilt_Key.elementAt(tagNo).elementAt(q)))
                				{
	                				temp = Data_Table.elementAt(tagNo).elementAt(q);
	                				break;
                				}
                			}
                			if (temp == null)
            					System.out.println("ERROR!");
                		}
                		for (int j = 0;j<key.length;j++)
                		{
                			putHM(temp,key[j]);
                		}
                	}                	
                for (int j = 0;j<key.length;j++)
            		putHM(WordPool,key[j]);
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
    static void putHM(HashMap<String,Integer>HM,String key)
    {
    	if (HM.containsKey(key))
    	{
    		HM.put(key,HM.get(key)+1);
    	}
    	else HM.put(key, 1);
    }
    static void PrintALL(int tagNo) throws IOException
    {
    	if (tag_type.elementAt(tagNo)<1) return; 
    	/*FileWriter fw = new FileWriter(tag_Name.elementAt(tagNo)+".txt");
    	fw.write("类型：");
    	if (tag_type.elementAt(tagNo)==2) fw.write("离散分布\n");
    	else fw.write("连续分布\n");
    	fw.write("分布区间：");
    	String pre = "负无穷";
    	for (int i = 0;i<spilt_Key.elementAt(tagNo).size();i++)
    	{
    		if (tag_type.elementAt(tagNo)==2) fw.write("["+String.valueOf((int)spilt_Key.elementAt(tagNo).elementAt(i))+"]");
        	else 
        		{
        		fw.write("("+pre+","+String.valueOf((double)spilt_Key.elementAt(tagNo).elementAt(i))+"]");
        		pre = String.valueOf((double)spilt_Key.elementAt(tagNo).elementAt(i));
        		}
    	}
    	if (tag_type.elementAt(tagNo)==1)
    	{
    		fw.write("("+String.valueOf((Double)spilt_Key.elementAt(tagNo).elementAt(spilt_Key.elementAt(tagNo).size()-1))+","+"正无穷]");    		
    	}
    	fw.write("\n");
    	for (int No = 0;No<Data_Table.elementAt(tagNo).size();No++)
    	{
    	List<HashMap.Entry<String, Integer>> infoIds =
    		    new ArrayList<HashMap.Entry<String, Integer>>(Data_Table.elementAt(tagNo).elementAt(No).entrySet());

    		//排序前    		

    		//排序
    		Collections.sort(infoIds, new Comparator<HashMap.Entry<String, Integer>>() {   
    		    public int compare(HashMap.Entry<String, Integer> o1, HashMap.Entry<String, Integer> o2) {      
    		        //return (o2.getValue() - o1.getValue()); 
    		        return (o1.getKey()).toString().compareTo(o2.getKey());
    		    }
    		});
    		for (int i = 0; i < infoIds.size(); i++) {
    		    String id = infoIds.get(i).toString();
    		    fw.write((id)+"  ");
    		}
    	fw.write("\n");
    	}
    	fw.close();*/
    	FileWriter fw = new FileWriter(tag_Name.elementAt(tagNo)+"_array.csv");
    	fw.write("类型,");
    	if (tag_type.elementAt(tagNo)==2) fw.write("离散分布\n");
    	else fw.write("连续分布\n");
    	//fw.write("分布区间：");
    	String pre = "负无穷";
    	for (int No = 0;No<Data_Table.elementAt(tagNo).size();No++)
    	{    		
    		if (tag_type.elementAt(tagNo)==2) fw.write("["+String.valueOf((int)spilt_Key.elementAt(tagNo).elementAt(No))+"]");
        	else 
        	if (No<Data_Table.elementAt(tagNo).size()-1)
        		{
        		fw.write("("+pre+","+String.valueOf((double)spilt_Key.elementAt(tagNo).elementAt(No))+"]");
        		pre = String.valueOf((double)spilt_Key.elementAt(tagNo).elementAt(No));
        		}
    		if (tag_type.elementAt(tagNo)==1&&No==Data_Table.elementAt(tagNo).size()-1)
        	{
        		fw.write("("+String.valueOf((Double)spilt_Key.elementAt(tagNo).elementAt(spilt_Key.elementAt(tagNo).size()-1))+","+"正无穷]");    		
        	}
    		fw.write(",");
    		/*List<HashMap.Entry<String, Integer>> infoIds =
    		    new ArrayList<HashMap.Entry<String, Integer>>(Data_Table.elementAt(tagNo).elementAt(No).entrySet());

    		//排序前    		

    		//排序
    		Collections.sort(infoIds, new Comparator<HashMap.Entry<String, Integer>>() {   
    		    public int compare(HashMap.Entry<String, Integer> o1, HashMap.Entry<String, Integer> o2) {      
    		        //return (o2.getValue() - o1.getValue()); 
    		        return (o1.getKey()).toString().compareTo(o2.getKey());
    		    }
    		});
    		for (int i = 0; i < infoIds.size(); i++) {
    		    fw.write(infoIds.get(i).getKey()+","+infoIds.get(i).getValue()+",");
    		}
    		*/
    		for (int i = 0; i < WordOrder.size(); i++) {
    			if (Data_Table.elementAt(tagNo).elementAt(No).containsKey(WordOrder.elementAt(i)))
    		    fw.write(WordOrder.elementAt(i)+","+Data_Table.elementAt(tagNo).elementAt(No).get(WordOrder.elementAt(i))+",");
    			else
    				fw.write(WordOrder.elementAt(i)+",0,");
    		}
        	fw.write("\n");
    	}
    	fw.close();
    }
}
