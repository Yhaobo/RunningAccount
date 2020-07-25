package erp.util;

import erp.dao.DetailDao;
import erp.domain.*;
import erp.vo.resp.FormatMoney;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

/**
 * @author Yhaobo
 * @date 2020/3/17
 */
public class MyUtils {
    /**
     * 格式化所有数字为货币格式
     *
     * @param detailRespVos
     */
    public static void formatNumber(List<FormatMoney> detailRespVos) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);
        for (FormatMoney i : detailRespVos) {
            i.setFormatEarning(numberFormat.format(i.getEarning()));
            i.setFormatExpense(numberFormat.format(i.getExpense()));
            i.setFormatBalance(numberFormat.format(i.getBalance()));
        }
    }

    /**
     * 获取随机的32位的16进制数的字符串
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 导入测试数据
     *
     * @param detailDao
     * @param numberOfTenThousand 插入几万条数据
     */
    public static void importTestData(DetailDao detailDao, int numberOfTenThousand) {
        int numberOfBatch = 10000;
        List<Detail> list0 = new ArrayList<>(numberOfBatch);
        Calendar calendar0 = Calendar.getInstance();
        calendar0.set(1900, Calendar.JANUARY, 1);
        Random random = new Random();
        NumberFormat numberFormat = NumberFormat.getInstance();
        long start = System.currentTimeMillis();
        System.out.println("开始导入测试数据");
        Thread thread = new Thread(() -> {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(2000, Calendar.JANUARY, 1);
            List<Detail> list1 = new ArrayList<>(numberOfBatch);
            for (int i = 0; i < numberOfTenThousand / 2; i++) {
                int j;
                for (j = 0; j < numberOfBatch; j++) {
                    Detail d = new Detail();
                    d.setDate(calendar1.getTime());
                    d.setDescription("测试数据" + ((i + 1) * (j + 1)));
                    d.setProject(new Project("项目1"));
                    d.setAccount(new Account("账户1"));
                    d.setDepartment(new Department("部门1"));
                    d.setCategory(new Category("分类" + (random.nextInt(2) + 1)));
                    d.setEarning(new BigDecimal(random.nextInt(100)));
                    d.setExpense(new BigDecimal(random.nextInt(100)));
                    list1.add(d);
                    calendar1.add(Calendar.MINUTE, 60);
                }
                detailDao.insertFromExcelByBatch(list1);
                list1.clear();
                System.out.println(Thread.currentThread() + "已插入" + numberFormat.format((i + 1) * j) + "条数据");
            }
        }, "t1");
        thread.start();
        for (int i = 0; i < numberOfTenThousand / 2; i++) {
            int j;
            for (j = 0; j < numberOfBatch; j++) {
                Detail d = new Detail();
                d.setDate(calendar0.getTime());
                d.setDescription("测试数据" + ((i + 1) * (j + 1)));
                d.setProject(new Project("项目1"));
                d.setAccount(new Account("账户1"));
                d.setDepartment(new Department("部门1"));
                d.setCategory(new Category("分类" + (random.nextInt(2) + 1)));
                d.setEarning(new BigDecimal(random.nextInt(100)));
                d.setExpense(new BigDecimal(random.nextInt(100)));
                list0.add(d);
                calendar0.add(Calendar.MINUTE, 60);
            }
            detailDao.insertFromExcelByBatch(list0);
            list0.clear();
            System.out.println(Thread.currentThread() + "已插入" + numberFormat.format((i + 1) * j) + "条数据");
        }
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("插入测试数据成功, 耗时: " + numberFormat.format(end - start) + "毫秒");
    }

}
