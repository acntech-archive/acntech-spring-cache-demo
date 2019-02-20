package no.acntech.spring.cache.demo.service.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import no.acntech.spring.cache.demo.domain.App;

@Service
public class AppService {

    private List<App> apps;

    public AppService() {
        apps = new ArrayList<>();

        apps.add(new App("system1", "app11"));
        apps.add(new App("system1", "app12"));
        apps.add(new App("system1", "app13"));
        apps.add(new App("system2", "app21"));
    }

    public List<App> getAll() {
        return apps;
    }

    public App get(String system, String name){
        return apps.stream().filter(app -> app.getSystem().equals(system) && app.getName().equals(name)).findFirst().orElse(new App());
    }

}
