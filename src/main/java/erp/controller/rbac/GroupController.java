package erp.controller.rbac;

import erp.entity.rbac.Group;
import erp.entity.rbac.Role;
import erp.entity.rbac.GroupWithUsers;
import erp.service.rbac.GroupService;
import erp.service.rbac.UserService;
import erp.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yhaobo
 * @date 2021-03-05 10:35
 */
@RestController
@RequestMapping("rbac/group")
public class GroupController {
    @Autowired
    private GroupService service;

    @Autowired
    private UserService userService;

    @PostMapping("assignRoles/{groupId}")
    public R assignRoles(@RequestBody List<String> roleIds, @PathVariable String groupId) {
        service.assignRoles(roleIds, groupId);
        return R.ok();
    }

    @GetMapping("listRole/{groupId}")
    public R listRole(@PathVariable String groupId) {
        List<Role> roles = service.listRole(groupId);
        ArrayList<String> roleIds = new ArrayList<>(roles.size());
        for (Role role : roles) {
            roleIds.add(role.getId());
        }
        return R.ok().data(roleIds);
    }

    @GetMapping("listAll")
    public R listAll() {
        return R.ok().data(service.listAll());
    }

    @PostMapping
    public R add(@RequestBody Group group) {
        service.add(group);
        return R.ok();
    }

    @GetMapping("listGroupAndUser")
    public R listGroupAndUser() {
        List<GroupWithUsers> list = service.listGroupAndUser();
        return R.ok().data(list);
    }

    @PutMapping
    public R update(@RequestBody Group group) {
        if (service.update(group)) {
            return R.ok();
        } else {
            return R.fail().message("无可更新数据");
        }
    }

    @DeleteMapping
    public R delete(@RequestBody String groupId) {
        if ("1".equals(groupId)) {
            return R.fail().message("管理员组不可删除");
        }
        if (userService.countByGroupId(groupId)>0) {
            return R.fail().message("请先清空此组下的用户");
        }
        if (service.delete(groupId)) {
            return R.ok();
        } else {
            return R.fail().message("无可删除数据");
        }
    }
}
