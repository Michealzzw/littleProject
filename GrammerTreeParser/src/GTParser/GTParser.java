package GTParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.spi.DirStateFactory.Result;

public class GTParser {
	static public Vector<Node> List;
	static public Node keyNode;
	static String fileName = "Result_parser";
	static public void main(String[] args)
	{
		readFileByLines(fileName);
	}
	/*
	 * 0 - 数次
	 * 1 - 名词
	 * 2 － 定语
	 * 3 － 方位
	 */
    public static int readFileByLines(String fileName) {
        File file = new File(fileName);
        List = new Vector<Node>();
        BufferedReader reader = null;
        Pattern patternTag = Pattern.compile("[A-Z]+");
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            line = 1;
            int wordNum = 0;
            Node root = null;
            while ((tempString = reader.readLine()) != null) {
            	String[] Word = tempString.split(" ");
                for (int i = 0 ;i<Word.length;i++)
                if (!Word[i].equals(""))
                {
                	if (Word[i].contains("(")&&!Word[i].contains("()"))
                	{
                		if (root==null)
                		{
                			root = new Node();
                			
                		}
                		else
                		{
                			Node tmp = new Node();
                			if (root.son.size()>0)
                				root.son.lastElement().next = tmp;
                			root.son.addElement(tmp);
                			tmp.fa = root;
                			root = tmp;
                		}
                		Matcher mc = patternTag.matcher(Word[i]);
            			if (!mc.find())
            			{
            				System.out.println("ERROR");
            			}
            			root.tag = mc.group();
                	}
                	else
                	if (Word[i].contains(")"))
                	{
                		if (Word[i].charAt(0)!=')'&&Word[i].charAt(0)!='(')
                		{
                			root.key = Word[i].replaceAll("[()]", "");
                    		wordNum++;
                		}                		
                		int num = 0;
                		for (int j = 0;j<Word[i].length();j++)
                			if (Word[i].charAt(j)=='(')
                				num--;
                			else
                			if (Word[i].charAt(j)==')')
                				num++;
                		while (num!=0)
                		{
                			if (root.fa!=null)root = root.fa;
                    		else
                    		{
                    			if (wordNum>0)
                    			List.addElement(root);
                    			wordNum = 0;
                    			root = null;
                    		}
                			num--;
                		}
                		
                	}
                	else
                	{
                		System.out.println("ERROR");
                	}
                }
                if (List.size()==4)
                {                	
                	System.out.println(dfsGetContent(List.elementAt(0)));
                	System.out.println(dfsGetContent(Parser()));
                	List = new Vector<Node>();
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
	static public Node Parser()
	{
		double max = 0.0;
		Node maxNode = null;
		Node query = DealQuestion(List.elementAt(0));
		if (query==null)
		{
			Node tmp = new Node();
			if (List.elementAt(0).son.elementAt(0).son.lastElement()!=null)
				List.elementAt(0).son.elementAt(0).son.lastElement().next = tmp;
			List.elementAt(0).son.elementAt(0).son.addElement(tmp);
			tmp.fa = List.elementAt(0).son.elementAt(0);
			tmp.son.addElement(new Node());
			query = tmp.son.elementAt(0);
			query.fa = tmp;
			tmp.tag="NP";
		}
		else
		{
			if (query.key.matches(".*(哪).*"))
				query = query.fa;
		}
		for (int i = 1;i<List.size();i++)
		{
			Node tmp = List.elementAt(i);
			while ((tmp = getNext(tmp))!=null)
			{
				//System.out.println(tmp.tag);
				double value = getSimilar(query,tmp);
				if (value>max)
				{
					max = value;
					maxNode = tmp;
				}
			}
		}
		return maxNode;
			
	}
	static public double getSimilar(Node query,Node now)
	{
		int time = 1;
		double ans = 0.0;
		HashMap <String,Integer> WordPool = new HashMap <String,Integer>();
		query = query.fa;
		double wh = 0;
		while (query!=null||now!=null)
		{
			if (query!=null)
			{
				if (query.fa!=null)
				{
				for (int i = 0;i<query.fa.son.size();i++)
					if (query.fa.son.elementAt(i)!=query)
						addBro(WordPool,time,query.fa.son.elementAt(i));
				}
				//query = query.fa;
			}
			if (now!=null)
			{
				if (now.fa!=null)
				{
				for (int i = 0;i<now.fa.son.size();i++)
					if (now.fa.son.elementAt(i)!=now)
						ans+=culcuteBro(WordPool,time,now.fa.son.elementAt(i));
				}
				//now = now.fa;
			}
			String tagQuery = "";
			String tagNow = "";
			while (query!=null&&query.fa!=null&&query.fa.son.size()==1)
			{
				tagQuery+=query.tag;
				query = query.fa;
			}
			if (query!=null)
				{
				tagQuery+=query.tag;
				query = query.fa;
				}
			while (now!=null&&now.fa!=null&&now.fa.son.size()==1)
			{
				tagNow+=now.tag;
				now = now.fa;
			}
			if (now!=null)
				{
				tagNow+=now.tag;
				now = now.fa;
				}
			//if(query!=null&&now!=null)
			{
				if (tagQuery.contains("N")&&tagNow.contains("N"))
					wh+=0.5;
				else
				{
				if (tagQuery.contains("DP")&&tagNow.contains("DP"))
					wh+=1;
				if (tagQuery.contains("LCP")&&tagNow.contains("LCP"))
					wh+=2;
				if (tagQuery.contains("NT")&&tagNow.contains("NT"))
					wh+=2;
				if (tagQuery.contains("QP")&&tagNow.contains("QP"))
					wh+=2;
				}
			}
		}
		return ans*wh;
	}
	static public void addBro(HashMap <String,Integer>WordPool,int time,Node query)
	{
		if (query.key!=null)
		{
			WordPool.put(query.key, time);
		}
		else
		{
			for (int i = 0;i<query.son.size();i++)
				addBro(WordPool,time,query.son.elementAt(i));
		}
		//return 0.0;
	}
	static public double culcuteBro(HashMap <String,Integer>WordPool,int time,Node query)
	{
		double res = 0.0;
		if (query.key!=null)
		{
			if (WordPool.containsKey(query.key))
				res = 1/(time-WordPool.get(query.key)+1);
		}
		else
		{
			for (int i = 0;i<query.son.size();i++)
				res+=culcuteBro(WordPool,time,query.son.elementAt(i));
		}
		return res;
	}
	static public Node getNext(Node root)
	{		
		if (root.son.size()==0)
		{
			while (root.fa!=null&&root.next==null)
			{
				root = root.fa;
			}
			if (root.next==null) return null;
			root = root.next;
		}
		else
			root = root.son.elementAt(0);
		return root;
	}
	static public Node DealQuestion(Node root)
	{
		/*(几|多少)[量词]*[名词] —— [数词]
				[定语]*[名词]是——[名词]|[短语]
				[哪][一|几]?[量词][名词]——[名词]|[短语]
				哪里[动词|短语]
				什么(样的)?[名词]——[定语]
				什么——直接替换句子中的内容

				特殊：下一句
				符号：“” 《》	

				属于－>是
				none－>是
				*/
		if (root.key!=null)
		{
			if (root.key.matches(".*(几|多少|哪|什么|谁).*"))
				return root;
			else return null;
		}
		else
		{
			for (int i = 0;i<root.son.size();i++)
			{
				Node tmp =DealQuestion(root.son.elementAt(i));
				if (tmp!=null)
				return tmp;
			}
			return null;
		}
	}
	static String dfsGetContent(Node root)
	{
		if (root.key!=null)
			return root.key;
		else
		{
			String Result = "";
			for (int i = 0;i<root.son.size();i++)
				Result+=dfsGetContent(root.son.elementAt(i));
			return Result;
		}
	}
}
