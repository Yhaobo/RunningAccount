package erp.dao.provider;

import erp.entity.dto.req.StatisticsQueryConditionDTO;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author Yhaobo
 * @date 2020/3/28
 */
public class StatisticsDaoProvider {
    public String listByFilterSql(StatisticsQueryConditionDTO dto) {
        return new SQL() {{
            String formatPattern = "%Y-%m";
            if (StatisticsQueryConditionDTO.GroupPolicy.MONTH.equals(dto.getGroupPolicy())) {
                //按月
                formatPattern = "%Y-%m";

            } else if (StatisticsQueryConditionDTO.GroupPolicy.YEAR.equals(dto.getGroupPolicy())) {
                //按年
                formatPattern = "%Y";
//                SELECT(" m.date date, m.earning earning, m.expense expense, d.d_balance balance");
//                FROM("(SELECT DATE_FORMAT(d_date,'%Y') yearly, SUM(d_earning) earning, SUM(d_expense) expense,MAX(d_date) date FROM detail WHERE a_id = #{accountId} GROUP BY yearly) m");
//                JOIN(" detail d ON d.d_date=m.date");
//                if (dto.getEndDate() != null && dto.getBeginDate() != null) {
//                    WHERE("d_date between #{beginDate} and #{endDate}");
//                }
//
//                if (dto.getProjectId() != null) {
//                    WHERE("p_id=#{projectId}");
//                }
//                if (dto.getDepartmentId() != null) {
//                    WHERE("dep_id=#{departmentId}");
//                }
//                if (dto.getCategoryId() != null) {
//                    WHERE("c_id=#{categoryId}");
//                }
//                GROUP_BY("yearly");
//                ORDER_BY("yearly DESC");
            }
            SELECT(" m.date date, m.earning earning, m.expense expense, d.d_balance balance");
            FROM("(SELECT DATE_FORMAT(d_date,'" + formatPattern + "') groupPolicy, SUM(d_earning) earning, SUM(d_expense) expense,MAX(d_date) date FROM detail WHERE a_id = #{accountId} GROUP BY groupPolicy) m");
            JOIN(" detail d ON d.d_date=m.date");
            if (dto.getEndDate() != null && dto.getBeginDate() != null) {
                WHERE("d_date between #{beginDate} and #{endDate}");
            }
            if (dto.getProjectId() != null) {
                WHERE("p_id=#{projectId}");
            }
            if (dto.getDepartmentId() != null) {
                WHERE("dep_id=#{departmentId}");
            }
            if (dto.getCategoryId() != null) {
                WHERE("c_id=#{categoryId}");
            }
            GROUP_BY("groupPolicy");
            ORDER_BY("groupPolicy DESC");
//
        }}.toString();
    }
}
