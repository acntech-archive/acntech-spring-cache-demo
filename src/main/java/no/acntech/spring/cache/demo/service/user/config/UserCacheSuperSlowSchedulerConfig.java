package no.acntech.spring.cache.demo.service.user.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import no.acntech.spring.cache.demo.service.user.service.UserCacheProcessService;

@ConditionalOnProperty(name = "scheduling.cachejob.superslow.enabled", havingValue = "true")
@Configuration
public class UserCacheSuperSlowSchedulerConfig {

    private static final Logger logger = LoggerFactory.getLogger(UserCacheSuperSlowSchedulerConfig.class);
    private final UserCacheProcessService userCacheProcessService;

    @Autowired
    public UserCacheSuperSlowSchedulerConfig(UserCacheProcessService userCacheProcessService) {
        this.userCacheProcessService = userCacheProcessService;
    }

    @Scheduled(fixedDelayString = "${scheduling.cachejob.test.superslow.fixedDelay.inMillis}")
    public void refreshUsersForAllAppsInTestFromSuperSlowServiceJob() {
        logger.info("Starting scheduled cache refresh in TEST from Super Slow Service");
        userCacheProcessService.refreshUsersForAllAppsInTestFromSuperSlowService().thenAccept(users -> logger.info("Finished scheduled cache refresh in TEST from Super Slow Service"));
    }

    @Scheduled(fixedDelayString = "${scheduling.cachejob.prod.superslow.fixedDelay.inMillis}")
    public void refreshUsersForAllAppsInProdFromSuperSlowServiceJob() {
        logger.info("Starting scheduled cache refresh in PROD from Super Slow Service");
        userCacheProcessService.refreshUsersForAllAppsInProdFromSuperSlowService().thenAccept(users -> logger.info("Finished scheduled cache refresh in PROD from Super Slow Service"));
    }

}
