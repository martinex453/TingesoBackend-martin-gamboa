package com.example.backend_martin_gamboa.Service;

import com.example.backend_martin_gamboa.Entity.LoanEntity;
import com.example.backend_martin_gamboa.Repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    @InjectMocks
    private LoanService loanService;

    @Mock
    private LoanRepository loanRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getLoanById_ShouldReturnLoan_WhenLoanExists() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        LoanEntity result = loanService.getLoanById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getAllLoans_ShouldReturnListOfLoans() {
        // Given
        LoanEntity loan1 = new LoanEntity();
        LoanEntity loan2 = new LoanEntity();

        List<LoanEntity> loansForEvaluate = new ArrayList<>();
        loansForEvaluate.add(loan1);
        loansForEvaluate.add(loan2);

        // When
        when(loanRepository.findAll()).thenReturn(Arrays.asList(loan1, loan2));

        List<LoanEntity> result = loanService.getAllLoans();

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void createLoan_ShouldSaveLoan_WhenLoanIsValid() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setCapital(10000.0);

        // When
        when(loanRepository.save(loan)).thenReturn(loan);

        LoanEntity result = loanService.createLoan(loan);

        // Then
        assertNotNull(result);
        assertEquals(10000.0, result.getCapital());
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void simulateLoan_ShouldReturnMonthlyPayment() {
        // Given
        Double capital = 10000.0;
        Integer term = 5;
        Double interest = 5.0;

        // When
        Double result = loanService.simulateLoan(capital, term, interest);

        // Then
        assertNotNull(result);
        assertTrue(result > 0);
    }

    @Test
    void totalPayments_ShouldReturnTotalPaymentAmount() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);
        loan.setTerm(5);
        loan.setInterest(3.5);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Double result = loanService.totalPayments(1L);

        // Then
        assertNotNull(result);
        assertTrue(result > 0);
    }

    @Test
    void incomeQuota_ShouldReturnTrue_WhenIncomeIsSufficient() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setMonthQuote(500.0);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Boolean result = loanService.incomeQuota(1L, 2000.0);

        // Then
        assertTrue(result);
    }

    @Test
    void debtIncome_ShouldReturnFalse_WhenTotalDebtExceedsThreshold() {
        // Given
        LoanEntity loan1 = new LoanEntity();
        loan1.setMonthQuote(500.0);

        LoanEntity loan2 = new LoanEntity();
        loan2.setMonthQuote(800.0);

        List<LoanEntity> loansForEvaluate = new ArrayList<>();
        loansForEvaluate.add(loan1);
        loansForEvaluate.add(loan2);

        // When
        when(loanRepository.findByUserId(1L)).thenReturn(loansForEvaluate);

        Boolean result = loanService.debtIncome(1L, 2000.0);

        // Then
        assertFalse(result);
    }

    @Test
    void maxCapital_ShouldReturnTrue_WhenCapitalIsBelowThresholdType1() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(70000.0);
        loan.setPropCost(100000.0);
        loan.setLoantype(1);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Boolean result = loanService.maxCapital(1L);

        // Then
        assertTrue(result);
    }

    @Test
    void maxCapital_ShouldReturnTrue_WhenCapitalIsBelowThresholdType2() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(60000.0);
        loan.setPropCost(100000.0);
        loan.setLoantype(2);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Boolean result = loanService.maxCapital(1L);

        // Then
        assertTrue(result);
    }

    @Test
    void maxCapital_ShouldReturnTrue_WhenCapitalIsBelowThresholdType3() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);
        loan.setPropCost(100000.0);
        loan.setLoantype(3);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Boolean result = loanService.maxCapital(1L);

        // Then
        assertTrue(result);
    }

    @Test
    void maxCapital_ShouldReturnTrue_WhenCapitalIsBelowThresholdType4() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);
        loan.setPropCost(100000.0);
        loan.setLoantype(4);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Boolean result = loanService.maxCapital(1L);

        // Then
        assertTrue(result);
    }

    @Test
    void maxCapital_ShouldReturnTrue_WhenCapitalIsBelowThresholdTypeIncorrect() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(70000.0);
        loan.setPropCost(100000.0);
        loan.setLoantype(9);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Boolean result = loanService.maxCapital(1L);

        // Then
        assertFalse(result);
    }

    @Test
    void getLoansForEvaluate_ShouldReturnLoansWithStatusDifferentFrom_2_7_8() {
        // Given
        LoanEntity loan1 = new LoanEntity();
        loan1.setStatus(1);
        LoanEntity loan2 = new LoanEntity();
        loan2.setStatus(2);
        LoanEntity loan3 = new LoanEntity();
        loan3.setStatus(7);

        List<LoanEntity> loansForEvaluate = new ArrayList<>();
        loansForEvaluate.add(loan1);
        loansForEvaluate.add(loan2);
        loansForEvaluate.add(loan3);

        // When
        when(loanRepository.findAll()).thenReturn(loansForEvaluate);

        List<LoanEntity> result = loanService.getLoansForEvaluate();

        // Then
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getStatus());
    }

    @Test
    void updateTotal_ShouldUpdateTotalPayments() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);
        loan.setTerm(5);
        loan.setInterest(3.5);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(loan)).thenReturn(loan);

        LoanEntity updatedLoan = loanService.updateTotal(loan);

        // Then
        assertNotNull(updatedLoan);
        assertTrue(updatedLoan.getTotal() > 0);
        verify(loanRepository, times(1)).save(loan);
    }


    @Test
    void updateMonthQuote_ShouldUpdateMonthQuote() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);
        loan.setTerm(5);
        loan.setInterest(3.5);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(loan)).thenReturn(loan);

        LoanEntity updatedLoan = loanService.updateMonthQuote(loan);

        // Then
        assertNotNull(updatedLoan);
        assertTrue(updatedLoan.getMonthQuote() > 0);
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void deleteLoan_ShouldReturnTrue_WhenLoanIsDeletedSuccessfully() throws Exception {
        // Given
        Long loanId = 1L;

        // When
        doNothing().when(loanRepository).deleteById(loanId);

        Boolean result = loanService.deleteLoan(loanId);

        // Then
        assertTrue(result);
        verify(loanRepository, times(1)).deleteById(loanId);
    }

    @Test
    void deleteLoan_ShouldThrowException_WhenLoanDeletionFails() {
        // Given
        Long loanId = 1L;

        // When
        doThrow(new RuntimeException("Error deleting loan")).when(loanRepository).deleteById(loanId);

        Exception exception = assertThrows(Exception.class, () -> {
            loanService.deleteLoan(loanId);
        });

        // Then
        assertEquals("Error deleting loan", exception.getMessage());
        verify(loanRepository, times(1)).deleteById(loanId);
    }

    @Test
    void savingCapacity_ShouldReturnLevelOne() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Integer result = loanService.savingCapacity(6000.0, 1L, true, true, true, false);

        // Then
        assertEquals(1, result);
    }

    @Test
    void savingCapacity_ShouldReturnLevelTwo() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Integer result = loanService.savingCapacity(4000.0, 1L, true, true, false, false);

        // Then
        assertEquals(2, result);
    }

    @Test
    void savingCapacity_ShouldReturnLevelThree() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Integer result = loanService.savingCapacity(2000.0, 1L, false, false, false, true);

        // Then
        assertEquals(3, result);
    }


    @Test
    void maxCapital_ShouldReturnFalse_WhenCapitalExceedsThreshold() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(90000.0);
        loan.setPropCost(100000.0);
        loan.setLoantype(1);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Boolean result = loanService.maxCapital(1L);

        // Then
        assertFalse(result);
    }

    @Test
    void consultLoanStateInt_ShouldReturnLoanState() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setStatus(3);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Integer result = loanService.consultLoanStateInt(1L);

        // Then
        assertNotNull(result);
        assertEquals(3, result);
    }

    @Test
    void debtIncome_ShouldReturnTrue_WhenTotalDebtIsBelowThreshold() {
        // Given
        LoanEntity loan1 = new LoanEntity();
        loan1.setMonthQuote(500.0);

        LoanEntity loan2 = new LoanEntity();
        loan2.setMonthQuote(300.0);

        List<LoanEntity> loans = new ArrayList<>();
        loans.add(loan1);
        loans.add(loan2);

        // When
        when(loanRepository.findByUserId(1L)).thenReturn(loans);

        Boolean result = loanService.debtIncome(1L, 2000.0);

        // Then
        assertTrue(result);
    }

    @Test
    void createLoan_ShouldReturnNull_WhenLoanIsNull() {
        // Given
        LoanEntity loan = null;

        // When
        LoanEntity result = loanService.createLoan(loan);

        // Then
        assertNull(result);
        verify(loanRepository, never()).save(any());
    }

    @Test
    void getLoanByUserId_ShouldReturnLoans_WhenLoansExistForUser() {
        // Given
        LoanEntity loan1 = new LoanEntity();
        loan1.setId(1L);
        loan1.setUserId(1L);

        LoanEntity loan2 = new LoanEntity();
        loan2.setId(2L);
        loan2.setUserId(1L);

        List<LoanEntity> loans = Arrays.asList(loan1, loan2);

        // When
        when(loanRepository.findByUserId(1L)).thenReturn(loans);

        List<LoanEntity> result = loanService.getLoanByUserId(1L);

        // Then
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals(1L, result.get(1).getUserId());
    }

    @Test
    void getLoanByTerm_ShouldReturnLoans_WhenLoansExistForTerm() {
        // Given
        LoanEntity loan1 = new LoanEntity();
        loan1.setId(1L);
        loan1.setTerm(5);

        LoanEntity loan2 = new LoanEntity();
        loan2.setId(2L);
        loan2.setTerm(5);

        List<LoanEntity> loans = Arrays.asList(loan1, loan2);

        // When
        when(loanRepository.findByTerm(5)).thenReturn(loans);

        List<LoanEntity> result = loanService.getLoanByTerm(5);

        // Then
        assertEquals(2, result.size());
        assertEquals(5, result.get(0).getTerm());
        assertEquals(5, result.get(1).getTerm());
    }

    @Test
    void totalPayments_ShouldReturnNull_WhenLoanDoesNotExist() {
        // Given
        Long loanId = 1L;

        // When
        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        Double result = loanService.totalPayments(loanId);

        // Then
        assertNull(result);
    }

    @Test
    void monthTotalPayments_ShouldReturnNull_WhenLoanIsNull() {
        // Given
        LoanEntity loan = null;

        // When
        Double result = loanService.monthTotalPayments(loan);

        // Then
        assertNull(result);
    }

    @Test
    void getLoansForEvaluate_ShouldReturnOnlyLoansWithStatusDifferentFrom_2_7_8() {
        // Given
        LoanEntity loan1 = new LoanEntity();
        loan1.setStatus(1);
        LoanEntity loan2 = new LoanEntity();
        loan2.setStatus(2);
        LoanEntity loan3 = new LoanEntity();
        loan3.setStatus(8);

        List<LoanEntity> loans = new ArrayList<>();
        loans.add(loan1);
        loans.add(loan2);
        loans.add(loan3);

        // When
        when(loanRepository.findAll()).thenReturn(loans);

        List<LoanEntity> result = loanService.getLoansForEvaluate();

        // Then
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getStatus());
    }

    @Test
    void updateLoan_ShouldReturnNull_WhenLoanIsNull() {
        // Given
        LoanEntity loan = null;

        // When
        LoanEntity result = loanService.updateLoan(loan);

        // Then
        assertNull(result);
        verify(loanRepository, never()).save(any());
    }

    @Test
    void simulateLoan_ShouldHandleNegativeCapital() {
        // Given
        Double capital = -10000.0;
        Integer term = 5;
        Double interest = 5.0;

        // When
        Double result = loanService.simulateLoan(capital, term, interest);

        // Then
        assertNotNull(result);
        assertTrue(result < 0);
    }

    @Test
    void incomeQuota_ShouldReturnFalse_WhenLoanDoesNotExist() {
        // Given
        Long loanId = 1L;

        // When
        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        Boolean result = loanService.incomeQuota(loanId, 2000.0);

        // Then
        assertFalse(result);
    }

    @Test
    void debtIncome_ShouldReturnTrue_WhenNoLoansExist() {
        // Given
        Long userId = 1L;

        // When
        when(loanRepository.findByUserId(userId)).thenReturn(new ArrayList<>());

        Boolean result = loanService.debtIncome(userId, 2000.0);

        // Then
        assertTrue(result);
    }

    @Test
    void maxCapital_ShouldReturnFalse_WhenLoanDoesNotExist() {
        // Given
        Long loanId = 1L;

        // When
        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        Boolean result = loanService.maxCapital(loanId);

        // Then
        assertFalse(result);
    }

    @Test
    void getUserLoans_ShouldReturnLoans_WhenUserHasLoans() {
        // Given
        Long userId = 1L;
        LoanEntity loan1 = new LoanEntity();
        loan1.setId(1L);
        loan1.setUserId(userId);

        LoanEntity loan2 = new LoanEntity();
        loan2.setId(2L);
        loan2.setUserId(userId);

        List<LoanEntity> loans = Arrays.asList(loan1, loan2);

        // When
        when(loanRepository.findByUserId(userId)).thenReturn(loans);

        List<LoanEntity> result = loanService.getUserLoans(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(loan1, result.get(0));
        assertEquals(loan2, result.get(1));
    }

    @Test
    void getUserLoans_ShouldReturnNull_WhenUserHasNoLoans() {
        // Given
        Long userId = 2L;
        List<LoanEntity> loans = new ArrayList<>();

        // When
        when(loanRepository.findByUserId(userId)).thenReturn(loans);

        List<LoanEntity> result = loanService.getUserLoans(userId);

        // Then
        assertNull(result);
    }

    @Test
    void getUserLoans_ShouldReturnNull_WhenUserIdIsInvalid() {
        // Given
        Long userId = 1L;
        List<LoanEntity> loans = new ArrayList<>();

        // When
        when(loanRepository.findByUserId(userId)).thenReturn(loans);

        List<LoanEntity> result = loanService.getUserLoans(userId);

        // Then
        assertNull(result);
    }

    @Test
    void updateState_ShouldUpdateStatusAndReturnLoan_WhenLoanIsValid() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setStatus(1);

        Integer newState = 2;

        // When
        when(loanRepository.save(loan)).thenReturn(loan);

        LoanEntity result = loanService.updateState(loan, newState);

        // Then
        assertNotNull(result);
        assertEquals(newState, result.getStatus());
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void updateState_ShouldReturnNull_WhenLoanIsNull() {
        // Given
        Integer newState = 1;
        LoanEntity loan = null;

        // When
        LoanEntity result = loanService.updateState(loan, newState);

        // Then
        assertNull(result);
    }

    @Test
    void savingCapacity_ShouldReturn2_WhenCountIs4() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setCapital(16000.0);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Integer result = loanService.savingCapacity(1500.0, 1L, true, true, true, false);

        // Then
        assertEquals(2, result);
    }

    @Test
    void savingCapacity_ShouldReturn2_WhenCountIs3() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setCapital(10000.0);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Integer result = loanService.savingCapacity(1500.0, 1L, true, true, false, false);

        // Then
        assertEquals(2, result);
    }

    @Test
    void incomeQuota_ShouldReturnFalse_WhenIncomeQuotaIsGreaterThan35() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setMonthQuote(400.0);

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Double income = 1000.0;

        Boolean result = loanService.incomeQuota(1L, income);

        // Then
        assertFalse(result);
    }

    @Test
    void consultLoanStateInt_ShouldReturnNull_WhenLoanIsNotFound() {
        // Given
        Long loanId = 1L;

        // When
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        Integer result = loanService.consultLoanStateInt(loanId);

        // Then
        assertNull(result);
    }

    @Test
    void updateLoan_ShouldSaveAndReturnLoan_WhenLoanIsValid() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);
        loan.setPropCost(100000.0);

        // When
        when(loanRepository.save(loan)).thenReturn(loan);

        LoanEntity result = loanService.updateLoan(loan);

        // Then
        verify(loanRepository, times(1)).save(loan);
        assertNotNull(result);
        assertEquals(loan, result);
    }

    @Test
    void createLoan_ShouldNotSetMonthFee_WhenMonthFeeIsNotNull() {
        // Given
        LoanEntity loan = new LoanEntity();
        loan.setCapital(50000.0);
        loan.setMonthFee(700.0);

        // When
        when(loanRepository.save(loan)).thenReturn(loan);

        LoanEntity result = loanService.createLoan(loan);

        // Then
        verify(loanRepository, times(1)).save(loan);
        assertNotNull(result);
        assertEquals(700.0, result.getMonthFee());
    }

    @Test
    void getLoanByCapital_ShouldReturnLoans_WhenCapitalIsFound() {
        // Given
        Double testCapital = 100000.0;
        LoanEntity loan1 = new LoanEntity();
        loan1.setCapital(testCapital);

        LoanEntity loan2 = new LoanEntity();
        loan2.setCapital(testCapital);

        List<LoanEntity> expectedLoans = Arrays.asList(loan1, loan2);

        // When
        when(loanRepository.findByCapital(testCapital)).thenReturn(expectedLoans);

        List<LoanEntity> result = loanService.getLoanByCapital(testCapital);

        // Then
        verify(loanRepository, times(1)).findByCapital(testCapital);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedLoans, result);
    }

    @Test
    void getLoanByCapital_ShouldReturnEmptyList_WhenNoLoansFound() {
        // Given
        Double testCapital = 100000.0;

        // When
        when(loanRepository.findByCapital(testCapital)).thenReturn(Arrays.asList());

        List<LoanEntity> result = loanService.getLoanByCapital(testCapital);

        verify(loanRepository, times(1)).findByCapital(testCapital);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}


