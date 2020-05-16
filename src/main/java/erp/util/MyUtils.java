package erp.util;

import erp.domain.Detail;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Yhaobo
 * @date 2020/3/17
 */
public class MyUtils {
    /**
     * 格式化所有数字为货币格式
     *
     * @param details
     */
    public static void formatNumber(List<Detail> details) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);
        for (Detail i : details) {
            i.setFormatEarning(format.format(i.getEarning()));
            i.setFormatExpense(format.format(i.getExpense()));
            i.setFormatBalance(format.format(i.getBalance()));
        }
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
