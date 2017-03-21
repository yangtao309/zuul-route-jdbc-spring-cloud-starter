package com.white.oka.spring.cloud.zuul.store;

import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.List;

/**
 * An general abstraction of persistent storage capable for storing zuul routes. The concrete implementation of this
 * interface will implement provider specific data retrieval.
 *
 * @author Jakub Narloch
 */
public interface ZuulRouteStore {

  /**
   * Retrieves the list of all stored Zuul routes from the persistence storage.
   *
   * @return the list of zuul routes
   */
  List<ZuulProperties.ZuulRoute> findAll();
}
