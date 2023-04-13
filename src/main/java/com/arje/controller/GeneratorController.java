package com.arje.controller;

import com.arje.service.GeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/generate")
public class GeneratorController {

    private final GeneratorService generatorService;

    @GetMapping(value = "/pdf")
    public ResponseEntity<?> generatePdf(@RequestParam String xlsPath) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(generatorService.generatePdf(xlsPath.replace("*", "/")));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
