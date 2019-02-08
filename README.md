# spring-cache-demo
Small Spring Boot demo app demonstrating how to use Spring Cache when integrating with slow external services.

## High level architecture
1. There are two slow services, 1. `SlowExternalUserService` and 2. `SuperSlowExternalService`
2. `ScheduledCacheTask` is responsible for initiating the `Users` cache at startup and refreshing the cache every 10 seconds (by using `@Scheduled(fixedDelay)`). This is done by calling the slow services and response is rewritten in the `Users` cache (by using `@CachePut`)
3. `UserResource` (REST API) is retrieving the data from `UserService`, which reads cached data by using the `CacheManager`
4. It takes about 10 seconds after startup before data from `SlowExternalUserService` is available and 20 seconds from `SuperSlowExternalService`

## Getting started
````bash
$ ./mvnw spring-boot:run
````

## REST APIs
- GET http://localhost:8080/users - return users from `Users` cache
- DELETE http://localhost:8080/users - clears `Users` cache

