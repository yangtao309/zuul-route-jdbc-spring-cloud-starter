/**
 * Copyright (c) 2015 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.white.oka.spring.cloud.zuul.store;

import com.google.common.collect.Sets;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Created by yangtao on 17/3/21.
 */
public class JdbcZuulRouteStore implements ZuulRouteStore {

  /**
   * The shared instance of row mapper.
   */
  private static final ZuulRouteRowMapper ZUUL_ROUTE_MAPPER = new ZuulRouteRowMapper();

  /**
   * The default table name.
   */
  private static final String DEFAULT_TABLE_NAME = "zuul_routes";

  /**
   * Casandra template.
   */
  private final JdbcOperations jdbcOperations;

  /**
   * The table name.
   */
  private final String table;

  /**
   * Creates new instance of {@link JdbcZuulRouteStore}.
   *
   * @param jdbcOperations the jdbc template
   */
  public JdbcZuulRouteStore(JdbcOperations jdbcOperations) {
    this(jdbcOperations, DEFAULT_TABLE_NAME);
  }

  /**
   * Creates new instance of {@link JdbcZuulRouteStore}.
   *
   * @param jdbcOperations      the jdbc template
   * @param table               the table name
   * @throws IllegalArgumentException if {@code keyspace} is {@code null}
   *                                  or {@code table} is {@code null} or empty
   */
  public JdbcZuulRouteStore(JdbcOperations jdbcOperations, String table) {
    Assert.notNull(jdbcOperations, "Parameter 'jdbcOperations' can not be null.");
    Assert.hasLength(table, "Parameter 'table' can not be empty.");
    this.jdbcOperations = jdbcOperations;
    this.table = table;
  }

  @Override
  public List<ZuulProperties.ZuulRoute> findAll() {
    final String sql = "select * from " + table;
    return jdbcOperations.query(sql, ZUUL_ROUTE_MAPPER);
  }

  /**
   * Cassandra's {@link ZuulProperties.ZuulRoute} raw mapper.
   *
   * @author Jakub Narloch
   */
  private static class ZuulRouteRowMapper implements RowMapper<ZuulProperties.ZuulRoute> {
    @Override
    public ZuulProperties.ZuulRoute mapRow(ResultSet rs, int rowNum) throws SQLException {
      final String rsSensitiveHeaders = rs.getString("sensitive_headers");
      Set<String> sensitiveHeaders = Sets.newHashSet();
      if (StringUtils.isNotEmpty(rsSensitiveHeaders)) {
        String[] arr = rsSensitiveHeaders.split(",");
        for (String key : arr) {
          sensitiveHeaders.add(key);
        }
      }

      ZuulProperties.ZuulRoute route = new ZuulProperties.ZuulRoute(
          rs.getString("id"),
          rs.getString("path"),
          rs.getString("service_id"),
          rs.getString("url"),
          rs.getBoolean("strip_prefix"),
          rs.getBoolean("retryable"),
          sensitiveHeaders
      );

      // TODO (tmack): found error in the above ZuulProperties.ZuulRoute constructor...
      route.setCustomSensitiveHeaders(route.getSensitiveHeaders() != null && route.getSensitiveHeaders().size() > 0);

      return route;
    }
  }
}