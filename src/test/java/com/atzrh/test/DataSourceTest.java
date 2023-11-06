package com.atzrh.test;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author zrh
 * @version 1.0.0
 * @title DataSourceTest
 * @description <TODO description class purpose>
 * @create 2023/11/6 14:57
 **/
public class DataSourceTest {
    public static void main(String[] args) throws Exception {
        Properties pro = new Properties();
        pro.load(DataSourceTest.class.getClassLoader().getResourceAsStream("druid.properties"));
        DataSource ds = DruidDataSourceFactory.createDataSource(pro);
        Connection conn = ds.getConnection();
        System.out.println(conn);
    }
}
