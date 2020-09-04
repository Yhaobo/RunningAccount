package erp.vo.resp;

import erp.entity.Account;
import erp.entity.Category;
import erp.entity.Department;
import erp.entity.Project;
import lombok.Data;

import java.util.Date;

/**
 * 用于返回前端收支明细页面的 VO
 *
 * @author Yhaobo
 * @date 2020/5/16
 */
@Data
public class DetailRespVO extends FormatMoney {
    private Integer id;
    private Date date;
    private String description;
    private Project project;
    private Account account;
    private Department department;
    private Category category;

    private boolean hasVoucher;
}
