package erp.controller;

import erp.domain.*;
import erp.service.SettingService;
import erp.util.ResultInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 基础设置
 */
@Controller
@RequestMapping("/setting")
public class SettingController {
    @Autowired
    private SettingService settingService;

    private Logger log = LoggerFactory.getLogger(this.getClass());

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
            log.error("[method:addCategory]" + e.getMessage());
            return new ResultInfo(false, "添加失败!");
        }
    }

    @RequestMapping("/updateCategory")
    @ResponseBody
    public ResultInfo updateCategory(Category category, HttpServletRequest request) throws IOException {
        try {
            settingService.updateCategory(category);
            return new ResultInfo(true);
        } catch (Exception e) {
            log.error("[method:updateCategory]" + e.getMessage());
            return new ResultInfo(false, "修改失败!");
        }
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
            log.error("[method:addDepartment]" + e.getMessage());

            return new ResultInfo(false, "添加失败!");
        }
    }

    @RequestMapping("/updateDepartment")
    @ResponseBody
    public ResultInfo updateDepartment(Department department, HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            settingService.updateDepartment(department);
            return new ResultInfo(true);
        } catch (Exception e) {
            log.error("[method:updateDepartment]" + e.getMessage());

            return new ResultInfo(false, "修改失败!");
        }
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
            log.error("[method:addAccount]" + e.getMessage());

            return new ResultInfo(false, "添加失败!");
        }
    }

    @RequestMapping("/updateAccount")
    @ResponseBody
    public ResultInfo updateAccount(Account account, HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            settingService.updateAccount(account);
            return new ResultInfo(true);
        } catch (Exception e) {
            log.error("[method:updateAccount]" + e.getMessage());

            return new ResultInfo(false, "修改失败!");
        }
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
            log.error("[method:addProject]" + e.getMessage());

            return new ResultInfo(false, "添加失败!");
        }
    }

    @RequestMapping("/updateProject")
    @ResponseBody
    public ResultInfo updateProject(Project project, HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            settingService.updateProject(project);
            return new ResultInfo(true);
        } catch (Exception e) {
            log.error("[method:updateProject]" + e.getMessage());

            return new ResultInfo(false, "修改失败!");
        }
    }
}
