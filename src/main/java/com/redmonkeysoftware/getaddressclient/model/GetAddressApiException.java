package com.redmonkeysoftware.getaddressclient.model;

public class GetAddressApiException extends Exception {

    private static final long serialVersionUID = -7359740864455531235L;

    public GetAddressApiException(String message) {
        super(message);
    }

    public GetAddressApiException(String message, Throwable exception) {
        super(message, exception);
    }
}
