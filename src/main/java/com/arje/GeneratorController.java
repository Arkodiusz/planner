package com.arje;

import com.arje.service.GeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/generate")
public class GeneratorController {

    private final GeneratorService generatorService;

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] generatePdf(@RequestParam String xlsPath) {
        return generatorService.generatePdf(xlsPath.replace("*", "/"));
    }
}
