package no.acntech.spring.cache.demo.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.spring.cache.demo.domain.App;
import no.acntech.spring.cache.demo.domain.User;
import no.acntech.spring.cache.demo.service.app.service.AppService;
import no.acntech.spring.cache.demo.service.user.service.UserService;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersResource {

    private static final Logger logger = LoggerFactory.getLogger(UsersResource.class);
    private UserService userService;
    private AppService appService;

    @Autowired
    public UsersResource(UserService userService, AppService appService) {
        this.userService = userService;
        this.appService = appService;
    }

    @RequestMapping(value = "/{systemName}/{appName}/{env}", method = RequestMethod.GET)
    public List<User> getUsers(@PathVariable("systemName") String systemName,
                               @PathVariable("appName") String appName,
                               @PathVariable("env") String env) {
        logger.debug("Retrieving users from cache");
        App app = appService.get(systemName, appName);
        return userService.getUsers(app, env);
    }

}
