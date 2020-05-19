package erp.dao;

import erp.domain.Account;
import erp.domain.Category;
import erp.domain.Department;
import erp.domain.Project;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 所有外表(detail表外键所指向的表)的通用CRU
 */
@Repository
public interface ForeignTableDao {
    @Select("select id, name from account where id=#{id}")
    Account findAccountById(int id);

    @Select("select id, name from category where id=#{id}")
    Category findCategoryById(int id);

    @Select("select id, name from department where id=#{id}")
    Department findDepartmentById(int id);

    @Select("select id, name from project where id=#{id}")
    Project findProjectById(int id);

    @Select("select id, name from account order by id")
    List<Account> findAllAccount();

    @Select("select id, name from category order by id")
    List<Category> findAllCategory();

    @Select("select id, name from department order by id")
    List<Department> findAllDepartment();

    @Select("select id, name from project order by id")
    List<Project> findAllProject();

    @Insert("insert into ${tableName}(name) values(#{entity.name})")
    void insert(@Param("tableName") String tableName, @Param("entity") Object entity);

    @Update("update ${tableName} set name=#{entity.name} where id=#{entity.id}")
    void update(@Param("tableName") String tableName, @Param("entity") Object entity);
}
