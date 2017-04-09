package softax.controller;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import softax.exception.FileNotFoundException;
import softax.exception.StorageException;
import softax.service.FileUploadService;
import softax.service.StorageService;

@Controller
public class FileUploadController {

    @Autowired
    private StorageService storageService;
    @Autowired
    private FileUploadService fileUploadService;

    @RequestMapping(value = "/download/{filename:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
        Path path = fileUploadService.loadPdf(filename);
        Resource file = storageService.loadAsResource(path.toString());
        return ResponseEntity.ok()
                .contentLength(file.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        fileUploadService.uploadFile(file);

        return "redirect:/";
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(StorageException.class)
    public String handleStorageException() {
        return "new_request";
    }

}
