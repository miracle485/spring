package com.example.mysqlbinlog.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DataSyncMysqlTaskConfig {
    private String targetJdbcUrl;
    private String targetUserName;
    private String targetPassWord;
    private String targetDataBaseName;
    private String targetTableName;

    public String getTargetJdbcUrl() {
        return targetJdbcUrl;
    }

    public void setTargetJdbcUrl(String targetJdbcUrl) {
        this.targetJdbcUrl = targetJdbcUrl;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public String getTargetPassWord() {
        return targetPassWord;
    }

    public void setTargetPassWord(String targetPassWord) {
        this.targetPassWord = targetPassWord;
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
        return StringUtils.isEmpty(getTargetJdbcUrl()) || StringUtils.isEmpty(getTargetTableName())
                || StringUtils.isEmpty(getTargetDataBaseName()) || StringUtils.isEmpty(getTargetUserName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DataSyncMysqlTaskConfig that = (DataSyncMysqlTaskConfig) o;

        return new EqualsBuilder().append(getTargetJdbcUrl(), that.getTargetJdbcUrl()).append(getTargetUserName(), that.getTargetUserName()).append(getTargetPassWord(), that.getTargetPassWord()).append(getTargetDataBaseName(), that.getTargetDataBaseName()).append(getTargetTableName(), that.getTargetTableName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getTargetJdbcUrl()).append(getTargetUserName()).append(getTargetPassWord()).append(getTargetDataBaseName()).append(getTargetTableName()).toHashCode();
    }
}
