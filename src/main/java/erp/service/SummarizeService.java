package erp.service;

import erp.dao.SummarizeDao;
import erp.domain.Detail;
import erp.util.MyUtils;
import erp.vo.req.SummarizeFilterVo;
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
@Transactional(readOnly = true)
public class SummarizeService {
    @Autowired
    private SummarizeDao dao;

    public List<Detail> listByFilter(SummarizeFilterVo vo) throws ParseException {
        System.out.println(vo);
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
        List<Detail> detailList = dao.listByFilter(vo);
        //格式化为货币格式
        MyUtils.formatNumber(detailList);
        return detailList;
    }
}
