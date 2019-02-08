package no.acntech.spring.cache.demo.service;

import java.util.ArrayList;
import java.util.List;

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

        List<User> cachedUsersFromSlowService = usersCache.get(namesForSlowService, List.class);
        List<User> cachedUsersFromSuperSlowService = usersCache.get(namesForSuperSlowService, List.class);

        if (cachedUsersFromSlowService != null) {
            users.addAll(cachedUsersFromSlowService);
        }

        if (cachedUsersFromSuperSlowService != null) {
            users.addAll(cachedUsersFromSuperSlowService);
        }

        logger.info(String.format("Returning %d users from cache", users.size()));
        return users;
    }

}
