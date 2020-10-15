package erp.dao;

import erp.dao.provider.StatisticsDaoProvider;
import erp.entity.Detail;
import erp.entity.dto.req.StatisticsQueryConditionDTO;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

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
    @SelectProvider(type = StatisticsDaoProvider.class, method = "listByFilterSql")
    List<Detail> listDetailByFilter(StatisticsQueryConditionDTO dto);
}
