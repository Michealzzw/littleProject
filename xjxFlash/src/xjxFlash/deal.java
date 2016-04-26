package xjxFlash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class deal {
	static String path = "xjxQZ.txt";
	static int[] Month = {31,28,31,30,31,30,31,31,30,31,30,31};
	public static int readFileByLines(String fileName) {
		Pattern tp = Pattern.compile("[0-9]{4}-[0-9]{3}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z");
		Pattern t2 = Pattern.compile("[0-9]+</a>");
        File file = new File(fileName);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            FileWriter fw = new FileWriter("QZ.csv");
            String tempString = null;
            //tempString = reader.readLine();
            line = 0;
            int [][] count = new int[15][12];
            boolean[][] flag = new boolean[15][367];
            for (int i = 0;i<15;i++) for (int j = 0;j<12;j++) count[i][j] = 0;
            for (int i = 0;i<15;i++) for (int j = 0;j<367;j++) flag[i][j] = false;
            int oddd = 0;
            while ((tempString = reader.readLine()) != null) {
            	Matcher mc = tp.matcher(tempString);
            	if (mc.find())
            		{            			
            			String tmp = mc.group();
            			System.out.println(tmp);
            			//fw.write(tmp+"\n");
            			String[] list = tmp.split("-|T|:");
            			int i = Integer.parseInt(list[0]);
            			int j = Integer.parseInt(list[1]);
            			flag[i-1998][j] = true;
            			if (i%4==0) Month[1]++;
            			int mo =0 ;
            			for (mo = 0;mo<12;mo++) if (j<=Month[mo]) break; else j-=Month[mo];
            			if (i%4==0) Month[1]--;
            			oddd++;
            			if (oddd%2==0)
            			{
            				tempString = reader.readLine();
            				Matcher mc2 = t2.matcher(tempString);
            				mc2.find();
            				tempString = mc2.group();
            				count[i-1998][mo]+=Integer.parseInt(tempString.split("<")[0]);
            			}
            		}
            }
            fw.write("Time Count\n");
            for (int i = 0;i<15;i++) for (int j = 0;j<12;j++)
            {
            	fw.write((i+1998)+","+(j+1)+","+count[i][j]+"\n");
            	System.out.println((i+1998)+" "+(j+1)+" "+count[i][j]);
            }
            fw.write("Day Count\n");
            for (int i = 0;i<15;i++)
            {
            	int year = i+1998;
            	if (i%4==0) Month[1] ++;
            	int num = 0;
            	for (int j = 0;j<12;j++){
            		int sum = 0;
            		for (int k = num;k<num+Month[j];k++)
            		{
            			if (flag[i][k]) sum++;
            		}
            		num+=Month[j];
            		fw.write((i+1998)+","+(j+1)+","+sum+"\n");
            		System.out.println((i+1998)+" "+(j+1)+" "+sum);
            	}
            	if (i%4==0) Month[1] --;
            }
            reader.close();
            fw.close();
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
	public static void main(String[] args) throws IOException
	{
		readFileByLines(path);
	}
}
