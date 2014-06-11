import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class Client 
{
	private int portNum = 59063;
	private Socket clientsocket = null;
	private String ip = "localhost";
	private String filename= "";
	
	public static void main(String args[])
	{
		Client client = null;
		client = new Client();
		client.UI();;
	}
	public Client()
	{
		JOptionPane.showMessageDialog(null,"MyBrowser is tested and verified with only the given Server."
				+ "","Info Regarding MyBrowser",JOptionPane.INFORMATION_MESSAGE);
	}
	private int sendRequest(String filename, String ip, int port)
	{
		PrintWriter output = null;
		try 
		{
			clientsocket = new Socket(ip, port);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientsocket.getOutputStream())));	
			//Send request to the server
			output.print("GET /"+filename+" HTTP/1.1\r\n\r\n");
			output.flush();	
			return 1;
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return -1;
		}
	}
	
	public void UI()
	{
		// frame
		JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("MyBrowser");
        frame.setSize(1100,500);
        frame.setLocationRelativeTo(null);
        
        //Declaring controls 
        JPanel Panel = new JPanel();
        JLabel Lbl = new JLabel("File name: ");
        final JTextField txt1 = new JTextField(20);
        txt1.setText(filename);
        JLabel Lblip = new JLabel("IP Address: ");
        final JTextField txtip = new JTextField(20);
        txtip.setText(ip);
        JLabel Lblport = new JLabel("Port Number: ");
        final JTextField txtport = new JTextField(20);
        // submit button
        final JButton submit = new JButton( "Submit");
        final JTextArea textarea = new JTextArea(25,90);
        
        JScrollPane areaScrollPane = new JScrollPane(textarea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // adding controls to panels       
        Panel.add(Lbl);
        Panel.add(txt1);
        Panel.add(Lblip);
        Panel.add(txtip);
        Panel.add(Lblport);
        Panel.add(txtport);
        Panel.add(submit);
        Panel.add(areaScrollPane);
        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);
        textarea.setEditable(false);
        Panel.setVisible(true);
        
        // listener for submit button
        submit.addActionListener(new ActionListener() 
        {	
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				// TODO Auto-generated method stub
				//txt1.disable();
				
				Object source = event.getSource();
				if(source == submit)
				{
					
					if((txtip.getText().isEmpty()==true) || (txtport.getText().isEmpty()==true) || (txt1.getText().isEmpty()==true ))
					{
						JOptionPane.showMessageDialog(null,"Value Not field","All field must be provided.",JOptionPane.WARNING_MESSAGE);
						return;
					}
					ip = txtip.getText();
					try
					{
						portNum = Integer.parseInt(txtport.getText());
					}
					catch(NumberFormatException e)
					{
						JOptionPane.showMessageDialog(null,"Port Number must be integer.","Port Number!",JOptionPane.WARNING_MESSAGE);
						return;
					}
					String filename = txt1.getText();
					submit.setEnabled(false);
					if (sendRequest(filename, ip, portNum)==-1)
					{
						JOptionPane.showMessageDialog(null,"Recheck the value of IP or Port.","Could not find the server",JOptionPane.WARNING_MESSAGE);
						submit.setEnabled(true);
						return;
					}
					BufferedReader in = null;
					try 
					{
						in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
						String line = in.readLine();
						System.out.println("Status="+line);
						textarea.setText(line);
						String request;
						while((request = in.readLine()) != null)
						{
							System.out.println(request);
							textarea.setText(textarea.getText()+"\n"+request);
						}
					} 
					catch (IOException e) 
					{
						// TODO Auto-generated catch block
						System.out.println(e.getMessage());
					}
					finally
					{
						System.out.println("Client: connection terminated");
						try 
						{
							clientsocket.close();
						} 
						catch (IOException e) 
						{
							// TODO Auto-generated catch block
							System.out.println("Client: termination "+e.getMessage());
						}
					}
				}
				
			}
		});
        
        frame.add(Panel);
        frame.setVisible(true);
	}
}
