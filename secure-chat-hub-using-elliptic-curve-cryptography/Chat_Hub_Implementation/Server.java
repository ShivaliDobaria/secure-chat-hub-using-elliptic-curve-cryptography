import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Certificate;
import java.security.KeyStore;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server{
	int port;
	ServerSocket server = null;
	Socket client = null;
	ExecutorService pool = null;
	int clientcount = 0;
	public static void main(String[] args) throws IOException {
		Server serverobj = new Server(5000);
		serverobj.startServer();
		
	}
	
	Server(int port){
		this.port = port;
		pool = Executors.newFixedThreadPool(5);
	}

	public void startServer() throws IOException {
		server = new ServerSocket(6000);
		
		while(true)
		{
			client = server.accept();
			clientcount++;
			ServerThread runnable = new ServerThread(client,clientcount,this);
			pool.execute(runnable);
		}
	}
	
	private static class ServerThread implements Runnable {
		Server server = null;
		Socket client;
		BufferedReader cin;
		PrintStream cout;
		Scanner sc = new Scanner(System.in);
		int id;
		String s;
		
		
		ServerThread(Socket client, int count, Server server) throws IOException {
			this.client = client;
			this.server = server;
			this.id = count;
			System.out.println("Connection "+ id +"established with client "+ client);
			cin = new BufferedReader(new InputStreamReader(client.getInputStream()));
			cout = new PrintStream(client.getOutputStream());	
		}
		
		public void run() 
		{
			int x = 1;
			try {
				while(true) {
					s = cin.readLine();
					/*if(s == "I want to communicate with bob bob.crt is his certificate");
					{
						// Loading a Keystore example
						KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

						// get user password and file input stream
						char[] password = "123456".toCharArray();

						java.io.FileInputStream fis = null;
						try {
						    fis = new java.io.FileInputStream("bob");
						    ks.load(fis, password);
						} finally {
						    if (fis != null) {
						        fis.close();
						    }
						}

						java.security.cert.Certificate cert = ks.getCertificate("bob");
						PublicKey publicKey = cert.getPublicKey();

						try {
						  cert.verify(publicKey);
						} catch(Exception e) {
						  System.out.println("An error occured...");
						}
						cout.println("ok write.");
					}*/
					System.out.print("Client("+id+"): "+ s +"\n");
					System.out.print("Server :");
					 s = sc.nextLine();
					 if(s.equalsIgnoreCase("bye")) {
						 cout.println("BYE");
						 x = 0;
						 break;
					 }
					 cout.println(s);
				}
				
				cin.close();
				client.close();
				cout.close();
				if(x == 0)
				{
					System.out.println("Server cleaning up");
					System.exit(0);
				}
			}catch(Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
}