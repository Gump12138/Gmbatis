package com.gant.gmbatis.executor;

import com.gant.gmbatis.Configuration;
import com.gant.gmbatis.SqlHandle;
import com.gant.gmbatis.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 抽象执行引擎，列举方法
 *
 * @author 甘明波
 * @date 2019-07-27
 */
public abstract class AbstractExecutor {

    /**
     * 日志
     */
    private final Logger logger;
    private Connection connection;
    private Configuration configuration;
    /**
     * Sql语句处理器
     */
    private SqlHandle sqlHandle;

    /**
     * init
     */
    public AbstractExecutor(Configuration configuration) {
        logger = LoggerFactory.getLogger(this.getClass());
        this.setConfiguration(configuration);
        this.setSqlHandle(new SqlHandle(configuration));
    }

    /**
     * 分发select到不同的处理方法
     *
     * @param method     dao层调用的方法
     * @param annotation 方法上的注解
     * @param args       方法传入实际参数
     * @param autoCommit 是否自动提交
     * @return 查询结果
     */
    public Object select(Method method, Annotation annotation, Object[] args,
                         boolean autoCommit) {
        logger.info("————————————————————————");
        logger.info("进入执行器的select方法");
        Object result = null;
        if (annotation instanceof Select) {
            result = defaultSelect(method, args);
        } else if (annotation instanceof SelectDynamic) {
            result = selectDynamic(method, args);
        }
        return result;
    }

    /**
     * 分发delete到不同的处理方法
     *
     * @param method     dao层调用的方法
     * @param annotation 方法上的注解
     * @param args       方法传入实际参数
     * @param autoCommit 是否自动提交
     * @return 影响行数
     */
    public int delete(Method method, Annotation annotation, Object[] args, boolean autoCommit) throws SQLException, IllegalAccessException {
        logger.info("————————————————————————");
        logger.info("进入执行器的delete方法");
        String sql = method.getAnnotation(Delete.class).value();
        int rows = 0;
        if (annotation instanceof Delete) {
            rows = defaultDelete(sql, args, autoCommit);
        } else if (annotation instanceof DeleteDynamic) {
            rows = deleteByObject(sql, args, autoCommit);
        }
        return rows;
    }

    /**
     * 分发insert到不同的处理方法
     *
     * @param method     dao层调用的方法
     * @param annotation 方法上的注解
     * @param args       方法传入实际参数
     * @param autoCommit 是否自动提交
     * @return 影响行数
     */
    public int insert(Method method, Annotation annotation, Object[] args, boolean autoCommit) {
        logger.info("————————————————————————");
        logger.info("进入执行器的insert方法");
        //动态进行SQL拼接
        String sql = "";
        int rows = 0;
        if (annotation instanceof Insert) {
            rows = defaultInsert(sql, args, autoCommit);
        }
        return rows;
    }

    /**
     * 分发update到不同的处理方法
     *
     * @param method     dao层调用的方法
     * @param annotation 方法上的注解
     * @param args       方法传入实际参数
     * @param autoCommit 是否自动提交
     * @return 影响行数
     */
    public int update(Method method, Annotation annotation, Object[] args, boolean autoCommit) {
        logger.info("————————————————————————");
        logger.info("进入执行器的update方法");
        //动态进行SQL拼接
        String sql = "";
        int rows = 0;
        if (annotation instanceof Update) {
            rows = defaultUpdate(sql, args, autoCommit);
        }
        return rows;
    }

    protected abstract int defaultInsert(String sql, Object[] args, boolean autoCommit);

    protected abstract int defaultUpdate(String sql, Object[] args, boolean autoCommit);

    protected abstract int deleteByObject(String sql, Object[] args, boolean autoCommit) throws SQLException;

    protected abstract int defaultDelete(String sql, Object[] args, boolean autoCommit) throws SQLException, IllegalAccessException;

    /**
     * 动态查询动态返回结果
     *
     * @param method     执行方法
     * @param args       实参
     * @return 查询结果
     */
    protected abstract Object selectDynamic(Method method, Object[] args);

    /**
     * 查询返回一个或多个实体类，动态
     *
     * @param method     执行方法
     * @param args       参数
     * @param autoCommit 是否要自动提交
     * @return 查询结果
     */
    protected abstract Object defaultSelect(Method method, Object[] args);

    public Logger getLogger() {
        return logger;
    }

    public Connection getConnection() {
        return connection;
    }

    public AbstractExecutor setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public AbstractExecutor setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    public SqlHandle getSqlHandle() {
        return sqlHandle;
    }

    public AbstractExecutor setSqlHandle(SqlHandle sqlHandle) {
        this.sqlHandle = sqlHandle;
        return this;
    }
}
