package erp.service;

import erp.dao.GroupQueryDao;
import erp.domain.Detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class GroupQueryService {
    @Autowired
    GroupQueryDao dao;

    public List<Detail> findDepartment(int dep_id) {
        List<Detail> details = dao.findByMonthlyDepartmentGroup(dep_id);
        formatNumber(details);
        return details;
    }

    public List<Detail> findAccount(int a_id) {
        List<Detail> details = dao.findByMonthlyAccountGroup(a_id);
        formatNumber(details);
        return details;
    }

    public List<Detail> findProject(int p_id) {
        List<Detail> details = dao.findByMonthlyProjectGroup(p_id);
        formatNumber(details);
        return details;
    }

    public List<Detail> findCategory(int c_id) {
        List<Detail> details = dao.findByMonthlyCategoryGroup(c_id);
        formatNumber(details);
        return details;
    }

    public List<Detail> findDetail() {
        List<Detail> details = dao.findByMonthlyGroup();
        formatNumber(details);
        return details;
    }

    /**
     * 格式化所有数字
     *
     * @param details
     */
    private void formatNumber(List<Detail> details) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);
        for (Detail i : details) {
            i.setFormatEarning(format.format(i.getEarning()));
            i.setFormatExpense(format.format(i.getExpense()));
            i.setFormatBalance(format.format(i.getBalance()));
        }
    }
}
