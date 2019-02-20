package no.acntech.spring.cache.demo.integration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import no.acntech.spring.cache.demo.domain.App;
import no.acntech.spring.cache.demo.domain.User;
import no.acntech.spring.cache.demo.service.app.service.AppService;

import static no.acntech.spring.cache.demo.service.user.config.UserCacheConfig.USERS_CACHE;

@Service
public class SuperSlowExternalUserService {

    private static final Logger logger = LoggerFactory.getLogger(SuperSlowExternalUserService.class);
    private final long SLEEP_IN_SECONDS = 10;
    private HashMap<String, List<AppUsers>> envAppUsers;
    private AppService appService;

    @Autowired
    public SuperSlowExternalUserService(AppService appService) {
        this.appService = appService;
    }

    @CachePut(value = USERS_CACHE, keyGenerator = "appUsersInEnvKeyGenerator")
    public List<User> getUsers(App app, String env) {
        logger.debug("Calling Super Slow external User Service");

        createMockData();

        List<User> users = new ArrayList<>();
        List<AppUsers> appUsersList = envAppUsers.get(env);

        if (appUsersList != null) {
            appUsersList
                    .stream()
                    .filter(au -> au.getApp().getSystem().equals(app.getSystem()) && au.getApp().getName().equals(app.getName()))
                    .map(AppUsers::getUsers).forEach(users::addAll);
        }

        simulateSlowService();

        logger.debug("Returning list of users");
        return users;
    }

    private void createMockData() {
        envAppUsers = new HashMap<>();

        List<App> apps = appService.getAll();
        LocalDateTime timeNow = LocalDateTime.now();

        List<User> users11 = new ArrayList<>();
        users11.add(new User("User11-A", timeNow, "SuperSlowExternalUserService"));

        List<User> users12 = new ArrayList<>();
        users12.add(new User("User12-A", timeNow, "SuperSlowExternalUserService"));
        users12.add(new User("User12-B", timeNow, "SuperSlowExternalUserService"));
        users12.add(new User("User12-C", timeNow, "SuperSlowExternalUserService"));

        List<User> users13 = new ArrayList<>();
        users13.add(new User("User13-A", timeNow, "SuperSlowExternalUserService"));
        users13.add(new User("User13-B", timeNow, "SuperSlowExternalUserService"));

        List<AppUsers> testAppUsers = new ArrayList<>();
        testAppUsers.add(new AppUsers(apps.get(0), users11));
        testAppUsers.add(new AppUsers(apps.get(1), users12));
        testAppUsers.add(new AppUsers(apps.get(2), users13));

        List<AppUsers> prodAppUsers = new ArrayList<>();
        prodAppUsers.add(new AppUsers(apps.get(0), users11));

        envAppUsers.put("TEST", testAppUsers);
        envAppUsers.put("PROD", prodAppUsers);
    }

    private void simulateSlowService() {
        try {
            TimeUnit.SECONDS.sleep(SLEEP_IN_SECONDS);
        } catch (InterruptedException e) {
            // do nothing
        }
    }
}
