package com.arje.controller;

import com.arje.service.GeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/generate")
public class GeneratorController {

    private final GeneratorService generatorService;

    @PostMapping(value = "/pdf")
    @ResponseBody
    public ResponseEntity<?> generatePdf(@RequestParam("source") MultipartFile multipartFile) {
        try {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(generatorService.generatePdf(multipartFile));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}
