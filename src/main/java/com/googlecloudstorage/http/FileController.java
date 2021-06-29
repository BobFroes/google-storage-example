package com.googlecloudstorage.http;

import com.googlecloudstorage.storage.GcpFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    private final GcpFileStorage gcpFileStorage;

    @Autowired
    public FileController(GcpFileStorage gcpFileStorage) {
        this.gcpFileStorage = gcpFileStorage;
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> upload(@RequestParam MultipartFile file) {
        gcpFileStorage.upload(file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Void> download(@PathVariable String fileName) {
        gcpFileStorage.download(fileName);
        return ResponseEntity.ok().build();
    }

}