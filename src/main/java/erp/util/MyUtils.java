package erp.util;

import erp.vo.resp.DetailRespVo;

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
     * @param detailRespVos
     */
    public static void formatNumber(List<DetailRespVo> detailRespVos) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);
        for (DetailRespVo i : detailRespVos) {
            i.setFormatEarning(numberFormat.format(i.getEarning()));
            i.setFormatExpense(numberFormat.format(i.getExpense()));
            i.setFormatBalance(numberFormat.format(i.getBalance()));
        }
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
