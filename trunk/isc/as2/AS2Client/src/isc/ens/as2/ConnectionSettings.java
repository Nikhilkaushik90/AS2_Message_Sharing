package isc.ens.as2;

/**
 * oleo Date: May 12, 2010 Time: 5:16:57 PM
 */
public class ConnectionSettings {

    String logLevel = "fatal";
    String senderEmail = "email";
    String receiverAs2Id = "Reciever";
    String senderAs2Id = "Sender";
    String receiverKeyAlias = "trusted_key";
    String senderKeyAlias = "key_pair";
    String receiverAs2Url = "http://172.16.148.1:8080/as2/HttpReceiver";
    String partnershipName = "partnership name";
    String mdnOptions = "signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, sha1";
    String encrypt = "3des";
    String sign = "sha1";

    String p12FilePath;
    String p12FilePassword;

    String format = "Ensemble-$date.ddMMyyyyHHmmssZ$-$rand.1234$@$msg.sender.as2_id$_$msg.receiver.as2_id$";

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverAs2Id() {
        return receiverAs2Id;
    }

    public void setReceiverAs2Id(String receiverAs2Id) {
        this.receiverAs2Id = receiverAs2Id;
    }

    public String getSenderAs2Id() {
        return senderAs2Id;
    }

    public void setSenderAs2Id(String senderAs2Id) {
        this.senderAs2Id = senderAs2Id;
    }

    public String getReceiverKeyAlias() {
        return receiverKeyAlias;
    }

    public void setReceiverKeyAlias(String receiverKeyAlias) {
        this.receiverKeyAlias = receiverKeyAlias;
    }

    public String getSenderKeyAlias() {
        return senderKeyAlias;
    }

    public void setSenderKeyAlias(String senderKeyAlias) {
        this.senderKeyAlias = senderKeyAlias;
    }

    public String getReceiverAs2Url() {
        return receiverAs2Url;
    }

    public void setReceiverAs2Url(String receiverAs2Url) {
        this.receiverAs2Url = receiverAs2Url;
    }

    public String getPartnershipName() {
        return partnershipName;
    }

    public void setPartnershipName(String partnershipName) {
        this.partnershipName = partnershipName;
    }

    public String getMdnOptions() {
        return mdnOptions;
    }

    public void setMdnOptions(String mdnOptions) {
        this.mdnOptions = mdnOptions;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getP12FilePath() {
        return p12FilePath;
    }

    public void setP12FilePath(String p12FilePath) {
        this.p12FilePath = p12FilePath;
    }

    public String getP12FilePassword() {
        return p12FilePassword;
    }

    public void setP12FilePassword(String p12FilePassword) {
        this.p12FilePassword = p12FilePassword;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }


}
