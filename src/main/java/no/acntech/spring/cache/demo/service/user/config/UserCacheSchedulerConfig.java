package no.acntech.spring.cache.demo.service.user.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import no.acntech.spring.cache.demo.service.user.service.UserCacheProcessService;

@Configuration
@EnableScheduling()
public class UserCacheSchedulerConfig {

    private static final Logger logger = LoggerFactory.getLogger(UserCacheSchedulerConfig.class);
    private final UserCacheProcessService userCacheProcessService;

    @Autowired
    public UserCacheSchedulerConfig(UserCacheProcessService userCacheProcessService) {
        this.userCacheProcessService = userCacheProcessService;
    }

    @Scheduled(fixedDelayString = "${scheduling.cachejob.test.slow.fixedDelay.inMillis}")
    public void refreshUsersForAllAppsInTestFromSlowServiceJob() {
        logger.info("Starting scheduled cache refresh in TEST from Slow Service");
        userCacheProcessService.refreshUsersForAllAppsInTestFromSlowService().thenAccept(users -> logger.info("Finished scheduled cache refresh in TEST from Slow Service"));
    }

    @Scheduled(fixedDelayString = "${scheduling.cachejob.test.superslow.fixedDelay.inMillis}")
    public void refreshUsersForAllAppsInTestFromSuperSlowServiceJob() {
        logger.info("Starting scheduled cache refresh in TEST from Super Slow Service");
        userCacheProcessService.refreshUsersForAllAppsInTestFromSuperSlowService().thenAccept(users -> logger.info("Finished scheduled cache refresh in TEST from Super Slow Service"));
    }

    @Scheduled(fixedDelayString = "${scheduling.cachejob.prod.slow.fixedDelay.inMillis}")
    public void refreshUsersForAllAppsInProdFromSlowServiceJob() {
        logger.info("Starting scheduled cache refresh in PROD from Slow Service");
        userCacheProcessService.refreshUsersForAllAppsInProdFromSlowService().thenAccept(users -> logger.info("Finished scheduled cache refresh in PROD from Slow Service"));
    }

    @Scheduled(fixedDelayString = "${scheduling.cachejob.prod.superslow.fixedDelay.inMillis}")
    public void refreshUsersForAllAppsInProdFromSuperSlowServiceJob() {
        logger.info("Starting scheduled cache refresh in PROD from Super Slow Service");
        userCacheProcessService.refreshUsersForAllAppsInProdFromSuperSlowService().thenAccept(users -> logger.info("Finished scheduled cache refresh in PROD from Super Slow Service"));
    }

}
