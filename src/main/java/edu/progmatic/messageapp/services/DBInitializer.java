package edu.progmatic.messageapp.services;


import edu.progmatic.messageapp.modell.Authority;
import edu.progmatic.messageapp.modell.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;

@Service
public class DBInitializer {
    private UserService userService;
    @PersistenceContext
    EntityManager em;

    @Autowired
    public DBInitializer(UserService userService) {
        this.userService = userService;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onAppStartup(ContextRefreshedEvent ev) throws ServletException {
        DBInitializer me = ev.getApplicationContext().getBean(DBInitializer.class);
        me.settings();
    }

    @Transactional
    public void settings() {
        if (em.createQuery("select u from User u", User.class).getResultList().isEmpty()) {
            User admin = new User("admin", "admin", "admin@admin.com", LocalDate.now());
            User user = new User("user", "user", "user@user.com", LocalDate.now());
            Authority myadmin = new Authority("ROLE_ADMIN");
            Authority myuser = new Authority("ROLE_USER");
            em.persist(myadmin);
            em.persist(myuser);
            admin.setAuthorities(Collections.singleton(myadmin));
            user.setAuthorities(Collections.singleton(myuser));
            //TODO Create admin/user role in sql authorities table
            userService.createUser(admin);
            userService.createUser(user);
        }
    }
}