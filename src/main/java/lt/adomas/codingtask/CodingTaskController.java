package lt.adomas.codingtask;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.*;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/coding-task")
public class CodingTaskController {
    private final CodingTaskService service;

    public CodingTaskController(CodingTaskService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/word-frequencies", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> calculateWordFrequencies(
            @RequestPart(value = "file", required = true ) List<MultipartFile> files) throws IOException {

        StringBuilder content = new StringBuilder();
        for(var file: files){
            content.append(new String(file.getBytes()));
        }

        Resource resource = new ByteArrayResource(service.resultToZip(content.toString()));
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_DISPOSITION, "attachment; filename=\"WordFrequencyResult.zip\"");

        return   ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .headers(headers)
                .body(resource);
    }
}
