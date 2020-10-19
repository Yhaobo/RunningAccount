package erp.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import erp.dao.provider.StatisticsDaoSqlProvider;
import erp.entity.Detail;
import erp.entity.dto.req.StatisticsQueryConditionDTO;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Yhaobo
 * @date 2020/3/28
 */
@Repository
public interface StatisticsDao {
    /**
     * 根据条件来获取收支结存记录变化集合
     */
    @SelectProvider(type = StatisticsDaoSqlProvider.class, method = "listDetailByCondition")
    List<Detail> listDetailByCondition(Page<?> page, StatisticsQueryConditionDTO dto);

    @SelectProvider(type = StatisticsDaoSqlProvider.class, method = "sumExpenseFromDepartment")
    String sumExpenseFromDepartment(Integer departmentId, Date beginDate, Date endDate);

    @SelectProvider(type = StatisticsDaoSqlProvider.class, method = "sumExpenseFromCategory")
    String sumExpenseFromCategory(Integer categoryId, Date beginDate, Date endDate);
}
