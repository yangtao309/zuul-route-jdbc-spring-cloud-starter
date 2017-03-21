package com.white.oka.spring.cloud.zuul.support;

import com.white.oka.spring.cloud.zuul.route.StoreRefreshableRouteLocator;
import com.white.oka.spring.cloud.zuul.store.ZuulRouteStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.ZuulProxyConfiguration;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.context.annotation.Configuration;

/**
 * Registers a {@link org.springframework.cloud.netflix.zuul.filters.RouteLocator} that is being populated through
 * external store.
 *
 * @author Jakub Narloch
 */
@Configuration
public class ZuulProxyStoreConfiguration extends ZuulProxyConfiguration {

  @Autowired
  private ZuulRouteStore zuulRouteStore;

  @Autowired
  private DiscoveryClient discovery;

  @Autowired
  private ZuulProperties zuulProperties;

  @Autowired
  private ServerProperties server;

  @Override
  public DiscoveryClientRouteLocator routeLocator() {
    return new StoreRefreshableRouteLocator(server.getServletPath(), discovery, zuulProperties, zuulRouteStore);
  }
}
