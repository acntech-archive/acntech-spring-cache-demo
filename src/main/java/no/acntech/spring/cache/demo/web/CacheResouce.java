package no.acntech.spring.cache.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import no.acntech.spring.cache.demo.service.CacheService;
import no.acntech.spring.cache.demo.service.ExternalServiceName;

@RestController
@RequestMapping(value = "/cache", produces = MediaType.APPLICATION_JSON_VALUE)
public class CacheResouce {

    private static final Logger logger = LoggerFactory.getLogger(CacheResouce.class);
    private CacheService cacheService;

    @Autowired
    public CacheResouce(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @RequestMapping(value = "/refresh/{externalServiceName}", method = RequestMethod.GET)
    public void refreshCache(@PathVariable String externalServiceName) {
        logger.debug("Refreshing the cache for {}", externalServiceName);
        cacheService.refreshCacheStore(ExternalServiceName.valueOf(externalServiceName));
    }

}
