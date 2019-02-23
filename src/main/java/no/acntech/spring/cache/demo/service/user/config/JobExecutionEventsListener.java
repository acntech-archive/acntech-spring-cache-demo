package no.acntech.spring.cache.demo.service.user.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import no.acntech.spring.cache.demo.domain.JobExecutionEvent;

@Component
public class JobExecutionEventsListener {

    private static final Logger logger = LoggerFactory.getLogger(JobExecutionEventsListener.class);

    @EventListener(value = JobExecutionEvent.class)
    public void listenToJobExecutionEvents(final JobExecutionEvent event) {
        logger.info(event.getMessage());
    }
}
