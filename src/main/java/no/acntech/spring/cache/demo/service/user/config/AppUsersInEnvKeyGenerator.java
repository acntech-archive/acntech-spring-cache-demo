package no.acntech.spring.cache.demo.service.user.config;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

import no.acntech.spring.cache.demo.domain.App;

public class AppUsersInEnvKeyGenerator implements KeyGenerator {

    @Override
    public String generate(Object target, Method method, Object... params) {
        return getKey(target.getClass().getSimpleName(), (App) params[0], (String) params[1]);
    }

    public static String getKey(String classSimpleName, App app, String env) {
        return AppUsersInEnvKey
                .builder()
                .className(classSimpleName)
                .systemName(app.getSystem())
                .appName(app.getName())
                .env(env)
                .build()
                .getKey();
    }

}
