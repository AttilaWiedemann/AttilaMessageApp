package edu.progmatic.messageapp;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

@Component
@Scope(
        scopeName = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionTest {
    private String name;
    private HashMap<String, Integer> userData;
    private int counter = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public HashMap<String, Integer> getUserData() {
        return userData;
    }

    public void setUserData(HashMap<String, Integer> userData) {
        this.userData = userData;
    }
}
