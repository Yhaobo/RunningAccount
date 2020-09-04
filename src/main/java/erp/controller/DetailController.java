package erp.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
import erp.entity.Detail;
import erp.service.DetailService;
import erp.service.ExcelService;
import erp.util.MyException;
import erp.util.ResultInfo;
import erp.vo.req.DetailConditionQueryVO;
import erp.vo.req.DetailFormReqVO;
import erp.vo.resp.DetailRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * 收支明细
 */
@RestController
@Slf4j
@RequestMapping("/detail")
public class DetailController {
    @Autowired
    private DetailService detailService;

    @Autowired
    private ExcelService excelService;

    @RequestMapping("/getAll")
    public ResultInfo getAll(DetailConditionQueryVO vo, String duringDate) {
        try {
            // 分页
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());

            //处理日期格式
            if (!StringUtils.isEmpty(duringDate)) {
                String[] dates = duringDate.split(" ~ ");
                if (dates.length == 2) {
                    //处理前日期
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                    vo.setFrontDate(simpleDateFormat.parse(dates[0].trim()));
                    //处理后日期
                    Calendar calendar = simpleDateFormat.getCalendar();
                    calendar.setTime(simpleDateFormat.parse(dates[1].trim()));
                    calendar.add(Calendar.MONTH, 1);
                    calendar.add(Calendar.MILLISECOND, -1);
                    vo.setBackDate(calendar.getTime());
                }
            }
            List<DetailRespVO> detailRespVOS = detailService.findAll(vo);
            PageSerializable<DetailRespVO> pageInfo = new PageSerializable<>(detailRespVOS);
            return new ResultInfo(true, pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[method:getAll]" + e);
            return new ResultInfo(false, e.getMessage());
        }
    }

    @RequestMapping("/findOne")
    public ResultInfo findOne(int id) {
        Detail detail = detailService.findOneById(id);
        return new ResultInfo(true, detail);
    }

    @RequestMapping("/add")
    public synchronized ResultInfo add(DetailFormReqVO form) {
        try {
            detailService.insert(form);
            return new ResultInfo(true, form.getId());
        } catch (Exception e) {
            log.error("[method:add]" + e);
            e.printStackTrace();
            return new ResultInfo(false, "添加失败！请确认所有的栏目都已填写");
        }
    }

    @RequestMapping("/update")
    public synchronized ResultInfo update(DetailFormReqVO form) {
        try {
            detailService.update(form);
        } catch (MyException e) {
            log.error("[method:update]" + e);
            return new ResultInfo(false, e.getMessage());
        } catch (Exception e) {
            log.error("[method:update]" + e);
            return new ResultInfo(false, "修改失败!");
        }
        return new ResultInfo(true);
    }

    @RequestMapping("/delete")
    public synchronized ResultInfo delete(Detail form) {
        try {
            detailService.delete(form);
            return new ResultInfo(true);
        } catch (Throwable t) {
            log.error("[method:delete]" + t);
            return new ResultInfo(false, "删除失败!");
        }
    }

    @RequestMapping("/updateBalance")
    public synchronized ResultInfo updateBalance() {
        try {
            detailService.updateAllBalance();
            return new ResultInfo(true);
        } catch (Throwable t) {
            log.error("[method:updateBalance]" + t);
            return new ResultInfo(false, "更新结存失败!");
        }
    }

    @PostMapping("/addVouchers")
    public synchronized ResultInfo addVouchers(MultipartFile file, Integer id) {
        try {
            detailService.insertVoucher(file, id);
            return new ResultInfo(true);
        } catch (Exception e) {
            log.error("[method:addVouchers] " + e);
            e.printStackTrace();
            return new ResultInfo(false);
        }
    }

    @PostMapping("/deleteVoucher")
    public synchronized ResultInfo deleteVoucher(Integer voucherId) {
        try {
            detailService.deleteVoucher(voucherId);
            return new ResultInfo(true);
        } catch (Exception e) {
            log.error("[method:deleteVoucher] " + e);
            e.printStackTrace();
            return new ResultInfo(false, "删除凭证失败");
        }
    }

    @PostMapping("/vouchers")
    public ResultInfo listVoucherByDetailId(Integer id) {
        return new ResultInfo(true, detailService.listVoucherByDetailId(id));
    }

    @GetMapping("/voucher/{fileName}")
    public void listVoucherByUrl(@PathVariable String fileName, HttpServletResponse response) {
        try {
            detailService.listVoucherByUrl(fileName, response);
        } catch (Exception e) {
            log.error("[method:listVoucherByUrl] " + e);
            e.printStackTrace();
        }
    }

    @RequestMapping("/excel/export")
    public void export(HttpServletResponse response) throws IOException {
        try {
            excelService.export(response);
        } catch (Exception e) {
            log.error("[method:export]" + e.getMessage());
        }
    }

    @RequestMapping("/excel/importing")
    public synchronized ResultInfo importing(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResultInfo(false);
        }
        try {
            excelService.importing(file.getInputStream());
            return new ResultInfo(true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[method:importing]" + e.getMessage());
            return new ResultInfo(false, "导入失败!");
        } finally {
            file.getInputStream().close();
        }
    }
}
