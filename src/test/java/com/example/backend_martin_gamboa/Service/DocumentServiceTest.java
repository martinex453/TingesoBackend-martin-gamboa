package com.example.backend_martin_gamboa.Service;

import com.example.backend_martin_gamboa.Entity.DocumentEntity;
import com.example.backend_martin_gamboa.Repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DocumentServiceTest {

    @InjectMocks
    private DocumentService documentService;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private MultipartFile mockFile;

    private DocumentEntity documentEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        documentEntity = new DocumentEntity(1L, "testDocument.pdf", new byte[]{}, 1L, 1L);
    }

    @Test
    public void testConsultDocument_DocumentNotFound() {
        // Given
        Long userId = 1L;
        String docName = "nonExistentDocument.pdf"; // Nombre de documento que no existe
        List<DocumentEntity> documents = new ArrayList<>();
        documents.add(new DocumentEntity(1L, "existingDocument.pdf", new byte[]{}, userId, 1L)); // Documento existente diferente

        // When
        when(documentRepository.findByUserId(userId)).thenReturn(documents);

        DocumentEntity result = documentService.consultDocument(userId, docName);

        // Then
        assertNull(result); // Verificamos que el resultado sea null, ya que no se encontró el documento
    }

    @Test
    public void testUploadDocument_Success() throws IOException {
        //Given
        Long userId = 1L;
        Long loanId = 1L;

        // When
        when(mockFile.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(mockFile.getSize()).thenReturn(100L);
        when(documentRepository.save(any(DocumentEntity.class))).thenReturn(documentEntity);

        DocumentEntity result = documentService.uploadDocument(mockFile, userId, loanId, "testDocument.pdf");

        //Then
        assertNotNull(result);
        assertEquals("testDocument.pdf", result.getDocName());
        verify(documentRepository, times(1)).save(any(DocumentEntity.class));
    }

    @Test
    public void testUploadDocument_FileIsEmpty() throws IOException {
        //Given
        Long userId = 1L;
        Long loanId = 1L;

        // When
        when(mockFile.isEmpty()).thenReturn(true);

        DocumentEntity result = documentService.uploadDocument(mockFile, userId, loanId, "testDocument.pdf");

        // Then
        assertNull(result);
        verify(documentRepository, never()).save(any(DocumentEntity.class));
    }



    @Test
    public void testUploadDocument_FileIsTooLarge() {
        //Given
        Long userId = 1L;
        Long loanId = 1L;

        // When
        when(mockFile.getSize()).thenReturn(11 * 1024 * 1024L); // 11 MB

        Exception exception = assertThrows(IOException.class, () -> {
            documentService.uploadDocument(mockFile, userId, loanId, "testDocument.pdf");
        });

        // Then
        assertEquals("El archivo es demasiado grande. Tamaño máximo permitido es 10 MB.", exception.getMessage());
    }

    @Test
    public void testConsultDocument_Success() {
        // Given
        List<DocumentEntity> documents = new ArrayList<>();
        documents.add(documentEntity);

        // When
        when(documentRepository.findByUserId(1L)).thenReturn(documents);

        DocumentEntity result = documentService.consultDocument(1L, "testDocument.pdf");
        //Then
        assertNotNull(result);
        assertEquals("testDocument.pdf", result.getDocName());
    }

    @Test
    public void testConsultDocument_NotFound() {
        // Given
        List<DocumentEntity> documents = new ArrayList<>();
        when(documentRepository.findByUserId(1L)).thenReturn(documents);

        // When
        DocumentEntity result = documentService.consultDocument(1L, "testDocument.pdf");

        //Then
        assertNull(result);
    }

    @Test
    public void testConsultLoanDocuments_Success() {
        // Given
        Long loanId = 1L;
        List<DocumentEntity> documents = new ArrayList<>();
        documents.add(documentEntity);

        // When
        when(documentRepository.findByLoanId(1L)).thenReturn(documents);

        List<DocumentEntity> result = documentService.consultLoanDocuments(loanId);

        //Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteLoanDocuments_Success() {
        // Given
        Long loanId = 1L;
        List<DocumentEntity> documents = new ArrayList<>();
        documents.add(documentEntity);

        // When
        when(documentRepository.findByLoanId(1L)).thenReturn(documents);

        Boolean result = documentService.deleteLoanDocuments(loanId);

        //Then
        assertTrue(result);
        verify(documentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteLoanDocuments_NoDocumentsFound() {
        //Given
        Long loanId = 1L;
        List<DocumentEntity> documents = new ArrayList<>();

        // When
        when(documentRepository.findByLoanId(1L)).thenReturn(documents);

        Boolean result = documentService.deleteLoanDocuments(loanId);

        // Then
        assertTrue(result);
        verify(documentRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testDeleteLoanDocuments_ExceptionCaught() {
        // Given
        Long loanId = 1L;
        List<DocumentEntity> documents = new ArrayList<>();
        documents.add(documentEntity);

        // When
        when(documentRepository.findByLoanId(1L)).thenReturn(documents);
        doThrow(new RuntimeException("Error deleting document")).when(documentRepository).deleteById(1L);

        Boolean result = documentService.deleteLoanDocuments(loanId);

        //Then
        assertFalse(result);
    }
}
