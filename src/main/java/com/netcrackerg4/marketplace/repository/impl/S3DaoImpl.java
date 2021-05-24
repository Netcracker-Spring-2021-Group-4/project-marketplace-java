package com.netcrackerg4.marketplace.repository.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.netcrackerg4.marketplace.repository.interfaces.IS3Dao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.net.URL;

@Repository
@RequiredArgsConstructor
public class S3DaoImpl implements IS3Dao {
    @Value("${custom.aws.bucket}")
    private String BUCKET_NAME;
    private final AmazonS3Client amazonS3Client;

    @Override
    public URL uploadImage(File image) {
        String fileName =  image.getName();
        amazonS3Client.putObject(
                this.BUCKET_NAME,
                fileName,
                image);
        amazonS3Client.setObjectAcl(
                this.BUCKET_NAME,
                fileName,
                CannedAccessControlList.PublicRead);
        return amazonS3Client.getUrl(this.BUCKET_NAME, fileName);
    }
}
