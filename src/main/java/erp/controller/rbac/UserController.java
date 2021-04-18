package erp.controller.rbac;

import erp.entity.rbac.User;
import erp.service.rbac.UserService;
import erp.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yhaobo
 * @date 2021-03-05 10:34
 */
@RestController
@Slf4j
@RequestMapping("rbac/user")
public class UserController {
    @Autowired
    private UserService service;

    @DeleteMapping("logout")
    public R logout() {
        SecurityUtils.getSubject().logout();
        return R.ok().message("成功登出");
    }

    @PutMapping
    public R update(@RequestBody User user) {
        if ("1".equals(user.getId()) && !"1".equals(user.getGroupId())) {
            return R.fail().message("初始管理员用户不可改组");
        }
        boolean flag = service.update(user);
        if (flag) {
            if (((User)(SecurityUtils.getSubject().getPrincipal())).getUsername().equals(user.getUsername())) {
                SecurityUtils.getSubject().logout();
            }
            return R.ok();
        } else {
            return R.fail().message("无可更新数据");
        }
    }

    @PostMapping
    public R add(@RequestBody User user) {
        service.add(user);
        return R.ok();
    }

    @GetMapping("listAll")
    public R listAll() {
        return R.ok().data(service.listAll());
    }

    @PostMapping("login")
    public R login(@RequestBody User user) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return R.ok().data(subject.getPrincipal());
        }
        try {
            subject.login(new UsernamePasswordToken(user.getUsername(), user.getPassword()));
            return R.ok().data(subject.getPrincipal());
        } catch (UnknownAccountException e) {
            log.error(e.getMessage(), e);
            return R.fail().message(e.getMessage());
        } catch (AuthenticationException e) {
            log.error(e.getMessage(), e);
            return R.fail().message("密码错误");
        }
    }

    @DeleteMapping
    public R delete(@RequestBody String userId) {
        if ("1".equals(userId)) {
            return R.fail().message("初始管理员用户不可删除");
        }
        if (service.delete(userId)) {
            return R.ok();
        } else {
            return R.fail().message("无可删除数据");
        }
    }
}
