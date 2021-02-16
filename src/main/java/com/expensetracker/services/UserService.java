package com.expensetracker.services;

import com.expensetracker.domain.User;
import com.expensetracker.exceptions.AuthException;

public interface UserService {
    User validate(String email, String password) throws AuthException;

    User register(String firstName, String lastName, String email, String password) throws AuthException;
}
