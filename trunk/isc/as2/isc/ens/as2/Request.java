package isc.ens.as2;

import java.io.InputStream;

/**
 * oleo Date: May 12, 2010 Time: 5:48:26 PM
 */
public class Request {
      public String contentType = "application/xml";
      public String subject = "subject";
      //TODO change it: use different constructors, class or enum as type
      public String text = "payload";
      public String filename = null;
      public InputStream stream = null;


}
