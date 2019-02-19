package no.acntech.spring.cache.demo.integration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import no.acntech.spring.cache.demo.domain.User;

import static no.acntech.spring.cache.demo.service.config.UserCacheConfig.USERS_CACHE;

@Service
public class SuperSlowExternalUserService {

    private static final Logger logger = LoggerFactory.getLogger(SuperSlowExternalUserService.class);
    private final long SLEEP_IN_SECONDS = 20;

    @Async
    @CachePut(value = USERS_CACHE)
    public CompletableFuture<List<User>> getUsers(List<String> namesForSuperSlowService) {
        logger.debug("Calling Super Slow external User Service");

        LocalDateTime timeNow = LocalDateTime.now();
        List<User> users = namesForSuperSlowService.stream().map(name -> new User(name, timeNow, "SuperSlowExternalUserService")).collect(Collectors.toList());
        simulateSlowService();

        logger.debug("Returning list of users");
        return CompletableFuture.completedFuture(users);
    }

    private void simulateSlowService() {
        try {
            TimeUnit.SECONDS.sleep(SLEEP_IN_SECONDS);
        } catch (InterruptedException e) {
            // do nothing
        }
    }
}
