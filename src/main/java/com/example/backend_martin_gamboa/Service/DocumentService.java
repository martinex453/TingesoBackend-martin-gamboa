package com.example.backend_martin_gamboa.Service;

import com.example.backend_martin_gamboa.Entity.DocumentEntity;
import com.example.backend_martin_gamboa.Entity.UserEntity;
import com.example.backend_martin_gamboa.Repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentService {
    @Autowired
    DocumentRepository documentRepository;

    public DocumentEntity uploadDocument(MultipartFile file, Long userId, Long loanId, String docName) throws IOException {
        //Verify if file is empty
        if (file.isEmpty()) {
            return null;
        }
        //Verify if fily is bigger than threshold
        if (file.getSize() > 10 * 1024 * 1024) {  // 10 MB
            throw new IOException("El archivo es demasiado grande. Tamaño máximo permitido es 10 MB.");
        }
        //Generate the document
        DocumentEntity doc = new DocumentEntity();
        doc.setContent(file.getBytes());
        doc.setDocName(docName);
        doc.setUserId(userId);
        doc.setLoanId(loanId);
        return documentRepository.save(doc);
    }

    public DocumentEntity consultDocument(Long userId, String docName){
        List<DocumentEntity> docs = documentRepository.findByUserId(userId);
        for (DocumentEntity doc : docs) {
            if(doc.getDocName().equals(docName)){
                return doc;
            }
        }
        return null;
    }

    public List<DocumentEntity> consultLoanDocuments(Long loanId){
        List<DocumentEntity> docs = documentRepository.findByLoanId(loanId);
        return docs;
    }

    public Boolean deleteLoanDocuments(Long loanId){
        List<DocumentEntity> docs = documentRepository.findByLoanId(loanId);
        try{
            for (DocumentEntity doc : docs) {
                documentRepository.deleteById(doc.getId());
            }
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

}
