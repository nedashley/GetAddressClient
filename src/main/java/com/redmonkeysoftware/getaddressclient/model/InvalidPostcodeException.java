package com.redmonkeysoftware.getaddressclient.model;

public class InvalidPostcodeException extends Exception {

    private static final long serialVersionUID = -8829676873712224079L;

    public InvalidPostcodeException(String postcode) {
        super("Postcode: " + postcode + " does not appear to be a valid postcode.");
    }
}
