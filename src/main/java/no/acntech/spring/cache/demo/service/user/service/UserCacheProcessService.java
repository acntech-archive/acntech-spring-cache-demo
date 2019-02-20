package no.acntech.spring.cache.demo.service.user.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import no.acntech.spring.cache.demo.domain.App;
import no.acntech.spring.cache.demo.domain.User;
import no.acntech.spring.cache.demo.integration.SlowExternalUserService;
import no.acntech.spring.cache.demo.integration.SuperSlowExternalUserService;
import no.acntech.spring.cache.demo.service.app.service.AppService;

@Service
public class UserCacheProcessService {

    private final SlowExternalUserService slowExternalUserService;
    private final SuperSlowExternalUserService superSlowExternalUserService;
    private final AppService appService;
    private final Cache usersCache;
    private static final Logger logger = LoggerFactory.getLogger(UserCacheProcessService.class);

    @Autowired
    public UserCacheProcessService(SlowExternalUserService slowExternalUserService,
                                   SuperSlowExternalUserService superSlowExternalUserService,
                                   AppService appService, Cache usersCache) {
        this.slowExternalUserService = slowExternalUserService;
        this.superSlowExternalUserService = superSlowExternalUserService;
        this.appService = appService;
        this.usersCache = usersCache;
    }

    @Async
    public CompletableFuture<String> refreshUsersForAllAppsInTestFromSlowService() {
        logger.debug("Starting refreshing cached users from Slow Service for all apps in TEST env");

        List<App> apps = appService.getAll();

        for (App app : apps) {
            slowExternalUserService.getUsers(app, "TEST");
        }

        logger.debug("Finished refreshing cached users from Slow Service for all apps in TEST env");
        return CompletableFuture.completedFuture("Done");
    }

    @Async
    public CompletableFuture<String> refreshUsersForAllAppsInTestFromSuperSlowService() {
        logger.debug("Starting refreshing cached users from Super Slow Service for all apps in TEST env");

        List<App> apps = appService.getAll();

        for (App app : apps) {
            superSlowExternalUserService.getUsers(app, "TEST");
        }

        logger.debug("Finished refreshing cached users from Super Slow Service for all apps in TEST env");
        return CompletableFuture.completedFuture("Done");
    }

    @Async
    public CompletableFuture<String> refreshUsersForAllAppsInProdFromSlowService() {
        logger.debug("Starting refreshing cached users from Slow Service for all apps in PROD env");

        List<App> apps = appService.getAll();

        for (App app : apps) {
            slowExternalUserService.getUsers(app, "PROD");
        }

        logger.debug("Finished refreshing cached users from Slow Service for all apps in PROD env");
        return CompletableFuture.completedFuture("Done");
    }

    @Async
    public CompletableFuture<String> refreshUsersForAllAppsInProdFromSuperSlowService() {
        logger.debug("Starting refreshing cached users from Super Slow Service for all apps in PROD env");

        List<App> apps = appService.getAll();

        for (App app : apps) {
            superSlowExternalUserService.getUsers(app, "PROD");
        }

        logger.debug("Finished refreshing cached users from Super Slow Service for all apps in PROD env");
        return CompletableFuture.completedFuture("Done");
    }

    @Async
    public void refreshUsersForAppInEnv(App app, String env) {
        logger.debug("Starting refreshing cached users from Super Slow Service for all apps in PROD env");

        slowExternalUserService.getUsers(app, env);
        superSlowExternalUserService.getUsers(app, env);

        logger.debug("Finished refreshing cached users from Super Slow Service for all apps in PROD env");
    }

    List<User> getUsersFromCache(App app, String env) {
        List<User> users = new ArrayList<>();

        String slowCacheKey = MessageFormat.format("slow-{0}-{1}-{2}", app.getSystem(), app.getName(), env);
        String superSlowCacheKey = MessageFormat.format("superslow-{0}-{1}-{2}", app.getSystem(), app.getName(), env);

        List<User> cachedUsersFromSlowService = usersCache.get(slowCacheKey, List.class);
        List<User> cachedUsersFromSuperSlowService = usersCache.get(superSlowCacheKey, List.class);

        if (cachedUsersFromSlowService != null) {
            users.addAll(cachedUsersFromSlowService);
        }

        if (cachedUsersFromSuperSlowService != null) {
            users.addAll(cachedUsersFromSuperSlowService);
        }

        return users;
    }
}
