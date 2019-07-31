package com.gant.gmbatis;

import com.gant.gmbatis.exception.GmbatisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * 创建SqlSession的工厂类
 * 2019-07-23
 *
 * @author 甘明波
 */
public class SqlSessionFactory {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(SqlSessionFactory.class);
    /**
     * 配置对象
     */
    private final Configuration configuration;

    /**
     * 私有构造，只能通过build方法创建Factory对象
     */
    private SqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * build工厂
     */
    public static SqlSessionFactory build(String file) {
        logger.info("————————————————");
        logger.info("开始读取资源文件");
        ResourceBundle bundle = ResourceBundle.getBundle(file);
        String driver = bundle.getString("jdbc.driver");
        String url = bundle.getString("jdbc.url");
        String username = bundle.getString("jdbc.username");
        String password = bundle.getString("jdbc.password");
        logger.info("资源文件读取完毕");
        return new SqlSessionFactory(new Configuration(driver, url, username, password));
    }

    /**
     * 获得一个Session对象
     * 默认--配置信息，一一绑定的执行器，关闭自动提交
     */
    public SqlSession openSession() {
        logger.info("获取SqlSession操作接口对象");
        if (configuration == null) {
            throw new GmbatisException("没有读取到文件");
        }
        return new SqlSession(configuration);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Logger getLogger() {
        return logger;
    }


}
