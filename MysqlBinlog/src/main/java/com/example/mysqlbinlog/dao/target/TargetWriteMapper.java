package com.example.mysqlbinlog.dao.target;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface TargetWriteMapper {
    @Insert("<script>insert into ${tableName} values " +
            "<foreach item='item' index='index' collection='params' separator=','>" +
            "<foreach item='row' index='i' collection='item' open='(' separator=',' close=')'>"+
            "#{row}" +
            "</foreach> " +
            "</foreach>" +
            "</script>")
    int syncWriteNewRows(String tableName, List<Serializable[]> params);


    @Select("select TABLE_NAME from information_schema.tables where table_schema = #{dataBaseName}")
    List<String> getTableListByDatabaseName(String dataBaseName);
}
