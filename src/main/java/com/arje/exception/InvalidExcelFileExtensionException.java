package com.arje.exception;

import static com.arje.service.GeneratorService.XLS;
import static com.arje.service.GeneratorService.XLSX;

public class InvalidExcelFileExtensionException extends RuntimeException {

    private static final String MESSAGE = "Selected plan is not " + XLS + " or " + XLSX;

    public InvalidExcelFileExtensionException() {
        super(MESSAGE);
    }
}
