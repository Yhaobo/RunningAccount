package erp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import erp.entity.Detail;
import erp.entity.dto.req.DetailFormReqDTO;
import erp.entity.dto.req.DetailQueryConditionDTO;
import erp.entity.dto.resp.DetailRespDTO;
import erp.service.DetailService;
import erp.util.MyPage;
import erp.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
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

    @GetMapping("")
    public R list(DetailQueryConditionDTO dto) {
        // 分页
        Page<List<DetailRespDTO>> page = new MyPage<>(dto.getCurrentPage(), dto.getPageSize());

        detailService.listByCondition(dto, page);
        return R.ok().data(page);
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
        if (form.getDigest() == null) {
            form.setDigest("无");
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

    @PostMapping("/picture/{detailId}")
    public synchronized R addVouchers(MultipartFile file, @PathVariable Integer detailId) throws Exception {
        detailService.insertVoucher(file, detailId);
        return R.ok();
    }

    @DeleteMapping("/picture/{pictureId}")
    public synchronized R deleteVoucher(@PathVariable Integer pictureId) {
        detailService.deleteVoucher(pictureId);
        return R.ok();
    }

    @GetMapping("/picture/{detailId}")
    public R listVoucherByDetailId(@PathVariable Integer detailId) {
        return R.ok().data(detailService.listVoucherByDetailId(detailId));
    }

    @GetMapping("/picture/img/{fileName}")
    public void getImg(@PathVariable String fileName, HttpServletResponse response) throws Exception {
        detailService.getImg(fileName, response);
    }

}
