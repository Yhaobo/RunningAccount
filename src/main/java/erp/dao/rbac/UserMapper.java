package erp.dao.rbac;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import erp.entity.rbac.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Yhaobo
 * @date 2021-03-04 10:41
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
