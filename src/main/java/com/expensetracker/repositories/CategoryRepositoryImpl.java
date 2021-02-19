package com.expensetracker.repositories;

import com.expensetracker.domain.Category;
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
public class CategoryRepositoryImpl implements CategoryRepository {

    private static final String SQL_CREATE_CATEGORY = "INSERT INTO CATEGORIES_TBL (CATEGORY_ID, USER_ID, TITLE, DESCRIPTION)" +
            " VALUES(NEXTVAL('CATEGORIES_SEQ'), ?, ?, ?)";
    private static final String SQL_FIND_BY_CATEGORY_ID = "SELECT C.CATEGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, COALESCE(SUM(T.AMOUNT), 0) TOTAL_EXPENSE " +
            "FROM CATEGORIES_TBL C LEFT OUTER JOIN TRANSACTIONS_TBL T ON C.CATEGORY_ID = T.CATEGORY_ID " +
            "WHERE C.USER_ID = ? AND C.CATEGORY_ID = ? GROUP BY C.CATEGORY_ID";
    private static final String SQL_FIND_ALL_CATEGORIES = "SELECT C.CATEGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, COALESCE(SUM(T.AMOUNT), 0) TOTAL_EXPENSE " +
            "FROM CATEGORIES_TBL C LEFT OUTER JOIN TRANSACTIONS_TBL T ON C.CATEGORY_ID = T.CATEGORY_ID " +
            "WHERE C.USER_ID = ? GROUP BY C.CATEGORY_ID";
    private static final String SQL_UPDATE_CATEGORY = "UPDATE CATEGORIES_TBL SET TITLE = ?, DESCRIPTION = ? WHERE " +
            "USER_ID = ? AND CATEGORY_ID = ?";
    private static final String SQL_DELETE_CATEGORY = "DELETE FROM CATEGORIES_TBL WHERE USER_ID = ? AND CATEGORY_ID = ?";
    private static final String SQL_DELETE_CATEGORY_TRANSACTION = "DELETE FROM TRANSACTIONS_TBL WHERE CATEGORY_ID = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    private RowMapper<Category> categoryRowMapper = ((rs, rowNum) -> {
        return new Category(
                rs.getInt("CATEGORY_ID"),
                rs.getString("TITLE"),
                rs.getString("DESCRIPTION"),
                rs.getDouble("TOTAL_EXPENSE"),
                rs.getInt("USER_ID")
        );
    });

    @Override
    public Integer create(Integer userId, String title, String description) throws BadRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE_CATEGORY, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setString(2, title);
                ps.setString(3, description);
                return ps;
            }, keyHolder);

            return (Integer) keyHolder.getKeys().get("CATEGORY_ID");
        } catch (Exception ex) {
            throw new BadRequestException("Invalid request!");
        }
    }

    @Override
    public void updateCategory(Integer userId, Integer categoryId, Category category) throws BadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE_CATEGORY, new Object[]{category.getTitle(), category.getDescription(), userId, categoryId});
        } catch (Exception ex) {
            throw new BadRequestException("Invalid request");
        }
    }

    @Override
    public List<Category> findAllCategories(Integer userId) throws ResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL_CATEGORIES, new Object[]{userId}, categoryRowMapper);
    }

    @Override
    public Category findById(Integer userId, Integer categoryId) throws ResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_CATEGORY_ID, new Object[]{userId, categoryId}, categoryRowMapper);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Category not found!");
        }
    }

    public void removeAllCategoryTransactions(Integer userId, Integer categoryId) throws ResourceNotFoundException {
        jdbcTemplate.update(SQL_DELETE_CATEGORY_TRANSACTION, new Object[]{categoryId});
    }

    @Override
    public void removeCategoryById(Integer userId, Integer categoryId) throws ResourceNotFoundException {
        removeAllCategoryTransactions(userId, categoryId);
        Integer deleteRowCount = jdbcTemplate.update(SQL_DELETE_CATEGORY, new Object[]{userId, categoryId});
        if (deleteRowCount == 0) {
            throw new ResourceNotFoundException("Category does not exist in records!");
        }
    }
}
