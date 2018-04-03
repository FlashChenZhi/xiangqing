package com.util.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * excel 导入工具类
 *
 * @author xiongying
 * @ClassName: ExcelImportUtil
 * @Description:
 * @date 2014年5月9日 下午6:55:14
 */
public class ExcelImportUtils {

    public static <T> List<T> doExcelImport(Class<T> clazz, List<ExcelImportParam> params, MultipartFile file, int sheetIdex) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, ExcelException {
        return ExcelImportUtils.doExcelImport(clazz, params, ExcelImportUtils.getWorkbook(file), sheetIdex);
    }

    public static <T> List<T> doExcelImport(Class<T> clazz, List<ExcelImportParam> params, File file, int sheetIdex) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, ExcelException {
        return ExcelImportUtils.doExcelImport(clazz, params, ExcelImportUtils.getWorkbook(file), sheetIdex);
    }

    private static Workbook getWorkbook(MultipartFile file) throws ExcelException {
//        String fileName = file.getOriginalFilename();
//        String[] fileNameArray = fileName.split("\\.");
//        if (fileNameArray.length > 1) {
//            try {
//                String fix = fileNameArray[fileNameArray.length - 1].trim().toLowerCase();
//                if ("xls".equals(fix.trim().toLowerCase())) {
//                    return new HSSFWorkbook(file.getInputStream());
//                }/*
//                 * else if("xlsx".equals(fix.trim().toLowerCase())){ return new
//				 * XSSFWorkbook(new FileInputStream(file)); }
//				 */
//            } catch (Exception e) {
//                throw new ExcelException("excel导入失败", e);
//            }
//        }
//        throw new ExcelException("excel导入失败,请选择后缀为.xls的文件");
        return null;
    }

    private static Workbook getWorkbook(File file) throws ExcelException {
        String fileName = file.getName();
        String[] fileNameArray = fileName.split("\\.");
        if (fileNameArray.length > 1) {
            try {
                String fix = fileNameArray[fileNameArray.length - 1].trim().toLowerCase();
                if ("xls".equals(fix.trim().toLowerCase())) {
                    return null;
                }/*
				 * else if("xlsx".equals(fix.trim().toLowerCase())){ return new
				 * XSSFWorkbook(new FileInputStream(file)); }
				 */
            } catch (Exception e) {
                throw new ExcelException("excel导入失败", e);
            }
        }
        throw new ExcelException("excel导入失败,请选择后缀为.xls的文件");
    }

    /**
     * 导入excel
     *
     * @param clazz
     * @param params
     * @param data
     * @param sheetIdex
     * @return
     */
    public static <T> List<T> doExcelImport(Class<T> clazz, List<ExcelImportParam> params, Workbook data, int sheetIdex) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, ExcelException {
        Sheet sheet = data.getSheetAt(sheetIdex);
        Iterator<Row> i = sheet.rowIterator();
        List<T> list = new ArrayList<T>();

        if (i.hasNext()) {
            // 首行为标题，跳过
            i.next();
        }
        while (i.hasNext()) {
            Row row = i.next();
            T entity;
            try {
                entity = clazz.newInstance();
            } catch (Exception e) {
                throw new ExcelException("excel导入失败", e);
            }
            for (ExcelImportParam param : params) {
                param.setValue(entity, row);
            }
            list.add(entity);
        }
        return list;
    }

}
