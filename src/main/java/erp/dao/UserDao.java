package erp.dao;

import erp.domain.User;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserDao {

    @Select("select * from user where u_username=#{u_username} and u_password=#{u_password}")
    User findOneByUser(User form);

    @Select("select * from user where u_username=#{username}")
    User findByUsername(String username);

    @Update("update user set u_password=#{u_password},u_username=#{u_username} where u_level=#{u_level}")
    void updateByUser(User user);

    @Select("select u_username from user order by u_level")
    List<String> listUsername();

    @Select("select u_level from user where u_username=#{username}")
    Integer getLevelByUsername(String username);

    @Select("select u_username from user where u_level=#{level}")
    String getUsernameByLevel(Integer level);

}
