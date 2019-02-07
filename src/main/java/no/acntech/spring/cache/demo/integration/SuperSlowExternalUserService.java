package no.acntech.spring.cache.demo.integration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import no.acntech.spring.cache.demo.domain.User;

@Service
public class SuperSlowExternalUserService {

    private final long SLEEP_IN_SECONDS = 6;

    public List<User> getUsers(List<String> names) {
        LocalDateTime timeNow = LocalDateTime.now();

        List<User> users = names.stream().map(name -> new User(name, timeNow, "SuperSlowExternalUserService")).collect(Collectors.toList());
        try {
            TimeUnit.SECONDS.sleep(SLEEP_IN_SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return users;
    }
}
