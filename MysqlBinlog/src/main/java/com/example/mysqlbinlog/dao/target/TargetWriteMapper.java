package com.example.mysqlbinlog.dao.target;

import org.apache.ibatis.annotations.Insert;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface TargetWriteMapper {
    @Insert("<script>insert into ${tableName} values " +
            "<foreach item='item' index='index' collection='params' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach> " +
            "</script>")
    int syncWriteNewRows(String tableName, List<Serializable> params);
}
