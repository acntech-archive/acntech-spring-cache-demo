package no.acntech.spring.cache.demo;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import no.acntech.spring.cache.demo.domain.User;
import no.acntech.spring.cache.demo.integration.SlowExternalUserService;
import no.acntech.spring.cache.demo.integration.SuperSlowExternalUserService;

@Configuration
@EnableScheduling
public class ScheduledCacheTask {

    private SlowExternalUserService slowExternalUserService;
    private SuperSlowExternalUserService superSlowExternalUserService;
    private static final Logger logger = LoggerFactory.getLogger(ScheduledCacheTask.class);

    @Autowired
    public ScheduledCacheTask(SlowExternalUserService slowExternalUserService,
                              SuperSlowExternalUserService superSlowExternalUserService) {
        this.slowExternalUserService = slowExternalUserService;
        this.superSlowExternalUserService = superSlowExternalUserService;
    }

    @Scheduled(fixedDelayString = "${scheduling.cachetask.fixedDelay.inMillis}")
    public void scheduleRefreshCache() {
        logger.info("Starting refreshing the cache");

        List<String> namesForSlowService = Arrays.asList("Tom", "Jon", "Peter");
        List<String> namesForSuperSlowService = Arrays.asList("Anna", "Isabelle", "Lizzi", "Madonna", "Gaga");

        List<User> usersFromSlowService = slowExternalUserService.getUsers(namesForSlowService);
        logger.info(String.format("Refreshed cache with %d from SlowService", usersFromSlowService.size()));

        List<User> usersFromSuperSlowService = superSlowExternalUserService.getUsers(namesForSuperSlowService);
        logger.info(String.format("Refreshed cache with %d from SuperSlowService", usersFromSuperSlowService.size()));

        logger.info("Finished refreshing the cache");
    }
}
