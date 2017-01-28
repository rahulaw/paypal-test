package com.paypal.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rahulaw on 28/01/17.
 */
public class UserConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory masterAdserverDB = new DataSourceFactory();

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory slaveAdserverDB = new DataSourceFactory();

    public DataSourceFactory getSlaveDataSourceFactory() {
        return slaveAdserverDB;
    }

    public DataSourceFactory getMasterDataSourceFactory() {
        return masterAdserverDB;
    }

    @Getter
    private DatabaseConfig databaseConfig = new DatabaseConfig();

    @Getter
    public static class DatabaseConfig {

        private String driverClass = "com.mysql.jdbc.Driver";

        private String user = "root";

        private String password = "";

        private String url = "jdbc:mysql://localhost:3306/sgv1";

        private Map<String, String> properties = new HashMap<>();

        private int maxWaitForConnectionMs = 1000;

        private String validationQuery = "/* Health Check */ SELECT 1";

        private int minSize = 1;

        private int maxSize = 8;

        private boolean checkConnectionWhileIdle = false;

        private int checkConnectionHealthWhenIdleForMs = 10*1000;

        private int closeConnectionIfIdleForMs = 60*1000;

        @JsonProperty
        private boolean defaultReadOnly = false;

        @JsonProperty
        private ImmutableList<String> connectionInitializationStatements = ImmutableList.of();

        @JsonProperty
        private boolean autoCommentsEnabled = true;

    }


}