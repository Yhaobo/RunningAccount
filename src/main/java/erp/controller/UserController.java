package erp.controller;

import erp.entity.User;
import erp.service.UserService;
import erp.util.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户
 */
@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService service;

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

    @PostMapping("/alter")
    public ResultInfo alter(String[] password, User user) {
        if (password[0].equals(password[1])&&password[0].length()>=6) {
            try {
                user.setU_password(password[0]);
                service.updateByUser(user);
                return new ResultInfo(true, user.getU_level());
            } catch (Exception e) {
                log.error("[method:alter]" + e.getMessage());
                e.printStackTrace();
                return new ResultInfo(false, "修改失败");
            }
        } else {
            return new ResultInfo(false, "密码不一致或长度没有达到6位");
        }
    }

    @RequestMapping("/getLevel")
    public ResultInfo getLevel() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        return new ResultInfo(true, principal);
    }

    @RequestMapping("/getUsername")
    public ResultInfo getUsername(Integer u_level) {
        String username = service.getUsernameByLevel(u_level);
        return new ResultInfo(true,"",username);
    }
}
