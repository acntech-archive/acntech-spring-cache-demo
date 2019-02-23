package no.acntech.spring.cache.demo.service.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import no.acntech.spring.cache.demo.domain.JobExecutionEvent;
import no.acntech.spring.cache.demo.service.user.service.UserCacheProcessService;

@ConditionalOnProperty(name = "scheduling.cachejob.superslow.enabled", havingValue = "true")
@Configuration
public class UserCacheSuperSlowSchedulerConfig implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;
    private final UserCacheProcessService userCacheProcessService;

    @Autowired
    public UserCacheSuperSlowSchedulerConfig(UserCacheProcessService userCacheProcessService) {
        this.userCacheProcessService = userCacheProcessService;
    }

    @Scheduled(fixedDelayString = "${scheduling.cachejob.test.superslow.fixedDelay.inMillis}")
    public void refreshUsersForAllAppsInTestFromSuperSlowServiceJob() {
        applicationEventPublisher.publishEvent(new JobExecutionEvent(this, "Starting scheduled cache refresh in TEST from Super Slow Service"));
        userCacheProcessService
                .refreshUsersForAllAppsInTestFromSuperSlowService()
                .thenAccept(users -> applicationEventPublisher.publishEvent(new JobExecutionEvent(this, "Finished scheduled cache refresh in TEST from Super Slow Service")));
    }

    @Scheduled(fixedDelayString = "${scheduling.cachejob.prod.superslow.fixedDelay.inMillis}")
    public void refreshUsersForAllAppsInProdFromSuperSlowServiceJob() {
        applicationEventPublisher.publishEvent(new JobExecutionEvent(this, "Starting scheduled cache refresh in PROD from Super Slow Service"));
        userCacheProcessService
                .refreshUsersForAllAppsInProdFromSuperSlowService()
                .thenAccept(users -> applicationEventPublisher.publishEvent(new JobExecutionEvent(this, "Finished scheduled cache refresh in PROD from Super Slow Service")));
    }

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
