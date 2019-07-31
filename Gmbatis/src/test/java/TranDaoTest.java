import com.gant.gmbatis.executor.BaseExecutor;
import com.gant.gmbatis.SqlSessionFactory;
import com.gant.secondhandsm.dao.TranDao;
import com.gant.secondhandsm.domain.TranHistory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: 甘明波
 * 2019-07-23
 */
public class TranDaoTest {

    @Test
    public void annotationTest() {
        Class<TranDao> daoClass = TranDao.class;
        Method method = daoClass.getDeclaredMethods()[0];
        Annotation annotation = method.getAnnotations()[0];
        //@com.gant.gmbatis.annotation.SelectDynamic()
        String s = annotation.toString();
        Class<? extends Annotation> annotationType = annotation.annotationType();
//        System.out.println(s);
        System.out.println(annotationType.getSimpleName().toLowerCase());
    }


    @Test
    public void select() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("————————————————————————");
        logger.info("测试开始");
        SqlSessionFactory factory = SqlSessionFactory.build("jdbc");
        TranDao dao = (TranDao) factory.openSession().getMapper(TranDao.class);
        TranHistory history = new TranHistory();
        history.setId("5299b62d2d7f48c1acf9f9a4b3bcad75");
        history.setStage("05提案/报价");
        TranHistory s = dao.selectByCondition(history);
        System.out.println(s);
//        String s = dao.selectByCondition(history);
        logger.info("结束测试");
        logger.info("————————————————————————");
    }

    /*@Test
    public void reflect() {
        try {
            Field field = TranDaoTest.class.getDeclaredField("list");
            System.out.println("字段对象：" + field);
            Type type = field.getGenericType();
            System.out.println(type);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }*/

    /*@Test
    public void delete() {
        logger.info("————————————————————————");
        logger.info("测试开始");
        SqlSessionFactory factory = SqlSessionFactory.build("jdbc");
        TranDao dao = (TranDao) factory.openSession().getMapper(TranDao.class);
        int row = dao.deleteById("f20cba10be0a4dc7a8f9f6e2b0d93ff5");
        System.out.println(row == 1);
        logger.info("结束测试");
        logger.info("————————————————————————");
    }*/
}
