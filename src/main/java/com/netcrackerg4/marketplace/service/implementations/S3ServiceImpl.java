package com.netcrackerg4.marketplace.service.implementations;

import com.netcrackerg4.marketplace.repository.interfaces.IS3Dao;
import com.netcrackerg4.marketplace.service.interfaces.IS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements IS3Service {
    private final IS3Dao s3Dao;

    @Override
    public URL uploadImage(UUID nameId, MultipartFile multipartFile) {
        String tempDir = getTempDirPath();
        String extension = this.getFileExtension(multipartFile)
                .orElseThrow(() -> {throw new IllegalStateException("File has no extension");});
        if(!extension.equalsIgnoreCase("png"))
            throw new IllegalStateException("Only .png images are permitted");
        String newFileName = getNewFileName(nameId, extension);
        File file = new File(tempDir+ newFileName);
        try {
            file.createNewFile();
            multipartFile.transferTo(file);
            return s3Dao.uploadImage(file);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if (file.exists()) file.delete();
        }
    }

    private String getTempDirPath() {
        String tempDir = System.getProperty("user.dir") + "/temp/img/";
        File directoryFile = new File(tempDir);
        if(!directoryFile.exists()) {
            directoryFile.mkdirs();
        }
        return tempDir;
    }

    private String getNewFileName(UUID name, String extension) {
        return  name + "." + extension;
    }

    private Optional<String> getFileExtension(MultipartFile file) {
        var name = file.getOriginalFilename();
        return Optional.ofNullable(name)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(name.lastIndexOf(".") + 1));
    }
}
