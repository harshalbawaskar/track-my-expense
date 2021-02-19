package com.expensetracker;

import com.expensetracker.filters.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
public class ExpenseTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> registrationBean() {
		FilterRegistrationBean<CorsFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedHeader("*");

		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

		CorsFilter corsFilter = new CorsFilter(urlBasedCorsConfigurationSource);

		filterFilterRegistrationBean.setFilter(corsFilter);
		filterFilterRegistrationBean.setOrder(0);

		return filterFilterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
		FilterRegistrationBean<AuthFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		AuthFilter authFilter = new AuthFilter();
		filterRegistrationBean.setFilter(authFilter);

		// Protect below Url from unauthorized access.
		filterRegistrationBean.addUrlPatterns("/api/categories/*");
		return filterRegistrationBean;
	}
}
