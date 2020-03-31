package erp.controller;

import erp.service.ExcelService;
import erp.util.ResultInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/excel")
public class ExcelController {
    @Autowired
    private ExcelService service;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        try {
            service.export(response);
        } catch (Exception e) {
            log.error("[method:export]" + e.getMessage());
        }
    }

    @RequestMapping("/importing")
    @ResponseBody
    public ResultInfo importing(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResultInfo(false);
        }
        try {
            service.importing(file.getInputStream());
            return new ResultInfo(true);
        } catch (Exception e) {
            log.error("[method:importing]" + e.getMessage());
            return new ResultInfo(false,"导入失败!");
        } finally {
            file.getInputStream().close();
        }
    }
}
