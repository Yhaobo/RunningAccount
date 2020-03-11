package erp.controller;

import erp.domain.User;
import erp.util.ResultInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户
 */
@RequestMapping("/user")
@Controller
public class UserController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @RequestMapping("/login")
    public ResultInfo login(User form) {
        Subject user = SecurityUtils.getSubject();
        if (user.isAuthenticated()) {
            return new ResultInfo(true);
        }
        try {
            user.login(new UsernamePasswordToken(form.getU_username(), form.getU_password()));
            return new ResultInfo(true);
        } catch (AuthenticationException e) {
            log.error("[method:login]" + e.getMessage());
            return new ResultInfo(false, "用户名或密码错误!");
        }
    }
}
