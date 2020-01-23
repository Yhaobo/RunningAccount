package erp.dao;

import erp.domain.User;
import org.apache.ibatis.annotations.Select;

public interface UserDao {

    @Select("select * from user where u_username=#{u_username} and u_password=#{u_password}")
    User findOne( User form);
}
