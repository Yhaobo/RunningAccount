package erp.dao;

import erp.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 所有外表(detail表外键所指向的表)的通用CRU
 */
@Repository
public interface OptionDao {
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

    @Select("select name from account ")
    List<String> listAccountName();

    @Select("select name from category ")
    List<String> listCategoryName();

    @Select("select name from department ")
    List<String> listDepartmentName();

    @Select("select name from project ")
    List<String> listProjectName();

    @Insert("insert into ${tableName}(name) values(#{option.name})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "option.id")
    void insert(@Param("tableName") String tableName, @Param("option") Option option);

    @Update("update ${tableName} set name=#{option.name} where id=#{option.id}")
    void update(@Param("tableName") String tableName, @Param("option") Option option);

    @Select("select id from account where name=#{accountName}")
    Integer getAccountId(String accountName);
}
