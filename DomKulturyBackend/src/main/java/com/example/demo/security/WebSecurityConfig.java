package com.example.demo.security;

import com.example.demo.security.jwt.AuthEntryPointJwt;
import com.example.demo.security.jwt.AuthTokenFilter;
import com.example.demo.security.userDetails.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    private final AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AuthEntryPointJwt unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }


    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .antMatchers("/api/auth/signin").permitAll()
            .antMatchers("/api/auth/signup").hasRole("ADMIN")

            .antMatchers("/attendances", "/attendances/*").hasAnyRole("ADMIN","TEACHER")

            .antMatchers(HttpMethod.GET,"/lessons", "/lessons/*").permitAll()
            .antMatchers("/lessons", "/lessons/*", "/lessons/*/*").hasAnyRole("ADMIN","TEACHER")

            .antMatchers("/downloadFile/*").permitAll()
            .antMatchers("/uploadFile").hasRole("ADMIN")

            .antMatchers(HttpMethod.GET,"/rooms", "/rooms/**").permitAll()
            .antMatchers("/rooms", "/rooms/**").hasRole("ADMIN")

            .antMatchers(HttpMethod.DELETE,"/students", "/students/**").hasRole("ADMIN")
            .antMatchers("/students", "/students/**").hasAnyRole("ADMIN","TEACHER")

            .antMatchers(HttpMethod.GET,"/subjects", "/subjects/**").permitAll()
            .antMatchers(HttpMethod.POST,"/subjects").hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE,"/subjects/*").hasRole("ADMIN")
            .antMatchers("/subjects", "/subjects/**").hasAnyRole("ADMIN","TEACHER")

            .antMatchers(HttpMethod.GET,"/users", "/users/*").permitAll()
            .antMatchers(HttpMethod.DELETE,"/users/*").hasRole("ADMIN")
            .antMatchers(HttpMethod.PATCH,"/users/changeRole/*", "/users/generateNewPassword/*").hasRole("ADMIN")
            .antMatchers("/users", "/users/**").hasAnyRole("ADMIN","TEACHER")

            .antMatchers("/email/**").hasAnyRole("ADMIN","TEACHER")

            .antMatchers("/generateToken").permitAll() //TODO usunąć - metody testowe
            .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
