package erp.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收支明细表的实体类
 */
public class Detail implements Serializable {
    private Integer id;
    private Date date;
    private String description;
    private Project project;
    private Account account;
    private Department department;
    private Category category;
    private BigDecimal earning;
    private BigDecimal expense;
    private BigDecimal balance;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public BigDecimal getEarning() {
        return earning;
    }

    public void setEarning(BigDecimal earning) {
        this.earning = earning;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "id=" + id +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", project=" + project +
                ", account=" + account +
                ", department=" + department +
                ", category=" + category +
                ", earning=" + earning +
                ", expense=" + expense +
                ", balance=" + balance +
                '}';
    }
}
