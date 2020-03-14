package erp.dao;

import erp.domain.Detail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupQueryDao {
    @Select("SELECT 0 balance, DATE_FORMAT(d_date,'%Y-%m') monthly, SUM(d_earning) earning, SUM(d_expense) expense,MAX(d_date) date FROM detail " +
            "WHERE dep_id=#{id} GROUP BY monthly " +
            "ORDER BY date DESC")
    List<Detail> findByMonthlyDepartmentGroup(int dep_id);

    @Select("SELECT 0 balance, DATE_FORMAT(d_date,'%Y-%m') monthly, SUM(d_earning) earning, SUM(d_expense) expense,MAX(d_date) date FROM detail " +
            "WHERE a_id=#{id} GROUP BY monthly " +
            "ORDER BY date DESC")
    List<Detail> findByMonthlyAccountGroup(int a_id);

    @Select("SELECT 0 balance, DATE_FORMAT(d_date,'%Y-%m') monthly, SUM(d_earning) earning, SUM(d_expense) expense,MAX(d_date) date FROM detail " +
            "WHERE p_id=#{id} GROUP BY monthly " +
            "ORDER BY date DESC")
    List<Detail> findByMonthlyProjectGroup(int p_id);

    @Select("SELECT 0 balance, DATE_FORMAT(d_date,'%Y-%m') monthly, SUM(d_earning) earning, SUM(d_expense) expense,MAX(d_date) date FROM detail " +
            "WHERE c_id=#{id} GROUP BY monthly " +
            "ORDER BY date DESC")
    List<Detail> findByMonthlyCategoryGroup(int c_id);

    @Select("SELECT m.date DATE, m.earning earning, m.expense expense, d.d_balance balance " +
            "FROM(SELECT DATE_FORMAT(d_date,'%Y-%m') monthly, SUM(d_earning) earning, SUM(d_expense) expense,MAX(d_date) DATE FROM detail GROUP BY monthly) m " +
            "JOIN detail d ON d.d_date=m.date " +
            "ORDER BY d.d_date DESC")
    List<Detail> findByMonthlyGroup();
}
