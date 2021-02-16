package com.expensetracker.services;

import com.expensetracker.domain.User;
import com.expensetracker.exceptions.AuthException;
import com.expensetracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service // Automatic bean detection, spring instantiates the bean
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User validate(String email, String password) throws AuthException {
        if (email != null)
            email = email.toLowerCase();

        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public User register(String firstName, String lastName, String email, String password) throws AuthException {
        if (email != null) {
            email = email.toLowerCase();
        }

        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if (!pattern.matcher(email).matches()) {
            throw new AuthException("Invalid email format.");
        }

        int emailCount = userRepository.getCountByEmail(email);
        if (emailCount > 1) {
            throw new AuthException("Email already registered.");
        }

        Integer userId = userRepository.create(firstName, lastName, email, password);
        return userRepository.findById(userId);
    }
}
