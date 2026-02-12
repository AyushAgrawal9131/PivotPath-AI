package com.project.pivotPath.Ai.service;

import org.springframework.core.io.Resource;

public interface IngestionService {

    public void ingestPdf(Resource pdfResource, String category);

    public void ingestLocalCourseCatalog();
}
