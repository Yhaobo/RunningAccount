package erp.util;

import erp.entity.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ImportExcelUtil {
    public static List<Detail> Importing(InputStream in) throws IOException, IllegalAccessException, InstantiationException, NoSuchFieldException, ParseException {
        List<Detail> list = new ArrayList<>();
        //得到工作薄
        XSSFWorkbook workbook = new XSSFWorkbook(in);
        //得到表格
        XSSFSheet sheet = workbook.getSheetAt(0);
        //对表格中的每一行进行迭代
        for (Row row : sheet) {
            //忽略第一行表头
            if (row.getRowNum() < 1) {
                continue;
            }
            //如果获取不到值,则取消循环
            if (row.getCell(0).getStringCellValue() == null) {
                break;
            }
            //创建实体类并赋值
            Detail entity = new Detail();
            entity.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(row.getCell(0).getStringCellValue()));
            entity.setDescription(row.getCell(1).getStringCellValue());
            entity.setProject(new Project(row.getCell(2).getStringCellValue()));
            entity.setAccount(new Account(row.getCell(3).getStringCellValue()));
            entity.setDepartment(new Department(row.getCell(4).getStringCellValue()));
            entity.setCategory(new Category(row.getCell(5).getStringCellValue()));
            entity.setEarning(new BigDecimal(row.getCell(6).getStringCellValue()));
            entity.setExpense(new BigDecimal(row.getCell(7).getStringCellValue()));
            //添加到list
            list.add(entity);
        }
        return list;
    }
}
