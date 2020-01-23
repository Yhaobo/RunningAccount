package erp.controller;

import erp.domain.ResultInfo;
import erp.domain.User;
import erp.service.ExcelService;
import erp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * 用户
 */
@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ExcelService excelService;

    @ResponseBody
    @RequestMapping("/login")
    public ResultInfo login(User form, HttpSession session) {
        if (form.getU_username().length() > 0 && form.getU_password().length() > 0) {
            User user = userService.findOne(form);
            if (user != null) {
                session.setAttribute("user", user);
                return new ResultInfo(true);
            } else {
                return new ResultInfo(false, "密码或用户名错误!");
            }
        } else {
            return new ResultInfo(false, "用户名或密码不能为空!");
        }
    }

    @RequestMapping("/exit")
    public void exit(HttpSession session, HttpServletResponse response, HttpServletRequest request) throws IOException {
        session.invalidate();
        try {
            excelService.backups();//备份数据
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/index.html");
        response.getOutputStream().close();
    }

//    @ResponseBody
//    @RequestMapping("/register")
//    public ResultInfo register(User form) {
//        return null;
//    }
}
