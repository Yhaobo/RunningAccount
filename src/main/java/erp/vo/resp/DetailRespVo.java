package erp.vo.resp;

import erp.domain.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用于返回前端页面的 VO
 *
 * @author Yhaobo
 * @date 2020/5/16
 */
@Data
public class DetailRespVo {
    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    private String description;
    private Project project;
    private Account account;
    private Department department;
    private Category category;
    private BigDecimal earning;
    private BigDecimal expense;
    private BigDecimal balance;
    private String formatEarning;
    private String formatExpense;
    private String formatBalance;
    private boolean hasVoucher;
}
