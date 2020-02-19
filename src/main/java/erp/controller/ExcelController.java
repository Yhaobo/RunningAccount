package erp.controller;

import erp.service.ExcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
        } finally {
            response.getOutputStream().close();
        }
    }

    @RequestMapping("/importing")
    public void importing(MultipartFile file, HttpServletResponse response, HttpServletRequest request) throws IOException {
        if (file.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/detail_list.html");
            return;
        }
        try {
            service.importing(file.getInputStream());
        } catch (Exception e) {
            log.error("[method:importing]" + e.getMessage());
        } finally {
            file.getInputStream().close();
            response.sendRedirect(request.getContextPath() + "/detail_list.html");
        }
    }
}
