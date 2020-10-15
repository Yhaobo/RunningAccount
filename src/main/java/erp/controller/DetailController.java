package erp.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
import erp.entity.Detail;
import erp.entity.dto.req.DetailFormReqDTO;
import erp.entity.dto.req.DetailQueryConditionDTO;
import erp.entity.dto.resp.DetailRespDTO;
import erp.service.DetailService;
import erp.service.ExcelService;
import erp.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
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

    @GetMapping("")
    public R getAll(DetailQueryConditionDTO vo) {
        // 分页
        PageHelper.startPage(vo.getCurrentPage(), vo.getPageSize());

        List<DetailRespDTO> detailRespDTOS = detailService.findAll(vo);
        PageSerializable<DetailRespDTO> pageInfo = new PageSerializable<>(detailRespDTOS);
        return R.ok().data(pageInfo);
    }

    @GetMapping("/{id}")
    public R findOne(@PathVariable Integer id) {
        return R.ok().data(detailService.findOneById(id));
    }

    @PostMapping("")
    public synchronized R add(@RequestBody DetailFormReqDTO form) {
        try {
            detailService.insert(form);
        } catch (DuplicateKeyException e) {
            log.error("", e);
            return R.fail().message("日期时间不允许重复, 请重新选择日期时间");
        }
        return R.ok().data(form.getId());
    }

    @PutMapping("")
    public synchronized R update(@RequestBody DetailFormReqDTO form) throws Exception {
        //处理null值
        if (form.getEarning() == null) {
            form.setEarning(new BigDecimal(0));
        }
        if (form.getExpense() == null) {
            form.setExpense(new BigDecimal(0));
        }
        if (form.getDescription() == null) {
            form.setDescription("无");
        }
        detailService.update(form);
        return R.ok();
    }

    @DeleteMapping("")
    public synchronized R delete(@RequestBody Detail form) {
        detailService.delete(form);
        return R.ok();
    }

    @PutMapping("/balance")
    public synchronized R updateBalance() {
        detailService.updateAllBalance();
        return R.ok();
    }

    @PostMapping("/voucher/{detailId}")
    public synchronized R addVouchers(MultipartFile file, @PathVariable Integer detailId) throws Exception {
        detailService.insertVoucher(file, detailId);
        return R.ok();
    }

    @DeleteMapping("/voucher/{voucherId}")
    public synchronized R deleteVoucher(@PathVariable Integer voucherId) {
        detailService.deleteVoucher(voucherId);
        return R.ok();
    }

    @GetMapping("/voucher/{detailId}")
    public R listVoucherByDetailId(@PathVariable Integer detailId) {
        return R.ok().data(detailService.listVoucherByDetailId(detailId));
    }

    @GetMapping("/voucher/img/{fileName}")
    public void getImg(@PathVariable String fileName, HttpServletResponse response) throws Exception {
        detailService.getImg(fileName, response);
    }

    @GetMapping("/excel/{accountId}")
    public void export(HttpServletResponse response, @PathVariable Integer accountId) throws Exception {
        excelService.export(response, accountId);
    }

    @PostMapping("/excel")
    public synchronized R importing(MultipartFile file) throws IOException, InstantiationException, IllegalAccessException, ParseException, NoSuchFieldException {
        if (file.isEmpty()) {
            return R.fail().message("上传文件为空");
        }
        try {
            excelService.importing(file.getInputStream());
            return R.ok();
        } finally {
            file.getInputStream().close();
        }
    }
}
