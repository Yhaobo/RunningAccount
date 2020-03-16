package erp.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
import erp.domain.Detail;
import erp.service.DetailService;
import erp.util.MyException;
import erp.util.ResultInfo;
import erp.vo.req.DetailFilterVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 收支明细
 */
@Controller
@RequestMapping("/detail")
public class DetailController {
    @Autowired
    private DetailService detailService;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/getAll")
    @ResponseBody
    public ResultInfo getAll(DetailFilterVo vo, String date, Integer pageNum, Integer pageSize) {
        try {
            // 分页
            PageHelper.startPage(pageNum, pageSize);

            //处理日期格式
            if (!StringUtils.isEmpty(date)) {
                String[] dates = date.split(" - ");
                if (dates.length == 2) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                    vo.setFrontDate(simpleDateFormat.parse(dates[0].trim()));
                    vo.setBackDate(simpleDateFormat.parse(dates[1].trim()));
                }
            }
            List<Detail> detailList = detailService.findAll(vo);
            PageSerializable<Detail> pageInfo = new PageSerializable<>(detailList);
            return new ResultInfo(true, pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[method:getAll]" + e.getMessage());
            return new ResultInfo(false, "解析日期失败");
        }
    }

    @RequestMapping("/add")
    @ResponseBody
    public ResultInfo add(Detail form) {
        try {
            detailService.add(form);
            return new ResultInfo(true);
        } catch (Exception e) {
            log.error("[method:add]" + e.getMessage());
            return new ResultInfo(false, "添加失败!");
        }
    }

    @RequestMapping("/findOne")
    @ResponseBody
    public ResultInfo findOne(int id) {
        Detail detail = detailService.findOne(id);
        return new ResultInfo(true, detail);
    }

    @RequestMapping("/update")
    @ResponseBody
    public ResultInfo update(Detail form) {
        try {
            detailService.update(form);
        } catch (MyException e) {
            log.error("[method:update]" + e.getMessage());
            return new ResultInfo(false, e.getMessage());
        } catch (Exception e) {
            log.error("[method:update]" + e.getMessage());
            return new ResultInfo(false, "修改失败!");
        }
        return new ResultInfo(true);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo delete(Detail form) {
        try {
            detailService.delete(form);
            return new ResultInfo(true);
        } catch (Throwable t) {
            log.error("[method:delete]" + t.getMessage());
            return new ResultInfo(false, "删除失败!");
        }
    }

    @RequestMapping("/updateBalance")
    @ResponseBody
    public ResultInfo updateBalance() {
        try {
            detailService.updateBalance();
            return new ResultInfo(true);
        } catch (Throwable t) {
            log.error("[method:updateBalance]" + t.getMessage());
            return new ResultInfo(false, "更新结存失败!");
        }
    }
}
