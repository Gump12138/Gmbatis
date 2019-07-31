package com.gant.gmbatis;

import com.gant.gmbatis.utils.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


/**
 * 配置类
 * ————————————————————————————
 * 作用：
 * 1.获取jdbc连接所需要的配置信息
 * 2.获取dao层上Sql语句
 *
 * @author 甘明波
 * @date 2019-07-23
 */
public class Configuration {

    /**
     * 日志
     */
    private final Logger logger;
    /**
     * 配置：数据库连接信息
     */
    private Map<String, String> dataSourceInfoMapper;
    /**
     * 映射：实体类与数据库表
     */
    private Map<String, String> tableMapper;

    /**
     * init
     */
    public Configuration(String driver, String url, String username, String password) {
        logger = LoggerFactory.getLogger(this.getClass());
        setDataSourceInfoMapper(driver, url, username, password)
                .setTableMapper();
    }

    /**
     * 获得dao层接口的代理实现类
     *
     * @param tClass     dao层接口class对象
     * @param sqlSession Gmbatis对外开放的统一接口
     * @return 代理实现类
     */
    public Object getMapper(Class tClass, SqlSession sqlSession) {
        logger.info("————————————————————————");
        logger.info("开始获取dao层接口的代理实现类");
        return MapperProxyFactory.newInstance(tClass, sqlSession);
    }

    /**
     * 获得所有表名并与实体类相映射
     */
    public void setTableMapper() {
        Map<String, String> tableMapper = new HashMap<>(20);
        ResultSet set = null;
        try (Connection connection = JdbcUtil.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "show tables";
            set = statement.executeQuery(sql);
            String[] urlSplit = JdbcUtil.getUrl().split("/");
            String columnLabel = urlSplit[urlSplit.length - 1];
            while (set.next()) {
                String value = set.getString("Tables_in_" + columnLabel);
                String[] split = value.split("_");
                StringBuilder key = new StringBuilder();
                for (int i = 1; i < split.length; i++) {
                    key.append(split[i]);
                }
                tableMapper.put(key.toString(), value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        this.tableMapper = tableMapper;
        logger.info("初始化实体类与数据库表的映射关系");
    }

    public Map<String, String> getTableMapper() {
        return tableMapper;
    }

    public String getDriver() {
        return dataSourceInfoMapper.get("driver");
    }

    public String getUrl() {
        return dataSourceInfoMapper.get("url");
    }

    public String getUsername() {
        return dataSourceInfoMapper.get("username");
    }

    public String getPassword() {
        return dataSourceInfoMapper.get("password");
    }

    public Map<String, String> getDataSourceInfoMapper() {
        return dataSourceInfoMapper;
    }

    public Logger getLogger() {
        return logger;
    }

    public Configuration setDataSourceInfoMapper(String driver, String url, String username, String password) {
        Map<String, String> dataSourceInfoMapper = new HashMap<>(10);
        dataSourceInfoMapper.put("driver", driver);
        dataSourceInfoMapper.put("url", url);
        dataSourceInfoMapper.put("username", username);
        dataSourceInfoMapper.put("password", password);
        this.dataSourceInfoMapper = dataSourceInfoMapper;
        logger.info("初始化数据库连接信息");
        return this;
    }
}
