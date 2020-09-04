package erp.service;

import erp.dao.DetailDao;
import erp.entity.Detail;
import erp.util.ExportExcelUtil;
import erp.util.ImportExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
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
     *
     * @param response
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @Transactional(readOnly = true)
    public void export(HttpServletResponse response) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        List<Detail> data = dao.listAll();
        ExportExcelUtil.exportExcelToRemote(fileName, title, headers, data, response);
    }

    /**
     * 导入
     *
     * @param in
     * @throws NoSuchFieldException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ParseException
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public void importing(InputStream in) throws NoSuchFieldException, InstantiationException, IllegalAccessException, ParseException, IOException {
        dao.insertFromExcelByBatch(ImportExcelUtil.Importing(in));
    }
}
