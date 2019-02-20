package no.acntech.spring.cache.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.spring.cache.demo.domain.App;
import no.acntech.spring.cache.demo.service.app.service.AppService;
import no.acntech.spring.cache.demo.service.user.service.UserCacheProcessService;

@RestController
@RequestMapping(value = "/cache", produces = MediaType.APPLICATION_JSON_VALUE)
public class CacheResouce {

    private static final Logger logger = LoggerFactory.getLogger(CacheResouce.class);
    private UserCacheProcessService userCacheProcessService;
    private AppService appService;

    @Autowired
    public CacheResouce(UserCacheProcessService userCacheProcessService, AppService appService) {
        this.userCacheProcessService = userCacheProcessService;
        this.appService = appService;
    }

    @RequestMapping(value = "/refresh/{systemName}/{appName}/{env}", method = RequestMethod.GET)
    public void refreshCache(@PathVariable("systemName") String systemName,
                             @PathVariable("appName") String appName,
                             @PathVariable("env") String env) {
        logger.debug("Refreshing the cache for system {}, app {} and in env {}", systemName, appName, env);
        App app = appService.get(systemName, appName);
        userCacheProcessService.refreshUsersForAppInEnv(app, env);
    }

}
