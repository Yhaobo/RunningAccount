package erp.controller;

import erp.entity.*;
import erp.service.OptionService;
import erp.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础设置
 */
@RestController
@RequestMapping("/option")
public class SettingController {
    @Autowired
    private OptionService optionService;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/all")
    public R getAllOption() {
        Map<String, List> map = new HashMap<>(8);
        map.put("accountOptions", optionService.listAccount());
        map.put("departmentOptions", optionService.listDepartment());
        map.put("projectOptions", optionService.listProject());
        map.put("categoryOptions", optionService.listCategory());
        return R.ok().data(map);
    }

    @GetMapping("/category")
    public R findCategory() {
        List<Category> categories = optionService.listCategory();
        return R.ok().data(categories);
    }

    @PostMapping("/category")
    public R addCategory(@RequestBody Option option) {
        if (StringUtils.isEmpty(option.getName())) {
            return R.fail().message("不能为空!");
        }

        try {
            optionService.addCategory(option);
            return R.ok().data(option.getId());
        } catch (DuplicateKeyException e) {
            return R.fail().message("此选项名已存在");
        }
    }

    @PutMapping("/category")
    public R updateCategory(@RequestBody Category category) {
        try {
            optionService.updateCategory(category);
            return R.ok();
        } catch (DuplicateKeyException e) {
            return R.fail().message("此选项名已存在");
        }
    }

    @GetMapping("/department")
    public R findDepartments() {
        List<Department> departments = optionService.listDepartment();
        return R.ok().data(departments);
    }

    @PostMapping("/department")
    public R addDepartment(@RequestBody Option option) {
        if (StringUtils.isEmpty(option.getName())) {
            return R.fail().message("不能为空!");
        }

        try {
            optionService.addDepartment(option);
            return R.ok().data(option.getId());
        } catch (DuplicateKeyException e) {
            return R.fail().message("此选项名已存在");
        }
    }

    @PutMapping("/department")
    public R updateDepartment(@RequestBody Department department) {
        try {
            optionService.updateDepartment(department);
            return R.ok();
        } catch (DuplicateKeyException e) {
            return R.fail().message("此选项名已存在");
        }
    }

    @GetMapping("/account")
    public R findAccounts() {
        List<Account> accounts = optionService.listAccount();
        return R.ok().data(accounts);
    }

    @PostMapping("/account")
    public R addAccount(@RequestBody Option option) {
        if (StringUtils.isEmpty(option.getName())) {
            return R.fail().message("不能为空!");
        }

        try {
            optionService.addAccount(option);
            return R.ok().data(option.getId());
        } catch (DuplicateKeyException e) {
            return R.fail().message("此选项名已存在");
        }
    }

    @PutMapping("/account")
    public R updateAccount(@RequestBody Account account) {
        try {
            optionService.updateAccount(account);
            return R.ok();
        } catch (DuplicateKeyException e) {
            return R.fail().message("此选项名已存在");
        }

    }

    @GetMapping("/project")
    public R findProjects() {
        List<Project> projects = optionService.listProject();
        return R.ok().data(projects);
    }

    @PostMapping("/project")
    public R addProject(@RequestBody Option option) {
        if (StringUtils.isEmpty(option.getName())) {
            return R.fail().message("不能为空!");
        }

        try {
            optionService.addProject(option);
            return R.ok().data(option.getId());
        } catch (DuplicateKeyException e) {
            return R.fail().message("此选项名已存在");
        }
    }

    @PutMapping("/project")
    public R updateProject(@RequestBody Project project) {
        try {
            optionService.updateProject(project);
            return R.ok();
        } catch (DuplicateKeyException e) {
            return R.fail().message("此选项名已存在");
        }

    }
}
