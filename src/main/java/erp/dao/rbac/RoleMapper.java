package erp.dao.rbac;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import erp.entity.rbac.Permission;
import erp.entity.rbac.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Yhaobo
 * @date 2021-03-05 9:24
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据角色id获取拥有权限
     *
     * @param roleId 角色id
     * @return 权限列表
     */
    List<Permission> listPermission(@Param("roleId") String roleId);

    /**
     * 清空此角色与权限的关系
     *
     * @param roleId 角色id
     */
    void clearAssignedPermissions(@Param("roleId") String roleId);

    /**
     * 建立角色与权限的关系
     * @param permissionId
     * @param roleId
     */
    void assignPermission(@Param("permissionId") String permissionId, @Param("roleId") String roleId);
}
