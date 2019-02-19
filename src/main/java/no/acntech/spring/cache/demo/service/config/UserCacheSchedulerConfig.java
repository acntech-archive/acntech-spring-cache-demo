package no.acntech.spring.cache.demo.service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import no.acntech.spring.cache.demo.service.UserCacheProcessService;

@Configuration
@EnableScheduling()
public class UserCacheSchedulerConfig {

    private static final Logger logger = LoggerFactory.getLogger(UserCacheSchedulerConfig.class);
    private final UserCacheProcessService userCacheProcessService;

    @Autowired
    public UserCacheSchedulerConfig(UserCacheProcessService userCacheProcessService) {
        this.userCacheProcessService = userCacheProcessService;
    }

    @Scheduled(fixedDelayString = "${scheduling.cachetask.fixedDelay.inMillis}")
    public void launchJob() {
        logger.info("Starting scheduled cache refresh");
        userCacheProcessService.refreshAllCacheStores();
        logger.info("Finished scheduled cache refresh");
    }
}
