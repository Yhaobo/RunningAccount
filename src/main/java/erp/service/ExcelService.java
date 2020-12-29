package erp.service;

import erp.dao.DetailDao;
import erp.dao.OptionDao;
import erp.entity.Detail;
import erp.entity.Option;
import erp.entity.dto.req.DetailQueryConditionDTO;
import erp.util.ExcelUtils;
import erp.util.MyException;
import erp.util.MyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExcelService {
    @Autowired
    private DetailDao detailDao;
    @Autowired
    private OptionDao optionDao;

    @Value("${erp.home.location}")
    private String homeLocation;

    private final String templateDirectoryName = "template/";

    private final String title = "收支明细";
    private final String[] headers = {"日期", "项目", "账户", "部门", "类别", "收入", "支出", "结存", "摘要"};

    /**
     * 导出数据为 Excel
     */
    @Transactional(readOnly = true)
    public void export(HttpServletResponse response, String accountName) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        final DetailQueryConditionDTO condition = new DetailQueryConditionDTO();
        condition.setAccountId(optionDao.getAccountId(accountName));
        List<Detail> data = detailDao.listByCondition(null, condition);
        String fileName = LocalDate.now().toString() + "_" + accountName;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ExcelUtils.exportExcel2007(title, headers, data, byteArrayOutputStream);
        ExcelUtils.setResponseHeaderWithExcel(response, fileName, byteArrayOutputStream.size());
        byteArrayOutputStream.writeTo(response.getOutputStream());
    }

    /**
     * 从Excel中导入记录
     */
    @Transactional(rollbackFor = Exception.class)
    public void importing(InputStream in, Date minDate) throws ParseException, IOException {
        //因为此输入流只能读一次, 所以先复制一份
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        StreamUtils.copy(in, byteArrayOutputStream);
        //几种选项(目前就4种)
        final int optionCategoryNum = 4;
        Map<String, Map<String, Integer>> allOptionsMap = new HashMap<>(optionCategoryNum + 1);
        allOptionsMap.put("accountOptions", optionsToMap(optionDao.listAccount()));
        allOptionsMap.put("departmentOptions", optionsToMap(optionDao.listDepartment()));
        allOptionsMap.put("projectOptions", optionsToMap(optionDao.listProject()));
        allOptionsMap.put("categoryOptions", optionsToMap(optionDao.listCategory()));

        final List<Detail> list = ExcelUtils.importing(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), minDate, allOptionsMap);
        if (list.size() == 0) {
            throw new MyException("无有效内容, 请检查后重新上传");
        }
        try {
            detailDao.insertFromExcelByBatch(list);
            saveImportSuccessExcel(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        } catch (DuplicateKeyException e) {
            log.error("", e);
            throw new MyException("日期时间不允许有重复, 请检查后重新上传");
        }

    }

    /**
     * 保存导入数据成功的Excel
     *
     * @param in
     */
    private void saveImportSuccessExcel(InputStream in) {
        final String directoryName = "excel";
        final File file = new File(homeLocation + directoryName, MyUtils.getFileNameWithCurrentTime() + "_" + MyUtils.getUUID(8) + ".xlsx");
        file.getParentFile().mkdirs();
        try (final FileOutputStream out = new FileOutputStream(file)) {
            StreamUtils.copy(in, out);
        } catch (IOException e) {
            log.error("保存Excel文件失败", e);
            throw new MyException("保存Excel文件失败");
        }
    }

    /**
     * 将 Option 集合转换成 Map (以name为键, id为值)
     */
    private Map<String, Integer> optionsToMap(List<? extends Option> options) {
        final HashMap<String, Integer> map = new HashMap<>(options.size() + 1);
        for (Option option : options) {
            map.put(option.getName(), option.getId());
        }
        return map;
    }

    public void getTemplate(HttpServletResponse response) throws IOException {
        final String fileName = "Excel导入模板.xlsx";
        try (final InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(templateDirectoryName + fileName)) {
            if (resourceAsStream == null) {
                log.error("找不到模板: " + fileName);
                return;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            updateTemplate(resourceAsStream, byteArrayOutputStream);
            ExcelUtils.setResponseHeaderWithExcel(response, LocalDate.now().toString() + "_Excel导入模板", byteArrayOutputStream.size());
            byteArrayOutputStream.writeTo(response.getOutputStream());
        }
    }

    /**
     * 更新模板的选项相关数据
     */
    private void updateTemplate(InputStream inputStream, OutputStream outputStream) throws IOException {
        final List<String> accountNames = optionDao.listAccountName();
        final List<String> categoryNames = optionDao.listCategoryName();
        final List<String> departmentNames = optionDao.listDepartmentName();
        final List<String> projectNames = optionDao.listProjectName();

        //得到工作薄
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        //得到第一张表格
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            //只修改第二行数据
            if (row.getRowNum() == 1) {
                final String delimiter = " || ";

                final Cell dateCell = row.getCell(0);
                dateCell.setCellValue(new Date());

                final Cell projectCell = row.getCell(1);
                projectCell.setCellValue(String.join(delimiter, projectNames));

                final Cell accountCell = row.getCell(2);
                accountCell.setCellValue(String.join(delimiter, accountNames));

                final Cell departmentCell = row.getCell(3);
                departmentCell.setCellValue(String.join(delimiter, departmentNames));

                final Cell categoryCell = row.getCell(4);
                categoryCell.setCellValue(String.join(delimiter, categoryNames));

            } else if (row.getRowNum() > 1) {
                break;
            }
        }
        workbook.write(outputStream);
        workbook.close();
    }

    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public XSSFWorkbook generateExpenseClaimForm(Detail[] details) throws IOException {
        final String fileName = "费用报销单模板.xlsx";
        final XSSFWorkbook workbook;
        try (final InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(templateDirectoryName + fileName)) {
            if (resourceAsStream == null) {
                throw new MyException("找不到模板: " + fileName);
            }
            workbook = new XSSFWorkbook(resourceAsStream);
            final XSSFSheet sheet = workbook.getSheetAt(0);
            //数据填入开始行索引
            int rowIndex = 4;
            //定义循环可重用变量
            XSSFRow row;
            XSSFCell cell;
            //支出合计
            BigDecimal expenseSum = new BigDecimal(0);
            for (int i = 0; i < details.length; i++) {
                final Detail detail = details[i];
                //记录修改状态
                detailDao.updateReimbursement(true, detail.getId());

                expenseSum = expenseSum.add(detail.getExpense());
                row = sheet.getRow(rowIndex + i);

                //分类名
                cell = row.getCell(0);
                cell.setCellValue(detail.getCategory().getName());
                //部门名
                cell = row.getCell(1);
                cell.setCellValue(detail.getDepartment().getName());
                //摘要
                cell = row.getCell(2);
                cell.setCellValue(detail.getDigest());
                //凭证张数(默认为1)
                cell = row.getCell(3);
                cell.setCellValue(1);
                //金额
                cell = row.getCell(5);
                cell.setCellValue(Double.parseDouble(detail.getExpense().toString()));
            }
            //合计金额
            row = sheet.getRow(12);
            row.getCell(0).setCellValue("（大写）" + ExcelUtils.number2CNMonetaryUnit(expenseSum) + "（小写）¥" + expenseSum.toString());
        }
        return workbook;
    }
}
