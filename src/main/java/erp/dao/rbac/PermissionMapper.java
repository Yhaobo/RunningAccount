package erp.dao.rbac;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import erp.entity.rbac.Permission;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Yhaobo
 * @date 2021-03-04 11:05
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

}
