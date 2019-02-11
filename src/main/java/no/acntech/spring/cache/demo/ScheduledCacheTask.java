package no.acntech.spring.cache.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import no.acntech.spring.cache.demo.service.CacheService;

@Configuration
@EnableScheduling()
public class ScheduledCacheTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledCacheTask.class);
    private final CacheService cacheService;

    @Autowired
    public ScheduledCacheTask(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Scheduled(fixedDelayString = "${scheduling.cachetask.fixedDelay.inMillis}")
    public void launchJob() {
        logger.info("Starting scheduled cache refresh");
        cacheService.refreshAllCacheStores();
        logger.info("Finished scheduled cache refresh");
    }
}
