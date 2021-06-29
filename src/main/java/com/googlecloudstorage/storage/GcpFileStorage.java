package com.googlecloudstorage.storage;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

@Slf4j
@Component
public class GcpFileStorage {

    private final Storage storage;
    private static final String BUCKET_NAME = "evoluumlabs";
    private static final String FILE_PATH = "/home/bob/Desktop/";

    @Autowired
    public GcpFileStorage(Storage storage) {
        this.storage = storage;
    }

    public void upload(MultipartFile file) {
        try {
            BlobId blobId = BlobId.of(BUCKET_NAME, Objects.requireNonNull(file.getOriginalFilename()));
            storage.create(BlobInfo.newBuilder(blobId).build(), file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Problemas ao tentar enviar o arquivo.", e);
        }
    }

    public void download(String fileName) {
        try {
            Storage storage = StorageOptions.newBuilder().setProjectId(BUCKET_NAME).build().getService();
            Blob blob = storage.get(BlobId.of(BUCKET_NAME, fileName));
            OutputStream outStream = new FileOutputStream(FILE_PATH + fileName);
            byte[] content = blob.getContent();
            outStream.write(content);
            outStream.close();
            log.info("Downloaded object "+ fileName + " from bucket name " + BUCKET_NAME + " to " + FILE_PATH);
        } catch (Exception e) {
            throw new RuntimeException("Problemas ao tentar baixar o arquivo.", e);
        }
    }

}
