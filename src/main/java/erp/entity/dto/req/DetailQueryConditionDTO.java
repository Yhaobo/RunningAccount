package erp.entity.dto.req;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yhaobo
 * @date 2020/3/13
 */
@Data
public class DetailQueryConditionDTO implements Serializable {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date beginDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date endDate;
    private String description;
    private Integer projectId;
    private Integer accountId;
    private Integer departmentId;
    private Integer categoryId;
    private Boolean justNoVoucher=false;
    private Integer currentPage;
    private Integer pageSize;

    public void setDescription(String description) {
        this.description = "%" + description + "%";
    }
}
