import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame {
	
	Socket socket;
	BufferedReader br;
	PrintWriter out;
	
	//Declare Components
	private JLabel heading=new JLabel("Client Area");
	private JTextArea messageArea =new JTextArea();
	
	private JTextField messageInput=new JTextField();
	private Font font=new Font("Roboto",Font.PLAIN,20);
	
	public Client() {
		try {
			System.out.println("Sending Request to server");
			socket = new Socket("127.0.0.1", 1112);
			System.out.println("Connection done");

			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());

			createGUI();
			handleEvents();
			startReading();
			//startWriting();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	private void handleEvents() {
		messageInput.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				System.out.println("key released"+e.getKeyCode());
				if(e.getKeyCode()==10) {
					//System.out.println("you have pressed enter button");
					String contentToSend=messageInput.getText();
					messageArea.append("Me :"+contentToSend+"\n");
					out.println(contentToSend);
					out.flush();
					messageInput.setText(" ");
				}
			}
			
		});
		
	}
	private void createGUI() {
		//gui code-
		this.setTitle("Client Messenger");
		this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//coding for componenet
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		heading.setIcon(new ImageIcon("download.png"));
		
		
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20, 20,20));
		messageArea.setEditable(false);
	
		//frame ka layout set karenge
		this.setLayout(new BorderLayout());
		
		//adding kara haba component  ... kahaku frame ku
		this.add(heading,BorderLayout.NORTH);
		JScrollPane scrollpane=new JScrollPane(messageArea);
		this.add(messageArea,BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);
		
		
		this.setVisible(true);
		
		
	}

	public void startReading() {
		// ei Thread ta read kareba
		Runnable r1 = () -> {
			System.out.println("reader Started");
			try {
			while (true) {
				
					String msg = br.readLine();
					if (msg.equals("Exit")) {
						System.out.println("Server stopped the Chat");
						JOptionPane.showMessageDialog(this, "Server stopped the chat");
						messageInput.setEnabled(false);
						socket.close();
						break;

					}
					//System.out.println("Server:" + msg);
					messageArea.append("Server :"+msg+"\n");
				
			}
			} catch (IOException e) {
				System.out.println("Connection is closed");
			}
		};
		new Thread(r1).start();
	}

	public void startWriting() {
		// ei thread ta write kariba
		Runnable r2 = () -> {
			System.out.println("Writer Started");
			try {
				while(!socket.isClosed()) {
				BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
				String content = br1.readLine();
				out.println(content);
				out.flush();
				
				if(content.equals("Exit")) {
				
					socket.close();
					break;
				}
				
			} 
			}catch (Exception e) {
				//e.printStackTrace();
				System.out.println("Connection is closed");
			}
		};
		new Thread(r2).start();
	}

	public static void main(String[] ar) {
		System.out.println("Client here");
		new Client();
	}
}
