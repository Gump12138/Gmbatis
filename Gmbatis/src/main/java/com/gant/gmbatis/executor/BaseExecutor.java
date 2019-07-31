package com.gant.gmbatis.executor;

import com.gant.gmbatis.Configuration;
import com.gant.gmbatis.ResultHandle;
import com.gant.gmbatis.annotation.*;
import com.gant.gmbatis.utils.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;

/**
 * 执行引擎(核心)
 * 接受配置信息，Sql相关，形成完整的prepareStatement或是statement
 * 使用连接池中的连接获得Sql语句处理对象，完成传参后推送至数据库
 * 并接收到返回结果集，并返回到上级调用者
 *
 * @author 甘明波
 * @date 2019-07-23
 */
public class BaseExecutor extends AbstractExecutor {

    /**
     * 日志
     */
    private final Logger logger;

    /**
     * init
     * @param configuration 配置对象
     */
    public BaseExecutor(Configuration configuration) {
        super(configuration);
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public Object selectDynamic(Method method, Object[] args) {
        String sql = getSqlHandle().handleSql(method, args);
        logger.info("preparedStatement：" + sql);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet set = null;
        Object result = null;
        try {
            connection = JdbcUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            setArguments(args, preparedStatement);
            logger.info("查询语句自动提交");
            set = preparedStatement.executeQuery();
            result = ResultHandle.handle(method, set);
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(connection, preparedStatement, set);
        }
        return result;
    }

    @Override
    public Object defaultSelect(Method method, Object[] args) {
        String sql = method.getAnnotation(Select.class).value();
        return 0;
    }

    @Override
    public int defaultUpdate(String sql, Object[] args, boolean autoCommit) {
        return 0;
    }

    @Override
    public int defaultInsert(String sql, Object[] args, boolean autoCommit) {
        return 0;
    }

    @Override
    public int deleteByObject(String sql, Object[] args, boolean autoCommit) {
        return 0;
    }

    @Override
    public int defaultDelete(String sql, Object[] args, boolean autoCommit) throws SQLException, IllegalAccessException {
        int rows = 0;
        if (args != null && args.length != 0) {
            //PreparedStatement语句
            PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
            setArguments(args, preparedStatement);
            //推送SQL
            rows = preparedStatement.executeUpdate();
            logger.info("preparedStatement返回影响行数：" + rows);
            preparedStatement.close();
        } else {
            //Statement语句
            Statement statement = getConnection().createStatement();
            logger.info("Sql参数：空");
            //推送SQL
            rows = statement.executeUpdate(sql);
            logger.info("statement返回影响行数：" + rows);
            statement.close();
        }
        return rows;
    }

    /**
     * 设置preparedStatement占位符值
     * 占位符的赋值顺序与dao层接口中调用的方法形参的顺序一致
     *
     * @param args              preparedStatement参数
     * @param preparedStatement 预处理语句
     * @throws SQLException 就处理不了的异常
     */
    private void setArguments(Object[] args, PreparedStatement preparedStatement) throws SQLException, IllegalAccessException {
        Object arg = args[0];
        Class<?> aClass = arg.getClass();
        Field[] fields = aClass.getDeclaredFields();
        int i = 1;
        for (Field field : fields) {
            field.setAccessible(true);
            Object o = field.get(arg);
            if (o != null) {
                preparedStatement.setString(i++, o.toString());
            }
        }
        logger.info("Sql参数：" + arg.toString());
        logger.info("Sql语句：" + preparedStatement.toString());
    }
}
