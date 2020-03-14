package erp.shiro.realms;

import erp.domain.User;
import erp.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Yhaobo 2020/1/30
 */
public class Realm extends AuthorizingRealm {
    @Autowired
    UserService service;

    public Realm(CredentialsMatcher matcher) {
        super(matcher);
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object principal = principalCollection.getPrimaryPrincipal();
        Set<String> roles = new HashSet<>();
        if (principal.equals(0)) {
            roles.add("writer");
            roles.add("reader");
        } else if (principal.equals(1)) {
            roles.add("reader");
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
            //使用用户名为盐值
            ByteSource salt = ByteSource.Util.bytes(user.getU_username());
            return new SimpleAuthenticationInfo(user.getU_level(), user.getU_password(), salt, user.getU_username());
        }
    }

    //hash加密
    public static void main(String[] args) {
        SimpleHash result = new SimpleHash("md5", "123", "visitor", 7);
        System.out.println(result);
    }
}
