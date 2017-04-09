package softax.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import softax.exception.FileNotFoundException;
import softax.exception.StorageException;

@Service
public class FileSystemStorageService implements StorageService {

    private Path rootLocationPath;

    @Autowired
    public FileSystemStorageService(Environment env) throws IOException {
        String rootLocation = env.getProperty("files_path.root");
        rootLocationPath = Paths.get(rootLocation).toRealPath();
    }

    @Override
    public void store(MultipartFile file) {
        store(file, rootLocationPath, null);
    }

    @Override
    public void store(MultipartFile file, Path path, String filename) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            String name = file.getOriginalFilename();
            if (filename != null) {
                name = filename;
            }
            Files.copy(file.getInputStream(), path.resolve(name));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public void store(byte[] file, Path path, String filename) {
        try {
            if (file == null || file.length == 0) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            Files.write(path.resolve(filename), file);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public void deleteFile(Path path) {
        FileSystemUtils.deleteRecursively(path.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(rootLocationPath)
                    .filter(path -> !path.equals(rootLocationPath))
                    .map(path -> rootLocationPath.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocationPath.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + filename, e);
        }
    }

}