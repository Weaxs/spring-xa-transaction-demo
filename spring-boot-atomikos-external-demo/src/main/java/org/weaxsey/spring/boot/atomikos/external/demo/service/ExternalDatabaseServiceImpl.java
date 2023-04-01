/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.atomikos.external.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.weaxsey.spring.boot.atomikos.external.demo.util.ExternalDatasourceGenerator;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class ExternalDatabaseServiceImpl implements ExternalDatabaseService {

    @Override
    public JsonNode executeExternalSql(Properties dbProperties, String rawSql) {
        DataSource dataSource;
        try {
            dataSource = ExternalDatasourceGenerator.resolveExternalDataSource(dbProperties);
        } catch (InvocationTargetException | NoSuchMethodException 
                 | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(rawSql);
            ResultSet rs = statement.getResultSet();

            return rs == null ? JsonNodeFactory.instance.nullNode() : toJSON(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonNode toJSON(ResultSet rs) throws SQLException {
        ArrayNode array = JsonNodeFactory.instance.arrayNode();
        ResultSetMetaData metaData = rs.getMetaData();
        int numColumns = metaData.getColumnCount();
        Map<Integer, String> columnNameMap = new HashMap<>();
        for (int i = 1; i <= numColumns; i++) {
            String columnName = metaData.getColumnLabel(i);
            if (StringUtils.isEmpty(columnName)) {
                columnName = metaData.getColumnName(i);
            }
            columnNameMap.put(i, columnName);
        }
        while (rs.next()) {
            ObjectNode json = JsonNodeFactory.instance.objectNode();
            for (int i = 1; i <= numColumns; i++) {
                Object object = rs.getObject(i);
                json.set(columnNameMap.get(i), JsonNodeFactory.instance.pojoNode(object));
            }
            array.add(json);
        }
        return array;
    }
}
