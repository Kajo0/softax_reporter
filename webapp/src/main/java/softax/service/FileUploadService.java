package softax.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softax.exception.FileNotFoundException;
import softax.model.FileToProcess;
import softax.util.TimeManager;

@Service
public class FileUploadService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StorageService storageService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private TimeManager timeManager;

    @Value("${files_path.status}")
    private String statusPath;
    @Value("${files_path.pdf}")
    private String pdfPath;

    public void uploadFile(MultipartFile file) {
        String filename = createFileName(file.getOriginalFilename());
        storageService.store(file, filename);
        log.info("File uploaded with name '{}'.", filename);

        try {
            Path statusFile = Paths.get(statusPath, filename + "_" + FileToProcess.Status.NEW);
            Files.write(statusFile, "".getBytes());
        } catch (IOException e) {
            log.error("IOException during create new file.", statusPath.toString());
        }

        jmsTemplate.convertAndSend("jmsQueue", filename);
    }

    public Path loadPdf(String filename) {
        Path file = storageService.load(Paths.get(pdfPath, filename + ".pdf").toString());
        if (!Files.exists(file)) {
            log.warn("File {}.pdf not found.", filename);
            throw new FileNotFoundException("File " + filename + ".pdf not found.");
        }

        return file;
    }

    public List<FileToProcess> loadProcessFiles() {
        List<FileToProcess> list = Lists.newArrayList();
        List<Path> paths = loadUploadedFiles();
        List<Path> statuses = loadStatuses();

        paths.sort((p1, p2) -> p2.getFileName().compareTo(p1.getFileName()));
        paths.forEach(path -> {
            String filename = path.getFileName().toString();
            FileToProcess file = new FileToProcess(filename, null, false);

            Optional<Path> status =
                    statuses.stream().filter(p -> p.getFileName().toString().startsWith(filename)).findFirst();
            if (status.isPresent()) {
                String strStatus = status.get().getFileName().toString().substring(filename.length() + 1);
                FileToProcess.Status s = FileToProcess.Status.valueOf(strStatus);
                file.setStatus(s);

                switch (s) {
                    case DONE:
                        file.setPdf(true);
                }

                list.add(file);
            } else {
                log.warn("No status for file: {}.", filename);
            }
        });

        return list;
    }

    private List<Path> loadUploadedFiles() {
        return storageService.loadAll(null).collect(Collectors.toList());
    }

    private List<Path> loadStatuses() {
        return storageService.loadAll(statusPath).collect(Collectors.toList());
    }

    private String createFileName(String originalFilename) {
        return timeManager.getCurrentDateTime().getTime() + "_" + originalFilename;
    }

}
