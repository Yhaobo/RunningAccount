package erp.controller;

import erp.entity.User;
import erp.service.UserService;
import erp.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
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

    @PostMapping("login")
    public R login(@RequestBody User loginForm) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return R.ok().data(subject.getPrincipal());
        }
        try {
            //密码解码
            loginForm.setPassword(new String(Base64.getDecoder().decode(loginForm.getPassword())));

            subject.login(new UsernamePasswordToken(loginForm.getUsername(), loginForm.getPassword()));
            return R.ok().data(subject.getPrincipal());
        } catch (UnknownAccountException e) {
            log.error(e.getMessage(), e);
            return R.fail().message(e.getMessage());
        } catch (AuthenticationException e) {
            log.error(e.getMessage(), e);
            return R.fail().message("密码错误");
        }
    }

    @PutMapping
    public R alter(@RequestBody User user) {
        //密码解码
        user.setPassword(new String(Base64.getDecoder().decode(user.getPassword())));
        service.updateByUser(user);
        return R.ok();
    }

    @GetMapping("listUsername/{level}")
    public R getUsername(@PathVariable Integer level) {
        List<User> username = service.getUsernameByLevel(level);
        return R.ok().data(username);
    }

    @DeleteMapping("logout")
    public R logout() {
        SecurityUtils.getSubject().logout();
        return R.ok().message("成功登出");
    }

    @PostMapping
    public R insert(@RequestBody User user) {
        if (user.getLevel() != null && user.getLevel() != 0) {
            //密码解码
            user.setPassword(new String(Base64.getDecoder().decode(user.getPassword())));
            try {
                service.insert(user);
            } catch (DuplicateKeyException e) {
                log.error("", e);
                return R.fail().message("此用户名已存在");
            }
            return R.ok();
        } else {
            return R.fail().message("管理员账号只允许一个");
        }
    }

    @DeleteMapping
    public R delete(@RequestBody User user) {
        if (user.getLevel() != null && user.getLevel() != 0) {
            service.delete(user);
            return R.ok();
        } else {
            return R.fail().message("管理员账号不允许删除");
        }
    }
}
