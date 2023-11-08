package com.example.mysqlbinlog.config;

import org.apache.commons.lang3.StringUtils;

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

}
