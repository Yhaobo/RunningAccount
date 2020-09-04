package erp.dao;

import erp.dao.provider.SummarizeDaoProvider;
import erp.entity.Detail;
import erp.vo.req.SummarizeConditionQueryVO;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yhaobo
 * @date 2020/3/28
 */
@Repository
public interface SummarizeDao {
    /**
     * 根据条件来获取记录
     *
     * @param vo
     * @return
     */
    @SelectProvider(type = SummarizeDaoProvider.class, method = "listByFilterSql")
    List<Detail> listDetailByFilter(SummarizeConditionQueryVO vo);
}
