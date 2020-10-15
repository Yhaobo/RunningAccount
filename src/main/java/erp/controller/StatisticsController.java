package erp.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
import erp.entity.dto.req.StatisticsQueryConditionDTO;
import erp.entity.dto.resp.MoneyStatisticsRespDTO;
import erp.service.StatisticsService;
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
        PageHelper.startPage(queryConditionDTO.getCurrentPage(), queryConditionDTO.getPageSize());

        List<MoneyStatisticsRespDTO> detailList = service.listMoneyChange(queryConditionDTO);
        PageSerializable<MoneyStatisticsRespDTO> pageInfo = new PageSerializable<>(detailList);
        return R.ok().data(pageInfo);
    }
}
