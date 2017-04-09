package softax.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void store(MultipartFile file);

    void store(MultipartFile file, Path path, String filename);

    void store(byte[] file, Path path, String filename);

    void deleteFile(Path path);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

}