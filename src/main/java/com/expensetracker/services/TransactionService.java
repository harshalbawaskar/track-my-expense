package com.expensetracker.services;

import com.expensetracker.domain.Transaction;
import com.expensetracker.exceptions.BadRequestException;
import com.expensetracker.exceptions.ResourceNotFoundException;

import java.util.List;

public interface TransactionService {
    List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId);
    Transaction fetchByTransactionId(Integer userId, Integer categoryId, Integer transactionId) throws ResourceNotFoundException;
    Transaction addTransaction(Integer userId, Integer categoryId, String note, double amount, long transactionDate) throws BadRequestException;
    void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) throws BadRequestException;
    void removeTransaction(Integer userId, Integer categoryId, Integer transactionId) throws ResourceNotFoundException;
}
