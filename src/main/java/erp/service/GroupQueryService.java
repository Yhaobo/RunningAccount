package erp.service;

import erp.dao.GroupQueryDao;
import erp.domain.Detail;
import erp.util.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GroupQueryService {
    @Autowired
    GroupQueryDao dao;

    public List<Detail> findDepartment(int dep_id) {
        List<Detail> details = dao.findByMonthlyDepartmentGroup(dep_id);
        MyUtils.formatNumber(details);
        return details;
    }

    public List<Detail> findAccount(int a_id) {
        List<Detail> details = dao.findByMonthlyAccountGroup(a_id);
        MyUtils.formatNumber(details);
        return details;
    }

    public List<Detail> findProject(int p_id) {
        List<Detail> details = dao.findByMonthlyProjectGroup(p_id);
        MyUtils.formatNumber(details);
        return details;
    }

    public List<Detail> findCategory(int c_id) {
        List<Detail> details = dao.findByMonthlyCategoryGroup(c_id);
        MyUtils.formatNumber(details);
        return details;
    }

    public List<Detail> findDetail() {
        List<Detail> details = dao.findByMonthlyGroup();
        MyUtils.formatNumber(details);
        return details;
    }


}
