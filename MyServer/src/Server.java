
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

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

public class Server {
	static ServerSocket server;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Socket c = null;
		BufferedReader in= null;
		PrintWriter out= null;
		int portnum =0;
		JFrame frame = new JFrame("InputDialog Example #1");
		String port = JOptionPane.showInputDialog(frame,"Provide the port number where you wan to run Server.","Port Number",JOptionPane.QUESTION_MESSAGE);
		try 
		{
			if (port.isEmpty()==true)
			{
				JOptionPane.showMessageDialog(null,"Please restart the Server.","Server Not Running",JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}
			try
			{
				portnum = Integer.parseInt(port);
			}
			catch(NumberFormatException e)
			{
				JOptionPane.showMessageDialog(null,"Port Number must be integer.","Port Number!",JOptionPane.WARNING_MESSAGE);
				JOptionPane.showMessageDialog(null,"Please restart the Server.","Server Shuting Down ",JOptionPane.INFORMATION_MESSAGE);
				System.exit(1);
			}
			server = new ServerSocket(portnum);
			JOptionPane.showMessageDialog(null,"Server Successfully Started!","Server Running ",JOptionPane.INFORMATION_MESSAGE);
			c = server.accept() ;
			in = new BufferedReader(new InputStreamReader(c.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(c.getOutputStream())));
			
			String firstline =in.readLine();
			System.out.println(firstline );
			String[] file= firstline.split(" ");
	        String line;
	        while ((line = in.readLine()) != null) {
	          if (line.length() == 0)
	            break;
	          System.out.println(line );
	        }
			


	        out.print("HTTP/1.1 200 \r\n"); 
	        out.print("Connection: keep-alive\r\n");
	        out.print("Content-Type: text/html\r\n"); 
	        out.print("Content-Length: 3988\r\n"); 
	        out.print("\r\n");
			
			
	        BufferedReader reader = new BufferedReader(new FileReader("bin\\"+ file[1]));
	        String ln = null;
	        while ((ln = reader.readLine()) != null) 
	        {
	        	out.print(ln + "\r\n");
	        }
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally
		{
			out.close();
            try {
            	in.close();
				c.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
		}
	}

}
