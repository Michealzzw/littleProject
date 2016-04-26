package cyc;

import java.util.Scanner;

public class third {
	static boolean[][][] world = new boolean[102][102][102];
	static int[][][] worldid = new int[102][102][102];
	static int[] x= new int[100000],y= new int[100000],z = new int[100000];
	static int[] fa = new int[200000];
	static boolean check(int x, int y, int z)
	{
		if (world[x][y][z]) return false;
		if (z==1) return true;
		if (world[x-1][y][z]||world[x+1][y][z]) return true;
		if (world[x][y-1][z]||world[x][y+1][z]) return true;
		if (world[x][y][z-1]||world[x][y][z+1]) return true;
		return false;
	}
	static void floodfill(int x, int y, int z , int color)
	{
		if (x<0||x>101||y<0||y>101||z<0||z>101) return;
		if (world[x][y][z]) return;
		if (worldid[x][y][z]!=0) return;
		worldid[x][y][z] = color;
		floodfill(x-1,y,z,color);
		floodfill(x+1,y,z,color);
		floodfill(x,y-1,z,color);
		floodfill(x,y+1,z,color);
		floodfill(x,y,z-1,color);
		floodfill(x,y,z+1,color);
		
	}
	static void BackToOrigin(int n)
	{
		for (int i = 0;i<n;i++)
		{
			world[x[i]][y[i]][z[i]] = false;
		}
	}
	static int gf(int now)
	{
		if (fa[now]==now) return now;
		fa[now] = gf(fa[now]);
		return fa[now];
	}
	static public void main(String[] args)
	{
		  Scanner sc = new Scanner(System.in);
		  for (int x = 0;x<102;x++)
			  for (int y = 0;y<102;y++)
				  for (int z = 0;z<102;z++)
					  world[x][y][z] = false;
		  int t = sc.nextInt();
		  while ((t>0))
		  {
			  t--;
			  int n  = sc.nextInt();
			  boolean flag = true;
			  for (int i = 0;i<n;i++)
			  {
				  x[i] = sc.nextInt();
				  y[i] = sc.nextInt();
				  z[i] = sc.nextInt();
				  if (!check(x[i],y[i],z[i])) flag = false;
				  world[x[i]][y[i]][z[i]] = true;
			  }
			  if (!flag) 
			  {
				  	System.out.println("No");
				  	BackToOrigin(n);
				  	continue;
			  }
			  int num = 0;
			  fa[0] = 0;
			  for (int x = 0;x<102;x++)
				  for (int y = 0;y<102;y++)
					  for (int z = 0;z<102;z++)
						  worldid[x][y][z] = 0;
			  for (int x = 0;x<102;x++)
				  for (int y = 0;y<102;y++)
					  for (int z = 0;z<102;z++)
						  if (worldid[x][y][z]==0&&!world[x][y][z])
						  {
							  num++;
							  floodfill(x,y,z,num);
							  fa[num] = num;
						  }
			  for (int i = n-1;i>=0;i--)
			  {
				  int color = 0;
				  int xx = x[i];
				  int yy= y[i];
				  int zz= z[i];
				  if (!world[xx-1][yy][zz])
				  {
					  if (color==0) color = gf(worldid[xx-1][yy][zz]);
					  else fa[gf(worldid[xx-1][yy][zz])] = color;
				  }
				  if (!world[xx+1][yy][zz])
				  {
					  if (color==0) color = gf(worldid[xx+1][yy][zz]);
					  else fa[gf(worldid[xx+1][yy][zz])] = color;
				  }
				  if (!world[xx][yy-1][zz])
				  {
					  if (color==0) color = gf(worldid[xx][yy-1][zz]);
					  else fa[gf(worldid[xx][yy-1][zz])] = color;
				  }
				  if (!world[xx][yy+1][zz])
				  {
					  if (color==0) color = gf(worldid[xx][yy+1][zz]);
					  else fa[gf(worldid[xx][yy+1][zz])] = color;
				  }
				  if (!world[xx][yy][zz+1])
				  {
					  if (color==0) color = gf(worldid[xx][yy][zz+1]);
					  else fa[gf(worldid[xx][yy][zz+1])] = color;
				  }
				  if (!world[xx][yy][zz-1])
				  {
					  if (color==0) color = gf(worldid[xx][yy][zz-1]);
					  else fa[gf(worldid[xx][yy][zz-1])] = color;
				  }
				  worldid[xx][yy][zz] = color;
				  if (gf(worldid[xx][yy][zz])!=gf(worldid[0][0][0]))
				  {
					  flag = false;
					  break;
				  }
				  
			  }
			  if (!flag) 
				  	System.out.println("No");
				  	else
				  		System.out.println("Yes");
			  BackToOrigin(n);
		  }
	}
}
