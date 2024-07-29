import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Server {
	Socket socket;
	ServerSocket server;

	BufferedReader br;
	PrintWriter out;

	public Server(){
		try {
			server = new ServerSocket(1112);
			System.out.println("server loading...");
			socket = server.accept();
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			
			startReading();
			startWriting();
		
		} catch (Exception e) {
			e.printStackTrace();
			}
	}

	public void startReading() {
		// ei Thread ta read kareba
		Runnable r1 = () -> {
			System.out.println("reader Started");
			try {
			while(true) {
				
					String msg=br.readLine();
					if(msg.equals("Exit")) {
						System.out.println("Client stopped the Chat");
						socket.close();
						break;
						
					}
					System.out.println("Client:"+msg);
				
			}
			} catch (IOException e) {
				e.printStackTrace();
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
				BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
				String content=br1.readLine();
				
				out.println(content);
				out.flush();
				
				if(content.equals("Exit")) {
					
					socket.close();
					break;
				}
			}
			}catch (Exception e) {
				System.out.println("Connection is closed");
			} 
		};
		new Thread(r2).start();
	}

	public static void main(String[] ar) {
		System.out.println("server here");
		new Server();
	}
}
