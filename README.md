# zuul-route-jdbc-spring-cloud-starter
> A Spring Cloud jdbc store for Zuul routes.
将zuul的routes信息存储在关系型数据库中，以方便定义zuul dashboard进行运维管理工作。

## Features

Extends the Spring Cloud's `DiscoveryClientRouteLocator` with capabilities of loading routes out of the configured Cassandra database.

Instead of configuring your routes through `zuul.routes` like follows:

```yaml
zuul:
  ignoredServices: '*'
  routes:
    resource:
      path: /api/**
      serviceId: rest-service
    oauth2:
      path: /uaa/**
      serviceId: oauth2-service
      stripPrefix: false
```

You can store the routes in mysql.

Keep in mind that the other properties except for routes are still relevant.

```yaml
zuul:
  ignoredServices: '*'
  store:
    jdbc:
      enabled: true
```

## Setup

Make sure the version of String Cloud Starter Zuul your project is using is at compatible with 1.2.4.RELEASE which this 
project is extending.

Add the Spring Cloud starter to your project:

```xml
<dependency>
  <groupId>io.github.yangtao309</groupId>
  <artifactId>zuul-route-jdbc-spring-cloud-starter</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Connect to mysql and create a keyspace:

```sql(mysql)

CREATE TABLE zuul_routes(
    id VARCHAR(50),
    path VARCHAR(500),
    service_id VARCHAR(50),
    url VARCHAR(500),
    strip_prefix tinyint(1),
    retryable tinyint(1),
    sensitive_headers VARCHAR(500),
    PRIMARY KEY(id)
);
```

Register `JdbcOperations` bean within your application:

```java
@SpringBootApplication
public static class Application {

  @Bean
  public DataSource dataSource(
      @Value("${spring.datasource.url}") String url,
      @Value("${spring.datasource.username}") String username,
      @Value("${spring.datasource.password}") String password,
      @Value("${spring.datasource.maxActive}") int maxActive) {

    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setMaxActive(maxActive);
    return dataSource;
  }

  @Bean
  public JdbcOperations mysqlJdbcTemplate(@Autowired DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}
```

Configure the jdbc to be used for loading the Zuul routes:

```yaml
zuul:
  store:
    jdbc:
      enabled: true
```

Finally enable the Zuul proxy with `@EnableZuulProxyStore` - use this annotation as a replacement for standard `@EnableZuulProxy`:

```java
@EnableZuulProxyStore
@SpringBootApplication
public static class Application {

    ...
}
```

## Properties

```yaml
zuul.store.jdbc.enabled=true# false by default
```

## License

Apache 2.0
