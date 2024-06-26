package org.glazweq.demo.configs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().requestMatchers("/static/**");
    }
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((authorize) ->

                        authorize
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/login").permitAll()

                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/movies").permitAll()

                                .requestMatchers("/home").permitAll()
                                .requestMatchers("/movieId={id}").permitAll()
                                .requestMatchers("/find-movie").permitAll()
                                .requestMatchers("/find-by-name-filter-page").permitAll()


                                .requestMatchers("/delete-review").permitAll()
                                .requestMatchers("/add-review").permitAll()
                                .requestMatchers("/add-new-review").permitAll()

                                .requestMatchers("/ban-user").hasRole("ADMIN")
                                .requestMatchers("/unban-user").hasRole("ADMIN")
                                .requestMatchers("/searchUsersByFilters").hasRole("ADMIN")
                                .requestMatchers("/admin-home").hasRole("ADMIN")
                                .requestMatchers("/userslist").hasRole("ADMIN")
                ).formLogin(
                        form -> form

                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/home")
                                .permitAll()

                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}