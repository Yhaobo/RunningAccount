package erp.dao;

import erp.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select * from user where u_username=#{u_username} and u_password=#{u_password}")
    User findOne( User form);

    @Select("select * from user where u_username=#{username}")
    User findUsername(String username);
}
