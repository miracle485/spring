package com.example.mysqlbinlog.dao;

import com.example.mysqlbinlog.mode.TableColumInfo;
import com.example.mysqlbinlog.mode.TableInfo;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 读取information中关于表的配置信息
 */
public interface InformationMapper {
    @Select("select COLUMN_NAME, ORDINAL_POSITION from information_schema.columns where table_name = #{tableName}")
    @Results({
            @Result(property = "columnName", column = "COLUMN_NAME"),
            @Result(property = "ordinalPosition", column = "ORDINAL_POSITION")
    })
    List<TableColumInfo> getTableInfoByName(String tableName);

    @Select("select TABLE_NAME from information_schema.tables where table_schema = #{dataBaseName}")
    List<String> getTableListByDatabaseName(String dataBaseName);
}
