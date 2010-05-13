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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }
}
