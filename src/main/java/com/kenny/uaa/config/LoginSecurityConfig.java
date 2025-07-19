package com.kenny.uaa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenny.uaa.security.auth.ldap.LDAPMultiAuthenticationProvider;
import com.kenny.uaa.security.filter.RestAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.Map;
@Slf4j
@RequiredArgsConstructor
@Configuration
@Order(100)
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter {
    private final ObjectMapper objectMapper;
    private final DaoAuthenticationProvider authenticationProvider;
    private final LDAPMultiAuthenticationProvider ldapAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(req -> req.anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll())
                .addFilterAt(restAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/perform_logout"))
                .rememberMe(rememberMe -> rememberMe.tokenValiditySeconds(30*24*3600).rememberMeCookieName("someKeyToRemember"));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/public/**", "/error")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    private RestAuthenticationFilter restAuthenticationFilter() throws Exception {
        RestAuthenticationFilter filter = new RestAuthenticationFilter(objectMapper);
        filter.setAuthenticationSuccessHandler(jsonLoginSuccessHandler());
        filter.setAuthenticationFailureHandler(jsonLoginFailureHandler());
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/authorize/login");
        return filter;
    }

    private static LogoutSuccessHandler jsonLogoutSuccessHandler() {
        return (request, response, authentication) -> {
            if (authentication != null && authentication.getDetails() != null) {
                request.getSession().invalidate();
            }
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("msg", "Logout success!")));
            log.debug("Logout success!");
        };
    }

    private AuthenticationFailureHandler jsonLoginFailureHandler() {
        return (request, response, e) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            Map<String, String> errData = Map.of("title", "auth failed", "details", e.getMessage());
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().println(objectMapper.writeValueAsString(errData));
        };
    }

    private AuthenticationSuccessHandler jsonLoginSuccessHandler() {
        return (request, response, authentication) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().println(objectMapper.writeValueAsString(authentication));
            log.debug("Auth successful");
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ldapAuthenticationProvider);
        auth.authenticationProvider(authenticationProvider);
    }
}
