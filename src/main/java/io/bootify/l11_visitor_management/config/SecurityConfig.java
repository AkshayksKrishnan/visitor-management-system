package io.bootify.l11_visitor_management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    protected  void configure(HttpSecurity httpSecurity ) throws Exception{
        httpSecurity.authorizeRequests()
                .antMatchers("/api/user-panel/**").hasAuthority("RESIDENT")
                .antMatchers("/api/admin-panel/**").hasAuthority("ADMIN")
                .antMatchers("/api/gatekeeper-panel/**").hasAuthority("GATEKEEPER");
        httpSecurity.cors().and().csrf().disable();
        httpSecurity.formLogin();
        httpSecurity.httpBasic();
    }


}
