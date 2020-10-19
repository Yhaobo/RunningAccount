package erp.dao.provider;

import erp.entity.dto.req.StatisticsQueryConditionDTO;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

/**
 * @author Yhaobo
 * @date 2020/3/28
 */
public class StatisticsDaoSqlProvider {
    public String listDetailByCondition(StatisticsQueryConditionDTO dto) {
        SQL sql = new SQL();
        String formatPattern = "%Y-%m";
        if (StatisticsQueryConditionDTO.GroupPolicy.MONTH.equals(dto.getGroupPolicy())) {
            //按月
            formatPattern = "%Y-%m";
        } else if (StatisticsQueryConditionDTO.GroupPolicy.YEAR.equals(dto.getGroupPolicy())) {
            //按年
            formatPattern = "%Y";
        }
        sql.SELECT(" m.date date, m.earning earning, m.expense expense, d.balance balance");
        sql.FROM("(SELECT DATE_FORMAT(date,'" + formatPattern + "') groupPolicy, SUM(earning) earning, SUM(expense) expense,MAX(date) date FROM detail WHERE account_id = #{dto.accountId} GROUP BY groupPolicy) m");
        sql.INNER_JOIN(" detail d ON d.date=m.date");
        if (dto.getEndDate() != null && dto.getBeginDate() != null) {
            sql.WHERE("d.date between #{dto.beginDate} and #{dto.endDate}");
        }
        if (dto.getProjectId() != null) {
            sql.WHERE("project_id=#{dto.projectId}");
        }
        if (dto.getDepartmentId() != null) {
            sql.WHERE("department_id=#{dto.departmentId}");
        }
        if (dto.getCategoryId() != null) {
            sql.WHERE("category_id=#{dto.categoryId}");
        }
        sql.GROUP_BY("groupPolicy");
        sql.ORDER_BY("groupPolicy DESC");
        return sql.toString();
    }

    public String sumExpenseFromDepartment(Integer departmentId, Date beginDate, Date endDate) {
        final SQL sql = new SQL();
        sql.SELECT("sum(expense) as value");
        sql.FROM("detail");
        if (beginDate != null) {
            sql.WHERE("date >= #{beginDate}");
        }
        if (endDate != null) {
            sql.WHERE("date <= #{endDate}");
        }
        sql.WHERE("department_id = #{departmentId}");
        return sql.toString();
    }

    public String sumExpenseFromCategory(Integer categoryId, Date beginDate, Date endDate) {
        final SQL sql = new SQL();
        sql.SELECT("sum(expense) as value");
        sql.FROM("detail");
        if (beginDate != null) {
            sql.WHERE("date >= #{beginDate}");
        }
        if (endDate != null) {
            sql.WHERE("date <= #{endDate}");
        }
        sql.WHERE("category_id = #{categoryId}");
        return sql.toString();
    }
}
