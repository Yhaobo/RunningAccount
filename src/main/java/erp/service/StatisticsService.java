package erp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import erp.dao.OptionDao;
import erp.dao.StatisticsDao;
import erp.entity.Category;
import erp.entity.Department;
import erp.entity.dto.req.StatisticsQueryConditionDTO;
import erp.entity.dto.resp.MoneyStatisticsRespDTO;
import erp.entity.dto.resp.ProportionStatisticsRespDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yhaobo
 * @date 2020/3/28
 */
@Service
public class StatisticsService {
    @Autowired
    private StatisticsDao statisticsDao;
    @Autowired
    private OptionDao optionDao;

    @Transactional(readOnly = true)
    public void listMoneyChange(StatisticsQueryConditionDTO vo, Page<List<MoneyStatisticsRespDTO>> page) {
        List detailList = statisticsDao.listDetailByCondition(page, vo);
        // 将DO的数据传递给DTO
        for (int i = 0; i < detailList.size(); i++) {
            MoneyStatisticsRespDTO detailRespVo = new MoneyStatisticsRespDTO();
            BeanUtils.copyProperties(detailList.get(i), detailRespVo);
            detailList.set(i, detailRespVo);
        }
        page.setRecords(detailList);
    }

    public List<ProportionStatisticsRespDTO> getDepartmentProportionData(StatisticsQueryConditionDTO queryConditionDTO) {
        final List<Department> departments = optionDao.listDepartment();
        final List<ProportionStatisticsRespDTO> proportionStatisticsRespDTOS = new ArrayList<>(departments.size());
        for (Department department : departments) {
            String sum = statisticsDao.sumExpenseFromDepartment(department.getId(), queryConditionDTO.getBeginDate(), queryConditionDTO.getEndDate());
            proportionStatisticsRespDTOS.add(new ProportionStatisticsRespDTO(department.getName(),sum));
        }
        return proportionStatisticsRespDTOS;
    }

    public List<ProportionStatisticsRespDTO> getCategoryProportionData(StatisticsQueryConditionDTO queryConditionDTO) {
        final List<Category> departments = optionDao.listCategory();
        final List<ProportionStatisticsRespDTO> proportionStatisticsRespDTOS = new ArrayList<>(departments.size());
        for (Category category : departments) {
            String sum = statisticsDao.sumExpenseFromCategory(category.getId(), queryConditionDTO.getBeginDate(), queryConditionDTO.getEndDate());
            proportionStatisticsRespDTOS.add(new ProportionStatisticsRespDTO(category.getName(),sum));
        }
        return proportionStatisticsRespDTOS;
    }
}
