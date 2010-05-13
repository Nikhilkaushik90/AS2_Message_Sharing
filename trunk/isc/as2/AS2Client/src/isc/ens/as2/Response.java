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

    public String getOriginalMessageId() {
        return originalMessageId;
    }

    public void setOriginalMessageId(String originalMessageId) {
        this.originalMessageId = originalMessageId;
    }

    public String getReceivedMdnId() {
        return receivedMdnId;
    }

    public void setReceivedMdnId(String receivedMdnId) {
        this.receivedMdnId = receivedMdnId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
