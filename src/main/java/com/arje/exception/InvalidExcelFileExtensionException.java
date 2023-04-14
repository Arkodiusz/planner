package com.arje.exception;

import static com.arje.service.GeneratorService.XLSX;

public class InvalidExcelFileExtensionException extends RuntimeException {

    private static final String MESSAGE = "Selected plan is not " + XLSX;

    public InvalidExcelFileExtensionException() {
        super(MESSAGE);
    }
}
