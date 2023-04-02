/*
 * Copyright (c) 2023, Weaxs
 *
 */

package org.weaxsey.spring.boot.bitronix.demo.repository.buyer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "buyer.datasource")
public class BuyerDatasourceProperties {
    private String url;
    private String user;
    private String password;
    private String type = "mysql";
}
