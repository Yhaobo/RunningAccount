package erp.dao.provider;

import erp.entity.Detail;
import erp.entity.dto.req.DetailQueryConditionDTO;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * @author Yhaobo
 * @date 2020/3/13
 */
public class DetailDaoSqlProvider {

    public String listByCondition(DetailQueryConditionDTO dto) {
        final SQL sql = new SQL();
        sql.SELECT("id,date,digest,project_id,account_id,department_id,category_id,earning,expense,balance,reimbursement");
        sql.FROM("detail");
        if (dto.getEndDate() != null && dto.getBeginDate() != null) {
            sql.WHERE("date between #{dto.beginDate} and #{dto.endDate}");
        }
        if (dto.getDigest() != null && dto.getDigest().length() > 2) {
            sql.WHERE("digest like #{dto.digest}");
        }
        if (dto.getProjectId() != null) {
            sql.WHERE("project_id=#{dto.projectId}");
        }
        if (dto.getAccountId() != null) {
            sql.WHERE("account_id=#{dto.accountId}");
        }
        if (dto.getDepartmentId() != null) {
            sql.WHERE("department_id=#{dto.departmentId}");
        }
        if (dto.getCategoryId() != null) {
            sql.WHERE("category_id=#{dto.categoryId}");
        }
        sql.ORDER_BY("date desc");
        return sql.toString();
    }

    public String addByBatch(List<Detail> list) {
        final SQL sql = new SQL();
        sql.INSERT_INTO("detail");
        sql.INTO_COLUMNS("date,digest,project_id,account_id,department_id,category_id,earning,expense,create_time");
        for (int i = 0; i < list.size(); i++) {
            sql.INTO_VALUES("#{list[" + i + "].date}," + "#{list[" + i + "].digest}," + "#{list[" + i + "].project.id}," +
                    "#{list[" + i + "].account.id}," + "#{list[" + i + "].department.id}," +
                    "#{list[" + i + "].category.id}," + "#{list[" + i + "].earning},#{list[" + i + "].expense}," +
                    "#{list[" + i + "].createTime}");
            sql.ADD_ROW();
        }
        return sql.toString();
    }
}

