package com.example.backend_martin_gamboa.Controller;

import com.example.backend_martin_gamboa.Entity.LoanEntity;
import com.example.backend_martin_gamboa.Service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan")
@CrossOrigin("*")
public class LoanController {
    @Autowired
    LoanService loanService;

    @GetMapping("/")
    public ResponseEntity<List<LoanEntity>> listLoans() {
        List<LoanEntity> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/loansForEvaluate")
    public ResponseEntity<List<LoanEntity>> listLoansForEvaluate() {
        List<LoanEntity> loans = loanService.getLoansForEvaluate();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/userLoans/{userId}")
    public ResponseEntity<List<LoanEntity>> listUserLoans(@PathVariable("userId") Long userId) {
        List<LoanEntity> loans = loanService.getUserLoans(userId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanEntity> getLoanById(@PathVariable("id") Long id) {
        LoanEntity loan = loanService.getLoanById(id);
        return ResponseEntity.ok(loan);
    }

    @GetMapping("capital/{capital}")
    public ResponseEntity<List<LoanEntity>> getLoanByCapital(@PathVariable("capital") Double capital) {
        List<LoanEntity> loans = loanService.getLoanByCapital(capital);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("userId/{userId}")
    public ResponseEntity<List<LoanEntity>> getLoanByUserId(@PathVariable("userId") Long userId) {
        List<LoanEntity> loans = loanService.getLoanByUserId(userId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("term/{term}")
    public ResponseEntity<List<LoanEntity>> getLoanByTermUnit(@PathVariable("term") Integer term) {
        List<LoanEntity> loans = loanService.getLoanByTerm(term);
        return ResponseEntity.ok(loans);
    }

    @PostMapping("/")
    public ResponseEntity<LoanEntity> createLoan(@RequestBody LoanEntity loan) {
        LoanEntity newLoan = loanService.createLoan(loan);
        loanService.updateMonthQuote(newLoan);
        loanService.updateTotal(newLoan);
        return ResponseEntity.ok(newLoan);
    }

    @PutMapping("/")
    public ResponseEntity<LoanEntity> updateLoan(@RequestBody LoanEntity loan) {
        LoanEntity updatedLoan = loanService.updateLoan(loan);
        return ResponseEntity.ok(updatedLoan);
    }

    @PutMapping("/upState/{newState}")
    public ResponseEntity<LoanEntity> updateLoanState(@RequestBody LoanEntity loan, @PathVariable("newState") Integer newState) {
        LoanEntity updateLoan = loanService.updateState(loan, newState);
        return ResponseEntity.ok(updateLoan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteLoan(@PathVariable("id") Long id) throws Exception {
        var isDeleted = loanService.deleteLoan(id);
        return ResponseEntity.ok(isDeleted);
    }

    @PostMapping("/simulate/{capital}/{term}/{interest}")
    public  ResponseEntity<Double> simulateLoan(@PathVariable("capital") Double capital, @PathVariable("term") Integer term, @PathVariable("interest") Double interest){
        Double result = loanService.simulateLoan(capital, term, interest);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/incomeQuota/{income}/{id}")
    public ResponseEntity<Boolean> incomeQuota(@PathVariable("income") Double income, @PathVariable("id") Long id){
        Boolean incomeQuota = loanService.incomeQuota(id, income);
        return ResponseEntity.ok(incomeQuota);
    }

    @PostMapping("/debtIncome/{userId}/{income}")
    public ResponseEntity<Boolean> debtIncome(@PathVariable("userId") Long userId, @PathVariable("income") Double income){
        Boolean debtIncome = loanService.debtIncome(userId, income);
        return ResponseEntity.ok(debtIncome);
    }

    @PostMapping("/savingCapacity")
    public ResponseEntity<Integer> savingCapacity(@RequestParam Double balance,
                                                  @RequestParam Long loanId,
                                                  @RequestParam Boolean consistentSaving,
                                                  @RequestParam Boolean periodicSavings,
                                                  @RequestParam Boolean seniorityBalance,
                                                  @RequestParam Boolean recentRetirement){
        Integer result = loanService.savingCapacity(balance, loanId, consistentSaving, periodicSavings, seniorityBalance, recentRetirement);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/statusInt/{loanId}")
    public ResponseEntity<Integer> getLoanStatus(@PathVariable("loanId") Long loanId){
        Integer status = loanService.consultLoanStateInt(loanId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/maxCapital/{loanId}")
    public ResponseEntity<Boolean> getMaxCapital(@PathVariable("loanId") Long loanId){
        Boolean response = loanService.maxCapital(loanId);
        return ResponseEntity.ok(response);
    }
}
