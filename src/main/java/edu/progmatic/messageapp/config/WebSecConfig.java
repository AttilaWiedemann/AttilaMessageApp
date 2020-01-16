package edu.progmatic.messageapp.config;

import edu.progmatic.messageapp.modell.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        User u = new User("admin", "a", "email", LocalDate.now());
        u.addAuthority("ROLE_ADMIN");
        manager.createUser(u);


       /*
        manager.createUser(User.withUsername("user").
                password("password").roles("USER").build());
        manager.createUser(User.withUsername("admin").
                password("password").roles("ADMIN").build());

        */
        return manager;
    }

    @SuppressWarnings("deprecation")
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //CharacterEncodingFilter filter = new CharacterEncodingFilter();
        //http.addFilterBefore(filter, CsrfFilter.class)
                http.formLogin()
                .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/messages", true)
                //.loginProcessingUrl("/login")
                .and()
                .logout()
                .logoutSuccessUrl("/home")
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/images/*"," /home", "/registration", "/messages", "/webjars/bootstrap/**", "/webjars/jquery/**", "/webjars/popper.js/**").permitAll()
                        .antMatchers("/statistics").hasRole("ADMIN")
                .anyRequest().authenticated();
    }


}