package com.gant.gmbatis;

import java.lang.reflect.Proxy;

/**
 * dao层接口代理实现类工厂类
 * @author 甘明波
 * @date 2019-07-24
 */
public class MapperProxyFactory {

    private MapperProxyFactory() {
    }

    public static Object newInstance(Class mapperInterface, SqlSession sqlSession) {
        MapperProxy mapperProxy = new MapperProxy(sqlSession);
        return Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }
}
