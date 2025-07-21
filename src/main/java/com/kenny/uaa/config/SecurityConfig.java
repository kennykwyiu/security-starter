package com.kenny.uaa.config;

import com.kenny.uaa.security.auth.ldap.LDAPMultiAuthenticationProvider;
import com.kenny.uaa.security.auth.ldap.LDAPUserRepo;
import com.kenny.uaa.security.jwt.JwtFilter;
import com.kenny.uaa.security.userdetails.UserDetailsPasswordServiceImpl;
import com.kenny.uaa.security.userdetails.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.Map;
@RequiredArgsConstructor
@Slf4j
@EnableWebSecurity
@Import(SecurityProblemSupport.class)
@Order(99)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityProblemSupport securityProblemSupport;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserDetailsPasswordServiceImpl userDetailsPasswordService;
    private final LDAPUserRepo ldapUserRepo;
    private final JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers(req -> req.mvcMatchers("/authorize/**", "/admin/**","/api/**"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(securityProblemSupport)
                        .accessDeniedHandler(securityProblemSupport)
                )
                .authorizeRequests(req -> req
                        .antMatchers("/authorize/**").permitAll()
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .antMatchers("/api/**").hasRole("USER")
//                    .antMatchers("/error").permitAll()
                    .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ldapMultiAuthenticationProvider());
        auth.authenticationProvider(authenticationProvider());

    }

    @Bean
    LDAPMultiAuthenticationProvider ldapMultiAuthenticationProvider() {
        return new LDAPMultiAuthenticationProvider(ldapUserRepo);
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsPasswordService(userDetailsPasswordService);
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        String idForDefault = "bcrypt";
        Map<String, PasswordEncoder> encoders = Map.of(
                idForDefault, new BCryptPasswordEncoder(),
                "SHA-1", new MessageDigestPasswordEncoder("SHA-1")
        );
        return new DelegatingPasswordEncoder(idForDefault, encoders);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/public/**", "/error", "/h2-console/**");
    }
}
