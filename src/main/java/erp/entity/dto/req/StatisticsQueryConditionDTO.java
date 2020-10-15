package erp.entity.dto.req;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yhaobo
 * @date 2020/3/28
 */
@Data
public class StatisticsQueryConditionDTO implements Serializable {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date beginDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date endDate;
    /**
     * 分组策略: 值有 "year","month"
     */
    private String groupPolicy;

    private Integer projectId;
    private Integer accountId;
    private Integer departmentId;
    private Integer categoryId;

    private Integer currentPage;
    private Integer pageSize;

    /**
     * 是否查询所有
     */
    public boolean isAll() {
        return projectId == null  && departmentId == null && categoryId == null;
    }

    public static class GroupPolicy {
        public static final String MONTH = "month";
        public static final String YEAR = "year";
    }
}
