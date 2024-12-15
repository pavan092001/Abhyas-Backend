package com.example.abhyasa.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FileUtils {



    public static MultipartFile copyMultipartFile(MultipartFile originalFile) throws IOException {
        byte[] bytes = originalFile.getBytes(); // Read the file bytes

        // Create a new MultipartFile from the bytes
        MultipartFile copiedFile = new MultipartFile() {
            @Override
            public String getName() {
                return originalFile.getName();
            }

            @Override
            public String getOriginalFilename() {
                return originalFile.getOriginalFilename();
            }

            @Override
            public String getContentType() {
                return originalFile.getContentType();
            }

            @Override
            public boolean isEmpty() {
                return originalFile.isEmpty();
            }

            @Override
            public long getSize() {
                return bytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return bytes;
            }

            @Override
            public java.io.InputStream getInputStream() throws IOException {
                return new java.io.ByteArrayInputStream(bytes);
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                // Optionally implement if you want to save the file to disk
            }
        };

        return copiedFile; // Return the new copy of the MultipartFile
    }



}
