package com.expensetracker.repositories;

import com.expensetracker.domain.User;
import com.expensetracker.exceptions.AuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_CREATE = "INSERT INTO USERS_TBL(USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)" +
            "VALUES (NEXTVAL('USERS_SEQ'), ?, ?, ?, ?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM USERS_TBL WHERE EMAIL = ?";
    private static final String SQL_FIND_USER_BY_ID = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD FROM USERS_TBL " +
            "WHERE USER_ID = ?";
    private static final String SQL_FIND_USER_BY_EMAIL = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD FROM USERS_TBL " +
            "WHERE EMAIL = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User(rs.getString("USER_ID"),
                rs.getString("FIRST_NAME"),
                rs.getString("LAST_NAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"));
    });

    @Override
    public String create(String firstName, String lastName, String email, String password) throws AuthException {
        String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt(07));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, hashPassword);
                return ps;
            }, keyHolder);

            return (String) keyHolder.getKeys().get("USER_ID");
        } catch (Exception ex) {
            throw new AuthException("Failed to create user.");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws AuthException {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_USER_BY_EMAIL, new Object[]{email}, userRowMapper);
            if (!BCrypt.checkpw(password, user.getPassword()))
                throw new AuthException("Invalid email/password!");

            return user;
        } catch (EmptyResultDataAccessException ex) {
            throw new AuthException("Invalid email/password!");
        }
    }

    @Override
    public Integer getCountByEmail(String email) throws AuthException {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[]{email}, Integer.class);
    }

    @Override
    public User findById(String userId) throws AuthException {
        return jdbcTemplate.queryForObject(SQL_FIND_USER_BY_ID, new  Object[]{userId}, userRowMapper);
    }
}
