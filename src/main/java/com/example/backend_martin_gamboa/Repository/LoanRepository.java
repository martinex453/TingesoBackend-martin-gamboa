package com.example.backend_martin_gamboa.Repository;

import com.example.backend_martin_gamboa.Entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
    public List<LoanEntity> findByUserId(long userId);
    public List<LoanEntity> findByCapital(Double capital);
    List<LoanEntity> findByTerm(Integer term);
}
