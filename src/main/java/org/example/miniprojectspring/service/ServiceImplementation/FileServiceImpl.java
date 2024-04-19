package org.example.miniprojectspring.service.ServiceImplementation;

import org.example.miniprojectspring.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private final Path path = Paths.get("src/main/resources/image-profiles");

    @Override
    public String saveFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        filename = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(filename);
        Files.copy(file.getInputStream(), path.resolve(filename));
        return filename;
    }

    @Override
    public Resource findFile(String filename) throws IOException {
        Path rootPath = Paths.get("src/main/resources/image-profiles/" + filename);
        return new ByteArrayResource(Files.readAllBytes(rootPath));
    }
}
