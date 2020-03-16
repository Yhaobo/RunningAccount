package erp.dao;

import erp.dao.provider.DetailDaoProvider;
import erp.domain.Detail;
import erp.vo.req.DetailFilterVo;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface DetailDao {
    /**
     * 查询所有
     *
     * @return
     */
    @Select("SELECT * FROM detail d " +
            "LEFT JOIN account a ON a.id=d.a_id " +
            "LEFT JOIN category c ON c.id=d.c_id " +
            "LEFT JOIN project p ON p.id=d.p_id " +
            "LEFT JOIN department dep ON dep.id=d.dep_id " +
            "ORDER BY d_date DESC")
    @Results(id = "detailMap", value = {
            @Result(id = true, column = "d_id", property = "id"),
            @Result(column = "d_date", property = "date"),
            @Result(column = "d_description", property = "description"),
            @Result(column = "c_id", property = "category", one = @One(select = "erp.dao.ForeignTableDao.findCategoryById")),
            @Result(column = "a_id", property = "account", one = @One(select = "erp.dao.ForeignTableDao.findAccountById")),
            @Result(column = "p_id", property = "project", one = @One(select = "erp.dao.ForeignTableDao.findProjectById")),
            @Result(column = "dep_id", property = "department", one = @One(select = "erp.dao.ForeignTableDao.findDepartmentById")),
            @Result(column = "d_earning", property = "earning"),
            @Result(column = "d_expense", property = "expense"),
            @Result(column = "d_balance", property = "balance")
    })
    List<Detail> findAll();

    /**
     * 查询最新的结存
     *
     * @return
     */
    @Select("SELECT d_balance FROM detail ORDER BY d_date DESC LIMIT 1")
    BigDecimal findLatestBalance();

    /**
     * 添加一条记录
     *
     * @param form
     */
    @Insert("insert into detail values(null,#{date},#{description},#{project.id},#{account.id},#{department.id},#{category.id}," +
            "#{earning},#{expense},#{balance})")
    void add(Detail form);

    /**
     * 添加一条记录
     *
     * @param detail 从excel解析得到的实体类(外键引用的属性只有name, 没有id)
     */
    @Insert("insert into detail values(null,#{date},#{description}," +
            "(select id from project where name=#{project.name})," +
            "(select id from account where name=#{account.name})," +
            "(select id from department where name=#{department.name})," +
            "(select id from category where name=#{category.name})," +
            "#{earning},#{expense},#{balance})")
    void addByExcel(Detail detail);

    /**
     * 根据id查找记录
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM detail d " +
            "LEFT JOIN category c ON c.id=d.c_id " +
            "LEFT JOIN account a ON a.id=d.a_id " +
            "LEFT JOIN project p ON p.id=d.p_id " +
            "LEFT JOIN department dep ON dep.id=d.dep_id " +
            "WHERE d_id=#{id} ")
    @ResultMap("detailMap")
    Detail findOne(int id);

    /**
     * 查询date时间之后的所有记录
     *
     * @param date
     * @return
     */
    @Select("select * from detail where d_date>#{date} order by d_date ")
    @ResultMap("detailMap")
    List<Detail> findLaterList(Date date);

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
     * 查询两个时间之间(不包括)的所有记录
     *
     * @param before
     * @param late
     * @return
     */
    @Select("select * from detail where d_date>#{before} and d_date<#{late} order by d_date")
    @ResultMap("detailMap")
    List<Detail> findBetweenList(@Param("before") Date before, @Param("late") Date late);

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
}
