package erp.service.rbac;

import erp.dao.rbac.GroupMapper;
import erp.dao.rbac.UserMapper;
import erp.entity.rbac.Group;
import erp.entity.rbac.Role;
import erp.entity.rbac.User;
import erp.entity.rbac.GroupWithUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Yhaobo
 * @date 2021-03-05 10:53
 */
@Service
public class GroupService {
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private UserMapper userMapper;

    public List<Group> listAll() {
        return groupMapper.selectList(null);
    }

    public List<Role> listRole(String groupId) {
        return groupMapper.listRole(groupId);
    }

    public void add(Group group) {
        groupMapper.insert(group);
    }

    /**
     * 构建树结构（第一层为Group，第二层为User）
     */
    public List<GroupWithUsers> listGroupAndUser() {
        List<Group> groups = groupMapper.selectList(null);
        List<User> users = userMapper.selectList(null);
        Map<String, List<GroupWithUsers>> stringListHashMap = new HashMap<>();
        //map生成（groupId为key，List<GroupWithUsers>为value）
        for (User user : users) {
            String groupId = user.getGroupId();
            if (stringListHashMap.containsKey(groupId)) {
                List<GroupWithUsers> list = stringListHashMap.get(groupId);
                list.add(new GroupWithUsers(user.getId(), user.getUsername(), null, null));
            } else {
                List<GroupWithUsers> list = new ArrayList<>();
                list.add(new GroupWithUsers(user.getId(), user.getUsername(), null, null));
                stringListHashMap.put(groupId, list);
            }
        }
        List<GroupWithUsers> list = new ArrayList<>();
        for (Group group : groups) {
            List<GroupWithUsers> children = stringListHashMap.get(group.getId());
            list.add(new GroupWithUsers(group.getId(), group.getName(), group.getDescription(), children));
        }
        Collections.reverse(list);
        return list;
    }

    public boolean update(Group group) {
        return groupMapper.updateById(group) == 1;
    }

    @Transactional(rollbackFor = Exception.class,timeout = 15)
    public boolean delete(String groupId) {
        //清除组与角色关系表的相关数据
        groupMapper.clearAssignedRoles(groupId);
        return groupMapper.deleteById(groupId) == 1;
    }

    @Transactional(rollbackFor = Exception.class, timeout = 15)
    public void assignRoles(List<String > roleIds, String groupId) {
        //先删除之前记录的所有角色
        groupMapper.clearAssignedRoles(groupId);
        //再插入新分配的角色
        for (String roleId : roleIds) {
            groupMapper.assignRole(roleId, groupId);
        }
    }
}
