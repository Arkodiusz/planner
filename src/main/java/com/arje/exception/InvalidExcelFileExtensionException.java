package com.arje.exception;

import java.io.IOException;

import static com.arje.service.GeneratorService.XLS;
import static com.arje.service.GeneratorService.XLSX;

public class InvalidExcelFileExtensionException extends IOException {

    private static final String MESSAGE = "Selected plan is not " + XLS + " or " + XLSX;

    public InvalidExcelFileExtensionException() {
        super(MESSAGE);
    }
}
