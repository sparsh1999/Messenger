import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

import com.sun.java.swing.SwingUtilities3;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.Font;
import java.awt.Color;


public class Server extends JFrame {

	private JPanel contentPane;
	private JTextField userText;
	private JTextArea chatWindow;
	
	// this is describes the port number , waiting for everyone to conncet on the server 
	private ServerSocket server;
	
	// the communication between two computers on the servers happends in form of STREAMS which have 
	// outputstream --> your computer to someone's computer 
	// inputstream --> someone's computer o your computer
	private OutputStream output ;
	private InputStream input;
	
	// a conncection between the server and your computer
	private Socket connection;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Server() {
		super("Server");
		//setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
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
	
	// set up and run the server
	public void startRunning () 
	{
		try 
		{
			// 6709 is the port number where application is located and 100 is the no of users that can be waiting on the same network
		    server = new ServerSocket(6743,100);	
		    
		    while (true)
		    {
		    	try 
		    	{
		        // wait for connection to happen
		    	waitForConnection();
		    	
		    	// setup all the streams
		    	setupStreams();
		    	
		    	// allows chatting before connection ends
		    	whileChatting();
		        }
		    	catch (Exception e)
		    	{
		    		e.printStackTrace();
		    		showMessage("Server ended the connection");
		    	}
		    	finally 
				{
					closeCrap();
				}
		    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
		
	}
	
	// wait for connection and then show / display the connection properties
	public void waitForConnection() throws IOException
	{
		showMessage("Waiting for someone to connect ....");
		
		// this will set the socket or connecion when someone sets up the connection 
		connection = server.accept();
		
		// gethostname is get the i/p address
		showMessage("Now connected to "+connection.getInetAddress().getHostName());
	}
	
	public void setupStreams() throws IOException
	{
		output = new ObjectOutputStream(connection.getOutputStream());
		// it will flush out any data value/bits left in your stream to someone's else computer
		output.flush();
		
		input = new ObjectInputStream(connection.getInputStream());
		// you can not flush someone's else output to your computer only they can do this
		
		showMessage("Streams are all set up");
	}
	
	public void whileChatting () throws IOException
	{
		String message = "You are now connected";
		showMessage(message);
		ableToType(true);
		
		do
		{
			try
			{
				message =  (String) ((ObjectInputStream) input).readObject();
				showMessage(message);
				
			}
			catch (Exception e)
			{
				//e.printStackTrace();
				//showMessage("\n"+"didn't understood the client's message");
			}
		}
		while(!message.equalsIgnoreCase("CLIENT - END"));
		
	}
	
	//close streams and sockets after we have done chatting 
	
	public void closeCrap()
	{
		showMessage("\n Closing Connections ..");
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
	
	// send Message to the client
	public void sendMessage(String s)
	{
		try
		{
			 ((ObjectOutputStream) output).writeObject("SERVER - "+s);
			output.flush();
			showMessage("SERVER - "+s);
		}
		catch(Exception e)
		{
			chatWindow.append("\n"+"Error sending message");
		}
		
	}
	
	// to display the messages / update the GUI using a thread
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

	

