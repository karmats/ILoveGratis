package net.karmats.ilovegratis.exception;

public class AdNotUploadedException extends Exception {

    private static final long serialVersionUID = 1L;

    private int errorCode;

    public AdNotUploadedException(String description, int errorCode) {
        super(description);
        this.errorCode = errorCode;
    }

    public AdNotUploadedException(Throwable cause) {
        super(cause);
    }

    public AdNotUploadedException(String description, Throwable cause) {
        super(description, cause);
    }

    public int getErrorCode() {
        return errorCode;
    }

}
