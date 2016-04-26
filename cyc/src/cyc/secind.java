package cyc;

import java.util.Scanner;

class treenode{
	treenode[] son;
	boolean flag;
	boolean allow;
	int time;
	public treenode()
	{
		son = new treenode[2];
		flag = false;
		
	}
}

public class secind {
	static public void main(String[] args)
	{
		  Scanner sc = new Scanner(System.in);
		  int n,m;
		  n = sc.nextInt();
		  m = sc.nextInt();
		  treenode root = new treenode();
		  sc.nextLine();
		  for (int i = 0;i<n;i++)
		  {
			  String type = sc.nextLine();
			  String[] list = type.split("[./ ]");
			  boolean flag =false;
			  if (list[0].equals("allow")) flag = true;
			  int[] digit = new int[32];
			  for (int j = 1;j<5;j++)
			  {
				  int tmp = Integer.parseInt(list[j]);
				  for (int q=0;q<8;q++)
				  {
					  digit[(j-1)*8+7-q] = tmp%2;
					  tmp = tmp/2;
				  }
			  }
			  int mask = 32;
			  if (list.length>5)
			  {
				  mask = Integer.parseInt(list[5]);
			  }
			  treenode now = root;
			  for (int j = 0;j<mask;j++)
			  {
				  int tmp = digit[j];
				  if (now.son[tmp]==null)
				  {
					  now.son[tmp] = new treenode();
				  }
				  now = now.son[tmp];
			  }
			  if (now.flag) continue;
			  now.flag = true;
			  now.allow = flag;
			  now.time = i;
		  }
		  for (int i = 0;i<m;i++)
		  {
			  String type = sc.nextLine();
			  String[] list = type.split("[.]");
			  int[] digit = new int[32];
			  for (int j = 0;j<4;j++)
			  {
				  int tmp = Integer.parseInt(list[j]);
				  for (int q=0;q<8;q++)
				  {
					  digit[j*8+7-q] = tmp%2;
					  tmp = tmp/2;
				  }
			  }
			  treenode now = root;
			  int time = n;
			  boolean flag = true;
			  for (int j = 0;j<32;j++)
			  {
				  if (now.flag)
				  {
					  if (now.time<time)
						  {
						  	time = now.time;
						  	flag = now.allow;
						  }
				  }
				  int tmp = digit[j];
				  if (now.son[tmp]==null)
				  {
					  break;
				  }
				  now = now.son[tmp];
			  }
			  if (flag)
			  {
				  System.out.println("YES");
			  }
			  else System.out.println("NO");
		  }
	}
}
