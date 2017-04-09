package softax.service;

import static softax.model.FileToProcess.Status.ERROR;
import static softax.model.FileToProcess.Status.IN_PROGRESS;
import static softax.model.FileToProcess.Status.NEW;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import softax.model.FileToProcess.Status;

@Component
public class JmsConsumer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StorageService storageService;

    @Value("${files_path.status}")
    private String statusPath;
    @Value("${files_path.pdf}")
    private String pdfPath;

    @JmsListener(destination = "jmsQueue", containerFactory = "jmsFactory")
    public void receiveMessage(String msg) {
        log.debug("Received path to process: {}.", msg);

        Path path = Paths.get(msg);
        renameFileStatus(path, NEW, IN_PROGRESS);

        try {
            processFile(path);
        } catch (Exception e) {
            log.error("Error during processing file {}.", msg);
            renameFileStatus(path, IN_PROGRESS, ERROR);
        }
    }

    private void processFile(Path path) {
    }

    private void renameFileStatus(Path path, Status oldStatus, Status newStatus) {
        try {
            storageService.rename(Paths.get(statusPath, path.getFileName().toString() + "_" + oldStatus),
                    path.getFileName().toString() + "_" + newStatus);
        } catch (IOException ex) {
            log.error("IOException during rename file.", ex);
        }
    }

}
