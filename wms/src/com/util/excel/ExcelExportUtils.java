package com.util.excel;

import com.util.common.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * excel导出 工具类
 *
 * @author xiongying
 * @ClassName: ExcelUtil
 * @Description:
 * @date 2014年5月8日 下午7:02:47
 */
public class ExcelExportUtils {

    /**
     * 默认日期格式
     */
    public static final String PATTEERN_DATE = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    public static final String PATTEERN_TIME = "yyyy-MM-dd mm:ss";
    /**
     * 默认金额格式
     */
    public static String CUERRENCE_FORMATE = "###,##0.00";

    protected static final ThreadLocal<CellStyle> TITLE_STYLE = new ThreadLocal<CellStyle>();
    protected static final ThreadLocal<CellStyle> CELL_STYLE = new ThreadLocal<CellStyle>();
    protected static final ThreadLocal<CellStyle> DATE_CELL_STYLE = new ThreadLocal<CellStyle>();

    /**
     * 生成带样式的单元格
     *
     * @param workbook
     * @param row      第几行
     * @param index    cell列
     * @param value    值
     * @param style    cell样式
     * @return
     */
    public static Cell generateCell(Workbook workbook, Row row, Integer index, Object value, CellStyle style) {
        Cell cell = row.createCell(index);
        ExcelExportUtils.setCellValue(workbook, cell, value, style);
        return cell;
    }

    /**
     * 获取cell style
     *
     * @param workbook
     * @return
     */
    public static CellStyle getCellStyle(Workbook workbook) {
        CellStyle cellStyle = ExcelExportUtils.CELL_STYLE.get();
        if (cellStyle == null) {
            cellStyle = ExcelExportUtils.generateCellStyle(workbook);
            ExcelExportUtils.CELL_STYLE.set(cellStyle);
        }
        return cellStyle;
    }

    /**
     * 获取title cell style
     *
     * @param workbook
     * @return
     */
    public static CellStyle getTitleCellStyle(Workbook workbook) {
        CellStyle cellStyle = ExcelExportUtils.TITLE_STYLE.get();
        if (cellStyle == null) {
            cellStyle = ExcelExportUtils.generateTitleCellStyle(workbook);
            ExcelExportUtils.TITLE_STYLE.set(cellStyle);
        }
        return cellStyle;
    }

    /**
     * 获取日期类型 cell style
     *
     * @param workbook
     * @return
     */
    public static CellStyle getDateCellStyle(Workbook workbook) {
        CellStyle cellStyle = ExcelExportUtils.DATE_CELL_STYLE.get();
        if (cellStyle == null) {
            cellStyle = ExcelExportUtils.generateDateCellStyle(workbook);
            ExcelExportUtils.DATE_CELL_STYLE.set(cellStyle);
        }
        return cellStyle;
    }

    /**
     * 生成默认的cell style
     *
     * @param workbook
     * @return
     */
    private static CellStyle generateCellStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("微软雅黑");
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font);
        return style;
    }

    /**
     * 生成默认的title cell style
     *
     * @param workbook
     * @return
     */
    private static CellStyle generateTitleCellStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.DARK_BLUE.index);
        font.setFontHeight((short) 300);
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font);
        return style;
    }

    /**
     * 生成默认的日期类型 cell style
     *
     * @param workbook
     * @return
     */
    private static CellStyle generateDateCellStyle(Workbook workbook) {
        return ExcelExportUtils.generateDateCellStyle(workbook, ExcelExportUtils.PATTEERN_DATE);
    }

    /**
     * 生成日期类型 cell style
     *
     * @param workbook
     * @param patteern
     * @return
     */
    public static CellStyle generateDateCellStyle(Workbook workbook, String patteern) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat(patteern));
        return style;
    }

    /**
     * 根据value的数据类型调用Cell不同的setCellValue方法
     *
     * @param workbook Workbook
     * @param cell     Cell
     * @param value    值
     */
    private static void setCellValue(Workbook workbook, Cell cell, Object value, CellStyle style) {
        cell.setCellStyle(ExcelExportUtils.getCellStyle(workbook));
        if (style != null) {
            cell.setCellStyle(style);
        }
        if (value instanceof Boolean) {
            cell.setCellValue(((Boolean) value).booleanValue());
        } else if (value instanceof Double) {
            cell.setCellValue(((Double) value).doubleValue());
        } else if (value instanceof Float) {
            cell.setCellValue(((Float) value).doubleValue());
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else if (value instanceof Integer) {
            cell.setCellValue(((Integer) value).intValue());
        } else if (value instanceof Long) {
            cell.setCellValue(((Long) value).longValue());
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            if (style == null) {
                cell.setCellStyle(ExcelExportUtils.getDateCellStyle(workbook));
            }
        } else if (value instanceof Calendar) {
            cell.setCellValue((Calendar) value);
        } else {
            cell.setCellValue(value == null ? "" : value.toString());
        }
    }

    /**
     * 生成excel文件
     *
     * @param params
     * @param datas
     * @return
     */
    public static HSSFWorkbook generateExcelFile(List<ExcelExportParam> params, List datas) {
//        return (HSSFWorkbook) generateExcelFile(new HSSFWorkbook(), params, datas);
        return null;
    }

    /**
     * 生成excel文件
     *
     * @param workbook
     * @param params
     * @param datas
     * @return
     */
    public static Workbook generateExcelFile(Workbook workbook, List<ExcelExportParam> params, List datas) {
        Map<Integer, ExcelExportParam> propRefs = new TreeMap<Integer, ExcelExportParam>();
        Sheet sheet = workbook.createSheet();
//        if (sheet instanceof SXSSFSheet) {
//            SXSSFSheet sxSheet = (SXSSFSheet) sheet;
//            sxSheet.trackAllColumnsForAutoSizing();
//        }
        Row row = sheet.createRow(0);
        row.setHeight((short) 500);// 设定行的高度
        Iterator<ExcelExportParam> iter = params.iterator();
        for (int i = 0; iter.hasNext(); i++) {
            ExcelExportParam param = iter.next();
            ExcelExportUtils.generateCell(workbook, row, i, param.getTitle(), ExcelExportUtils.getTitleCellStyle(workbook));
            propRefs.put(new Integer(i), param);
            // 设置单元格根据title自适应宽度
            sheet.autoSizeColumn(i, true);
        }

        Iterator dataIter = datas.iterator();
        for (int i = 0; dataIter.hasNext(); i++) {
            Object pojo = dataIter.next();
            row = sheet.createRow(i + 1);
            row.setHeight((short) 500);// 设定行的高度
            Iterator<Integer> ki = propRefs.keySet().iterator();
            while (ki.hasNext()) {
                Integer key = (Integer) ki.next();
                ExcelExportParam exportParam = propRefs.get(key);
                ExcelExportUtils.generateCell(workbook, row, key, exportParam.getValue(pojo), null);
            }
        }
        ExcelExportUtils.resetCellStyle();

        return workbook;

    }

    public static void resetCellStyle() {
        ExcelExportUtils.TITLE_STYLE.remove();
        ExcelExportUtils.CELL_STYLE.remove();
        ExcelExportUtils.DATE_CELL_STYLE.remove();
    }

    /**
     * 导出excel到客户端
     *
     * @param wb
     * @param fileName
     * @param response
     * @throws IOException
     */
    public static void exportExcelToClient(Workbook wb, String fileName, HttpServletResponse response) throws IOException {
        response.setContentType("application/msexcel;charset=utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + StringUtils.toUtf8String(fileName) + "\"");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        wb.write(out);
        out.flush();
        out.close();
    }

}
