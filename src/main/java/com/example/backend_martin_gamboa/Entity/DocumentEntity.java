package com.example.backend_martin_gamboa.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String docName;
    @Lob
    @Column(name = "content", columnDefinition = "LONGBLOB")
    private byte[] content;
    private Long userId;
    private Long loanId;
}
