package erp.service;

import erp.dao.ForeignTableDao;
import erp.domain.Account;
import erp.domain.Category;
import erp.domain.Department;
import erp.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SettingService {
    //    @Autowired
//    private AccountDao accountDao;
//    @Autowired
//    private ProjectDao projectDao;
//    @Autowired
//    private CategoryDao categoryDao;
//    @Autowired
//    private DepartmentDao departmentDao;
    @Autowired
    private ForeignTableDao dao;

    @Transactional(readOnly = true)
    public List<Category> findCategorys() {
        return dao.findAllCategory();
    }

    @Transactional(readOnly = true)
    public List<Department> findDepartments() {
        return dao.findAllDepartment();
    }

    @Transactional(readOnly = true)
    public List<Account> findAccounts() {
        return dao.findAllAccount();
    }

    @Transactional(readOnly = true)
    public List<Project> findProjects() {
        return dao.findAllProject();
    }

    public void addCategory(Category category) {
        dao.add("category", category);
    }

    public void addDepartment(Department department) {
        dao.add("department", department);
    }

    public void addAccount(Account account) {
        dao.add("account", account);
    }

    public void addProject(Project project) {
        dao.add("project", project);
    }

    public void updateCategory(Category category) {
        dao.update("category", category);
    }

    public void updateDepartment(Department department) {
        dao.update("department", department);
    }

    public void updateAccount(Account account) {
        dao.update("account", account);
    }

    public void updateProject(Project project) {
        dao.update("project", project);
    }
}
