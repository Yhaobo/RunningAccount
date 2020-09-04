package erp.dao;

import erp.entity.Account;
import erp.entity.Category;
import erp.entity.Department;
import erp.entity.Project;
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
    Account getAccountById(int id);

    @Select("select id, name from category where id=#{id}")
    Category getCategoryById(int id);

    @Select("select id, name from department where id=#{id}")
    Department getDepartmentById(int id);

    @Select("select id, name from project where id=#{id}")
    Project getProjectById(int id);

    @Select("select id, name from account ")
    List<Account> listAccount();

    @Select("select id, name from category ")
    List<Category> listCategory();

    @Select("select id, name from department ")
    List<Department> listDepartment();

    @Select("select id, name from project ")
    List<Project> listProject();

    @Insert("insert into ${tableName}(name) values(#{entity.name})")
    void insert(@Param("tableName") String tableName, @Param("entity") Object entity);

    @Update("update ${tableName} set name=#{entity.name} where id=#{entity.id}")
    void update(@Param("tableName") String tableName, @Param("entity") Object entity);
}
