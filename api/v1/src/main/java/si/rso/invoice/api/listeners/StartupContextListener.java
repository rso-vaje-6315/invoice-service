package si.rso.invoice.api.listeners;

import com.google.common.base.Charsets;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@ApplicationScoped
public class StartupContextListener implements ServletContextListener {
    
    public static final Logger LOG = LogManager.getLogger(StartupContextListener.class.getSimpleName());
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String credentials = ConfigurationUtil.getInstance()
            .get("google.storage.credentials")
            .orElseThrow(() -> new RuntimeException("Google credentials not set!"));
        
        LOG.info("Decoding credentials...");
        String decodedCredentials = new String(Base64.getDecoder().decode(credentials));
        LOG.info("Credentials decoded!");
        
        try {
            if (!Files.exists(Paths.get("credentials"))) {
                Files.createDirectory(Paths.get("credentials"));
            }
            LOG.info("Creating credentials file...");
            File file = new File("credentials/google-key.json");
            LOG.info("Saving file to location {}", file.getAbsolutePath());
            FileWriter fileWriter = new FileWriter(file, Charsets.UTF_8);
            fileWriter.write(decodedCredentials);
            fileWriter.close();
            LOG.info("Credentials file created!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    
    }
}
