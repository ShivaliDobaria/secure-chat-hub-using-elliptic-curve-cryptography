import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
public class keyValuesForCommand {

   public static void main(String args[]) {
}
      /* This is how to declare HashMap */
     HashMap<String, String> hmap = new HashMap<String, String>();

      /*Adding elements to HashMap*/
      public keyValuesForCommand(){
      hmap.put(":ka", "1.ecdh-secp256r1+x509+aes128/gcm128  2.ecdh-secp224r1+x509+aes128/gcm128");
      hmap.put(":kaok", "2.ecdh-secp224r1+nocert+aes128/cbc");
      hmap.put(":cert", "Certification verification");
      hmap.put(":ka1", "send the base64 encoded public key as the argument");
      hmap.put(":err", "Problem in key aggrement");
      hmap.put(":fail", "send message for non-recoverable error (reset conversation)");
    }

// }
    public  String getValue(String key) throws Exception {
    String var= hmap.get(key);
    return var;
    }
}
