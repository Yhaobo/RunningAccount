package erp.vo.req;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yhaobo
 * @date 2020/3/13
 */
public class DetailFilterVo implements Serializable {
    private Date frontDate;
    private Date backDate;
    private String description;
    private Integer projectId;
    private Integer accountId;
    private Integer departmentId;
    private Integer categoryId;

    public Date getFrontDate() {
        return frontDate;
    }

    public void setFrontDate(Date frontDate) {
        this.frontDate = frontDate;
    }

    public Date getBackDate() {
        return backDate;
    }

    public void setBackDate(Date backDate) {
        this.backDate = backDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = "%"+description+"%";
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "DetailFilterVo{" +
                "frontDate=" + frontDate +
                ", backDate=" + backDate +
                ", description='" + description + '\'' +
                ", projectId=" + projectId +
                ", accountId=" + accountId +
                ", departmentId=" + departmentId +
                ", categoryId=" + categoryId +
                '}';
    }
}
