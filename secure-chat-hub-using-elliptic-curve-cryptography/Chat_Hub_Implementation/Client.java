import javax.crypto.*;
import java.math.BigInteger;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.KeyAgreement;
import java.security.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Client {

  public static void main(String[] args) throws UnknownHostException, IOException {
      Socket sk = new Socket("localhost", 6000);
      BufferedReader sin = new BufferedReader(new InputStreamReader(sk.getInputStream()));
      PrintStream sout = new PrintStream(sk.getOutputStream());
      BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
      String s;
      while(true)
      {
    	  System.out.print("Client: ");
    	  s = stdin.readLine();
    	  sout.println(s);
    	  if(s.equalsIgnoreCase("BYE")) {
    		  System.out.println("Connection ended ");
    		  break;
    	  }
    	  s = sin.readLine();
    	  System.out.println("From Server: " + s + "\n");
    	 
      }
  
      sk.close();
      sin.close();
      sout.close();
      stdin.close();
    }
}
