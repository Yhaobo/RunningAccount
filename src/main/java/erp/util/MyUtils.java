package erp.util;

import erp.dao.DetailDao;
import erp.entity.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Yhaobo
 * @date 2020/3/17
 */
public class MyUtils {

    /**
     * 获取随机的32位的16进制数的字符串
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取随机的指定长度的16进制数的字符串
     *
     * @param len 指定长度
     */
    public static String getUUID(int len) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, len);
    }

    /**
     * 获取以当前时间命名的文件名, 格式为 "yyyy-MM-dd^A" <br/>
     * 这些符号在系统路径中不允许: \  /  :  *  ?  "  <  >  |
     */
    public static String getFileNameWithCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd^A"));
    }

    /**
     * 导入测试数据
     *
     * @param numberOfThousand 插入几千条数据
     * @param calenderField    每条记录时间间隔
     * @param startYear        第一条记录开始的年份
     */
    public static void importTestData(DetailDao detailDao, int numberOfThousand, int startYear, int calenderField) {
        int numberOfBatch = 1000;
        List<Detail> details = new ArrayList<>(numberOfBatch);
        Calendar calendar = Calendar.getInstance();
        calendar.set(startYear, Calendar.JANUARY, 1);
        Random random = new Random();
        NumberFormat numberFormat = NumberFormat.getInstance();
        System.out.println("开始导入测试数据");
        long start = System.currentTimeMillis();
        for (int i = 0; i < numberOfThousand; i++) {
            int j;
            for (j = 0; j < numberOfBatch; j++) {
                Detail d = new Detail();
                d.setDate(calendar.getTime());
                d.setDigest("测试数据" + ((i + 1) * (j + 1)));
                d.setProject(new Project("项目1"));
                d.setAccount(new Account("账户1"));
                d.setDepartment(new Department("部门1"));
                d.setCategory(new Category("分类" + (random.nextInt(2) + 1)));
                d.setEarning(new BigDecimal(random.nextInt(100)));
                d.setExpense(new BigDecimal(random.nextInt(100)));
                details.add(d);
                calendar.add(calenderField, 1);
            }
            detailDao.insertFromExcelByBatch(details);
            details.clear();
            System.out.println(Thread.currentThread() + "已插入" + numberFormat.format((i + 1) * j) + "条数据");
        }
        long end = System.currentTimeMillis();
        System.out.println("插入测试数据成功, 耗时: " + numberFormat.format(end - start) + "毫秒");
    }

}
