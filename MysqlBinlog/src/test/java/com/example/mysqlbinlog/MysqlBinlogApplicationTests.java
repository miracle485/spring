package com.example.mysqlbinlog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class MysqlBinlogApplicationTests {
    private String pattern="jdbc:(?<db>\\w+):.*((//)|@)(?<host>.+):(?<port>\\d+)(/|(;DatabaseName=)|:)(?<dbName>\\w+)\\??.*";
    private String jdbcUrl = "jdbc:mysql://localhost:3306/information_schema";
    @Test
    void contextLoads() throws URISyntaxException {


        String pattern = "jdbc:(?<type>[a-z]+)://(?<host>[a-zA-Z0-9-//.]+):(?<port>[0-9]+)/(?<database>[a-zA-Z0-9_]+)?";
        Pattern namePattern = Pattern.compile(pattern);
        Matcher dateMatcher = namePattern.matcher(jdbcUrl);
        dateMatcher.find();
        System.out.println(dateMatcher.group("db"));
    }

}
