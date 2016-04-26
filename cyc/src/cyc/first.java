package cyc;

import java.util.Scanner;

public class first {
	static public void main(String[] args)
	{
		  Scanner sc = new Scanner(System.in);
	      int t = sc.nextInt();
	      for (int task = 0;task<t;task++)
	      {
	    	  int n,w,h,p;
	    	  n = sc.nextInt();
	    	  p = sc.nextInt();
	    	  w = sc.nextInt();
	    	  h = sc.nextInt();
	    	  int[] para = new int[p];
	    	  for (int i = 0;i<n;i++)
	    		  para[i] = sc.nextInt();
	    	  int l = 1;
	    	  int r = w;
	    	  if (r>h) r = h;
	    	  while (l!=r)
	    	  {
	    		  int mid = (l+r)/2+1;
	    		  int wmax = w/mid;
	    		  int hmax = h/mid;
	    		  int sum = 1;
	    		  int count = 0;
	    		  for (int i = 0;i<n;i++)
	    		  {
	    			  int le;
	    			  if (para[i]%wmax==0)
	    			  le = para[i]/wmax;
	    			  else le = para[i]/wmax +1;
	    			  count +=le;
	    			  if (count>hmax)
	    			  {
	    				  sum+=count/hmax;
	    				  if (sum>p) break;
	    				  count = count%hmax;
	    				  if (count ==0) sum--;
	    			  }
	    		  }
	    		  if (sum<=p) l = mid;
	    		  else r = mid-1;
	    		  //System.out.println(mid);
	    	  }
	    	  System.out.println(l);
	    	  
	      }
	}
}
