package erp.service;

import erp.dao.OptionDao;
import erp.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OptionService {
    @Autowired
    private OptionDao dao;

    @Transactional(readOnly = true)
    public List<Category> listCategory() {
        return dao.listCategory();
    }

    @Transactional(readOnly = true)
    public List<Department> listDepartment() {
        return dao.listDepartment();
    }

    @Transactional(readOnly = true)
    public List<Account> listAccount() {
        return dao.listAccount();
    }

    @Transactional(readOnly = true)
    public List<Project> listProject() {
        return dao.listProject();
    }

    public void addCategory(Option option) {
        dao.insert("category", option);
    }

    public void addDepartment(Option option) {
        dao.insert("department", option);
    }

    public void addAccount(Option option) {
        dao.insert("account", option);
    }

    public void addProject(Option option) {
        dao.insert("project", option);
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
