package com.example.dsap1.Controllers.Utils;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;

@Component
@Lazy
public class TikaTextExtractor {

    public String extractTextWithOCR(File file) throws Exception {
        // OCR конфигурация
        TesseractOCRConfig ocrConfig = new TesseractOCRConfig();
        ocrConfig.setLanguage("rus+eng");

        // PDF конфигурация (также влияет на DOCX)
        PDFParserConfig pdfConfig = new PDFParserConfig();
        pdfConfig.setExtractInlineImages(true); // обязательный флаг

        // Контекст парсинга
        ParseContext parseContext = new ParseContext();
        parseContext.set(TesseractOCRConfig.class, ocrConfig);
        parseContext.set(PDFParserConfig.class, pdfConfig);

        // Обработка
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();

        AutoDetectParser parser = new AutoDetectParser();
        parser.parse(new FileInputStream(file), handler, metadata, parseContext);

        return handler.toString();
    }
}

