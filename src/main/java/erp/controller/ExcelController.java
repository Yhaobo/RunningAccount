package erp.controller;

import erp.entity.Detail;
import erp.service.DetailService;
import erp.service.ExcelService;
import erp.util.ExcelUtils;
import erp.util.MyException;
import erp.util.MyUtils;
import erp.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yhaobo
 * @since 2020/10/20
 */
@RestController
@Slf4j
@RequestMapping("/excel")
public class ExcelController {
    @Autowired
    private ExcelService excelService;
    @Autowired
    private DetailService detailService;

    @GetMapping("")
    public void export(HttpServletResponse response, String accountName) throws Exception {
        excelService.export(response, accountName);
    }

    @PostMapping("")
    public synchronized R importing(MultipartFile file) throws IOException, InstantiationException, IllegalAccessException, ParseException, NoSuchFieldException {
        if (file.isEmpty()) {
            return R.fail().message("上传文件为空");
        }
        //导入记录中最小的时间
        final Date minDate = new Date();
        excelService.importing(file.getInputStream(), minDate);
        detailService.updateBalance(minDate);
        return R.ok();
    }

    @GetMapping("/template")
    public void downloadExcelTemplate(HttpServletResponse response) throws IOException {
        excelService.getTemplate(response);
    }

    @GetMapping("/expenseClaimForm")
    public void downloadExpenseClaimForm(HttpServletResponse response, @RequestParam String formId) throws IOException {
        final Session session = SecurityUtils.getSubject().getSession();
        final Map<String, ByteArrayOutputStream> map = (Map<String, ByteArrayOutputStream>) session.getAttribute(EXPENSE_CLAIM_FORM_SESSION_KEY);
        ByteArrayOutputStream outputStream = map.remove(formId);
        ExcelUtils.setResponseHeaderWithExcel(response, LocalDate.now().toString() + "_费用报销单", outputStream.size());
        outputStream.writeTo(response.getOutputStream());
    }

    private static final String EXPENSE_CLAIM_FORM_SESSION_KEY = "expenseClaimFormSessionKey";

    @PostMapping("/expenseClaimForm")
    public R generateExpenseClaimForm(@RequestBody Detail[] details) throws IOException {
        if (details.length > 7) {
            throw new MyException("一张费用报销单最多添加7条记录, 请检查后重试");
        }
        final Session session = SecurityUtils.getSubject().getSession();
        XSSFWorkbook workbook = excelService.generateExpenseClaimForm(details);
        if (workbook == null) {
            throw new MyException("生成费用报销单失败");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();

        Object formIdToStreamMap = session.getAttribute(EXPENSE_CLAIM_FORM_SESSION_KEY);
        String formId = MyUtils.getUUID();
        if (formIdToStreamMap == null) {
            Map<String, ByteArrayOutputStream> map = new ConcurrentHashMap<>();
            map.put(formId, byteArrayOutputStream);
            session.setAttribute(EXPENSE_CLAIM_FORM_SESSION_KEY, map);
        } else {
            ((Map<String, ByteArrayOutputStream>) formIdToStreamMap).put(formId, byteArrayOutputStream);
        }

        return R.ok().data(formId);
    }
}
