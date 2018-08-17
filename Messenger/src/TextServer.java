import javax.swing.JFrame;


public class TextServer 
{
   public static void main(String[] args)
   {
	   Server frame = new Server();
//	   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.startRunning();
   }
}
