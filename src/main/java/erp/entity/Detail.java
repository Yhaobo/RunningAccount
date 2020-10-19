package erp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
}
