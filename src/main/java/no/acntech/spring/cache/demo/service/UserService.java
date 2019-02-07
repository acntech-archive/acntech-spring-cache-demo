package no.acntech.spring.cache.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.acntech.spring.cache.demo.domain.User;
import no.acntech.spring.cache.demo.integration.SlowExternalUserService;
import no.acntech.spring.cache.demo.integration.SuperSlowExternalUserService;

@Service
public class UserService {

    @Autowired
    private SlowExternalUserService slowExternalUserService;

    @Autowired
    private SuperSlowExternalUserService superSlowExternalUserService;

    public List<User> getUsers(List<String> namesForSlowService, List<String> namesForSuperSlowService) {
        List<User> users = new ArrayList<>();
        users.addAll(slowExternalUserService.getUsers(namesForSlowService));
        users.addAll(superSlowExternalUserService.getUsers(namesForSuperSlowService));
        return users;
    }
}
