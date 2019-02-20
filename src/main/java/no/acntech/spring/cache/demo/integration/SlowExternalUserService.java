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
public class SlowExternalUserService {

    private static final Logger logger = LoggerFactory.getLogger(SlowExternalUserService.class);
    private final long SLEEP_IN_SECONDS = 5;
    private HashMap<String, List<AppUsers>> envAppUsers;
    private AppService appService;

    @Autowired
    public SlowExternalUserService(AppService appService) {
        this.appService = appService;
    }

    @CachePut(value = USERS_CACHE, keyGenerator = "appUsersInEnvKeyGenerator")
    public List<User> getUsers(App app, String env) {
        logger.debug("Calling Slow external User Service");

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
        users11.add(new User("User11-X", timeNow, "SlowExternalUserService"));

        List<User> users12 = new ArrayList<>();
        users12.add(new User("User12-X", timeNow, "SlowExternalUserService"));

        List<User> users13 = new ArrayList<>();
        users13.add(new User("User13-X", timeNow, "SlowExternalUserService"));
        users13.add(new User("User13-Y", timeNow, "SlowExternalUserService"));

        List<User> users21 = new ArrayList<>();
        users21.add(new User("User21-X", timeNow, "SlowExternalUserService"));
        users21.add(new User("User22-Y", timeNow, "SlowExternalUserService"));

        List<AppUsers> testAppUsers = new ArrayList<>();
        testAppUsers.add(new AppUsers(apps.get(0), users11));
        testAppUsers.add(new AppUsers(apps.get(1), users12));
        testAppUsers.add(new AppUsers(apps.get(2), users13));
        testAppUsers.add(new AppUsers(apps.get(3), users21));

        List<AppUsers> prodAppUsers = new ArrayList<>();
        prodAppUsers.add(new AppUsers(apps.get(3), users21));

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
