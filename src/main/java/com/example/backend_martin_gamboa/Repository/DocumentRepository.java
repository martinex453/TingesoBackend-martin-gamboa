package com.example.backend_martin_gamboa.Repository;

import com.example.backend_martin_gamboa.Entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    public List<DocumentEntity> findByUserId(Long UserId);
    public List<DocumentEntity> findByLoanId(Long LoanId);
}
