/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.atomikos.external.demo.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Properties;


public interface ExternalDatabaseService {

    JsonNode executeExternalSql(Properties dbProperties, String rawSql);

}
