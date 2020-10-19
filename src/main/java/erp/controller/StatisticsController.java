package erp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import erp.entity.dto.req.StatisticsQueryConditionDTO;
import erp.entity.dto.resp.MoneyStatisticsRespDTO;
import erp.entity.dto.resp.ProportionStatisticsRespDTO;
import erp.service.StatisticsService;
import erp.util.MyPage;
import erp.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Yhaobo
 * @date 2020/3/28
 */
@Slf4j
@RestController
@RequestMapping("statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService service;

    @GetMapping("money")
    public R money(StatisticsQueryConditionDTO queryConditionDTO) {
        if (queryConditionDTO.getAccountId() == null) {
            return R.fail().message("请先选择银行账户");
        }

        // 分页
        final Page<List<MoneyStatisticsRespDTO>> page = new MyPage<>(queryConditionDTO.getCurrentPage(), queryConditionDTO.getPageSize());

        service.listMoneyChange(queryConditionDTO, page);
        return R.ok().data(page);
    }

    @GetMapping("proportion/department")
    public R proportionDepartment(StatisticsQueryConditionDTO queryConditionDTO) {
        List<ProportionStatisticsRespDTO> data=service.getDepartmentProportionData(queryConditionDTO);
        return R.ok().data(data);
    }

    @GetMapping("proportion/category")
    public R proportionCategory(StatisticsQueryConditionDTO queryConditionDTO) {
        List<ProportionStatisticsRespDTO> data=service.getCategoryProportionData(queryConditionDTO);
        return R.ok().data(data);
    }
}
