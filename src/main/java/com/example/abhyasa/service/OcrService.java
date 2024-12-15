package com.example.abhyasa.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class OcrService {

    private static final String TESSDATA_PATH = "C:\\Program Files\\Tesseract-OCR\\tessdata";

    public String extractTextFromImage(MultipartFile file) {
        File tempFile = null;

        try {
            File dir = new File(TESSDATA_PATH);
            if (dir.exists()) {
                System.out.println("Directory exists.");

                if (dir.isDirectory())
                    System.out.println("It's a valid directory.");

                // Save the uploaded file temporarily
                tempFile = File.createTempFile("uploaded_", "_" + file.getOriginalFilename());
                file.transferTo(tempFile);

                // Initialize Tesseract instance
                ITesseract tesseract = new Tesseract();
                tesseract.setDatapath(TESSDATA_PATH);
                tesseract.setLanguage("eng"); // Set the language to English

                // Extract text
                return tesseract.doOCR(tempFile);

            }
        } catch (TesseractException | IOException e) {
            e.printStackTrace();
            return "Error extracting text: " + e.getMessage();
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete(); // Clean up temporary file
            }
        }
        return null;
    }
}
