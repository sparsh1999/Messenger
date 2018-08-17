import javax.swing.*;
public class TestClient
{
	public static void main(String[] args) {
		Client client = new Client("127.1.1.0");
	    client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    client.setLocationRelativeTo(null);
	    client.setVisible(true);
	    client.startRunning();
	    
	    
	}
	
}
	
