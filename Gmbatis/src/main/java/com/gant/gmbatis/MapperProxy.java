package com.gant.gmbatis;

import com.gant.gmbatis.annotation.*;
import com.gant.gmbatis.executor.BaseExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author 甘明波
 * @date 2019-07-24
 */
public class MapperProxy implements InvocationHandler {
    private final SqlSession sqlSession;
    private static final Logger logger = LoggerFactory.getLogger(MapperProxy.class);

    public MapperProxy(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("——————————————————————————————");
        logger.info("进入接口的代理实现类的invoke方法");
        Object result = null;
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            //暂时只支持不动态的
            if (annotation instanceof Select || annotation instanceof SelectDynamic) {
                result = sqlSession.select(method, annotation, args);
            } else if (annotation instanceof Update) {
                result = sqlSession.update(method, annotation, args);
            } else if (annotation instanceof Insert) {
                result = sqlSession.insert(method, annotation, args);
            } else if (annotation instanceof Delete || annotation instanceof DeleteDynamic) {
                result = sqlSession.delete(method, annotation, args);
            }
        }
        return result;
    }
}
