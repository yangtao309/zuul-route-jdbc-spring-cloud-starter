package com.white.oka.spring.cloud.zuul.support;

import com.white.oka.spring.cloud.zuul.store.JdbcZuulRouteStore;
import com.white.oka.spring.cloud.zuul.store.ZuulRouteStore;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;

/**
 * Created by yangtao on 17/3/21.
 */
@Configuration
@ConditionalOnProperty(value = "zuul.store.jdbc.enabled", havingValue = "true", matchIfMissing = false)
public class JdbcZuulStoreAutoConfiguration {
  @Bean
  @ConditionalOnMissingBean
  public ZuulRouteStore mysqlZuulRouteStore(JdbcOperations jdbcOperations) {
    return new JdbcZuulRouteStore(jdbcOperations);
  }
}