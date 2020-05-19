package erp.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 收支明细表的实体类
 * 此类的所有BigDecimal类型的属性setter时,会舍弃小数点2位之后的部分
 */
public class Detail implements Serializable {
    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
        if (earning != null) {
            this.earning = earning.setScale(2, BigDecimal.ROUND_DOWN);
        }
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        if (expense != null) {
            this.expense = expense.setScale(2, BigDecimal.ROUND_DOWN);
        }
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        if (balance != null) {
            this.balance = balance.setScale(2, BigDecimal.ROUND_DOWN);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Detail detail = (Detail) o;
        return Objects.equals(id, detail.id) &&
                Objects.equals(date, detail.date) &&
                Objects.equals(description, detail.description) &&
                Objects.equals(project, detail.project) &&
                Objects.equals(account, detail.account) &&
                Objects.equals(department, detail.department) &&
                Objects.equals(category, detail.category) &&
                Objects.equals(earning, detail.earning) &&
                Objects.equals(expense, detail.expense) &&
                Objects.equals(balance, detail.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, description, project, account, department, category, earning, expense, balance);
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
