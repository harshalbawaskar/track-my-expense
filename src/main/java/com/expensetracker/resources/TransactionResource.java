package com.expensetracker.resources;

import com.expensetracker.domain.Transaction;
import com.expensetracker.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories/{categoryId}/transactions")
public class TransactionResource {

    @Autowired
    TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(HttpServletRequest httpServletRequest,
                                                @PathVariable("categoryId") Integer categoryId) {
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        List<Transaction> transactionMap = transactionService.fetchAllTransactions(userId, categoryId);
        return new ResponseEntity<>(transactionMap, HttpStatus.OK);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(HttpServletRequest httpServletRequest,
                                                          @PathVariable("categoryId") Integer categoryId,
                                                          @PathVariable("transactionId") Integer transactionId) {
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        Transaction transaction = transactionService.fetchByTransactionId(userId, categoryId, transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transaction> addTransaction(HttpServletRequest httpServletRequest,
                                                          @PathVariable("categoryId") Integer categoryId,
                                                          @RequestBody Map<String, Object> transRequestBody) {
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        String note = (String) transRequestBody.get("note");
        Double amount = (Double) transRequestBody.get("amount");
        Long transactionDate = (Long) transRequestBody.get("transactionDate");
        Transaction transaction = transactionService.addTransaction(userId, categoryId, note, amount,transactionDate);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Map<String, String>> updateTransaction(HttpServletRequest httpServletRequest,
                                                      @PathVariable("categoryId") Integer categoryId,
                                                      @PathVariable("transactionId") Integer transactionId,
                                                      @RequestBody Transaction transaction) {
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        transactionService.updateTransaction(userId, categoryId, transactionId, transaction);
        Map<String, String> updateResponse = new HashMap<>();
        updateResponse.put("message", "Transaction updated success!");
        return new ResponseEntity<>(updateResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Map<String, String>> removeTransaction(HttpServletRequest httpServletRequest,
                                              @PathVariable("categoryId") Integer categoryId,
                                              @PathVariable("transactionId") Integer transactionId) {
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        transactionService.removeTransaction(userId, categoryId, transactionId);
        Map<String, String> updateResponse = new HashMap<>();
        updateResponse.put("message", "Transaction deleted success!");
        return new ResponseEntity<>(updateResponse, HttpStatus.OK);
    }
}
