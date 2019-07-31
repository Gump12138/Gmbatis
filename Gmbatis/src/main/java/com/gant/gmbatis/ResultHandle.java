package com.gant.gmbatis;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 甘明波
 * @date 2019-07-24
 */
public class ResultHandle {

    private ResultHandle() {
    }

    public static Object handle(Method method, ResultSet set){
        if (set == null) {
            throw new RuntimeException("结果处理器没有收到数据库的结果集");
        }
        Class<?> aClass = method.getParameterTypes()[0];
        Field[] fields = aClass.getDeclaredFields();
        List list = new ArrayList();
        try {
            while (set.next()) {
                Object o = aClass.newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(o, set.getString(field.getName()));
                }
                list.add(o);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return list.size() > 1 ? list : list.get(0);
    }
}
