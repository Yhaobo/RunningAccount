package erp.dao.provider;

import erp.vo.req.DetailFilterVo;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author Yhaobo
 * @date 2020/3/13
 */
public class DetailDaoProvider {
    public String listByFilterSql(DetailFilterVo vo) {
        return new SQL() {{
            SELECT("*");
            FROM("detail");
            if (vo.getBackDate() != null && vo.getFrontDate() != null) {
                WHERE("d_date between #{frontDate} and #{backDate}");
            }
            if (vo.getDescription() != null && vo.getDescription().length()>0) {
                WHERE("d_description like #{description}");
            }
            if (vo.getProjectId() != null) {
                WHERE("p_id=#{projectId}");
            }
            if (vo.getAccountId() != null) {
                WHERE("a_id=#{accountId}");
            }
            if (vo.getDepartmentId() != null) {
                WHERE("dep_id=#{departmentId}");
            }
            if (vo.getCategoryId() != null) {
                WHERE("c_id=#{categoryId}");
            }
            ORDER_BY("d_date desc");
        }}.toString();
    }
}
