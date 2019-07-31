# Gmbatis
手写的mybatis

写这个东西目的：
    一个是学习mybatis原理
    一个是实现注解的形式完成Sql的传递，而不是mapper文件（返回类型从dao接口的方法上找）
这样写的好处：极为方便的调试（不用每一次都去Mapper文件去读取，从而导致一次出错就需要重启tomcat）
缺点：每一次都需要去用反射查询和动态生成Sql语句，多次调用十分浪费内存和时间
    解决：考虑增加整个Gmbatis级别的缓存，包括查询结果和SQL语句————2019-7-31

流程：

    创建SqlSessionFactory：factory = SqlSessionFactory.build("jdbc配置文件")
        通过IO流读取，创建唯一配置Configuration对象
    获得接口层对象：SqlSession：factory.openSession()
    获得指定持久层接口的代理实现类：sqlSession.getMapper("接口Class对象")
        JDK动态代理——调用SqlSession对象的各个方法select，delete...
        sqlSession调用执行器AbstractExecutor进行具体操作
    调用指定方法
        反射，获得注解并根据传入实体类对象参数动态拼接成SQL语句
        事务
    返回查询结果
        通过反射完成结果集的映射处理
