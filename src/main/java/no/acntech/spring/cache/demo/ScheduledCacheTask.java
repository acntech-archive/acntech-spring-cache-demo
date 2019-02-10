package no.acntech.spring.cache.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import no.acntech.spring.cache.demo.integration.SlowExternalUserService;
import no.acntech.spring.cache.demo.integration.SuperSlowExternalUserService;

@Configuration
@EnableScheduling
public class ScheduledCacheTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledCacheTask.class);
    private SlowExternalUserService slowExternalUserService;
    private SuperSlowExternalUserService superSlowExternalUserService;

    @Autowired
    public ScheduledCacheTask(SlowExternalUserService slowExternalUserService,
                              SuperSlowExternalUserService superSlowExternalUserService) {
        this.slowExternalUserService = slowExternalUserService;
        this.superSlowExternalUserService = superSlowExternalUserService;
    }

    @Scheduled(fixedDelayString = "${scheduling.cachetask.fixedDelay.inMillis}")
    public void scheduleRefreshCache() {
        refreshCaches();
    }

    private void refreshCaches() {
        logger.info("Starting refreshing the cache");

        List<String> namesForSlowService = Arrays.asList("Tom", "Jon", "Peter");
        List<String> namesForSuperSlowService = Arrays.asList("Anna", "Isabelle", "Lizzi", "Madonna", "Gaga");
        List<CompletableFuture> futures = new ArrayList<>();

        // Call async
        futures.add(slowExternalUserService.getUsers(namesForSlowService)
                .thenAccept(users -> logger.info(String.format("Refreshed cache with %d from SlowService", users.size()))));
        futures.add(superSlowExternalUserService.getUsers(namesForSuperSlowService)
                .thenAccept(users -> logger.info(String.format("Refreshed cache with %d from SuperSlowService", users.size()))));

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        logger.info("Finished refreshing the cache");
    }
}
