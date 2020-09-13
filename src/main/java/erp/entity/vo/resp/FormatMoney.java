package erp.entity.vo.resp;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Yhaobo
 * @date 2020/5/18
 */
@Data
public class FormatMoney {
    private BigDecimal earning;
    private BigDecimal expense;
    private BigDecimal balance;
    private String formatEarning;
    private String formatExpense;
    private String formatBalance;
}
