package no.acntech.spring.cache.demo.service.user.config;

import org.springframework.util.StringUtils;

public class AppUsersInEnvKey {

    private String className;
    private String systemName;
    private String appName;
    private String env;

    private AppUsersInEnvKey(String className, String systemName, String appName, String env) {
        this.className = className;
        this.systemName = systemName;
        this.appName = appName;
        this.env = env;
    }

    private String getClassName() {
        return className;
    }

    private String getSystemName() {
        return systemName;
    }

    private String getAppName() {
        return appName;
    }

    public String getEnv() {
        return env;
    }

    String getKey() {
        return StringUtils.arrayToDelimitedString(new String[]{getClassName(), getSystemName(), getAppName(), getEnv()}, "-");
    }

    static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String className;
        private String systemName;
        private String appName;
        private String env;

        private Builder() {

        }

        Builder className(String className) {
            this.className = className;
            return this;
        }

        Builder systemName(String systemName) {
            this.systemName = systemName;
            return this;
        }

        Builder appName(String appName) {
            this.appName = appName;
            return this;
        }

        Builder env(String env) {
            this.env = env;
            return this;
        }

        AppUsersInEnvKey build() {
            return new AppUsersInEnvKey(className, systemName, appName, env);
        }
    }
}
