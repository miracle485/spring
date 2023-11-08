package com.example.mysqlbinlog.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class DataSyncElasticSearchConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private int port;
    private String host;
    private String userName;
    private String password;
    private String indexName;

    public DataSyncElasticSearchConfig() {
    }

    public DataSyncElasticSearchConfig(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DataSyncElasticSearchConfig that = (DataSyncElasticSearchConfig) o;

        return new EqualsBuilder().append(getPort(), that.getPort()).append(getHost(), that.getHost()).append(getUserName(), that.getUserName()).append(getPassword(), that.getPassword()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getPort()).append(getHost()).append(getUserName()).append(getPassword()).toHashCode();
    }

    public boolean isInValidConfig() {
        return StringUtils.isEmpty(getHost()) || StringUtils.isEmpty(getIndexName()) || getPort() <= 0;
    }
}
