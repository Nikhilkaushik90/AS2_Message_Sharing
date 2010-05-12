package isc.ens.as2;

/**
 * oleo Date: May 12, 2010 Time: 5:16:57 PM
 */
public class ConnectionSettings {

      public String logLevel = "fatal";
      public String senderEmail = "email";
      public String receiverAs2Id = "Reciever";
      public String senderAs2Id = "Sender";
      public String receiverKeyAlias = "trusted_key";
      public String senderKeyAlias = "key_pair";
      public String receiverAs2Url = "http://172.16.148.1:8080/as2/HttpReceiver";
      public String partnershipName = "partnership name";
      public String mdnOptions =  "signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, sha1";
      public String encrypt = "3des";
      public String sign = "sha1";

      public String p12FilePath;
      public String p12FilePassword;

      public String format = "Ensemble-$date.ddMMyyyyHHmmssZ$-$rand.1234$@$msg.sender.as2_id$_$msg.receiver.as2_id$";


     
}
