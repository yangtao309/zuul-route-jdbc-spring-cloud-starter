package com.white.oka.spring.cloud.zuul.route;

import com.white.oka.spring.cloud.zuul.store.ZuulRouteStore;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;

import java.util.LinkedHashMap;

/**
 * A simple {@link org.springframework.cloud.netflix.zuul.filters.RouteLocator} that is being populated from configured
 * {@link ZuulRouteStore}.
 *
 * @author Jakub Narloch
 */
public class StoreRefreshableRouteLocator extends DiscoveryClientRouteLocator {

  private final ZuulRouteStore store;

  /**
   * Creates new instance of {@link StoreRefreshableRouteLocator}
   * @param servletPath the application servlet path
   * @param discovery the discovery service
   * @param properties the zuul properties
   * @param store the route store
   */
  public StoreRefreshableRouteLocator(String servletPath,
                                      DiscoveryClient discovery,
                                      ZuulProperties properties,
                                      ZuulRouteStore store) {
    super(servletPath, discovery, properties);
    this.store = store;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected LinkedHashMap<String, ZuulProperties.ZuulRoute> locateRoutes() {
    LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>();
    routesMap.putAll(super.locateRoutes());
    for (ZuulProperties.ZuulRoute route : store.findAll()) {
      routesMap.put(route.getPath(), route);
    }
    return routesMap;
  }
}
