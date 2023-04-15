package com.arje.controller;

import com.arje.service.GeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class PageController {

    private static final String HOME = "/";
    public static final String _PDF = ".pdf";

    private final GeneratorService service;

    private String errorMessage = "";

    @RequestMapping(HOME)
    public String index(Model model) {
        model.addAttribute("errorMessage", errorMessage);
        errorMessage = "";
        return "index";
    }

    @PostMapping("/generate")
    public void generatePdf(
            @RequestParam("file") MultipartFile sourceFile,
            HttpServletResponse response) throws IOException {

        try {
            byte[] generatedPdf = service.generatePdf(sourceFile);
            if(isValidFile(generatedPdf)) {
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition",
                        "attachment; filename = " + getFileNameForPdf(sourceFile));
                //use 'inline' to open file or 'attachment' to force download

                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(generatedPdf);
                outputStream.close();
            }
            response.sendRedirect(HOME);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorMessage = e.getMessage();
            response.sendRedirect(HOME);
        }
    }

    private static boolean isValidFile(byte[] generatedPdf) {
        return generatedPdf != null && generatedPdf.length > 0;
    }

    private String getFileNameForPdf(MultipartFile sourceFile) {
        return Objects.requireNonNull(sourceFile.getOriginalFilename()).split("\\.")[0] + _PDF;
    }
}