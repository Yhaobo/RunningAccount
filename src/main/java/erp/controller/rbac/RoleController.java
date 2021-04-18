package erp.controller.rbac;

import erp.entity.rbac.Permission;
import erp.entity.rbac.Role;
import erp.service.rbac.RoleService;
import erp.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yhaobo
 * @date 2021-03-05 10:36
 */
@RestController
@RequestMapping("rbac/role")
public class RoleController {
    @Autowired
    private RoleService service;

    @PostMapping("assignPermissions/{roleId}")
    public R assignPermissions(@PathVariable String roleId,@RequestBody List<String> permissionIds){
        service.assignPermissions(permissionIds, roleId);
        return R.ok();
    }
    @GetMapping("listPermission/{roleId}")
    public R listPermission(@PathVariable String roleId) {
        List<Permission> permissions = service.listPermission(roleId);
        List<String> permissionIds = new ArrayList<>(permissions.size());
        for (Permission permission : permissions) {
            permissionIds.add(permission.getId());
        }
        return R.ok().data(permissionIds);
    }

    @DeleteMapping
    public R delete(@RequestBody String roleId) {
        if (service.delete(roleId)) {
            return R.ok();
        } else {
            return R.fail().message("无可删除数据");
        }
    }

    @PutMapping
    public R update(@RequestBody Role role) {
        if (service.update(role)) {
            return R.ok();
        } else {
            return R.fail().message("无可更新数据");
        }
    }

    @PostMapping
    public R add(@RequestBody Role role) {
        service.add(role);
        return R.ok();
    }

    @GetMapping("listAll")
    public R listAll() {
        return R.ok().data(service.listAll());
    }
}
