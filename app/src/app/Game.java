package app;
import javax.swing.*;
import java.awt.*;
import java.applet.*;

public class Game extends JFrame{ 
	public static void main(String[] argu)
	{
		Game f = new Game();
		f.setVisible(true);
		f.setSize(300,400);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
	}
	public void paint(Graphics g) {    
	    super.paint(g);// 调用父类的paint方法或调用下面的方法直接绘制组件   
	    g.drawRect(10 ,10 , 0, 0);    
	    g.setFont(new Font("", Font.BOLD, 13));    
	    g.setColor(Color.WHITE);    
	}  
}

class MessagePanel extends JPanel {
	  public void paintComponent(Graphics grafObj) {
	    super.paintComponent(grafObj);
	    grafObj.drawString("Hello World!!",50,25);
	    Integer.p
	  }
	}