package erp.service.rbac;

import erp.dao.rbac.PermissionMapper;
import erp.entity.rbac.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Yhaobo
 * @date 2021-03-05 10:55
 */
@Service
public class PermissionService {
    @Autowired
    private PermissionMapper mapper;

    public List<Permission> listAll() {
        return mapper.selectList(null);
    }

}
