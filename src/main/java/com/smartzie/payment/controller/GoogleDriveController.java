package com.smartzie.payment.controller;

import com.smartzie.payment.service.GoogleDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
//@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/drive")
public class GoogleDriveController {
    @Autowired
    private GoogleDriveService service;

    @PostMapping("getAll")
    public String getAllAudio() throws IOException, GeneralSecurityException {
        return service.getfiles();
    }

    @PostMapping("set")
    public String uploadAudio(@RequestParam("file") MultipartFile file) throws IOException, GeneralSecurityException{
        System.out.println(file.getOriginalFilename());
        return service.uploadFile(file);
    }
}
