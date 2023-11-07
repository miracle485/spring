package com.example.mysqlbinlog.config;

public class DataSyncMysqlTaskConfig {
    private String targetUrlWithPort;
    private String targetUserName;
    private String targetPassWord;
    private String targetDataBaseName;
    private String targetTableName;

    public String getTargetUrlWithPort() {
        return targetUrlWithPort;
    }

    public void setTargetUrlWithPort(String targetUrlWithPort) {
        this.targetUrlWithPort = targetUrlWithPort;
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

}
