package me.liye.framework.datasource.id;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author knight@momo.com
 */

@Data
@ConfigurationProperties("server-framework.id-generator.datasource")
public class IdGeneratorDatasourceProperties {

    /**
     * Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.
     */
    private String driverClassName;

    /**
     * JDBC URL of the database.
     */
    private String url;

    /**
     * Login username of the database.
     */
    private String username;

    /**
     * Login password of the database.
     */
    private String password;
}
