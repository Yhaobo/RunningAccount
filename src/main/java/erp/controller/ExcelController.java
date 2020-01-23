package erp.controller;

import erp.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

@Controller
@RequestMapping("/excel")
public class ExcelController {
    @Autowired
    private ExcelService service;

    @RequestMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        try {
            service.export(response);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            response.getOutputStream().close();
        }
    }

    @RequestMapping("/importing")
    public void importing(MultipartFile file, HttpServletResponse response, HttpServletRequest request) throws IOException {
        if (!file.isEmpty()) {
            try {
                service.importing(file.getInputStream());
                response.sendRedirect(request.getContextPath() + "/detail_List.html");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                file.getInputStream().close();
            }
        }
    }
}
