package erp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import erp.dao.provider.DetailDaoSqlProvider;
import erp.entity.Detail;
import erp.entity.dto.req.DetailQueryConditionDTO;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface DetailDao extends BaseMapper<Detail> {
    /**
     * 插入一条记录
     */
    @Override
    int insert(Detail detail);

    /**
     * 从Excel文件导入多条记录
     */
    @InsertProvider(type = DetailDaoSqlProvider.class, method = "addByBatch")
    void insertFromExcelByBatch(List<Detail> details);

    /**
     * 修改一条记录
     *
     * @param detail
     */
    void update(Detail detail);

    /**
     * 修改两个时间之间(不包括边界值)的所有记录的结存
     *
     * @param change 变化值
     * @param late   最大时间边界值
     * @param before 最小时间边界值
     */
    void updateDuring(@Param("change") BigDecimal change, @Param("before") Date before, @Param("late") Date late, @Param("accountId") Integer accountId);

    /**
     * 修改date时间之后的所有账户ID为accountId记录的结存
     *
     * @param change    变化值
     * @param date      最小时间边界值
     * @param accountId 银行账户ID
     */
    void updateLater(@Param("change") BigDecimal change, @Param("date") Date date, @Param("accountId") Integer accountId);

    /**
     * 查询date之前的一条记录
     *
     * @param date 最大时间边界值
     */
    BigDecimal getBeforeOneBalance(Date date, @Param("accountId") Integer accountId);

    /**
     * 根据查询条件来获取记录
     */
    @SelectProvider(type = DetailDaoSqlProvider.class, method = "listByCondition")
    @ResultMap("detailResultMap")
    List<Detail> listByCondition(Page<?> page, DetailQueryConditionDTO dto);

    List<Detail> listByAccountId(Integer id);
}
