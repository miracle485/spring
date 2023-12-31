package com.example.mysqlbinlog.config;

import com.example.mysqlbinlog.config.iface.DataSourceConfigIface;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@ConfigurationProperties(prefix = "data-sync.config.source")
public class SourceMySqlConfig implements DataSourceConfigIface {

    private String userName;

    private String password;

    private String jdbcUrl;

    private final String pattern = "jdbc:(?<db>\\w+):.*((//)|@)(?<host>.+):(?<port>\\d+)(/|(;DatabaseName=)|:)(?<dbName>\\w+)\\??.*";
    Pattern namePattern = Pattern.compile(pattern);

    private String host;
    private int port = -1;

    public String getHost() {
        if (host == null) {
            parseUrl(jdbcUrl);
        }

        return host;
    }

    public int getPort() {
        if (port == -1) {
            parseUrl(jdbcUrl);
        }
        return port;
    }

    private void parseUrl(String jdbcUrl) {
        Matcher matcher = namePattern.matcher(jdbcUrl);
        if (matcher.find()) {
            host = matcher.group("host");
            port = NumberUtils.createInteger(matcher.group("port"));
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
}
