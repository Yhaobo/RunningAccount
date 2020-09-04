package erp.vo.req;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yhaobo
 * @date 2020/3/13
 */
@Data
public class DetailConditionQueryVO implements Serializable {
    private Date frontDate;
    private Date backDate;
    private String description;
    private Integer projectId;
    private Integer accountId;
    private Integer departmentId;
    private Integer categoryId;
    private int pageNum;
    private int pageSize;

    public void setDescription(String description) {
        this.description = "%" + description + "%";
    }
}
