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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class Client {
	private int portNum = 59063;
	private Socket clientsocket = null;
	private String ip = "";
	
	public static void main(String args[]){
		Client wc = null;
		try {
			wc = new Client();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("Client unknown host exception-"+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Client io exception-"+e.getMessage());
		}
		wc.userInterface();;
	}
	
	public Client() throws UnknownHostException, IOException{
		
	}
	
	private void sendRequest(String filename, String ip, int port){
		PrintWriter output = null;
		try {
			clientsocket = new Socket(ip, port);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientsocket.getOutputStream())));
			
			//Send filename to the server
			output.print("GET /"+filename+" HTTP/1.1\r\n\r\n");
			output.flush();
			System.out.println("request send");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void userInterface(){
		JFrame guiFrame = new JFrame();
        
        //to make sure the program exits when the frame closes
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("Web Client");
        guiFrame.setSize(1100,500);
        
      //This will center the JFrame in the middle of the screen
        guiFrame.setLocationRelativeTo(null);
        
      //The first JPanel contains a JLabel and JCombobox
        JPanel comboPanel = new JPanel();
        JLabel comboLbl = new JLabel("Enter the file name: ");
        final JTextField txt1 = new JTextField(20);
        JLabel Lblip = new JLabel("IP Address: ");
        final JTextField txtip = new JTextField(20);
        JLabel Lblport = new JLabel("Port Number: ");
        final JTextField txtport = new JTextField(20);
        final JButton submit = new JButton( "Submit");
        final JTextArea textarea = new JTextArea(25,90);
        //DefaultCaret caret = (DefaultCaret)textarea.getCaret();
        //caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        //textarea.setAutoscrolls(true);
        JScrollPane areaScrollPane = new JScrollPane(textarea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        //areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        comboPanel.add(comboLbl);
        comboPanel.add(txt1);
        comboPanel.add(Lblip);
        comboPanel.add(txtip);
        comboPanel.add(Lblport);
        comboPanel.add(txtport);
        comboPanel.add(submit);
        //comboPanel.add(textarea);
        comboPanel.add(areaScrollPane);
        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);
        textarea.setEditable(false);
        comboPanel.setVisible(true);
        
      //The ActionListener class is used to handle the event that happens when the user clicks the button
        submit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				//txt1.disable();
				submit.setEnabled(false);
				Object source = event.getSource();
				ip = txtip.getText();
				portNum = Integer.parseInt(txtport.getText());
				if(source == submit){
					String filename = txt1.getText();
					sendRequest(filename, ip, portNum);
					
					BufferedReader in = null;
					try {
						in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
						String line = in.readLine();
						System.out.println("Status="+line);
						textarea.setText(line);
						
						String request;
						while((request = in.readLine()) != null){
							System.out.println(request);
							textarea.setText(textarea.getText()+"\n"+request);
							//textarea.append(request);
							//textarea.setCaretPosition(textarea.getDocument().getLength());
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally{
						System.out.println("Client connection terminated");
						try {
							clientsocket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.println("Client connection closed. "+e.getMessage());
						}
					}
				}
				
			}
		});
        
        guiFrame.add(comboPanel);
        
        //make sure the JFrame is visible
        guiFrame.setVisible(true);
	}
}
