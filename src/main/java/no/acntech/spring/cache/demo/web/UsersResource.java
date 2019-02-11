package no.acntech.spring.cache.demo.web;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.spring.cache.demo.domain.User;
import no.acntech.spring.cache.demo.service.UserService;

import static no.acntech.spring.cache.demo.CachingConfig.USERS_CACHE;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersResource {

    private static final Logger logger = LoggerFactory.getLogger(UsersResource.class);
    private UserService userService;

    @Autowired
    public UsersResource(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getUsers() {
        logger.debug("Calling UserService");

        List<String> namesForSlowService = Arrays.asList("Tom", "Jon", "Peter");
        List<String> namesForSuperSlowService = Arrays.asList("Anna", "Isabelle", "Lizzi", "Madonna", "Gaga");
        List<User> users = userService.getUsers(namesForSlowService, namesForSuperSlowService);

        logger.debug("Returning response");
        return users;
    }

    @CacheEvict(value = USERS_CACHE, allEntries = true)
    @RequestMapping(method = RequestMethod.DELETE)
    public void clearUsersCache() {

    }

}
