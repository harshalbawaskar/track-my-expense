package com.expensetracker.repositories;

import com.expensetracker.domain.User;
import com.expensetracker.exceptions.AuthException;

public interface UserRepository {
    String create(String firstName, String lastName, String email, String password) throws AuthException;
    User findByEmailAndPassword(String email, String password) throws AuthException;
    Integer getCountByEmail(String email) throws AuthException;
    User findById(String userId) throws AuthException;
}
