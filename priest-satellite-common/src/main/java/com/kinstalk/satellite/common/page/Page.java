
package com.kinstalk.satellite.common.page;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mayan on 14-11-5.
 */
public class Page<T> implements Serializable {
	//初始化size
	private final static int INIT_SIZE = 20;
    private static final long serialVersionUID = 5784595885066034295L;
    private int pageSize = INIT_SIZE;
	private int totalCount;
	private int currentPage;
	private List<T> data;

	public Page() {
		// 默认构造器
	}

	public Page(int currentPage) {
		this.currentPage = currentPage;
	}

	public Page(int currentPage, int pageSize) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}

	/**
	 * 获取开始索引
	 * @return
	 */
	public int getStartIndex() {
		return (getCurrentPage() - 1) * this.pageSize;
	}

	/**
	 * 获取结束索引
	 * @return
	 */
	public int getEndIndex() {
		return getCurrentPage() * this.pageSize;
	}

	/**
	 * 是否第一页
	 * @return
	 */
	public boolean isFirstPage() {
		return getCurrentPage() <= 1;
	}

	/**
	 * 是否末页
	 * @return
	 */
	public boolean isLastPage() {
		return getCurrentPage() >= getPageCount();
	}

	/**
	 * 获取下一页页码
	 * @return
	 */
	public int getNextPage() {
		if (isLastPage()) {
			return getCurrentPage();
		} 
		return getCurrentPage() + 1;
	}

	/**
	 * 获取上一页页码
	 * @return
	 */
	public int getPreviousPage() {
		if (isFirstPage()) {
			return 1;
		}
		return getCurrentPage() - 1;
	}

	/**
	 * 获取当前页页码
	 * @return
	 */
	public int getCurrentPage() {
		if (currentPage == 0) {
			currentPage = 1;
		}
		return currentPage;
	}

	/**
	 * 取得总页数
	 * @return
	 */
	public int getPageCount() {
		if (totalCount % pageSize == 0) {
			return totalCount / pageSize;
		} else {
			return totalCount / pageSize + 1;
		}
	}

	/**
	 * 取总记录数.
	 * @return
	 */
	public int getTotalCount() {
		return this.totalCount;
	}

	/**
	 * 设置当前页
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * 获取每页数据容量.
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * 该页是否有下一页.
	 * @return
	 */
	public boolean hasNextPage() {
		return getCurrentPage() < getPageCount();
	}

	/**
	 * 该页是否有上一页.
	 * @return
	 */
	public boolean hasPreviousPage() {
		return getCurrentPage() > 1;
	}

	/**
	 * 获取数据集
	 * @return
	 */
	public List<T> getResult() {
		return data;
	}

	/**
	 * 设置数据集
	 * @param data
	 */
	public void setResult(List<T> data) {
		this.data = data;
	}

	/**
	 * 设置总记录条数
	 * @param totalCount
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	//==============扩展字段===============//
	private String unit = "条";//单位
	private String extInfo;//扩展信息
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUnit() {
		return unit;
	}
	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}
	public String getExtInfo() {
		return extInfo;
	}
}
