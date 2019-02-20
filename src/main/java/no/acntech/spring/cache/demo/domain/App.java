package no.acntech.spring.cache.demo.domain;

public class App {

    private String system;
    private String name;

    public App(String system, String name) {
        this.system = system;
        this.name = name;
    }

    public App() {
        this.system = "";
        this.name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}
