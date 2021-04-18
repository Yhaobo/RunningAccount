package erp.shiro.realms;

import erp.entity.rbac.Permission;
import erp.entity.rbac.Role;
import erp.entity.rbac.User;
import erp.service.rbac.GroupService;
import erp.service.rbac.PermissionService;
import erp.service.rbac.RoleService;
import erp.service.rbac.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Yhaobo
 * @date 2020/1/30
 */
public class MySqlRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;

    public MySqlRealm(CredentialsMatcher matcher) {
        super(matcher);
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        String groupId = user.getGroupId();
        if ("1".equals(groupId)) {
            //管理员组拥有所有权限
            List<Permission> permissions = permissionService.listAll();
            for (Permission permission : permissions) {
                simpleAuthorizationInfo.addStringPermission(permission.getPerm());
            }
        } else {
            //非管理员
            List<Role> roles = groupService.listRole(groupId);
            for (Role role : roles) {
                List<Permission> permissions = roleService.listPermission(role.getId());
                for (Permission permission : permissions) {
                    simpleAuthorizationInfo.addStringPermission(permission.getPerm());
                }
            }
        }
        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        User user = userService.getByUsername(username);
        if (null == user) {
            throw new UnknownAccountException("此用户不存在");
        } else {
            //临时保存密码
            String password = user.getPassword();
            //用户密码和id置空
            user.setPassword(null);
            user.setId(null);
            //使用用户名为盐值
            ByteSource salt = ByteSource.Util.bytes(user.getUsername());
            return new SimpleAuthenticationInfo(user, password, salt, user.getUsername());
        }
    }

    //hash加密
//    public static void main(String[] args) {
//        SimpleHash result = new SimpleHash("md5", "123", "user", 7);
//        System.out.println(result);
//    }
}
