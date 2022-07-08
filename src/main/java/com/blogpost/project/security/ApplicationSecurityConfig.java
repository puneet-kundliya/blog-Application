package com.blogpost.project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/newpost","post/delete/{postId}","/post/edit/{id}").hasAnyRole("ADMIN","AUTHOR")
                .antMatchers(HttpMethod.POST,"/api/posts/save").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/posts/{id}","/api/posts/{postId}/comments/{commentId}").authenticated()
                .antMatchers(HttpMethod.DELETE,"/api/posts/{postId}","/api/posts/{postId}/comments/{commentId}").authenticated()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic()
                .and()
                .exceptionHandling().accessDeniedPage("/error")
                .and()
                .formLogin().loginPage("/loginPage")
                .loginProcessingUrl("/processLogin")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/");
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
