package no.acntech.spring.cache.demo.service.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import no.acntech.spring.cache.demo.domain.JobExecutionEvent;
import no.acntech.spring.cache.demo.service.user.service.UserCacheProcessService;

@ConditionalOnProperty(name = "scheduling.cachejob.slow.enabled", havingValue = "true")
@Configuration
public class UserCacheSlowSchedulerConfig implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;
    private final UserCacheProcessService userCacheProcessService;

    @Autowired
    public UserCacheSlowSchedulerConfig(UserCacheProcessService userCacheProcessService) {
        this.userCacheProcessService = userCacheProcessService;
    }

    @Scheduled(fixedDelayString = "${scheduling.cachejob.test.slow.fixedDelay.inMillis}")
    public void refreshUsersForAllAppsInTestFromSlowServiceJob() {
        applicationEventPublisher.publishEvent(new JobExecutionEvent(this, "Starting scheduled cache refresh in TEST from Slow Service"));
        userCacheProcessService
                .refreshUsersForAllAppsInTestFromSlowService()
                .thenAccept(users -> applicationEventPublisher.publishEvent(new JobExecutionEvent(this, "Finished scheduled cache refresh in TEST from Slow Servic")));
    }

    @Scheduled(fixedDelayString = "${scheduling.cachejob.prod.slow.fixedDelay.inMillis}")
    public void refreshUsersForAllAppsInProdFromSlowServiceJob() {
        applicationEventPublisher.publishEvent(new JobExecutionEvent(this, "Starting scheduled cache refresh in PROD from Slow Service"));
        userCacheProcessService
                .refreshUsersForAllAppsInProdFromSlowService()
                .thenAccept(users -> applicationEventPublisher.publishEvent(new JobExecutionEvent(this, "Finished scheduled cache refresh in PROD from Slow Servic")));
    }

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
