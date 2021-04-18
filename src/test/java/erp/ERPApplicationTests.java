package erp;

import erp.dao.DetailDao;
import erp.dao.rbac.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Yhaobo
 * @since 2020/10/15
 */
@SpringBootTest
public class ERPApplicationTests {
    @Autowired
    DetailDao detailDao;

    @Autowired
    UserMapper userDao;

    @Test
    public void insertTestData() {
//        MyUtils.importTestData(detailDao, 1, 2018, Calendar.DATE);
    }

    @Test
    public void testMyBatisPlusInsertData() throws Exception {
        //测试插入实体类如果主键有值的情况下, 数据库主键是如何赋值
//        final User entity = new User();
//        //设置主键值
//        entity.setId(5);
//        entity.setUsername("test");
//        entity.setPassword("test");
//        entity.setLevel(5);
//        userDao.insert(entity);
        //只有当 @TableId(type = IdType.AUTO) 时, 设置的主键值无效, 数据库主键自增
        //其他情况都会根据实体类的主键值来插入主键值
    }

    @Test
    public void testSql() throws Exception {
//        final List<Detail> data = detailDao.listByAccountId(2);
//        System.out.println(data);
    }
}
