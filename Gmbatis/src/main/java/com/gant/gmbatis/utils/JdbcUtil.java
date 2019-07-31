package com.gant.gmbatis.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ResourceBundle;

/**
 * JDBC工具类
 *
 * @author  甘明波
 * @date 2019-07-25
 */
public class JdbcUtil {

    private JdbcUtil() {
    }

    private static final Logger logger = LoggerFactory.getLogger(JdbcUtil.class);
    private static ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
    private static String driver = bundle.getString("jdbc.driver");
    private static String url = bundle.getString("jdbc.url");
    private static String username = bundle.getString("jdbc.username");
    private static String password = bundle.getString("jdbc.password");

    static {
        try {
            Class.forName(driver);
            logger.info("注册驱动");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized static Connection getConnection() throws SQLException {
        logger.info("获得数据库连接");
        return DriverManager.getConnection(url, username, password);
    }

    public static void close(Connection connection) {
        close(connection, null, null);
    }

    public static void close(Connection connection, Statement statement) {
        close(connection, statement, null);
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
                logger.info("关闭结果集");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
                logger.info("关闭操作语句");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
                logger.info("关闭数据库连接");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getUrl() {
        return url;
    }
}
