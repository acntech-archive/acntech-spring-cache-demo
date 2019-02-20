package no.acntech.spring.cache.demo.integration;

import java.util.List;

import no.acntech.spring.cache.demo.domain.App;
import no.acntech.spring.cache.demo.domain.User;

public class AppUsers {

    private App app;
    private List<User> users;

    public AppUsers(App app, List<User> users) {
        this.app = app;
        this.users = users;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
