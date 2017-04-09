package softax.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import softax.exception.FileNotFoundException;
import softax.service.StorageService;

@RestController
public class FileUploadController {

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public ResponseEntity<Resource> serveFile(@RequestParam("filename") String filename) throws IOException {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .contentLength(file.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        storageService.store(file);
        return "ok";
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound() {
        return ResponseEntity.notFound().build();
    }

}
