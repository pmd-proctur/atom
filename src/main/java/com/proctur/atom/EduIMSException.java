package com.proctur.atom;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public final class EduIMSException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 8508650234160135813L;
    int                       statusCode       = 0;
    String                    displayMessage;
    private String[]          arguments;

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public EduIMSException() {
        super();
    }

    public EduIMSException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EduIMSException(final String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public EduIMSException(final String message, int statusCode, String displayMessage) {
        super(message);
        this.statusCode = statusCode;
        this.displayMessage = displayMessage;
    }

    public EduIMSException(final String message, int statusCode, String displayMessage, String... args) {
        super(message);
        this.statusCode = statusCode;
        this.displayMessage = displayMessage;
        this.arguments = args;
    }

    public EduIMSException(final Throwable cause) {
        super(cause);
    }

    public EduIMSException(String message) {
        super(message);
    }

}

