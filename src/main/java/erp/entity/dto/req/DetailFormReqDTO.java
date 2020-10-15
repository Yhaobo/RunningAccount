package erp.entity.dto.req;

import erp.entity.Detail;

import java.math.BigDecimal;

/**
 * 此类的所有BigDecimal类型的属性setter时,会舍弃小数点2位之后的部分
 *
 * @author Yhaobo
 * @date 2020/9/5
 */
public class DetailFormReqDTO extends Detail {
    @Override
    public void setEarning(BigDecimal earning) {
        if (earning != null) {
            super.setEarning(earning.setScale(2, BigDecimal.ROUND_DOWN));
        }
    }

    @Override
    public void setExpense(BigDecimal expense) {
        if (expense != null) {
            super.setExpense(expense.setScale(2, BigDecimal.ROUND_DOWN));
        }
    }

    @Override
    public void setBalance(BigDecimal balance) {
        if (balance != null) {
            super.setBalance(balance.setScale(2, BigDecimal.ROUND_DOWN));
        }
    }
}
