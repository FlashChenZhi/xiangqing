package com.util.pages;



/**
 * 分页类
 * User: xiongying
 * Date: 14-10-17
 * Time: 下午18:03
 * To change this template use File | Settings | File Templates.
 */
public class Pages {
	public static final int DEFAULT_PAGE_SIZE = 20;

	private int pageSize = DEFAULT_PAGE_SIZE;

	private long totalRow = 0;

	private int currentPage = 1;

	public Pages() {

	}

	public Pages(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		// if (currentPage == 0)
		// {
		// setCurrentNumber(0);
		// return;
		// }
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(long totalRow) {
		this.totalRow = totalRow;
	}

	public int getTotalPage() {
		int totalPage = (int) (((getTotalRow() - 1) / getPageSize() + 1));
		return totalPage;
	}

	public int getFirstRow() {
		return getPageSize() * (getCurrentPage() - 1);
	}

	public int getLastRow() {
		return getPageSize() * getCurrentPage();
	}
}
