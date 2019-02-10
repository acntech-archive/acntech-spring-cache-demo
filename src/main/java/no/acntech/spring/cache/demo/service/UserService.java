package no.acntech.spring.cache.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import no.acntech.spring.cache.demo.domain.User;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final Cache usersCache;

    public UserService(Cache usersCache) {
        this.usersCache = usersCache;
    }

    public List<User> getUsers(List<String> namesForSlowService, List<String> namesForSuperSlowService) {
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

        logger.info(String.format("Returning %d users from cache", users.size()));
        return users;
    }

}
