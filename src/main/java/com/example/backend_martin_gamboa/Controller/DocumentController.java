package com.example.backend_martin_gamboa.Controller;

import com.example.backend_martin_gamboa.Entity.DocumentEntity;
import com.example.backend_martin_gamboa.Service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/document")
@CrossOrigin("*")
public class DocumentController {
    @Autowired
    DocumentService documentService;
    @PostMapping("/")
    public ResponseEntity<DocumentEntity> addDocument(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("userId") Long userId,
                                                      @RequestParam("loanId") Long loanId,
                                                      @RequestParam("docName") String docName) throws IOException {
        DocumentEntity doc = documentService.uploadDocument(file, userId, loanId, docName);
        return ResponseEntity.ok(doc);
    }

    @GetMapping("/{userId}/{docName}")
    public ResponseEntity<byte[]> consultDoc(@PathVariable("userId") Long userId, @PathVariable("docName") String docName) {
        DocumentEntity doc = documentService.consultDocument(userId, docName);
        if (doc == null) {
            return ResponseEntity.notFound().build(); // If don't find the document, return a 404 error
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getDocName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(doc.getContent()); // Return the content in byte[]

    }

    @GetMapping("/loanDocs/{loanId}")
    public ResponseEntity<byte[]> loanDocs(@PathVariable("loanId") Long loanId) {
        List<DocumentEntity> docs = documentService.consultLoanDocuments(loanId);

        if (docs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Create a ZIP in memory
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(baos)) {

            for (DocumentEntity doc : docs) {
                // Create an enter in the ZIP for all documents
                ZipEntry zipEntry = new ZipEntry(doc.getDocName());
                zipOut.putNextEntry(zipEntry);
                zipOut.write(doc.getContent()); // Agregar el contenido del archivo al zip
                zipOut.closeEntry();
            }

            zipOut.finish(); // Finish the ZIP

            // Preparing the response ZIP
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"loanDocuments_" + loanId + ".zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(baos.toByteArray());

        } catch (IOException e) {
            // Exception in case of error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @DeleteMapping("/deleteLoanDocs/{loanId}")
    public ResponseEntity<Boolean> deleteLoanDocs(@PathVariable("loanId") Long loanId) {
        var deleted  = documentService.deleteLoanDocuments(loanId);
        return ResponseEntity.ok(deleted);
    }

}
