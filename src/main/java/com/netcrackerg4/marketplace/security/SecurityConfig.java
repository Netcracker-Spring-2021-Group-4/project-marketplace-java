package com.netcrackerg4.marketplace.security;

import com.netcrackerg4.marketplace.exception.ExceptionHandlerFilter;
import com.netcrackerg4.marketplace.security.filter.JwtFilter;
import com.netcrackerg4.marketplace.security.filter.LoginFilter;
import com.netcrackerg4.marketplace.security.jwt.IJwtService;
import com.netcrackerg4.marketplace.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtFilter jwtFilter;
    private final IJwtService jwtUtil;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Autowired
    public SecurityConfig(JwtUtil jwtUtil, JwtFilter jwtFilter,
                          ExceptionHandlerFilter exceptionHandlerFilter) {
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
        this.exceptionHandlerFilter = exceptionHandlerFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .addFilterBefore(exceptionHandlerFilter, LoginFilter.class)
                .addFilter(new LoginFilter(authenticationManager(), jwtUtil))
                .addFilterAfter(jwtFilter, LoginFilter.class)

                .authorizeRequests()
                .antMatchers("/api/v*/public/**").permitAll()
                .antMatchers("/api/v*/admin/**").hasRole("ADMIN")
                .antMatchers("/api/v*/courier/**").hasRole("COURIER")
                .antMatchers("/api/v*/manager/**").hasAnyRole("PRODUCT_MGR", "ADMIN")
                .antMatchers("/api/v*/auth-costumer/**").hasRole("CUSTOMER")
                //maybe also auth-customer-manager branch
                //staff branch
                .antMatchers(HttpMethod.OPTIONS).permitAll()
            .anyRequest().authenticated();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.addAllowedMethod("PUT");
        corsConfiguration.addAllowedMethod("POST");
        corsConfiguration.addAllowedMethod("DELETE");

        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean(name = "pwdEncoder")
    public PasswordEncoder getPasswordEncoder() {
        DelegatingPasswordEncoder delPasswordEncoder = (DelegatingPasswordEncoder)
                PasswordEncoderFactories.createDelegatingPasswordEncoder();
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        delPasswordEncoder.setDefaultPasswordEncoderForMatches(bcryptPasswordEncoder);
        return delPasswordEncoder;
    }
}
