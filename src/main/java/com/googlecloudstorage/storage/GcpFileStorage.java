package com.googlecloudstorage.storage;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobTargetOption;
import com.google.cloud.storage.Storage.PredefinedAcl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Component
public class GcpFileStorage {

    private final Storage storage;
    private static final String BUCKET_NAME = "evoluumlabs";

    @Autowired
    public GcpFileStorage(Storage storage) {
        this.storage = storage;
    }

    public String upload(MultipartFile file) {
        try {
            BlobId blobId = BlobId.of(BUCKET_NAME, Objects.requireNonNull(file.getOriginalFilename()));
            storage.create(BlobInfo.newBuilder(blobId).build(), file.getBytes(),
                    BlobTargetOption.predefinedAcl(PredefinedAcl.PUBLIC_READ));
            return storage.getOptions().getHost().concat("/").concat(BUCKET_NAME)
                    .concat("/").concat(file.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException("Problemas ao tentar enviar o arquivo.", e);
        }
    }

}
