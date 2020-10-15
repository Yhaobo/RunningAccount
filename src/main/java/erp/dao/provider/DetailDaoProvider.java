package erp.dao.provider;

import erp.entity.Detail;
import erp.entity.dto.req.DetailQueryConditionDTO;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * @author Yhaobo
 * @date 2020/3/13
 */
public class DetailDaoProvider {
    public String listByFilterSql(DetailQueryConditionDTO dto) {
        return new SQL() {{
            SELECT("*");
            FROM("detail");
            if (dto.getJustNoVoucher()) {
                LEFT_OUTER_JOIN("voucher on detail.d_id=voucher.detail_id");
                WHERE("detail_id is null");
            }
            if (dto.getEndDate() != null && dto.getBeginDate() != null) {
                WHERE("d_date between #{beginDate} and #{endDate}");
            }
            if (dto.getDescription() != null && dto.getDescription().length() > 2) {
                WHERE("d_description like #{description}");
            }
            if (dto.getProjectId() != null) {
                WHERE("p_id=#{projectId}");
            }
            if (dto.getAccountId() != null) {
                WHERE("a_id=#{accountId}");
            }
            if (dto.getDepartmentId() != null) {
                WHERE("dep_id=#{departmentId}");
            }
            if (dto.getCategoryId() != null) {
                WHERE("c_id=#{categoryId}");
            }
            ORDER_BY("d_date desc");
        }}.toString();
    }

    public String addByBatchSql(List<Detail> list) {
        return new SQL() {{
            INSERT_INTO("detail");
            INTO_COLUMNS("d_date,d_description,p_id,a_id,dep_id,c_id,d_earning,d_expense");
            for (int i = 0; i < list.size(); i++) {
                INTO_VALUES(
                        "#{list[" + i + "].date},#{list[" + i + "].description}," +
                                "(select id from project where name=#{list[" + i + "].project.name})," +
                                "(select id from account where name=#{list[" + i + "].account.name})," +
                                "(select id from department where name=#{list[" + i + "].department.name})," +
                                "(select id from category where name=#{list[" + i + "].category.name})," +
                                "#{list[" + i + "].earning},#{list[" + i + "].expense}");
                ADD_ROW();
            }
        }}.toString();
    }
}

