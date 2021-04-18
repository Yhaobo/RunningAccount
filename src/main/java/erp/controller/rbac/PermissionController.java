package erp.controller.rbac;

import erp.service.rbac.PermissionService;
import erp.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yhaobo
 * @date 2021-03-05 10:37
 */
@RestController
@RequestMapping("rbac/permission")
public class PermissionController {
    @Autowired
    private PermissionService service;

    @GetMapping("listAll")
    public R listAll() {
        return R.ok().data(service.listAll());
    }
}
