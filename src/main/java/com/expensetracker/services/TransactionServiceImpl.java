package com.expensetracker.services;

import com.expensetracker.domain.Transaction;
import com.expensetracker.exceptions.BadRequestException;
import com.expensetracker.exceptions.ResourceNotFoundException;
import com.expensetracker.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId) {
        return transactionRepository.findAllTransactions(userId, categoryId);
    }

    @Override
    public Transaction fetchByTransactionId(Integer userId, Integer categoryId, Integer transactionId) throws ResourceNotFoundException {
        return transactionRepository.findById(userId, categoryId, transactionId);
    }

    @Override
    public Transaction addTransaction(Integer userId, Integer categoryId, String note, double amount, long transactionDate) throws BadRequestException {
        Integer transactionId = transactionRepository.create(userId, categoryId, note, amount, transactionDate);
        return transactionRepository.findById(userId, categoryId, transactionId);
    }

    @Override
    public void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) throws BadRequestException {
        transactionRepository.update(userId, categoryId, transactionId, transaction);
    }

    @Override
    public void removeTransaction(Integer userId, Integer categoryId, Integer transactionId) throws ResourceNotFoundException {
        fetchByTransactionId(userId, categoryId, transactionId);
        transactionRepository.remove(userId, categoryId, transactionId);
    }
}
