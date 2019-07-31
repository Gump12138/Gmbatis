package com.gant.gmbatis;

import com.gant.gmbatis.executor.AbstractExecutor;
import com.gant.gmbatis.executor.BaseExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * 统一的接口
 * 1.获取dao层接口上的Sql信息(通过读取配置信息，实时—注解—反射)
 * 2.调用Executor执行器进行增删改查操作。
 * 3.返回结果集
 *
 * @author 甘明波
 * @date 2019-07-23
 */
public class SqlSession {

    /**
     * 日志
     */
    private final Logger logger;
    /**
     * 加载配置信息
     */
    private Configuration configuration;
    /**
     * 执行器执行
     */
    private AbstractExecutor executor;
    /**
     * 是否自动提交
     */
    private boolean autoCommit;

    /**
     * init
     * @param configuration 配置对象
     */
    public SqlSession(Configuration configuration) {
        this(configuration, false);
    }

    /**
     * init
     * @param configuration 配置对象
     * @param autoCommit 是否要自动提交
     */
    public SqlSession(Configuration configuration, boolean autoCommit) {
        logger = LoggerFactory.getLogger(this.getClass());
        logger.info("————————————————————————");
        logger.info("初始化SqlSession");
        setConfiguration(configuration);
        setExecutor(new BaseExecutor(configuration));
        setAutoCommit(autoCommit);
    }

    /**
     * 获得dao层接口的代理实现类
     */
    public Object getMapper(Class tClass) {
        return configuration.getMapper(tClass, this);
    }

    /**
     * 调用执行器执行查询
     */
    public Object select(Method method, Annotation annotation, Object[] args) {
        return executor.select(method, annotation, args, autoCommit);
    }

    public int update(Method method, Annotation annotation, Object[] args) {
        return executor.update(method, annotation, args, autoCommit);
    }

    public int insert(Method method, Annotation annotation, Object[] args) {
        return executor.insert(method, annotation, args, autoCommit);
    }

    public int delete(Method method, Annotation annotation, Object[] args) {
        int row = 0;
        try {
            row = executor.delete(method, annotation, args, autoCommit);
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return row;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public AbstractExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(AbstractExecutor executor) {
        this.executor = executor;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public Logger getLogger() {
        return logger;
    }
}
