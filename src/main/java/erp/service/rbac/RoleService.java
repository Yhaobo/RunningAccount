package erp.service.rbac;

import erp.dao.rbac.RoleMapper;
import erp.entity.rbac.Permission;
import erp.entity.rbac.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Yhaobo
 * @date 2021-03-05 10:54
 */
@Service
public class RoleService {
    @Autowired
    private RoleMapper mapper;

    public List<Role> listAll() {
        return mapper.selectList(null);
    }

    public List<Permission> listPermission(String roleId) {
        return mapper.listPermission(roleId);
    }

    public void add(Role role) {
        mapper.insert(role);
    }

    public boolean update(Role role) {
        return mapper.updateById(role) == 1;
    }

    @Transactional(rollbackFor = Exception.class, timeout = 15)
    public boolean delete(String roleId) {
        //清除角色与权限关系表的相关数据
        mapper.clearAssignedPermissions(roleId);
        return mapper.deleteById(roleId) == 1;
    }

    @Transactional(rollbackFor = Exception.class, timeout = 15)
    public void assignPermissions(List<String> permissionIds, String roleId) {
        //先清空拥有权限
        mapper.clearAssignedPermissions(roleId);
        //再插入分配权限
        for (String permissionId : permissionIds) {
            mapper.assignPermission(permissionId, roleId);
        }
    }
}
