package no.acntech.spring.cache.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import no.acntech.spring.cache.demo.domain.User;
import no.acntech.spring.cache.demo.integration.SlowExternalUserService;
import no.acntech.spring.cache.demo.integration.SuperSlowExternalUserService;

@Service
public class CacheService {

    private SlowExternalUserService slowExternalUserService;
    private SuperSlowExternalUserService superSlowExternalUserService;
    private final Cache usersCache;
    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);
    private final List<String> namesForSlowService = Arrays.asList("Tom", "Jon", "Peter");
    private final List<String> namesForSuperSlowService = Arrays.asList("Anna", "Isabelle", "Lizzi", "Madonna", "Gaga");

    @Autowired
    public CacheService(SlowExternalUserService slowExternalUserService,
                        SuperSlowExternalUserService superSlowExternalUserService,
                        Cache usersCache) {
        this.slowExternalUserService = slowExternalUserService;
        this.superSlowExternalUserService = superSlowExternalUserService;
        this.usersCache = usersCache;
    }

    public void refreshAllCacheStores() {
        logger.debug("Starting refreshing all cache stores");
        List<CompletableFuture> futures = new ArrayList<>();

        // Call async
        futures.add(slowExternalUserService.getUsers(namesForSlowService)
                .thenAccept(users -> logger.debug("Refreshed cache with {} users from SlowService", users.size())));
        futures.add(superSlowExternalUserService.getUsers(namesForSuperSlowService)
                .thenAccept(users -> logger.debug("Refreshed cache with {} users from SuperSlowService", users.size())));

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        logger.debug("Finished refreshing all cache stores");
    }

    @Async
    public void refreshCacheStore(ExternalServiceName externalServiceName) {
        logger.debug("Starting refreshing cache store for external service {}", externalServiceName);
        CompletableFuture completableFuture;

        if (externalServiceName.equals(ExternalServiceName.SLOW)) {
            completableFuture = slowExternalUserService.getUsers(namesForSlowService)
                    .thenAccept(users -> logger.debug("Refreshed cache with {} users from SlowService", users.size()));
        } else {
            completableFuture = superSlowExternalUserService.getUsers(namesForSuperSlowService)
                    .thenAccept(users -> logger.debug("Refreshed cache with {} users from SuperSlowService", users.size()));
        }

        try {
            completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        logger.debug("Finished refreshing cache store for external service {}", externalServiceName);
    }

    List<User> getUsersFromCache(List<String> namesForSlowService, List<String> namesForSuperSlowService) {
        List<User> users = new ArrayList<>();

        // Since SlowExternalUserService and SuperSlowExternalUserService is async and has return type CompletableFuture, objects cached are also having the same type
        // However, there is no async involved in the cached values so we can safely retrieve result values and cast to List<User>
        CompletableFuture cachedUsersFromSlowService = usersCache.get(namesForSlowService, CompletableFuture.class);
        CompletableFuture cachedUsersFromSuperSlowService = usersCache.get(namesForSuperSlowService, CompletableFuture.class);

        if (cachedUsersFromSlowService != null) {
            users.addAll((List<User>) cachedUsersFromSlowService.getNow(List.class));
        }

        if (cachedUsersFromSuperSlowService != null) {
            users.addAll((List<User>) cachedUsersFromSuperSlowService.getNow(List.class));
        }

        return users;
    }
}
