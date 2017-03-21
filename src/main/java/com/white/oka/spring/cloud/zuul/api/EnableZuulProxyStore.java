package com.white.oka.spring.cloud.zuul.api;

import com.white.oka.spring.cloud.zuul.support.ZuulProxyStoreConfiguration;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables the Zuul Proxy with list of routes being provided from configured
 * {@link com.white.oka.spring.cloud.zuul.store.ZuulRouteStore} instance, besides that this pretty much resembles the
 * standard {@link org.springframework.cloud.netflix.zuul.EnableZuulProxy}.
 *
 * @author Jakub Narloch
 */
@EnableCircuitBreaker
@EnableDiscoveryClient
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ZuulProxyStoreConfiguration.class)
public @interface EnableZuulProxyStore {
}