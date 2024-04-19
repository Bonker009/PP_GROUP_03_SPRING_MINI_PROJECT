package org.example.miniprojectspring.controller;

import lombok.AllArgsConstructor;
import org.example.miniprojectspring.model.response.ApiResponse;
import org.example.miniprojectspring.model.response.FileResponse;
import org.example.miniprojectspring.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/files/")
@AllArgsConstructor
public class AppFileController {
    private final FileService fileService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "upload")
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file) throws IOException {
        String filename = fileService.saveFile(file);
        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(filename).toUriString();
        FileResponse fileResponse = new FileResponse(filename,
                fileUrl
                , file.getContentType()
                , file.getSize());
        ApiResponse<FileResponse> apiResponse = ApiResponse.<FileResponse>builder()
                .status(HttpStatus.CREATED)
                .code(201)
                .message("File uploaded successfully")
                .payload(fileResponse)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("file")
    public ResponseEntity<?> getFile(@RequestParam String filename) throws IOException {
        Resource resource = fileService.findFile(filename);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
    }

}
