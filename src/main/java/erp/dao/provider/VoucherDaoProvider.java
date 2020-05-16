package erp.dao.provider;

import erp.domain.Voucher;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * @author Yhaobo
 * @date 2020/5/15
 */
public class VoucherDaoProvider {
    public String addByBatchSql(List<Voucher> vouchers) {
        return new SQL() {{
            INSERT_INTO("voucher");
            INTO_COLUMNS("url, d_id");
            for (Voucher voucher : vouchers) {
                INTO_VALUES(voucher.getUrl()+","+voucher.getD_id());
                ADD_ROW();
            }
        }}.toString();
    }
}
