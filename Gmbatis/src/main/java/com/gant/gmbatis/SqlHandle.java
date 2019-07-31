package com.gant.gmbatis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 动态生成SQL语句
 *
 * @author 甘明波
 * @date 2019-07-23
 */
public class SqlHandle {
    /**
     * 日志
     */
    private final Logger logger;
    /**
     * sql语句
     */
    private StringBuilder sql;
    /**
     * 实体类与数据库表的映射
     */
    private Map<String, String> mapper;

    /**
     * sql语句的类型
     */
    private static final String SELECT = "select";
    private static final String UPDATE = "update";
    private static final String INSERT = "insert";
    private static final String DELETE = "delete";

    /**
     * init
     *
     * @param configuration 配置对象
     */
    public SqlHandle(Configuration configuration) {
        logger = LoggerFactory.getLogger(this.getClass());
        this.setSql(new StringBuilder());
        this.setMapper(configuration.getTableMapper());
    }

    /**
     * 动态生成sql语句
     *
     * @param method dao层调用的方法
     * @param args   dao层调用的方法传入的实际参数
     * @return 返回包含占位符的sql语句或完整sql语句
     */
    public String handleSql(Method method, Object[] args) {
        logger.info("————————————————————————");
        logger.info("处理Sql语句");
        try {
            Annotation annotation = method.getDeclaredAnnotations()[0];
            String annotationName = annotation.annotationType().getSimpleName().toLowerCase();
            if (annotationName.contains(SELECT)) {
                logger.info("处理" + SELECT + "语句");
                sql.append("select * from ");
            }

            Object parameter = args[0];
            Class<?> pClass = parameter.getClass();
            sql.append(mapper.get(pClass.getSimpleName().toLowerCase())).append(" where 1 = 1 ");
            Field[] fields = pClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object o = field.get(parameter);
                if (o != null) {
                    sql.append("and ").append(field.getName()).append(" = ").append("?");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sql.toString();
    }

    public StringBuilder getSql() {
        return sql;
    }

    public SqlHandle setSql(StringBuilder sql) {
        this.sql = sql;
        return this;
    }

    public Map<String, String> getMapper() {
        return mapper;
    }

    public SqlHandle setMapper(Map<String, String> mapper) {
        this.mapper = mapper;
        return this;
    }
}
