package com.smartzie.payment.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

@Getter
@Setter
public class MultiPartFile implements MultipartFile {
    private File file;

    public MultiPartFile(String image) {
        try {
            String[] parts = image.split(",");
            String contentType = extractContentType(parts[0]);
            byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
            this.file = createTempFile(decodedBytes, contentType);
        } catch (IOException e) {
            throw new RuntimeException("Error while converting base64 data to MultipartFile", e);
        }
    }


    private String extractContentType(String prefix) {
        return prefix.split(";")[0].split(":")[1];
    }

    @Async
    public File createTempFile(byte[] data, String contentType) throws IOException {
        String suffix = "." + contentType.split("/")[1];
        File tempFile = File.createTempFile("Pembayaran-", "."+suffix);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(data);
        }
        return tempFile;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getOriginalFilename() {
        return file.getName();
    }

    @Override
    public String getContentType() {
        try {
            return Files.probeContentType(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error while extracting MIME type of file", e);
        }
    }

    @Override
    public boolean isEmpty() {
        return file.length() == 0;
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException();
    }
}
