package erp.util;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExportExcelUtil {
    /**
     * <p>
     * 导出带有头部标题行的Excel <br>
     * 时间格式默认：yyyy-MM-dd hh:mm:ss <br>
     * </p>
     *
     * @param title    表格标题
     * @param headers  头部标题集合
     * @param dataset  数据集合
     * @param response HttpServletResponse
     */
    public static <T> void exportExcelToRemote(String fileName, String title, String[] headers, Collection<T> dataset, HttpServletResponse response) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        exportExcel2007(title, headers, dataset, response.getOutputStream(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * <p>
     * 通用Excel导出方法,利用反射机制遍历对象的所有字段，将数据写入Excel文件中 <br>
     * 此版本生成2007以上版本的文件 (文件后缀：xlsx)
     * </p>
     *
     * @param title   表格标题名
     * @param headers 表格头部标题集合
     * @param dataset 需要显示的数据集合,集合中一定要放置符合JavaBean风格的类的对象。此方法支持的
     *                JavaBean属性的数据类型有基本数据类型及String,Date
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyyy-MM-dd hh:mm:ss"
     */
    public static <T> void exportExcel2007(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattern) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(20);
        // 生成一个样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
//        style.setFillForegroundColor(new XSSFColor(Color.gray));
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
//        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
//        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
//        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
//        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        font.setColor(Font.COLOR_NORMAL);
        font.setFontHeightInPoints((short) 11);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        XSSFCellStyle style2 = workbook.createCellStyle();
//        style2.setFillForegroundColor(new XSSFColor(java.awt.Color.WHITE));
//        style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//        style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
//        style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
//        style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
//        style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
//        style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//        style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        XSSFFont font2 = workbook.createFont();
        font.setBold(true);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        // 产生表格标题行
        XSSFRow row = sheet.createRow(0);
        XSSFCell cellHeader;
        for (int i = 0; i < headers.length; i++) {
            cellHeader = row.createCell(i);
            cellHeader.setCellStyle(style);
            cellHeader.setCellValue(new XSSFRichTextString(headers[i]));
        }

        // 遍历集合数据，产生数据行
        Iterator<T> iterator = dataset.iterator();
        int index = 1;
        T item;
        Field[] fields;
        Field field;
        XSSFRichTextString richString;
        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
        Matcher matcher;
        String fieldName;
        String getMethodName;
        XSSFCell cell;
        Class tCls;
        Method getMethod;
        Object value;
        String textValue;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        while (iterator.hasNext()) {
            row = sheet.createRow(index);
            item = (T) iterator.next();
            // 利用反射，根据JavaBean属性的先后顺序，动态调用getXxx()方法得到属性值
            fields = item.getClass().getDeclaredFields();
            for (int i = 0; i < headers.length; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(style2);
                field = fields[i + 1];
                fieldName = field.getName();
                getMethodName = "get" + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);

                tCls = item.getClass();
                getMethod = tCls.getMethod(getMethodName, new Class[]{});
                value = getMethod.invoke(item, new Object[]{});
                // 判断值的类型后进行强制类型转换
                textValue = null;
                if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                } else if (value instanceof Float) {
                    textValue = String.valueOf((Float) value);
                    cell.setCellValue(textValue);
                } else if (value instanceof Double) {
                    textValue = String.valueOf((Double) value);
                    cell.setCellValue(textValue);
                } else if (value instanceof Long) {
                    cell.setCellValue((Long) value);
                }
                if (value instanceof Boolean) {
                    textValue = "是";
                    if (!(Boolean) value) {
                        textValue = "否";
                    }
                } else if (value instanceof Date) {
                    textValue = sdf.format((Date) value);
                } else {
                    // 其它数据类型都当作字符串简单处理
                    if (value != null) {
                        textValue = value.toString();
                    }
                }
                if (textValue != null) {
                    matcher = p.matcher(textValue);
                    if (matcher.matches()) {
                        // 是数字当作double处理
                        cell.setCellValue(Double.parseDouble(textValue));
                    } else {
                        richString = new XSSFRichTextString(textValue);
                        cell.setCellValue(richString);
                    }
                }
            }
            index++;
        }
        try {
            workbook.write(out);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
