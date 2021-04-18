package erp.dao.rbac;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import erp.entity.rbac.Group;
import erp.entity.rbac.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Yhaobo
 * @date 2021-03-05 9:23
 */
@Mapper
public interface GroupMapper extends BaseMapper<Group> {
    /**
     * @param groupId 组id
     * @return 此组所拥有的所有角色
     */
    List<Role> listRole(@Param("groupId") String groupId);

    /**
     * 清空此组与角色的关系
     *
     * @param groupId 此组id
     */
    void clearAssignedRoles(@Param("groupId") String groupId);

    /**
     * 建立组与角色的关系
     *
     * @param roleId  角色id
     * @param groupId 组id
     */
    void assignRole(@Param("roleId") String roleId, @Param("groupId") String groupId);
}
