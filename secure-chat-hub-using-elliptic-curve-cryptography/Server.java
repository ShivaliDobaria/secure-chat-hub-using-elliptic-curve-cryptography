import java.io.*;
import java.math.BigInteger;
import java.net.*;
import javax.crypto.*;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.KeyAgreement;
import java.security.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.spec.EncodedKeySpec;
import java.util.*;
public class Server
{
  public static void main(String[] args) throws Exception
  {

      ServerSocket sersock = new ServerSocket(3005);

      Socket sock = sersock.accept( );
      BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
      OutputStream ostream = sock.getOutputStream();
      PrintWriter pwrite = new PrintWriter(ostream, true);
      InputStream istream = sock.getInputStream();
      BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
      boolean isCommunicate = false;

      String receiveMessage, sendMessage;
      String cipherText = "";
      String plainText = "";

      BigInteger keyprivate = BigInteger.ZERO;
      BigInteger sharedKey = BigInteger.ZERO;
        if((receiveMessage = receiveRead.readLine()) != null)
        {
          System.out.println("Recieved :ka Command");
          if(receiveMessage.equalsIgnoreCase("1.ecdh-secp256r1+x509+aes128/gcm128  2.ecdh-secp224r1+x509+aes128/gcm128"))
          {
              keyValuesForCommand object = new  keyValuesForCommand();
              String valueofkey = object.getValue(":kaok");
              System.out.println(valueofkey);
              System.out.println("Sending :kaok Command after validation of algo");
              EcdhEncDec obj = new EcdhEncDec();
              KeyPair key1 = obj.generateKey();
              byte[] ourPbk = key1.getPublic().getEncoded();
              byte[] ourPvk = key1.getPrivate().getEncoded();
              BigInteger keypublic = new BigInteger(ourPbk);
               keyprivate = new BigInteger(ourPvk);
              String publicKeyOfServer = new String(ourPbk);
              String privateKeyOfServer = new String(ourPvk);
              BigInteger bi1 = new BigInteger("7");
              System.out.println("Public Key: "+publicKeyOfServer);
              System.out.println("Private Key: "+privateKeyOfServer);
              pwrite.println(keypublic.multiply(bi1));
            }
            else{
              keyValuesForCommand object = new  keyValuesForCommand();
              String valueofkey = object.getValue(":err");
              System.out.println(valueofkey);
              System.out.println("Sending :kaok Command after validation of algo");
            }

          }

          if((receiveMessage = receiveRead.readLine()) != null)
          {
            EcdhEncDec obj1 = new EcdhEncDec();
            System.out.println("Public Key of Client: "+receiveMessage);
             sharedKey = obj1.generateSharedSecret(new BigInteger(receiveMessage),keyprivate);
            System.out.println("SharedKey: "+sharedKey);
          }
          System.out.println("Connection Establised now you can communicate with each other.......");
          while(true)
          {
            if((receiveMessage = receiveRead.readLine()) != null)
            {
              try{
                  EcdhEncDec obj1 = new EcdhEncDec();
                 // plainText = obj1.decrypt(receiveMessage,new String(sharedKey.toByteArray()));
                } catch(Exception e){
                  e.printStackTrace();
                }
              
                System.out.println("Text: " +receiveMessage);

            }
            sendMessage = keyRead.readLine();
            try{
                EcdhEncDec obj = new EcdhEncDec();
                cipherText = obj.encrypt(sendMessage,new String(sharedKey.toByteArray()));
              } catch(Exception e){
                e.printStackTrace();
              }
            pwrite.println(cipherText);
            pwrite.flush();
          }
    }
}
