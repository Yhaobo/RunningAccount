package erp.dao;

import erp.dao.provider.DetailDaoProvider;
import erp.domain.Detail;
import erp.vo.req.DetailFilterVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface DetailDao {
    /**
     * 查询所有
     *
     * @return
     */
    @Select("SELECT * FROM detail FORCE INDEX(d_date) ORDER BY d_date DESC")
    @Results(id = "detailMap", value = {
            @Result(id = true, column = "d_id", property = "id"),
            @Result(column = "d_date", property = "date"),
            @Result(column = "d_description", property = "description"),
            @Result(column = "c_id", property = "category", one = @One(select = "erp.dao.ForeignTableDao.getCategoryById")),
            @Result(column = "a_id", property = "account", one = @One(select = "erp.dao.ForeignTableDao.getAccountById")),
            @Result(column = "p_id", property = "project", one = @One(select = "erp.dao.ForeignTableDao.getProjectById")),
            @Result(column = "dep_id", property = "department", one = @One(select = "erp.dao.ForeignTableDao.getDepartmentById")),
            @Result(column = "d_earning", property = "earning"),
            @Result(column = "d_expense", property = "expense"),
            @Result(column = "d_balance", property = "balance")
    })
    List<Detail> listAll();

    /**
     * 添加一条记录, 并返回自增主键的 id 给 detail
     *
     * @param detail
     */
    @Insert("insert into detail values(null,#{date},#{description},#{project.id},#{account.id},#{department.id},#{category.id}," +
            "#{earning},#{expense},#{balance})")
    @Options(useGeneratedKeys = true, keyColumn = "d_id", keyProperty = "id")
    void add(Detail detail);

    /**
     * 从Excel文件导入多条记录
     *
     * @param details
     */
    @InsertProvider(type = DetailDaoProvider.class, method = "addByBatchSql")
    void insertFromExcelByBatch(List<Detail> details);

    /**
     * 根据id查找记录
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM detail d WHERE d_id=#{id} ")
    @ResultMap("detailMap")
    Detail getById(int id);

    /**
     * 修改一条记录
     *
     * @param detail
     */
    @Update("update detail set d_date=#{date},d_description=#{description},c_id=#{category.id}," +
            "a_id=#{account.id},p_id=#{project.id},dep_id=#{department.id},d_earning=#{earning}," +
            "d_expense=#{expense},d_balance=#{balance} " +
            "where d_id=#{id} ")
    void update(Detail detail);

    /**
     * 修改两个时间之间(不包括边界值)的所有记录的结存
     *
     * @param change
     * @param late
     * @param before
     */
    @Update("update detail set d_balance=d_balance+#{change} where d_date>#{before} and d_date<#{late}")
    void updateDuring(@Param("change") BigDecimal change, @Param("before") Date before, @Param("late") Date late);

    /**
     * 修改date时间之后的所有记录的结存
     *
     * @param change
     * @param date
     */
    @Update("update detail set d_balance=d_balance+#{change} where d_date>#{date}")
    void updateLater(@Param("change") BigDecimal change, @Param("date") Date date);

    /**
     * 查询date之前的一条记录
     *
     * @param date
     * @return
     */
    @Select("select * from detail where d_date<#{date} order by d_date DESC limit 1")
    @ResultMap("detailMap")
    Detail findBeforeOne(Date date);

    /**
     * 根据id删除一条记录
     *
     * @param id
     */
    @Delete("delete from detail where d_id=#{id}")
    void delete(int id);

    /**
     * 根据条件来获取记录
     *
     * @param vo
     * @return
     */
    @SelectProvider(type = DetailDaoProvider.class, method = "listByFilterSql")
    @ResultMap("detailMap")
    List<Detail> listByFilter(DetailFilterVo vo);

    /**
     * 返回没有凭证的明细记录的id的Set集合
     *
     * @return
     */
    @Select("SELECT d.d_id FROM detail d LEFT JOIN voucher v ON v.d_id=d.d_id WHERE v.id IS NULL")
    Set<Integer> listNoVoucherDetailId();
}
