package com.expensetracker.resources;

import com.expensetracker.Constants;
import com.expensetracker.domain.User;
import com.expensetracker.services.UserService;
import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureBaseRSA;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController // Add controller and response body annotation
@RequestMapping("api/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, Object> userMap) {
        String firstName = (String)userMap.get("firstName");
        String lastName = (String)userMap.get("lastName");
        String email = (String)userMap.get("email");
        String password = (String)userMap.get("password");

        User user = userService.register(firstName, lastName, email, password);
        return new ResponseEntity<Map<String, String>>(generateJWTToken(user), HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, Object> userMap) {
        String email = (String)userMap.get("email");
        String password = (String)userMap.get("password");

        User user = userService.validate(email, password);

        return new ResponseEntity<Map<String, String>>(generateJWTToken(user), HttpStatus.OK);
    }

    public Map<String, String> generateJWTToken(User user) {
        long timeInMillis = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timeInMillis))
                .setExpiration(new Date(timeInMillis + Constants.TOKEN_VALIDITY))
                .claim("userId", user.getUserId())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("email", user.getEmail())
                .compact();

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return tokenMap;
    }
}
