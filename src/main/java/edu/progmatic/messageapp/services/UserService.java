/*package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {

    Map<String, User> users = new HashMap<>();

    public UserService(){
        createUser(new User());
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

    public void createUser(User user){
        users.put(user.getUsername(), user);
    }
}

 */