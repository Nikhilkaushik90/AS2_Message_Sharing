package isc.ens.as2;

/**
 * oleo Date: May 12, 2010 Time: 5:53:45 PM
 */
public class Response {

     public String originalMessageId;
     public String receivedMdnId;
     public String text;
     public String disposition;
     public boolean isError = false;
     public String errorDescription;
     public Throwable exception;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        sb.append("originalId: ").append(originalMessageId).append('\n');
        sb.append("receivedId: ").append(receivedMdnId).append('\n');
        sb.append("disposition: ").append(disposition).append('\n');
        if (isError) {
            
            sb.append("errorDescription: ").append(errorDescription).append('\n');
        }
        sb.append("text: ").append(text).append('\n');

        return sb.toString();
    }

}
