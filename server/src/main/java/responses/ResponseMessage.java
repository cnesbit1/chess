package responses;

public class ResponseMessage {
    private String message;
    private int gameID;

    public ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
