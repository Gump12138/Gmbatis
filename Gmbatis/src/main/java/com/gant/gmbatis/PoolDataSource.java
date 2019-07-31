package com.gant.gmbatis;

import com.gant.secondhandsm.domain.PoolConnection;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 与数据库的连接池
 * Author: 甘明波
 * 2019-07-24
 */
public class PoolDataSource {
    //存储连接池子——自定义线程——自定义大小
    private static PoolConnection[] dataSource;

    //单例的连接池
    private static PoolDataSource POOL_DATA_SOURCE;

    private PoolDataSource() {
    }

    private PoolDataSource(Configuration configuration) {
        //初始化dataSource
        Class<? extends Configuration> driver = configuration.getClass();
        try {
            //注册驱动
            Class.forName(driver.getName());
            //获得配置信息
            String username = configuration.getUsername();
            String password = configuration.getPassword();
            String url = configuration.getUrl();
            //初始化5条线程
            PoolDataSource.dataSource = new PoolConnection[5];
            //放置连接至线程池
            for (int i = 0; i < dataSource.length; i++) {
                dataSource[i] = new PoolConnection(DriverManager.getConnection(url, username, password), false, false);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        POOL_DATA_SOURCE = new PoolDataSource();
    }


    //获得连接
    public PoolConnection getConnection() {
        PoolConnection connection = null;
        for (PoolConnection conn : dataSource) {
            if (!conn.isUsing() && !conn.isClosed()) {
                connection = conn;
                conn.setUsing(true);
                break;
            }
        }
        if (connection == null) {
            throw new RuntimeException("线程池所有线程已经满了，请稍后再试试");
        }
        return connection;
    }

    //创建或得到一个连接池对象
    public static PoolDataSource getInstance(Configuration configuration) {
        if (POOL_DATA_SOURCE == null) {
            POOL_DATA_SOURCE = new PoolDataSource(configuration);
        }
        return POOL_DATA_SOURCE;
    }
}
