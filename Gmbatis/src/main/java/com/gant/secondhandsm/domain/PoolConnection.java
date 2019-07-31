package com.gant.secondhandsm.domain;

import java.sql.Connection;

/**
 * Author: 甘明波
 * 2019-07-25
 */
public class PoolConnection {
    //根据Configuration创建的连接
    private Connection connection;
    //连接是否正在被使用
    private boolean using;
    //连接是否已经被关闭
    private boolean closed;

    public PoolConnection(Connection connection) {
        this(connection, false, false);
    }

    public PoolConnection(Connection connection, boolean using) {
        this(connection, using, false);
    }

    public PoolConnection(Connection connection, boolean using, boolean closed) {
        this.connection = connection;
        this.using = using;
        this.closed = closed;
    }

    //归还线程到线程池——将线程的状态改为没有被使用，从而再PoolDataSource中可以取出
    public void close() {
        if (connection != null) {
            using = false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public PoolConnection setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public boolean isUsing() {
        return using;
    }

    public PoolConnection setUsing(boolean using) {
        this.using = using;
        return this;
    }

    public boolean isClosed() {
        return closed;
    }

    public PoolConnection setClosed(boolean closed) {
        this.closed = closed;
        return this;
    }

    @Override
    public String toString() {
        return "PoolConnection{" +
                "connection=" + connection +
                ", using=" + using +
                ", closed=" + closed +
                '}';
    }
}
