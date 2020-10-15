package erp.entity.dto.resp;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Yhaobo
 * @date 2020/5/18
 */
@Data
public class MoneyStatisticsRespDTO {
    private Date date;
    private BigDecimal earning;
    private BigDecimal expense;
    private BigDecimal balance;
}
