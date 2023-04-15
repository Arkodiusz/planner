package com.arje.exception;

public class InvalidGeneratedPdfException extends RuntimeException {

    private static final String MESSAGE = "Generated PDF file is invalid (empty or null)";

    public InvalidGeneratedPdfException() {
        super(MESSAGE);
    }
}
