package testdb2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer2 {
	
	public void start(int port) {
		ServerSocket server;
		Socket socket;
		ServerThread2 thread;
		
		try {
			server = new ServerSocket(port);
			System.err.println("서버 시작:"+port);
			while(true){
				socket = server.accept();
				System.out.println("Socket 연결"+socket);
				thread = new ServerThread2(socket);
				thread.start();				
			}
			
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void main(String[] args) {
		ChatServer2 server = new ChatServer2();
		server.start(8266);
	}
}
