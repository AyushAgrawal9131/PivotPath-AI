package com.project.pivotPath.Ai.service.impl;


import com.project.pivotPath.Ai.service.IngestionService;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngestionServiceImpl implements IngestionService {

   @Autowired
   private VectorStore vectorStore;

    @Autowired
    private ResourceLoader resourceLoader;
    @Override
    public void ingestPdf(Resource pdfResource, String category) {
        PagePdfDocumentReader reader = new PagePdfDocumentReader(pdfResource,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageBottomMargin(0)
                        .build());

        List<Document> documents = reader.get();

        // 2. TRANSFORM: Chunking is still necessary so the AI can handle the text
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> chunks = splitter.apply(documents);

        // 3. ENRICH: Add your metadata
        chunks.forEach(doc -> {
            doc.getMetadata().put("category", category);
            doc.getMetadata().put("source", (pdfResource).getFilename());
        });

        // 4. LOAD: Save to Aiven
        vectorStore.accept(chunks);
    }
    public void ingestLocalCourseCatalog() {
        try {
            Resource resource = resourceLoader.getResource("classpath:data/courses.json");

            // Pass "url" explicitly so it stays in Metadata
            JsonReader jsonReader = new JsonReader(resource, "title", "description", "url", "provider");
            List<Document> documents = jsonReader.get();

            documents.forEach(doc -> {
                doc.getMetadata().put("category", "COURSE");
            });

            vectorStore.accept(documents);
            System.out.println("Courses ingested with URL Metadata!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

