import java.security.Key;
import java.security.cert.X509Certificate;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import javax.crypto.SecretKey;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.security.cert.CertificateExpiredException;
import java.security.*;
import java.security.spec.*;

import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

import com.sun.corba.se.impl.ior.ByteBuffer;

import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Stream;

public class EcdhEncDec {
  public static void main(String[] args) {
  }
  private static final String ALGO = "AES";
  private static final byte[] keyValue =
           new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
    private  static String INITIALIZATIO_VECTOR = "AODVNUASDNVVAOVF";
    
    public static final int AES_KEY_SIZE = 128; // in bits
    public static final int GCM_NONCE_LENGTH = 12; // in bytes
    public static final int GCM_TAG_LENGTH = 16; // in bytes
    
    static KeyPairGenerator kpg;

    public static String verifyCertificate(X509Certificate cert, Certificate caCert, String signature) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException, SignatureException {
		try {
			cert.checkValidity();
		} catch (Exception e) {
			return "";
		}
		try {
			cert.verify(caCert.getPublicKey());
		} catch (Exception e) {
			return "";
		}
		byte[] decrypt = convertStringToBytes(signature);
		if(!verifySignature(cert.getPublicKey().getEncoded(), decrypt, cert.getPublicKey())) {
			return "";
		}
		return null;
	}
    
	public byte[] getPublicKey(){
		return this.kpg.genKeyPair().getPublic().getEncoded();
	}
	
    public static byte[] convertStringToBytes(String s) throws UnsupportedEncodingException {
		return s.getBytes("UTF-8");
	}
    
    public static boolean verifySignature(byte[] message, byte[] signature, PublicKey key) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
		Signature publicSignature = Signature.getInstance("SHA256withRSA");
		publicSignature.initVerify(key);
		publicSignature.update(message);
		return publicSignature.verify(signature);
	}
    
    
    public static String encrypt(String data,String keyforencryption) throws Exception {
     /* Key key = generateKeyuse();
      Cipher c = Cipher.getInstance(ALGO);
      c.init(Cipher.ENCRYPT_MODE, key);
      byte[] encVal = c.doFinal(data.getBytes());
      return Base64.getEncoder().encodeToString(encVal);*/
    	SecureRandom random = SecureRandom.getInstanceStrong();
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_KEY_SIZE, random);
        SecretKey key = keyGen.generateKey();
    	Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
        final byte[] nonce = new byte[GCM_NONCE_LENGTH];
        random.nextBytes(nonce);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

       // byte[] aad = "Whatever I like".getBytes();
       // cipher.updateAAD(aad);

        final byte[] tag = new byte[GCM_TAG_LENGTH]; 
        random.nextBytes(tag); 
        byte[] cipherText = cipher.doFinal(data.getBytes());

        // Decrypt; nonce is shared implicitly
      //  cipher.init(Cipher.DECRYPT_MODE, key, spec);
        return Base64.getEncoder().encodeToString(cipherText);
    }


    public static String decrypt(String encryptedData,String keyforDecryption) throws Exception {
     /* Key key = generateKeyuse();
      Cipher c = Cipher.getInstance(ALGO);
      c.init(Cipher.DECRYPT_MODE, key);
      byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
      byte[] decValue = c.doFinal(decordedValue);
      return new String(decValue);*/
    	Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
    	SecureRandom random = SecureRandom.getInstanceStrong();
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_KEY_SIZE, random);
        SecretKey key = keyGen.generateKey();
        final byte[] nonce = new byte[GCM_NONCE_LENGTH];
        random.nextBytes(nonce);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
    	cipher.init(Cipher.DECRYPT_MODE, key, spec);
    	
    	//cipher.update(encryptedData.getBytes());
    	
            byte[] plainText = cipher.doFinal(encryptedData.getBytes());
            return new String(plainText);
            
 }

    public static KeyPair generateKey() throws Exception {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
      ECGenParameterSpec ecsp;
      ecsp = new ECGenParameterSpec("secp256r1");
      kpg.initialize(ecsp);
      KeyPair kp = kpg.genKeyPair();
      return kp;
    }

    public static BigInteger generateSharedSecret(BigInteger privateKey,BigInteger publicKey) throws Exception {
      BigInteger sharedkey = privateKey.multiply(publicKey);
      return sharedkey;
    }
    

    private static Key generateKeyuse() throws Exception {
    	SecureRandom random = SecureRandom.getInstanceStrong();
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_KEY_SIZE, random);
        SecretKey key = keyGen.generateKey();
        return key;
       // return new SecretKeySpec(keyValue, ALGO);
    }

}
