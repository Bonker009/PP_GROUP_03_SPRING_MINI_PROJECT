package org.example.miniprojectspring.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {
    private String filename;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
}
