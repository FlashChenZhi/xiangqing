package com.util.common;



/**
 * 用于ComboBox使用的vo
 * User: xiongying
 * Date: 14-10-22
 * Time: 下午14:43
 * To change this template use File | Settings | File Templates.
 */
public class ComboBoxVo {
    private String value;
    private String displayValue;

    public ComboBoxVo() {
    }

    public ComboBoxVo(String value, String displayValue) {
        this.value = value;
        this.displayValue = displayValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }
}
