package erp.service;

import erp.dao.StatisticsDao;
import erp.entity.dto.req.StatisticsQueryConditionDTO;
import erp.entity.dto.resp.MoneyStatisticsRespDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Yhaobo
 * @date 2020/3/28
 */
@Service
public class StatisticsService {
    @Autowired
    private StatisticsDao statisticsDao;

    @Transactional(readOnly = true)
    public List<MoneyStatisticsRespDTO> listMoneyChange(StatisticsQueryConditionDTO vo) {
        List detailList = statisticsDao.listDetailByFilter(vo);
        // 将DO的数据传递给DTO
        for (int i = 0; i < detailList.size(); i++) {
            MoneyStatisticsRespDTO detailRespVo = new MoneyStatisticsRespDTO();
            BeanUtils.copyProperties(detailList.get(i), detailRespVo);
            detailList.set(i, detailRespVo);
        }
        return detailList;
    }
}
