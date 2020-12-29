package erp.util;

import erp.entity.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelUtils {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^-?\\d+(\\.\\d+)?$");
    private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static void setResponseHeaderWithExcel(HttpServletResponse response, String fileName,long fileLength) throws UnsupportedEncodingException {
        response.setContentType(CONTENT_TYPE);
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        response.addHeader("Content-Length", String.valueOf(fileLength));
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
     */
    public static <T> void exportExcel2007(String title, String[] headers, Collection<T> dataset, OutputStream out) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        //声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        //生成一个表格
        XSSFSheet sheet = workbook.createSheet(title);
        //设置表格默认列宽度
        sheet.setDefaultColumnWidth(20);
        //设置样式
        //  生成一个样式
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        //  生成一个字体
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        font.setColor(Font.COLOR_NORMAL);
        font.setFontHeightInPoints((short) 11);
        //  把字体应用到当前的样式
        titleStyle.setFont(font);
        //  生成并设置另一个样式
        XSSFCellStyle contentStyle = workbook.createCellStyle();
        //  生成另一个字体
        XSSFFont font2 = workbook.createFont();
        //  把字体应用到当前的样式
        contentStyle.setFont(font2);
        //  日期样式
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));

        //产生表格标题行
        XSSFRow row = sheet.createRow(0);
        XSSFCell cellHeader;
        for (int i = 0; i < headers.length; i++) {
            cellHeader = row.createCell(i);
            cellHeader.setCellStyle(titleStyle);
            cellHeader.setCellValue(new XSSFRichTextString(headers[i]));
        }

        //遍历集合数据，产生数据行
        Iterator<T> iterator = dataset.iterator();
        int rowNum = 1;
        T item;
        Field[] fields;
        Field field;
        XSSFRichTextString richString;

        Matcher matcher;
        String fieldName;
        String getMethodName;
        XSSFCell cell;
        Method getMethod;
        Object value;
        String textValue;
        while (iterator.hasNext()) {
            row = sheet.createRow(rowNum);
            item = iterator.next();
            // 利用反射，根据实体类属性的先后顺序，动态调用getXxx()方法得到属性值
            fields = item.getClass().getDeclaredFields();
            for (int i = 0; i < headers.length; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(contentStyle);
                field = fields[i + 1];
                fieldName = field.getName();
                getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                getMethod = item.getClass().getMethod(getMethodName);
                value = getMethod.invoke(item);
                // 判断值的类型后进行强制类型转换
                textValue = null;
                if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                } else if (value instanceof Float) {
                    cell.setCellValue((Float) value);
                } else if (value instanceof Double) {
                    cell.setCellValue((Double) value);
                } else if (value instanceof Long) {
                    cell.setCellValue((Long) value);
                } else if (value instanceof Boolean) {
                    textValue = "是";
                    if (!(Boolean) value) {
                        textValue = "否";
                    }
                } else if (value instanceof BigDecimal) {
                    cell.setCellValue(Double.parseDouble(value.toString()));
                } else if (value instanceof Date) {
                    cell.setCellValue((Date) value);
                    cell.setCellStyle(dateStyle);
                } else {
                    // 其它数据类型都当作字符串简单处理
                    if (value != null) {
                        textValue = value.toString();
                    }
                }
                if (textValue != null) {
                    matcher = NUMBER_PATTERN.matcher(textValue);
                    if (matcher.matches()) {
                        // 是数字当作double处理
                        cell.setCellValue(Double.parseDouble(textValue));
                    } else {
                        // 不是数字当富文本处理
                        richString = new XSSFRichTextString(textValue);
                        cell.setCellValue(richString);
                    }
                }
            }
            rowNum++;
        }

        workbook.write(out);
        workbook.close();
    }

    public static List<Detail> importing(InputStream in, Date minDate, Map<String, Map<String, Integer>> allOptionsMap) throws IOException {
        final Map<String, Integer> accountOptions = allOptionsMap.get("accountOptions");
        final Map<String, Integer> departmentOptions = allOptionsMap.get("departmentOptions");
        final Map<String, Integer> projectOptions = allOptionsMap.get("projectOptions");
        final Map<String, Integer> categoryOptions = allOptionsMap.get("categoryOptions");

        List<Detail> list = new ArrayList<>();
        //得到工作薄
        XSSFWorkbook workbook = new XSSFWorkbook(in);
        //得到表格
        XSSFSheet sheet = workbook.getSheetAt(0);
        //定义可重用变量
        Cell cell;
        Detail entity;
        Date dateCellValue;
        Integer id;
        String digest;
        //对表格中的每一行进行迭代
        for (Row row : sheet) {
            //忽略前两行
            if (row.getRowNum() < 2) {
                continue;
            }
            //如果第一列类型为空,则取消循环
            if (row.getCell(0).getCellType() == CellType.BLANK) {
                break;
            }
            //创建实体类并赋值
            entity = new Detail();
            //日期
            cell = row.getCell(0);
            judgeCellType(cell, CellType.NUMERIC);
            dateCellValue = cell.getDateCellValue();
            entity.setDate(dateCellValue);
            //项目选项
            cell = row.getCell(1);
            judgeCellType(cell, CellType.STRING);
            id = projectOptions.get(cell.getStringCellValue().trim());
            if (id != null) {
                entity.setProject(new Project(id));
            } else {
                throw new MyException(MessageFormat.format("{0} 行 {1} 列: 此项目名不存在", String.valueOf(cell.getRowIndex() + 1), String.valueOf((char) (cell.getColumnIndex() + 'A'))));
            }
            //银行账户选项
            cell = row.getCell(2);
            judgeCellType(cell, CellType.STRING);
            id = accountOptions.get(cell.getStringCellValue().trim());
            if (id != null) {
                entity.setAccount(new Account(id));
            } else {
                throw new MyException(MessageFormat.format("{0} 行 {1} 列: 此银行账户名不存在", String.valueOf(cell.getRowIndex() + 1), String.valueOf((char) (cell.getColumnIndex() + 'A'))));
            }
            //部门选项
            cell = row.getCell(3);
            judgeCellType(cell, CellType.STRING);
            id = departmentOptions.get(cell.getStringCellValue().trim());
            if (id != null) {
                entity.setDepartment(new Department(id));
            } else {
                throw new MyException(MessageFormat.format("{0} 行 {1} 列: 此部门名不存在", String.valueOf(cell.getRowIndex() + 1), String.valueOf((char) (cell.getColumnIndex() + 'A'))));
            }
            //类别选项
            cell = row.getCell(4);
            judgeCellType(cell, CellType.STRING);
            id = categoryOptions.get(cell.getStringCellValue().trim());
            if (id != null) {
                entity.setCategory(new Category(id));
            } else {
                throw new MyException(MessageFormat.format("{0} 行 {1} 列: 此类别名不存在", String.valueOf(cell.getRowIndex() + 1), String.valueOf((char) (cell.getColumnIndex() + 'A'))));
            }
            //收入
            cell = row.getCell(5);
            judgeCellType(cell, CellType.NUMERIC);
            final String earning = String.valueOf(cell.getNumericCellValue());
            judgeDecimalPlace(cell, earning);
            entity.setEarning(new BigDecimal(earning));
            //支出
            cell = row.getCell(6);
            judgeCellType(cell, CellType.NUMERIC);
            final String expense = String.valueOf(cell.getNumericCellValue());
            judgeDecimalPlace(cell, expense);
            entity.setExpense(new BigDecimal(expense));
            //摘要
            cell = row.getCell(7);
            judgeCellType(cell, CellType.STRING);
            digest = cell.getStringCellValue().trim();
            if (digest.isEmpty()) {
                digest = "无";
            }
            entity.setDigest(digest);

            //添加到list
            list.add(entity);

            //比较并设置最早的时间
            if (minDate.getTime() > dateCellValue.getTime()) {
                minDate.setTime(dateCellValue.getTime());
            }
        }
        return list;
    }

    /**
     * 判断数据类型是否匹配, 不匹配则直接抛异常
     *
     * @param cell   判断的对象
     * @param expect 期望的数据类型
     */
    private static void judgeCellType(Cell cell, CellType expect) {
        if (cell.getCellType() != expect) {
            throw new MyException(MessageFormat.format("{0} 行 {1} 列: 数据类型不正确", String.valueOf(cell.getRowIndex() + 1), String.valueOf((char) (cell.getColumnIndex() + 'A'))));
        }
    }

    /**
     * 判断小数类型的小数位长度, 大于2位则抛异常
     *
     * @param cell    当前cell
     * @param decimal 要判断小数的字符串
     */
    private static void judgeDecimalPlace(Cell cell, String decimal) {
        final int index = decimal.indexOf(".");
        if (index >= 0 && decimal.length() - index - 1 > 2) {
            throw new MyException(MessageFormat.format("{0} 行 {1} 列: 小数位超过2位", String.valueOf(cell.getRowIndex() + 1), String.valueOf((char) (cell.getColumnIndex() + 'A'))));
        }
    }

    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆",
            "伍", "陆", "柒", "捌", "玖"};
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONETARY_UNIT = {"分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾", "佰", "仟"};
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "整";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;
    /**
     * 特殊字符：零元整
     */
    private static final String CN_ZEOR_FULL = "零元" + CN_FULL;

    /**
     * 把输入的金额转换为汉语中人民币的大写
     *
     * @param numberOfMoney 输入的金额
     * @return 对应的汉语大写
     */
    public static String number2CNMonetaryUnit(BigDecimal numberOfMoney) {
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        // 这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETARY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETARY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETARY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero) && numIndex != 2) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETARY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETARY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }
}
