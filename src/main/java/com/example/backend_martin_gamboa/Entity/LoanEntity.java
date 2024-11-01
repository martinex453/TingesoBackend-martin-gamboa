package com.example.backend_martin_gamboa.Entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "loan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private Long userId; //User requesting the loan
    private Integer loantype; //1: First home, 2: Second home, 3: commercial properties, 4: remodeling
    private Double capital;
    private Double monthFee;
    private Double interest; //Anual interest
    private Integer term; //term of the loan in years
    private Integer status;
    private Double propCost;
    private Double total;
    private Double monthQuote;
}
