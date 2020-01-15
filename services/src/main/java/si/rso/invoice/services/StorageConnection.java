package si.rso.invoice.services;

import com.google.cloud.storage.*;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class StorageConnection {
    
    public static final Logger LOG = LogManager.getLogger(StorageConnection.class.getSimpleName());
    
    private Storage storage;
    
    private String BUCKET_NAME;
    
    private String API_URL;
    
    @PostConstruct
    private void init() {
        LOG.info("Searching for google credentials in {}", System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
        this.storage = StorageOptions.getDefaultInstance().getService();
        this.BUCKET_NAME = ConfigurationUtil.getInstance()
            .get("google.storage.bucket-name")
            .orElseThrow(() -> new RuntimeException("No configuration for Cloud Storage!"));
        this.API_URL = ConfigurationUtil.getInstance()
            .get("google.storage.url")
            .orElseThrow(() -> new RuntimeException("No configuration for Cloud Storage!"));
    }
    
    public String uploadFile(File file) throws IOException {
        String fileName = String.format("%d-%s", System.currentTimeMillis(), file.getName());
        
        Path uploadFile = Files.move(file.toPath(), file.toPath().resolveSibling(fileName));
        
        BlobId blobId = BlobId.of(this.BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/pdf").build();
        byte[] fileBytes = Files.readAllBytes(uploadFile);
        Blob blob = this.storage.create(blobInfo, fileBytes);
        
        return String.format("%s/%s/%s", this.API_URL, this.BUCKET_NAME, fileName);
    }
    
}
