package erp.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收支明细表的实体类
 */
@Data
@TableName(resultMap="detailResultMap")
public class Detail implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Date date;
    private Project project;
    private Account account;
    private Department department;
    private Category category;
    private BigDecimal earning;
    private BigDecimal expense;
    private BigDecimal balance;
    private String digest;
    private Boolean reimbursement;
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
    @JsonIgnore
    private Date alterTime;
}
