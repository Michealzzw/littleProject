package Modality;

	import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileReader;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.util.HashMap;
	import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
	import java.util.regex.Matcher;
	import java.util.regex.Pattern;

	import WordDictionary.WordCount;  

	public class Matrix {
		static int TopicNum = 135;
		static String _Text_Path = "bin/Sentences2TFIDF/Question_";
		static String Word_Path = "bin/WordDictFile";;
		static String Text_Path ;
		static String Topic_Path = "bin/Sentences2TFIDF/Question";
		static String Topic_Content_Path = "bin/tagged_question_Utf8.txt";
		static String _Text_Content_Path = "bin/Sentences2TFIDF/Content_";
		static String Text_Content_Path;
//		static String Doc_result = "bin/Doc_classifier_Data/Doc_classifier_result";
		static String ResultPath = "bin/Sentences2TFIDF/Result";
		static HashMap<String,WordCount> WordList;
		static Vector<Docu> Document;
		static Vector<String> TopicContent =new Vector<String>();
		static Vector<String> TextContent;
		static Vector<Docu> Topics;
		static int DocuNum;
		static Double alpharatio = 0.4;//越小初始越重
		static Double alpharatio1 = 0.2;//越小初始越重
		static Double alpharatio2 = 0.2;//越小初始越重
		static Boolean NonuFocus = true;
		static Boolean InitUsedCos = true;
		static Boolean SingleModality = true;
		public static void main(String[] argu) throws IOException
		{
			FileWriter answ = new FileWriter(ResultPath);
			WordList = new HashMap<String,WordCount>();
			Topics = new Vector<Docu>();			
			readWordListByLines(Word_Path);
			readTextSegByLines(Topic_Path);
			readContentByLines(Topic_Content_Path,TopicContent);
			//readTopic
			//readWordList
			Double max;
			for (int topic = 1;topic<=TopicNum;topic++)
			{
				Document = new Vector<Docu>();
				Text_Content_Path = _Text_Content_Path+String.valueOf(topic);
				TextContent = new Vector<String>();
				TextContent.addElement(TopicContent.elementAt(topic-1));
				readContentByLines(Text_Content_Path,TextContent);
				Document.addElement(Topics.elementAt(topic-1));
				Text_Path = _Text_Path+String.valueOf(topic);
				readTextSegByLines(Text_Path);				
				Double [][]  Matrix = new Double [Document.size()][Document.size()];
				Double [] InitValue = new Double [Document.size()];
				InitValue[0] = 1.0;
				for (int i = 1;i<Document.size();i++)
					InitValue[i] = 0.0;
				
				
				//FileWriter fwTT = null,fwPP = null,fwNO = null;
				//try {  
					//fwNO = new FileWriter(MatrixID);
					//fwTT = new FileWriter(MatrixTT);
					//fwPP = new FileWriter(MatrixPP);
				//readMatrixByLines(Matrix_out+"_"+String.valueOf(topic),Matrix);
				/*FirstUse->>>>*/
					for (int i = 0;i<Document.size();i++)
						for (int j = i;j<Document.size();j++)
						{
								Docu A = Document.elementAt(i);
								Docu B = Document.elementAt(j);
								double cosD = 0;
								if (i==j)cosD = 0;
								else
									cosD = cosineDistence(A,B);
								Matrix[i][j] = Matrix[j][i] = cosD;						
							}
					for (int i = 1;i<Document.size();i++)
					{
						if (InitUsedCos)
						InitValue[i] = cosineDistence(Document.elementAt(0), Document.elementAt(i));
						else
							InitValue[i] = 0.0;
						//	InitValue[i]/=max;					
					}
					Double [][]  MatrixA = new Double [Document.size()][Document.size()];
					Double[] sum = new Double[Document.size()];
					if (SingleModality)
					{
						
					
					for (int i = 0;i<Document.size();i++)
					{
						sum[i] = 0.0;
						for (int j = 0;j<Document.size();j++)
							{
								sum[i]+= Matrix[i][j];					
							}
					}
					for (int i = 0;i<Document.size();i++)
					{						
						for (int j = 0;j<Document.size();j++)
							if (Matrix[i][j]>1e-8)
							{
								Matrix[i][j]/=Math.sqrt(sum[i]*sum[j]);					
							}
							else
								Matrix[i][j] = 0.0;
					}
					}
					else
					{
						for (int i = 0 ;i<Document.size();i++)
							for (int j = 0;j<Document.size();j++)
								if (i!=0&&j!=0&&Document.elementAt(i).Text_ID==Document.elementAt(j).Text_ID)
									MatrixA[i][j] = 0.0;
								else MatrixA[i][j] = Matrix[i][j];
							for (int i = 0;i<Document.size();i++)
							{
								sum[i] = 0.0;
								for (int j = 0;j<Document.size();j++)
									{
										sum[i]+= MatrixA[i][j];					
									}
							}
							for (int i = 0;i<Document.size();i++)
							{						
								for (int j = 0;j<Document.size();j++)
									if (MatrixA[i][j]>1e-8)
									{
										MatrixA[i][j]/=Math.sqrt(sum[i]*sum[j]);					
									}
									else
										MatrixA[i][j] = 0.0;
							}	
					}
				/*FileWriter hj = new FileWriter(Matrix_out+"_"+String.valueOf(topic));
				for (int i = 0;i<Document.size();i++)
					hj.write(String.valueOf(InitValue[i])+" ");
				hj.write("\n");
				for (int i = 0;i<Document.size();i++)
				{
					for (int j = 0;j<Document.size();j++)
						hj.write(String.valueOf(Matrix[i][j])+" ");
					hj.write("\n");
				}
				hj.close();
				*/
					/*Double max = 0.0;
				for (int i = 1;i<Document.size();i++)
				{
					InitValue[i] = cosineDistence(Document.elementAt(0), Document.elementAt(i));
					if (PrRR[topic].containsKey(String.valueOf(Document.elementAt(i).Text_ID)))

						InitValue[i] = PrRR[topic].get(String.valueOf(Document.elementAt(i).Text_ID));
					else
						InitValue[i] = 0.0;
					if (InitValue[i]>max) max = InitValue[i];
				}
				*/
				
					
				Double change = 10.0;
				
				Double [] ans = new Double [Document.size()];
				Double [] ans_old = new Double [Document.size()];
				
				
				for (int i = 0;i<Document.size();i++)
				{
					ans[i] = 0.0;
					ans_old[i] = InitValue[i];
				}
				if (!SingleModality)
				{
				while (change>1e-16)
				{
					for (int i = 0;i<Document.size();i++)
					{
						ans[i] = 0.0;
						for (int j = 0;j<Document.size();j++)
						{
							ans[i]+=ans_old[j]*MatrixA[i][j];
						}
						ans[i] = ans[i]*alpharatio1+(1-alpharatio1)*InitValue[i];
					}
					change = 0.0;
					for (int i = 0 ;i<Document.size();i++)
					{
						change += Math.pow(ans[i]-ans_old[i], 2);
						ans_old[i] = ans[i];
					}
					System.out.println(change);
				}
				
				for (int i = 0 ;i<Document.size();i++)
					for (int j = 0;j<Document.size();j++)
						if (i!=0&&j!=0&&Document.elementAt(i).Text_ID!=Document.elementAt(j).Text_ID)
							MatrixA[i][j] = 0.0;
						else MatrixA[i][j] = Matrix[i][j];
				sum = new Double[Document.size()];
					for (int i = 0;i<Document.size();i++)
					{
						sum[i] = 0.0;
						for (int j = 0;j<Document.size();j++)
							{
								sum[i]+= MatrixA[i][j];					
							}
					}
					for (int i = 0;i<Document.size();i++)
					{						
						for (int j = 0;j<Document.size();j++)
							if (MatrixA[i][j]>1e-8)
							{
								MatrixA[i][j]/=Math.sqrt(sum[i]*sum[j]);					
							}
							else
								MatrixA[i][j] = 0.0;
					}	
					change = 10.0;
					
					
					for (int i = 0;i<Document.size();i++)
					{
						InitValue[i] = ans[i];
						ans[i] = 0.0;
						ans_old[i] = InitValue[i];
					}
					while (change>1e-16)
					{
						for (int i = 0;i<Document.size();i++)
						{
							ans[i] = 0.0;
							for (int j = 0;j<Document.size();j++)
							{
								ans[i]+=ans_old[j]*MatrixA[i][j];
							}
							ans[i] = ans[i]*alpharatio2+(1-alpharatio2)*InitValue[i];
						}
						change = 0.0;
						for (int i = 0 ;i<Document.size();i++)
						{
							change += Math.pow(ans[i]-ans_old[i], 2);
							ans_old[i] = ans[i];
						}
						System.out.println(change);
					}
				}
				else
				{
				while (change>1e-16)
				{
					for (int i = 0;i<Document.size();i++)
					{
						ans[i] = 0.0;
						for (int j = 0;j<Document.size();j++)
						{
							ans[i]+=ans_old[j]*Matrix[i][j];
						}
						ans[i] = ans[i]*alpharatio+(1-alpharatio)*InitValue[i];
					}
					change = 0.0;
					for (int i = 0 ;i<Document.size();i++)
					{
						change += Math.pow(ans[i]-ans_old[i], 2);
						ans_old[i] = ans[i];
					}
					System.out.println(change);
				}
				}
				
				
				
				
				int maxId = 1;
				max = 0.0;
				ans[0] = 0.0;
				answ.write("\n\n"+TextContent.elementAt(0).replaceAll("#[A-Z]+ ", "")+"\n");
				
				for (int i = 0;i<3;i++)
				{
					maxId = 1;
					max = 0.0;
					ans[0] = 0.0;
					for (int j = 0;j<Document.size();j++)
					{
						if (ans[j]>max)
						{
							max = ans[j];
							maxId = j;
						}						
					}
					/*
					 * answ.write(String.valueOf(topic)+" Q0 "+String.valueOf(Document.elementAt(maxId).Text_ID)
						+"_"+String.valueOf(Document.elementAt(maxId).Para_ID)
						+" "+String.valueOf(i)+" "+String.valueOf(max)+" SingleModality\n");
							*/
					String temp = TextContent.elementAt(maxId).replaceAll(" #PU ", " ，#PU ");
					 temp = temp.replaceAll("#[A-Z]+ ", "");
					answ.write(temp.substring(temp.indexOf(' ')+1)+"\n");
					ans[maxId] = 0.0;
					
				}
					/*fwNO.write("0 Topic\n");
					fwPP.write("0 0 1\n");
					fwTT.write("0 0 1\n");
					for (int i = 0;i<Document.size();i++)
					{
						fwPP.write(String.valueOf(0)+" "+String.valueOf(i+1)+" "+
								String.valueOf(cosineDistence(Topics.elementAt(topic),
										Document.elementAt(i)))+"\n");
						fwTT.write(String.valueOf(0)+" "+String.valueOf(i+1)+" "+
								String.valueOf(cosineDistence(Topics.elementAt(topic),
										Document.elementAt(i)))+"\n");
						fwNO.write(String.valueOf(i+1)+" "+Document.elementAt(i).Text_ID+"_"+Document.elementAt(i).Para_ID+"\n");
					}
					
				} catch (IOException e) {  
		            // TODO Auto-generated catch block  
		            e.printStackTrace();  
		        }  
				try {
					fwTT.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // �ر�������
				try {
					fwPP.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // �ر�������
				try {
					fwNO.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // �ر�������
				//Topic*/
			}
			answ.close();
		}
		public static double cosineDistence(Docu _A , Docu _B)
		{
			Docu A,B;
			if (_A.WordList.size()<_A.WordList.size())
			{
				A = _A;
				B = _B;
			}
			else
			{
				A = _B;
				B = _A;
			}
			double ans = 0.0;
	    	Iterator<HashMap.Entry<String,Double>> iter = A.WordList.entrySet().iterator();
	    	while (iter.hasNext()) {
	        	HashMap.Entry<String,Double> entry = (HashMap.Entry<String,Double>) iter.next();
	        	String key = entry.getKey();
	        	Double num = entry.getValue();
	        	Double num2 = B.WordList.get(key);
	        	if (num2!=null)
	        	{
	        		ans += num*num2;
	        	}
	        	//TF*IDF
	        } 		
	    	if (ans<1e-8) return 0.0;
	    	return ans/(A.len*B.len);
		}
		
	    public static void readTextSegByLines(String fileName) {
	        File file = new File(fileName);
	        BufferedReader reader = null;
	        Pattern pattern = Pattern.compile(" ");
	        try {
	            reader = new BufferedReader(new FileReader(file));
	            String tempString = null;
	            while ((tempString = reader.readLine()) != null) {
	            	Docu dc = new Docu();
	            	String[] List = pattern.split(tempString);
	            	dc.getID(List[0]);
	            	for (int i = 2;i<List.length;i+=2)
	            	if (WordList.containsKey(List[i]))
	            	{
	            		dc.WordList.put(List[i], (double)Integer.parseInt(List[i+1]));
	            		dc.wordnum+= Integer.parseInt(List[i+1]);
	            	}
	            	Iterator<HashMap.Entry<String,Double>> iter = dc.WordList.entrySet().iterator();
	            	while (iter.hasNext()) {
	                	HashMap.Entry<String,Double> entry = (HashMap.Entry<String,Double>) iter.next();
	                	String key = entry.getKey();
	                	Double num = entry.getValue();
	                	
	                		num = (num/dc.wordnum) * WordList.get(key).IDF;
	                	//TF*IDF
	                		dc.WordList.put(key, num);
	                	
	                	dc.len += num*num;
	                }  
	            	dc.len = Math.sqrt(dc.len);
	            	if (dc.Para_ID!=-1)
	            		Document.addElement(dc);
	            	else
	            		Topics.addElement(dc);
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
		
	    
	    public static void readWordListByLines(String fileName) {
	        File file = new File(fileName);
	        BufferedReader reader = null;
	        DocuNum = 0;
	        Pattern pattern = Pattern.compile(" ");
	        Pattern patternStopWord = Pattern.compile("几|#PU|#BA|#CC|#CS|#DE|#ETC|#DT|#IJ|#LB|#PN|#SB|#SP");
	        try {
	            reader = new BufferedReader(new FileReader(file));
	            String tempString = null;
	            while ((tempString = reader.readLine()) != null) {
	            	String[] List = pattern.split(tempString);	            
	          		WordCount wc = new WordCount();
	           		wc.Textnum = Integer.parseInt(List[2]);
	           		if (wc.Textnum>DocuNum) DocuNum = wc.Textnum;
	           		wc.num = Integer.parseInt(List[1]);	           		
	           		WordList.put(List[0], wc);

	            }
	            reader.close();
	            String[] Key= new String[WordList.size()];
	            Pattern Noun = Pattern.compile("#NN|#NR");
	            int num = 0;
	            Iterator<HashMap.Entry<String,WordCount>> iter = WordList.entrySet().iterator();
	            while (iter.hasNext()) {
	            	HashMap.Entry<String,WordCount> entry = (HashMap.Entry<String,WordCount>) iter.next();	            	
	            	WordCount wc = entry.getValue();
	            	if (NonuFocus&&Noun.matcher(entry.getKey()).find())
	            		wc.IDF = Math.log((double)DocuNum/wc.Textnum)*2;
	            	else
	            	wc.IDF = Math.log((double)DocuNum/wc.Textnum);
	            	Key[num++] = entry.getKey();
	            }	    
	            for (int i = 0;i<num;i++)
	            {
	            	Matcher mc = patternStopWord.matcher(Key[i]);
	            	if (mc.find())
	            		WordList.remove(Key[i]);
	            }
	            
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
	    
	    public static void readContentByLines(String fileName,Vector<String> Content) {
	        File file = new File(fileName);
	        BufferedReader reader = null;
	        try {
	            reader = new BufferedReader(new FileReader(file));
	            String tempString = null;
	            while ((tempString = reader.readLine()) != null) {
	            	Content.addElement(tempString);
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
		
	    public static void readMatrixByLines(String fileName,Double[][]Matrix) {
	        File file = new File(fileName);
	        BufferedReader reader = null;
	        try {
	            reader = new BufferedReader(new FileReader(file));
	            String tempString = null;
	            tempString = reader.readLine();
	            int line = 0;
	            while ((tempString = reader.readLine()) != null) {
	            	String[] List = tempString.split(" ");
	            	for (int i = 0;i<List.length;i++)
	            		Matrix[line][i] = Double.parseDouble(List[i]);
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
	
}
