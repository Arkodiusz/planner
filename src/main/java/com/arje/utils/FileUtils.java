package com.arje.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public static String getStringFromFile(String pathToFile) {
        try {
            File file = new File(pathToFile);
            return org.apache.commons.io.FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("ERROR! Couldn't load " + pathToFile);
        }
    }

    public static String getPathWithDifferentExtension(String sourcePath, String extension) {
        return sourcePath.substring(0, sourcePath.lastIndexOf(".")) + extension;
    }
}
