package no.acntech.spring.cache.demo.domain;

import java.time.LocalDateTime;

public class User {

    private String name;
    private LocalDateTime lastUpdated;
    private String source;

    public User(String name, LocalDateTime lastUpdated, String source) {
        this.name = name;
        this.lastUpdated = lastUpdated;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
