package com.project.pivotPath.Ai.controller;


import com.project.pivotPath.Ai.service.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ingestion")
@RequiredArgsConstructor
public class IngestionController {

    private final IngestionService ingestionService;

    private final VectorStore vectorStore;

    @PostMapping("/upload")
    public String uploadResume(@RequestParam("file") MultipartFile file){
        ingestionService.ingestPdf(file.getResource(),"RESUME");
        return "Resume '" + file.getOriginalFilename() + "' has been processed and stored in Aiven!";
    }

    @GetMapping("/admin/refresh-catalog")
    public String refreshCatalog() {
        ingestionService.ingestLocalCourseCatalog();
        return "Local catalog re-indexed!";
    }

}

