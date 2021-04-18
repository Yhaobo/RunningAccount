package erp.aop;

import erp.config.ShiroConfig;
import erp.entity.rbac.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 处理密码的解码和加密
 *
 * @author Yhaobo
 * @date 2021-03-06 21:08
 */
@Component
@Aspect
public class EncryptionAspect {
    @Autowired
    private ShiroConfig shiroConfig;

    @Before("execution(* erp.controller.rbac.UserController.*(erp.entity.rbac.User))")
    public void base64Decode(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length == 1 && args[0] instanceof User) {
            User user = (User) args[0];
            user.setPassword(new String(Base64.getDecoder().decode(user.getPassword()), StandardCharsets.UTF_8));
        }
    }

    @Before("execution(* erp.service.rbac.UserService.*(erp.entity.rbac.User))")
    public void haseEncode(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length == 1 && args[0] instanceof User) {
            User user = (User) args[0];
            user.setPassword(new SimpleHash(shiroConfig.getAlgorithmName(), user.getPassword(), user.getUsername(), shiroConfig.getHashIterations()).toString());
        }
    }
}
