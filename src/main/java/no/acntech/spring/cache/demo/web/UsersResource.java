package no.acntech.spring.cache.demo.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.spring.cache.demo.domain.User;
import no.acntech.spring.cache.demo.service.UserService;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CacheConfig(cacheNames = "users")
public class UsersResource {

    @Autowired
    private UserService userService;

    @Cacheable
    @RequestMapping(method = RequestMethod.GET)
    public List<User> getUsers() {
        List<String> namesForSlowService = Arrays.asList("Tom", "Jon", "Peter");

        List<String> namesForSuperSlowService = Arrays.asList("Anna", "Isabelle", "Lizzi");

        return userService.getUsers(namesForSlowService, namesForSuperSlowService);
    }

    @CacheEvict(value = "users", allEntries = true)
    @RequestMapping(method = RequestMethod.DELETE)
    public void clearUsersCache() {

    }

}
