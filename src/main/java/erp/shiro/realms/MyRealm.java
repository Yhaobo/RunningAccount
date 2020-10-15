package erp.shiro.realms;

import erp.entity.User;
import erp.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Yhaobo
 * @date 2020/1/30
 */
public class MyRealm extends AuthorizingRealm {
    @Autowired
    UserService service;

    public MyRealm(CredentialsMatcher matcher) {
        super(matcher);
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User principal = (User) principalCollection.getPrimaryPrincipal();
        Set<String> roles = new HashSet<>();
        switch (principal.getLevel()) {
            case 0:
                roles.add("admin");
                roles.add("user");
                break;
            case 1:
                roles.add("user");
                break;
            default:
                roles.add("visitor");
        }
        return new SimpleAuthorizationInfo(roles);
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        User user = service.findByUsername(username);
        if (null == user) {
            throw new UnknownAccountException("此用户不存在");
        } else {
            String password = user.getPassword();
            //密码置空
            user.setPassword(null);
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
