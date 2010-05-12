package isc.ens.as2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openas2.Component;
import org.openas2.OpenAS2Exception;
import org.openas2.cert.CertificateFactory;
import org.openas2.cert.PKCS12CertificateFactory;
import org.openas2.message.AS2Message;
import org.openas2.message.Message;
import org.openas2.partner.AS2Partnership;
import org.openas2.partner.Partnership;
import org.openas2.partner.PartnershipFactory;
import org.openas2.partner.SecurePartnership;
import org.openas2.processor.sender.SenderModule;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EdiIntAs2Client {
   private static Log logger;
   //TODO do object
    //TODO extract interface
   public static Response sendSync(ConnectionSettings settings,Request request) {

        setLogger(settings);

        Response response = new Response();
        Message msg = null;
        try {

        Partnership partnership = buildPartnership(settings);

         msg = buildMessage(partnership,request);
         response.originalMessageId = msg.getMessageID();

        //logger.info("msgId to send: "+msg.getMessageID());

        PKCS12CertificateFactory cf = new PKCS12CertificateFactory();
        Map map = new HashMap();
        map.put(PKCS12CertificateFactory.PARAM_FILENAME,settings.p12FilePath);
        map.put(PKCS12CertificateFactory.PARAM_PASSWORD,settings.p12FilePassword);


        Session session = new Session();
        cf.init(session,map);
        session.setComponent(CertificateFactory.COMPID_CERTIFICATE_FACTORY,cf);

        Component pf = new SimplePartnershipFactory();
        session.setComponent(PartnershipFactory.COMPID_PARTNERSHIP_FACTORY,pf);

        Sender service = new Sender();
        service.init(session,null);

        //logger.info("sender is ready.");

        service.handle(SenderModule.DO_SEND,msg,null);



        }
        catch (Exception e) {
            logger.error(e.getMessage(),e);
            response.isError = true;
            response.exception = e;
            response.errorDescription =  e.getMessage();
        }
        finally {
              if (msg.getMDN()!=null) {
                   response.receivedMdnId = msg.getMDN().getMessageID();
                   response.text = msg.getMDN().getText();
                   response.disposition = msg.getMDN().getAttribute("DISPOSITION");
              }
        }

        logger.info(response);

        return response;
   }

    private static Message buildMessage(Partnership partnership,Request request) throws MessagingException, FileNotFoundException, OpenAS2Exception {

        Message msg = new AS2Message();

        msg.setContentType(request.contentType);
        msg.setSubject(request.subject);
        msg.setPartnership(partnership);
        msg.setMessageID(msg.generateMessageID());

        msg.setAttribute(AS2Partnership.PA_AS2_URL,partnership.getAttribute(AS2Partnership.PA_AS2_URL));
        msg.setAttribute(AS2Partnership.PID_AS2,partnership.getReceiverID(AS2Partnership.PID_AS2));
        msg.setAttribute(Partnership.PID_EMAIL,partnership.getSenderID(Partnership.PID_EMAIL));

        MimeBodyPart part;

        if (request.stream != null) part = new MimeBodyPart(request.stream);
            else if (request.filename != null) part = new MimeBodyPart(new FileInputStream(request.filename));
                else {
                    part = new MimeBodyPart();
                    part.setText(request.text);

        }

        msg.setData(part);

        return msg;
    }

    public Response sendAsync(ConnectionSettings settings,Request request) {
         throw new NotImplementedException();
       //Response response = null;
       //return response;
   }
   public Response processAsyncReply(ConnectionSettings settings,InputStream stream) {
       throw new NotImplementedException();
      // Response response = null;
      // return response;
   }

   private static void setLogger(ConnectionSettings settings) {
       System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
       System.setProperty("org.apache.commons.logging.simplelog.defaultlog",settings.logLevel);

       
       logger = LogFactory.getLog(EdiIntAs2Client.class.getSimpleName());

    }

   private static Partnership buildPartnership(ConnectionSettings settings) {

        Partnership partnership = new Partnership();

        partnership.setName(settings.partnershipName);

        partnership.setAttribute(AS2Partnership.PA_AS2_URL,settings.receiverAs2Url);
        partnership.setReceiverID(AS2Partnership.PID_AS2,settings.receiverAs2Id);
        partnership.setReceiverID(SecurePartnership.PID_X509_ALIAS,settings.receiverKeyAlias);

        partnership.setSenderID(AS2Partnership.PID_AS2,settings.senderAs2Id);
        partnership.setSenderID(SecurePartnership.PID_X509_ALIAS,settings.senderKeyAlias);
        partnership.setSenderID(Partnership.PID_EMAIL,settings.senderEmail);


        partnership.setAttribute(AS2Partnership.PA_AS2_MDN_OPTIONS,settings.mdnOptions);

        partnership.setAttribute(SecurePartnership.PA_ENCRYPT,settings.encrypt);
        partnership.setAttribute(SecurePartnership.PA_SIGN,settings.sign);
        partnership.setAttribute(Partnership.PA_PROTOCOL,"as2");
         //partnership.setAttribute(AS2Partnership.PA_AS2_MDN_TO,"http://localhost:10080");
        partnership.setAttribute(AS2Partnership.PA_AS2_RECEIPT_OPTION,null);

        partnership.setAttribute(AS2Partnership.PA_MESSAGEID,settings.format);

        return partnership;
   }

}
