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
    boolean isCommunicate = false;

      Socket sock = new Socket("127.0.0.1", 3005);
      BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
      OutputStream ostream = sock.getOutputStream();
      PrintWriter pwrite = new PrintWriter(ostream, true);

     InputStream istream = sock.getInputStream();
     BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
     String receiveMessage, sendMessage;
     String cipherText = "";
     String plainText = "";
     BigInteger keyprivate = BigInteger.ZERO;
     BigInteger sharedKey = BigInteger.ZERO;
     try{
     keyValuesForCommand object = new  keyValuesForCommand();
     String valueofkey = object.getValue(":ka");
    System.out.println(valueofkey);
     pwrite.println(valueofkey);

     if((receiveMessage = receiveRead.readLine()) != null)
     {
      // if(receiveMessage.equalsIgnoreCase("ecdh-secp224r1+nocert+aes128/cbc"))
       //{
         System.out.println("Ok Message from Server");
         EcdhEncDec obj = new EcdhEncDec();
         KeyPair key1 = obj.generateKey();
         PrivateKey forClient = key1.getPrivate();
         byte[] ourPbk = key1.getPublic().getEncoded();
         byte[] ourPvk = key1.getPrivate().getEncoded();
         keyprivate = new BigInteger(ourPvk);
         BigInteger keypublic = new BigInteger(ourPbk);
         String publicKeyOfClient = new String(ourPbk);
         String privateKeyOfClient = new String(ourPvk);
         BigInteger bi1 = new BigInteger("7");
         System.out.println("Public Key: "+publicKeyOfClient);
         System.out.println("Private Key: "+privateKeyOfClient);
         pwrite.println(keypublic.multiply(bi1));
      // }
     }

    // if((receiveMessage = receiveRead.readLine()) != null)
     //{
       EcdhEncDec obj1 = new EcdhEncDec();
       System.out.println("Public Key of Server: "+receiveMessage);
        sharedKey = obj1.generateSharedSecret(new BigInteger(receiveMessage),keyprivate);
       System.out.println("SharedKey: "+sharedKey);
       System.out.println("Connection Establised now you can communicate with each other.......");
     //}
   } catch(Exception e){
   e.printStackTrace();
   }

   while(true)
    {
      sendMessage = keyRead.readLine();  // keyboard reading
      try{
        EcdhEncDec obj = new EcdhEncDec();
        cipherText = obj.encrypt(sendMessage,new String(sharedKey.toByteArray()));
      } catch(Exception e){
        e.printStackTrace();
      }
      pwrite.println(cipherText);       // sending to server
      pwrite.flush();                    // flush the data
      if((receiveMessage = receiveRead.readLine()) != null) //receive from server
      {
        try{
            EcdhEncDec obj1 = new EcdhEncDec();
            plainText = obj1.decrypt(receiveMessage,new String(sharedKey.toByteArray()));
          } catch(Exception e){
            e.printStackTrace();
          }

            System.out.println("Text: " +plainText);
      }
    }
   }
}
