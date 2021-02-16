package com.expensetracker.filters;

import com.expensetracker.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null) {
            String authHeaderTokens[] = authorizationHeader.split("Bearer ");
            if (authHeaderTokens.length > 1 && authHeaderTokens[1] != null) {
                String authHeaderToken = authHeaderTokens[1];
                try {
                    Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY).parseClaimsJws(authHeaderToken).getBody();
                    httpServletRequest.setAttribute("userId", Integer.parseInt(claims.get("userId").toString()));
                } catch (Exception ex) {
                    httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid or expired authorization header!");
                    return;
                }

            } else {
                httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid authorization header, expected bearer [token]");
                return;
            }
        } else {
            httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization header is missing!");
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
