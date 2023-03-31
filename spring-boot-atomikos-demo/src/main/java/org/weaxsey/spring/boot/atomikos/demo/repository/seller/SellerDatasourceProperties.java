/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.atomikos.demo.repository.seller;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;


@Data
@ConfigurationProperties(prefix = "seller.datasource")
public class SellerDatasourceProperties {
    private String url;
    private String user;
    private String password;
    private String type = "mysql";
}
