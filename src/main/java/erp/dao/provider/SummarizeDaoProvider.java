package erp.dao.provider;

import erp.entity.vo.req.SummarizeConditionQueryVO;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author Yhaobo
 * @date 2020/3/28
 */
public class SummarizeDaoProvider {
    public String listByFilterSql(SummarizeConditionQueryVO vo) {
        return new SQL() {{
            if ("month".equals(vo.getGroupPolicy())) {
                //按月
                if (vo.isAll()) {
                    //所有
                    SELECT(" m.date date, m.earning earning, m.expense expense, d.d_balance balance");
                    FROM("(SELECT DATE_FORMAT(d_date,'%Y-%m') monthly, SUM(d_earning) earning, SUM(d_expense) expense,MAX(d_date) date FROM detail GROUP BY monthly) m");
                    JOIN(" detail d ON d.d_date=m.date");
                    if (vo.getBackDate() != null && vo.getFrontDate() != null) {
                        WHERE(" d_date between #{frontDate} and #{backDate}");
                    }
                    ORDER_BY("date DESC");
                } else {
                    //按某项
                    SELECT("0 balance, DATE_FORMAT(d_date,'%Y-%m') monthly, SUM(d_earning) earning, SUM(d_expense) expense,MAX(d_date) date");
                    FROM("detail");
                    if (vo.getBackDate() != null && vo.getFrontDate() != null) {
                        WHERE("d_date between #{frontDate} and #{backDate}");
                    }
                    if (vo.getProjectId() != null) {
                        WHERE("p_id=#{projectId}");
                    } else if (vo.getAccountId() != null) {
                        WHERE("a_id=#{accountId}");
                    } else if (vo.getDepartmentId() != null) {
                        WHERE("dep_id=#{departmentId}");
                    } else if (vo.getCategoryId() != null) {
                        WHERE("c_id=#{categoryId}");
                    }
                    GROUP_BY("monthly");
                    ORDER_BY("monthly DESC");
                }
            } else if ("year".equals(vo.getGroupPolicy())) {
                //按年
                if (vo.isAll()) {
                    //所有
                    SELECT(" m.date date, m.earning earning, m.expense expense, d.d_balance balance");
                    FROM("(SELECT DATE_FORMAT(d_date,'%Y') yearly, SUM(d_earning) earning, SUM(d_expense) expense,MAX(d_date) date FROM detail GROUP BY yearly) m");
                    JOIN("detail d ON d.d_date=m.date");
                    if (vo.getBackDate() != null && vo.getFrontDate() != null) {
                        WHERE("date between #{frontDate} and #{backDate}");
                    }
                    ORDER_BY("date DESC");
                } else {
                    //按某项
                    SELECT("0 balance, DATE_FORMAT(d_date,'%Y') yearly, SUM(d_earning) earning, SUM(d_expense) expense,MAX(d_date) date");
                    FROM("detail");
                    if (vo.getBackDate() != null && vo.getFrontDate() != null) {
                        WHERE("d_date between #{frontDate} and #{backDate}");
                    }
                    if (vo.getProjectId() != null) {
                        WHERE("p_id=#{projectId}");
                    } else if (vo.getAccountId() != null) {
                        WHERE("a_id=#{accountId}");
                    } else if (vo.getDepartmentId() != null) {
                        WHERE("dep_id=#{departmentId}");
                    } else if (vo.getCategoryId() != null) {
                        WHERE("c_id=#{categoryId}");
                    }
                    GROUP_BY("yearly");
                    ORDER_BY("yearly DESC");
                }
            }
        }}.toString();
    }
}
