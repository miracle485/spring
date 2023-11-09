package com.example.mysqlbinlog.config;

import com.example.mysqlbinlog.config.iface.DataSourceConfigIface;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DataSyncMysqlTaskConfig implements DataSourceConfigIface {
    private String jdbcUrl;
    private String userName;
    private String password;
    private String targetDataBaseName;
    private String targetTableName;

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
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

    public String getTargetDataBaseName() {
        return targetDataBaseName;
    }

    public void setTargetDataBaseName(String targetDataBaseName) {
        this.targetDataBaseName = targetDataBaseName;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    public boolean isInValidConfig() {
        return StringUtils.isEmpty(getJdbcUrl()) || StringUtils.isEmpty(getTargetTableName())
                || StringUtils.isEmpty(getTargetDataBaseName()) || StringUtils.isEmpty(getUserName());
    }

    @Override
    public String toString() {
        return "DataSyncMysqlTaskConfig{" +
                "targetJdbcUrl='" + jdbcUrl + '\'' +
                ", targetUserName='" + userName + '\'' +
                ", targetPassWord='" + password + '\'' +
                ", targetDataBaseName='" + targetDataBaseName + '\'' +
                ", targetTableName='" + targetTableName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DataSyncMysqlTaskConfig that = (DataSyncMysqlTaskConfig) o;

        return new EqualsBuilder().append(getJdbcUrl(), that.getJdbcUrl()).append(getUserName(), that.getUserName()).append(getPassword(), that.getPassword()).append(getTargetDataBaseName(), that.getTargetDataBaseName()).append(getTargetTableName(), that.getTargetTableName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getJdbcUrl()).append(getUserName()).append(getPassword()).append(getTargetDataBaseName()).append(getTargetTableName()).toHashCode();
    }
}
