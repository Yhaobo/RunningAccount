package erp.controller;

import erp.domain.*;
import erp.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 基础设置
 */
@Controller
@RequestMapping("/setting")
public class SettingController {
    @Autowired
    private SettingService settingService;

    @RequestMapping("/findCategorys")
    @ResponseBody
    public ResultInfo findCategorys() {
        List<Category> directions = settingService.findCategorys();
        return new ResultInfo(true, directions);
    }

    @RequestMapping("/addCategory")
    @ResponseBody
    public ResultInfo addCategory(Category category) {
        if (category.getName().isEmpty()) {
            return new ResultInfo(false, "不能为空!");
        }

        try {
            settingService.addCategory(category);
            return new ResultInfo(true);
        } catch (Exception e) {
            return new ResultInfo(false, "添加失败!");
        }
    }

    @RequestMapping("/updateCategory")
    public void updateCategory(Category category, HttpServletResponse response, HttpServletRequest request) throws IOException {
        settingService.updateCategory(category);
        response.sendRedirect(request.getContextPath() + "/basic_setting.html?title=" + URLEncoder.encode("分类", "utf-8") + "&path=Category");
    }

    @RequestMapping("/findDepartments")
    @ResponseBody
    public ResultInfo findDepartments() {
        List<Department> departments = settingService.findDepartments();
        return new ResultInfo(true, departments);
    }

    @RequestMapping("/addDepartment")
    @ResponseBody
    public ResultInfo addDepartment(Department department) {
        if (department.getName().isEmpty()) {
            return new ResultInfo(false, "不能为空!");
        }
        try {
            settingService.addDepartment(department);
            return new ResultInfo(true);
        } catch (Exception e) {
            return new ResultInfo(false, "添加失败!");
        }
    }

    @RequestMapping("/updateDepartment")
    public void updateDepartment(Department department, HttpServletResponse response, HttpServletRequest request) throws IOException {
        settingService.updateDepartment(department);
        response.sendRedirect(request.getContextPath() + "/basic_setting.html?title=" + URLEncoder.encode("部门", "utf-8") + "&path=Department");
    }

    @RequestMapping("/findAccounts")
    @ResponseBody
    public ResultInfo findAccounts() {
        List<Account> accounts = settingService.findAccounts();
        return new ResultInfo(true, accounts);
    }

    @RequestMapping("/addAccount")
    @ResponseBody
    public ResultInfo addAccount(Account account) {
        if (account.getName().isEmpty()) {
            return new ResultInfo(false, "不能为空!");
        }

        try {
            settingService.addAccount(account);
            return new ResultInfo(true);
        } catch (Exception e) {
            return new ResultInfo(false, "添加失败!");
        }
    }

    @RequestMapping("/updateAccount")
    public void updateAccount(Account account, HttpServletResponse response, HttpServletRequest request) throws IOException {
        settingService.updateAccount(account);
        response.sendRedirect(request.getContextPath() + "/basic_setting.html?title=" + URLEncoder.encode("账户", "utf-8") + "&path=Account");
    }

    @RequestMapping("/findProjects")
    @ResponseBody
    public ResultInfo findProjects() {
        List<Project> projects = settingService.findProjects();
        return new ResultInfo(true, projects);
    }

    @RequestMapping("/addProject")
    @ResponseBody
    public ResultInfo addProject(Project project) {
        if (project.getName().isEmpty()) {
            return new ResultInfo(false, "不能为空!");
        }
        try {
            settingService.addProject(project);
            return new ResultInfo(true);
        } catch (Exception e) {
            return new ResultInfo(false, "添加失败!");
        }
    }

    @RequestMapping("/updateProject")
    public void updateProject(Project project, HttpServletResponse response, HttpServletRequest request) throws IOException {
        settingService.updateProject(project);
        response.sendRedirect(request.getContextPath() + "/basic_setting.html?title=" + URLEncoder.encode("项目", "utf-8") + "&path=Project");
    }
}
