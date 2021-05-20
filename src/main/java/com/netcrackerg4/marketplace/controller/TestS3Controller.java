package com.netcrackerg4.marketplace.controller;

import com.netcrackerg4.marketplace.service.interfaces.IS3Service;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/public/s3")
@RequiredArgsConstructor
public class TestS3Controller {
    private final IS3Service s3Service;

    @PostMapping("/upload")
    URL uploadImage(@RequestParam("file") MultipartFile multipartFile) {
        UUID id = UUID.randomUUID();
        return s3Service.uploadImage(id, multipartFile);
    }

    @PostMapping("/upload/{id}")
    URL uploadImage(@PathVariable("id") UUID id, @RequestParam("file") MultipartFile multipartFile) {
        return s3Service.uploadImage(id, multipartFile);
    }
}
