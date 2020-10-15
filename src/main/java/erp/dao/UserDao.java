package erp.dao;

import erp.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    @Insert("insert into user(username,password,level) values(#{username},#{password},#{level})")
    void insert(User user);

    @Select("select * from user where username=#{username}")
    User getByUsername(String username);

    @Update("update user set password=#{password},username=#{username} where id=#{id}")
    void updateByUser(User user);

    @Select("select username from user order by level")
    List<String> listUsername();

    @Select("select level from user where username=#{username}")
    Integer getLevelByUsername(String username);

    @Select("select username,id from user where level=#{level}")
    List<User> getUsernameByLevel(Integer level);

    @Delete("delete from user where username=#{username}")
    void delete(String username);
}
