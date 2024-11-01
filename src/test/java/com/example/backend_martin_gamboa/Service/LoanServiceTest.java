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
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        LoanEntity result = loanService.getLoanById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getAllLoans_ShouldReturnListOfLoans() {
        LoanEntity loan1 = new LoanEntity();
        LoanEntity loan2 = new LoanEntity();

        List<LoanEntity> loansForEvaluate = new ArrayList<>();
        loansForEvaluate.add(loan1);
        loansForEvaluate.add(loan2);

        when(loanRepository.findAll()).thenReturn(Arrays.asList(loan1, loan2));

        List<LoanEntity> result = loanService.getAllLoans();
        assertEquals(2, result.size());
    }

    @Test
    void createLoan_ShouldSaveLoan_WhenLoanIsValid() {
        LoanEntity loan = new LoanEntity();
        loan.setCapital(10000.0);

        when(loanRepository.save(loan)).thenReturn(loan);

        LoanEntity result = loanService.createLoan(loan);
        assertNotNull(result);
        assertEquals(10000.0, result.getCapital());
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void simulateLoan_ShouldReturnMonthlyPayment() {
        Double capital = 10000.0;
        Integer term = 5; // años
        Double interest = 5.0; // porcentaje anual

        Double result = loanService.simulateLoan(capital, term, interest);

        assertNotNull(result);
        assertTrue(result > 0);
    }

    @Test
    void totalPayments_ShouldReturnTotalPaymentAmount() {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);
        loan.setTerm(5); // en años
        loan.setInterest(3.5); // en porcentaje

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Double result = loanService.totalPayments(1L);

        assertNotNull(result);
        assertTrue(result > 0);
    }

    @Test
    void incomeQuota_ShouldReturnTrue_WhenIncomeIsSufficient() {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setMonthQuote(500.0);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Boolean result = loanService.incomeQuota(1L, 2000.0);
        assertTrue(result);
    }

    @Test
    void debtIncome_ShouldReturnFalse_WhenTotalDebtExceedsThreshold() {
        LoanEntity loan1 = new LoanEntity();
        loan1.setMonthQuote(500.0);

        LoanEntity loan2 = new LoanEntity();
        loan2.setMonthQuote(800.0);

        List<LoanEntity> loansForEvaluate = new ArrayList<>();
        loansForEvaluate.add(loan1);
        loansForEvaluate.add(loan2);

        when(loanRepository.findByUserId(1L)).thenReturn(loansForEvaluate);

        Boolean result = loanService.debtIncome(1L, 2000.0);
        assertFalse(result);
    }

    @Test
    void maxCapital_ShouldReturnTrue_WhenCapitalIsBelowThresholdType1() {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(70000.0);
        loan.setPropCost(100000.0);
        loan.setLoantype(1);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Boolean result = loanService.maxCapital(1L);
        assertTrue(result);
    }

    @Test
    void maxCapital_ShouldReturnTrue_WhenCapitalIsBelowThresholdType2() {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(60000.0);
        loan.setPropCost(100000.0);
        loan.setLoantype(2);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Boolean result = loanService.maxCapital(1L);
        assertTrue(result);
    }

    @Test
    void maxCapital_ShouldReturnTrue_WhenCapitalIsBelowThresholdType3() {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);
        loan.setPropCost(100000.0);
        loan.setLoantype(3);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Boolean result = loanService.maxCapital(1L);
        assertTrue(result);
    }

    @Test
    void maxCapital_ShouldReturnTrue_WhenCapitalIsBelowThresholdType4() {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);
        loan.setPropCost(100000.0);
        loan.setLoantype(4);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Boolean result = loanService.maxCapital(1L);
        assertTrue(result);
    }

    @Test
    void maxCapital_ShouldReturnTrue_WhenCapitalIsBelowThresholdTypeIncorrect() {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(70000.0);
        loan.setPropCost(100000.0);
        loan.setLoantype(9);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Boolean result = loanService.maxCapital(1L);
        assertFalse(result);
    }

    @Test
    void getLoansForEvaluate_ShouldReturnLoansWithStatusDifferentFrom_2_7_8() {
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

        when(loanRepository.findAll()).thenReturn(loansForEvaluate);

        List<LoanEntity> result = loanService.getLoansForEvaluate();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getStatus());
    }

    @Test
    void updateTotal_ShouldUpdateTotalPayments() {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);
        loan.setTerm(5); // años
        loan.setInterest(3.5); // porcentaje anual

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(loan)).thenReturn(loan);

        LoanEntity updatedLoan = loanService.updateTotal(loan);

        assertNotNull(updatedLoan);
        assertTrue(updatedLoan.getTotal() > 0);
        verify(loanRepository, times(1)).save(loan);
    }


    @Test
    void updateMonthQuote_ShouldUpdateMonthQuote() {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);
        loan.setTerm(5); // años
        loan.setInterest(3.5); // porcentaje anual

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(loan)).thenReturn(loan);

        LoanEntity updatedLoan = loanService.updateMonthQuote(loan);

        assertNotNull(updatedLoan);
        assertTrue(updatedLoan.getMonthQuote() > 0);
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void deleteLoan_ShouldReturnTrue_WhenLoanIsDeletedSuccessfully() throws Exception {
        Long loanId = 1L;

        doNothing().when(loanRepository).deleteById(loanId);

        Boolean result = loanService.deleteLoan(loanId);

        assertTrue(result);
        verify(loanRepository, times(1)).deleteById(loanId);
    }

    @Test
    void deleteLoan_ShouldThrowException_WhenLoanDeletionFails() {
        Long loanId = 1L;

        doThrow(new RuntimeException("Error deleting loan")).when(loanRepository).deleteById(loanId);

        Exception exception = assertThrows(Exception.class, () -> {
            loanService.deleteLoan(loanId);
        });

        assertEquals("Error deleting loan", exception.getMessage());
        verify(loanRepository, times(1)).deleteById(loanId);
    }

    @Test
    void savingCapacity_ShouldReturnCorrectCapacityLevel() {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Integer result = loanService.savingCapacity(6000.0, 1L, true, true, true, false);
        assertEquals(1, result);  // Capacidad máxima

        result = loanService.savingCapacity(4000.0, 1L, true, true, false, false);
        assertEquals(2, result);  // Capacidad media

        result = loanService.savingCapacity(2000.0, 1L, false, false, false, true);
        assertEquals(3, result);  // Capacidad baja
    }

    @Test
    void maxCapital_ShouldReturnFalse_WhenCapitalExceedsThreshold() {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(90000.0);
        loan.setPropCost(100000.0);
        loan.setLoantype(1); // Tipo: primera vivienda

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Boolean result = loanService.maxCapital(1L);
        assertFalse(result);  // El capital es mayor que el permitido para el tipo 1
    }

    @Test
    void consultLoanStateInt_ShouldReturnLoanState() {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setStatus(3);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Integer result = loanService.consultLoanStateInt(1L);
        assertNotNull(result);
        assertEquals(3, result);  // El estado del préstamo debe ser 3
    }

    @Test
    void debtIncome_ShouldReturnTrue_WhenTotalDebtIsBelowThreshold() {
        LoanEntity loan1 = new LoanEntity();
        loan1.setMonthQuote(500.0);

        LoanEntity loan2 = new LoanEntity();
        loan2.setMonthQuote(300.0);

        List<LoanEntity> loans = new ArrayList<>();
        loans.add(loan1);
        loans.add(loan2);

        when(loanRepository.findByUserId(1L)).thenReturn(loans);

        Boolean result = loanService.debtIncome(1L, 2000.0);
        assertTrue(result);  // La deuda total está por debajo del umbral
    }

    @Test
    void createLoan_ShouldReturnNull_WhenLoanIsNull() {
        LoanEntity loan = null;

        LoanEntity result = loanService.createLoan(loan);

        assertNull(result); // Debe retornar null
        verify(loanRepository, never()).save(any()); // No debe llamar a save
    }

    @Test
    void getLoanByUserId_ShouldReturnLoans_WhenLoansExistForUser() {
        LoanEntity loan1 = new LoanEntity();
        loan1.setId(1L);
        loan1.setUserId(1L);

        LoanEntity loan2 = new LoanEntity();
        loan2.setId(2L);
        loan2.setUserId(1L);

        List<LoanEntity> loans = Arrays.asList(loan1, loan2);

        when(loanRepository.findByUserId(1L)).thenReturn(loans);

        List<LoanEntity> result = loanService.getLoanByUserId(1L);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals(1L, result.get(1).getUserId());
    }

    @Test
    void getLoanByTerm_ShouldReturnLoans_WhenLoansExistForTerm() {
        LoanEntity loan1 = new LoanEntity();
        loan1.setId(1L);
        loan1.setTerm(5);

        LoanEntity loan2 = new LoanEntity();
        loan2.setId(2L);
        loan2.setTerm(5);

        List<LoanEntity> loans = Arrays.asList(loan1, loan2);

        when(loanRepository.findByTerm(5)).thenReturn(loans);

        List<LoanEntity> result = loanService.getLoanByTerm(5);

        assertEquals(2, result.size());
        assertEquals(5, result.get(0).getTerm());
        assertEquals(5, result.get(1).getTerm());
    }

    @Test
    void totalPayments_ShouldReturnNull_WhenLoanDoesNotExist() {
        Long loanId = 1L;

        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        Double result = loanService.totalPayments(loanId);

        assertNull(result); // Debe retornar null
    }

    @Test
    void monthTotalPayments_ShouldReturnNull_WhenLoanIsNull() {
        LoanEntity loan = null;

        Double result = loanService.monthTotalPayments(loan);

        assertNull(result); // Debe retornar null
    }

    @Test
    void getLoansForEvaluate_ShouldReturnOnlyLoansWithStatusDifferentFrom_2_7_8() {
        LoanEntity loan1 = new LoanEntity();
        loan1.setStatus(1); // Estado válido
        LoanEntity loan2 = new LoanEntity();
        loan2.setStatus(2); // Estado no válido
        LoanEntity loan3 = new LoanEntity();
        loan3.setStatus(8); // Estado no válido

        List<LoanEntity> loans = new ArrayList<>();
        loans.add(loan1);
        loans.add(loan2);
        loans.add(loan3);

        when(loanRepository.findAll()).thenReturn(loans);

        List<LoanEntity> result = loanService.getLoansForEvaluate();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getStatus()); // Solo debe retornar el préstamo con estado 1
    }

    @Test
    void updateLoan_ShouldReturnNull_WhenLoanIsNull() {
        LoanEntity loan = null;

        LoanEntity result = loanService.updateLoan(loan);

        assertNull(result); // Debe retornar null
        verify(loanRepository, never()).save(any()); // No debe llamar a save
    }

    @Test
    void simulateLoan_ShouldHandleNegativeCapital() {
        Double capital = -10000.0;
        Integer term = 5; // años
        Double interest = 5.0; // porcentaje anual

        Double result = loanService.simulateLoan(capital, term, interest);

        assertNotNull(result);
        assertTrue(result < 0); // Debe ser negativo o manejarlo de acuerdo a la lógica de negocio
    }

    @Test
    void incomeQuota_ShouldReturnFalse_WhenLoanDoesNotExist() {
        Long loanId = 1L;
        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        Boolean result = loanService.incomeQuota(loanId, 2000.0);

        assertFalse(result); // Debería retornar falso
    }

    @Test
    void debtIncome_ShouldReturnTrue_WhenNoLoansExist() {
        Long userId = 1L;

        when(loanRepository.findByUserId(userId)).thenReturn(new ArrayList<>());

        Boolean result = loanService.debtIncome(userId, 2000.0);

        assertTrue(result); // No hay deudas, por lo que debería retornar verdadero
    }

    @Test
    void maxCapital_ShouldReturnFalse_WhenLoanDoesNotExist() {
        Long loanId = 1L;

        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        Boolean result = loanService.maxCapital(loanId);

        assertFalse(result); // No debe haber capital máximo si el préstamo no existe
    }

    @Test
    void getUserLoans_ShouldReturnLoans_WhenUserHasLoans() {
        Long userId = 1L;
        LoanEntity loan1 = new LoanEntity();
        loan1.setId(1L);
        loan1.setUserId(userId);

        LoanEntity loan2 = new LoanEntity();
        loan2.setId(2L);
        loan2.setUserId(userId);

        List<LoanEntity> loans = Arrays.asList(loan1, loan2);

        when(loanRepository.findByUserId(userId)).thenReturn(loans);

        List<LoanEntity> result = loanService.getUserLoans(userId);
        assertNotNull(result); // Asegúrate de que no sea null
        assertEquals(2, result.size()); // Debería retornar 2 préstamos
        assertEquals(loan1, result.get(0)); // Comprobar que el primer préstamo es correcto
        assertEquals(loan2, result.get(1)); // Comprobar que el segundo préstamo es correcto
    }

    @Test
    void getUserLoans_ShouldReturnNull_WhenUserHasNoLoans() {
        Long userId = 2L;
        List<LoanEntity> loans = new ArrayList<>(); // Lista vacía

        when(loanRepository.findByUserId(userId)).thenReturn(loans);

        List<LoanEntity> result = loanService.getUserLoans(userId);
        assertNull(result); // Debería retornar null cuando no hay préstamos
    }

    @Test
    void getUserLoans_ShouldReturnNull_WhenUserIdIsInvalid() {
        Long userId = 1L; // ID de usuario inválido
        List<LoanEntity> loans = new ArrayList<>(); // Lista vacía

        when(loanRepository.findByUserId(userId)).thenReturn(loans);

        List<LoanEntity> result = loanService.getUserLoans(userId);
        assertNull(result); // Debería retornar null
    }

    @Test
    void updateState_ShouldUpdateStatusAndReturnLoan_WhenLoanIsValid() {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setStatus(1); // Estado inicial

        Integer newState = 2; // Nuevo estado

        when(loanRepository.save(loan)).thenReturn(loan); // Simulamos que el repositorio guarda el préstamo

        LoanEntity result = loanService.updateState(loan, newState);

        assertNotNull(result); // Asegúrate de que el resultado no es null
        assertEquals(newState, result.getStatus()); // Verifica que el estado se haya actualizado
        verify(loanRepository, times(1)).save(loan); // Verifica que se llama al repositorio
    }

    @Test
    void updateState_ShouldReturnNull_WhenLoanIsNull() {
        LoanEntity result = loanService.updateState(null, 1);
        assertNull(result); // Debería retornar null cuando el préstamo es null
    }

    @Test
    void savingCapacity_ShouldReturn2_WhenCountIs4() {
        LoanEntity loan = new LoanEntity();
        loan.setCapital(16000.0); // Capital que permite un balance mínimo de 1000.0 (10% de 10000)

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        // Parámetros: balance = 1500.0, consistenSaving = true, periodicSavings = true, seniorityBalance = true, recentRetirement = false
        Integer result = loanService.savingCapacity(1500.0, 1L, true, true, true, false);

        assertEquals(2, result); // Se espera que retorne 2
    }

    @Test
    void savingCapacity_ShouldReturn2_WhenCountIs3() {
        LoanEntity loan = new LoanEntity();
        loan.setCapital(10000.0); // Capital que permite un balance mínimo de 1000.0 (10% de 10000)

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        // Parámetros: balance = 1500.0, consistenSaving = true, periodicSavings = true, seniorityBalance = false, recentRetirement = false
        Integer result = loanService.savingCapacity(1500.0, 1L, true, true, false, false);

        assertEquals(2, result); // Se espera que retorne 2
    }

    @Test
    void incomeQuota_ShouldReturnFalse_WhenIncomeQuotaIsGreaterThan35() {
        // Crea un préstamo simulado
        LoanEntity loan = new LoanEntity();
        loan.setMonthQuote(400.0); // Cuota mensual del préstamo

        // Simula que el préstamo es encontrado en el repositorio
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        // Establece un ingreso que causará que incomeQuota sea mayor a 35
        Double income = 1000.0; // Ingreso mensual

        // Llama al método
        Boolean result = loanService.incomeQuota(1L, income);

        // Verifica que el resultado sea falso
        assertFalse(result); // Se espera que retorne false, porque (400/1000)*100 = 40
    }

    @Test
    void consultLoanStateInt_ShouldReturnNull_WhenLoanIsNotFound() {
        // Simula que no se encuentra el préstamo en el repositorio
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        // Llama al método
        Integer result = loanService.consultLoanStateInt(1L);

        // Verifica que el resultado sea null
        assertNull(result); // Se espera que retorne null porque el préstamo no se encontró
    }

    @Test
    void updateLoan_ShouldSaveAndReturnLoan_WhenLoanIsValid() {
        // Crea un préstamo válido
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setCapital(50000.0);
        loan.setPropCost(100000.0);

        // Simula el comportamiento del repositorio para guardar el préstamo
        when(loanRepository.save(loan)).thenReturn(loan);

        // Llama al método
        LoanEntity result = loanService.updateLoan(loan);

        // Verifica que el método save fue llamado una vez
        verify(loanRepository, times(1)).save(loan);

        // Verifica que el préstamo retornado sea el mismo que el que se pasó
        assertNotNull(result); // Se espera que no sea nulo
        assertEquals(loan, result); // Se espera que retorne el mismo préstamo
    }

    @Test
    void createLoan_ShouldNotSetMonthFee_WhenMonthFeeIsNotNull() {
        // Crea un préstamo con monthFee ya establecido
        LoanEntity loan = new LoanEntity();
        loan.setCapital(50000.0); // Capital para el préstamo
        loan.setMonthFee(700.0); // Establece un valor para monthFee

        // Simula el comportamiento del repositorio para guardar el préstamo
        when(loanRepository.save(loan)).thenReturn(loan);

        // Llama al método
        LoanEntity result = loanService.createLoan(loan);

        // Verifica que el método save fue llamado una vez
        verify(loanRepository, times(1)).save(loan);

        // Verifica que el monthFee no ha sido modificado
        assertNotNull(result); // Se espera que no sea nulo
        assertEquals(700.0, result.getMonthFee()); // Se espera que el monthFee siga siendo 700
    }

    @Test
    void getLoanByCapital_ShouldReturnLoans_WhenCapitalIsFound() {
        // Define un capital de prueba
        Double testCapital = 100000.0;

        // Crea una lista de préstamos para retornar
        LoanEntity loan1 = new LoanEntity();
        loan1.setCapital(testCapital);

        LoanEntity loan2 = new LoanEntity();
        loan2.setCapital(testCapital);

        List<LoanEntity> expectedLoans = Arrays.asList(loan1, loan2);

        // Simula el comportamiento del repositorio para encontrar préstamos por capital
        when(loanRepository.findByCapital(testCapital)).thenReturn(expectedLoans);

        // Llama al método
        List<LoanEntity> result = loanService.getLoanByCapital(testCapital);

        // Verifica que el método findByCapital fue llamado una vez
        verify(loanRepository, times(1)).findByCapital(testCapital);

        // Verifica que el resultado es el esperado
        assertNotNull(result); // Se espera que no sea nulo
        assertEquals(2, result.size()); // Se espera que haya 2 préstamos
        assertEquals(expectedLoans, result); // Se espera que los préstamos sean iguales
    }

    @Test
    void getLoanByCapital_ShouldReturnEmptyList_WhenNoLoansFound() {
        // Define un capital de prueba
        Double testCapital = 100000.0;

        // Simula el comportamiento del repositorio para no encontrar préstamos
        when(loanRepository.findByCapital(testCapital)).thenReturn(Arrays.asList());

        // Llama al método
        List<LoanEntity> result = loanService.getLoanByCapital(testCapital);

        // Verifica que el método findByCapital fue llamado una vez
        verify(loanRepository, times(1)).findByCapital(testCapital);

        // Verifica que el resultado es una lista vacía
        assertNotNull(result); // Se espera que no sea nulo
        assertTrue(result.isEmpty()); // Se espera que la lista esté vacía
    }
}


