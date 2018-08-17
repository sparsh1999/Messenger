import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;


public class Client extends JFrame
{
	private JPanel contentPane;
	private JTextField userText;
	private JTextArea chatWindow;
	private String serverIP = "";
	private String message = "";
	// this is describes the port number , waiting for everyone to conncet on the server 
	private ServerSocket server;
	
	// the communication between two computers on the servers happends in form of STREAMS which have 
	// outputstream --> your computer to someone's computer 
	// inputstream --> someone's computer o your computer
	private OutputStream output ;
	private InputStream input;
	
	// a conncection between the server and your computer
	private Socket connection;

public Client(String host) 
{
	super("Client");
	//setLocationRelativeTo(null);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(0, 0, 500, 500);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	contentPane.setLayout(new BorderLayout(0, 0));
	setContentPane(contentPane);
	
	userText = new JTextField();
	userText.setFont(new Font("Tahoma", Font.PLAIN, 17));
	
	// to disable sending or typing message before everyone gets connected
	
	
	// to send messages 
	userText.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0)
		{
			sendMessage(arg0.getActionCommand().toString());
			userText.setText("");
			
		}
	});
	userText.setEditable(false);
	contentPane.add(userText, BorderLayout.NORTH);
	userText.setColumns(10);
	
	chatWindow = new JTextArea();
	chatWindow.setFont(new Font("Tahoma", Font.PLAIN, 17));
	getContentPane().add(new JScrollPane(chatWindow),BorderLayout.CENTER);
	
}

public void startRunning ()
{
  try 	
  {
	  connectToServer();
	  setupStreams();
	  whileChatting();
  }
  catch (EOFException eof)
  {
	  showMessage("Client terminated the Connection");
  }
  catch (IOException io)
  {
	  io.printStackTrace();
  }
  finally 
  {
	closeCrap();  
  }
}

public void connectToServer() throws IOException
{
	showMessage("Trying to connect to Server");
	
	connection = new Socket(InetAddress.getByName("127.1.1.0"),6743);
	
	showMessage("Now connected to "+connection.getInetAddress().getHostName());
}

public void setupStreams() throws IOException
{
 output = new ObjectOutputStream(connection.getOutputStream());
 output.flush();
 
 input = new ObjectInputStream(connection.getInputStream());
showMessage("All the Client side Streams now set up ");
}

public void whileChatting() throws IOException
{
 ableToType(true);
 
  do 
  {
	 try 
	 
     {
	 message =(String) ((ObjectInputStream) input).readObject();
	 showMessage(message);
     }
     catch(ClassNotFoundException e)
     {
	 showMessage("didn't understood what server said");
     }
 }
 while (!message.equalsIgnoreCase("SERVER - END"));
}

public void closeCrap()
{
	showMessage(" Closing Connections ..");
	ableToType(false);
	
	try 
	{
		output.close();
		input.close();
		connection.close();
	}
	catch (IOException e)
	{
		e.printStackTrace();
	}
}

public void sendMessage(String s)
{
	try
	{
		((ObjectOutputStream) output).writeObject("CLIENT - "+s);
		output.flush();
		showMessage("CLIENT - "+s);
	}
	catch(Exception e)
	{
		chatWindow.append("\n"+"Error sending message");
	}
	
}

public void showMessage(final String s)
{
	// it will update the part of the GUI
	SwingUtilities.invokeLater(new Runnable() 
	{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			chatWindow.append("\n"+s);
		}
	});
}

// it will let the user to type
public void ableToType(final boolean able)
{
     SwingUtilities.invokeLater(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			userText.setEditable(able);
		}
	});
}
}
