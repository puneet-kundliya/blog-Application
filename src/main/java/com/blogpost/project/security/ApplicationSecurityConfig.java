package com.blogpost.project.security;

import com.blogpost.project.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/newpost","post/delete/{postId}","/post/edit/{id}").hasAnyRole("ADMIN","AUTHOR")
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
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

    //    @Autowired
//    CustomUserDetailService customUserDetailService;
//
//    @Autowired
//    UserDetailsService userDetailsService;


//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http
//                .authorizeRequests()
//                .antMatchers("/newpost","post/delete/{postId}","/post/edit/{id}").hasAnyRole("ADMIN","AUTHOR")
//                .antMatchers("/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .exceptionHandling().accessDeniedPage("/error")
//                .and()
//                .formLogin().loginPage("/loginPage")
//                .loginProcessingUrl("/processLogin")
//                .and()
//                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/");
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
//    }
}
