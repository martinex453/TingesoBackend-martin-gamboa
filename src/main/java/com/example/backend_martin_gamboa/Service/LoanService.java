package com.example.backend_martin_gamboa.Service;

import com.example.backend_martin_gamboa.Entity.LoanEntity;
import com.example.backend_martin_gamboa.Repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class LoanService {
    @Autowired
    LoanRepository loanRepository;

    public List<LoanEntity> getAllLoans() { return loanRepository.findAll(); }

    public LoanEntity getLoanById(Long id) {return loanRepository.findById(id).orElse(null); }

    public List<LoanEntity> getLoanByCapital(Double capital){ return loanRepository.findByCapital(capital); }

    public List<LoanEntity> getLoanByUserId(Long userId){ return loanRepository.findByUserId(userId); }

    public List<LoanEntity> getLoanByTerm(Integer term){ return loanRepository.findByTerm(term); }

    public LoanEntity createLoan(LoanEntity loan){
        if(loan == null) return null;
        if(loan.getMonthFee() == null){
            loan.setMonthFee(loan.getCapital()*0.01);
        }
        return loanRepository.save(loan);
    }

    public LoanEntity updateTotal(LoanEntity loan){
        loan.setTotal(totalPayments(loan.getId()));
        return loanRepository.save(loan);
    }

    public LoanEntity updateMonthQuote(LoanEntity loan){
        loan.setMonthQuote(monthTotalPayments(loan));
        return loanRepository.save(loan);
    }

    public LoanEntity updateLoan(LoanEntity loan){
        if(loan == null){
            return null;
        }
        return loanRepository.save(loan);
    }

    public Boolean deleteLoan(Long id) throws Exception {
        try{
            loanRepository.deleteById(id);
            return true;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public Double simulateLoan(Double capital, Integer term, Double interest){
        Double monthInterest = (interest/12)/100;
        Integer payments = term*12;
        Double Result = (capital*monthInterest*Math.pow(1+monthInterest, payments))/((Math.pow(1+monthInterest, payments)) - 1);
        Result = Math.round(Result * 100.0) / 100.0;
        return Result;
    }

    public Double totalPayments(Long loanId){
        LoanEntity loan = getLoanById(loanId);
        if (loan == null) return null;

        Double capital = loan.getCapital();
        Integer term = loan.getTerm();
        Double interest = loan.getInterest();
        Double monthInterest = (interest / 12) / 100;
        Integer payments = term * 12;
        Double monthPayment = (capital * monthInterest * Math.pow(1 + monthInterest, payments)) /
                (Math.pow(1 + monthInterest, payments) - 1);
        Double deductionSure = capital * 0.0003;
        Double fireSure = 20000.0;
        Double adminFee = capital * 0.01;
        Double totalMonthPayments = monthPayment + deductionSure + fireSure;
        Double totalPayments = (double) Math.round(totalMonthPayments) * payments + adminFee;
        totalPayments = (double) Math.round(totalPayments);
        return totalPayments;
    }

    public Double monthTotalPayments(LoanEntity loan){
        if(loan == null) return null;
        Double capital = loan.getCapital();
        Integer term = loan.getTerm();
        Double interest = loan.getInterest();
        Double monthInterest = (interest / 12) / 100;
        Integer payments = term * 12;
        Double monthPayment = (capital * monthInterest * Math.pow(1 + monthInterest, payments)) /
                (Math.pow(1 + monthInterest, payments) - 1);
        Double deductionSure = capital * 0.0003;
        Double fireSure = 20000.0;
        Double totalMonthPayments = monthPayment + deductionSure + fireSure;
        totalMonthPayments = (double) Math.round(totalMonthPayments);
        return totalMonthPayments;
    }

    public Integer consultLoanStateInt(Long loanId){
        LoanEntity loan = getLoanById(loanId);
        if(loan == null) return null;
        return loan.getStatus();
    }

    public Boolean incomeQuota(Long id, Double income){
        LoanEntity loan = getLoanById(id);
        if(loan == null) return false;
        Double monthQuota = loan.getMonthQuote();
        Double incomeQuota = (monthQuota/income)*100;
        if(incomeQuota > 35){
            return false;
        }
        return true;
    }

    public Boolean debtIncome(Long userId, Double income){
        List<LoanEntity> loans = loanRepository.findByUserId(userId);
        Double totalDebt = 0.0;
        for(LoanEntity loan : loans){
            totalDebt = totalDebt + loan.getMonthQuote();
        }
        Double threshold = income*0.5;
        if(totalDebt > threshold){
            return false;
        }
        return true;
    }

    public Integer savingCapacity(Double balance, Long loanId, Boolean consistenSaving, Boolean periodicSavings, Boolean seniorityBalance, Boolean recentRetirement){
        LoanEntity loan = getLoanById(loanId);
        Double capital = loan.getCapital()*0.1;
        Integer count = 0;
        if(consistenSaving){
            count += 1;
        }
        if(periodicSavings){
            count += 1;
        }
        if(seniorityBalance){
            count += 1;
        }
        if(!recentRetirement){
            count += 1;
        }
        if(balance >= capital){
            count += 1;
        }

        if(count == 5){
            return 1;
        }
        else if (count == 3 || count == 4) {
            return 2;
        }
        return 3;

    }

    public LoanEntity updateState(LoanEntity loan, Integer state){
        if(loan == null) return null;
        loan.setStatus(state);
        return loanRepository.save(loan);
    }

    public Boolean maxCapital(Long loanId){
        LoanEntity loan = getLoanById(loanId);
        if(loan == null) return false;
        Double capital = loan.getCapital();
        Double propCost = loan.getPropCost();
        Integer loanType = loan.getLoantype();
        Double maxCapital = 0.0;
        switch (loanType){
            case 1:
                maxCapital = propCost*0.8;
                break;
            case 2:
                maxCapital = propCost*0.7;
                break;
            case 3:
                maxCapital = propCost*0.6;
                break;
            case 4:
                maxCapital = propCost*0.5;
                break;
            default:
                return false;
        }
        return maxCapital >= capital;
    }

    public List<LoanEntity> getLoansForEvaluate(){
        List<LoanEntity> loans = loanRepository.findAll();
        Iterator<LoanEntity> iterator = loans.iterator();

        while (iterator.hasNext()) {
            LoanEntity loan = iterator.next();
            if (loan.getStatus() == 2 || loan.getStatus() == 7 || loan.getStatus() == 8) {
                iterator.remove();
            }
        }
        return loans;
    }

    public List<LoanEntity> getUserLoans(Long userId){
        List<LoanEntity> loans = loanRepository.findByUserId(userId);
        if(loans.isEmpty()){
            return null;
        }
        return loans;
    }

}
