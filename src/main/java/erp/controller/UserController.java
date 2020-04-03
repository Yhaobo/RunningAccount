package erp.controller;

import erp.domain.User;
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

import java.util.List;

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

    @PostMapping("/alterPassword")
    public ResultInfo alterPassword(String[] password, String username) {
        if (password[0].equals(password[1])) {
            try {
                service.updatePasswordByUser(new User(username, password[0]));
                return new ResultInfo(true, service.getLevelByUsername(username));
            } catch (Exception e) {
                log.error("[method:alterPassword]" + e.getMessage());
                e.printStackTrace();
                return new ResultInfo(false, "修改失败");
            }
        } else {
            return new ResultInfo(false, "密码不一致");
        }
    }

    @RequestMapping("/getRole")
    public ResultInfo getRole() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        return new ResultInfo(true, principal);
    }

    @RequestMapping("/listUsername")
    public ResultInfo listUsername() {
        List<String> usernameList = service.listUsername();
        return new ResultInfo(true,usernameList);
    }
}
