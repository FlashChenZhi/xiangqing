package com.util.excel;


import com.util.common.BeanUtils;
import com.util.common.MessageReader;

/**
 * excel导出参数
 * 
 * @ClassName: ExcelExportParam
 * @Description:
 * @author xiongying
 * @date 2014年5月8日 下午6:49:52
 * 
 */
public class ExcelExportParam {
	// 不做处理
	private static final int INTER_TYPE_EMPTY = 1;
	// 使用国际化
	private static final int INTER_TYPE_RES_FILE = 2;
	// bean反射
	private static final int RPOERTY_NAME_TYPE_BEAN_REFLECT = 3;

	private int titleNameType = INTER_TYPE_RES_FILE;
	private String titleName;

	private int propertyNameType = RPOERTY_NAME_TYPE_BEAN_REFLECT;
	private String propertyName;
	private String nullValue = "";

	public ExcelExportParam(String titleName, String propertyName) {
		super();
		this.titleName = titleName;
		this.propertyName = propertyName;
	}

	public ExcelExportParam(int titleNameType, String titleName, String propertyName) {
		super();
		this.titleNameType = titleNameType;
		this.titleName = titleName;
		this.propertyName = propertyName;
	}

	public ExcelExportParam(int titleNameType, String titleName, int propertyNameType, String propertyName) {
		super();
		this.titleNameType = titleNameType;
		this.titleName = titleName;
		this.propertyNameType = propertyNameType;
		this.propertyName = propertyName;
	}

	public ExcelExportParam(int titleNameType, String titleName, int propertyNameType, String propertyName, String nullValue) {
		super();
		this.titleNameType = titleNameType;
		this.titleName = titleName;
		this.propertyNameType = propertyNameType;
		this.propertyName = propertyName;
		this.nullValue = nullValue;
	}

	public int getTitleNameType() {
		return titleNameType;
	}

	public void setTitleNameType(int titleNameType) {
		this.titleNameType = titleNameType;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public int getPropertyNameType() {
		return propertyNameType;
	}

	public void setPropertyNameType(int propertyNameType) {
		this.propertyNameType = propertyNameType;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getNullValue() {
		return nullValue;
	}

	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	public String getTitle() {
		String title = null;
		switch (titleNameType) {
		case INTER_TYPE_EMPTY:
			title = this.titleName;
			break;
		case INTER_TYPE_RES_FILE:
			title = MessageReader.getMessage(this.titleName);
			break;
		default:
			break;
		}
		return title == null ? nullValue : title;

	}

	public String getValue(Object pojo) {
		Object title = null;
		switch (propertyNameType) {
		case RPOERTY_NAME_TYPE_BEAN_REFLECT:
			title = BeanUtils.invokGetMethodChain(pojo, propertyName, ".", new Object[0], null) == null ? null :  BeanUtils.invokGetMethodChain(pojo,
					propertyName, ".", new Object[0], null);
			break;
		default:
			break;
		}
		return title == null ? nullValue : title.toString();
	}
}
