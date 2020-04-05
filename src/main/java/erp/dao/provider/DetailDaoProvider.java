package erp.dao.provider;

import erp.domain.Detail;
import erp.vo.req.DetailFilterVo;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

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
            if (vo.getDescription() != null && vo.getDescription().length() > 0) {
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

    public String addByBatchSql(List<Detail> list) {
        return new SQL() {{
            INSERT_INTO("detail");
            INTO_COLUMNS("d_date,d_description,p_id,a_id,dep_id,c_id,d_earning,d_expense,d_balance");
            for (int i = 0; i < list.size(); i++) {
                INTO_VALUES(
                        "#{list[" + i + "].date},#{list[" + i + "].description}," +
                                "(select id from project where name=#{list[" + i + "].project.name})," +
                                "(select id from account where name=#{list[" + i + "].account.name})," +
                                "(select id from department where name=#{list[" + i + "].department.name})," +
                                "(select id from category where name=#{list[" + i + "].category.name})," +
                                "#{list[" + i + "].earning},#{list[" + i + "].expense},#{list[" + i + "].balance}");
                ADD_ROW();
            }
        }}.toString();
    }
}

