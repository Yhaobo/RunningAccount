package erp.vo.req;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yhaobo
 * @date 2020/3/28
 */
@Data
public class SummarizeConditionQueryVO implements Serializable {
    private String duringDate;
    private Date frontDate;
    private Date backDate;
    private String groupPolicy;

    private Integer projectId;
    private Integer accountId;
    private Integer departmentId;
    private Integer categoryId;

    private boolean isAll;

    public boolean isAll() {
        return isAll;
    }

    public void setIsAll(boolean all) {
        isAll = all;
    }
}
