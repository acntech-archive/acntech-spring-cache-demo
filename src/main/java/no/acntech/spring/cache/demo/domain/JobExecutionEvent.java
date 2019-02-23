package no.acntech.spring.cache.demo.domain;

import org.springframework.context.ApplicationEvent;

public class JobExecutionEvent extends ApplicationEvent {

    private String message;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     * @param message the message to be logged for the job execution event
     */
    public JobExecutionEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
