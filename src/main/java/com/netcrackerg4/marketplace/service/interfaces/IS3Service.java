package com.netcrackerg4.marketplace.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.UUID;

public interface IS3Service {

    URL uploadImage(UUID nameId, MultipartFile image);
}
