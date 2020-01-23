package erp.service;

import erp.dao.DetailDao;
import erp.domain.Detail;
import erp.util.ExportExcelUtil;
import erp.util.ExportExcelWrapper;
import erp.util.ImportExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    private DetailDao dao;

    private String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private String title = "收支明细";
    private String[] headers = {"日期", "摘要", "项目", "账户", "部门", "分类", "收入", "支出", "结存"};

    /**
     * 导出
     * @param response
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public void export(HttpServletResponse response) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        List<Detail> data = dao.findAll();
        ExportExcelWrapper.exportExcelToRemote(fileName, title, headers, data, response);
    }

    /**
     * 备份
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void backups() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        File dest = new File("d:\\财务数据备份", fileName + ".xlsx");
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        FileOutputStream out = null;
        out = new FileOutputStream(dest);
        List<Detail> data = dao.findAll();
        ExportExcelUtil.exportExcel2007(title, headers, data, out, "yyyy-MM-dd HH:mm");
    }

    /**
     * 导入
     * @param in
     * @throws NoSuchFieldException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ParseException
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public void importing(InputStream in) throws NoSuchFieldException, InstantiationException, IllegalAccessException, ParseException, IOException {
        List<Detail> details = ImportExcelUtil.Importing(in);
        for (Detail i : details) {
            dao.addByExcel(i);
        }
    }

}
