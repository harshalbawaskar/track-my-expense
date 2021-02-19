package com.expensetracker.repositories;

import com.expensetracker.domain.Transaction;
import com.expensetracker.exceptions.BadRequestException;
import com.expensetracker.exceptions.ResourceNotFoundException;

import java.util.List;

public interface TransactionRepository {
    List<Transaction> findAllTransactions(Integer userId, Integer categoryId);

    Transaction findById(Integer userId, Integer categoryId, Integer transactionId) throws ResourceNotFoundException;

    Integer create(Integer userId, Integer categoryId, String note, double amount, long transactionDate) throws BadRequestException;

    void update(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) throws BadRequestException;

    void remove(Integer userId, Integer categoryId, Integer transactionId) throws ResourceNotFoundException;
}
