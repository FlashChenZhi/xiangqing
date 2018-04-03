package com.util.excel;

import com.util.common.MessageReader;
import com.util.common.StringUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

/**
 * excel导入参数bean
 *
 * @author xiongying
 * @ClassName: ExcelImportParam
 * @Description:
 * @date 2014年5月9日 下午6:55:44
 */
public class ExcelImportParam {
    public static final int VALIDATE_TYPE_DEFAULT = 1;// 不验证
    public static final int VALIDATE_TYPE_METHOD = 2;// 方法级别的验证
    public static final int VALIDATE_TYPE_REGEX = 3;// 正则验证

    public static final int PROPERTY_VALUE_TYPE_DEFAULT = 1; // 默认,直接赋值
    public static final int PROPERTY_VALUE_TYPE_METHOD = 2; // 调用entity方法,方法名为propertyValueKey值,返回的类型为entity需要设定属性的类型

    private int cellIndex; // excel单元格下标
    private String propertyName; // 属性名
    private boolean nullable; // 是否允许空
    private String nullValueInterKey; // 空值错误信息国际化key
    private String validateKey; // 用于值验证key(正则 or 方法验证)
    private String validateFailInterKey;// 验证失败的国际化key
    private int validateType = VALIDATE_TYPE_DEFAULT; // 验证类型
    private int propertyValueType = PROPERTY_VALUE_TYPE_DEFAULT;// 属性值类型
    private String propertyValueKey;// 用于值类型是方法的方法名
    private Object nullValue; // 值为空,使用该属性赋值

    public ExcelImportParam() {
        super();
    }

    public ExcelImportParam(int cellIndex, String propertyName, boolean nullable) {
        this.cellIndex = cellIndex;
        this.propertyName = propertyName;
        this.nullable = nullable;
    }

    public ExcelImportParam(int cellIndex, String propertyName, boolean nullable, String nullValueInterKey, String validateKey, String validateFailInterKey, int validateType) {
        super();
        this.cellIndex = cellIndex;
        this.propertyName = propertyName;
        this.nullable = nullable;
        this.nullValueInterKey = nullValueInterKey;
        this.validateKey = validateKey;
        this.validateFailInterKey = validateFailInterKey;
        this.validateType = validateType;
    }

    public ExcelImportParam(int cellIndex, String propertyName, boolean nullable, String nullValueInterKey, String validateKey, String validateFailInterKey, int validateType, int propertyValueType,
                            String propertyValueKey) {
        super();
        this.cellIndex = cellIndex;
        this.propertyName = propertyName;
        this.nullable = nullable;
        this.nullValueInterKey = nullValueInterKey;
        this.validateKey = validateKey;
        this.validateFailInterKey = validateFailInterKey;
        this.validateType = validateType;
        this.propertyValueType = propertyValueType;
        this.propertyValueKey = propertyValueKey;
    }

    public ExcelImportParam(int cellIndex, String propertyName, boolean nullable, String nullValueInterKey, String validateKey, String validateFailInterKey, int validateType, int propertyValueType,
                            String propertyValueKey, String nullValue) {
        super();
        this.cellIndex = cellIndex;
        this.propertyName = propertyName;
        this.nullable = nullable;
        this.nullValueInterKey = nullValueInterKey;
        this.validateKey = validateKey;
        this.validateFailInterKey = validateFailInterKey;
        this.validateType = validateType;
        this.propertyValueType = propertyValueType;
        this.propertyValueKey = propertyValueKey;
        this.nullValue = nullValue;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getNullValueInterKey() {
        return nullValueInterKey;
    }

    public void setNullValueInterKey(String nullNullInterKey) {
        this.nullValueInterKey = nullNullInterKey;
    }

    public String getValidateKey() {
        return validateKey;
    }

    public void setValidateKey(String validateKey) {
        this.validateKey = validateKey;
    }

    public String getValidateFailInterKey() {
        return validateFailInterKey;
    }

    public void setValidateFailInterKey(String validateFailInterKey) {
        this.validateFailInterKey = validateFailInterKey;
    }

    public int getValidateType() {
        return validateType;
    }

    public void setValidateType(int validateType) {
        this.validateType = validateType;
    }

    public int getPropertyValueType() {
        return propertyValueType;
    }

    public void setPropertyValueType(int propertyValueType) {
        this.propertyValueType = propertyValueType;
    }

    public String getPropertyValueKey() {
        return propertyValueKey;
    }

    public void setPropertyValueKey(String propertyValueKey) {
        this.propertyValueKey = propertyValueKey;
    }

    public Object getNullValue() {
        return nullValue;
    }

    public void setNullValue(Object nullValue) {
        this.nullValue = nullValue;
    }

    public void setValue(Object entity, Row row) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, ExcelException {
        Class<?> propertyType = PropertyUtils.getPropertyType(entity, this.getPropertyName());

        Cell cell = row.getCell(this.getCellIndex());
        if (cell == null)
            throw new ExcelException(String.format("EXCEL列为空, 行号[%s], 列[%s]", row.getRowNum(), this.getCellIndex() + 1));
        // 单元格值
        Object cellValue = getCellValue(cell, propertyType);
        // 当前值为空且不允许值为空抛出异常
        if (cellValue == null && !this.isNullable())
            throw new ExcelException(MessageReader.getMessage(this.getNullValueInterKey(), new String[]{String.valueOf(row.getRowNum() + 1)}));
        // 验证值格式
        doValidate(entity, cellValue, row.getRowNum() + 1);

        cellValue = getValue(entity, cellValue);
        // 设置值
        PropertyUtils.setProperty(entity, this.getPropertyName(), cellValue);
    }

    /**
     * 获取单元格值
     *
     * @param cell
     * @param propertyType
     * @return
     */
    private Object getCellValue(Cell cell, Class<?> propertyType) throws ExcelException {
        Object cellValue = getCellValue(cell);
        if (propertyType.isAssignableFrom(Boolean.class)) {
            cellValue = cellValue == null ? null : Boolean.valueOf(cellValue.toString());
        } else if (propertyType.isAssignableFrom(Short.class)) {
            cellValue = cellValue == null ? null : Short.valueOf(cellValue.toString());
        } else if (propertyType.isAssignableFrom(Integer.class)) {
            cellValue = cellValue == null ? null : Integer.valueOf(cellValue.toString());
        } else if (propertyType.isAssignableFrom(Long.class)) {
            cellValue = cellValue == null ? null : Long.valueOf(cellValue.toString());
        } else if (propertyType.isAssignableFrom(Float.class)) {
            cellValue = cellValue == null ? null : Float.valueOf(cellValue.toString());
        } else if (propertyType.isAssignableFrom(Double.class)) {
            cellValue = cellValue == null ? null : Double.valueOf(cellValue.toString());
        } else if (propertyType.isAssignableFrom(Byte.class)) {
            cellValue = cellValue == null ? null : Byte.valueOf(cellValue.toString());
        } else if (propertyType.isAssignableFrom(Character.class)) {
            cellValue = cellValue == null ? null : Character.valueOf((Character) cellValue);
        } else if (propertyType.isAssignableFrom(String.class)) {
            cellValue = cellValue == null ? null : String.valueOf(cellValue.toString());
        } else if (propertyType.isAssignableFrom(BigDecimal.class)) {
            cellValue = cellValue == null ? null : new BigDecimal(cellValue.toString());
        } else if (this.propertyValueType == PROPERTY_VALUE_TYPE_METHOD && this.propertyValueKey != null) {
            return cellValue;
        } else {
            throw new ExcelException("excel导入失败,未知类型:" + propertyType.getClass());
        }
        return cellValue;
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                return null;
            default:
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                return StringUtils.ToDBC(StringUtils.doTrim(cell.getStringCellValue()));
        }
    }

    private Object getValue(Object entity, Object cellValue) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Object value = null;
        switch (this.getPropertyValueType()) {
            case PROPERTY_VALUE_TYPE_DEFAULT:
                value = cellValue;
                break;
            case PROPERTY_VALUE_TYPE_METHOD:
                value = cellValue == null ? null : MethodUtils.invokeMethod(entity, this.getPropertyValueKey(), cellValue);
                break;
            default:
                break;
        }
        return value == null ? this.getNullValue() : value;
    }

    private void doValidate(Object entity, Object cellValue, int rowNum) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ExcelException {
        switch (this.getValidateType()) {
            case ExcelImportParam.VALIDATE_TYPE_DEFAULT:
                break;
            case ExcelImportParam.VALIDATE_TYPE_METHOD:
                Object reuslt = MethodUtils.invokeMethod(entity, this.getValidateKey(), cellValue);
                if (reuslt != null && reuslt instanceof String)
                    throw new ExcelException(MessageReader.getMessage(reuslt.toString(), new String[]{String.valueOf(rowNum)}));
                break;
            case ExcelImportParam.VALIDATE_TYPE_REGEX:
                if (cellValue != null && !cellValue.toString().matches(this.getValidateKey()))
                    throw new ExcelException(MessageReader.getMessage(this.getValidateFailInterKey(), new String[]{String.valueOf(rowNum)}));
                break;
            default:
                break;
        }
    }
}
