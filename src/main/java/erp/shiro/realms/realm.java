package erp.shiro.realms;

import erp.domain.User;
import erp.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Yhaobo 2020/1/30
 */
public class realm extends AuthorizingRealm {
    @Autowired
    UserService service;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        User user = service.findUsername(username);
        if (null == user) {
            throw new UnknownAccountException("此用户不存在");
        } else {
            ByteSource salt = ByteSource.Util.bytes(user.getU_username());
            return new SimpleAuthenticationInfo(user.getU_level(), user.getU_password(), salt, user.getU_username());
        }
    }

    //hash加密
    public static void main(String[] args) {
        SimpleHash result = new SimpleHash("md5", "123", "yc8858", 7);
        System.out.println(result);
    }
}
