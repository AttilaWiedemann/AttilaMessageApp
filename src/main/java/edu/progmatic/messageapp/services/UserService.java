package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//Az adatbázisból keresse ki a loaduserbyusername a felhasználót
@Service
public class UserService implements UserDetailsService {

    @PersistenceContext
    EntityManager em;

    /*
    public UserService(){
        createUser(new User());
    }*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = em.createQuery("select u from User u where u.username=:username", User.class).setParameter("username", username).getSingleResult();
            return user;
        } catch (NoResultException e) {
            //logger.debug("User was not found: {}", username);
            throw new UsernameNotFoundException("User was not found: " + username);
        }
    }

    public boolean userExists(String user) {
        try {
            em.createQuery("select u from User u where u=:user", User.class).setParameter("user", user).getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getUsernames(){
        List<User> userlist = em.createQuery("select u from User u").getResultList();
        Stream<String> userNames = userlist.stream().map(user -> user.getUsername());
        return userNames.collect(Collectors.toList());
    }


    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    private PasswordEncoder passwordEncoder;
    @Transactional
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        em.persist(user);
    }
}