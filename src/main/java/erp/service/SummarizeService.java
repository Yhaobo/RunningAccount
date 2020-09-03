package erp.service;

import erp.dao.SummarizeDao;
import erp.util.MyUtils;
import erp.vo.req.SummarizeFilterVo;
import erp.vo.resp.DetailRespVo;
import erp.vo.resp.SummarizeRespVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @author Yhaobo
 * @date 2020/3/28
 */
@Service
public class SummarizeService {
    @Autowired
    private SummarizeDao summarizeDao;

    @Transactional(readOnly = true)
    public List<DetailRespVo> listByFilter(SummarizeFilterVo vo) throws ParseException {
        //处理日期格式
        if (!StringUtils.isEmpty(vo.getDuringDate())) {
            String[] dates = vo.getDuringDate().split(" ~ ");
            if (dates.length == 2) {
                Calendar calendar = Calendar.getInstance();
                if ("month".equals(vo.getGroupPolicy())) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                    //处理前日期
                    vo.setFrontDate(simpleDateFormat.parse(dates[0].trim()));
                    //处理后日期
                    calendar.setTime(simpleDateFormat.parse(dates[1].trim()));
                    calendar.add(Calendar.MONTH, 1);
                    calendar.add(Calendar.MILLISECOND, -1);
                    vo.setBackDate(calendar.getTime());
                } else if ("year".equals(vo.getGroupPolicy())) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
                    //处理前日期
                    vo.setFrontDate(simpleDateFormat.parse(dates[0].trim()));
                    //处理后日期
                    calendar.setTime(simpleDateFormat.parse(dates[1].trim()));
                    calendar.add(Calendar.YEAR, 1);
                    calendar.add(Calendar.MILLISECOND, -1);
                    vo.setBackDate(calendar.getTime());
                }
            }
        }

        // 将DO的数据传递给VO
        List detailList = summarizeDao.listDetailByFilter(vo);
        for (int i = 0; i < detailList.size(); i++) {
            SummarizeRespVo detailRespVo = new SummarizeRespVo();
            BeanUtils.copyProperties(detailList.get(i), detailRespVo);
            detailList.set(i, detailRespVo);
        }

        //格式化为货币格式
        MyUtils.formatNumber(detailList);
        return detailList;
    }
}
