package isc.ens.as2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openas2.Component;
import org.openas2.OpenAS2Exception;
import org.openas2.cert.CertificateFactory;
import org.openas2.cert.PKCS12CertificateFactory;
import org.openas2.message.AS2Message;
import org.openas2.message.Message;
import org.openas2.message.MessageMDN;
import org.openas2.params.InvalidParameterException;
import org.openas2.partner.AS2Partnership;
import org.openas2.partner.Partnership;
import org.openas2.partner.PartnershipFactory;
import org.openas2.partner.SecurePartnership;
import org.openas2.processor.sender.SenderModule;
import org.openas2.util.DateUtil;

import javax.mail.Header;
import javax.mail.internet.MimeBodyPart;
import java.net.HttpURLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * oleo Date: May 4, 2010 Time: 6:56:31 PM
 */
public class TestClient {
    //Message msg = new AS2Message();
    //  getSession().getProcessor().handle(SenderModule.DO_SEND, msg, null);

    private static Log logger = LogFactory.getLog("default");

    public static void main(String[] args) {
        ConnectionSettings settings = new ConnectionSettings();
      
        settings.logLevel = "info";
        settings.p12FilePassword = "test";
        settings.p12FilePath = "/Users/oleo/samples/parfum.spb.ru/as2/mendelson/certificates.p12";
        settings.senderEmail = "email";
        settings.receiverAs2Id = "GWTESTFM2i";
        settings.senderAs2Id = "Sender";
        settings.receiverKeyAlias = "rg_trusted";
        settings.senderKeyAlias = "rg";
        settings.receiverAs2Url = "http://172.16.148.1:8080/as2/HttpReceiver";
        settings.partnershipName = "partnership name";
        settings.mdnOptions =  "signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, sha1";
        settings.encrypt = "3des";
        settings.sign = "sha1";

        Request request = new Request();
        request.subject = "Test message";
        request.text = "Some info to you";

        Response response = EdiIntAs2Client.sendSync(settings,request);
        

    }
    public static void main2(String[] args) {

        // Received-content-MIC
        //original-message-id
        setLogger();
        //
        String pidSenderEmail = "email";
        String pidAs2 = "GWTESTFM2i";
        String pidSenderAs2 = "Sender";
        String recieverKey = "rg_trusted";//"gwtestfm2i_trusted"; //
        String senderKey = "rg";
        String paAs2Url = "http://172.16.148.1:8080/as2/HttpReceiver";



       // AS2SenderModule service = new AS2SenderModule();
         Sender service = new Sender();


        Partnership partnership = new Partnership();
        partnership.setName("partnership name");
        partnership.setAttribute(AS2Partnership.PA_AS2_URL,paAs2Url);
        partnership.setReceiverID(AS2Partnership.PID_AS2,pidAs2);
        partnership.setReceiverID(SecurePartnership.PID_X509_ALIAS,recieverKey);
        partnership.setSenderID(AS2Partnership.PID_AS2,pidSenderAs2);
        partnership.setSenderID(SecurePartnership.PID_X509_ALIAS,senderKey);


        partnership.setSenderID(Partnership.PID_EMAIL,pidSenderEmail);

        //partnership.setAttribute(AS2Partnership.PA_AS2_MDN_TO,"http://localhost:10080");
        partnership.setAttribute(AS2Partnership.PA_AS2_MDN_OPTIONS,"signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, sha1");

        partnership.setAttribute(SecurePartnership.PA_ENCRYPT,"3des");
        partnership.setAttribute(SecurePartnership.PA_SIGN,"sha1");
        partnership.setAttribute(Partnership.PA_PROTOCOL,"as2");
        
        partnership.setAttribute(AS2Partnership.PA_AS2_RECEIPT_OPTION,null);

        logger.info("ALIAS: " + partnership.getSenderID(SecurePartnership.PID_X509_ALIAS));

        Message msg = new AS2Message();
        msg.setContentType("application/xml");
        msg.setSubject("some subject");

        msg.setAttribute(AS2Partnership.PA_AS2_URL,paAs2Url);

        msg.setAttribute(AS2Partnership.PID_AS2,pidAs2);
        msg.setAttribute(Partnership.PID_EMAIL,"email");
        try {
            MimeBodyPart part;
            //part = new MimeBodyPart(new FileInputStream("/tmp/tst"));
            part = new MimeBodyPart();

            part.setText("some text from mme part");
            //part.setFileName("/");
            msg.setData(part);
        } catch (Exception e) {
            e.printStackTrace();
        }

        msg.setPartnership(partnership);
        msg.setMessageID(msg.generateMessageID());
        logger.info("msg id: "+msg.getMessageID());
    
        Session session = null;
        try {
            session = new Session();
            PKCS12CertificateFactory cf = new PKCS12CertificateFactory();
            /*
            filename="%home%/certs.p12"
		password="test"
		interval="300"
             */
            //String filename = "/Users/oleo/samples/parfum.spb.ru/as2/openas2/config/certs.p12";
           String filename = "/Users/oleo/samples/parfum.spb.ru/as2/mendelson/certificates.p12";
            //String filename = "/Users/oleo/samples/parfum.spb.ru/as2/test/test.p12";
            String password = "test";
            // gwtestfm2i
            // /Users/oleo/Downloads/portecle-1.5.zip
     
            ///Users/oleo/samples/parfum.spb.ru/as2/test/test.p12

            Map map = new HashMap();
            map.put(PKCS12CertificateFactory.PARAM_FILENAME,filename);
            map.put(PKCS12CertificateFactory.PARAM_PASSWORD,password);

            cf.init(session,map);

           // logger.info(cf.getCertificate(msg.getMDN(), Partnership.PTYPE_SENDER));


            //logger.info(cf.getCertificates());


            session.setComponent(CertificateFactory.COMPID_CERTIFICATE_FACTORY,cf);
             Component pf = new SimplePartnershipFactory();
            session.setComponent(PartnershipFactory.COMPID_PARTNERSHIP_FACTORY,pf);
            service.init(session,null);
        } catch (OpenAS2Exception e) {
            e.printStackTrace();
        }







        logger.info("is requesting  MDN?: "+msg.isRequestingMDN());
        logger.info("is async MDN?: "+msg.isRequestingAsynchMDN());
        logger.info("is rule to recieve MDN active?: "+msg.getPartnership().getAttribute(AS2Partnership.PA_AS2_RECEIPT_OPTION));


        try {
            service.handle(SenderModule.DO_SEND,msg,null);
            logger.info("MDN is "+msg.getMDN().toString());

            logger.info("message sent"+msg.getLoggingText());

            MessageMDN reply = msg.getMDN();

            Enumeration list = reply.getHeaders().getAllHeaders();
            StringBuilder sb = new StringBuilder("MDN headers:\n");
            while (list.hasMoreElements() ) {

                Header h =  (Header)list.nextElement();
                sb.append(h.getName()).append(" = ").append(h.getValue()).append('\n');

            }

            //logger.info(sb);



            Enumeration list2 = reply.getData().getAllHeaders();
            StringBuilder sb2 = new StringBuilder("Mime headers:\n");
            while (list2.hasMoreElements() ) {

                Header h =  (Header)list2.nextElement();
                sb2.append(h.getName()).append(" = ").append(h.getValue()).append('\n');

            }

            //logger.info(sb2);

            //logger.info(reply.getData().getRawInputStream().toString());




        } catch (Exception e) {
            logger.error("shit happens");
            e.printStackTrace();

        }





        


    }

    private static void setLogger() {
        System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.defaultlog","error");
        logger = LogFactory.getLog("default");
        logger.info(logger);
    }

    protected static void checkRequired(Message msg)  {
        Partnership partnership = msg.getPartnership();

        try {
            InvalidParameterException.checkValue(msg, "ContentType", msg.getContentType());
            InvalidParameterException.checkValue(msg, "Attribute: " + AS2Partnership.PA_AS2_URL, partnership
                    .getAttribute(AS2Partnership.PA_AS2_URL));
            InvalidParameterException.checkValue(msg, "Receiver: " + AS2Partnership.PID_AS2, partnership
                    .getReceiverID(AS2Partnership.PID_AS2));
            InvalidParameterException.checkValue(msg, "Sender: " + AS2Partnership.PID_AS2, partnership
                    .getSenderID(AS2Partnership.PID_AS2));
            InvalidParameterException.checkValue(msg, "Subject", msg.getSubject());
            InvalidParameterException.checkValue(msg, "Sender: " + Partnership.PID_EMAIL, partnership
                    .getSenderID(Partnership.PID_EMAIL));
            InvalidParameterException.checkValue(msg, "Message Data", msg.getData());
        } catch (InvalidParameterException rpe) {
            rpe.addSource(OpenAS2Exception.SOURCE_MESSAGE, msg);

        }
    }

    protected void updateHttpHeaders(HttpURLConnection conn, Message msg) {
        Partnership partnership = msg.getPartnership();

        conn.setRequestProperty("Connection", "close, TE");
        conn.setRequestProperty("User-Agent", "OpenAS2 AS2Sender");

        conn.setRequestProperty("Date", DateUtil.formatDate("EEE, dd MMM yyyy HH:mm:ss Z"));
        conn.setRequestProperty("Message-ID", msg.getMessageID());
        conn.setRequestProperty("Mime-Version", "1.0"); // make sure this is the encoding used in the msg, run TBF1
        conn.setRequestProperty("Content-type", msg.getContentType());
        conn.setRequestProperty("AS2-Version", "1.1");
        conn.setRequestProperty("Recipient-Address", partnership.getAttribute(AS2Partnership.PA_AS2_URL));
        conn.setRequestProperty("AS2-To", partnership.getReceiverID(AS2Partnership.PID_AS2));
        conn.setRequestProperty("AS2-From", partnership.getSenderID(AS2Partnership.PID_AS2));
        conn.setRequestProperty("Subject", msg.getSubject());
        conn.setRequestProperty("From", partnership.getSenderID(Partnership.PID_EMAIL));

        String dispTo = partnership.getAttribute(AS2Partnership.PA_AS2_MDN_TO);

        if (dispTo != null) {
            conn.setRequestProperty("Disposition-Notification-To", dispTo);
        }

        String dispOptions = partnership.getAttribute(AS2Partnership.PA_AS2_MDN_OPTIONS);

        if (dispOptions != null) {
            conn.setRequestProperty("Disposition-Notification-Options", dispOptions);
        }

        //Asynch MDN 2007-03-12
        String receiptOption = partnership.getAttribute(AS2Partnership.PA_AS2_RECEIPT_OPTION);
        if (receiptOption != null) {
            conn.setRequestProperty("Receipt-delivery-option", receiptOption);
        }

        //As of 2007-06-01

        String contentDisp = msg.getContentDisposition();
        if (contentDisp != null) {
        	conn.setRequestProperty("Content-Disposition", contentDisp);
        }

    }

                           
            /*
        <partnerships>
	<partner name="OpenAS2A"
		as2_id="OpenAS2A"
		x509_alias="OpenAS2A"
		email="OpenAS2 A email"/>
	<partner name="OpenAS2B"
		as2_id="OpenAS2B"
		x509_alias="OpenAS2B"
		email="OpenAS2 A email"/>

	<partnership name="OpenAS2A-OpenAS2B">
		<sender name="OpenAS2A"/>
		<receiver name="OpenAS2B"/>
		<attribute name="protocol" value="as2"/>
		<attribute name="subject" value="From OpenAS2A to OpenAS2B"/>
		<attribute name="as2_url" value="http://localhost:10080"/>
		<attribute name="as2_mdn_to" value="http://localhost:10080"/>
		<attribute name="as2_mdn_options" value="signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, sha1"/>
		<attribute name="encrypt" value="3des"/>
		<attribute name="sign" value="md5"/>
	</partnership>

	<partnership name="OpenAS2B-OpenAS2A">
		<sender name="OpenAS2B"/>
		<receiver name="OpenAS2A"/>
		<attribute name="protocol" value="as2"/>
		<attribute name="subject" value="From OpenAS2B to OpenAS2A"/>
		<attribute name="as2_url" value="http://localhost:10080"/>
		<attribute name="as2_mdn_to" value="http://localhost:10080"/>
		<attribute name="as2_mdn_options" value="signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, sha1"/>
		<attribute name="encrypt" value="3des"/>
		<attribute name="sign" value="sha1"/>
	</partnership>
</partnerships>
         */
    
}
