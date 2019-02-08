package no.acntech.spring.cache.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import no.acntech.spring.cache.demo.domain.User;

import static no.acntech.spring.cache.demo.CachingConfig.USERS_CACHE;

@Service
public class UserService {

    private CacheManager cacheManager;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public List<User> getUsers(List<String> namesForSlowService, List<String> namesForSuperSlowService) {
        List<User> users = new ArrayList<>();

        Cache usersCache = cacheManager.getCache(USERS_CACHE);
        ArrayList cachedUsersFromSlowService = usersCache.get(namesForSlowService, ArrayList.class);
        ArrayList cachedUsersFromSuperSlowService = usersCache.get(namesForSuperSlowService, ArrayList.class);

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
