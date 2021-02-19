package com.expensetracker.repositories;

import com.expensetracker.domain.Transaction;
import com.expensetracker.exceptions.BadRequestException;
import com.expensetracker.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_FIND_ALL_TRANSACTIONS = "SELECT TRANSACTION_ID, USER_ID, CATEGORY_ID, NOTE, AMOUNT, TRANSACTION_DATE " +
            "FROM TRANSACTIONS_TBL WHERE USER_ID = ? AND CATEGORY_ID = ?";
    private static final String SQL_FIND_TRANSACTIONS_BY_ID = "SELECT TRANSACTION_ID, USER_ID, CATEGORY_ID, NOTE, AMOUNT, TRANSACTION_DATE " +
            "FROM TRANSACTIONS_TBL WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSACTION_ID = ?";
    private static final String SQL_CREATE_TRANSACTION = "INSERT INTO TRANSACTIONS_TBL (TRANSACTION_ID, USER_ID, CATEGORY_ID, NOTE, AMOUNT, TRANSACTION_DATE) " +
            "VALUES(NEXTVAL('TRANSACTIONS_SEQ'), ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_TRANSACTION = "UPDATE TRANSACTIONS_TBL SET NOTE = ?, AMOUNT = ?, TRANSACTION_DATE = ? " +
            "WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSACTION_ID = ?";
    private static final String SQL_DELETE_TRANSACTION = "DELETE FROM TRANSACTIONS_TBL WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSACTION_ID = ?";

    private RowMapper<Transaction> transactionRowMapper = ((rs, rowNum) -> {
        return new Transaction(
                rs.getInt("TRANSACTION_ID"),
                rs.getInt("USER_ID"),
                rs.getInt("CATEGORY_ID"),
                rs.getString("NOTE"),
                rs.getDouble("AMOUNT"),
                rs.getLong("TRANSACTION_DATE")
        );
    });

    @Override
    public List<Transaction> findAllTransactions(Integer userId, Integer categoryId) {
        return jdbcTemplate.query(SQL_FIND_ALL_TRANSACTIONS, new Object[]{userId, categoryId}, transactionRowMapper);
    }

    @Override
    public Transaction findById(Integer userId, Integer categoryId, Integer transactionId) throws ResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_TRANSACTIONS_BY_ID, new Object[]{userId, categoryId, transactionId}, transactionRowMapper);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Invalid id provided!");
        }
    }

    @Override
    public Integer create(Integer userId, Integer categoryId, String note, double amount, long transactionDate) throws BadRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update((connection) -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE_TRANSACTION, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setInt(2, categoryId);
                ps.setString(3, note);
                ps.setDouble(4, amount);
                ps.setLong(5, transactionDate);
                return ps;
            }, keyHolder);

            return (Integer) keyHolder.getKeys().get("TRANSACTION_ID");
        } catch (Exception ex) {
            throw new BadRequestException("Invalid transactions details!");
        }
    }

    @Override
    public void update(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) throws BadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE_TRANSACTION, new Object[]{transaction.getNote(), transaction.getAmount(), transaction.getTransactionDate(), userId, categoryId, transactionId});
        } catch (Exception ex) {
            throw new BadRequestException("Invalid transactions details!");
        }
    }

    @Override
    public void remove(Integer userId, Integer categoryId, Integer transactionId) throws ResourceNotFoundException {
        Integer deleteRowCount = jdbcTemplate.update(SQL_DELETE_TRANSACTION, new Object[]{userId, categoryId, transactionId});
        if (deleteRowCount == 0) {
            throw new ResourceNotFoundException("Transaction does not exist in record!");
        }
    }
}
